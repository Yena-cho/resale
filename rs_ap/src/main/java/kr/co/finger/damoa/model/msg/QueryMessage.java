package kr.co.finger.damoa.model.msg;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class QueryMessage implements CommonMessage {

    private String originMessage;
    private String type = "";
    private int length;

    private String systemId= "";	//시스템ID
    private String msgSndRcvCorpCode= "";	//전문송수신기관코드
    private String sndRcvFlag= "";	//송수신 FLAG
    private String msgTypeCode= "";	//전문구분코드(MSG TYPE)
    private String dealTypeCode= "";	//거래구분코드
    private String handlingAgencyCode= "";	//취급기관코드
    private String dealSeqNo= "";	//거래일련번호
    private String msgSndDate= "";	//전문전송 일시
    private String dealStartDate= "";	//거래개시일 시
    private String resCode= "";	//응답코드
    private String usrWorkArea1= "";	//USER WORK AREA 1
    private String usrWorkArea2= "";	//USER WORK AREA 2
    private String deadlineYn= "";	//마감전후구분
    private String occurGubun= "";	//발생구분
    private String mediaGubun= "";	//매체구분
    private String institutionCode= "";	//개설기관코드
    private String terminalInfo= "";	//취급단말정보
    private String resMsg= "";	//응답 MESSAGE
    private String commonNetworkNo= "";	//공동망 고유번호

    private String depositCorpCode;                         //입금기관코드
    private String depositAccountNo;                        //입금계좌번호
    private String depositAccountName;          //입금계좌성명
    private String withdrawalCorpCode;          //출금기관코드
    private String withdrawalAccountNo;         //출금계좌번호
    private String withdrawalAccountName;       //출금계좌성명
    private String wathdrawalAccountPw;         //출금계좌비밀번호
    private String secretCardInfo;              //시크릿카정보
    private String transactionAmount;             //거래금액
    private String cashOfTransactionAmount;       //거래금액중현금금액
    private String outstandingCheckOfTransactionAmount;   //거래금액중미결제수표금액
    private String balanceSignAfterTransaction; //거래후잔액부호
    private String balanceAfterTransaction;   //거래후잔액
    private String outstandingBalanceAfterTransaction;    //거래후잔액중미결제타점권잔액
    private String amountOfFee;   //수수료금액
    private String clearingHouseCode;   //어음교환소코드
    private String referenceNo; //참조번호
    private String cancelReasonCode;    //취소사유코드
    private String accountProcessingInfo;   //계정처리정보
    private String msTrackData; //MS 카드 거래시
    private String filter;  //예비항목(사용안함)

    private String desc;

    // 추가 : 2024.09.23
    // 중계방식 기관은 chkValue (해시값) 과 (depositAccountNo + transactionAmount)을
    // 해시한 결과값이 동일한지 비교하여, 데이터의 위변조여부를 체크하기 위해 사용한다.
    private String chkValue;

    public String getDesc() {
        return desc;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }
    @Override
    public String getKey() {
        return msgSndRcvCorpCode+"_"+dealSeqNo;
    }
    @Override
    public String[] keyFields() {
        String[] strings = new String[2];
        strings[0] = "MsgSndRcvCorpCode";
        strings[1] = "DealSeqNo";
        return strings;
    }
    @Override
    public String getOriginMessage() {
        return originMessage;
    }

    @Override
    public void setOriginMessage(String originMessage) {
        this.originMessage = originMessage;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public void response() {
        setupMsgTypeCode();
        setupSndRcvFlag();
        setResCode("0000");
    }

    private void setupMsgTypeCode() {
        if (MessageCode.NOTICE_TYPE_REQUEST.getId().equalsIgnoreCase(msgTypeCode)) {
            setMsgTypeCode(MessageCode.NOTICE_TYPE_RESPONSE.getId());
        } else if (MessageCode.NOTICE_CANCEL_TYPE_REQEUEST.getId().equals(msgTypeCode)) {
            setMsgTypeCode(MessageCode.NOTICE_CANCEL_TYPE_RESPONSE.getId());
        } else {
            // ERROR...
        }
    }


    @Override
    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    @Override
    public String getMsgSndRcvCorpCode() {
        return msgSndRcvCorpCode;
    }

    public void setMsgSndRcvCorpCode(String msgSndRcvCorpCode) {
        this.msgSndRcvCorpCode = msgSndRcvCorpCode;
    }

    @Override
    public String getSndRcvFlag() {
        return sndRcvFlag;
    }

    public void setSndRcvFlag(String sndRcvFlag) {
        this.sndRcvFlag = sndRcvFlag;
    }

    @Override
    public String getMsgTypeCode() {
        return msgTypeCode;
    }

    public void setMsgTypeCode(String msgTypeCode) {
        this.msgTypeCode = msgTypeCode;
    }

    @Override
    public String getDealTypeCode() {
        return dealTypeCode;
    }

    public void setDealTypeCode(String dealTypeCode) {
        this.dealTypeCode = dealTypeCode;
    }

    @Override
    public String getHandlingAgencyCode() {
        return handlingAgencyCode;
    }

    public void setHandlingAgencyCode(String handlingAgencyCode) {
        this.handlingAgencyCode = handlingAgencyCode;
    }

    @Override
    public String getDealSeqNo() {
        return dealSeqNo;
    }

    public void setDealSeqNo(String dealSeqNo) {
        this.dealSeqNo = dealSeqNo;
    }

    @Override
    public String getMsgSndDate() {
        return msgSndDate;
    }

    public void setMsgSndDate(String msgSndDate) {
        this.msgSndDate = msgSndDate;
    }

    @Override
    public String getDealStartDate() {
        return dealStartDate;
    }

    public void setDealStartDate(String dealStartDate) {
        this.dealStartDate = dealStartDate;
    }

    @Override
    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    @Override
    public String getUsrWorkArea1() {
        return usrWorkArea1;
    }

    public void setUsrWorkArea1(String usrWorkArea1) {
        this.usrWorkArea1 = usrWorkArea1;
    }

    @Override
    public String getUsrWorkArea2() {
        return usrWorkArea2;
    }

    public void setUsrWorkArea2(String usrWorkArea2) {
        this.usrWorkArea2 = usrWorkArea2;
    }

    @Override
    public String getDeadlineYn() {
        return deadlineYn;
    }

    public void setDeadlineYn(String deadlineYn) {
        this.deadlineYn = deadlineYn;
    }

    @Override
    public String getOccurGubun() {
        return occurGubun;
    }

    public void setOccurGubun(String occurGubun) {
        this.occurGubun = occurGubun;
    }

    @Override
    public String getMediaGubun() {
        return mediaGubun;
    }

    public void setMediaGubun(String mediaGubun) {
        this.mediaGubun = mediaGubun;
    }

    @Override
    public String getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    @Override
    public String getTerminalInfo() {
        return terminalInfo;
    }

    public void setTerminalInfo(String terminalInfo) {
        this.terminalInfo = terminalInfo;
    }

    @Override
    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    @Override
    public String getCommonNetworkNo() {
        return commonNetworkNo;
    }

    public void setCommonNetworkNo(String commonNetworkNo) {
        this.commonNetworkNo = commonNetworkNo;
    }

    public String getDepositCorpCode() {
        return depositCorpCode;
    }

    public void setDepositCorpCode(String depositCorpCode) {
        this.depositCorpCode = depositCorpCode;
    }

    public String getDepositAccountNo() {
        return depositAccountNo;
    }

    public void setDepositAccountNo(String depositAccountNo) {
        this.depositAccountNo = depositAccountNo;
    }

    public String getDepositAccountName() {
        return depositAccountName;
    }

    public void setDepositAccountName(String depositAccountName) {
        this.depositAccountName = depositAccountName;
    }

    public String getWithdrawalCorpCode() {
        return withdrawalCorpCode;
    }

    public void setWithdrawalCorpCode(String withdrawalCorpCode) {
        this.withdrawalCorpCode = withdrawalCorpCode;
    }

    public String getWithdrawalAccountNo() {
        return withdrawalAccountNo;
    }

    public void setWithdrawalAccountNo(String withdrawalAccountNo) {
        this.withdrawalAccountNo = withdrawalAccountNo;
    }

    public String getWithdrawalAccountName() {
        return withdrawalAccountName;
    }

    public void setWithdrawalAccountName(String withdrawalAccountName) {
        this.withdrawalAccountName = withdrawalAccountName;
    }

    public String getWathdrawalAccountPw() {
        return wathdrawalAccountPw;
    }

    public void setWathdrawalAccountPw(String wathdrawalAccountPw) {
        this.wathdrawalAccountPw = wathdrawalAccountPw;
    }

    public String getSecretCardInfo() {
        return secretCardInfo;
    }

    public void setSecretCardInfo(String secretCardInfo) {
        this.secretCardInfo = secretCardInfo;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getCashOfTransactionAmount() {
        return cashOfTransactionAmount;
    }

    public void setCashOfTransactionAmount(String cashOfTransactionAmount) {
        this.cashOfTransactionAmount = cashOfTransactionAmount;
    }

    public String getOutstandingCheckOfTransactionAmount() {
        return outstandingCheckOfTransactionAmount;
    }

    public void setOutstandingCheckOfTransactionAmount(String outstandingCheckOfTransactionAmount) {
        this.outstandingCheckOfTransactionAmount = outstandingCheckOfTransactionAmount;
    }

    public String getBalanceSignAfterTransaction() {
        return balanceSignAfterTransaction;
    }

    public void setBalanceSignAfterTransaction(String balanceSignAfterTransaction) {
        this.balanceSignAfterTransaction = balanceSignAfterTransaction;
    }

    public String getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }

    public void setBalanceAfterTransaction(String balanceAfterTransaction) {
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    public String getOutstandingBalanceAfterTransaction() {
        return outstandingBalanceAfterTransaction;
    }

    public void setOutstandingBalanceAfterTransaction(String outstandingBalanceAfterTransaction) {
        this.outstandingBalanceAfterTransaction = outstandingBalanceAfterTransaction;
    }

    public String getAmountOfFee() {
        return amountOfFee;
    }

    public void setAmountOfFee(String amountOfFee) {
        this.amountOfFee = amountOfFee;
    }

    public String getClearingHouseCode() {
        return clearingHouseCode;
    }

    public void setClearingHouseCode(String clearingHouseCode) {
        this.clearingHouseCode = clearingHouseCode;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getCancelReasonCode() {
        return cancelReasonCode;
    }

    public void setCancelReasonCode(String cancelReasonCode) {
        this.cancelReasonCode = cancelReasonCode;
    }

    public String getAccountProcessingInfo() {
        return accountProcessingInfo;
    }

    public void setAccountProcessingInfo(String accountProcessingInfo) {
        this.accountProcessingInfo = accountProcessingInfo;
    }

    public String getMsTrackData() {
        return msTrackData;
    }

    public void setMsTrackData(String msTrackData) {
        this.msTrackData = msTrackData;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getChkValue() {
        return chkValue;
    }

    public void setChkValue(String chkValue) {
        this.chkValue = chkValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
//                .append("originMessage", originMessage)
//                .append("type", type)
                .append("length", length)
                .append("systemId", systemId)
                .append("msgSndRcvCorpCode", msgSndRcvCorpCode)
                .append("sndRcvFlag", sndRcvFlag)
                .append("msgTypeCode", msgTypeCode)
                .append("dealTypeCode", dealTypeCode)
                .append("handlingAgencyCode", handlingAgencyCode)
                .append("dealSeqNo", dealSeqNo)
                .append("msgSndDate", msgSndDate)
                .append("dealStartDate", dealStartDate)
                .append("resCode", resCode)
                .append("usrWorkArea1", usrWorkArea1)
                .append("usrWorkArea2", usrWorkArea2)
                .append("deadlineYn", deadlineYn)
                .append("occurGubun", occurGubun)
                .append("mediaGubun", mediaGubun)
                .append("institutionCode", institutionCode)
                .append("terminalInfo", terminalInfo)
                .append("resMsg", resMsg)
                .append("commonNetworkNo", commonNetworkNo)
                .append("depositCorpCode", depositCorpCode)
                .append("depositAccountNo", depositAccountNo)
                .append("depositAccountName", depositAccountName)
                .append("withdrawalCorpCode", withdrawalCorpCode)
                .append("withdrawalAccountNo", withdrawalAccountNo)
                .append("withdrawalAccountName", withdrawalAccountName)
                .append("wathdrawalAccountPw", wathdrawalAccountPw)
                .append("secretCardInfo", secretCardInfo)
                .append("transactionAmount", transactionAmount)
                .append("cashOfTransactionAmount", cashOfTransactionAmount)
                .append("outstandingCheckOfTransactionAmount", outstandingCheckOfTransactionAmount)
                .append("balanceSignAfterTransaction", balanceSignAfterTransaction)
                .append("balanceAfterTransaction", balanceAfterTransaction)
                .append("outstandingBalanceAfterTransaction", outstandingBalanceAfterTransaction)
                .append("amountOfFee", amountOfFee)
                .append("clearingHouseCode", clearingHouseCode)
                .append("referenceNo", referenceNo)
                .append("cancelReasonCode", cancelReasonCode)
                .append("accountProcessingInfo", accountProcessingInfo)
                .append("msTrackData", msTrackData)
                .append("filter", filter)
                .append("desc", desc)
                .append("chkValue", chkValue)
                .toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
