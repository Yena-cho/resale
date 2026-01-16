package kr.co.finger.damoa.model.msg;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 집계대사 개별부
 */
public class AdminMessage implements CommonMessage {

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

    private String dealDate;
    private String adminInfo;
    private String holidayCode;
    private String safCount;

    private String filter;

    private String desc;

    public String getDesc() {
        return desc;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String getKey() {
        return "";
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
        setMsgTypeCode(MessageCode.ADMIN_TYPE_RESPONSE.getId());
        setResCode("0000");
        setupSndRcvFlag();
    }

    public String getDealDate() {
        return dealDate;
    }

    public void setDealDate(String dealDate) {
        this.dealDate = dealDate;
    }

    public String getAdminInfo() {
        return adminInfo;
    }

    public void setAdminInfo(String adminInfo) {
        this.adminInfo = adminInfo;
    }

    public String getHolidayCode() {
        return holidayCode;
    }

    public void setHolidayCode(String holidayCode) {
        this.holidayCode = holidayCode;
    }

    public String getSafCount() {
        return safCount;
    }

    public void setSafCount(String safCount) {
        safCount = safCount;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
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

    @Override
    public void setMsgTypeCode(String msgTypeCode) {
        this.msgTypeCode = msgTypeCode;
    }

    @Override
    public String getDealTypeCode() {
        return dealTypeCode;
    }

    @Override
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
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
                .append("adminInfo", adminInfo)
                .append("holidayCode", holidayCode)
                .append("safCount", safCount)
                .append("filter", filter)
                .toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
