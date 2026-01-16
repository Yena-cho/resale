package kr.co.finger.damoa.scheduler.task;

import kr.co.finger.damoa.commons.Constants;
import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.damoa.model.rcp.ChacdInfo;
import kr.co.finger.damoa.model.rcp.NotiDet;
import kr.co.finger.damoa.model.rcp.NotiMas;
import kr.co.finger.damoa.scheduler.model.CancelBean;
import kr.co.finger.damoa.scheduler.model.NotiDetBean;
import kr.co.finger.damoa.scheduler.model.NoticeMessage;
import kr.co.finger.damoa.scheduler.model.RcpBean;
import kr.co.finger.damoa.scheduler.service.DamoaService;
import kr.co.finger.damoa.scheduler.utils.DamoaBizUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

import static kr.co.finger.damoa.commons.Damoas.makeKey;
import static kr.co.finger.damoa.commons.Damoas.sum;

/**
 * 가상계좌전문 비즈니스 로직 처리
 * 1. 입금(이체)수납처리
 * 2. 입금(취소) 처리.
 */
@Component
public class DamoaMessageTask {

    @Autowired
    private DamoaService damoaService;

    private ScheduledExecutorService scheduledExecutorService;
    private Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * 아직 전문서버에서 입금, 이체, 취소 전부 바로 처리중
     */
//    @Scheduled(fixedDelayString = "${damoa.damoaMessageTask.fixedDelay}")
    public void execute() {

        // 전문테이블 수집함. 10초이전에 처리된 전문테이블 데이터를 수집해 보자.
        String now = DateUtils.toDateString(new Date());
        // 오늘 날짜 처리할 전문 수집..
        List<Map<String, Object>> mapList = findProcessNMsg(now);
        if (mapList == null || mapList.size() == 0) {
            return;
        }

        for (Map<String, Object> map : mapList) {
            try {
                // 시간체크..
                String dealSeqNo = Maps.getValue(map, "DEALSEQNO");
                String nowTime = Maps.getValue(map, "TRANSTIME");
                String msgType = Maps.getValue(map, "TRCODE");

                if (is10SecondPassed(now + nowTime)) {
                    LOG.info("10초가 지난 전문. ");
                    String procYn = Maps.getValue(map, "PROCYN ");
                    // 비즈니스 처리를 수행함.
                    if (isDepositMsg(msgType)) {
                        handleDepositMsg(map,dealSeqNo,now);
                    } else {
                        handleCancelMsg(map,dealSeqNo, now);
                    }

                    updateMsgProcY(dealSeqNo, now);
                    // 처리한 전문은 PROCYN을 Y로 처리함.
                } else {
                    LOG.info("통지된지 아직 10초가 지나지 않은 전문임 " + dealSeqNo);
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }finally {

            }

        }
    }

    private void updateMsgProcY(String dealSeqNo, String now) {
        damoaService.updateMsgProcY(dealSeqNo, now);
    }

    private void handleCancelMsg(Map<String, Object> map,String dealSeqNo,String now) {
        // 취소 처리임...
        LOG.info("입금(이체)통지 취소처리..  " + now + " " + dealSeqNo);
        String msg = Maps.getValue(map, "RULEDATA");
        NoticeMessage noticeMessage = new NoticeMessage();
        noticeMessage.setupMsg(msg.getBytes(Charset.forName("EUC-KR")));
        insertDepositMsgHistory(noticeMessage);

        ///////////////////////////////////// 취소처리..
        String nowDate = DateUtils.toDateString(new Date());
        // 취소 입금(이체)전문 수집..
        CancelBean cancelBean = damoaService.findCancel(dealSeqNo, nowDate);
        if (cancelBean == null) {
            // 이러 경우는 없어야 함...
            LOG.error("취소 처리할 수납건이 없음.."+dealSeqNo,nowDate);
            return;
        }
        LOG.info("취소할 수 있는 정보 " + cancelBean.toString());
        String rcpMasCd = cancelBean.getRcpMasCd();
        String notiMasCd = cancelBean.getNotiMasCd();
        // 수납은 하나임..
        LOG.info("취소처리 수납건.. " + rcpMasCd);
        updateRcpCancel(rcpMasCd);
        // 한 수납건의 데이터 수집
        //RCPMASST 가 완납인 걸 수집..
        List<Map<String, Object>> rcps = damoaService.findRcpCanCelInfo(notiMasCd);
        setupCancelNoti(rcps, notiMasCd);
        LOG.info("취소처리완료 " + rcpMasCd);

        ////////////////////////////////////// 취소처리..
    }

    private void handleDepositMsg(Map<String, Object> map,String dealSeqNo,String now) {
        LOG.info("입금(이체)통지 수납처리..  " + now + " " + dealSeqNo);
        String cancelYn = Maps.getValue(map, "CANCELYN");
        if ("Y".equalsIgnoreCase(cancelYn)) {
            // 취소여부가 Y이므로 수납처리하지 않음.
            LOG.info("취소여부가 Y이므로 수납처리 SKIP " + now + " " + dealSeqNo);
            String msg = Maps.getValue(map, "RULEDATA");
            NoticeMessage noticeMessage = new NoticeMessage();
            noticeMessage.setupMsg(msg.getBytes(Charset.forName("EUC-KR")));
            String msgTypeCode = noticeMessage.getMsgTypeCode().trim();
            String corpCode = noticeMessage.getDepositCorpCode().trim();
            String accountNo = noticeMessage.getDepositAccountNo().trim();
            String amount = noticeMessage.getTransactionAmount().trim();
            String amountOfFee = noticeMessage.getAmountOfFee().trim();

            insertDepositMsgHistory(msgTypeCode, corpCode, accountNo, amount, amountOfFee);
            insertDepositMsgHistory("0400", corpCode, accountNo, amount, amountOfFee);

        } else {
            LOG.info("취소되지 않아 수납처리 수행.. " + now + " " + dealSeqNo);
            String msg = Maps.getValue(map, "RULEDATA");
            NoticeMessage noticeMessage = new NoticeMessage();
            noticeMessage.setupMsg(msg.getBytes(Charset.forName("EUC-KR")));
            String corpCode = noticeMessage.getDepositCorpCode().trim();
            String accountNo = noticeMessage.getDepositAccountNo().trim();
            String amount = noticeMessage.getTransactionAmount().trim();
            String msgSndDate = noticeMessage.getMsgSndDate().trim();
            insertDepositMsgHistory(noticeMessage);

            ChacdInfo chacdInfo = damoaService.findChaSimpleInfo(corpCode);
            List<Map<String, Object>> notiMasDetList = findNotiMasDet(corpCode, accountNo);
            List<NotiMas> notiMasList = divideAmount(notiMasDetList, corpCode, accountNo, amount, msgSndDate, chacdInfo);
            setupRcpMascd(notiMasList);

            handleDeposit(notiMasList, noticeMessage);

        }
    }

    private void insertDepositMsgHistory(NoticeMessage noticeMessage) {
        String corpCode = noticeMessage.getDepositCorpCode().trim();
        String accountNo = noticeMessage.getDepositAccountNo().trim();
        String amount = noticeMessage.getTransactionAmount().trim();
        String msgTypeCode = noticeMessage.getMsgTypeCode().trim();
        String amountOfFee = noticeMessage.getAmountOfFee().trim();
        insertDepositMsgHistory(msgTypeCode, corpCode, accountNo, amount, amountOfFee);
    }
    private boolean isDepositMsg(String msgType) {
        if ("02001100".equalsIgnoreCase(msgType) || "02003110".equalsIgnoreCase(msgType)) {
            return true;
        } else {
            return false;
        }
    }

    @PreDestroy
    private void shutdown() {
        LOG.info("shutdonw... scheduledExecutorService");
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
    }

    private List<Map<String, Object>> findProcessNMsg(String now) {
        try {
            List<Map<String, Object>> mapList = damoaService.findProcessNMsg(now);
            return mapList;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }

    }

    private void handleDeposit(List<NotiMas> notiMasList, NoticeMessage noticeMessage) {
        for (NotiMas notiMas : notiMasList) {
            //NotiMas가 미납이 아닌 경우에만 insert..
            if ("PA02".equals(notiMas.getSt())) {
                LOG.info("청구가 미납이므로 납부 처리하지 않음." + notiMas.getNotimascd());
                continue;
            }
            LOG.info(notiMas.toString());

            String rcpMasCd = notiMas.getRcpMasCd();
            Date now = new Date();
            insertRcpMas(noticeMessage, notiMas, rcpMasCd, now, noticeMessage.getDealSeqNo().trim());
            insertRcpDet(notiMas, rcpMasCd, now);
            if ("03".equalsIgnoreCase(notiMas.getChatrty()) == false) {
                // 기관일 때는 SKIP>...
                insertCashMas(notiMas, rcpMasCd, now);
            }
            updateNotiMas(notiMas);
            updateNotiDet(notiMas);
        }
    }

    private void updateNotiDet(NotiMas notiMas) {
        for (NotiDet notiDet : notiMas.getNotiDets()) {
            damoaService.updateNotidetWithNotidetcd(notiDet.getNotidetcd(), notiDet.getSt());
        }
    }

    private void updateNotiMas(NotiMas notiMas) {
        damoaService.updateNotimas(notiMas.getNotimascd(), notiMas.getSt());
    }

    private String rcpReqType(Map<String, Object> chaCusInfo) {
        return Maps.getValue(chaCusInfo, "RCPREQTY");
    }

    private void insertCashMas(NotiMas notiMas, String rcpMasCd, Date now) {
        Map<String, Object> chaCusInfo = damoaService.findChaCusInfo(notiMas.getVano());
        // 취소처리가 여기까지 오지 않으므로 msgTypeCode를 공백으로 처리해도 무방함..
        String state = notiMas.findRcpMasSt("");
        if ("PA03".equalsIgnoreCase(state)) {
            // 완납에 대해서 현금영수증 처리.
            String type = rcpReqType(chaCusInfo);

            long amount = NumberUtils.toLong(notiMas.findAmount(), 0L);
            if (amount <= 0) {
                // 이때는 skip...
                LOG.info("현금영수증 발행 총금액이 0이므로 현금영수증 처리하지 않음. " + notiMas.getNotimascd());
            } else {
                damoaService.insertCashMaster(notiMas.toInsertCashMasterMap(chaCusInfo, rcpMasCd, now, amount, type));
            }
        } else {
            LOG.info("완납이 아니므로 현금영수증 처리하지 않음. " + notiMas.getNotimascd());
        }
    }


    private void insertRcpDet(NotiMas notiMas, String rcpMasCd, Date now) {
        List<NotiDet> notiDets = notiMas.getNotiDets();
        for (NotiDet notiDet : notiDets) {
            if (notiDet.getAmount() > 0) {
                if (Constants.NO_PAY.equals(notiDet.getSt()) == false) {
                    LOG.info(notiDet.toString());
                    damoaService.insertRcpDetail(notiDet.toInsertRcpDetailMap(rcpMasCd, now, notiMas.getCusoffno(), notiMas.getChatrty()));
                } else {
                    LOG.info("SKIP NO_PAY " + notiDet.getNotmascd() + "_" + notiDet.getNotidetcd());
                }
            } else {
                LOG.info("SKIP 수납 0 " + notiDet.getNotmascd() + "_" + notiDet.getNotidetcd());
            }
        }
    }

    private void insertRcpMas(NoticeMessage noticeMessage, NotiMas notiMas, String rcpMasCd, Date now, String dealSeqNo) {
        damoaService.insertRcpMaster(notiMas.toInsertRcpMasterMap(
                noticeMessage.getDealSeqNo().trim()
                , noticeMessage.getWithdrawalCorpCode()
                , noticeMessage.getWithdrawalAccountName()
                , noticeMessage.getTransactionAmount()
                , noticeMessage.getOccurGubun()
                , noticeMessage.getMediaGubun()
                , noticeMessage.getMsgTypeCode()
                , rcpMasCd
                , "021"
                , now));

    }


    /**
     */
    private void insertDepositMsgHistory(String msgTypeCode, String depositCorpCode, String depositAccountNo, String transactionAmount, String amountOfFee) {
        String type = "";
        if ("0400".equalsIgnoreCase(msgTypeCode)) {
            type = "CNCL";
        } else {
            type = "RCP";
        }
        damoaService.insertDepositMsgHistory(depositCorpCode, depositAccountNo, transactionAmount, amountOfFee, type);

    }

    private String findRcpMasCd() {
        return damoaService.findRcpMasCd();
    }

    private void setupRcpMascd(List<NotiMas> notiMasList) {
        for (NotiMas notiMas : notiMasList) {
            //NotiMas가 미납이 아닌 경우에만 insert..
            if ("PA02".equals(notiMas.getSt())) {
                continue;
            }
            LOG.info(notiMas.toString());
            String rcpMasCd = findRcpMasCd();
            notiMas.setRcpMasCd(rcpMasCd);
        }
    }
    private List<NotiMas> divideAmount(List<Map<String, Object>> mapList, String corpCode, String accountNo, String amount, String msgSndDate, ChacdInfo chacdInfo) {

        Map<String, String> rcpInfos = findSimpleRcpMasDet(corpCode, accountNo);
        List<NotiMas> notiMasList = DamoaBizUtil.aggregate(mapList);
        DamoaBizUtil.aggregate(notiMasList, rcpInfos);
        // 청구데이터에 수납금액 분배.
        DamoaBizUtil.divideAmount(amount, msgSndDate, notiMasList, chacdInfo);
//        DamoaBizUtil.divideAmount(amount, msgSndDate, notiMasList, chacdInfo);
        return notiMasList;
    }


    /**
     * 이전에 수납된 데이터 수집
     *
     * @param corpCode
     * @param accountNo
     * @return
     */
    Map<String, String> findSimpleRcpMasDet(String corpCode, String accountNo) {
        Map<String, String> rcpMap = new HashMap<>();
        List<Map<String, Object>> mapList = damoaService.findSimpleRcpMasDet(corpCode, accountNo);
        if (mapList == null) {
            return rcpMap;
        }
        for (Map<String, Object> map : mapList) {
            String notimas = Maps.findNotimasd(map);
            String notidet = Maps.findNotidetcd(map);
            String amount = Maps.findAmount(map);
            String _key = makeKey(notimas, notidet);
            if (rcpMap.containsKey(_key)) {
                String _amount = rcpMap.get(_key);
                rcpMap.put(_key, sum(amount, _amount));
            } else {
                rcpMap.put(_key, amount);
            }
        }
        return rcpMap;
    }

    private List<Map<String, Object>> findNotiMasDet(String depositCorpCode, String depositAccountNo) {
        try {
            return damoaService.findNotiMasDet(depositCorpCode, depositAccountNo);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 하나의 청구와 여러 수납데이터 처리..
     *
     * @param rcps 여러 수납데이터임..
     */
    private void setupCancelNoti(List<Map<String, Object>> rcps, String notimasCd) {
        //notimas + notidet
        Map<String, RcpBean> rcpBeanMap = aggregate(rcps);
        // 청구 한건 청구상세 여러건
        Map<String, NotiDetBean> notiDetBeanMap = findNotiDetBean(notimasCd);
        LOG.info(" " + rcpBeanMap);

        if (rcpBeanMap.size() == 0) {
            for (String key : notiDetBeanMap.keySet()) {
                NotiDetBean detBean = notiDetBeanMap.get(key);
                detBean.setAfterState("PA02");
                LOG.info(detBean.getBeforeState() + " ==> [PA02]미납 업데이트 .." + notimasCd + " " + detBean.getNotdetCd());
                damoaService.updateNotidetWithNotidetcd(detBean.getNotdetCd(), "PA02");
            }

        } else {
            for (String key : rcpBeanMap.keySet()) {
                RcpBean rcpBean = rcpBeanMap.get(key);
                NotiDetBean detBean = notiDetBeanMap.get(key);
                if (detBean == null) {
                    // 이건 경우는 없음.
                } else {
                    setupCancelNotidet(detBean, rcpBean.getAmount(), rcpBean.getNotdetCd(), rcpBean.getNotimasCd());
                }
            }
        }

        List<NotiDetBean> notiDetBeans = toList(notiDetBeanMap);
        setupCancelNotimas(notiDetBeans, notimasCd);
    }

    private String nvl(String value) {
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }

    private List<NotiDetBean> toList(Map<String, NotiDetBean> notiDetBeanMap) {
        List<NotiDetBean> notiDetBeans = new ArrayList<>();
        for (String key : notiDetBeanMap.keySet()) {
            notiDetBeans.add(notiDetBeanMap.get(key));
        }
        return notiDetBeans;
    }

    /**
     * 청구는 한건. 청구상세 여러건...
     *
     * @param notiDetBeans
     */
    private void setupCancelNotimas(List<NotiDetBean> notiDetBeans, String notimasCd) {
        LOG.info("청구 MAS 취소처리중...");
        String state = checkNotiDet(notiDetBeans);
        LOG.info("청구건 " + notimasCd + " " + notimasState(notiDetBeans) + " == " + state);
        damoaService.updateNotimas(notimasCd, state);
        LOG.info("청구 MAS 취소처리 완료...");
    }

    private String notimasState(List<NotiDetBean> notiDetBeans) {
        return notiDetBeans.get(0).getMasState();
    }

    private String checkNotiDet(List<NotiDetBean> notiDetBeans) {

        // 미납이면
        if (checkPa02(notiDetBeans)) {
            return "PA02";
        }
        // 완납이면
        if (checkPa03(notiDetBeans)) {
            return "PA03";
        }
        //초과납이면
        if (checkPa05(notiDetBeans)) {
            return "PA05";
        }

        return "PA04";
    }


    /**
     * 초과납 체크..
     * 하나라도 초과납이 있으면..
     *
     * @param notiDetBeans
     * @return
     */
    private boolean checkPa05(List<NotiDetBean> notiDetBeans) {
        for (NotiDetBean detBean : notiDetBeans) {
            if (StringUtils.isEmpty(detBean.getAfterState())) {
                String state = detBean.getBeforeState();
                // 완납, 초과납이 아니면...
                if ("PA05".equalsIgnoreCase(state)) {
                    return true;
                }
            } else {
                if ("PA05".equalsIgnoreCase(detBean.getAfterState())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 완납체크.. 모두 완납이면 완납
     *
     * @param notiDetBeans
     * @return
     */
    private boolean checkPa03(List<NotiDetBean> notiDetBeans) {
        for (NotiDetBean detBean : notiDetBeans) {
            if (StringUtils.isEmpty(detBean.getAfterState())) {
                String state = detBean.getBeforeState();
                // 완납
                if ("PA03".equalsIgnoreCase(state) == false) {
                    return false;
                }
            } else {
                if ("PA03".equalsIgnoreCase(detBean.getAfterState()) == false) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 미납 체크.. 모두 미납이면.. 미납..
     *
     * @param notiDetBeans
     * @return
     */
    private boolean checkPa02(List<NotiDetBean> notiDetBeans) {
        for (NotiDetBean detBean : notiDetBeans) {
            if ("PA02".equalsIgnoreCase(detBean.getAfterState()) == false) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param notiDetBean  청구데이터..
     * @param rcpDetAmount 수닙금액
     * @param notidetcd    수납상세cd
     * @param notimasCd    수납cd
     */
    private void setupCancelNotidet(NotiDetBean notiDetBean, Long rcpDetAmount, String notidetcd, String notimasCd) {
        //청구상세
        LOG.info("청구상세 취소처리.. " + notiDetBean.getNotimasCd() + " " + notiDetBean.getNotdetCd() + " " + notiDetBean.getBeforeState() + " " + notiDetBean.getAmount() + " " + rcpDetAmount);
        Long notiDetAmount = notiDetBean.getAmount();
        long aRcpDetAmount = rcpDetAmount.longValue();
        long aNotiDetAmount = notiDetAmount.longValue();
        if (aRcpDetAmount > aNotiDetAmount) {
            LOG.info(notiDetBean.getBeforeState() + " ==> [PA05] 초과납 업데이트 .." + notimasCd + " " + notidetcd);
            damoaService.updateNotidetWithNotidetcd(notidetcd, "PA05");
            notiDetBean.setAfterState("PA05");

        } else if (aRcpDetAmount == aNotiDetAmount) {
            LOG.info(notiDetBean.getBeforeState() + " ==> [PA02]미납 업데이트 .." + notimasCd + " " + notidetcd);
            damoaService.updateNotidetWithNotidetcd(notidetcd, "PA02");
            notiDetBean.setAfterState("PA02");
        } else if (aNotiDetAmount > aRcpDetAmount) {
            LOG.info(notiDetBean.getBeforeState() + " ==>  [PA04]부분납 업데이트 .." + notimasCd + " " + notidetcd);
            damoaService.updateNotidetWithNotidetcd(notidetcd, "PA04");
            notiDetBean.setAfterState("PA04");
        } else if (aRcpDetAmount == 0) {
            //
            LOG.info(notiDetBean.getBeforeState() + " ==> [PA03]완납 업데이트 .." + notimasCd + " " + notidetcd);
            damoaService.updateNotidetWithNotidetcd(notidetcd, "PA03");
            notiDetBean.setAfterState("PA03");
        } else {
            LOG.info("이런 경우는 업어야...");
        }
    }


    /**
     * 한청구 여러 청구항목 정보 수집..
     *
     * @param notimasCd
     * @return
     */
    private Map<String, NotiDetBean> findNotiDetBean(String notimasCd) {
        List<Map<String, Object>> notiMapList = damoaService.findNotiCanCelInfo(notimasCd);
        Map<String, NotiDetBean> detBeans = new HashMap<>();
        for (Map<String, Object> map : notiMapList) {
            /**
             */
            String notidetcd = Maps.getValue(map, "NOTIDETCD");
            String payItemAmt = Maps.getValue(map, "PAYITEMAMT");
            String notiState = Maps.getValue(map, "NOTIMASST");
            String detState = Maps.getValue(map, "NOTIDETST");

            NotiDetBean detBean = new NotiDetBean(notimasCd, notidetcd);
            detBean.setAmount(Long.valueOf(payItemAmt));
            detBean.setBeforeState(detState);
            detBean.setMasState(notiState);
            detBeans.put(detBean.notimasDetCd(), detBean);
        }

        return detBeans;
    }

    /**
     * 청구항목별로 얼마나 수납되었는지 확인..
     *
     * @param rcps 여러 수납데이터..
     * @return
     */
    private Map<String, RcpBean> aggregate(List<Map<String, Object>> rcps) {

        Map<String, RcpBean> notiMasDetMap = new HashMap<>();
        for (Map<String, Object> map : rcps) {
            String notimasCd = Maps.getValue(map, "NOTIMASCD");
            String notidetcd = Maps.getValue(map, "NOTIDETCD");
            String payItemAmt = Maps.getValue(map, "PAYITEMAMT");
            String sveCd = Maps.getValue(map, "SVECD");
            String rcpmasCd = Maps.getValue(map, "RCPMASCD");
            String rcpdetCd = Maps.getValue(map, "RCPDETCD");
            String key = notiDetKey(notimasCd, notidetcd);
            if (notiMasDetMap.containsKey(key)) {
                RcpBean detBean = notiMasDetMap.get(key);
                detBean.setAmount(detBean.getAmount() + Long.valueOf(payItemAmt));
            } else {
                RcpBean rcpBean = new RcpBean(notimasCd, notidetcd);
                rcpBean.setAmount(Long.valueOf(payItemAmt));
                rcpBean.setSveCd(sveCd);
                rcpBean.setRcpmasCd(rcpmasCd);
                rcpBean.setRcpdetCd(rcpdetCd);
                notiMasDetMap.put(key, rcpBean);
            }
        }

        return notiMasDetMap;
    }

    private String notiDetKey(String notimasCd, String notidetcd) {
        return notimasCd + "_" + notidetcd;
    }

    private void updateRcpCancel(String rcpMasCd) {
        damoaService.updateRcpCancel(rcpMasCd);
        damoaService.updateRcpDetCancel(rcpMasCd);
        LOG.info("수납 취소 처리... " + rcpMasCd);
    }

    /**
     * 10초가 지난 전문인지 체크...
     *
     * @param nowDateTime
     * @return
     */
    private boolean is10SecondPassed(String nowDateTime) throws ParseException {

        long now = new Date().getTime();
        Date parsedDate = parse(nowDateTime);
        long parsed = parsedDate.getTime();

        long interval = now - parsed;
        if ((interval) >= 10000) {
            return true;
        } else {
            return false;
        }
    }

    private Date parse(String nowDateTime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return simpleDateFormat.parse(nowDateTime);
    }
}
