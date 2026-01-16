package damoa.server.entity;

public class DamoaMsg {

	private String dRecordType	 = "";
	private String chaCd  		 = "";
	private String workField     = "";    // 처리내역
	private String result_cd     = "";    // 결과코드
	private String result_msg    = "";    // 결과메시지
	private String trNo          = "";    // 청구거래번호
	private String vano          = "";    // 가상계좌번호
	private String payMasMonth   = "";    // 청구월
	private String payMasDt      = "";    // 청구일자
	private String rcpStartDt    = "";    // 수납시작일
	private String rcpStartTm    = "";    // 수납시작시각
	private String rcpEndDt      = "";    // 수납종료일
	private String rcpEndTm      = "";    // 수납종료시각
	private String rcpPrtEndDt   = "";    // 출력납부마감일
	private String rcpPrtEndTm   = "";    // 출력납부마감시각
	private String cusNm         = "";    // 고객명
	private String smsYn         = "";    // sms전송여부
	private String mobileNo      = "";    // 휴대번호
	private String emailYn       = "";    // email전송여부
	private String email         = "";    // email
	private String content1      = "";    // 참조1
	private String content2      = "";    // 참조2
	private String content3      = "";    // 참조3
	private String content4      = "";    // 참조4
	private String regSeq        = "";    // 전문번호

	private String chaTrNo       = "";    // 항목거래번호
	private String adjfiGrpCd    = "";    // 신한은행발급배분키
	private String payItemNm     = "";    // 항목명
	private String payItemAmt    = "";    // 항목금액
	private String payItemYN     = "";    // 항목필수여부
	private String rcpItemYN     = "";    // 현금영수증발행여부
	private String rcpBusinessNo = "";    // 현금영수증발행사업자번호
	private String rcpPersonNo   = "";    // 현금영수증발행사업자번호
	private String prtPayItemNm  = "";    // 출력항목명
	private String prtContent1   = "";    // 출력비고내용
	private String prtSeq        = "";    // 출력순서
	private String rcpPersonNo2  = "";    // 현금영수증발행사업자번호(카드)

