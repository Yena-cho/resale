package com.finger.shinhandamoa.sys.cashreceipt.rest;

import com.finger.shinhandamoa.common.ListResult;
import com.finger.shinhandamoa.common.PageBounds;
import com.finger.shinhandamoa.org.transformer.DownloadView;
import com.finger.shinhandamoa.sys.cashreceipt.service.CashReceiptHistoryDTO;
import com.finger.shinhandamoa.sys.cashreceipt.service.CashReceiptService;
import com.finger.shinhandamoa.sys.cashreceipt.service.ClientWithCashReceiptStatusDTO;
import com.finger.shinhandamoa.sys.cashreceipt.service.ReceiptWithCashReceiptStatusDTO;
import com.finger.shinhandamoa.sys.cashreceipt.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 현금영수증
 *
 * @author suhlee
 * @desc 부가서비스 > 현금영수증 이용내역 조회
 * @date 20180530
 */
@RestController("sys-rest-cash-receipt")
@RequestMapping("/sys/rest/cash-receipt")
public class SysCashReceiptController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysCashReceiptController.class);

    @Inject
    private CashReceiptService cashReceiptService;

    /**
     * 기관별 현금영수증 발행 현황
     *
     * @return
     */
    @RequestMapping(value = "/status-by-client", method = RequestMethod.GET)
    public ListResult<ClientWithCashReceiptStatusDTO> getStatusByClient(CashReceiptStatusByClientParam param, PageBounds pageBounds) {
        final ListResult<ClientWithCashReceiptStatusDTO> listResult = cashReceiptService.getStatusByClient(param.getStartDate(), param.getEndDate(), param.getClientId(), param.getClientName(), param.getStatus(), param.getEnableCashReceipt(), param.getEnableAutomaticCashReceipt(), param.getOrderBy(), pageBounds, param.getMandRcpYn());

        return listResult;
    }

    /**
     * 수납별 현금영수증 현황
     *
     * @return
     */
    @RequestMapping(value = "/status-by-receipt", method = RequestMethod.GET)
    public ListResult<ReceiptWithCashReceiptStatusDTO> getStatusByReceipt(CashReceiptStatusByReceiptParam param, PageBounds pageBounds) {
        final ListResult<ReceiptWithCashReceiptStatusDTO> listResult = cashReceiptService.getStatusByReceipt(param.getStartDate(), param.getEndDate(), param.getStartDate2(), param.getEndDate2(), param.getChaCd(), param.getChaName(), param.getSveCd(), param.getCreateCashReceipt(), param.getNotIssuedErrorCashReceipt(), param.getOrderBy(), pageBounds);

        return listResult;
    }

    /**
     * 발행 현황
     *
     * @return
     */
    @RequestMapping(value = "/status-by-issue", method = RequestMethod.GET)
    public ListResult<IssueWithCashReceiptStatusDTO> getStatusByIssueCashReceipt(CashReceiptStatusByIssueParam param, PageBounds pageBounds) {
        final ListResult<IssueWithCashReceiptStatusDTO> listResult = cashReceiptService.getStatusByIssueCashReceipt(param.getStartDate(), param.getEndDate(), param.getStartDate2(), param.getEndDate2(), param.getStartDate3(), param.getEndDate3(), param.getChaCd(), param.getChaName(), param.getSveCd(), param.getCreateErrorCashReceipt(), param.getDuplicationIssuedErrorCashReceipt(), param.getNotIssuedErrorCashReceipt(), param.getOrderBy(), pageBounds);

        return listResult;
    }

    /**
     * 발행 현황 조회 - 현금영수증번호 상세보기 팝업
     *
     * @return
     */
    @RequestMapping(value = "/datailPopUpCashmasCd", method = RequestMethod.GET)
    public ListResult<DetailPopUpCashmasCdDTO> datailPopUpCashmasCd(String cashmasCd) {
        final ListResult<DetailPopUpCashmasCdDTO> listResult = cashReceiptService.datailPopUpCashmasCd(cashmasCd);

        return listResult;
    }

    /**
     * 발행 현황 조회 - 기관명 상세보기 팝업
     *
     * @return
     */
    @RequestMapping(value = "/datailPopUpChaName", method = RequestMethod.GET)
    public ListResult<DetailPopUpChaNameDTO> datailPopUpChaName(String chaCd) {
        final ListResult<DetailPopUpChaNameDTO> listResult = cashReceiptService.datailPopUpChaName(chaCd);

        return listResult;
    }

    /**
     * 발행 현황 조회 - 거래일시 상세보기 팝업
     *
     * @return
     */
    @RequestMapping(value = "/datailPopUpRegDt", method = RequestMethod.GET)
    public ListResult<DetailPopUpRegDtDTO> datailPopUpRegDt(String rcpmasCd) {
        final ListResult<DetailPopUpRegDtDTO> listResult = cashReceiptService.datailPopUpRegDt(rcpmasCd);

        return listResult;
    }

    /**
     * 발행 현황 파일저장
     *
     * @return
     */
    @RequestMapping(value = "/status-by-issue/download", method = RequestMethod.GET)
    public DownloadView downloadStatusByIssueCashReceipt(CashReceiptStatusByIssueParam param) throws IOException {
        InputStream inputStream;
        try {
            inputStream = cashReceiptService.getStatusByIssueCashReceiptExcel(param.getStartDate(), param.getEndDate(), param.getStartDate2(), param.getEndDate2(), param.getStartDate3(), param.getEndDate3(), param.getChaCd(), param.getChaName(), param.getSveCd(), param.getCreateErrorCashReceipt(), param.getDuplicationIssuedErrorCashReceipt(), param.getNotIssuedErrorCashReceipt(), param.getOrderBy());
        } catch (IOException e) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());
            }
            inputStream = null;
        }
        return new DownloadView(String.format("발행 현황[%s].xlsx", new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())), inputStream);
    }

    /**
     * 수납별 현금영수증 현황 파일저장
     *
     * @return
     */
    @RequestMapping(value = "/status-by-receipt/download", method = RequestMethod.GET)
    public DownloadView downloadStatusByReceipt(CashReceiptStatusByReceiptParam param) throws IOException {
        InputStream inputStream;
        try {
            inputStream = cashReceiptService.getStatusByReceiptExcel(param.getStartDate(), param.getEndDate(), param.getStartDate2(), param.getEndDate2(), param.getChaCd(), param.getChaName(), param.getSveCd(), param.getCreateCashReceipt(), param.getNotIssuedErrorCashReceipt(), param.getOrderBy());
        } catch (IOException e) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());
            }
            inputStream = null;
        }
        return new DownloadView(String.format("수납별 현금영수증 발행 현황[%s].xlsx", new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())), inputStream);
    }

    @RequestMapping(value = "/status-by-client/download", method = RequestMethod.GET)
    public DownloadView downloadStatusByClient(CashReceiptStatusByClientParam param) throws IOException {
        InputStream inputStream;
        try {
            inputStream = cashReceiptService.getStatusByClientExcel(param.getStartDate(), param.getEndDate(), param.getClientId(), param.getClientName(), param.getStatus(), param.getEnableCashReceipt(), param.getEnableAutomaticCashReceipt(), param.getOrderBy(), param.getMandRcpYn());
        } catch (IOException e) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());
            }
            inputStream = null;
        }
        return new DownloadView(String.format("기관별 현금영수증 발행 현황[%s].xlsx", new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())), inputStream);
    }

    /**
     * 현금영수증 정보 생성
     *
     * @param chaCd
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/create-with-client-and-period", method = RequestMethod.POST)
    public ModelMap createWithClientAndPeriod(@RequestParam("clientId") String chaCd,
                                              @RequestParam("sveCd") String paymentType,
                                              @RequestParam("startDate") String startDate,
                                              @RequestParam("endDate") String endDate) {
        final long count = cashReceiptService.createWithClientAndPeriod(chaCd, paymentType, startDate, endDate);

        final ModelMap modelMap = new ModelMap();
        modelMap.put("count", count);

        return modelMap;
    }

    /**
     * 현금영수증 발행
     *
     * @param chaCd
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/request-with-client-and-period", method = RequestMethod.POST)
    public ModelMap requestWithClientAndPeriod(@RequestParam("clientId") String chaCd,
                                               @RequestParam("sveCd") String paymentType,
                                               @RequestParam("startDate") String startDate,
                                               @RequestParam("endDate") String endDate) {
        final long count = cashReceiptService.requestWithClientAndPeriod(chaCd, paymentType, startDate, endDate);

        final ModelMap modelMap = new ModelMap();
        modelMap.put("count", count);

        return modelMap;
    }

    /**
     * 현금영수증 발행 이용내역
     * @return
     */
    @RequestMapping(value = "/cash-receipt-history", method = RequestMethod.GET)
    public ModelMap cashReceiptHistory(CashReceiptHistoryParam param, PageBounds pageBounds) {
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("startDate", param.getStartDate());
        map.put("endDate", param.getEndDate());
        map.put("chaCd", param.getChaCd());
        map.put("chaName", param.getChaName());
        map.put("dateTy", param.getDateTy());
        map.put("resultTy", param.getResultTy());
        map.put("searchGb", param.getSearchGb());
        map.put("searchValue", param.getSearchValue());
        map.put("orderBy", param.getOrderBy());

        final HashMap<String, Object> returnMap = cashReceiptService.getCashReceiptHistory(map, pageBounds);

        final ModelMap modelMap = new ModelMap();
        modelMap.put("list", returnMap);

        return modelMap;
    }

    /**
     * 현금영수증 발행 이용내역
     * @return
     */
    @RequestMapping(value = "/cash-receipt-history/download", method = RequestMethod.GET)
    public View cashReceiptHistoryExcel(CashReceiptHistoryParam param, PageBounds pageBounds, Model model) {
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("startDate", param.getStartDate());
        map.put("endDate", param.getEndDate());
        map.put("chaCd", param.getChaCd());
        map.put("chaName", param.getChaName());
        map.put("dateTy", param.getDateTy());
        map.put("resultTy", param.getResultTy());
        map.put("searchGb", param.getSearchGb());
        map.put("searchValue", param.getSearchValue());
        map.put("orderBy", param.getOrderBy());

        pageBounds.setPageSize(Integer.MAX_VALUE);
        try {
        final HashMap<String, Object> returnMap = cashReceiptService.getCashReceiptHistory(map, pageBounds);

            model.addAttribute("list", returnMap.get("itemList"));

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }

        return new ExcelCashHst();
    }
}
