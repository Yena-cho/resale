package com.finger.shinhandamoa.org.notimgmt.service;

import com.finger.shinhandamoa.org.notimgmt.dto.NotiSendDTO;

import java.util.HashMap;
import java.util.List;


/**
 * @author by puki
 * @date 2018. 5. 09.
 * @desc 최초생성
 */
public interface NotiService {

    /**
     * SMS고지 전송을 위한 sms사용여부 by YEJ
     *
     * @param map
     * @return
     */
    public String selSmsUseYn(String chaCd) throws Exception;

    /**
     * sms고지 발송을 위한 고객정보 by YEJ
     *
     * @param map
     * @return
     */
    public List<NotiSendDTO> selCusInfo(HashMap<String, Object> map) throws Exception;

    /**
     * SMS고지 전송을 위한 발송번호 by YEJ
     *
     * @param map
     * @return
     */
    public List<NotiSendDTO> selSendNo(String chaCd) throws Exception;

    /**
     * SMS고지 전송을 위한 문자템플릿정보 by YEJ
     *
     * @param map
     * @return
     */
    public NotiSendDTO selSmsMsgbox(HashMap<String, Object> map) throws Exception;

    /**
     * 문자메시지 서비스 이용 등록을 위한 기관 정보 by YEJ
     *
     * @param chaCd
     * @return
     */
    public NotiSendDTO selOrgInfo(String chaCd) throws Exception;

    /**
     * 문자메시지 서비스 이용 등록 by YEJ
     *
     * @param chaCd
     * @return
     */
    public void insertFormInfo(HashMap<String, Object> map) throws Exception;

    /**
     * 고객명 확인 by YEJ
     *
     * @param map
     * @return
     */
    public NotiSendDTO selCusName(HashMap<String, Object> map) throws Exception;

    /**
     * SMS 발송 후처리 by YEJ
     *
     * @param map
     * @return
     */
    public void insertSmsReq(HashMap<String, Object> map) throws Exception;

    /**
     * SMS 발송 이력 total count by YEJ
     *
     * @param map
     * @return
     */
    public int sendSmsTotalCount(HashMap<String, Object> map) throws Exception;

    /**
     * SMS 발송 이력  by YEJ
     *
     * @param map
     * @return
     */
    public List<NotiSendDTO> sendSmsList(HashMap<String, Object> map) throws Exception;

    public List<NotiSendDTO> excelSendSmsListDownload(HashMap<String, Object> map) throws Exception;

    /**
     * SMS 발송 전송상태 update 데이터 추출 by YEJ
     *
     * @param map
     * @return
     */
    public List<NotiSendDTO> selSendList(String chaCd) throws Exception;

    /**
     * SMS 발송 전송상태 update  by YEJ
     *
     * @param map
     * @return
     */
    public void sendStatus(HashMap<String, Object> map) throws Exception;

    /**
     * email 발송을 위한 고객정보 by YEJ
     *
     * @param map
     * @return
     */
    public int selEmailUseYn(HashMap<String, Object> map) throws Exception;

    /**
     * email 발송을 위한 고객정보 by YEJ
     *
     * @param map
     * @return
     */
    public List<NotiSendDTO> selEmailCusInfo(HashMap<String, Object> map) throws Exception;

    /**
     * email 발송을 위한 안내문구정보 by YEJ
     *
     * @param map
     * @return
     */
    public NotiSendDTO selBillName(HashMap<String, Object> map) throws Exception;

    /**
     * email 발송 이력 total count by YEJ
     *
     * @param map
     * @return
     */
    public int sendEmailTotalCount(HashMap<String, Object> map) throws Exception;

    /**
     * email 발송 이력  by YEJ
     *
     * @param map
     * @return
     */
    public List<NotiSendDTO> sendEmailList(HashMap<String, Object> map) throws Exception;

    /**
     * email 발송 청구항목 by YEJ
     *
     * @param map
     * @return
     */
    public List<NotiSendDTO> selEmailItem(HashMap<String, Object> map) throws Exception;

    /**
     * email 발송 고객구분 by YEJ
     *
     * @param map
     * @return
     */
    public NotiSendDTO selCusGubn(String vano) throws Exception;

    /**
     * email 발송 정보 by YEJ
     *
     * @param map
     * @return
     */
    public NotiSendDTO selMailCont(HashMap<String, Object> map) throws Exception;

    /**
     * email 발송 정보 seq 생성 by YEJ
     *
     * @param map
     * @return
     */
    public String selEmailSeq(String ecareNo) throws Exception;

    /**
     * email 발송  by YEJ
     *
     * @param map
     * @return
     */
    public void insertEmailSend(HashMap<String, Object> map) throws Exception;

    /**
     * email 발송 - 이력등록 by YEJ
     *
     * @param map
     * @return
     */
    public void insertEmailHist(HashMap<String, Object> map) throws Exception;

    /**
     * email 수정 by YEJ
     *
     * @param map
     * @return
     */
    public void emailInfoUpdate(HashMap<String, Object> map) throws Exception;

    /**
     * @param map
     * @throws Exception
     */
    public void updateFormInfo(HashMap<String, Object> map) throws Exception;

    int selectDailySentCount(HashMap<String, Object> map) throws Exception;

    public String selAtUseYn(String chaCd) throws Exception;

    public NotiSendDTO selAtChaInfo(String chaCd) throws Exception;

    public void atCertificateIns(HashMap<String, Object> map) throws Exception;

    public void atApply(HashMap<String, Object> map) throws Exception;

    public void insertAtReq(HashMap<String, Object> map) throws Exception;

    public void updateAtReq(HashMap<String, Object> map) throws Exception;

    public int sendAtTotalCount(HashMap<String, Object> map) throws Exception;

    public List<NotiSendDTO> sendAtList(HashMap<String, Object> map) throws Exception;

    public List<NotiSendDTO> setAtMsg() throws Exception;

    int selectDailyRepeatedCount(HashMap<String,Object> map) throws Exception;

    int selectMonthlyRepeatedMean(HashMap<String,Object> map) throws Exception;

    /**
     * 동일 메세지 일 3회 이상 발송 제한
     * @param map
     * @throws Exception
     */
    int selectDailySameMsgCnt(HashMap<String, Object> map) throws Exception;
}
