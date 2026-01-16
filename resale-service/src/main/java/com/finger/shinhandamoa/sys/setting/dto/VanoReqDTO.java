package com.finger.shinhandamoa.sys.setting.dto;

public class VanoReqDTO {
    private String  transVanoHistId;
    private String  chaCd;
    private String  checkYn;
    private String  assignDt;
    private String  remark;
    private Long  vanoCount;

    public String getTransVanoHistId() {
        return transVanoHistId;
    }

    public void setTransVanoHistId(String transVanoHistId) {
        this.transVanoHistId = transVanoHistId;
    }

    public String getChaCd() {
        return chaCd;
    }

    public void setChaCd(String chaCd) {
        this.chaCd = chaCd;
    }

    public String getCheckYn() {
        return checkYn;
    }

    public void setCheckYn(String checkYn) {
        this.checkYn = checkYn;
    }

    public String getAssignDt() {
        return assignDt;
    }

    public void setAssignDt(String assignDt) {
        this.assignDt = assignDt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getVanoCount() {
        return vanoCount;
    }

    public void setVanoCount(Long vanoCount) {
        this.vanoCount = vanoCount;
    }
}
