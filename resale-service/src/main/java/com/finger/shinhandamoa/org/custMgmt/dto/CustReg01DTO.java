package com.finger.shinhandamoa.org.custMgmt.dto;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

public class CustReg01DTO {

    // 가상계좌 납부자 DTO
    private String notiMasCd;    // 원장코드
    private String chaCd;        // 가맹점코드
    private String vano;        // 가상계좌번호
    private String masKey;        // 청구거래번호-청구key
    private String cusKey;        // 고객거래번호-고객key
    private String cusGubn1;    // 참조1
    private String cusGubn2;    // 참조2
    private String cusGubn3;    // 참조3
    private String cusGubn4;    // 참조4
    private String cusName;        // 고객명
    private String cusHp;        // 고객핸드폰번호
    private String cusMail;        // 고객메일주소
    private String smsYn;        // 문자전송여부
    private String mailYn;        // 전자메일여부
    private String cusOffNo;    // 현금영수증발행고객정보
    private String makeDt;        // 조작일시
    private String maker;        // 조작자
    private String regDt;        // 등록일시
    private String disabled;    // 납부자삭제여부

    /*2018.04.18 추가*/
    private String rcpGubn;        // 납부여부['Y':납부대상(default) N:납부제외]
    private String rcpReqTy;    // 현금영수증자동발급여부('A':자동,'M':수동(default))
    private String memo;

    /*화면 출력용 컬럼*/
    private String cusType;        // 현금영수증발급용도
    private String cusMtd;        // 현금영수증발급방법

    private String rn;
    private String cnt;

    private int curPage;
    private int pageScale;
    private String searchOrderBy;
    private String searchGb;
    private String searchValue;
    private String calDateFrom;
    private String calDateTo;
    private String cusGubn;
    private String payRadio;
    private String statRadio;
    private String rcpReqRadio;
    private String cusGubnValue;

    //VALIST
    private String fiCd;
    private String fitxCd;
    private String cusCd;
    private String useYn;
    private String varRgTy;
    private String remark;
    private String useDt;

    private String role;
    private String user;

    //Excel
    private String xRow;
    private String result;
    private String passVano;
    private String passCushp;
    private String jobType;   // B : 일괄 정상고객등록(엑셀등록아님)및 일괄선택삭제
    private String cashShow;
    private int cusGubnCnt;

    // 실패 DTO
    private String masMonth;  // 청구월
    private String startDate; // 시작일
    private String endDate;   // 종료일
    private String printDate; // 출력일
    private String xCount;    // 항목건수
    private String xAmt;      // 항목금액
    private String notiDetCd;
    private String payItemName;
    private String payItemAmt;
    private String ptrItemName;
    private String ptrItemRemark;
    private String totAmt;
    private String tbgubn;
    private String confirm;
    private String beCusName;

    private List<String> payList;
    private List<String> cusList;
    private List<String> monList;

    private List<String> searchValueList;
    private List<String> cusGubnValueList;

    public String getNotiMasCd() {
        return notiMasCd;
    }

    public void setNotiMasCd(String notiMasCd) {
        this.notiMasCd = notiMasCd;
    }

    public String getChaCd() {
        return chaCd;
    }

    public void setChaCd(String chaCd) {
        this.chaCd = chaCd;
    }

    public String getVano() {
        return vano;
    }

    public void setVano(String vano) {
        this.vano = vano;
    }

    public String getMasKey() {
        return masKey;
    }

    public void setMasKey(String masKey) {
        this.masKey = masKey;
    }

    public String getCusKey() {
        return cusKey;
    }

    public void setCusKey(String cusKey) {
        this.cusKey = cusKey;
    }

    public String getCusGubn1() {
        return cusGubn1;
    }

    public void setCusGubn1(String cusGubn1) {
        this.cusGubn1 = cusGubn1;
    }

    public String getCusGubn2() {
        return cusGubn2;
    }

    public void setCusGubn2(String cusGubn2) {
        this.cusGubn2 = cusGubn2;
    }

    public String getCusGubn3() {
        return cusGubn3;
    }

    public void setCusGubn3(String cusGubn3) {
        this.cusGubn3 = cusGubn3;
    }

    public String getCusGubn4() {
        return cusGubn4;
    }

