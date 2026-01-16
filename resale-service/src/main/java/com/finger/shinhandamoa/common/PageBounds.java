package com.finger.shinhandamoa.common;

import org.apache.ibatis.session.RowBounds;

/**
 * 페이지
 * 
 * @author wisehouse@finger.co.kr
 */
public class PageBounds {
    private int pageNo;
    private int pageSize;

    public PageBounds(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public PageBounds() {
        this(1, 10);
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    public RowBounds toRowBounds() {
        final int offset = pageNo * pageSize - pageSize;
        final int limit = pageSize;
        return new RowBounds(offset, limit);
    }
}
