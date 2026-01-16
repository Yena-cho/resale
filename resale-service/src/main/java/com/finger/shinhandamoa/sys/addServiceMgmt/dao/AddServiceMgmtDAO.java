package com.finger.shinhandamoa.sys.addServiceMgmt.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.sys.addServiceMgmt.dto.AddServiceMgmtDTO;
import com.finger.shinhandamoa.sys.addServiceMgmt.dto.XNotimasreqDTO;

/**
 * @author
 * @date
 * @desc
 */
public interface AddServiceMgmtDAO {

    public List<XNotimasreqDTO> notiPrintListAll(Map<String, Object> map) throws Exception;

    public int notiPrintCount(Map<String, Object> map) throws Exception;

    public int notiReqPrintCount(Map<String, Object> map) throws Exception;

    public int failNotiPrintCount(Map<String, Object> map) throws Exception;

    public void notiPrintUpdate(Map<String, Object> map) throws Exception;

    public void quickPrint(Map<String, Object> map) throws Exception;

    public void rePrint(Map<String, Object> map) throws Exception;

    //문자서비스 신청내역 대기건수
    public HashMap<String, Object> smsRegListWaitCount(HashMap<String, Object> reqMap) throws Exception;

    //문자서비스 신청내역 갯수
    public HashMap<String, Object> smsRegListCount(HashMap<String, Object> reqMap) throws Exception;

    //문자서비스 신청내역 조회
    public List<AddServiceMgmtDTO> smsRegList(HashMap<String, Object> reqMap) throws Exception;

    //제출서류 발신번호 조회
    public AddServiceMgmtDTO getCallerNum(HashMap<String, Object> reqMap) throws Exception;

    //발신번호 수정
    public void updateContents(HashMap<String, Object> reqMap) throws Exception;

    public void updateSmsSendTel(HashMap<String, Object> reqMap) throws Exception;

    //온라인카드 내역 건수
    public HashMap<String, Object> cardPayHistoryCount(HashMap<String, Object> reqMap) throws Exception;

    //온라인카드 내역 조회
    public List<AddServiceMgmtDTO> cardPayHistoryList(HashMap<String, Object> reqMap) throws Exception;

    //sms 사용여부 수정
    public void updateUseSmsYn(HashMap<String, Object> reqMap) throws Exception;

    public AddServiceMgmtDTO smsRegFileInfo(HashMap<String, Object> reqMap) throws Exception;

    public void updateSmsRegInfo(HashMap<String, Object> reqMap) throws Exception;

    public void deleteUseSmsYn(HashMap<String, Object> reqMap) throws Exception;

    HashMap<String, Object> pastRcpHistListCount(HashMap<String, Object> reqMap) throws Exception;

    List<AddServiceMgmtDTO> pastRcpHistList(HashMap<String, Object> reqMap) throws Exception;

    HashMap<String, Object> pastPaymentHistListCount(HashMap<String, Object> reqMap) throws Exception;

    List<AddServiceMgmtDTO> pastPaymentHistList(HashMap<String, Object> reqMap) throws Exception;

    //알림톡 신청내역 조회
    public List<AddServiceMgmtDTO> atRegList(HashMap<String, Object> reqMap) throws Exception;

    //알림톡 신청내역 갯수
    public HashMap<String, Object> atRegListCount(HashMap<String, Object> reqMap) throws Exception;

    //알림톡 신청내역 대기건수
    public HashMap<String, Object> atRegListWaitCount(HashMap<String, Object> reqMap) throws Exception;

    // 알림톡 발송기관명 수정
    public void updateAtChaName(HashMap<String, Object> reqMap) throws Exception;

    public void updateAtChaNameHis(HashMap<String, Object> reqMap) throws Exception;

    // 알림톡 신청 승인 or 거절
    public void updateAtAcptDt(HashMap<String, Object> reqMap) throws Exception;

    public void updateAtAcptHis(HashMap<String, Object> reqMap) throws Exception;

    // 알림톡 해지
    public void deleteAtYn(HashMap<String, Object> reqMap) throws Exception;

    public void deleteAtYnHis(HashMap<String, Object> reqMap) throws Exception;

    //알림톡 이용내역 조회
    public List<AddServiceMgmtDTO> atUseList(HashMap<String, Object> reqMap);

    //알림톡 이용내역 갯수
    public HashMap<String, Object> atUseListCount(HashMap<String, Object> reqMap);

    //알림톡 이용내역 성공건수
    public HashMap<String, Object> atUseListSuccessCount(HashMap<String, Object> reqMap);

    //알림톡 이용내역 실패건수
    public HashMap<String, Object> atUseListFailCount(HashMap<String, Object> reqMap);


}
