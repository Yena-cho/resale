package kr.co.finger.damoa.model.msg;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * 집계대사 개별부
 */
public class AggregateMessage implements CommonMessage {

    private String originMessage;
    private String type = "";
    private int length;

    private String systemId= "VAS";	//시스템ID
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

    private String dealDate= "";	//거래일자
    private String corpCode= "";	//기관코드
    private String depositTotalNo= "0";	//입금총건수
    private String depositTotalAmount= "0";	//입금총건수
    private String depositCancelNo= "0";	//입금취소건수
    private String depostCancelAmount= "0";	//입금취소금액
    private String withdrawalTotalNo= "0";	//출금총건수
    private String withdrawalTotalAmount= "0";	//출금총금액
    private String withdrawalCancelNo= "0";	//출금취소건수
    private String withdrawalCancelAmount= "0";	//출금취소 금액
    private String feesNo= "0";	//수수료건수
    private String feesAmount= "0";	//수수료금액
    private String withdrawalInstructionNo= "0";	//출금지시건수
    private String withdrawalInstructionAmount= "0";	//출금지시금액
    private String withdrawalInstructionCancelNo= "0";	//출금지시 취소건수
    private String withdrawalInstructionCancelAmount= "0";	//출급지시 취소금액
    private String withdrawalInstructionFeeNo= "0";	//출금지시 수수료건수
    private String withdrawalInstructionFeeAmount= "0";	//출금지시 수수료금액
    private String filter= "";	//FILLER

    private String desc;

    public String getDesc() {
        return desc;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }


    public int getLength() {
        return length;
    }

    @Override
    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public void response() {
        setMsgTypeCode(MessageCode.AGGREGATE_TYPE_RESPONSE.getId());
        setupSndRcvFlag();
//        setSndRcvFlag("1");
        setResCode("0000");
        setFeesAmount("0");
        setFeesNo("0");
        setDepositCancelNo("0");
        setDepositTotalNo("0");
        setDepostCancelAmount("0");
        setDepositTotalAmount("0");


        setWithdrawalInstructionNo("0");
        setWithdrawalInstructionAmount("0");
        setWithdrawalInstructionCancelNo("0");
        setWithdrawalInstructionCancelAmount("0");
        setWithdrawalInstructionFeeAmount("0");
        setWithdrawalInstructionFeeNo("0");


    }

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


    public String getDealDate() {
        return dealDate;
    }

