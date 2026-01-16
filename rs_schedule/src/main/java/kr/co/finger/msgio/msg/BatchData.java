package kr.co.finger.msgio.msg;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

public class BatchData extends BaseMsg{
    private String recordType = "R";	//Record구분
    private String seqNo ="";	//일련번호
    private String corpCode= "";	//기관코드
    private String withdrawalBankCode= "";	//출금참가기관
    private String withdrawalAccntNo= "";	//출금계좌번호
    private String withdrawalAmout= "";	//출금의뢰금액
    private String compRegNo= "";	//사업자등록번호
    private String resultCode= "";	//결과코드
    private String errorCode= "";	//불능코드
    private String passBookInfo= "핑거";	//통장기재내용
    private String fundType= "";	//자금종료
    private String payerNo= "";	//납부자번호
    private String temp= "";	//이용기관사용영역
    private String withdrawalType= "1";	//출금형태
    private String phoneNo= "";	//전화번호
    private String filler = "";	//filler

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("recordType", recordType)
                .append("seqNo", seqNo)
                .append("corpCode", corpCode)
                .append("withdrawalBankCode", withdrawalBankCode)
                .append("withdrawalAccntNo", withdrawalAccntNo)
                .append("withdrawalAmout", withdrawalAmout)
                .append("compRegNo", compRegNo)
                .append("resultCode", resultCode)
                .append("errorCode", errorCode)
                .append("passBookInfo", passBookInfo)
                .append("fundType", fundType)
                .append("payerNo", payerNo)
                .append("temp", temp)
                .append("withdrawalType", withdrawalType)
                .append("phoneNo", phoneNo)
                .append("filler", filler)
                .toString();
    }

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

    public String getWithdrawalBankCode() {
        return withdrawalBankCode;
    }

    public void setWithdrawalBankCode(String withdrawalBankCode) {
        this.withdrawalBankCode = withdrawalBankCode;
    }

    public String getWithdrawalAccntNo() {
        return withdrawalAccntNo;
    }

    public void setWithdrawalAccntNo(String withdrawalAccntNo) {
        this.withdrawalAccntNo = withdrawalAccntNo;
    }

    public String getWithdrawalAmout() {
        return withdrawalAmout;
    }

    public void setWithdrawalAmout(String withdrawalAmout) {
        this.withdrawalAmout = withdrawalAmout;
    }

    public String getCompRegNo() {
        return compRegNo;
    }

    public void setCompRegNo(String compRegNo) {
        this.compRegNo = compRegNo;
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

    public String getPassBookInfo() {
        return passBookInfo;
    }

    public void setPassBookInfo(String passBookInfo) {
        this.passBookInfo = passBookInfo;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getPayerNo() {
        return payerNo;
    }

    public void setPayerNo(String payerNo) {
        this.payerNo = payerNo;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWithdrawalType() {
        return withdrawalType;
    }

    public void setWithdrawalType(String withdrawalType) {
        this.withdrawalType = withdrawalType;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }

    public Map<String, Object> toEB21UpdateParam(String month) {
        Map<String, Object> param = Maps.hashmap();
        param.put("MONTH", month);    //
        param.put("CHACD", getPayerNo());
        param.put("WIT_REQ_DT", DateUtils.to14NowString());

        return param;
    }
    public Map<String, Object> toEB22UpdateParam(String month) {
        Map<String, Object> param = Maps.hashmap();
        param.put("MONTH", month);    //
        param.put("CHACD", getPayerNo());

        if ("Y".equalsIgnoreCase(resultCode)) {
            param.put("RCP_FAIL_RESON", "");
            param.put("RCP_ST", "ST03");
        } else {
            param.put("RCP_FAIL_RESON", errorCode);
            param.put("RCP_ST", "ST04");

        }

        param.put("RCP_DT", DateUtils.to14NowString());

        return param;
    }
}
