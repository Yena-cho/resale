package com.finger.shinhandamoa.sys.setting.dto;

import java.util.List;

/**
 * @author 홍길동
 * @date 2018. 4. 28.
 * @desc 최초생성
 */
public class VanoMgmtDTO {
    private String fgcd; //은행코드
    private String ficd;
    private String vano; //가상계좌번호
    private String fitxcd;
    private String chacd; //기관코드
    private String cuscd; //납부자코드
    private String useyn;
    private String regdt;
    private String varegty;
    private String remark;
    private String usedt;
    private String chaname;
    private String cusname;
    private String chrname;
    private String chrhp;
    private String chast; 	//기관상태
    private String chatrty;    //기관분류
    private String chastatus; //업종
    private String chatype; //업태

    private int ycnt;
    private int wcnt;
    private int acnt;
    private int ncnt;
    private int rcnt;
    private int vacnt;

    private int rn;
    private int cnt;
    private int curPage;
    private int pageScale;
    private String searchOrderBy;
    private String searchGb;
    private String searchValue;
    private String statRadio;

    private long totcnt;
    private long usedcnt = 0;
    private long notusedcnt = 0;
    private long delcnt = 0;

    private String fromNotUseCnt;
    private String toNotUseCnt;
    private String lastrcpdate;
    private int acccnt;
    private String startacct;
    private int istartacct;

    private String searchStartday;        //가상계계좌 발급일 (sajoh@finger.com)
    private String searchEndday;

    private String fchacd;
    private String fsearchGb;
    private String fsearchValue;
    private String fuseyn;
    private String rcpmascd;
    private String notimascd;
    private String svecd;           //서비스코드, VAS-가상계좌,DCS-창구현금,DCD-창구카드,DVA-무통장입금,OCD-인터넷카드
    private String maskey;
    private String masmonth;
    private String masday;
    private String cuskey;
    private String cusgubn1;
    private String cusgubn2;
    private String cusgubn3;
    private String cusgubn4;
    private String cushp;
    private String cusmail;
    private String smsyn;
    private String mailyn;
    private String cusoffno;
    private String payday;        //은행납부일자
    private String paytime;
    private String bnkmsgno;
    private String bnkcd;
    private String bchcd;
    private String rcpusrname;
    private long rcpamt;
    private String occgubn;
    private String mdgubn;
    private String sucday;
    private String suctime;
    private String trnst;       //(업체)전송상태(전송전:ST01 완료:ST07)
    private String rcpmasst;       //수납상태(수납:PA03  취소:PA09)
    private String cashday;       //현금영수증발행일자
    private String paydayFrom;
    private String paydayTo;
    private String paytimeFrom;
    private String paytimeTo;
    private String fpaydayFrom;
    private String fpaydayTo;
    private String fpaytimeFrom;
    private String fpaytimeTo;
    private String chaCd;
    private String chaName;
    private String chaCloseChk;
    private String agtCd;
    private String chrTelNo;
    private String sendSt;
    private String appendYn;
    private String reqTy;

    private List<String> stList;
    private List<String> chatrtyList;

    public int getVacnt() {
        return vacnt;
    }

    public void setVacnt(int vacnt) {
        this.vacnt = vacnt;
    }

    public int getRcnt() {
        return rcnt;
    }

    public void setRcnt(int rcnt) {
        this.rcnt = rcnt;
    }

    public int getYcnt() {
		return ycnt;
	}

	public void setYcnt(int ycnt) {
		this.ycnt = ycnt;
	}

	public int getWcnt() {
		return wcnt;
	}

	public void setWcnt(int wcnt) {
		this.wcnt = wcnt;
	}

	public int getAcnt() {
		return acnt;
	}

	public void setAcnt(int acnt) {
		this.acnt = acnt;
	}

	public int getNcnt() {
		return ncnt;
	}

	public void setNcnt(int ncnt) {
		this.ncnt = ncnt;
	}

	public String getChastatus() {
        return chastatus;
    }

    public void setChastatus(String chastatus) {
        this.chastatus = chastatus;
    }

