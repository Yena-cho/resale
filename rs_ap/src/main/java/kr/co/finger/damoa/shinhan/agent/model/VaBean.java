package kr.co.finger.damoa.shinhan.agent.model;

import java.io.Serializable;

public class VaBean implements Serializable {
    private String result;
    private String startAccount;
    private String createNo;

    public VaBean() {
    }

    public VaBean(String result) {
        this.result = result;
    }

    public VaBean(String result, String startAccount) {
        this.result = result;
        this.startAccount = startAccount;
    }

    public VaBean(String result, String startAccount, String createNo) {
        this.result = result;
        this.startAccount = startAccount;
        this.createNo = createNo;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStartAccount() {
        return startAccount;
    }

    public void setStartAccount(String startAccount) {
        this.startAccount = startAccount;
    }

    public String getCreateNo() {
        return createNo;
    }

    public void setCreateNo(String createNo) {
        this.createNo = createNo;
    }


}
