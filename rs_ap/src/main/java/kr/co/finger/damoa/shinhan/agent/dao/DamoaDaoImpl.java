package kr.co.finger.damoa.shinhan.agent.dao;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.damoa.model.msg.AggregateMessage;
import kr.co.finger.damoa.model.msg.NoticeMessage;
import kr.co.finger.shinhandamoa.data.table.model.TransApiRelayData;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 전문서버 DB 처리 Dao 클래스.
 * 여러 데이터를 한번에 저장하는 기능때문에 SqlSession 객체를 이용함.
 */
public class DamoaDaoImpl implements DamoaDao {

    private final SqlSession sqlSession;

    private Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * Instantiates a new Damoa dao.
     *
     * @param sqlSession the sql session
     */
    public DamoaDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public void insertTransDataAcct(Map<String, Object> map) {

        this.sqlSession.insert("insertTransDataAcct", map);
    }


    public String findVirtualAccountName(String vano) {
        return this.sqlSession.selectOne("findVirtualAccountName", vano);
    }


    public long findDealSeq() {
        return this.sqlSession.selectOne("findDealSeq");
    }


    @Override
    public void insertVaReq(Map<String, Object> map) {
        this.sqlSession.insert("inserVaReq", map);
    }

    @Override
    public void updateVaReq(Map<String, Object> map) {
        this.sqlSession.update("updateVaReq", map);
    }

    @Override
    public void insertRcpMaster(Map<String, Object> map) {
        handleEncryptedData(map);
        LOG.info(map.toString());
        this.sqlSession.insert("insertXRcpMas", map);
    }

    private void handleEncryptedData(Map<String, Object> map) {
        encryptData(map, "cushp");
        encryptData(map, "cusoffno");
    }

    private void handleDecryptedData(Map<String, Object> map) {
        decryptData(map, "cushp");
        decryptData(map, "cusoffno");
    }

    private void decryptData(Map<String, Object> map, String key) {
        String value = getValue(map, key);
        map.put(key, value);
//        if (StringUtils.isEmpty(value)) {
//            LOG.debug("[KEY]" + key +"[VALUE]"+value+ "SKIP[Decrypted VALUE]" );
//        } else {
//            String _value = EncryptUtil.decrypt(value);
//            LOG.debug("[KEY]" + key +"[VALUE]"+value+ "[Decrypted VALUE]" + _value);
//            map.put(key, _value);
//        }
    }

    private void encryptData(Map<String, Object> map, String key) {
        String value = Maps.getValue(map, key);
        map.put(key, value);
//        String _value = null;
//        if (StringUtils.isEmpty(value)) {
//            _value = EncryptUtil.encrypt("");
//        } else {
//            _value = EncryptUtil.encrypt(value);
//        }
//        LOG.debug("[KEY]" + key +"[value]"+value+ "[Encrypted VALUE]" + _value);
//        map.put(key, _value);
    }

    private String getValue(Map<String, Object> map, String key) {
        if (map.containsKey(key)) {
            Object o = map.get(key);
            if (o == null) {
                return "";
            }
            if (o instanceof java.sql.Clob) {
                if (o == null) {
                    return "";
                } else {
                    java.sql.Clob clob = (java.sql.Clob) o;
                    return clob.toString();
                }
            } else {
                return o == null ? "" : o.toString();
            }
        } else {
            return "";
        }
    }


    @Override
    public void insertRcpDetail(Map<String, Object> map) {
        handleEncryptedData(map);
        LOG.info(map.toString());
        this.sqlSession.insert("insertXRcpDet", map);
    }

    @Override
    public void insertCashMaster(Map<String, Object> map) {
        handleEncryptedData(map);
        LOG.info(map.toString());
        this.sqlSession.insert("insertXCashMas", map);
    }

    /**
     * 정상처리되지 않은 전문을 전문테이블에 입력..
     *
     * @param typeCode
     * @param dealSeqNo
     * @param destination
     * @param msg
     */
    @Override

