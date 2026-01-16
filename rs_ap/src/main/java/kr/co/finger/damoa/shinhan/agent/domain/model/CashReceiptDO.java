package kr.co.finger.damoa.shinhan.agent.domain.model;

import kr.co.finger.shinhandamoa.data.table.model.Xcashmas;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 현금영수증 도메인
 * 
 * @author wisehouse@finger.co.kr
 */
public class CashReceiptDO {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashReceiptDO.class);

    /**
     * 미발행
     */
    public static final String STATUS_ST02 = "ST02";

    /**
     * 발행
     */
    public static final String STATUS_ST03 = "ST03";

    /**
     * 발행 중
     */
    public static final String STATUS_ST04 = "ST04";

    /**
     * 발행 요청
     */
    public static final String STATUS_ST05 = "ST05";

    /**
     * 발행
     */
    public static final String REQUEST_TYPE_ISSUE = "I";

    /**
     * 취소
     */
    public static final String REQUEST_TYPE_CANCEL = "D";

    /**
     * 재발행
     */
    public static final String REQUEST_TYPE_UPDATE = "U";

    /**
     * 전화번호
     */
    public static final String IDENTITY_NO_TYPE_CONTACT_NO = "11";

    /**
     * 신용카드 번호 (현금영수증 카드 번호)
     */
    public static final String IDENTITY_NO_TYPE_CREDIT_CARD_NO = "12";

    /**
     * 사업자등록번호
     */
    public static final String IDENTITY_NO_TYPE_BUSINESS_REGISTER_NO = "21";

    /**
     * 소득공제 (개인)
     */
    public static final String PURPOSE_TYPE_PERSONAL = "1";

    /**
     * 지출증빙 (사업자)
     */
    public static final String PURPOSE_TYPE_BUSINESS = "2";
    
    
    private Xcashmas xcashmas;

    public CashReceiptDO(Xcashmas xcashmas) {
        this.xcashmas = xcashmas;
    }

    public CashReceiptDO() {
        this(new Xcashmas());
    }

    public void setJob(String job) {
        this.xcashmas.setJob(job);
        this.xcashmas.setMakedt(new Date());
    }

    public void setStatus(String status) {
        this.xcashmas.setCashmasst(status);
        this.xcashmas.setMakedt(new Date());
    }

    public Xcashmas getXcashmas() {
        return xcashmas;
    }

    /**
     * 현금영수증 정보 생성
     * <p>
     * 현금영수증 정보를 생성한다.
     * 기관에 따라서 현금영수증을 여러 개의 사업자번호로 나눠서 발행하는 경우가 있다. 
     * 현금영수증을 발행할 사업자등록번호는 PayItem에 들어 있다.
     *
     * @param cha
     * @param customer
     * @param receiptMaster
     * @return
     */
    public static List<CashReceiptDO> createWithReceiptMaster(ChaDO cha, CustomerDO customer, ReceiptMasterDO receiptMaster) {
        // 1. 사업자등록번호별로 공급가액을 집계한다.
        final Map<String, Long> amountMap = new HashMap<>();
        for (ReceiptDetailsDO each : receiptMaster.getDetailsList()) {
            PayItemDO payItem = each.getPayItem();
            final String clientIdentityNo = StringUtils.defaultIfBlank(payItem.getCashReceiptIdentityNo(), cha.getIndentityNo());
            if(!amountMap.containsKey(clientIdentityNo)) {
                amountMap.put(clientIdentityNo, 0L);
            }

            if(!payItem.isEnableCashReceipt()) {
                continue;
            }

            final long amountForCashReceipt = amountMap.get(clientIdentityNo);
            amountMap.put(clientIdentityNo, amountForCashReceipt + each.getAmount());
        }
        
        if(receiptMaster.getDetailsList().isEmpty()) {
            amountMap.put(cha.getIndentityNo(), receiptMaster.getPaymentAmount());
        }
        
        // 2. 사업자등록번호별로 현금영수증 정보를 생성한다.        
        final List<CashReceiptDO> itemList = new ArrayList<>();
        for (Map.Entry<String, Long> eachEntry : amountMap.entrySet()) {
            final CashReceiptDO each = CashReceiptDO.createInternal(cha, customer, eachEntry.getKey(), receiptMaster.getId(), eachEntry.getValue(), receiptMaster.getCustomerIdentityNo());
            itemList.add(each);
        }
        
        return itemList;
    }

    private static CashReceiptDO createInternal(ChaDO cha, CustomerDO customer, String clientIdentityNo, String receiptMasterId, long amountForCashReceipt, String customerIdentityNo) {
        final Xcashmas xcashmas = new Xcashmas();
        xcashmas.setRcpmascd(receiptMasterId);
        xcashmas.setChacd(cha.getId());
        xcashmas.setAmtchkty(cha.isEnableValidateAmount() ? "Y" : "N");
        xcashmas.setNotaxyn(cha.isNoTax() ? "N" : "Y");
        xcashmas.setRcpreqty(cha.isEnableAutomaticCashReceipt() ? "A" : "M");
        xcashmas.setChatrty(cha.getAccessTypeCode());
        xcashmas.setRcpamt(amountForCashReceipt);
        xcashmas.setTip(0L);
        xcashmas.setTax(cha.isNoTax() ? 0 : Double.valueOf(Math.round(amountForCashReceipt / 11)).longValue());
        xcashmas.setCashmasst(STATUS_ST02);
        xcashmas.setMakedt(new Date());
        xcashmas.setMaker("DamoaShinhanAgent");
        xcashmas.setRegdt(new Date());

        xcashmas.setChaoffno(clientIdentityNo);

        // 고객 유형
        if(customer != null) {
            LOGGER.info("의무발행여부[{}]: 현금영수증 의무발행여부확인", cha.getMandRcpYn());
            if("Y".equals(cha.getMandRcpYn()) && StringUtils.isBlank(customer.getIdentityNo())){
                xcashmas.setCustype("1");
                xcashmas.setConfirm("11");
                xcashmas.setCusoffno("0100001234");
            }else{
                xcashmas.setCustype(customer.getCashReceiptCustomerTypeCode());
                xcashmas.setConfirm(customer.getCashReceiptMediaTypeCode());
                xcashmas.setCusoffno(customer.getIdentityNo());
            }
        } else {
            xcashmas.setCustype(StringUtils.EMPTY);
            xcashmas.setConfirm(StringUtils.EMPTY);
            xcashmas.setCusoffno(customerIdentityNo);
        }

        xcashmas.setCustype(getCusType(xcashmas.getCustype(), xcashmas.getCusoffno()));
        xcashmas.setConfirm(getConfirm(xcashmas.getConfirm(), xcashmas.getCusoffno()));

        return new CashReceiptDO(xcashmas);
    }

    public String getIdentityNo() {
        return xcashmas.getCusoffno();
    }

    public long setTotalAmount() {
        return xcashmas.getRcpamt();
    }

    private static String getConfirm(String confirm, String identityNo) {
        if (StringUtils.length(identityNo) == 10 && !identityNo.startsWith("0")) {
            // 사업자 등록번호
            return IDENTITY_NO_TYPE_BUSINESS_REGISTER_NO;
        } else if (StringUtils.length(identityNo) >= 16) {
            // 카드 번호
            return IDENTITY_NO_TYPE_CREDIT_CARD_NO;
        } else {
            // 전화번호
            return IDENTITY_NO_TYPE_CONTACT_NO;
        }
    }

    private static String getCusType(String cusType, String identityNo) {
        if (StringUtils.length(identityNo) == 10 && !identityNo.startsWith("0")) {
            // 사업자 등록번호
            return PURPOSE_TYPE_BUSINESS;
        } else {
            return PURPOSE_TYPE_PERSONAL;
        }
    }

    /**
     * 현금영수증 발행
     */
    public void issue() {
        this.setJob(REQUEST_TYPE_ISSUE);
        this.setStatus(STATUS_ST05);
        this.setSendDate(DateUtils.addDays(new Date(), 1));
    }

    private void setSendDate(Date date) {
        this.xcashmas.setSenddt(kr.co.finger.damoa.commons.DateUtils.toString(date, "yyyyMMdd"));
        this.xcashmas.setMakedt(new Date());
    }
}
