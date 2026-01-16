package kr.co.finger.msgio.msg;

import kr.co.finger.damoa.commons.DateUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EB21 extends BaseMsg {
    private BatchHeader header;
    private List<BatchData> dataList = new ArrayList<>();
    private EB21BatchTrailer trailer;

    public BatchHeader getHeader() {
        return header;
    }

    public List<BatchData> getDataList() {
        return dataList;
    }

    public void setHeader(BatchHeader header) {
        this.header = header;
    }

    public EB21BatchTrailer getTrailer() {
        return trailer;
    }

    public void setTrailer(EB21BatchTrailer trailer) {
        this.trailer = trailer;
    }

    public void addDataList(BatchData newData) {
        dataList.add(newData);
    }

    public int dataSize() {
        return dataList.size();
    }

    public BatchData get(int i) {
        return dataList.get(i);
    }

    public static BatchHeader createHeader() {
        BatchHeader header = new BatchHeader();
        return header;
    }

    public static EB21BatchTrailer createTrailer() {
        return new EB21BatchTrailer();
    }

    public static BatchData createData() {
        return new BatchData();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("header", header)
                .append("dataList", dataList)
                .append("trailer", trailer)
                .toString();
    }


    public void from(List<Map<String, Object>> mapList, String corpCode, String mainBankCode, String depositAccrNo, String fileName, String now, String shinhanCode) {

        BatchHeader header = new BatchHeader();
        header.setCorpCode(corpCode);
        header.setFileName(fileName);
        header.setWithdrawalDate(now);
        header.setMainBankCd(mainBankCode);
        header.setDepositAccountNo(depositAccrNo);
        setHeader(header);

        int index = 0;
        int totalCount = 0;
        long totalAmount = 0;
        int partialCount = 0;
        long partialAmount = 0;
        for (Map<String, Object> map : mapList) {
            Long amount = findWithdrawalAmount(map);
            if (amount ==0) {

                continue;
            }
            index++;
            BatchData data = new BatchData();
            data.setSeqNo(index + "");
            data.setCorpCode(corpCode);
            //출금참가기관점코드 (계약지점코드)
            data.setWithdrawalBankCode(shinhanCode);
            //출금의뢰금액 XMONTHSUM의 청구수수료 + SMS 수수료 + LMS 수수료
            data.setWithdrawalAmout(amount+"");
            // 계좌번호
            data.setWithdrawalAccntNo(getValue(map, "FEEACCNO"));
            // 사업자등록번호
            data.setCompRegNo(getValue(map, "CHAOFFNO"));
            // 납부자번호
            data.setPayerNo(getValue(map, "CHACD"));

            // 출금형태는 전액출금으로 고정되어 있음..
            // 추후 부분출금으로 변경되는 걸 대비해서 처리로직은 해 두었음..

            if ("1".equalsIgnoreCase(data.getWithdrawalType())) {
                totalCount++;
                totalAmount += Long.valueOf(data.getWithdrawalAmout());
            } else {
                partialCount++;
                partialAmount += Long.valueOf(data.getWithdrawalAmout());
            }
            addDataList(data);
        }


        EB21BatchTrailer trailer = new EB21BatchTrailer();
        trailer.setCorpCode(corpCode);
        trailer.setFileName(fileName);
        trailer.setTatalRecordCount(index + "");
        trailer.setAllCount(totalCount + "");
        trailer.setAllAmount(totalAmount + "");
        trailer.setPartialCount(partialCount+"");
        trailer.setPartialAmount(partialAmount+"");

        setTrailer(trailer);
    }

    /**
     * 청구수수료와 SMS 수수료를 더한 금액.
     *
     * @param map
     * @return
     */
    private Long findWithdrawalAmount(Map<String, Object> map) {
        String notiFee = getValue(map, "NOTIFEE");  //청구수수료
        String smsFee = getValue(map, "SMSFEE");    //SMS 수수료
        String lmsFee = getValue(map, "LMSFEE");    //LMS 수수료

        return (Long.valueOf(longNvl(notiFee)) + Long.valueOf(longNvl(smsFee)) + Long.valueOf(longNvl(lmsFee)));
    }

    private String longNvl(String value) {
        if (value == null || "".equalsIgnoreCase(value)) {
            return "0";
        } else {
            return value;
        }
    }

}