    //public void insertTransDataAcct(String typeCode, String dealSeqNo, String destination, String msg) {
    public TransApiRelayData insertTransDataAcct(String typeCode, String dealSeqNo, String destination, String msg) {

        TransApiRelayData transApiRelayData = new TransApiRelayData();

        try {

            Map<String, Object> param = transDataAcctParam(typeCode, dealSeqNo, destination, msg, "X");
            insertTransDataAcct(param);

            // 중계방식 기관일 경우 데이터 처리 이력을 저장하기 위해 사용
            transApiRelayData.setPacketno((String)param.get("PACKETNO"));
            transApiRelayData.setDealseqno((String)param.get("DEALSEQNO"));
            transApiRelayData.setTrcode((String)param.get("TRCODE"));
            transApiRelayData.setDestination((String)param.get("DESTINATION"));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return transApiRelayData;
    }

    /**
     * 정상처리된 전문을 전문테이블에 입력..
     *
     * @param typeCode
     * @param dealSeqNo
     * @param destination
     * @param msg
     */
    @Override
    public void insertOkTransDataAcct(String typeCode, String dealSeqNo, String destination, String msg) {
        try {

            Map<String, Object> param = transDataAcctParam(typeCode, dealSeqNo, destination, msg, "N");
            insertTransDataAcct(param);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void insertCorpCdStatus(String nowDate, String corpCd, String status) {
        Map<String, Object> param = Maps.hashmap();
        param.put("CHACD", corpCd);
        param.put("TRANSDT", nowDate);
        param.put("STATUS", status);
        this.sqlSession.insert("insertCorpCdStatus", param);
    }

    @Override
    public Integer findCorpCd(String nowDate, String corpCd) {
        Map<String, Object> param = Maps.hashmap();
        param.put("CHACD", corpCd);
        param.put("TRANSDT", nowDate);
        return this.sqlSession.selectOne("findCorpCd", param);
    }

    @Override
    public void updateCorpCdStatus(String nowDate, String corpCd, String status) {
        Map<String, Object> param = Maps.hashmap();
        param.put("CHACD", corpCd);
        param.put("TRANSDT", nowDate);
        param.put("STATUS", status);
        this.sqlSession.update("updateCorpCdStatus", param);
    }

    @Override
    public String findCorpCdStatus(String nowDate, String corpCd) {
        Map<String, Object> param = Maps.hashmap();
        param.put("CHACD", corpCd);
        param.put("TRANSDT", nowDate);
        return this.sqlSession.selectOne("findCorpCdStatus", param);
    }

    @Override
    public void updateAllFinishStatus(String now) {
        this.sqlSession.update("updateAllFinishStatus", now);
    }

    @Override
    public Map<String, Object> findChaInfo(String chacd) {
        return this.sqlSession.selectOne("findChaInfo", chacd);
    }

    @Override
    public void insertSmsReq(Map<String, Object> imap) {
        this.sqlSession.selectOne("insertSmsReq", imap);
    }

    @Override
    public List<Map<String, Object>> findSimpleNotiMasDet3(String corpCode, String virtualAccount) {
        return this.sqlSession.selectList("findSimpleNotiMasDet3", param(corpCode, virtualAccount));
    }


    private Map<String, Object> transDataAcctParam(String typeCode, String dealSeqNo, String destination, String msg, String procYn) {
        Map<String, Object> param = new HashMap<>();
        Date now = new Date();
        param.put("TRANSDT", DateUtils.toDateString(now));
        param.put("TRANSTIME", DateUtils.toTimeString(now));
        param.put("TRCODE", typeCode);
        param.put("RULENAME", "ACCT_PAY_SHINHAN");
        param.put("DESTINATION", destination);
        param.put("RULEDATA", msg);
        param.put("DEALSEQNO", dealSeqNo);
        param.put("PACKETNO", DateUtils.toNowString(now));
        param.put("PROCYN", procYn);
        return param;
    }


    @Override
    public List<Map<String, Object>> findAggregateMsgInfo(String corpCd, String startRcpDate, String endRcpDate) {
        Map<String, Object> param = Maps.hashmap();
        param.put("CHACD", corpCd);
        param.put("START_RCPDAY", startRcpDate);
        param.put("END_RCPDAY", endRcpDate);
        return this.sqlSession.selectList("findAggregateMsgInfo", param);
    }

    /**
     * @param corpCd
     * @param vaNo
     * @param depositAmout
     * @param amountOfFee
     * @param type         CANCEL,RCP
     */
    @Override
    public void insertDepositMsgHistory(String nowString, String corpCd, String vaNo, String depositAmout, String amountOfFee, String type,String dealSeqNo) {
        Map<String, Object> param = Maps.hashmap();
        param.put("RCPDAY", nowString);
        param.put("CHACD", corpCd);
        param.put("VANO", vaNo);
        param.put("RCPAMT", Integer.parseInt(depositAmout));
        param.put("FEEAMT", Integer.parseInt(amountOfFee));
        param.put("RCPMASST", type);
        param.put("DEALSEQNO", dealSeqNo);

        this.sqlSession.insert("insertDepositMsgHistory", param);
    }

    @Override
    public String findRcpMasCd() {
        return this.sqlSession.selectOne("findRcpMasCd");
    }

    @Override
    public List<Map<String, Object>> findNotiMasDet3(String chacd, String vano) {
        List<Map<String, Object>> mapList = this.sqlSession.selectList("findNotiMasDet3", param(chacd, vano));

        for (Map<String, Object> map : mapList) {
            handleDecryptedData(map);
        }

        return mapList;
    }

    @Override
    public void removeDepositMsgHistory(NoticeMessage noticeMessage) {
        try {
            Map<String, Object> param = Maps.hashmap();
            String nowDate = nowDate();
            String startRcpDate = startOfRcpDate(nowDate);
            String endRcpDate = endOfRcpDate(nowDate);
            param.put("CHACD", noticeMessage.getDepositCorpCode().trim());
            param.put("START_RCPDAY", startRcpDate);
            param.put("END_RCPDAY", endRcpDate);
            param.put("DEALSEQNO", noticeMessage.getDealSeqNo().trim());

            this.sqlSession.delete("removeDepositMsgHistory", param);
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }

    }

    @Override
    public void deleteCashmas(String rcpMascd) {
        this.sqlSession.delete("deleteCashmas", rcpMascd);
    }

    /**
     * @param rcpDate 8자리 yyyyMMdd
     * @return
     */
    private String startOfRcpDate(String rcpDate) {
        return rcpDate + "000000000";
    }

    private String endOfRcpDate(String rcpDate) {
        return rcpDate + "235959999";
    }


    private String nowDate() {
        return DateUtils.toString(new Date(), "yyyyMMdd");
    }

    @Override
    public Map<String, Object> findChaSimpleInfo(String chacd) {
        return this.sqlSession.selectOne("findChaSimpleInfo", chacd);
    }

    @Override
    public Map<String, Object> findChaCusInfo(String vano) {
        Map<String, Object> map =  this.sqlSession.selectOne("findChaCusInfo", vano);
        if (map == null) {
            return map;
        }
        handleDecryptedData(map);
        return map;
    }

    @Override
    public List<Map<String, Object>> findLastRcp(String chacd, String vano) {
        return this.sqlSession.selectList("findLastRcp", param(chacd, vano));
    }

    @Override
    public void updateNotimas(String notimascd, String state) {
        Map<String, Object> param = Maps.hashmap();
        param.put("NOTIMASCD", notimascd);
        param.put("NOTIMASST", state);
        this.sqlSession.update("updateNotimas", param);
    }

    @Override
    public void updateRcpMas(String rcpmascd, String state) {
        Map<String, Object> param = Maps.hashmap();
        param.put("RCPMASCD", rcpmascd);
        param.put("RCPMASST", state);
        this.sqlSession.update("updateRcpMas", param);
    }

    @Override
    public void updateCashCancel(String rcpmasd) {
        this.sqlSession.update("updateCashCancel", rcpmasd);
    }

    @Override
    public void updateNotidetWithNotidetcd(String notidetcd, String st) {
        Map<String, Object> param = Maps.hashmap();
        param.put("NOTIDETCD", notidetcd);
        param.put("NOTIDETST", st);
        this.sqlSession.update("updateNotidetWithNotidetcd", param);
    }


    @Override
    public void updateRcpDet(String rcpmasd, String st) {

    }

    @Override
    public void insertRcpSum(AggregateMessage aggregateMessage) throws ParseException {
        this.sqlSession.insert("insertRcpSum", aggregateMessage.insertParam());
    }

    /**
     * 집계대사는 테이블에 하나만 입력하므로 selectOne 문제가 되지 않음.
     *
     * @param aggregateMessage
     * @return
     * @throws ParseException
     */
    @Override
    public Map<String, Object> findRcpSum(AggregateMessage aggregateMessage) throws ParseException {
        return this.sqlSession.selectOne("findRcpSum", aggregateMessage.selectParam());
    }


    @Override
    public List<Map<String, Object>> containsSeqNo(String dealSeqNo, String nowDate) {
        Map<String, Object> param = Maps.hashmap();
        param.put("TRANSDT", nowDate);
        param.put("DEALSEQNO", dealSeqNo);

        return this.sqlSession.selectList("containsSeqNo", param);
    }

    @Override
    public List<Map<String, Object>> containsCanCel(String dealSeqNo, String nowDate, String msgTypeCode) {
        Map<String, Object> param = Maps.hashmap();
        param.put("TRANSDT", nowDate);
        param.put("DEALSEQNO", dealSeqNo);
        param.put("TRCODE", msgTypeCode);

        return this.sqlSession.selectList("containsCanCel", param);
    }

    @Override
    public int countTransDataAcct(String transdt, String trcode, String dealSeqNo) {
        final Map<String, String> param = new HashMap();
        param.put("TRANSDT", transdt);
        param.put("DEALSEQNO", dealSeqNo);
        param.put("TRCODE", trcode);

        return this.sqlSession.selectOne("countTransDataAcct", param);
    }
    /**
     * 가상계좌번호 수집..
     *
     * @param corpCode
     * @param accountNo
     * @return
     */
    @Override
    public Map<String, Object> findAccountNo(String corpCode, String accountNo) {
        Map<String, Object> param = Maps.hashmap();
        param.put("CHACD", corpCode);
        param.put("VANO", accountNo);
        return this.sqlSession.selectOne("findAccountNo", param);
    }

    @Override
    public List<Map<String, Object>> containsSeqNoAtCancel(String dealSeqNo, String nowDate, String typeCode) {
        Map<String, Object> param = Maps.hashmap();
        param.put("TRANSDT", nowDate);
        param.put("DEALSEQNO", dealSeqNo);
        param.put("TRCODE", typeCode);
        return this.sqlSession.selectList("containsSeqNoAtCancel", param);
    }

    @Override
    public void updateRcpCancel(String rcpMasCd) {
        this.sqlSession.update("updateRcpCancel", rcpMasCd);
    }

    @Override
    public void updateRcpDetCancel(String rcpMasCd) {
        this.sqlSession.update("updateRcpDetCancel", rcpMasCd);
    }


    @Override
    public List<Map<String, Object>> findRcpCanCelInfo(String rcpMasCd) {
        return this.sqlSession.selectList("findRcpCanCelInfo", rcpMasCd);
    }

    @Override
    public List<Map<String, Object>> findNotiCanCelInfo(String notimasCd) {
        return this.sqlSession.selectList("findNotiCanCelInfo", notimasCd);
    }

    @Override
    public List<String> findOriginalMsg(String dealSeqNo, String typeCode) {
        Map<String, Object> param = Maps.hashmap();
        param.put("TRANSDT", DateUtils.toDateString(new Date()));
        param.put("TRCODE", typeCode);
        param.put("DEALSEQNO", dealSeqNo);

        return this.sqlSession.selectList("findOriginalMsg", param);
    }

    @Override
    public List<Map<String, Object>> findCancel(String dealSeqNo, String payDay) {
        Map<String, Object> map = Maps.hashmap();
        map.put("DEALSEQNO", dealSeqNo);
        map.put("PAYDAY", payDay);

        return this.sqlSession.selectList("findCancel", map);
    }


    private Map<String, Object> param(String chacd, String vano) {
        Map<String, Object> param = Maps.hashmap();
        param.put("CHACD", chacd);
        param.put("VANO", vano);
        return param;
    }

    @Override
    public Map<String, Object> findAccountInfo(String fgcd, String accountNo) {
        Map<String, Object> map = Maps.hashmap();
        map.put("fgcd", fgcd);
        map.put("accountNo", accountNo);

        return this.sqlSession.selectOne("findAccountInfo", map);
    }

    @Override
    public Map<String, Object> getLimitPerVano(String vano) {
        Map<String, Object> map = Maps.hashmap();
        map.put("vano", vano);

        return this.sqlSession.selectOne("getLimitPerVano", map);
    }

}
