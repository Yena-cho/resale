package com.finger.shinhandamoa.sys.cust.dto;

public class SysCustDTO {

    private long no;
    private int bbs;            //구분값 9 : 전화상담예약
    private int rn;
    private String day;         //신청일시
    private String userClass;   //신청구분 0-전체 1-이용기관 2-납부자/신규
    private String id;          //기관코드//작성자 ID
    private String writer;      //작성자 // 기관명 // 신청자?
    private String data1;       //전화번호 //담당자 연락처
    private String data2;       //상담예약사유코드
    private String data3;       //상담예약사유
    private String data4;       //상태 0-전체 1-대기 2-진행중 3-완료 4-대기/진행 5-대기/완료 6-진행/완료
    private String day1;        //상담일시

    private String data5;       //상담직원
    private String title;       //문의내용
    private String contents;    //답변내용

    //페이징 및 조회용 검색조건변수
    private String searchOption;
    private String keyword;
    private String searchOrderBy;
    private int pageScale;
    private int curPage;
    private String startday;
    private String endday;
    private String fStartDate;
    private String fEndDate;
    private String fid;
    private String fuserClass;
    private String fsearchOption;
    private String fkeyword;
    private String fdata4;
    private String fsearchOrderBy;

    private String chaName;
    private String dataContent;        // 상담예약사유
    private String loginName;            // 기관명
    private String chaCd;                // 기관코드

    public SysCustDTO() {
        super();
    }

    public String getSearchOrderBy() {
        return searchOrderBy;
    }

    public void setSearchOrderBy(String searchOrderBy) {
        this.searchOrderBy = searchOrderBy;
    }

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3;
    }

    public String getData4() {
        return data4;
    }

    public void setData4(String data4) {
        this.data4 = data4;
    }

    public String getData5() {
        return data5;
    }

    public void setData5(String data5) {
        this.data5 = data5;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public String getDay1() {
        return day1;
    }

    public void setDay1(String day1) {
        this.day1 = day1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    // 페이징 및 조회
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

    public String getfStartDate() {
        return fStartDate;
    }

    public void setfStartDate(String fStartDate) {
        this.fStartDate = fStartDate;
    }

    public String getfEndDate() {
        return fEndDate;
    }

    public void setfEndDate(String fEndDate) {
        this.fEndDate = fEndDate;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFuserClass() {
        return fuserClass;
    }

    public void setFuserClass(String fuserClass) {
        this.fuserClass = fuserClass;
    }

    public String getFsearchOption() {
        return fsearchOption;
    }

    public void setFsearchOption(String fsearchOption) {
        this.fsearchOption = fsearchOption;
    }

    public String getFkeyword() {
        return fkeyword;
    }

    public void setFkeyword(String fkeyword) {
        this.fkeyword = fkeyword;
    }

    public String getFdata4() {
        return fdata4;
    }

    public void setFdata4(String fdata4) {
        this.fdata4 = fdata4;
    }

    public String getFsearchOrderBy() {
        return fsearchOrderBy;
    }

    public void setFsearchOrderBy(String fsearchOrderBy) {
        this.fsearchOrderBy = fsearchOrderBy;
    }

    public String getChaName() {
        return chaName;
    }

    public void setChaName(String chaName) {
        this.chaName = chaName;
    }

    public String getDataContent() {
        return dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getChaCd() {
        return chaCd;
    }

    public void setChaCd(String chaCd) {
        this.chaCd = chaCd;
    }
}
