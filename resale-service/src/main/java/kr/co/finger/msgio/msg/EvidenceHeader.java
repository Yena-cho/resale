package kr.co.finger.msgio.msg;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class EvidenceHeader extends BaseMsg {
    private String workType= "AE1112";	//업무구분코드 AE1112

    private String recordType= "11";	//Record구분 11
    private String seqNo= "0000000";	//일련번호 0000000
    private String reqData= "";	//신청일 YYYYMMDD
    private String corpCode= "";	//기관코드
    private String totalEvidenceCount= "";	//총증빙자료개수
    private String filler= "";	//filler

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getReqData() {
        return reqData;
    }

    public void setReqData(String reqData) {
        this.reqData = reqData;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getTotalEvidenceCount() {
        return totalEvidenceCount;
    }

    public void setTotalEvidenceCount(String totalEvidenceCount) {
        this.totalEvidenceCount = totalEvidenceCount;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("workType", workType)
                .append("recordType", recordType)
                .append("seqNo", seqNo)
                .append("reqData", reqData)
                .append("corpCode", corpCode)
                .append("totalEvidenceCount", totalEvidenceCount)
                .append("filler", filler)
                .toString();
    }
}