	public String getdRecordType() {
		return dRecordType;
	}
	public void setdRecordType(String dRecordType) {
		this.dRecordType = dRecordType;
	}
	public String getChaCd() {
		return chaCd;
	}
	public void setChaCd(String chaCd) {
		this.chaCd = chaCd;
	}
	public String getWorkField() {
		return workField;
	}
	public void setWorkField(String workField) {
		this.workField = workField;
	}
	public String getResult_cd() {
		return result_cd;
	}
	public void setResult_cd(String resultCd) {
		result_cd = resultCd;
	}
	public String getResult_msg() {
		return result_msg;
	}
	public void setResult_msg(String resultMsg) {
		result_msg = resultMsg;
	}
	public String getTrNo() {
		return trNo;
	}
	public void setTrNo(String trNo) {
		this.trNo = trNo;
	}
	public String getVano() {
		return vano;
	}
	public void setVano(String vano) {
		this.vano = vano;
	}
	public String getPayMasMonth() {
		return payMasMonth;
	}
	public void setPayMasMonth(String payMasMonth) {
		this.payMasMonth = payMasMonth;
	}
	public String getPayMasDt() {
		return payMasDt;
	}
	public void setPayMasDt(String payMasDt) {
		this.payMasDt = payMasDt;
	}
	public String getRcpStartDt() {
		return rcpStartDt;
	}
	public void setRcpStartDt(String rcpStartDt) {
		this.rcpStartDt = rcpStartDt;
	}
	public String getRcpStartTm() {
		return rcpStartTm;
	}
	public void setRcpStartTm(String rcpStartTm) {
		this.rcpStartTm = rcpStartTm;
	}
	public String getRcpEndDt() {
		return rcpEndDt;
	}
	public void setRcpEndDt(String rcpEndDt) {
		this.rcpEndDt = rcpEndDt;
	}
	public String getRcpEndTm() {
		return rcpEndTm;
	}
	public void setRcpEndTm(String rcpEndTm) {
		this.rcpEndTm = rcpEndTm;
	}
	public String getRcpPrtEndDt() {
		return rcpPrtEndDt;
	}
	public void setRcpPrtEndDt(String rcpPrtEndDt) {
		this.rcpPrtEndDt = rcpPrtEndDt;
	}
	public String getRcpPrtEndTm() {
		return rcpPrtEndTm;
	}
	public void setRcpPrtEndTm(String rcpPrtEndTm) {
		this.rcpPrtEndTm = rcpPrtEndTm;
	}
	public String getCusNm() {
		return cusNm;
	}
	public void setCusNm(String cusNm) {
		this.cusNm = cusNm;
	}
	public String getSmsYn() {
		return smsYn;
	}
	public void setSmsYn(String smsYn) {
		this.smsYn = smsYn;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getEmailYn() {
		return emailYn;
	}
	public void setEmailYn(String emailYn) {
		this.emailYn = emailYn;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContent1() {
		return content1;
	}
	public void setContent1(String content1) {
		this.content1 = content1;
	}
	public String getContent2() {
		return content2;
	}
	public void setContent2(String content2) {
		this.content2 = content2;
	}
	public String getContent3() {
		return content3;
	}
	public void setContent3(String content3) {
		this.content3 = content3;
	}
	public String getContent4() {
		return content4;
	}
	public void setContent4(String content4) {
		this.content4 = content4;
	}
	public String getRegSeq() {
		return regSeq;
	}
	public void setRegSeq(String regSeq) {
		this.regSeq = regSeq;
	}
	public String getChaTrNo() {
		return chaTrNo;
	}
	public void setChaTrNo(String chaTrNo) {
		this.chaTrNo = chaTrNo;
	}
	public String getAdjfiGrpCd() {
		return adjfiGrpCd;
	}
	public void setAdjfiGrpCd(String adjfiGrpCd) {
		this.adjfiGrpCd = adjfiGrpCd;
	}
	public String getPayItemNm() {
		return payItemNm;
	}
	public void setPayItemNm(String payItemNm) {
		this.payItemNm = payItemNm;
	}
	public String getPayItemAmt() {
		return payItemAmt;
	}
	public void setPayItemAmt(String payItemAmt) {
		this.payItemAmt = payItemAmt;
	}
	public String getPayItemYN() {
		return payItemYN;
	}
	public void setPayItemYN(String payItemYN) {
		this.payItemYN = payItemYN;
	}
	public String getRcpItemYN() {
		return rcpItemYN;
	}
	public void setRcpItemYN(String rcpItemYN) {
		this.rcpItemYN = rcpItemYN;
	}
	public String getRcpBusinessNo() {
		return rcpBusinessNo;
	}
	public void setRcpBusinessNo(String rcpBusinessNo) {
		this.rcpBusinessNo = rcpBusinessNo;
	}
	public String getRcpPersonNo() {
		return rcpPersonNo;
	}
	public void setRcpPersonNo(String rcpPersonNo) {
		this.rcpPersonNo = rcpPersonNo;
	}
	public String getPrtPayItemNm() {
		return prtPayItemNm;
	}
	public void setPrtPayItemNm(String prtPayItemNm) {
		this.prtPayItemNm = prtPayItemNm;
	}
	public String getPrtContent1() {
		return prtContent1;
	}
	public void setPrtContent1(String prtContent1) {
		this.prtContent1 = prtContent1;
	}
	public String getPrtSeq() {
		return prtSeq;
	}
	public void setPrtSeq(String prtSeq) {
		this.prtSeq = prtSeq;
	}
	public String getRcpPersonNo2() {
		return rcpPersonNo2;
	}
	public void setRcpPersonNo2(String rcpPersonNo2) {
		this.rcpPersonNo2 = rcpPersonNo2;
	}
	public void clear(){
		chaCd  		  = "";
		workField     = "";    // 처리내역
		result_cd     = "";    // 결과코드
		result_msg    = "";    // 결과메시지
		trNo          = "";    // 청구거래번호
		vano          = "";    // 가상계좌번호
		payMasMonth   = "";    // 청구월
		payMasDt      = "";    // 청구일자
		rcpStartDt    = "";    // 수납시작일
		rcpStartTm    = "";    // 수납시작시각
		rcpEndDt      = "";    // 수납종료일
		rcpEndTm      = "";    // 수납종료시각
		rcpPrtEndDt   = "";    // 출력납부마감일
		rcpPrtEndTm   = "";    // 출력납부마감시각
		cusNm         = "";    // 고객명
		smsYn         = "";    // sms전송여부
		mobileNo      = "";    // 휴대번호
		emailYn       = "";    // email전송여부
		email         = "";    // email
		content1      = "";    // 참조1
		content2      = "";    // 참조2
		content3      = "";    // 참조3
		content4      = "";    // 참조4
		regSeq        = "";    // 전문번호

		chaTrNo       = "";    // 항목거래번호
		adjfiGrpCd    = "";    // 신한은행발급배분키
		payItemNm     = "";    // 항목명
		payItemAmt    = "";    // 항목금액
		payItemYN     = "";    // 항목필수여부
		rcpItemYN     = "";    // 현금영수증발행여부
		rcpBusinessNo = "";    // 현금영수증발행사업자번호
		rcpPersonNo   = "";    // 현금영수증발행사업자번호
		prtPayItemNm  = "";    // 출력항목명
		prtContent1   = "";    // 출력비고내용
		prtSeq        = "";    // 출력순서
		rcpPersonNo2  = "";    // 현금영수증발행사업자번호(카드)
	}
}
