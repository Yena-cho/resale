package kr.co.finger.damoa.model.msg;

public class MessageCode {
    /**
     * 관리요청전문
     */
    public static final Code ADMIN_REQUEST = new Code("08001100");

    /**
     * 관리응답전문
     */
    public static final Code ADMIN_RESPONSE = new Code("08101100");
    /**
     * 입금이체통지요청전문
     */
    public static final Code TRANSFER_MONEY_NOTICE_REQUEST = new Code("02001100");
    /**
     * 입금이체통지응답전문
     */
    public static final Code TRANSFER_MONEY_NOTICE_RESOPONSE = new Code("02101100");
    /**
     * 입금이체통지취소요청전문
     */
    public static final Code TRANSFER_MONEY_NOTICE_CANCEL_REQUEST = new Code("04001100");
    /**
     * 입금이체통지취소응답전문
     */
    public static final Code TRANSFER_MONEY_NOTICE_CANCEL_RESPONSE = new Code("04101100");
    /**
     * 입금통지요청전문
     */
    public static final Code DEPOSIT_NOTICE_REQUEST = new Code("02003110");
    /**
     * 입금통지응답전문
     */
    public static final Code DEPOSIT_NOTICE_RESOPONSE = new Code("02103110");
    /**
     * 입금통지취소요청전문
     */
    public static final Code DEPOSIT_NOTICE_CANCEL_REQUEST = new Code("04003110");
    /**
     * 입금통지취소응답전문
     */
    public static final Code DEPOSIT_NOTICE_CANCEL_RESPONSE = new Code("04103110");
    /**
     * 계좌수취조회요청전문
     */
    public static final Code RECEIPT_INQUIRY_REQUEST = new Code("02004110");
    /**
     * 계좌수취조회응답전문
     */
    public static final Code RECEIPT_INQUIRY_RESPONSE = new Code("02104110");
//    /**
//     * 조회용청전문
//     */
//    public static final Code LOOKUP_REQUEST = new Code("02004210");
//    /**
//     * 조회응답전문
//     */
//    public static final Code LOOKUP_RESPONSE = new Code("02104210");
    /**
     * 조회용청전문
     */
    public static final Code RANGE_ACCOUNT_REQUEST = new Code("02006320");
    /**
     * 조회응답전문
     */
    public static final Code RANGE_ACCOUNT_RESPONSE = new Code("02106320");

    public static final Code AGGREGATE_REQUEST = new Code("07001100");
    public static final Code AGGREGATE_RESPONSE = new Code("07101100");


    public static final Code ADMIN_START = new Code("001");
    public static final Code ADMIN_RESTART = new Code("002");
    public static final Code ADMIN_FINISH_NOTICE = new Code("003");
    public static final Code ADMIN_FINISH = new Code("004");
    public static final Code ADMIN_DISABILITY = new Code("005");
    public static final Code ADMIN_DISABILITY_RECOVERY = new Code("006");
    public static final Code ADMIN_TEST_CALL = new Code("007");


    public static final Code NOTICE_TYPE_RESPONSE = new Code("0210");
    public static final Code NOTICE_TYPE_REQUEST = new Code("0200");
    public static final Code NOTICE_CANCEL_TYPE_REQEUEST = new Code("0400");
    public static final Code NOTICE_CANCEL_TYPE_RESPONSE = new Code("0410");
    public static final Code AGGREGATE_TYPE_RESPONSE = new Code("0710");
    public static final Code AGGREGATE_TYPE_REQUEST = new Code("0700");
    public static final Code ADMIN_TYPE_RESPONSE = new Code("0810");

    public static final Code POLLING_TYPE_REQUEST = new Code("HDRREQPOLL");
    public static final Code POLLING_TYPE_RESPONSE = new Code("HDRRESPOLL");


}
