package kr.co.finger.shinhandamoa.adapter.bbtec;

import kr.co.finger.shinhandamoa.common.SamElement;

/**
 * 고지서 본문
 *
 * @author wisehouse@finger.co.kr
 */
public class InvoiceSamBody extends SamElement {
    public InvoiceSamBody() {
        super(3755, ' ');
        this.setString(0, 1, "D");
    }

    /**
     * SEQ
     *
     * @param seqNo
     */
    public void setSeqNo(String seqNo) {
        this.setString(1, 7, seqNo);
    }

    /**
     * 기관코드
     *
     * @param chaCd
     */
    public void setChaCd(String chaCd) {
        this.setString(8, 8, chaCd);
    }

    /**
     * 메시지 타이틀
     *
     * @param title
     */
    public void setTitle(String title) {
        this.setString(36, 20, title);
    }

    /**
     * 수신자명
     *
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.setString(56, 30, customerName);
    }

    /**
     * 메시지1
     *
     * @param content1
     */
    public void setContent1(String content1) {
        this.setString(86, 160, content1);
    }

    /**
     * 메시지2
     *
     * @param content2
     */
    public void setContent2(String content2) {
        this.setString(246, 160, content2);
    }

    /**
     * 메시지3
     *
     * @param content3
     */
    public void setContent3(String content3) {
        this.setString(406, 160, content3);
    }

    /**
     * 메시지4
     *
     * @param content4
     */
    public void setContent4(String content4) {
        this.setString(566, 160, content4);
    }

    /**
     * 메시지5
     *
     * @param content5
     */
    public void setContent5(String content5) {
        this.setString(726, 160, content5);
    }

    /**
     * 메시지6
     *
     * @param content6
     */
    public void setContent6(String content6) {
        this.setString(886, 160, content6);
    }

    /**
     * 메시지7
     *
     * @param content7
     */
    public void setContent7(String content7) {
        this.setString(1046, 160, content7);
    }

    /**
     * 메시지8
     *
     * @param content8
     */
    public void setContent8(String content8) {
        this.setString(1206, 160, content8);
    }

    /**
     * 메시지9
     *
     * @param content9
     */
    public void setContent9(String content9) {
        this.setString(1366, 160, content9);
    }

    /**
     * 메시지10
     *
     * @param content10
     */
    public void setContent10(String content10) {
        this.setString(1526, 160, content10);
    }

    /**
     * 학원명
     *
     * @param chaName
     */
    public void setChaName(String chaName) {
        this.setString(1686, 30, chaName);
    }

    /**
     * 학원전화번호
     *
     * @param chaContactNo
     */
    public void setChaContactNo(String chaContactNo) {
        this.setString(1716, 15, chaContactNo);
    }

    /**
     * 계좌번호
     *
     * @param vano
     */
    public void setVano(String vano) {
        this.setString(1731, 20, vano);
    }

    /**
     * 납기년
     *
     * @param year
     */
    public void setYear(String year) {
        this.setString(1751, 4, year);
    }

    /**
     * 납기월
     *
     * @param month
     */
    public void setMonth(String month) {
        this.setString(1755, 2, month);
    }

    /**
     * 납기일
     *
     * @param day
     */
    public void setDay(String day) {
        this.setString(1757, 2, day);
    }

    /**
     * 그룹명
     *
     * @param gubn
     */
    public void setGubn(String gubn) {
        this.setString(1759, 80, gubn);
    }

    /**
     * 항목명1
     *
     * @param item1Name
     */
    public void setItem1Name(String item1Name) {
        this.setString(1839, 30, item1Name);
    }

    /**
     * 금액1
     *
     * @param item1Amount
     */
    public void setItem1Amount(long item1Amount) {
        if(item1Amount < 1) {
            return;
        }

        this.setLong(1869, 12, item1Amount);
    }

    /**
     * 비고1
     *
     * @param item1Remark
     */
    public void setItem1Remark(String item1Remark) {
        this.setString(1881, 50, item1Remark);
    }

    /**
     * 항목명2
     *
     * @param item2Name
     */
    public void setItem2Name(String item2Name) {
        this.setString(1931, 30, item2Name);
    }

    /**
     * 금액2
     *
     * @param item2Amount
     */
    public void setItem2Amount(long item2Amount) {
        if(item2Amount < 1) {
            return;
        }

        this.setLong(1961, 12, item2Amount);
    }

    /**
     * 비고2
     *
     * @param item2Remark
     */
    public void setItem2Remark(String item2Remark) {
        this.setString(1973, 50, item2Remark);
    }

    /**
     * 항목명3
     *
     * @param item3Name
     */
    public void setItem3Name(String item3Name) {
        this.setString(2023, 30, item3Name);
    }

    /**
     * 금액3
     *
     * @param item3Amount
     */
    public void setItem3Amount(long item3Amount) {
        if(item3Amount < 1) {
            return;
        }

        this.setLong(2053, 12, item3Amount);
    }

    /**
     * 비고3
     *
     * @param item3Remark
     */
    public void setItem3Remark(String item3Remark) {
        this.setString(2065, 50, item3Remark);
    }

    /**
     * 항목명4
     *
     * @param item4Name
     */
    public void setItem4Name(String item4Name) {
        this.setString(2115, 30, item4Name);
    }

    /**
     * 금액4
     *
     * @param item4Amount
     */
    public void setItem4Amount(long item4Amount) {
        if(item4Amount < 1) {
            return;
        }

        this.setLong(2145, 12, item4Amount);
    }

