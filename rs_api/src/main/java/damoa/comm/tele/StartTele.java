package damoa.comm.tele;

import damoa.Constants;
import damoa.comm.db.DBExec;
import damoa.comm.log.GeneLog;
import damoa.comm.util.ComCheck;
import damoa.comm.util.FileCheck;

import java.sql.Connection;

public class StartTele {

    public static boolean checkStartTele(byte[] rcvSByte, String[] rcvSCheck, int msgsize, GeneLog log,
                                         boolean testFlag, Connection conn) {

        rcvSCheck[0]    = new String(rcvSByte,0,1, Constants.MY_CHARSET).trim();      // Record구분
        rcvSCheck[1]    = new String(rcvSByte,1,8, Constants.MY_CHARSET).trim();      // 가맹점코드
        rcvSCheck[2]    = new String(rcvSByte,45,3, Constants.MY_CHARSET).trim();     // DATA구분
        rcvSCheck[3]    = new String(rcvSByte,48,1, Constants.MY_CHARSET).trim();     // 작업구분
        rcvSCheck[4]    = new String(rcvSByte,49,8, Constants.MY_CHARSET).trim();     // 처리일자
        rcvSCheck[5]    = new String(rcvSByte,57,6, Constants.MY_CHARSET).trim();     // 처리순번

        String result_cd    = "S000";
        String result_msg   = "정상";
        boolean boolResult  = true;

        DBExec dbe   = new DBExec(log, conn);

        String SrecordType  = rcvSCheck[0];
        String serviceId    = rcvSCheck[1];
        String dataField    = rcvSCheck[2];
        String workFiled    = rcvSCheck[3];
        String payDT        = rcvSCheck[4];
        String paySeq       = rcvSCheck[5];

        if (!"S".equalsIgnoreCase(SrecordType)) {
            result_cd   = "S003";
            result_msg  = "레코드구분오류["+SrecordType+"]";
            boolResult  = false;
        } else if (!("PAY".equalsIgnoreCase(dataField)||"RCP".equalsIgnoreCase(dataField))) {
            result_cd   = "S004";
            result_msg  = "데이타구분오류["+dataField+"]";
            boolResult  = false;
        } else if (!ComCheck.chkRecordLen(rcvSByte, rcvSByte.length, msgsize)) {
            result_cd   = "S001";
            result_msg  = "전문길이오류["+rcvSByte.length+"]";
            boolResult  = false;
        } else if (!ComCheck.chkRecordCRLF(rcvSByte, rcvSByte.length, msgsize)) {
            result_cd   = "S002";
            result_msg  = "전문CRLF없음";
            boolResult  = false;
        } else if (!("A".equalsIgnoreCase(workFiled)||"R".equalsIgnoreCase(workFiled))) {
            result_cd   = "S005";
            result_msg  = "작업구분오류["+workFiled+"]";
            boolResult  = false;
        } else if (("PAY".equalsIgnoreCase(dataField)&&"R".equalsIgnoreCase(workFiled))||
                ("RCP".equalsIgnoreCase(dataField)&&"A".equalsIgnoreCase(workFiled))) {
            result_cd   = "S004";
            result_msg  = "데이타구분오류["+dataField+"]["+workFiled+"]";
            boolResult  = false;
        } else if ("".equals(payDT)) {
            result_cd   = "S006";
            result_msg  = "처리일자오류["+payDT+"]";
            boolResult  = false;
        } else if (!ComCheck.isDate(payDT)) {
            result_cd   = "S006";
            result_msg  = "처리일자오류["+payDT+"]";
            boolResult  = false;
        } else if (!ComCheck.isProcDate(workFiled, payDT)) {
            result_cd   = "S007";
            result_msg  = "처리일자무효["+workFiled+"]["+payDT+"]";
            boolResult  = false;
        } else if ("A".equalsIgnoreCase(workFiled)&&"".equals(paySeq)) {
            result_cd   = "S008";
            result_msg  = "처리순번오류["+workFiled+"]["+paySeq+"]";
            boolResult  = false;
        } else if (!"".equals(paySeq)&&(!ComCheck.isAllNumber(paySeq) || 6!=paySeq.length()) ) {
            result_cd   = "S008";
            result_msg  = "처리순번오류["+paySeq+"]";
            boolResult  = false;
        } else if (FileCheck.isRecvFile(serviceId, payDT, paySeq, dataField, log,testFlag)) {
            result_cd   = "S020";
            result_msg  = "데이터처리중["+paySeq+"]";
            boolResult  = false;
        } else if ("A".equalsIgnoreCase(workFiled)&&
                FileCheck.isDoneFile(serviceId, payDT, paySeq, dataField, log,testFlag)) {
            result_cd   = "S017";
            result_msg  = "기처리데이터["+paySeq+"]";
            boolResult  = false;
        }

        if (boolResult) {
            int sresult_no = dbe.chkCha(serviceId);
            if (sresult_no==1) {
                result_cd   = "S009";
                result_msg  = "가맹점ID오류["+serviceId+"]";
                boolResult  = false;
            } else if (sresult_no==2) {
                result_cd   = "S010";
                result_msg  = "삭제된 가맹점ID["+serviceId+"]";
                boolResult  = false;
            } else if (sresult_no==3) {
                result_cd   = "S011";
                result_msg  = "정지된 가맹점ID["+serviceId+"]";
                boolResult  = false;
            } else if (sresult_no==4) {
                result_cd   = "S012";
                result_msg  = "대기중 가맹점ID["+serviceId+"]";
                boolResult  = false;
            } else if (sresult_no==5) {
                result_cd   = "S013";
                result_msg  = "연동기관아님 ID["+serviceId+"]";
                boolResult  = false;
            }
        }

        rcvSCheck[6] = result_cd;
        rcvSCheck[7] = ComCheck.makeSpaceData(result_msg,30);

        return boolResult;
    }

    public static String makeStartTele(byte[] rcvSByte, String[] rcvSCheck, boolean startYN) {

        String sStartYN = "N";
        if (startYN) sStartYN = "Y";

        String result_cd    = rcvSCheck[6];
        String result_msg   = rcvSCheck[7];

        return new String(rcvSByte,0,10, Constants.MY_CHARSET) + sStartYN + result_cd + result_msg + new String(rcvSByte,45,18, Constants.MY_CHARSET) +
                ComCheck.makeSpaceData("",635) + "\r\n";
    }
}