    public String getChatype() {
        return chatype;
    }

    public void setChatype(String chatype) {
        this.chatype = chatype;
    }

    public String getFgcd() {
        return fgcd;
    }

    public void setFgcd(String fgcd) {
        this.fgcd = fgcd;
    }

    public String getSearchStartday() {
        return searchStartday;
    }

    public void setSearchStartday(String searchStartday) {
        this.searchStartday = searchStartday;
    }

    public List<String> getChatrtyList() {
        return chatrtyList;
    }

    public void setChatrtyList(List<String> chatrtyList) {
        this.chatrtyList = chatrtyList;
    }

    public String getChatrty() {
        return chatrty;
    }

    public void setChatrty(String chatrty) {
        this.chatrty = chatrty;
    }

    public String getSearchEndday() {
        return searchEndday;
    }

    public void setSearchEndday(String searchEndday) {
        this.searchEndday = searchEndday;
    }

    public String getFicd() {
        return ficd;
    }

    public void setFicd(String ficd) {
        this.ficd = ficd;
    }

    public String getVano() {
        return vano;
    }

    public void setVano(String vano) {
        this.vano = vano;
    }

    public String getFitxcd() {
        return fitxcd;
    }

    public void setFitxcd(String fitxcd) {
        this.fitxcd = fitxcd;
    }

    public String getChacd() {
        return chacd;
    }

    public void setChacd(String chacd) {
        this.chacd = chacd;
    }

    public String getCuscd() {
        return cuscd;
    }

    public void setCuscd(String cuscd) {
        this.cuscd = cuscd;
    }

    public String getUseyn() {
        return useyn;
    }

    public void setUseyn(String useyn) {
        this.useyn = useyn;
    }

    public String getRegdt() {
        return regdt;
    }

    public void setRegdt(String regdt) {
        this.regdt = regdt;
    }

    public String getVaregty() {
        return varegty;
    }

