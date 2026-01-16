package com.ghvirtualaccount.vo;

public class UserInfoVO {
	
	private String	userId;			//사용자ID      
	private String	userNm;			//성명   
	private String	userPswd;			//성명
	private String	position;		//직급          
	private String	belong;			//소속          
	private String	belongId;		//소속 ID    
	private String	dept;			//부서          
	private String	phoneNum1;		//전화번호1     
	private String	phoneNum2;		//전화번호2     
	private String	phoneNum3;		//전화번호3     
	private String	mobileNum1;		//휴대폰1       
	private String	mobileNum2;		//휴대폰2       
	private String	mobileNum3;		//휴대폰3       
	private String	emailAddr;		//이메일주소    
	private String	regDt;			//등록일        
	private String	authGrpCd;		//권한그룹코드  
	private String	authGrpCdNm;	//권한그룹코드명
	private String	useYn;			//사용여부      
	private String	useYnNm;		//사용여부명
	private String	lastConnDttm;	//마지막접속일시
	private String	memo="";   		//메모
	private String	belongPhoneNum1;		//소속 전화번호1
	private String	belongPhoneNum2;		//소속 전화번호2
	private String	belongPhoneNum3;		//소속 전화번호3
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getBelong() {
		return belong;
	}
	public void setBelong(String belong) {
		this.belong = belong;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getPhoneNum1() {
		return phoneNum1;
	}
	public void setPhoneNum1(String phoneNum1) {
		this.phoneNum1 = phoneNum1;
	}
	public String getPhoneNum2() {
		return phoneNum2;
	}
	public void setPhoneNum2(String phoneNum2) {
		this.phoneNum2 = phoneNum2;
	}
	public String getPhoneNum3() {
		return phoneNum3;
	}
	public void setPhoneNum3(String phoneNum3) {
		this.phoneNum3 = phoneNum3;
	}
	public String getMobileNum1() {
		return mobileNum1;
	}
	public void setMobileNum1(String mobileNum1) {
		this.mobileNum1 = mobileNum1;
	}
	public String getMobileNum2() {
		return mobileNum2;
	}
	public void setMobileNum2(String mobileNum2) {
		this.mobileNum2 = mobileNum2;
	}
	public String getMobileNum3() {
		return mobileNum3;
	}
	public void setMobileNum3(String mobileNum3) {
		this.mobileNum3 = mobileNum3;
	}
	public String getEmailAddr() {
		return emailAddr;
	}
	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public String getAuthGrpCd() {
		return authGrpCd;
	}
	public void setAuthGrpCd(String authGrpCd) {
		this.authGrpCd = authGrpCd;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getLastConnDttm() {
		return lastConnDttm;
	}
	public void setLastConnDttm(String lastConnDttm) {
		this.lastConnDttm = lastConnDttm;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getAuthGrpCdNm() {
		return authGrpCdNm;
	}
	public void setAuthGrpCdNm(String authGrpCdNm) {
		this.authGrpCdNm = authGrpCdNm;
	}
	public String getUseYnNm() {
		return useYnNm;
	}
	public void setUseYnNm(String useYnNm) {
		this.useYnNm = useYnNm;
	}
	public String getUserPswd() {
		return userPswd;
	}
	public void setUserPswd(String userPswd) {
		this.userPswd = userPswd;
	}
	public String getBelongId() {
		return belongId;
	}
	public void setBelongId(String belongId) {
		this.belongId = belongId;
	}
	public String getBelongPhoneNum1() {
		return belongPhoneNum1;
	}
	public void setBelongPhoneNum1(String belongPhoneNum1) {
		this.belongPhoneNum1 = belongPhoneNum1;
	}
	public String getBelongPhoneNum2() {
		return belongPhoneNum2;
	}
	public void setBelongPhoneNum2(String belongPhoneNum2) {
		this.belongPhoneNum2 = belongPhoneNum2;
	}
	public String getBelongPhoneNum3() {
		return belongPhoneNum3;
	}
	public void setBelongPhoneNum3(String belongPhoneNum3) {
		this.belongPhoneNum3 = belongPhoneNum3;
	}
}
