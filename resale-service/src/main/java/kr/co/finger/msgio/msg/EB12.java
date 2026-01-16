package kr.co.finger.msgio.msg;

import kr.co.finger.damoa.exception.InvalidValueException;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EB12 extends BaseMsg {
    private NewHeader header;
    private List<NewData> dataList = new ArrayList<>();
    private NewTrailer trailer;

    public NewHeader getHeader() {
        return header;
    }

    public List<NewData> getDataList() {
        return dataList;
    }

    public void setHeader(NewHeader header) {
        this.header = header;
    }

    public NewTrailer getTrailer() {
        return trailer;
    }

    public void setTrailer(NewTrailer trailer) {
        this.trailer = trailer;
    }

    public void addDataList(NewData newData) {
        dataList.add(newData);
    }

    public int dataSize() {
        return dataList.size();
    }

    public NewData get(int i) {
        return dataList.get(i);
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("header", header)
                .append("dataList", dataList)
                .append("trailer", trailer)
                .toString();
    }
    public void from(List<Map<String, Object>> maps, String corpCd, String bankCode, String fileName, String now) throws InvalidValueException {
        NewHeader header = new NewHeader();
        header.setCorpCode(corpCd);
        header.setFileName(fileName);
        header.setReqData(now);
        setHeader(header);
        int index = 0;
        AtomicInteger addCount = new AtomicInteger(0);
        AtomicInteger delCount = new AtomicInteger(0);
        AtomicInteger tempDelCount = new AtomicInteger(0);

        for (Map<String, Object> map : maps) {
            String cmsType = getValue(map,"CMS_APP_GUBN");
            // 변경인 경우 해지,신규로 나누어 처리.
            if ("CM02".equalsIgnoreCase(cmsType)) {
                // 해지 처리
                index++;
                NewData data = create(index,corpCd,map,"CM03",bankCode);
                handleCount(data, addCount, delCount, tempDelCount);
                addDataList(data);
                // 신규 처리
                index++;
                data = create(index,corpCd,map,"CM01",bankCode);
                handleCount(data, addCount, delCount, tempDelCount);
                addDataList(data);
            } else {

                index++;
                NewData data = create(index,corpCd,map,cmsType,bankCode);
                handleCount(data, addCount, delCount, tempDelCount);
                addDataList(data);
            }
        }

        NewTrailer trailer = new NewTrailer();
        trailer.setCorpCode(corpCd);
        trailer.setFileName(fileName);
        trailer.setTotalRecordCount(maps.size()+"");
        trailer.setNewReqCount(addCount.get()+"");
        trailer.setMoveReqCount(delCount.get()+"");
        trailer.setTempMoveReqCount(tempDelCount.get()+"");
        setTrailer(trailer);
    }

    private void handleCount(NewData data,AtomicInteger addCount,AtomicInteger delCount,AtomicInteger tempDelCount) {
        String reqType = data.getReqType();
        if ("1".equalsIgnoreCase(reqType)) {
            //신규
            addCount.incrementAndGet();
        } else if ("3".equalsIgnoreCase(reqType)) {
            //삭제
            delCount.incrementAndGet();
        } else {
            // 이때는 없음...
        }

    }

    private NewData create(int index,String corpCd,Map<String,Object> map,String cmsType,String bankCode) throws InvalidValueException {
        NewData data = new NewData();
        data.setSeqNo(index+"");
        data.setCorpCode(corpCd);
        //신청일자 (등록일시?)
        data.setReqDate(getDateValue(map,"REGDT","yyMMdd"));
        //신청구분
        data.setReqType(toReqType(cmsType));
        // 납부자번호
        data.setPayerNo(getValue(map,"CHACD"));
        // 참가기관점코드 ?
        data.setBankCode(bankCode);
        // 계좌번호
        data.setWithdrawalAccountNo(getValue(map, "FEEACCNO"));
        // 사업자등록번호
        data.setCompRegNo(getValue(map,"FEEOFFNO"));

        return data;
    }
    /**
     * 해피콜 진행상태에서 신청구분 확인..
     * @param value
     * @return
     * @throws InvalidValueException
     */
    private String toReqType(String value) throws InvalidValueException {
        if ("CM01".equalsIgnoreCase(value)) {
            // 신규
            return "1";
        } else if ("CM03".equalsIgnoreCase(value)) {
            // 해지
            return "3";
        } else {
            // 임의해지와 변경은 없음.
            throw new InvalidValueException("invalid ReqType=" + value);
        }
    }
}
