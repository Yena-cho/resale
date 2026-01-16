package com.finger.shinhandamoa.org.mypage.dto;

public class MyPageDTO {
    private String chaCd;        // 가맹점코드
    private String notSms;    // 청구발송 메시지
    private String notSms2;    // 미납발송 메시지
    private String rcpSms;    // 입금발송 메시지
    private String inputsms3;    // 미납발송 메시지 여기 아래로 3개 사용안하는듯...확인
    private String inputsms4;    // 기본설정1 메시지
    private String inputsms5;    // 기본설정2 메시지
    private String useSmsYn;    // SMS전송사용여부
    private String notSmsYn;    // 청구시SMS전송여부 (M:수동,   N:미사용,  A:자동 )
    private String rcpSmsYn;    // 납부시SMS전송여부 (M:수동,   N:미사용,  A:자동 )
    private String agtCd;        // 계약지점코드
    private String chaName;        // 가맹점명
    private String owner;        // 대표자
    private String ownerTel;    // 대표전화번호
    private String chaOffNo;    // 사업자번호
    private String chaStatus;    // 업종
    private String chaType;        // 업태
    private String chaAddress1;    // 주소1
    private String chaAddress2;    // 주소2
    private String chaZipCode;    // 우편번호
    private String chrName;        // 담당자명
    private String chrTelNo;    // 담당자전화번호
    private String chrHp;        // 담당자핸드폰번호
    private String chrMail;        // 담당자메일주소
    private String feeAccNo;    // 수수료계좌번호
    private String feeOffNo;    // 계좌주주민/사업자번호
    private String feeAccName;    // 계좌주명
    private String accNoCnt;    // 가상계좌 1회발급수
    private String conName;        // 승인직원이름
    private String conTelNo;    // 승인직원연락처
    private String adjAccYn;    // 다계좌사용여부
    private String rcpReqYn;    // 현금영수증신청여부
    private String rcpReqTy;    // 현금영수증자동발급여부
    private String rcpDtDupYn;    // 납부기간중복[허용:Y, 불가:N, 부분납:P]
    private String rcpSmsTy;    // 납부시SMS자동전송여부
    private String useMailYn;    // 전자메일사용여부
    private String cusNameTy;    // 납부자명:U/가맹점명:C
    private String noTaxYn;        // 과세/비과세여부
    private String chatrTy;        // 가맹점접속형태 웹(01),전문(03)
    private String chaIp;        // 가맹점아이피
    private String chaPort;        // 가맹점포트
    private String cusGubn1;    // 고객구분값1
    private String cusGubn2;    // 고객구분값2
    private String cusGubn3;    // 고객구분값3
    private String cusGubn4;    // 고객구분값4
    private String cusGubnYn1;    // 고지서출력여부1
    private String cusGubnYn2;    // 고지서출력여부2
    private String cusGubnYn3;    // 고지서출력여부3
    private String cusGubnYn4;    // 고지서출력여부4
    private String billName;    // 고지서이름
    private String billCont;    // 고지서내용
    private String cusAls;        // 납부자호칭
    private String notiChaName;
    private String telNo;
    private String remark;        // 비고
    private String remark2;        // 비고
    private String remark3;        // 비고
    private String chaSt;        // 상태
    private String preChast;    // 해피콜미완료:10, 해피콜완료:20, 교육진행완료:30, 미사용:40, 사용동의:50, 사용보류:60, 사용중:70
    private String makeDt;        // 수정일
    private String maker;        // 수정자
    private String regDt;        // 등록일
    private String usrPwd;        // 비밀번호
    private String agtName;        // 영업점명
    private String grpUsrCd;    // 그룹코드
    private String agtTelNo;    // 영업점 연락처
    private String amtChkTy;    // 납부금액체크여부
    private String rcpDueChk;    // 납부기한체크여부
    private String usePgYn;        // 온라인카드결제여부
    private String billName1;
    private String billName2;
    private String billName3;
    private String billCont1;
    private String billCont2;
    private String billCont3;
    private String billGubn;
    private String pwd;
    private String chgPwd1;
    private String chgPwd2;
    private String adjFiRegkey; //분할입금코드
    private String grpAdjName;  //분할입금명
    private String smsMsgDef1;  //sms기본메세지1
    private String smsMsgDef2;  //sms기본메세지2
    private String loginId;
    private String partialPayment;
    private String mnlRcpReqTy; // 수기수납시 현금영수증 자동발급 여부
    private String rcpReqSveTy; // 수기수납시 현금영수증 발급여부
    private String mandRcpYn; // 의무발행업체 여부

