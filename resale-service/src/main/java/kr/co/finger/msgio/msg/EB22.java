package kr.co.finger.msgio.msg;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class EB22 extends BaseMsg {
    private BatchHeader header;
    private List<BatchData> dataList = new ArrayList<>();
    private EB22BatchTrailer trailer;

    public BatchHeader getHeader() {
        return header;
    }

    public List<BatchData> getDataList() {
        return dataList;
    }

    public void setHeader(BatchHeader header) {
        this.header = header;
    }

    public EB22BatchTrailer getTrailer() {
        return trailer;
    }

    public void setTrailer(EB22BatchTrailer trailer) {
        this.trailer = trailer;
    }

    public void addDataList(BatchData newData) {
        dataList.add(newData);
    }

    public int dataSize() {
        return dataList.size();
    }

    public BatchData get(int i) {
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
