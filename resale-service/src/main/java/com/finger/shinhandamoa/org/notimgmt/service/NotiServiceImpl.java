package com.finger.shinhandamoa.org.notimgmt.service;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finger.shinhandamoa.org.notimgmt.dao.NotiDAO;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiSendDTO;

/**
 * @author by puki
 * @date 2018. 5. 09.
 * @desc 최초생성
 */
@Service
public class NotiServiceImpl implements NotiService {

    @Inject
    private NotiDAO notiDao;

    /**
     * sms고지 발송을 위한 고객정보 by  YEJ
     *
     * @param map
     * @return
     */
    @Override
    public String selSmsUseYn(String chaCd) throws Exception {
        return notiDao.selSmsUseYn(chaCd);
    }

    /**
     * sms고지 발송을 위한 고객정보 by  YEJ
     *
     * @param map
     * @return
     */
    @Override
    public List<NotiSendDTO> selCusInfo(HashMap<String, Object> map) throws Exception {
        return notiDao.selCusInfo(map);
    }

    /**
     * SMS고지 전송을 위한 발송번호 by YEJ
     *
     * @param map
     * @return
     */
    @Override
    public List<NotiSendDTO> selSendNo(String chaCd) throws Exception {
        return notiDao.selSendNo(chaCd);
    }

    /**
     * SMS고지 전송을 위한 문자템플릿정보 by YEJ
     *
     * @param map
     * @return
     */
    @Override
    public NotiSendDTO selSmsMsgbox(HashMap<String, Object> map) throws Exception {
        return notiDao.selSmsMsgbox(map);
    }

    /**
     * 문자메시지 서비스 이용 등록을 위한 기관 정보 by YEJ
     *
     * @param chaCd
     * @return
     */
    @Override
    public NotiSendDTO selOrgInfo(String chaCd) throws Exception {
        return notiDao.selOrgInfo(chaCd);
    }

    /**
     * 문자메시지 서비스 이용 등록 by YEJ
     *
     * @param map
     * @return
     */
    @Transactional
    @Override
    public void insertFormInfo(HashMap<String, Object> map) throws Exception {
        notiDao.insertFormInfo(map); // 이용증명원 insert
        notiDao.updateSmsUseYn(map); // sms 사용유무 update
    }

    /**
     * 고객명 확인 by YEJ
     *
     * @param map
     * @return
     */
    @Override
    public NotiSendDTO selCusName(HashMap<String, Object> map) throws Exception {
        return notiDao.selCusName(map);
    }

    /**
     * SMS 발송 후처리 by YEJ
     *
     * @param map
     * @return
     */
    @Override
    public void insertSmsReq(HashMap<String, Object> map) throws Exception {
        notiDao.insertSmsReq(map);
    }

    /**
     * SMS 발송 이력 total count by YEJ
     *
     * @param map
     * @return
     */
    @Override
    public int sendSmsTotalCount(HashMap<String, Object> map) throws Exception {
        return notiDao.sendSmsTotalCount(map);
    }

    /**
     * SMS 발송 이력  by YEJ
     *
     * @param map
     * @return
     */
    @Override
    public List<NotiSendDTO> sendSmsList(HashMap<String, Object> map) throws Exception {
        return notiDao.sendSmsList(map);
    }

    @Override
    public List<NotiSendDTO> excelSendSmsListDownload(HashMap<String, Object> map) throws Exception {
        return notiDao.excelSendSmsListDownload(map);
    }

    /**
     * SMS 발송 전송상태 update 데이터 추출 by YEJ
     *
     * @param map
     * @return
     */
    public List<NotiSendDTO> selSendList(String chaCd) throws Exception {
        return notiDao.selSendList(chaCd);
    }

    /**
     * SMS 발송 전송상태 update  by YEJ
     *
     * @param map
     * @return
     */
    public void sendStatus(HashMap<String, Object> map) throws Exception {
        notiDao.sendStatus(map);
    }

    /**
     * email 발송을 위한 고객정보 by YEJ
     *
     * @param map
     * @return
     */
    public int selEmailUseYn(HashMap<String, Object> map) throws Exception {
        return notiDao.selEmailUseYn(map);
    }

