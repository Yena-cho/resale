package kr.co.finger.shinhandamoa.sys.controller;

import com.finger.shinhandamoa.common.ListResult;
import com.finger.shinhandamoa.common.PageBounds;
import com.finger.shinhandamoa.org.transformer.DownloadView;
import kr.co.finger.shinhandamoa.common.DateUtils;
import kr.co.finger.shinhandamoa.sys.service.ClientSettleService;
import kr.co.finger.shinhandamoa.sys.service.DailyClientSettleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 이용기관 정산 컨트롤러
 *
 * @author wisehouse@finger.co.kr
 */
@RestController
@RequestMapping(value = "/sys/rest/client/settle")
public class ClientSettleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSettleController.class);

    @Autowired
    private ClientSettleService clientSettleService;

    /**
     * 일 정산 목록조회
     */
    @RequestMapping(value = "/daily")
    public ListResult<DailyClientSettleDTO> getDailySettle(@RequestParam(value = "clientId", defaultValue = "") String clientId,
                                                           @RequestParam(value = "clientName", defaultValue = "") String clientName,
                                                           @RequestParam(value = "fromDate", defaultValue = "") String fromDateString,
                                                           @RequestParam(value = "toDate", defaultValue = "") String toDateString,
                                                           @RequestParam(value = "sort", defaultValue = "clientId_asc") String sort,
                                                           PageBounds pageBounds) {

        Date fromDate = null;
        Date toDate = null;

        if(!fromDateString.isEmpty()){
            fromDate = DateUtils.parseDate(fromDateString, "yyyyMMdd", null);
        }

        if(!toDateString.isEmpty()){
            toDate = DateUtils.parseDate(toDateString, "yyyyMMdd", null);
        }

        final ListResult<DailyClientSettleDTO> listResult = clientSettleService.getDailySettleList(clientId, clientName, fromDate, toDate, sort, pageBounds);

        return listResult;
    }

    /**
     * 일 정산 다운로드
     */
    @RequestMapping(value = "/daily/download")
    public DownloadView downloadDailySettle(@RequestParam(value = "clientId", defaultValue = "") String clientId,
                                            @RequestParam(value = "clientName", defaultValue = "") String clientName,
                                            @RequestParam(value = "fromDate", defaultValue = "") String fromDateString,
                                            @RequestParam(value = "toDate", defaultValue = "") String toDateString,
                                            @RequestParam(value = "sort", defaultValue = "clientId_asc") String sort) {

        Date fromDate = null;
        Date toDate = null;

        if(fromDate != null){
            fromDate = DateUtils.parseDate(fromDateString, "yyyyMMdd", null);
        }

        if(toDate != null){
            toDate = DateUtils.parseDate(toDateString, "yyyyMMdd", null);
        }

        final InputStream inputStream = clientSettleService.getDailySettleExcel(clientId, clientName, fromDate, toDate, sort);
        
        return new DownloadView("일 마감_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx", inputStream);
    }

}
