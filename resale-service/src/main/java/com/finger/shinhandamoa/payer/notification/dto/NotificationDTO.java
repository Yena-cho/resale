package com.finger.shinhandamoa.payer.notification.dto;

/**
 * @author  by PYS
 * @date    2018. 4. 12.
 * @desc    
 * @version 
 * 
 */
public class NotificationDTO {

	private String notimasCd;	//원장코드
	private String masMonth;	//청구월
	private String chaCd;		//가맹점코드
	private String vaNo;		//가상계좌번호
	private String endDate;		//납부마감일(일시)			
	private String cusName;		//고객명
	private int subTot;			//
	private int unSubTot;		//미납금액
	private String payItemName;	//항목명
	private String notidetCd;	//항목번호
	private String notidetSt;	//상태
	private String payItemAmt;	//항목금액
	private String fmasMonth;	//from 청구월
	private String tmasMonth;	//to 청구월
	private String usePgYn;		//PG서비스사용여부
	private String chaName;		//가맹점명
	private String rcpDtDupYn;	//납부기간중복(허용:Y, 불가:N, 부분납:P)
	private String rn;			//리스트넘버
	private int curPage;
	private int pageScale;
	private String searchOrderBy;
	private int detCnt;
	private String notimasSt;
	private String notimasNm;
	private String pgServiceId;
	private String pgLicenKey;
	private String amt;
	private String cusKey;
	private String cusHp;
	private String cusMail;
	private String itemName;
	private String rcpMasCd;
	private String chaAddress1;
	private String chaAddress2;
	private String ownerTel;
	private String rcpAmt;
	private String toDay;
	private String stCd;

	private String totalAmt;

	private String partialPayment;
	private String amtchkty;

	public String getNotimasCd() {
		return notimasCd;
	}
	public void setNotimasCd(String notimasCd) {
		this.notimasCd = notimasCd;
	}
	public String getChaCd() {
		return chaCd;
	}
	public void setChaCd(String chaCd) {
		this.chaCd = chaCd;
	}
	public String getVaNo() {
		return vaNo;
	}
	public void setVaNo(String vaNo) {
		this.vaNo = vaNo;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	public int getSubTot() {
		return subTot;
	}
	public void setSubTot(int subTot) {
		this.subTot = subTot;
	}
	public String getPayItemName() {
		return payItemName;
	}
	public void setPayItemName(String payItemName) {
		this.payItemName = payItemName;
	}
	public String getNotidetCd() {
		return notidetCd;
	}
	public void setNotidetCd(String notidetCd) {
		this.notidetCd = notidetCd;
	}
	public String getNotidetSt() {
		return notidetSt;
	}
	public void setNotidetSt(String notidetSt) {
		this.notidetSt = notidetSt;
	}
	public String getPayItemAmt() {
		return payItemAmt;
	}
	public void setPayItemAmt(String payItemAmt) {
		this.payItemAmt = payItemAmt;
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
	public String getRn() {
		return rn;
	}
	public void setRn(String rn) {
		this.rn = rn;
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
	public String getMasMonth() {
		return masMonth;
	}
	public void setMasMonth(String masMonth) {
		this.masMonth = masMonth;
	}
	public int getDetCnt() {
		return detCnt;
	}
	public void setDetCnt(int detCnt) {
		this.detCnt = detCnt;
	}
	public String getNotimasSt() {
		return notimasSt;
	}
	public void setNotimasSt(String notimasSt) {
		this.notimasSt = notimasSt;
	}
	public String getPgServiceId() {
		return pgServiceId;
	}
	public void setPgServiceId(String pgServiceId) {
		this.pgServiceId = pgServiceId;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getCusKey() {
		return cusKey;
	}
	public void setCusKey(String cusKey) {
		this.cusKey = cusKey;
	}
	public String getCusHp() {
		return cusHp;
	}
	public void setCusHp(String cusHp) {
		this.cusHp = cusHp;
	}
	public String getCusMail() {
		return cusMail;
	}
	public void setCusMail(String cusMail) {
		this.cusMail = cusMail;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getRcpMasCd() {
		return rcpMasCd;
	}
	public void setRcpMasCd(String rcpMasCd) {
		this.rcpMasCd = rcpMasCd;
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
	public String getRcpAmt() {
		return rcpAmt;
	}
	public void setRcpAmt(String rcpAmt) {
		this.rcpAmt = rcpAmt;
	}
	public int getUnSubTot() {
		return unSubTot;
	}
	public void setUnSubTot(int unSubTot) {
		this.unSubTot = unSubTot;
	}
	public String getNotimasNm() {
		return notimasNm;
	}
	public void setNotimasNm(String notimasNm) {
		this.notimasNm = notimasNm;
	}
	public String getToDay() {
		return toDay;
	}
	public void setToDay(String toDay) {
		this.toDay = toDay;
	}
	public String getStCd() {
		return stCd;
	}
	public void setStCd(String stCd) {
		this.stCd = stCd;
	}
	public String getPgLicenKey() {
		return pgLicenKey;
	}
	public void setPgLicenKey(String pgLicenKey) {
		this.pgLicenKey = pgLicenKey;
	}

	public String getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getPartialPayment() {
		return partialPayment;
	}

	public void setPartialPayment(String partialPayment) {
		this.partialPayment = partialPayment;
	}

	public String getAmtchkty() {
		return amtchkty;
	}

	public void setAmtchkty(String amtchkty) {
		this.amtchkty = amtchkty;
	}
}
