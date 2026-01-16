package kr.co.finger.msgio.cash;

import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.damoa.exception.InvalidValueException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static kr.co.finger.damoa.commons.Maps.getCashValue;

public class Data {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    private static Map<String, String> MSG = new HashMap<>();
    static {
        MSG.put("8003", "전문 오류");
        MSG.put("8009", "현금 판매 출력");
        MSG.put("4949", "1분후재조회요망");
        MSG.put("7003", "미등록 단말기");
        MSG.put("8700", "B/L 등록 가맹점");
        MSG.put("8009", "금액 오류 미달");
        MSG.put("8037", "고객 번호 오류");
        MSG.put("0814", "금액    부적당");
        MSG.put("8373", "금액 오류 초과");
        MSG.put("8370", "포인트 별도 거래요망");
        MSG.put("0000", "정상처리");
        MSG.put("TS1", "신분확인정보(주민등록번호/사업자번호/휴대전화번호) 자릿수 오류");
        MSG.put("TS2", "카드정보 자릿수 오류");
        MSG.put("TS3", "불성실/위장/휴폐업/제외업종사업자와 거래한 현금결제내역");
        MSG.put("TS4", "매출금액 입력오류");
        MSG.put("TS5", "승인번호 중복 및 오류");
        MSG.put("TS6", "매출일자 오류");
        MSG.put("TS7", "가맹점 사업자등록번호 미등록 오류");
        MSG.put("TS8", "Layout 오류(Record 항목별 입력 값 오류)");
        MSG.put("0000", "정상");
    }

    private String dataType= "20";	//Data구분 20
    private String traderType= "";	//거래자구분 0:소득공제, 1:지출증빙(사업자번호 승인시)
    private String dealType= "";	//거래구분 0:승인거래, 1:취소거래(승인+매출)
    private String seqNo= "";	//데이터 일련번호
    private String trtmntType= "";	//처리구분  1:주민번호 2:핸드폰번호 3:카드번호 4:사업자번호
    private String bizNo= "";	//가맹점 사업자 번호 좌측정렬을 위해 AN
    private String id= "";	//신분확인 주민번호/핸드폰번호/카드번호/사업자번호
    private String customerNo= "";	//고객번호 사업자에서 관리하는 고객번호
    private String terminalNo= "";	//단말기번호
    private String salesDate= "";	//매출일자 YYYYMMDD
    private String posEntity= "";	//Swipe : 01, Key-in : 02
    private String creditApprovalNo= "";	//신용승인번호 공란
    private String cashApprovalNo= "";	//현금승인번호 KIS에서 승인번호 부여 (응답시 전송)
    private String cashOriginApprovalNo= "";	//취소시 현금 원승인의 승인번호 ‘취소시만 사용’
    private String salesAmount= "";	//매출액
    private String serviceCharge= "";	//봉사료 발생시만, 없으면 0 취소시 제외
    private String tax= "";	//없으면 0 취소시 제외
    private String originSalesDate= "";	//운매출일자 YYYYMMDD (취소시만, 승인거래의 매출일자)
    private String currencyCd= "";	//통화코드 원화 420, 미화:840
    private String returnCd= "";	//반송코드
    private String temp= "";	//요청사이용영역


    private String chaCd;

    public String getChaCd() {
        return chaCd;
    }

