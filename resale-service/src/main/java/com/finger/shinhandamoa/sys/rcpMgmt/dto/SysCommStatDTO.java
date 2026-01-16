package com.finger.shinhandamoa.sys.rcpMgmt.dto;

public class SysCommStatDTO {

	private int no;				
	private int bbs;			//구분값 
	private int rn;
	
	
	private String custSt; //기관 분류
	private String totCusCount; // 총 이용기관 수 
	private String totRcp; // 이용기관 납부 수수료
	private String totFGRcp; // 핑거 수수료
	
	//전체이용기관
	private String day; //이용월
	private String custCount;	//이용기관수
	private String mgmtFee;//관리비 원
	private String mgmtFeeCount;//관리비 건
	private String smsFee;		//문자 원
	private String smsFeeCount;		//문자 건
	private String bankFee;		//은행수수료 원
	private String bankFeeCount;		//은행수수료 건
	private String cardFee;		//카드수수료 원
	private String cardFeeCount;		//카드수수료 건

	
	private String mfSum;
	private String mfCSum;
	private String sfSum;
	private String sfCSum;
	private String bfSum;
	private String bfCSum;
	private String cfSum;
	private String cfCSum;

		
	//핑거 수수료
	private String fgday; //이용월
	private String fgcustCount;	//이용기관수
	private String fingerIC;		//핑거정산 원
	private String fingerICCount;		// 건
	private String bankIC;		//은행정산 원
	private String bankICCount;		// 건
	private String cardIC;		//카드정산 건
	private String cardICCount;		// 건

	
	private String fingerSum;
	private String fingerCSum;
	private String bankSum;
	private String bankCSum;
	private String cardSum;
	private String cardCSum;


	//페이징 및 조회용 검색조건변수
	private int pageScale;
	private int curPage;
	private String startday;
	private String endday;
	private String comSt;
	private String comCd;
	private String comNm;
	

	public SysCommStatDTO() {
		super();
	}


	public int getNo() {
		return no;
	}


