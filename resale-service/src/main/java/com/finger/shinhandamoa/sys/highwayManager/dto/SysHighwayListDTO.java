package com.finger.shinhandamoa.sys.highwayManager.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class SysHighwayListDTO {

    private static final long serialVersionUID = 1L;
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
    private String chaCd;
    private String chaName;
    private String masDtDay; //고속도로 가상계좌 해당기간동안 청구를 한번이라도했는지 확인
    private String masStDay; //고속도로 가상계좌 해당기간동안 청구를 한번이라도했는지 확인
    private String assignStDay; //고속도로 가상계좌 할당 시작일
    private String assignDtDay; //고속도로 가상계좌 할당 마감일
    private String trnVanoId;

    private int highwayUnUsedCnt; //고속도로 미사용 가상계좌수
    private int highwayAssignTotalCnt; //고속도로 할당 가상계좌수

    public String getMasDtDay() {
        return masDtDay;
    }

    public void setMasDtDay(String masDtDay) {
        this.masDtDay = masDtDay;
    }

    public String getMasStDay() {
        return masStDay;
    }

    public void setMasStDay(String masStDay) {
        this.masStDay = masStDay;
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

    public String getChaCd() {
        return chaCd;
    }

    public void setChaCd(String chaCd) {
        this.chaCd = chaCd;
    }

    public String getChaName() {
        return chaName;
    }

    public void setChaName(String chaName) {
        this.chaName = chaName;
    }

    public String getTrnVanoId() {
        return trnVanoId;
    }

    public void setTrnVanoId(String trnVanoId) {
        this.trnVanoId = trnVanoId;
    }


    public String getAssignStDay() {
        return assignStDay;
    }

    public void setAssignStDay(String assignStDay) {
        this.assignStDay = assignStDay;
    }

    public String getAssignDtDay() {
        return assignDtDay;
    }

    public void setAssignDtDay(String assignDtDay) {
        this.assignDtDay = assignDtDay;
    }

    public int getHighwayUnUsedCnt() {
        return highwayUnUsedCnt;
    }

    public void setHighwayUnUsedCnt(int highwayUnUsedCnt) {
        this.highwayUnUsedCnt = highwayUnUsedCnt;
    }

    public int getHighwayAssignTotalCnt() {
        return highwayAssignTotalCnt;
    }

    public void setHighwayAssignTotalCnt(int highwayAssignTotalCnt) {
        this.highwayAssignTotalCnt = highwayAssignTotalCnt;
    }


}
