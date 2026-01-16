package com.finger.shinhandamoa.org.claimMgmt.dto;

/**
 * 기관 DTO
 * 
 * @author wisehouse@finger.co.kr
 */
public class ChaDTO {
    private boolean isEnabledPartialPayment;
    private boolean isEnabledCheckAmount;
    private boolean isEnabledCheckPeriod;
    private boolean isEnabledMultipleAccount;

    public boolean isEnabledPartialPayment() {
        return isEnabledPartialPayment;
    }

    public void setEnabledPartialPayment(boolean enablePartialPayment) {
        isEnabledPartialPayment = enablePartialPayment;
    }

    public boolean isEnabledCheckAmount() {
        return isEnabledCheckAmount;
    }

    public void setEnabledCheckAmount(boolean enableCheckAmount) {
        isEnabledCheckAmount = enableCheckAmount;
    }

    public boolean isEnabledCheckPeriod() {
        return isEnabledCheckPeriod;
    }

    public void setEnabledCheckPeriod(boolean enableCheckPeriod) {
        isEnabledCheckPeriod = enableCheckPeriod;
    }

    public boolean isEnabledMultipleAccount() {
        return isEnabledMultipleAccount;
    }

    public void setEnabledMultipleAccount(boolean enableMultipleAccount) {
        isEnabledMultipleAccount = enableMultipleAccount;
    }
}