    private String atYn;
    private String notAtYn;
    private String rcpAtYn;

    public String getChaCd() {
        return chaCd;
    }

    public void setChaCd(String chaCd) {
        this.chaCd = chaCd;
    }

    public String getNotSms() {
        return notSms;
    }

    public void setNotSms(String notSms) {
        this.notSms = notSms;
    }

    public String getNotSms2() {
        return notSms2;
    }

    public void setNotSms2(String notSms2) {
        this.notSms2 = notSms2;
    }

    public String getRcpSms() {
        return rcpSms;
    }

    public void setRcpSms(String rcpSms) {
        this.rcpSms = rcpSms;
    }

    public String getInputsms3() {
        return inputsms3;
    }

    public void setInputsms3(String inputsms3) {
        this.inputsms3 = inputsms3;
    }

    public String getInputsms4() {
        return inputsms4;
    }

    public void setInputsms4(String inputsms4) {
        this.inputsms4 = inputsms4;
    }

    public String getInputsms5() {
        return inputsms5;
    }

    public void setInputsms5(String inputsms5) {
        this.inputsms5 = inputsms5;
    }

    public String getUseSmsYn() {
        return useSmsYn;
    }

    public void setUseSmsYn(String useSmsYn) {
        this.useSmsYn = useSmsYn;
    }

    public String getNotSmsYn() {
        return notSmsYn;
    }

    public void setNotSmsYn(String notSmsYn) {
        this.notSmsYn = notSmsYn;
    }

    public String getRcpSmsYn() {
        return rcpSmsYn;
    }

    public void setRcpSmsYn(String rcpSmsYn) {
        this.rcpSmsYn = rcpSmsYn;
    }

    public String getAgtCd() {
        return agtCd;
    }

    public void setAgtCd(String agtCd) {
        this.agtCd = agtCd;
    }

    public String getChaName() {
        return chaName;
    }

