package com.finger.shinhandamoa.sys.setting.dto;

public class ChaUpdateDTO {
    private String pullDt;
    private String mchtId;
    private String chaCd;

    private String rcpDueChk;
    private String clientIdNo;
    private String limitDay;
    private String limitDayCnt;
    private String limitOnce;
    private String amtchkty;
    private String regDt;
    private String assignDt;
    private String assignYn;
    private Long pgPullSeq;


    //페이징용 변수

    private String searchValue;
    private String searchGb;
    private String orderBy;
    private int curPage;
    private int pageScale;
    private int pageNo;
    private String searchOrderBy;
    private String rn;
    private String startDate;
    private String endDate;
    private String chaName;
    private String masStDay; //가상계좌 요청일 시작
    private String masDtDay; //가상계좌 요청일 마감

    private int start; //페이징용 변수
    private int end; //페이징용 변수

    public String getRcpDueChk() {
        return rcpDueChk;
    }

    public void setRcpDueChk(String rcpDueChk) {
        this.rcpDueChk = rcpDueChk;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getSearchGb() {
        return searchGb;
    }

    public void setSearchGb(String searchGb) {
        this.searchGb = searchGb;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
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

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getSearchOrderBy() {
        return searchOrderBy;
    }

    public void setSearchOrderBy(String searchOrderBy) {
        this.searchOrderBy = searchOrderBy;
    }

    public String getRn() {
        return rn;
    }

    public void setRn(String rn) {
        this.rn = rn;
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

    public String getChaName() {
        return chaName;
    }

    public void setChaName(String chaName) {
        this.chaName = chaName;
    }

    public String getMasStDay() {
        return masStDay;
    }

    public void setMasStDay(String masStDay) {
        this.masStDay = masStDay;
    }

    public String getMasDtDay() {
        return masDtDay;
    }

    public void setMasDtDay(String masDtDay) {
        this.masDtDay = masDtDay;
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

    public Long getPgPullSeq() {
        return pgPullSeq;
    }

    public void setPgPullSeq(Long pgPullSeq) {
        this.pgPullSeq = pgPullSeq;
    }

    public String getPullDt() {
        return pullDt;
    }

    public void setPullDt(String pullDt) {
        this.pullDt = pullDt;
    }

    public String getMchtId() {
        return mchtId;
    }

    public void setMchtId(String mchtId) {
        this.mchtId = mchtId;
    }

    public String getChaCd() {
        return chaCd;
    }

    public void setChaCd(String chaCd) {
        this.chaCd = chaCd;
    }

    public String getClientIdNo() {
        return clientIdNo;
    }

    public void setClientIdNo(String clientIdNo) {
        this.clientIdNo = clientIdNo;
    }

    public String getLimitDay() {
        return limitDay;
    }

    public void setLimitDay(String limitDay) {
        this.limitDay = limitDay;
    }

    public String getLimitDayCnt() {
        return limitDayCnt;
    }

    public void setLimitDayCnt(String limitDayCnt) {
        this.limitDayCnt = limitDayCnt;
    }

    public String getLimitOnce() {
        return limitOnce;
    }

    public void setLimitOnce(String limitOnce) {
        this.limitOnce = limitOnce;
    }

    public String getAmtchkty() {
        return amtchkty;
    }

    public void setAmtchkty(String amtchkty) {
        this.amtchkty = amtchkty;
    }

    public String getRegDt() {
        return regDt;
    }

    public void setRegDt(String regDt) {
        this.regDt = regDt;
    }

    public String getAssignDt() {
        return assignDt;
    }

    public void setAssignDt(String assignDt) {
        this.assignDt = assignDt;
    }

    public String getAssignYn() {
        return assignYn;
    }

    public void setAssignYn(String assignYn) {
        this.assignYn = assignYn;
    }
}