    /**
     * email 발송을 위한 고객정보 by YEJ
     *
     * @param map
     * @return
     */
    @Override
    public List<NotiSendDTO> selEmailCusInfo(HashMap<String, Object> map) throws Exception {
        return notiDao.selEmailCusInfo(map);
    }

    /**
     * email 발송을 위한 안내문구정보 by YEJ
     *
     * @param map
     * @return
     */
    @Override
    public NotiSendDTO selBillName(HashMap<String, Object> map) throws Exception {
        return notiDao.selBillName(map);
    }

    /**
     * email 발송 이력 total count by YEJ
     *
     * @param map
     * @return
     */
    public int sendEmailTotalCount(HashMap<String, Object> map) {
        return notiDao.sendEmailTotalCount(map);
    }

    /**
     * email 발송 이력  by YEJ
     *
     * @param map
     * @return
     */
    public List<NotiSendDTO> sendEmailList(HashMap<String, Object> map) {
        return notiDao.sendEmailList(map);
    }

    @Override
    public List<NotiSendDTO> selEmailItem(HashMap<String, Object> map) throws Exception {
        return notiDao.selEmailItem(map);
    }

    @Override
    public NotiSendDTO selCusGubn(String vano) throws Exception {
        return notiDao.selCusGubn(vano);
    }

    @Override
    public NotiSendDTO selMailCont(HashMap<String, Object> map) throws Exception {
        return notiDao.selMailCont(map);
    }

    @Override
    public String selEmailSeq(String ecareNo) throws Exception {
        return notiDao.selEmailSeq(ecareNo);
    }

    @Override
    public void insertEmailSend(HashMap<String, Object> map) throws Exception {
        notiDao.insertEmailSend(map);
    }

    @Override
    public void insertEmailHist(HashMap<String, Object> map) throws Exception {
        notiDao.insertEmailHist(map);
    }

    @Override
    public void emailInfoUpdate(HashMap<String, Object> map) throws Exception {
        notiDao.emailInfoUpdate(map);
    }

    @Override
    public void updateFormInfo(HashMap<String, Object> map) throws Exception {
        notiDao.updateFormInfo(map);
    }

    @Override
    public int selectDailySentCount(HashMap<String, Object> map) throws Exception {
        return notiDao.selectDailySentCount(map);
    }

    @Override
    public String selAtUseYn(String chaCd) throws Exception {
        return notiDao.selAtUseYn(chaCd);
    }

    @Override
    public NotiSendDTO selAtChaInfo(String chaCd) throws Exception {
        return notiDao.selAtChaInfo(chaCd);
    }

    @Override
    public void atCertificateIns(HashMap<String, Object> map) throws Exception {
        notiDao.atCertificateIns(map);
    }

    @Override
    public void atApply(HashMap<String, Object> map) throws Exception {
        notiDao.atApply(map);
    }

    @Override
    public void insertAtReq(HashMap<String, Object> map) throws Exception {
        notiDao.insertAtReq(map);
    }

    @Override
    public void updateAtReq(HashMap<String, Object> map) throws Exception {
        notiDao.updateAtReq(map);
    }

    @Override
    public int sendAtTotalCount(HashMap<String, Object> map) throws Exception {
        return notiDao.sendAtTotalCount(map);
    }

    @Override
    public List<NotiSendDTO> sendAtList(HashMap<String, Object> map) throws Exception {
        return notiDao.sendAtList(map);
    }

    @Override
    public List<NotiSendDTO> setAtMsg() throws Exception {
        return notiDao.setAtMsg();
    }

    @Override
    public int selectDailyRepeatedCount(HashMap<String, Object> map) throws Exception {
        return notiDao.selectDailyRepeatedCount(map);
    }

    @Override
    public int selectMonthlyRepeatedMean(HashMap<String, Object> map) throws Exception {
        return notiDao.selectMonthlyRepeatedMean(map);
    }

    @Override
    public int selectDailySameMsgCnt(HashMap<String, Object> map) throws Exception {
        return notiDao.selectDailySameMsgCnt(map);
    }
}
