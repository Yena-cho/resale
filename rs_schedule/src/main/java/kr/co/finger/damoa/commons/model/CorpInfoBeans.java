package kr.co.finger.damoa.commons.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CorpInfoBeans implements Serializable {
    private String itemCount;
    private String totalItemCount;

    private List<CorpInfoBean> itemList = new ArrayList<>();

    public List<CorpInfoBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<CorpInfoBean> itemList) {
        this.itemList = itemList;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public String getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(String totalItemCount) {
        this.totalItemCount = totalItemCount;
    }
}
