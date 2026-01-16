package com.damoa.process;

import com.damoa.comm.ClientInfo;
import com.damoa.comm.UserException;
import com.damoa.comm.db.DBConn;
import com.damoa.log.MyLogger;
import com.damoa.msg.*;
import com.damoa.net.ApplySocket;
import com.damoa.util.DateUtil;
import com.damoa.util.StringUtil;

import java.nio.charset.Charset;
import java.util.Date;

@Deprecated
public class ApplyProcessRcp {
    private ApplySocket as = null;
    private DBConn conn = null;
    private ApplyDB adb = null;
    private DateUtil du = null;
    private MyLogger log;
    public ApplyProcessRcp(){

        @SuppressWarnings("unused")
		ClientInfo ci = new ClientInfo();
        log = new MyLogger();

        try{
            as = new ApplySocket();
            du = new DateUtil();
            conn = (DBConn)Class.forName(ClientInfo.DB_CLASS_NAME).newInstance();
            adb = (ApplyDB)Class.forName(ClientInfo.PROCESS_NAME).newInstance();
            conn.init();
            conn.debug(true);
            adb.setInfo(conn);

        }catch(Exception e){
            log.printStackTrace(e);
        }
    }

    public void process(){
        try{
            try{
                conn.setAutoCommit(false);

                //암호화 소켓 오픈
                as.openSocket();

                //수납자료수신
                if(rcpProcess()){
                    conn.commit();
                }else{
                    conn.rollback();
                }
            }catch(UserException ue){
                conn.rollback();
                throw ue;
            }catch(Exception e){
                conn.rollback();
                throw e;
            }finally{
                try{
                    as.closeSocket();
                }catch(Exception e){
                }
            }
        }catch(UserException ue){
            log.printStackTrace(ue);
        }catch(Exception e){
            log.printStackTrace(e);
        }finally{
            conn.disconnect();
        }
    }

    private boolean rcpProcess() throws UserException, Exception{
        AegisStartMsg sMsg = new AegisStartMsg();
        AegisEndMsg eMsg = new AegisEndMsg();
        AegisHMsg hMsg = new AegisHMsg();
        AegisRMsg rMsg = new AegisRMsg();
        AegisTMsg tMsg = new AegisTMsg();

        Date now = new Date();
        log.setStrDataField("RCP");
        log.setStrWorkField("R");
        log.setPacketno(du.dateTime17(now));
        boolean isSent = true;
        int recvCnt = 0;

        //---- 1.시작전문 전송
        sMsg.procDate = du.toString(now,"yyyyMMdd");
        sMsg.procSeq = du.toString(now,"HHmmss");
        sMsg.cust_id = ClientInfo.USER_ID;
        sMsg.dataField = "RCP";
        sMsg.workField = "R";
        if(as.sendSocket(sMsg.makeMsg())){
            log.println(">>>>>시작전문송신완료");
            log.println("▷["+sMsg.procDate+"]["+sMsg.procSeq+"]["+sMsg.cust_id+"]");
        }else{
            log.println("#####시작전문송신실패");
            isSent = false;
        }

        //---- 2.시작전문 수신
        sMsg.setMsg(as.recvSocket().getBytes(Charset.forName(ClientInfo.CHAR_ENCODING)));
        log.println("<<<<<시작전문수신완료");
        log.println("▷["+sMsg.startYN+"]["+sMsg.startCd+"]["+sMsg.startMsg+"]");
        if(!"Y".equalsIgnoreCase(sMsg.startYN)) {
            log.println("#####시작전문수신실패");
            isSent = false;
        }

        if(isSent){
            //---- 3.헤더 수신
            hMsg.clear();
            hMsg.setMsg(as.recvSocket().getBytes(Charset.forName(ClientInfo.CHAR_ENCODING)));
            log.println("<<<<<헤더전문수신완료");
            log.println("헤더 전문 수신" + hMsg.toString());
            //---- 4.데이타 수신
            if(StringUtil.null2zero(hMsg.rowCnt) > 0){
                for(int i=1; i<=StringUtil.null2zero(hMsg.rowCnt); i++){
                    rMsg.setMsg(as.recvSocket().getBytes(Charset.forName(ClientInfo.CHAR_ENCODING)));
                    log.println("▷[D]파일라인번호=>["+recvCnt+"]>>>>>" + rMsg.toString());

                    if(adb.InsertRcpData(rMsg)){
                        recvCnt++;
                    }else{
                        break;
                    }
                }
                conn.commit();
            }
        }

        if(StringUtil.null2zero(hMsg.rowCnt) != recvCnt){
            log.println("#####데이타전문수신실패");
            isSent = false;
        }

        if(isSent){
            //---- 5.테일 수신
            tMsg.clear();
            tMsg.setMsg(as.recvSocket().getBytes(Charset.forName(ClientInfo.CHAR_ENCODING)));
            log.println("<<<<<테일전문수신완료");

            //---- 6.종료전문 수신
            eMsg.setMsg(as.recvSocket().getBytes(Charset.forName(ClientInfo.CHAR_ENCODING)));
            log.println("<<<<<종료전문수신완료");
            log.println("▷["+eMsg.endYN+"]["+eMsg.endCd+"]["+eMsg.endMsg+"]");
        }

        return isSent;
    }
}
