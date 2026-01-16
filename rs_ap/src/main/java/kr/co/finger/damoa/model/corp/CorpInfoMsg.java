package kr.co.finger.damoa.model.corp;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;
@Deprecated
public class CorpInfoMsg {
    private Head head;
    private List<Data> dataList;
    private Trailer trailer;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public void add(Data data) {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        dataList.add(data);
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("head", head)
                .append("dataList", dataList)
                .append("trailer", trailer)
                .toString();
    }
}
