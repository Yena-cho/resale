package com.ghvirtualaccount.vo;

import java.math.BigDecimal;

public class TbPayerInfoMasterVO {

	private String	payerInfoId;	//납부자정보ID   
	private String	regDttm;		//등록일시
	private String	regMonth;		//등록월 
	private BigDecimal	claimCnt;     	//청구건수
	private BigDecimal	claimAmtSum;    //청구합계금액
	private String	status;		//상태 
	private String	regUserId;   	//등록자ID
	
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
	public BigDecimal getClaimCnt() {
		return claimCnt;
	}
	public void setClaimCnt(BigDecimal claimCnt) {
		this.claimCnt = claimCnt;
	}
	public BigDecimal getClaimAmtSum() {
		return claimAmtSum;
	}
	public void setClaimAmtSum(BigDecimal claimAmtSum) {
		this.claimAmtSum = claimAmtSum;
	}
	public String getRegUserId() {
		return regUserId;
	}
	public void setRegUserId(String regUserId) {
		this.regUserId = regUserId;
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
	
	

}
