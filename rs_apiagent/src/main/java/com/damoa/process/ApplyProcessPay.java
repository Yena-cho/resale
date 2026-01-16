package com.damoa.process;

import com.damoa.comm.ClientInfo;
import com.damoa.comm.UserException;
import com.damoa.comm.db.DBConn;
import com.damoa.log.MyLogger;
import com.damoa.msg.*;
import com.damoa.net.ApplySocket;
import com.damoa.util.DateUtil;
import com.damoa.util.MirRecord;
import com.damoa.util.MirRecordSet;
import com.damoa.util.StringUtil;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;

@Deprecated
public class ApplyProcessPay {
    private ApplySocket as = null;
    private DBConn conn = null;
    private ApplyDB adb = null;
    private DateUtil du = null;
    private MyLogger log = new MyLogger();
    public ApplyProcessPay(){

        @SuppressWarnings("unused")
		ClientInfo ci = new ClientInfo();

        try{
            as = new ApplySocket();
            du = new DateUtil();
            conn = (DBConn)Class.forName(ClientInfo.DB_CLASS_NAME).newInstance();
            adb = (ApplyDB)Class.forName(ClientInfo.PROCESS_NAME).newInstance();
            conn.init();
            conn.debug(ClientInfo.isDEBUG);
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

                //청구자료송신
                if(payProcess()){
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

    private boolean payProcess() throws UserException, Exception{
        MirRecord record = null;
        AegisStartMsg sMsg = new AegisStartMsg();
        AegisEndMsg eMsg = new AegisEndMsg();
        AegisHMsg hMsg = new AegisHMsg();
        AegisDMsg dMsg = new AegisDMsg();
        AegisTMsg tMsg = new AegisTMsg();

        Date now = new Date();
        log.setStrDataField("PAY");
        log.setStrWorkField("A");
        log.setPacketno(du.dateTime17(now));
//        log.setPacketno(du.getTime("YYYYMMDDhhmmssmis"));
        boolean isSent = true;

        int rowCnt = 0;
        int sendCnt = 0;
        int sendFailCnt = 0;
        int recvCnt = 0;
        ArrayList<String> failRegSeq = new ArrayList<String>();  

        record = adb.selectPayData();
        MirRecordSet rs = record.getRec();
        rowCnt = rs.size();

        //---- 1.시작전문 전송
        sMsg.procDate = du.toString(now,"yyyyMMdd");
        sMsg.procSeq = du.toString(now,"HHmmss");
        sMsg.cust_id = ClientInfo.USER_ID;
        sMsg.dataField = "PAY";
        sMsg.workField = "A";
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

        //---- 3.헤더 전송
        hMsg.recordType = "H";
        hMsg.serviceId = ClientInfo.USER_ID;
        hMsg.rowCnt = String.valueOf(rowCnt);
        if(isSent && as.sendSocket(hMsg.makeMsg())){
            log.println(">>>>>헤더전문송신완료["+hMsg.rowCnt+"]건");
        }else{
            log.println("#####헤더전문송신실패");
            isSent = false;
        }

        //---- 4.데이타 전송
        if(isSent){
        	log.println(">>>>>데이터전문송신시작["+hMsg.rowCnt+"]건");
            while(rs.isnext()){

                dMsg.clear();
                dMsg.recordType = StringUtil.null2void(rs.getFieldString("recordtype"));
                dMsg.serviceId = StringUtil.null2void(rs.getFieldString("serviceid"));
                dMsg.workField = StringUtil.null2void(rs.getFieldString("workfield"));
                dMsg.trNo = StringUtil.null2void(rs.getFieldString("trno"));
                dMsg.vaNo = StringUtil.null2void(rs.getFieldString("vano"));
                dMsg.payMasMonth = StringUtil.null2void(rs.getFieldString("paymasmonth"));
                dMsg.payMasDt = StringUtil.null2void(rs.getFieldString("paymasdt"));
                dMsg.rcpStartDt = StringUtil.null2void(rs.getFieldString("rcpstartdt"));
                dMsg.rcpEndDt = StringUtil.null2void(rs.getFieldString("rcpenddt"));
                dMsg.rcpPrtEndDt = StringUtil.null2void(rs.getFieldString("rcpprtenddt"));
                dMsg.cusNm = StringUtil.null2void(rs.getFieldString("cusnm"));
                dMsg.smsYn = StringUtil.null2void(rs.getFieldString("smsyn"));
                dMsg.mobileNo = StringUtil.null2void(rs.getFieldString("mobileno"));
                dMsg.emailYn = StringUtil.null2void(rs.getFieldString("emailyn"));
                dMsg.email = StringUtil.null2void(rs.getFieldString("email"));
                dMsg.content1 = StringUtil.null2void(rs.getFieldString("content1"));
                dMsg.content2 = StringUtil.null2void(rs.getFieldString("content2"));
                dMsg.content3 = StringUtil.null2void(rs.getFieldString("content3"));
                dMsg.content4 = StringUtil.null2void(rs.getFieldString("content4"));
                dMsg.chaTrNo = StringUtil.null2void(rs.getFieldString("chatrno"));
                dMsg.adjfiGrpCd = StringUtil.null2void(rs.getFieldString("adjfigrpcd"));
                dMsg.payItemNm = StringUtil.null2void(rs.getFieldString("payitemnm"));
                dMsg.payItemAmt = StringUtil.null2void(rs.getFieldString("payitemamt"));
                dMsg.payItemYN = StringUtil.null2void(rs.getFieldString("payitemyn"));
                dMsg.rcpItemYN = StringUtil.null2void(rs.getFieldString("rcpitemyn"));
                dMsg.rcpBusinessNo = StringUtil.null2void(rs.getFieldString("rcpbusinessno"));
                dMsg.rcpPersonNo = StringUtil.null2void(rs.getFieldString("rcppersonno"));
                dMsg.prtPayItemNm = StringUtil.null2void(rs.getFieldString("prtpayitemnm"));
                dMsg.prtContent1 = StringUtil.null2void(rs.getFieldString("prtcontent1"));
                dMsg.prtSeq = StringUtil.null2void(rs.getFieldString("prtseq"));
                dMsg.regSeq = StringUtil.null2void(rs.getFieldString("regseq"));

                if(as.sendSocket(dMsg.makeMsg())){
                    sendCnt++;
                    if (ClientInfo.isDEBUG)log.println("▷[D]파일라인번호=>["+sendCnt+"]>>>>>" + dMsg.makeMsg());
                }else{
                	sendFailCnt++;
                	failRegSeq.add(dMsg.regSeq);
                	if (ClientInfo.isDEBUG)log.println("▷[D]파일라인번호(Failed)=>["+(sendCnt+sendFailCnt)+"]>>>>>" + dMsg.makeMsg());
                }
                rs.next();
            }
        }

        if(rowCnt != sendCnt){
            log.println("#####데이타전문송신 완료[성공"+sendCnt+"건/실패"+sendFailCnt+"건]");
            isSent = false;
            // 성공+실패건수가 rowcount와 같은시만 성공
            //isSent = (rowCnt == (sendCnt+sendFailCnt));
        } else {
        	log.println("#####데이타전문송신 성공["+sendCnt+"건]");
            isSent = true;
        }
        
        // ---- 전송실패 전문 TRANST='99' 로 변경
        if (sendFailCnt > 0) {
        	int updateCount = 0;
        	for(String regSeq : failRegSeq) {
        		if(adb.UpdatePayFailedData(regSeq)) {
        			updateCount++;
        		} 
        	}
        	conn.commit();
        	log.println("#####데이타전문송신 실패데이터 상태코드 변경[TRANST:99 "+updateCount+"건]");
        }

        //---- 5.테일 전송
        tMsg.recordType = "T";
        tMsg.serviceId = ClientInfo.USER_ID;
        tMsg.rowCnt = String.valueOf(rowCnt);
        //tMsg.rowCnt = String.valueOf(sendCnt);
        if(isSent && as.sendSocket(tMsg.makeMsg())){
            log.println(">>>>>테일전문송신완료");
        }else{
            log.println("#####테일전문송신실패");
        }

        if(isSent){

            //---- 6.헤더 수신
            hMsg.clear();
            hMsg.setMsg(as.recvSocket().getBytes(Charset.forName(ClientInfo.CHAR_ENCODING)));
            log.println("<<<<<헤더전문수신완료");

            //---- 7.데이타 수신
            if(sendCnt > 0){
                for(int i=1; i<=sendCnt; i++){
                    dMsg.clear();
                    dMsg.setMsg(as.recvSocket().getBytes(Charset.forName(ClientInfo.CHAR_ENCODING)));
                    if (ClientInfo.isDEBUG)log.println("▷[D]파일라인번호=>["+i+"]>>>>>" + dMsg.makeMsg());

                    if(adb.UpdatePayData(dMsg)){
                        recvCnt++;
                    }else{
                        break;
                    }
                }
                conn.commit();
            }
        }

        if(sendCnt != recvCnt){
            log.println("#####데이타전문수신실패");
            isSent = false;
        }

        if(isSent){
            //---- 8.테일 수신
            tMsg.clear();
            tMsg.setMsg(as.recvSocket().getBytes(Charset.forName(ClientInfo.CHAR_ENCODING)));
            log.println("<<<<<테일전문수신완료");

            //---- 9.종료전문 수신
            eMsg.setMsg(as.recvSocket().getBytes(Charset.forName(ClientInfo.CHAR_ENCODING)));
            log.println("<<<<<종료전문수신완료");
            log.println("▷["+eMsg.endYN+"]["+eMsg.endCd+"]["+eMsg.endMsg+"]");
        }
        
        return isSent;
    }//end payProcess()
}
