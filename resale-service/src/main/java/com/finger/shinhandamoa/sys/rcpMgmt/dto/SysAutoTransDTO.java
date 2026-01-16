package com.finger.shinhandamoa.sys.rcpMgmt.dto;

public class SysAutoTransDTO {

	private int no;				
	private int bbs;			//구분값 
	private int rn;
	private String day; //출금일시
	private String payAccNum;		//출금계좌번호
	private String comCd;//기관코드
	private String comNm;//기관명
	private String payAmount;		//출금액
	private String toPay;		//미납액
	private String transSt;		//출금상태

	//페이징 및 조회용 검색조건변수
	private String searchOption;
	private String keyword;
	private String searchOrderBy;
	private int pageScale;
	private int curPage;
	private String startday;
	private String endday;
	private String starttime;
	private String endtime;
	

	public SysAutoTransDTO() {
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


	public String getDay() {
		return day;
	}


	public void setDay(String day) {
		this.day = day;
	}


	public String getPayAccNum() {
		return payAccNum;
	}


	public void setPayAccNum(String payAccNum) {
		this.payAccNum = payAccNum;
	}


	public String getComCd() {
		return comCd;
	}


	public void setComCd(String comCd) {
		this.comCd = comCd;
	}


	public String getComNm() {
		return comNm;
	}


	public void setComNm(String comNm) {
		this.comNm = comNm;
	}


	public String getPayAmount() {
		return payAmount;
	}


	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}


	public String getToPay() {
		return toPay;
	}


	public void setToPay(String toPay) {
		this.toPay = toPay;
	}


	public String getTransSt() {
		return transSt;
	}


	public void setTransSt(String transSt) {
		this.transSt = transSt;
	}


	public String getSearchOption() {
		return searchOption;
	}


	public void setSearchOption(String searchOption) {
		this.searchOption = searchOption;
	}


	public String getKeyword() {
		return keyword;
	}


	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}


	public String getSearchOrderBy() {
		return searchOrderBy;
	}


	public void setSearchOrderBy(String searchOrderBy) {
		this.searchOrderBy = searchOrderBy;
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


	public String getStarttime() {
		return starttime;
	}


	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}


	public String getEndtime() {
		return endtime;
	}


	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}


	
}
