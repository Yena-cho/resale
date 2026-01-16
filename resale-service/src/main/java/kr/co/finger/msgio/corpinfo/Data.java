package kr.co.finger.msgio.corpinfo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 기관 연동 전문 본문
 *
 * @author lloydkwon@gmail.com
 * @author wisehouse@finger.co.kr
 */
public class Data {
    /**
     * 데이터 구분
     */
    private String kubun;

    /**
     * 기관코드
     */
    private String corpCode;
    
    /**
     * 기관(업체)명
     */
    private String corpName;

    /**
     * 삭제 여부
     */
    private String deleteYn;

    /**
     * 중계 기관 코드
     */
    private String relayCorpCode;

    /**
     * 기관 신규일자
     */
    private String createDt;
    
    /**
     * 사업자등록번호
     */
    private String bizNo;
    
    /**
     * 기관상태
     */
    private String corpStatus;

    /**
     * 대리점코드1
     */
    private String agencyCode1;
    /**
     * 대리점명1
     */
    private String agencyName1;
    /**
     * 대리점상태1
     */
    private String agencyStatus1;
    
    /**
     * 대리점코드2
     */
    private String agencyCode2;
    /**
     * 대리점명2
     */
    private String agencyName2;
    /**
     * 대리점상태2
     */
    private String agencyStatus2;
    
    /**
     * 대리점코드3
     */
    private String agencyCode3;
    /**
     * 대리점명3
     */
    private String agencyName3;
    /**
     * 대리점상태3
     */
    private String agencyStatus3;
    
    /**
     * 대리점코드4
     */
    private String agencyCode4;
    /**
     * 대리점명4
     */
    private String agencyName4;
    /**
     * 대리점상태4
     */
    private String agencyStatus4;
    
    /**
     * 대리점코드5
     */
    private String agencyCode5;
    /**
     * 대리점명5
     */
    private String agencyName5;
    /**
     * 대리점상태5
     */
    private String agencyStatus5;
    
    /**
     * 대리점코드6
     */
    private String agencyCode6;
    /**
     * 대리점명6
     */
    private String agencyName6;
    /**
     * 대리점상태6
     */
    private String agencyStatus6;
    
    /**
     * 대리점코드7
     */
    private String agencyCode7;
    /**
     * 대리점명7
     */
    private String agencyName7;
    /**
     * 대리점상태7
     */
    private String agencyStatus7;
    
    /**
     * 대리점코드8
     */
    private String agencyCode8;
    /**
     * 대리점명8
     */
    private String agencyName8;
    /**
     * 대리점상태8
     */
    private String agencyStatus8;
    
    /**
     * 대리점코드9
     */
    private String agencyCode9;
    /**
     * 대리점명9
     */
    private String agencyName9;
    /**
     * 대리점상태9
     */
    private String agencyStatus9;
    
    /**
     * 대리점코드10
     */
    private String agencyCode10;
    /**
     * 대리점명10
     */
    private String agencyName10;
    /**
     * 대리점상태10
     */
    private String agencyStatus10;
    
    /**
     * 입금수수료
     */
    private String feeAmount;
    /**
     * 수수료배분율
     */
    private String feeRate;
    /**
     * 지점번호
     */
    private String branchCode;
    
    private String filler;

    public String getKubun() {
        return kubun;
    }

