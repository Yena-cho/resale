package com.finger.shinhandamoa.bank.dto;



import java.util.Date;

public class BankList01DTO {

	// 가상계좌 납부자 DTO
	private String notiMasCd;	// 원장코드
	private String chaCd;		// 가맹점코드
	private String vano;		// 가상계좌번호
	private String masKey;		// 청구거래번호-청구key
	private String cusKey;		// 고객거래번호-고객key
	private String cusGubn1;	// 참조1
	private String cusGubn2;	// 참조2
	private String cusGubn3;	// 참조3
	private String cusGubn4;	// 참조4
	private String cusName;		// 고객명
	private String cusHp;		// 고객핸드폰번호
	private String cusMail;		// 고객메일주소
	private String smsYn;		// 문자전송여부
	private String mailYn;		// 전자메일여부
	private String cusOffNo;	// 현금영수증발행고객정보
	private String makeDt;		// 조작일시
	private String maker;		// 조작자
	private String  regDt;		// 등록일시
	private String disabled;	// 납부자삭제여부
	
	/*2018.04.18 추가*/
	private String rcpGubn;	    // 납부여부['Y':납부대상(default) N:납부제외]
	private String rcpReqTy;	    // 현금영수증자동발급여부('A':자동,'M':수동(default))
	
	/*화면 출력용 컬럼*/
	private String cusType;	    // 현금영수증발급용도
	private String cusMtd;	    // 현금영수증발급방법	
	
	// 실패 DTO
//	private String chaCd;	  // 기관코드
	private String xRow;      // 행번호
	private String masMonth;  // 청구월
//	private String cusKey;    // 납부자코드
//	private String cusName;   // 납부자명
//	private String cusHp;	  // 휴대전화번호
//	private String cusMail;   // 이메일
//	private String cusOffNo;  // 현금영수증인증번호
	private String startDate; // 시작일
	private String endDate;   // 종료일
	private String printDate; // 출력일
	private String xCount;    // 항목건수
	private String xAmt;      // 항목금액
	private String result;    // 결과
//	private Date   makeDt;    // 수정일
//	private String maker;     // 수정자
//	private Date   regDt;	  // 등록일
	
//	private String notiMasCd;        
	private String notiDetCd;        
//	private String cusKey;           
//	private String cusName;          
//	private String cusGubn1;         
//	private String cusGubn2;        
//	private String cusGubn3;         
//	private String cusGubn4;         
	private String payItemName;      
	private String payItemAmt;       
	private String ptrItemName;      
	private String ptrItemRemark;    
	private String rn;               
	private String cnt;
	private String totAmt;
	private String tbgubn;
	
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
	public String getxRow() {
		return xRow;
	}
	public void setxRow(String xRow) {
		this.xRow = xRow;
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
		
	@Override
	public String toString() {
		return "BankList01DTO [notiMasCd=" + notiMasCd + ", chaCd=" + chaCd + ", vano=" + vano + ", masKey=" + masKey
				+ ", cusKey=" + cusKey + ", cusGubn1=" + cusGubn1 + ", cusGubn2=" + cusGubn2 + ", cusGubn3=" + cusGubn3
				+ ", cusGubn4=" + cusGubn4 + ", cusName=" + cusName + ", cusHp=" + cusHp + ", cusMail=" + cusMail
				+ ", smsYn=" + smsYn + ", mailYn=" + mailYn + ", cusOffNo=" + cusOffNo + ", makeDt=" + makeDt
				+ ", maker=" + maker + ", regDt=" + regDt + ", disabled=" + disabled + ", rcpGubn=" + rcpGubn
				+ ", rcpReqTy=" + rcpReqTy + ", cusType=" + cusType + ", cusMtd=" + cusMtd + ", xRow=" + xRow
				+ ", masMonth=" + masMonth + ", startDate=" + startDate + ", endDate=" + endDate + ", printDate="
				+ printDate + ", xCount=" + xCount + ", xAmt=" + xAmt + ", result=" + result + ", notiDetCd="
				+ notiDetCd + ", payItemName=" + payItemName + ", payItemAmt=" + payItemAmt + ", ptrItemName="
				+ ptrItemName + ", ptrItemRemark=" + ptrItemRemark + ", rn=" + rn + ", cnt=" + cnt + ", totAmt="
				+ totAmt + ", tbgubn=" + tbgubn + ", curPage=" + curPage + ", pageScale=" + pageScale
				+ ", searchOrderBy=" + searchOrderBy + ", searchGb=" + searchGb + ", searchValue=" + searchValue
				+ ", calDateFrom=" + calDateFrom + ", calDateTo=" + calDateTo + ", cusGubn=" + cusGubn + ", payRadio="
				+ payRadio + ", statRadio=" + statRadio + ", rcpReqRadio=" + rcpReqRadio + ", cusGubnValue="
				+ cusGubnValue + "]";
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

	public String getCusGubnValue() {
		return cusGubnValue;
	}
	public void setCusGubnValue(String cusGubnValue) {
		this.cusGubnValue = cusGubnValue;
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


}

