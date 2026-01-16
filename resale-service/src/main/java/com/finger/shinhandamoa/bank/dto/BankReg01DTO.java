package com.finger.shinhandamoa.bank.dto;

public class BankReg01DTO {
    private String chaCd;
    private String agtCd;
    private String chaName;
    private String owner;
    private String ownerTel;
    private String chaOffNo;
    private String chaZipCode;
    private String chaAddress1;
    private String chaAddress2;
    private String chrName;
    private String chrTelNo;
    private String chrHp;
    private String chrMail;
    private String feeAccNo;
    private String feeOffno;
    private String adjaccyn;
    private String remark;
    private String chast;
    private String makeDt;
    private String regDt;
    private String chaNewRegDt;
    private String searchOrderBy;
    private String calDateFrom;
    private String calDateTo;
    private String agtName;
    private String searchGb;
    private String flag;
    private String startDate;
    private String endDate;
    private String month;
    private String maker;
    private String jobType;
    private String chaCloseChk;
    private String chaCloseSt;
    private String chaCloseVarDt;
    private String chaCloseVarReson;
    private String preChast;
    private String cmsFileName;
    private String chatrty;
    private String daMngMemo;
    private String usePgYn;
    private String pgAprDt;
    private String pgAprMemo;
    private String useSmsYn;
    private String smsAplDt;
    private String smsAplMemo;
    private String pgComName;
    private String pgLicenKey;
    private String chaCloseDt;
    private String pgRevCommiRate;
    private String fingerRevCommiRate;
    private String adjfiregKey;
    private String grpadjName;
    private String chaStatus;
    private String chaType;
    private String loginId;
    private String chaGb;
    private String pgServiceId;
    private String amtChkTy;
    private String rcpDueChk;
    private String partialPayment;
    private String bnkCd;
    private String bValue;
    private String finFeeAccNo;
    private String finFeeAccOwner;
    private String finFeeAccIdent;
    private String finFeeOwnNo;
    private String finFeePayTy;

    private String mchtId; //가맹정코드 (PG)

    private int rn;
    private int cnt;
    private int curPage;
    private int pageScale;
    private int totCnt;
    private long notMinLimit = 0l;      //  청구정액범위
    private long notMinFee = 0l;      //  청구정액수수료
    private long notCntFee = 0l;      //  청구건당수수료
    private long rcpCntFee = 0l;      //  수납건당수수료
    private long rcpBnkFee = 0l;         //  수납건당은행수수료
    private long totFee = 0l;
    private long totNotiCnt = 0l;
    private long totNotiFee = 0l;
    private long totRcpCnt = 0l;
    private long totRcpFee = 0l;
    private long totFeeSum = 0l;
    private long notiAmt = 0l;
    private long notiFee = 0l;
    private long notiCnt = 0l;
    private long rcpCnt = 0l;
    private long rcpFee = 0l;
    private long smsCnt = 0l;
    private long smsFee = 0l;
    private String mnlRcpReqTy; // 수기수납시 현금영수증 자동발급 여부
    private String rcpReqYn;    // 현금영수증신청여부
    private String rcpReqTy;    // 현금영수증자동발급여부
    private String noTaxYn;        // 과세/비과세여부
    private String rcpReqSveTy; // 수기수납시 현금영수증 발급여부
    private String bnkAcptDt; // 은행승인일자
    private String bnkRegiDt; // 은행등록일자
    private int accNoCnt; // 가상계좌 1회발급수
    private String cusNameTy; // 수취인명 설정
    private String mandRcpYn; // 현금영수증 의무발행여부 설정
    private String concurrentBlockYn; // 멀티디바이스 로그인 가능여부 설정

    public String getConcurrentBlockYn() {
        return concurrentBlockYn;
    }

    public void setConcurrentBlockYn(String concurrentBlockYn) {
        this.concurrentBlockYn = concurrentBlockYn;
    }

    public String getMandRcpYn() {
        return mandRcpYn;
    }
    public void setMandRcpYn(String mandRcpYn) {
        this.mandRcpYn = mandRcpYn;
    }

    public String getCusNameTy() {
        return cusNameTy;
    }
    public void setCusNameTy(String cusNameTy) {
        this.cusNameTy = cusNameTy;
    }

    public String getBnkRegiDt() {
        return bnkRegiDt;
    }
    public void setBnkRegiDt(String bnkRegiDt) {
        this.bnkRegiDt = bnkRegiDt;
    }

