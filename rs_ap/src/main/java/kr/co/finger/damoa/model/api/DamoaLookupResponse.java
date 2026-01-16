package kr.co.finger.damoa.model.api;

import java.io.Serializable;
@Deprecated
/**
 * API 서버 구현할 때 사용하려면 클래스..
 * API 서버는 기존로직을 사용하였으므로 이 클래스는 사용하지 않음.
 */
public class DamoaLookupResponse implements Serializable {
    private String DrecordType  	= "";
    private String serviceId    	= "";
    private String workField    	= "";
    private String seccessYN    	= "";
    private String resultCd     	= "";
    private String resultMsg    	= "";
    private String trNo         	= "";
    private String vaNo         	= "";
    private String payMasMonth  	= "";
    private String payMasDt     	= "";
    private String rcpDt			= "";
    private String rcpUsrName   	= "";
    private String bnkCd     	= "";
    private String bchCd     	= "";
    private String mdGubn     	= "";
    private String trnDay	    = "";
    private String rcpAmt     	= "";
    private String vaNoBnkCd 	= "";
    private String content1 		= "";
    private String content2 		= "";
    private String content3 		= "";
    private String content4 		= "";
    private String svecd	 		= "";
    private String chaTrNo      	= "";
    private String adjfiGrpCd   	= "";
    private String payItemNm    	= "";
    private String payItemAmt   	= "";

    private String rcvMsg		= "";

    public String getDrecordType() {
        return DrecordType;
    }

    public void setDrecordType(String drecordType) {
        DrecordType = drecordType;
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

    public String getSeccessYN() {
        return seccessYN;
    }

    public void setSeccessYN(String seccessYN) {
        this.seccessYN = seccessYN;
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

    public String getRcpDt() {
        return rcpDt;
    }

    public void setRcpDt(String rcpDt) {
        this.rcpDt = rcpDt;
    }

    public String getRcpUsrName() {
        return rcpUsrName;
    }

    public void setRcpUsrName(String rcpUsrName) {
        this.rcpUsrName = rcpUsrName;
    }

    public String getBnkCd() {
        return bnkCd;
    }

    public void setBnkCd(String bnkCd) {
        this.bnkCd = bnkCd;
    }

    public String getBchCd() {
        return bchCd;
    }

    public void setBchCd(String bchCd) {
        this.bchCd = bchCd;
    }

    public String getMdGubn() {
        return mdGubn;
    }

    public void setMdGubn(String mdGubn) {
        this.mdGubn = mdGubn;
    }

    public String getTrnDay() {
        return trnDay;
    }

    public void setTrnDay(String trnDay) {
        this.trnDay = trnDay;
    }

    public String getRcpAmt() {
        return rcpAmt;
    }

    public void setRcpAmt(String rcpAmt) {
        this.rcpAmt = rcpAmt;
    }

    public String getVaNoBnkCd() {
        return vaNoBnkCd;
    }

    public void setVaNoBnkCd(String vaNoBnkCd) {
        this.vaNoBnkCd = vaNoBnkCd;
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

    public String getSvecd() {
        return svecd;
    }

    public void setSvecd(String svecd) {
        this.svecd = svecd;
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

    public String getRcvMsg() {
        return rcvMsg;
    }

    public void setRcvMsg(String rcvMsg) {
        this.rcvMsg = rcvMsg;
    }
}
