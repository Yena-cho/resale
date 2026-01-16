package com.finger.shinhandamoa.org.notimgmt.dao;

import com.finger.shinhandamoa.org.notimgmt.dto.NotiSendDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;

/**
 * @author by puki
 * @date 2018. 5. 09.
 * @desc 최초생성
 */
@Repository
public class NotiDAO {

    @Inject
    private SqlSession sqlSession;

    /**
     * sms고지 발송을 위한 고객정보 by YEJ
     *
     * @param map
     * @return
     */
    public List<NotiSendDTO> selCusInfo(HashMap<String, Object> map) {
        return sqlSession.selectList("NotiDao.selSmsCusInfo", map);
    }

    /**
     * SMS고지 전송을 위한 sms사용여부 by YEJ
     *
     * @param map
     * @return
     */
    public String selSmsUseYn(String chaCd) {
        return sqlSession.selectOne("NotiDao.selSmsUseYn", chaCd);
    }

    /**
     * SMS고지 전송을 위한 발송번호 by YEJ
     *
     * @param map
     * @return
     */
    public List<NotiSendDTO> selSendNo(String chaCd) {
        return sqlSession.selectList("NotiDao.selSmsSendNo", chaCd);
    }

    /**
     * SMS고지 전송을 위한 문자템플릿정보 by YEJ
     *
     * @param map
     * @return
     */
    public NotiSendDTO selSmsMsgbox(HashMap<String, Object> map) {
        return sqlSession.selectOne("NotiDao.selSmsMasInfo", map);
    }

    /**
     * 문자메시지 서비스 이용 등록을 위한 기관 정보 by YEJ
     *
     * @param chaCd
     * @return
     */
    public NotiSendDTO selOrgInfo(String chaCd) {
        return sqlSession.selectOne("NotiDao.selOrgInfo", chaCd);
    }

    /**
     * 문자메시지 서비스 이용 등록 by YEJ
     *
     * @param chaCd
     * @return
     */
    public void insertFormInfo(HashMap<String, Object> map) {
        sqlSession.insert("NotiDao.insertFormInfo", map);
    }

    /**
     * 고객명 확인 by YEJ
     *
     * @param map
     * @return
     */
    public NotiSendDTO selCusName(HashMap<String, Object> map) {
        return sqlSession.selectOne("NotiDao.selCusName", map);
    }

    /**
     * SMS 발송 후처리 by YEJ
     *
     * @param map
     * @return
     */
    public void insertSmsReq(HashMap<String, Object> map) {
        sqlSession.insert("NotiDao.insertSmsReq", map);
    }

    /**
     * SMS 발송 이력 total count by YEJ
     *
     * @param map
     * @return
     */
    public int sendSmsTotalCount(HashMap<String, Object> map) {
        return sqlSession.selectOne("NotiDao.sendSmsTotalCount", map);
    }


    /**
     * SMS 발송 이력  by YEJ
     *
     * @param map
     * @return
     */
    public List<NotiSendDTO> sendSmsList(HashMap<String, Object> map) {
        return sqlSession.selectList("NotiDao.sendSmsList", map);
    }

    public List<NotiSendDTO> excelSendSmsListDownload(HashMap<String, Object> map) {
        return sqlSession.selectList("NotiDao.excelSendSmsListDownload", map);
    }

    /**
     * SMS 발송 전송상태 update 데이터 추출 by YEJ
     *
     * @param map
     * @return
     */
    public List<NotiSendDTO> selSendList(String chaCd) {
        return sqlSession.selectList("NotiDao.selSendList", chaCd);
    }

    /**
     * SMS 발송 전송상태 update  by YEJ
     *
     * @param map
     * @return
     */
    public void sendStatus(HashMap<String, Object> map) {
        sqlSession.update("NotiDao.sendStatus", map);
    }

    /**
     * email 발송을 위한 기관템플릿 유무 by YEJ
     *
     * @param map
     * @return
     */
    public int selEmailUseYn(HashMap<String, Object> map) {
        return sqlSession.selectOne("NotiDao.selEmailUseYn", map);
    }

    /**
     * email 발송을 위한 고객정보 by YEJ
     *
     * @param map
     * @return
     */
    public List<NotiSendDTO> selEmailCusInfo(HashMap<String, Object> map) {
        return sqlSession.selectList("NotiDao.selEmailCusInfo", map);
    }

    /**
     * email 발송을 위한 안내문구정보 by YEJ
     *
     * @param map
     * @return
     */
    public NotiSendDTO selBillName(HashMap<String, Object> map) {
        return sqlSession.selectOne("NotiDao.selBillName", map);
    }