    public void setChaName(String chaName) {
        this.chaName = chaName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerTel() {
        return ownerTel;
    }

    public void setOwnerTel(String ownerTel) {
        this.ownerTel = ownerTel;
    }

    public String getChaOffNo() {
        return chaOffNo;
    }

    public void setChaOffNo(String chaOffNo) {
        this.chaOffNo = chaOffNo;
    }

    public String getChaStatus() {
        return chaStatus;
    }

    public void setChaStatus(String chaStatus) {
        this.chaStatus = chaStatus;
    }

    public String getChaType() {
        return chaType;
    }

    public void setChaType(String chaType) {
        this.chaType = chaType;
    }

    public String getChaAddress1() {
        return chaAddress1;
    }

    public void setChaAddress1(String chaAddress1) {
        this.chaAddress1 = chaAddress1;
    }

    public String getChaAddress2() {
        return chaAddress2;
    }

    public void setChaAddress2(String chaAddress2) {
        this.chaAddress2 = chaAddress2;
    }

    public String getChaZipCode() {
        return chaZipCode;
    }

    public void setChaZipCode(String chaZipCode) {
        this.chaZipCode = chaZipCode;
    }

    public String getChrName() {
        return chrName;
    }

    public void setChrName(String chrName) {
        this.chrName = chrName;
    }

    public String getChrTelNo() {
        return chrTelNo;
    }

    public void setChrTelNo(String chrTelNo) {
        this.chrTelNo = chrTelNo;
    }

    public String getChrHp() {
        return chrHp;
    }

    public void setChrHp(String chrHp) {
        this.chrHp = chrHp;
    }

    public String getChrMail() {
        return chrMail;
    }

    public void setChrMail(String chrMail) {
        this.chrMail = chrMail;
    }

    public String getFeeAccNo() {
        return feeAccNo;
    }

    public void setFeeAccNo(String feeAccNo) {
        this.feeAccNo = feeAccNo;
    }

    public String getFeeOffNo() {
        return feeOffNo;
    }

    public void setFeeOffNo(String feeOffNo) {
        this.feeOffNo = feeOffNo;
    }

    public String getFeeAccName() {
        return feeAccName;
    }

    public void setFeeAccName(String feeAccName) {
        this.feeAccName = feeAccName;
    }

    public String getAccNoCnt() {
        return accNoCnt;
    }

    public void setAccNoCnt(String accNoCnt) {
        this.accNoCnt = accNoCnt;
    }

    public String getConName() {
        return conName;
    }

    public void setConName(String conName) {
        this.conName = conName;
    }

    public String getConTelNo() {
        return conTelNo;
    }

    public void setConTelNo(String conTelNo) {
        this.conTelNo = conTelNo;
    }

    public String getAdjAccYn() {
        return adjAccYn;
    }

    public void setAdjAccYn(String adjAccYn) {
        this.adjAccYn = adjAccYn;
    }

    public String getRcpReqYn() {
        return rcpReqYn;
    }

    public void setRcpReqYn(String rcpReqYn) {
        this.rcpReqYn = rcpReqYn;
    }

    public String getRcpReqTy() {
        return rcpReqTy;
    }

    public void setRcpReqTy(String rcpReqTy) {
        this.rcpReqTy = rcpReqTy;
    }

    public String getRcpDtDupYn() {
        return rcpDtDupYn;
    }

    public void setRcpDtDupYn(String rcpDtDupYn) {
        this.rcpDtDupYn = rcpDtDupYn;
    }

    public String getRcpSmsTy() {
        return rcpSmsTy;
    }

    public void setRcpSmsTy(String rcpSmsTy) {
        this.rcpSmsTy = rcpSmsTy;
    }

    public String getUseMailYn() {
        return useMailYn;
    }

    public void setUseMailYn(String useMailYn) {
        this.useMailYn = useMailYn;
    }

    public String getCusNameTy() {
        return cusNameTy;
    }

    public void setCusNameTy(String cusNameTy) {
        this.cusNameTy = cusNameTy;
    }

    public String getNoTaxYn() {
        return noTaxYn;
    }

    public void setNoTaxYn(String noTaxYn) {
        this.noTaxYn = noTaxYn;
    }

    public String getChatrTy() {
        return chatrTy;
    }

    public void setChatrTy(String chatrTy) {
        this.chatrTy = chatrTy;
    }

    public String getChaIp() {
        return chaIp;
    }

    public void setChaIp(String chaIp) {
        this.chaIp = chaIp;
    }

    public String getChaPort() {
        return chaPort;
    }

    public void setChaPort(String chaPort) {
        this.chaPort = chaPort;
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

    public String getCusGubnYn1() {
        return cusGubnYn1;
    }

    public void setCusGubnYn1(String cusGubnYn1) {
        this.cusGubnYn1 = cusGubnYn1;
    }

    public String getCusGubnYn2() {
        return cusGubnYn2;
    }

    public void setCusGubnYn2(String cusGubnYn2) {
        this.cusGubnYn2 = cusGubnYn2;
    }

    public String getCusGubnYn3() {
        return cusGubnYn3;
    }

    public void setCusGubnYn3(String cusGubnYn3) {
        this.cusGubnYn3 = cusGubnYn3;
    }

    public String getCusGubnYn4() {
        return cusGubnYn4;
    }

    public void setCusGubnYn4(String cusGubnYn4) {
        this.cusGubnYn4 = cusGubnYn4;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getBillCont() {
        return billCont;
    }

    public void setBillCont(String billCont) {
        this.billCont = billCont;
    }

    public String getCusAls() {
        return cusAls;
    }

    public void setCusAls(String cusAls) {
        this.cusAls = cusAls;
    }

    public String getNotiChaName() {
        return notiChaName;
    }

    public void setNotiChaName(String notiChaName) {
        this.notiChaName = notiChaName;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    public String getChaSt() {
        return chaSt;
    }

    public void setChaSt(String chaSt) {
        this.chaSt = chaSt;
    }

    public String getPreChast() {
        return preChast;
    }

    public void setPreChast(String preChast) {
        this.preChast = preChast;
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

    public String getUsrPwd() {
        return usrPwd;
    }

    public void setUsrPwd(String usrPwd) {
        this.usrPwd = usrPwd;
    }

    public String getAgtName() {
        return agtName;
    }

    public void setAgtName(String agtName) {
        this.agtName = agtName;
    }

    public String getGrpUsrCd() {
        return grpUsrCd;
    }

    public void setGrpUsrCd(String grpUsrCd) {
        this.grpUsrCd = grpUsrCd;
    }

    public String getAgtTelNo() {
        return agtTelNo;
    }

    public void setAgtTelNo(String agtTelNo) {
        this.agtTelNo = agtTelNo;
    }

    public String getAmtChkTy() {
        return amtChkTy;
    }

    public void setAmtChkTy(String amtChkTy) {
        this.amtChkTy = amtChkTy;
    }

    public String getRcpDueChk() {
        return rcpDueChk;
    }

    public void setRcpDueChk(String rcpDueChk) {
        this.rcpDueChk = rcpDueChk;
    }

    public String getUsePgYn() {
        return usePgYn;
    }

    public void setUsePgYn(String usePgYn) {
        this.usePgYn = usePgYn;
    }

    public String getBillName1() {
        return billName1;
    }

    public void setBillName1(String billName1) {
        this.billName1 = billName1;
    }

    public String getBillName2() {
        return billName2;
    }

    public void setBillName2(String billName2) {
        this.billName2 = billName2;
    }

    public String getBillName3() {
        return billName3;
    }

    public void setBillName3(String billName3) {
        this.billName3 = billName3;
    }

    public String getBillCont1() {
        return billCont1;
    }

    public void setBillCont1(String billCont1) {
        this.billCont1 = billCont1;
    }

    public String getBillCont2() {
        return billCont2;
    }

    public void setBillCont2(String billCont2) {
        this.billCont2 = billCont2;
    }

    public String getBillCont3() {
        return billCont3;
    }

    public void setBillCont3(String billCont3) {
        this.billCont3 = billCont3;
    }

    public String getBillGubn() {
        return billGubn;
    }

    public void setBillGubn(String billGubn) {
        this.billGubn = billGubn;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getChgPwd1() {
        return chgPwd1;
    }

    public void setChgPwd1(String chgPwd1) {
        this.chgPwd1 = chgPwd1;
    }

    public String getChgPwd2() {
        return chgPwd2;
    }

    public void setChgPwd2(String chgPwd2) {
        this.chgPwd2 = chgPwd2;
    }

    public String getAdjFiRegkey() {
        return adjFiRegkey;
    }

    public void setAdjFiRegkey(String adjFiRegkey) {
        this.adjFiRegkey = adjFiRegkey;
    }

    public String getGrpAdjName() {
        return grpAdjName;
    }

    public void setGrpAdjName(String grpAdjName) {
        this.grpAdjName = grpAdjName;
    }

    public String getSmsMsgDef1() {
        return smsMsgDef1;
    }

    public void setSmsMsgDef1(String smsMsgDef1) {
        this.smsMsgDef1 = smsMsgDef1;
    }

    public String getSmsMsgDef2() {
        return smsMsgDef2;
    }

    public void setSmsMsgDef2(String smsMsgDef2) {
        this.smsMsgDef2 = smsMsgDef2;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPartialPayment() {
        return partialPayment;
    }

    public void setPartialPayment(String partialPayment) {
        this.partialPayment = partialPayment;
    }

    public String getMnlRcpReqTy() {
        return mnlRcpReqTy;
    }

    public void setMnlRcpReqTy(String mnlRcpReqTy) {
        this.mnlRcpReqTy = mnlRcpReqTy;
    }

    public String getRcpReqSveTy() {
        return rcpReqSveTy;
    }

    public void setRcpReqSveTy(String rcpReqSveTy) {
        this.rcpReqSveTy = rcpReqSveTy;
    }

    public String getMandRcpYn() {
        return mandRcpYn;
    }

    public void setMandRcpYn(String mandRcpYn) {
        this.mandRcpYn = mandRcpYn;
    }

    public String getAtYn() {
        return atYn;
    }

    public void setAtYn(String atYn) {
        this.atYn = atYn;
    }

    public String getNotAtYn() {
        return notAtYn;
    }

    public void setNotAtYn(String notAtYn) {
        this.notAtYn = notAtYn;
    }

    public String getRcpAtYn() {
        return rcpAtYn;
    }

    public void setRcpAtYn(String rcpAtYn) {
        this.rcpAtYn = rcpAtYn;
    }
}

