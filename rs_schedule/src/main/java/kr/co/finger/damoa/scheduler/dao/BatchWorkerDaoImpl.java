package kr.co.finger.damoa.scheduler.dao;


import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.msgio.cash.CashMessage;
import kr.co.finger.msgio.cash.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static kr.co.finger.damoa.scheduler.utils.DamoaBizUtil.handleDecryptData;
import static kr.co.finger.damoa.scheduler.utils.DamoaBizUtil.handleEncryptData;

/**
 * workId,date,virtAcctId
 */
public class BatchWorkerDaoImpl implements BatchWorkerDao {

    private final SqlSession sqlSession;

    private class Result {
        public String code;
        public Date now;

        public Result(String code, Date now) {
            this.code = code;
            this.now = now;
        }
    }


    private Logger LOG = LoggerFactory.getLogger(getClass());

    public BatchWorkerDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public List<Map<String, Object>> findCashReceipts() {
        return sqlSession.selectList("scheduler.findCashReceipts");
    }

    @Override
    public List<Map<String, Object>> findNewAutomaticWithdrawal() {
        List<Map<String, Object>> mapList = sqlSession.selectList("scheduler.findNewAutomaticWithdrawal");
        for (Map<String, Object> map : mapList) {
            handleDecryptData(map, new String[]{"FEEACCNO", "FEEOFFNO"});
        }
        return mapList;
    }

    @Override
    public List<Map<String, Object>> findMonthlyAutomaticWithdrawal(String month) {
        return sqlSession.selectList("scheduler.findMonthlyAutomaticWithdrawal", month);
    }

    /**
     * 배치요청을 수행한다.
     * 1보다 크면 배치요청을 처리할 수 있다.
     * 0 이면 배치처리를 하지 않는다.
     * workId, date 가 주요 키이다.
     */
    public int requestPermission(String startDate, String jobId) {
        Map<String, Object> param = Maps.hashmap();
        param.put("startdate", startDate);
        param.put("jobid", jobId);
        return sqlSession.insert("scheduler.requestPermission", param);
    }

    @Override
    public void updateXCashMasForSending(Map<String, Object> param) {
        sqlSession.update("scheduler.updateXCashMasForSending", param);
    }

    @Override
    public void updateXCashMasForReceiving(Map<String, Object> param) {
        sqlSession.update("scheduler.updateXCashMasForReceiving", param);
    }

    @Override
    public void insertCashReqForSending(Map<String, Object> param) {
        LOG.info("insertCashReqForSending " + param);
//        sqlSession.insert("scheduler.insertCashReqForSending", param);
    }

    @Override
    public void updateCashReq(Map<String, Object> param) {
        sqlSession.update("scheduler.updateCashReq", param);
    }

    @Override
    public void insertCashFilePath(String fullFilePath) {
        String fileName = FilenameUtils.getName(fullFilePath);
        Map<String, Object> param = Maps.hashmap();
        param.put("cash_file_name", fileName);
        param.put("trans_dt", DateUtils.to14NowString());
        sqlSession.insert("scheduler.insertCashFilePath", param);
    }

    @Override
    public void insertCashReqForSending(CashMessage cashMessage, List<Map<String, Object>> maps) {
        LOG.info("insertCashReqForSending...");
        List<Data> dataList = cashMessage.getDataList();
        for (Data data : dataList) {
            String temp = data.getTemp();
            String[] strings = StringUtils.split(temp, ",");
            String masCd = null;
            if (strings.length == 2) {
                masCd = strings[0];
            } else {
                masCd = temp;
            }
            Map map = findMap(maps, masCd);
            if (map == null) {
                LOG.info("SKIP " + masCd);
                continue;
            }
            Map<String, Object> param = data.toParamForSending(map);
            String _temp = Maps.getValue(param, "cashmascd");
            String[] strings1 = StringUtils.split(_temp, ",");
            if (strings1.length == 2) {
                param.put("cashmascd", strings1[0]);
            }
            LOG.info("INSERT " + param);

            sqlSession.insert("scheduler.insertCashReqForSending", param);
        }
    }


