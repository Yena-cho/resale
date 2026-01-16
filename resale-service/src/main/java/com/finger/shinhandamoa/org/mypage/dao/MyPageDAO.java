package com.finger.shinhandamoa.org.mypage.dao;

import java.util.HashMap;
import java.util.List;

import com.finger.shinhandamoa.org.mypage.dto.MyPageDTO;

/**
 * @author
 * @date
 * @desc
 */
public interface MyPageDAO {

    public MyPageDTO getSmsConfig(HashMap<String, Object> reqMap) throws Exception;

    public void updateSmsNoti(HashMap<String, Object> reqMap) throws Exception;

    public MyPageDTO selectChaInfo(HashMap<String, Object> reqMap) throws Exception;

    public void updateOrgInfo(HashMap<String, Object> reqMap) throws Exception;

    public void updateCusGubn(HashMap<String, Object> reqMap) throws Exception;

    public void updateRcpChk(HashMap<String, Object> reqMap) throws Exception;

    public void updateCashRcp(HashMap<String, Object> reqMap) throws Exception;

    public List<MyPageDTO> selectBillForm(HashMap<String, Object> map) throws Exception;

    public void insertBillForm(HashMap<String, Object> reqMap) throws Exception;

    public void updateBillForm(HashMap<String, Object> reqMap) throws Exception;

    public MyPageDTO getBillConfig(HashMap<String, Object> reqMap) throws Exception;

    public String selectPwd(HashMap<String, Object> reqMap) throws Exception;

    public void updatePwd(HashMap<String, Object> reqMap) throws Exception;

    public List<MyPageDTO> adjList(HashMap<String, Object> reqMap) throws Exception;

    public void updateUsePgYn(HashMap<String, Object> reqMap) throws Exception;

    public void updateOrgAccInfo(HashMap<String, Object> map) throws Exception;

    void updateFailCnt(HashMap<String, Object> reqMap) throws Exception;

    int selectFailCnt(HashMap<String, Object> reqMap) throws Exception;

    public MyPageDTO getAtConfig(HashMap<String, Object> reqMap) throws Exception;

    public void updateAtNoti(HashMap<String, Object> reqMap) throws Exception;
}
