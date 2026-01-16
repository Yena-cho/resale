package damoa.comm.tele;

import damoa.Constants;
import damoa.comm.log.GeneLog;
import damoa.comm.util.ComCheck;

public class TailTele {

    /* 회원 테일전문체크
     *
     * return boolean
     */
    public static boolean checkTailTele(byte[] rcvTByte, String[] rcvTCheck,
                                        String logFileID, int msgSize, GeneLog gLog, boolean testFlag) {

        rcvTCheck[0] = new String(rcvTByte,0,1, Constants.MY_CHARSET);
        rcvTCheck[1] = new String(rcvTByte,1,8, Constants.MY_CHARSET).trim();
        rcvTCheck[2] = new String(rcvTByte,45,6, Constants.MY_CHARSET).trim();

        String result_cd = "T000";
        String result_msg = "정상";
        boolean boolResult = true;

        String TrecordType  = rcvTCheck[0];
        String serviceId    = rcvTCheck[1];
        String strRecordCnt = rcvTCheck[2];

        if (!ComCheck.chkRecordLen(rcvTByte, rcvTByte.length, msgSize)) {
            result_cd = "T001";
            result_msg = "전문길이오류["+rcvTByte.length+"]";
            boolResult = false;
        } else if (!ComCheck.chkRecordCRLF(rcvTByte, rcvTByte.length, msgSize)) {
            result_cd = "T002";
            result_msg = "전문CRLF없음";
            boolResult = false;
        } else if (!"T".equalsIgnoreCase(TrecordType)) {
            result_cd = "T003";
            result_msg = "레코드구분오류["+TrecordType+"]";
            boolResult = false;
        } else if ("".equals(strRecordCnt.trim())||!ComCheck.isAllNumber(strRecordCnt)) {
            result_cd   = "T005";
            result_msg  = "신청건수오류["+strRecordCnt+"]";
            boolResult  = false;
        } else if (!logFileID.equals(serviceId)) {
            result_cd = "T004";
            result_msg = "가맹점ID오류["+serviceId+"]";
            boolResult  = false;
        }

        rcvTCheck[3] = result_cd;
        rcvTCheck[4] = ComCheck.makeSpaceData(result_msg,30);

        return boolResult;
    }

    /* 회원,출금 테일전문작성
     *
     * return boolean
     */
    public static String makeTailTele(byte[] rcvTByte, String[] rcvTCheck, boolean tailYN, int okCnt,
                                      int msgSize) {

        String sTeadYN = "N";
        if (tailYN) sTeadYN = "Y";

        String result_cd    = rcvTCheck[3];
        String result_msg   = rcvTCheck[4];

        return new String(rcvTByte,0,10, Constants.MY_CHARSET) + sTeadYN + result_cd + result_msg + new String(rcvTByte,45,6, Constants.MY_CHARSET)
                + ComCheck.makeSpaceData(String.valueOf(okCnt),6) + new String(rcvTByte,57,msgSize-59, Constants.MY_CHARSET) + "\r\n";
    }
}
