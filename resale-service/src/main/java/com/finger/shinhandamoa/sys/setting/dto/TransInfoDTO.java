package com.finger.shinhandamoa.sys.setting.dto;

import java.util.List;

/**
 * @author  신재현
 * @date    2018. 11. 29.
 * @desc    가상계좌전문 조회용
 * @version 
 */
public class TransInfoDTO {

	private String chacd;
	private String vano;
	private String fgcd;

	private int rn;
	private int cnt;	
	private int curPage;	
	private int pageScale;	
	private String searchOrderBy;
	private String startday;
	private String endday;
	private String succSt;
	private String sndrcvSt;
	private List<String> sndrcvList;
	private List<String> tyList;
	//입금전문
	private String dealSeqNo;
	private String transDt;
	private String transTime;
	private String trCd;
	private String destination;
	private String msgTy;
	private String msgTy1;
	private String resCd;
	private String resMsg;
	private String bnkCd;
	private String bnkNm;
	private String withDrawalVano;
	private String depositNm;
	private String withDrawalNm;
	private long money;
	//개시전문 only
	private String transDt2;
	private String transTy;
	private String transTyNm;
	private String holiday;

	public String getFgcd() {
		return fgcd;
	}

	public void setFgcd(String fgcd) {
		this.fgcd = fgcd;
	}

	public List<String> getSndrcvList() {
		return sndrcvList;
	}

	public void setSndrcvList(List<String> sndrcvList) {
		this.sndrcvList = sndrcvList;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public String getTransDt2() {
		return transDt2;
	}

	public void setTransDt2(String transDt2) {
		this.transDt2 = transDt2;
	}

	public String getTransTy() {
		return transTy;
	}

	public void setTransTy(String transTy) {
		this.transTy = transTy;
	}

	public String getTransTyNm() {
		return transTyNm;
	}

	public void setTransTyNm(String transTyNm) {
		this.transTyNm = transTyNm;
	}

	public String getHoliday() {
		return holiday;
	}

	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}

	public String getBnkNm() {
		return bnkNm;
	}

	public void setBnkNm(String bnkNm) {
		this.bnkNm = bnkNm;
	}

	public String getDealSeqNo() {
		return dealSeqNo;
	}

	public void setDealSeqNo(String dealSeqNo) {
		this.dealSeqNo = dealSeqNo;
	}

	public String getWithDrawalVano() {
		return withDrawalVano;
	}

	public void setWithDrawalVano(String withDrawalVano) {
		this.withDrawalVano = withDrawalVano;
	}

	public String getChacd() {
		return chacd;
	}

	public void setChacd(String chacd) {
		this.chacd = chacd;
	}

	public String getVano() {
		return vano;
	}

	public void setVano(String vano) {
		this.vano = vano;
	}

	public int getRn() {
		return rn;
	}

	public void setRn(int rn) {
		this.rn = rn;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
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

	public String getSuccSt() {
		return succSt;
	}

	public void setSuccSt(String succSt) {
		this.succSt = succSt;
	}

	public String getSndrcvSt() {
		return sndrcvSt;
	}

	public void setSndrcvSt(String sndrcvSt) {
		this.sndrcvSt = sndrcvSt;
	}

	public List<String> getTyList() {
		return tyList;
	}

	public void setTyList(List<String> tyList) {
		this.tyList = tyList;
	}

	public String getTransDt() {
		return transDt;
	}

	public void setTransDt(String transDt) {
		this.transDt = transDt;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getTrCd() {
		return trCd;
	}

	public void setTrCd(String trCd) {
		this.trCd = trCd;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getMsgTy() {
		return msgTy;
	}

	public void setMsgTy(String msgTy) {
		this.msgTy = msgTy;
	}

	public String getMsgTy1() {
		return msgTy1;
	}

	public void setMsgTy1(String msgTy1) {
		this.msgTy1 = msgTy1;
	}

	public String getResCd() {
		return resCd;
	}

	public void setResCd(String resCd) {
		this.resCd = resCd;
	}

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

	public String getBnkCd() {
		return bnkCd;
	}

	public void setBnkCd(String bnkCd) {
		this.bnkCd = bnkCd;
	}

	public String getDepositNm() {
		return depositNm;
	}

	public void setDepositNm(String depositNm) {
		this.depositNm = depositNm;
	}

	public String getWithDrawalNm() {
		return withDrawalNm;
	}

	public void setWithDrawalNm(String withDrawalNm) {
		this.withDrawalNm = withDrawalNm;
	}

	public long getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = Long.parseLong(money.replaceAll(",", ""));
	}

}
