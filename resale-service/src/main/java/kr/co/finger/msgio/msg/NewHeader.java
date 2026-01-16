package kr.co.finger.msgio.msg;

public class NewHeader extends BaseMsg {
    private String recordType = "H";	//Record구분
    private String seqNo = "00000000";	//일련번호
    private String corpCode ="";	//기관코드
    private String fileName = "";	//파일이름
    private String reqData ="";	//신청접수일자
    private String filler ="";	//filler

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

    public String getReqData() {
        return reqData;
    }

    public void setReqData(String reqData) {
        this.reqData = reqData;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NewHeader{");
        sb.append("recordType='").append(recordType).append('\'');
        sb.append(", seqNo='").append(seqNo).append('\'');
        sb.append(", corpCode='").append(corpCode).append('\'');
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append(", reqData='").append(reqData).append('\'');
        sb.append(", filler='").append(filler).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
