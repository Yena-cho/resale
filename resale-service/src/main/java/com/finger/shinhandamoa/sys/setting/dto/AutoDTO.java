package com.finger.shinhandamoa.sys.setting.dto;

import java.util.List;

/**
 * @author  by puki
 * @date    2018. 5. 23.
 * @desc    최초생성
 * @version
 *
 */
public class AutoDTO {

	private String iD;
	private String chaCd;
	private String chaName;
	private String chkCms;
	private String useCmsYn;
	private String cmsAppFee;
	private String cmsMinLimitOne;
	private String cmsMaxLimitOne;
	private String cmsLimitMonth;
	private String cmsSetPeriod;
	private String cmsAddIcheYn;
	private String cmsAdIchePeriod;
	private String cmsSmsYn;
	private String cmsEmailYn;
	private String cmsDesc;
	private String cmsFileName;
	private String regDt;
	private String chrName;
	private String feeAccNo;
	private String fileYn;
	private String startDt;
	private String endDt;
	private String cmsReqSt;
	private String cmsReqFailCont;
	private String cmsReqDt;
	private String feeAccName;
	private String chaSt;
	private String cmsSt;

	private String rcpDt;
	private String feeSum;
	private String rcpSt;
	private String smsReqSt;

	private String witReqDt;
	private String failFee;
	private String rcpFailReson;
	private String cmsReqFinDt;
	private String cmsAppGubn;
	private String bankCd; //은행코드
	private String feeOffNo;
	private String REGDT;
	private String CHAST;
	private String CHACD;
	private String BANKCD;
	private String FEEOFFNO;
	private String CMS_FILE_NAME;
	private String CMS_REQ_ST;
	private String CMS_APP_GUBN;

	private int rn;
	private int pageScale;
	private int curPage;
	private int cnt;
	private String searchOption;
	private String keyword;
	private String orderBy;
	private String chatrty; //고객분류
	private String consentTy; //동의방법
	private String chkcmsYN; //동의상태
	private String chaOffNo; //사업자번호
	private String chrHp; //담당자폰
	private String bnkCd; //은행명
	private String waId; //withdraw_agreement 파일 아이디
	private String finFeeAccIdent; //FINGER_FEE_ACCOUNT_IDENTITY 사업자번호(생년월일)
	private String finFeeOwnNo; //FINGER_FEE_OWNER_NO 납부자번호

	private List<String> stList; //CMS신청상태
	private List<String> cmsList; // 동의상태
	private List<String> chaList; //기관상태

	private String successYn;
	private String resultCd;
	private String resultName;
	private String resultMessage;

	public String getiD() {
		return iD;
	}

	public void setiD(String iD) {
		this.iD = iD;
	}

