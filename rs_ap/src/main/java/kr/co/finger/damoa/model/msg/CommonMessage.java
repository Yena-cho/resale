package kr.co.finger.damoa.model.msg;

public interface CommonMessage extends MsgIF {

    public int getLength();

    public String getSystemId();

    public String getMsgSndRcvCorpCode();

    public String getSndRcvFlag();

    public String getMsgTypeCode();

    public String getDealTypeCode();

    public String getHandlingAgencyCode();

    public String getDealSeqNo();

    public String getMsgSndDate();

    public String getDealStartDate();

    public String getResCode();

    public String getUsrWorkArea1();

    public String getUsrWorkArea2();

    public String getDeadlineYn();

    public String getOccurGubun();

    public String getMediaGubun();

    public String getInstitutionCode();

    public String getTerminalInfo();

    public String getResMsg();

    public String getCommonNetworkNo();


    public void setMsgTypeCode(String msgTypeCode);

    public void setDealTypeCode(String dealTypeCode);

    public default String getType(){
        return getMsgTypeCode() + getDealTypeCode();
    }


    public void setSndRcvFlag(String value);

    /**
     * 송수신flag 처리...
     */
    public default void setupSndRcvFlag() {
        String sndRcvFlag = getSndRcvFlag().trim();
        if ("1".equals(sndRcvFlag)) {
            setSndRcvFlag("2");
        } else if ("2".equals(sndRcvFlag)) {
            setSndRcvFlag("1");
        } else if ("3".equals(sndRcvFlag)) {
            setSndRcvFlag("4");
        } else if ("4".equals(sndRcvFlag)) {
            setSndRcvFlag("3");
        }
    }

}
