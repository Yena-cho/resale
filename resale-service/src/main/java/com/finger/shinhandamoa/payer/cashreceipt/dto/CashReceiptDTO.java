package com.finger.shinhandamoa.payer.cashreceipt.dto;

/**
 * @author  
 * @date    
 * @desc    
 * @version 
 * 
 */
public class CashReceiptDTO {
	private String cashMasCd;	//원장코드
	private String masMonth;	//청구월
	private String payDay;		//납부일자
	private String payTime;		//납부시간
	private String cusKey;		//고객거래번호
	private String appDay;		//승인일자
	private String appNo;		//승인번호
	private String appTime;		//승인시간
	private String cusName;		//고객명
	private String cusGubn1;	//참조1
	private String cusGubn2;	//참조2
	private String cusGubn3;	//참조3
	private String cusGubn4;	//참조4
	private String cusOffNo;	//현금영수증발행고객정보
	private String cusOffNo2;	//신분확인번호
	private String rcpAmt;		//수납금액
	private String cshAmt;		
	private String cshAmt2;		
	private String cashMasSt;	//상태 ST02:미발행 ST03:발행
	private String cashMasStNm;
	private String appMsg;		//승인메세지
	private String disAbled;
	private String job;			//작업구분 I:신규 U:재발행 D:취소
	private String sveCd;		//서비스코드
	private String sendDt;		//전송일자
	private int curPage;
	private int pageScale;
	private String searchOrderBy;
	private String vaNo;
	private String chaCd;
	private String tmasMonth;
	private String fmasMonth;
	private String confirm;		//인증번호유형
	private String confirmNm;
	private String cusType;		//발급용도
	private String cusTypeNm;		
	private String status;
	private String totCnt;		
	
	public String getCashMasCd() {
		return cashMasCd;
	}
	public void setCashMasCd(String cashMasCd) {
		this.cashMasCd = cashMasCd;
	}
	public String getMasMonth() {
		return masMonth;
	}
	public void setMasMonth(String masMonth) {
		this.masMonth = masMonth;
	}
	public String getPayDay() {
		return payDay;
	}
	public void setPayDay(String payDay) {
		this.payDay = payDay;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getCusKey() {
		return cusKey;
	}
	public void setCusKey(String cusKey) {
		this.cusKey = cusKey;
	}
	public String getAppDay() {
		return appDay;
	}
	public void setAppDay(String appDay) {
		this.appDay = appDay;
	}
	public String getAppNo() {
		return appNo;
	}
	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}
	public String getAppTime() {
		return appTime;
	}
	public void setAppTime(String appTime) {
		this.appTime = appTime;
	}
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	public String getCusGubn1() {
		return cusGubn1;
	}
	public void setCusGubn1(String cusGubn1) {
		this.cusGubn1 = cusGubn1;
	}
	public String getCusGubn2() {
		return cusGubn2;
	}
	public void setCusGubn2(String cusGubn2) {
		this.cusGubn2 = cusGubn2;
	}
	public String getCusGubn3() {
		return cusGubn3;
	}
	public void setCusGubn3(String cusGubn3) {
		this.cusGubn3 = cusGubn3;
	}
	public String getCusGubn4() {
		return cusGubn4;
	}
	public void setCusGubn4(String cusGubn4) {
		this.cusGubn4 = cusGubn4;
	}
	public String getCusOffNo() {
		return cusOffNo;
	}
	public void setCusOffNo(String cusOffNo) {
		this.cusOffNo = cusOffNo;
	}
	public String getCusOffNo2() {
		return cusOffNo2;
	}
	public void setCusOffNo2(String cusOffNo2) {
		this.cusOffNo2 = cusOffNo2;
	}
	public String getRcpAmt() {
		return rcpAmt;
	}
	public void setRcpAmt(String rcpAmt) {
		this.rcpAmt = rcpAmt;
	}
	public String getCshAmt() {
		return cshAmt;
	}
	public void setCshAmt(String cshAmt) {
		this.cshAmt = cshAmt;
	}
	public String getCshAmt2() {
		return cshAmt2;
	}
	public void setCshAmt2(String cshAmt2) {
		this.cshAmt2 = cshAmt2;
	}
	public String getCashMasSt() {
		return cashMasSt;
	}
	public void setCashMasSt(String cashMasSt) {
		this.cashMasSt = cashMasSt;
	}
	public String getCashMasStNm() {
		return cashMasStNm;
	}
	public void setCashMasStNm(String cashMasStNm) {
		this.cashMasStNm = cashMasStNm;
	}
	public String getAppMsg() {
		return appMsg;
	}
	public void setAppMsg(String appMsg) {
		this.appMsg = appMsg;
	}
	public String getDisAbled() {
		return disAbled;
	}
	public void setDisAbled(String disAbled) {
		this.disAbled = disAbled;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getSveCd() {
		return sveCd;
	}
	public void setSveCd(String sveCd) {
		this.sveCd = sveCd;
	}
	public String getSendDt() {
		return sendDt;
	}
	public void setSendDt(String sendDt) {
		this.sendDt = sendDt;
	}
	public int getCurPage() {
		return curPage;
	}
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	public int getPageScale() {
		return pageScale;
	}
	public void setPageScale(int pageScale) {
		this.pageScale = pageScale;
	}
	public String getSearchOrderBy() {
		return searchOrderBy;
	}
	public void setSearchOrderBy(String searchOrderBy) {
		this.searchOrderBy = searchOrderBy;
	}
	public String getVaNo() {
		return vaNo;
	}
	public void setVaNo(String vaNo) {
		this.vaNo = vaNo;
	}
	public String getChaCd() {
		return chaCd;
	}
	public void setChaCd(String chaCd) {
		this.chaCd = chaCd;
	}
	public String getTmasMonth() {
		return tmasMonth;
	}
	public void setTmasMonth(String tmasMonth) {
		this.tmasMonth = tmasMonth;
	}
	public String getFmasMonth() {
		return fmasMonth;
	}
	public void setFmasMonth(String fmasMonth) {
		this.fmasMonth = fmasMonth;
	}
	public String getConfirm() {
		return confirm;
	}
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	public String getCusType() {
		return cusType;
	}
	public void setCusType(String cusType) {
		this.cusType = cusType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTotCnt() {
		return totCnt;
	}
	public void setTotCnt(String totCnt) {
		this.totCnt = totCnt;
	}
	public String getConfirmNm() {
		return confirmNm;
	}
	public void setConfirmNm(String confirmNm) {
		this.confirmNm = confirmNm;
	}
	public String getCusTypeNm() {
		return cusTypeNm;
	}
	public void setCusTypeNm(String cusTypeNm) {
		this.cusTypeNm = cusTypeNm;
	}
	
}
