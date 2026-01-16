package com.finger.shinhandamoa.org.receiptmgmt.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FwCashReceiptMasterDTO {
    private String id;
    private String clientTypeCode;
    private String txTypeCode;
    private String dataNo;
    private String mediaTypeCode;
    private String hostIdentityNo;
    private String clientIdentityNo;
    private String clientNo;
    private String txDate;
    private String posEntry;
    private String txNo;
    private String cancelTxNo;
    private Long txAmount;
    private Long tip;
    private Long tax;
    private String cancelTxDate;
    private String currencyCode;
    private String responseCode;
    private String userDefine;
    private String statusCd;
    private String requestDate;
    private String responseDate;
    private String createDate;
    private String createUser;

    private String responseMessage;
    private String cashMasCd;
    private String job;

    public String getCashMasCd() {
        return cashMasCd;
    }

    public void setCashMasCd(String cashMasCd) {
        this.cashMasCd = cashMasCd;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setResponseMessage(String responseCode) {
        this.responseMessage = getResponseMessage(responseCode);
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    private String getResponseMessage(String responseCode) {
        switch (responseCode) {
            case "8003":
                return "전문 오류";
            case "4949":
                return "1분후재조회요망";
            case "7003":
                return "미등록 단말기";
            case "8700":
                return "B/L 등록 가맹점";
            case "8009":
                return "금액 오류 미달";
            case "8037":
                return "고객 번호 오류";
            case "0814":
                return "금액    부적당";
            case "8373":
                return "금액 오류 초과";
            case "8370":
                return "포인트 별도 거래요망";
            case "TS1":
                return "신분확인정보(주민등록번호/사업자번호/휴대전화번호) 자릿수 오류";
            case "TS2":
                return "카드정보 자릿수 오류";
            case "TS3":
                return "불성실/위장/휴폐업/제외업종사업자와 거래한 현금결제내역";
            case "TS4":
                return "매출금액 입력오류";
            case "TS5":
                return "승인번호 중복 및 오류";
            case "TS6":
                return "매출일자 오류";
            case "TS7":
                return "가맹점 사업자등록번호 미등록 오류";
            case "TS8":
                return "Layout 오류(Record 항목별 입력 값 오류)";
            case "0000":
                return "정상";
            default:
                return "기타 오류";
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientTypeCode() {
        return clientTypeCode;
    }

    public void setClientTypeCode(String clientTypeCode) {
        this.clientTypeCode = clientTypeCode;
    }

    public String getTxTypeCode() {
        return txTypeCode;
    }

    public void setTxTypeCode(String txTypeCode) {
        this.txTypeCode = txTypeCode;
    }

    public String getDataNo() {
        return dataNo;
    }

    public void setDataNo(String dataNo) {
        this.dataNo = dataNo;
    }

    public String getMediaTypeCode() {
        return mediaTypeCode;
    }

    public void setMediaTypeCode(String mediaTypeCode) {
        this.mediaTypeCode = mediaTypeCode;
    }

    public String getHostIdentityNo() {
        return hostIdentityNo;
    }

    public void setHostIdentityNo(String hostIdentityNo) {
        this.hostIdentityNo = hostIdentityNo;
    }

    public String getClientIdentityNo() {
        return clientIdentityNo;
    }

    public void setClientIdentityNo(String clientIdentityNo) {
        this.clientIdentityNo = clientIdentityNo;
    }

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public String getTxDate() {
        return txDate;
    }

    public void setTxDate(String txDate) {
        this.txDate = txDate;
    }

    public String getPosEntry() {
        return posEntry;
    }

    public void setPosEntry(String posEntry) {
        this.posEntry = posEntry;
    }

    public String getTxNo() {
        return txNo;
    }

    public void setTxNo(String txNo) {
        this.txNo = txNo;
    }

    public String getCancelTxNo() {
        return cancelTxNo;
    }

    public void setCancelTxNo(String cancelTxNo) {
        this.cancelTxNo = cancelTxNo;
    }

    public Long getTxAmount() {
        return txAmount;
    }

    public void setTxAmount(Long txAmount) {
        this.txAmount = txAmount;
    }

    public Long getTip() {
        return tip;
    }

    public void setTip(Long tip) {
        this.tip = tip;
    }

    public Long getTax() {
        return tax;
    }

    public void setTax(Long tax) {
        this.tax = tax;
    }

    public String getCancelTxDate() {
        return cancelTxDate;
    }

    public void setCancelTxDate(String cancelTxDate) {
        this.cancelTxDate = cancelTxDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getUserDefine() {
        return userDefine;
    }

    public void setUserDefine(String userDefine) {
        this.userDefine = userDefine;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(String responseDate) {
        this.responseDate = responseDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}
