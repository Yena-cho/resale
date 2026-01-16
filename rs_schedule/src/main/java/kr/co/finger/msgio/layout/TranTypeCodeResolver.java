package kr.co.finger.msgio.layout;

/**
 * 전문에서 전문의 종류를 추출한다.
 * 전문프로토콜마다 다르므로 추출룰을 적용하게 함.
 */
public interface TranTypeCodeResolver {
    public String getTranTypeCode(String msg);
}
