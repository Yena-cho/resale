package kr.co.finger.shinhandamoa.service.invoice;

import kr.co.finger.shinhandamoa.adapter.bbtec.InvoiceSamBody;
import kr.co.finger.shinhandamoa.common.StringByteUtils;
import kr.co.finger.shinhandamoa.constants.InvoiceApplyDeliveryType;
import kr.co.finger.shinhandamoa.domain.model.InvoiceApplyDO;
import kr.co.finger.shinhandamoa.domain.model.InvoiceDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;

import static kr.co.finger.shinhandamoa.adapter.bbtec.InvoiceSam.DEFAULT_CHARSET;

/**
 * 고지서 서비스 헬퍼
 *
 * @author wisehouse@finger.co.kr
 */
@Component
public class InvoiceHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceHelper.class);

    private static final String INVOICE_ROOT_PATH = "/invoice";

    private final static DecimalFormat FILE_SEQ_NO_FORMAT = new DecimalFormat("00");

    @Value("${resources.root-path}")
    private String resourceRootPath;

    public InvoiceSamBody castToInvoiceSamBody(InvoiceApplyDO item, InvoiceDO each) {
        final InvoiceSamBody body = new InvoiceSamBody();

        // 순번
        body.setSeqNo(StringUtils.right(each.getSeqNo(), 7));
        // 기관코드
        body.setChaCd(item.getChaCd());
        // 메시지 타이틀
        body.setTitle(StringByteUtils.left(each.getTitle(), DEFAULT_CHARSET, 20));
        // 수신자명
        final String customerName = StringUtils.join(new String[]{StringByteUtils.left(each.getCustomerName(), DEFAULT_CHARSET, 19), StringByteUtils.left(each.getCustomerTitle(), DEFAULT_CHARSET, 10)}, " ");
        body.setCustomerName(customerName);
        // 메시지1
        body.setContent1(each.getContent1());
        // 메시지2
        body.setContent2(each.getContent2());
        // 메시지3
        body.setContent3(each.getContent3());
        // 메시지4
        body.setContent4(each.getContent4());
        // 메시지5
        body.setContent5(each.getContent5());
        // 메시지6
        body.setContent6(each.getContent6());
        // 메시지7
        body.setContent7(each.getContent7());
        // 메시지8
        body.setContent8(each.getContent8());
        // 메시지9
        body.setContent9(each.getContent9());
        // 메시지10
        body.setContent10(each.getContent10());
        // 학원명
        body.setChaName(StringByteUtils.left(each.getInvoiceChaName(), DEFAULT_CHARSET, 30));
        // 학원전화번호
        body.setChaContactNo(each.getInvoiceChaContactNo());
        // 계좌번호
        body.setVano(formatVano(each.getVano()));
        // 납기년
        body.setYear(StringUtils.substring(each.getDueDt(), 0, 4));
        // 납기월
        body.setMonth(StringUtils.substring(each.getDueDt(), 4, 6));
        // 납기일
        body.setDay(StringUtils.substring(each.getDueDt(), 6, 8));
        // 그룹명
        body.setGubn(StringByteUtils.left(formatGubn(each), DEFAULT_CHARSET, 80));
        // 항목명1
        body.setItem1Name(each.getItem1Name());
        // 금액1
        body.setItem1Amount(each.getItem1Amount());
        // 비고1
        body.setItem1Remark(each.getItem1Remark());
        // 항목명2
        body.setItem2Name(each.getItem2Name());
        // 금액2
        body.setItem2Amount(each.getItem2Amount());
        // 비고2
        body.setItem2Remark(each.getItem2Remark());
        // 항목명3
        body.setItem3Name(each.getItem3Name());
        // 금액3
        body.setItem3Amount(each.getItem3Amount());
        // 비고3
        body.setItem3Remark(each.getItem3Remark());
        // 항목명4
        body.setItem4Name(each.getItem4Name());
        // 금액4
        body.setItem4Amount(each.getItem4Amount());
        // 비고4
        body.setItem4Remark(each.getItem4Remark());
        // 항목명5
        body.setItem5Name(each.getItem5Name());
        // 금액5
        body.setItem5Amount(each.getItem5Amount());
        // 비고5
        body.setItem5Remark(each.getItem5Remark());
        // 항목명6
        body.setItem6Name(each.getItem6Name());
        // 금액6
        body.setItem6Amount(each.getItem6Amount());
        // 비고6
        body.setItem6Remark(each.getItem6Remark());
        // 항목명7
        body.setItem7Name(each.getItem7Name());
        // 금액7
        body.setItem7Amount(each.getItem7Amount());
        // 비고7
        body.setItem7Remark(each.getItem7Remark());
        // 항목명8
        body.setItem8Name(each.getItem8Name());
        // 금액8
        body.setItem8Amount(each.getItem8Amount());
        // 비고8
        body.setItem8Remark(each.getItem8Remark());
        // 항목명9
        body.setItem9Name(each.getItem9Name());
        // 금액9
        body.setItem9Amount(each.getItem9Amount());
        // 비고9
        body.setItem9Remark(each.getItem9Remark());
        // 납부연월
        body.setYearMonth(item.getMasMonth());
        // 합계금액
        body.setTotalAmount(each.getTotalNoticeAmount());
        // 비고10
//        body.setNoticeRemark(each.getRemark());
        // 택배우편번호
        body.setDestinationPostalCode(item.getDestinationPostalCode());
        // 택배주소
        body.setDestinationAddress(item.getDestinationAddress());
        // 택배수신자명
        body.setDestinationManager(item.getDestinationManager());
        // 택배핸드폰번호
        body.setDestinationContactNo(item.getDestinationContactNo());
        // 추가메시지
        body.setOptionalMessage(each.getOptionalMessage());
        // 추가안내1
        body.setFooter1(each.getFooter1());
        // 추가안내2
        body.setFooter2(each.getFooter2());
        // 추가안내3
        body.setFooter3(each.getFooter3());
        // 대표번호
        body.setChaContactNo2(each.getChaContactNo2());
        // 기관주소
        body.setChaAddress(each.getChaAddress());

        return body;
    }

    private String formatGubn(InvoiceDO each) {
        StringBuilder customerProperty = new StringBuilder();
        if (StringUtils.isNotBlank(each.getCustomerProperty1Name()) && StringUtils.isNotBlank(each.getCustomerProperty1())) {
            customerProperty.append(each.getCustomerProperty1Name());
            customerProperty.append(" : ");
            customerProperty.append(each.getCustomerProperty1());
        }
        if (StringUtils.isNotBlank(each.getCustomerProperty2Name()) && StringUtils.isNotBlank(each.getCustomerProperty2())) {
            if(customerProperty.length() != 0) {
                customerProperty.append(" | ");
            }
            customerProperty.append(each.getCustomerProperty2Name());
            customerProperty.append(" : ");
            customerProperty.append(each.getCustomerProperty2());
        }
        if (StringUtils.isNotBlank(each.getCustomerProperty3Name()) && StringUtils.isNotBlank(each.getCustomerProperty3())) {
            if(customerProperty.length() != 0) {
                customerProperty.append(" | ");
            }
            customerProperty.append(each.getCustomerProperty3Name());
            customerProperty.append(" : ");
            customerProperty.append(each.getCustomerProperty3());
        }
        if (StringUtils.isNotBlank(each.getCustomerProperty4Name()) && StringUtils.isNotBlank(each.getCustomerProperty4())) {
            if(customerProperty.length() != 0) {
                customerProperty.append(" | ");
            }
            customerProperty.append(each.getCustomerProperty4Name());
            customerProperty.append(" : ");
            customerProperty.append(each.getCustomerProperty4());
        }

        return StringUtils.trim(customerProperty.toString());
    }

    private String formatVano(String vano) {
        final StringBuilder builder = new StringBuilder();
        builder.append(StringUtils.substring(vano, 0, 3));
        builder.append('-');

        builder.append(StringUtils.substring(vano, 3, 6));
        builder.append('-');

        builder.append(StringUtils.substring(vano, 6));

        return builder.toString();
    }

    public String getInvoiceFilePath(InvoiceApplyDO invoiceApplyDO) {
        final StringBuilder sb = new StringBuilder();
        sb.append(resourceRootPath);
        sb.append(INVOICE_ROOT_PATH);
        sb.append(File.separator);
        sb.append(invoiceApplyDO.getFileDt());
        sb.append(File.separator);

        // 날짜
        sb.append(invoiceApplyDO.getFileDt());
        sb.append("_");

        // 순번
        final int fileSeqNo = NumberUtils.toInt(invoiceApplyDO.getFileNo());
        sb.append(FILE_SEQ_NO_FORMAT.format(fileSeqNo));

        // 배송방법
        if (StringUtils.equals(invoiceApplyDO.getDeliveryTypeCd(), InvoiceApplyDeliveryType.EMERGENCY)) {
            sb.append("_Q");
        }

        sb.append(".txt");

        final String filePath = sb.toString();

        new File(filePath).getParentFile().mkdirs();

        return filePath;
    }
}
