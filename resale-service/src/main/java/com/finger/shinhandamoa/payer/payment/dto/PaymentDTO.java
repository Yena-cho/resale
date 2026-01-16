package com.finger.shinhandamoa.payer.payment.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author  by PYS
 * @date    2018. 4. 12.
 * @desc    
 * @version 
 * 
 */
public class PaymentDTO {

	private String masMonth;	//청구월
	private String sveCd;		//서비스코드, VAS-가상계좌,DCS-창구현금,DCD-창구카드,DVA-무통장입금,OCD-인터넷카드
	private String rcpAmt;		//수납금액
	private String totAmt;					
	private String payRcpYn;	//현금영수증발행고객정보 유무
	private String chaOffNo;	//사업자번호
	private String owner;		//대표자
	private String chaAddress1;	//주소1
	private String chaAddress2;	//주소2
	private String ownerTel;	//대표전화번호
	private String dealAmt;		//거래금액
	private String tax;			//부가세
	private String tip;			//봉사료
	private String cusoffNo;	//신분확인번호
	private String appNo;		//납부기간중복(허용:Y, 불가:N, 부분납:P)		
	private String appDay;		//승인일자
	private String cusName;		//고객명
	private String bnkMsgNo;	//은행전문번호
	private String payDay;		//수납일자
	private String payDay2;		//수납일자
	private String notimasCd;	//원장코드
	private String packetNo;	//이지스전문번호
	private String pgServiceId;
	private String rcpReqYn;	//현금영수증신청여부
	private String usePgYn;		//PG서비스사용여부
	private String chaName;		//가맹점명
	private String rcpDtDupYn;	//납부기간중복(허용:Y, 불가:N, 부분납:P)	
	private String chaCd; 		//가맹점코드
	private String payItemAmt;	//청구금액
	private String rcpMasSt;	//납부상태
	private String rcpmasCd;	
	private String fmasMonth;	//청구월 조회
	private String tmasMonth;	//청구월 조회
	private String rn;			//리스트넘버
	private String vaNo;		//가상계좌
	private int curPage;
	private int pageScale;
	private String searchOrderBy;
	private ArrayList<String> checkArr;
	private String notiMasSt;
	
	public String getMasMonth() {
		return masMonth;
	}
	public void setMasMonth(String masMonth) {
		this.masMonth = masMonth;
	}
	public String getSveCd() {
		return sveCd;
	}
	public void setSveCd(String sveCd) {
		this.sveCd = sveCd;
	}
	public String getRcpAmt() {
		return rcpAmt;
	}
	public void setRcpAmt(String rcpAmt) {
		this.rcpAmt = rcpAmt;
	}
	public String getTotAmt() {
		return totAmt;
	}
	public void setTotAmt(String totAmt) {
		this.totAmt = totAmt;
	}
	public String getPayRcpYn() {
		return payRcpYn;
	}
	public void setPayRcpYn(String payRcpYn) {
		this.payRcpYn = payRcpYn;
	}
	public String getChaOffNo() {
		return chaOffNo;
	}
	public void setChaOffNo(String chaOffNo) {
		this.chaOffNo = chaOffNo;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getChaAddress1() {
		return chaAddress1;
	}
	public void setChaAddress1(String chaAddress1) {
		this.chaAddress1 = chaAddress1;
	}
	public String getChaAddress2() {
		return chaAddress2;
	}
	public void setChaAddress2(String chaAddress2) {
		this.chaAddress2 = chaAddress2;
	}
	public String getOwnerTel() {
		return ownerTel;
	}
	public void setOwnerTel(String ownerTel) {
		this.ownerTel = ownerTel;
	}
	public String getDealAmt() {
		return dealAmt;
	}
	public void setDealAmt(String dealAmt) {
		this.dealAmt = dealAmt;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getCusoffNo() {
		return cusoffNo;
	}
	public void setCusoffNo(String cusoffNo) {
		this.cusoffNo = cusoffNo;
	}
	public String getAppNo() {
		return appNo;
	}
	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}
	public String getAppDay() {
		return appDay;
	}
	public void setAppDay(String appDay) {
		this.appDay = appDay;
	}
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	public String getBnkMsgNo() {
		return bnkMsgNo;
	}
	public void setBnkMsgNo(String bnkMsgNo) {
		this.bnkMsgNo = bnkMsgNo;
	}
	public String getPayDay() {
		return payDay;
	}
	public void setPayDay(String payDay) {
		this.payDay = payDay;
	}
	public String getPayDay2() {
		return payDay2;
	}
	public void setPayDay2(String payDay2) {
		this.payDay2 = payDay2;
	}
	public String getNotimasCd() {
		return notimasCd;
	}
	public void setNotimasCd(String notimasCd) {
		this.notimasCd = notimasCd;
	}
	public String getPacketNo() {
		return packetNo;
	}
	public void setPacketNo(String packetNo) {
		this.packetNo = packetNo;
	}
	public String getPgServiceId() {
		return pgServiceId;
	}
	public void setPgServiceId(String pgServiceId) {
		this.pgServiceId = pgServiceId;
	}
	public String getRcpReqYn() {
		return rcpReqYn;
	}
	public void setRcpReqYn(String rcpReqYn) {
		this.rcpReqYn = rcpReqYn;
	}
	public String getUsePgYn() {
		return usePgYn;
	}
	public void setUsePgYn(String usePgYn) {
		this.usePgYn = usePgYn;
	}
	public String getChaName() {
		return chaName;
	}
	public void setChaName(String chaName) {
		this.chaName = chaName;
	}
	public String getRcpDtDupYn() {
		return rcpDtDupYn;
	}
	public void setRcpDtDupYn(String rcpDtDupYn) {
		this.rcpDtDupYn = rcpDtDupYn;
	}
	public String getChaCd() {
		return chaCd;
	}
	public void setChaCd(String chaCd) {
		this.chaCd = chaCd;
	}
	public String getPayItemAmt() {
		return payItemAmt;
	}
	public void setPayItemAmt(String payItemAmt) {
		this.payItemAmt = payItemAmt;
	}
	public String getRcpMasSt() {
		return rcpMasSt;
	}
	public void setRcpMasSt(String rcpMasSt) {
		this.rcpMasSt = rcpMasSt;
	}
	public String getFmasMonth() {
		return fmasMonth;
	}
	public void setFmasMonth(String fmasMonth) {
		this.fmasMonth = fmasMonth;
	}
	public String getTmasMonth() {
		return tmasMonth;
	}
	public void setTmasMonth(String tmasMonth) {
		this.tmasMonth = tmasMonth;
	}
	public String getRn() {
		return rn;
	}
	public void setRn(String rn) {
		this.rn = rn;
	}
	public String getVaNo() {
		return vaNo;
	}
	public void setVaNo(String vaNo) {
		this.vaNo = vaNo;
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
	public String getRcpmasCd() {
		return rcpmasCd;
	}
	public void setRcpmasCd(String rcpmasCd) {
		this.rcpmasCd = rcpmasCd;
	}
	public ArrayList<String> getCheckArr() {
		return checkArr;
	}
	public void setCheckArr(ArrayList<String> checkArr) {
		this.checkArr = checkArr;
	}
	public String getNotiMasSt() {
		return notiMasSt;
	}
	public void setNotiMasSt(String notiMasSt) {
		this.notiMasSt = notiMasSt;
	}
	
}
