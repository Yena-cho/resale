package kr.co.finger.msgio.cash;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CashMessage {
    private Head head;
    private List<Data> dataList;
    private Trailer trailer;

    public List<Data> getDataList() {
        return dataList;
    }

    public void addData(Data data) {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        dataList.add(data);
    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
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

    public List<Map<String, Object>> toParamMaps() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Data data : dataList) {
            Map<String, Object> map = new HashMap<>();
            String temp = data.getTemp();
            String[] strings = StringUtils.split(temp, ",");
            if (strings.length == 2) {
                map.put("CASHMASCD", strings[0]);
            } else {
                map.put("CASHMASCD", temp);
            }
            mapList.add(map);
        }

        return mapList;
    }
}
