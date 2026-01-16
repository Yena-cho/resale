package kr.co.finger.msgio.msg;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class EvidenceData extends BaseMsg{

    private String workType= "AE1112";	//업무구분코드 AE1112

    private String recordType= "22";	//Record구분 22
    private String seqNo= "";	//일련번호
    private String filler1= "";	//filler
    private String corpCode= "";	//기관코드
    private String payerNo= "";	//납부자번호
    private String bankCd= "";	//은행코드
    private String accountNo= "";	//계좌번호
    private String reqData= "";	//신청일 YYYYMMDD
    private String evidenceType= "1";	//증빙자료구분 1(서번)
    private String evidenceExt= "JPG";	//증빙자료확장자
    private String evidenceLength= "0";	//증빙자료길이
    private List<byte[]> evidenceDataList= new ArrayList<>();	//증빙자료데이터
    private String filler2= "";	//filler
    private byte[] evidenceData = new byte[0];      // 증빙자료 데이터의 바이너리 데이터

    private int filler2Size;

    public int getFiller2Size() {
        return filler2Size;
    }

    public void setFiller2Size(int filler2Size) {
        this.filler2Size = filler2Size;
    }

    public List<byte[]> getEvidenceDataList() {
        return evidenceDataList;
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

    public String getFiller1() {
        return filler1;
    }

    public void setFiller1(String filler1) {
        this.filler1 = filler1;
    }

    public String getFiller2() {
        return filler2;
    }

    public void setFiller2(String filler2) {
        this.filler2 = filler2;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getPayerNo() {
        return payerNo;
    }

    public void setPayerNo(String payerNo) {
        this.payerNo = payerNo;
    }

    public String getBankCd() {
        return bankCd;
    }

    public void setBankCd(String bankCd) {
        this.bankCd = bankCd;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getReqData() {
        return reqData;
    }

    public void setReqData(String reqData) {
        this.reqData = reqData;
    }

    public String getEvidenceType() {
        return evidenceType;
    }

    public void setEvidenceType(String evidenceType) {
        this.evidenceType = evidenceType;
    }

    public String getEvidenceExt() {
        return evidenceExt;
    }

    public void setEvidenceExt(String evidenceExt) {
        this.evidenceExt = evidenceExt;
    }

    public String getEvidenceLength() {
        return evidenceLength;
    }

    public void setEvidenceLength(String evidenceLength) {
        this.evidenceLength = evidenceLength;
    }

    public void addEvidenceData(byte[] bytes) {
        evidenceDataList.add(bytes);
    }

    public byte[] getEvidenceData() {
        return evidenceData;
    }

    private void setEvidenceData(byte[] evidenceData) {
        this.evidenceData = evidenceData;
    }

    /**
     *
     * @param bytes
     * @param LOG
     */
    public void setupEvidenceData(byte[] bytes, Logger LOG) {
        log(LOG, "setupEvidenceData ...");
        setEvidenceLength(bytes.length+"");
        setEvidenceData(bytes);
        ByteBuffer byteBuf = ByteBuffer.wrap(bytes);
        int size = bytes.length + 119;
        int divide = size / 1024;
        int mod = size % 1024;

        log(LOG,"images "+bytes.length);
        log(LOG,"divide "+divide);
        log(LOG,"mod "+mod);
        int total = 119 + bytes.length + (1024-mod);
        log(LOG,"total "+ total);
        log(LOG,"block "+ total/1024);

        byte[] _bytes = null;
        if (divide == 0) {
            _bytes= handleFirstLast(byteBuf,bytes.length);
            addEvidenceData(_bytes);
            setFiller2Size((1024-119-bytes.length));
        } else {
            for (int i=0; i <= divide; i++) {
                if (i == 0) {
                    _bytes =handleFirst(byteBuf);
                } else if (i == divide) {
                    _bytes =handleLast(byteBuf,mod);
                    setFiller2Size((1024-mod));
                } else {
                    _bytes=handleMiddle(byteBuf);
                }
                addEvidenceData(_bytes);
            }
        }

    }

    private void log(Logger LOG, String value) {
        if (LOG != null) {
            LOG.debug(value);

        }
    }
    private byte[] handleMiddle(ByteBuffer byteBuf) {
        return handleBuffer(byteBuf,1024);
    }

    private byte[] handleLast(ByteBuffer byteBuf,int mod) {
        return handleBuffer(byteBuf,mod);
    }

    private byte[] handleFirstLast(ByteBuffer byteBuf,int length) {
        return handleBuffer(byteBuf, length);
    }

    private byte[] handleFirst(ByteBuffer byteBuf) {
        return handleBuffer(byteBuf,1024-119);
    }

    private byte[] handleBuffer(ByteBuffer byteBuf,int length) {
        byte[] bytes = new byte[length];
        byteBuf.get(bytes);
        return bytes;
    }

    public int size() {
        int result = 119;
        for (byte[] bytes : evidenceDataList) {
            result += bytes.length;
        }
        result += filler2Size;
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("workType", workType)
                .append("recordType", recordType)
                .append("seqNo", seqNo)
                .append("filler1", filler1)
                .append("corpCode", corpCode)
                .append("payerNo", payerNo)
                .append("bankCd", bankCd)
                .append("accountNo", accountNo)
                .append("reqData", reqData)
                .append("evidenceType", evidenceType)
                .append("evidenceExt", evidenceExt)
                .append("evidenceLength", evidenceLength)
//                .append("evidenceDataList", evidenceDataList)
                .append("filler2", filler2)
//                .append("evidenceData", evidenceData)
                .append("filler2Size", filler2Size)
                .toString();
    }


    public void write(String dir) throws IOException {
        String filePath = dir+"/"+seqNo+"."+evidenceExt;
        System.out.println("filePath="+filePath);
        FileUtils.writeByteArrayToFile(new File(filePath),getEvidenceData());
    }
}
