package com.finger.shinhandamoa.sys.addServiceMgmt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.sys.addServiceMgmt.dao.AddServiceMgmtDAO;
import com.finger.shinhandamoa.sys.addServiceMgmt.dto.AddServiceMgmtDTO;
import com.finger.shinhandamoa.sys.addServiceMgmt.dto.XNotimasreqDTO;

/**
 * @author
 * @date
 * @desc
 */
@Service
public class AddServiceMgmtServiceImpl implements AddServiceMgmtService {

    @Inject
    private AddServiceMgmtDAO addServiceMgmtDao;

    @Override
    public List<XNotimasreqDTO> notiPrintListAll(Map<String, Object> map) throws Exception {
        return addServiceMgmtDao.notiPrintListAll(map);
    }

    @Override
    public int notiPrintCount(Map<String, Object> map) throws Exception {
        return addServiceMgmtDao.notiPrintCount(map);
    }

    @Override
    public void notiPrintUpdate(Map<String, Object> map) throws Exception {
        addServiceMgmtDao.notiPrintUpdate(map);
    }

    @Override
    public void quickPrint(Map<String, Object> map) throws Exception {
        addServiceMgmtDao.quickPrint(map);
    }

    @Override
    public void rePrint(Map<String, Object> map) throws Exception {
        addServiceMgmtDao.rePrint(map);
    }

    @Override
    public int notiReqPrintCount(Map<String, Object> map) throws Exception {
        return addServiceMgmtDao.notiReqPrintCount(map);
    }

    @Override
    public HashMap<String, Object> smsRegListCount(HashMap<String, Object> reqMap) throws Exception {
        return addServiceMgmtDao.smsRegListCount(reqMap);
    }

    @Override
    public HashMap<String, Object> smsRegListWaitCount(HashMap<String, Object> reqMap) throws Exception {
        return addServiceMgmtDao.smsRegListWaitCount(reqMap);
    }

    @Override
    public List<AddServiceMgmtDTO> smsRegList(HashMap<String, Object> reqMap) throws Exception {
        return addServiceMgmtDao.smsRegList(reqMap);
    }

    @Override
    public AddServiceMgmtDTO getCallerNum(HashMap<String, Object> reqMap) throws Exception {
        return addServiceMgmtDao.getCallerNum(reqMap);
    }

    @Override
    public void updateCallerNum(HashMap<String, Object> reqMap) throws Exception {
        addServiceMgmtDao.updateSmsSendTel(reqMap);
        addServiceMgmtDao.updateContents(reqMap);
    }

    @Override
    public int failNotiPrintCount(Map<String, Object> map) throws Exception {
        return addServiceMgmtDao.failNotiPrintCount(map);
    }

    @Override
    public HashMap<String, Object> cardPayHistoryCount(HashMap<String, Object> reqMap) throws Exception {
        return addServiceMgmtDao.cardPayHistoryCount(reqMap);
    }

    @Override
    public List<AddServiceMgmtDTO> cardPayHistoryList(HashMap<String, Object> reqMap) throws Exception {
        return addServiceMgmtDao.cardPayHistoryList(reqMap);
    }

    @Override
    public void updateUseSmsYn(HashMap<String, Object> reqMap) throws Exception {
        addServiceMgmtDao.updateUseSmsYn(reqMap);
    }

    @Override
    public AddServiceMgmtDTO smsRegFileInfo(HashMap<String, Object> reqMap) throws Exception {
        return addServiceMgmtDao.smsRegFileInfo(reqMap);
    }

    @Override
    public void updateSmsRegInfo(HashMap<String, Object> reqMap) throws Exception {
        addServiceMgmtDao.updateSmsRegInfo(reqMap);
    }

    @Override
    public void deleteUseSmsYn(HashMap<String, Object> reqMap) throws Exception {
        addServiceMgmtDao.deleteUseSmsYn(reqMap);
    }

    @Override
    public List<AddServiceMgmtDTO> pastRcpHistList(HashMap<String, Object> reqMap) throws Exception {
        return addServiceMgmtDao.pastRcpHistList(reqMap);
    }

    @Override
    public HashMap<String, Object> pastRcpHistListCount(HashMap<String, Object> reqMap) throws Exception {
        return addServiceMgmtDao.pastRcpHistListCount(reqMap);
    }

    @Override
    public HashMap<String, Object> pastPaymentHistListCount(HashMap<String, Object> reqMap) throws Exception {
        return addServiceMgmtDao.pastPaymentHistListCount(reqMap);
    }

    @Override
    public List<AddServiceMgmtDTO> pastPaymentHistList(HashMap<String, Object> reqMap) throws Exception {
        return addServiceMgmtDao.pastPaymentHistList(reqMap);
    }

    @Override
    public List<AddServiceMgmtDTO> atRegList(HashMap<String, Object> reqMap) throws Exception {
        return addServiceMgmtDao.atRegList(reqMap);
    }

    @Override
    public HashMap<String, Object> atRegListCount(HashMap<String, Object> reqMap) throws Exception {
        return addServiceMgmtDao.atRegListCount(reqMap);
    }

    @Override
    public HashMap<String, Object> atRegListWaitCount(HashMap<String, Object> reqMap) throws Exception {
        return addServiceMgmtDao.atRegListWaitCount(reqMap);
    }

    @Override
    public void updateAtChaName(HashMap<String, Object> reqMap) throws Exception {
        addServiceMgmtDao.updateAtChaName(reqMap);
    }

    @Override
    public void updateAtChaNameHis(HashMap<String, Object> reqMap) throws Exception {
        addServiceMgmtDao.updateAtChaNameHis(reqMap);
    }

    @Override
    public void updateAtAcptDt(HashMap<String, Object> reqMap) throws Exception {
        addServiceMgmtDao.updateAtAcptDt(reqMap);
    }

    @Override
    public void updateAtAcptHis(HashMap<String, Object> reqMap) throws Exception {
        addServiceMgmtDao.updateAtAcptHis(reqMap);
    }

    @Override
    public void deleteAtYn(HashMap<String, Object> reqMap) throws Exception {
        addServiceMgmtDao.deleteAtYn(reqMap);
    }

    @Override
    public void deleteAtYnHis(HashMap<String, Object> reqMap) throws Exception {
        addServiceMgmtDao.deleteAtYnHis(reqMap);
    }

    @Override
    public List<AddServiceMgmtDTO> atUseList(HashMap<String, Object> reqMap) throws Exception {
        return addServiceMgmtDao.atUseList(reqMap);
    }

    @Override
    public HashMap<String, Object> atUseListCount(HashMap<String, Object> reqMap) throws Exception {
        return addServiceMgmtDao.atUseListCount(reqMap);
    }

    @Override
    public HashMap<String, Object> atUseListSuccessCount(HashMap<String, Object> reqMap) throws Exception {
        return addServiceMgmtDao.atUseListSuccessCount(reqMap);
    }

    @Override
    public HashMap<String, Object> atUseListFailCount(HashMap<String, Object> reqMap) throws Exception {
        return addServiceMgmtDao.atUseListFailCount(reqMap);
    }
}
