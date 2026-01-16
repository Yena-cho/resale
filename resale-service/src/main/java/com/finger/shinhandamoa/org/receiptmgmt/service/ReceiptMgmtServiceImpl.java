package com.finger.shinhandamoa.org.receiptmgmt.service;

import com.finger.shinhandamoa.common.CashUtil;
import com.finger.shinhandamoa.common.Maps;
import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.common.XlsxBuilder;
import com.finger.shinhandamoa.data.table.mapper.CashHstMapper;
import com.finger.shinhandamoa.data.table.model.CashHst;
import com.finger.shinhandamoa.org.receiptmgmt.ReceiptMgmtDAO;
import com.finger.shinhandamoa.org.receiptmgmt.dto.*;
import com.finger.shinhandamoa.util.dto.CodeDTO;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 수납관리
 *
 * @author suhlee
 */
@Service
public class ReceiptMgmtServiceImpl implements ReceiptMgmtService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptMgmtServiceImpl.class);
    
    private static final SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    DefaultTransactionDefinition def = null;
    TransactionStatus status = null;
    
    @Resource(name = "receiptMgmtDao")
    private ReceiptMgmtDAO receiptMgmtDao;
    
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private CashHstMapper cashHstMapper;

    /**
     * 수납내역조회
     */
    @Override
    public List<ReceiptMgmtDTO> getReceiptList(Map<String, Object> map) throws Exception {
        return receiptMgmtDao.getReceiptList(map);
    }

    /**
     * 수납내역조회 카운트
     */
    @Override
    public HashMap<String, Object> getReceiptCount(Map<String, Object> map) throws Exception {
        return receiptMgmtDao.getReceiptCount(map);
    }

    /**
     * 카드결제 취소(환불)
     *
     * @param map
     * @return
     */
    @Override
    @Transactional
    public int insertRepay(Map<String, Object> map) throws Exception {
        int flag = 1;

        if (receiptMgmtDao.updateXnotiMasSt(map) <= 0 || receiptMgmtDao.updateXnotiDetSt(map) <= 0 ||
                receiptMgmtDao.insertRepay(map) <= 0) flag = 0;

        return flag;
    }

    /**
     * 영수증 출력시 XCHAOPTION 테이블 조회해서 refcd 값 추출
     *
     * @param chaCd
     * @return
     * @throws Exception
     */
    @Override
    public String getXchaoption(String chaCd) throws Exception {
        return receiptMgmtDao.getXchaoption(chaCd);
    }

    /**
     * GETXCHAOPTION 테이블이   refcd값이 01
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public List<PdfReceiptMgmtDTO> selectRcpBillCutDet(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.selectRcpBillCutDet(map);
    }

    /**
     * GETXCHAOPTION 테이블이   refcd값이 01 아닌경우
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public List<PdfReceiptMgmtDTO> selectRcpBillCut(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.selectRcpBillCut(map);
    }

    /**
     * 교육비납입증명서
     *
     * @param map
     * @return
     */
    @Override
    public List<PdfReceiptMgmtDTO> getEduList(HashMap<String, Object> map) throws Exception {
        // 증명서에 대한 정보 목록
        // 고객(VANO) / 연도
        final List<PdfReceiptMgmtDTO> masterList = receiptMgmtDao.getEduList(map);

        for (PdfReceiptMgmtDTO each : masterList) {
            // 증명서 세부내용
            // 조회를 VANO/연도
            // ==> 월별 납부 내역
            final String vano = each.getVano();
            final String paymentYear = each.getPaymentYear();
            final ArrayList<String> checkList = (ArrayList<String>)map.get("notiMasList");

            final List<PdfReceiptDetailsDTO> itemList = receiptMgmtDao.selectEduDetailsList(vano, paymentYear, checkList);
            each.setDetailsList(itemList);
        }
        return masterList;
    }

    /**
     * 장기요양급여 납부확인서
     *
     * @param map
     * @return
     */
    @Override
    public List<PdfReceiptMgmtDTO> getRecpList(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getRecpList(map);
    }

    /**
     * 장기요양 계좌리스트
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public List<PdfReceiptMgmtDTO> getRecpAccNoList(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getRecpAccNoList(map);
    }

    /**
     * 기부금 영수증
     */
    @Override
    public List<PdfReceiptMgmtDTO> getDntnList(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getDntnList(map);
    }

    /**
     * 수납내역 조회
     *
     * @return
     * @throws Exception
     */
    @Override
    public ReceiptMgmtDTO selectRcpMas(HashMap<String, Object> reqMap) throws Exception {
        return receiptMgmtDao.selectRcpMas(reqMap);
    }

    /**
     * 카드취소대상 원장코드 조회
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReceiptMgmtDTO getCancelRcp(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getCancelRcp(map);
    }

    /**
     * 수납정보 수정
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public int updateRcpMas(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.updateRcpMas(map);
    }


    /**
     * 수납상세항목 수정
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public void updateRcpDet(HashMap<String, Object> map) throws Exception {
        receiptMgmtDao.updateRcpDet(map);
    }

    /**
     * 고지정보 수정
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public void updateNotiBill(HashMap<String, Object> map) throws Exception {
        receiptMgmtDao.updateNotiBill(map);
    }

    /**
     * 현장수납 카운트
     */
    @Override
    public HashMap<String, Object> getActCount(Map<String, Object> map) throws Exception {
        return receiptMgmtDao.getActCount(map);
    }

    /**
     * 현장수납 카운트 완납
     */
    @Override
    public HashMap<String, Object> getActCount2(Map<String, Object> map) throws Exception {
        return receiptMgmtDao.getActCount2(map);
    }

    /**
     * 현장수납 리스트
     */
    @Override
    public List<ReceiptMgmtDTO> getActList(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getActList(map);
    }

    /**
     * 현장수납 리스트
     */
    @Override
    public List<ReceiptMgmtDTO> getActList2(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getActList2(map);
    }

    /**
     * 직접수납관리 INSERT, UPDATE
     *
     * @param map
     * @param nDto
     * @return
     * @throws SQLException
     * @throws Exception
     */
    @Transactional
    public boolean amtUpdate(HashMap<String, Object> map, NotiDTO nDto) throws SQLException, Exception {
        boolean flag = true;

        try {
            def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);

            // 청구 master update
            HashMap<String, Object> xRcpmap = new HashMap<String, Object>();
            long inputAmt = Long.parseLong(map.get("rcpAmt").toString());
            long cashAmt = 0; // 현금역수증 처리 금액
            CashUtil cashUtil = new CashUtil();

            if (updateXnotiMas(map) > 0) {
                // insert
                xRcpmap.put("rcpMasCd", map.get("rcpMasCd"));
                xRcpmap.put("sveCd", map.get("sveCd"));
                xRcpmap.put("rcpAmt", map.get("rcpAmt"));
                xRcpmap.put("afterCd", "PA03");
                xRcpmap.put("chaCd", map.get("chaCd"));
                xRcpmap.put("notiMasCd", map.get("notiMasCd"));

                if (map.get("type").equals("I")) {
                    map.put("gubun", "A");
                    List<ReceiptMgmtDTO> rcpList = receiptMgmtDao.getXnotiState(map);
                    if (insertXrcpMas(xRcpmap) <= 0)
                        flag = false; //transactionManager.rollback(status);// 수납 master 88insert

                    long remainAmt = 0;// 잔액
                    String notiDetSt = ""; // 청구상태코드
                    long payItemAmt = 0;// 수납 금액
                    // 수납 상세
                    for (ReceiptMgmtDTO dto : rcpList) {
                        if (dto.getNotiDetSt().equals("PA03")) continue;

                        long remAmt = dto.getRemAmt();
                        remainAmt = inputAmt - remAmt;
                        inputAmt = remainAmt;
                        LOGGER.info("remainAmt " + remainAmt);
                        //고객입금액과 청구항목 금액이 동일하면 청구항목의 상태를 'PA03'(완납)으로 업데이트
                        if (remainAmt >= 0) {
                            notiDetSt = "PA03";
                            payItemAmt = remAmt;
                            LOGGER.info("payItemAmt00 " + payItemAmt);
                        } else if (remainAmt < 0) { // 청구항목보다 작으면 나머지금액으로 수납 / 일부납 PA04
                            notiDetSt = "PA04";
                            payItemAmt = 0;

                            LOGGER.info("111 >> " + remAmt);
                            LOGGER.info("112 >> " + inputAmt);
                            payItemAmt = remAmt + inputAmt;
                            LOGGER.info("payItemAmt11 " + payItemAmt);
                        }

                        xRcpmap.put("notiDetCd", dto.getNotiDetCd());
                        xRcpmap.put("payItemAmt", payItemAmt);
                        xRcpmap.put("notiDetSt", notiDetSt);
                        xRcpmap.put("payItemCd", dto.getPayItemCd());

                        // 현금영수증 발행처리 Y 인것만 금액 합산
                        if (dto.getRcpItemYn().equals("Y")) {
                            cashAmt += payItemAmt;
                        }

                        // 수납 상세 insert
                        if (insertXrcpDet(xRcpmap) <= 0) flag = false;

                        // 청구 상세 update
                        if (updateXnotiDet(xRcpmap) <= 0) flag = false;

                        // 고객입금액이 청구항목 금액에 반영된 후 더이상 잔액이 없으면 END.
                        if (remainAmt <= 0) {
                            break;
                        }
                    }
                    //현금영수증 발급 (XRCPDET 테이블의  RCPITEMYN : Y 인것만 합산해서 현금영수증 처리)
                    if (nDto != null) {
                        // 현금이 0 원일때는 발급하지 않는다.
                        if (cashAmt > 0) {
                            Date now = new Date();
                            String date = StrUtil.toDTType(now);
                            Map<String, Object> param = Maps.hashmap();

                            param.put("chaCd", nDto.getChacd());
                            param.put("chaOffNo", StrUtil.nullToVoid(nDto.getChaoffno()));
                            param.put("cusOffNo", StrUtil.nullToVoid(nDto.getCusOffNo()));
                            param.put("cusType", StrUtil.nullToVoid(nDto.getCusType()));
                            param.put("confirm", StrUtil.nullToVoid(nDto.getConfirm()));
                            param.put("amtChkTy", StrUtil.nullToVoid(nDto.getAmtChkTy()));
                            param.put("noTaxYn", StrUtil.nullToVoid(nDto.getNoTaxYn()));
                            param.put("rcpReqTy", StrUtil.nullToVoid(nDto.getRcpReqTy()));
                            param.put("chaTrTy", StrUtil.nullToVoid(nDto.getChatrty()));
                            param.put("rcpMasCd", map.get("rcpMasCd"));  //원장코드
                            //String amount = map.get("rcpAmt").toString(); // cashUtil.findAmount()
                            param.put("rcpAmt", cashAmt);  //거래금액
                            param.put("tip", "0");  //봉사료
                            param.put("tax", cashUtil.tax(String.valueOf(cashAmt), nDto.getNoTaxYn()));  //부가세
                            //param.put("job", "I");  //작업구분 I:신규 U:재발행 D:취소
                            param.put("cashMasSt", "ST02");  //상태 ST02:미발행, ST03:발행
                            param.put("makeDt", date);  //조작일시
                            param.put("maker", map.get("chaCd"));  //조작자
                            param.put("regdt", date);  //등록일시
                            LOGGER.info("param >>>>>> " + param);
                            if (insertCashMaster(param) <= 0) flag = false;
                        }

                    }
                }
                if (map.get("type").equals("U")) { //update
                    // 수납 master update
                    ReceiptMgmtDTO isRcpAmt = new ReceiptMgmtDTO();
                    ReceiptMgmtDTO rcpCd = new ReceiptMgmtDTO();
                    rcpCd = receiptMgmtDao.getRcpCd(xRcpmap);

                    if (updateXrcpMas(xRcpmap) <= 0) flag = false;

                    LOGGER.info("rcpCd.getRcpAmt() " + rcpCd.getRcpAmt());
                    long oldRcpAmt = rcpCd.getRcpAmt(); // 기존 금액
                    long addRcpAmt = 0; // 기존에 존재하는 직접입력금을 수정하는 경우 추가금액을 저장하기 위한 변수
                    addRcpAmt = inputAmt - oldRcpAmt; //기존에 존재하는 직접입력금을 수정하는 경우 추가금액을 구하는 공식 10,000=10,000-15,000
                    inputAmt = addRcpAmt; // 10,000
                    cashAmt = 0;
                    if (addRcpAmt < 0) {
                        // 고객 입금액이 200,000 이었던 금액 으로 20,000 원으로 수정한 경우
                        LOGGER.info("addRcpAmt <<<<<< " + addRcpAmt);
                        map.put("gubun", "D");
                        List<ReceiptMgmtDTO> rcpList = receiptMgmtDao.getXnotiState(map);
                        long remainAmt = 0; //잔액
                        for (ReceiptMgmtDTO dto : rcpList) {
                            xRcpmap.put("payItemCd", dto.getPayItemCd());
                            xRcpmap.put("notiDetCd", dto.getNotiDetCd());

                            LOGGER.info("xRcpmap >>> " + xRcpmap.get("payItemCd") + " rcpmascd >> " + xRcpmap.get("rcpMasCd"));

                            if (dto.getNotiDetSt().equals("PA02")) continue;

                            isRcpAmt = receiptMgmtDao.getRcpAmt(xRcpmap);
                            LOGGER.info("rcpAmt >> " + isRcpAmt);
                            if (isRcpAmt == null) {
                                continue;
                            } else {
                                long rcptAmt = Long.parseLong(isRcpAmt.getPayItemAmt().toString());
                                remainAmt = inputAmt + rcptAmt; // 잔액 = 입력금액 + 수납금액
                                if (remainAmt <= 0) { // 미납처리
                                    LOGGER.info("xRcpmap >>> " + xRcpmap);
                                    if (remainAmt == 0) {
                                        if (receiptMgmtDao.deleteRcpmas(xRcpmap) <= 0) flag = false;
                                    }
                                    if (receiptMgmtDao.deleteRcpdet(xRcpmap) <= 0) flag = false;
                                    inputAmt = remainAmt;
                                    // 미납처리하기전에 마이너스 입금처리할때 다른 결제방법도 값체크후 PA04처리 해야함!!20180618
                                    // 현금입금시 오프라인 카드 체크
                                    if (map.get("sveCd").equals("DCS")) {
                                        if (dto.getDcdAmt() > 0) {
                                            xRcpmap.put("notiDetSt", "PA04");
                                        } else {
                                            xRcpmap.put("notiDetSt", "PA02");
                                        }
                                    } else { // sveCd : DCD
                                        if (dto.getDcsAmt() > 0) {
                                            xRcpmap.put("notiDetSt", "PA04");
                                        } else {
                                            xRcpmap.put("notiDetSt", "PA02");
                                        }
                                    }

                                    // 청구상세 update
                                    if (updateXnotiDet(xRcpmap) <= 0) flag = false;

                                    // 고객입금액이 청구항목 금액에 반영된 후 더이상 잔액이 없으면 END.
                                } else { // 일부납처리
                                    LOGGER.info("inputAmt >> " + inputAmt);
                                    LOGGER.info("rcptAmt >> " + rcptAmt);
                                    LOGGER.info("remainAmt >> " + remainAmt);
                                    LOGGER.info("oldRcpAmt >> " + oldRcpAmt);
                                    LOGGER.info("addRcpAmt >> " + addRcpAmt);
                                    xRcpmap.put("notiDetSt", "PA04");
                                    //remainAmt = oldRcpAmt + addRcpAmt;
                                    //remainAmt = rcptAmt + addRcpAmt;
                                    // 납부금액 - 추가금액
                                    xRcpmap.put("payItemAmt", remainAmt);
                                    LOGGER.info("map >>> " + map);

                                    // 현금영수증 발행처리 Y 인것만 금액 합산
                                    if (dto.getRcpItemYn().equals("Y")) {
                                        cashAmt += remainAmt;
                                    }

                                    if (receiptMgmtDao.updateXrcpDet(xRcpmap) <= 0) flag = false;

                                    // 청구상세 update
                                    if (updateXnotiDet(xRcpmap) <= 0) flag = false;
                                }

                            }
                            if (remainAmt >= 0) break;
                        }
                    } else { // + 금액으로 수정한 경우
                        LOGGER.info("addRcpAmt >>>>>>> " + addRcpAmt);
                        map.put("gubun", "A");
                        List<ReceiptMgmtDTO> rcpList = receiptMgmtDao.getXnotiState(map);
                        long remainAmt = 0; // 잔액(나머지금액) = 고객입금액 - 미납액 계산으로 산출된 결과 금액
                        int index = 0;
                        for (ReceiptMgmtDTO dto : rcpList) {
                            index++;
                            LOGGER.info("rcpMas >> " + dto.getRcpMasCd());
                            LOGGER.info("NotiDetCd >>> " + dto.getNotiDetCd());
                            LOGGER.info("PayItemCd >> " + dto.getPayItemCd());
                            LOGGER.info("NotiDetSt >> " + dto.getNotiDetSt());
                            long remAmt = dto.getRemAmt(); // 미납금액 400
                            if (dto.getNotiDetSt().equals("PA03")) continue;

                            remainAmt = inputAmt - remAmt; // 고객입금액 -  미납금액  9600 = 10000-400
                            //고객입금액과 청구항목 금액이 동일하면 청구항목의 상태를 'PA03'(완납)으로 업데이트
                            if (remainAmt >= 0) {
                                long notiPayItemAmt = 0;
                                ReceiptMgmtDTO rcpAmt = new ReceiptMgmtDTO();
                                xRcpmap.put("notiDetCd", dto.getNotiDetCd());
                                xRcpmap.put("payItemCd", dto.getPayItemCd());

                                rcpAmt = receiptMgmtDao.getRcpAmt(xRcpmap);
                                if (rcpAmt != null) {
                                    LOGGER.info("payAmt > " + Long.parseLong(rcpAmt.getPayItemAmt().toString()));
                                    LOGGER.info("remAmt >> " + remAmt);
                                    notiPayItemAmt = remAmt + Long.parseLong(rcpAmt.getPayItemAmt().toString()); //45,000
                                } else {
                                    //notiPayItemAmt = Integer.parseInt(dto.getPayItemAmt().toString());
                                    notiPayItemAmt = remAmt;
                                }
                                //고객입금액과 청구항목금액이 동일하므로 청구항목의 미납금액을  을 XRCPDET 테이블에  수납상태 "PA03" UPDATE
                                xRcpmap.put("payItemAmt", notiPayItemAmt);
                                xRcpmap.put("notiDetSt", "PA03");
                                inputAmt = remainAmt; // 청구항목으로 차감후 남은 금액을 고객입력금 변수에 입력
                            } else {
                                xRcpmap.put("notiDetSt", "PA04");
                                long rcpItemAmt = 0;    // 기존 청구항목의 수납액 변수
                                rcpItemAmt = dto.getRcpItemAmt();
                                // 고객입금액 -미납금액 = 음수 이면 잔액이 부족한 경우이므로 "수납액 +  고객입금액"으로 수납액을 UPDATE
                                //rcpItemAmp = rcpItemAmp + inputAmt;
                                LOGGER.info("rcpItemAmp >> " + rcpItemAmt);
                                LOGGER.info("inputAmt >> " + inputAmt);
                                LOGGER.info("oldRcpAmt >>> " + oldRcpAmt);
                                if (index == 1) {
                                    rcpItemAmt = inputAmt + oldRcpAmt;
                                } else if (index > 1) {
                                    rcpItemAmt = inputAmt;
                                }
                                LOGGER.info("rcpItemAmp >> " + rcpItemAmt);
                                xRcpmap.put("payItemAmt", rcpItemAmt);

                            }
                            LOGGER.info("remainAmt >>> " + remainAmt);
                            xRcpmap.put("notiDetCd", dto.getNotiDetCd());
                            xRcpmap.put("payItemCd", dto.getPayItemCd());
                            // 수납상세 update
                            // 수납 상세 insert
                            ReceiptMgmtDTO rcpAmt = new ReceiptMgmtDTO();
                            rcpAmt = receiptMgmtDao.getRcpAmt(xRcpmap);
                            LOGGER.info("rcpAmt " + rcpAmt);

                            // 현금영수증 발행처리 Y 인것만 금액 합산
                            if (dto.getRcpItemYn().equals("Y")) {
                                cashAmt += Integer.parseInt(xRcpmap.get("payItemAmt").toString());
                            }

                            if (rcpAmt != null) {
                                if (updateXrcpDet(xRcpmap) <= 0) flag = false;
                            } else {
                                if (insertXrcpDet(xRcpmap) <= 0) flag = false;
                            }

                            // 청구상세 update
                            if (updateXnotiDet(xRcpmap) <= 0) flag = false;

                            // 고객입금액이 청구항목 금액에 반영된 후 더이상 잔액이 없으면 END.
                            if (remainAmt <= 0) {
                                break;
                            }
                        }
                    }
                    //현금영수증 재발행
                    if (nDto != null) { // 금액 0원되면 현금영수증 데이터 삭제
                        // 현금이 0 원일때는 발급하지 않는다.
                        Date now = new Date();
                        String date = StrUtil.toDTType(now);
                        Map<String, Object> param = Maps.hashmap();
                        // select
                        param.put("chaCd", nDto.getChacd());
                        param.put("rcpMasCd", map.get("rcpMasCd"));  //원장코드

                        // JOB IS NULL -> cusOffNo, rcpAmt UPDATE
                        List<ReceiptMgmtDTO> clist = receiptMgmtDao.selectFlagCashMas(map);

                        LOGGER.debug("clisrt >>>>>>>>>>>>> " + clist.size());

                        if (clist.size() > 0) { // 현금영수증 수정인경우
                            if (clist.get(0).getJob() != null) { // 발행, 재발행한경우
                                LOGGER.debug("1111111111111111111111 >>>>");
                                ReceiptMgmtDTO vo = receiptMgmtDao.selectCashMas(map);
                                if (vo != null) {
                                    param.put("checkUpdate", "U2");
                                    param.put("cusOffNo", vo.getCusOffNo2());
                                    param.put("confirm", vo.getConfirm2());
                                    param.put("rcpAmt", vo.getRcpAmt2());  //거래금액
                                } else {
                                    param.put("checkUpdate", "U");
                                }
                                param.put("cusOffNo2", nDto.getCusOffNo());
                                param.put("confirm2", nDto.getConfirm());
                                param.put("rcpAmt2", cashAmt);  //거래금액

                                param.put("cusType", nDto.getCusType());
                                param.put("tax", cashUtil.tax(String.valueOf(cashAmt), nDto.getNoTaxYn()));  //부가세
                                param.put("cashMasSt", "ST02");  //상태 ST02:미발행, ST03:발행
                                param.put("makeDt", date);  //조작일시
                                param.put("maker", map.get("chaCd"));  //조작자
                                LOGGER.info("update param >>>>>> " + param);

                                if (cashAmt <= 0) {
                                    if (deleteCashMaster(param) <= 0) flag = false;
                                } else {
                                    if (updateCashMaster(param) <= 0) flag = false;
                                }
                            } else { // 한번도 발행하지 않은 경우
                                param.put("checkUpdate", "U3");
                                param.put("rcpAmt", cashAmt);  //거래금액
                                param.put("tax", cashUtil.tax(String.valueOf(cashAmt), nDto.getNoTaxYn()));  //부가세
                                param.put("makeDt", date);  //조작일시
                                param.put("maker", map.get("chaCd"));  //조작자
                                param.put("cashMasSt", "ST02");
                                /*cusOffNo2
								rcpAmt2
								confirm2
								confirm2*/
                                if (cashAmt <= 0) {
                                    if (deleteCashMaster(param) <= 0) flag = false;
                                } else {
                                    if (updateCashMaster(param) <= 0) flag = false;
                                }
                            }

                        } else {
                            if (cashAmt > 0) {
                                map.put("rcpAmt", cashAmt);  //거래금액
                                map.put("tax", cashUtil.tax(String.valueOf(cashAmt), nDto.getNoTaxYn()));  //부가세
                                LOGGER.debug("1111112222222222222222222 map >>>> " + map);

                                if (receiptMgmtDao.updateCashMasJobNull(map) <= 0) flag = false;
                            }
                        }


                    }
                }
            } else {
                flag = false;
                transactionManager.rollback(status);

            }
        } catch (Exception e) {
            // TODO: handle exception
            flag = false;
            transactionManager.rollback(status);
            e.printStackTrace();
            throw e;

        }

        transactionManager.commit(status);

        return flag;
    }

    /**
     * insert 시에는 0원은 저장 할 수 없고
     * 수정시에는 -금액처리로 0원 처리 할 수 있다(0원시 수납마스터, 수납상세 delete).
     */
    @Override
    public boolean updateAct(HashMap<String, Object> map, NotiDTO nDto) throws SQLException, Exception {

        boolean flag = true;
        LOGGER.info("map >>> " + map);
        if (map.get("dcsAmt").equals("0")) {
            map.put("rcpAmt", map.get("dcsAmt"));
            map.put("sveCd", "DCS");
            ReceiptMgmtDTO rcpCd = receiptMgmtDao.getRcpCd(map);
            if (rcpCd != null) {
                map.put("rcpMasCd", rcpCd.getRcpMasCd());
                map.put("type", "U");

                if (Integer.parseInt(map.get("dcsAmt").toString()) != rcpCd.getRcpAmt()) {
                    flag = amtUpdate(map, nDto);
                }
            }
        } else {
            map.put("rcpAmt", map.get("dcsAmt"));
            map.put("sveCd", "DCS");
            ReceiptMgmtDTO rcpCd = receiptMgmtDao.getRcpCd(map);
            if (rcpCd == null) {
                String seqCd = receiptMgmtDao.getSeq(map).getSeqCd();
                map.put("rcpMasCd", seqCd);
                map.put("type", "I");
                flag = amtUpdate(map, nDto);
            } else {
                map.put("rcpMasCd", rcpCd.getRcpMasCd());
                map.put("type", "U");

                if (Integer.parseInt(map.get("dcsAmt").toString()) != rcpCd.getRcpAmt()) {
                    flag = amtUpdate(map, nDto);
                }
            }
        }

        if (map.get("dcdAmt").equals("0")) {
            map.put("rcpAmt", map.get("dcdAmt"));
            map.put("sveCd", "DCD");
            ReceiptMgmtDTO rcpCd = receiptMgmtDao.getRcpCd(map);
            if (rcpCd != null) {
                map.put("rcpMasCd", rcpCd.getRcpMasCd());
                map.put("type", "U");
                if (Integer.parseInt(map.get("dcdAmt").toString()) != rcpCd.getRcpAmt()) {
                    flag = amtUpdate(map, null);
                }
            }
        } else {
            map.put("rcpAmt", map.get("dcdAmt"));
            map.put("sveCd", "DCD");
            ReceiptMgmtDTO rcpCd = receiptMgmtDao.getRcpCd(map);
            if (rcpCd == null) {
                String seqCd = receiptMgmtDao.getSeq(map).getSeqCd();
                map.put("rcpMasCd", seqCd);
                map.put("type", "I");
                flag = amtUpdate(map, null);
            } else {
                map.put("rcpMasCd", rcpCd.getRcpMasCd());
                map.put("type", "U");
                if (Integer.parseInt(map.get("dcdAmt").toString()) != rcpCd.getRcpAmt()) {
                    flag = amtUpdate(map, null);
                }
            }
        }

        return flag;
    }

    /**
     * 현장수납관리 수납테이블에 저장
     *
     * @param map
     * @return
     */
    @Override
    public int insertXrcpMas(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.insertXrcpMas(map);
    }

    /**
     * 현장수납관리 수납상세테이블에 저장
     *
     * @param map
     * @return
     */
    @Override
    @Transactional
    public int insertXrcpDet(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.insertXrcpDet(map);
    }

    /**
     * 현장수납관리 수납테이블에 수정
     *
     * @param map
     * @return
     */
    @Override
    @Transactional
    public int updateXrcpMas(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.updateXrcpMas(map);
    }

    /**
     * 현장수납관리 수납상세테이블에 수정
     *
     * @param map
     * @return
     */
    @Override
    @Transactional
    public int updateXrcpDet(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.updateXrcpDet(map);
    }

    /**
     * 현장수납관리 청구테이블 update
     *
     * @param map
     * @return
     */
    @Override
    public int updateXnotiMas(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.updateXnotiMas(map);
    }

    /**
     * 현장수납관리 청구상세테이블 update
     *
     * @param map
     * @return
     */
    @Override
    public int updateXnotiDet(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.updateXnotiDet(map);
    }

    /**
     * 시퀀스
     *
     * @param map
     * @return
     */
    @Override
    public ReceiptMgmtDTO getSeq(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getSeq(map);
    }

    /**
     * 환불가능 내역 조회 AMTCHKTY :[IR방식:Y]
     *
     * @param map
     * @return
     */
    @Override
    public List<ReceiptMgmtDTO> getReplyYList(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getReplyYList(map);
    }

    /**
     * 환불가능 내역 조회 AMTCHKTY :[SET방식:N]
     *
     * @param map
     * @return
     */
    @Override
    public List<ReceiptMgmtDTO> getReplyNList(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getReplyNList(map);
    }

    /**
     * 환불완료 내역 조회 AMTCHKTY :[IR방식:Y]
     *
     * @param map
     * @return
     */
    @Override
    public List<ReceiptMgmtDTO> getEndReplyYList(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getEndReplyYList(map);
    }

    /**
     * 환불완료 내역 조회 AMTCHKTY :[SET방식:N]
     *
     * @param map
     * @return
     */
    @Override
    public List<ReceiptMgmtDTO> getEndReplyNList(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getEndReplyNList(map);
    }

    /**
     * 환불가능 내역 카운트 조회 AMTCHKTY :[IR방식:Y]
     *
     * @param map
     * @return
     */
    @Override
    public HashMap<String, Object> getReplyYCount(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getReplyYCount(map);
    }

    /**
     * 환불가능 내역 카운트 조회 AMTCHKTY :[SET방식:N]
     *
     * @param map
     * @return
     */
    @Override
    public HashMap<String, Object> getReplyNCount(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getReplyNCount(map);
    }

    /**
     * 환불완료 내역 카운트 조회 AMTCHKTY :[IR방식:Y]
     *
     * @param map
     * @return
     */
    @Override
    public HashMap<String, Object> getEndReplyYCount(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getEndReplyYCount(map);
    }

    /**
     * 환불완료 내역 카운트 조회 AMTCHKTY :[SET방식:N]
     *
     * @param map
     * @return
     */
    @Override
    public HashMap<String, Object> getEndReplyNCount(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getEndReplyNCount(map);
    }

    /**
     * 현금영수증 내역 카운트
     *
     * @param map
     * @return
     */
    @Override
    public HashMap<String, Object> getCashMasCount(Map<String, Object> map) throws Exception {
        return receiptMgmtDao.getCashMasCount(map);
    }

    /**
     * 현금영수증 내역 리스트
     *
     * @param map
     * @return
     */
    @Override
    public List<ReceiptMgmtDTO> getCashMasList(Map<String, Object> map) throws Exception {
        return receiptMgmtDao.getCashMasList(map);
    }

    /**
     * 현금영수증 발행
     */
    @Override
    @Transactional
    public int insertCashMas(HashMap<String, Object> map) throws Exception {
        CashHst cashHst = new CashHst();
        cashHst.setChacd(String.valueOf(map.get("chaCd")));
        cashHst.setCashmascd(String.valueOf(map.get("cashMasCd")));
        cashHst.setRequestUser(String.valueOf(map.get("chaCd")));
        cashHst.setRequestDate(new Date());
        cashHst.setRequestTypeCd(String.valueOf(map.get("job")));
        cashHst.setResultCd("3");
        cashHst.setCusoffno(String.valueOf(map.get("cusOffNo")));
        cashHst.setRcpamt((Long) map.get("rcpAmt"));
        cashHst.setTax((Long) map.get("tax"));
        cashHstMapper.insertSelective(cashHst);

        return receiptMgmtDao.insertCashMas(map);
    }

    /**
     * 현금영수증 재발행
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public int updateCashMas(HashMap<String, Object> map) throws Exception {
        CashHst cashHst = new CashHst();
        cashHst.setChacd(String.valueOf(map.get("chaCd")));
        cashHst.setCashmascd(String.valueOf(map.get("cashMasCd")));
        cashHst.setRequestUser(String.valueOf(map.get("chaCd")));
        cashHst.setRequestDate(new Date());
        cashHst.setRequestTypeCd(String.valueOf(map.get("job")));
        cashHst.setResultCd("3");
        cashHst.setCusoffno(String.valueOf(map.get("cusOffNo2")));
        cashHst.setRcpamt((Long) map.get("rcpAmt2"));
        cashHst.setTax((Long) map.get("tax"));
        cashHstMapper.insertSelective(cashHst);

        return receiptMgmtDao.updateCashMas(map);
    }

    /**
     * 현금영수증 취소
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public int deleteCashMas(HashMap<String, Object> map) throws Exception {
        CashHst cashHst = new CashHst();
        cashHst.setChacd(String.valueOf(map.get("chaCd")));
        cashHst.setCashmascd(String.valueOf(map.get("cashMasCd")));
        cashHst.setRequestUser(String.valueOf(map.get("chaCd")));
        cashHst.setRequestDate(new Date());
        cashHst.setRequestTypeCd(String.valueOf(map.get("job")));
        cashHst.setResultCd("3");
        cashHst.setRcpamt(0L);
        cashHstMapper.insertSelective(cashHst);

        return receiptMgmtDao.deleteCashMas(map);
    }

    /**
     * 현금영수증 취소요청 철회
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public int noDeleteIssue(HashMap<String, Object> map) throws Exception {
        CashHst cashHst = new CashHst();
        cashHst.setChacd(String.valueOf(map.get("chaCd")));
        cashHst.setCashmascd(String.valueOf(map.get("cashMasCd")));
        cashHst.setRequestUser(String.valueOf(map.get("chaCd")));
        cashHst.setRequestDate(new Date());
        cashHst.setRequestTypeCd("D");
        cashHst.setResultCd("2");
        cashHst.setRcpamt(0L);
        cashHstMapper.insertSelective(cashHst);

        return receiptMgmtDao.noDeleteIssue(map);
    }

    /**
     * 현금영수증 재발행 요청 철회
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public int reInsertIssueDelete(HashMap<String, Object> map) throws Exception {
        CashHst cashHst = new CashHst();
        cashHst.setChacd(String.valueOf(map.get("chaCd")));
        cashHst.setCashmascd(String.valueOf(map.get("cashMasCd")));
        cashHst.setRequestUser(String.valueOf(map.get("chaCd")));
        cashHst.setRequestDate(new Date());
        cashHst.setRequestTypeCd("U");
        cashHst.setResultCd("2");
        cashHst.setRcpamt(0L);
        cashHstMapper.insertSelective(cashHst);

        return receiptMgmtDao.reInsertIssueDelete(map);
    }

    /**
     * 현금영수증 발행요청 철회
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public int doInsertIssueDelete(HashMap<String, Object> map) throws Exception {
        CashHst cashHst = new CashHst();
        cashHst.setChacd(String.valueOf(map.get("chaCd")));
        cashHst.setCashmascd(String.valueOf(map.get("cashMasCd")));
        cashHst.setRequestUser(String.valueOf(map.get("chaCd")));
        cashHst.setRequestDate(new Date());
        cashHst.setRequestTypeCd("I");
        cashHst.setResultCd("2");
        cashHst.setRcpamt(0L);
        cashHstMapper.insertSelective(cashHst);

        return receiptMgmtDao.doInsertIssueDelete(map);
    }

    @Override
    @Transactional
    public int updateCashMasU(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.updateCashMasU(map);
    }

    /**
     * 청구항목 상세보기
     */
    @Override
    public List<ReceiptMgmtDTO> getReceiptDetail(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getReceiptDetail(map);
    }

    /**
     * 현금영수증 사전 데이터
     *
     * @param
     * @return
     * @throws Exception
     */
    @Override
    public NotiDTO getCashData(String vaNo) throws Exception {
        return receiptMgmtDao.getCashData(vaNo);
    }

    /**
     * 현금영수증 인서트
     */
    @Override
    public int insertCashMaster(Map<String, Object> map) throws Exception {
        return receiptMgmtDao.insertCashMaster(map);
    }

    public int updateCashMaster(Map<String, Object> map) throws Exception {
        return receiptMgmtDao.updateCashMaster(map);
    }

    public int deleteCashMaster(Map<String, Object> map) throws Exception {
        return receiptMgmtDao.deleteCashMaster(map);
    }

    @Override
    public ReceiptMgmtDTO getXrcpDetList(Map<String, Object> map) throws Exception {
        return receiptMgmtDao.getXrcpDetList(map);
    }

    @Override
    public ReceiptMgmtDTO getRcpCd(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getRcpCd(map);
    }

    /**
     * 가상계좌 금액
     */
    @Override
    public ReceiptMgmtDTO getVasAmt(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getVasAmt(map);
    }

    @Override
    public NotiDTO insertCashMas(NotiDTO dto) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * rcpmascd 찾기
     *
     * @return
     * @throws Exception
     */
    public String getRcpMasCd(String notiMasCd) throws Exception {
        return receiptMgmtDao.getRcpMasCd(notiMasCd);
    }

    @Override
    public ReceiptMgmtDTO getCustomerByRcpMasCd(String rcpMasCd) {
        return receiptMgmtDao.selectCusMasByRcpMasCd(rcpMasCd);
    }

    @Override
    public Map<String, Object> getNoticeMasterByRcpMasCd(String rcpMasCd) {
        return receiptMgmtDao.selectNoticeMasterByRcpMasCd(rcpMasCd);
    }

    @Override
    public List<Map<String, Object>> getNoticeDetailsByRcpMasCd(String rcpMasCd) {
        return receiptMgmtDao.selectNoticeDetailsByRcpMasCd(rcpMasCd);
    }

    public ReceiptMgmtDTO selectCashMas(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.selectCashMas(map);
    }

    public List<ReceiptMgmtDTO> getChaMasCd(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getChaMasCd(map);
    }

    @Override
    public HashMap<String, Object> getPayItemListCnt(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getPayItemListCnt(map);
    }

    @Override
    public List<PayMgmtDTO> getPayItemList(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getPayItemList(map);
    }

    @Deprecated
    @Override
    public List<PayMgmtDTO> getPayItemExcelList(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getPayItemExcelList(map);
    }

    /**
     * 수납관리 > 수납내역 엑셀 다운로드
     *
     * @param params
     * @return
     * @throws Exception
     * @author jhjeong@finger.co.kr
     * @modified 2018. 10. 29.
     */
    @Override
    public InputStream getReceiptHistoryExcel(Map<String, Object> params) throws Exception {
        final XlsxBuilder xlsxBuilder = new XlsxBuilder();
        xlsxBuilder.newSheet("수납내역");

        // 수납내역목록
        final List<ReceiptMgmtDTO> receiptList = receiptMgmtDao.getReceiptList(params);
        
        // 고객구분 목록
        @SuppressWarnings("unchecked")
        final List<CodeDTO> cdList = (List<CodeDTO>) params.get("cusGbList");

        // 헤더생성
        int headerColumnNo = 0;
        xlsxBuilder.addHeader(0, 1, headerColumnNo, headerColumnNo, "NO");
        headerColumnNo++;
        xlsxBuilder.addHeader(0, 1, headerColumnNo, headerColumnNo, "고객명");
        headerColumnNo++;
        xlsxBuilder.addHeader(0, 1, headerColumnNo, headerColumnNo, "가상계좌번호");
        headerColumnNo++;
        xlsxBuilder.addHeader(0, 1, headerColumnNo, headerColumnNo, "청구월");
        headerColumnNo++;
        xlsxBuilder.addHeader(0, 1, headerColumnNo, headerColumnNo, "수납일시");
        headerColumnNo++;
        xlsxBuilder.addHeader(0, 1, headerColumnNo, headerColumnNo, "수납상태");

        // 고객구분
        for (CodeDTO code : cdList) {
            headerColumnNo++;
            xlsxBuilder.addHeader(0, 1, headerColumnNo, headerColumnNo, code.getCodeName());
        }

        headerColumnNo++;
        xlsxBuilder.addHeader(0, 1, headerColumnNo, headerColumnNo, "청구항목건수");
        headerColumnNo++;
        xlsxBuilder.addHeader(0, 1, headerColumnNo, headerColumnNo, "청구금액(원)");
        headerColumnNo++;
        xlsxBuilder.addHeader(0, 1, headerColumnNo, headerColumnNo, "수납금액(원)");
        headerColumnNo++;
        xlsxBuilder.addHeader(0, 1, headerColumnNo, headerColumnNo, "미납금액(원)");

        headerColumnNo++;
        xlsxBuilder.addHeader(0, 0, headerColumnNo, headerColumnNo + 3, "납부수단상세");
        xlsxBuilder.addHeader(1, 1, headerColumnNo, headerColumnNo, "가상계좌");
        headerColumnNo++;
        xlsxBuilder.addHeader(1, 1, headerColumnNo, headerColumnNo, "온라인카드");
        headerColumnNo++;
        xlsxBuilder.addHeader(1, 1, headerColumnNo, headerColumnNo, "현금");
        headerColumnNo++;
        xlsxBuilder.addHeader(1, 1, headerColumnNo, headerColumnNo, "오프라인카드");
        headerColumnNo++;
        xlsxBuilder.addHeader(0, 1, headerColumnNo, headerColumnNo, "환불금액(원)");

        int rows = 1;
        for (ReceiptMgmtDTO data : receiptList) {
            xlsxBuilder.newDataRow();

            int columnNo = 0;

            xlsxBuilder.addData(columnNo++, rows++);
            xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusName()));
            xlsxBuilder.addData(columnNo++, data.getVano().substring(0, 3) + "-" + data.getVano().substring(3, 6) + "-" + data.getVano().substring(6));
            xlsxBuilder.addData(columnNo++, data.getMasMonth());

            if(StringUtils.isNotBlank(data.getPayDay())) {
                final Date date = yyyyMMddHHmmss.parse(StringUtils.rightPad(data.getPayDay() + data.getPayTime(), 14, '0'));
                xlsxBuilder.addData(columnNo++, sdf.format(date));
            } else {
                xlsxBuilder.addData(columnNo++, StringUtils.EMPTY);
            }

            xlsxBuilder.addData(columnNo++, data.getNotiMasStNm());

            for (int n = 0; n < cdList.size(); n++) {
                if (n == 0) xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn1()));
                if (n == 1) xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn2()));
                if (n == 2) xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn3()));
                if (n == 3) xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn4()));
            }

            xlsxBuilder.addData(columnNo++, data.getItemCnt() + "건");

            xlsxBuilder.addData(columnNo++, data.getAmt());
            xlsxBuilder.addData(columnNo++, data.getRcpAmt());
            xlsxBuilder.addData(columnNo++, data.getUnAmt());
            xlsxBuilder.addData(columnNo++, data.getVasAmt());
            xlsxBuilder.addData(columnNo++, data.getOcdAmt());
            xlsxBuilder.addData(columnNo++, data.getDcsAmt());
            xlsxBuilder.addData(columnNo++, data.getDcdAmt());
            xlsxBuilder.addData(columnNo++, NumberUtils.toLong(data.getRePayAmt(), 0));
        }

        final File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".tmp");
        xlsxBuilder.writeTo(new FileOutputStream(tempFile));
        return new FileInputStream(tempFile);
    }

    /**
     * 수납관리 > 현금영수증 내역 엑셀 다운로드 - 신규
     */
    @Override
    public InputStream getCashReceiptHistoryExcel(Map<String, Object> params) throws Exception {
        XlsxBuilder xlsxBuilder = new XlsxBuilder();
        xlsxBuilder.newSheet("현금영수증 내역");

        // 고객구분 목록
        @SuppressWarnings("unchecked")
        List<CodeDTO> cdList = (List<CodeDTO>) params.get("cusGbList");

        // 현금영수증 목록
        List<ReceiptMgmtDTO> list = receiptMgmtDao.getCashMasList(params);

        // 헤더생성
        int columnNo = 0;
        xlsxBuilder.addHeader(0, columnNo++, "NO");
        xlsxBuilder.addHeader(0, columnNo++, "입금일시");
        xlsxBuilder.addHeader(0, columnNo++, "승인일자");
        xlsxBuilder.addHeader(0, columnNo++, "승인번호");
        xlsxBuilder.addHeader(0, columnNo++, "고객명");

        // 고객구분
        for (CodeDTO code : cdList) {
            xlsxBuilder.addHeader(0, columnNo++, code.getCodeName());
        }

        xlsxBuilder.addHeader(0, columnNo++, "현금영수증 발행번호");
        xlsxBuilder.addHeader(0, columnNo++, "입금금액(원)");
        xlsxBuilder.addHeader(0, columnNo++, "발행금액(원)");
        xlsxBuilder.addHeader(0, columnNo++, "발행구분");
        xlsxBuilder.addHeader(0, columnNo++, "입금구분");
        xlsxBuilder.addHeader(0, columnNo++, "메시지");
        xlsxBuilder.addHeader(0, columnNo++, "재발행 발급번호");
        xlsxBuilder.addHeader(0, columnNo++, "재발행금액(원)");

        // 데이터 처리
        int rows = 1;
        for (ReceiptMgmtDTO data : list) {
            columnNo = 0;
            xlsxBuilder.newDataRow();

            xlsxBuilder.addData(columnNo++, rows++);
            final Date payDate = yyyyMMddHHmmss.parse(StringUtils.rightPad(data.getPayDay() + data.getPayTime(), 14, '0'));
            xlsxBuilder.addData(columnNo++, sdf.format(payDate));
            xlsxBuilder.addData(columnNo++, data.getAppDay());
            xlsxBuilder.addData(columnNo++, data.getAppNo());
            xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusName()));

            for (int n = 0; n < cdList.size(); n++) {
                if (n == 0) xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn1()));
                if (n == 1) xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn2()));
                if (n == 2) xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn3()));
                if (n == 3) xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn4()));
            }

            xlsxBuilder.addData(columnNo++, StringUtils.defaultString(data.getCusOffNo()));
            xlsxBuilder.addData(columnNo++, data.getRcpAmt());
            xlsxBuilder.addData(columnNo++, NumberUtils.toLong(data.getCshAmt(), 0));
            xlsxBuilder.addData(columnNo++, data.getCashMasStNm());
            xlsxBuilder.addData(columnNo++, data.getSveCdNm());
            xlsxBuilder.addData(columnNo++, data.getAppMsg());

            xlsxBuilder.addData(columnNo++, StringUtils.defaultString(data.getCusOffNo2()));
            xlsxBuilder.addData(columnNo++, NumberUtils.toLong(data.getCshAmt2(), 0));
        }

        final File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".tmp");
        xlsxBuilder.writeTo(new FileOutputStream(tempFile));
        return new FileInputStream(tempFile);
    }
    
    /**
     * 수납관리 > 현금영수증 내역 국세청 양식 엑셀 다운로드 - 신규
     * @author sajoh@finger.co.kr
     * @modified 2019. 05. 13
     */
    @Override
    public InputStream getNtsCashReceiptHistoryExcel(Map<String, Object> params) throws Exception {
    	XlsxBuilder xlsxBuilder = new XlsxBuilder();
    	xlsxBuilder.newSheet("현금영수증 내역");

    	// 고객구분 목록
    	@SuppressWarnings("unchecked")
    	List<CodeDTO> cdList = (List<CodeDTO>) params.get("cusGbList");

    	// 현금영수증 목록
//    	List<ReceiptMgmtDTO> list = receiptMgmtDao.getCashMasList(params);
    	List<ReceiptMgmtDTO> list = receiptMgmtDao.getFWCashReceiptMasterList(params);
         
    	// 헤더생성
        int columnNo = 0;
        xlsxBuilder.addHeader(0, columnNo++, "NO");        
        xlsxBuilder.addHeader(0, columnNo++, "승인일자");
        xlsxBuilder.addHeader(0, columnNo++, "공급가");
        xlsxBuilder.addHeader(0, columnNo++, "부가세");
        xlsxBuilder.addHeader(0, columnNo++, "총금액"); 
        xlsxBuilder.addHeader(0, columnNo++, "승인번호");
        xlsxBuilder.addHeader(0, columnNo++, "발행번호");
        xlsxBuilder.addHeader(0, columnNo++, "입금일자");
        xlsxBuilder.addHeader(0, columnNo++, "고객명");
        xlsxBuilder.addHeader(0, columnNo++, "거래구분");
        
        // 데이터 처리
        int rows = 1;
        for (ReceiptMgmtDTO data : list) {
        	columnNo = 0;
            xlsxBuilder.newDataRow();
            
            xlsxBuilder.addData(columnNo++, rows++);					//"NO"
            xlsxBuilder.addData(columnNo++, data.getTxDate());			//"승인일자"
            xlsxBuilder.addData(columnNo++, data.getSplyAmt());			//"공급가"
            xlsxBuilder.addData(columnNo++, data.getTax());				//"부가세"
            xlsxBuilder.addData(columnNo++, data.getTxAmt());			//"총금액"
            xlsxBuilder.addData(columnNo++, data.getTxNo());			//"승인번호"
            xlsxBuilder.addData(columnNo++, data.getClientIdNo());		//"발행번호"            
            xlsxBuilder.addData(columnNo++, data.getPayDate());			//"입금일자"
            xlsxBuilder.addData(columnNo++, data.getCusName());			//"고객명"
            xlsxBuilder.addData(columnNo++, data.getTxTypeCodeNm());	//"거래구분"
        }

        final File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".tmp");
        xlsxBuilder.writeTo(new FileOutputStream(tempFile));
        return new FileInputStream(tempFile);
    }
    
    @Override
    public List<PdfReceiptMgmtDTO> getAdmRecpList(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getAdmRecpList(map);
    }

    @Override
    public List<PdfReceiptMgmtDTO> selectAdmBillCut(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.selectAdmBillCut(map);
    }

    @Override
    public List<PdfReceiptMgmtDTO> getAdmEduList(HashMap<String, Object> map) throws Exception {
        final List<PdfReceiptMgmtDTO> masterList = receiptMgmtDao.getAdmEduList(map);
        final String amtchkty = String.valueOf(map.get("amtChkTy"));
        for (PdfReceiptMgmtDTO each : masterList) {
            final String vano = each.getVano();
            final String paymentYear = each.getPaymentYear();
            final ArrayList<String> checkList;
            if("N".equals(map.get("amtChkTy"))){
                checkList = (ArrayList<String>)map.get("rcpMasList");
            }else{
                checkList = (ArrayList<String>)map.get("notiMasList");
            }

            final List<PdfReceiptDetailsDTO> itemList = receiptMgmtDao.selectAdmEduDetailsList(vano, paymentYear, checkList, amtchkty);
            each.setDetailsList(itemList);
        }
        return masterList;
    }

    @Override
    public List<PdfReceiptMgmtDTO> getAdmRcpAccNoList(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getAdmRcpAccNoList(map);
    }

    @Override
    public List<PdfReceiptMgmtDTO> getAdmDntnList(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getAdmDntnList(map);
    }

    @Override
    public List<PdfReceiptMgmtDTO> selectAdmBillCutWithoutNotice(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.selectAdmBillCutWithoutNotice(map);
    }

    @Override
    public List<FwCashReceiptMasterDTO> getCashHistList(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.getCashHistList(map);
    }

    @Override
    public HashMap<String, Object> countCashHistList(HashMap<String, Object> map) throws Exception {
        return receiptMgmtDao.countCashHistList(map);
    }
}
