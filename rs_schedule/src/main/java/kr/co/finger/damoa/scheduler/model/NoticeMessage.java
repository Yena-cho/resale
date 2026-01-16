package kr.co.finger.damoa.scheduler.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.nio.charset.Charset;

public class NoticeMessage {
    private String length;
    private String systemId = "";    //시스템ID
    private String msgSndRcvCorpCode = "";    //전문송수신기관코드
    private String sndRcvFlag = "";    //송수신 FLAG
    private String msgTypeCode = "";    //전문구분코드(MSG TYPE)
    private String dealTypeCode = "";    //거래구분코드
    private String handlingAgencyCode = "";    //취급기관코드
    private String dealSeqNo = "";    //거래일련번호
    private String msgSndDate = "";    //전문전송 일시
    private String dealStartDate = "";    //거래개시일 시
    private String resCode = "";    //응답코드
    private String usrWorkArea1 = "";    //USER WORK AREA 1
    private String usrWorkArea2 = "";    //USER WORK AREA 2
    private String deadlineYn = "";    //마감전후구분 마감전0, 마감후1
    private String occurGubun = "";    //발생구분
    private String mediaGubun = "";    //매체구분
    private String institutionCode = "";    //개설기관코드
    private String terminalInfo = "";    //취급단말정보
    private String resMsg = "";    //응답 MESSAGE
    private String commonNetworkNo = "";    //공동망 고유번호

    private String depositCorpCode;             //입금기관코드
    private String depositAccountNo;            //입금계좌번호
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
    private String virtualAccountDivideCount = "";    //가상계좌거래배분건수
    private String virtualAccountKey01 = "";    //배분대리점번호01
    private String virtualAccountAmount01 = "";    //배분금액01
    private String filler01 = "";    //Filler
    private String virtualAccountKey02 = "";    //배분대리점번호02
    private String virtualAccountAmount02 = "";    //배분금액02
    private String filler02 = "";    //Filler
    private String virtualAccountKey03 = "";    //배분대리점번호03
    private String virtualAccountAmount03 = "";    //배분금액03
    private String filler03 = "";    //Filler
    private String virtualAccountKey04 = "";    //배분대리점번호04
    private String virtualAccountAmount04 = "";    //배분금액04
    private String filler04 = "";    //Filler
    private String virtualAccountKey05 = "";    //배분대리점번호05
    private String virtualAccountAmount05 = "";    //배분금액05
    private String filler05 = "";    //Filler
    private String virtualAccountKey06 = "";    //배분대리점번호06
    private String virtualAccountAmount06 = "";    //배분금액06
    private String filler06 = "";    //Filler
    private String virtualAccountKey07 = "";    //배분대리점번호07
    private String virtualAccountAmount07 = "";    //배분금액07
    private String filler07 = "";    //Filler
    private String virtualAccountKey08 = "";    //배분대리점번호08
    private String virtualAccountAmount08 = "";    //배분금액08
    private String filler08 = "";    //Filler
    private String virtualAccountKey09 = "";    //배분대리점번호09
    private String virtualAccountAmount09 = "";    //배분금액09
    private String filler09 = "";    //Filler

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getMsgSndRcvCorpCode() {
        return msgSndRcvCorpCode;
    }

    public void setMsgSndRcvCorpCode(String msgSndRcvCorpCode) {
        this.msgSndRcvCorpCode = msgSndRcvCorpCode;
    }

    public String getSndRcvFlag() {
        return sndRcvFlag;
    }

    public void setSndRcvFlag(String sndRcvFlag) {
        this.sndRcvFlag = sndRcvFlag;
    }

    public String getMsgTypeCode() {
        return msgTypeCode;
    }

    public void setMsgTypeCode(String msgTypeCode) {
        this.msgTypeCode = msgTypeCode;
    }

    public String getDealTypeCode() {
        return dealTypeCode;
    }

    public void setDealTypeCode(String dealTypeCode) {
        this.dealTypeCode = dealTypeCode;
    }

    public String getHandlingAgencyCode() {
        return handlingAgencyCode;
    }

    public void setHandlingAgencyCode(String handlingAgencyCode) {
        this.handlingAgencyCode = handlingAgencyCode;
    }

    public String getDealSeqNo() {
        return dealSeqNo;
    }

    public void setDealSeqNo(String dealSeqNo) {
        this.dealSeqNo = dealSeqNo;
    }

    public String getMsgSndDate() {
        return msgSndDate;
    }

