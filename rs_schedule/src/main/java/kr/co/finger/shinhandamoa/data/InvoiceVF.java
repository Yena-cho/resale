package kr.co.finger.shinhandamoa.data;

/**
 * 고지서 VF
 *
 * @author wisehouse@finger.co.kr
 */
public class InvoiceVF {
    private String masterId;
    private String seqNo;

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getSeqNo() {
        return seqNo;
    }
}
