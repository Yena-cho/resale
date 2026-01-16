package com.finger.shinhandamoa.home.dto;


/**
 * @author by puki
 * @date 2018. 4. 28.
 * @desc 최초생성
 */
public class LoginDTO {

    // 로그인정보
    private String loginId;
    private String chaCd;
    private String loginPw;
    private String idType;
    private String loginName;
    private String idSt;
    private int failCnt;
    private String disabledYn;
    private String loginKey;
    private String email;
    private String userId;
    private String nowPw;

    // 기관정보
    private String feeAccNo;
    private String feeOffNo;
    private String chaOffNo;
    private String chrHp;
    private String chaSvcYn;

    // 자동출금 관련 기관정보
    private String chaName;

    private String feeVano;
    private String feeBankCd;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getChaCd() {
        return chaCd;
    }

    public void setChaCd(String chaCd) {
        this.chaCd = chaCd;
    }

    public String getLoginPw() {
        return loginPw;
    }

    public void setLoginPw(String loginPw) {
        this.loginPw = loginPw;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getIdSt() {
        return idSt;
    }

    public void setIdSt(String idSt) {
        this.idSt = idSt;
    }

    public int getFailCnt() {
        return failCnt;
    }

    public void setFailCnt(int failCnt) {
        this.failCnt = failCnt;
    }

    public String getDisabledYn() {
        return disabledYn;
    }

    public void setDisabledYn(String disabledYn) {
        this.disabledYn = disabledYn;
    }

    public String getLoginKey() {
        return loginKey;
    }

    public void setLoginKey(String loginKey) {
        this.loginKey = loginKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFeeAccNo() {
        return feeAccNo;
    }

    public void setFeeAccNo(String feeAccNo) {
        this.feeAccNo = feeAccNo;
    }

    public String getFeeOffNo() {
        return feeOffNo;
    }

    public void setFeeOffNo(String feeOffNo) {
        this.feeOffNo = feeOffNo;
    }

    public String getChaOffNo() {
        return chaOffNo;
    }

    public void setChaOffNo(String chaOffNo) {
        this.chaOffNo = chaOffNo;
    }

    public String getChrHp() {
        return chrHp;
    }

    public void setChrHp(String chrHp) {
        this.chrHp = chrHp;
    }

    public String getChaSvcYn() {
        return chaSvcYn;
    }

    public void setChaSvcYn(String chaSvcYn) {
        this.chaSvcYn = chaSvcYn;
    }

    public String getNowPw() {
        return nowPw;
    }

    public void setNowPw(String nowPw) {
        this.nowPw = nowPw;
    }


    public String getChaName() {
        return chaName;
    }

    public void setChaName(String chaName) {
        this.chaName = chaName;
    }

    public String getFeeVano() {
        return feeVano;
    }

    public void setFeeVano(String feeVano) {
        this.feeVano = feeVano;
    }

    public String getFeeBankCd() {
        return feeBankCd;
    }

    public void setFeeBankCd(String feeBankCd) {
        this.feeBankCd = feeBankCd;
    }
}
