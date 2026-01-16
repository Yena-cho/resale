package com.finger.shinhandamoa.org.claimMgmt.dto;

import java.util.List;

public class ClaimDTO {

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
    private String notiCanYn;    // 청구상태

    // 실패 DTO
    private String masMonth;  // 청구월
    private String masDay;      // 청구일자
    private String startDate; // 시작일
    private String endDate;   // 종료일
    private String printDate; // 출력일
    private String xRow;      // 행번호
    private String xCount;    // 항목건수
    private String xAmt;      // 항목금액
    private String result;    // 결과

    private String notiDetCd;
    private String payItemCd;
    private String notiMasSt;
    private String payItemName;
    private String payItemAmt;
    private String ptrItemName;
    private String ptrItemRemark;
    private String rn;
    private String cnt;
    private String tbgubn;
    private String remark;
    private int payAmtSum;
    private String rcpGubn;

    private int curPage;
    private int curModalPage;
    private int pageScale;
    private String delYN;
    private String search_orderBy;
    private String selGb;
    private String masStDt;
    private String masEdDt;
    private String cusGubn;
    private String keyWord;
    private String seachCusGubn;
    private String seachCusGubnValue;

    private String monthCheck;
    private String excelClaimYear;
    private String excelClaimMonth;
    private String insPeriod;
    private String excelStartDate;
    private String excelEndDate;
    private String printCheck;
    private String excelPrintDate;
    private String selCusCk;
    private String year;
    private String month;
    private String notSmsYn;
    private String rcpAmt;

    private List<String> itemList;
    private List<String> idxList;
    private List<String> strList;

    private List<ClaimItemDTO> detailsList;

    private String viewType;

    private List<String> payList;

    private List<String> searchValueList;
    private List<String> cusGubnValueList;

    private String searchValue;
    private String cusGubnValue;

    private String payItemRcpAmt;
    private String notiDetSt;

    public String getPayItemRcpAmt() {
        return payItemRcpAmt;
    }

    public void setPayItemRcpAmt(String payItemRcpAmt) {
        this.payItemRcpAmt = payItemRcpAmt;
    }

    public String getNotiDetSt() {
        return notiDetSt;
    }

