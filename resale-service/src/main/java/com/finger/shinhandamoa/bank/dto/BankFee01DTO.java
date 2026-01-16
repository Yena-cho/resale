package com.finger.shinhandamoa.bank.dto;

import java.util.Date;




public class BankFee01DTO {

	private String chacd        = "";
	private String month        = "";
	private String startDate    = "";
	private String endDate      = "";
	private long   noticnt      = 0;
	private long   notiamt      = 0;
	private long   notifee      = 0;
	private long   rcpcnt       = 0;
	private long   rcpamt       = 0;
	private long   rcpfee       = 0;
	private long   rcpbnkfee    = 0;
	private long   smscnt       = 0;
	private long   smsfee       = 0;
	private String finishyn     = "";	
	private long   rcpcntNot    = 0;
	private long   rcpamtNot    = 0;
	private long   rcpfeeNot    = 0;
	
	private String chaname      = "";
	private String agtcd        = "";
	private String agtname      = "";
	
	private float   rcpRatio1    = 0.0f;  //납입율
	private float   rcpRatio2    = 0.0f;  //미납율
	private String  overRcpYn    = "";
	
	private int rn   = 0;
	private int cnt  = 0;
	
	private int curPage          = 0;	
	private int pageScale        = 0;	
	private String searchOrderBy = "";	
	
	private int  totCnt       = 0;
	private long totNoticnt   = 0;
	private long totNotifee   = 0;
	private long totRcpcnt    = 0;
	private long totRcpfee    = 0;
	private long totFeeSum    = 0;
	
	
	
	
	
	
	
	public String getChacd() {
		return chacd;
	}
	public void setChacd(String chacd) {
		this.chacd = chacd;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public long getNoticnt() {
		return noticnt;
	}
	public void setNoticnt(long noticnt) {
		this.noticnt = noticnt;
	}
	public long getNotiamt() {
		return notiamt;
	}
	public void setNotiamt(long notiamt) {
		this.notiamt = notiamt;
	}
	public long getNotifee() {
		return notifee;
	}
	public void setNotifee(long notifee) {
		this.notifee = notifee;
	}
	public long getRcpcnt() {
		return rcpcnt;
	}
	public void setRcpcnt(long rcpcnt) {
		this.rcpcnt = rcpcnt;
	}
	public long getRcpamt() {
		return rcpamt;
	}
	public void setRcpamt(long rcpamt) {
		this.rcpamt = rcpamt;
	}
	public long getRcpfee() {
		return rcpfee;
	}
	public void setRcpfee(long rcpfee) {
		this.rcpfee = rcpfee;
	}
	public long getRcpbnkfee() {
		return rcpbnkfee;
	}
	public void setRcpbnkfee(long rcpbnkfee) {
		this.rcpbnkfee = rcpbnkfee;
	}
	public long getSmscnt() {
		return smscnt;
	}
	public void setSmscnt(long smscnt) {
		this.smscnt = smscnt;
	}
	public long getSmsfee() {
		return smsfee;
	}
	public void setSmsfee(long smsfee) {
		this.smsfee = smsfee;
	}
	public String getFinishyn() {
		return finishyn;
	}
	public void setFinishyn(String finishyn) {
		this.finishyn = finishyn;
	}
	public long getRcpcntNot() {
		return rcpcntNot;
	}
	public void setRcpcntNot(long rcpcntNot) {
		this.rcpcntNot = rcpcntNot;
	}
	public long getRcpamtNot() {
		return rcpamtNot;
	}
	public void setRcpamtNot(long rcpamtNot) {
		this.rcpamtNot = rcpamtNot;
	}
	public long getRcpfeeNot() {
		return rcpfeeNot;
	}
	public void setRcpfeeNot(long rcpfeeNot) {
		this.rcpfeeNot = rcpfeeNot;
	}
	public float getRcpRatio1() {
		return rcpRatio1;
	}
	public void setRcpRatio1(float rcpRatio1) {
		this.rcpRatio1 = rcpRatio1;
	}
	public float getRcpRatio2() {
		return rcpRatio2;
	}
	public void setRcpRatio2(float rcpRatio2) {
		this.rcpRatio2 = rcpRatio2;
	}

	public String getOverRcpYn() {
		return overRcpYn;
	}
	public void setOverRcpYn(String overRcpYn) {
		this.overRcpYn = overRcpYn;
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
	@Override
	public String toString() {
		return "BankFee01DTO [chacd=" + chacd + ", month=" + month + ", noticnt=" + noticnt + ", notiamt=" + notiamt
				+ ", notifee=" + notifee + ", rcpcnt=" + rcpcnt + ", rcpamt=" + rcpamt + ", rcpfee=" + rcpfee
				+ ", rcpbnkfee=" + rcpbnkfee + ", smscnt=" + smscnt + ", smsfee=" + smsfee + ", finishyn=" + finishyn
				+ ", rcpcntNot=" + rcpcntNot + ", rcpamtNot=" + rcpamtNot + ", rcpfeeNot=" + rcpfeeNot + ", chaname="
				+ chaname + ", agtcd=" + agtcd + ", agtname=" + agtname + ", rcpRatio1=" + rcpRatio1 + ", rcpRatio2="
				+ rcpRatio2 + ", overRcpYn=" + overRcpYn + ", rn=" + rn + ", cnt=" + cnt + ", curPage=" + curPage
				+ ", pageScale=" + pageScale + ", searchOrderBy=" + searchOrderBy + ", totCnt=" + totCnt
				+ ", totNoticnt=" + totNoticnt + ", totNotifee=" + totNotifee + ", totRcpcnt=" + totRcpcnt
				+ ", totRcpfee=" + totRcpfee + ", totFeeSum=" + totFeeSum + "]";
	}
	public String getChaname() {
		return chaname;
	}
	public void setChaname(String chaname) {
		this.chaname = chaname;
	}
	public String getAgtcd() {
		return agtcd;
	}
	public void setAgtcd(String agtcd) {
		this.agtcd = agtcd;
	}
	public String getAgtname() {
		return agtname;
	}
	public void setAgtname(String agtname) {
		this.agtname = agtname;
	}
	public int getTotCnt() {
		return totCnt;
	}
	public void setTotCnt(int totCnt) {
		this.totCnt = totCnt;
	}
	public long getTotNoticnt() {
		return totNoticnt;
	}
	public void setTotNoticnt(long totNoticnt) {
		this.totNoticnt = totNoticnt;
	}
	public long getTotNotifee() {
		return totNotifee;
	}
	public void setTotNotifee(long totNotifee) {
		this.totNotifee = totNotifee;
	}
	public long getTotRcpcnt() {
		return totRcpcnt;
	}
	public void setTotRcpcnt(long totRcpcnt) {
		this.totRcpcnt = totRcpcnt;
	}
	public long getTotRcpfee() {
		return totRcpfee;
	}
	public void setTotRcpfee(long totRcpfee) {
		this.totRcpfee = totRcpfee;
	}
	public long getTotFeeSum() {
		return totFeeSum;
	}
	public void setTotFeeSum(long totFeeSum) {
		this.totFeeSum = totFeeSum;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	
	
	
	
}

