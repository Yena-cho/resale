package kr.co.finger.shinhandamoa.service;

/**
 * 현금영수증 파일 전송 내역
 * 
 * @author wisehouse@finger.co.kr
 */
public class CashReceiptFileDTO {
    private String transferDt;
    private String receiveDt;
    private String transferFileName;
    private String receiveFileName;

    public void setTransferDt(String transferDt) {
        this.transferDt = transferDt;
    }

    public String getTransferDt() {
        return transferDt;
    }

    public void setReceiveDt(String receiveDt) {
        this.receiveDt = receiveDt;
    }

    public String getReceiveDt() {
        return receiveDt;
    }

    public void setTransferFileName(String fileName) {
        this.transferFileName = fileName;
    }

    public String getTransferFileName() {
        return transferFileName;
    }

    public void setReceiveFileName(String receiveFileName) {
        this.receiveFileName = receiveFileName;
    }

    public String getReceiveFileName() {
        return receiveFileName;
    }

    public String getId() {
        return transferDt;
    }
}
