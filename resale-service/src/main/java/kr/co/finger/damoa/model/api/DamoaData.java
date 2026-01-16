package kr.co.finger.damoa.model.api;

import java.io.Serializable;
@Deprecated
/**
 * API 서버 구현할 때 사용하려면 클래스..
 * API 서버는 기존로직을 사용하였으므로 이 클래스는 사용하지 않음.
 */
public class DamoaData implements Serializable{
    private String recordType 	= "";
    private String serviceId 	= "";
    private String workField 	= "";
    private String successYN 	= "";
    private String resultCd 	= "";
    private String resultMsg 	= "";
    private String trNo 		= "";
    private String vaNo 		= "";
    private String payMasMonth 	= "";
    private String payMasDt 	= "";
    private String rcpStartDt 	= "";
    private String rcpStartTm 	= "";
    private String rcpEndDt 	= "";
    private String rcpEndTm 	= "";
    private String rcpPrtEndDt 	= "";
    private String rcpPrtEndTm 	= "";
    private String cusNm 		= "";
    private String smsYn 		= "";
    private String mobileNo 	= "";
    private String emailYn 		= "";
    private String email 		= "";
    private String content1 	= "";
    private String content2 	= "";
    private String content3 	= "";
    private String content4 	= "";
    private String regSeq       = "";

    private String chaTrNo       = "";
    private String adjfiGrpCd    = "";
    private String payItemNm     = "";
    private String payItemAmt    = "";
    private String payItemYN     = "";
    private String rcpItemYN     = "";
    private String rcpBusinessNo = "";
    private String rcpPersonNo   = "";
    private String prtPayItemNm  = "";
    private String prtContent1   = "";
    private String prtSeq        = "";


    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getWorkField() {
        return workField;
    }

    public void setWorkField(String workField) {
        this.workField = workField;
    }

    public String getSuccessYN() {
        return successYN;
    }

    public void setSuccessYN(String successYN) {
        this.successYN = successYN;
    }

    public String getResultCd() {
        return resultCd;
    }

    public void setResultCd(String resultCd) {
        this.resultCd = resultCd;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getTrNo() {
        return trNo;
    }

    public void setTrNo(String trNo) {
        this.trNo = trNo;
    }

    public String getVaNo() {
        return vaNo;
    }

    public void setVaNo(String vaNo) {
        this.vaNo = vaNo;
    }

    public String getPayMasMonth() {
        return payMasMonth;
    }

    public void setPayMasMonth(String payMasMonth) {
        this.payMasMonth = payMasMonth;
    }

    public String getPayMasDt() {
        return payMasDt;
    }

    public void setPayMasDt(String payMasDt) {
        this.payMasDt = payMasDt;
    }

    public String getRcpStartDt() {
        return rcpStartDt;
    }

    public void setRcpStartDt(String rcpStartDt) {
        this.rcpStartDt = rcpStartDt;
    }

    public String getRcpStartTm() {
        return rcpStartTm;
    }

    public void setRcpStartTm(String rcpStartTm) {
        this.rcpStartTm = rcpStartTm;
    }

    public String getRcpEndDt() {
        return rcpEndDt;
    }

    public void setRcpEndDt(String rcpEndDt) {
        this.rcpEndDt = rcpEndDt;
    }

    public String getRcpEndTm() {
        return rcpEndTm;
    }

    public void setRcpEndTm(String rcpEndTm) {
        this.rcpEndTm = rcpEndTm;
    }

    public String getRcpPrtEndDt() {
        return rcpPrtEndDt;
    }

    public void setRcpPrtEndDt(String rcpPrtEndDt) {
        this.rcpPrtEndDt = rcpPrtEndDt;
    }

    public String getRcpPrtEndTm() {
        return rcpPrtEndTm;
    }

    public void setRcpPrtEndTm(String rcpPrtEndTm) {
        this.rcpPrtEndTm = rcpPrtEndTm;
    }

    public String getCusNm() {
        return cusNm;
    }

    public void setCusNm(String cusNm) {
        this.cusNm = cusNm;
    }

    public String getSmsYn() {
        return smsYn;
    }

    public void setSmsYn(String smsYn) {
        this.smsYn = smsYn;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailYn() {
        return emailYn;
    }

    public void setEmailYn(String emailYn) {
        this.emailYn = emailYn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public String getContent3() {
        return content3;
    }

    public void setContent3(String content3) {
        this.content3 = content3;
    }

    public String getContent4() {
        return content4;
    }

    public void setContent4(String content4) {
        this.content4 = content4;
    }

    public String getRegSeq() {
        return regSeq;
    }

    public void setRegSeq(String regSeq) {
        this.regSeq = regSeq;
    }

    public String getChaTrNo() {
        return chaTrNo;
    }

    public void setChaTrNo(String chaTrNo) {
        this.chaTrNo = chaTrNo;
    }

    public String getAdjfiGrpCd() {
        return adjfiGrpCd;
    }

    public void setAdjfiGrpCd(String adjfiGrpCd) {
        this.adjfiGrpCd = adjfiGrpCd;
    }

    public String getPayItemNm() {
        return payItemNm;
    }

    public void setPayItemNm(String payItemNm) {
        this.payItemNm = payItemNm;
    }

    public String getPayItemAmt() {
        return payItemAmt;
    }

    public void setPayItemAmt(String payItemAmt) {
        this.payItemAmt = payItemAmt;
    }

    public String getPayItemYN() {
        return payItemYN;
    }

    public void setPayItemYN(String payItemYN) {
        this.payItemYN = payItemYN;
    }

    public String getRcpItemYN() {
        return rcpItemYN;
    }

    public void setRcpItemYN(String rcpItemYN) {
        this.rcpItemYN = rcpItemYN;
    }

    public String getRcpBusinessNo() {
        return rcpBusinessNo;
    }

    public void setRcpBusinessNo(String rcpBusinessNo) {
        this.rcpBusinessNo = rcpBusinessNo;
    }

    public String getRcpPersonNo() {
        return rcpPersonNo;
    }

    public void setRcpPersonNo(String rcpPersonNo) {
        this.rcpPersonNo = rcpPersonNo;
    }

    public String getPrtPayItemNm() {
        return prtPayItemNm;
    }

    public void setPrtPayItemNm(String prtPayItemNm) {
        this.prtPayItemNm = prtPayItemNm;
    }

    public String getPrtContent1() {
        return prtContent1;
    }

    public void setPrtContent1(String prtContent1) {
        this.prtContent1 = prtContent1;
    }

    public String getPrtSeq() {
        return prtSeq;
    }

    public void setPrtSeq(String prtSeq) {
        this.prtSeq = prtSeq;
    }
}