    private Map<String, Object> findMap(List<Map<String, Object>> maps, String rcpMasCd) {
        for (Map<String, Object> map : maps) {
            String _rcpMasCd = Maps.getValue(map, "cashmascd");
            if (rcpMasCd.equalsIgnoreCase(_rcpMasCd)) {
                return map;
            }
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> findUnhandledFilePath() {
        return sqlSession.selectList("scheduler.findUnhandledFilePath");
    }

    /**
     * 수집할 파일을 처리함.
     * 수신파일명을 송신파일명으로 변경후 처리.
     *
     * @param fileName 수신파일명.
     */
    @Override
    public void finishUnhandledFilePath(String transDate, String fileName) {
        Map<String, Object> map = Maps.hashmap();
        map.put("TRANS_DT", transDate);
        map.put("CASH_FILE_NAME", fileName);
        map.put("REC_DT", DateUtils.to14NowString());
        LOG.info("finishUnhandledFilePath " + map);
        sqlSession.update("scheduler.finishUnhandledFilePath", map);
    }

    @Override
    public void updateEB14Result(Map<String, Object> param) {
        sqlSession.update("scheduler.updateEB14Result", param);
    }

    @Override
    public void updateEBI13Result(Map<String, Object> param) {
        sqlSession.update("scheduler.updateEB13Result", param);
    }

    @Override
    public void updateEB21Result(Map<String, Object> param) {
        sqlSession.update("scheduler.updateEB21Result", param);
    }

    @Override
    public void updateEB22Result(Map<String, Object> param) {
        sqlSession.update("scheduler.updateEB22Result", param);
    }

    @Override
    public void finishExecution(String startDate, String workId, int count, boolean isOk) {
        Map<String, Object> param = Maps.hashmap();
        param.put("ENDDATE", DateUtils.to14NowString());
        if (isOk == true) {
            param.put("STATUS", "2");   //정상실행종료
        } else {
            param.put("STATUS", "3");   //비정상실행종료
        }
        param.put("TOTCNT", count);
        param.put("STARTDATE", startDate);
        param.put("JOBID", workId);
        sqlSession.update("scheduler.finishExecution", param);
    }

    @Override
    public List<Map<String, Object>> findProcessNMsg(String date) {
        Map<String, Object> param = Maps.hashmap();
        param.put("TRANSDT", date);
        return sqlSession.selectList("scheduler.findProcessNMsg", param);
    }

    @Override
    public Map<String, Object> findCancel(String dealSeqNo, String payDay) {
        Map<String, Object> map = Maps.hashmap();
        map.put("DEALSEQNO", dealSeqNo);
        map.put("PAYDAY", payDay);

        return this.sqlSession.selectOne("scheduler.findCancel", map);
    }

    @Override
    public void updateRcpCancel(String rcpMasCd) {
        this.sqlSession.update("scheduler.updateRcpCancel", rcpMasCd);
    }

    @Override
    public void updateRcpDetCancel(String rcpMasCd) {
        this.sqlSession.update("scheduler.updateRcpDetCancel", rcpMasCd);
    }

    @Override
    public List<Map<String, Object>> findRcpCanCelInfo(String rcpMasCd) {
        return this.sqlSession.selectList("scheduler.findRcpCanCelInfo", rcpMasCd);
    }


    @Override
    public void updateNotidetWithNotidetcd(String notidetcd, String st) {
        Map<String, Object> param = Maps.hashmap();
        param.put("NOTIDETCD", notidetcd);
        param.put("NOTIDETST", st);
        this.sqlSession.update("scheduler.updateNotidetWithNotidetcd", param);
    }

    @Override
    public void updateNotimas(String notimascd, String state) {
        Map<String, Object> param = Maps.hashmap();
        param.put("NOTIMASCD", notimascd);
        param.put("NOTIMASST", state);
        this.sqlSession.update("scheduler.updateNotimas", param);
    }

    @Override
    public List<Map<String, Object>> findNotiCanCelInfo(String notimasCd) {
        return this.sqlSession.selectList("scheduler.findNotiCanCelInfo", notimasCd);
    }

    @Override
    public Map<String, Object> findChaSimpleInfo(String chacd) {
        return this.sqlSession.selectOne("scheduler.findChaSimpleInfo", chacd);
    }

    @Override
    public List<Map<String, Object>> findNotiMasDet(String chacd, String vano) {
        return this.sqlSession.selectList("scheduler.findNotiMasDet", param(chacd, vano));
    }

    @Override
    public List<Map<String, Object>> findSimpleRcpMasDet(String corpCode, String accountNo) {
        return this.sqlSession.selectList("scheduler.findSimpleRcpMasDet", param(corpCode, accountNo));
    }

    @Override
    public String findRcpMasCd() {
        return this.sqlSession.selectOne("scheduler.findRcpMasCd");
    }


    private Map<String, Object> param(String chacd, String vano) {
        Map<String, Object> param = Maps.hashmap();
        param.put("CHACD", chacd);
        param.put("VANO", vano);
        return param;
    }

    @Override
    public void insertDepositMsgHistory(String corpCd, String vaNo, String depositAmout, String amountOfFee, String type) {
        Map<String, Object> param = Maps.hashmap();
        param.put("RCPDAY", DateUtils.toNowString());
        param.put("CHACD", corpCd);
        param.put("VANO", vaNo);
        param.put("RCPAMT", depositAmout);
        param.put("FEEAMT", amountOfFee);
        param.put("RCPMASST", type);

        this.sqlSession.insert("scheduler.insertDepositMsgHistory", param);
    }

    @Override
    public void insertRcpMaster(Map<String, Object> map) {
        this.sqlSession.insert("scheduler.insertXRcpMas", map);
    }

    @Override
    public void insertRcpDetail(Map<String, Object> map) {
        this.sqlSession.insert("scheduler.insertXRcpDet", map);
    }

    //    @Override
//    public void updateXCashMasForCancelReceiving(Map<String, Object> param) {
//        sqlSession.updateStatus("scheduler.updateXCashMasForCancelReceiving", param);
//    }
    @Override
    public void insertCashMaster(Map<String, Object> map) {
        this.sqlSession.insert("scheduler.insertXCashMas", map);
    }

    @Override
    public Map<String, Object> findChaCusInfo(String vano) {
        return this.sqlSession.selectOne("scheduler.findChaCusInfo", vano);
    }

    @Override
    public List<Map<String, Object>> findVareq() {
        return this.sqlSession.selectList("scheduler.findVareq");
    }

    @Override
    public String findBranchCode(String chacd) {
        return this.sqlSession.selectOne("scheduler.findBranchCode", chacd);
    }

    public String findChatrty(String chacd) {
        return this.sqlSession.selectOne("scheduler.findChatrty", chacd);
    }

    @Override
    public void insertValist(String ficd, String vano, String fitxCd, String chacd, String useYn, String varegty, String remark) {
        //(#{FICD}, #{VANO}, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISSsss'), #{CHACD}, #{USEYN}, SYSDATE, #{VAREGTY})
        Map<String, Object> param = Maps.hashmap();
        param.put("FICD", ficd);
        param.put("VANO", vano);
        param.put("FITXCD", fitxCd);
        param.put("CHACD", chacd);
        param.put("USEYN", useYn);
        param.put("VAREGTY", varegty);
        param.put("REMARK", remark);
        this.sqlSession.insert("scheduler.insertValist", param);
    }

    @Override
    public void sendVanoRequest(String ruleMsgNo) {
        this.sqlSession.update("scheduler.updateSendVareq", ruleMsgNo);
    }

    @Override
    public void finishVanoRequestFail(String ruleMsgNo) {
        this.sqlSession.update("scheduler.updateReceivingVareqFail", ruleMsgNo);
    }

    @Override
    public void finishVanoRequestFailRetry(String msgNo) {
        this.sqlSession.update("scheduler.finishVanoRequestFailRetry", msgNo);
    }

    @Override
    public List<Map<String, Object>> getRsOrgMonitoredData(String fgCd) {
        return this.sqlSession.selectList("scheduler.selectRsOrgMonitoredDataList", fgCd);
    }

    @Override
    public List<Map<String, Object>> getRsVaMonitoredData(String fgcd) {
        return this.sqlSession.selectList("scheduler.selectRsVaMonitoredDataList", fgcd);
    }

    @Override
    public String getChacdByChaOffNoAndChaNameFromChaList(Map<String, Object> paramMap) {
        return this.sqlSession.selectOne("scheduler.getChacdByChaOffNoAndChaNameFromChaList",paramMap);
    }

    @Override
    public String getChacdByChaOffNoAndChaNameFromVaList(String vaNo) {
        return this.sqlSession.selectOne("scheduler.getChacdByChaOffNoAndChaNameFromVaList",vaNo);
    }

    @Override
    public void insertMonitoringFileHist(Map<String,Object> paramMap) {
        this.sqlSession.insert("scheduler.insertMonitoringFileHist", paramMap);
    }

    @Override
    public List<Map<String, Object>> selectMonitoringFileList() {
        return this.sqlSession.selectList("scheduler.selectMonitoringFileList");
    }

    @Override
    public void updateMonitoringFileHist(Map<String, Object> fileMap) {
        this.sqlSession.update("scheduler.updateMonitoringFileHist", fileMap);
    }

    @Override
    public void insertMonitoringFileHistDet(Map<String, Object> map) {
        this.sqlSession.insert("scheduler.insertMonitoringFileHistDet",map);
    }

    @Override
    public void finishVanoRequestOk(String ruleMsgNo, String startAccount) {
        Map<String, Object> param = Maps.hashmap();
        param.put("RULEMNGNO", ruleMsgNo);
        param.put("STARTACCT", startAccount);
        this.sqlSession.update("scheduler.updateReceivingVareqOK", param);
    }

    @Override
    public void updateReqOkInsertFail(String ruleMsgNo, String startAccount) {
        Map<String, Object> param = Maps.hashmap();
        param.put("RULEMNGNO", ruleMsgNo);
        param.put("STARTACCT", startAccount);
        this.sqlSession.update("scheduler.updateReqOkInsertFail", param);
    }

    @Override
    public void updateMsgProcY(String dealSeqNo, String now) {
        Map<String, Object> param = Maps.hashmap();
        param.put("DEALSEQNO", dealSeqNo);
        param.put("TRANSDT", now);
        this.sqlSession.update("scheduler.updateMsgProcY", param);
    }

    @Override
    public List<Map<String, Object>> findAllCorpCode() {
        return this.sqlSession.selectList("scheduler.findAllCorpCode");
    }

    @Override
    public void updateNoChangeCompanyStatus(String chacd) {
        Map<String, Object> param = Maps.hashmap();

        param.put("CHA_CLOSE_VAR_DT", DateUtils.toString(new Date(), "yyyyMMdd"));
        param.put("CHACD", chacd);
        this.sqlSession.update("scheduler.updateNoChangeCompanyStatus", param);
    }

    /**
     * CHA_CLOSE_CHK	휴폐업상태 [ Y:휴폐업상태  N:정상 ]
     * CHA_CLOSE_ST	휴폐업상태코드 [ 11:정상, 31:휴업, 32:폐업 ]
     * CHA_CLOSE_VAR_DT	휴폐업 검증일시
     * CHA_CLOSE_VAR_RESON	기관 검증 결과 부적합사유
     *
     * @param chacd
     * @param code
     */
    @Override
    public void updateFailChangeCompanyStatus(String chacd, String code) {
        Map<String, Object> param = companyStatusParam("Y", code, chacd);
        this.sqlSession.update("scheduler.updateChangeCompanyStatus", param);
    }

    @Override
    public void updateOkChangeCompanyStatus(String chacd, String code) {
        Map<String, Object> param = companyStatusParam("N", code, chacd);
        this.sqlSession.update("scheduler.updateChangeCompanyStatus", param);
    }

    @Override
    public boolean findVano(String vano) {
        Integer count = this.sqlSession.selectOne("scheduler.findVano", vano);
        if (count == null || count == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void insertVareq(String nowString, String chacd, String count) {
        Map<String, Object> param = Maps.hashmap();
        param.put("RULEMNGNO", nowString);
        param.put("CHACD", chacd);
        param.put("ACCCNT", count);
        this.sqlSession.insert("scheduler.insertVareq", param);
    }

    /**
     * 기관정보 입력...
     *
     * @param data
     */
    @Override
    public void insertChalist(kr.co.finger.msgio.corpinfo.Data data) {

//        Map<String,Object> param = data.toXChalist();
//        handleEncryptData(param,new String[]{"FEEACCNO"});
//        handleFee(param, data);
//        insertChalist(param);
//
//        List<Map<String, Object>> mapList = new ArrayList<>();
//        handleXadjGroup(data.toXadjGroup(mapList));
    }

    private void handleFee(Map<String, Object> param, kr.co.finger.msgio.corpinfo.Data data) {
        param.put("RCPCNTFEE", fingerFee(data));
        param.put("RCPBNKFEE", bankFee(data));
    }

    //FIXME 수수료 계산 로직은 살펴봐야 함..
    private String fingerFee(kr.co.finger.msgio.corpinfo.Data data) {
//        return data.getFeeAmount().trim();
        return null;
    }

    //FIXME 수수료 계산 로직은 살펴봐야 함..
    private String bankFee(kr.co.finger.msgio.corpinfo.Data data) {
//        return calculate(data.getFeeAmount().trim(), data.getFeeRatio().trim())+"";
        return null;
    }

    private long calculate(String amount, String feeRatio) {
        Long _amount = Long.valueOf(amount);
        Long _ratio = Long.valueOf(feeRatio);
        Long fingerFee = _amount * _ratio / 100L;
        return _amount - fingerFee;
    }

    /**
     * 기관정보 입력...
     *
     * @param param
     */
    private void insertChalist(Map<String, Object> param) {
        sqlSession.insert("scheduler.insertXChalist", param);
    }

    @Override
    public boolean exist(String chacd) {
        Integer count = sqlSession.selectOne("scheduler.findChacd", chacd);
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 기관정보 업데이트...
     *
     * @param param
     */
    private void updateXChalist(Map<String, Object> param) {
        LOG.info("updateXChalist " + param);
        handleEncryptData(param, new String[]{"FEEACCNO"});
        sqlSession.update("scheduler.updateXChalist", param);
    }

    public void updateChalist(kr.co.finger.msgio.corpinfo.Data msg) {
        LOG.info("기관업데이트수행.. " + msg);
//        Map<String,Object> param = msg.paramUpdateXChalist();
//        handleFee(param, msg);
//        updateXChalist(param);
//        List<Map<String,Object>> mapList = findAdjfiRegKey(msg.getCorpCode());
//        handleXadjGroup(msg.toXadjGroup(mapList));
    }

    /**
     * 수수료/조건을 은행수수료이력 테이블에 저장함.
     *
     * @param data
     */
    @Override
    public void handleFeeHist(kr.co.finger.msgio.corpinfo.Data data) {
//        String chacd = data.getCorpCode().trim();
//        String startDate = data.getFeeStartDate();
//        //FIXME 종료일자가 없음..
//        if (findFeeHist(chacd, startDate)) {
//            LOG.info("수수료히스토리 테이블에 데이터가 있어 SKIP...");
//        } else {
//            String rcpFee = fingerFee(data);
//            String bnkFee = bankFee(data);
//            String ration = data.getFeeRatio().trim();
//            insertFeeHist(chacd, startDate, rcpFee, bnkFee,ration);
//        }
    }

    @Override
    public List<Map<String, Object>> checkCancelAPStatus(String today) {
        return this.sqlSession.selectList("scheduler.checkCancelAPStatus", today);
    }

    private void insertFeeHist(String chacd, String stateDate, String rcpFee, String bnkFee, String ratio) {
        Map<String, Object> param = Maps.hashmap();
        param.put("CHACD", chacd);
        param.put("FEE_START_DT", stateDate);
        //FIXME 종료일자가 없음..
        param.put("FEE_END_DT", stateDate);
        param.put("RCP_CNT_FEE", rcpFee);
        param.put("RCP_BNK_FEE", bnkFee);
        param.put("RCP_BNK_RATE", ratio);
        sqlSession.insert("scheduler.insertFeeHist", param);
    }

    private boolean findFeeHist(String chacd, String stateDate) {
        Map<String, Object> param = Maps.hashmap();
        param.put("CHACD", chacd);
        param.put("FEE_START_DT", stateDate);
        int count = sqlSession.selectOne("scheduler.findFeeHist", param);
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    private List<Map<String, Object>> findAdjfiRegKey(String chacd) {
        String[] keys = new String[]{"REAL_ACCNO"};
        List<Map<String, Object>> mapList = sqlSession.selectList("scheduler.findAdjfiRegKey", chacd);
        for (Map<String, Object> map : mapList) {
            handleDecryptData(map, keys);
        }
        return mapList;
    }

    /**
     * 다계좌정보 처리..
     *
     * @param maps
     */
    private void handleXadjGroup(List<Map<String, Object>> maps) {
        for (Map<String, Object> map : maps) {
            handleEncryptData(map, new String[]{"REAL_ACCNO"});
            String type = Maps.getValue(map, "TYPE");
            if ("I".equalsIgnoreCase(type)) {
                LOG.info("insertXadjGroup " + map);
                sqlSession.insert("scheduler.insertXadjGroup", map);
            } else if ("U".equalsIgnoreCase(type)) {
                LOG.info("updateXadjGroup " + map);
                sqlSession.update("scheduler.updateXadjGroup", map);
            } else if ("D".equalsIgnoreCase(type)) {
                LOG.info("deleteXadjGroup " + map);
                sqlSession.update("scheduler.deleteXadjGroup", map);
            }
        }
    }

    private Map<String, Object> companyStatusParam(String closeChk, String code, String chacd) {
        Map<String, Object> param = Maps.hashmap();
        param.put("CHA_CLOSE_CHK", closeChk);
        param.put("CHA_CLOSE_ST", code);
        param.put("CHA_CLOSE_VAR_DT", DateUtils.toString(new Date(), "yyyyMMdd"));
        param.put("CHA_CLOSE_VAR_RESON", "");
        param.put("CHACD", chacd);

        return param;
    }

}
