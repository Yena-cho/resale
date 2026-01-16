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

import java.util.ArrayList;
import java.util.Date;

public class ApplyProcess {
    private ApplySocket as = null;
    private DBConn conn = null;
    private ApplyDB adb = null;
    private DateUtil du = null;
    private MyLogger log = new MyLogger();

    public ApplyProcess() {
        try {
            as = new ApplySocket();
            du = new DateUtil();
            //log.println(ClientInfo.DB_CLASS_NAME);
            //log.println(ClientInfo.PROCESS_NAME);
            conn = (DBConn) Class.forName(ClientInfo.DB_CLASS_NAME).newInstance();
            //log.println(ClientInfo.DB_CLASS_NAME + " new Instance..");
            adb = (ApplyDB) Class.forName(ClientInfo.PROCESS_NAME).newInstance();
            log.println(ClientInfo.PROCESS_NAME + " new Instance..");
            conn.init();
            log.println("JDBC Connection init...");
            conn.debug(ClientInfo.isDEBUG);
            adb.setInfo(conn);
            //log.println(ClientInfo.PROCESS_NAME + " setInfo......");
        } catch (Exception e) {
            log.printStackTrace(e);
            e.printStackTrace();
        }
    }

    public void process() {

        try {
        	conn.setAutoCommit(false);
        	//암호화 소켓 오픈
        	as.openSocket();

        	long start = System.currentTimeMillis();
        	try {
            	
            	if (as.isOpenSocket() == false) {
                	as = new ApplySocket();
                	as.openSocket();
                }

                //수납자료수신
                if (rcpProcess()) {
                    conn.commit();
                } else {
                    conn.rollback();
                }

            } catch (UserException ue) {
                conn.rollback();
                throw ue;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                long rcpTerm = (System.currentTimeMillis() - start);
                log.println(":::::::::::::::::: RCP=" + rcpTerm + "ms");
                as.closeSocket();
            }
        	
        	Thread.sleep(10 * 1000);

        	long payStart = System.currentTimeMillis();
        	try {
        		
        		if (as.isOpenSocket() == false) {
                	as = new ApplySocket();
                	as.openSocket();
                }

                //청구자료송신
                if (payProcess()) {
                    conn.commit();
                } else {
                    conn.rollback();
                }
            } catch (UserException ue) {
                conn.rollback();
                throw ue;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
            	long payTerm = System.currentTimeMillis() - payStart;
                log.println(":::::::::::::::::: PAY=" + payTerm + "ms");
                as.closeSocket();
            }

        } catch (UserException ue) {
            ue.printStackTrace();
            conn.rollback();
            log.printStackTrace(ue);
        } catch (Exception e) {
            conn.rollback();
            log.printStackTrace(e);
        } finally {
            conn.disconnect();
            if (as != null) {
            	try {
            		if (as.isOpenSocket()) {
            			as.closeSocket();
            		}
            	} catch (Exception e) {
            		log.printStackTrace(e);
            	} finally {
            		as = null;
            	}
            }
        }
    }

