package com.finger.shinhandamoa.org.notimgmt.dto;

import java.util.ArrayList;
import java.util.List;

public class NotiMgmtReqDTO {

	private String masMonth; // 조회 년월
	 
	private String searchGb; // 검색구분 선택
	
	private String searchValue; // 검색구분 텍스트값
	
	private String chaCd;		// 가맹점코드
	
	private String rcpDtDupYn; //납부기간중복[허용:Y, 불가:N, 부분납:P]

	private String payGb1;	//납부구분(전체)
	
	private String payGb2;	//납부구분(미납)
	
	private String payGb3;	//납부구분(완납)
	
	private String payGb4;	//납부구분(일부납)
	
	private String payGb5;	//납부구분(초과납)
	
	private String cusGubn;// 고객구분 선택
	
	private String cusGubnValue; //고객구분 텍스트값

	private ArrayList<String> payList; // 납부구분 선택(리스트)
	
	private List<String> itemList;
	
	private int curPage;
	
	private int pageScale;
	
	private int start;
	
	private int end;
	
	private String search_orderBy;
	
	
	public void setMasMonth(String masMonth) {
		this.masMonth = masMonth;
	}

	public void setCusGubnValue(String cusGubnValue) {
		this.cusGubnValue = cusGubnValue;
	}

	public String getSearch_orderBy() {
		return search_orderBy;
	}

	public void setSearch_orderBy(String search_orderBy) {
		this.search_orderBy = search_orderBy;
	}

	public String getMasMonth() {
		return masMonth;
	}

	public String getSearchGb() {
		return searchGb;
	}

	public void setSearchGb(String searchGb) {
		this.searchGb = searchGb;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public String getChaCd() {
		return chaCd;
	}

	public void setChaCd(String chaCd) {
		this.chaCd = chaCd;
	}

	public String getRcpDtDupYn() {
		return rcpDtDupYn;
	}

	public void setRcpDtDupYn(String rcpDtDupYn) {
		this.rcpDtDupYn = rcpDtDupYn;
	}

	public String getPayGb1() {
		return payGb1;
	}

	public void setPayGb1(String payGb1) {
		this.payGb1 = payGb1;
	}

	public String getPayGb2() {
		return payGb2;
	}

	public void setPayGb2(String payGb2) {
		this.payGb2 = payGb2;
	}

	public String getPayGb3() {
		return payGb3;
	}

	public void setPayGb3(String payGb3) {
		this.payGb3 = payGb3;
	}

	public String getPayGb4() {
		return payGb4;
	}

	public void setPayGb4(String payGb4) {
		this.payGb4 = payGb4;
	}

	public String getPayGb5() {
		return payGb5;
	}

	public void setPayGb5(String payGb5) {
		this.payGb5 = payGb5;
	}

	public String getCusGubn() {
		return cusGubn;
	}

	public void setCusGubn(String cusGubn) {
		this.cusGubn = cusGubn;
	}

	public String getCusGubnValue() {
		return cusGubnValue;
	}

	public ArrayList<String> getPayList() {
		return payList;
	}

	public void setPayList(ArrayList<String> payList) {
		this.payList = payList;
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

	public List<String> getItemList() {
		return itemList;
	}

	public void setItemList(List<String> itemList) {
		this.itemList = itemList;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
	
}