    public void setMsgSndDate(String msgSndDate) {
        this.msgSndDate = msgSndDate;
    }

    public String getDealStartDate() {
        return dealStartDate;
    }

    public void setDealStartDate(String dealStartDate) {
        this.dealStartDate = dealStartDate;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getUsrWorkArea1() {
        return usrWorkArea1;
    }

    public void setUsrWorkArea1(String usrWorkArea1) {
        this.usrWorkArea1 = usrWorkArea1;
    }

    public String getUsrWorkArea2() {
        return usrWorkArea2;
    }

    public void setUsrWorkArea2(String usrWorkArea2) {
        this.usrWorkArea2 = usrWorkArea2;
    }

    public String getDeadlineYn() {
        return deadlineYn;
    }

    public void setDeadlineYn(String deadlineYn) {
        this.deadlineYn = deadlineYn;
    }

    public String getOccurGubun() {
        return occurGubun;
    }

    public void setOccurGubun(String occurGubun) {
        this.occurGubun = occurGubun;
    }

    public String getMediaGubun() {
        return mediaGubun;
    }

    public void setMediaGubun(String mediaGubun) {
        this.mediaGubun = mediaGubun;
    }

    public String getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    public String getTerminalInfo() {
        return terminalInfo;
    }

    public void setTerminalInfo(String terminalInfo) {
        this.terminalInfo = terminalInfo;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

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

    public String getVirtualAccountDivideCount() {
        return virtualAccountDivideCount;
    }

    public void setVirtualAccountDivideCount(String virtualAccountDivideCount) {
        this.virtualAccountDivideCount = virtualAccountDivideCount;
    }

    public String getVirtualAccountKey01() {
        return virtualAccountKey01;
    }

    public void setVirtualAccountKey01(String virtualAccountKey01) {
        this.virtualAccountKey01 = virtualAccountKey01;
    }

    public String getVirtualAccountAmount01() {
        return virtualAccountAmount01;
    }

    public void setVirtualAccountAmount01(String virtualAccountAmount01) {
        this.virtualAccountAmount01 = virtualAccountAmount01;
    }

    public String getFiller01() {
        return filler01;
    }

    public void setFiller01(String filler01) {
        this.filler01 = filler01;
    }

    public String getVirtualAccountKey02() {
        return virtualAccountKey02;
    }

    public void setVirtualAccountKey02(String virtualAccountKey02) {
        this.virtualAccountKey02 = virtualAccountKey02;
    }

    public String getVirtualAccountAmount02() {
        return virtualAccountAmount02;
    }

    public void setVirtualAccountAmount02(String virtualAccountAmount02) {
        this.virtualAccountAmount02 = virtualAccountAmount02;
    }

    public String getFiller02() {
        return filler02;
    }

    public void setFiller02(String filler02) {
        this.filler02 = filler02;
    }

    public String getVirtualAccountKey03() {
        return virtualAccountKey03;
    }

    public void setVirtualAccountKey03(String virtualAccountKey03) {
        this.virtualAccountKey03 = virtualAccountKey03;
    }

    public String getVirtualAccountAmount03() {
        return virtualAccountAmount03;
    }

    public void setVirtualAccountAmount03(String virtualAccountAmount03) {
        this.virtualAccountAmount03 = virtualAccountAmount03;
    }

    public String getFiller03() {
        return filler03;
    }

    public void setFiller03(String filler03) {
        this.filler03 = filler03;
    }

    public String getVirtualAccountKey04() {
        return virtualAccountKey04;
    }

    public void setVirtualAccountKey04(String virtualAccountKey04) {
        this.virtualAccountKey04 = virtualAccountKey04;
    }

    public String getVirtualAccountAmount04() {
        return virtualAccountAmount04;
    }

    public void setVirtualAccountAmount04(String virtualAccountAmount04) {
        this.virtualAccountAmount04 = virtualAccountAmount04;
    }

    public String getFiller04() {
        return filler04;
    }

    public void setFiller04(String filler04) {
        this.filler04 = filler04;
    }

    public String getVirtualAccountKey05() {
        return virtualAccountKey05;
    }

    public void setVirtualAccountKey05(String virtualAccountKey05) {
        this.virtualAccountKey05 = virtualAccountKey05;
    }

    public String getVirtualAccountAmount05() {
        return virtualAccountAmount05;
    }

    public void setVirtualAccountAmount05(String virtualAccountAmount05) {
        this.virtualAccountAmount05 = virtualAccountAmount05;
    }

    public String getFiller05() {
        return filler05;
    }

    public void setFiller05(String filler05) {
        this.filler05 = filler05;
    }

    public String getVirtualAccountKey06() {
        return virtualAccountKey06;
    }

    public void setVirtualAccountKey06(String virtualAccountKey06) {
        this.virtualAccountKey06 = virtualAccountKey06;
    }

    public String getVirtualAccountAmount06() {
        return virtualAccountAmount06;
    }

    public void setVirtualAccountAmount06(String virtualAccountAmount06) {
        this.virtualAccountAmount06 = virtualAccountAmount06;
    }

    public String getFiller06() {
        return filler06;
    }

    public void setFiller06(String filler06) {
        this.filler06 = filler06;
    }

    public String getVirtualAccountKey07() {
        return virtualAccountKey07;
    }

    public void setVirtualAccountKey07(String virtualAccountKey07) {
        this.virtualAccountKey07 = virtualAccountKey07;
    }

    public String getVirtualAccountAmount07() {
        return virtualAccountAmount07;
    }

    public void setVirtualAccountAmount07(String virtualAccountAmount07) {
        this.virtualAccountAmount07 = virtualAccountAmount07;
    }

    public String getFiller07() {
        return filler07;
    }

    public void setFiller07(String filler07) {
        this.filler07 = filler07;
    }

    public String getVirtualAccountKey08() {
        return virtualAccountKey08;
    }

    public void setVirtualAccountKey08(String virtualAccountKey08) {
        this.virtualAccountKey08 = virtualAccountKey08;
    }

    public String getVirtualAccountAmount08() {
        return virtualAccountAmount08;
    }

    public void setVirtualAccountAmount08(String virtualAccountAmount08) {
        this.virtualAccountAmount08 = virtualAccountAmount08;
    }

    public String getFiller08() {
        return filler08;
    }

    public void setFiller08(String filler08) {
        this.filler08 = filler08;
    }

    public String getVirtualAccountKey09() {
        return virtualAccountKey09;
    }

    public void setVirtualAccountKey09(String virtualAccountKey09) {
        this.virtualAccountKey09 = virtualAccountKey09;
    }

    public String getVirtualAccountAmount09() {
        return virtualAccountAmount09;
    }

    public void setVirtualAccountAmount09(String virtualAccountAmount09) {
        this.virtualAccountAmount09 = virtualAccountAmount09;
    }

    public String getFiller09() {
        return filler09;
    }

    public void setFiller09(String filler09) {
        this.filler09 = filler09;
    }

    public void setupMsg(byte[] bytes) {

        length = newString(bytes,0,4);
        systemId = newString(bytes,4,3);
        msgSndRcvCorpCode = newString(bytes,7,8);
        sndRcvFlag = newString(bytes,15,1);
        msgTypeCode = newString(bytes,16,4);
        dealTypeCode = newString(bytes,20,4);
        handlingAgencyCode = newString(bytes,24,8);
        dealSeqNo = newString(bytes,32,7);
        msgSndDate = newString(bytes,39,14);
        dealStartDate = newString(bytes,53,14);
        resCode = newString(bytes,67,4);
        usrWorkArea1 = newString(bytes,71,15);
        usrWorkArea2 = newString(bytes,86,15);
        deadlineYn = newString(bytes,101,1);
        occurGubun = newString(bytes,102,1);
        mediaGubun = newString(bytes,103,1);
        institutionCode = newString(bytes,104,8);
        terminalInfo = newString(bytes,112,25);
        resMsg = newString(bytes,137,50);
        commonNetworkNo = newString(bytes,187,13);
        depositCorpCode = newString(bytes,200,8);
        depositAccountNo = newString(bytes,208,16);
        depositAccountName = newString(bytes,224,20);
        withdrawalCorpCode = newString(bytes,244,8);
        withdrawalAccountNo = newString(bytes,252,16);
        withdrawalAccountName = newString(bytes,268,20);
        wathdrawalAccountPw = newString(bytes,288,16);
        secretCardInfo = newString(bytes,304,20);
        transactionAmount = newString(bytes,324,13);
        cashOfTransactionAmount = newString(bytes,337,13);
        outstandingCheckOfTransactionAmount = newString(bytes,350,13);
        balanceSignAfterTransaction = newString(bytes,363,1);
        balanceAfterTransaction = newString(bytes,364,13);
        outstandingBalanceAfterTransaction = newString(bytes,377,13);
        amountOfFee = newString(bytes,390,28);
        clearingHouseCode = newString(bytes,418,6);
        referenceNo = newString(bytes,424,20);
        cancelReasonCode = newString(bytes,444,4);
        accountProcessingInfo = newString(bytes,448,87);
        msTrackData = newString(bytes,535,60);
        filter = newString(bytes,595,5);
        virtualAccountDivideCount = newString(bytes,600,2);
        virtualAccountKey01 = newString(bytes,602,5);
        virtualAccountAmount01 = newString(bytes,607,13);
        filler01 = newString(bytes,620,1);
        virtualAccountKey02 = newString(bytes,621,5);
        virtualAccountAmount02 = newString(bytes,626,13);
        filler02 = newString(bytes,639,1);
        virtualAccountKey03 = newString(bytes,640,5);
        virtualAccountAmount03 = newString(bytes,645,13);
        filler03 = newString(bytes,658,1);
        virtualAccountKey04 = newString(bytes,659,5);
        virtualAccountAmount04 = newString(bytes,664,13);
        filler04 = newString(bytes,677,1);
        virtualAccountKey05 = newString(bytes,678,5);
        virtualAccountAmount05 = newString(bytes,683,13);
        filler05 = newString(bytes,696,1);
        virtualAccountKey06 = newString(bytes,697,5);
        virtualAccountAmount06 = newString(bytes,702,13);
        filler06 = newString(bytes,715,1);
        virtualAccountKey07 = newString(bytes,716,5);
        virtualAccountAmount07 = newString(bytes,721,13);
        filler07 = newString(bytes,734,1);
        virtualAccountKey08 = newString(bytes,735,5);
        virtualAccountAmount08 = newString(bytes,740,13);
        filler08 = newString(bytes,753,1);
        virtualAccountKey09 = newString(bytes,754,5);
        virtualAccountAmount09 = newString(bytes,759,13);
        filler09 = newString(bytes,772,28);
    }

    public static String newString(byte[] rcvByte, int offset, int length) {
        return new String(rcvByte, offset, length, Charset.forName("EUC-KR")).trim();
    }


    public static void main(String[] args) {
        NoticeMessage noticeMessage = new NoticeMessage();
        noticeMessage.setupMsg("0800VAS2000149920200311000000000056462620170531150205201705311502040000       0065634                028200068920171800000000000000000000                                                  004FTV00052972000689256211899951639                      00000004123456789012    김영준                                                  000000017000000000000000000000000000000 000000000000000000000000000000000000000000003000000000                              100140011573280    1143901718142890100                     0171814289010001718641630901                                                                                                                                                                                                                                                                         ".getBytes(Charset.forName("EUC-KR")));

        System.out.println(noticeMessage);
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
                .append("virtualAccountDivideCount", virtualAccountDivideCount)
                .append("virtualAccountKey01", virtualAccountKey01)
                .append("virtualAccountAmount01", virtualAccountAmount01)
                .append("filler01", filler01)
                .append("virtualAccountKey02", virtualAccountKey02)
                .append("virtualAccountAmount02", virtualAccountAmount02)
                .append("filler02", filler02)
                .append("virtualAccountKey03", virtualAccountKey03)
                .append("virtualAccountAmount03", virtualAccountAmount03)
                .append("filler03", filler03)
                .append("virtualAccountKey04", virtualAccountKey04)
                .append("virtualAccountAmount04", virtualAccountAmount04)
                .append("filler04", filler04)
                .append("virtualAccountKey05", virtualAccountKey05)
                .append("virtualAccountAmount05", virtualAccountAmount05)
                .append("filler05", filler05)
                .append("virtualAccountKey06", virtualAccountKey06)
                .append("virtualAccountAmount06", virtualAccountAmount06)
                .append("filler06", filler06)
                .append("virtualAccountKey07", virtualAccountKey07)
                .append("virtualAccountAmount07", virtualAccountAmount07)
                .append("filler07", filler07)
                .append("virtualAccountKey08", virtualAccountKey08)
                .append("virtualAccountAmount08", virtualAccountAmount08)
                .append("filler08", filler08)
                .append("virtualAccountKey09", virtualAccountKey09)
                .append("virtualAccountAmount09", virtualAccountAmount09)
                .append("filler09", filler09)
                .toString();
    }


}
