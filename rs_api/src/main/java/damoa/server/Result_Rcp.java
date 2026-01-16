package damoa.server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.*;

import damoa.Constants;
import damoa.comm.Sock;
import damoa.comm.bean.Xc3Deliver;
import damoa.comm.data.ServerInfo;
import damoa.comm.db.DBExec;
import damoa.comm.log.GeneLog;
import damoa.comm.util.ComCheck;
import damoa.comm.util.DateUtil;
import damoa.comm.util.FileCheck;
import damoa.server.entity.AegisRMsg;
import damoa.server.entity.Mybean;
import damoa.server.entity.rcpEntity;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Result_Rcp {

    private Xc3Deliver xc3Ssock;
    private Socket sockClient;
    private BufferedReader dis;
    private DataOutputStream dos;

    private int hCtx;
    private String custId;
    private String procDate;
    private String procSeq;
    private String dataKind;

    private int intRecCnt = 0;

    private GeneLog log;
    private boolean testFlag;

    private Connection conn = null;
    
    private Logger LOG = LoggerFactory.getLogger(getClass());

    public Result_Rcp(Xc3Deliver xc3Ssocket, Socket sockCsocket, BufferedReader dis,
                      DataOutputStream dos, int hCtx, String custId, String procDate, String procSeq,
                      String dataKind, int msgSize, boolean testFlag, Connection conn, String packetNo, String workKind) {

        this.xc3Ssock = xc3Ssocket;
        this.sockClient = sockCsocket;
        this.dis = dis;
        this.dos = dos;
        this.hCtx = hCtx;
        this.custId = custId;
        this.procDate = procDate;
        this.procSeq = procSeq;
        this.dataKind = dataKind;
        this.log = new GeneLog(packetNo, this.dataKind, workKind, this.testFlag, LOG);
        this.testFlag = testFlag;
        this.conn = conn;
    }

    public void Process() throws Exception {

        try {

            this.log.debugln("파일쓰기시작");
            //Map<String, String> map = WriteData();
            List<Map<String, String>> rcpMasKeyList = WriteData();
            this.log.debugln("파일쓰기종료");

            this.log.debugln("전송시작");
            SendData(rcpMasKeyList);
            this.log.debugln("전송종료");

            this.conn.commit();

        } catch (Exception e) {
            throw e;
        }
    }

    public void SendData(List<Map<String, String>> rcpMasKeyList) throws Exception {
        DBExec dbe = new DBExec(this.log, this.conn, this.procDate, this.procSeq);
        //파일읽기
        FileInputStream rFile = FileCheck.readDoneFile(this.custId, this.procDate, this.procSeq,
                this.dataKind, this.log, this.testFlag);

        String readHeader = FileCheck.readOneLine(rFile, this.dataKind);
        this.intRecCnt = Integer.parseInt(ComCheck.getStr2Byte2Str(readHeader, 45, 6));

        // 헤더전문송신
        this.log.debugln("H전문송신시작");
        int sndHLen = Sock.sndMessage(this.xc3Ssock, this.sockClient, this.dis, this.dos, this.hCtx, readHeader);
        if (sndHLen > 0) {
            this.log.debugln("H전문송신완료[" + sndHLen + "]:[" + readHeader + "]");
        }

        // 바디전문송신
        this.log.debugln("B전문총건수:" + this.intRecCnt);
        for (int iCnt = 0; iCnt < this.intRecCnt; iCnt++) {
            // 데이터전문
            String readData = FileCheck.readOneLine(rFile, this.dataKind);

            //
            this.log.println("READ_ONE_LINE " + readData);
            AegisRMsg aegisRMsg = new AegisRMsg();
            aegisRMsg.setMsg(readData.getBytes(Charset.forName("EUC-KR")));
            this.log.debugln(aegisRMsg.toString());
            int sndDLen = Sock.sndMessage(this.xc3Ssock, this.sockClient, this.dis, this.dos, this.hCtx, readData);

            if (sndDLen > 0) {
                this.log.debugln("B전문송신종료[" + sndDLen + "]:[" + readData + "]");
                Map<String, String> map = rcpMasKeyList.get(iCnt);
                String rcpMasCd = map.get("rcpmascd");
            	if (dbe.updateTrnSt(rcpMasCd)) {
            		// 참고 위메소드에서 예외를 던지지 않는다.
            		this.log.debugln("["+this.custId+"]rcpmasCd=" + rcpMasCd + " 업데이트 성공..");
            	} else {
            		// 청구없는 입금에 대한 처리로 vano로 수납전송후 상태값 업데이트
            		String vaNo = map.get("vano");
            		if (!dbe.updateTrnStByVano(vaNo)) {
            			this.log.debugln("["+this.custId+"]vaNo=" + vaNo + " 업데이트오류...");
            		} else {
            			this.log.debugln("["+this.custId+"]vaNo=" + vaNo + " 업데이트 성공..");
            		}
            	}
            }
        }

        // 테일전문송신
        this.log.debugln("T전문송신시작");
        String readTailer = FileCheck.readOneLine(rFile, this.dataKind);

        int sndTLen = Sock.sndMessage(this.xc3Ssock, this.sockClient, this.dis, this.dos, this.hCtx, readTailer);
        if (sndTLen > 0) {
            this.log.debugln("T전문송신완료[" + sndTLen + "]:[" + readTailer + "]");
        }

        if (rFile != null) rFile.close();

        if (this.intRecCnt == 0) {
            String strFile = "";

            if (testFlag) strFile = ServerInfo.RCP_DATA_TESTPATH + procDate + "-" + procSeq + "." + custId;
            else strFile = ServerInfo.RCP_DATA_PATH + procDate + "-" + procSeq + "." + custId;

            this.log.debugln("빈파일삭제 :" + strFile);
            File file = new File(strFile);
            if (file.isFile()) {
                file.delete();
            }
        }

        return;
    }

    public List<Map<String, String>> WriteData() throws Exception {

    	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        FileCheck.deleteDoneFile(this.custId, this.procDate, this.procSeq,
                this.dataKind, this.log, this.testFlag);

        // 첵크결과임시파일생성
        FileOutputStream wFile = FileCheck.writeDoneFile(this.custId, this.procDate, this.procSeq,
                this.dataKind, this.log, this.testFlag);

        DBExec dbe = new DBExec(this.log, this.conn, this.procDate, this.procSeq);

        String tmpDate = new DateUtil().getTime("YYYYMMDDhhmm");
        int tmpTime = Integer.parseInt(tmpDate.substring(8));

        //전체수납내역이 아니면 23시40분 ~ 00시 30분까지 수납내역 전송안함
        if (!"000000".equals(this.procSeq) && tmpTime > 2340 || tmpTime < 30) {
            System.out.println("수납내역 전송 시간이 아님...");
            this.intRecCnt = 0;
        } else {
            this.intRecCnt = dbe.getRcpDataCnt(this.custId, this.procDate, tmpDate, this.procSeq);
            System.out.println("수납내역 건수 " + intRecCnt);
        }

        String HrecordType = "H";                                                  //Record구분
        String Hservice_id = ComCheck.makeSpaceData(this.custId, 8);                //가맹점코드
        String Hfiller1 = " ";                                                  //filler1
        String Hresult = "Y";                                                  //처리결과
        String HresultCode = "H000";                                               //결과코드
        String HresultMsg = ComCheck.makeSpaceData("정상", 30);                    //결과메세지
        String HcntSuccess = ComCheck.lPad(String.valueOf(this.intRecCnt), "0", 6);  //납부결과건수
        String Hfiller2 = ComCheck.makeSpaceData("", 647);                       //filler2

        String HeaderMsg = HrecordType + Hservice_id + Hfiller1 + Hresult + HresultCode + HresultMsg +
                HcntSuccess + Hfiller2 + "\r\n";


        this.log.debugln(HeaderMsg);
        System.out.println("HeaderMsg " + HeaderMsg);

        wFile.write(HeaderMsg.getBytes(Constants.MY_CHARSET));


        Vector vData = null;

        if (!"000000".equals(this.procSeq) && tmpTime > 2340 || tmpTime < 30) {
            vData = null;
        } else {
            vData = dbe.getRcpData(this.custId, this.procDate, this.intRecCnt, tmpDate, this.procSeq);
        }

        rcpEntity entity;

        String DrecordType = "D";                              // Record구분
        String Dservice_id = Hservice_id;                      // 가맹점코드
        String Dchacd;                                        // 가맹점코드
        String DworkField = "N";                              // 처리내역
        String Dresult = "Y";                              // 처리결과
        String DresultCode = "D000";                           // 결과코드
        String DresultMsg = ComCheck.makeSpaceData("정상", 30);  // 결과메세지
        String Dmaskey;                                         // 청구거래번호
        String Dvano;                                           // 가상계좌번호
        String Dmasmonth;                                       // 청구월
        String Dmasday;                                         // 청구일자
        String Dpayday;                                         // 수납일시
        String Drcpusrname;                                     // 수납자명
        String Dbnkcd;                                          // 수납은행코드
        String Dbchcd;                                          // 수납지점코드
        String Dmdgubn;                                         // 매체구분
        String Dtrnday;                                         // 처리일시
        String Drcpamt;                                         // 총수납금액
        String Dcusgubn1;                                       // 참조1
        String Dcusgubn2;                                       // 참조2
        String Dcusgubn3;                                       // 참조3
        String Dcusgubn4;                                       // 참조4
        String Dsvecd;                                        // 수납구분
        String Dfiller1 = ComCheck.makeSpaceData("", 122);   // filler1
        String Ddetkey;                                         // 항목거래번호
        String Dadjfiregkey;                                    // 은행배분Key
        String Dpayitemname;                                    // 항목명
        String Dpayitemamt;                                     // 항목금액
        String Dfiller2 = ComCheck.makeSpaceData("", 183);   // filler2

        String Rcpmascds = "";
        String rcpmascd = "";
        for (int i = 0; i < this.intRecCnt; i++) {
            entity = (rcpEntity) vData.get(i);

            if (i == 0) {
                rcpmascd = ComCheck.null2space(entity.rcpmascd);
                Rcpmascds = "'" + ComCheck.null2space(entity.rcpmascd) + "'";
            } else {
                rcpmascd = ComCheck.null2space(entity.rcpmascd);
                Rcpmascds += ",'" + ComCheck.null2space(entity.rcpmascd) + "'";
            }

            Dchacd = ComCheck.makeSpaceData(ComCheck.null2space(entity.chacd), 8);
            Dmaskey = ComCheck.makeSpaceData(ComCheck.null2space(entity.maskey), 30);
            Dvano = ComCheck.makeSpaceData(ComCheck.null2space(entity.vano), 20);
            Dmasmonth = ComCheck.makeSpaceData(ComCheck.null2space(entity.masmonth), 6);
            Dmasday = ComCheck.makeSpaceData(ComCheck.null2space(entity.masday), 8);
            Dpayday = ComCheck.makeSpaceData(ComCheck.null2space(entity.payday), 14);
            Drcpusrname = ComCheck.makeSpaceData(ComCheck.null2space(entity.rcpusrname), 20);
            Dbnkcd = ComCheck.makeSpaceData(ComCheck.null2space(entity.bnkcd), 3);
            Dbchcd = ComCheck.makeSpaceData(ComCheck.null2space(entity.bchcd), 4);
            Dmdgubn = ComCheck.makeSpaceData(ComCheck.null2space(entity.mdgubn), 1);
            Dtrnday = ComCheck.makeSpaceData(ComCheck.null2space(entity.trnday), 14);
            Drcpamt = ComCheck.makeSpaceData(ComCheck.null2space(entity.rcpamt), 10);
            Dcusgubn1 = ComCheck.makeSpaceData(ComCheck.null2space(entity.cusgubn1), 30);
            Dcusgubn2 = ComCheck.makeSpaceData(ComCheck.null2space(entity.cusgubn2), 30);
            Dcusgubn3 = ComCheck.makeSpaceData(ComCheck.null2space(entity.cusgubn3), 30);
            Dcusgubn4 = ComCheck.makeSpaceData(ComCheck.null2space(entity.cusgubn4), 30);
            Dsvecd = ComCheck.makeSpaceData(ComCheck.null2space(entity.svecd), 3);
            Ddetkey = ComCheck.makeSpaceData(ComCheck.null2space(entity.detkey), 30);
            Dadjfiregkey = ComCheck.makeSpaceData(ComCheck.null2space(entity.adjfiregkey), 5);
            Dpayitemname = ComCheck.makeSpaceData(ComCheck.null2space(entity.payitemname), 50);
            Dpayitemamt = ComCheck.makeSpaceData(ComCheck.null2space(entity.payitemamt), 10);

            String DataMsg = DrecordType + Dchacd + DworkField + Dresult + DresultCode + DresultMsg +
                    Dmaskey + Dvano + Dmasmonth + Dmasday + Dpayday + Drcpusrname + Dbnkcd + Dbchcd + Dmdgubn +
                    Dtrnday + Drcpamt + Dcusgubn1 + Dcusgubn2 + Dcusgubn3 + Dcusgubn4 + Dsvecd + Dfiller1 +
                    Ddetkey + Dadjfiregkey + Dpayitemname + Dpayitemamt + Dfiller2 + "\r\n";

            wFile.write(DataMsg.getBytes(Constants.MY_CHARSET));
            this.log.debugln(DataMsg);

            this.log.debugln("["+Dchacd+"]rcpmascd=" + rcpmascd + "[maskey]" + Dmaskey);

            Map<String, String> map = new HashMap<>();
            map.put("rcpmascd", rcpmascd.trim());
            map.put("vano", Dvano.trim());
            list.add(map);
        }

        String teleMsg = "정상";
        String teleRslt = "T000";
        String trnDate = new DateUtil().getTime("YYYYMMDD");

        if (trnDate.equals(this.procDate) && !"000000".equals(this.procSeq) && this.intRecCnt > 0) {
            // 업데이트 오류..
//            if(!dbe.updateTrnSt(Rcpmascds)){
//                teleRslt = "T006";
//                teleMsg = "업데이트오류";
//            }
        }


        String TrecordType = "T";                                                  // Record구분
        String Tservice_id = Hservice_id;                                          // 가맹점코드
        String Tfiller1 = " ";                                                  // filler1
        String Tresult = "Y";                                                  // 처리결과
        String TresultCode = teleRslt;                                             // 결과코드
        String TresultMsg = ComCheck.makeSpaceData(teleMsg, 30);                   // 결과메세지
        String TSucCnt = ComCheck.lPad(String.valueOf(this.intRecCnt), "0", 6);  // 납부결과건수
        String Tfiller2 = ComCheck.makeSpaceData("", 647);                       // filler2

        String TeaderMsg = TrecordType + Tservice_id + Tfiller1 + Tresult + TresultCode + TresultMsg +
                TSucCnt + Tfiller2 + "\r\n";

        wFile.write(TeaderMsg.getBytes(Constants.MY_CHARSET));
        this.log.debugln(TeaderMsg);

        if (wFile != null) wFile.close();

        return list;
    }
}
