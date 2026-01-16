package kr.co.finger.msgio.msg;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class EvidenceTrailer extends BaseMsg {
    private String workType= "AE1112";	//업무구분코드 AE1112

    private String recordType= "33";	//Record구분 33
    private String seqNo= "9999999";	//일련번호 9999999
    private String corpCode= "";	//기관코드
    private String totalDataRecordCount= "0";	//총 Data Record 수
    private String totalDataBlockCount= "0";	//총 Data Block 수
    private String filler= "";	//filler


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("workType", workType)
                .append("recordType", recordType)
                .append("seqNo", seqNo)
                .append("corpCode", corpCode)
                .append("totalDataRecordCount", totalDataRecordCount)
                .append("totalDataBlockCount", totalDataBlockCount)
                .append("filler", filler)
                .toString();
    }

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

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getTotalDataRecordCount() {
        return totalDataRecordCount;
    }

    public void setTotalDataRecordCount(String totalDataRecordCount) {
        this.totalDataRecordCount = totalDataRecordCount;
    }

    public String getTotalDataBlockCount() {
        return totalDataBlockCount;
    }

    public void setTotalDataBlockCount(String totalDataBlockCount) {
        this.totalDataBlockCount = totalDataBlockCount;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }
}
