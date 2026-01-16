package com.finger.shinhandamoa.sys.rcpMgmt.dto;

public class SysInvoiceDTO {

	private int no;				
	private int bbs;			//구분값 
	private int rn;
	private String useDay; //사용월
	private String comNm;//기관명
	private String comCd;//기관코드
	private String price;		//공급가액
	private String tax;		//부가세
	private String email;		//이메일
	private String issueSt;		//발행상태
	private String issueNm;//승인번호
	private String issueDay;//발행일자

	//페이징 및 조회용 검색조건변수
	private String searchOption;
	private String keyword;
	private String searchOrderBy;
	private int pageScale;
	private int curPage;
	private String useDate;

	public SysInvoiceDTO() {
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

	public String getUseDay() {
		return useDay;
	}

	public void setUseDay(String useDay) {
		this.useDay = useDay;
	}

	public String getComNm() {
		return comNm;
	}

	public void setComNm(String comNm) {
		this.comNm = comNm;
	}

	public String getComCd() {
		return comCd;
	}

	public void setComCd(String comCd) {
		this.comCd = comCd;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIssueSt() {
		return issueSt;
	}

	public void setIssueSt(String issueSt) {
		this.issueSt = issueSt;
	}

	public String getIssueNm() {
		return issueNm;
	}

	public void setIssueNm(String issueNm) {
		this.issueNm = issueNm;
	}

	public String getIssueDay() {
		return issueDay;
	}

	public void setIssueDay(String issueDay) {
		this.issueDay = issueDay;
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

	public String getUseDate() {
		return useDate;
	}

	public void setUseDate(String useDate) {
		this.useDate = useDate;
	}


	
	
}