    public void setChaCd(String chaCd) {
        this.chaCd = chaCd;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("dataType", dataType)
                .append("traderType", traderType)
                .append("dealType", dealType)
                .append("seqNo", seqNo)
                .append("trtmntType", trtmntType)
                .append("bizNo", bizNo)
                .append("id", id)
                .append("customerNo", customerNo)
                .append("terminalNo", terminalNo)
                .append("salesDate", salesDate)
                .append("posEntity", posEntity)
                .append("creditApprovalNo", creditApprovalNo)
                .append("cashApprovalNo", cashApprovalNo)
                .append("cashOriginApprovalNo", cashOriginApprovalNo)
                .append("salesAmount", salesAmount)
                .append("serviceCharge", serviceCharge)
                .append("tax", tax)
                .append("originSalesDate", originSalesDate)
                .append("currencyCd", currencyCd)
                .append("returnCd", returnCd)
                .append("temp", temp)
                .toString();
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getTraderType() {
        return traderType;
    }

    public void setTraderType(String traderType) {
        this.traderType = traderType;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
//        if ("I".equalsIgnoreCase(dealType)) {
//            this.dealType = "0";
//        } else if ("U".equalsIgnoreCase(dealType)) {
//            this.dealType = "0";
//        } else if ("D".equalsIgnoreCase(dealType)) {
//            this.dealType = "1";
//        } else if ("".equalsIgnoreCase(dealType)) {
//            this.dealType = "0";
//        } else {
//        }
        this.dealType = dealType;
    }


    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getTrtmntType() {
        return trtmntType;
    }

    /**
     * @param trtmntType
     */
    public void setTrtmntType(String trtmntType) {
        this.trtmntType = trtmntType;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * @param customerNo
     */
    public void setCustomerNo(String customerNo) {
        if ("".equalsIgnoreCase(customerNo)) {
            this.customerNo = id;
        } else {
            this.customerNo = customerNo;
        }
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }

    public String getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(String salesDate) {
        this.salesDate = salesDate;
    }

    public String getPosEntity() {
        return posEntity;
    }

    public void setPosEntity(String posEntity) {
        this.posEntity = posEntity;
    }

    public String getCreditApprovalNo() {
        return creditApprovalNo;
    }

    public void setCreditApprovalNo(String creditApprovalNo) {
        this.creditApprovalNo = creditApprovalNo;
    }

    public String getCashApprovalNo() {
        return cashApprovalNo;
    }

    public void setCashApprovalNo(String cashApprovalNo) {
        this.cashApprovalNo = cashApprovalNo;
    }

    public String getCashOriginApprovalNo() {
        return cashOriginApprovalNo;
    }

    public void setCashOriginApprovalNo(String cashOriginApprovalNo) {
        this.cashOriginApprovalNo = cashOriginApprovalNo;
    }

    public String getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(String salesAmount) {
        this.salesAmount = salesAmount;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getOriginSalesDate() {
        return originSalesDate;
    }

    public void setOriginSalesDate(String originSalesDate) {
        this.originSalesDate = originSalesDate;
    }

    public String getCurrencyCd() {
        return currencyCd;
    }

    public void setCurrencyCd(String currencyCd) {
        this.currencyCd = currencyCd;
    }

    public String getReturnCd() {
        return returnCd;
    }

    public void setReturnCd(String returnCd) {
        this.returnCd = returnCd;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String findTrtmntType(Map<String, Object> map,String dealType,String job) throws InvalidValueException {
        if (checkConfirm1(map,dealType,job)) {
            return toTrtmntType(getCashValue(map,"CONFIRM"));
        } else {
            return toTrtmntType(getCashValue(map,"CONFIRM2"));
        }
    }

    public String findSalesAmount(Map<String, Object> map,String dealType,String job) {
        if (checkConfirm1(map,dealType,job)) {
            return getCashValue(map,"RCPAMT");
        } else {
            return getCashValue(map,"RCPAMT2");
        }
    }

    /**
     * 재발행취소는 CONFIRM 체크
     * 취소취소는 CONFIRM,CONFIRM2체크해서 확인가능.
     * @param map
     * @param dealType "0" 승인 "1" 취소
     * @param job 작업구분 I:신규 U:재발행 D:취소
     * @return
     */
    public String findId(Map<String, Object> map,String dealType,String job) {

        if (checkConfirm1(map,dealType,job)) {
            return getCashValue(map,"CUSOFFNO");
        } else {
            return getCashValue(map,"CUSOFFNO2");
        }

    }

    /**
     * 재발행취소는 CONFIRM 체크
     * 취소,취소이므로  CONFIRM1,CONFIRM2 체크해야 함
     *
     * 신규,승인 CONFIRM 체크
     * 재발행,승인이므로 CONFIRM2 체크
     * @param map
     * @param dealType "0" 승인 "1" 취소
     * @param job 작업구분 I:신규 U:재발행 D:취소
     * @return
     */
    private boolean checkConfirm1(Map<String, Object> map,String dealType,String job) {
        if ("1".equalsIgnoreCase(dealType)) {
            if ("U".equalsIgnoreCase(job)) {
                //재발행,취소이므로 CONFIRM
                LOG.info("재발행,취소이므로 CONFIRM 체크");
                return true;
            } else {
                //취소,취소이므로
                LOG.info("취소,취소이므로  CONFIRM1,CONFIRM2 체크해야 함");
                String confirm = getCashValue(map,"CONFIRM2");
                if (StringUtils.isEmpty(confirm.trim())) {
                    LOG.info("취소,취소 확인결과  CONFIRM 체크");
                    return true;
                } else {
                    LOG.info("취소,취소 확인결과  CONFIRM2 체크");
                    return false;
                }
            }
        } else {
            if ("I".equalsIgnoreCase(job)) {
                //승인,신규
                LOG.info("신규,승인이므로 CONFIRM 체크");
                return true;
            } else {
                //승인, 재발행이므로
                LOG.info("재발행,승인이므로 CONFIRM2 체크");
                return false;
            }
        }

    }

    /**
     * 인증번호유형 11: 휴대폰번호, 12현금영수증카드번호, 13:주민번호, 21:사업자번호 (처리구분)
     * @param value
     * @return
     * @throws InvalidValueException
     */
    private String toTrtmntType(String value) throws InvalidValueException {
        value = value.trim();
        if ("11".equalsIgnoreCase(value)) {
            return "2";
        } else if ("12".equalsIgnoreCase(value)) {
            return "3";
        } else if ("13".equalsIgnoreCase(value)) {
            return "1";
        } else if ("21".equalsIgnoreCase(value)) {
            return "4";
        } else {
            throw new InvalidValueException("invalid confirm ... " + value);
        }
    }

    /**
     * 수신파일 업데이트시 항목값 처리..
     * 정상승인 ST03 job 삭제
     * 오류건 ST02 job 삭제
     * 정상취소 ST02 job 삭제
     *
     * @return
     */
    public Map<String,Object> toReceiveUpdate() {
        // 승인일자 : 승인일자는 매출일자와 동일
        // TODO 위의 내용 확인 필요
        final String appday = getSalesDate();
        // 승인시각 : KIS에서 국세청에 등록을 00:00:00으로 하는 것 같다.
        // TODO 위의 내용 확인 필요
        final String apptime = "000000";

        // 승인 메시지
        final String responseMessage = StringUtils.defaultString(MSG.get(returnCd));

        // 현금영수증 발행 상태
        final String cashReceiptMasterStatus;
        if(StringUtils.equalsIgnoreCase(returnCd, "0000") && StringUtils.equalsIgnoreCase(dealType, "0")) {
            // 정상 발행이면 상태를 ST03
            cashReceiptMasterStatus = "ST03";
        } else {
            // 취소 또는 오류면 ST02
            cashReceiptMasterStatus = "ST02";
        }
        
        // CASHMASCD
        final String cashmascd;
        String[] strings = StringUtils.split(temp, ",");
        if (strings.length == 2) {
            cashmascd = strings[0];
        } else {
            cashmascd = temp;
        }

        final Map<String, Object> map = new HashMap<>();
        map.put("CASHMASCD", cashmascd);
        map.put("CASHMASST", cashReceiptMasterStatus);
        map.put("APPDAY", appday);
        map.put("APPTIME", apptime);
        map.put("APPCD", returnCd);
        map.put("APPMSG", responseMessage);
        map.put("APPNO", cashApprovalNo);
        map.put("JOB", "");

        return map;

    }


    private String msg(String returnCd) {
        if (MSG.containsKey(returnCd)) {
            return MSG.get(returnCd);
        } else {
            return "";
        }
    }
    public void setupTotalAmount(AtomicLong totalAmount) {
        if ("0".equalsIgnoreCase(dealType)) {
            totalAmount.addAndGet(Long.valueOf(salesAmount));
        } else if ("1".equalsIgnoreCase(dealType)) {
            totalAmount.addAndGet(Long.valueOf(salesAmount)* (-1));
        } else {

        }
    }

    public Map<String,Object> toParamForSending(Map<String,Object> map) {
        Map<String,Object> param = Maps.hashmap();
        param.putAll(map);
        String temp = getTemp();
        String[] strings = StringUtils.split(temp);
        if (strings.length == 2) {
            param.put("CASHMASCD", strings[0]);

        } else {
            param.put("CASHMASCD", temp);
        }
        param.put("GUBN", "현금(소득공제)");
        param.put("MSG1", "국세청 세미래콜센터");
        param.put("MSG2", "Tel : 국번없이 126");
        param.put("MSG3", "http://현금영수증.kr");
        param.put("PAYCLS", "1");

        return param;
    }
}
