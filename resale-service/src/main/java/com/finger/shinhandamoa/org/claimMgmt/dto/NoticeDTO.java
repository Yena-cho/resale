package com.finger.shinhandamoa.org.claimMgmt.dto;

import com.finger.shinhandamoa.data.table.model.NoticeDetails;
import com.finger.shinhandamoa.data.table.model.NoticeMaster;
import com.finger.shinhandamoa.data.table.model.NoticeMasterWithBLOBs;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * 청구
 * 
 * @author wisehouse@finger.co.kr
 */
public class NoticeDTO {
    private NoticeMaster noticeMaster;
    private List<NoticeDetails> itemList;

    public NoticeDTO(NoticeMaster noticeMaster, List<NoticeDetails> itemList) {
        this.noticeMaster = noticeMaster;
        this.itemList = itemList;
    }

    public NoticeMaster getNoticeMaster() {
        return noticeMaster;
    }

    public List<NoticeDetails> getItemList() {
        return itemList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