	public void setNo(int no) {
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


	public String getCustSt() {
		return custSt;
	}


	public void setCustSt(String custSt) {
		this.custSt = custSt;
	}


	public String getTotCusCount() {
		return totCusCount;
	}


	public void setTotCusCount(String totCusCount) {
		this.totCusCount = totCusCount;
	}


	public String getTotRcp() {
		return totRcp;
	}


	public void setTotRcp(String totRcp) {
		this.totRcp = totRcp;
	}


	public String getTotFGRcp() {
		return totFGRcp;
	}


	public void setTotFGRcp(String totFGRcp) {
		this.totFGRcp = totFGRcp;
	}


	public String getDay() {
		return day;
	}


	public void setDay(String day) {
		this.day = day;
	}


	public String getCustCount() {
		return custCount;
	}


	public void setCustCount(String custCount) {
		this.custCount = custCount;
	}


	public String getMgmtFee() {
		return mgmtFee;
	}


	public void setMgmtFee(String mgmtFee) {
		this.mgmtFee = mgmtFee;
	}


	public String getMgmtFeeCount() {
		return mgmtFeeCount;
	}


	public void setMgmtFeeCount(String mgmtFeeCount) {
		this.mgmtFeeCount = mgmtFeeCount;
	}


	public String getSmsFee() {
		return smsFee;
	}


	public void setSmsFee(String smsFee) {
		this.smsFee = smsFee;
	}


	public String getSmsFeeCount() {
		return smsFeeCount;
	}


	public void setSmsFeeCount(String smsFeeCount) {
		this.smsFeeCount = smsFeeCount;
	}


	public String getBankFee() {
		return bankFee;
	}


	public void setBankFee(String bankFee) {
		this.bankFee = bankFee;
	}


	public String getBankFeeCount() {
		return bankFeeCount;
	}


	public void setBankFeeCount(String bankFeeCount) {
		this.bankFeeCount = bankFeeCount;
	}


	public String getCardFee() {
		return cardFee;
	}


	public void setCardFee(String cardFee) {
		this.cardFee = cardFee;
	}


	public String getCardFeeCount() {
		return cardFeeCount;
	}


	public void setCardFeeCount(String cardFeeCount) {
		this.cardFeeCount = cardFeeCount;
	}


	public String getMfSum() {
		return mfSum;
	}


	public void setMfSum(String mfSum) {
		this.mfSum = mfSum;
	}


	public String getMfCSum() {
		return mfCSum;
	}


	public void setMfCSum(String mfCSum) {
		this.mfCSum = mfCSum;
	}


	public String getSfSum() {
		return sfSum;
	}


	public void setSfSum(String sfSum) {
		this.sfSum = sfSum;
	}


	public String getSfCSum() {
		return sfCSum;
	}


	public void setSfCSum(String sfCSum) {
		this.sfCSum = sfCSum;
	}


	public String getBfSum() {
		return bfSum;
	}


	public void setBfSum(String bfSum) {
		this.bfSum = bfSum;
	}


	public String getBfCSum() {
		return bfCSum;
	}


	public void setBfCSum(String bfCSum) {
		this.bfCSum = bfCSum;
	}


	public String getCfSum() {
		return cfSum;
	}


	public void setCfSum(String cfSum) {
		this.cfSum = cfSum;
	}


	public String getCfCSum() {
		return cfCSum;
	}


	public void setCfCSum(String cfCSum) {
		this.cfCSum = cfCSum;
	}


	public String getFgday() {
		return fgday;
	}


	public void setFgday(String fgday) {
		this.fgday = fgday;
	}


	public String getFgcustCount() {
		return fgcustCount;
	}


	public void setFgcustCount(String fgcustCount) {
		this.fgcustCount = fgcustCount;
	}


	public String getFingerIC() {
		return fingerIC;
	}


	public void setFingerIC(String fingerIC) {
		this.fingerIC = fingerIC;
	}


	public String getFingerICCount() {
		return fingerICCount;
	}


	public void setFingerICCount(String fingerICCount) {
		this.fingerICCount = fingerICCount;
	}


	public String getBankIC() {
		return bankIC;
	}


	public void setBankIC(String bankIC) {
		this.bankIC = bankIC;
	}


	public String getBankICCount() {
		return bankICCount;
	}


	public void setBankICCount(String bankICCount) {
		this.bankICCount = bankICCount;
	}


	public String getCardIC() {
		return cardIC;
	}


	public void setCardIC(String cardIC) {
		this.cardIC = cardIC;
	}


	public String getCardICCount() {
		return cardICCount;
	}


	public void setCardICCount(String cardICCount) {
		this.cardICCount = cardICCount;
	}


	public String getFingerSum() {
		return fingerSum;
	}


	public void setFingerSum(String fingerSum) {
		this.fingerSum = fingerSum;
	}


	public String getFingerCSum() {
		return fingerCSum;
	}


	public void setFingerCSum(String fingerCSum) {
		this.fingerCSum = fingerCSum;
	}


	public String getBankSum() {
		return bankSum;
	}


	public void setBankSum(String bankSum) {
		this.bankSum = bankSum;
	}


	public String getBankCSum() {
		return bankCSum;
	}


	public void setBankCSum(String bankCSum) {
		this.bankCSum = bankCSum;
	}


	public String getCardSum() {
		return cardSum;
	}


	public void setCardSum(String cardSum) {
		this.cardSum = cardSum;
	}


	public String getCardCSum() {
		return cardCSum;
	}


	public void setCardCSum(String cardCSum) {
		this.cardCSum = cardCSum;
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


	public String getComSt() {
		return comSt;
	}


	public void setComSt(String comSt) {
		this.comSt = comSt;
	}


	public String getComCd() {
		return comCd;
	}


	public void setComCd(String comCd) {
		this.comCd = comCd;
	}


	public String getComNm() {
		return comNm;
	}


	public void setComNm(String comNm) {
		this.comNm = comNm;
	}



}
