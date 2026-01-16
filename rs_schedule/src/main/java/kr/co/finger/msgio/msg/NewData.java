package kr.co.finger.msgio.msg;

import kr.co.finger.damoa.commons.Maps;

import java.util.Map;

public class NewData extends BaseMsg {
    private String recordType = "R";    //Record구분
    private String seqNo = "";    //일련번호
    private String corpCode = "";    //기관코드
    private String reqDate = "";    //신청일자
    private String reqType = "";    //신청구분
    private String payerNo = "";    //납부자번호
    private String bankCode = "";    //은행점코드
    private String withdrawalAccountNo = "";    //지점출금계좌번호
    private String compRegNo = "";    //사업자등록번호
    private String dealerCode = "";    //취급점코드
    private String fundType = "";    //자금종료
    private String resultCode = "";    //결과코드
    private String errorCode = "";    //불능코드
    private String filler1 = "";    //filler1
    private String phoneNo = "";    //전화번호
    private String channel = "";    //신청서접수채널
    private String filler2 = "";    //filler2


    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getPayerNo() {
        return payerNo;
    }

    public void setPayerNo(String payerNo) {
        this.payerNo = payerNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getWithdrawalAccountNo() {
        return withdrawalAccountNo;
    }

    public void setWithdrawalAccountNo(String withdrawalAccountNo) {
        this.withdrawalAccountNo = withdrawalAccountNo;
    }

    public String getCompRegNo() {
        return compRegNo;
    }

    public void setCompRegNo(String compRegNo) {
        this.compRegNo = compRegNo;
    }

    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getFiller1() {
        return filler1;
    }

    public void setFiller1(String filler1) {
        this.filler1 = filler1;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getFiller2() {
        return filler2;
    }

    public void setFiller2(String filler2) {
        this.filler2 = filler2;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NewData{");
        sb.append("recordType='").append(recordType).append('\'');
        sb.append(", seqNo='").append(seqNo).append('\'');
        sb.append(", corpCode='").append(corpCode).append('\'');
        sb.append(", reqDate='").append(reqDate).append('\'');
        sb.append(", reqType='").append(reqType).append('\'');
        sb.append(", payerNo='").append(payerNo).append('\'');
        sb.append(", bankCode='").append(bankCode).append('\'');
        sb.append(", withdrawalAccountNo='").append(withdrawalAccountNo).append('\'');
        sb.append(", compRegNo='").append(compRegNo).append('\'');
        sb.append(", dealerCode='").append(dealerCode).append('\'');
        sb.append(", fundType='").append(fundType).append('\'');
        sb.append(", resultCode='").append(resultCode).append('\'');
        sb.append(", errorCode='").append(errorCode).append('\'');
        sb.append(", filler1='").append(filler1).append('\'');
        sb.append(", phoneNo='").append(phoneNo).append('\'');
        sb.append(", channel='").append(channel).append('\'');
        sb.append(", filler2='").append(filler2).append('\'');
        sb.append('}');
        return sb.toString();
    }


    public Map<String, Object> toUpdateParam() {
        Map<String, Object> param = Maps.hashmap();
        if ("N".equalsIgnoreCase(resultCode)) {
            param.put("CMS_REQ_FAIL_CONT", errorCode);
            param.put("CMS_REQ_ST", "CST04");   // 신청실패

        } else {
            param.put("CMS_REQ_FAIL_CONT", "");
            param.put("CMS_REQ_ST", "CST03");   // 신청성공
        }
        param.put("CHACD", getPayerNo());

        return param;
    }

    public Map<String, Object> toEB13UpdateParam() {
        Map<String, Object> param = Maps.hashmap();
        param.put("CMS_REQ_ST", "CST02");    // 신청중
        param.put("CHACD", getPayerNo());

        return param;
    }
}
