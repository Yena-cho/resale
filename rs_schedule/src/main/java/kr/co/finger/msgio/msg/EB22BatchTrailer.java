package kr.co.finger.msgio.msg;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class EB22BatchTrailer extends BaseMsg{
    private String recordType ="T";	//Record구분
    private String seqNo ="99999999";	//일련번호
    private String corpCode= "";	//기관코드
    private String fileName= "";	//파일이름
    private String tatalRecordCount = "0";   //총데이터레코드건수
    private String allCount = "0";	//출금의뢰한레코드건수
    private String allAmount = "0";	//출금의뢰금액합계
    private String partialCount= "00000000";	//00000000
    private String partialAmount= "0000000000000";	//0000000000000
    private String filler = "";	//filler
    private String macValue= "";	//Mac검증값

    public String getTatalRecordCount() {
        return tatalRecordCount;
    }

    public void setTatalRecordCount(String tatalRecordCount) {
        this.tatalRecordCount = tatalRecordCount;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAllCount() {
        return allCount;
    }

    public void setAllCount(String allCount) {
        this.allCount = allCount;
    }

    public String getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(String allAmount) {
        this.allAmount = allAmount;
    }

    public String getPartialCount() {
        return partialCount;
    }

    public void setPartialCount(String partialCount) {
        this.partialCount = partialCount;
    }

    public String getPartialAmount() {
        return partialAmount;
    }

    public void setPartialAmount(String partialAmount) {
        this.partialAmount = partialAmount;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }

    public String getMacValue() {
        return macValue;
    }

    public void setMacValue(String macValue) {
        this.macValue = macValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("recordType", recordType)
                .append("seqNo", seqNo)
                .append("corpCode", corpCode)
                .append("fileName", fileName)
                .append("tatalRecordCount", tatalRecordCount)
                .append("allCount", allCount)
                .append("allAmount", allAmount)
                .append("partialCount", partialCount)
                .append("partialAmount", partialAmount)
                .append("filler", filler)
                .append("macValue", macValue)
                .toString();
    }
}
