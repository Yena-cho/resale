package com.finger.shinhandamoa.org.main.dto;


/**
 * @author  by puki
 * @date    2018. 4. 28.
 * @desc    최초생성
 * @version 
 * 
 */
public class XmontSumDTO {

	private String chaCd        = "";
	private String month        = "";
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
	
	private long   lmscnt       = 0;
	private long   lmsfee       = 0;
	
	private String witReqDt     = "";
	private String rcpDt        = "";
	private String rcpSt        = "";
	private String rcpFailReson = ""; 
	
	private float   rcpRatio1    = 0.0f;  //납입율
	private float   rcpRatio2    = 0.0f;  //미납율
	private String  overRcpYn    = "";
	
	private int rn;
	private int cnt;
	
	private int curPage;	
	private int pageScale;	
	private String searchOrderBy;	
	
	private long   totsumfee    = 0;
	
	private String chaname         = "";
	
	private long totchacnt        = 0l;
	private long lastmonregcnt    = 0l;
	private long thismonregcnt    = 0l;
	private long lastmonresigncnt = 0l;
	private long thismonresigncnt = 0l;
	private long thismonreadycnt  = 0l;
	private long lastmonsmscnt    = 0l;
	private long thismonsmscnt    = 0l;
	private long lastmonlmscnt    = 0l;
	private long thismonlmscnt    = 0l;
	private long usesmsaplcnt     = 0l;
	private long serviceaskcnt    = 0l;
	private long phonebookingcnt  = 0l; 
	private String day;
	private String result;
	private String today;

	private long changeChaCnt     = 0L;

	public long getChangeChaCnt() {
		return changeChaCnt;
	}

	public void setChangeChaCnt(long changeChaCnt) {
		this.changeChaCnt = changeChaCnt;
	}

	public String getChaCd() {
		return chaCd;
	}
	public void setChaCd(String chaCd) {
		this.chaCd = chaCd;
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
	public long getTotchacnt() {
		return totchacnt;
	}
	public void setTotchacnt(long totchacnt) {
		this.totchacnt = totchacnt;
	}
	public long getLastmonregcnt() {
		return lastmonregcnt;
	}
	public void setLastmonregcnt(long lastmonregcnt) {
		this.lastmonregcnt = lastmonregcnt;
	}
	public long getThismonregcnt() {
		return thismonregcnt;
	}
	public void setThismonregcnt(long thismonregcnt) {
		this.thismonregcnt = thismonregcnt;
	}
	public long getLastmonresigncnt() {
		return lastmonresigncnt;
	}
	public void setLastmonresigncnt(long lastmonresigncnt) {
		this.lastmonresigncnt = lastmonresigncnt;
	}
	public long getThismonresigncnt() {
		return thismonresigncnt;
	}
	public void setThismonresigncnt(long thismonresigncnt) {
		this.thismonresigncnt = thismonresigncnt;
	}
	public long getThismonreadycnt() {
		return thismonreadycnt;
	}
	public void setThismonreadycnt(long thismonreadycnt) {
		this.thismonreadycnt = thismonreadycnt;
	}
	public long getLastmonsmscnt() {
		return lastmonsmscnt;
	}
	public void setLastmonsmscnt(long lastmonsmscnt) {
		this.lastmonsmscnt = lastmonsmscnt;
	}
	public long getThismonsmscnt() {
		return thismonsmscnt;
	}
	public void setThismonsmscnt(long thismonsmscnt) {
		this.thismonsmscnt = thismonsmscnt;
	}
	public long getLastmonlmscnt() {
		return lastmonlmscnt;
	}
	public void setLastmonlmscnt(long lastmonlmscnt) {
		this.lastmonlmscnt = lastmonlmscnt;
	}
	public long getThismonlmscnt() {
		return thismonlmscnt;
	}
	public void setThismonlmscnt(long thismonlmscnt) {
		this.thismonlmscnt = thismonlmscnt;
	}
	public long getServiceaskcnt() {
		return serviceaskcnt;
	}
	public void setServiceaskcnt(long serviceaskcnt) {
		this.serviceaskcnt = serviceaskcnt;
	}
	public long getPhonebookingcnt() {
		return phonebookingcnt;
	}
	public void setPhonebookingcnt(long phonebookingcnt) {
		this.phonebookingcnt = phonebookingcnt;
	}
	public long getUsesmsaplcnt() {
		return usesmsaplcnt;
	}
	public void setUsesmsaplcnt(long usesmsaplcnt) {
		this.usesmsaplcnt = usesmsaplcnt;
	}
	public String getChaname() {
		return chaname;
	}
	public void setChaname(String chaname) {
		this.chaname = chaname;
	}
	public String getWitReqDt() {
		return witReqDt;
	}
	public void setWitReqDt(String witReqDt) {
		this.witReqDt = witReqDt;
	}
	public String getRcpDt() {
		return rcpDt;
	}
	public void setRcpDt(String rcpDt) {
		this.rcpDt = rcpDt;
	}
	public String getRcpSt() {
		return rcpSt;
	}
	public void setRcpSt(String rcpSt) {
		this.rcpSt = rcpSt;
	}
	public String getRcpFailReson() {
		return rcpFailReson;
	}
	public void setRcpFailReson(String rcpFailReson) {
		this.rcpFailReson = rcpFailReson;
	}
	public long getTotsumfee() {
		return totsumfee;
	}
	public void setTotsumfee(long totsumfee) {
		this.totsumfee = totsumfee;
	}
	public long getLmscnt() {
		return lmscnt;
	}
	public void setLmscnt(long lmscnt) {
		this.lmscnt = lmscnt;
	}
	public long getLmsfee() {
		return lmsfee;
	}
	public void setLmsfee(long lmsfee) {
		this.lmsfee = lmsfee;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getToday() {
		return today;
	}
	public void setToday(String today) {
		this.today = today;
	}
	
}
