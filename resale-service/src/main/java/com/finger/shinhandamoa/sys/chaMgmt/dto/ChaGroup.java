package com.finger.shinhandamoa.sys.chaMgmt.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 기관 그룹
 * 
 * @author wisehouse@finger.co.kr
 */
public class ChaGroup {
    private String id;
    private String name;
    private String remark;
    private String status;
    private String transactionType;
    private String password;
    private List<ChaVO> chaVOList = new ArrayList<>();

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ChaVO> getChaVOList() {
        return Collections.unmodifiableList(chaVOList);
    }

    public void setChaVOList(List<ChaVO> chaVOList) {
        this.chaVOList = Collections.unmodifiableList(chaVOList);
    }
}
