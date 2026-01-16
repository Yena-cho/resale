package com.finger.shinhandamoa.org.receiptmgmt.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReceiptMgmtDTO {
    private String masMonth;
    private String chaCd;
    private String notiMasCd;
    private String vano;
    private String cusKey;
    private String cusName;
    private String cusGubn1;
    private String cusGubn2;
    private String cusGubn3;
    private String cusGubn4;
    private String notiDetCd;
    private String rcpMasCd;
    private String rcpDetCd;
    private String rcpDate;
    private String payItemName;
    private String payItemAmt;
    private String nonPayItemAmt;
    private String tbGubn;
    private String masDay;
    private String payDay;
    private String payTime;
    private String notAmt;
    private String detCnt;
    private String sumAmt;
    private String notiMasSt;
    private String notiMasStNm;
    private String sveCd;
    private String sveCdNm;
    private String rePayAmt;
    private String regDt;
    private String payItemCd;
    private int pageScale;
    private int curPage;
    private String cusGubnValue;
    private String cusGubn;
    private ArrayList<String> svecdList;
    private List<String> payList;
    private String searchValue;
    private String searchGb;
    private String sortGb;
    private String startDate;
    private String endDate;
    private int start;
    private int end;
    private int cnt;
    private long totAmt;
    private long totUnAmt;
    private long rcpAmt;
    private long rcpPayItemAmt;
    private long sumRcpAmt;
    private long unAmt;
    private long amt;
    private String maker;
    private int rn;
    private String claimItemCd;
    private String pageGubn;
    private String fSearchGb;
    private String fSearchValue;
    private String fCusGubn;
    private String fCusGubnValue;
    private String fSortGb;
    private String dateGb;
    private String fDateGb;
    private String payGb1;
    private String svecdGb1;
    private List<String> checkList;
    private List<String> fCheckListValue;
    private String printDate;
    private String orderBy;
    private String notiDetSt;
    private String notiDetStNm;
    private HashMap<String, Object> rec;
    private List<String> notiStList;
    private String payDate;
    private int gubn;
    private String rcpMasSt;
    private String rcpMasStNm;
    private String amtChkTy;
    private String type;
    private String rePayYN;
    private String nottAmt;
    private String rePayDay;
    private String rePayMasCd;
    private String lv;
    private String spanCnt;
    private String fGubn;
    private String fOrderBy;
    private String fType;
    private String fAmtChkTy;
    private String bnkCd;
    private String pgServiceId;
    private String bnkMsgNo;
    private String packetNo;
    private String appDay;
    private String appNo;
    private String cusOffNo;
    private String disabled;
    private String cshAmt;
    private String cshAmt2;
    private String cusOffNo2;
    private String job;
    private String cashMasSt;
    private String cashMasStNm;
    private String appMsg;
    private String appCd;
    private String cashMasCd;
    private String seqCd;
    private String fCashMasSt;
    private long rcpAmt2;
    private String cusType;
    private String confirm;
    private String itemCnt;
    private String notiCanYn;
    private long vasAmt;
    private long ocdAmt;
    private long dcsAmt;
    private long dcdAmt;
    private String chaOffNo;
    private String noTaxYn;
    private String rcpReqTy;
    private String chaTrTy;
    private String ptrItemOrder;
    private long rcpItemAmt;
    private long remAmt;
    private String gubun;
    private String rcpItemYn;
    private String confirm2;
    private String makeDt;
    private String payNoGb;
    private String rcpGubn;
    private String ptrItemRemark;

    private String payItem;
    private String adjFiregKey;
    private String grpadjName;
    private String fPayItem;
    private String fAdjFiregKey;

    private List<String> searchValueList;
    private List<String> cusGubnValueList;
    private List<String> fSearchValueList;
    private List<String> fCusGubnValueList;

    private long cashRcpAmt;
    private long cashRcpAmt2;

    private String realJob;

    private String selectedDate;

    /**
     * 추가
     */
    private String clientIdNo;
    private String txTypeCodeNm;
    private String mediaTypeCodeNm;
    private String txDate;
    private String txNo;
    private long splyAmt;
    private long txAmt;
    private long tax;

    private String histCnt;

    public String getHistCnt() {
        return histCnt;
    }

    public void setHistCnt(String histCnt) {
        this.histCnt = histCnt;
    }


	public String getClientIdNo() {
		return clientIdNo;
	}

	public void setClientIdNo(String clientIdNo) {
		this.clientIdNo = clientIdNo;
	}

	public String getTxTypeCodeNm() {
		return txTypeCodeNm;
	}

	public void setTxTypeCodeNm(String txTypeCodeNm) {
		this.txTypeCodeNm = txTypeCodeNm;
	}

	public String getMediaTypeCodeNm() {
		return mediaTypeCodeNm;
	}

	public void setMediaTypeCodeNm(String mediaTypeCodeNm) {
		this.mediaTypeCodeNm = mediaTypeCodeNm;
	}

	public String getTxDate() {
		return txDate;
	}

	public void setTxDate(String txDate) {
		this.txDate = txDate;
	}

	public String getTxNo() {
		return txNo;
	}

	public void setTxNo(String txNo) {
		this.txNo = txNo;
	}

	public long getSplyAmt() {
		return splyAmt;
	}

	public void setSplyAmt(long splyAmt) {
		this.splyAmt = splyAmt;
	}

	public long getTxAmt() {
		return txAmt;
	}

	public void setTxAmt(long txAmt) {
		this.txAmt = txAmt;
	}

	public long getTax() {
		return tax;
	}

	public void setTax(long tax) {
		this.tax = tax;
	}

	public List<String> getfSearchValueList() {
        return fSearchValueList;
    }

    public void setfSearchValueList(List<String> fSearchValueList) {
        this.fSearchValueList = fSearchValueList;
    }

    public List<String> getfCusGubnValueList() {
        return fCusGubnValueList;
    }

    public void setfCusGubnValueList(List<String> fCusGubnValueList) {
        this.fCusGubnValueList = fCusGubnValueList;
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

    public String getRcpGubn() {
        return rcpGubn;
    }

    public void setRcpGubn(String rcpGubn) {
        this.rcpGubn = rcpGubn;
    }

    public String getMasMonth() {
        return masMonth;
    }

    public void setMasMonth(String masMonth) {
        this.masMonth = masMonth;
    }

    public String getChaCd() {
        return chaCd;
    }

    public void setChaCd(String chaCd) {
        this.chaCd = chaCd;
    }

    public String getNotiMasCd() {
        return notiMasCd;
    }

    public void setNotiMasCd(String notiMasCd) {
        this.notiMasCd = notiMasCd;
    }

    public String getVano() {
        return vano;
    }

    public void setVano(String vano) {
        this.vano = vano;
    }

    public String getCusKey() {
        return cusKey;
    }

    public void setCusKey(String cusKey) {
        this.cusKey = cusKey;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
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

    public String getNotiDetCd() {
        return notiDetCd;
    }

    public void setNotiDetCd(String notiDetCd) {
        this.notiDetCd = notiDetCd;
    }

    public String getRcpMasCd() {
        return rcpMasCd;
    }

    public void setRcpMasCd(String rcpMasCd) {
        this.rcpMasCd = rcpMasCd;
    }

    public String getRcpDetCd() {
        return rcpDetCd;
    }

    public void setRcpDetCd(String rcpDetCd) {
        this.rcpDetCd = rcpDetCd;
    }

    public String getRcpDate() {
        return rcpDate;
    }

    public void setRcpDate(String rcpDate) {
        this.rcpDate = rcpDate;
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

    public String getNonPayItemAmt() {
        return nonPayItemAmt;
    }

    public void setNonPayItemAmt(String nonPayItemAmt) {
        this.nonPayItemAmt = nonPayItemAmt;
    }

    public String getTbGubn() {
        return tbGubn;
    }

    public void setTbGubn(String tbGubn) {
        this.tbGubn = tbGubn;
    }

    public String getMasDay() {
        return masDay;
    }

    public void setMasDay(String masDay) {
        this.masDay = masDay;
    }

    public String getPayDay() {
        return payDay;
    }

    public void setPayDay(String payDay) {
        this.payDay = payDay;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getNotAmt() {
        return notAmt;
    }

    public void setNotAmt(String notAmt) {
        this.notAmt = notAmt;
    }

    public String getDetCnt() {
        return detCnt;
    }

    public void setDetCnt(String detCnt) {
        this.detCnt = detCnt;
    }

    public String getSumAmt() {
        return sumAmt;
    }

    public void setSumAmt(String sumAmt) {
        this.sumAmt = sumAmt;
    }

    public String getSveCd() {
        return sveCd;
    }

    public void setSveCd(String sveCd) {
        this.sveCd = sveCd;
    }

    public String getSveCdNm() {
        return sveCdNm;
    }

    public void setSveCdNm(String sveCdNm) {
        this.sveCdNm = sveCdNm;
    }

    public String getRePayAmt() {
        return rePayAmt;
    }

    public void setRePayAmt(String rePayAmt) {
        this.rePayAmt = rePayAmt;
    }

    public String getRegDt() {
        return regDt;
    }

    public void setRegDt(String regDt) {
        this.regDt = regDt;
    }

    public String getPayItemCd() {
        return payItemCd;
    }

    public void setPayItemCd(String payItemCd) {
        this.payItemCd = payItemCd;
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

    public String getCusGubnValue() {
        return cusGubnValue;
    }

    public void setCusGubnValue(String cusGubnValue) {
        this.cusGubnValue = cusGubnValue;
    }

    public String getCusGubn() {
        return cusGubn;
    }

    public void setCusGubn(String cusGubn) {
        this.cusGubn = cusGubn;
    }

    public ArrayList<String> getSvecdList() {
        return svecdList;
    }

    public void setSvecdList(ArrayList<String> svecdList) {
        this.svecdList = svecdList;
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

    public String getSortGb() {
        return sortGb;
    }

    public void setSortGb(String sortGb) {
        this.sortGb = sortGb;
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

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public int getRn() {
        return rn;
    }

    public void setRn(int rn) {
        this.rn = rn;
    }

    public String getClaimItemCd() {
        return claimItemCd;
    }

    public void setClaimItemCd(String claimItemCd) {
        this.claimItemCd = claimItemCd;
    }

    public String getPageGubn() {
        return pageGubn;
    }

    public void setPageGubn(String pageGubn) {
        this.pageGubn = pageGubn;
    }

    public String getfSearchGb() {
        return fSearchGb;
    }

    public void setfSearchGb(String fSearchGb) {
        this.fSearchGb = fSearchGb;
    }

    public String getfSearchValue() {
        return fSearchValue;
    }

    public void setfSearchValue(String fSearchValue) {
        this.fSearchValue = fSearchValue;
    }

    public String getfCusGubn() {
        return fCusGubn;
    }

    public void setfCusGubn(String fCusGubn) {
        this.fCusGubn = fCusGubn;
    }

    public String getfCusGubnValue() {
        return fCusGubnValue;
    }

    public void setfCusGubnValue(String fCusGubnValue) {
        this.fCusGubnValue = fCusGubnValue;
    }

    public String getfSortGb() {
        return fSortGb;
    }

    public void setfSortGb(String fSortGb) {
        this.fSortGb = fSortGb;
    }

    public String getDateGb() {
        return dateGb;
    }

    public void setDateGb(String dateGb) {
        this.dateGb = dateGb;
    }

    public List<String> getPayList() {
        return payList;
    }

    public void setPayList(List<String> payList) {
        this.payList = payList;
    }

    public String getPayGb1() {
        return payGb1;
    }

    public void setPayGb1(String payGb1) {
        this.payGb1 = payGb1;
    }

    public String getSvecdGb1() {
        return svecdGb1;
    }

    public void setSvecdGb1(String svecdGb1) {
        this.svecdGb1 = svecdGb1;
    }

    public List<String> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<String> checkList) {
        this.checkList = checkList;
    }

    public List<String> getfCheckListValue() {
        return fCheckListValue;
    }

    public void setfCheckListValue(List<String> fCheckListValue) {
        this.fCheckListValue = fCheckListValue;
    }

    public String getfDateGb() {
        return fDateGb;
    }

    public void setfDateGb(String fDateGb) {
        this.fDateGb = fDateGb;
    }

    public String getPrintDate() {
        return printDate;
    }

    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getNotiMasSt() {
        return notiMasSt;
    }

    public void setNotiMasSt(String notiMasSt) {
        this.notiMasSt = notiMasSt;
    }

    public String getNotiMasStNm() {
        return notiMasStNm;
    }

    public void setNotiMasStNm(String notiMasStNm) {
        this.notiMasStNm = notiMasStNm;
    }

    public String getNotiDetSt() {
        return notiDetSt;
    }

    public void setNotiDetSt(String notiDetSt) {
        this.notiDetSt = notiDetSt;
    }

    public String getNotiDetStNm() {
        return notiDetStNm;
    }

    public void setNotiDetStNm(String notiDetStNm) {
        this.notiDetStNm = notiDetStNm;
    }

    public HashMap<String, Object> getRec() {
        return rec;
    }

    public void setRec(HashMap<String, Object> rec) {
        this.rec = rec;
    }

    public List<String> getNotiStList() {
        return notiStList;
    }

    public void setNotiStList(List<String> notiStList) {
        this.notiStList = notiStList;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public int getGubn() {
        return gubn;
    }

    public void setGubn(int gubn) {
        this.gubn = gubn;
    }

    public String getRcpMasSt() {
        return rcpMasSt;
    }

    public void setRcpMasSt(String rcpMasSt) {
        this.rcpMasSt = rcpMasSt;
    }

    public String getRcpMasStNm() {
        return rcpMasStNm;
    }

    public void setRcpMasStNm(String rcpMasStNm) {
        this.rcpMasStNm = rcpMasStNm;
    }

    public String getAmtChkTy() {
        return amtChkTy;
    }

    public void setAmtChkTy(String amtChkTy) {
        this.amtChkTy = amtChkTy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRePayYN() {
        return rePayYN;
    }

    public void setRePayYN(String rePayYN) {
        this.rePayYN = rePayYN;
    }

    public String getNottAmt() {
        return nottAmt;
    }

    public void setNottAmt(String nottAmt) {
        this.nottAmt = nottAmt;
    }

    public String getRePayDay() {
        return rePayDay;
    }

    public void setRePayDay(String rePayDay) {
        this.rePayDay = rePayDay;
    }

    public String getRePayMasCd() {
        return rePayMasCd;
    }

    public void setRePayMasCd(String rePayMasCd) {
        this.rePayMasCd = rePayMasCd;
    }

    public String getLv() {
        return lv;
    }

    public void setLv(String lv) {
        this.lv = lv;
    }

    public String getSpanCnt() {
        return spanCnt;
    }

    public void setSpanCnt(String spanCnt) {
        this.spanCnt = spanCnt;
    }

    public String getfGubn() {
        return fGubn;
    }

    public void setfGubn(String fGubn) {
        this.fGubn = fGubn;
    }

    public String getfOrderBy() {
        return fOrderBy;
    }

    public void setfOrderBy(String fOrderBy) {
        this.fOrderBy = fOrderBy;
    }

    public String getfType() {
        return fType;
    }

    public void setfType(String fType) {
        this.fType = fType;
    }

    public String getfAmtChkTy() {
        return fAmtChkTy;
    }

    public void setfAmtChkTy(String fAmtChkTy) {
        this.fAmtChkTy = fAmtChkTy;
    }

    public String getBnkCd() {
        return bnkCd;
    }

    public void setBnkCd(String bnkCd) {
        this.bnkCd = bnkCd;
    }

    public String getPgServiceId() {
        return pgServiceId;
    }

    public void setPgServiceId(String pgServiceId) {
        this.pgServiceId = pgServiceId;
    }

    public String getBnkMsgNo() {
        return bnkMsgNo;
    }

    public void setBnkMsgNo(String bnkMsgNo) {
        this.bnkMsgNo = bnkMsgNo;
    }

    public String getPacketNo() {
        return packetNo;
    }

    public void setPacketNo(String packetNo) {
        this.packetNo = packetNo;
    }

    public String getAppDay() {
        return appDay;
    }

    public void setAppDay(String appDay) {
        this.appDay = appDay;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public String getCusOffNo() {
        return cusOffNo;
    }

    public void setCusOffNo(String cusOffNo) {
        this.cusOffNo = cusOffNo;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getCshAmt() {
        return cshAmt;
    }

    public void setCshAmt(String cshAmt) {
        this.cshAmt = cshAmt;
    }

    public String getCshAmt2() {
        return cshAmt2;
    }

    public void setCshAmt2(String cshAmt2) {
        this.cshAmt2 = cshAmt2;
    }

    public String getCusOffNo2() {
        return cusOffNo2;
    }

    public void setCusOffNo2(String cusOffNo2) {
        this.cusOffNo2 = cusOffNo2;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCashMasStNm() {
        return cashMasStNm;
    }

    public void setCashMasStNm(String cashMasStNm) {
        this.cashMasStNm = cashMasStNm;
    }

    public String getAppMsg() {
        return appMsg;
    }

    public void setAppMsg(String appMsg) {
        this.appMsg = appMsg;
    }

    public String getCashMasCd() {
        return cashMasCd;
    }

    public void setCashMasCd(String cashMasCd) {
        this.cashMasCd = cashMasCd;
    }

    public String getCashMasSt() {
        return cashMasSt;
    }

    public void setCashMasst(String cashMasSt) {
        this.cashMasSt = cashMasSt;
    }

    public String getSeqCd() {
        return seqCd;
    }

    public void setSeqCd(String seqCd) {
        this.seqCd = seqCd;
    }

    public void setCashMasSt(String cashMasSt) {
        this.cashMasSt = cashMasSt;
    }

    public String getfCashMasSt() {
        return fCashMasSt;
    }

    public void setfCashMasSt(String fCashMasSt) {
        this.fCashMasSt = fCashMasSt;
    }

    public String getCusType() {
        return cusType;
    }

    public void setCusType(String cusType) {
        this.cusType = cusType;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConFirm(String confirm) {
        this.confirm = confirm;
    }

    public String getItemCnt() {
        return itemCnt;
    }

    public void setItemCnt(String itemCnt) {
        this.itemCnt = itemCnt;
    }

    public String getNotiCanYn() {
        return notiCanYn;
    }

    public void setNotiCanYn(String notiCanYn) {
        this.notiCanYn = notiCanYn;
    }

    public String getChaOffNo() {
        return chaOffNo;
    }

    public void setChaOffNo(String chaOffNo) {
        this.chaOffNo = chaOffNo;
    }

    public String getNoTaxYn() {
        return noTaxYn;
    }

    public void setNoTaxYn(String noTaxYn) {
        this.noTaxYn = noTaxYn;
    }

    public String getRcpReqTy() {
        return rcpReqTy;
    }

    public void setRcpReqTy(String rcpReqTy) {
        this.rcpReqTy = rcpReqTy;
    }

    public String getChaTrTy() {
        return chaTrTy;
    }

    public void setChaTrTy(String chaTrTy) {
        this.chaTrTy = chaTrTy;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getPtrItemOrder() {
        return ptrItemOrder;
    }

    public void setPtrItemOrder(String ptrItemOrder) {
        this.ptrItemOrder = ptrItemOrder;
    }

    public long getRcpItemAmt() {
        return rcpItemAmt;
    }

    public void setRcpItemAmt(long rcpItemAmt) {
        this.rcpItemAmt = rcpItemAmt;
    }

    public long getRemAmt() {
        return remAmt;
    }

    public void setRemAmt(long remAmt) {
        this.remAmt = remAmt;
    }

    public String getGubun() {
        return gubun;
    }

    public void setGubun(String gubun) {
        this.gubun = gubun;
    }

    public String getRcpItemYn() {
        return rcpItemYn;
    }

    public void setRcpItemYn(String rcpItemYn) {
        this.rcpItemYn = rcpItemYn;
    }

    public String getAppCd() {
        return appCd;
    }

    public void setAppCd(String appCd) {
        this.appCd = appCd;
    }

    public String getConfirm2() {
        return confirm2;
    }

    public void setConfirm2(String confirm2) {
        this.confirm2 = confirm2;
    }

    public String getMakeDt() {
        return makeDt;
    }

    public void setMakeDt(String makeDt) {
        this.makeDt = makeDt;
    }

    public String getPayNoGb() {
        return payNoGb;
    }

    public void setPayNoGb(String payNoGb) {
        this.payNoGb = payNoGb;
    }

    public String getPtrItemRemark() {
        return ptrItemRemark;
    }

    public void setPtrItemRemark(String ptrItemRemark) {
        this.ptrItemRemark = ptrItemRemark;
    }

    public String getPayItem() {
        return payItem;
    }

    public void setPayItem(String payItem) {
        this.payItem = payItem;
    }

    public String getAdjFiregKey() {
        return adjFiregKey;
    }

    public void setAdjFiregKey(String adjFiregKey) {
        this.adjFiregKey = adjFiregKey;
    }

    public String getGrpadjName() {
        return grpadjName;
    }

    public void setGrpadjName(String grpadjName) {
        this.grpadjName = grpadjName;
    }

    public String getfPayItem() {
        return fPayItem;
    }

    public void setfPayItem(String fPayItem) {
        this.fPayItem = fPayItem;
    }

    public String getfAdjFiregKey() {
        return fAdjFiregKey;
    }

    public void setfAdjFiregKey(String fAdjFiregKey) {
        this.fAdjFiregKey = fAdjFiregKey;
    }

    
    public long getTotAmt() {
        return totAmt;
    }

    public void setTotAmt(long totAmt) {
        this.totAmt = totAmt;
    }

    public long getTotUnAmt() {
        return totUnAmt;
    }

    public void setTotUnAmt(long totUnAmt) {
        this.totUnAmt = totUnAmt;
    }

    public long getRcpAmt() {
        return rcpAmt;
    }

    public void setRcpAmt(long rcpAmt) {
        this.rcpAmt = rcpAmt;
    }

    public long getUnAmt() {
        return unAmt;
    }

    public void setUnAmt(long unAmt) {
        this.unAmt = unAmt;
    }

    public long getAmt() {
        return amt;
    }

    public void setAmt(long amt) {
        this.amt = amt;
    }

    public long getRcpAmt2() {
        return rcpAmt2;
    }

    public void setRcpAmt2(long rcpAmt2) {
        this.rcpAmt2 = rcpAmt2;
    }

    public long getVasAmt() {
        return vasAmt;
    }

    public void setVasAmt(long vasAmt) {
        this.vasAmt = vasAmt;
    }

    public long getOcdAmt() {
        return ocdAmt;
    }

    public void setOcdAmt(long ocdAmt) {
        this.ocdAmt = ocdAmt;
    }

    public long getDcsAmt() {
        return dcsAmt;
    }

    public void setDcsAmt(long dcsAmt) {
        this.dcsAmt = dcsAmt;
    }

    public long getDcdAmt() {
        return dcdAmt;
    }

    public void setDcdAmt(long dcdAmt) {
        this.dcdAmt = dcdAmt;
    }

/**
     * 총수납금액
     * @return
     * @author jhjeong@finger.co.kr
     * @modified 2018. 9. 12.
     */
	public long getSumRcpAmt() {
		return sumRcpAmt;
	}

	/**
	 * 총수납금액
	 * @param sumRcpAmt
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 12.
	 */
	public void setSumRcpAmt(long sumRcpAmt) {
		this.sumRcpAmt = sumRcpAmt;
	}

	/**
	 * 항목별 수납금액
	 * @return
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 13.
	 */
	public long getRcpPayItemAmt() {
		return rcpPayItemAmt;
	}

	/**
	 * 항목별 수납금액
	 * @param rcpPayItemAmt
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 13.
	 */
	public void setRcpPayItemAmt(long rcpPayItemAmt) {
		this.rcpPayItemAmt = rcpPayItemAmt;
	}

    public long getCashRcpAmt() {
        return cashRcpAmt;
    }

    public void setCashRcpAmt(long cashRcpAmt) {
        this.cashRcpAmt = cashRcpAmt;
    }

    public long getCashRcpAmt2() {
        return cashRcpAmt2;
    }

    public void setCashRcpAmt2(long cashRcpAmt2) {
        this.cashRcpAmt2 = cashRcpAmt2;
    }

    public String getRealJob() {
        return realJob;
    }

    public void setRealJob(String realJob) {
        this.realJob = realJob;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

}
