package com.finger.damoa.core.exception.constant;

/**
 * 통신 결과 기본 상수 (OK, FAIL을 제외하고는 messages.properties 사용)
 * @author volka
 */
public enum ResultCode {
    OK("OK", "정상 처리 되었습니다."),
    FAIL("COE00000", "오류가 발생하였습니다."),

    /*스크래핑 코드*/
    SCRAP_SUCCESS_CD("S000000", "스크래핑 성공")
    ;

    private final String code;
    private final String message;
//    private final String contentMsg;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
//        this.contentMsg = contentMsg;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

//    public String getContentMsg() {
//        return contentMsg;
//    }
}
