package com.finger.shinhandamoa.org.mypage.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.org.mypage.dao.MyPageDAO;
import com.finger.shinhandamoa.org.mypage.dto.MyPageDTO;

/**
 * @author
 * @date
 * @desc
 */
@Service
public class MyPageServiceImpl implements MyPageService {

    @Autowired
    private MyPageDAO myPageDAO;

    @Override
    public MyPageDTO getSmsConfig(HashMap<String, Object> reqMap) throws Exception {

        return myPageDAO.getSmsConfig(reqMap);
    }

    @Override
    public void updateSmsNoti(HashMap<String, Object> reqMap) throws Exception {
        myPageDAO.updateSmsNoti(reqMap);
    }

    @Override
    public MyPageDTO selectChaInfo(HashMap<String, Object> reqMap) throws Exception {
        return myPageDAO.selectChaInfo(reqMap);
    }

    @Override
    public void updateOrgInfo(HashMap<String, Object> reqMap) throws Exception {
        myPageDAO.updateOrgInfo(reqMap);
    }

    @Override
    public void updateCusGubn(HashMap<String, Object> reqMap) throws Exception {
        myPageDAO.updateCusGubn(reqMap);
    }

    @Override
    public void updateRcpChk(HashMap<String, Object> reqMap) throws Exception {
        myPageDAO.updateRcpChk(reqMap);
    }

    @Override
    public void updateCashRcp(HashMap<String, Object> reqMap) throws Exception {
        myPageDAO.updateCashRcp(reqMap);
    }

    @Override
    public List<MyPageDTO> selectBillForm(HashMap<String, Object> map) throws Exception {
        return myPageDAO.selectBillForm(map);
    }

    @Override
    public void insertBillForm(HashMap<String, Object> reqMap) throws Exception {
        myPageDAO.insertBillForm(reqMap);
    }

    @Override
    public void updateBillForm(HashMap<String, Object> reqMap) throws Exception {
        myPageDAO.updateBillForm(reqMap);
    }

    @Override
    public MyPageDTO getBillConfig(HashMap<String, Object> reqMap) throws Exception {
        return myPageDAO.getBillConfig(reqMap);
    }

    @Override
    public void updatePwd(HashMap<String, Object> reqMap) throws Exception {
        myPageDAO.updatePwd(reqMap);
    }

    @Override
    public String selectPwd(HashMap<String, Object> reqMap) throws Exception {
        return myPageDAO.selectPwd(reqMap);
    }

    @Override
    public List<MyPageDTO> adjList(HashMap<String, Object> reqMap) throws Exception {
        return myPageDAO.adjList(reqMap);
    }

    @Override
    public void updateUsePgYn(HashMap<String, Object> reqMap) throws Exception {
        myPageDAO.updateUsePgYn(reqMap);
    }

    @Override
    public void updateOrgAccInfo(HashMap<String, Object> map) throws Exception {
        myPageDAO.updateOrgAccInfo(map);

    }

    @Override
    public void updateFailCnt(HashMap<String, Object> reqMap) throws Exception {
        myPageDAO.updateFailCnt(reqMap);
    }

    @Override
    public int selectFailCnt(HashMap<String, Object> reqMap) throws Exception {
        return myPageDAO.selectFailCnt(reqMap);
    }

    @Override
    public MyPageDTO getAtConfig(HashMap<String, Object> reqMap) throws Exception {
        return myPageDAO.getAtConfig(reqMap);
    }

    @Override
    public void updateAtNoti(HashMap<String, Object> reqMap) throws Exception {
        myPageDAO.updateAtNoti(reqMap);
    }

}
