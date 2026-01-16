package kr.co.finger.damoa.scheduler.service;

import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.damoa.commons.biz.VirtualAccountNoChecker;
import kr.co.finger.damoa.model.rcp.ChacdInfo;
import kr.co.finger.damoa.scheduler.dao.BatchWorkerDao;
import kr.co.finger.damoa.scheduler.model.CancelBean;
import kr.co.finger.msgio.corpinfo.CorpInfo;
import kr.co.finger.msgio.corpinfo.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 가상계좌 발급,
 * 입금(이체), 취소 전문 처리...
 */
@Service
public class DamoaService {

    @Autowired
    private BatchWorkerDao batchWorkerDao;

    private Logger LOG = LoggerFactory.getLogger(getClass());

    public List<Map<String, Object>> findProcessNMsg(String date) {
        return batchWorkerDao.findProcessNMsg(date);
    }

    public CancelBean findCancel(String dealSeqNo, String date) {
        Map<String, Object> map = batchWorkerDao.findCancel(dealSeqNo, date);
        if (map == null) {
            LOG.info("수납건이 없음.. " + dealSeqNo + "\t" + date);
            return null;
        }

        CancelBean cancelBean = new CancelBean(dealSeqNo, date);
        cancelBean.setRcpMasCd(Maps.getValue(map, "RCPMASCD"));
        cancelBean.setNotiMasCd(Maps.getValue(map, "NOTIMASCD"));
        return cancelBean;
    }

    public void updateRcpCancel(String rcpMasCd) {
        batchWorkerDao.updateRcpCancel(rcpMasCd);
    }

    public void updateRcpDetCancel(String rcpMasCd) {
        batchWorkerDao.updateRcpDetCancel(rcpMasCd);
    }

    public List<Map<String, Object>> findRcpCanCelInfo(String notiMasCd) {
        return batchWorkerDao.findRcpCanCelInfo(notiMasCd);
    }

    public void updateNotidetWithNotidetcd(String notdetCd, String state) {
        batchWorkerDao.updateNotidetWithNotidetcd(notdetCd, state);
    }

    public void updateNotimas(String notimasCd, String state) {
        batchWorkerDao.updateNotimas(notimasCd, state);
    }

    public List<Map<String, Object>> findNotiCanCelInfo(String notimasCd) {
        return batchWorkerDao.findNotiCanCelInfo(notimasCd);
    }

    private boolean doCheck(Map<String, Object> result, String key) {
        String doCheck = Maps.getValue(result, key);
        if ("Y".equalsIgnoreCase(doCheck)) {
            return true;
        } else {
            return false;
        }
    }

    public ChacdInfo findChaSimpleInfo(String corpCode) {
        Map<String, Object> result = batchWorkerDao.findChaSimpleInfo(corpCode);
        if (result == null) {
            return null;
        }

        boolean doCheckDate = doCheck(result, "RCP_DUE_CHK");
        boolean doCheckAmount = doCheck(result, "AMTCHKTY");
        String chast = Maps.getValue(result, "CHAST");
        String chatrty = Maps.getValue(result, "CHATRTY");
        String adjaccYn = Maps.getValue(result, "ADJACCYN");
        return new ChacdInfo(doCheckDate, doCheckAmount, chast, chatrty, adjaccYn);
    }

    public List<Map<String, Object>> findNotiMasDet(String chacd, String vano) {
        return this.batchWorkerDao.findNotiMasDet(chacd, vano);
    }

    public List<Map<String, Object>> findSimpleRcpMasDet(String corpCode, String accountNo) {
        return batchWorkerDao.findSimpleRcpMasDet(corpCode, accountNo);
    }

    public String findRcpMasCd() {
        return batchWorkerDao.findRcpMasCd();
    }

    public void insertDepositMsgHistory(String depositCorpCode, String depositAccountNo, String transactionAmount, String amountOfFee, String type) {
        batchWorkerDao.insertDepositMsgHistory(depositCorpCode, depositAccountNo, transactionAmount, amountOfFee, type);
    }

    public void insertRcpMaster(Map<String, Object> map) {
        batchWorkerDao.insertRcpMaster(map);
    }

    public void insertRcpDetail(Map<String, Object> map) {
        batchWorkerDao.insertRcpDetail(map);
    }

    public Map<String, Object> findChaCusInfo(String vano) {
        return batchWorkerDao.findChaCusInfo(vano);
    }

    public void insertCashMaster(Map<String, Object> map) {
        batchWorkerDao.insertCashMaster(map);
    }

    public List<Map<String, Object>> findVareq() {
        return batchWorkerDao.findVareq();
    }

    public String findBranchCode(String chacd) {
        return batchWorkerDao.findBranchCode(chacd);
    }

    public String findChatrty(String chacd) {
        return batchWorkerDao.findChatrty(chacd);
    }

    @Transactional
    public void insertValist(String startAccount, String acccnt, String ficd, String fitxCd, String chacd, String useYn, String varegty,String remark) throws Exception {
        boolean is13 = false;
        long start = 0;
        if (startAccount.length() == 13) {
            start = Long.valueOf(startAccount.substring(0, 12));
            is13 = true;
        } else if(startAccount.length() == 14){
            start = Long.valueOf(startAccount.substring(0, 13));
        }else {
            // 이때는 처리할 수 없음...
            LOG.error("가상계좌가 13자리나 14자리가 아님.. 처리할 수 없음..");
            throw new Exception("가상계좌가 13자리나 14자리가 아님.. 처리할 수 없음..");
        }

        int count = Integer.valueOf(acccnt);
        for (int i = 0; i < count; i++) {
            long _vano = start + i;
            String parity = null;
            if (is13) {
                parity = VirtualAccountNoChecker.find13CrcNo(_vano + "");
            } else {
                parity = VirtualAccountNoChecker.find14CrcNo(_vano + "");
            }
            String vano = _vano + "" + parity;
            LOG.info("생성..[가상계좌요청번호]"+fitxCd+"[기관]"+chacd+"[가상계좌번호]"+vano);
            batchWorkerDao.insertValist(ficd, vano, fitxCd, chacd, useYn, varegty,remark);

        }

    }

    public void sendVanoRequest(String msgNo) {
        batchWorkerDao.sendVanoRequest(msgNo);
    }

    public void finishVanoRequestOk(String msgNo, String startAccount) {
        batchWorkerDao.finishVanoRequestOk(msgNo,startAccount);
    }
    public void updateReqOkInsertFail(String msgNo, String startAccount) {
        batchWorkerDao.updateReqOkInsertFail(msgNo,startAccount);
    }
    public void finishVanoRequestFail(String msgNo) {
        batchWorkerDao.finishVanoRequestFail(msgNo);
    }

    public void updateMsgProcY(String dealSeqNo, String now) {

        batchWorkerDao.updateMsgProcY(dealSeqNo, now);
    }

    public List<Map<String, Object>> findAllCorpCode() {
        return batchWorkerDao.findAllCorpCode();
    }

    public void updateNoChangeCompanyStatus(String chacd) {
        batchWorkerDao.updateNoChangeCompanyStatus(chacd);
    }

    public void updateFailChangeCompanyStatus(String chacd, String code) {
        batchWorkerDao.updateFailChangeCompanyStatus(chacd,code);
    }

    public void updateOkChangeCompanyStatus(String chacd,String code) {
        batchWorkerDao.updateOkChangeCompanyStatus(chacd,code);
    }

    public void insertChalist(Data data) {
        batchWorkerDao.insertChalist(data);
    }

    public void finishVanoRequestFailRetry(String msgNo) {
        batchWorkerDao.finishVanoRequestFailRetry(msgNo);
    }

}
