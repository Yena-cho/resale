package kr.co.finger.damoa.shinhan.agent.model;

public class ValidationResult {
    private boolean isValid = true;
    private String code = "0000";
    private String msg = "정상";

    public ValidationResult() {
    }

    public ValidationResult(String code, String msg) {
        this.isValid = false;
        this.code = code;
        this.msg = msg;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