    public void setCusGubn4(String cusGubn4) {
        this.cusGubn4 = cusGubn4;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
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

    public String getSmsYn() {
        return smsYn;
    }

    public void setSmsYn(String smsYn) {
        this.smsYn = smsYn;
    }

    public String getMailYn() {
        return mailYn;
    }

    public void setMailYn(String mailYn) {
        this.mailYn = mailYn;
    }

    public String getCusOffNo() {
        return cusOffNo;
    }

    public void setCusOffNo(String cusOffNo) {
        this.cusOffNo = cusOffNo;
    }

    public String getMakeDt() {
        return makeDt;
    }

    public void setMakeDt(String makeDt) {
        this.makeDt = makeDt;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getRegDt() {
        return regDt;
    }

    public void setRegDt(String regDt) {
        this.regDt = regDt;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getRcpGubn() {
        return rcpGubn;
    }

    public void setRcpGubn(String rcpGubn) {
        this.rcpGubn = rcpGubn;
    }

    public String getRcpReqTy() {
        return rcpReqTy;
    }

    public void setRcpReqTy(String rcpReqTy) {
        this.rcpReqTy = rcpReqTy;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCusType() {
        return cusType;
    }

    public void setCusType(String cusType) {
        this.cusType = cusType;
    }

    public String getCusMtd() {
        return cusMtd;
    }

    public void setCusMtd(String cusMtd) {
        this.cusMtd = cusMtd;
    }

    public String getRn() {
        return rn;
    }

    public void setRn(String rn) {
        this.rn = rn;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
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

    public String getCalDateFrom() {
        return calDateFrom;
    }

    public void setCalDateFrom(String calDateFrom) {
        this.calDateFrom = calDateFrom;
    }

    public String getCalDateTo() {
        return calDateTo;
    }

    public void setCalDateTo(String calDateTo) {
        this.calDateTo = calDateTo;
    }

    public String getCusGubn() {
        return cusGubn;
    }

    public void setCusGubn(String cusGubn) {
        this.cusGubn = cusGubn;
    }

    public String getPayRadio() {
        return payRadio;
    }

    public void setPayRadio(String payRadio) {
        this.payRadio = payRadio;
    }

    public String getStatRadio() {
        return statRadio;
    }

    public void setStatRadio(String statRadio) {
        this.statRadio = statRadio;
    }

    public String getRcpReqRadio() {
        return rcpReqRadio;
    }

    public void setRcpReqRadio(String rcpReqRadio) {
        this.rcpReqRadio = rcpReqRadio;
    }

    public String getCusGubnValue() {
        return cusGubnValue;
    }

    public void setCusGubnValue(String cusGubnValue) {
        this.cusGubnValue = cusGubnValue;
    }

    public String getFiCd() {
        return fiCd;
    }

    public void setFiCd(String fiCd) {
        this.fiCd = fiCd;
    }

    public String getFitxCd() {
        return fitxCd;
    }

    public void setFitxCd(String fitxCd) {
        this.fitxCd = fitxCd;
    }

    public String getCusCd() {
        return cusCd;
    }

    public void setCusCd(String cusCd) {
        this.cusCd = cusCd;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getVarRgTy() {
        return varRgTy;
    }

    public void setVarRgTy(String varRgTy) {
        this.varRgTy = varRgTy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUseDt() {
        return useDt;
    }

    public void setUseDt(String useDt) {
        this.useDt = useDt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getxRow() {
        return xRow;
    }

    public void setxRow(String xRow) {
        this.xRow = xRow;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPassVano() {
        return passVano;
    }

    public void setPassVano(String passVano) {
        this.passVano = passVano;
    }

    public String getPassCushp() {
        return passCushp;
    }

    public void setPassCushp(String passCushp) {
        this.passCushp = passCushp;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getMasMonth() {
        return masMonth;
    }

    public void setMasMonth(String masMonth) {
        this.masMonth = masMonth;
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

    public String getPrintDate() {
        return printDate;
    }

    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }

    public String getxCount() {
        return xCount;
    }

    public void setxCount(String xCount) {
        this.xCount = xCount;
    }

    public String getxAmt() {
        return xAmt;
    }

    public void setxAmt(String xAmt) {
        this.xAmt = xAmt;
    }

    public String getNotiDetCd() {
        return notiDetCd;
    }

    public void setNotiDetCd(String notiDetCd) {
        this.notiDetCd = notiDetCd;
    }

    public String getPayItemName() {
        return payItemName;
    }

    public void setPayItemName(String payItemName) {
        this.payItemName = payItemName;
    }

    public String getPayItemAmt() {
        return payItemAmt;
    }

    public void setPayItemAmt(String payItemAmt) {
        this.payItemAmt = payItemAmt;
    }

    public String getPtrItemName() {
        return ptrItemName;
    }

    public void setPtrItemName(String ptrItemName) {
        this.ptrItemName = ptrItemName;
    }

    public String getPtrItemRemark() {
        return ptrItemRemark;
    }

    public void setPtrItemRemark(String ptrItemRemark) {
        this.ptrItemRemark = ptrItemRemark;
    }

    public String getTotAmt() {
        return totAmt;
    }

    public void setTotAmt(String totAmt) {
        this.totAmt = totAmt;
    }

    public String getTbgubn() {
        return tbgubn;
    }

    public void setTbgubn(String tbgubn) {
        this.tbgubn = tbgubn;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public List<String> getPayList() {
        return payList;
    }

    public void setPayList(List<String> payList) {
        this.payList = payList;
    }

    public List<String> getCusList() {
        return cusList;
    }

    public void setCusList(List<String> cusList) {
        this.cusList = cusList;
    }

    public List<String> getMonList() {
        return monList;
    }

    public void setMonList(List<String> monList) {
        this.monList = monList;
    }

    public String getCashShow() {
        return cashShow;
    }

    public void setCashShow(String cashShow) {
        this.cashShow = cashShow;
    }

    public String getBeCusName() {
        return beCusName;
    }

    public void setBeCusName(String beCusName) {
        this.beCusName = beCusName;
    }

    public int getCusGubnCnt() {
        return cusGubnCnt;
    }

    public void setCusGubnCnt(int cusGubnCnt) {
        this.cusGubnCnt = cusGubnCnt;
    }

    public List<String> getSearchValueList() {
        return searchValueList;
    }

    public void setSearchValueList(List<String> searchValueList) {
        this.searchValueList = searchValueList;
    }

    public List<String> getCusGubnValueList() {
        return cusGubnValueList;
    }

    public void setCusGubnValueList(List<String> cusGubnValueList) {
        this.cusGubnValueList = cusGubnValueList;
    }
}

