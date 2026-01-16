package kr.co.finger.damoa.model.msg;

public class PollingMessage implements MsgIF {

    private String originMessage;
    private String type = "";
    private int length;
    private String desc;

    private String pollingType;
    private String dateTime;

    public String getPollingType() {
        return pollingType;
    }

    public void setPollingType(String pollingType) {
        this.pollingType = pollingType;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
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
        setPollingType("HDRRESPOLL");
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public void setResCode(String resCode) {

    }

    @Override
    public void setResMsg(String resMsg) {

    }

    @Override
    public String getMsgTypeCode() {
        return "";
    }

    @Override
    public String getDealTypeCode() {
        return "";
    }

    @Override
    public String getDealSeqNo() {
        return "";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    public static PollingMessage newMessage(String pollingType,String dateTime) {
        PollingMessage pollingMessage = new PollingMessage();
        pollingMessage.setPollingType(pollingType);
        pollingMessage.setDesc("통신망폴링요청");
        pollingMessage.setType(pollingType);
        pollingMessage.setDateTime(dateTime);
        pollingMessage.setLength(20);

        return pollingMessage;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PollingMessage{");
        sb.append(", type='").append(type).append('\'');
        sb.append(", length=").append(length);
        sb.append(", desc='").append(desc).append('\'');
        sb.append(", pollingType='").append(pollingType).append('\'');
        sb.append(", dateTime='").append(dateTime).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