    /**
     * 비고4
     *
     * @param item4Remark
     */
    public void setItem4Remark(String item4Remark) {
        this.setString(2157, 50, item4Remark);
    }

    /**
     * 항목명5
     *
     * @param item5Name
     */
    public void setItem5Name(String item5Name) {
        this.setString(2207, 30, item5Name);
    }

    /**
     * 금액5
     *
     * @param item5Amount
     */
    public void setItem5Amount(long item5Amount) {
        if(item5Amount < 1) {
            return;
        }

        this.setLong(2237, 12, item5Amount);
    }

    /**
     * 비고5
     *
     * @param item5Remark
     */
    public void setItem5Remark(String item5Remark) {
        this.setString(2249, 50, item5Remark);
    }

    /**
     * 항목명6
     *
     * @param item6Name
     */
    public void setItem6Name(String item6Name) {
        this.setString(2299, 30, item6Name);
    }

    /**
     * 금액6
     *
     * @param item6Amount
     */
    public void setItem6Amount(long item6Amount) {
        if(item6Amount < 1) {
            return;
        }

        this.setLong(2329, 12, item6Amount);
    }

    /**
     * 비고6
     *
     * @param item6Remark
     */
    public void setItem6Remark(String item6Remark) {
        this.setString(2341, 50, item6Remark);
    }

    /**
     * 항목명7
     *
     * @param item7Name
     */
    public void setItem7Name(String item7Name) {
        this.setString(2391, 30, item7Name);
    }

    /**
     * 금액7
     *
     * @param item7Amount
     */
    public void setItem7Amount(long item7Amount) {
        if(item7Amount < 1) {
            return;
        }

        this.setLong(2421, 12, item7Amount);
    }

    /**
     * 비고7
     *
     * @param item7Remark
     */
    public void setItem7Remark(String item7Remark) {
        this.setString(2433, 50, item7Remark);
    }

    /**
     * 항목명8
     *
     * @param item8Name
     */
    public void setItem8Name(String item8Name) {
        this.setString(2483, 30, item8Name);
    }

    /**
     * 금액8
     *
     * @param item8Amount
     */
    public void setItem8Amount(long item8Amount) {
        if(item8Amount < 1) {
            return;
        }

        this.setLong(2513, 12, item8Amount);
    }

    /**
     * 비고8
     *
     * @param item8Remark
     */
    public void setItem8Remark(String item8Remark) {
        this.setString(2525, 50, item8Remark);
    }

    /**
     * 항목명9
     *
     * @param item9Name
     */
    public void setItem9Name(String item9Name) {
        this.setString(2575, 30, item9Name);
    }

    /**
     * 금액9
     *
     * @param item9Amount
     */
    public void setItem9Amount(long item9Amount) {
        if(item9Amount < 1) {
            return;
        }

        this.setLong(2605, 12, item9Amount);
    }

    /**
     * 비고9
     *
     * @param item9Remark
     */
    public void setItem9Remark(String item9Remark) {
        this.setString(2617, 50, item9Remark);
    }

    /**
     * 납부연월
     *
     * @param yearMonth
     */
    public void setYearMonth(String yearMonth) {
        this.setString(2667, 30, yearMonth);
    }

    /**
     * 합계금액
     *
     * @param totalAmount
     */
    public void setTotalAmount(long totalAmount) {
        this.setLong(2697, 12, totalAmount);
    }

    /**
     * 청구비고
     *
     * @param noticeRemark
     */
    public void setNoticeRemark(String noticeRemark) {
        this.setString(2709, 50, noticeRemark);
    }

    /**
     * 택배우편번호
     *
     * @param destinationPostalCode
     */
    public void setDestinationPostalCode(String destinationPostalCode) {
        this.setString(2759, 6, destinationPostalCode);
    }

    /**
     * 택배주소
     *
     * @param destinationAddress
     */
    public void setDestinationAddress(String destinationAddress) {
        this.setString(2765, 150, destinationAddress);
    }

    /**
     * 택배수신자명
     *
     * @param destinationManager
     */
    public void setDestinationManager(String destinationManager) {
        this.setString(2915, 180, destinationManager);
    }

    /**
     * 택배수신연락처
     *
     * @param destinationContactNo
     */
    public void setDestinationContactNo(String destinationContactNo) {
        this.setString(3095, 15, destinationContactNo);
    }

    /**
     * 추가메시지
     *
     * @param optionalMessage
     */
    public void setOptionalMessage(String optionalMessage) {
        this.setString(3110, 120, optionalMessage);
    }

    /**
     * 추가안내1
     *
     * @param footer1
     */
    public void setFooter1(String footer1) {
        this.setString(3230, 120, footer1);
    }

    /**
     * 추가안내2
     *
     * @param footer2
     */
    public void setFooter2(String footer2) {
        this.setString(3350, 120, footer2);
    }

    /**
     * 추가안내3
     *
     * @param footer3
     */
    public void setFooter3(String footer3) {
        this.setString(3470, 120, footer3);
    }

    /**
     * 대표번호
     *
     * @param chaContactNo2
     */
    public void setChaContactNo2(String chaContactNo2) {
        this.setString(3590, 15, chaContactNo2);
    }

    /**
     * 기관주소
     *
     * @param chaAddress
     */
    public void setChaAddress(String chaAddress) {
        this.setString(3605, 150, chaAddress);
    }
}
