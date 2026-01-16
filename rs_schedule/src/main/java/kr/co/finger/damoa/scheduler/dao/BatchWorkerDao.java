package kr.co.finger.damoa.scheduler.dao;

import kr.co.finger.msgio.cash.CashMessage;
import kr.co.finger.msgio.corpinfo.Data;

import java.util.List;
import java.util.Map;



public interface BatchWorkerDao {

    public List<Map<String, Object>> findCashReceipts();
    public List<Map<String,Object>> findNewAutomaticWithdrawal();
    public List<Map<String,Object>> findMonthlyAutomaticWithdrawal(String month);
    public int requestPermission(String startDate,String jobId);

    void updateXCashMasForSending(Map<String, Object> param);
    void updateXCashMasForReceiving(Map<String, Object> param);

    void insertCashReqForSending(Map<String, Object> param);

    void updateCashReq(Map<String, Object> param);

    void insertCashFilePath(String fullFilePath);

    void insertCashReqForSending(CashMessage cashMessage, List<Map<String, Object>> maps);

    List<Map<String,Object>> findUnhandledFilePath();

    void finishUnhandledFilePath(String transDate, String fileName);

    void updateEB14Result(Map<String, Object> param);

    void updateEBI13Result(Map<String, Object> param);

    void updateEB21Result(Map<String, Object> map);

    void updateEB22Result(Map<String, Object> map);

    void finishExecution(String startDate, String workId, int count,boolean isOk);


    List<Map<String,Object>> findProcessNMsg(String date);

    Map<String, Object> findCancel(String dealSeqNo, String nowDate);

    void updateRcpCancel(String rcpMasCd);

    void updateRcpDetCancel(String rcpMasCd);

    List<Map<String,Object>> findRcpCanCelInfo(String notiMasCd);

    void updateNotidetWithNotidetcd(String notdetCd, String state);

    void updateNotimas(String notimasCd, String state);

    List<Map<String,Object>> findNotiCanCelInfo(String notimasCd);

    Map<String,Object> findChaSimpleInfo(String corpCode);

    List<Map<String,Object>> findNotiMasDet(String chacd, String vano);

    List<Map<String,Object>> findSimpleRcpMasDet(String corpCode, String accountNo);

    String findRcpMasCd();

    void insertDepositMsgHistory(String depositCorpCode, String depositAccountNo, String transactionAmount, String amountOfFee, String type);

    void insertRcpMaster(Map<String, Object> map);

    void insertRcpDetail(Map<String, Object> map);

    void insertCashMaster(Map<String, Object> map);

    Map<String,Object> findChaCusInfo(String vano);

    List<Map<String,Object>> findVareq();

    String findBranchCode(String chacd);

    String findChatrty(String chacd);

    void insertValist(String ficd, String vano, String fitxCd, String chacd, String useYn, String varegty,String remark);

    void sendVanoRequest(String ruleMsgNo);
    //가상계좌발급 실패..
    void finishVanoRequestFail(String msgNo);
    // 가상계좌발급과 가상계좌 생성 모두 성공했을때
    void finishVanoRequestOk(String msgNo, String startAccount);
    //성공적으로 가상계좌발급은 받았으나 테이블에 가상계좌를 생성하지 못했을 때...
    void updateReqOkInsertFail(String msgNo, String startAccount);

    void updateMsgProcY(String dealSeqNo, String now);

    List<Map<String,Object>> findAllCorpCode();

    void updateNoChangeCompanyStatus(String chacd);

    void updateFailChangeCompanyStatus(String chacd, String code);

    void updateOkChangeCompanyStatus(String chacd, String code);

    boolean findVano(String vano);

    void insertVareq(String nowString, String chacd, String count);

    void insertChalist(kr.co.finger.msgio.corpinfo.Data data);
//    void updateXCashMasForCancelReceiving(Map<String, Object> param);

    public boolean exist(String chacd);

    void updateChalist(Data data);

    void handleFeeHist(Data data);

    List<Map<String, Object>> checkCancelAPStatus(String today);

    void finishVanoRequestFailRetry(String msgNo);

    List<Map<String,Object>> getRsOrgMonitoredData(String fgCd);
    List<Map<String,Object>> getRsVaMonitoredData(String fgcd);
    String getChacdByChaOffNoAndChaNameFromChaList(Map<String, Object> paramMap);
    String getChacdByChaOffNoAndChaNameFromVaList(String vano);

    void insertMonitoringFileHist(Map<String,Object> paramMap);

    List<Map<String, Object>> selectMonitoringFileList();

    void updateMonitoringFileHist(Map<String,Object> fileMap);

    void insertMonitoringFileHistDet (Map<String,Object> map);


}
