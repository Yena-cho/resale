package kr.co.finger.msgio.msg;

public class NewTrailer extends BaseMsg{
    private String recordType ="T";	//Record구분
    private String seqNo ="99999999";	//일련번호
    private String corpCode ="";	//기관코드
    private String fileName ="";	//파일이름
    private String totalRecordCount="0";	//총DataRecord수
    private String newReqCount ="0";	//신규등록건수
    private String modiReqCount ="0";	//변경등록건수
    private String moveReqCount ="0";	//해지등록
    private String tempMoveReqCount ="0";	//임의해지등록
    private String filler ="";	//filler
    private String macValue="";	//Mac검증값


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

    public String getTotalRecordCount() {
        return totalRecordCount;
    }

    public void setTotalRecordCount(String totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
    }

    public String getNewReqCount() {
        return newReqCount;
    }

    public void setNewReqCount(String newReqCount) {
        this.newReqCount = newReqCount;
    }

    public String getModiReqCount() {
        return modiReqCount;
    }

    public void setModiReqCount(String modiReqCount) {
        this.modiReqCount = modiReqCount;
    }

    public String getMoveReqCount() {
        return moveReqCount;
    }

    public void setMoveReqCount(String moveReqCount) {
        this.moveReqCount = moveReqCount;
    }

    public String getTempMoveReqCount() {
        return tempMoveReqCount;
    }

    public void setTempMoveReqCount(String tempMoveReqCount) {
        this.tempMoveReqCount = tempMoveReqCount;
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
        final StringBuilder sb = new StringBuilder("NewTrailer{");
        sb.append("recordType='").append(recordType).append('\'');
        sb.append(", seqNo='").append(seqNo).append('\'');
        sb.append(", corpCode='").append(corpCode).append('\'');
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append(", totalRecordCount='").append(totalRecordCount).append('\'');
        sb.append(", newReqCount='").append(newReqCount).append('\'');
        sb.append(", modiReqCount='").append(modiReqCount).append('\'');
        sb.append(", moveReqCount='").append(moveReqCount).append('\'');
        sb.append(", tempMoveReqCount='").append(tempMoveReqCount).append('\'');
        sb.append(", filler='").append(filler).append('\'');
        sb.append(", macValue='").append(macValue).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
