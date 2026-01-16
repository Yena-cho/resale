package com.finger.shinhandamoa.sys.rcpMgmt.dto;

public class SysSmsUsageDTO {

	private int no;				
	private int bbs;			//구분값 
	private int rn;
	private String day; //발송일시
	private String comCd;//기관코드
	private String comNm;//기관명
	private String name;		//담당자명
	private String sendSt;		//발송유형
	private String sendcount;		//발송건수
	private String failcount;		//실패건수
	private String charge;//이용금액
	private String fingerCharge;//핑거정산액

	//페이징 및 조회용 검색조건변수
	private String searchOption;
	private String keyword;
	private String searchOrderBy;
	private int pageScale;
	private int curPage;
	private String startday;
	private String endday;

	public SysSmsUsageDTO() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSendSt() {
		return sendSt;
	}

	public void setSendSt(String sendSt) {
		this.sendSt = sendSt;
	}

	public String getSendcount() {
		return sendcount;
	}

	public void setSendcount(String sendcount) {
		this.sendcount = sendcount;
	}

	public String getFailcount() {
		return failcount;
	}

	public void setFailcount(String failcount) {
		this.failcount = failcount;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getFingerCharge() {
		return fingerCharge;
	}

	public void setFingerCharge(String fingerCharge) {
		this.fingerCharge = fingerCharge;
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



	
	
}
