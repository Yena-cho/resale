package kr.co.finger.msgio.msg;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BatchHeader extends BaseMsg {
    private String recordType = "H";	//Record구분
    private String seqNo = "00000000";	//일련번호
    private String corpCode= "";	//기관코드
    private String fileName= "";	//파일이름
    private String withdrawalDate= "";	//출금일자
    private String mainBankCd= "";	//주거래은행코드
    private String depositAccountNo = "";	//입금계좌번호
    private String filler = "";	//filler

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


    public String getWithdrawalDate() {
        return withdrawalDate;
    }

    public void setWithdrawalDate(String withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    public String getMainBankCd() {
        return mainBankCd;
    }

    public void setMainBankCd(String mainBankCd) {
        this.mainBankCd = mainBankCd;
    }

    public String getDepositAccountNo() {
        return depositAccountNo;
    }

    public void setDepositAccountNo(String depositAccountNo) {
        this.depositAccountNo = depositAccountNo;
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
                .append("recordType", recordType)
                .append("seqNo", seqNo)
                .append("corpCode", corpCode)
                .append("fileName", fileName)
                .append("withdrawalDate", withdrawalDate)
                .append("mainBankCd", mainBankCd)
                .append("depositAccountNo", depositAccountNo)
                .append("filler", filler)
                .toString();
    }
}
