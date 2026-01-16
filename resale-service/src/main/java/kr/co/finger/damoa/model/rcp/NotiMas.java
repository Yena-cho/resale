package kr.co.finger.damoa.model.rcp;

import kr.co.finger.damoa.commons.Damoas;
import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static kr.co.finger.damoa.commons.Constants.*;

/**
 * 입금(이체) 처리시에 청구와 수납 중 결과를 가지는 객체임..
 */
public class NotiMas implements Serializable, Cloneable {
    private String chacd;
    private String vano;
    private String maskey;
    private String masmonth;
    private String masday;
    private String cuskey;
    private String cusgubn1;
    private String cusgubn2;
    private String cusgubn3;
    private String cusgubn4;
    private String cusname;
    private String cushp;
    private String cusmail;
    private String smsyn;
    private String mailyn;
    private String cusoffno;
    private String chatrty;
    private String notimascd;
    private String startdate;
    private String enddate;
    private String checkAmount;
    private String st;
    private long totalAmount;       // 현재 납부되어야 할 금액... 부분납이 되었다면 원래 금액보다 작다.
    private long remainingAmount;
    private long minAmount;         // 납부되어야 할 상세 금액중에 가장 작은 금액..
    private long remainingMinAmount;         // 납부되어야 할 상세 금액중에 가장 작은 금액..


    private String rcpMasCd;
    private List<NotiDet> notiDets = new ArrayList<>();

    private Logger LOG = LoggerFactory.getLogger(getClass());

    public NotiMas() {
    }

    public NotiMas(String masmonth, String masday) {
        this.masmonth = masmonth;
        this.masday = masday;
    }

    public NotiMas(String masmonth, String masday, String startdate, String enddate) {
        this.masmonth = masmonth;
        this.masday = masday;
        this.startdate = startdate;
        this.enddate = enddate;
    }


    public String getRcpMasCd() {
        return rcpMasCd;
    }

    public void setRcpMasCd(String rcpMasCd) {
        this.rcpMasCd = rcpMasCd;
    }

    public String getCheckAmount() {
        return checkAmount;
    }

    public void setCheckAmount(String checkAmount) {
        this.checkAmount = checkAmount;
    }