    public int getAccNoCnt() {
        return accNoCnt;
    }
    public void setAccNoCnt(int accNoCnt) {
        this.accNoCnt = accNoCnt;
    }
    public String getBnkAcptDt() {
        return bnkAcptDt;
    }
    public void setBnkAcptDt(String bnkAcptDt) {
        this.bnkAcptDt = bnkAcptDt;
    }
    public String getRcpReqSveTy() {
        return rcpReqSveTy;
    }

    public void setRcpReqSveTy(String rcpReqSveTy) {
        this.rcpReqSveTy = rcpReqSveTy;
    }

    public String getMnlRcpReqTy() {
        return mnlRcpReqTy;
    }

    public void setMnlRcpReqTy(String mnlRcpReqTy) {
        this.mnlRcpReqTy = mnlRcpReqTy;
    }

    public String getRcpReqTy() {
        return rcpReqTy;
    }

    public void setRcpReqTy(String rcpReqTy) {
        this.rcpReqTy = rcpReqTy;
    }

    public String getNoTaxYn() {
        return noTaxYn;
    }

    public void setNoTaxYn(String noTaxYn) {
        this.noTaxYn = noTaxYn;
    }

    public String getFinFeeOwnNo() {
        return finFeeOwnNo;
    }

    public void setFinFeeOwnNo(String finFeeOwnNo) {
        this.finFeeOwnNo = finFeeOwnNo;
    }

    public String getFinFeePayTy() {
        return finFeePayTy;
    }

    public void setFinFeePayTy(String finFeePayTy) {
        this.finFeePayTy = finFeePayTy;
    }

    public String getFinFeeAccIdent() {
        return finFeeAccIdent;
    }

    public void setFinFeeAccIdent(String finFeeAccIdent) {
        this.finFeeAccIdent = finFeeAccIdent;
    }

    public String getFinFeeAccOwner() {
        return finFeeAccOwner;
    }

    public void setFinFeeAccOwner(String finFeeAccOwner) {
        this.finFeeAccOwner = finFeeAccOwner;
    }

    public String getbValue() {
        return bValue;
    }

    public void setbValue(String bValue) {
        this.bValue = bValue;
    }

    public String getBnkCd() {
        return bnkCd;
    }
    public void setBnkCd(String bnkCd) {
        this.bnkCd = bnkCd;
    }
    public String getFinFeeAccNo() {
        return finFeeAccNo;
    }
    public void setFinFeeAccNo(String finFeeAccNo) {
        this.finFeeAccNo = finFeeAccNo;
    }
    public String getAdjfiregKey() {
        return adjfiregKey;
    }

    public void setAdjfiregKey(String adjfiregKey) {
        this.adjfiregKey = adjfiregKey;
    }

    public String getGrpadjName() {
        return grpadjName;
    }

    public void setGrpadjName(String grpadjName) {
        this.grpadjName = grpadjName;
    }

    public String getPreChast() {
        return preChast;
    }

    public void setPreChast(String preChast) {
        this.preChast = preChast;
    }

    public String getCmsFileName() {
        return cmsFileName;
    }

    public void setCmsFileName(String cmsFileName) {
        this.cmsFileName = cmsFileName;
    }

    public String getChatrty() {
        return chatrty;
    }

    public void setChatrty(String chatrty) {
        this.chatrty = chatrty;
    }

    public String getDaMngMemo() {
        return daMngMemo;
    }

    public void setDaMngMemo(String daMngMemo) {
        this.daMngMemo = daMngMemo;
    }

    public String getUsePgYn() {
        return usePgYn;
    }

    public void setUsePgYn(String usePgYn) {
        this.usePgYn = usePgYn;
    }

    public String getPgAprDt() {
        return pgAprDt;
    }

    public void setPgAprDt(String pgAprDt) {
        this.pgAprDt = pgAprDt;
    }

    public String getPgAprMemo() {
        return pgAprMemo;
    }

    public void setPgAprMemo(String pgAprMemo) {
        this.pgAprMemo = pgAprMemo;
    }

    public String getUseSmsYn() {
        return useSmsYn;
    }

    public void setUseSmsYn(String useSmsYn) {
        this.useSmsYn = useSmsYn;
    }

    public String getSmsAplDt() {
        return smsAplDt;
    }

    public void setSmsAplDt(String smsAplDt) {
        this.smsAplDt = smsAplDt;
    }

    public String getSmsAplMemo() {
        return smsAplMemo;
    }

    public void setSmsAplMemo(String smsAplMemo) {
        this.smsAplMemo = smsAplMemo;
    }

    public String getPgComName() {
        return pgComName;
    }

    public void setPgComName(String pgComName) {
        this.pgComName = pgComName;
    }

    public String getPgLicenKey() {
        return pgLicenKey;
    }

    public void setPgLicenKey(String pgLicenKey) {
        this.pgLicenKey = pgLicenKey;
    }

