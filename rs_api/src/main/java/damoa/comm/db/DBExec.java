package damoa.comm.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Vector;

import damoa.comm.util.ComCheck;
import damoa.comm.util.DateUtil;
import damoa.comm.log.GeneLog;
import damoa.comm.util.StringUtil;
import damoa.server.entity.DamoaMsg;
import damoa.server.entity.rcpEntity;

public class DBExec {

    private GeneLog log;
    private StringUtil su = new StringUtil();
    private DateUtil du = new DateUtil();
    private Connection conn;

    private String chaCd	     = "";
    private String workField     = "";
    private String trNo          = "";
    private String vano          = "";
    private String payMasMonth   = "";
    private String payMasDt      = "";
    private String rcpStartDt    = "";
    private String rcpStartTm    = "";
    private String rcpEndDt      = "";
    private String rcpEndTm      = "";
    private String rcpPrtEndDt   = "";
    private String rcpPrtEndTm   = "";
    private String cusNm         = "";
    private String smsYn         = "";
    private String mobileNo      = "";
    private String emailYn       = "";
    private String email         = "";
    private String content1      = "";
    private String content2      = "";
    private String content3      = "";
    private String content4      = "";
    private String chaTrNo       = "";
    private String adjfiGrpCd    = "";
    private String payItemNm     = "";
    private String payItemAmt    = "";
    private String payItemYN     = "";
    private String rcpItemYN     = "";
    private String rcpBusinessNo = "";
    private String rcpPersonNo   = "";
    private String prtPayItemNm  = "";
    private String prtContent1   = "";
    private String prtSeq        = "";
    private String rcpPersonNo2  = "";

    private String procDate		 = "";
    private String procSeq		 = "";

    public DBExec(GeneLog log, Connection conn) {
        this.log = log;
        this.conn = conn;
    }

    public DBExec(GeneLog log, Connection conn, String procDate, String procSeq) {
        this.log = log;
        this.conn = conn;
        this.procDate = procDate;
        this.procSeq = procSeq;
    }

    public void setMsg(DamoaMsg dMsg){
        this.chaCd	       = dMsg.getChaCd();
        this.workField     = dMsg.getWorkField();
        this.trNo          = dMsg.getTrNo();
        this.vano          = dMsg.getVano();
        this.payMasMonth   = dMsg.getPayMasMonth();
        this.payMasDt      = dMsg.getPayMasDt();
        this.rcpStartDt    = dMsg.getRcpStartDt();
        this.rcpStartTm    = dMsg.getRcpStartTm();
        this.rcpEndDt      = dMsg.getRcpEndDt();
        this.rcpEndTm      = dMsg.getRcpEndTm();
        this.rcpPrtEndDt   = dMsg.getRcpPrtEndDt();
        this.rcpPrtEndTm   = dMsg.getRcpPrtEndTm();
        this.cusNm         = dMsg.getCusNm();
        this.smsYn         = dMsg.getSmsYn();
        this.mobileNo      = dMsg.getMobileNo();
        this.emailYn       = dMsg.getEmailYn();
        this.email         = dMsg.getEmail();
        this.content1      = dMsg.getContent1();
        this.content2      = dMsg.getContent2();
        this.content3      = dMsg.getContent3();
        this.content4      = dMsg.getContent4();
        this.chaTrNo       = dMsg.getChaTrNo();
        this.adjfiGrpCd    = dMsg.getAdjfiGrpCd();
        this.payItemNm     = dMsg.getPayItemNm();
        this.payItemAmt    = dMsg.getPayItemAmt();
        this.payItemYN     = dMsg.getPayItemYN();
        this.rcpItemYN     = dMsg.getRcpItemYN();
        this.rcpBusinessNo = dMsg.getRcpBusinessNo();
        this.rcpPersonNo   = dMsg.getRcpPersonNo();
        this.prtPayItemNm  = dMsg.getPrtPayItemNm();
        this.prtContent1   = dMsg.getPrtContent1();
        this.prtSeq        = dMsg.getPrtSeq();
        this.rcpPersonNo2  = dMsg.getRcpPersonNo2();
    }

