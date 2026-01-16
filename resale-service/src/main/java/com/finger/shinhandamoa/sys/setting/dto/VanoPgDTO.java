package com.finger.shinhandamoa.sys.setting.dto;

public class VanoPgDTO {
    private static final long serialVersionUID = 1L;

    private Long pgvareqId;
    private String chaCd;
    private String mchtId;
    private String appendYn;
    private String assignDt;
    private String reqDt;
    private String sendDt;
    private String recvDt;
    private String remark;
    private Long vanoCount;
    private Long pgSeq;
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

    public Long getPgvareqId() {
        return pgvareqId;
    }

    public void setPgvareqId(Long pgvareqId) {
        this.pgvareqId = pgvareqId;
    }

    public String getChaCd() {
        return chaCd;
    }

    public void setChaCd(String chaCd) {
        this.chaCd = chaCd;
    }

    public String getMchtId() {
        return mchtId;
    }

    public void setMchtId(String mchtId) {
        this.mchtId = mchtId;
    }

    public String getAppendYn() {
        return appendYn;
    }

    public void setAppendYn(String appendYn) {
        this.appendYn = appendYn;
    }

    public String getAssignDt() {
        return assignDt;
    }

    public void setAssignDt(String assignDt) {
        this.assignDt = assignDt;
    }

    public String getReqDt() {
        return reqDt;
    }

    public void setReqDt(String reqDt) {
        this.reqDt = reqDt;
    }

    public String getSendDt() {
        return sendDt;
    }

    public void setSendDt(String sendDt) {
        this.sendDt = sendDt;
    }

    public String getRecvDt() {
        return recvDt;
    }

    public void setRecvDt(String recvDt) {
        this.recvDt = recvDt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getVanoCount() {
        return vanoCount;
    }

    public void setVanoCount(Long vanoCount) {
        this.vanoCount = vanoCount;
    }

    public Long getPgSeq() {
        return pgSeq;
    }

    public void setPgSeq(Long pgSeq) {
        this.pgSeq = pgSeq;
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
}