    public long getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(long remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public long getRemainingMinAmount() {
        return remainingMinAmount;
    }

    public void setRemainingMinAmount(long remainingMinAmount) {
        this.remainingMinAmount = remainingMinAmount;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getSt() {
        if (st == null) {
            return PayStateCode.NO_PAY.getId();
        } else {
            return st;
        }
    }

    public void setSt(String st) {
        this.st = st;
    }


    public void add(NotiDet notiDet) {
        notiDets.add(notiDet);
    }

    public List<NotiDet> getNotiDets() {
        return notiDets;
    }

    public String getNotimascd() {
        return notimascd;
    }

    public void setNotimascd(String notimascd) {
        this.notimascd = notimascd;
    }

    public String masmonthDay() {
        return masmonth + masday;
    }

    public String getChacd() {
        return chacd;
    }

    public void setChacd(String chacd) {
        this.chacd = chacd;
    }

    public String getVano() {
        return vano;
    }

    public void setVano(String vano) {
        this.vano = vano;
    }

    public String getMaskey() {
        return maskey;
    }

    public void setMaskey(String maskey) {
        this.maskey = maskey;
    }

    public String getMasmonth() {
        return masmonth;
    }

    public void setMasmonth(String masmonth) {
        this.masmonth = masmonth;
    }

    public String getMasday() {
        return masday;
    }

    public void setMasday(String masday) {
        this.masday = masday;
    }

    public String getCuskey() {
        return cuskey;
    }

    public void setCuskey(String cuskey) {
        this.cuskey = cuskey;
    }

    public String getCusgubn1() {
        return cusgubn1;
    }

    public void setCusgubn1(String cusgubn1) {
        this.cusgubn1 = cusgubn1;
    }

    public String getCusgubn2() {
        return cusgubn2;
    }

    public void setCusgubn2(String cusgubn2) {
        this.cusgubn2 = cusgubn2;
    }

    public String getCusgubn3() {
        return cusgubn3;
    }

    public void setCusgubn3(String cusgubn3) {
        this.cusgubn3 = cusgubn3;
    }

    public String getCusgubn4() {
        return cusgubn4;
    }

    public void setCusgubn4(String cusgubn4) {
        this.cusgubn4 = cusgubn4;
    }

    public String getCusname() {
        return cusname;
    }

    public void setCusname(String cusname) {
        this.cusname = cusname;
    }

    public String getCushp() {
        return cushp;
    }

    public void setCushp(String cushp) {
        this.cushp = cushp;
    }

    public String getCusmail() {
        return cusmail;
    }

    public void setCusmail(String cusmail) {
        this.cusmail = cusmail;
    }

    public String getSmsyn() {
        return smsyn;
    }

    public void setSmsyn(String smsyn) {
        this.smsyn = smsyn;
    }

    public String getMailyn() {
        return mailyn;
    }

    public void setMailyn(String mailyn) {
        this.mailyn = mailyn;
    }

    public String getCusoffno() {
        return cusoffno;
    }

    public void setCusoffno(String cusoffno) {
        this.cusoffno = cusoffno;
    }

    public String getChatrty() {
        return chatrty;
    }

    public void setChatrty(String chatrty) {
        this.chatrty = chatrty;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setupFrom(Map<String, Object> map) {
        setChacd(Maps.getValue(map, "CHACD"));
        setVano(Maps.getValue(map, "VANO"));
        setMaskey(Maps.getValue(map, "MASKEY"));
        setMasmonth(Maps.getValue(map, "MASMONTH"));
        setMasday(Maps.getValue(map, "MASDAY"));
        setCuskey(Maps.getValue(map, "CUSKEY"));
        setCusgubn1(Maps.getValue(map, "CUSGUBN1"));
        setCusgubn2(Maps.getValue(map, "CUSGUBN2"));
        setCusgubn3(Maps.getValue(map, "CUSGUBN3"));
        setCusgubn4(Maps.getValue(map, "CUSGUBN4"));
        setCusname(Maps.getValue(map, "CUSNAME"));
        setCushp(Maps.getValue(map, "CUSHP"));
        setCusmail(Maps.getValue(map, "CUSMAIL"));
        setSmsyn(Maps.getValue(map, "SMSYN"));
        setMailyn(Maps.getValue(map, "MAILYN"));
        setCusoffno(Maps.getValue(map, "CUSOFFNO"));
        setChatrty(Maps.getValue(map, "CHATRTY"));
        setNotimascd(Maps.getValue(map, "NOTIMASCD"));
        setStartdate(Maps.getValue(map, "STARTDATE"));
        setEnddate(Maps.getValue(map, "ENDDATE"));
        setCheckAmount(Maps.getValue(map, "AMTCHKTY"));
    }

    public void simpleSetupFrom(Map<String, Object> map) {
        setMaskey(Maps.getValue(map, "MASKEY"));
        setMasmonth(Maps.getValue(map, "MASMONTH"));
        setMasday(Maps.getValue(map, "MASDAY"));
        setStartdate(Maps.getValue(map, "STARTDATE"));
        setEnddate(Maps.getValue(map, "ENDDATE"));
        setCusname(Maps.getValue(map, "CUSNAME"));
        setNotimascd(Maps.getValue(map, "NOTIMASCD"));
    }

    public Map<String, Object> toInsertCashMasterMap(Map<String, Object> chaCusInfo, String rcpMasCd, Date now, long amount, String type) {
        final String date = DateUtils.toDTType(now);
        final Map<String, Object> param = Maps.hashmap();

        param.putAll(chaCusInfo);
        param.put("RCPMASCD", rcpMasCd);  //원장코드
        param.put("RCPAMT", amount);  //거래금액
        param.put("TIP", "0");  //봉사료
        param.put("TAX", tax(amount, findNoTaxYn(chaCusInfo)));  //부가세
        if ("A".equalsIgnoreCase(type)) {
            param.put("JOB", "I");  //작업구분 I:신규 U:재발행 D:취소
            param.put("CASHMASST", "ST04");  //상태 ST02:미발행, ST03:발행
        } else {
            param.put("JOB", "");  //작업구분 I:신규 U:재발행 D:취소
            param.put("CASHMASST", "ST02");  //상태 ST02:미발행, ST03:발행
        }
        param.put("MAKEDT", date);  //조작일시
        param.put("MAKER", "DamoaShinhanAgent");  //조작자
        param.put("REGDT", date);  //등록일시

        return param;
    }

    private String findNoTaxYn(Map<String, Object> map) {
        return Maps.getValue(map, "NOTAXYN");
    }

    /**
     * 현금영수증 금액 처리..
     *
     * @return
     */
    public String findAmount() {
        long amount = 0;
        for (NotiDet notiDet : getNotiDets()) {
            if ("N".equalsIgnoreCase(notiDet.getRcpitemyn())) {
                // 현금영수증 발행여부가 N 인경우는 skip..
                continue;
            }
            amount += notiDet.getAmount();
        }
        return amount + "";
    }

    private long tax(long amount, String noTaxYn) {
        if ("Y".equalsIgnoreCase(noTaxYn)) {
            return Double.valueOf(Math.round(amount / 11.0)).longValue();
        } else {
            return 0L;
        }
    }

    public Map<String, Object> toInsertRcpMasterMap(String dealSeqNo, String withdrawalCorpCode, String withdrawalAccountName, String transactionAmount, String occurGubun, String mediaGubun, String msgTypeCode, String rcpMasCd, String shinhanCode, Date now) {
        Map<String, Object> param = Maps.hashmap();
        String date = DateUtils.toDTType(now);
        param.put("RCPMASCD", rcpMasCd); //수납원장코드
        param.put("NOTIMASCD", notimascd); //원장코드
        param.put("SVECD", "VAS"); //서비스코드, VAS-가상계좌,DCS-창구현금,DCD-창구카드,DVA-무통장입금,OCD-인터넷카드
        param.put("CHACD", chacd); //가맹점코드
        param.put("VANO", vano); //가상계좌번호
        param.put("MASKEY", maskey); //청구거래번호[청구KEY]
        param.put("MASMONTH", masmonth); //청구월
        param.put("MASDAY", masday); //청구일자
        param.put("CUSKEY", cuskey); //고객거래번호[고객KEY]
        param.put("CUSGUBN1", cusgubn1); //참조1
        param.put("CUSGUBN2", cusgubn2); //참조2
        param.put("CUSGUBN3", cusgubn3); //참조3
        param.put("CUSGUBN4", cusgubn4); //참조4
        param.put("CUSNAME", cusname); //고객명
        param.put("CUSHP", cushp); //고객핸드폰번호
        param.put("CUSMAIL", cusmail); //고객메일주소
        param.put("SMSYN", smsyn); //문자전송여부
        param.put("MAILYN", mailyn); //전자메일여부
        param.put("CUSOFFNO", cusoffno); //현금영수증발행고객정보
        param.put("PAYDAY", DateUtils.toDateString(now)); //(은행전문)수납일자
        param.put("PAYTIME", DateUtils.to6TimeString(now)); //(은행전문)수납시간
        param.put("BNKMSGNO", dealSeqNo); //은행전문번호/PG승인번호
        param.put("FICD", shinhanCode); //은행코드
        String bankCode = withdrawalCorpCode;
        param.put("BNKCD", findBankCode(bankCode)); //출금은행코드/PG ISP, 일반결제
        param.put("BCHCD", findBankPartCode(bankCode)); //출금지점코드
        param.put("RCPUSRNAME", withdrawalAccountName); //출금계좌성명
        param.put("RCPAMT", calculateAmount()); //수납금액
//        param.put("RCPAMT", transactionAmount); //수납금액
        param.put("OCCGUBN", occurGubun); //발생구분
        param.put("MDGUBN", mediaGubun); //매체구분
        param.put("RCPMASST", findRcpMasSt(msgTypeCode)); //수납상태(수납:PA03  취소:PA09)
        param.put("MAKEDT", date); //조작일시
        param.put("MAKER", "DamoaShinhanAgent"); //조작자
        param.put("REGDT", date); //등록일시
        param.put("CHATRTY", chatrty); //가맹점접속형태
        param.put("DEALSEQNO", dealSeqNo); //가맹점접속형태

        return param;
    }

    public String calculateAmount() {
        long amount = 0;
        for (NotiDet notiDet : notiDets) {
            if (notiDet.getAmount() < 0) {
                LOG.debug("SKIP " + notiDet.getPayitemcd() + " " + notiDet.getAmount());
                //아래는 고치면 안됨.. 발생한 에러를 알 수 없게 만들 수 있음.
//                continue;
            } else {
                LOG.info(notiDet.getPayitemcd() + " " + notiDet.getAmount());
            }
            amount += notiDet.getAmount();
        }
        LOG.debug("calculateAmount " + amount);
        return amount + "";
    }

    private String findBankCode(String bankCode) {
        if (bankCode == null || "".equalsIgnoreCase(bankCode)) {
            return "";
        } else {
            if (bankCode.length() == 8) {
                return bankCode.substring(5,8);
            } else {
                return "";
            }
        }
    }

    private String findBankPartCode(String bankCode) {
        return "";
//        if (bankCode == null || "".equalsIgnoreCase(bankCode)) {
//            return "";
//        } else {
//            if (bankCode.length() >= 7) {
//                return bankCode.substring(3, 7);
//            } else {
//                return "";
//            }
//        }
    }

    /**
     * 수납상태
     * 취소일 때는 PA09 하지만 취소처리가 이 메소드를 타지 않는다.
     *
     * @param msgTypeCode
     * @return
     */
    public String findRcpMasSt(String msgTypeCode) {
        if (msgTypeCode.startsWith("04")) {
            return "PA09";
        } else {
            String st = getSt();
            if ("PA02".equals(st)) {
                return st;
            } else {
                return "PA03";
            }
        }
    }

    /**
     * 돈 분배하기..
     * PA02 미납
     * PA03 수납
     * PA04 일부납
     * PA05 초과납
     *
     * @param amount
     */
    public void divideAmountExactly(AtomicLong amount) {
        divideAmount(amount, totalAmount, minAmount, new NotiDetHandler() {
            @Override
            public Long findAmountToPay(NotiDet notiDet) {
                return Damoas.toLong(notiDet.getPayitemamt());
            }
        });
    }

    public void divideAmount(AtomicLong amount) {
        divideAmount(amount, remainingAmount, remainingMinAmount, new NotiDetHandler() {
            @Override
            public Long findAmountToPay(NotiDet notiDet) {
                return notiDet.getAmountToPay();
            }
        });
    }

    private void divideAmount(AtomicLong amount, long amountToPay, long minAmount, NotiDetHandler notiDetHandler) {
        if (amount.get() <= 0) {
            setSt(NO_PAY);  //
            for (NotiDet notiDet : notiDets) {
                notiDet.setSt(NO_PAY);
            }
            LOG.debug("divideAmount notiMas NO_PAY " + notimascd);
            return;
        }

        LOG.debug("divideAmount 수납금액 " + amount.get() + " 청구금액=" + amountToPay + " " + notimascd);
        if (amountToPay == 0) {
            setSt(OK_PAY);
            for (NotiDet notiDet : notiDets) {
                notiDet.setSt(OK_PAY);
            }
            LOG.debug("divideAmount 청구금액을 모두 납부했으므로 완료처림 " + notimascd);
            return;
        }
        if (amount.get() >= amountToPay) {
            setSt(OK_PAY);
            LOG.debug("divideAmount notiMas OK_PAY " + notimascd);
        } else {
            setSt(PART_PAY);
            LOG.debug("divideAmount notiMas PART_PAY " + notimascd);
        }

        for (NotiDet notiDet : notiDets) {
            String notidetcd = notiDet.getNotidetcd();
            Long _amount = notiDetHandler.findAmountToPay(notiDet);
            Long theAmount = amount.get();
            if (theAmount == 0) {
                notiDet.setAmount(0);
                notiDet.setSt(NO_PAY);
                LOG.debug("divideAmount notiDet NO_PAY 0 " + notimascd + " " + notidetcd);
            } else {
                if (_amount < 0) {
                    notiDet.setAmount(_amount);
                    notiDet.setSt(OK_PAY);
                    LOG.debug("수납금액이상(<0)으로 남아있는 금액 조정하지 않음...");
                    LOG.debug("divideAmount notiDet OK_PAY 청구 " + _amount + " 수납 " + _amount + " " + notimascd + " " + notidetcd);

                } else {
                    if (theAmount.compareTo(_amount) >= 0) {
                        notiDet.setAmount(_amount);
                        notiDet.setSt(OK_PAY);
                        amount.set(theAmount - _amount);
                        LOG.debug("divideAmount notiDet OK_PAY 청구 " + _amount + " 수납 " + _amount + " " + notimascd + " " + notidetcd);
                    } else {


                        notiDet.setAmount(theAmount);
                        notiDet.setSt(PART_PAY);
                        amount.set(0);
                        LOG.debug("divideAmount notiDet PART_PAY 청구 " + _amount + " 수납 " + theAmount + " " + notimascd + " " + notidetcd);
                    }
                }

            }
        }
    }

    interface NotiDetHandler {
        public Long findAmountToPay(NotiDet notiDet);
    }


    /**
     * 총금액
     * 작은 금액
     * 계산
     */
    public void calculate() {
        long _minAmount = 0;
        long _remaingMinAmount = 0;
        totalAmount = 0;
        remainingAmount = 0;

        for (NotiDet notiDet : notiDets) {
            long amount = Damoas.toLong(notiDet.getPayitemamt());
            long paid = notiDet.getPaid();
            //***  이미 부분납이 경우를 처리해야 하므로 ***
            if (paid > amount) {
//                // 이런 경우는 예외 상항임..
//                // 청구금액보다 납부된게 많은 경우는 없다.
                LOG.debug("잘못된 수납금액 : 청구금액 " + amount + " 이미수납된 금액 " + paid);
            } else {
                LOG.debug("calculate 청구금액 " + amount + " 이미수납된 금액 " + paid);
            }
            LOG.debug("amount "+amount);
            long amountToPay = (amount - paid);
            remainingAmount += amountToPay;
            LOG.debug("amountToPay "+amountToPay);
            totalAmount += amount;
            if (_minAmount == 0) {
                _minAmount = amount;
            } else {
                if (amount < _minAmount) {
                    _minAmount = amount;
                }
            }
            if (_remaingMinAmount == 0) {
                _remaingMinAmount = amountToPay;
            } else {
                if (amountToPay < _remaingMinAmount) {
                    _remaingMinAmount = amountToPay;
                }
            }

        }
        remainingMinAmount = _remaingMinAmount;
        minAmount = _minAmount;

        LOG.debug("calculate totalAmount " + totalAmount);
        LOG.debug("calculate minAmount " + minAmount);
        LOG.debug("calculate remainingAmount " + remainingAmount);
        LOG.debug("calculate remainingMinAmount " + remainingMinAmount);
    }

    public boolean validateAmount(Long theAmount) {
        LOG.debug("validateAmount 청구금액=" + totalAmount + " 수납금액=" + theAmount);
        return totalAmount == theAmount;
    }

    public boolean validateDate(String msgSndDate) {
        String date = toTableDate(msgSndDate);
        LOG.debug("validateDate 수납일자=" + date + " 수납시작=" + startdate + " 수납종료=" + enddate);
        if (date.compareTo(startdate) >= 0 && enddate.compareTo(date) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateDateNAmount(Long theAmount, String msgSndDate) {
        LOG.debug("validateDateNAmount 금액="+theAmount+" 수납일자=" + msgSndDate + " 수납시작=" + startdate + " 수납종료=" + enddate);
        if (validateAmount(theAmount) && validateDate(msgSndDate)) {
            return true;
        } else {
            return false;
        }
    }

    private String toTableDate(String value) {
        return value.substring(0, 8);
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("chacd", chacd)
                .append("cusname", cusname)
                .append("st", st)
                .append("totalAmount", totalAmount)
                .toString();
    }
}
