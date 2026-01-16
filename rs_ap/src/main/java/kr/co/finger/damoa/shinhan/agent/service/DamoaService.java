package kr.co.finger.damoa.shinhan.agent.service;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.damoa.model.msg.NoticeMessage;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.dao.DamoaDao;
import kr.co.finger.damoa.shinhan.agent.domain.model.*;
import kr.co.finger.damoa.shinhan.agent.domain.repository.CashReceiptRepository;
import kr.co.finger.damoa.shinhan.agent.domain.repository.NoticeMasterRepository;
import kr.co.finger.damoa.shinhan.agent.model.CancelMessageBean;
import kr.co.finger.damoa.shinhan.agent.model.NotiDetBean;
import kr.co.finger.damoa.shinhan.agent.model.NoticeMessageBean;
import kr.co.finger.damoa.shinhan.agent.model.RcpBean;
import kr.co.finger.damoa.shinhan.agent.util.FingerIntegrateMessaging;
import kr.co.finger.damoa.shinhan.agent.util.FingerTalk;
import kr.co.finger.damoa.shinhan.agent.util.ListResult;
import kr.co.finger.shinhandamoa.data.table.mapper.*;
import kr.co.finger.shinhandamoa.data.table.model.Atreq;
import kr.co.finger.shinhandamoa.data.table.model.Smsreq;
import kr.co.finger.shinhandamoa.data.table.model.Xchalist;
import kr.co.finger.shinhandamoa.data.table.model.Xcusmas;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 전문 관련 비즈니스 처리
 *
 * @author lloydkwon@gmail.com
 * @author wisehouse@finger.co.kr
 */
