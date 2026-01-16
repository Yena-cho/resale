package kr.co.finger.damoa.shinhan.agent.domain.model;

import kr.co.finger.damoa.model.msg.NoticeMessage;
import kr.co.finger.shinhandamoa.data.table.model.Xrcpmas;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 수납 원장 도메인
 * 
 * @author wisehouse@finger.co.kr
 */
public class ReceiptMasterDO {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptMasterDO.class);
    private static final String STATUS_CODE_PAYMENT = "PA03";

    private static final String DEFAULT_NOTIMASCD = "00000000000000000000";

    private Xrcpmas xrcpmas;
    
    private NoticeMasterDO noticeMaster;
    
    private List<ReceiptDetailsDO> detailsList;

    public ReceiptMasterDO(Xrcpmas xrcpmas) {
        this.xrcpmas = xrcpmas;
        this.detailsList = new ArrayList<>();
    }
    
    private ReceiptMasterDO() {
        this(new Xrcpmas());
        this.xrcpmas.setSmsyn("N");
        this.xrcpmas.setMailyn("N");
        this.xrcpmas.setNotimascd(DEFAULT_NOTIMASCD);
    }

    public static ReceiptMasterDO createWithNoticeMaster(NoticeMessage noticeMessage, ChaDO cha, CustomerDO customer, NoticeMasterDO noticeMaster, long transactionAmount) {
        final ReceiptMasterDO receiptMaster = noticeMaster.createReceiptMaster(transactionAmount);

        receiptMaster.setNoticeMaster(noticeMaster);

        // 입금일시
        try {
            receiptMaster.setPaymentDate(new SimpleDateFormat("yyyyMMddHHmmss").parse(noticeMessage.getDealStartDate()));
        } catch (Exception e) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.warn(e.getMessage(), e);
            } else {
                LOGGER.warn(e.getMessage());
            }

            receiptMaster.setPaymentDate(new Date());
        }

        receiptMaster.setPaymentMethodCode("VAS");
        // 전문번호
        receiptMaster.setBankMessageNo(noticeMessage.getDealSeqNo());
        // 출금 은행코드 (공동망)
        receiptMaster.setKftcBankCode(StringUtils.substring(noticeMessage.getCommonNetworkNo(), 0, 3));
        // 출금 은행 코드
        receiptMaster.setSourceBankCode(StringUtils.substring(noticeMessage.getWithdrawalCorpCode(), 5, 8));
        // 지점코드
        receiptMaster.setSourceBranchCode(StringUtils.substring(noticeMessage.getTerminalInfo(), 1, 5));
        // 입금자명
        receiptMaster.setPayerName(noticeMessage.getWithdrawalAccountName());
        // 발생구분
        receiptMaster.setSourceTypeCode(noticeMessage.getOccurGubun());
        // 매체구분
        receiptMaster.setMediaTypeCode(noticeMessage.getMediaGubun());
        // 패킷번호
        receiptMaster.setPacketNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
        // 수납상태
        receiptMaster.setStatus(ReceiptMasterDO.STATUS_CODE_PAYMENT);
        // 등록일시
        receiptMaster.setCreateDate(new Date());
        // 기관 접속 유형
        receiptMaster.setChaAccessTypeCode(cha.getAccessTypeCode());
        
        return receiptMaster;
    }

    private void setNoticeMaster(NoticeMasterDO noticeMaster) {
        this.noticeMaster = noticeMaster;
        
        if(noticeMaster == null) {
            this.xrcpmas.setNotimascd(DEFAULT_NOTIMASCD);
        } else {
            this.xrcpmas.setNotimascd(noticeMaster.getId());
        }
    }

    public NoticeMasterDO getNoticeMaster() {
        return noticeMaster;
    }

    public static ReceiptMasterDO createWithoutNoticeMaster(NoticeMessage noticeMessage, ChaDO cha, CustomerDO customer, long transactionAmount) {
        final ReceiptMasterDO receiptMaster = new ReceiptMasterDO();
        receiptMaster.xrcpmas.setChacd(cha.getId());
        receiptMaster.setAccountNo(noticeMessage.getDepositAccountNo());
        receiptMaster.setNoticeMonth(StringUtils.substring(noticeMessage.getDealStartDate(), 0, 6));
        receiptMaster.setNoticeDate(StringUtils.substring(noticeMessage.getDealStartDate(), 0, 8));
        if(customer != null) {
            receiptMaster.setCustomerKey(customer.getKey());
            receiptMaster.setCustomerCategory1(customer.getCategory1());
            receiptMaster.setCustomerCategory2(customer.getCategory2());
            receiptMaster.setCustomerCategory3(customer.getCategory3());
            receiptMaster.setCustomerCategory4(customer.getCategory4());
            receiptMaster.setCustomerContactNo(customer.getContactNo());
            receiptMaster.setCustomerIdentityNo(customer.getIdentityNo());
        }
        if(customer == null) {
            receiptMaster.setCustomerName(cha.getName());
        } else {
            receiptMaster.setCustomerName(customer.getName());
        }
        
        // 입금일시
        try {
            receiptMaster.setPaymentDate(new SimpleDateFormat("yyyyMMddHHmmss").parse(noticeMessage.getDealStartDate()));
        } catch (Exception e) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.warn(e.getMessage(), e);
            } else {
                LOGGER.warn(e.getMessage());
            }
            
            receiptMaster.setPaymentDate(new Date());
        }
        
        receiptMaster.setPaymentMethodCode("VAS");
        // 전문번호
        receiptMaster.setBankMessageNo(noticeMessage.getDealSeqNo());
        // 출금 은행코드 (공동망)
        receiptMaster.setKftcBankCode(StringUtils.substring(noticeMessage.getCommonNetworkNo(), 0, 3));
        // 출금 은행 코드
        receiptMaster.setSourceBankCode(StringUtils.substring(noticeMessage.getWithdrawalCorpCode(), 5, 8));
        // 지점코드
        receiptMaster.setSourceBranchCode(StringUtils.substring(noticeMessage.getTerminalInfo(), 1, 5));
        // 입금자명
        receiptMaster.setPayerName(noticeMessage.getWithdrawalAccountName());
        // 출금액
        receiptMaster.setPaymentAmount(transactionAmount);
        // 발생구분
        receiptMaster.setSourceTypeCode(noticeMessage.getOccurGubun());
        // 매체구분
        receiptMaster.setMediaTypeCode(noticeMessage.getMediaGubun());
        // 패킷번호
        receiptMaster.setPacketNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
        // 수납상태
        receiptMaster.setStatus(ReceiptMasterDO.STATUS_CODE_PAYMENT);
        // 등록일시
        receiptMaster.setCreateDate(new Date());
        // 기관 접속 유형
        receiptMaster.setChaAccessTypeCode(cha.getAccessTypeCode());
        
        return receiptMaster;
    }

    private void setPaymentMethodCode(String paymentMethodCode) {
        this.xrcpmas.setSvecd(paymentMethodCode);
    }

    private void setChaAccessTypeCode(String chaAccessTypeCode) {
        this.xrcpmas.setChatrty(chaAccessTypeCode);
    }

    private void setPacketNo(String packetNo) {
        this.xrcpmas.setPacketno(packetNo);
    }

    private void setMediaTypeCode(String modiMediaTypeCode) {
        this.xrcpmas.setMdgubn(modiMediaTypeCode);
    }

    private void setSourceTypeCode(String sourceTypeCode) {
        this.xrcpmas.setOccgubn(sourceTypeCode);
    }
    
    private  void setPaymentAmount(long paymentAmount) {
        this.xrcpmas.setRcpamt(paymentAmount);
    }

    private void setPayerName(String payerName) {
        this.xrcpmas.setRcpusrname(payerName);
    }

    private void setSourceBankCode(String sourceBankCode) {
        this.xrcpmas.setBnkcd(sourceBankCode);
    }
    
    private void setSourceBranchCode(String sourceBranchCode) {
        this.xrcpmas.setBchcd(sourceBranchCode);
    }

    private void setKftcBankCode(String kftcBankCode) {
        this.xrcpmas.setFicd(kftcBankCode);
    }

    private void setPaymentDate(Date paymentDate) {
        this.xrcpmas.setPayday(new SimpleDateFormat("yyyyMMdd").format(paymentDate));
        this.xrcpmas.setPaytime(new SimpleDateFormat("HHmmss").format(paymentDate));
    }

    private void setCustomerContactNo(String customerContactNo) {
        this.xrcpmas.setCushp(customerContactNo);
    }
    
    private void setCustomerIdentityNo(String customerIdentityNo) {
        this.xrcpmas.setCusoffno(customerIdentityNo);
    }

    private void setCustomerCategory1(String category1) {
        this.xrcpmas.setCusgubn1(category1);
    }

    private void setCustomerCategory2(String category2) {
        this.xrcpmas.setCusgubn2(category2);
    }

    private void setCustomerCategory3(String category3) {
        this.xrcpmas.setCusgubn3(category3);
    }

    private void setCustomerCategory4(String category4) {
        this.xrcpmas.setCusgubn4(category4);
    }

    private void setStatus(String statusCode) {
        this.xrcpmas.setRcpmasst(statusCode);
    }

    private void setCreateDate(Date date) {
        this.xrcpmas.setRegdt(date);
        this.xrcpmas.setMakedt(date);
        this.xrcpmas.setMaker("DamoaShinhanAgent");
    }

    private void setBankMessageNo(String messageNo) {
        this.xrcpmas.setBnkmsgno(messageNo);
        this.xrcpmas.setDealseqno(messageNo);
    }

    private void setCustomerKey(String customerKey) {
        this.xrcpmas.setCuskey(customerKey);
    }

    private void setNoticeDate(String noticeDate) {
        this.xrcpmas.setMasday(noticeDate);
    }

    private void setNoticeMonth(String noticeMonth) {
        this.xrcpmas.setMasmonth(noticeMonth);
    }

    private void setAccountNo(String accountNo) {
        this.xrcpmas.setVano(accountNo);
    }

    private void setCustomerName(String customerName) {
        this.xrcpmas.setCusname(customerName);
    }

    public void addDetails(ReceiptDetailsDO receiptDetails) {
        this.detailsList.add(receiptDetails);
    }

    public Xrcpmas getXrcpmas() {
        return xrcpmas;
    }

    public List<ReceiptDetailsDO> getDetailsList() {
        for (ReceiptDetailsDO each : detailsList) {
            each.setMasterId(this.getId());
        }
        
        return detailsList;
    }
    
    public String getId() {
        return xrcpmas.getRcpmascd();
    }

    public String getCustomerName() {
        return xrcpmas.getCusname();
    }

    public String getCustomerIdentityNo() {
        return StringUtils.defaultString(this.xrcpmas.getCusoffno());
    }

    public String getPaymentMonth() {
        return xrcpmas.getMasmonth();
    }

    public String getNoticeMasterId() {
        return xrcpmas.getNotimascd();
    }

    public String getClientId() {
        return xrcpmas.getChacd();
    }

    public long getPaymentAmount() {
        return this.xrcpmas.getRcpamt();
    }
}
