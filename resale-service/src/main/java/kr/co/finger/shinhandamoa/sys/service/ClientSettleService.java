package kr.co.finger.shinhandamoa.sys.service;

import com.finger.shinhandamoa.common.ListResult;
import com.finger.shinhandamoa.common.PageBounds;
import com.finger.shinhandamoa.common.XlsxBuilder;
import kr.co.finger.shinhandamoa.domain.model.DailyClientSettleDO;
import kr.co.finger.shinhandamoa.domain.repository.DailyClientSettleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 이용기관 정산 서비스
 *
 * @author wisehouse@finger.co.kr
 */
@Service
public class ClientSettleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSettleService.class);

    @Autowired
    private DailyClientSettleRepository dailyClientSettleRepository;

    /**
     * 일 정산 목록 조회
     *
     * @param clientId
     * @param clientName
     * @param fromDate
     * @param toDate
     * @param pageBounds
     * @return
     */
    public ListResult<DailyClientSettleDTO> getDailySettleList(String clientId, String clientName, Date fromDate, Date toDate, String sort, PageBounds pageBounds) {
        LOGGER.info("일 정산 목록 조회[clientId:{}, clientName:{}, fromDate:{}, toDate:{}, sort: {}, pageNo:{}, pageSize:{}]",
                clientId, clientName, fromDate, toDate, sort, pageBounds.getPageNo(), pageBounds.getPageSize());
        final List<DailyClientSettleDO> doList = dailyClientSettleRepository.findWithRowBounds(clientId, clientName, fromDate, toDate, sort, pageBounds.toRowBounds());
        final long totalItemCount = dailyClientSettleRepository.count(clientId, clientName, fromDate, toDate);

        final List<DailyClientSettleDTO> itemList = new ArrayList<>(doList.size());
        for (DailyClientSettleDO each : doList) {
            itemList.add(DailyClientSettleDTO.newInstance(each));
        }

        return new ListResult<>(totalItemCount, itemList);
    }

    /**
     * 일 정산 엑셀 생성
     *
     * @param clientId
     * @param clientName
     * @param fromDate
     * @param toDate
     * @param sort
     * @return
     */
    public InputStream getDailySettleExcel(String clientId, String clientName, Date fromDate, Date toDate, String sort) {
        LOGGER.info("일 정산 엑셀 생성[clientId:{}, clientName:{}, fromDate:{}, toDate:{}, sort: {}]",
                clientId, clientName, fromDate, toDate, sort);
        final XlsxBuilder xlsxBuilder = new XlsxBuilder();
        xlsxBuilder.newSheet("일 마감");


        xlsxBuilder.addHeader(0, 1, 0, 0, "마감일");
        xlsxBuilder.addHeader(0, 1, 1, 1, "은행코드");
        xlsxBuilder.addHeader(0, 1, 2, 2, "기관코드");
        xlsxBuilder.addHeader(0, 1, 3, 3, "기관명");
        xlsxBuilder.addHeader(0, 0, 4, 6, "청구");
        xlsxBuilder.addHeader(0, 0, 7, 11, "수납");
        xlsxBuilder.addHeader(0, 0, 12, 15, "문자메시지");
        /*xlsxBuilder.addHeader(0, 0, 15, 16, "알림톡");
        xlsxBuilder.addHeader(0, 0, 17, 18, "고지서출력의뢰");*/

        xlsxBuilder.addHeader(1, 1, 4, 4, "금액");
        xlsxBuilder.addHeader(1, 1, 5, 5, "건수");
        xlsxBuilder.addHeader(1, 1, 6, 6, "수수료");
        xlsxBuilder.addHeader(1, 1, 7, 7, "금액");
        xlsxBuilder.addHeader(1, 1, 8, 8, "건수");
        xlsxBuilder.addHeader(1, 1, 9, 9, "수수료");
        xlsxBuilder.addHeader(1, 1, 10, 10, "은행수수료");
        xlsxBuilder.addHeader(1, 1, 11, 11, "핑거수수료");
        xlsxBuilder.addHeader(1, 1, 12, 12, "SMS건수");
        xlsxBuilder.addHeader(1, 1, 13, 13, "SMS금액");
        xlsxBuilder.addHeader(1, 1, 14, 14, "LMS건수");
        xlsxBuilder.addHeader(1, 1, 15, 15, "LMS금액");
        /*xlsxBuilder.addHeader(1, 1, 15, 15, "건수");
        xlsxBuilder.addHeader(1, 1, 16, 16, "금액");
        xlsxBuilder.addHeader(1, 1, 17, 17, "출력건수");
        xlsxBuilder.addHeader(1, 1, 18, 18, "금액");*/

        final PageBounds pageBounds = new PageBounds();
        for (int i = 1; ; i++) {
            pageBounds.setPageNo(i);

            final List<DailyClientSettleDO> doList = dailyClientSettleRepository.findWithRowBounds(clientId, clientName, fromDate, toDate, sort, pageBounds.toRowBounds());
            if (doList.isEmpty()) {
                break;
            }

            for (DailyClientSettleDO each : doList) {
                xlsxBuilder.newDataRow();

                int columnNo = 0;

                xlsxBuilder.addData(columnNo++, new SimpleDateFormat("yyyy-MM-dd").format(each.getSettleDate()));
                xlsxBuilder.addData(columnNo++, each.getFgcd());
                xlsxBuilder.addData(columnNo++, each.getClientId());
                xlsxBuilder.addData(columnNo++, each.getClientName());
                xlsxBuilder.addData(columnNo++, each.getNoticeAmount());
                xlsxBuilder.addData(columnNo++, each.getNoticeCount());
                xlsxBuilder.addData(columnNo++, each.getNoticeFee());
                xlsxBuilder.addData(columnNo++, each.getVaPaymentAmount());
                xlsxBuilder.addData(columnNo++, each.getVaPaymentCount());
                xlsxBuilder.addData(columnNo++, each.getVaPaymentFee());
                xlsxBuilder.addData(columnNo++, each.getVaBankFee());
                xlsxBuilder.addData(columnNo++, each.getVaPaymentFee() - each.getVaBankFee());
                xlsxBuilder.addData(columnNo++, each.getSmsCount());
                xlsxBuilder.addData(columnNo++, each.getSmsFee());
                xlsxBuilder.addData(columnNo++, each.getLmsCount());
                xlsxBuilder.addData(columnNo++, each.getLmsFee());
                /*xlsxBuilder.addData(columnNo++, each.getAtCount());
                xlsxBuilder.addData(columnNo++, each.getAtFee());
                xlsxBuilder.addData(columnNo++, each.getPrnCount());
                xlsxBuilder.addData(columnNo++, each.getPrnFee());*/
            }
        }

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            xlsxBuilder.writeTo(outputStream);
        } catch (IOException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());
            }

        }

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
