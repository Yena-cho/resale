package com.ghvirtualaccount.vo;

public class MsgSendHistInfoVO {
	
	private String	msgSendHistId;	//문자전송이력ID
	private String	msgSendDttm;    //발송일시      
	private String	msgContent;     //문자내용      
	private String	recvNum;        //수신번호      
	private String	successYn;      //성공여부      
	
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
	public String getSuccessYn() {
		return successYn;
	}
	public void setSuccessYn(String successYn) {
		this.successYn = successYn;
	}
}