@Service
public class DamoaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DamoaService.class);

    @Value("${damoa.shinhanCode}")
    private String shinhanCode;

    @Autowired
    private DamoaContext damoaContext;

    @Autowired
    private DamoaDao damoaDao;

    @Autowired
    private FingerIntegrateMessaging fingerIntegrateMessaging;

    @Autowired
    private XcusmasMapper xcusmasMapper;

    @Autowired
    private XchalistMapper xchalistMapper;

    @Autowired
    private SmsreqMapper smsreqMapper;

    @Autowired
    private NoticeMasterRepository noticeMasterRepository;

    @Autowired
    private CashReceiptRepository cashReceiptRepository;

    @Autowired
    private FingerTalk fingerTalk;

    @Autowired
    private AtreqMapper atreqMapper;

    /**
     * 입금 통지 비동기 작업
     *
     * @param messageBean 전문데이터를 가지고 있음.
     */
    @Transactional
    public void handleDeposit(NoticeMessageBean messageBean) {
        LOGGER.info("handleDeposit " + messageBean);

        final NoticeMessage noticeMessage = messageBean.getNoticeMessage();
        final String dealSeqNo = noticeMessage.getDealSeqNo().trim();
        // 통지요청일 때
        String date = DateUtils.toDateString(new Date());
        if (damoaContext.hasCancelRequest(dealSeqNo, date)) {
            LOGGER.error("취소요청이 들어와 수납처리를 하지 않음..[시간]" + date + "[거래일련번호]" + dealSeqNo);
        } else {
            LOGGER.info("수납처리: {}", dealSeqNo);
            handleDeposit(messageBean.getNoticeMessage(), messageBean.getCha(), messageBean.getCustomer(), messageBean.getNoticeMasterList(), messageBean.getReceiptMasterList());
        }
    }

    /**
     * 수납처리<br />
     * <p>
     * 수납원장은 생성했으므로 수납원장 기준으로 청구원장을 변경한다
     *
     * @param noticeMessage
     * @param cha
     * @param customer
     * @param noticeMasterList
     */
    private void handleDeposit(NoticeMessage noticeMessage, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList, List<ReceiptMasterDO> receiptMasterList) {
        // 1. 청구에 대한 수납처리
        try {
            for (ReceiptMasterDO each : receiptMasterList) {
                if (each.getNoticeMaster() == null) {
                    continue;
                }

                LOGGER.info("청구원장[{}]: 수납처리", each.getNoticeMaster().getId());
                noticeMasterRepository.updateNotice(each.getNoticeMaster());

                NoticeMasterDO noticeMaster = noticeMasterRepository.findById(each.getNoticeMaster().getId());
                noticeMaster.refreshStatus();
                noticeMasterRepository.updateNotice(noticeMaster);
            }
        } catch (Exception e) {
            sendMessage("수납 처리 중 오류");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());
            }
        }

        // 2. 현금영수증 처리
        try {
            if(cha.isEnableCashReceipt()) {
                for (ReceiptMasterDO each : receiptMasterList) {
                    LOGGER.info("수납원장[{}]: 현금영수증 생성");

                    final List<CashReceiptDO> cashReceiptList = CashReceiptDO.createWithReceiptMaster(cha, customer, each);

                    for (CashReceiptDO cashReceipt : cashReceiptList) {
                        if (cha.isEnableAutomaticCashReceipt() && StringUtils.isNotBlank(cashReceipt.getIdentityNo()) && cashReceipt.setTotalAmount() > 0) {
                            LOGGER.info("수납원장[{}]: 현금영수증 자동발행", each.getId());
                            cashReceipt.issue();
                        } else {
                            LOGGER.info("수납원장[{}]: 현금영수증 수동발행", each.getId());
                        }

                        cashReceiptRepository.createCashReceipt(cashReceipt);
                    }
                }
            }
        } catch (Exception e) {
            sendMessage("현금영수증 생성 중 오류");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());
            }
        }

        String atMsgContent = "[신한 다모아]<br>"
                            + "[#기관명#] 입금이 확인 되었습니다.<br><br>"
                            + "고객명 : [#고객명#]<br>"
                            + "입금금액 : [#입금액#]<br>"
                            + "납부계좌 : [#납부계좌번호#]<br>"
                            + "기관전화번호 : [#기관대표전화번호#]<br><br>"
                            + "※ 신한 다모아 홈페이지 안내(모바일가능)<br>"
                            + "http://www.shinhandamoa.com";

        // 3. 문자 메시지 발송
        switch (receiptMasterList.size()) {
            case 1:
                ReceiptMasterDO receiptMaster = receiptMasterList.get(0);
                handleSms(noticeMessage, receiptMaster.getPaymentMonth(), receiptMaster.getNoticeMasterId(), "");
                handleAt(noticeMessage, receiptMaster.getPaymentMonth(), receiptMaster.getNoticeMasterId(), atMsgContent);
                break;
            default:
                String msg = "[$기관명] [#고객명#] [#청구액#]원 입금처리 완료되었습니다";
                handleSms(noticeMessage, "", "", msg);
                handleAt(noticeMessage, "", "", atMsgContent);
                break;
        }
    }
    
    private void sendMessage(String message) {
        fingerIntegrateMessaging.createMessage(FingerIntegrateMessaging.MessageType.SMS, "신한다모아 전문서버 오류알림", message, null, "15449350", "01098163323", "정빛나"); // sms 발송
        fingerIntegrateMessaging.createMessage(FingerIntegrateMessaging.MessageType.SMS, "신한다모아 전문서버 오류알림", message, null, "15449350", "01090421391", "임연주"); // sms 발송
        fingerIntegrateMessaging.createMessage(FingerIntegrateMessaging.MessageType.SMS, "신한다모아 전문서버 오류알림", message, null, "15449350", "01089225008", "신재현"); // sms 발송

    }

    /**
     * 예외처리하여 수납처리 트랜잭션에 영향이 가지 않게 함.
     */
    private void handleSms(NoticeMessage noticeMessage, String masMonth, String notiMasCd, String msg) {
        LOGGER.info("SMS 처리...");

        try {
            //
            final String virtualAccountNo = StringUtils.trim(noticeMessage.getDepositAccountNo());
            final Xcusmas xcusmas = (Xcusmas) xcusmasMapper.selectByPrimaryKey(virtualAccountNo);

            if (xcusmas == null) {
                LOGGER.info("가상계좌번호에 대한 사용자 정보가 없음.. 가상계좌번호=" + virtualAccountNo);
                return;
            }

            final Xchalist xchalist = (Xchalist) xchalistMapper.selectByPrimaryKey(xcusmas.getChacd());

            final String smsYn = xchalist.getRcpsmsyn();
            final String smsType = xchalist.getRcpsmsty();
            if (!(StringUtils.equals(smsYn, "Y") && StringUtils.equals(smsType, "A"))) {
                LOGGER.debug("입금 문자 메시지 자동 발송 기관 아닙니다.");
                return;
            }

            LOGGER.debug("문자 메시지 자동 발송 기관");
            // 고객전화번호
            final String hpNo = xcusmas.getCushp();
            // 기관전화번호
            final String telNo = xchalist.getSmsSendTel();
            // 기관명
            final String writer = xchalist.getChaname();
            // 고객명
            final String cusName = xcusmas.getCusname();
            // 거래 금액
            String amount = StringUtils.trim(noticeMessage.getTransactionAmount());
            amount = deleteZero(amount);

            LOGGER.debug("고객전화번호: {}", hpNo);
            LOGGER.debug("기관전화번호: {}", telNo);
            LOGGER.debug("고객명: {}", cusName);

            if (StringUtils.isBlank(hpNo)) {
                LOGGER.warn("고객전화번호가 없어 SMS 보내지 않음..  가상계좌번호=" + noticeMessage.getDepositAccountNo().trim());
                return;
            }

            if (StringUtils.isEmpty(msg)) {
                msg = xchalist.getRcpsms();
            }

            if (StringUtils.isEmpty(msg)) {
                LOGGER.debug("문자메시지 내용 없음");
                return;
            }
            msg = rcpSmsMsg(msg, cusName, amount, virtualAccountNo, masMonth, writer);
            LOGGER.debug("문자메시지 생성 완료");

            final String chacd = xchalist.getChacd();
            final int msgLength = msg.getBytes(Charset.forName("EUC-KR")).length;

            final FingerIntegrateMessaging.MessageType messageType;
            final String type;
            if (msgLength > 80) {
                messageType = FingerIntegrateMessaging.MessageType.MMS;
                type = "1";
                LOGGER.debug("문자 메시지 종류 : MMS");
            } else {
                messageType = FingerIntegrateMessaging.MessageType.SMS;
                type = "0";
                LOGGER.debug("문자 메시지 종류 : SMS");
            }

            final ListResult<FingerIntegrateMessaging.Message> listResult = fingerIntegrateMessaging.createMessage(
                    messageType, "", msg, null, telNo, hpNo, cusName); // sms 발송

            if (listResult != null && listResult.getItemCount() > 0) {
                final List<FingerIntegrateMessaging.Message> itemList = listResult.getItemList();
                final FingerIntegrateMessaging.Message message = itemList.get(0);
                final long messageId = message.getId();

                final Smsreq smsreq = new Smsreq();
                smsreq.setSmsreqcd(String.valueOf(messageId));
                smsreq.setChacd(chacd);
                smsreq.setFicd("088");            // 은행코드
                smsreq.setPhone(hpNo);
                smsreq.setCallback(telNo);
                smsreq.setStatus("0");
                smsreq.setRslt("");
                smsreq.setMsg(msg);
                smsreq.setType(type);
                smsreq.setSendcnt(0);
                smsreq.setSmsreqst("");
                // 가맹점접속형태 Web(01), 전문(03)
                smsreq.setChatrty("03");
                smsreq.setNotimascd(notiMasCd);
                smsreq.setReqdate(new Date());
                smsreq.setRegdt(new Date());

                smsreqMapper.insert(smsreq);
            }
            // SMS 처리함.
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());
            }

        }
    }

    /**
     * 알림톡 발송 예외처리하여 수납처리 트랜잭션에 영향이 가지 않게 함.
     */
    private void handleAt(NoticeMessage noticeMessage, String masMonth, String notiMasCd, String atMsgContent) {
        LOGGER.info("알림톡 발송 예외 처리");

        try {
            final String virtualAccountNo = StringUtils.trim(noticeMessage.getDepositAccountNo());
            final Xcusmas xcusmas = (Xcusmas) xcusmasMapper.selectByPrimaryKey(virtualAccountNo);

            if (xcusmas == null) {
                LOGGER.info("가상계좌번호에 대한 사용자 정보가 없음.. 가상계좌번호=" + virtualAccountNo);
                return;
            }

            final Xchalist xchalist = (Xchalist) xchalistMapper.selectByPrimaryKey(xcusmas.getChacd());

            final String atYn = xchalist.getAtyn();
            final String rcpatYn = xchalist.getRcpatyn();

            // atYn : Y 알림톡 사용, N 알림톡 미사용 | rcpatYn : A 자동발송, M 수동발송, N 미사용
            if (!(StringUtils.equals(atYn, "Y") && StringUtils.equals(rcpatYn, "A"))) {
                LOGGER.debug("입금 시 알림톡 자동 발송 기관 아님");
                return;
            }

            LOGGER.debug("입금 시 알림톡 자동 발송 기관");

            // 기관명
            final String writer;
            final String chaName = xchalist.getChaname();
            final String atChaName = xchalist.getAtchaname();

            LOGGER.debug("다모아에 등록된 기관명 chaName : {}", chaName);
            LOGGER.debug("알림톡 발송 시 사용할 기관명 atChaName : {}", atChaName);

            if ("".equals(atChaName) || atChaName == null) {
                writer = chaName;
            } else {
                writer = atChaName;
            }
            LOGGER.debug("실제 알림톡 발송에 사용될 기관명 writer : {}", writer);

            // 고객명
            final String cusName = xcusmas.getCusname();

            // 입금 금액
            String amount = StringUtils.trim(noticeMessage.getTransactionAmount());

            amount = deleteZero(amount);

            // 기관전화번호
            final String chaTelNo = xchalist.getOwnertel();
            LOGGER.debug("알림톡을 발송하는 기관전화번호 : {}", chaTelNo);

            // 고객전화번호
            final String hpNo = xcusmas.getCushp();
            LOGGER.debug("알림톡을 받을 고객 전화번호 : {}", hpNo);

            if (StringUtils.isBlank(hpNo)) {
                LOGGER.warn("고객전화번호가 없어 알림톡을 보내지 않음.  가상계좌번호=" + noticeMessage.getDepositAccountNo().trim());
                return;
            }

            if (StringUtils.isEmpty(atMsgContent)) {
                LOGGER.debug("알림톡 내용 없음");
                return;
            }

            // 입금 알림톡 메시지 내용
            String atMsg = atMsgContent.replaceAll("<br>", "\r\n");

            atMsg = rcpAtMsg(atMsg, writer, cusName, amount, virtualAccountNo, chaTelNo);
            LOGGER.debug("알림톡 생성 완료");

            final String chacd = xchalist.getChacd();
            final int msgLength = atMsg.getBytes(Charset.forName("EUC-KR")).length;

            final FingerTalk.Message sendMsg = fingerTalk.createMessage(hpNo, chaTelNo, atMsg, "TC_DEPOSIT_FIN");      // 알람톡 발송

            if (sendMsg != null) {
                final Atreq atreq = new Atreq();
                atreq.setCmid(sendMsg.getResultObject().getCmid());
                atreq.setTrxkey(sendMsg.getTrxKey());
                atreq.setChacd(chacd);
                atreq.setChatelno(chaTelNo.replaceAll("-", ""));
                atreq.setCustelno(hpNo.replaceAll("-", ""));
                atreq.setSendmsg(atMsg);
                atreq.setMsgtype("2");
                atreq.setSendstatuscd("0");
                atreq.setSendresultcd("0");
                atreq.setNotimascd(notiMasCd);
                atreq.setPaymascd("");
                atreq.setSenddate(new Date());
                atreq.setMakedt(new Date());
                atreq.setMaker("damoa-va-agent");

                atreqMapper.insert(atreq);
            }
            // 알림톡 발송 및 테이블 저장 완료.

            LOGGER.info("알림톡 발송 및 발송이력 저장 완료.");
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());
            }

        }
    }

    private String deleteZero(String amount) {
        return String.valueOf(Long.valueOf(amount));
    }

    /**
     * @param str        SMS문자열
     * @param cusName    고객명
     * @param payItemAmt 청구금액
     * @param vano       가상계좌번호
     * @param masMonth   청구월
     * @param chaname    기관명
     * @return
     */
    private String rcpSmsMsg(String str, String cusName, String payItemAmt, String vano, String masMonth, String chaname) {
        // 고객명
        if (str.contains("[#CUSNAME#]")) {
            if ("".equals(cusName) || cusName == null) {
                cusName = "";
            }
            str = str.replace("[#CUSNAME#]", cusName);
        }
        if (str.contains("[#고객명#]")) {
            if ("".equals(cusName) || cusName == null) {
                cusName = "";
            }
            str = str.replace("[#고객명#]", cusName);
        }
        // 납부자명
        if (str.contains("[$납부자명]")) {
            if ("".equals(cusName) || cusName == null) {
                cusName = "";
            }
            str = str.replace("[$납부자명]", cusName);
        }
        if (str.contains("[$NAME]")) {
            if ("".equals(cusName) || cusName == null) {
                cusName = "";
            }
            str = str.replace("[$NAME]", cusName);
        }
        // 청구월
        if (str.contains("[#MASMONTH#]")) {
            if ("".equals(masMonth) || masMonth == null) {
                masMonth = "";
            }
            str = str.replace("[#MASMONTH#]", masMonth);
        }
        if (str.contains("[$청구월]")) {
            if ("".equals(masMonth) || masMonth == null) {
                masMonth = "";
            }
            str = str.replace("[$청구월]", masMonth);
        }
        if (str.contains("[$PMONTH]")) {
            if ("".equals(masMonth) || masMonth == null) {
                masMonth = "";
            }
            str = str.replace("[$PMONTH]", masMonth);
        }
        // 청구금액
        if (str.contains("[#PAYITEMAMT#]")) {
            if ("".equals(payItemAmt) || payItemAmt == null) {
                payItemAmt = "";
            }
            str = str.replace("[#PAYITEMAMT#]", payItemAmt);
        }
        if (str.contains("[#청구액#]")) {
            if ("".equals(payItemAmt) || payItemAmt == null) {
                payItemAmt = "";
            }
            str = str.replace("[#청구액#]", payItemAmt);
        }
        if (str.contains("[$청구금액]")) {
            if ("".equals(payItemAmt) || payItemAmt == null) {
                payItemAmt = "";
            }
            str = str.replace("[$청구금액]", payItemAmt);
        }
        if (str.contains("[$AMT]")) {
            if ("".equals(payItemAmt) || payItemAmt == null) {
                payItemAmt = "";
            }
            str = str.replace("[$AMT]", payItemAmt);
        }
        // 납부계좌
        if (str.contains("[#PAYVANO#]")) {
            if ("".equals(vano) || vano == null) {
                vano = "";
            }
            str = str.replace("[#PAYVANO#]", vano);
        }
        if (str.contains("[#납부계좌#]")) {
            if ("".equals(vano) || vano == null) {
                vano = "";
            }
            str = str.replace("[#납부계좌#]", vano);
        }
        // 납부계좌번호
        if (str.contains("[#납부계좌번호#]")) {
            if ("".equals(vano) || vano == null) {
                vano = "";
            }
            str = str.replace("[#납부계좌번호#]", vano);
        }
        // 수납계좌
        if (str.contains("[$수납계좌번호]")) {
            if ("".equals(vano) || vano == null) {
                vano = "";
            }
            str = str.replace("[$수납계좌번호]", vano);
        }
        if (str.contains("[$ACCNO]")) {
            if ("".equals(vano) || vano == null) {
                vano = "";
            }
            str = str.replace("[$ACCNO]", vano);
        }
        // 기관명
        if (str.contains("[$기관명]")) {
            if ("".equals(chaname) || chaname == null) {
                chaname = "";
            }
            str = str.replace("[$기관명]", chaname);
        }
        if (str.contains("[#기관명#]")) {
            if ("".equals(chaname) || chaname == null) {
                chaname = "";
            }
            str = str.replace("[#기관명#]", chaname);
        }

        return str;
    }

    /**
     * 알림톡 메시지 생성
     */
    private String rcpAtMsg(String str, String chaname, String cusName, String payItemAmt, String vano, String chaTelNo) {
        DecimalFormat df = new DecimalFormat("###,###,###,###");
        int formatVal = 0;

        // 기관명
        if (str.contains("[#기관명#]")) {
            if ("".equals(chaname) || chaname == null) {
                chaname = "";
            }
            str = str.replace("[#기관명#]", chaname);
        }
        // 고객명
        if (str.contains("[#고객명#]")) {
            if ("".equals(cusName) || cusName == null) {
                cusName = "";
            }
            str = str.replace("[#고객명#]", cusName);
        }
        // 입금액
        if (str.contains("[#입금액#]")) {
            if ("".equals(payItemAmt) || payItemAmt == null) {
                payItemAmt = "";
            }
            formatVal = Integer.parseInt(payItemAmt);
            str = str.replace("[#입금액#]", df.format(formatVal));
        }
        // 납부계좌번호
        if (str.contains("[#납부계좌번호#]")) {
            if ("".equals(vano) || vano == null) {
                vano = "";
            }
            str = str.replace("[#납부계좌번호#]", "신한 " + vano);
        }
        // 기관대표전화번호
        if (str.contains("[#기관대표전화번호#]")) {
            if ("".equals(chaTelNo) || chaTelNo == null) {
                chaTelNo = "";
            }
            str = str.replace("[#기관대표전화번호#]", chaTelNo);
        }

        return str;
    }

    @Transactional
    public void handleCancel(CancelMessageBean messageBean) {
        final String rcpMasCd = messageBean.getRcpMasCd();
        final String notiMasCd = messageBean.getNotiMasCd();

        // TODO 청구 미납 처리
        // 한 수납건의 데이터 수집
        //RCPMASST 가 완납인 걸 수집..
        if ("00000000000000000000".equalsIgnoreCase(notiMasCd) == false) {
            List<Map<String, Object>> rcps = damoaDao.findRcpCanCelInfo(notiMasCd);
            setupCancelNoti(rcps, notiMasCd);
        }

        try {
            LOGGER.info("현금영수증 삭제 ... 수납번호 {}", rcpMasCd);
            damoaDao.deleteCashmas(rcpMasCd);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    /**
     * 하나의 청구와 여러 수납데이터 처리..
     *
     * @param rcps 여러 수납데이터임..
     */
    private void setupCancelNoti(List<Map<String, Object>> rcps, String notimasCd) {
        //notimas + notidet
        //남아있는 수납건을 데이터를 모은다.
        Map<String, RcpBean> rcpBeanMap = aggregate(rcps);

        // 청구 한건 청구상세 여러건
        Map<String, NotiDetBean> notiDetBeanMap = findNotiDetBean(notimasCd);
        LOGGER.info(" " + rcpBeanMap);

        if (rcpBeanMap.size() == 0) {
            for (String key : notiDetBeanMap.keySet()) {
                NotiDetBean detBean = notiDetBeanMap.get(key);
                detBean.setAfterState("PA02");
                LOGGER.info(detBean.getBeforeState() + " ==> [PA02]미납 업데이트 .." + notimasCd + " " + detBean.getNotdetCd());
                damoaDao.updateNotidetWithNotidetcd(detBean.getNotdetCd(), "PA02");
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

    /**
     * 청구항목별로 얼마나 수납되었는지 확인..
     * 가상계좌에서 수납한거랑 아닌게 있음에 유의...
     *
     * @param rcps 여러 수납데이터..
     * @return
     */
    private Map<String, RcpBean> aggregate(List<Map<String, Object>> rcps) {

        Map<String, RcpBean> notiMasDetMap = new HashMap<>();
        for (Map<String, Object> map : rcps) {
            String notimasCd = Maps.getValue(map, "notimascd");
            String notidetcd = Maps.getValue(map, "notidetcd");
            String payItemAmt = Maps.getValue(map, "payitemamt");
            String rcpmasCd = Maps.getValue(map, "rcpmascd");
            String rcpdetCd = Maps.getValue(map, "rcpdetcd");
            String key = notiDetKey(notimasCd, notidetcd);
            if (notiMasDetMap.containsKey(key)) {
                RcpBean detBean = notiMasDetMap.get(key);
                detBean.setAmount(detBean.getAmount() + Long.valueOf(payItemAmt));
            } else {
                RcpBean rcpBean = new RcpBean(notimasCd, notidetcd);
                rcpBean.setAmount(Long.valueOf(payItemAmt));
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

    /**
     * 한청구 여러 청구항목 정보 수집..
     *
     * @param notimasCd
     * @return
     */
    private Map<String, NotiDetBean> findNotiDetBean(String notimasCd) {
        List<Map<String, Object>> notiMapList = damoaDao.findNotiCanCelInfo(notimasCd);
        Map<String, NotiDetBean> detBeans = new HashMap<>();
        for (Map<String, Object> map : notiMapList) {
            /**
             */
            String notidetcd = Maps.getValue(map, "notidetcd");
            String payItemAmt = Maps.getValue(map, "payitemamt");
            String notiState = Maps.getValue(map, "notimasst");
            String detState = Maps.getValue(map, "notidetst");

            NotiDetBean detBean = new NotiDetBean(notimasCd, notidetcd);
            detBean.setAmount(Long.valueOf(payItemAmt));
            detBean.setBeforeState(detState);
            detBean.setMasState(notiState);
            detBeans.put(detBean.notimasDetCd(), detBean);
        }

        return detBeans;
    }

    /**
     * @param notiDetBean  청구데이터..
     * @param rcpDetAmount 수닙금액
     * @param notidetcd    수납상세cd
     * @param notimasCd    수납cd
     */
    private void setupCancelNotidet(NotiDetBean notiDetBean, Long rcpDetAmount, String notidetcd, String notimasCd) {
        //청구상세
        LOGGER.info("청구상세 취소처리.. " + notiDetBean.getNotimasCd() + " " + notiDetBean.getNotdetCd() + " " + notiDetBean.getBeforeState() + " " + notiDetBean.getAmount() + " " + rcpDetAmount);
        Long notiDetAmount = notiDetBean.getAmount();
        long aRcpDetAmount = rcpDetAmount.longValue();
        long aNotiDetAmount = notiDetAmount.longValue();
        if (aRcpDetAmount > aNotiDetAmount) {
            LOGGER.info(notiDetBean.getBeforeState() + " ==> [PA05] 초과납 업데이트 .." + notimasCd + " " + notidetcd);
            damoaDao.updateNotidetWithNotidetcd(notidetcd, "PA05");
            notiDetBean.setAfterState("PA05");

        } else if (aRcpDetAmount == aNotiDetAmount) {
            LOGGER.info(notiDetBean.getBeforeState() + " ==> [PA02]미납 업데이트 .." + notimasCd + " " + notidetcd);
            damoaDao.updateNotidetWithNotidetcd(notidetcd, "PA02");
            notiDetBean.setAfterState("PA02");
        } else if (aNotiDetAmount > aRcpDetAmount) {
            LOGGER.info(notiDetBean.getBeforeState() + " ==>  [PA04]부분납 업데이트 .." + notimasCd + " " + notidetcd);
            damoaDao.updateNotidetWithNotidetcd(notidetcd, "PA04");
            notiDetBean.setAfterState("PA04");
        } else if (aRcpDetAmount == 0) {
            //
            LOGGER.info(notiDetBean.getBeforeState() + " ==> [PA03]완납 업데이트 .." + notimasCd + " " + notidetcd);
            damoaDao.updateNotidetWithNotidetcd(notidetcd, "PA03");
            notiDetBean.setAfterState("PA03");
        } else {
            LOGGER.info("이런 경우는 없어야...");
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
        LOGGER.info("청구 MAS 취소처리중...");
        String state = checkNotiDet(notiDetBeans);
        if (notiDetBeans == null || notiDetBeans.size() == 0) {
            LOGGER.info("청구건 " + notimasCd + " " + state);
        } else {
            LOGGER.info("청구건 " + notimasCd + " " + notimasState(notiDetBeans) + " == " + state);
        }
        damoaDao.updateNotimas(notimasCd, state);
        LOGGER.info("청구 MAS 취소처리 완료...");
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

    private String notimasState(List<NotiDetBean> notiDetBeans) {
        return notiDetBeans.get(0).getMasState();
    }
}
