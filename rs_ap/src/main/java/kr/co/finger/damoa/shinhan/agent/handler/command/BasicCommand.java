package kr.co.finger.damoa.shinhan.agent.handler.command;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.damoa.model.msg.Code;
import kr.co.finger.damoa.model.msg.CommonMessage;
import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.model.rcp.ChacdInfo;
import kr.co.finger.damoa.model.rcp.NotiMas;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.dao.DamoaDao;
import kr.co.finger.damoa.shinhan.agent.handler.DamoaException;
import kr.co.finger.damoa.shinhan.agent.handler.command.validation.Validator;
import kr.co.finger.damoa.shinhan.agent.handler.command.validation.ValidatorFactory;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import kr.co.finger.msgagent.command.Command;
import kr.co.finger.msgagent.layout.MessageFactory;
import kr.co.finger.shinhandamoa.common.StringByteUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class BasicCommand implements Command {
    
    @Override
    public boolean executeWithPush(MsgIF request, MsgIF response) throws Exception {
        return false;
    }

    String getMsgType(MsgIF msgIF) {
        return msgIF.getMsgTypeCode() + msgIF.getDealTypeCode();
    }

    Code createByMsgIF(MsgIF msgIF) {
        return Code.createByMsgIF(msgIF);
    }

    MsgIF response(MsgIF msgIF) throws CloneNotSupportedException {
        MsgIF response = (MsgIF) msgIF.clone();
        response.response();
        return response;
    }

    MsgIF clone(MsgIF msgIF) throws CloneNotSupportedException {
        MsgIF response = (MsgIF) msgIF.clone();
        return response;
    }

    /**
     * 여기서 문제가 발생하면 응답을 줄 수 있는 방법이 없다.
     *
     * @param messageFactory
     * @param msgIF
     * @return
     * @throws Exception
     */
    String encode(MessageFactory messageFactory, MsgIF msgIF) throws Exception {
        return messageFactory.encode(msgIF);
    }

    String encode(MessageFactory messageFactory, MsgIF msgIF, String type) throws Exception {
        return messageFactory.encode(msgIF, type);
    }

    String split(String name) {
        Charset charset = Charset.forName("EUC-KR");

        return StringByteUtils.left(name, charset, 20);
    }

    /**
     * 기본적으로 전문테이블에 저장함.
     *
     * @param typeCode
     * @param dealSeqNo
     * @param destination
     * @param msg
     */
    void insertTransDataAcct(DamoaDao damoaDao, String typeCode, String dealSeqNo, String destination, String msg, Logger LOG) {
        damoaDao.insertTransDataAcct(typeCode, dealSeqNo, destination, msg);
    }

    void insertOkTransDataAcct(DamoaDao damoaDao, String typeCode, String dealSeqNo, String destination, String msg, Logger LOG) {
        damoaDao.insertOkTransDataAcct(typeCode, dealSeqNo, destination, msg);
    }

    /**
     * 기본적인 기관정보를 수집하여 객체로 반환함.
     * 다만 쿼리되는 모든 항목을 처리하지는 않음에 유믜바람..
     * 다른 곳에서 같은 쿼리를 사용하여 데이터 처리하니.. 항목을 추가하는 것은 괜찮으나 삭제는 곤란함.
     * @param damoaDao
     * @param corpCode
     * @return
     */
    ChacdInfo findChaSimpleInfo(DamoaDao damoaDao, String corpCode) {
        Map<String, Object> result = damoaDao.findChaSimpleInfo(corpCode);
        if (result == null) {
            return null;
        }

        boolean doCheckDate = doCheck(result, "rcp_due_chk");
        boolean doCheckAmount = doCheck(result, "amtchkty");
        String chast = Maps.getValue(result, "chast");
        String chatrty = Maps.getValue(result, "chatrty");
        String adjaccYn = Maps.getValue(result, "adjaccyn");

        ChacdInfo chacdInfo = new ChacdInfo(doCheckDate, doCheckAmount, chast, chatrty, adjaccYn);
        chacdInfo.setChaName(Maps.getValue(result, "chaname"));
        chacdInfo.setCusNameTy(Maps.getValue(result, "cusnamety"));
        return chacdInfo;
    }

    private boolean doCheck(Map<String, Object> result, String key) {
        String doCheck = Maps.getValue(result, key);
        if ("Y".equalsIgnoreCase(doCheck)) {
            return true;
        } else {
            return false;
        }
    }


//    /**
//     * 이전에 수납된 데이터 수집
//     *
//     * @param corpCode
//     * @param accountNo
//     * @return
//     */
//    Map<String, String> findSimpleRcpMasDet(DamoaDao damoaDao, String corpCode, String accountNo) {
//        Map<String, String> rcpMap = new HashMap<>();
//        List<Map<String, Object>> mapList = damoaDao.findSimpleRcpMasDet(corpCode, accountNo);
//        if (mapList == null) {
//            return rcpMap;
//        }
//        for (Map<String, Object> map : mapList) {
//            String notimas = Maps.findNotimasd(map);
//            String notidet = Maps.findNotidetcd(map);
//            String amount = Maps.findAmount(map);
//            String _key = makeKey(notimas, notidet);
//            if (rcpMap.containsKey(_key)) {
//                String _amount = rcpMap.get(_key);
//                rcpMap.put(_key, sum(amount, _amount));
//            } else {
//                rcpMap.put(_key, amount);
//            }
//        }
//        return rcpMap;
//    }


    /**
     * @param commonMessage
     * @param messageFactory
     * @return 정상은 0
     * @throws DamoaException
     */
    ResultBean validateCommons(CommonMessage commonMessage, MessageFactory messageFactory, DamoaContext damoaContext, Logger LOG) throws DamoaException {
        ResultBean result = validateCommon(messageFactory, commonMessage, "MsgCorCode", damoaContext, LOG);
        if (isFail(result)) {
            return result;
        }
        result = validateCommon(messageFactory, commonMessage, "SndRcvFlag", damoaContext, LOG);
        if (isFail(result)) {
            return result;
        }
        result = validateCommon(messageFactory, commonMessage, "MsgTypeCode", damoaContext, LOG);
        if (isFail(result)) {
            return result;
        }
        result = validateCommon(messageFactory, commonMessage, "DealTypeCode", damoaContext, LOG);
        if (isFail(result)) {
            return result;
        }
        result = validateCommon(messageFactory, commonMessage, "DealSeqNo", damoaContext, LOG);
        if (isFail(result)) {
            return result;
        }
        result = validateCommon(messageFactory, commonMessage, "MsgSndDate", damoaContext, LOG);
        if (isFail(result)) {
            return result;
        }
        result = validateCommon(messageFactory, commonMessage, "DeadlineYn", damoaContext, LOG);
        if (isFail(result)) {
            return result;
        }
        result = validateCommon(messageFactory, commonMessage, "OccurGubun", damoaContext, LOG);
        if (isFail(result)) {
            return result;
        }
        result = validateCommon(messageFactory, commonMessage, "MediaGubun", damoaContext, LOG);
        if (isFail(result)) {
            return result;
        }

        return result;
    }


    ResultBean validateCommon(MessageFactory messageFactory, MsgIF commonMessage, String key, DamoaContext damoaContext, Logger LOG) throws DamoaException {
        LOG.debug("[KEY]" + key + " 정합성 체크중....");
        Validator validator = ValidatorFactory.getInstance().getValidator(key);

        return validator.validate(commonMessage, damoaContext);
    }


    private ResultBean handleValidate(MessageFactory messageFactory, MsgIF commonMessage, ResultBean result, String key) throws DamoaException {
        if (isFail(result)) {
            int index = messageFactory.findFormatIndex(commonMessage, key);
            index = index + 1;
            if (10000 == index) {
                throw new DamoaException("V785", "포맷Index를 찾을 수 없음. " + key);
            } else {
                return new ResultBean(result + leftPad(index), "");
            }
        } else {
            return result;
        }
    }


    ResultBean validateEtc(MessageFactory messageFactory, MsgIF commonMessage, String key, DamoaContext damoaContext, Logger LOG) throws DamoaException {
        LOG.debug("[KEY]" + key + " 정합성 체크중....");
        Validator validator = ValidatorFactory.getInstance().getValidator(key);
        return validator.validate(commonMessage, damoaContext, commonMessage.getMsgTypeCode() + commonMessage.getDealTypeCode());
    }

    String leftPad(int index) {
        return StringUtils.leftPad(index + "", 3, "0");
    }

    boolean isFail(ResultBean result) {

        if ("0".equalsIgnoreCase(result.getCode())) {
            return false;
        } else {
            return true;
        }
    }

    public ResultBean validate(MsgIF message, MessageFactory messageFactory, DamoaContext damoaContext, Logger LOG) throws DamoaException {
        ResultBean result = validateEtc(messageFactory, message, "ResaleCorpCd", damoaContext, LOG);
        if (isFail(result)) {
            return result;
        }
//        ResultBean result = validateEtc(messageFactory, message, "DepositCorpCode", damoaContext, LOG);
//        if (isFail(result)) {
//            return result;
//        }
//
//        result = validateEtc(messageFactory, message, "DepositAccountNo", damoaContext, LOG);
//        if (isFail(result)) {
//            return result;
//        }

        return new ResultBean("0", "");
    }

    public ResultBean validateNotice(MsgIF message, MessageFactory messageFactory, DamoaContext damoaContext, Logger LOG) throws DamoaException {
        ResultBean result = validateEtc(messageFactory, message, "TransactionAmount", damoaContext, LOG);
        if (isFail(result)) {
            return result;
        }
        return new ResultBean("0", "");
    }

    public ResultBean validateAdmin(MsgIF message, MessageFactory messageFactory, DamoaContext damoaContext, Logger LOG) throws DamoaException {
        ResultBean result = validateEtc(messageFactory, message, "DealDate", damoaContext, LOG);
        if (isFail(result)) {
            return result;
        }
        result = validateEtc(messageFactory, message, "AdminInfo", damoaContext, LOG);
        if (isFail(result)) {
            return result;
        }
        result = validateEtc(messageFactory, message, "InstitutionCode", damoaContext, LOG);
        if (isFail(result)) {
            return result;
        }
        return new ResultBean("0", "");
    }

    public ResultBean validateAggregate(MsgIF message, MessageFactory messageFactory, DamoaContext damoaContext, Logger LOG) throws DamoaException {
        ResultBean result = validateEtc(messageFactory, message, "AggregateDealDate", damoaContext, LOG);
        if (isFail(result)) {
            return result;
        }
        //20008153,20008155 만 신한에 등록되어있으므로 집계대사는 두 기관만 진행
        result = validateEtc(messageFactory, message, "CorpCode", damoaContext, LOG);
        if (isFail(result)) {
            return result;
        }
        return new ResultBean("0", "");
    }

    public List<Map<String, Object>> filterForCheckNoAmountAndPeriod(List<Map<String, Object>> mapList, ChacdInfo chacdInfo, Logger LOG) {
        List<Map<String, Object>> maps = new ArrayList<>();
        if (mapList == null || mapList.size() == 0) {
            return maps;
        } else {
            if (chacdInfo.isDoAmountCheck() == false && chacdInfo.isDoDateCheck() == true) {
                for (Map<String, Object> map : mapList) {
                    String startdate = Maps.getValue(map, "STARTDATE");
                    String enddate = Maps.getValue(map, "ENDDATE");
                    String date = DateUtils.toDateString(new Date());
                    if (date.compareTo(startdate) >= 0 && enddate.compareTo(date) >= 0) {
                        maps.add(map);
                    } else {
                        LOG.info("FILTER.. " + map);
                        //FILTER...
                    }
                }
                return maps;
            } else {
                return mapList;
            }

        }

    }

    public String findNameInMapList(List<Map<String, Object>> notimasDets) {
        if (notimasDets == null || notimasDets.size() == 0) {
            return "";
        } else {
            String name = findName(notimasDets.get(0));
            if (StringUtils.isEmpty(name.trim())) {
                return "";
            }

            if (hasAllSameNameInMapList(notimasDets, name)) {
                return name;
            } else {
                return "";
            }
        }
    }

    public String findName(List<NotiMas> notiMasList) {
        if (notiMasList == null || notiMasList.size() == 0) {
            return "";
        } else {
            String name = findName(notiMasList.get(0));
            if (StringUtils.isEmpty(name.trim())) {
                return "";
            }
            if (hasAllSameName(notiMasList, name)) {
                return name;
            } else {
                return "";
            }
        }
    }

    private String findName(NotiMas notiMas) {
        return notiMas.getCusname();
    }

    private String findName(Map<String, Object> map) {
        return Maps.getValue(map, "CUSNAME");
    }

    private boolean hasAllSameNameInMapList(List<Map<String, Object>> notimasDets, String name) {

        for (Map<String, Object> map : notimasDets) {
            String _name = Maps.getValue(map, "CUSNAME");
            if (name.trim().equals(_name.trim()) == false) {
                return false;
            }
        }

        return true;
    }

    private boolean hasAllSameName(List<NotiMas> notiMasList, String name) {

        for (NotiMas notiMas : notiMasList) {
            String _name = notiMas.getCusname();
            if (name.trim().equals(_name.trim()) == false) {
                return false;
            }
        }

        return true;
    }
}