    /**
     * 청구 데이터 입력
     * @param dMsg
     * @param transConn
     */
    public void insertTranData(DamoaMsg dMsg, Connection transConn){
        log.println("Dao 청구전문 입력...");
        PreparedStatement pstmt = null;
        StringBuffer sb         = new StringBuffer();

        String result_cd = dMsg.getResult_cd();
        String result_msg = dMsg.getResult_msg();
        String regSeq = dMsg.getRegSeq();


        sb.append("\n INSERT INTO trans_data_damoa ");
        sb.append("\n          (transdt, transtime, ");
        sb.append("\n           procdate, procseq, serviceid, workfield, result_cd, result_msg, ");
        sb.append("\n           trno, regseq, vano, paymasmonth, paymasdt, rcpstartdt, ");
        sb.append("\n           rcpstarttm, rcpenddt, rcpendtm, rcpprtenddt, rcpprtendtm, cusnm, ");
        sb.append("\n           smsyn, mobileno, emailyn, email, content1, content2, content3, ");
        sb.append("\n           content4, chatrno, adjfigrpcd, payitemnm, payitemamt, payitemyn, ");
        sb.append("\n           rcpitemyn, rcpbusinessno, rcppersonno, prtpayitemnm, ");
        sb.append("\n           prtcontent1, prtseq, rcppersonno2, filename, fileseq ");
        sb.append("\n          ) ");
        sb.append("\n   VALUES (TO_CHAR (now(), 'yyyymmdd'), TO_CHAR (now(), 'hh24miss'), ");
        sb.append("\n           ?, ?, ?, ?, ?, ?, ");
        sb.append("\n           ?, ?, ?, ?, ?, ?, ");
        sb.append("\n           ?, ?, ?, ?, ?, ?, ");
        sb.append("\n           ?, ?, ?, ?, ?, ?, ?, ");
        sb.append("\n           ?, ?, ?, ?, ?, ?, ");
        sb.append("\n           ?, ?, ?, ?, ");
        sb.append("\n           ?, ?, ?, ?, ? ");
        sb.append("\n          ) ");

        try {
            log.debugln(sb.toString());
            pstmt   = transConn.prepareStatement(sb.toString());

            pstmt.setString(1, procDate);
            pstmt.setString(2, procSeq);
            pstmt.setString(3, chaCd);
            pstmt.setString(4, workField);
            pstmt.setString(5, result_cd);
            pstmt.setString(6, result_msg);
            pstmt.setString(7, trNo);
            pstmt.setString(8, regSeq);
            pstmt.setString(9, vano);
            pstmt.setString(10, payMasMonth);
            pstmt.setString(11, payMasDt);
            pstmt.setString(12, rcpStartDt);
            pstmt.setString(13, rcpStartTm);
            pstmt.setString(14, rcpEndDt);
            pstmt.setString(15, rcpEndTm);
            pstmt.setString(16, rcpPrtEndDt);
            pstmt.setString(17, rcpPrtEndTm);
            pstmt.setString(18, cusNm);
            pstmt.setString(19, smsYn);
            pstmt.setString(20, mobileNo);
            pstmt.setString(21, emailYn);
            pstmt.setString(22, email);
            pstmt.setString(23, content1);
            pstmt.setString(24, content2);
            pstmt.setString(25, content3);
            pstmt.setString(26, content4);
            pstmt.setString(27, chaTrNo);
            pstmt.setString(28, adjfiGrpCd);
            pstmt.setString(29, payItemNm);
            pstmt.setString(30, payItemAmt);
            pstmt.setString(31, payItemYN);
            pstmt.setString(32, rcpItemYN);
            pstmt.setString(33, rcpBusinessNo);
            pstmt.setString(34, rcpPersonNo);
            pstmt.setString(35, prtPayItemNm);
            pstmt.setString(36, prtContent1);
            pstmt.setString(37, prtSeq);
            pstmt.setString(38, rcpPersonNo2);
            pstmt.setString(39, procDate);
            pstmt.setString(40, procSeq);

            log.debugln("[procDate]:"+procDate);
            log.debugln("[procSeq]:"+procSeq);
            log.debugln("[chaCd]:"+chaCd);
            log.debugln("[workField]:"+workField);
            log.debugln("[result_cd]:"+result_cd);
            log.debugln("[result_msg]:"+result_msg);
            log.debugln("[trNo]:"+trNo);
            log.debugln("[regSeq]:"+regSeq);
            log.debugln("[vano]:"+vano);
            log.debugln("[payMasMonth]:"+payMasMonth);
            log.debugln("[payMasDt]:"+payMasDt);
            log.debugln("[rcpStartDt]:"+rcpStartDt);
            log.debugln("[rcpStartTm]:"+rcpStartTm);
            log.debugln("[rcpEndDt]:"+rcpEndDt);
            log.debugln("[rcpEndTm]:"+rcpEndTm);
            log.debugln("[rcpPrtEndDt]:"+rcpPrtEndDt);
            log.debugln("[rcpPrtEndTm]:"+rcpPrtEndTm);
            log.debugln("[cusNm]:"+cusNm);
            log.debugln("[smsYn]:"+smsYn);
            log.debugln("[mobileNo]:"+mobileNo);
            log.debugln("[emailYn]:"+emailYn);
            log.debugln("[content1]:"+content1);
            log.debugln("[content2]:"+content2);
            log.debugln("[content3]:"+content3);
            log.debugln("[content4]:"+content4);
            log.debugln("[chaTrNo]:"+chaTrNo);
            log.debugln("[adjfiGrpCd]:"+adjfiGrpCd);
            log.debugln("[payItemNm]:"+payItemNm);
            log.debugln("[payItemAmt]:"+payItemAmt);
            log.debugln("[payItemYN]:"+payItemYN);
            log.debugln("[rcpItemYN]:"+rcpItemYN);
            log.debugln("[rcpBusinessNo]:"+rcpBusinessNo);
            log.debugln("[rcpPersonNo]:"+rcpPersonNo);
            log.debugln("[prtPayItemNm]:"+prtPayItemNm);
            log.debugln("[prtContent1]:"+prtContent1);
            log.debugln("[prtSeq]:"+prtSeq);
            log.debugln("[rcpPersonNo2]:"+rcpPersonNo2);
            log.debugln("[procDate]:"+procDate);
            log.debugln("[procSeq]:"+procSeq);

            pstmt.executeUpdate();
            if (transConn.getAutoCommit() == false) {
                transConn.commit();
            } else {
                log.println("autoCommit이 아님... ");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            log.println("-->exception발생 : "+sb.toString());

            StackTraceElement[] elem =  ex.getStackTrace();
            for(int i=0; i<elem.length; i++){
                log.println(elem[i].toString());
            }
        }finally {
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }
        return;
    }


    public int chkCha(String chaCd) {
        log.println("Dao 업체ID 체크....");
        PreparedStatement pstmt = null;
        ResultSet result        = null;
        StringBuffer sb         = new StringBuffer();
        int ret                 = -1;

        String chast = "";
        String chatrty = "";

        try {
            if("9".equals(chaCd.substring(0,1))) {
                sb.append("\n SELECT GRPST as chast, GRPTRTY as chatrty FROM XCHAGROUP ");
                sb.append("\n WHERE CHAGROUPID = ? ");
            } else {
                sb.append("\n SELECT CHAST, CHATRTY FROM XCHALIST WHERE CHACD = ? ");
            }

            log.debugln(sb.toString());
            pstmt   = conn.prepareStatement(sb.toString());
            pstmt.setString(1, chaCd);

            log.debugln("[chaCd]:"+chaCd);
            result  = pstmt.executeQuery();

            // 데이타가 없으면, 업체ID가 없는것으로 간주한다.
            if (result.next()) {
                chast = result.getString("chast");
                chatrty = result.getString("chatrty");

                log.debugln("chaCd="+chaCd+" chast="+chast+" chatrty="+chatrty);

                if("9".equals(chaCd.substring(0,1))) {
                    if ("ST02".equals(chast)) { // 해지..
                        ret = 2;
                    } else {
                        //ST01 정상..
                        ret = 0;    // 정상  // ST06만 정상.
                    }
                }else{
                    if ("ST02".equals(chast)) {  // 삭제된 가맹점ID임
                        ret = 2;
                    } else if ("ST04".equals(chast) || "ST07".equalsIgnoreCase(chast) || "ST08".equalsIgnoreCase(chast)){   // 정지된 가맹점ID임
                        ret = 3;
                    } else if ("ST05".equals(chast) || "ST01".equalsIgnoreCase(chast)){   // 대기중 가맹점ID임
                        ret = 4;
                    } else {
                        ret = 0;    // 정상  // ST06만 정상.
                    }
                }


                if(!"03".equals(chatrty)){
                    ret = 5;
                }

            } else {
                ret = 1;    // 가맹점ID 오류
                log.println("가맹점 없음... "+chaCd);
            }

        } catch (Exception e) {
            log.println("-->exception발생 : "+sb.toString());

            StackTraceElement[] elem =  e.getStackTrace();
            for(int i=0; i<elem.length; i++){
                log.println(elem[i].toString());
            }
        } finally {
            try { if (result != null) result.close(); }   catch (Exception ne) {}
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }

        return ret;
    }



    // 청구등록
    public boolean processPayList(DamoaMsg dMsg, int rowno){
        log.debugln("Dao 청구등록...");
        boolean dataYN          = false;
        Savepoint sp 			= null;

        if(!"".equals(rcpPersonNo2))
            rcpPersonNo = rcpPersonNo2;

        // 가맹점코드체크
        if(chkCha(chaCd) != 0){
            dMsg.setResult_cd("D004");
            dMsg.setResult_msg(ComCheck.makeSpaceData( "가맹점ID오류",30));
        }else{

            try{
                sp = conn.setSavepoint();

                if("N".equalsIgnoreCase(workField)){
                    processNew(dMsg);
                }else if("A".equalsIgnoreCase(workField)){
                    processAdd(dMsg);
                }else if("U".equalsIgnoreCase(workField) && !"".equals(trNo) && "".equals(chaTrNo)){
                    processModify(dMsg);
                }else if("U".equalsIgnoreCase(workField) && !"".equals(trNo) && !"".equals(chaTrNo)){
                    processModifyDet(dMsg);
                }else if("D".equalsIgnoreCase(workField) && !"".equals(trNo) && "".equals(chaTrNo)){
                    processDelete(dMsg);
                }else if("D".equalsIgnoreCase(workField) && !"".equals(trNo) && !"".equals(chaTrNo)){
                    processDeleteDet(dMsg);
                }

                if ("D000".equals(dMsg.getResult_cd())) {
                    dataYN = true;
                    log.println("DB 정상처리");
                }else{
                    conn.rollback(sp);
                    log.println("DB 비정상처리 "+dMsg.getResult_msg());
                }

                dMsg.setResult_msg(ComCheck.makeSpaceData(dMsg.getResult_msg(),30));

            }catch(Exception e){
                try{
                    conn.rollback(sp);
                }catch(Exception ex){}

                dMsg.setResult_cd("D999");
                dMsg.setResult_msg(ComCheck.makeSpaceData("DB데이타처리오류",30));

                StackTraceElement[] elem =  e.getStackTrace();
                for(int i=0; i<elem.length; i++){
                    log.println(elem[i].toString());
                }
            }
        }

        return dataYN;
    }

    public int getRcpDataCnt(String chaCd, String procDate, String tmpDate, String procSeq) {

        PreparedStatement pstmt = null;
        ResultSet result        = null;
        StringBuffer sb         = new StringBuffer();

        
        int cnt = 0;

        try {
            sb.append("\n select ");
            sb.append("\n         count(A.rcpmascd) as cnt ");
            sb.append("\n from ( ");

             sb.append("\n select m.rcpmascd, ");
             sb.append("\n case ");
             sb.append("\n         when c.rsltpgyn = 'Y' ");
             sb.append("\n     then ");
             sb.append("\n     CASE WHEN m.svecd = 'VAS'  THEN '01' ");
             sb.append("\n     WHEN m.svecd = 'OCD' then '01' END ");
             sb.append("\n         else ");
             sb.append("\n     CASE WHEN m.svecd = 'VAS'  THEN '01' ");
             sb.append("\n     ELSE '00' END ");
             sb.append("\n     end as svegubn ");
             sb.append("\n     from xrcpmas m ");
             sb.append("\n     left outer join xrcpdet d on m.rcpmascd = d.rcpmascd, ");
             sb.append("\n         xchalist c ");
             sb.append("\n         where ");
             sb.append("\n     m.chacd = c.chacd ");
             sb.append("\n     and svecd in ('VAS', 'OCD') ");
             sb.append("\n     and rcpmasst = 'PA03' ");

//            sb.append("\n 	select m.rcpmascd, ");
//            sb.append("\n 		   decode(c.rsltpgyn, 'Y', decode(m.svecd, 'VAS', '01', 'OCD', '01'), decode(m.svecd, 'VAS', '01', '00')) as svegubn ");
//            sb.append("\n 	from xrcpmas m, xrcpdet d, xchalist c  ");
//            sb.append("\n 	where m.rcpmascd = d.rcpmascd(+) ");
//            sb.append("\n 	and m.chacd = c.chacd ");
//            sb.append("\n 	and svecd in ('VAS', 'OCD') ");
//            sb.append("\n 	and rcpmasst = 'PA03' ");

            if("9".equals(chaCd.substring(0, 1))){
                sb.append("\n 	and m.chacd in (select chacd from xchalist where chagroupid = ?) ");
            }else{
                sb.append("\n 	and m.chacd = ? ");
            }

            if(!"000000".equals(procSeq)){
                // 전체 수납이 아니면..
//                sb.append("\n 	and m.regdt <= sysdate - 10 / 24 / 60 ");
//                sb.append("\n 	and payday > to_char(sysdate - 30, 'yyyymmdd') ");
                sb.append("\n and m.regdt <= now() - interval '10 minute' ");
                sb.append("\n and m.payday > to_char(now() - interval '30 day', 'yyyymmdd') ");
                sb.append("\n 	and m.trnst = 'ST01' ");
            } else {
                sb.append("\n 	and m.payday= ? ");
            }

            sb.append("\n 	) A");
            sb.append("\n where A.svegubn = '01'  ");

            log.debugln(sb.toString());
            pstmt   = conn.prepareStatement(sb.toString());

            pstmt.setString(1, chaCd);
            if("000000".equals(procSeq)){
                pstmt.setString(2, procDate);
            }

            log.debugln("[chaCd]:"+chaCd);
            if("000000".equals(procSeq)){
                log.debugln("[procDate]:"+procDate);
            }

            result  = pstmt.executeQuery();

            // 데이타가 없으면, 업체ID가 없는것으로 간주한다.
            if (result.next()) {
                cnt = Integer.parseInt(result.getString("cnt"));
            }
        } catch (Exception e) {
            log.println("-->exception발생 : "+sb.toString());
            log.println(e.toString());
        } finally {
            try { if (result != null) result.close(); }   catch (Exception ne) {}
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }

        return cnt;
    }

    public Vector getRcpData(String chaCd, String procDate, int rowCnt, String tmpDate, String procSeq) {

        Vector vResult = new Vector();

        PreparedStatement pstmt = null;
        ResultSet result        = null;
        StringBuffer sb         = new StringBuffer();

        rcpEntity[] entity = new rcpEntity[rowCnt];

        try {
            sb.append("\n select ");
            sb.append("\n         * ");
            sb.append("\n from ( ");
            sb.append("\n 		select ");
            sb.append("\n   		m.chacd,  ");
            sb.append("\n   		maskey,  ");
            sb.append("\n   		vano, masmonth,  ");
            sb.append("\n   		masday,  ");
            sb.append("\n   		payday||paytime payday,  ");
            sb.append("\n   		rcpusrname,  ");
            sb.append("\n   		substr(bnkcd,1,3) as bnkcd, bchcd, mdgubn,  ");
            sb.append("\n   		sucday || suctime sucday,  ");
            sb.append("\n   		rcpamt,  ");
            sb.append("\n   		detkey, ");
            sb.append("\n   		m.cusgubn1, ");
            sb.append("\n   		m.cusgubn2, ");
            sb.append("\n   		m.cusgubn3, ");
            sb.append("\n   		m.cusgubn4, ");
            sb.append("\n   		adjfiregkey, ");
            sb.append("\n   		payitemname, ");
            sb.append("\n   		payitemamt, ");
            sb.append("\n   		m.rcpmascd, ");
            sb.append("\n   		m.svecd, ");
            sb.append("\n case ");
            sb.append("\n         when c.rsltpgyn = 'Y' ");
            sb.append("\n     then ");
            sb.append("\n     CASE WHEN m.svecd = 'VAS'  THEN '01' ");
            sb.append("\n     WHEN m.svecd = 'OCD' then '01' END ");
            sb.append("\n         else ");
            sb.append("\n     CASE WHEN m.svecd = 'VAS'  THEN '01' ");
            sb.append("\n     ELSE '00' END ");
            sb.append("\n     end as svegubn ");

//            sb.append("\n   		decode(c.rsltpgyn, 'Y', decode(m.svecd, 'VAS', '01', 'OCD', '01'), decode(m.svecd, 'VAS', '01', '00')) as svegubn ");
            sb.append("\n     from xrcpmas m ");
            sb.append("\n     left outer join xrcpdet d on m.rcpmascd = d.rcpmascd, ");
            sb.append("\n         xchalist c ");
            sb.append("\n         where ");
            sb.append("\n     m.chacd = c.chacd ");
            sb.append("\n     and svecd in ('VAS', 'OCD') ");
            sb.append("\n     and rcpmasst = 'PA03' ");

//            sb.append("\n 		from xrcpmas m, xrcpdet d, xchalist c ");
//            sb.append("\n 		where m.rcpmascd = d.rcpmascd(+) ");
//            sb.append("\n 		and m.chacd = c.chacd ");
//            sb.append("\n 		and svecd in ('VAS', 'OCD') ");
//            sb.append("\n 		and rcpmasst = 'PA03' ");

            if("9".equals(chaCd.substring(0, 1))){
                sb.append("\n 		and m.chacd in (select chacd from xchalist where chagroupid = ?) ");
            }else{
                sb.append("\n 		and m.chacd = ? ");
            }

            if(!"000000".equals(procSeq)){
//                sb.append("\n 		and m.regdt <= sysdate - 10 / 24 / 60 ");
//                sb.append("\n 		and payday > to_char(sysdate - 30, 'yyyymmdd') ");
                sb.append("\n and m.regdt <= now() - interval '10 minute' ");
                sb.append("\n and m.payday > to_char(now() - interval '30 day', 'yyyymmdd') ");
                sb.append("\n 		and m.trnst = 'ST01' ");
            }else{
                sb.append("\n 		and m.payday= ? ");
            }

            sb.append("\n 	) A");
            sb.append("\n where A.svegubn = '01'  ");

            log.debugln(sb.toString());
            pstmt   = conn.prepareStatement(sb.toString());

            pstmt.setString(1, chaCd);
            if("000000".equals(procSeq)){
                pstmt.setString(2, procDate);
            }

            log.debugln("[chaCd]:"+chaCd);
            if("000000".equals(procSeq)){
                log.debugln("[procDate]:"+procDate);
            }

            result  = pstmt.executeQuery();

            for (int i=0;result.next();i++) {
                entity[i] = new rcpEntity();
                entity[i].chacd         = result.getString("chacd");
                entity[i].maskey        = result.getString("maskey");
                entity[i].vano          = result.getString("vano");
                entity[i].masmonth      = result.getString("masmonth");
                entity[i].masday        = result.getString("masday");
                entity[i].payday        = result.getString("payday");
                entity[i].rcpusrname    = result.getString("rcpusrname");
                entity[i].bnkcd         = result.getString("bnkcd");
                entity[i].bchcd         = result.getString("bchcd");
                entity[i].mdgubn        = result.getString("mdgubn");
                entity[i].trnday        = result.getString("sucday");
                entity[i].rcpamt        = result.getString("rcpamt");
                entity[i].detkey        = result.getString("detkey");
                entity[i].cusgubn1      = result.getString("cusgubn1");
                entity[i].cusgubn2      = result.getString("cusgubn2");
                entity[i].cusgubn3      = result.getString("cusgubn3");
                entity[i].cusgubn4      = result.getString("cusgubn4");
                entity[i].adjfiregkey   = result.getString("adjfiregkey");
                entity[i].payitemname   = result.getString("payitemname");
                entity[i].payitemamt    = result.getString("payitemamt");
                entity[i].rcpmascd      = result.getString("rcpmascd");
                entity[i].svecd       	= result.getString("svecd");
                vResult.add(entity[i]);
            }

        } catch (Exception e) {
            log.println("-->exception발생 : "+sb.toString());
            log.println(e.toString());
        } finally {
            try { if (result != null) result.close(); }   catch (Exception ne) {}
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }

        return vResult;
    }

    public boolean updateTrnSt(String Rcpmascds) {

        PreparedStatement pstmt = null;
        int result              = 0;
        StringBuffer sb         = new StringBuffer();

        try {
            sb.append("\n update xrcpmas ");
            sb.append("\n set trnst = 'ST07', ");
            sb.append("\n     trnday = to_char(now(),'yyyymmdd'), ");
            sb.append("\n     trntime = to_char(now(),'hh24miss') ");
            sb.append("\n where rcpmascd = '" +Rcpmascds+"'");

            log.debugln(sb.toString());
            pstmt   = conn.prepareStatement(sb.toString());

            log.debugln("[Rcpmascds]:"+Rcpmascds);
            result  = pstmt.executeUpdate();

        } catch (Exception e) {
            log.println("-->exception발생 : "+sb.toString());
            log.println(e.toString());
        } finally {
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }

        if(result > 0){
            log.println("조회한 건과 업데이트 건수 같음 : 성공");
            return true;
        }else{
            log.println("조회한 건과 업데이트 건수 다름 : 실패");
            return false;
        }
    }
    
    /**
     * 가상계좌로 전송상태값 업데이트
     * @param VaNo
     * @return
     */
    public boolean updateTrnStByVano(String VaNo) {
    	
    	PreparedStatement pstmt = null;
    	int result              = 0;
    	StringBuffer sb         = new StringBuffer();
    	
    	try {
    		sb.append("\n update xrcpmas ");
    		sb.append("\n set trnst = 'ST07', ");
    		sb.append("\n     trnday = to_char(now(),'yyyymmdd'), ");
    		sb.append("\n     trntime = to_char(now(),'hh24miss') ");
    		sb.append("\n where vano = '" +VaNo+"'");
    		
    		log.debugln(sb.toString());
    		pstmt   = conn.prepareStatement(sb.toString());
    		
    		log.debugln("[VaNo]:"+VaNo);
    		result  = pstmt.executeUpdate();
    		
    	} catch (Exception e) {
    		log.println("-->exception발생 : "+sb.toString());
    		log.println(e.toString());
    	} finally {
    		try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
    	}
    	
    	if(result > 0){
    		log.println("조회한 건과 업데이트 건수 같음 : 성공");
    		return true;
    	}else{
    		log.println("조회한 건과 업데이트 건수 다름 : 실패");
    		return false;
    	}
    }

    private void processNew(DamoaMsg dMsg) throws Exception{
        log.debugln("Dao 새청구데이터 저장.");
        PreparedStatement pstmt = null;
        ResultSet result        = null;
        StringBuffer sb			= new StringBuffer();

        String resultCd    		= "D000";
        String resultMsg   		= "정상";

        String notiMasCd     	= "";
        String notiDetCd     	= "";

        try {
            // ********************************* //
            // ********    청구마스터처리   ******** //
            // ********************************* //

            // 청구거래번호체크 notimas
            notiMasCd = chkNotiMas()[0];
            if(!"".equals(notiMasCd)){
                resultCd   = "D053";
                resultMsg  = "거래번호중복";
            }
            // 가상계좌체크
            if("D000".equals(resultCd)){
                String vanoUseYN = chkVano();
                if ("".equals(vanoUseYN)) {
                    resultCd   = "D021";
                    resultMsg  = "등록계좌아님";
                } else if ("N".equals(vanoUseYN)) {
                    resultCd   = "D068";
                    resultMsg  = "미사용계좌";
                }
            }
            // 납부마감일체크 notimas
            if ("D000".equals(resultCd)) {
                if(!chkEndDt()){
                    resultCd    = "D052";
                    resultMsg   = "청구일중복";
                }
            }

            if ("D000".equals(resultCd)) {
                // notimascd
                try {
                    sb.append("\n         select nextval('seqxnotimascd') as next");
                    pstmt   = conn.prepareStatement(sb.toString());
                    result  = pstmt.executeQuery();

                    log.debugln(sb.toString());

                    if (result.next()) {
                        notiMasCd = result.getString("next");
//                        notiMasCd = String.valueOf(result.getDouble("next"));
                    } else {
                        resultCd    = "D053";
                        resultMsg   = "DB청구등록오류";
                    }
                } catch (SQLException e) {
                    resultCd    = "D071";
                    resultMsg   = "DB청구등록오류";
                    log.println("-->exception발생 : "+sb.toString());
                    throw e;
                } finally {
                    sb.delete(0, sb.capacity());
                    try { if (result != null) result.close(); }   catch (Exception ne) {}
                    try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
                }
            }

            if ("D000".equals(resultCd)) {
                if(!insertNotiMas(notiMasCd)){
                    resultCd    = "D053";
                    resultMsg   = "DB청구등록오류";
                }
            }

            // ********************************* //
            // ********    청구디테일처리   ******** //
            // ********************************* //
            // 항목거래번호체크 notidet
            if ("D000".equals(resultCd)) {
                if(!"".equals(chkNotiDet(notiMasCd)[0])){
                    resultCd   = "D054";
                    resultMsg  = "항목번호중복["+chaTrNo+"]";
                }
            }
            // 다계좌체크
            if ("D000".equals(resultCd)) {
                String adjAcctResult = chkAdjAcct();
                if("1".equals(adjAcctResult)){
                    resultCd   = "D069";
                    resultMsg  = "다계좌미사용";
                }else if("2".equals(adjAcctResult)){
                    resultCd   = "D069";
                    resultMsg  = "다계좌미입력";
                }else if("3".equals(adjAcctResult)){
                    resultCd   = "D069";
                    resultMsg  = "다계좌미존재";
                }
            }

            // notidetcd
            if ("D000".equals(resultCd)) {
                notiDetCd = selectNotiDetCd();
                if("".equals(notiDetCd)){
                    resultCd    = "D071";
                    resultMsg   = "DB항목등록오류";
                }
            }
            if ("D000".equals(resultCd)) {
                if(!insertNotiDet(notiMasCd, notiDetCd)){
                    resultCd    = "D072";
                    resultMsg   = "DB항목등록오류";
                }
            }

            dMsg.setResult_cd(resultCd);
            dMsg.setResult_msg(resultMsg);

        } catch (Exception e) {
            resultCd    = "D071";
            resultMsg   = "DB청구거래오류";
            dMsg.setResult_cd(resultCd);
            dMsg.setResult_msg(resultMsg);

            throw e;

        } finally {
            try { if (result != null) result.close(); }   catch (Exception ne) {}
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }
    }

    private void processAdd(DamoaMsg dMsg) throws Exception{
        log.debugln("Dao 청구데이터 추가.");
        PreparedStatement pstmt = null;
        ResultSet result        = null;

        String resultCd    		= "D000";
        String resultMsg   		= "정상";

        String[] notiInfo		= {"",""};
        String notiMasCd     	= "";
        String notiMasSt     	= "";
        String notiDetCd     	= "";

        try {
            // ********************************* //
            // ********    청구마스터처리   ******** //
            // ********************************* //

            // 청구거래번호체크 notimas
            notiInfo = chkNotiMas();
            notiMasCd = notiInfo[0];
            notiMasSt = notiInfo[1];
            if("".equals(notiMasCd)){
                resultCd   = "D053";
                resultMsg  = "원거래없음";
            }else if("PA03".equals(notiMasSt)){
                resultCd   = "D075";
                resultMsg  = "기수납청구";
            }

            // ********************************* //
            // ********    청구디테일처리   ******** //
            // ********************************* //
            // 항목거래번호체크 notidet
            if ("D000".equals(resultCd)) {
                if(!"".equals(chkNotiDet(notiMasCd)[0])){
                    resultCd   = "D054";
                    resultMsg  = "항목번호중복";
                }
            }
            // 다계좌체크
            if ("D000".equals(resultCd)) {
                String adjAcctResult = chkAdjAcct();
                if("1".equals(adjAcctResult)){
                    resultCd   = "D069";
                    resultMsg  = "다계좌미사용";
                }else if("2".equals(adjAcctResult)){
                    resultCd   = "D069";
                    resultMsg  = "다계좌미입력";
                }else if("3".equals(adjAcctResult)){
                    resultCd   = "D069";
                    resultMsg  = "다계좌미존재";
                }
            }

            // notidetcd
            if ("D000".equals(resultCd)) {
                notiDetCd = selectNotiDetCd();
                if("".equals(notiDetCd)){
                    resultCd    = "D071";
                    resultMsg   = "DB항목등록오류";
                }
            }
            if ("D000".equals(resultCd)) {
                if(!insertNotiDet(notiMasCd, notiDetCd)){
                    resultCd    = "D072";
                    resultMsg   = "DB항목등록오류";
                }
            }

            dMsg.setResult_cd(resultCd);
            dMsg.setResult_msg(resultMsg);

        } catch (Exception e) {
            resultCd    = "D071";
            resultMsg   = "DB청구거래오류";
            dMsg.setResult_cd(resultCd);
            dMsg.setResult_msg(resultMsg);

            throw e;

        } finally {
            try { if (result != null) result.close(); }   catch (Exception ne) {}
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }
    }

    private void processModify(DamoaMsg dMsg) throws Exception{
        log.debugln("Dao 청구데이터 수정.");
        PreparedStatement pstmt = null;
        ResultSet result        = null;

        String resultCd    		= "D000";
        String resultMsg   		= "정상";

        String[] notiInfo		= {"",""};
        String notiMasCd     	= "";
        String notiMasSt     	= "";

        try {
            // ********************************* //
            // ********    청구마스터처리   ******** //
            // ********************************* //

            // 청구거래번호체크 notimas
            notiInfo = chkNotiMas();
            notiMasCd = notiInfo[0];
            notiMasSt = notiInfo[1];
            if("".equals(notiMasCd)){
                resultCd   = "D053";
                resultMsg  = "원거래없음";
            }else if("PA03".equals(notiMasSt)){
                resultCd   = "D075";
                resultMsg  = "기수납청구";
            }
            // 가상계좌체크
            if("D000".equals(resultCd)){
                String vanoUseYN = chkVano();
                if ("".equals(vanoUseYN)) {
                    resultCd   = "D021";
                    resultMsg  = "등록계좌아님";
                } else if ("N".equals(vanoUseYN)) {
                    resultCd   = "D068";
                    resultMsg  = "미사용계좌";
                }
            }
            // 납부마감일체크 notimas
            if ("D000".equals(resultCd)) {
                if(!chkEndDt()){
                    resultCd    = "D052";
                    resultMsg   = "청구일중복";
                }
            }

            if ("D000".equals(resultCd)) {
                if(!updateNotiMas(notiMasCd)){
                    resultCd    = "D053";
                    resultMsg   = "청구수정오류";
                }
            }

            dMsg.setResult_cd(resultCd);
            dMsg.setResult_msg(resultMsg);

        } catch (Exception e) {
            resultCd    = "D071";
            resultMsg   = "DB청구거래오류";
            dMsg.setResult_cd(resultCd);
            dMsg.setResult_msg(resultMsg);

            throw e;

        } finally {
            try { if (result != null) result.close(); }   catch (Exception ne) {}
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }
    }

    private void processModifyDet(DamoaMsg dMsg) throws Exception{
        log.debugln("Dao 청구데이터 수정. 항목거래번호존재");
        PreparedStatement pstmt = null;
        ResultSet result        = null;

        String resultCd    		= "D000";
        String resultMsg   		= "정상";

        String[] masInfo		= {"",""};
        String[] detInfo		= {"",""};
        String notiMasCd     	= "";
        String notiMasSt     	= "";
        String notiDetCd     	= "";
        String notiDetSt     	= "";

        try {
            // ********************************* //
            // ********    청구마스터처리   ******** //
            // ********************************* //

            // 청구거래번호체크 notimas
            masInfo = chkNotiMas();
            notiMasCd = masInfo[0];
            notiMasSt = masInfo[1];
            if("".equals(notiMasCd)){
                resultCd   = "D053";
                resultMsg  = "원거래없음";
            }else if("PA03".equals(notiMasSt)){
                resultCd   = "D075";
                resultMsg  = "기수납청구";
            }

            // ********************************* //
            // ********    청구디테일처리   ******** //
            // ********************************* //

            // 항목거래번호체크 notidet
            if ("D000".equals(resultCd)) {
                detInfo = chkNotiDet(notiMasCd);
                notiDetCd = detInfo[0];
                notiDetSt = detInfo[1];

                if("".equals(notiDetCd)){
                    resultCd   = "D054";
                    resultMsg  = "원거래없음";
                }else if("PA03".equals(notiDetSt)){
                    resultCd   = "D074";
                    resultMsg  = "기수납청구";
                }
            }
            // 다계좌체크
            if ("D000".equals(resultCd)) {
                String adjAcctResult = chkAdjAcct();
                if("1".equals(adjAcctResult)){
                    resultCd   = "D069";
                    resultMsg  = "다계좌미사용";
                }else if("2".equals(adjAcctResult)){
                    resultCd   = "D069";
                    resultMsg  = "다계좌미입력";
                }else if("3".equals(adjAcctResult)){
                    resultCd   = "D069";
                    resultMsg  = "다계좌미존재";
                }
            }

            if ("D000".equals(resultCd)) {
                if(!updateNotiDet(notiDetCd)){
                    resultCd    = "D054";
                    resultMsg   = "항목수정오류";
                }
            }

            dMsg.setResult_cd(resultCd);
            dMsg.setResult_msg(resultMsg);

        } catch (Exception e) {
            resultCd    = "D071";
            resultMsg   = "DB청구거래오류";
            dMsg.setResult_cd(resultCd);
            dMsg.setResult_msg(resultMsg);

            throw e;

        } finally {
            try { if (result != null) result.close(); }   catch (Exception ne) {}
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }
    }

    private void processDelete(DamoaMsg dMsg) throws Exception{
        log.debugln("Dao 청구데이터 삭제.");
        PreparedStatement pstmt = null;
        ResultSet result        = null;
        StringBuffer sb			= new StringBuffer();

        String resultCd    		= "D000";
        String resultMsg   		= "정상";

        String[] notiInfo		= {"",""};
        String notiMasCd     	= "";
        String notiMasSt     	= "";

        try {
            // ********************************* //
            // ********    청구마스터처리   ******** //
            // ********************************* //

            // 청구거래번호체크 notimas
            notiInfo = chkNotiMas();
            notiMasCd = notiInfo[0];
            notiMasSt = notiInfo[1];
            if("".equals(notiMasCd)){
                resultCd   = "D053";
                resultMsg  = "원거래없음";
            }else if("PA03".equals(notiMasSt)){
                resultCd   = "D075";
                resultMsg  = "기수납청구";
            }
            if("D000".equals(resultCd)){
                if(chkIsRcped(notiMasCd)){
                    resultCd   = "D075";
                    resultMsg  = "기수납청구";
                }
            }

            if ("D000".equals(resultCd)) {
                if(!DeleteNotiDet(notiMasCd, "")){
                    resultCd    = "D071";
                    resultMsg   = "DB청구삭제오류";
                }
            }
            if ("D000".equals(resultCd)) {
                if(!DeleteNotiMas(notiMasCd)){
                    resultCd    = "D053";
                    resultMsg   = "DB청구삭제오류";
                }
            }

            dMsg.setResult_cd(resultCd);
            dMsg.setResult_msg(resultMsg);

        } catch (Exception e) {
            resultCd    = "D071";
            resultMsg   = "DB청구거래오류";
            dMsg.setResult_cd(resultCd);
            dMsg.setResult_msg(resultMsg);

            throw e;

        } finally {
            try { if (result != null) result.close(); }   catch (Exception ne) {}
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }
    }

    private void processDeleteDet(DamoaMsg dMsg) throws Exception{
        log.debugln("Dao 청구데이터 삭제. 항목거래번호존재");
        PreparedStatement pstmt = null;
        ResultSet result        = null;
        StringBuffer sb			= new StringBuffer();

        String resultCd    		= "D000";
        String resultMsg   		= "정상";

        String[] notiInfo		= {"",""};
        String[] detInfo		= {"",""};
        String notiMasCd     	= "";
        String notiMasSt     	= "";
        String notiDetCd     	= "";
        String notiDetSt     	= "";

        try {
            // ********************************* //
            // ********    청구마스터처리   ******** //
            // ********************************* //

            // 청구거래번호체크 notimas
            notiInfo = chkNotiMas();
            notiMasCd = notiInfo[0];
            notiMasSt = notiInfo[1];
            if("".equals(notiMasCd)){
                resultCd   = "D053";
                resultMsg  = "원거래없음";
            }else if("PA03".equals(notiMasSt)){
                resultCd   = "D075";
                resultMsg  = "기수납청구";
            }

            // ********************************* //
            // ********    청구디테일처리   ******** //
            // ********************************* //
            if ("D000".equals(resultCd)) {

                // 항목거래번호체크 notidet
                detInfo = chkNotiDet(notiMasCd);
                notiDetCd = detInfo[0];
                notiDetSt = detInfo[1];

                if("".equals(notiDetCd)){
                    resultCd   = "D054";
                    resultMsg  = "원거래없음";
                }else if("PA03".equals(notiDetSt)){
                    resultCd   = "D074";
                    resultMsg  = "기수납청구";
                }
            }

            if ("D000".equals(resultCd)) {
                if(!DeleteNotiDet(notiMasCd, notiDetCd)){
                    resultCd    = "D071";
                    resultMsg   = "DB청구삭제오류";
                }
            }

            int detCnt = 0;
            if ("D000".equals(resultCd)) {
                sb.append("\n SELECT count(*) cnt FROM xnotidet ");
                sb.append("\n WHERE notimascd = ? ");
                sb.append("\n AND NOTIDETST = 'PA02' ");
                sb.append("\n AND CHATRTY = '03' ");
                sb.append("\n AND noti_can_yn = 'N' "); // 삭제여부확인

                log.debugln(sb.toString());
                log.debugln("[notiMasCd]:"+notiMasCd);

                try {
                    pstmt   = conn.prepareStatement(sb.toString());
                    pstmt.setString(1, notiMasCd);
                    result  = pstmt.executeQuery();

                    if (result.next()) {
                        detCnt = result.getInt("cnt");
                    }
                } catch (SQLException ex) {
                    resultCd    = "D071";
                    resultMsg   = "DB항목삭제오류";
                    log.println("-->exception발생 : "+sb.toString());
                    throw ex;
                } finally {
                    sb.delete(0, sb.capacity());
                    try { if (result != null) result.close(); }   catch (Exception ne) {}
                    try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
                }
            }

            if (detCnt==0) {
                if("D000".equals(resultCd) && !chkIsRcped(notiMasCd)){
                    if(!DeleteNotiMas(notiMasCd)){
                        resultCd    = "D053";
                        resultMsg   = "DB청구삭제오류";
                    }
                }
            }

            dMsg.setResult_cd(resultCd);
            dMsg.setResult_msg(resultMsg);

        } catch (Exception e) {
            resultCd    = "D071";
            resultMsg   = "DB청구거래오류";
            dMsg.setResult_cd(resultCd);
            dMsg.setResult_msg(resultMsg);

            throw e;

        } finally {
            try { if (result != null) result.close(); }   catch (Exception ne) {}
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }
    }

    private String[] chkNotiMas() throws Exception{
        log.debugln("청구거래번호체크 확인.");
        PreparedStatement pstmt = null;
        ResultSet result        = null;
        StringBuffer sb			= new StringBuffer();

        String notiMasCd = "";
        String notiMasSt = "";

        try {
            sb.append("\n SELECT notimascd, notimasst ");
            sb.append("\n   FROM xnotimas ");
            sb.append("\n  WHERE chacd = ? AND maskey = ? ");
            sb.append("\n    AND noti_can_yn = 'N' "); // 삭제여부확인

            log.debugln(sb.toString());
            pstmt   = conn.prepareStatement(sb.toString());
            pstmt.setString(1, chaCd);
            pstmt.setString(2, trNo);

            log.debugln("[chaCd]:"+chaCd);
            log.debugln("[trNo]:"+trNo);
            result  = pstmt.executeQuery();

            if (result.next()) {
                notiMasCd = su.null2void(result.getString("notimascd"));
                notiMasSt = su.null2void(result.getString("notimasst"));
            }

        } catch (Exception e) {
            log.println("-->exception발생 : "+sb.toString());
            throw e;
        } finally {
            sb.delete(0, sb.capacity());
            try { if (result != null) result.close(); }   catch (Exception ne) {}
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }

        String[] arrNotiMas = {notiMasCd, notiMasSt};
        return arrNotiMas;
    }

    private String[] chkNotiDet(String notiMasCd) throws Exception {
        log.debugln("Dao NotiDet 확인.");
        PreparedStatement pstmt = null;
        ResultSet result        = null;
        StringBuffer sb			= new StringBuffer();

        String notiDetCd = "";
        String notiDetSt = "";

        try {

            sb.append("\n SELECT notidetcd, notidetst ");
            sb.append("\n   FROM xnotidet ");
            sb.append("\n  WHERE detkey = ? AND notimascd = ? ");
            sb.append("\n    AND noti_can_yn = 'N' "); // 삭제여부확인

            log.debugln(sb.toString());
            pstmt   = conn.prepareStatement(sb.toString());
            pstmt.setString(1, chaTrNo);
            pstmt.setString(2, notiMasCd);

            log.debugln("[chaTrNo]:"+chaTrNo);
            log.debugln("[notiMasCd]:"+notiMasCd);
            result  = pstmt.executeQuery();

            if (result.next()) {
                notiDetCd = su.null2void(result.getString("notidetcd"));
                notiDetSt = su.null2void(result.getString("notidetst"));
            }

        } catch (Exception e) {
            log.println("-->exception발생 : "+sb.toString());
            throw e;
        } finally {
            sb.delete(0, sb.capacity());
            try { if (result != null) result.close(); }   catch (Exception ne) {}
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }

        String[] arrNotiDet = {notiDetCd, notiDetSt};
        return arrNotiDet;
    }

    private boolean chkEndDt() throws Exception {
        log.debugln("Dao EndDt 확인.");
        PreparedStatement pstmt = null;
        ResultSet result        = null;
        StringBuffer sb			= new StringBuffer();

        String ret				= "";

        boolean endDtResult = true;

        /**
         * FIXME 청구삭제 요청은 청구테이블의 NOTI_CAN_YN=N 조건 추가
         * 2018. 09. 06 jhjeong@finger.co.kr
         * 
         */
        try {
            sb.append("\n SELECT '1' cnt ");
            sb.append("\n   FROM xnotimas ");
            sb.append("\n  WHERE chacd = ? ");
            sb.append("\n    AND vano = ? ");
            sb.append("\n    AND notimasst = 'PA02' ");
            sb.append("\n    AND noti_can_yn = 'N' "); // 삭제여부확인
            sb.append("\n    AND enddate = ? ");
            if ("U".equals(workField)) {
                sb.append("\n AND MASKEY <> ? ");
            }
//            sb.append("\n    AND ROWNUM = 1 ");
            sb.append("\n    limit 1 ");

            log.debugln(sb.toString());
            pstmt   = conn.prepareStatement(sb.toString());

            pstmt.setString(1, chaCd);
            pstmt.setString(2, vano);
            pstmt.setString(3, rcpEndDt);
            if ("U".equals(workField)) {
                pstmt.setString(4, trNo);
            }

            log.debugln("[chaCd]:"+chaCd);
            log.debugln("[vano]:"+vano);
            log.debugln("[pPayEndDt]:"+rcpEndDt);
            if ("U".equals(workField)) {
                log.debugln("[masKey]:"+trNo);
            }

            result  = pstmt.executeQuery();

            ret = "";
            if (result.next()) {
                ret = su.null2void(result.getString("cnt"));
            }
            if ("1".equals(ret)) {
                endDtResult = false;
            }

        } catch (Exception e) {
            log.println("-->exception발생 : "+sb.toString());
            throw e;
        } finally {
            sb.delete(0, sb.capacity());
            try { if (result != null) result.close(); }   catch (Exception ne) {}
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }

        return endDtResult;
    }

    private String chkVano() throws Exception {
        log.debugln("Dao 가상계좌 확인.");
        PreparedStatement pstmt = null;
        ResultSet result        = null;
        StringBuffer sb			= new StringBuffer();

        String vanoUseYN = "";

        try {
            sb.append("\n SELECT USEYN FROM VALIST WHERE VANO = ? AND CHACD = ? ");

            log.debugln(sb.toString());
            pstmt   = conn.prepareStatement(sb.toString());

            pstmt.setString(1, vano);
            pstmt.setString(2, chaCd);

            log.debugln("[vano]:"+vano);
            log.debugln("[chaCd]:"+chaCd);

            result  = pstmt.executeQuery();

            if (result.next()) {
                vanoUseYN = su.null2void(result.getString("useyn"));
            }

        } catch (Exception e) {
            log.println("-->exception발생 : "+sb.toString());
            throw e;
        } finally {
            sb.delete(0, sb.capacity());
            try { if (result != null) result.close(); }   catch (Exception ne) {}
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }

        return vanoUseYN;
    }

    private String chkAdjAcct() throws Exception {
        log.debugln("다계좌 체크.....");
        PreparedStatement pstmt = null;
        ResultSet result        = null;
        StringBuffer sb			= new StringBuffer();

        String ret = "";
        String adjAcctResult = "0";

        try {
            sb.append("\n SELECT 1 CNT FROM XCHALIST WHERE CHACD = ? AND ADJACCYN = 'Y' ");

            log.debugln(sb.toString());
            pstmt   = conn.prepareStatement(sb.toString());
            pstmt.setString(1, chaCd);

            log.debugln("[chaCd]:"+chaCd);
            result  = pstmt.executeQuery();

            ret = "";
            if (result.next()) {
                ret = su.null2void(result.getString("cnt"));
            }

        } catch (Exception e) {
            log.println("-->exception발생 : "+sb.toString());
            throw e;
        } finally {
            sb.delete(0, sb.capacity());
            try { if (result != null) result.close(); }   catch (Exception ne) {}
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }

        if("".equals(ret) && !"".equals(adjfiGrpCd)){
            adjAcctResult = "1";
        }else if("1".equals(ret) && "".equals(adjfiGrpCd)){
            adjAcctResult = "2";
        }else if("1".equals(ret) && !"".equals(adjfiGrpCd)){

            try {

                sb.append("\n SELECT 1 cnt FROM XADJGROUP WHERE CHACD = ? AND ADJFIREGKEY = ? ");

                log.debugln(sb.toString());
                pstmt   = conn.prepareStatement(sb.toString());
                pstmt.setString(1, chaCd);
                pstmt.setString(2, adjfiGrpCd);

                log.debugln("[chaCd]:"+chaCd);
                log.debugln("[adjfiGrpCd]:"+adjfiGrpCd);
                result  = pstmt.executeQuery();

                ret = "";
                if (result.next()) {
                    ret = su.null2void(result.getString("cnt"));
                }

            } catch (Exception e) {
                log.println("-->exception발생 : "+sb.toString());
                throw e;
            } finally {
                sb.delete(0, sb.capacity());
                try { if (result != null) result.close(); }   catch (Exception ne) {}
                try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
            }

            if(!"1".equals(ret)){
                adjAcctResult = "3";
            }
        }

        return adjAcctResult;
    }

    private boolean chkIsRcped(String notiMasCd) throws Exception {
        log.debugln("Dao chkIsRcped ...");
        PreparedStatement pstmt = null;
        ResultSet result        = null;
        StringBuffer sb			= new StringBuffer();

        boolean isRcped = false;

        try {
            sb.append("\n select '1' as cnt from xrcpmas");
            sb.append("\n where notimascd = ? ");
            sb.append("\n and   rcpmasst = 'PA03' ");
//            sb.append("\n and   rownum = 1 ");
            sb.append("\n limit 1 ");

            pstmt   = conn.prepareStatement(sb.toString());
            pstmt.setString(1, notiMasCd);

            log.debugln(sb.toString());
            log.debugln("[notiMasCd]:"+notiMasCd);

            result = pstmt.executeQuery();

            String ret = "";
            if (result.next()) {
                ret = su.null2void(result.getString("cnt"));
            }
            if ("1".equals(ret)) {
                isRcped = true;
            }

        } catch (Exception e) {
            log.println("-->exception발생 : "+sb.toString());
            throw e;
        } finally {
            sb.delete(0, sb.capacity());
            try { if (result != null) result.close(); }   catch (Exception ne) {}
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }

        return isRcped;
    }

    private String selectNotiDetCd() throws Exception {
        log.debugln("청구 상세 확인.. ...");
        PreparedStatement pstmt = null;
        ResultSet result        = null;
        StringBuffer sb			= new StringBuffer();

        String notiDetCd = "";

        sb.append("\n select nextval('seqxnotidetcd') as next ");

        log.debugln(sb.toString());

        try {
            pstmt   = conn.prepareStatement(sb.toString());
            result  = pstmt.executeQuery();

            if (result.next()) {
                notiDetCd = result.getString("next");
//                notiDetCd = String.valueOf(result.getDouble("next"));
            }
        } catch (SQLException e) {
            log.println("-->exception발생 : "+sb.toString());
            throw e;
        } finally {
            sb.delete(0, sb.capacity());
            try { if (result != null) result.close(); }   catch (Exception ne) {}
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }

        return notiDetCd;
    }

    private boolean insertNotiMas(String notiMasCd) throws Exception {
        log.debugln("Dao 청구서 입력 ...");
        PreparedStatement pstmt = null;
        StringBuffer sb			= new StringBuffer();

        boolean result = false;

        sb.append("\n insert into xnotimas (");
        sb.append("\n notimascd, ");
        sb.append("\n chacd, ");
        sb.append("\n vano, ");
        sb.append("\n maskey, ");
        sb.append("\n masmonth, ");
        sb.append("\n masday, ");
        sb.append("\n cusgubn1, ");
        sb.append("\n cusgubn2, ");
        sb.append("\n cusgubn3, ");
        sb.append("\n cusgubn4, ");
        sb.append("\n cusname, ");
        sb.append("\n cushp, ");
        sb.append("\n cusmail, ");
        sb.append("\n smsyn, ");
        sb.append("\n mailyn, ");
        sb.append("\n cusoffno, ");
        sb.append("\n startdate, ");
        sb.append("\n starttime, ");
        sb.append("\n enddate, ");
        sb.append("\n endtime, ");
        sb.append("\n printdate, ");
        sb.append("\n printtime, ");
        sb.append("\n notimasst, ");
        sb.append("\n makedt, ");
        sb.append("\n maker, ");
        sb.append("\n regdt, ");
        sb.append("\n chatrty ");
        sb.append("\n ) values( ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n 'PA02', ");
        sb.append("\n now(), ");
        sb.append("\n ?, ");
        sb.append("\n now(), ");
        sb.append("\n '03' ");
        sb.append("\n ) ");

        try {
            int pIdx = 0;
            pstmt   = conn.prepareStatement(sb.toString());
            pstmt.setString(++pIdx, notiMasCd);
            pstmt.setString(++pIdx, chaCd);
            pstmt.setString(++pIdx, vano);
            pstmt.setString(++pIdx, trNo.toUpperCase());
            pstmt.setString(++pIdx, payMasMonth);
            pstmt.setString(++pIdx, payMasDt);
            pstmt.setString(++pIdx, content1);
            pstmt.setString(++pIdx, content2);
            pstmt.setString(++pIdx, content3);
            pstmt.setString(++pIdx, content4);
            pstmt.setString(++pIdx, cusNm);
            pstmt.setString(++pIdx, mobileNo);
            pstmt.setString(++pIdx, email);
            pstmt.setString(++pIdx, smsYn.toUpperCase());
            pstmt.setString(++pIdx, emailYn.toUpperCase());
            pstmt.setString(++pIdx, rcpPersonNo);
            pstmt.setString(++pIdx, rcpStartDt);
            pstmt.setString(++pIdx, rcpStartTm);
            pstmt.setString(++pIdx, rcpEndDt);
            pstmt.setString(++pIdx, rcpEndTm);
            pstmt.setString(++pIdx, rcpPrtEndDt);
            pstmt.setString(++pIdx, rcpPrtEndTm);
            pstmt.setString(++pIdx, chaCd);
            if (pstmt.executeUpdate() == 1) {
                result = true;
            }
        } catch (SQLException e) {
            log.println("-->exception발생 : "+sb.toString());
            throw e;
        }finally{
            sb.delete(0, sb.capacity());
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }

        return result;
    }

    private boolean insertNotiDet(String notiMasCd, String notiDetCd) throws Exception {
        log.debugln("청구서 상세 입력 ...");
        PreparedStatement pstmt = null;
        StringBuffer sb			= new StringBuffer();

        boolean result = false;

        sb.append("\n insert into xnotidet (");
        sb.append("\n notidetcd, ");
        sb.append("\n notimascd, ");
        sb.append("\n detkey, ");
        sb.append("\n adjfiregkey, ");
        sb.append("\n payitemname, ");
        sb.append("\n payitemamt, ");
        sb.append("\n payitemselyn, ");
        sb.append("\n rcpitemyn, ");
        sb.append("\n chaoffno, ");
        sb.append("\n cusoffno, ");
        sb.append("\n ptritemname, ");
        sb.append("\n ptritemremark, ");
        sb.append("\n ptritemorder, ");
        sb.append("\n notidetst, ");
        sb.append("\n makedt, ");
        sb.append("\n maker, ");
        sb.append("\n regdt, ");
        sb.append("\n chatrty, ");
        sb.append("\n batchseq ");
        sb.append("\n ) values( ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n ?, ");
        sb.append("\n 'PA02', ");
        sb.append("\n now(), ");
        sb.append("\n ?, ");
        sb.append("\n now(), ");
        sb.append("\n '03', ");
        sb.append("\n ? ");
        sb.append("\n ) ");

        log.debugln(sb.toString());
        try {
            int pIdx = 0;
            pstmt   = conn.prepareStatement(sb.toString());
            pstmt.setString(++pIdx, notiDetCd);
            pstmt.setString(++pIdx, notiMasCd);
            pstmt.setString(++pIdx, chaTrNo.toUpperCase());
            if("".equals(adjfiGrpCd)){
                adjfiGrpCd = "00001";
            }
            pstmt.setString(++pIdx, adjfiGrpCd);
            pstmt.setString(++pIdx, payItemNm);
            pstmt.setLong(++pIdx, Long.parseLong(payItemAmt));
            pstmt.setString(++pIdx, payItemYN.toUpperCase());
            pstmt.setString(++pIdx, rcpItemYN.toUpperCase());
            pstmt.setString(++pIdx, rcpBusinessNo);
            pstmt.setString(++pIdx, rcpPersonNo);
            pstmt.setString(++pIdx, prtPayItemNm);
            pstmt.setString(++pIdx, prtContent1);
            pstmt.setString(++pIdx, prtSeq);
            pstmt.setString(++pIdx, chaCd);
            pstmt.setString(++pIdx, procDate+procSeq);
            if (pstmt.executeUpdate() == 1) {
                result = true;
            }

            log.debugln("[notiDetCd] : " + notiDetCd);
            log.debugln("[notiMasCd] : " + notiMasCd);
            log.debugln("[chaTrNo] : " + chaTrNo.toUpperCase());
            log.debugln("[adjfiGrpCd] : " + adjfiGrpCd);
            log.debugln("[payItemNm] : " + payItemNm);
            log.debugln("[payItemAmt] : " + payItemAmt);
            log.debugln("[payItemYN] : " + payItemYN.toUpperCase());
            log.debugln("[rcpItemYN] : " + rcpItemYN.toUpperCase());
            log.debugln("[rcpBusinessNo] : " + rcpBusinessNo);
            log.debugln("[rcpPersonNo] : " + rcpPersonNo);
            log.debugln("[prtPayItemNm] : " + prtPayItemNm);
            log.debugln("[prtContent1] : " + prtContent1);
            log.debugln("[prtSeq] : " + prtSeq);
            log.debugln("[chaCd] : " + chaCd);
            log.debugln("[procDate+procSeq] : " + procDate+procSeq);

        } catch (SQLException e) {
            log.println("-->exception발생 : "+sb.toString());
            throw e;
        }finally{
            sb.delete(0, sb.capacity());
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }

        return result;
    }

    private boolean updateNotiMas(String notiMasCd) throws Exception {
        log.debugln("Dao 청구서 업데이트");
        PreparedStatement pstmt = null;
        StringBuffer sb			= new StringBuffer();

        boolean result = false;

        sb.append("\n update xnotimas set ");
        sb.append("\n vano = ?, ");
        sb.append("\n masmonth = ?, ");
        sb.append("\n masday = ?, ");
        sb.append("\n cusgubn1 = ?, ");
        sb.append("\n cusgubn2 = ?, ");
        sb.append("\n cusgubn3 = ?, ");
        sb.append("\n cusgubn4 = ?, ");
        sb.append("\n cusname = ?, ");
        sb.append("\n cushp = ?, ");
        sb.append("\n cusmail = ?, ");
        sb.append("\n SMSYN = ?, ");
        sb.append("\n MAILYN = ?, ");
        sb.append("\n startdate = ?, ");
        sb.append("\n starttime = ?, ");
        sb.append("\n enddate = ?, ");
        sb.append("\n endtime = ?, ");
        sb.append("\n printdate = ?, ");
        sb.append("\n printtime = ?, ");
        sb.append("\n MAKER = ?, ");
        sb.append("\n makedt = now() ");
        sb.append("\n where notimascd = ? ");
        sb.append("\n and notimasst = 'PA02' ");
        sb.append("\n and chatrty = '03' ");

        try {
            int pIdx = 0;
            pstmt   = conn.prepareStatement(sb.toString());
            pstmt.setString(++pIdx, vano);
            pstmt.setString(++pIdx, payMasMonth);
            pstmt.setString(++pIdx, payMasDt);
            pstmt.setString(++pIdx, content1);
            pstmt.setString(++pIdx, content2);
            pstmt.setString(++pIdx, content3);
            pstmt.setString(++pIdx, content4);
            pstmt.setString(++pIdx, cusNm);
            pstmt.setString(++pIdx, mobileNo);
            pstmt.setString(++pIdx, email);
            pstmt.setString(++pIdx, smsYn.toUpperCase());
            pstmt.setString(++pIdx, emailYn.toUpperCase());
            pstmt.setString(++pIdx, rcpStartDt);
            pstmt.setString(++pIdx, rcpStartTm);
            pstmt.setString(++pIdx, rcpEndDt);
            pstmt.setString(++pIdx, rcpEndTm);
            pstmt.setString(++pIdx, rcpPrtEndDt);
            pstmt.setString(++pIdx, rcpPrtEndTm);
            pstmt.setString(++pIdx, chaCd);
            pstmt.setString(++pIdx, notiMasCd);
            if (pstmt.executeUpdate() == 1) {
                result = true;
            }
        } catch (SQLException e) {
            log.println("-->exception발생 : "+sb.toString());
            throw e;
        }finally{
            sb.delete(0, sb.capacity());
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }

        return result;
    }

    private boolean updateNotiDet(String notiDetCd) throws Exception {
        log.debugln("Dao 청구서 상세 업데이트");
        PreparedStatement pstmt = null;
        StringBuffer sb			= new StringBuffer();

        boolean result = false;

        sb.append("\n update xnotidet set ");
        sb.append("\n adjfiregkey = ?, ");
        sb.append("\n payitemname = ?, ");
        sb.append("\n payitemamt = ?, ");
        sb.append("\n payitemselyn = ?, ");
        sb.append("\n rcpitemyn = ?, ");
        sb.append("\n chaoffno = ?, ");
        sb.append("\n cusoffno = ?, ");
        sb.append("\n ptritemname = ?, ");
        sb.append("\n ptritemremark = ?, ");
        sb.append("\n ptritemorder = ?, ");
        sb.append("\n MAKER = ?, ");
        sb.append("\n makedt = now(), ");
        sb.append("\n batchseq = ? ");
        sb.append("\n where notidetcd = ? ");
        sb.append("\n and notidetst = 'PA02' ");
        sb.append("\n and chatrty = '03' ");

        try {
            int pIdx = 0;
            pstmt   = conn.prepareStatement(sb.toString());
            if("".equals(adjfiGrpCd)){
                adjfiGrpCd = "00001";
            }
            pstmt.setString(++pIdx, adjfiGrpCd);
            pstmt.setString(++pIdx, payItemNm);
            pstmt.setString(++pIdx, payItemAmt);
            pstmt.setString(++pIdx, payItemYN.toUpperCase());
            pstmt.setString(++pIdx, rcpItemYN.toUpperCase());
            pstmt.setString(++pIdx, rcpBusinessNo);
            pstmt.setString(++pIdx, rcpPersonNo);
            pstmt.setString(++pIdx, prtPayItemNm);
            pstmt.setString(++pIdx, prtContent1);
            pstmt.setString(++pIdx, prtSeq);
            pstmt.setString(++pIdx, chaCd);
            pstmt.setString(++pIdx, procDate+procSeq);
            pstmt.setString(++pIdx, notiDetCd);

            if (pstmt.executeUpdate() == 1) {
                result = true;
            }
        } catch (SQLException e) {
            log.println("-->exception발생 : "+sb.toString());
            throw e;
        }finally{
            sb.delete(0, sb.capacity());
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }

        return result;
    }

    private boolean DeleteNotiMas(String notiMasCd) throws Exception {
        log.debugln("Dao 청구서 삭제");
        PreparedStatement pstmt = null;
        StringBuffer sb			= new StringBuffer();

        boolean result = false;
        
        /**
         * FIXME 청구삭제 요청은 청구테이블의 NOTI_CAN_YN=Y 로  업데이트하는 로직으로 변경
         * 2018. 09. 04 jhjeong@finger.co.kr
         * 
         */
        //sb.append("\n delete from xnotimas ");
        sb.append("\n update xnotimas ");
        sb.append("\n set noti_can_yn = 'Y' ");
        sb.append("\n where notimascd = ? ");
        sb.append("\n and notimasst = 'PA02' ");
        sb.append("\n and chatrty = '03' ");

        log.debugln(sb.toString());
        log.debugln("xnotimas[notiMasCd]:"+notiMasCd);

        try {
            int pIdx = 0;
            pstmt   = conn.prepareStatement(sb.toString());
            pstmt.setString(++pIdx, notiMasCd);
            if (pstmt.executeUpdate() == 1) {
                result = true;
            }
        } catch (SQLException e) {
            log.println("-->exception발생 : "+sb.toString());
            throw e;
        }finally{
            sb.delete(0, sb.capacity());
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }

        return result;
    }

    private boolean DeleteNotiDet(String notiMasCd, String notiDetCd) throws Exception {
        log.debugln("Dao 청구서 상세 삭제");
        PreparedStatement pstmt = null;
        StringBuffer sb			= new StringBuffer();

        boolean result = false;

        /**
         * FIXME 청구삭제 요청은 청구상세테이블의 NOTI_CAN_YN=Y 로  업데이트하는 로직으로 변경
         * 2018. 09. 04 jhjeong@finger.co.kr
         * 
         */
        //sb.append("\n delete from xnotidet ");
        sb.append("\n update xnotidet ");
        sb.append("\n set noti_can_yn = 'Y' ");
        sb.append("\n where notimascd = ? ");
        sb.append("\n and notidetst = 'PA02' ");
        sb.append("\n and chatrty = '03' ");
        if(!"".equals(notiDetCd)){
            sb.append("\n and notidetcd = ? ");
        }

        log.debugln(sb.toString());
        log.debugln("xnotidet[notiDetCd]:"+notiDetCd);

        try {
            int pIdx = 0;
            pstmt   = conn.prepareStatement(sb.toString());
            pstmt.setString(++pIdx, notiMasCd);
            if (!"".equals(notiDetCd)){
                pstmt.setString(++pIdx, notiDetCd);
                log.debugln("[notiDetCd]:"+notiDetCd);
            }
            if (!"".equals(notiDetCd) && pstmt.executeUpdate() == 1) {
                result = true;
            }else if("".equals(notiDetCd) && pstmt.executeUpdate() >= 1){
                result = true;
            }

        } catch (SQLException e) {
            log.println("-->exception발생 : "+sb.toString());
            throw e;
        }finally{
            sb.delete(0, sb.capacity());
            try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {}
        }

        return result;
    }
}
