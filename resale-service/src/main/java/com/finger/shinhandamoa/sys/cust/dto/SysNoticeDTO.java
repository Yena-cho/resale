package com.finger.shinhandamoa.sys.cust.dto;

public class SysNoticeDTO {

	private long no;		
	private int rn;
	private String userStatus; //chast
	private String userDate; // chaty
	private String userClass; // usety
	
	private String startday;
	private String endday;

	private String smsReqCd; //smsreqcd
	private String chaCd; //chacd
	private String writer;
	private String hpNo; //hpno
	private String status; //status
	
	private String reqDate; //reqdt
	private String telNo; //SEND_TELNO
	private String msgTy; // msgty
	private String sendCnt; // SEND_CNT
	private String failCnt; // FAIL_CNT
	private String id; //SEND_ID
	private String smsTy; // SMSTY
	private String smsSeq; // SMSSEQ
	private String title; //title
	private String smsMsg; //msg
	
	private String emSendCnt; // SEND_CNT
	private String emFailCnt; // FAIL_CNT
	private String emailTy; // SMSTY
	private String emailSeq; // SMSSEQ
	private String emailChast; // 대상구분
	private String emUseTy; // msgty
	private String emChaTy; // msgty
	private String emailTitle; //mail title
	private String emailMsg; //email message
	private String emId; //SEND_ID
	private String emReqDate; //reqdt
	
	private String cusMail;
	private String mail;
	private String chaName;
	
	private int curPage;
	private int pageScale;

	private String emailAdr;
	private String etitle;
	private String econtent;
	private String phoneNo;
	private String stitle;
	private String scontent;

	public String getEmailAdr() {
		return emailAdr;
	}

	public void setEmailAdr(String emailAdr) {
		this.emailAdr = emailAdr;
	}

	public String getEtitle() {
		return etitle;
	}

	public void setEtitle(String etitle) {
		this.etitle = etitle;
	}

	public String getEcontent() {
		return econtent;
	}

	public void setEcontent(String econtent) {
		this.econtent = econtent;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getStitle() {
		return stitle;
	}

	public void setStitle(String stitle) {
		this.stitle = stitle;
	}

	public String getScontent() {
		return scontent;
	}

	public void setScontent(String scontent) {
		this.scontent = scontent;
	}

	public String getEmSendCnt() {
		return emSendCnt;
	}
	public void setEmSendCnt(String emSendCnt) {
		this.emSendCnt = emSendCnt;
	}
	public String getEmFailCnt() {
		return emFailCnt;
	}
	public void setEmFailCnt(String emFailCnt) {
		this.emFailCnt = emFailCnt;
	}
	public String getEmailTy() {
		return emailTy;
	}
	public void setEmailTy(String emailTy) {
		this.emailTy = emailTy;
	}
	public String getEmailSeq() {
		return emailSeq;
	}
	public void setEmailSeq(String emailSeq) {
		this.emailSeq = emailSeq;
	}
	public String getEmailChast() {
		return emailChast;
	}
	public void setEmailChast(String emailChast) {
		this.emailChast = emailChast;
	}
	public String getEmUseTy() {
		return emUseTy;
	}
	public void setEmUseTy(String emUseTy) {
		this.emUseTy = emUseTy;
	}
	public String getEmChaTy() {
		return emChaTy;
	}
	public void setEmChaTy(String emChaTy) {
		this.emChaTy = emChaTy;
	}
	public String getEmailTitle() {
		return emailTitle;
	}
	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}
	public String getEmId() {
		return emId;
	}
	public void setEmId(String emId) {
		this.emId = emId;
	}
	public String getEmReqDate() {
		return emReqDate;
	}
	public void setEmReqDate(String emReqDate) {
		this.emReqDate = emReqDate;
	}
	public String getCusMail() {
		return cusMail;
	}
	public void setCusMail(String cusMail) {
		this.cusMail = cusMail;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getChaName() {
		return chaName;
	}
	public void setChaName(String chaName) {
		this.chaName = chaName;
	}
	public String getEmailMsg() {
		return emailMsg;
	}
	public void setEmailMsg(String emailMsg) {
		this.emailMsg = emailMsg;
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
	public int getRn() {
		return rn;
	}
	public void setRn(int rn) {
		this.rn = rn;
	}
	public long getNo() {
		return no;
	}
	public void setNo(long no) {
		this.no = no;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getUserDate() {
		return userDate;
	}
	public void setUserDate(String userDate) {
		this.userDate = userDate;
	}
	public String getUserClass() {
		return userClass;
	}
	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSmsMsg() {
		return smsMsg;
	}
	public void setSmsMsg(String smsMsg) {
		this.smsMsg = smsMsg;
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
	public String getSmsReqCd() {
		return smsReqCd;
	}
	public void setSmsReqCd(String smsReqCd) {
		this.smsReqCd = smsReqCd;
	}
	public String getChaCd() {
		return chaCd;
	}
	public void setChaCd(String chaCd) {
		this.chaCd = chaCd;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getHpNo() {
		return hpNo;
	}
	public void setHpNo(String hpNo) {
		this.hpNo = hpNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReqDate() {
		return reqDate;
	}
	public void setReqDate(String reqDate) {
		this.reqDate = reqDate;
	}
	public String getTelNo() {
		return telNo;
	}
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}
	public String getMsgTy() {
		return msgTy;
	}
	public void setMsgTy(String msgTy) {
		this.msgTy = msgTy;
	}
	public String getSendCnt() {
		return sendCnt;
	}
	public void setSendCnt(String sendCnt) {
		this.sendCnt = sendCnt;
	}
	public String getFailCnt() {
		return failCnt;
	}
	public void setFailCnt(String failCnt) {
		this.failCnt = failCnt;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSmsTy() {
		return smsTy;
	}
	public void setSmsTy(String smsTy) {
		this.smsTy = smsTy;
	}
	public String getSmsSeq() {
		return smsSeq;
	}
	public void setSmsSeq(String smsSeq) {
		this.smsSeq = smsSeq;
	}
	@Override
	public String toString() {
		return "SysNoticeDTO [no=" + no + ", userStatus=" + userStatus + ", userDate=" + userDate + ", userClass="
				+ userClass + ", title=" + title + ", smsMsg=" + smsMsg + ", startday=" + startday + ", endday="
				+ endday + ", smsReqCd=" + smsReqCd + ", chaCd=" + chaCd + ", writer=" + writer + ", hpNo=" + hpNo
				+ ", status=" + status + ", reqDate=" + reqDate + ", telNo=" + telNo + ", msgTy=" + msgTy + ", sendCnt="
				+ sendCnt + ", failCnt=" + failCnt + ", id=" + id + ", smsTy=" + smsTy + ", smsSeq=" + smsSeq + "]";
	}

		
}
