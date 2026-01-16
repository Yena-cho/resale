package com.finger.shinhandamoa.org.receiptmgmt.service;

import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.common.XlsxBuilder;
import com.finger.shinhandamoa.data.table.mapper.*;
import com.finger.shinhandamoa.data.table.model.*;
import com.finger.shinhandamoa.org.notimgmt.dao.NotiMgmtDAO;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtBaseDTO;
import com.finger.shinhandamoa.org.receiptmgmt.DirectReceiptMgmtDAO;
import com.finger.shinhandamoa.org.receiptmgmt.dto.ReceiptMgmtDTO;
import com.finger.shinhandamoa.org.receiptmgmt.dto.RefundRcpDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DirectReceiptMgmtServiceImpl implements DirectReceiptMgmtService {

    @Autowired
    private ChaMapper chaMapper;

    @Autowired
    private NoticeDetailsTypeMapper noticeDetailsTypeMapper;

    @Autowired
    private NoticeMasterMapper noticeMasterMapper;

    @Autowired
    private NoticeDetailsMapper noticeDetailsMapper;

    @Autowired
    private ReceiptMasterMapper rcpMasterMapper;

    @Autowired
    private ReceiptDetailsMapper rcpDetailsMapper;

    @Autowired
    private CashReceiptMasterMapper cashReceiptMasterMapper;

    @Autowired
    private RePayMasterMapper rePayMasterMapper;

    @Autowired
    private DirectReceiptMgmtDAO directReceiptMgmtDAO;

    @Autowired
    private CashHstMapper cashHstMapper;

    @Resource(name = "notiMgmtDao")
    private NotiMgmtDAO notiMgmtDao;

    final SimpleDateFormat fmtFullTime = new SimpleDateFormat("yyyyMMdd HHmmss");
    final SimpleDateFormat fmtYmd = new SimpleDateFormat("yyyy-MM-dd");
    final SimpleDateFormat fmtYYMMDD = new SimpleDateFormat("yyyyMMdd");
    final SimpleDateFormat fmtHHmmss = new SimpleDateFormat("HHmmss");

    final SimpleDateFormat fmtPayDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectReceiptMgmtServiceImpl.class);

    @Override
    public Map<String, String> getNotiMasMonth(String chacd, String... status) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("chaCd", chacd);
        map.put("masSt", status);
        return directReceiptMgmtDAO.selectNoticeMasMaxMonth(map);
    }

    @Override
    public List<NoticeDetailsType> selectPayItems(String chacd) {
        NoticeDetailsTypeExample example = new NoticeDetailsTypeExample();
        example.createCriteria().andChacdEqualTo(chacd).andPayitemstEqualTo("ST01");
        example.setOrderByClause("PTRITEMORDER::numeric, PAYITEMCD");
        List<NoticeDetailsType> list = noticeDetailsTypeMapper.selectByExample(example);
        return list;
    }

    private boolean isReceiptItem(String payitemcd) {
        try {
            NoticeDetailsType items = noticeDetailsTypeMapper.selectByPrimaryKey(payitemcd);
            return items.getRcpitemyn().equalsIgnoreCase("Y");
        } catch (Exception e) {
            LOGGER.warn(e.toString(), e);
            return false;
        }
    }

    @Override
    public List<ReceiptMgmtDTO> selectNoticeCatalog(Map<String, Object> params) throws Exception {
        params.put("srchValues", this.convertByLikeClause(params.get("searchValue")));
        params.put("srchGubnValues", this.convertByLikeClause(params.get("cusGubnValue")));

        List<ReceiptMgmtDTO> result;
        if (params.get("pageTab").toString().equalsIgnoreCase("direct")) {
            // 직접수납등록 목록조회
            result = directReceiptMgmtDAO.selectDirectNoticeList(params);
        } else {
            // 직접수납완료 목록조회
            result = directReceiptMgmtDAO.selectCompleteNoticeList(params);
        }

        return result;
    }

    @Override
    public Map<String, Object> selectNoticeCatalogCount(Map<String, Object> params) throws Exception {
        params.put("srchValues", this.convertByLikeClause(params.get("searchValue")));
        params.put("srchGubnValues", this.convertByLikeClause(params.get("cusGubnValue")));

        Map<String, Object> result;

        if (params.get("pageTab").toString().equalsIgnoreCase("direct")) {
            // 직접수납등록 건수조회
            result = directReceiptMgmtDAO.selectDirectNoticeListCount(params);
            result.put("sumrcpamt", 0);
        } else {
            // 직접수납완료 건수조회
            result = directReceiptMgmtDAO.selectCompleteNoticeListCount(params);
        }

        return result;
    }

    /**
     * 즉시 수납은 Master와 Detail에 관계가 1:1로 생성한다.
     * --> 청구항목 1개에 대해서 n개의 방법으로 입금이 가능 (ex> 청구액 10만원에 5만원 현금, 5만원 카드)
     * 1. 수납Master에 수납정보 생성
     * 2. 수납Detail에 수납정보 생성
     * 3. 청구Detail에 수납상태 업데이트
     * 4. 청구Master에 수납상태 업데이트
     * 5. 현금영수증 발행 처리
     */
    @SuppressWarnings({"unchecked", "unused"})
    @Override
    public Map<String, Object> processReceipt(Map<String, Object> params) throws Exception {

        // 수납처리 일시
        final Date now = new Date();
        // 먼저 기관정보 로드
        String chacd = params.get("chacd").toString();
        final Cha cha = chaMapper.selectByPrimaryKey(chacd);
        final Map<String, Object> amtMap = (Map<String, Object>) params.get("rcpAmtMap");
        final ArrayList<Map<String, Object>> rcpList = (ArrayList<Map<String, Object>>) params.get("rcpList");

        // 현금영수증 자동발급 여부
        // 수납방법코드[ DCS-창구현금(현장), DVA-기관계좌입금 ]
        final boolean isAutoRcpCashReq = (cha.getMnlrcpreqty().equalsIgnoreCase("A") && cha.getRcpreqsvety().equalsIgnoreCase("01"));
        // 현금영수증 발급여부
        final boolean isRcpCashReq = (cha.getRcpreqyn().equalsIgnoreCase("Y"));


        // 수납상태 처리를 위한 map { notiMascd : NoticeMaster }
        Map<String, NoticeMaster> notiMasterMap = new HashMap<String, NoticeMaster>();

        // 청구, 수납 maskey 보관 및 비교용도 { notiMascd : rcpMasCd }
        //Map<String, String> masKeySet = new HashMap<String, String>();
        List<String> rcpMasterList = new ArrayList<String>();
        // #1. 수납마스터 생성
        int count = 0;
        for (final Map<String, Object> rcpMap : rcpList) {

            final String notimascd = rcpMap.get("notiMasCd").toString();
            final String notidetcd = rcpMap.get("notiDetCd").toString();
            final String svecd = rcpMap.get("sveCd").toString().toUpperCase();

            // 청구마스터 조회
            final NoticeMaster notiMas = noticeMasterMapper.selectByPrimaryKey(notimascd);
            // #4에서 사용할 NoticeMaster 객체
            notiMasterMap.put(notimascd, notiMas);

            //final Long rcpamt = NumberUtils.toLong(amtMap.get(notimascd).toString(), 0L);
            /**
             * FIXME 수납마스터에 수납상세의 입금액을 sum해서 넣을 필요는 없다.
             * @author jhjeong@finger.co.kr
             * @modified 2018. 10. 10.
             */
            final Long rcpamt = NumberUtils.toLong(rcpMap.get("inputRcpAmt").toString(), 0L);

            // insert
            ReceiptMasterWithBLOBs mas = new ReceiptMasterWithBLOBs();
            mas.setNotimascd(notimascd);
            mas.setSvecd(svecd);
            mas.setChacd(cha.getChacd());
            mas.setVano(rcpMap.get("vano").toString());
            mas.setMaskey(notiMas.getMaskey());
            mas.setMasmonth(notiMas.getMasmonth());
            mas.setMasday(notiMas.getMasday());
            mas.setCuskey(notiMas.getCuskey());
            mas.setCusgubn1(notiMas.getCusgubn1());
            mas.setCusgubn2(notiMas.getCusgubn2());
            mas.setCusgubn3(notiMas.getCusgubn3());
            mas.setCusgubn4(notiMas.getCusgubn4());
            mas.setCusname(notiMas.getCusname());
            mas.setCushp(notiMas.getCushp());
            mas.setCusmail(notiMas.getCusmail());
            mas.setSmsyn(notiMas.getSmsyn());
            mas.setMailyn(notiMas.getMailyn());
            mas.setCusoffno(notiMas.getCusoffno());
            mas.setPayday(rcpMap.get("payday").toString());
            mas.setPaytime(fmtHHmmss.format(now));
            mas.setRcpamt(rcpamt);
            mas.setRcpmasst("PA03");
            mas.setMakedt(now);
            mas.setMaker(cha.getChacd());
            mas.setChatrty(cha.getChatrty());
            rcpMasterMapper.insertSelective(mas);
            final String rcpmascd = mas.getRcpmascd();
            rcpMasterList.add(rcpmascd);
            // #2. 수납 상세데이터 생성
            // 청구상세 조회
            NoticeDetails notiDet = noticeDetailsMapper.selectByPrimaryKey(notidetcd);
            final Long payitemamt = NumberUtils.toLong(rcpMap.get("inputRcpAmt").toString(), 0L);

            // 수납상세 객체는 입금시 insert로 생성
            ReceiptDetails detail;
            // insert
            detail = new ReceiptDetails();
            detail.setRcpmascd(rcpmascd);
            detail.setNotidetcd(notidetcd);
            detail.setDetkey(notiDet.getDetkey());
            detail.setAdjfiregkey(notiDet.getAdjfiregkey());
            detail.setPayitemcd(notiDet.getPayitemcd());
            detail.setPayitemname(notiDet.getPayitemname());
            detail.setPayitemamt(payitemamt);
            detail.setRcpitemyn(notiDet.getPayitemselyn());
            detail.setChaoffno(notiDet.getChaoffno());
            detail.setCusoffno(notiDet.getCusoffno());
            detail.setPtritemname(notiDet.getPtritemname());
            detail.setPtritemremark(notiDet.getPtritemremark());
            detail.setPtritemorder(notiDet.getPtritemorder());
            detail.setRcpdetst("PA03");
            detail.setMakedt(now);
            detail.setMaker(cha.getChacd());
            detail.setChatrty(cha.getChatrty());
            detail.setRegdt(now);
            count += rcpDetailsMapper.insertSelective(detail);

            // 기존에 입금정보를 조회후 청구상세 값을 갱신한다.
            ReceiptDetailsExample rcpExample = new ReceiptDetailsExample();
            rcpExample.createCriteria().andNotidetcdEqualTo(notidetcd).andRcpdetstEqualTo("PA03");
            final List<ReceiptDetails> rcpdetailList = rcpDetailsMapper.selectByExample(rcpExample);
            long rcpItemPayment = 0L;
            for (ReceiptDetails rcpdetail : rcpdetailList) {
                rcpItemPayment += rcpdetail.getPayitemamt();
            }

            // 청구 상세의 상태값을 처리
            String notiStatus = "";
            if (notiDet.getPayitemamt() == rcpItemPayment) {
                // 완납
                notiStatus = "PA03";
            } else {
                if (notiDet.getPayitemamt() > rcpItemPayment) {
                    // 일부납
                    notiStatus = "PA04";
                } else {
                    // 초과납
                    notiStatus = "PA05";
                }
            }

            // #3. 청구 상세 수납상태값 변경
            notiDet.setNotidetst(notiStatus);
            notiDet.setMakedt(now);
            noticeDetailsMapper.updateByPrimaryKey(notiDet);
        } // rcpList.foreach end

        // #4. 청구 마스터의 수납상태값 변경
        for (String notiMasCd : notiMasterMap.keySet()) {
            NoticeMaster noticeMaster = notiMasterMap.get(notiMasCd);

            if (noticeMaster != null) {
                NoticeDetailsExample example;

                // 항목별 수납건수 확인
                example = new NoticeDetailsExample();
                example.createCriteria().andNotimascdEqualTo(notiMasCd).andNotidetcdNotEqualTo("PA00").andNotiCanYnEqualTo("N");
                final int detailAllCount = (int) noticeDetailsMapper.countByExample(example);

                example = new NoticeDetailsExample();
                example.createCriteria().andNotimascdEqualTo(notiMasCd).andNotidetstEqualTo("PA03").andNotiCanYnEqualTo("N");
                final int detailPA03Count = (int) noticeDetailsMapper.countByExample(example);

                example = new NoticeDetailsExample();
                example.createCriteria().andNotimascdEqualTo(notiMasCd).andNotidetstEqualTo("PA04").andNotiCanYnEqualTo("N");
                final int detailPA04Count = (int) noticeDetailsMapper.countByExample(example);

                example = new NoticeDetailsExample();
                example.createCriteria().andNotimascdEqualTo(notiMasCd).andNotidetstEqualTo("PA05").andNotiCanYnEqualTo("N");
                final int detailPA05Count = (int) noticeDetailsMapper.countByExample(example);

                if (detailAllCount == detailPA03Count) {
                    noticeMaster.setNotimasst("PA03");
                } else {
                    if (detailPA05Count > 0) {
                        noticeMaster.setNotimasst("PA05");
                    } else if (detailPA04Count > 0) {
                        noticeMaster.setNotimasst("PA04");
                    } else {
                        // 입금처리한 항목이 있으면, 부분납
                        noticeMaster.setNotimasst("PA04");
                    }
                }
                noticeMasterMapper.updateByPrimaryKey(noticeMaster);
            }

        } // masStatusMap.foreach end

        // #5. 현급영수증 발급
        if (isRcpCashReq) {
            try {

                /**
                 * 수납방법코드[ DCS-창구현금(현장), DVA-기관계좌입금 ]
                 * 현금영수증 발급 처리 변경
                 * 청구항목별 발급용 사업자번호가 세팅되어서 개별 발급요청으로 처리
                 * @modified 2018. 10. 02.
                 */
                for (String rcpMList : rcpMasterList) {
                    LOGGER.debug("수기수납 현금영수증 발행 [{}]", rcpMList);
                    final ReceiptMaster master = rcpMasterMapper.selectByPrimaryKey(rcpMList);

                    // 온라인카드, 오프라인카드는 현금영수증 발급대상에서 제외
                    if (StringUtils.containsAny(master.getSvecd(), "OCD", "DCD")) {
                        continue;
                    }

                    // 온라인카드, 오프라인카드는 현금영수증 발급대상에서 제외
                    if (StringUtils.containsAny(master.getSvecd(), "OCD", "DCD")) {
                        continue;
                    }

                    final Date senddt = DateUtils.addDays(new Date(), 1); //오늘 날짜 기준에서 내일날짜로 세팅.

                    // 현금영수증 발급대상 조회
                    final ReceiptDetailsExample rcpDetailExample = new ReceiptDetailsExample();
                    rcpDetailExample.createCriteria().andRcpmascdEqualTo(master.getRcpmascd());
                    final List<ReceiptDetails> rcpDetailsList = rcpDetailsMapper.selectByExample(rcpDetailExample);
                    final Map<String, Long> chaOffMap = new HashMap<>();
                    for (ReceiptDetails rcpDetail : rcpDetailsList) {
                        long rcpAmtCash;
                        // 현금영수증 발급유무 확인
                        if (this.isReceiptItem(rcpDetail.getPayitemcd())) {
                            rcpAmtCash = rcpDetail.getPayitemamt();
                        } else {
                            rcpAmtCash = 0L;
                        }

                        if (!chaOffMap.containsKey(rcpDetail.getChaoffno())) {
                            chaOffMap.put(rcpDetail.getChaoffno(), 0L);
                        }
                        chaOffMap.put(rcpDetail.getChaoffno(), chaOffMap.get(rcpDetail.getChaoffno()) + rcpAmtCash);
                    }

                    if (chaOffMap.isEmpty()) {
                        chaOffMap.put(cha.getChaoffno(), master.getRcpamt());
                    }

                    for (Map.Entry<String, Long> each : chaOffMap.entrySet()) {
                        try {
                            final String key = each.getKey();
                            final Long value = each.getValue();

                            LOGGER.debug("수기수납 현금영수증 발행 [{}] - CHAOFFNO:{}, RCPAMT", rcpMList, key, value);

                            CashReceiptMasterWithBLOBs cash = new CashReceiptMasterWithBLOBs();
                            cash.setRcpmascd(master.getRcpmascd());
                            cash.setChacd(chacd);
                            cash.setChaoffno(key);
                            if("Y".equals(cha.getMandRcpYn()) && StringUtils.isBlank(master.getCusoffno())){
                                cash.setCusoffno("0100001234");
                                cash.setCustype("1");
                                cash.setConfirm("11");
                            }else{
                                cash.setCusoffno(master.getCusoffno());
                                cash.setCustype(getCusType("", StringUtils.defaultString(master.getCusoffno())));
                                cash.setConfirm(getConfirm("", StringUtils.defaultString(master.getCusoffno())));
                            }

                            cash.setNotaxyn(cha.getNotaxyn());
                            cash.setRcpreqty(cha.getRcpreqty());
                            cash.setChatrty(cha.getChatrty());
                            cash.setRcpamt(value);
                            cash.setTip(0L);
                            cash.setAmtchkty(cha.getAmtchkty());

                            long tax = 0L;
                            if (cha.getNotaxyn().equalsIgnoreCase("Y")) {
                                tax = Math.round(value / 11D);
                            } else {
                                tax = 0L;
                            }
                            cash.setTax(tax);
                            if (isAutoRcpCashReq && value > 0 && StringUtils.isNotBlank(cash.getCusoffno())) {
                                // 자동발행
                                cash.setJob("I");    // 작업구분 I:신규 U:재발행 D:취소
                                cash.setCashmasst("ST05");    // 미발행
                                cash.setSenddt(fmtYYMMDD.format(senddt));
                            } else {
                                // 현금영수증으로 등록만
                                cash.setJob(null);
                                cash.setCashmasst("ST02");    // 미발행
                            }
                            cash.setMakedt(now);
                            cash.setMaker("damoa-service");
                            cash.setRegdt(now);

                            // 현금영수증 발행처리
                            cashReceiptMasterMapper.insertSelective(cash);

                            //현금영수증 이용내역 등록 (자동발행시에만 요청으로등록)
                            if("ST05".equals(cash.getCashmasst())){
                                CashHst cashHst = new CashHst();
                                cashHst.setChacd(cash.getChacd());
                                cashHst.setCashmascd(cash.getCashmascd());
                                cashHst.setRequestUser("damoa-service");
                                cashHst.setRequestDate(new Date());
                                cashHst.setRequestTypeCd("I");
                                cashHst.setResultCd("3");
                                cashHst.setCusoffno(cash.getCusoffno());
                                cashHst.setRcpamt(value);
                                cashHst.setTax(tax);
                                cashHstMapper.insertSelective(cashHst);
                            }
                        } catch (Exception e) {
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.error(e.getMessage(), e);
                            } else {
                                LOGGER.error(e.getMessage());
                            }

                        }
                    }
                }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }

        Map<String, Object> result = new HashMap<String, Object>();
        if (count == rcpList.size()) {
            result.put("retCode", "0000");
            result.put("retMsg", "정상");
        } else {
            result.put("retCode", "0001");
            result.put("retMsg", rcpList.size() + "건 중 " + count + "건을 저장했습니다.");
        }

        return result;
    }

    /**
     * 수납을 취소한 경우
     * rcp 마스터/상세 테이블은 상태값을 PA09로 변경
     * noti 마스터/상테 테이블은 상태값을 PA02로 변경 --> 수납가능
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> processReceiptCancel(Map<String, Object> params) throws Exception {

        // 변경일시
        final Date now = new Date();
        List<String> notimascdList = new ArrayList<String>();
        List<String> notidetcdList = new ArrayList<String>();
        List<String> rcpmascdList = new ArrayList<String>();
        List<String> rcpdetcdList = new ArrayList<String>();
        List<String> detcdList = new ArrayList<String>();

        int notiMasCount = 0;
        int notiDetCount = 0;
        int rcpMasCount = 0;
        int rcpDetCount = 0;
        int cashMasCount = 0;

        if (params.get("rcpList") == null) {
            // 일괄취소
            // 검색조건 생성
            params.put("srchValues", this.convertByLikeClause(params.get("searchValue")));
            params.put("srchGubnValues", this.convertByLikeClause(params.get("cusGubnValue")));

            params.put("orderBy", "month");
            List<ReceiptMgmtDTO> rcpCdList = directReceiptMgmtDAO.selectCompleteNoticeList(params);

            for (ReceiptMgmtDTO dto : rcpCdList) {
                rcpdetcdList.add(dto.getRcpDetCd());
            }

        } else {
            // 선택취소
            final ArrayList<Map<String, String>> rcpList = (ArrayList<Map<String, String>>) params.get("rcpList");

            for (Map<String, String> map : rcpList) {
                rcpdetcdList.add(map.get("rcpDetCd"));
            }
        }


        Map<String, Object> daoParams = new HashMap<String, Object>();

        // 1. 수납detail 취소처리
        daoParams.put("makedt", now);
        daoParams.put("chacd", params.get("chacd").toString());
        daoParams.put("rcpStatus", "PA09");
        daoParams.put("rcpdetcdList", rcpdetcdList);
        rcpDetCount = directReceiptMgmtDAO.updateXrcpDetSt(daoParams);

        // 2. 취소된 detail의 수납master 조회
        ReceiptDetailsExample rcpExample = new ReceiptDetailsExample();
        rcpExample.createCriteria().andRcpdetcdIn(rcpdetcdList).andRcpdetstEqualTo("PA09");
        final List<ReceiptDetails> rcpDetailsList = rcpDetailsMapper.selectByExample(rcpExample);
        Set<String> rcpKeyset = new HashSet<String>();
        Set<String> ndetKeyset = new HashSet<String>();
        Set<String> nmasKeyset = new HashSet<String>();
        for (ReceiptDetails det : rcpDetailsList) {
            rcpKeyset.add(det.getRcpmascd());
            ndetKeyset.add(det.getNotidetcd());
        }
        // 3. 수납 마스터 취소
        rcpmascdList.clear();
        rcpmascdList.addAll(rcpKeyset);
        daoParams.clear();
        daoParams.put("makedt", now);
        daoParams.put("rcpStatus", "PA09");
        daoParams.put("chacd", params.get("chacd").toString());
        daoParams.put("rcpmascdList", rcpmascdList);
        rcpMasCount = directReceiptMgmtDAO.updateXrcpMasSt(daoParams);

        // 4. 현금영수증 취소
        // TODO 수기수납에 대한 현금영수증 취소는 해당 기관이 직접 수정.
        // 추후 해당 처리방법에 대한 기획이 확정되면 로직으로 처리
        // cashMasCount = directReceiptMgmtDAO.updateXcashMasDisabled(daoParams);

        // 5. 청구Detail 취소
        notidetcdList.clear();
        notidetcdList.addAll(ndetKeyset);
        daoParams.clear();
        daoParams.put("makedt", now);
        daoParams.put("chacd", params.get("chacd").toString());
        daoParams.put("notiStatus", "PA02");
        daoParams.put("notidetcdList", notidetcdList);
        notiDetCount = directReceiptMgmtDAO.updateXnotiDetSt(daoParams);

        // 6. 청구 Master 취소
        NoticeDetailsExample example = new NoticeDetailsExample();
        example.createCriteria().andNotidetcdIn(notidetcdList);
        List<NoticeDetails> notiDetList = noticeDetailsMapper.selectByExample(example);
        for (NoticeDetails detail : notiDetList) {
            nmasKeyset.add(detail.getNotimascd());
        }
        notimascdList.clear();
        notimascdList.addAll(nmasKeyset);
        daoParams.clear();
        daoParams.put("makedt", now);
        daoParams.put("chacd", params.get("chacd").toString());
        daoParams.put("notiStatus", "PA02");
        daoParams.put("notimascdList", notimascdList);
        notiMasCount = directReceiptMgmtDAO.updateXnotiMasSt(daoParams);

        // 7.1. 청구Detailcd에 한개라도 수납된 데이터가 있으면 부분납으로 처리한다.
        rcpExample = new ReceiptDetailsExample();
        rcpExample.createCriteria().andNotidetcdIn(notidetcdList).andRcpdetstEqualTo("PA03");
        final List<ReceiptDetails> rDetailsList = rcpDetailsMapper.selectByExample(rcpExample);
        ndetKeyset.clear();
        for (ReceiptDetails det : rDetailsList) {
            ndetKeyset.add(det.getNotidetcd());
        }
        if (ndetKeyset.size() > 0) {
            notidetcdList.clear();
            notidetcdList.addAll(ndetKeyset);
            daoParams.clear();
            daoParams.put("makedt", now);
            daoParams.put("chacd", params.get("chacd").toString());
            daoParams.put("notiStatus", "PA04");
            daoParams.put("notidetcdList", notidetcdList);
            notiDetCount = directReceiptMgmtDAO.updateXnotiDetSt(daoParams);
        }

        NoticeDetailsExample detExample = new NoticeDetailsExample();
        detExample.createCriteria().andNotimascdIn(notimascdList);
        List<NoticeDetails> detList = noticeDetailsMapper.selectByExample(detExample);
        for(NoticeDetails detail : detList){
            detcdList.add(detail.getNotidetcd());
        }

        // 7.2. 청구 마스터의 수납상태 업데이트
        List<String> values = new ArrayList<String>();
        values.add("PA03");
        values.add("PA04");
        values.add("PA05");
        example = new NoticeDetailsExample();
        example.createCriteria().andNotidetcdIn(detcdList).andNotidetstIn(values);
        //example.createCriteria().andNotidetcdIn(notidetcdList).andNotidetstIn(values);
        notiDetList = noticeDetailsMapper.selectByExample(example);
        nmasKeyset.clear();
        for (NoticeDetails detail : notiDetList) {
            nmasKeyset.add(detail.getNotimascd());
        }
        if (nmasKeyset.size() > 0) {
            // 수납된 detail이 한개라도 있으면 부분납으로 처리한다.
            notimascdList.clear();
            notimascdList.addAll(nmasKeyset);
            daoParams.clear();
            daoParams.put("makedt", now);
            daoParams.put("chacd", params.get("chacd").toString());
            daoParams.put("notiStatus", "PA04");
            daoParams.put("notimascdList", notimascdList);
            notiMasCount = directReceiptMgmtDAO.updateXnotiMasSt(daoParams);
        }

        LOGGER.debug(String.format("수기수납취소 건수[청구=%d, 청구상세=%d, 수납=%d, 수납상세=%d, 현금영수증=%d]", notiMasCount, notiDetCount, rcpMasCount, rcpDetCount, cashMasCount));

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("retCode", "0000");
        result.put("retMsg", "청구항목 " + notiDetCount + "건, 수납항목 " + rcpDetCount + "건을 취소 했습니다.\n현금영수증 발행(요청)건이 있는 경우 취소처리해주세요.");

        return result;
    }

    @Override
    public InputStream getDirectReceiptExcel(Map<String, Object> params) throws Exception {

        params.put("srchValues", this.convertByLikeClause(params.get("searchValue")));
        params.put("srchGubnValues", this.convertByLikeClause(params.get("cusGubnValue")));

        params.put("curPage", 1);
        Map<String, Object> result;
        final List<ReceiptMgmtDTO> itemList;
        if (params.get("pageTab").toString().equalsIgnoreCase("direct")) {
            // 직접수납등록 목록조회
            result = directReceiptMgmtDAO.selectDirectNoticeListCount(params);
            params.put("pageScale", NumberUtils.toInt(result.get("cnt").toString(), 0));
            itemList = directReceiptMgmtDAO.selectDirectNoticeList(params);
        } else {
            // 직접수납완료 목록조회
            result = directReceiptMgmtDAO.selectCompleteNoticeListCount(params);
            params.put("pageScale", NumberUtils.toInt(result.get("cnt").toString(), 0));
            itemList = directReceiptMgmtDAO.selectCompleteNoticeList(params);
        }

        XlsxBuilder xlsxBuilder = new XlsxBuilder();
        xlsxBuilder.newSheet("수기수납내역");

        NotiMgmtBaseDTO baseInfo = notiMgmtDao.selectNotiBaseInfo(params.get("chacd").toString());

        xlsxBuilder.addHeader(0, 0, "NO");
        xlsxBuilder.addHeader(0, 1, "청구월");
        xlsxBuilder.addHeader(0, 2, "고객명");
        xlsxBuilder.addHeader(0, 3, "가상계좌번호");
        xlsxBuilder.addHeader(0, 4, baseInfo.getCusGubn1());
        xlsxBuilder.addHeader(0, 5, baseInfo.getCusGubn2());
        xlsxBuilder.addHeader(0, 6, baseInfo.getCusGubn3());
        xlsxBuilder.addHeader(0, 7, baseInfo.getCusGubn4());
        xlsxBuilder.addHeader(0, 8, "청구항목코드");
        xlsxBuilder.addHeader(0, 9, "청구항목명");
        xlsxBuilder.addHeader(0, 10, "입금수단");
        xlsxBuilder.addHeader(0, 11, "입금금액");
        xlsxBuilder.addHeader(0, 12, "청구금액");
        xlsxBuilder.addHeader(0, 13, "입금일시");
        xlsxBuilder.addHeader(0, 14, "납부시작일");
        xlsxBuilder.addHeader(0, 15, "납부종료일");

        Map<String, String> sveMap = new HashMap<String, String>();
        sveMap.put("VAS", "가상계좌");
        sveMap.put("DCS", "현금");
        sveMap.put("DCD", "오프라인카드");
        sveMap.put("DVA", "계좌입금");
        sveMap.put("OCD", "인터넷카드");

        for (ReceiptMgmtDTO each : itemList) {
            xlsxBuilder.newDataRow();

            xlsxBuilder.addData(0, each.getRn());
            xlsxBuilder.addData(1, StrUtil.dotDate(each.getMasMonth()));
            xlsxBuilder.addData(2, each.getCusName());
            xlsxBuilder.addData(3, each.getVano());
            xlsxBuilder.addData(4, each.getCusGubn1());
            xlsxBuilder.addData(5, each.getCusGubn2());
            xlsxBuilder.addData(6, each.getCusGubn3());
            xlsxBuilder.addData(7, each.getCusGubn4());
            xlsxBuilder.addData(8, each.getPayItemCd());
            xlsxBuilder.addData(9, each.getPayItemName());
            xlsxBuilder.addData(10, sveMap.get(each.getSveCd()));
            xlsxBuilder.addData(11, each.getRcpPayItemAmt());
            xlsxBuilder.addData(12, Long.parseLong(each.getPayItemAmt()));
            if (each.getPayDay() != null && each.getPayTime() != null) {
                final Date date = fmtFullTime.parse(each.getPayDay() + " " + each.getPayTime());
                xlsxBuilder.addData(13, fmtPayDateTime.format(date));
            } else {
                xlsxBuilder.addData(13, "");
            }
            xlsxBuilder.addData(14, StrUtil.dotDate(each.getStartDate()));
            xlsxBuilder.addData(15, StrUtil.dotDate(each.getEndDate()));
        }

        final File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".tmp");
        xlsxBuilder.writeTo(new FileOutputStream(tempFile));
        return new FileInputStream(tempFile);
    }

    @Override
    public List<RefundRcpDTO> selectRefundReceiptCatalog(Map<String, Object> params) throws Exception {
        params.put("srchValues", this.convertByLikeClause(params.get("searchValue")));
        params.put("srchGubnValues", this.convertByLikeClause(params.get("cusGubnValue")));

        final List<RefundRcpDTO> list;

        if (params.get("pageTab").toString().equalsIgnoreCase("refund")) {
            // 환불등록
            list = directReceiptMgmtDAO.selectRcpCatalog(params);
        } else {
            // 환불완료
            list = directReceiptMgmtDAO.selectRefundRcpCatalog(params);
        }

        return list;
    }

    @Override
    public Map<String, Object> selectRefundReceiptCatalogCount(Map<String, Object> params) throws Exception {
        params.put("srchValues", this.convertByLikeClause(params.get("searchValue")));
        params.put("srchGubnValues", this.convertByLikeClause(params.get("cusGubnValue")));

        final Map<String, Object> result;
        if (params.get("pageTab").toString().equalsIgnoreCase("refund")) {
            // 환불등록
            result = directReceiptMgmtDAO.selectRcpCatalogCount(params);
        } else {
            // 환불완료
            result = directReceiptMgmtDAO.selectRefundRcpCatalogCount(params);
        }

        return result;
    }

    /**
     * FIXME 환불등록시 수납정보의 상태값 변경은 없음.
     * 환불처리는 환불테이블(XREPAYMAS)의 정보로만 확인.
     *
     * @modified 2018. 10. 10.
     * @author jhjeong@finger.co.kr
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> processRefundReceipt(Map<String, Object> params) throws Exception {

        // 변경일시
        final Date now = new Date();
        Set<String> notimascdSet = new HashSet<String>();
        Set<String> rcpmascdSet = new HashSet<String>();

        List<String> notidetcdList = new ArrayList<String>();
        List<String> rcpdetcdList = new ArrayList<String>();

        // 상태값 변경용도
        Map<String, Object> daoParams = new HashMap<String, Object>();

        final String chacd = params.get("chacd").toString();
        final String repayday = params.get("repayday").toString();

        int notiMasCount = 0;
        int notiDetCount = 0;

        final ArrayList<Map<String, String>> rcpList = (ArrayList<Map<String, String>>) params.get("rcpList");
        for (Map<String, String> map : rcpList) {

            if (StringUtils.isEmpty(map.get("notiMasCd")) || StringUtils.isEmpty(map.get("rcpMasCd"))) {
                LOGGER.warn("processRefundReceipt >>> pk is null. notimascd = " + map.get("notiMasCd") + " / rcpmascd = " + map.get("rcpMasCd"));
                continue;
            }
            rcpmascdSet.add(map.get("rcpMasCd"));
            notimascdSet.add(map.get("notiMasCd"));
            notidetcdList.add(map.get("notiDetCd"));
            rcpdetcdList.add(map.get("rcpDetCd"));
        }

        // 청구상세 상태값 PA06으로 변경
        daoParams.clear();
        daoParams.put("makedt", now);
        daoParams.put("chacd", chacd);
        daoParams.put("notiStatus", "PA06");
        daoParams.put("notidetcdList", notidetcdList);
        notiDetCount = directReceiptMgmtDAO.updateXnotiDetSt(daoParams);

        // 청구마스터 환불 상태값 변경
        for (String masCd : notimascdSet) {
            NoticeDetailsExample example = new NoticeDetailsExample();
            example.createCriteria().andNotimascdEqualTo(masCd).andNotiCanYnEqualTo("N").andNotidetstNotEqualTo("PA00");
            long detailCount = noticeDetailsMapper.countByExample(example);
            example = new NoticeDetailsExample();
            example.createCriteria().andNotimascdEqualTo(masCd).andNotiCanYnEqualTo("N").andNotidetstEqualTo("PA06");
            long refundCount = noticeDetailsMapper.countByExample(example);

            daoParams.clear();
            daoParams.put("makedt", now);
            daoParams.put("chacd", chacd);
            daoParams.put("notimascdList", new String[]{masCd});
            if (detailCount == refundCount) {
                // 모두 환불
                daoParams.put("notiStatus", "PA06");
            } else {
                // 일부항목 환불
                daoParams.put("notiStatus", "PA04");
            }
            notiMasCount = directReceiptMgmtDAO.updateXnotiMasSt(daoParams);
        }

        // 수납상세 상태값 PA09으로 변경
        // FIXME 2018. 10. 10. 수납상태는 변경안함.

        // 환불 정보 생성
        int refundCount = 0;
        ReceiptDetailsExample example = new ReceiptDetailsExample();
        example.createCriteria().andRcpdetcdIn(rcpdetcdList);
        final List<ReceiptDetails> rcpDetList = rcpDetailsMapper.selectByExample(example);
        for (ReceiptDetails rcp : rcpDetList) {
            RePayMaster repay = new RePayMaster();
            repay.setMaker(chacd);
            repay.setRcpmascd(rcp.getRcpmascd());
            repay.setRcpdetcd(rcp.getRcpdetcd());
            repay.setRegdt(now);
            repay.setRepayamt(rcp.getPayitemamt());
            repay.setRepayday(repayday);
            refundCount += rePayMasterMapper.insertSelective(repay);
        }

        LOGGER.debug(String.format("환불처리 건수[청구=%d, 청구상세=%d, 환불=%d]", notiMasCount, notiDetCount, refundCount));

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("retCode", "0000");
        result.put("retMsg", "[" + refundCount + "]건이 환불처리 되었습니다.\n현금영수증 발행(요청)건이 있는 경우 취소처리해주세요.");
        return result;
    }

    /**
     * FIXME 수납상태는 변경안함.
     *
     * @modified 2018. 10. 10.
     * @author jhjeong@finger.co.kr
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> processRefundReceiptCancel(Map<String, Object> params) throws Exception {

        // 변경일시
        final Date now = new Date();
        Set<String> notimascdSet = new HashSet<String>();
        Set<String> rcpmascdSet = new HashSet<String>();
        List<String> notidetcdList = new ArrayList<String>();
        List<String> rcpdetcdList = new ArrayList<String>();

        // 상태값 변경용도
        Map<String, Object> daoParams = new HashMap<String, Object>();

        final String chacd = params.get("chacd").toString();

        int notiMasCount = 0;
        int notiDetCount = 0;
        int refundCancelCount = 0;
        if (params.get("rcpList") == null) {
            // 일괄취소
            // 검색조건 생성
            params.put("srchValues", this.convertByLikeClause(params.get("searchValue")));
            params.put("srchGubnValues", this.convertByLikeClause(params.get("cusGubnValue")));

            params.put("orderBy", "month");
            List<RefundRcpDTO> rcpCdList = directReceiptMgmtDAO.selectRefundRcpCatalog(params);

            for (RefundRcpDTO dto : rcpCdList) {
                int count = directReceiptMgmtDAO.deleteRefundCatalog(dto.getRepaymascd(), dto.getRcpmascd(), dto.getRcpdetcd());

                if (count > 0) {
                    // 환불정보 삭제 성공.
                    rcpmascdSet.add(dto.getRcpmascd());
                    rcpdetcdList.add(dto.getRcpdetcd());
                    notimascdSet.add(dto.getNotimascd());
                    notidetcdList.add(dto.getNotidetcd());
                }
                refundCancelCount += count;
            }

        } else {
            // 선택취소
            final ArrayList<Map<String, String>> rcpList = (ArrayList<Map<String, String>>) params.get("rcpList");

            for (Map<String, String> map : rcpList) {

                if (StringUtils.isEmpty(map.get("rePayMasCd")) || StringUtils.isEmpty(map.get("rcpMasCd"))) {
                    LOGGER.warn("processRefundReceiptCancel >>> pk is null. repaymascd = " + map.get("rePayMasCd") + " / rcpmascd = " + map.get("rcpMasCd"));
                    continue;
                }

                int count = directReceiptMgmtDAO.deleteRefundCatalog(map.get("rePayMasCd"), map.get("rcpMasCd"), map.get("rcpDetCd"));

                if (count > 0) {
                    // 환불정보 삭제 성공.
                    rcpmascdSet.add(map.get("rcpMasCd"));
                    rcpdetcdList.add(map.get("rcpDetCd"));
                    notimascdSet.add(map.get("notiMasCd"));
                    notidetcdList.add(map.get("rcpDetCd"));
                }
                refundCancelCount += count;
            }
        }

        // 수납상세 상태값 PA09 >> PA03으로 변경
        // FIXME 수납상태는 변경안함.


        // 청구상세 수납 상태값 변경
        ReceiptDetailsExample rcpExample = new ReceiptDetailsExample();
        rcpExample.createCriteria().andRcpdetcdIn(rcpdetcdList);
        final List<ReceiptDetails> rcpDetailList = rcpDetailsMapper.selectByExample(rcpExample);
        for (ReceiptDetails rcp : rcpDetailList) {
            final long rcpAtm = rcp.getPayitemamt();
            NoticeDetailsExample example = new NoticeDetailsExample();
            example.createCriteria().andNotidetcdEqualTo(rcp.getNotidetcd()).andNotiCanYnEqualTo("N");
            for (NoticeDetails detail : noticeDetailsMapper.selectByExample(example)) {
                daoParams.clear();
                daoParams.put("makedt", now);
                daoParams.put("chacd", chacd);
                daoParams.put("notidetcdList", new String[]{detail.getNotidetcd()});

                if (rcpAtm < detail.getPayitemamt()) {
                    // 일부납으로 변경
                    daoParams.put("notiStatus", "PA04");
                } else if (rcpAtm == detail.getPayitemamt()) {
                    // 완납으로 변경
                    daoParams.put("notiStatus", "PA03");
                } else {
                    // 초과납으로 변경
                    daoParams.put("notiStatus", "PA05");
                }
                notiDetCount += directReceiptMgmtDAO.updateXnotiDetSt(daoParams);
            }
        }

        // 수납마스터 상태값 PA09 >> PA03으로 변경
        // FIXME 수납상태는 변경안함.

        // 청구마스터 수납 상태값 변경
        for (String masCd : notimascdSet) {
            NoticeDetailsExample example = new NoticeDetailsExample();
            example.createCriteria().andNotimascdEqualTo(masCd).andNotiCanYnEqualTo("N").andNotidetstNotEqualTo("PA00");
            long detailCount = noticeDetailsMapper.countByExample(example);
            example = new NoticeDetailsExample();
            example.createCriteria().andNotimascdEqualTo(masCd).andNotiCanYnEqualTo("N").andNotidetstEqualTo("PA03");
            long PA03Count = noticeDetailsMapper.countByExample(example);
            example = new NoticeDetailsExample();
            example.createCriteria().andNotimascdEqualTo(masCd).andNotiCanYnEqualTo("N").andNotidetstEqualTo("PA02");
            long PA02Count = noticeDetailsMapper.countByExample(example);

            daoParams.clear();
            daoParams.put("makedt", now);
            daoParams.put("chacd", chacd);
            daoParams.put("notimascdList", new String[]{masCd});
            if (detailCount == PA03Count) {
                // 완납으로
                daoParams.put("notiStatus", "PA03");
            } else {
                if (detailCount == PA02Count) {
                    daoParams.put("notiStatus", "PA02");
                } else {
                    daoParams.put("notiStatus", "PA04");
                }
            }
            notiMasCount += directReceiptMgmtDAO.updateXnotiMasSt(daoParams);
        }

        LOGGER.debug(String.format("환불취소 건수[청구=%d, 청구상세=%d, 환불취소=%d]", notiMasCount, notiDetCount, refundCancelCount));

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("retCode", "0000");
        result.put("retMsg", "[" + refundCancelCount + "]건에 대한 환불이 취소되었습니다.");

        return result;
    }

    @Override
    public InputStream getRefundReceiptExcel(Map<String, Object> params) throws Exception {

        params.put("srchValues", this.convertByLikeClause(params.get("searchValue")));
        params.put("srchGubnValues", this.convertByLikeClause(params.get("cusGubnValue")));

        params.put("curPage", 1);
        Map<String, Object> result;
        final List<RefundRcpDTO> itemList;

        NotiMgmtBaseDTO baseInfo = notiMgmtDao.selectNotiBaseInfo(params.get("chacd").toString());

        XlsxBuilder xlsxBuilder = new XlsxBuilder();
        xlsxBuilder.newSheet("수기환불내역");

        xlsxBuilder.addHeader(0, 0, "NO");
        xlsxBuilder.addHeader(0, 1, "청구월");
        xlsxBuilder.addHeader(0, 2, "입금일시");

        if (params.get("pageTab").toString().equalsIgnoreCase("refund")) {
            // 직접환불등록 목록조회
            result = directReceiptMgmtDAO.selectRcpCatalogCount(params);
            params.put("pageScale", NumberUtils.toInt(result.get("listcount").toString(), 0));
            itemList = directReceiptMgmtDAO.selectRcpCatalog(params);

            xlsxBuilder.addHeader(0, 3, "고객명");
            xlsxBuilder.addHeader(0, 4, "가상계좌");
            xlsxBuilder.addHeader(0, 5, baseInfo.getCusGubn1());
            xlsxBuilder.addHeader(0, 6, baseInfo.getCusGubn2());
            xlsxBuilder.addHeader(0, 7, baseInfo.getCusGubn3());
            xlsxBuilder.addHeader(0, 8, baseInfo.getCusGubn4());
            xlsxBuilder.addHeader(0, 9, "청구항목");
            xlsxBuilder.addHeader(0, 10, "수납상태");
            xlsxBuilder.addHeader(0, 11, "입금금액");
            xlsxBuilder.addHeader(0, 12, "청구금액");
            xlsxBuilder.addHeader(0, 13, "입금수단");

        } else {
            // 직접환불완료 목록조회
            result = directReceiptMgmtDAO.selectRefundRcpCatalogCount(params);
            params.put("pageScale", NumberUtils.toInt(result.get("listcount").toString(), 0));
            itemList = directReceiptMgmtDAO.selectRefundRcpCatalog(params);

            xlsxBuilder.addHeader(0, 3, "환불일자");
            xlsxBuilder.addHeader(0, 4, "고객명");
            xlsxBuilder.addHeader(0, 5, "가상계좌");
            xlsxBuilder.addHeader(0, 6, baseInfo.getCusGubn1());
            xlsxBuilder.addHeader(0, 7, baseInfo.getCusGubn2());
            xlsxBuilder.addHeader(0, 8, baseInfo.getCusGubn3());
            xlsxBuilder.addHeader(0, 9, baseInfo.getCusGubn4());
            xlsxBuilder.addHeader(0, 10, "청구항목");
            xlsxBuilder.addHeader(0, 11, "환불금액");
            xlsxBuilder.addHeader(0, 12, "입금금액");
            xlsxBuilder.addHeader(0, 13, "청구금액");
            xlsxBuilder.addHeader(0, 14, "입금수단");
        }

        Map<String, String> sveMap = new HashMap<String, String>();
        sveMap.put("VAS", "가상계좌");
        sveMap.put("DCS", "현금");
        sveMap.put("DCD", "오프라인카드");
        sveMap.put("DVA", "계좌입금");
        sveMap.put("OCD", "인터넷카드");
        sveMap.put("PA02", "미납");
        sveMap.put("PA03", "완납");
        sveMap.put("PA04", "일부납");
        sveMap.put("PA05", "초과납");
        sveMap.put("PA06", "환불");


        for (RefundRcpDTO each : itemList) {
            xlsxBuilder.newDataRow();
            xlsxBuilder.addData(0, each.getRn());
            xlsxBuilder.addData(1, StrUtil.dotDate(each.getMasmonth()));
            final Date date = fmtFullTime.parse(each.getPayday() + " " + each.getPaytime());
            xlsxBuilder.addData(2, fmtPayDateTime.format(date));

            if (params.get("pageTab").toString().equalsIgnoreCase("refund")) {
                // 직접환불등록
                xlsxBuilder.addData(3, each.getCusname());
                xlsxBuilder.addData(4, each.getVano());
                xlsxBuilder.addData(5, each.getCusgubn1());
                xlsxBuilder.addData(6, each.getCusgubn2());
                xlsxBuilder.addData(7, each.getCusgubn3());
                xlsxBuilder.addData(8, each.getCusgubn4());
                xlsxBuilder.addData(9, each.getPayitemname());
                xlsxBuilder.addData(10, sveMap.get(each.getNotimasst()));
                xlsxBuilder.addData(11, each.getRcppayitemamt());
                xlsxBuilder.addData(12, each.getPayitemamt());
                xlsxBuilder.addData(13, sveMap.get(each.getSvecd()));
            } else {
                // 환불완료
                xlsxBuilder.addData(3, StrUtil.dotDate(each.getRepayday()));
                xlsxBuilder.addData(4, each.getCusname());
                xlsxBuilder.addData(5, each.getVano());
                xlsxBuilder.addData(6, each.getCusgubn1());
                xlsxBuilder.addData(7, each.getCusgubn2());
                xlsxBuilder.addData(8, each.getCusgubn3());
                xlsxBuilder.addData(9, each.getCusgubn4());
                xlsxBuilder.addData(10, each.getPayitemname());
                xlsxBuilder.addData(11, each.getRefundamt());
                xlsxBuilder.addData(12, each.getRcppayitemamt());
                xlsxBuilder.addData(13, each.getPayitemamt());
                xlsxBuilder.addData(14, sveMap.get(each.getSvecd()));
            }
        }

        final File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".tmp");
        xlsxBuilder.writeTo(new FileOutputStream(tempFile));
        return new FileInputStream(tempFile);
    }

    /**
     * like 쿼리용 iterator 생성
     *
     * @param parameter
     * @return
     * @author jhjeong@finger.co.kr
     * @modified 2018. 9. 20.
     */
    private ArrayList<String> convertByLikeClause(Object parameter) {
        try {
            if (parameter == null) return null;
            if (parameter.toString().trim() == "") return null;
            Set<String> values = new HashSet<>();
            for (String value : StringUtils.split(parameter.toString(), ",")) {
                values.add(value.trim());
            }

            return new ArrayList<String>(values);
        } catch (Exception e) {
            LOGGER.warn(e.toString(), e);
            return null;
        }
    }

    private String getConfirm(String confirm, String identityNo) {
        if (StringUtils.length(identityNo) == 10 && !identityNo.startsWith("0")) {
            // 사업자 등록번호
            return "21";
        } else if (StringUtils.length(identityNo) >= 16) {
            // 카드 번호
            return "12";
        } else {
            // 전화번호
            return "11";
        }
    }

    private String getCusType(String cusType, String identityNo) {
        if (StringUtils.length(identityNo) == 10 && !identityNo.startsWith("0")) {
            // 사업자 등록번호
            return "2";
        } else {
            return "1";
        }
    }
}