    /**
     * sms 사용유무 수정 by YEJ
     *
     * @param map
     * @return
     */
    public void updateSmsUseYn(HashMap<String, Object> map) {
        sqlSession.update("NotiDao.updateSmsUseYn", map);
    }

    /**
     * email 발송 이력 total count by YEJ
     *
     * @param map
     * @return
     */
    public int sendEmailTotalCount(HashMap<String, Object> map) {
        return sqlSession.selectOne("NotiDao.sendEmailTotalCount", map);
    }

    /**
     * email 발송 이력  by YEJ
     *
     * @param map
     * @return
     */
    public List<NotiSendDTO> sendEmailList(HashMap<String, Object> map) {
        return sqlSession.selectList("NotiDao.sendEmailList", map);
    }

    /**
     * email 발송 청구항목 by YEJ
     *
     * @param map
     * @return
     */
    public List<NotiSendDTO> selEmailItem(HashMap<String, Object> map) {
        return sqlSession.selectList("NotiDao.selEmailItem", map);
    }

    /**
     * email 발송 고객구분 by YEJ
     *
     * @return
     */
    public NotiSendDTO selCusGubn(String vano) {
        return sqlSession.selectOne("NotiDao.selCusGubn", vano);
    }

    /**
     * email 발송 정보 by YEJ
     *
     * @param map
     * @return
     */
    public NotiSendDTO selMailCont(HashMap<String, Object> map) {
        return sqlSession.selectOne("NotiDao.selMailCont", map);
    }

    /**
     * email 발송 정보 seq 생성 by YEJ
     *
     * @return
     */
    public String selEmailSeq(String ecareNo) {
        return sqlSession.selectOne("NotiDao.selEmailSeq", ecareNo);
    }

    /**
     * email 발송  by YEJ
     *
     * @param map
     * @return
     */
    public void insertEmailSend(HashMap<String, Object> map) {
        sqlSession.selectOne("NotiDao.insertEmailSend", map);
    }

    /**
     * email 발송 - 이력등록 by YEJ
     *
     * @param map
     * @return
     */
    public void insertEmailHist(HashMap<String, Object> map) {
        sqlSession.selectOne("NotiDao.insertEmailHist", map);
    }

    /**
     * email 수정 by YEJ
     *
     * @param map
     * @return
     */
    public void emailInfoUpdate(HashMap<String, Object> map) {
        sqlSession.update("emailInfoUpdate", map);
    }

    public void updateFormInfo(HashMap<String, Object> map) {
        sqlSession.update("NotiDao.updateFormInfo", map);

    }

    public int selectDailySentCount(HashMap<String, Object> map) {
        return sqlSession.selectOne("NotiDao.selectDailySentCount", map);
    }

    public String selAtUseYn(String chaCd) {
        return sqlSession.selectOne("NotiDao.selAtUseYn", chaCd);
    }

    public NotiSendDTO selAtChaInfo(String chaCd) {
        return sqlSession.selectOne("NotiDao.selAtChaInfo", chaCd);
    }

    public void atCertificateIns(HashMap<String, Object> map) {
        sqlSession.update("NotiDao.atCertificateIns", map);
    }

    public void atApply(HashMap<String, Object> map) { sqlSession.insert("NotiDao.atApply", map); }

    public void insertAtReq(HashMap<String, Object> map) {
        sqlSession.insert("NotiDao.insertAtReq", map);
    }

    public void updateAtReq(HashMap<String, Object> map) {
        sqlSession.insert("NotiDao.updateAtReq", map);
    }

    public int sendAtTotalCount(HashMap<String, Object> map) {
        return sqlSession.selectOne("NotiDao.sendAtTotalCount", map);
    }

    public List<NotiSendDTO> sendAtList(HashMap<String, Object> map) {
        return sqlSession.selectList("NotiDao.sendAtList", map);
    }

    public List<NotiSendDTO> setAtMsg() {
        return sqlSession.selectList("NotiDao.setAtMsg");
    }

    public int selectDailyRepeatedCount(HashMap<String,Object> map) {
        return sqlSession.selectOne("NotiDao.selectDailyRepeatedCount", map);
    }

    public int selectMonthlyRepeatedMean(HashMap<String,Object> map) {
        return sqlSession.selectOne("NotiDao.selectMonthlyRepeatedMean", map);
    }

    public int selectDailySameMsgCnt(HashMap<String, Object> map) {
        return sqlSession.selectOne("NotiDao.selectDailySameMsgCnt", map);
    }
}
