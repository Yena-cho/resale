package kr.co.finger.msgio.msg;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class EB14 extends BaseMsg {
    private NewHeader header;
    private List<NewData> dataList = new ArrayList<>();
    private NewTrailer trailer;

    public NewHeader getHeader() {
        return header;
    }

    public List<NewData> getDataList() {
        return dataList;
    }

    public void setHeader(NewHeader header) {
        this.header = header;
    }

    public NewTrailer getTrailer() {
        return trailer;
    }

    public void setTrailer(NewTrailer trailer) {
        this.trailer = trailer;
    }

    public void addDataList(NewData newData) {
        dataList.add(newData);
    }

    public int dataSize() {
        return dataList.size();
    }

    public NewData get(int i) {
        return dataList.get(i);
    }

    public static NewHeader createHeader() {
        NewHeader header = new NewHeader();
        return header;
    }

    public static NewTrailer createTrailer() {
        return new NewTrailer();
    }

    public static NewData createData() {
        return new NewData();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("header", header)
                .append("dataList", dataList)
                .append("trailer", trailer)
                .toString();
    }
}
