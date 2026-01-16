package kr.co.finger.damoa.commons.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class CorpInfoBean implements Serializable {

    private String id;
    private String orgCode;
    private String infoCode;
    private String date;
    private String fileName;
    private String fileSize;
    private String createDate;
    private String createUser;

    public CorpInfoBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getInfoCode() {
        return infoCode;
    }

    public void setInfoCode(String infoCode) {
        this.infoCode = infoCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("orgCode", orgCode)
                .append("infoCode", infoCode)
                .append("date", date)
                .append("fileName", fileName)
                .append("fileSize", fileSize)
                .append("createDate", createDate)
                .append("createUser", createUser)
                .toString();
    }
}
