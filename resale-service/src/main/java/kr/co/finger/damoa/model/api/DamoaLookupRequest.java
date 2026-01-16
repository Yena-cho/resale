package kr.co.finger.damoa.model.api;
@Deprecated
/**
 * API 서버 구현할 때 사용하려면 클래스..
 * API 서버는 기존로직을 사용하였으므로 이 클래스는 사용하지 않음.
 */
public class DamoaLookupRequest {
    private String procDate = "";
    private String procSeq = "";
    private String cust_id = "";
    private String dataField = "";
    private String workField = "";

    private String startYN = "";
    private String startCd = "";
    private String startMsg = "";

    public String getProcDate() {
        return procDate;
    }

    public void setProcDate(String procDate) {
        this.procDate = procDate;
    }

    public String getProcSeq() {
        return procSeq;
    }

    public void setProcSeq(String procSeq) {
        this.procSeq = procSeq;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getDataField() {
        return dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

    public String getWorkField() {
        return workField;
    }

    public void setWorkField(String workField) {
        this.workField = workField;
    }

    public String getStartYN() {
        return startYN;
    }

    public void setStartYN(String startYN) {
        this.startYN = startYN;
    }

    public String getStartCd() {
        return startCd;
    }

    public void setStartCd(String startCd) {
        this.startCd = startCd;
    }

    public String getStartMsg() {
        return startMsg;
    }

    public void setStartMsg(String startMsg) {
        this.startMsg = startMsg;
    }
}
