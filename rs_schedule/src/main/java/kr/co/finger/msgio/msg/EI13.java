package kr.co.finger.msgio.msg;

import kr.co.finger.damoa.commons.DateUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EI13 extends BaseMsg {
    private EvidenceHeader header;
    private List<EvidenceData> dataList = new ArrayList<>();
    private EvidenceTrailer trailer;

    public EvidenceHeader getHeader() {
        return header;
    }

    public List<EvidenceData> getDataList() {
        return dataList;
    }

    public void setHeader(EvidenceHeader header) {
        this.header = header;
    }

    public EvidenceTrailer getTrailer() {
        return trailer;
    }

    public void setTrailer(EvidenceTrailer trailer) {
        this.trailer = trailer;
    }

    public void addDataList(EvidenceData newData) {
        dataList.add(newData);
    }

    public int dataSize() {
        return dataList.size();
    }

    public EvidenceData get(int i) {
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

    public void write(String dir) throws IOException {
        for (EvidenceData data : dataList) {
            data.write(dir);
        }
    }


    public void from(List<Map<String, Object>> maps,Date _now,String corpCd,String shinhan) throws IOException {

        String now = DateUtils.toString(_now, "yyyyMMdd");

        EvidenceHeader header = new EvidenceHeader();
        header.setCorpCode(corpCd);
        header.setReqData(now);
        header.setTotalEvidenceCount(maps.size()+"");
        setHeader(header);

        int index = 0;
        int totalSize = 0;
        for (Map<String, Object> map : maps) {
            String filePath = getValue(map,"CMS_FILE_NAME");
            if (isExist(filePath)==false) {
                System.out.println(filePath+" 존재하지 않음.");
                continue;
            }
            index++;
            EvidenceData data = new EvidenceData();
            data.setSeqNo(index+"");
            data.setCorpCode(corpCd);
            //납부자번호 - 가맹점코드
            data.setPayerNo(getValue(map,"CHACD"));
            //참가기관코드 - 계약지점코드
            data.setBankCd(shinhan);
            //계좌번호 - 수수료계좌번호
            data.setAccountNo(getValue(map,"FEEACCNO"));
            //신청일
            data.setReqData(getDateValue(map,"REGDT","yyyyMMdd"));
            //증빙자료 위치 -?
            data.setupEvidenceData(readFile(filePath),null);
            totalSize += data.size();
            addDataList(data);
        }

        EvidenceTrailer trailer = new EvidenceTrailer();
        trailer.setCorpCode(corpCd);
        trailer.setTotalDataRecordCount(index+"");
        trailer.setTotalDataBlockCount((totalSize/1024)+"");

        setTrailer(trailer);
    }

    private boolean isExist(String filePath) {
        return new File(filePath).exists();
    }

}