    public void setKubun(String kubun) {
        this.kubun = kubun;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(String deleteYn) {
        this.deleteYn = deleteYn;
    }

    public String getRelayCorpCode() {
        return relayCorpCode;
    }

    public void setRelayCorpCode(String relayCorpCode) {
        this.relayCorpCode = relayCorpCode;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public String getCorpStatus() {
        return corpStatus;
    }

    public void setCorpStatus(String corpStatus) {
        this.corpStatus = corpStatus;
    }

    public String getAgencyCode1() {
        return agencyCode1;
    }

    public void setAgencyCode1(String agencyCode1) {
        this.agencyCode1 = agencyCode1;
    }

    public String getAgencyName1() {
        return agencyName1;
    }

    public void setAgencyName1(String agencyName1) {
        this.agencyName1 = agencyName1;
    }

    public String getAgencyStatus1() {
        return agencyStatus1;
    }

    public void setAgencyStatus1(String agencyStatus1) {
        this.agencyStatus1 = agencyStatus1;
    }

    public String getAgencyCode2() {
        return agencyCode2;
    }

    public void setAgencyCode2(String agencyCode2) {
        this.agencyCode2 = agencyCode2;
    }

    public String getAgencyName2() {
        return agencyName2;
    }

    public void setAgencyName2(String agencyName2) {
        this.agencyName2 = agencyName2;
    }

    public String getAgencyStatus2() {
        return agencyStatus2;
    }

    public void setAgencyStatus2(String agencyStatus2) {
        this.agencyStatus2 = agencyStatus2;
    }

    public String getAgencyCode3() {
        return agencyCode3;
    }

    public void setAgencyCode3(String agencyCode3) {
        this.agencyCode3 = agencyCode3;
    }

    public String getAgencyName3() {
        return agencyName3;
    }

    public void setAgencyName3(String agencyName3) {
        this.agencyName3 = agencyName3;
    }

    public String getAgencyStatus3() {
        return agencyStatus3;
    }

    public void setAgencyStatus3(String agencyStatus3) {
        this.agencyStatus3 = agencyStatus3;
    }

    public String getAgencyCode4() {
        return agencyCode4;
    }

    public void setAgencyCode4(String agencyCode4) {
        this.agencyCode4 = agencyCode4;
    }

    public String getAgencyName4() {
        return agencyName4;
    }

    public void setAgencyName4(String agencyName4) {
        this.agencyName4 = agencyName4;
    }

    public String getAgencyStatus4() {
        return agencyStatus4;
    }

    public void setAgencyStatus4(String agencyStatus4) {
        this.agencyStatus4 = agencyStatus4;
    }

    public String getAgencyCode5() {
        return agencyCode5;
    }

    public void setAgencyCode5(String agencyCode5) {
        this.agencyCode5 = agencyCode5;
    }

    public String getAgencyName5() {
        return agencyName5;
    }

    public void setAgencyName5(String agencyName5) {
        this.agencyName5 = agencyName5;
    }

    public String getAgencyStatus5() {
        return agencyStatus5;
    }

    public void setAgencyStatus5(String agencyStatus5) {
        this.agencyStatus5 = agencyStatus5;
    }

    public String getAgencyCode6() {
        return agencyCode6;
    }

    public void setAgencyCode6(String agencyCode6) {
        this.agencyCode6 = agencyCode6;
    }

    public String getAgencyName6() {
        return agencyName6;
    }

    public void setAgencyName6(String agencyName6) {
        this.agencyName6 = agencyName6;
    }

    public String getAgencyStatus6() {
        return agencyStatus6;
    }

    public void setAgencyStatus6(String agencyStatus6) {
        this.agencyStatus6 = agencyStatus6;
    }

    public String getAgencyCode7() {
        return agencyCode7;
    }

    public void setAgencyCode7(String agencyCode7) {
        this.agencyCode7 = agencyCode7;
    }

    public String getAgencyName7() {
        return agencyName7;
    }

    public void setAgencyName7(String agencyName7) {
        this.agencyName7 = agencyName7;
    }

    public String getAgencyStatus7() {
        return agencyStatus7;
    }

    public void setAgencyStatus7(String agencyStatus7) {
        this.agencyStatus7 = agencyStatus7;
    }

    public String getAgencyCode8() {
        return agencyCode8;
    }

    public void setAgencyCode8(String agencyCode8) {
        this.agencyCode8 = agencyCode8;
    }

    public String getAgencyName8() {
        return agencyName8;
    }

    public void setAgencyName8(String agencyName8) {
        this.agencyName8 = agencyName8;
    }

    public String getAgencyStatus8() {
        return agencyStatus8;
    }

    public void setAgencyStatus8(String agencyStatus8) {
        this.agencyStatus8 = agencyStatus8;
    }

    public String getAgencyCode9() {
        return agencyCode9;
    }

    public void setAgencyCode9(String agencyCode9) {
        this.agencyCode9 = agencyCode9;
    }

    public String getAgencyName9() {
        return agencyName9;
    }

    public void setAgencyName9(String agencyName9) {
        this.agencyName9 = agencyName9;
    }

    public String getAgencyStatus9() {
        return agencyStatus9;
    }

    public void setAgencyStatus9(String agencyStatus9) {
        this.agencyStatus9 = agencyStatus9;
    }

    public String getAgencyCode10() {
        return agencyCode10;
    }

    public void setAgencyCode10(String agencyCode10) {
        this.agencyCode10 = agencyCode10;
    }

    public String getAgencyName10() {
        return agencyName10;
    }

    public void setAgencyName10(String agencyName10) {
        this.agencyName10 = agencyName10;
    }

    public String getAgencyStatus10() {
        return agencyStatus10;
    }

    public void setAgencyStatus10(String agencyStatus10) {
        this.agencyStatus10 = agencyStatus10;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(String feeRate) {
        this.feeRate = feeRate;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
