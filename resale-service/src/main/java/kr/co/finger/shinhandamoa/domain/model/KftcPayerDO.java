package kr.co.finger.shinhandamoa.domain.model;

import com.finger.shinhandamoa.data.table.model.KftcPayer;
import org.apache.commons.lang3.StringUtils;

/**
 * 금융결제원 납부자 도메인
 * 
 * @author wisehouse@finger.co.kr
 */
public class KftcPayerDO {
    private KftcPayer kftcPayer;

    public KftcPayerDO(KftcPayer kftcPayer) {
        this.kftcPayer = kftcPayer;
    }

    public String getId() {
        return kftcPayer.getId();
    }

    public String getPayerKftcCode() {
        return kftcPayer.getPayerKftcCode();
    }

    public String getPayerAccountNo() {
        return kftcPayer.getPayerAccountNo();
    }

    public String getPayerIdentityNo() {
        return kftcPayer.getPayerIdentityNo();
    }

    public String getPayerContactNo() {
        return kftcPayer.getContactNo();
    }

    public void register(String eb13Id) {
        setStatusCd("K00002");
        setSuccessYn(StringUtils.EMPTY);
        setResultCd(StringUtils.EMPTY);
        setEb13Id(eb13Id);
    }

    private void setStatusCd(String statusCd) {
        kftcPayer.setStatusCd(statusCd);
    }

    public KftcPayer getKftcPayer() {
        return kftcPayer;
    }

    public void updateResult(String successYn, String resultCd, String eb14Id) {
        setSuccessYn(successYn);
        setResultCd(resultCd);
        setEb14Id(eb14Id);
        if(StringUtils.equals(successYn, "Y")) {
            setStatusCd("K00003");
        } else {
            setStatusCd("K00000");
        }
    }

    private void setResultCd(String resultCd) {
        this.kftcPayer.setResultCd(resultCd);
    }

    private void setSuccessYn(String successYn) {
        this.kftcPayer.setSuccessYn(successYn);
    }
    
    private void setEb13Id(String eb13Id) {
        this.kftcPayer.setEb13Id(eb13Id);
    }
    
    private void setEb14Id(String eb14Id) {
        this.kftcPayer.setEb14Id(eb14Id);
    }

    public String getResultCd() {
        return this.kftcPayer.getResultCd();
    }

    public String getSuccessYn() {
        return this.kftcPayer.getSuccessYn();
    }
}
