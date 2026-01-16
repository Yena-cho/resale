package com.ghvirtualaccount.vo;

import java.math.BigDecimal;

public class TbMsgSendHistVO {
	
	private String	msgSendHistId;	//문자전송이력ID
	private String	msgSendMonth;    //발송월
	private String	msgSendDttm;    //발송일시      
	private String	smsLmsInd;      //장단문구분    
	private BigDecimal	useAmt;     //사용요금      
	private String	msgContent;     //문자내용      
	private String	recvNum;        //수신번호      
	private String	sendNum;        //발신번호      
	private String	successYn;      //성공여부      
	private String	regUserId;      //등록자ID  
	private BigDecimal	msgId;      //메세지ID  
	
	public String getMsgSendHistId() {
		return msgSendHistId;
	}
	public void setMsgSendHistId(String msgSendHistId) {
		this.msgSendHistId = msgSendHistId;
	}
	public String getMsgSendDttm() {
		return msgSendDttm;
	}
	public void setMsgSendDttm(String msgSendDttm) {
		this.msgSendDttm = msgSendDttm;
	}
	public String getSmsLmsInd() {
		return smsLmsInd;
	}
	public void setSmsLmsInd(String smsLmsInd) {
		this.smsLmsInd = smsLmsInd;
	}
	public BigDecimal getUseAmt() {
		return useAmt;
	}
	public void setUseAmt(BigDecimal useAmt) {
		this.useAmt = useAmt;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public String getRecvNum() {
		return recvNum;
	}
	public void setRecvNum(String recvNum) {
		this.recvNum = recvNum;
	}
	public String getSendNum() {
		return sendNum;
	}
	public void setSendNum(String sendNum) {
		this.sendNum = sendNum;
	}
	public String getSuccessYn() {
		return successYn;
	}
	public void setSuccessYn(String successYn) {
		this.successYn = successYn;
	}
	public String getRegUserId() {
		return regUserId;
	}
	public void setRegUserId(String regUserId) {
		this.regUserId = regUserId;
	}
	public String getMsgSendMonth() {
		return msgSendMonth;
	}
	public void setMsgSendMonth(String msgSendMonth) {
		this.msgSendMonth = msgSendMonth;
	}
	public BigDecimal getMsgId() {
		return msgId;
	}
	public void setMsgId(BigDecimal msgId) {
		this.msgId = msgId;
	}
	
}
