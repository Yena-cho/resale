package kr.co.finger.shinhandamoa.domain.model;

import com.finger.shinhandamoa.data.table.model.Cha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 이용기관 도메인
 * 
 * @author wisehouse@finger.co.kr
 */
public class ClientDO {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientDO.class);
    
    private Cha cha;
    
    public ClientDO(Cha cha) {
        this.cha = cha;
    }

    private void setKftcPayerStatus(String kftcPayerStatus) {
        this.cha.setCmsReqSt(kftcPayerStatus);
    }

    public Cha getCha() {
        return cha;
    }

    public String getWithdrawAgreementId() {
        return cha.getWithdrawAgreementId();
    }

    public void failToRegisterAsKftcPayer() {
        setKftcPayerStatus("CST01");
        setWithdrawAgreeStatus("A00002");
    }

    private void setWithdrawAgreeStatus(String withdrawAgreeStatus) {
        cha.setFingerFeeAgreeStatus(withdrawAgreeStatus);
    }

    public void completeRegisterAsKftcPayer() {
        setKftcPayerStatus("CST04");
    }

    public void registerAsKftcPayer() {
        setKftcPayerStatus("CST03");
    }
    
    public void readyToRegisterAsKftcPayer(String payerId) {
        setKftcPayerStatus("CST02");
        setKftcPayerId(payerId);
    }

    private void setKftcPayerId(String payerId) {
        this.cha.setFingerFeeOwnerNo(payerId);
    }

    public String getIdentityNo() {
        return cha.getChaoffno();
    }

    public String getName() {
        return cha.getChaname();
    }

    public String getOwnerName() {
        return cha.getOwner();
    }

    public String getBusinessType() {
        return cha.getChastatus();
    }

    public String getBusinessKind() {
        return cha.getChatype();
    }

    public String getContactNo() {
        return cha.getTelno();
    }

    public String getInvoiceEmail() {
        return cha.getChrmail();
    }

    public String getAddress() {
        return cha.getChaaddress1();
    }
}
