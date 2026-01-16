package kr.co.finger.shinhandamoa.constants;

/**
 * 고지서 신청서 처리 상태
 *
 * @author wisehouse@finger.co.kr
 */
public class InvoiceApplyProcessStatus {
    /**
     * 생성
     */
    public static final String CREATED = "N10000";

    /**
     * 대기
     */
    public static final String STAND_BY = "N11000";

    /**
     * 처리 중
     */
    public static final String WIP = "N12000";

    /**
     * 성공
     */
    public static final String SUCCESS = "N13000";

    /**
     * 실패 - 일반
     */
    public static final String FAIL = "N14000";

    /**
     * 실패 - 다모아
     */
    public static final String FAIL_AT_DAMOA = "N14100";

    /**
     * 실패 - 파일 날짜 없음
     */
    public static final String FAIL_AS_NO_FILE_DT = "N14101";

    /**
     * 실패 - 파일 번호 없음
     */
    public static final String FAIL_AS_NO_FILE_NO = "N14102";

    /**
     * 실패 - 비더빌텍
     */
    public static final String FAIL_AT_BBTEC = "N14200";

    /**
     * 실패 - 데이터 없음
     */
    public static final String FAIL_AS_NO_DATA = "N14301";
}