    private boolean payProcess() throws UserException, Exception {
        Date now = new Date();
        log.setStrDataField("PAY");
        log.setStrWorkField("A");
        log.setPacketno(du.dateTime17(now));
        log.println("청구자료 송신...");

        MirRecord record = null;
        AegisStartMsg sMsg = new AegisStartMsg();
        AegisEndMsg eMsg = new AegisEndMsg();
        AegisHMsg hMsg = new AegisHMsg();
        AegisDMsg dMsg = new AegisDMsg();
        AegisTMsg tMsg = new AegisTMsg();

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
        sMsg.procDate = du.toString(now, "yyyyMMdd");
        sMsg.procSeq = du.toString(now, "HHmmss");
        sMsg.cust_id = ClientInfo.USER_ID;
        sMsg.dataField = "PAY";
        sMsg.workField = "A";
        if (as.sendSocket(sMsg.makeMsg())) {
        	log.println("▷[" + sMsg.toString() + "]");
            log.println(">>>>>시작전문송신완료");
        } else {
            log.println("#####시작전문송신실패");
            isSent = false;
        }

        //---- 2.시작전문 수신
        sMsg.setMsg(as.recvSocket().getBytes(ClientInfo.CHAR_ENCODING));
        log.println("▷[" + sMsg.toString() + "]");
        log.println("<<<<<시작전문수신완료");
        if (!"Y".equalsIgnoreCase(sMsg.startYN)) {
            log.println("#####시작전문수신실패");
            isSent = false;
        }

        //---- 3.헤더 전송
        hMsg.recordType = "H";
        hMsg.serviceId = ClientInfo.USER_ID;
        hMsg.rowCnt = String.valueOf(rowCnt);
        if (isSent && as.sendSocket(hMsg.makeMsg())) {
        	log.println("▷[" + hMsg.toString() + "]");
        	log.println(">>>>>헤더전문송신완료");
        } else {
            log.println("#####헤더전문송신실패");
            isSent = false;
        }

        //---- 4.데이타 전송
        if (isSent) {
        	log.println(">>>>>데이터전문송신시작[청구Data " + hMsg.rowCnt + "건]");
            while (rs.isnext()) {
            	
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

                if (as.sendSocket(dMsg.makeMsg())) {
                    sendCnt++;
                    if (ClientInfo.isDEBUG) log.println("▷[D]파일라인번호=>[" + sendCnt + "]>>>>>" + dMsg.toString().trim());
                } else {
                	sendFailCnt++;
                	failRegSeq.add(dMsg.regSeq);
                	if (ClientInfo.isDEBUG) log.println("▷[D]파일라인번호(Failed)=>["+(sendCnt+sendFailCnt)+"]>>>>>" + dMsg.toString().trim());
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
        	if (sendCnt > 0){
        		log.println("#####데이타전문송신 성공["+sendCnt+"]건");
        		isSent = true;
        	}
        }
        
        // ---- 전송실패 전문 TRANST='99' 로 변경
        if (sendFailCnt > 0) {
        	int updateCount = 0;
        	for(String regseq : failRegSeq) {
        		if(adb.UpdatePayFailedData(regseq)) {
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
        if (isSent && as.sendSocket(tMsg.makeMsg())) {
            log.println(">>>>>테일전문송신완료");
        } else {
            log.println("#####테일전문송신실패");
        }

        if (isSent) {

            //---- 6.헤더 수신
            hMsg.clear();
            hMsg.setMsg(as.recvSocket().getBytes(ClientInfo.CHAR_ENCODING));
            log.println("<<<<<헤더전문수신완료");

            //---- 7.데이타 수신
            if (sendCnt > 0) {
                for (int i = 1; i <= sendCnt; i++) {
                    dMsg.clear();
                    dMsg.setMsg(as.recvSocket().getBytes(ClientInfo.CHAR_ENCODING));
                    if (ClientInfo.isDEBUG) log.println("▷[D]파일라인번호=>[" + i + "]>>>>>" + dMsg.toString());

                    if (adb.UpdatePayData(dMsg)) {
                        recvCnt++;
                    } else {
                        break;
                    }
                }
                conn.commit();
            }
        }

        if (sendCnt != recvCnt) {
            log.println("#####데이타전문수신실패");
            isSent = false;
        }

        if (isSent) {
            //---- 8.테일 수신
            tMsg.clear();
            tMsg.setMsg(as.recvSocket().getBytes(ClientInfo.CHAR_ENCODING));
            log.println("<<<<<테일전문수신완료");

            //---- 9.종료전문 수신
            eMsg.setMsg(as.recvSocket().getBytes(ClientInfo.CHAR_ENCODING));
            if (ClientInfo.isDEBUG) log.println("▷[" + eMsg.endYN + "][" + eMsg.endCd + "][" + eMsg.endMsg + "]");
            log.println("<<<<<종료전문수신완료");
        }

        return isSent;
    }//end payProcess()

    private boolean rcpProcess() throws UserException, Exception {
        Date now = new Date();
        log.setStrDataField("RCP");
        log.setStrWorkField("R");
        log.setPacketno(du.dateTime17(now));
        log.println("수납자료 수신...");

        AegisStartMsg sMsg = new AegisStartMsg();
        AegisEndMsg eMsg = new AegisEndMsg();
        AegisHMsg hMsg = new AegisHMsg();
        AegisRMsg rMsg = new AegisRMsg();
        AegisTMsg tMsg = new AegisTMsg();


        boolean isSent = true;
        int recvCnt = 0;

        //---- 1.시작전문 전송

        sMsg.procDate = du.toString(now, "yyyyMMdd");
        sMsg.procSeq = du.toString(now, "HHmmss");

        sMsg.cust_id = ClientInfo.USER_ID;
        sMsg.dataField = "RCP";
        sMsg.workField = "R";

        if (as.sendSocket(sMsg.makeMsg())) {
        	if (ClientInfo.isDEBUG) log.println("▷[" + sMsg.toString() + "]");
            log.println(">>>>>시작전문송신완료");
        } else {
            log.println("#####시작전문송신실패");
            isSent = false;
        }

        //---- 2.시작전문 수신
        sMsg.setMsg(as.recvSocket().getBytes(ClientInfo.CHAR_ENCODING));
        if (ClientInfo.isDEBUG) log.println("▷[" + sMsg.toString() + "]");
        log.println("<<<<<시작전문수신완료");
        if (!"Y".equalsIgnoreCase(sMsg.startYN)) {
            log.println("#####시작전문수신실패");
            isSent = false;
        }

        if (isSent) {
            //---- 3.헤더 수신
            hMsg.clear();
            hMsg.setMsg(as.recvSocket().getBytes(ClientInfo.CHAR_ENCODING));
            if (ClientInfo.isDEBUG) log.println("▷[" + hMsg.toString() + "]");
            log.println("<<<<<헤더전문수신완료");
            log.println("헤더 전문 수신" + hMsg.toString());
            //---- 4.데이타 수신
            if (StringUtil.null2zero(hMsg.rowCnt) > 0) {
            	log.println("<<<<<데이터전문수신시작["+hMsg.rowCnt+"]건");
                for (int i = 1; i <= StringUtil.null2zero(hMsg.rowCnt); i++) {
                    rMsg.setMsg(as.recvSocket().getBytes(ClientInfo.CHAR_ENCODING));
                    if (ClientInfo.isDEBUG) log.println("▷[D]파일라인번호=>[" + recvCnt + "]>>>>>" + rMsg.toString());

                    if (adb.InsertRcpData(rMsg)) {
                        recvCnt++;
                    } else {
                        break;
                    }
                }
                conn.commit();
            }
        }

        if (StringUtil.null2zero(hMsg.rowCnt) != recvCnt) {
            log.println("#####데이타전문수신실패");
            isSent = false;
        } else {
        	if (recvCnt > 0) log.println("#####데이타전문수신 성공["+recvCnt+"]건");
        }

        if (isSent) {
            //---- 5.테일 수신
            tMsg.clear();
            tMsg.setMsg(as.recvSocket().getBytes(ClientInfo.CHAR_ENCODING));
            if (ClientInfo.isDEBUG) log.println("▷[" + tMsg.toString() + "]");
            log.println("<<<<<테일전문수신완료");

            //---- 6.종료전문 수신
            eMsg.setMsg(as.recvSocket().getBytes(ClientInfo.CHAR_ENCODING));
            if (ClientInfo.isDEBUG) log.println("▷[" + eMsg.toString() + "]");
            log.println("<<<<<종료전문수신완료");
        }

        return isSent;
    }
}
