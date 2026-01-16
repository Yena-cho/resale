package kr.co.finger.damoa.shinhan.agent.model;

/**
 * 취소 스케줄러 대기얼 데이터
 *
 * @author wisehouse@finger.co.kr
 */
public class CancelMessageBean implements MessageBean {
    private String rcpMasCd;
    private String notiMasCd;

    public CancelMessageBean(String rcpMasCd, String notiMasCd) {
        this.rcpMasCd = rcpMasCd;
        this.notiMasCd = notiMasCd;
    }

    public String getRcpMasCd() {
        return rcpMasCd;
    }

    public String getNotiMasCd() {
        return notiMasCd;
    }
}
