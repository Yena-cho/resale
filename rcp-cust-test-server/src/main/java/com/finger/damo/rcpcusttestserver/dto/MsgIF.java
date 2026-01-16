package com.finger.damo.rcpcusttestserver.dto;

import java.io.Serializable;

public interface MsgIF extends Serializable,Cloneable{
    /**
     * 전문에서 key에 해당하는 값을 문자열로 반환
     * 비동기처리를 동기식으로 처리할 때 한쌍이 되는 것 확인하기 위한 KEY임.
     * @return key값
     */
    public String getKey();

    /**
     * 수신받은 전문을 반환함.
     * @return
     */
    public String getOriginMessage();

    /**
     * 전문을 구분하는 문자열조합을 반환
     * @return
     */
    public String getType();

    public void setOriginMessage(String msg);

    public void setType(String type);

    public void setLength(int msgSize);

    /**
     * 전문응답시에 변경해야 할 항목 설정함.
     */
    public void response();

    public void setDesc(String desc);

    public void setResCode(String resCode);

    public void setResMsg(String resMsg);

    public String getMsgTypeCode();

    public String getDealTypeCode();


    public String getDealSeqNo();
    public Object clone() throws CloneNotSupportedException;

    public default String[] keyFields() {
        String[] strings = new String[2];
        strings[0] = "MsgSndRcvCorpCode";
        strings[1] = "DealSeqNo";
        return strings;
    }

}
