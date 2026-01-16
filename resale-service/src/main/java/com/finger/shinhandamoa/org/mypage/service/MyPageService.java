package com.finger.shinhandamoa.org.mypage.service;

import java.util.HashMap;
import java.util.List;

import com.finger.shinhandamoa.org.mypage.dto.MyPageDTO;


/**
 * @author
 * @date
 * @desc
 */
public interface MyPageService {
    //sms설정 조회
    public MyPageDTO getSmsConfig(HashMap<String, Object> reqMap) throws Exception;

    //sms설정 수정
    public void updateSmsNoti(HashMap<String, Object> reqMap) throws Exception;

    //기관정보 조회
    public MyPageDTO selectChaInfo(HashMap<String, Object> reqMap) throws Exception;

    //기관정보
    public void updateOrgInfo(HashMap<String, Object> reqMap) throws Exception;

    //고객구분 수정
    public void updateCusGubn(HashMap<String, Object> reqMap) throws Exception;

    //가상계좌수납정보 수정
    public void updateRcpChk(HashMap<String, Object> reqMap) throws Exception;

    //현금영수증발급정보 수정
    public void updateCashRcp(HashMap<String, Object> reqMap) throws Exception;

    //고지서설정 조회
    public List<MyPageDTO> selectBillForm(HashMap<String, Object> map) throws Exception;

    //고지서설정 등록
    public void insertBillForm(HashMap<String, Object> reqMap) throws Exception;

    //고지서설정 수정
    public void updateBillForm(HashMap<String, Object> reqMap) throws Exception;

    //고지서설정 조회(구분별)
    public MyPageDTO getBillConfig(HashMap<String, Object> reqMap) throws Exception;

    //비밀번호 변경
    public void updatePwd(HashMap<String, Object> reqMap) throws Exception;

    //현재비밀번호 조회
    public String selectPwd(HashMap<String, Object> reqMap) throws Exception;

    //다계좌 정보조회
    public List<MyPageDTO> adjList(HashMap<String, Object> reqMap) throws Exception;

    //온라인카드결제 설정 수정
    public void updateUsePgYn(HashMap<String, Object> reqMap) throws Exception;

    public void updateOrgAccInfo(HashMap<String, Object> map) throws Exception;

    void updateFailCnt(HashMap<String, Object> reqMap) throws Exception;

    int selectFailCnt(HashMap<String, Object> reqMap) throws Exception;

    // 알림톡 조회
    public MyPageDTO getAtConfig(HashMap<String, Object> reqMap) throws Exception;

    public void updateAtNoti(HashMap<String, Object> reqMap) throws Exception;
}