    public void setVaregty(String varegty) {
        this.varegty = varegty;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUsedt() {
        return usedt;
    }

    public void setUsedt(String usedt) {
        this.usedt = usedt;
    }

    public String getChaname() {
        return chaname;
    }

    public void setChaname(String chaname) {
        this.chaname = chaname;
    }

    public String getCusname() {
        return cusname;
    }

    public void setCusname(String cusname) {
        this.cusname = cusname;
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

    public long getTotcnt() {
        return totcnt;
    }

    public void setTotcnt(long totcnt) {
        this.totcnt = totcnt;
    }

    public long getUsedcnt() {
        return usedcnt;
    }

    public void setUsedcnt(long usedcnt) {
        this.usedcnt = usedcnt;
    }

    public long getNotusedcnt() {
        return notusedcnt;
    }

    public void setNotusedcnt(long notusedcnt) {
        this.notusedcnt = notusedcnt;
    }

    public long getDelcnt() {
        return delcnt;
    }

    public void setDelcnt(long delcnt) {
        this.delcnt = delcnt;
    }

    public String getChrname() {
        return chrname;
    }

    public void setChrname(String chrname) {
        this.chrname = chrname;
    }

    public String getChrhp() {
        return chrhp;
    }

    public void setChrhp(String chrhp) {
        this.chrhp = chrhp;
    }

    public String getChast() {
        return chast;
    }

    public void setChast(String chast) {
        this.chast = chast;
    }

    public String getStatRadio() {
        return statRadio;
    }

    public void setStatRadio(String statRadio) {
        this.statRadio = statRadio;
    }

    public String getFromNotUseCnt() {
        return fromNotUseCnt;
    }

    public void setFromNotUseCnt(String fromNotUseCnt) {
        this.fromNotUseCnt = fromNotUseCnt;
    }

    public String getToNotUseCnt() {
        return toNotUseCnt;
    }

    public void setToNotUseCnt(String toNotUseCnt) {
        this.toNotUseCnt = toNotUseCnt;
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

    public int getAcccnt() {
        return acccnt;
    }

    public void setAcccnt(int acccnt) {
        this.acccnt = acccnt;
    }

    public String getStartacct() {
        return startacct;
    }

    public void setStartacct(String startacct) {
        this.startacct = startacct;
    }

    public int getIstartacct() {
        return istartacct;
    }

    public void setIstartacct(int istartacct) {
        this.istartacct = istartacct;
    }

    public String getLastrcpdate() {
        return lastrcpdate;
    }

    public void setLastrcpdate(String lastrcpdate) {
        this.lastrcpdate = lastrcpdate;
    }

    public String getFchacd() {
        return fchacd;
    }

    public void setFchacd(String fchacd) {
        this.fchacd = fchacd;
    }

    public String getFsearchGb() {
        return fsearchGb;
    }

    public void setFsearchGb(String fsearchGb) {
        this.fsearchGb = fsearchGb;
    }

    public String getFsearchValue() {
        return fsearchValue;
    }

    public void setFsearchValue(String fsearchValue) {
        this.fsearchValue = fsearchValue;
    }

    public String getFuseyn() {
        return fuseyn;
    }

    public void setFuseyn(String fuseyn) {
        this.fuseyn = fuseyn;
    }

    public String getRcpmascd() {
        return rcpmascd;
    }

    public void setRcpmascd(String rcpmascd) {
        this.rcpmascd = rcpmascd;
    }

    public String getNotimascd() {
        return notimascd;
    }

    public void setNotimascd(String notimascd) {
        this.notimascd = notimascd;
    }

    public String getSvecd() {
        return svecd;
    }

    public void setSvecd(String svecd) {
        this.svecd = svecd;
    }

    public String getMaskey() {
        return maskey;
    }

    public void setMaskey(String maskey) {
        this.maskey = maskey;
    }

    public String getMasmonth() {
        return masmonth;
    }

    public void setMasmonth(String masmonth) {
        this.masmonth = masmonth;
    }

    public String getMasday() {
        return masday;
    }

    public void setMasday(String masday) {
        this.masday = masday;
    }

    public String getCuskey() {
        return cuskey;
    }

    public void setCuskey(String cuskey) {
        this.cuskey = cuskey;
    }

    public String getCusgubn1() {
        return cusgubn1;
    }

    public void setCusgubn1(String cusgubn1) {
        this.cusgubn1 = cusgubn1;
    }

    public String getCusgubn2() {
        return cusgubn2;
    }

    public void setCusgubn2(String cusgubn2) {
        this.cusgubn2 = cusgubn2;
    }

    public String getCusgubn3() {
        return cusgubn3;
    }

    public void setCusgubn3(String cusgubn3) {
        this.cusgubn3 = cusgubn3;
    }

    public String getCusgubn4() {
        return cusgubn4;
    }

    public void setCusgubn4(String cusgubn4) {
        this.cusgubn4 = cusgubn4;
    }

    public String getCushp() {
        return cushp;
    }

    public void setCushp(String cushp) {
        this.cushp = cushp;
    }

    public String getCusmail() {
        return cusmail;
    }

    public void setCusmail(String cusmail) {
        this.cusmail = cusmail;
    }

    public String getSmsyn() {
        return smsyn;
    }

    public void setSmsyn(String smsyn) {
        this.smsyn = smsyn;
    }

    public String getMailyn() {
        return mailyn;
    }

    public void setMailyn(String mailyn) {
        this.mailyn = mailyn;
    }

    public String getCusoffno() {
        return cusoffno;
    }

    public void setCusoffno(String cusoffno) {
        this.cusoffno = cusoffno;
    }

    public String getPayday() {
        return payday;
    }

    public void setPayday(String payday) {
        this.payday = payday;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getBnkmsgno() {
        return bnkmsgno;
    }

    public void setBnkmsgno(String bnkmsgno) {
        this.bnkmsgno = bnkmsgno;
    }

    public String getBnkcd() {
        return bnkcd;
    }

    public void setBnkcd(String bnkcd) {
        this.bnkcd = bnkcd;
    }

    public String getBchcd() {
        return bchcd;
    }

    public void setBchcd(String bchcd) {
        this.bchcd = bchcd;
    }

    public String getRcpusrname() {
        return rcpusrname;
    }

    public void setRcpusrname(String rcpusrname) {
        this.rcpusrname = rcpusrname;
    }

    public long getRcpamt() {
        return rcpamt;
    }

    public void setRcpamt(long rcpamt) {
        this.rcpamt = rcpamt;
    }

    public String getOccgubn() {
        return occgubn;
    }

    public void setOccgubn(String occgubn) {
        this.occgubn = occgubn;
    }

    public String getMdgubn() {
        return mdgubn;
    }

    public void setMdgubn(String mdgubn) {
        this.mdgubn = mdgubn;
    }

    public String getSucday() {
        return sucday;
    }

    public void setSucday(String sucday) {
        this.sucday = sucday;
    }

    public String getSuctime() {
        return suctime;
    }

    public void setSuctime(String suctime) {
        this.suctime = suctime;
    }

    public String getTrnst() {
        return trnst;
    }

    public void setTrnst(String trnst) {
        this.trnst = trnst;
    }

    public String getRcpmasst() {
        return rcpmasst;
    }

    public void setRcpmasst(String rcpmasst) {
        this.rcpmasst = rcpmasst;
    }

    public String getCashday() {
        return cashday;
    }

    public void setCashday(String cashday) {
        this.cashday = cashday;
    }

    public String getPaytimeFrom() {
        return paytimeFrom;
    }

    public void setPaytimeFrom(String paytimeFrom) {
        this.paytimeFrom = paytimeFrom;
    }

    public String getPaytimeTo() {
        return paytimeTo;
    }

    public void setPaytimeTo(String paytimeTo) {
        this.paytimeTo = paytimeTo;
    }

    public String getPaydayFrom() {
        return paydayFrom;
    }

    public void setPaydayFrom(String paydayFrom) {
        this.paydayFrom = paydayFrom;
    }

    public String getPaydayTo() {
        return paydayTo;
    }

    public void setPaydayTo(String paydayTo) {
        this.paydayTo = paydayTo;
    }

    public String getFpaydayFrom() {
        return fpaydayFrom;
    }

    public void setFpaydayFrom(String fpaydayFrom) {
        this.fpaydayFrom = fpaydayFrom;
    }

    public String getFpaydayTo() {
        return fpaydayTo;
    }

    public void setFpaydayTo(String fpaydayTo) {
        this.fpaydayTo = fpaydayTo;
    }

    public String getFpaytimeFrom() {
        return fpaytimeFrom;
    }

    public void setFpaytimeFrom(String fpaytimeFrom) {
        this.fpaytimeFrom = fpaytimeFrom;
    }

    public String getFpaytimeTo() {
        return fpaytimeTo;
    }

    public void setFpaytimeTo(String fpaytimeTo) {
        this.fpaytimeTo = fpaytimeTo;
    }

    public List<String> getStList() {
        return stList;
    }

    public void setStList(List<String> stList) {
        this.stList = stList;
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

    public String getChaCloseChk() {
        return chaCloseChk;
    }

    public void setChaCloseChk(String chaCloseChk) {
        this.chaCloseChk = chaCloseChk;
    }

    public String getAgtCd() {
        return agtCd;
    }

    public void setAgtCd(String agtCd) {
        this.agtCd = agtCd;
    }

    public String getChrTelNo() {
        return chrTelNo;
    }

    public void setChrTelNo(String chrTelNo) {
        this.chrTelNo = chrTelNo;
    }

    public String getSendSt() {
        return sendSt;
    }

    public void setSendSt(String sendSt) {
        this.sendSt = sendSt;
    }

    public String getAppendYn() {
        return appendYn;
    }

    public void setAppendYn(String appendYn) {
        this.appendYn = appendYn;
    }

    public String getReqTy() {
        return reqTy;
    }

    public void setReqTy(String reqTy) {
        this.reqTy = reqTy;
    }
}
