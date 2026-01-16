package damoa.comm.tele;

import damoa.Constants;
import damoa.comm.util.ComCheck;

public class HeadTele {

    /*
     * 헤더부 수신 체크
     * return boolean
     */
    public static boolean checkHeadTele(byte[] rcvHByte, String[] rcvHCheck, String paramProcDate,
                                        String custId, int msgSize) {

        rcvHCheck[0] = new String(rcvHByte,0,1,Constants.MY_CHARSET);
        rcvHCheck[1] = new String(rcvHByte,1,8,Constants.MY_CHARSET).trim();
        rcvHCheck[2] = new String(rcvHByte,45,6,Constants.MY_CHARSET).trim();

        String result_cd    = "H000";
        String result_msg   = "정상";
        boolean boolResult  = true;

        String HrecordType  = rcvHCheck[0];
        String serviceId    = rcvHCheck[1];
        String strRecordCnt = rcvHCheck[2];

        if (!ComCheck.chkRecordLen(rcvHByte, rcvHByte.length, msgSize)) {
            result_cd   = "H001";
            result_msg  = "전문길이오류["+rcvHByte.length+"]";
            boolResult  = false;
        } else if (!ComCheck.chkRecordCRLF(rcvHByte, rcvHByte.length, msgSize)) {
            result_cd   = "H002";
            result_msg  = "전문CRLF없음";
            boolResult  = false;
        } else if (!"H".equalsIgnoreCase(HrecordType)) {
            result_cd   = "H003";
            result_msg  = "레코드구분오류["+HrecordType+"]";
            boolResult  = false;
        } else if (!custId.equals(serviceId)) {
            result_cd   = "H004";
            result_msg  = "가맹점ID오류["+serviceId+"]";
            boolResult  = false;
        } else if ("".equals(strRecordCnt.trim())||!ComCheck.isAllNumber(strRecordCnt)||Integer.parseInt(strRecordCnt)==0
                ||Integer.parseInt(strRecordCnt)>300) {
            result_cd   = "H005";
            result_msg  = "신청건수오류["+strRecordCnt+"]";
            boolResult  = false;
        }

        rcvHCheck[3] = result_cd;
        rcvHCheck[4] = ComCheck.makeSpaceData(result_msg,30);

        return boolResult;
    }

    /* 회원 헤더부 작성
     *
     * return String
     */
    public static String makeHeadTele(byte[] rcvHByte, String[] rcvHCheck, boolean headYN, int msgSize) {

        String sHeadYN = "N";
        if (headYN) sHeadYN = "Y";

        String result_cd    = rcvHCheck[3];
        String result_msg   = rcvHCheck[4];

        return new String(rcvHByte,0,10,Constants.MY_CHARSET) + sHeadYN + result_cd + result_msg +
                new String(rcvHByte,45,msgSize-47,Constants.MY_CHARSET) + "\r\n";
    }
}
