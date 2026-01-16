package kr.co.finger.shinhandamoa.domain.model;

import kr.co.finger.shinhandamoa.constants.InvoiceApplyProcessStatus;
import kr.co.finger.shinhandamoa.constants.InvoiceApplyStatus;
import kr.co.finger.shinhandamoa.data.InvoiceApplyVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 고지서 신청서
 *
 * @author wisehouse@finger.co.kr
 */
public class InvoiceApplyDO {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceApplyDO.class);
    private final InvoiceApplyVO vo;

    public InvoiceApplyDO(InvoiceApplyVO vo) {
        this.vo = vo;
    }

    /**
     * 처리 상태를 진행 중으로 변경
     */
    public void changeProcessStatusIntoWIP() {
        vo.setProcessStatusCd(InvoiceApplyProcessStatus.WIP);
    }

    public String getId() {
        return vo.getId();
    }

    public String getStatusCode() {
        return vo.getStatusCode();
    }

    public String getProcessStatusCd() {
        return vo.getProcessStatusCd();
    }

    public String getChaCd() {
        return vo.getChaCd();
    }

    public String getMasMonth() {
        return vo.getMasMonth();
    }

    public String getDestinationPostalCode() {
        return vo.getDestinationPostalCode();
    }

    public String getDestinationAddress() {
        return vo.getDestinationAddress();
    }

    public String getDestinationManager() {
        return vo.getDestinationManager();
    }

    public String getDestinationContactNo() {
        return vo.getDestinationContactNo();
    }

    public String getFileNo() {
        return vo.getFileNo();
    }

    public String getDeliveryTypeCd() {
        return vo.getDeliveryTypeCd();
    }

    public InvoiceApplyVO getVO() {
        return vo;
    }

    @Override
    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toStringBuilder.append("id", this.getId());
        toStringBuilder.append("statusCode", this.getStatusCode());
        return toStringBuilder.build();
    }

    public void modify(InvoiceApplyDE example) {
        if(example == null) {
            return;
        }

        if (StringUtils.isNotBlank(example.getStatusCode())) {
            vo.setStatusCode(example.getStatusCode());
        }

        if (StringUtils.isNotBlank(example.getProcessStatusCd())) {
            vo.setProcessStatusCode(example.getProcessStatusCd());
        }
    }

    public void complete() {
        vo.setStatusCode(InvoiceApplyStatus.COMPLETE);
        vo.setProcessStatusCd(InvoiceApplyProcessStatus.SUCCESS);
        vo.setSendDate(new Date());
    }

    public void failAtBbtec() {
        vo.setProcessStatusCd(InvoiceApplyProcessStatus.FAIL_AT_BBTEC);
        vo.setStatusCode(InvoiceApplyStatus.FAILED);
    }

    public void failAtDamoa() {
        vo.setProcessStatusCd(InvoiceApplyProcessStatus.FAIL_AT_DAMOA);
        vo.setStatusCode(InvoiceApplyStatus.FAILED);
    }

    public void fail() {
        vo.setProcessStatusCd(InvoiceApplyProcessStatus.FAIL);
        vo.setStatusCode(InvoiceApplyStatus.FAILED);
    }

    public void fail(String invoiceApplyProcessStatusCd) {
        vo.setProcessStatusCd(invoiceApplyProcessStatusCd);
        vo.setStatusCode(InvoiceApplyStatus.FAILED);
    }

    public Date getSendDate() {
        return vo.getSendDate();
    }

    public void failAsNoData() {
        vo.setProcessStatusCd(InvoiceApplyProcessStatus.FAIL_AS_NO_DATA);
        vo.setStatusCode(InvoiceApplyStatus.FAILED);
    }

    public Date getCreateDate() {
        return vo.getCreateDate();
    }

    public String getFileDt() {
        return vo.getFileDt();
    }

    public void start(String fileDt, int fileNo) {
        vo.setProcessStatusCd(InvoiceApplyProcessStatus.STAND_BY);
        vo.setFileNo(fileNo);
        vo.setFileDt(fileDt);
    }

    public void ready() {
        vo.setStatusCode(InvoiceApplyStatus.WIP);
    }

    public int getInvoiceCount() {
        return vo.getInvoiceCount();
    }
}
