package com.finger.shinhandamoa.home.dto;

public class UserOtpDTO {

	private String loginId;
	private String otpType;
	private String email;
	private String hpNo;
	private String otpNo;
	private String isSueDt;
	private String exprieDt;
	private String enableDt;
	private String loginYn;
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getOtpType() {
		return otpType;
	}
	public void setOtpType(String otpType) {
		this.otpType = otpType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHpNo() {
		return hpNo;
	}
	public void setHpNo(String hpNo) {
		this.hpNo = hpNo;
	}
	public String getOtpNo() {
		return otpNo;
	}
	public void setOtpNo(String otpNo) {
		this.otpNo = otpNo;
	}
	public String getIsSueDt() {
		return isSueDt;
	}
	public void setIsSueDt(String isSueDt) {
		this.isSueDt = isSueDt;
	}
	public String getExprieDt() {
		return exprieDt;
	}
	public void setExprieDt(String exprieDt) {
		this.exprieDt = exprieDt;
	}
	public String getEnableDt() {
		return enableDt;
	}
	public void setEnableDt(String enableDt) {
		this.enableDt = enableDt;
	}
	public String getLoginYn() {
		return loginYn;
	}
	public void setLoginYn(String loginYn) {
		this.loginYn = loginYn;
	}
	@Override
	public String toString() {
		return "UserOtpVO [loginId=" + loginId + ", otpType=" + otpType + ", email=" + email + ", hpNo=" + hpNo
				+ ", otpNo=" + otpNo + ", isSueDt=" + isSueDt + ", exprieDt=" + exprieDt + ", enableDt=" + enableDt
				+ ", loginYn=" + loginYn + "]";
	}
	
}