    public String getChaCloseDt() {
        return chaCloseDt;
    }

    public void setChaCloseDt(String chaCloseDt) {
        this.chaCloseDt = chaCloseDt;
    }

    public String getPgRevCommiRate() {
        return pgRevCommiRate;
    }

    public void setPgRevCommiRate(String pgRevCommiRate) {
        this.pgRevCommiRate = pgRevCommiRate;
    }

    public String getFingerRevCommiRate() {
        return fingerRevCommiRate;
    }

    public void setFingerRevCommiRate(String fingerRevCommiRate) {
        this.fingerRevCommiRate = fingerRevCommiRate;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getChaCloseChk() {
        return chaCloseChk;
    }

    public void setChaCloseChk(String chaCloseChk) {
        this.chaCloseChk = chaCloseChk;
    }

    public String getChaCloseSt() {
        return chaCloseSt;
    }

    public void setChaCloseSt(String chaCloseSt) {
        this.chaCloseSt = chaCloseSt;
    }

    public String getChaCloseVarDt() {
        return chaCloseVarDt;
    }

    public void setChaCloseVarDt(String chaCloseVarDt) {
        this.chaCloseVarDt = chaCloseVarDt;
    }

    public String getChaCloseVarReson() {
        return chaCloseVarReson;
    }

    public void setChaCloseVarReson(String chaCloseVarReson) {
        this.chaCloseVarReson = chaCloseVarReson;
    }

    public long getNotiAmt() {
        return notiAmt;
    }

    public void setNotiAmt(long notiAmt) {
        this.notiAmt = notiAmt;
    }

    public long getNotiFee() {
        return notiFee;
    }

    public void setNotiFee(long notiFee) {
        this.notiFee = notiFee;
    }

    public long getNotiCnt() {
        return notiCnt;
    }

    public void setNotiCnt(long notiCnt) {
        this.notiCnt = notiCnt;
    }

    public long getRcpCnt() {
        return rcpCnt;
    }

    public void setRcpCnt(long rcpCnt) {
        this.rcpCnt = rcpCnt;
    }

    public long getRcpFee() {
        return rcpFee;
    }

    public void setRcpFee(long rcpFee) {
        this.rcpFee = rcpFee;
    }

    public long getSmsCnt() {
        return smsCnt;
    }

    public void setSmsCnt(long smsCnt) {
        this.smsCnt = smsCnt;
    }

    public long getSmsFee() {
        return smsFee;
    }

    public void setSmsFee(long smsFee) {
        this.smsFee = smsFee;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getChaCd() {
        return chaCd;
    }

    public void setChaCd(String chaCd) {
        this.chaCd = chaCd;
    }

    public String getAgtCd() {
        return agtCd;
    }

    public void setAgtCd(String agtCd) {
        this.agtCd = agtCd;
    }

    public String getChaName() {
        return chaName;
    }

    public void setChaName(String chaName) {
        this.chaName = chaName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerTel() {
        return ownerTel;
    }

    public void setOwnerTel(String ownerTel) {
        this.ownerTel = ownerTel;
    }

    public String getChaOffNo() {
        return chaOffNo;
    }

    public void setChaOffNo(String chaOffNo) {
        this.chaOffNo = chaOffNo;
    }

    public String getChaZipCode() {
        return chaZipCode;
    }

    public void setChaZipCode(String chaZipCode) {
        this.chaZipCode = chaZipCode;
    }

    public String getChaAddress1() {
        return chaAddress1;
    }

    public void setChaAddress1(String chaAddress1) {
        this.chaAddress1 = chaAddress1;
    }

    public String getChaAddress2() {
        return chaAddress2;
    }

    public void setChaAddress2(String chaAddress2) {
        this.chaAddress2 = chaAddress2;
    }

    public String getChrName() {
        return chrName;
    }

    public void setChrName(String chrName) {
        this.chrName = chrName;
    }

    public String getChrTelNo() {
        return chrTelNo;
    }

    public void setChrTelNo(String chrTelNo) {
        this.chrTelNo = chrTelNo;
    }

    public String getChrHp() {
        return chrHp;
    }

    public void setChrHp(String chrHp) {
        this.chrHp = chrHp;
    }

    public String getChrMail() {
        return chrMail;
    }

    public void setChrMail(String chrMail) {
        this.chrMail = chrMail;
    }

    public String getFeeAccNo() {
        return feeAccNo;
    }

    public void setFeeAccNo(String feeAccNo) {
        this.feeAccNo = feeAccNo;
    }

    public String getFeeOffno() {
        return feeOffno;
    }

    public void setFeeOffno(String feeOffno) {
        this.feeOffno = feeOffno;
    }

    public String getAdjaccyn() {
        return adjaccyn;
    }

    public void setAdjaccyn(String adjaccyn) {
        this.adjaccyn = adjaccyn;
    }

    public String getRcpReqYn() {
        return rcpReqYn;
    }

    public void setRcpReqYn(String rcpReqYn) {
        this.rcpReqYn = rcpReqYn;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getChast() {
        return chast;
    }

    public void setChast(String chast) {
        this.chast = chast;
    }

    public String getMakeDt() {
        return makeDt;
    }

    public void setMakeDt(String makeDt) {
        this.makeDt = makeDt;
    }

    public String getRegDt() {
        return regDt;
    }

    public void setRegDt(String regDt) {
        this.regDt = regDt;
    }

    public String getChaNewRegDt() {
        return chaNewRegDt;
    }

    public void setChaNewRegDt(String chaNewRegDt) {
        this.chaNewRegDt = chaNewRegDt;
    }

    public String getSearchOrderBy() {
        return searchOrderBy;
    }

    public void setSearchOrderBy(String searchOrderBy) {
        this.searchOrderBy = searchOrderBy;
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

    public String getAgtName() {
        return agtName;
    }

    public void setAgtName(String agtName) {
        this.agtName = agtName;
    }

    public String getSearchGb() {
        return searchGb;
    }

    public void setSearchGb(String searchGb) {
        this.searchGb = searchGb;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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

    public int getTotCnt() {
        return totCnt;
    }

    public void setTotCnt(int totCnt) {
        this.totCnt = totCnt;
    }

    public long getNotMinLimit() {
        return notMinLimit;
    }

    public void setNotMinLimit(long notMinLimit) {
        this.notMinLimit = notMinLimit;
    }

    public long getNotMinFee() {
        return notMinFee;
    }

    public void setNotMinFee(long notMinFee) {
        this.notMinFee = notMinFee;
    }

    public long getNotCntFee() {
        return notCntFee;
    }

    public void setNotCntFee(long notCntFee) {
        this.notCntFee = notCntFee;
    }

    public long getRcpCntFee() {
        return rcpCntFee;
    }

    public void setRcpCntFee(long rcpCntFee) {
        this.rcpCntFee = rcpCntFee;
    }

    public long getRcpBnkFee() {
        return rcpBnkFee;
    }

    public void setRcpBnkFee(long rcpBnkFee) {
        this.rcpBnkFee = rcpBnkFee;
    }

    public long getTotFee() {
        return totFee;
    }

    public void setTotFee(long totFee) {
        this.totFee = totFee;
    }

    public long getTotFeeSum() {
        return totFeeSum;
    }

    public void setTotFeeSum(long totFeeSum) {
        this.totFeeSum = totFeeSum;
    }

    public long getTotNotiCnt() {
        return totNotiCnt;
    }

    public void setTotNotiCnt(long totNotiCnt) {
        this.totNotiCnt = totNotiCnt;
    }

    public long getTotNotiFee() {
        return totNotiFee;
    }

    public void setTotNotiFee(long totNotiFee) {
        this.totNotiFee = totNotiFee;
    }

    public long getTotRcpCnt() {
        return totRcpCnt;
    }

    public void setTotRcpCnt(long totRcpCnt) {
        this.totRcpCnt = totRcpCnt;
    }

    public long getTotRcpFee() {
        return totRcpFee;
    }

    public void setTotRcpFee(long totRcpFee) {
        this.totRcpFee = totRcpFee;
    }

    public String getChaStatus() {
        return chaStatus;
    }

    public void setChaStatus(String chaStatus) {
        this.chaStatus = chaStatus;
    }

    public String getChaType() {
        return chaType;
    }

    public void setChaType(String chaType) {
        this.chaType = chaType;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getChaGb() {
        return chaGb;
    }

    public void setChaGb(String chaGb) {
        this.chaGb = chaGb;
    }

    public String getPgServiceId() {
        return pgServiceId;
    }

    public void setPgServiceId(String pgServiceId) {
        this.pgServiceId = pgServiceId;
    }

    public String getAmtChkTy() {
        return amtChkTy;
    }

    public void setAmtChkTy(String amtChkTy) {
        this.amtChkTy = amtChkTy;
    }

    public String getRcpDueChk() {
        return rcpDueChk;
    }

    public void setRcpDueChk(String rcpDueChk) {
        this.rcpDueChk = rcpDueChk;
    }

    public String getPartialPayment() {
        return partialPayment;
    }

    public void setPartialPayment(String partialPayment) {
        this.partialPayment = partialPayment;
    }
}

