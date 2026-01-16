package kr.co.finger.damoa.scheduler.task;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.damoa.commons.io.SftpClient;
import kr.co.finger.damoa.exception.InvalidValueException;
import kr.co.finger.damoa.scheduler.Constants;
import kr.co.finger.shinhandamoa.service.CashReceiptService;
import kr.co.finger.damoa.scheduler.service.FileService;
import kr.co.finger.damoa.scheduler.service.SimpleService;
import kr.co.finger.msgio.cash.CashMessage;
import kr.co.finger.msgio.cash.Data;
import kr.co.finger.msgio.cash.Head;
import kr.co.finger.msgio.cash.Trailer;
import kr.co.finger.msgio.layout.LayoutFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static kr.co.finger.damoa.commons.Maps.getCashValue;
import static kr.co.finger.damoa.commons.Maps.getValue;
import static kr.co.finger.damoa.scheduler.utils.DamoaBizUtil.handleDecryptData;

/**
 * 현금영수증 전송..
 * 1. sftp로 파일 전송
 * 2. XCASHMAS 테이블 업데이트 처리.
 * 3. CASHREQ 테이블에 이력 남김
 */
@Component
public class CashReceiptsSenderTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashReceiptsSenderTask.class);
    
    @Autowired
    private LayoutFactory cashLayoutFactory;
    
    @Autowired
    private CashReceiptService cashReceiptService;
    @Autowired
    private FileService fileService;
    @Autowired
    private SimpleService simpleService;
    @Autowired
    private String cashFilePath;
    @Value("${damoa.cash.sftpHost}")
    private String sftpHost;
    @Value("${damoa.cash.sftpPort}")
    private int sftpPort;
    @Value("${damoa.cash.sftpUser}")
    private String sftpUser;
    @Value("${damoa.cash.sftpPassword}")
    private String sftpPassword;
    @Value("${damoa.cash.sftpPutRemoteDir}")
    private String putRemoteDir;

    private String workId = "CASH_SENDER";

    private Logger LOG = LoggerFactory.getLogger(getClass());
    
    public CashReceiptsSenderTask() {

    }

    //TODO 일단 2차개발
    @Scheduled(cron = "${damoa.cashSenderTask.cron}")
    public void execute() {
        // XCASHMAS 테이블에서 현금영수증 관련 정보 수집..
        // sam 파일 생성.
        // sftp 로 파일 전송.
        // 전송 결과 처리.
        String startDate = DateUtils.to14NowString(new Date());

        try {
            LOG.info("EXECUTE " + startDate + " " + workId);
            final String fullFilePath = fullFilePath(true);
            
            LOG.info("현금영수증 발행중..." + fullFilePath);
            // 1. 현금영수증 발행 데이터 조회
            //    전일 거래 건과 전일 발행 신청 건을 처리한다
            final List<Map<String, Object>> cashMasList = cashReceiptService.getCashReceiptFileTransferHistory();
            if (cashMasList == null || cashMasList.isEmpty()) {
                LOG.info("현금영수증 발행할 내역이 없음.");
                return;
            }

            final CashMessage cashMessage = createCashMessage(cashMasList);
            final List<Data> dataList = cashMessage.getDataList();
            if (dataList == null || dataList.size() == 0) {
                LOG.info("현금영수증 내역에 정상적인 것이 없음.");
                return;
            }
            LOG.info("현금영수증 파일 생성중.." + fullFilePath);
            String msg = encodeCash(cashMessage);
            LOG.info(msg);
            write(msg, fullFilePath);
            LOG.info("현금영수증 파일 생성.." + fullFilePath);
            handleMadeCashFile(fullFilePath, cashMessage, cashMasList);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {

        }

    }

    /**
     * 파일 전송처리...
     * 파일전송실패시
     *
     * @param fullFilePath
     * @param cashMessage
     * @param maps
     * @throws Exception
     */
    private void handleMadeCashFile(String fullFilePath, CashMessage cashMessage, List<Map<String, Object>> maps) throws Exception {
        LOG.info("현금영수증 파일 전송중.. " + fullFilePath);
        transferRemotePlace(fullFilePath);
        LOG.info("현금영수증 파일 전송완료.. " + fullFilePath);
        LOG.info("현금영수증 파일 전송후 DB처리중.. " + fullFilePath);
        cashReceiptService.handleSendFile(fullFilePath, cashMessage, maps);
        LOG.info("현금영수증 파일 전송후 DB처리완료.. " + fullFilePath);

    }

    @PostConstruct
    public void init() {
    }


    private void insertCashReqForSending(CashMessage cashMessage, List<Map<String, Object>> maps) {
        cashReceiptService.insertCashReqForSending(cashMessage, maps);
    }

    private void insertCashFilePath(String fullFilePath) {
        cashReceiptService.insertCashFilePath(fullFilePath);
    }

    private void view(List<Map<String, Object>> maps) {

        for (Map<String, Object> map : maps) {
            LOG.info(map.toString());
        }
    }

    private void insertCashReqForSending(List<Map<String, Object>> maps) {
        LOG.info("insertCashReqForSending");
        cashReceiptService.insertCashReqForSending(maps);
    }

    /**
     * 현금영수증 발행중 처리함.
     *
     * @param cashMessage
     */
    private void updateXCashMasForSending(CashMessage cashMessage) {
        LOG.info("updateXCashMasForSending");
        cashReceiptService.updateXCashMasForSending(cashMessage.toParamMaps());
    }

    /**
     * 현금영수증 객체를 전문으로 인코딩..
     *
     * @param cashMessage
     * @return
     * @throws Exception
     */
    private String encodeCash(CashMessage cashMessage) throws Exception {
        return cashLayoutFactory.encodeCash(cashMessage);
    }

    /**
     * @param fullFilePath
     * @throws Exception
     */
    private void transferRemotePlace(String fullFilePath) throws Exception {
        SftpClient sftpClient = new SftpClient(sftpHost, sftpPort, sftpUser, sftpPassword);
        sftpClient.upload(fullFilePath, putRemoteDir);
    }

    private String fullFilePath(boolean send) {
        String corp = Constants.THIS_CORP;
        String filePath = cashFilePath;
        String fuleFilePath = filePath + "/" + cashFileName(corp, send);
        return fuleFilePath;
    }

    private void write(String msg, String fuleFilePath) throws IOException {
        fileService.write(msg, fuleFilePath);
    }

    private String cashFileName(String corp, boolean send) {
        if (send) {
            return "S_" + corp + "." + DateUtils.toString(new Date(), "yyyyMMdd");
        } else {
            return "R_" + corp + "." + DateUtils.toString(new Date(), "yyyyMMdd");
        }
    }


    /**
     * JOB(I:신규 U:재발행 D:취소)에 따라 로직이 달라짐.
     * 신규는 딱히 문제없음.
     * 취소는 등록일시와 조작일시가 같은 날이면 생성하지 않음.
     * 등록일시와 조작일시가 다른 날이면 취소가 생성되어야 함.
     * 재발행은 취소, 신규이므로 등록일시,조작일시가 같은 날이면 신규 생성
     * 등록일시,조작일시가 다른날이면 취소,신규 함께 생성되어야 함.
     *
     * @param maps
     * @return
     */
    private CashMessage createCashMessage(List<Map<String, Object>> maps) throws InvalidValueException {
        // 현금영수증 발행 요청일자
        final String requestDate = DateUtils.toString(new Date(), "yyyyMMdd");
        // 요청사코드
        final String requestCorpCd = Constants.REQUEST_CORP;
        // 수행사코드
        final String processCorpCd = Constants.PROCESS_CORP;
        
        final CashMessage cashMessage = new CashMessage();
        // HEAD
        final Head head = new Head();
        head.setSendDate(requestDate);
        head.setReqCorp(requestCorpCd);
        head.setProcessCorp(processCorpCd);
        cashMessage.setHead(head);

        AtomicLong totalAmount = new AtomicLong(0);
        int index = 0;
        for (Map<String, Object> map : maps) {
            String masCd = Maps.getValue(map, "cashmascd");
            try {
                LOG.info(map.toString());
                
                handleDecryptData(map);
                
                //발급방법 [11: 휴대폰번호, 12현금영수증카드번호, 13:주민번호, 21:사업자번호]
                final String confirm = Maps.getValue(map, "confirm");
                final String confirm2 = Maps.getValue(map, "confirm2");
                final String cusType = Maps.getValue(map, "custype");
                final String appno = Maps.getValue(map, "appno");
                if (StringUtils.isEmpty(confirm.trim())) {
                    LOG.error("SKIP [cashMasCd]" + masCd + " connfirm is invalid null.");
                    continue;
                }
                
                //작업구분 I:신규 U:재발행 D:취소
                //dealType    "0" 승인 "1" 취소
                final String job = getJob(map);
                Data data;
                switch(job) {
                    case "I":
                        // 신규
                        if(StringUtils.isBlank(cusType) || StringUtils.isBlank(confirm)) {
                            continue;
                        }
                        index++;
                        data = create(map, index, totalAmount, "0", job);
                        LOG.info(getConfirmType(confirm) + " 신규처리 " + requestDate);
                        cashMessage.addData(data);
                        break;
                    case "D":
                        // 취소
                        if(StringUtils.isBlank(appno)) {
                            continue;
                        }
                        index++;
                        data = create(map, index, totalAmount, "1", job);
                        LOG.info(getConfirmType(confirm) + " 취소처리 " + data);
                        cashMessage.addData(data);
                        break;
                    case "U":
                        // 재발생. 취소와 발행을 해야 함.
                        if(StringUtils.isBlank(cusType) || StringUtils.isBlank(confirm2)) {
                            continue;
                        }
                        index++;
                        data = create(map, index, totalAmount, "1", job);
                        LOG.info(getConfirmType(confirm) + "재발행 취소처리 " + data);
                        cashMessage.addData(data);

                        index++;
                        data = create(map, index, totalAmount, "0", job);
                        LOG.info(getConfirmType(confirm) + "재발행 신규처리 " + data);
                        cashMessage.addData(data);

                        break;
                    default:
                        LOG.warn("잘못된 Job " + job);
                        
                        break;
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                LOG.error("SKIP [cashMasCd]" + masCd + " 예외발생으로 skip함. ");
            }
        }

        // TRAILER
        final Trailer trailer = new Trailer();
        trailer.setTotalCount(cashMessage.getDataList().size() + "");
        trailer.setTotalAmount(totalAmount.toString() + "");
        cashMessage.setTrailer(trailer);
        
        return cashMessage;
    }

    private String getConfirmType(String confirm) {
        //11: 휴대폰번호, 12현금영수증카드번호, 13:주민번호, 21:사업자번호
        LOG.info("confirm " + confirm);
        if ("11".equalsIgnoreCase(confirm)) {
            return "휴대폰번호";
        } else if ("12".equalsIgnoreCase(confirm)) {
            return "현금영수증카드번호";
        } else if ("13".equalsIgnoreCase(confirm)) {
            return "주민번호";
        } else if ("21".equalsIgnoreCase(confirm)) {
            return "사업자번호";
        } else {

            return "잘못된 confirm데이터 " + confirm;
        }
    }

    /**
     * @param map
     * @param index
     * @param totalAmount
     * @param dealType    "0" 승인 "1" 취소
     * @param job         작업구분 I:신규 U:재발행 D:취소
     * @return
     */
    private Data create(Map<String, Object> map, int index, AtomicLong totalAmount, String dealType, String job) throws InvalidValueException {
        // 거래일. KIS의 경우 요청하는 날은 단일 매출일로 만들어야 한다
        final String yesterday = new SimpleDateFormat("yyyyMMdd").format(org.apache.commons.lang3.time.DateUtils.addDays(new Date(), -1));
        // 실거래일/실요청일과 맞추기 위해서 하루 전으로 한다.        
        final String salesDate = yesterday;
        
        final Data data = new Data();
        from(data, map, index, dealType, job, salesDate);
        data.setupTotalAmount(totalAmount);

        return data;
    }
    
    /**
     * job 수집..
     * job은 null일 수 없음.
     * 수집할 때부터 걸러냄..
     *
     * @param map
     * @return
     */
    private String getJob(Map<String, Object> map) {
        return getValue(map, "job");
    }

    /**
     *
     * @param map 데이터
     * @param index 순서
     * @param dealType "0" 승인 "1" 취소
     * @param job 작업구분 I:신규 U:재발행 D:취소
     * @throws InvalidValueException
     */
    public void from(Data data, Map<String, Object> map,int index,String dealType,String job, String salesDate) throws InvalidValueException {
        //작업구분 I:신규 U:재발행 D:취소
        data.setDealType(dealType);
        data.setSeqNo(String.valueOf(index));
        data.setBizNo(getCashValue(map,"chaoffno"));
        data.setId(data.findId(map,dealType,job));
        data.setSalesAmount(data.findSalesAmount(map,dealType,job));

        //1:주민번호 2:핸드폰번호 3:카드번호 4:사업자번호
        String trtmntType = data.findTrtmntType(map,dealType,job);

        data.setTrtmntType(trtmntType);
        if ("4".equalsIgnoreCase(trtmntType)) {
            // 사업자번호
            //지출증빙
            data.setTraderType("1");
        } else {
            // 핸드폰번호
            // 주민번호
            //카드번호
            // 소득공제
            data.setTraderType("0");
        }
//        setCustomerNo(getCashValue(map,"CUSOFFNO"));
        // TODO 고객번호를 식별번호와 동일하게 사용할 경우 입력하지 않는다
        // TODO KIS의 요청 있었음
        // TODO 추후 고객 번호를 사용하는 방법을 검토 필요
        data.setCustomerNo(StringUtils.repeat(' ', 20));
//        data.setTerminalNo("12345678");
        // KIS요청으로 재판매는 단말기번호 다르게 사용
        data.setTerminalNo("39260429");

        // 거래일
        data.setSalesDate(salesDate);
        data.setPosEntity("02");
        data.setCreditApprovalNo("");
        data.setCashApprovalNo("");
        data.setCashOriginApprovalNo("");

        if ("0".equalsIgnoreCase(data.getDealType())) {
            data.setServiceCharge(getCashValue(map,"tip"));
            data.setTax(getCashValue(map,"tax"));
        }else if ("1".equalsIgnoreCase(data.getDealType())) {
            // 현금원승인번호, 원매출일자
            data.setOriginSalesDate(getCashValue(map, "appday"));
            data.setCashOriginApprovalNo(getCashValue(map,"appno"));
            data.setServiceCharge(getCashValue(map,"tip"));
            data.setTax(getCashValue(map,"tax"));
        }

        data.setCurrencyCd("410");
        // cashmascd+","+job
        data.setTemp(getCashValue(map, "cashmascd")+","+job);

        data.setReturnCd("");
    }

}
