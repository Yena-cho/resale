package kr.co.finger.shinhandamoa.service;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.damoa.scheduler.ThreadSession;
import kr.co.finger.damoa.scheduler.ThreadSessionHolder;
import kr.co.finger.damoa.scheduler.dao.BatchWorkerDao;
import kr.co.finger.msgio.cash.CashMessage;
import kr.co.finger.msgio.cash.Data;
import kr.co.finger.msgio.layout.LayoutFactory;
import kr.co.finger.shinhandamoa.data.table.mapper.*;
import kr.co.finger.shinhandamoa.data.table.model.*;
import kr.co.finger.shinhandamoa.domain.repository.CashReceiptFileRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 현금영수증 서비스
 *
 * @author wisehouse@finger.co.kr
 */
@Service
@Transactional
public class CashReceiptService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashReceiptService.class);

    @Autowired
    private BatchWorkerDao batchWorkerDao;
    
    @Autowired
    private CashFileTranHistMapper cashFileTranHistMapper;
    
    @Autowired
    private XcashmasMapper xcashmasMapper;
    
    @Autowired
    private CashreqMapper cashreqMapper;

    @Autowired
    private XcashhstMapper xcashhstMapper;
    
    @Autowired
    private FwCashReceiptMasterMapper fwCashReceiptMasterMapper;
    
    @Autowired
    private CashReceiptFileRepository cashReceiptFileRepository;
    
    @Autowired
    private LayoutFactory cashLayoutFactory;

    /**
     * 현금영수증 발행 대상 데이터 조회
     * <p>
     * 발행 요청 상태(ST05)이면서 전송일자(SENDDT)가 오늘(SYSDATE)인 것을 처리한다
     *
     * @return
     */
    public List<Map<String, Object>> getCashReceiptFileTransferHistory() {
        return batchWorkerDao.findCashReceipts();
    }

    public void updateXCashMasForSending(List<Map<String, Object>> maps) {
        for (Map<String, Object> map : maps) {
            String cashMasCd = Maps.findKey(map, "cashmascd");
            batchWorkerDao.updateXCashMasForSending(param(cashMasCd));
        }
    }

    private Map<String, Object> param(String cashMasCd) {
        final Map<String, Object> param = new HashMap<>();

        param.put("CASHMASCD", cashMasCd);
        param.put("SENDDT", DateUtils.toDateString(new Date()));
        param.put("CASHMASST", "ST04");

        return param;
    }

    /**
     * 현금영수증 발행 결과를 XCASHMAS 테이블에 반영한다
     *
     * @param cashMessage
     */
    public void updateXCashMasForReceiving(CashMessage cashMessage) {
        LOGGER.info("현금영수증 수신에 따른 xcashmas 업데이트...");
        List<Data> dataList = cashMessage.getDataList();
        // 재발행이 있는 경우 취소처리는 딱히 할 필요가 없다.
        // 취소처리만 있는 경우 취소처리를 해야 함.

        for (Data data : dataList) {
            //xcashmascd,job 의 형태임.
            final String temp = data.getTemp();
            final String[] strings = StringUtils.split(temp, ",");

            final String job;
            if (strings.length == 2) {
                job = strings[1];
            } else {
                job = StringUtils.EMPTY;
            }

            if (StringUtils.equalsIgnoreCase(job, "U") && StringUtils.equalsIgnoreCase(data.getDealType(), "1")) {
                // 재발행의 취소는 XCASHMAS 테이블을 변경하지 않아도 된다
                continue;
            }

            batchWorkerDao.updateXCashMasForReceiving(data.toReceiveUpdate());
        }
    }

    public void insertCashReqForSending(List<Map<String, Object>> maps) {
        for (Map<String, Object> map : maps) {
            String cashMasCd = Maps.findKey(map, "cashmascd");
            batchWorkerDao.insertCashReqForSending(param(cashMasCd));
        }
    }

    public void updateCashReq(CashMessage cashMessage) {
        LOGGER.info("현금영수증 수신에 따른 cashreq 업데이트...");
        List<Data> dataList = cashMessage.getDataList();
        for (Data data : dataList) {
            //ST는 처리하지 않음..
            batchWorkerDao.updateCashReq(data.toReceiveUpdate());
        }
    }

    public void insertCashFilePath(String fullFilePath) {
        batchWorkerDao.insertCashFilePath(fullFilePath);
    }

    public void finishUnhandledFilePath(String transDate, String fullFilePath) {
        batchWorkerDao.finishUnhandledFilePath(transDate, fullFilePath);
    }

    public void insertCashReqForSending(CashMessage cashMessage, List<Map<String, Object>> maps) {

        batchWorkerDao.insertCashReqForSending(cashMessage, maps);
    }

    /**
     * 다운로드해야 할 현금영수증 파일 목록
     *
     * @return
     */
    public List<CashReceiptFileDTO> getCashReceiptFileListForReceive() {
        final CashFileTranHistExample example = new CashFileTranHistExample();
        example.createCriteria().andRecDtIsNull();
        example.setOrderByClause("trans_dt asc");

        final List<CashFileTranHist> voList = cashFileTranHistMapper.selectByExample(example);
        final List<CashReceiptFileDTO> dtoList = new ArrayList<>(voList.size());
        for (CashFileTranHist each : voList) {
            dtoList.add(transformCashReceiptFileDTO(each));
        }

        return new ArrayList<>(dtoList.stream().collect(Collectors.groupingBy(CashReceiptFileDTO::getTransferFileName, Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(CashReceiptFileDTO::getTransferDt)), Optional::get))).values());
    }

    /**
     * 다운로드해야 할 현금영수증 파일 목록
     *
     * @return
     */
    public List<CashReceiptFileDTO> getCashReceiptFileListForImport() {
        final CashFileTranHistExample example = new CashFileTranHistExample();
        example.createCriteria().andRecDtIsNotNull().andImportYnEqualTo("N");
        example.setOrderByClause("trans_dt asc");

        final List<CashFileTranHist> voList = cashFileTranHistMapper.selectByExample(example);
        final List<CashReceiptFileDTO> dtoList = new ArrayList<>(voList.size());
        for (CashFileTranHist each : voList) {
            dtoList.add(transformCashReceiptFileDTO(each));
        }

        return new ArrayList<>(dtoList.stream().collect(Collectors.groupingBy(CashReceiptFileDTO::getTransferFileName, Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(CashReceiptFileDTO::getTransferDt)), Optional::get))).values());
        //         LOGGER.debug("itemList.stream().collect(Collectors.groupingBy(Pair::getRight)): {}", new ArrayList<>(itemList.stream().collect(Collectors.groupingBy(Pair::getRight, Collectors.maxBy(Comparator.comparing(Pair::getLeft)))).values()));
    }

    private CashReceiptFileDTO transformCashReceiptFileDTO(CashFileTranHist vo) {
        final CashReceiptFileDTO dto = new CashReceiptFileDTO();
        dto.setTransferDt(vo.getTransDt());
        dto.setReceiveDt(vo.getRecDt());
        dto.setTransferFileName(vo.getCashFileName());
        dto.setReceiveFileName(StringUtils.replace(vo.getCashFileName(), "S_FING_RS", "R_FING_RS"));

        return dto;
    }

    /**
     * 송신 파일 처리
     *
     * @param fullFilePath
     * @param cashMessage
     * @param maps
     */
    @Transactional
    public void handleSendFile(String fullFilePath, CashMessage cashMessage, List<Map<String, Object>> maps) {
        insertCashFilePath(fullFilePath);
        updateXCashMasForSending(maps);
        insertCashReqForSending(cashMessage, maps);
    }

    /**
     * 파일 수신
     *
     * @param cashReceiptFileId
     * @throws Exception
     */
    public void receiveCashReceiptResponseFile(String cashReceiptFileId) throws Exception {
        try {
            cashReceiptFileRepository.receiveResponseFile(cashReceiptFileId);
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.warn(e.getMessage(), e);
            } else {
                LOGGER.warn(e.getMessage());
            }
        }
    }

    /**
     * 파일 데이터 임포트
     *
     * @param cashReceiptFileId
     */
    public void importCashReceiptFile(String cashReceiptFileId) {
        InputStream is = null;
        try {
            is = cashReceiptFileRepository.loadReceiveFile(cashReceiptFileId);
            final List<String> stringList = IOUtils.readLines(is, StandardCharsets.ISO_8859_1);
            final CashMessage cashMessage = cashLayoutFactory.decodeCash(stringList);

            List<Data> dataList = cashMessage.getDataList();
            dataList.sort(new Comparator<Data>() {
                @Override
                public int compare(Data o1, Data o2) {
                    return o1.getSeqNo().compareTo(o2.getSeqNo());
                }
            });

            for (Data each : dataList) {
                FwCashReceiptMasterExample example = new FwCashReceiptMasterExample();
                example.createCriteria()
                        .andDataNoEqualTo(each.getSeqNo())
                        .andTxDateEqualTo(each.getSalesDate())
                        .andTxNoEqualTo(each.getCashApprovalNo());
                
                if(fwCashReceiptMasterMapper.countByExample(example) > 0) {
                    LOGGER.warn("이미 처리된 현금영수증 데이터");
                    continue;
                }
                
                final FwCashReceiptMaster fwCashReceiptMaster = new FwCashReceiptMaster();
                fwCashReceiptMaster.setClientTypeCode(each.getTraderType());
                fwCashReceiptMaster.setTxTypeCode(each.getDealType());
                fwCashReceiptMaster.setDataNo(each.getSeqNo());
                fwCashReceiptMaster.setMediaTypeCode(each.getTrtmntType());
                fwCashReceiptMaster.setHostIdentityNo(each.getBizNo());
                fwCashReceiptMaster.setClientIdentityNo(each.getId());
                fwCashReceiptMaster.setClientNo(each.getCustomerNo());
                fwCashReceiptMaster.setTxDate(each.getSalesDate());
                fwCashReceiptMaster.setPosEntry(each.getPosEntity());
                fwCashReceiptMaster.setTxNo(each.getCashApprovalNo());
                fwCashReceiptMaster.setCancelTxNo(each.getCashOriginApprovalNo());
                fwCashReceiptMaster.setTxAmount(NumberUtils.toLong(each.getSalesAmount()));
                fwCashReceiptMaster.setTip(NumberUtils.toLong(each.getServiceCharge()));
                fwCashReceiptMaster.setTax(NumberUtils.toLong(each.getTax()));
                fwCashReceiptMaster.setCancelTxDate(each.getOriginSalesDate());
                fwCashReceiptMaster.setCurrencyCode(each.getCurrencyCd());
                fwCashReceiptMaster.setResponseCode(each.getReturnCd());
                fwCashReceiptMaster.setUserDefine(each.getTemp());
                fwCashReceiptMaster.setRequestDate(new Date());
                fwCashReceiptMaster.setResponseDate(new Date());
                fwCashReceiptMaster.setCreateDate(new Date());
                fwCashReceiptMaster.setCreateUser(ThreadSessionHolder.getInstance().getSession("finger-service").getAttribute(ThreadSession.SESSION_ATTRIBUTE_KEY_OWNER));
                fwCashReceiptMaster.setStatusCd("3");

                fwCashReceiptMasterMapper.insert(fwCashReceiptMaster);

                // XCASHMAS 업데이트
                final String userWorkArea = each.getTemp();
                final String cashMasCd = StringUtils.substringBefore(userWorkArea, ",");
                final String job = StringUtils.substringAfter(userWorkArea, ",");
                
                LOGGER.debug("현금영수증[{}] 처리 결과", cashMasCd);
                final Xcashmas xcashmas = xcashmasMapper.selectByPrimaryKey(cashMasCd);
                if(xcashmas == null) {
                    LOGGER.warn("현금영수증[{}] 존재하지 않음", cashMasCd);
                    continue;
                }

                xcashmas.setJob(StringUtils.EMPTY);

                switch (job) {
                    case "I":
                        switch(each.getReturnCd()) {
                            case "0000":
                                LOGGER.info("현금영수증[{}] 발행", cashMasCd);
                                xcashmas.setAppday(each.getSalesDate());
                                xcashmas.setApptime("000000");
                                xcashmas.setAppno(each.getCashApprovalNo());
                                xcashmas.setAppcd(each.getReturnCd());
                                xcashmas.setAppmsg(getResponseMessage(each.getReturnCd()));
                                xcashmas.setCashmasst("ST03");
                                break;
                            default:
                                LOGGER.info("현금영수증[{}] 발행 실패", cashMasCd);
                                xcashmas.setAppcd(each.getReturnCd());
                                xcashmas.setAppmsg(getResponseMessage(each.getReturnCd()));
                                xcashmas.setCashmasst("ST02");
                                break;
                        }

                        break;
                    case "U":
                        switch(each.getDealType()) {
                            case "1":
                                switch(each.getReturnCd()) {
                                    case "0000":
                                        LOGGER.info("현금영수증[{}] 재발행 취소", cashMasCd);
                                        xcashmas.setCustype(StringUtils.EMPTY);
                                        xcashmas.setCusoffno(StringUtils.EMPTY);
                                        xcashmas.setConfirm(StringUtils.EMPTY);
                                        xcashmas.setRcpamt(0L);

                                        xcashmas.setAppday(StringUtils.EMPTY);
                                        xcashmas.setApptime(StringUtils.EMPTY);
                                        xcashmas.setAppno(StringUtils.EMPTY);
                                        xcashmas.setAppcd(StringUtils.EMPTY);
                                        xcashmas.setAppmsg(StringUtils.EMPTY);
                                        xcashmas.setCashmasst("ST02");
                                        
                                        break;
                                    default:
                                        // 취소 실패
                                        LOGGER.warn("현금영수증[{}] 재발행 취소 실패", cashMasCd);
                                        xcashmas.setCashmasst("ST03");
                                        xcashmas.setAppcd(each.getReturnCd());
                                        xcashmas.setAppmsg(getResponseMessage(each.getReturnCd()));
                                        
                                        break;
                                }

                                break;
                            case "0":
                                switch(each.getReturnCd()) {
                                    case "0000":
                                        LOGGER.info("현금영수증[{}] 재발행", cashMasCd);
                                        
                                        xcashmas.setCustype(xcashmas.getCustype2());
                                        xcashmas.setCusoffno(xcashmas.getCusoffno2());
                                        xcashmas.setConfirm(xcashmas.getConfirm2());
                                        xcashmas.setRcpamt((long) defaultInt(xcashmas.getRcpamt2(), 0));
                                        
                                        xcashmas.setCustype2(StringUtils.EMPTY);
                                        xcashmas.setCusoffno2(StringUtils.EMPTY);
                                        xcashmas.setConfirm2(StringUtils.EMPTY);
                                        xcashmas.setRcpamt2(null);

                                        xcashmas.setAppday(each.getSalesDate());
                                        xcashmas.setApptime("000000");
                                        xcashmas.setAppno(each.getCashApprovalNo());
                                        xcashmas.setAppcd(each.getReturnCd());
                                        xcashmas.setAppmsg(getResponseMessage(each.getReturnCd()));
                                        xcashmas.setCashmasst("ST03");
                                        
                                        break;
                                    default:
                                        LOGGER.info("현금영수증[{}] 재발행 실패", cashMasCd);
                                        
                                        xcashmas.setJob(StringUtils.EMPTY);
                                        if(StringUtils.equals(xcashmas.getCashmasst(), "ST02")) {
                                            xcashmas.setCustype(xcashmas.getCustype2());
                                            xcashmas.setCusoffno(xcashmas.getCusoffno2());
                                            xcashmas.setConfirm(xcashmas.getConfirm2());
                                            xcashmas.setRcpamt((long) defaultInt(xcashmas.getRcpamt2(), 0));

                                            xcashmas.setCustype2(StringUtils.EMPTY);
                                            xcashmas.setCusoffno2(StringUtils.EMPTY);
                                            xcashmas.setConfirm2(StringUtils.EMPTY);
                                            xcashmas.setRcpamt2(null);
                                            
                                            xcashmas.setAppcd(each.getReturnCd());
                                            xcashmas.setAppmsg(getResponseMessage(each.getReturnCd()));
                                            xcashmas.setCashmasst("ST02");
                                        } else {
                                            xcashmas.setCashmasst("ST03");
                                        }
                                        break;
                                }

                                break;
                        }

                        break;
                    case "D":
                        switch(each.getReturnCd()) {
                            case "0000":
                                LOGGER.info("현금영수증[{}] 취소", cashMasCd);
                                xcashmas.setAppday(StringUtils.EMPTY);
                                xcashmas.setApptime(StringUtils.EMPTY);
                                xcashmas.setAppno(StringUtils.EMPTY);
                                xcashmas.setAppcd(StringUtils.EMPTY);
                                xcashmas.setAppmsg(StringUtils.EMPTY);

                                xcashmas.setCashmasst("ST02");
                                break;
                            default:
                                LOGGER.warn("현금영수증[{}] 취소 실패", cashMasCd);

                                xcashmas.setCashmasst("ST03");
                                xcashmas.setAppcd(each.getReturnCd());
                                xcashmas.setAppmsg(getResponseMessage(each.getReturnCd()));
                                break;
                        }

                        break;
                }
                xcashmasMapper.updateByPrimaryKey(xcashmas);

                //현금영수증 수신결과에 대한 현금영수증 이용내역 등록
                Xcashhst cashHst = new Xcashhst();
                cashHst.setChacd(xcashmas.getChacd());
                cashHst.setCashmascd(xcashmas.getCashmascd());
                cashHst.setRequestUser("rs-scheduler");
                cashHst.setRequestDate(new Date());
                cashHst.setRequestTypeCd(job);
                if("0000".equals(each.getReturnCd())){
                    cashHst.setResultCd("0");
                }else{
                    cashHst.setResultCd("1");
                }
                cashHst.setRequestDate(new Date());
                cashHst.setRequestUser("rs-scheduler");
                cashHst.setCusoffno(xcashmas.getCusoffno());
                cashHst.setRcpamt(xcashmas.getRcpamt());
                cashHst.setTax(xcashmas.getTax());
                cashHst.setFwCashReceiptMasterId(fwCashReceiptMaster.getId());
                xcashhstMapper.insertSelective(cashHst);

                // CASHREQ 업데이트
                final Cashreq cashreq = new Cashreq();
                cashreq.setAppday(each.getSalesDate());
                cashreq.setApptime("000000");
                cashreq.setAppno(each.getCashApprovalNo());
                cashreq.setAppcd(each.getReturnCd());
                cashreq.setAppmsg(getResponseMessage(each.getReturnCd()));
                
                final CashreqExample cashreqExample = new CashreqExample();
                cashreqExample.createCriteria().andCashmascdEqualTo(cashMasCd);
                
                cashreqMapper.updateByExampleSelective(cashreq, cashreqExample);
            }

            final CashFileTranHist vo = cashFileTranHistMapper.selectByPrimaryKey(cashReceiptFileId);
            
            final CashFileTranHist record = new CashFileTranHist();
            record.setImportYn("Y");

            final CashFileTranHistExample example = new CashFileTranHistExample();
            example.createCriteria().andCashFileNameEqualTo(vo.getCashFileName());
            
            cashFileTranHistMapper.updateByExampleSelective(record, example);
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());
            }

            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.warn(e.getMessage(), e);
                    } else {
                        LOGGER.warn(e.getMessage());
                    }

                }
            }
        }
    }
    
    private long defaultLong(Long input, long defaultValue) {
        if(input == null) {
            return defaultValue;
        }
        
        return input.longValue();
    }
    
    private int defaultInt(Integer input, int defaultValue) {
        if(input == null) {
            return defaultValue;
        }
        
        return input.intValue();
    }
    
    private String getResponseMessage(String responseCode) {
        switch (responseCode) {
            case "8003":
                return "전문 오류";
            case "4949":
                return "1분후재조회요망";
            case "7003":
                return "미등록 단말기";
            case "8700":
                return "B/L 등록 가맹점";
            case "8009":
                return "금액 오류 미달";
            case "8037":
                return "고객 번호 오류";
            case "0814":
                return "금액    부적당";
            case "8373":
                return "금액 오류 초과";
            case "8370":
                return "포인트 별도 거래요망";
            case "TS1":
                return "신분확인정보(주민등록번호/사업자번호/휴대전화번호) 자릿수 오류";
            case "TS2":
                return "카드정보 자릿수 오류";
            case "TS3":
                return "불성실/위장/휴폐업/제외업종사업자와 거래한 현금결제내역";
            case "TS4":
                return "매출금액 입력오류";
            case "TS5":
                return "승인번호 중복 및 오류";
            case "TS6":
                return "매출일자 오류";
            case "TS7":
                return "가맹점 사업자등록번호 미등록 오류";
            case "TS8":
                return "Layout 오류(Record 항목별 입력 값 오류)";
            case "0000":
                return "정상";
            default:
                return "기타 오류";

        }
    }
}
