package kr.co.finger.shinhandamoa.data;

import kr.co.finger.shinhandamoa.data.table.model.Xnotimasreqdet;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 청구서
 *
 * @author wisehouse@finger.co.kr
 */
public class InvoiceVO {
    private Xnotimasreqdet xnotimasreqdet;

    public String getMasterId() {
        return xnotimasreqdet.getNotimasreqcd();
    }

    public String getSeqNo() {
        return xnotimasreqdet.getSeqNo();
    }

    public String getTitle() {
        return xnotimasreqdet.getTitle();
    }

    public String getVano() {
        return xnotimasreqdet.getVano();
    }

    public String getCustomerName() {
        return xnotimasreqdet.getCusname();
    }

    public String getContent1() {
        return getContent(0);
    }

    public String getContent2() {
        return getContent(1);
    }

    public String getContent3() {
        return getContent(2);
    }

    public String getContent4() {
        return getContent(3);
    }

    public String getContent5() {
        return getContent(4);
    }

    public String getContent6() {
        return getContent(5);
    }

    public String getContent7() {
        return getContent(6);
    }

    public String getContent8() {
        return getContent(7);
    }

    public String getContent9() {
        return getContent(8);
    }

    public String getContent10() {
        return getContent(9);
    }

    private String getContent(int index) {
        if(index > 9 || index < 0) {
            return StringUtils.EMPTY;
        }

        final String content = StringUtils.defaultString(xnotimasreqdet.getContent());
        String[] contentList = StringUtils.split(content, "\n");

        if(index >= contentList.length) {
            return StringUtils.EMPTY;
        }

        return StringUtils.trim(contentList[index]);
    }

    public String getInvoiceChaName() {
        return xnotimasreqdet.getChaname();
    }

    public String getInvoiceChaContactNo() {
        return xnotimasreqdet.getTelno();
    }

    public String getDueDt() {
        return xnotimasreqdet.getDueDt();
    }

    public String getCustomerProperty1() {
        return xnotimasreqdet.getCusgubn1();
    }

    public String getCustomerProperty2() {
        return xnotimasreqdet.getCusgubn2();
    }

    public String getCustomerProperty3() {
        return xnotimasreqdet.getCusgubn3();
    }

    public String getCustomerProperty4() {
        return xnotimasreqdet.getCusgubn4();
    }

    public String getItem1Name() {
        return xnotimasreqdet.getItem1Name();
    }
    public String getItem1Amount() {
        return xnotimasreqdet.getItem1Amt();
    }
    public String getItem1Remark() {
        return xnotimasreqdet.getItem1Remark();
    }
    public String getItem2Name() {
        return xnotimasreqdet.getItem2Name();
    }
    public String getItem2Amount() {
        return xnotimasreqdet.getItem2Amt();
    }
    public String getItem2Remark() {
        return xnotimasreqdet.getItem2Remark();
    }
    public String getItem3Name() {
        return xnotimasreqdet.getItem3Name();
    }
    public String getItem3Amount() {
        return xnotimasreqdet.getItem3Amt();
    }
    public String getItem3Remark() {
        return xnotimasreqdet.getItem3Remark();
    }
    public String getItem4Name() {
        return xnotimasreqdet.getItem4Name();
    }
    public String getItem4Amount() {
        return xnotimasreqdet.getItem4Amt();
    }
    public String getItem4Remark() {
        return xnotimasreqdet.getItem4Remark();
    }
    public String getItem5Name() {
        return xnotimasreqdet.getItem5Name();
    }
    public String getItem5Amount() {
        return xnotimasreqdet.getItem5Amt();
    }
    public String getItem5Remark() {
        return xnotimasreqdet.getItem5Remark();
    }
    public String getItem6Name() {
        return xnotimasreqdet.getItem6Name();
    }
    public String getItem6Amount() {
        return xnotimasreqdet.getItem6Amt();
    }
    public String getItem6Remark() {
        return xnotimasreqdet.getItem6Remark();
    }
    public String getItem7Name() {
        return xnotimasreqdet.getItem7Name();
    }
    public String getItem7Amount() {
        return xnotimasreqdet.getItem7Amt();
    }
    public String getItem7Remark() {
        return xnotimasreqdet.getItem7Remark();
    }
    public String getItem8Name() {
        return xnotimasreqdet.getItem8Name();
    }
    public String getItem8Amount() {
        return xnotimasreqdet.getItem8Amt();
    }
    public String getItem8Remark() {
        return xnotimasreqdet.getItem8Remark();
    }
    public String getItem9Name() {
        return xnotimasreqdet.getItem9Name();
    }
    public String getItem9Amount() {
        return xnotimasreqdet.getItem9Amt();
    }
    public String getItem9Remark() {
        return xnotimasreqdet.getItem9Remark();
    }
    public String getTotalNoticeAmount() {
        return xnotimasreqdet.getNotiamt();
    }
    public String getRemark() {
        return xnotimasreqdet.getRemark();
    }
    public String getOptionalMessage() {
        return xnotimasreqdet.getOptContent();
    }
    public String getFooter1() {
        return xnotimasreqdet.getFooter1();
    }
    public String getFooter2() {
        return xnotimasreqdet.getFooter2();
    }
    public String getFooter3() {
        return xnotimasreqdet.getFooter3();
    }
    public String getChaContactNo2() {
        return xnotimasreqdet.getOwnertel();
    }
    public String getChaAddress() {
        return xnotimasreqdet.getAddress();
    }
    public String getCustomerProperty1Name() {
        return xnotimasreqdet.getCusgubn1Name();
    }
    public String getCustomerProperty2Name() {
        return xnotimasreqdet.getCusgubn2Name();
    }
    public String getCustomerProperty3Name() {
        return xnotimasreqdet.getCusgubn3Name();
    }
    public String getCustomerProperty4Name() {
        return xnotimasreqdet.getCusgubn4Name();
    }

    public String getCustomerTitle() {
        return xnotimasreqdet.getCusals();
    }
}
