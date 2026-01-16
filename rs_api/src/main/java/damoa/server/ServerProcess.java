package damoa.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import damoa.Constants;
import damoa.comm.Sock;
import damoa.comm.bean.Xc3Deliver;
import damoa.comm.db.DBPoolConnector;
import damoa.comm.log.GeneLog;
import damoa.comm.tele.EndTele;
import damoa.comm.tele.StartTele;
import damoa.comm.util.DateUtil;
import damoa.conf.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerProcess implements Runnable {

    private Xc3Deliver xc3Ssock;
    private Socket sockClient;
    private BufferedReader dis;
    private DataOutputStream dos;
    private int hCtx;
    private String dataKind;
    private String workKind;
    private Logger LOG = LoggerFactory.getLogger(getClass());
    private GeneLog log;
    private String packetNo;
    private boolean testFlag;

    private DataSource ds;
    private Connection conn;
    private Connection transConn;

    public ServerProcess(Socket sockCsocket, int hCtx, boolean testFlag) {

        this.xc3Ssock   = new Xc3Deliver();
        this.sockClient = sockCsocket;
        this.hCtx       = hCtx;
        this.testFlag   = testFlag;

        this.packetNo = (new DateUtil()).getTime("YYYYMMDDhhmmssmis");
        this.log = new GeneLog(this.packetNo, "OPEN", "", this.testFlag,LOG);

        try {
            this.sockClient.setSoTimeout(10 * 60 * 1000);
            this.dis = new BufferedReader(new InputStreamReader(sockClient.getInputStream(),Constants.MY_CHARSET));
            this.dos = new DataOutputStream(this.sockClient.getOutputStream());

        } catch (Exception e) {
            log.println(e.getMessage());
        }
    }

    public void run() {

        String strCustID    = "";
        String strProcDate  = "";
        String strProcSeq   = "";

        try {
            // (C-1)클라이언트컨텍스트생성 - 아래는 보안적용시 사용
//            try {
//                hCtx = Sock.handShake(this.xc3Ssock, this.sockClient, this.dis,this.dos);
//            } catch (Exception e) {
//                try {
//                    // 클라이언트 소켓종료
//                    Sock.clientClose(this.xc3Ssock, this.sockClient, this.dis,this.dos);
//                } catch (Exception ex) {
//                    throw ex;
//                }
//            }
            if (hCtx < 0) {
                log.println("SERVER IS ALIVE ! "); // 향후삭제
                try {
                    // 클라이언트 소켓종료
                    Sock.clientClose(this.xc3Ssock, this.sockClient, this.dis,this.dos);
                } catch (Exception e) {
                    throw e;
                }
            } else {

                this.ds = DBPoolConnector.getInstance().getDataSource();

                try {

                    this.conn = ds.getConnection();
                    this.transConn = ds.getConnection();

                } catch (Exception e) {
                    log.println("[" + packetNo + "]" + "★★★ DB연결 초기화 중에 Error가 발생하였습니다.\n" + e.toString());
                }

                this.conn.setAutoCommit(false);

                log.println("클라이언트접속[소켓번호:" + this.sockClient.getLocalPort() + "]");

                // (S-1)시작전문 수신
                log.println("시작전문수신시작");
                byte rcvSByte[] = new byte[1024];
                int rcvSLen = Sock.rcvMessage(this.xc3Ssock, this.sockClient,this.dis, this.dos, this.hCtx, rcvSByte);

                String rcvSStr = new String(rcvSByte, 0, rcvSLen,Constants.MY_CHARSET);

                if (rcvSLen > 0) {
                    log.println("수신시작전문[" + rcvSLen + "]:[" + rcvSStr + "]");
                }
                log.println("시작전문수신완료");

                // (S-2)시작전문 체크
                String[] rcvSCheck = new String[8];
                boolean sProc   = StartTele.checkStartTele(rcvSStr.getBytes(Constants.MY_CHARSET), rcvSCheck,
                        Property.MSGSIZE_LENGTH, this.log, this.testFlag, this.conn);
                strCustID   = rcvSCheck[1].trim();  // 업체아이디
                this.dataKind   = rcvSCheck[2];
                this.workKind   = rcvSCheck[3];
                strProcDate = rcvSCheck[4].trim();  // 처리일자
                strProcSeq  = rcvSCheck[5].trim();  // 처리순번
                this.log = new GeneLog(this.packetNo, this.dataKind, this.workKind, this.testFlag,LOG);
                log.println("시작전문체크결과:["+rcvSCheck[6].trim()+"]["+rcvSCheck[7].trim()+"]["+
                        rcvSCheck[0].trim()+"]["+rcvSCheck[1].trim()+"]["+rcvSCheck[2].trim()+"]["+
                        rcvSCheck[3].trim()+"]["+rcvSCheck[4].trim()+"]["+rcvSCheck[5].trim()+"]");

                // (S-3)시작전문 결과만들기
                String strSendStart = StartTele.makeStartTele(rcvSStr.getBytes(Constants.MY_CHARSET), rcvSCheck, sProc);

                // (S-4)시작전문 전송
                log.println(">>> ]"+strSendStart+"[");
                int sndSLen = Sock.sndMessage(this.xc3Ssock, this.sockClient, this.dis, this.dos, this.hCtx, strSendStart);
                if(sndSLen > 0){
                    log.println("송신시작전문:[" + sndSLen + "]:[" + strSendStart + "]");
                }

                log.println("시작전문송신완료");


                // ///////////////////////////////////////////////////////////////////////////////////////

                if (sProc == true) {// 시작전문체크결과 true 이면 진행

                    if ("A".equalsIgnoreCase(this.workKind)) {
                        if (this.dataKind.equalsIgnoreCase("PAY")) {
                            Apply_Pay ap = new Apply_Pay(this.xc3Ssock, this.sockClient, this.dis,
                                    this.dos, this.hCtx, strCustID, strProcDate, strProcSeq, this.dataKind,
                                    Property.MSGSIZE_LENGTH, this.testFlag, this.conn, this.transConn,this.packetNo,this.workKind);
                            ap.Process();
                            ap = null;
                        }
                    } else if ("R".equalsIgnoreCase(this.workKind)) {
                        if (this.dataKind.equalsIgnoreCase("RCP")) {
                            Result_Rcp ap = new Result_Rcp(this.xc3Ssock, this.sockClient, this.dis,
                                    this.dos, this.hCtx, strCustID, strProcDate, strProcSeq, this.dataKind,
                                    Property.MSGSIZE_LENGTH, this.testFlag, this.conn,this.packetNo,this.workKind);
                            ap.Process();
                            ap = null;
                        }
                    }

                    // (E-1)이지스효성 종료전문작성 : 스트링형
                    String strSendEnd = EndTele.makeEndTele(rcvSStr.getBytes(Constants.MY_CHARSET), rcvSLen);

                    // (E-2)이지스효성 종료전문송신(이지스효성->업체)
                    log.println("종료전문송신시작");
                    int sndELen =  Sock.sndMessage(this.xc3Ssock, this.sockClient, this.dis, this.dos, this.hCtx, strSendEnd);
                    if(sndELen > 0){
                        log.println("종료전문송신DATA:[" + sndELen + "]:[" + strSendEnd + "]");
                    }
                    log.println("종료전문송신완료");
                    log.println("업무완료");

                    rcvSCheck = null;

                } else if (sProc == false) {
                    log.println("시작전문 오류로 접속종료");
                    try {
                        // 클라이언트 소켓종료
                        Sock.clientClose(this.xc3Ssock, this.sockClient, this.dis, this.dos);
                    } catch (Exception e) {
                        throw e;
                    }
                }
            }
        } catch(Exception e) {
            log.println(e.getMessage());
            e.printStackTrace();

            try {
                this.conn.rollback();
            } catch (SQLException e1) {
                log.println("[FATAL-EXCEPTION]"+e1.getMessage());
            }
        } finally {
            try {
                // 클라이언트 소켓종료
                Sock.clientClose(this.xc3Ssock, this.sockClient, this.dis, this.dos);
                log.println("소켓 종료");
                log.println("FINISH :::::::::::::::::::::::::::");
                if(this.conn!=null) conn.close();
                if(this.transConn!=null) transConn.close();
            } catch (Exception e) {
                log.println("[FATAL-EXCEPTION]"+e.getMessage());
            }
        }
    }
}