    public void setDealDate(String dealDate) {
        this.dealDate = dealDate;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String  getDepositTotalNo() {
        return depositTotalNo;
    }

    public void setDepositTotalNo(String depositTotalNo) {
        this.depositTotalNo = depositTotalNo;
    }

    @Override
    public String getType() {
        return type;
    }

    public String getDepositTotalAmount() {
        return depositTotalAmount;
    }

    public void setDepositTotalAmount(String depositTotalAmount) {
        this.depositTotalAmount = depositTotalAmount;
    }

    public String getDepositCancelNo() {
        return depositCancelNo;
    }

    public void setDepositCancelNo(String depositCancelNo) {
        this.depositCancelNo = depositCancelNo;
    }

    public String getDepostCancelAmount() {
        return depostCancelAmount;
    }

    public void setDepostCancelAmount(String depostCancelAmount) {
        this.depostCancelAmount = depostCancelAmount;
    }

    public String getWithdrawalTotalNo() {
        return withdrawalTotalNo;
    }

    public void setWithdrawalTotalNo(String withdrawalTotalNo) {
        this.withdrawalTotalNo = withdrawalTotalNo;
    }

    public String getWithdrawalTotalAmount() {
        return withdrawalTotalAmount;
    }

    public void setWithdrawalTotalAmount(String withdrawalTotalAmount) {
        this.withdrawalTotalAmount = withdrawalTotalAmount;
    }

    public String getWithdrawalCancelNo() {
        return withdrawalCancelNo;
    }

    public void setWithdrawalCancelNo(String withdrawalCancelNo) {
        this.withdrawalCancelNo = withdrawalCancelNo;
    }

    public String getWithdrawalCancelAmount() {
        return withdrawalCancelAmount;
    }

    public void setWithdrawalCancelAmount(String withdrawalCancelAmount) {
        this.withdrawalCancelAmount = withdrawalCancelAmount;
    }

    public String getFeesNo() {
        return feesNo;
    }

    public void setFeesNo(String feesNo) {
        this.feesNo = feesNo;
    }

    public String getFeesAmount() {
        return feesAmount;
    }

    public void setFeesAmount(String feesAmount) {
        this.feesAmount = feesAmount;
    }

    public String getWithdrawalInstructionNo() {
        return withdrawalInstructionNo;
    }

    public void setWithdrawalInstructionNo(String withdrawalInstructionNo) {
        this.withdrawalInstructionNo = withdrawalInstructionNo;
    }

    public String getWithdrawalInstructionAmount() {
        return withdrawalInstructionAmount;
    }

    public void setWithdrawalInstructionAmount(String withdrawalInstructionAmount) {
        this.withdrawalInstructionAmount = withdrawalInstructionAmount;
    }

    public String getWithdrawalInstructionCancelNo() {
        return withdrawalInstructionCancelNo;
    }

    public void setWithdrawalInstructionCancelNo(String withdrawalInstructionCancelNo) {
        this.withdrawalInstructionCancelNo = withdrawalInstructionCancelNo;
    }

    public String getWithdrawalInstructionCancelAmount() {
        return withdrawalInstructionCancelAmount;
    }

    public void setWithdrawalInstructionCancelAmount(String withdrawalInstructionCancelAmount) {
        this.withdrawalInstructionCancelAmount = withdrawalInstructionCancelAmount;
    }

    public String getWithdrawalInstructionFeeNo() {
        return withdrawalInstructionFeeNo;
    }

    public void setWithdrawalInstructionFeeNo(String withdrawalInstructionFeeNo) {
        this.withdrawalInstructionFeeNo = withdrawalInstructionFeeNo;
    }

    public String getWithdrawalInstructionFeeAmount() {
        return withdrawalInstructionFeeAmount;
    }

    public void setWithdrawalInstructionFeeAmount(String withdrawalInstructionFeeAmount) {
        this.withdrawalInstructionFeeAmount = withdrawalInstructionFeeAmount;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getKey() {
        return corpCode+"_"+dealDate;
    }
    public String[] keyFields() {
        String[] strings = new String[2];
        strings[0] = "MsgSndRcvCorpCode";
        strings[1] = "DealDate";
        return strings;
    }

    public String getOriginMessage() {
        return originMessage;
    }

    @Override
    public void setOriginMessage(String msg) {
        this.originMessage = msg;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("originMessage", originMessage)
                .append("type", type)
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
                .append("dealDate", dealDate)
                .append("corpCode", corpCode)
                .append("depositTotalNo", depositTotalNo)
                .append("depositTotalAmount", depositTotalAmount)
                .append("depositCancelNo", depositCancelNo)
                .append("depostCancelAmount", depostCancelAmount)
                .append("withdrawalTotalNo", withdrawalTotalNo)
                .append("withdrawalTotalAmount", withdrawalTotalAmount)
                .append("withdrawalCancelNo", withdrawalCancelNo)
                .append("withdrawalCancelAmount", withdrawalCancelAmount)
                .append("feesNo", feesNo)
                .append("feesAmount", feesAmount)
                .append("withdrawalInstructionNo", withdrawalInstructionNo)
                .append("withdrawalInstructionAmount", withdrawalInstructionAmount)
                .append("withdrawalInstructionCancelNo", withdrawalInstructionCancelNo)
                .append("withdrawalInstructionCancelAmount", withdrawalInstructionCancelAmount)
                .append("withdrawalInstructionFeeNo", withdrawalInstructionFeeNo)
                .append("withdrawalInstructionFeeAmount", withdrawalInstructionFeeAmount)
                .append("filter", filter)
                .append("desc", desc)
                .toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    public void setupAggregate(String totalCount,String totalAmount,String cancelCount,String cancelAmout,String feeCount,String feeAmount) {
        setDepositTotalNo(totalCount+"");
        setDepositTotalAmount(totalAmount+"");
        setDepositCancelNo(cancelCount+"");
        setDepostCancelAmount(cancelAmout+"");
        setFeesNo(feeCount+"");
        setFeesAmount(feeAmount+"");
    }

    /**
     * .append("dealDate", dealDate)
     *                 .append("corpCode", corpCode)
     *                 .append("depositTotalNo", depositTotalNo)
     *                 .append("depositTotalAmount", depositTotalAmount)
     *                 .append("depositCancelNo", depositCancelNo)
     *                 .append("depostCancelAmount", depostCancelAmount)
     *                 .append("withdrawalTotalNo", withdrawalTotalNo)
     *                 .append("withdrawalTotalAmount", withdrawalTotalAmount)
     *                 .append("withdrawalCancelNo", withdrawalCancelNo)
     *                 .append("withdrawalCancelAmount", withdrawalCancelAmount)
     *                 .append("feesNo", feesNo)
     *                 .append("feesAmount", feesAmount)
     * @return
     */

    public Map<String,Object> insertParam() throws ParseException {
        Map<String,Object> map = Maps.hashmap();
        map.put("CHACD", corpCode);
        map.put("TRADEDT", toDbDate(dealDate));

        map.put("RECVCNT", Long.parseLong(depositTotalNo));
        map.put("RECVAMT", Long.parseLong(depositTotalAmount));
        map.put("PAYCNT", 0);
        map.put("PAYAMT", 0);
        map.put("RECVCANCELCNT", Long.parseLong(depositCancelNo));
        map.put("RECVCANCELAMT", Long.parseLong(depostCancelAmount));
        map.put("ICHECNT", 0);
        map.put("ICHEAMT", 0);
        map.put("ICHECANCELCNT", 0);
        map.put("ICHECANCELAMT", 0);
        map.put("ICHEFEECNT", 0);
        map.put("ICHEFEEAMT", 0);
        map.put("FEECNT", Long.parseLong(feesNo));
        map.put("FEEAMT", Long.parseLong(feesAmount));

        map.put("FITXCD", DateUtils.toNowString());
        map.put("REGDT", DateUtils.toDTType());

        return map;
    }

    public Map<String,Object> selectParam() throws ParseException {
        Map<String,Object> map = Maps.hashmap();
        map.put("CHACD", corpCode);
        map.put("TRADEDT", toDbDate(dealDate));
        return map;
    }
    private String toDbDate(String date8) throws ParseException {
        Date date = DateUtils.toDate(date8, "yyyyMMdd");
        return DateUtils.toDTType(date);
    }
}
