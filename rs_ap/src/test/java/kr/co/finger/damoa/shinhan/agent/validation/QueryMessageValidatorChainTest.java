package kr.co.finger.damoa.shinhan.agent.validation;

import kr.co.finger.damoa.model.msg.QueryMessage;
import kr.co.finger.damoa.shinhan.agent.domain.model.*;
import kr.co.finger.damoa.shinhan.agent.model.ValidationResult;
import kr.co.finger.damoa.shinhan.agent.validation.query.QueryMessageValidatorChain;
import kr.co.finger.shinhandamoa.data.table.model.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 수취
 */
public class QueryMessageValidatorChainTest {
    private QueryMessageValidatorChain validator;
    
    @Before
    public void before() {
        validator = new QueryMessageValidatorChain();
    }

    /**
     * 거래금액 확인 안하는 경우
     * 
     */
//    @Test
//    public void validate1() {
//        final String chaCd = "20007389";
//        final String accountNo = "56209799123412";
//
//        final String chast = "ST06";
//        final String amtchkty = "N";
//        final String partialPayment = "Y";
//        final long transactionAmount = 30000L;
//
//        final QueryMessage requestInterface = new QueryMessage();
//        requestInterface.setDepositCorpCode(chaCd);
//        requestInterface.setDepositAccountNo(accountNo);
//        requestInterface.setTransactionAmount(String.valueOf(transactionAmount));
//
//        final ChaDO cha = newCha(chaCd, chast, amtchkty, partialPayment);
//        final CustomerDO customer = newCustomer(accountNo);
//        final List<NoticeMasterDO> noticeMasterList = buildNoticeMasterList(accountNo);
//
//        final ValidationResult result = validator.validate(requestInterface, cha, customer, noticeMasterList);
//
//        assertEquals(result.getCode(), "0000");
//    }
//
//    /**
//     * 거래금액 확인하고, 금액 맞는 경우
//     *
//     */
//    @Test
//    public void validate2() {
//        final String chaCd = "20007389";
//        final String accountNo = "56209799123412";
//
//        final String chast = "ST06";
//        final String amtchkty = "Y";
//        final String partialPayment = "N";
//        final long transactionAmount = 90000L;
//
//        final QueryMessage requestInterface = new QueryMessage();
//        requestInterface.setDepositCorpCode(chaCd);
//        requestInterface.setDepositAccountNo(accountNo);
//        requestInterface.setTransactionAmount(String.valueOf(transactionAmount));
//
//        final ChaDO cha = newCha(chaCd, chast, amtchkty, partialPayment);
//        final CustomerDO customer = newCustomer(accountNo);
//        final List<NoticeMasterDO> noticeMasterList = buildNoticeMasterList(accountNo);
//
//        final ValidationResult result = validator.validate(requestInterface, cha, customer, noticeMasterList);
//
//        assertEquals(result.getCode(), "0000");
//    }
//
//    /**
//     * 거래금액 확인하고, 금액 안맞는 경우
//     *
//     */
//    @Test
//    public void validate3() {
//        final String chaCd = "20007389";
//        final String accountNo = "56209799123412";
//
//        final String chast = "ST06";
//        final String amtchkty = "Y";
//        final String partialPayment = "N";
//        final long transactionAmount = 98000L;
//
//        final QueryMessage requestInterface = new QueryMessage();
//        requestInterface.setDepositCorpCode(chaCd);
//        requestInterface.setDepositAccountNo(accountNo);
//        requestInterface.setTransactionAmount(String.valueOf(transactionAmount));
//
//        final ChaDO cha = newCha(chaCd, chast, amtchkty, partialPayment);
//        final CustomerDO customer = newCustomer(accountNo);
//        final List<NoticeMasterDO> noticeMasterList = buildNoticeMasterList(accountNo);
//
//        final ValidationResult result = validator.validate(requestInterface, cha, customer, noticeMasterList);
//
//        assertEquals(result.getCode(), "V407");
//    }
//
//    /**
//     * 거래금액 확인하고, 부분납 맞는 경우 #1
//     *
//     */
//    @Test
//    public void validate4() {
//        final String chaCd = "20007389";
//        final String accountNo = "56209799123412";
//
//        final String chast = "ST06";
//        final String amtchkty = "Y";
//        final String partialPayment = "Y";
//        final long transactionAmount = 90000L;
//
//        final QueryMessage requestInterface = new QueryMessage();
//        requestInterface.setDepositCorpCode(chaCd);
//        requestInterface.setDepositAccountNo(accountNo);
//        requestInterface.setTransactionAmount(String.valueOf(transactionAmount));
//
//        final ChaDO cha = newCha(chaCd, chast, amtchkty, partialPayment);
//        final CustomerDO customer = newCustomer(accountNo);
//        final List<NoticeMasterDO> noticeMasterList = buildNoticeMasterList(accountNo);
//
//        final ValidationResult result = validator.validate(requestInterface, cha, customer, noticeMasterList);
//
//        assertEquals(result.getCode(), "0000");
//    }
//
//    /**
//     * 거래금액 확인하고, 부분납 맞는 경우 #2
//     *
//     */
//    @Test
//    public void validate5() {
//        final String chaCd = "20007389";
//        final String accountNo = "56209799123412";
//
//        final String chast = "ST06";
//        final String amtchkty = "Y";
//        final String partialPayment = "Y";
//        final long transactionAmount = 20000L;
//
//        final QueryMessage requestInterface = new QueryMessage();
//        requestInterface.setDepositCorpCode(chaCd);
//        requestInterface.setDepositAccountNo(accountNo);
//        requestInterface.setTransactionAmount(String.valueOf(transactionAmount));
//
//        final ChaDO cha = newCha(chaCd, chast, amtchkty, partialPayment);
//        final CustomerDO customer = newCustomer(accountNo);
//        final List<NoticeMasterDO> noticeMasterList = buildNoticeMasterList(accountNo);
//
//        final ValidationResult result = validator.validate(requestInterface, cha, customer, noticeMasterList);
//
//        assertEquals(result.getCode(), "0000");
//    }
//
//    /**
//     * 거래금액 확인하고, 부분납 맞는 경우 #3
//     *
//     */
//    @Test
//    public void validate6() {
//        final String chaCd = "20007389";
//        final String accountNo = "56209799123412";
//
//        final String chast = "ST06";
//        final String amtchkty = "Y";
//        final String partialPayment = "Y";
//        final long transactionAmount = 30000L;
//
//        final QueryMessage requestInterface = new QueryMessage();
//        requestInterface.setDepositCorpCode(chaCd);
//        requestInterface.setDepositAccountNo(accountNo);
//        requestInterface.setTransactionAmount(String.valueOf(transactionAmount));
//
//        final ChaDO cha = newCha(chaCd, chast, amtchkty, partialPayment);
//        final CustomerDO customer = newCustomer(accountNo);
//        final List<NoticeMasterDO> noticeMasterList = buildNoticeMasterList(accountNo);
//
//        final ValidationResult result = validator.validate(requestInterface, cha, customer, noticeMasterList);
//
//        assertEquals(result.getCode(), "0000");
//    }
//
//    /**
//     * 거래금액 확인하고, 부분납 안맞는 경우
//     *
//     */
//    @Test
//    public void validate7() {
//        final String chaCd = "20007389";
//        final String accountNo = "56209799123412";
//
//        final String chast = "ST06";
//        final String amtchkty = "Y";
//        final String partialPayment = "Y";
//        final long transactionAmount = 33000L;
//
//        final QueryMessage requestInterface = new QueryMessage();
//        requestInterface.setDepositCorpCode(chaCd);
//        requestInterface.setDepositAccountNo(accountNo);
//        requestInterface.setTransactionAmount(String.valueOf(transactionAmount));
//
//        final ChaDO cha = newCha(chaCd, chast, amtchkty, partialPayment);
//        final CustomerDO customer = newCustomer(accountNo);
//        final List<NoticeMasterDO> noticeMasterList = buildNoticeMasterList(accountNo);
//
//        final ValidationResult result = validator.validate(requestInterface, cha, customer, noticeMasterList);
//
//        assertEquals(result.getCode(), "V407");
//    }
//
//    /**
//     * 거래금액 확인하고, 청구원장 없는 경우
//     *
//     */
//    @Test
//    public void validate8() {
//        final String chaCd = "20007389";
//        final String accountNo = "56209799123412";
//
//        final String chast = "ST06";
//        final String amtchkty = "Y";
//        final String partialPayment = "Y";
//        final long transactionAmount = 33000L;
//
//        final QueryMessage requestInterface = new QueryMessage();
//        requestInterface.setDepositCorpCode(chaCd);
//        requestInterface.setDepositAccountNo(accountNo);
//        requestInterface.setTransactionAmount(String.valueOf(transactionAmount));
//
//        final ChaDO cha = newCha(chaCd, chast, amtchkty, partialPayment);
//        final CustomerDO customer = newCustomer(accountNo);
//        final List<NoticeMasterDO> noticeMasterList = new ArrayList<>();
//
//        final ValidationResult result = validator.validate(requestInterface, cha, customer, noticeMasterList);
//
//        assertEquals(result.getCode(), "V814");
//    }
    

