package kr.co.finger.damoa.commons.io;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class VaBean implements Serializable {
    private String result = "OK";
    private String startAccount;

    public VaBean() {
    }

    public VaBean(String result) {
        this.result = result;
    }

    public VaBean(String result, String startAccount) {
        this.result = result;
        this.startAccount = startAccount;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("result", result)
                .append("startAccount", startAccount)
                .toString();
    }
}
