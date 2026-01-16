package com.finger.shinhandamoa.sys.rcpMgmt.dto;

public class SysRcpStatDTO {

	private int no;				
	private int bbs;			//구분값 
	private int rn;
	
	private String custSt; 		//기관 분류
	private String chaCd; 		//기관코드
	private String totCusCount; // 총 이용기관 수 
	private String totRcpAmt; 	// 총수납금액
	private String totRcpCount; // 총수납건수
	
	//전체이용기관
	private String masMonth; 	//청구월
	private String payMethod;	//납부방법
	private String custCount;	//이용기관수
	private String rcpAmt;		//수납금액
	private String rcpCount;	//입금건수
	
	private int ccSum;			//이용기관수 합
	private int raSum; 			//수납금액 합
	private int rcSum; 			//입금건수 합
		

	//페이징 및 조회용 검색조건변수
	private int pageScale;
	private int curPage;
	private String startday;
	private String endday;
	private String comSt;
	

	public SysRcpStatDTO() {
		super();
	}


	public int getNo() {
		return no;
	}


	public void setNo(int no) {
		this.no = no;
	}


	public int getBbs() {
		return bbs;
	}


	public void setBbs(int bbs) {
		this.bbs = bbs;
	}


	public int getRn() {
		return rn;
	}


	public void setRn(int rn) {
		this.rn = rn;
	}


	public String getCustSt() {
		return custSt;
	}


	public void setCustSt(String custSt) {
		this.custSt = custSt;
	}


	public String getChaCd() {
		return chaCd;
	}


	public void setChaCd(String chaCd) {
		this.chaCd = chaCd;
	}


	public String getTotCusCount() {
		return totCusCount;
	}


	public void setTotCusCount(String totCusCount) {
		this.totCusCount = totCusCount;
	}


	public String getTotRcpAmt() {
		return totRcpAmt;
	}


	public void setTotRcpAmt(String totRcpAmt) {
		this.totRcpAmt = totRcpAmt;
	}


	public String getTotRcpCount() {
		return totRcpCount;
	}


	public void setTotRcpCount(String totRcpCount) {
		this.totRcpCount = totRcpCount;
	}


	public String getMasMonth() {
		return masMonth;
	}


	public void setMasMonth(String masMonth) {
		this.masMonth = masMonth;
	}


	public String getPayMethod() {
		return payMethod;
	}


	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}


	public String getCustCount() {
		return custCount;
	}


	public void setCustCount(String custCount) {
		this.custCount = custCount;
	}


	public String getRcpAmt() {
		return rcpAmt;
	}


	public void setRcpAmt(String rcpAmt) {
		this.rcpAmt = rcpAmt;
	}


	public String getRcpCount() {
		return rcpCount;
	}


	public void setRcpCount(String rcpCount) {
		this.rcpCount = rcpCount;
	}


	public int getCcSum() {
		return ccSum;
	}


	public void setCcSum(int ccSum) {
		this.ccSum = ccSum;
	}


	public int getRaSum() {
		return raSum;
	}


	public void setRaSum(int raSum) {
		this.raSum = raSum;
	}


	public int getRcSum() {
		return rcSum;
	}


	public void setRcSum(int rcSum) {
		this.rcSum = rcSum;
	}


	public int getPageScale() {
		return pageScale;
	}


	public void setPageScale(int pageScale) {
		this.pageScale = pageScale;
	}


	public int getCurPage() {
		return curPage;
	}


	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}


	public String getStartday() {
		return startday;
	}


	public void setStartday(String startday) {
		this.startday = startday;
	}


	public String getEndday() {
		return endday;
	}


	public void setEndday(String endday) {
		this.endday = endday;
	}


	public String getComSt() {
		return comSt;
	}


	public void setComSt(String comSt) {
		this.comSt = comSt;
	}


}
