package kr.co.finger.damoa.shinhan.agent.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ResultBean {
    private String code;
    private String msg;

    public ResultBean(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultBean(String code) {
        this.code = code;
    }

    public ResultBean() {
        this.code = "0";
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("code", code)
                .append("msg", msg)
                .toString();
    }
}
