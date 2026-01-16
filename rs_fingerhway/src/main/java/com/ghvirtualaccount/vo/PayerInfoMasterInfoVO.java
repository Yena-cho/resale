package com.ghvirtualaccount.vo;

public class PayerInfoMasterInfoVO {

	private String	payerInfoId;	//납부자정보ID   
	private String	regDttm;		//등록일시
	private String	regMonth;		//등록월 
	private String	claimCnt;     	//청구건수
	private String	claimAmtSum;    //청구합계금액
	private String	status;			//상태
	private String	statusNm;		//상태명 
	private String	regUserId;   	//등록자ID
	private String	regUserNm;   	//등록자명
	
	public String getPayerInfoId() {
		return payerInfoId;
	}
	public void setPayerInfoId(String payerInfoId) {
		this.payerInfoId = payerInfoId;
	}
	public String getRegDttm() {
		return regDttm;
	}
	public void setRegDttm(String regDttm) {
		this.regDttm = regDttm;
	}
	public String getClaimCnt() {
		return claimCnt;
	}
	public void setClaimCnt(String claimCnt) {
		this.claimCnt = claimCnt;
	}
	public String getClaimAmtSum() {
		return claimAmtSum;
	}
	public void setClaimAmtSum(String claimAmtSum) {
		this.claimAmtSum = claimAmtSum;
	}
	public String getRegUserId() {
		return regUserId;
	}
	public void setRegUserId(String regUserId) {
		this.regUserId = regUserId;
	}
	public String getRegUserNm() {
		return regUserNm;
	}
	public void setRegUserNm(String regUserNm) {
		this.regUserNm = regUserNm;
	}
	public String getRegMonth() {
		return regMonth;
	}
	public void setRegMonth(String regMonth) {
		this.regMonth = regMonth;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusNm() {
		return statusNm;
	}
	public void setStatusNm(String statusNm) {
		this.statusNm = statusNm;
	}
	
	

}