    private CustomerDO newCustomer(String accountNo) {
        final Xcusmas xcusmas = new Xcusmas();
        xcusmas.setVano(accountNo);
        return new CustomerDO(xcusmas);
    }

    private ChaDO newCha(String chaCd, String chast, String amtchkty, String partialPayment) {
        final Xchalist xchalist = new Xchalist();
        xchalist.setChast(chast);
        xchalist.setChacd(chaCd);
        xchalist.setAmtchkty(amtchkty);
        xchalist.setPartialPayment(partialPayment);
        return new ChaDO(xchalist);
    }

    private List<NoticeMasterDO> buildNoticeMasterList(String accountNo) {
        final List<NoticeMasterDO> noticeMasterList = new ArrayList<>();
        
        final NoticeMasterDO noticeMaster = new NoticeMasterDO(newNotiMas(accountNo));
        noticeMaster.addDetails(newNoticeDetailsDO(2000L, "N"));
        noticeMaster.addDetails(newNoticeDetailsDO(4000L, "N"));
        noticeMaster.addDetails(newNoticeDetailsDO(6000L, "N"));
        noticeMaster.addDetails(newNoticeDetailsDO(8000L, "N"));
        noticeMaster.addDetails(newNoticeDetailsDO(10000L, "Y"));
        noticeMaster.addDetails(newNoticeDetailsDO(12000L, "Y"));
        noticeMaster.addDetails(newNoticeDetailsDO(14000L, "Y"));
        noticeMaster.addDetails(newNoticeDetailsDO(16000L, "Y"));
        noticeMaster.addDetails(newNoticeDetailsDO(18000L, "Y"));
        
        noticeMasterList.add(noticeMaster);
        
        return noticeMasterList;
    }

    private NoticeDetailsDO newNoticeDetailsDO(long amount, String optionalYn) {
        final Xnotidet xnotidet = new Xnotidet();
        xnotidet.setPayitemamt(amount);
        
        final NoticeDetailsDO noticeDetailsDO = new NoticeDetailsDO(xnotidet);
        noticeDetailsDO.setPayItem(newPayItem(optionalYn));
        return noticeDetailsDO;
    }

    private PayItemDO newPayItem(String optionalYn) {
        Xpayitem xpayitem = new Xpayitem();
        xpayitem.setPayitemselyn(optionalYn);
        return new PayItemDO(xpayitem);
    }

    private Xnotimas newNotiMas(String accountNo) {
        final Xnotimas xnotimas = new Xnotimas();
        xnotimas.setNotimascd(StringUtils.leftPad(String.valueOf(System.currentTimeMillis()), 20, '0'));
        xnotimas.setVano(accountNo);
        
        return xnotimas;
    }

}
