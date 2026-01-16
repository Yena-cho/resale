package kr.co.finger.damoa.shinhan.agent.domain.model;

import kr.co.finger.shinhandamoa.data.table.model.Xchalist;
import org.apache.commons.lang3.StringUtils;

/**
 * 기관 도메인
 * 
 * @author wisehouse@finger.co.kr
 */
public class ChaDO extends DomainObjectTemplate {
    private Xchalist xchalist;

    public ChaDO(Xchalist xchalist) {
        this.xchalist = xchalist;
    }

    /**
     * 기관코드 조회
     * 
     */
    public String getId() {
        return xchalist.getChacd();
    }

    /**
     * 기관 상태 조회
     * 
     */
    public String getStatus() {
        return xchalist.getChast();
    }

    /**
     * 금액 체크 가능 여부 조회
     * 
     */
    public boolean isEnableValidateAmount() {
        return StringUtils.equals(xchalist.getAmtchkty(), "Y");
    }

    /**
     * 부분납 가능 여부 조회
     * 
     */
    public boolean isEnablePartialPayment() {
        return StringUtils.equals(xchalist.getPartialPayment(), "Y");
    }

    /**
     * 기관명 조회
     */
    public String getName() {
        return xchalist.getChaname();
    }

    /**
     * 수취인명 방식
     */
    public String getAccountDisplayNameType() {
        return xchalist.getCusnamety();
    }

    /**
     * 다계좌 기관 여부
     * @return
     */
    public boolean hasMultipleAccount() {
        return StringUtils.equals(xchalist.getAdjaccyn(), "Y");
    }

    /**
     * 납부기간 확인여부
     * @return
     */
    public boolean isEnableValidatePeriod() {
        return StringUtils.equals(xchalist.getRcpDueChk(), "Y");
    }

    /**
     * 접속방식 코드
     * @return
     */
    public String getAccessTypeCode() {
        return xchalist.getChatrty();
    }

    /**
     * 현금영수증 자동발급여부
     * @return
     */
    public boolean isEnableAutomaticCashReceipt() {
        return StringUtils.equals(xchalist.getRcpreqyn(), "Y") && StringUtils.equals(xchalist.getRcpreqty(), "A");
    }

    /**
     * 사업자등록번호
     * @return
     */
    public String getIndentityNo() {
        return xchalist.getChaoffno();
    }

    /**
     * 세금면제여부
     * @return
     */
    public boolean isNoTax() {
        return StringUtils.equals(xchalist.getNotaxyn(), "N");
    }

    /**
     * 현금영수증 발급여부
     * @return
     */
    public boolean isEnableCashReceipt() {
        return StringUtils.equals(xchalist.getRcpreqyn(), "Y");
    }

    /**
     * 현금영수증 의무발행기관 여부
     * @return
     */
    public String getMandRcpYn() {
        return xchalist.getMandRcpYn();
    }

    /**
     * 기관 수납옵션 확인 amtchkty
     */
    public String getAmtchkty() {
        return xchalist.getAmtchkty();
    }

    /**
     * 기관 수납 1회 한도
     */
    public String getLimitOnce() {
        return xchalist.getLimitOnce();
    }

    /**
     * 기관 수납 1일 한도
     */
    public String getLimitDay() {
        return xchalist.getLimitDay();
    }

    /**
     * 기관 수납 1일 횟수
     */
    public String getLimitDayCnt() {
        return xchalist.getLimitDayCnt();
    }

    /**
     * 은행의 수취조회요청을 전송할 기관(고객사)의 API url
     */
    public String getChaApiUrlQuery() {
        return xchalist.getChaApiUrlQuery();
    }

    /**
     * 은행의 입금통지요청을 전송할 기관(고객사)의 API url
     */
    public String getChaApiUrlNotice() {
        return xchalist.getChaApiUrlNotice();
    }
}
