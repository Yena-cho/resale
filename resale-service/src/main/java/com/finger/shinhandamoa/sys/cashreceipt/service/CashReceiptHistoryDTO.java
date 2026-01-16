package com.finger.shinhandamoa.sys.cashreceipt.service;

import kr.co.finger.shinhandamoa.common.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.method.P;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 현금영수증 이용내역 DTO (현금영수증 발급,요청 내역 포함)
 * 화면에 보여주거나 필요한 값만
 * @author denny91@finger.co.kr
 */
public class CashReceiptHistoryDTO {
//    private String rn;
    private String chaCd;
    private String chaName;
    private String noTaxYn;
    private String payDay;
    private String txDate;
    private String txNo;
    private String txTypeCode;
    private String cancelTxDate;
    private String cancelTxNo;
    private String hostIdentityNo; //chaoffno
    private String clientIdentityNo; //cusoffno
    private String clientIdentityNo2; //cusoffno2
    private String cusName;
    private String rcpusrName;
    private Long txAmount;
    private Long tax;
    private Long taxFreeAmount;
    private String svecd;
    private String svecdNm;
    private String requestTypeCd;
    private String resultCd;
    private String reqStatus;
    private Date requestDate;
    private String requestDt;
    private String requestUser;
    private String responseCode;
    private String responseMessage;
    private String cashMasCd;

//    public String getRn() {
//        return rn;
//    }
//
//    public void setRn(String rn) {
//        this.rn = rn;
//    }

    public String getCashMasCd() {
        return cashMasCd;
    }

    public void setCashMasCd(String cashMasCd) {
        this.cashMasCd = cashMasCd;
    }

    public String getRequestDt() {
        return requestDt;
    }

    public void setRequestDt(String requestDt) {
            this.requestDt = requestDt;
    }

    public String getSvecdNm() {
        return svecdNm;
    }

    public void setSvecdNm(String svecdNm) {
        this.svecdNm = svecdNm;
    }

    public Long getTaxFreeAmount() {
        return taxFreeAmount;
    }

    public void setTaxFreeAmount(Long txAmount) {
            this.taxFreeAmount = txAmount;
    }

    public String getReqStatus() {
        return reqStatus;
    }

    public void setReqStatus(String reqStatus) {
        this.reqStatus = getReqStatus(this.getRequestTypeCd(), this.getResultCd());
    }

    private String getReqStatus(String requestTypeCd, String resultCd){
        if("I".equals(requestTypeCd)){
            switch(resultCd){
                case "0":
                    return "발행";
                case "1":
                    return "발행실패";
                case "2":
                    return "발행요청철회";
                case "3":
                    return "발행요청";
                default:
                    return "오류";
            }
        } else if ("U".equals(requestTypeCd)) {
            switch(resultCd){
                case "0":
                    return "재발행";
                case "1":
                    return "재발행실패";
                case "2":
                    return "재발행요청철회";
                case "3":
                    return "재발행요청";
                default:
                    return "오류";
            }
        } else if ("D".equals(requestTypeCd)) {
            switch(resultCd){
                case "0":
                    return "취소";
                case "1":
                    return "취소실패";
                case "2":
                    return "취소요청철회";
                case "3":
                    return "취소요청";
                default:
                    return "오류";
            }
        } else {
            return "오류";
        }
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = this.getResponseMessage(this.responseCode);
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    private String getResponseMessage(String responseCode) {
        if(StringUtils.isBlank(responseCode)){
            return "";
        }

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
                return "";
            default:
                return "기타 오류";
        }
    }

    public String getChaCd() {
        return chaCd;
    }

    public void setChaCd(String chaCd) {
        this.chaCd = chaCd;
    }

    public String getChaName() {
        return chaName;
    }

    public void setChaName(String chaName) {
        this.chaName = chaName;
    }

    public String getNoTaxYn() {
        return noTaxYn;
    }

    public void setNoTaxYn(String noTaxYn) {
        this.noTaxYn = noTaxYn;
    }

    public String getPayDay() {
        return payDay;
    }

    public void setPayDay(String payDay) {
        if(!StringUtils.isBlank(payDay)){
            this.payDay = payDay.substring(0,4) + "." + payDay.substring(4,6) + "." + payDay.substring(6,8);
        }else {
            this.payDay = payDay;
        }
    }

    public String getTxDate() {
        return txDate;
    }

    public void setTxDate(String txDate) {
        if(!StringUtils.isBlank(txDate)){
            this.txDate = txDate.substring(0,4) + "." + txDate.substring(4,6) + "." + txDate.substring(6,8);
        }else {
            this.txDate = txDate;
        }
    }

    public String getTxNo() {
        return txNo;
    }

    public void setTxNo(String txNo) {
        this.txNo = txNo;
    }

    public String getTxTypeCode() {
        return txTypeCode;
    }

    public void setTxTypeCode(String txTypeCode) {
        this.txTypeCode = txTypeCode;
    }

    public String getCancelTxDate() {
        return cancelTxDate;
    }

    public void setCancelTxDate(String cancelTxDate) {
        if(!StringUtils.isBlank(cancelTxDate)){
            this.cancelTxDate = cancelTxDate.substring(0,4) + "." + cancelTxDate.substring(4,6) + "." + cancelTxDate.substring(6,8);
        }else {
            this.cancelTxDate = cancelTxDate;
        }
    }

    public String getCancelTxNo() {
        return cancelTxNo;
    }

    public void setCancelTxNo(String cancelTxNo) {
        this.cancelTxNo = cancelTxNo;
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

    public String getClientIdentityNo2() {
        return clientIdentityNo2;
    }

    public void setClientIdentityNo2(String clientIdentityNo2) {
        this.clientIdentityNo2 = clientIdentityNo2;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getRcpusrName() {
        return rcpusrName;
    }

    public void setRcpusrName(String rcpusrName) {
        this.rcpusrName = rcpusrName;
    }

    public Long getTxAmount() {
        return txAmount;
    }

    public void setTxAmount(Long txAmount) {
        this.txAmount = txAmount;
    }

    public Long getTax() {
        return tax;
    }

    public void setTax(Long tax) {
        this.tax = tax;
    }

    public String getSvecd() {
        return svecd;
    }

    public void setSvecd(String svecd) {
        this.svecd = svecd;
    }

    public String getRequestTypeCd() {
        return requestTypeCd;
    }

    public void setRequestTypeCd(String requestTypeCd) {
        this.requestTypeCd = requestTypeCd;
    }

    public String getResultCd() {
        return resultCd;
    }

    public void setResultCd(String resultCd) {
        this.resultCd = resultCd;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(String requestUser) {
        this.requestUser = requestUser;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
