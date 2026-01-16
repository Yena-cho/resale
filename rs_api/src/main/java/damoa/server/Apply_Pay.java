package damoa.server;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;

import damoa.Constants;
import damoa.comm.SendSMS;
import damoa.comm.Sock;
import damoa.comm.bean.Xc3Deliver;
import damoa.comm.db.DBExec;
import damoa.comm.log.GeneLog;
import damoa.comm.tele.DataTele;
import damoa.comm.tele.HeadTele;
import damoa.comm.tele.TailTele;
import damoa.comm.util.ComCheck;
import damoa.comm.util.FileCheck;
import damoa.server.entity.DamoaMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Apply_Pay {

    private Xc3Deliver          xc3Ssock;
    private Socket              sockClient;
    private BufferedReader dis;
    private DataOutputStream    dos;

    private int     hCtx;
    private String  custId;
    private String  procDate;
    private String  procSeq;
    private String  dataKind;
    private DamoaMsg dMsg;

    private int     intRecCnt   = 0;
    private int     msgSize     = 0;

    private GeneLog log;
    private boolean testFlag;

    private Connection conn = null;
    private Connection transConn = null;
    private Logger LOG = LoggerFactory.getLogger(getClass());

    public Apply_Pay(Xc3Deliver xc3Ssocket, Socket sockCsocket, BufferedReader dis,
                     DataOutputStream dos, int hCtx, String custId, String procDate, String procSeq,
                     String dataKind, int msgSize, boolean testFlag, Connection conn,
                     Connection transConn,String packetNo,String workKind) {

        this.xc3Ssock   = xc3Ssocket;
        this.sockClient = sockCsocket;
        this.dis        = dis;
        this.dos        = dos;
        this.hCtx       = hCtx;
        this.custId     = custId;
        this.procDate   = procDate;
        this.procSeq    = procSeq;
        this.dataKind   = dataKind;
        this.msgSize    = msgSize;
        this.log = new GeneLog(packetNo, this.dataKind, workKind, this.testFlag,LOG);
        this.testFlag   = testFlag;
        this.conn       = conn;
        this.transConn  = transConn;
        this.dMsg		= new DamoaMsg();
    }

    public void Process() throws Exception {

        try{
            this.log.debugln("체크시작");
            CheckFormat();
            this.log.debugln("체크종료");

            this.log.debugln("전송시작");
            SendData();
            this.log.debugln("전송종료");

            this.conn.commit();
        }catch(Exception e){
            throw e;
        }
    }

    public void CheckFormat() throws Exception {

        DBExec dbe   = new DBExec(this.log, this.conn, this.procDate, this.procSeq);
        // dat 파일생성
        this.log.debugln("### Payment log Begin ###");

        this.log.debugln("DAT파일생성");
        FileOutputStream wFile = FileCheck.writeRecvFile(this.custId, this.procDate, this.procSeq,
                this.dataKind, this.log, this.testFlag);

        // 헤더부 수신
        this.log.debugln("H수신시작");
        byte[] rcvHByte = new byte[1024];
        int rcvHLen = 0;
        rcvHLen = Sock.rcvMessage(this.xc3Ssock, this.sockClient, this.dis, this.dos, this.hCtx, rcvHByte);
        String rcvHeader = new String(rcvHByte, 0, rcvHLen,Constants.MY_CHARSET);

        this.log.debugln("H수신종료[" + rcvHLen + "]:[" + rcvHeader + "]");

        // 헤더부 체크
        this.log.debugln("H체크시작");
        String[] rcvHCheck = new String[5];
        boolean hProc = HeadTele.checkHeadTele(rcvHeader.getBytes(Constants.MY_CHARSET), rcvHCheck, this.procDate,
                this.custId, this.msgSize);
        this.log.println("H체크결과:["+rcvHCheck[3].trim()+"]["+rcvHCheck[4].trim()+"]["+
                rcvHCheck[0].trim()+"]["+rcvHCheck[1].trim()+"]["+rcvHCheck[2].trim()+"]");

        // 헤더부 작성
        this.log.debugln("H작성시작");
        String strHeader = HeadTele.makeHeadTele(rcvHeader.getBytes(Constants.MY_CHARSET), rcvHCheck, hProc, this.msgSize);
        this.log.debugln("H작성자료[" + strHeader.length() + "]:"+strHeader);

        byte[] byteHeader = strHeader.getBytes(Constants.MY_CHARSET);
        wFile.write(byteHeader);
        this.log.debugln("H작성종료");

        String strRecCnt = ComCheck.getStr2Byte2Str(strHeader, 45, 6).trim();
        if ("".equals(strRecCnt)||(!"".equals(strRecCnt)&&!ComCheck.isAllNumber(strRecCnt))) {
            this.intRecCnt = 0;
        } else {
            this.intRecCnt = Integer.parseInt(strRecCnt);
        }
        int intOkCnt = 0;

        this.log.debugln("B수신시작[총건수:"+ this.intRecCnt+"]");

        for (int iCnt = 0; iCnt < this.intRecCnt; iCnt++) {
            //데이터부 수신
            byte[] rcvDByte = new byte[1024];
            byte[] byteBody = null;
            int rcvDLen = 0;
            this.dMsg.clear();

            rcvDLen = Sock.rcvMessage(this.xc3Ssock, this.sockClient, this.dis, this.dos, this.hCtx,
                    rcvDByte);

            String rcvData = new String(rcvDByte, 0, rcvDLen,Constants.MY_CHARSET);
            this.log.debugln("B수신종료[" + rcvDLen + "]:[" + rcvData + "]");

            // 데이터부 체크
            this.log.debugln("1.B체크시작(파일)");
            boolean bProc = false;

            if(hProc){
                bProc = DataTele.checkPayDataTele(rcvData.getBytes(Constants.MY_CHARSET), this.dMsg, this.custId,
                        this.msgSize, this.log, this.testFlag);

                dbe.setMsg(this.dMsg);
                if(bProc) {
                    this.log.debugln("2.B체크시작(DB)");

                    //************ 로직처리 ************//
                    bProc = dbe.processPayList(this.dMsg, iCnt+1);
                }

                this.log.debugln("3.B전문저장(DB)");
                dbe.insertTranData(this.dMsg, transConn);

                this.log.println("B체크결과:["+this.dMsg.getResult_cd()+"]["+this.dMsg.getResult_msg()+
                        "]["+this.dMsg.getdRecordType()+"]["+this.dMsg.getWorkField()+"]["+this.dMsg.getTrNo()+
                        "]["+this.dMsg.getVano()+"]["+this.dMsg.getChaTrNo()+"]["+this.dMsg.getPayMasDt()+"]");

                //데이터부 작성
                this.log.debugln("B작성시작");
                String strData = DataTele.makeDataTele(rcvData.getBytes(Constants.MY_CHARSET), this.dMsg, bProc, this.msgSize);
                this.log.debugln("B작성자료[" + strData.length() + "]:"+strData);

                byteBody = strData.getBytes(Constants.MY_CHARSET);
            }else{
                byteBody = rcvData.getBytes(Constants.MY_CHARSET);
            }

            wFile.write(byteBody);
            this.log.debugln("B작성종료");

            if (bProc != false) {
                // 성공건수카운트
                intOkCnt++;
            }
        }
        this.log.println("B[성공건수:" + intOkCnt + "]");
        this.log.debugln("B수신종료");

        // 테일전문수신
        this.log.debugln("T수신시작");
        byte[] rcvTByte = new byte[1024];
        int rcvTLen = 0;

        rcvTLen = Sock.rcvMessage(this.xc3Ssock, this.sockClient, this.dis, this.dos, this.hCtx, rcvTByte);
        String rcvTailer = new String(rcvTByte, 0, rcvTLen,Constants.MY_CHARSET);

        this.log.debugln("T수신종료[" + rcvTLen + "]:[" + rcvTailer + "]");

        // 테일전문체크
        this.log.debugln("T체크시작");
        String rcvTCheck[] = new String[5];
        boolean tProc = TailTele.checkTailTele(rcvTailer.getBytes(Constants.MY_CHARSET), rcvTCheck, this.custId,
                this.msgSize, this.log, this.testFlag);
        this.log.println("T체크결과:["+rcvTCheck[3].trim()+"]["+rcvTCheck[4].trim()+"]["+
                rcvTCheck[0].trim()+"]["+rcvTCheck[1].trim()+"]["+rcvTCheck[2]+"]");


        // 테일전문작성
        this.log.debugln("T작성시작");
        String strTailer = TailTele.makeTailTele(rcvTailer.getBytes(Constants.MY_CHARSET), rcvTCheck, tProc, intOkCnt,
                this.msgSize);
        this.log.debugln("T작성자료[" + strTailer.length() + "]:"+strTailer);

        byte[] byteTail = strTailer.getBytes(Constants.MY_CHARSET);
        wFile.write(byteTail);
        this.log.debugln("T작성종료");

        // 파일닫기
        if(wFile!=null) {
            wFile.close();
            this.log.debugln("DAT 파일생성완료");
        }
        return;
    }

    public void SendData() throws Exception {

        try{
            //파일읽기
            FileInputStream rFile   = FileCheck.readRecvFile(this.custId, this.procDate, this.procSeq,
                    this.dataKind, this.log, this.testFlag);

            String readHeader = FileCheck.readOneLine(rFile,this.dataKind);
            String HresultCd = readHeader.substring(11,15);

            // 헤더전문송신
            this.log.println("H전문송신시작");
            int sndHLen = Sock.sndMessage(this.xc3Ssock, this.sockClient, this.dis, this.dos, this.hCtx, readHeader);
            if(sndHLen > 0){
                this.log.debugln("H전문송신완료["+sndHLen+"]:["+readHeader+"]");
            }

            // 총결과건수설정
            int intRecCnt = this.intRecCnt;

            // 바디전문송신
            this.log.debugln("B전문총건수:" + intRecCnt);
            for (int iCnt = 0; iCnt < intRecCnt; iCnt++) {
                // 데이터전문
                String readData = FileCheck.readOneLine(rFile,this.dataKind);

                int sndDLen = Sock.sndMessage(this.xc3Ssock, this.sockClient, this.dis, this.dos, this.hCtx, readData);
                if(sndDLen > 0){
                    this.log.debugln("B전문송신종료["+sndDLen+"]:["+readData+"]");
                }
            }

            // 테일전문송신
            this.log.println("T전문송신시작");
            String readTailer = FileCheck.readOneLine(rFile,this.dataKind);

            int sndTLen = Sock.sndMessage(this.xc3Ssock, this.sockClient, this.dis, this.dos, this.hCtx, readTailer);
            if(sndTLen > 0){
                this.log.debugln("T전문송신완료["+sndTLen+"]:["+readTailer+"]");
            }

            if(rFile!=null) rFile.close();
            if("H000".equals(HresultCd) || this.intRecCnt == 0) {
                FileCheck.deleteReceiveFile(this.custId, this.procDate, this.procSeq, this.dataKind,
                        this.log, this.testFlag);
            }

            return;

        }catch(Exception e){

            //FIXME SMS 보낼 수 있도록 수정해야 함.
            try{
//                SendSMS sSMS = new SendSMS(custId, procDate, procSeq, custId + ":" + this.dataKind + e.getMessage(), this.log, this.testFlag);
//                sSMS.SMSSend();
            } catch (Exception s) {
                log.println(s.getMessage());
            }

            throw e;
        }
    }
}
