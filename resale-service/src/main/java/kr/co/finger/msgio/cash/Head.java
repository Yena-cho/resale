package kr.co.finger.msgio.cash;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Head {
    private String headType= "10";	//Head구분 10
    private String sendDate= "";	//전송일자 YYYYMMDD
    private String reqCorp= "FINGER";	//요청사 FINGER
    private String processCorp= "KIS";	//처리사 KIS
    private String processCount= "1";	//작업회차 1
    private String filler= "";	//filler

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("headType", headType)
                .append("sendDate", sendDate)
                .append("reqCorp", reqCorp)
                .append("processCorp", processCorp)
                .append("processCount", processCount)
                .append("filler", filler)
                .toString();
    }

    public String getHeadType() {
        return headType;
    }

    public void setHeadType(String headType) {
        this.headType = headType;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getReqCorp() {
        return reqCorp;
    }

    public void setReqCorp(String reqCorp) {
        this.reqCorp = reqCorp;
    }

    public String getProcessCorp() {
        return processCorp;
    }

    public void setProcessCorp(String processCorp) {
        this.processCorp = processCorp;
    }

    public String getProcessCount() {
        return processCount;
    }

    public void setProcessCount(String processCount) {
        this.processCount = processCount;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }
}
