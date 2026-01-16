package kr.co.finger.shinhandamoa.data.domain.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 일 정산 검색 파라미터
 * 
 * @author wisehouse@finger.co.kr
 */
public class DailyClientSettleExample {
    private String clientId;
    private String clientName;
    private String orderByClause;
    private String fromDate;
    private String toDate;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = "%" + clientId + "%";
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = "%" + clientName + "%";
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }
    
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setFromDate(Date fromDate) {
        if(fromDate == null) {
            this.fromDate = null;
            return;
        }

        this.fromDate = new SimpleDateFormat("yyyyMMdd").format(fromDate);
    }

    public String getFromDate() {
        return fromDate;
    }
    
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
    
    public void setToDate(Date toDate) {
        if(toDate == null) {
            this.toDate = null;
            return;
        }

        this.toDate = new SimpleDateFormat("yyyyMMdd").format(toDate);
    }

    public String getToDate() {
        return toDate;
    }
}