    public void setNotiDetSt(String notiDetSt) {
        this.notiDetSt = notiDetSt;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getCusGubnValue() {
        return cusGubnValue;
    }

    public void setCusGubnValue(String cusGubnValue) {
        this.cusGubnValue = cusGubnValue;
    }

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

    public String getNotiCanYn() {
        return notiCanYn;
    }

    public void setNotiCanYn(String notiCanYn) {
        this.notiCanYn = notiCanYn;
    }

    public String getMasMonth() {
        return masMonth;
    }

    public void setMasMonth(String masMonth) {
        this.masMonth = masMonth;
    }

    public String getMasDay() {
        return masDay;
    }

    public void setMasDay(String masDay) {
        this.masDay = masDay;
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

    public String getxRow() {
        return xRow;
    }

    public void setxRow(String xRow) {
        this.xRow = xRow;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getNotiDetCd() {
        return notiDetCd;
    }

    public void setNotiDetCd(String notiDetCd) {
        this.notiDetCd = notiDetCd;
    }

    public String getPayItemCd() {
        return payItemCd;
    }

    public void setPayItemCd(String payItemCd) {
        this.payItemCd = payItemCd;
    }

    public String getNotiMasSt() {
        return notiMasSt;
    }

    public void setNotiMasSt(String notiMasSt) {
        this.notiMasSt = notiMasSt;
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

    public String getTbgubn() {
        return tbgubn;
    }

    public void setTbgubn(String tbgubn) {
        this.tbgubn = tbgubn;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPayAmtSum() {
        return payAmtSum;
    }

    public void setPayAmtSum(int payAmtSum) {
        this.payAmtSum = payAmtSum;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getCurModalPage() {
        return curModalPage;
    }

    public void setCurModalPage(int curModalPage) {
        this.curModalPage = curModalPage;
    }

    public int getPageScale() {
        return pageScale;
    }

    public void setPageScale(int pageScale) {
        this.pageScale = pageScale;
    }

    public String getDelYN() {
        return delYN;
    }

    public void setDelYN(String delYN) {
        this.delYN = delYN;
    }

    public String getSearch_orderBy() {
        return search_orderBy;
    }

    public void setSearch_orderBy(String search_orderBy) {
        this.search_orderBy = search_orderBy;
    }

    public String getSelGb() {
        return selGb;
    }

    public void setSelGb(String selGb) {
        this.selGb = selGb;
    }

    public String getMasStDt() {
        return masStDt;
    }

    public void setMasStDt(String masStDt) {
        this.masStDt = masStDt;
    }

    public String getMasEdDt() {
        return masEdDt;
    }

    public void setMasEdDt(String masEdDt) {
        this.masEdDt = masEdDt;
    }

    public String getCusGubn() {
        return cusGubn;
    }

    public void setCusGubn(String cusGubn) {
        this.cusGubn = cusGubn;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getMonthCheck() {
        return monthCheck;
    }

    public void setMonthCheck(String monthCheck) {
        this.monthCheck = monthCheck;
    }

    public String getExcelClaimYear() {
        return excelClaimYear;
    }

    public void setExcelClaimYear(String excelClaimYear) {
        this.excelClaimYear = excelClaimYear;
    }

    public String getExcelClaimMonth() {
        return excelClaimMonth;
    }

    public void setExcelClaimMonth(String excelClaimMonth) {
        this.excelClaimMonth = excelClaimMonth;
    }

    public String getInsPeriod() {
        return insPeriod;
    }

    public void setInsPeriod(String insPeriod) {
        this.insPeriod = insPeriod;
    }

    public String getExcelStartDate() {
        return excelStartDate;
    }

    public void setExcelStartDate(String excelStartDate) {
        this.excelStartDate = excelStartDate;
    }

    public String getExcelEndDate() {
        return excelEndDate;
    }

    public void setExcelEndDate(String excelEndDate) {
        this.excelEndDate = excelEndDate;
    }

    public String getPrintCheck() {
        return printCheck;
    }

    public void setPrintCheck(String printCheck) {
        this.printCheck = printCheck;
    }

    public String getExcelPrintDate() {
        return excelPrintDate;
    }

    public void setExcelPrintDate(String excelPrintDate) {
        this.excelPrintDate = excelPrintDate;
    }

    public String getSelCusCk() {
        return selCusCk;
    }

    public void setSelCusCk(String selCusCk) {
        this.selCusCk = selCusCk;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<String> getItemList() {
        return itemList;
    }

    public void setItemList(List<String> itemList) {
        this.itemList = itemList;
    }

    public List<String> getIdxList() {
        return idxList;
    }

    public void setIdxList(List<String> idxList) {
        this.idxList = idxList;
    }

    public List<String> getStrList() {
        return strList;
    }

    public void setStrList(List<String> strList) {
        this.strList = strList;
    }

    public String getNotSmsYn() {
        return notSmsYn;
    }

    public void setNotSmsYn(String notSmsYn) {
        this.notSmsYn = notSmsYn;
    }

    public String getRcpGubn() {
        return rcpGubn;
    }

    public void setRcpGubn(String rcpGubn) {
        this.rcpGubn = rcpGubn;
    }

    public String getRcpAmt() {
        return rcpAmt;
    }

    public void setRcpAmt(String rcpAmt) {
        this.rcpAmt = rcpAmt;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getSeachCusGubn() {
        return seachCusGubn;
    }

    public void setSeachCusGubn(String seachCusGubn) {
        this.seachCusGubn = seachCusGubn;
    }

    public String getSeachCusGubnValue() {
        return seachCusGubnValue;
    }

    public void setSeachCusGubnValue(String seachCusGubnValue) {
        this.seachCusGubnValue = seachCusGubnValue;
    }

    public List<ClaimItemDTO> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<ClaimItemDTO> detailsList) {
        this.detailsList = detailsList;
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

    public List<String> getPayList() {
        return payList;
    }

    public void setPayList(List<String> payList) {
        this.payList = payList;
    }
}

