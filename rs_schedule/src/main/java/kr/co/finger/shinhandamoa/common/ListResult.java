package kr.co.finger.shinhandamoa.common;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Collections;
import java.util.List;

/**
 * 목록 결과
 *
 * @param <T>
 */
public class ListResult<T> {
    private long totalItemCount;
    private int itemCount;
    private List<T> itemList;

    public ListResult(long totalItemCount, List<T> itemList) {
        this.totalItemCount = totalItemCount;
        
        if(itemList == null) {
            this.itemList  = Collections.emptyList();
        } else {
            this.itemList = Collections.unmodifiableList(itemList);
        }
        
        this.itemCount = this.itemList.size();
    }

    public long getTotalItemCount() {
        return totalItemCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public List<T> getItemList() {
        return itemList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