	public String getFinFeeOwnNo() {
        return finFeeOwnNo;
    }
    public void setFinFeeOwnNo(String finFeeOwnNo) {
        this.finFeeOwnNo = finFeeOwnNo;
    }
    public String getFinFeeAccIdent() {
        return finFeeAccIdent;
    }
    public void setFinFeeAccIdent(String finFeeAccIdent) {
        this.finFeeAccIdent = finFeeAccIdent;
    }
    public String getWaId() { return waId; }
	public void setWaId(String waId) {
		this.waId = waId;
	}
	public String getBnkCd() {
		return bnkCd;
	}
	public void setBnkCd(String bnkCd) {
		this.bnkCd = bnkCd;
	}
	public String getChrHp() {
		return chrHp;
	}
	public void setChrHp(String chrHp) {
		this.chrHp = chrHp;
	}
	public String getChaOffNo() {
		return chaOffNo;
	}
	public void setChaOffNo(String chaOffNo) {
		this.chaOffNo = chaOffNo;
	}
	public String getChatrty() {
        return chatrty;
    }
    public void setChatrty(String chatrty) {
        this.chatrty = chatrty;
    }
    public String getConsentTy() {
        return consentTy;
    }
    public void setConsentTy(String consentTy) {
        this.consentTy = consentTy;
    }
    public String getChkcmsYN() {
        return chkcmsYN;
    }
    public void setChkcmsYN(String chkcmsYN) {
        this.chkcmsYN = chkcmsYN;
    }
    public List<String> getChaList() {
        return chaList;
    }
    public void setChaList(List<String> chaList) {
        this.chaList = chaList;
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
	public String getChkCms() {
		return chkCms;
	}
	public void setChkCms(String chkCms) {
		this.chkCms = chkCms;
	}
	public String getUseCmsYn() {
		return useCmsYn;
	}
	public void setUseCmsYn(String useCmsYn) {
		this.useCmsYn = useCmsYn;
	}
	public String getCmsAppFee() {
		return cmsAppFee;
	}
	public void setCmsAppFee(String cmsAppFee) {
		this.cmsAppFee = cmsAppFee;
	}
	public String getCmsMinLimitOne() {
		return cmsMinLimitOne;
	}
	public void setCmsMinLimitOne(String cmsMinLimitOne) {
		this.cmsMinLimitOne = cmsMinLimitOne;
	}
	public String getCmsMaxLimitOne() {
		return cmsMaxLimitOne;
	}
	public void setCmsMaxLimitOne(String cmsMaxLimitOne) {
		this.cmsMaxLimitOne = cmsMaxLimitOne;
	}
	public String getCmsLimitMonth() {
		return cmsLimitMonth;
	}
	public void setCmsLimitMonth(String cmsLimitMonth) {
		this.cmsLimitMonth = cmsLimitMonth;
	}
	public String getCmsSetPeriod() {
		return cmsSetPeriod;
	}
	public void setCmsSetPeriod(String cmsSetPeriod) {
		this.cmsSetPeriod = cmsSetPeriod;
	}
	public String getCmsAddIcheYn() {
		return cmsAddIcheYn;
	}
	public void setCmsAddIcheYn(String cmsAddIcheYn) {
		this.cmsAddIcheYn = cmsAddIcheYn;
	}
	public String getCmsAdIchePeriod() {
		return cmsAdIchePeriod;
	}
	public void setCmsAdIchePeriod(String cmsAdIchePeriod) {
		this.cmsAdIchePeriod = cmsAdIchePeriod;
	}
	public String getCmsSmsYn() {
		return cmsSmsYn;
	}
	public void setCmsSmsYn(String cmsSmsYn) {
		this.cmsSmsYn = cmsSmsYn;
	}
	public String getCmsEmailYn() {
		return cmsEmailYn;
	}
	public void setCmsEmailYn(String cmsEmailYn) {
		this.cmsEmailYn = cmsEmailYn;
	}
	public String getCmsDesc() {
		return cmsDesc;
	}
	public void setCmsDesc(String cmsDesc) {
		this.cmsDesc = cmsDesc;
	}
	public String getCmsFileName() {
		return cmsFileName;
	}
	public void setCmsFileName(String cmsFileName) {
		this.cmsFileName = cmsFileName;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public String getChrName() {
		return chrName;
	}
	public void setChrName(String chrName) {
		this.chrName = chrName;
	}
	public String getFeeAccNo() {
		return feeAccNo;
	}
	public void setFeeAccNo(String feeAccNo) {
		this.feeAccNo = feeAccNo;
	}
	public String getFileYn() {
		return fileYn;
	}
	public void setFileYn(String fileYn) {
		this.fileYn = fileYn;
	}
	public String getStartDt() {
		return startDt;
	}
	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}
	public String getEndDt() {
		return endDt;
	}
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
	public String getCmsReqSt() {
		return cmsReqSt;
	}
	public void setCmsReqSt(String cmsReqSt) {
		this.cmsReqSt = cmsReqSt;
	}
	public String getCmsReqFailCont() {
		return cmsReqFailCont;
	}
	public void setCmsReqFailCont(String cmsReqFailCont) {
		this.cmsReqFailCont = cmsReqFailCont;
	}
	public String getCmsReqDt() {
		return cmsReqDt;
	}
	public void setCmsReqDt(String cmsReqDt) {
		this.cmsReqDt = cmsReqDt;
	}
	public String getFeeAccName() {
		return feeAccName;
	}
	public void setFeeAccName(String feeAccName) {
		this.feeAccName = feeAccName;
	}
	public String getChaSt() {
		return chaSt;
	}
	public void setChaSt(String chaSt) {
		this.chaSt = chaSt;
	}
	public String getCmsSt() {
		return cmsSt;
	}
	public void setCmsSt(String cmsSt) {
		this.cmsSt = cmsSt;
	}
	public String getRcpDt() {
		return rcpDt;
	}
	public void setRcpDt(String rcpDt) {
		this.rcpDt = rcpDt;
	}
	public String getFeeSum() {
		return feeSum;
	}
	public void setFeeSum(String feeSum) {
		this.feeSum = feeSum;
	}
	public String getRcpSt() {
		return rcpSt;
	}
	public void setRcpSt(String rcpSt) {
		this.rcpSt = rcpSt;
	}
	public String getSmsReqSt() {
		return smsReqSt;
	}
	public void setSmsReqSt(String smsReqSt) {
		this.smsReqSt = smsReqSt;
	}
	public String getWitReqDt() {
		return witReqDt;
	}
	public void setWitReqDt(String witReqDt) {
		this.witReqDt = witReqDt;
	}
	public String getFailFee() {
		return failFee;
	}
	public void setFailFee(String failFee) {
		this.failFee = failFee;
	}
	public String getRcpFailReson() {
		return rcpFailReson;
	}
	public void setRcpFailReson(String rcpFailReson) {
		this.rcpFailReson = rcpFailReson;
	}
	public String getCmsReqFinDt() {
		return cmsReqFinDt;
	}
	public void setCmsReqFinDt(String cmsReqFinDt) {
		this.cmsReqFinDt = cmsReqFinDt;
	}
	public String getCmsAppGubn() {
		return cmsAppGubn;
	}
	public void setCmsAppGubn(String cmsAppGubn) {
		this.cmsAppGubn = cmsAppGubn;
	}
	public String getBankCd() {
		return bankCd;
	}
	public void setBankCd(String bankCd) {
		this.bankCd = bankCd;
	}
	public String getFeeOffNo() {
		return feeOffNo;
	}
	public void setFeeOffNo(String feeOffNo) {
		this.feeOffNo = feeOffNo;
	}
	public String getREGDT() {
		return REGDT;
	}
	public void setREGDT(String rEGDT) {
		REGDT = rEGDT;
	}
	public String getCHAST() {
		return CHAST;
	}
	public void setCHAST(String cHAST) {
		CHAST = cHAST;
	}
	public String getCHACD() {
		return CHACD;
	}
	public void setCHACD(String cHACD) {
		CHACD = cHACD;
	}
	public String getBANKCD() {
		return BANKCD;
	}
	public void setBANKCD(String bANKCD) {
		BANKCD = bANKCD;
	}
	public String getFEEOFFNO() {
		return FEEOFFNO;
	}
	public void setFEEOFFNO(String fEEOFFNO) {
		FEEOFFNO = fEEOFFNO;
	}
	public String getCMS_FILE_NAME() {
		return CMS_FILE_NAME;
	}
	public void setCMS_FILE_NAME(String cMS_FILE_NAME) {
		CMS_FILE_NAME = cMS_FILE_NAME;
	}
	public String getCMS_REQ_ST() {
		return CMS_REQ_ST;
	}
	public void setCMS_REQ_ST(String cMS_REQ_ST) {
		CMS_REQ_ST = cMS_REQ_ST;
	}
	public String getCMS_APP_GUBN() {
		return CMS_APP_GUBN;
	}
	public void setCMS_APP_GUBN(String cMS_APP_GUBN) {
		CMS_APP_GUBN = cMS_APP_GUBN;
	}
	public int getRn() {
		return rn;
	}
	public void setRn(int rn) {
		this.rn = rn;
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
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
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
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public List<String> getStList() {
		return stList;
	}
	public void setStList(List<String> stList) {
		this.stList = stList;
	}
	public List<String> getCmsList() {
		return cmsList;
	}
	public void setCmsList(List<String> cmsList) {
		this.cmsList = cmsList;
	}

	public String getSuccessYn() {
		return successYn;
	}

	public void setSuccessYn(String successYn) {
		this.successYn = successYn;
	}

	public String getResultCd() {
		return resultCd;
	}

	public void setResultCd(String resultCd) {
		this.resultCd = resultCd;
	}

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
}
