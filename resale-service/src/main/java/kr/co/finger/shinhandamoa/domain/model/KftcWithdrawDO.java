package kr.co.finger.shinhandamoa.domain.model;

import com.finger.shinhandamoa.data.table.model.KftcWithdraw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 금융결제원 출금이체 도메인
 * 
 * @author wisehouse@finger.co.kr
 */
public class KftcWithdrawDO {
    private static final Logger LOGGER = LoggerFactory.getLogger(KftcWithdrawDO.class);
    
    private KftcWithdraw kftcWithdraw;

    public KftcWithdrawDO(KftcWithdraw kftcWithdraw) {
        this.kftcWithdraw = kftcWithdraw;
    }


    public long getAmount() {
        return kftcWithdraw.getAmount();
    }

    public String getPayerIdentityNo() {
        return kftcWithdraw.getPayerIdentityNo();
    }

    public String getRemark() {
        return kftcWithdraw.getRemark();
    }

    public String getPayerNo() {
        return kftcWithdraw.getPayerNo();
    }

    public String getContactNo() {
        return kftcWithdraw.getContactNo();
    }

    public String getWithdrawTypeCd() {
        return kftcWithdraw.getWithdrawTypeCd();
    }

    public String getPayerKftcCode() {
        return kftcWithdraw.getPayerKftcCode();
    }

    public String getPayerAccountNo() {
        return kftcWithdraw.getPayerAccountNo();
    }

    public void register(String eb21Id, String dataNo) {
        this.setStatusCd("K00002");
        this.setEb21Id(eb21Id);
        this.setDataNo(dataNo);
    }

    private void setStatusCd(String statusCd) {
        this.kftcWithdraw.setStatusCd(statusCd);
    }

    public KftcWithdraw getKftcWithdraw() {
        return kftcWithdraw;
    }

    private void setEb21Id(String eb21Id) {
        kftcWithdraw.setEb21Id(eb21Id);
    }

    private void setDataNo(String dataNo) {
        kftcWithdraw.setDataNo(dataNo);
    }

    public void updateResult(String successYn, String resultCd) {
        this.setSuccessYn(successYn);
        this.setResultCd(resultCd);
        this.setStatusCd("K00003");
    }

    private void setSuccessYn(String successYn) {
        kftcWithdraw.setSuccessYn(successYn);
    }

    private void setResultCd(String resultCd) {
        kftcWithdraw.setResultCd(resultCd);
    }

    public String getDataNo() {
        return this.kftcWithdraw.getDataNo();
    }
}
