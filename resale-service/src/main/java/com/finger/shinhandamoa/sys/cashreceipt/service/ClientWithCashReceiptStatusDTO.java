package com.finger.shinhandamoa.sys.cashreceipt.service;

/**
 * 기관 정보 DTO (현금영수증 발급 내역 포함)
 * 
 * @author wisehouse@finger.co.kr
 */
public class ClientWithCashReceiptStatusDTO {
    private String id;
    private String name;
    private String enableCashReceipt;
    private String enableAutomaticCashReceipt;
    private String mandRcpYn;
    private String statusCode;
    private String statusName;
    
    private int vasNoCashReceiptCount;
    private int vasReadyCashReceiptCount;
    private int vasRequestCashReceiptCount;
    private int vasIssueCashReceiptCount;
    
    private int dcsNoCashReceiptCount;
    private int dcsReadyCashReceiptCount;
    private int dcsRequestCashReceiptCount;
    private int dcsIssueCashReceiptCount;
    
    private int dvaNoCashReceiptCount;
    private int dvaReadyCashReceiptCount;
    private int dvaRequestCashReceiptCount;
    private int dvaIssueCashReceiptCount;

    public String getMandRcpYn() {
        return mandRcpYn;
    }

    public void setMandRcpYn(String mandRcpYn) {
        this.mandRcpYn = mandRcpYn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnableCashReceipt() {
        return enableCashReceipt;
    }

    public void setEnableCashReceipt(String enableCashReceipt) {
        this.enableCashReceipt = enableCashReceipt;
    }

    public String getEnableAutomaticCashReceipt() {
        return enableAutomaticCashReceipt;
    }

    public void setEnableAutomaticCashReceipt(String enableAutomaticCashReceipt) {
        this.enableAutomaticCashReceipt = enableAutomaticCashReceipt;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public int getVasNoCashReceiptCount() {
        return vasNoCashReceiptCount;
    }

    public void setVasNoCashReceiptCount(int vasNoCashReceiptCount) {
        this.vasNoCashReceiptCount = vasNoCashReceiptCount;
    }

    public int getVasReadyCashReceiptCount() {
        return vasReadyCashReceiptCount;
    }

    public void setVasReadyCashReceiptCount(int vasReadyCashReceiptCount) {
        this.vasReadyCashReceiptCount = vasReadyCashReceiptCount;
    }

    public int getVasRequestCashReceiptCount() {
        return vasRequestCashReceiptCount;
    }

    public void setVasRequestCashReceiptCount(int vasRequestCashReceiptCount) {
        this.vasRequestCashReceiptCount = vasRequestCashReceiptCount;
    }

    public int getVasIssueCashReceiptCount() {
        return vasIssueCashReceiptCount;
    }

    public void setVasIssueCashReceiptCount(int vasIssueCashReceiptCount) {
        this.vasIssueCashReceiptCount = vasIssueCashReceiptCount;
    }

    public int getDcsNoCashReceiptCount() {
        return dcsNoCashReceiptCount;
    }

    public void setDcsNoCashReceiptCount(int dcsNoCashReceiptCount) {
        this.dcsNoCashReceiptCount = dcsNoCashReceiptCount;
    }

    public int getDcsReadyCashReceiptCount() {
        return dcsReadyCashReceiptCount;
    }

    public void setDcsReadyCashReceiptCount(int dcsReadyCashReceiptCount) {
        this.dcsReadyCashReceiptCount = dcsReadyCashReceiptCount;
    }

    public int getDcsRequestCashReceiptCount() {
        return dcsRequestCashReceiptCount;
    }

    public void setDcsRequestCashReceiptCount(int dcsRequestCashReceiptCount) {
        this.dcsRequestCashReceiptCount = dcsRequestCashReceiptCount;
    }

    public int getDcsIssueCashReceiptCount() {
        return dcsIssueCashReceiptCount;
    }

    public void setDcsIssueCashReceiptCount(int dcsIssueCashReceiptCount) {
        this.dcsIssueCashReceiptCount = dcsIssueCashReceiptCount;
    }

    public int getDvaNoCashReceiptCount() {
        return dvaNoCashReceiptCount;
    }

    public void setDvaNoCashReceiptCount(int dvaNoCashReceiptCount) {
        this.dvaNoCashReceiptCount = dvaNoCashReceiptCount;
    }

    public int getDvaReadyCashReceiptCount() {
        return dvaReadyCashReceiptCount;
    }

    public void setDvaReadyCashReceiptCount(int dvaReadyCashReceiptCount) {
        this.dvaReadyCashReceiptCount = dvaReadyCashReceiptCount;
    }

    public int getDvaRequestCashReceiptCount() {
        return dvaRequestCashReceiptCount;
    }

    public void setDvaRequestCashReceiptCount(int dvaRequestCashReceiptCount) {
        this.dvaRequestCashReceiptCount = dvaRequestCashReceiptCount;
    }

    public int getDvaIssueCashReceiptCount() {
        return dvaIssueCashReceiptCount;
    }

    public void setDvaIssueCashReceiptCount(int dvaIssueCashReceiptCount) {
        this.dvaIssueCashReceiptCount = dvaIssueCashReceiptCount;
    }
}
