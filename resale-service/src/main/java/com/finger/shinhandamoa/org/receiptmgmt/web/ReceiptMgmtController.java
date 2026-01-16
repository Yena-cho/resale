package com.finger.shinhandamoa.org.receiptmgmt.web;

import com.finger.shinhandamoa.bank.dto.BankReg01DTO;
import com.finger.shinhandamoa.bank.service.BankMgmtService;
import com.finger.shinhandamoa.common.CmmnUtils;
import com.finger.shinhandamoa.common.PdfViewer;
import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.data.table.mapper.CashReceiptMasterMapper;
import com.finger.shinhandamoa.org.custMgmt.dto.CustReg01DTO;
import com.finger.shinhandamoa.org.custMgmt.service.CustMgmtService;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtBaseDTO;
import com.finger.shinhandamoa.org.notimgmt.service.NotiMgmtService;
import com.finger.shinhandamoa.org.receiptmgmt.dto.*;
import com.finger.shinhandamoa.org.receiptmgmt.service.PayMgmtService;
import com.finger.shinhandamoa.org.receiptmgmt.service.ReceiptMgmtService;
import com.finger.shinhandamoa.org.transformer.DownloadView;
import com.finger.shinhandamoa.util.dto.CodeDTO;
import com.finger.shinhandamoa.util.service.CodeService;
import com.finger.shinhandamoa.vo.PageVO;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import kr.co.nicepay.module.lite.NicePayWebConnector;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateParser;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 수납관리 controller
 *
 * @author suhlee
 * @author wisehouse@finger.co.kr
 * @author mljeong@finger.co.kr
 * @author jhjeong@finger.co.kr 직접수납관리 DirectReceiptMgmtController 로 분리
 */
@Controller
@Slf4j
@RequestMapping({"org/receiptMgnt/**", "org/receiptMgmt/**"})
public class ReceiptMgmtController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptMgmtController.class);

    @Inject
    private NotiMgmtService notiMgmtService;

    @Inject
    private ReceiptMgmtService receiptMgmtService;

    @Inject
    private PayMgmtService payMgmtService;

    @Inject
    private CustMgmtService custMgmtService;

    // 공통코드 Service
    @Inject
    private CodeService codeService;

    @Autowired
    private CashReceiptMasterMapper cashReceiptMasterMapper;

    @Inject
    private BankMgmtService bankMgmtService;

    // pdftemp 디렉토리
    @Value("${file.path.pdfTemp}")
    private String pdfTempPath;

    // pdf 디렉토리
    @Value("${file.path.pdf}")
    private String pdfPath;

    final SimpleDateFormat YMD_Times = new SimpleDateFormat("yyyyMMdd_HHmmss");

    @RequestMapping("receiptList")
    public ModelAndView getReceiptList(@RequestParam(defaultValue = "1") int curPage,
                                       @RequestParam(defaultValue = "10") int PAGE_SCALE,
                                       @RequestParam(defaultValue = "1") int start,
                                       @RequestParam(defaultValue = "10") int end,
                                       @RequestParam(defaultValue = "RG") String sortGb,
                                       @RequestParam(defaultValue = "") String gubn
    ) throws Exception {
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 수납관리 > 조회", user);
        try {
            NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(user);

            int count = 0;
            long totAmt = 0;
            long realTotAmt = 0;
            long totUnAmt = 0;
            long rePayAmt = 0;

            if (gubn.equals("main")) {
                map.put("gubn", "main");
                map.put("masMonth", "");
                map.put("startDate", StrUtil.getCalMonthDateStr(-3));
                map.put("endDate", StrUtil.getCurrentDateStr());
                map.put("dateGb", "D");
                map.put("payNoGb", null);
            } else {
                map.put("gubn", "");
                map.put("startDate", "");
                map.put("endDate", "");
                map.put("masMonth", StrUtil.getCurrentMonthStr());
                map.put("dateGb", "M");
                map.put("payNoGb", "CHK");
            }
            map.put("sortGb", sortGb);
            map.put("payList", null);
            map.put("svecdList", null);
            map.put("cusGubnValue", "");
            map.put("cusGubn", "");
            map.put("searchValue", "");
            map.put("payItemCd", "");
            map.put("chaCd", baseInfo.getChacd());
            map.put("searchGb", "");
            map.put("payGb1", "ALL");

            LOGGER.debug("getReceiptList >> " + map);
            HashMap<String, Object> totValue = receiptMgmtService.getReceiptCount(map);
            if (totValue != null) {
                count = Integer.parseInt(totValue.get("cnt").toString());
                totAmt = NumberUtils.toLong(totValue.get("totamt").toString());
                realTotAmt = NumberUtils.toLong(totValue.get("realtotamt").toString());
                totUnAmt = NumberUtils.toLong(totValue.get("totunamt").toString());
                rePayAmt = NumberUtils.toLong(totValue.get("repayamt").toString());
            }

            // 청구항목코드
            List<CodeDTO> claimItemCd = codeService.claimItemCd(baseInfo.getChacd());
            map.put("claimItemCd", claimItemCd);

            PageVO page = new PageVO(count, curPage, PAGE_SCALE);
            map.put("start", start);
            map.put("end", end);
            map.put("pageGubn", "D");

            List<ReceiptMgmtDTO> list = receiptMgmtService.getReceiptList(map);
            map.put("list", list);
            map.put("count", count);
            map.put("totAmt", totAmt);
            map.put("realTotAmt", realTotAmt);
            map.put("totUnAmt", totUnAmt);
            map.put("rePayAmt", rePayAmt);
            map.put("pager", page);
            map.put("PAGE_SCALE", PAGE_SCALE);
            mav.addObject("orgSess", baseInfo);
            mav.addObject("map", map);

            LOGGER.debug("map >>> " + map);
            mav.setViewName("org/receiptMgnt/receiptList");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
        return mav;
    }

    @RequestMapping("receiptListAjax")
    @ResponseBody
    public Map<String, Object> getReceiptListAjax(@RequestBody ReceiptMgmtDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        Map<String, Object> map = new HashMap<String, Object>();
        LOGGER.debug("기관 {} 수납관리 > 조회", user);
        try {
            map.put("chaCd", body.getChaCd());
            map.put("masMonth", body.getMasMonth());
            map.put("curPage", body.getCurPage());
            map.put("PAGE_SCALE", body.getPageScale());

            map.put("startDate", StringEscapeUtils.escapeHtml4(body.getStartDate()));
            map.put("endDate", StringEscapeUtils.escapeHtml4(body.getEndDate()));
            map.put("sortGb", body.getSortGb());

            if (!"ALL".equals(body.getPayGb1()) && !body.getPayList().isEmpty()) {
                map.put("payList", body.getPayList());
            }
            map.put("payNoGb", body.getPayNoGb());
            if (!"ALL".equals(body.getSvecdGb1()) && !body.getSvecdList().isEmpty()) {
                map.put("svecdList", body.getSvecdList());
            }

            map.put("cusGubn", body.getCusGubn());
            if (body.getCusGubnValue() != null && !"".equals(body.getCusGubnValue())) {
                String value = body.getCusGubnValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("cusGubnValue", valueList);
            }

            map.put("searchGb", StringEscapeUtils.escapeHtml4(body.getSearchGb()));
            if (body.getSearchValue() != null && !"".equals(body.getSearchValue())) {
                String value = body.getSearchValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("searchValue", valueList);
            }

            map.put("payItemCd", body.getPayItemCd());
            map.put("dateGb", body.getDateGb());
            map.put("payGb1", body.getPayGb1());

            int count = 0;
            long totAmt = 0;
            long totUnAmt = 0;
            long realTotAmt = 0;
            long rePayAmt = 0;

            HashMap<String, Object> totValue = receiptMgmtService.getReceiptCount(map);
            if (totValue != null) {
                count = NumberUtils.toInt(String.valueOf(totValue.get("cnt")));
                totAmt = NumberUtils.toLong(String.valueOf(totValue.get("totamt")));
                realTotAmt = NumberUtils.toLong(String.valueOf(totValue.get("realtotamt")));
                totUnAmt = NumberUtils.toLong(String.valueOf(totValue.get("totunamt")));
                rePayAmt = NumberUtils.toLong(String.valueOf(totValue.get("repayamt")));
            }

            LOGGER.debug("count " + count);
            LOGGER.debug("totAmt " + totAmt);
            LOGGER.debug("totUnAmt " + totUnAmt);

            PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();

            LOGGER.debug("start >" + start);
            LOGGER.debug("end >" + end);

            map.put("start", start);
            map.put("end", end);
            map.put("pageGubn", "D");

            List<ReceiptMgmtDTO> list = receiptMgmtService.getReceiptList(map);
            String vano = "";
            for (ReceiptMgmtDTO receiptMgmtDTO : list) {
                if (receiptMgmtDTO.getVano() != null) {
                    vano = receiptMgmtDTO.getVano().substring(0, 3) + "-" + receiptMgmtDTO.getVano().substring(3, 6) + "-" + receiptMgmtDTO.getVano().substring(6);
                    receiptMgmtDTO.setVano(vano);
                }
            }
            map.put("list", list);
            map.put("count", count);
            map.put("totAmt", totAmt);
            map.put("realTotAmt", realTotAmt);
            map.put("rePayAmt", rePayAmt);
            map.put("totUnAmt", totUnAmt);
            map.put("pager", page);
            map.put("PAGE_SCALE", body.getPageScale());
            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }


        return map;
    }

    /**
     * 신용카드 취소(환불 처리)
     *
     * @param body
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("insertRefundAjax")
    public HashMap<String, Object> insertRefundAjax(@RequestBody ReceiptMgmtDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        LOGGER.debug("기관 {} 신용카드 취소(환불 처리)", user);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("maker", body.getChaCd());
        map.put("rcpMasCd", body.getRcpMasCd());
        map.put("rePayAmt", body.getRePayAmt());
        map.put("masMonth", body.getMasMonth());
        map.put("curPage", body.getCurPage());
        map.put("PAGE_SCALE", body.getPageScale());
        map.put("startDate", body.getStartDate());
        map.put("endDate", body.getEndDate());
        map.put("sortGb", body.getSortGb());
        map.put("cusGubn", body.getCusGubn());
        if (body.getCusGubn().equals("all")) {
            map.put("cusGubnValue", body.getCusGubnValue());
        } else {
            map.put("cusGubnValue", body.getCusGubnValue().replaceAll(",", "|").trim());
        }
        map.put("searchValue", body.getSearchValue());
        map.put("payItemCd", body.getPayItemCd());
        map.put("chaCd", body.getChaCd());
        map.put("searchGb", body.getSearchGb());
        map.put("dateGb", body.getDateGb());

        if (!"ALL".equals(body.getPayGb1()) && !body.getPayList().isEmpty()) {
            map.put("payList", body.getPayList());
        }
        if (!"ALL".equals(body.getSvecdGb1()) && !body.getSvecdList().isEmpty()) {
            map.put("svecdList", body.getSvecdList());
        }

        try {
            if (receiptMgmtService.insertRepay(map) > 0) {

                int count = 0;
                long totAmt = 0;
                long totUnAmt = 0;

                HashMap<String, Object> totValue = receiptMgmtService.getReceiptCount(map);

                if (totValue != null) {
                    count = NumberUtils.toInt(totValue.get("cnt").toString(), 0);
                    totAmt = NumberUtils.toLong(totValue.get("totamt").toString(), 0L);
                    totUnAmt = NumberUtils.toLong(totValue.get("totunamt").toString(), 0L);
                }

                PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
                int start = page.getPageBegin();
                int end = page.getPageEnd();

                map.put("start", start);
                map.put("end", end);
                map.put("pageGubn", "D");

                List<ReceiptMgmtDTO> list = receiptMgmtService.getReceiptList(map);

                map.put("list", list);
                map.put("count", count);
                map.put("totAmt", totAmt);
                map.put("totUnAmt", totUnAmt);
                map.put("pager", page);
                map.put("PAGE_SCALE", body.getPageScale());
                map.put("retCode", "0000");
                map.put("retMsg", "정상");
                map.put("retCode", "0000");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /**
     * 수납내역 엑셀다운로드
     *
     * @param request
     * @param response
     * @param body
     * @param model
     * @return
     * @throws Exception
     */
    @Deprecated
    @RequestMapping("excelDown")
    public View receiptExcelDownload(HttpServletRequest request, HttpServletResponse response, ReceiptMgmtDTO body, Model model) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} 수납내역 엑셀다운로드", chaCd);
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("chaCd", chaCd);
            map.put("masMonth", body.getMasMonth());
            map.put("curPage", body.getCurPage());
            map.put("PAGE_SCALE", body.getPageScale());
            map.put("dateGb", body.getfDateGb());
            map.put("startDate", body.getStartDate());
            map.put("endDate", body.getEndDate());
            map.put("sortGb", body.getfSortGb());

            map.put("cusGubn", body.getfCusGubn());
            if (!"".equals(body.getfCusGubnValue())) {
                String value = body.getfCusGubnValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("cusGubnValue", valueList);
            }
            map.put("searchGb", body.getfSearchGb());
            if (!"".equals(body.getfSearchValue())) {
                String value = body.getfSearchValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("searchValue", valueList);
            }

            map.put("payItemCd", body.getPayItemCd());
            map.put("pageGubn", "E");
            map.put("payGb1", body.getPayGb1());
            if (body.getPayGb1().equals("ALL")) {
                map.put("payList", null);
            } else {
                map.put("payList", body.getPayList());
            }
            map.put("payNoGb", body.getPayNoGb());

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            model.addAttribute("cusGbList", cusGbList);
            model.addAttribute("cusGbCnt", cusGbList.size());

            List<ReceiptMgmtDTO> list = receiptMgmtService.getReceiptList(map);

            model.addAttribute("list", list);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
        return new ExcelSaveReceiptReg();
    }
    
    /**
     * 수납내역 엑셀다운로드 (신규)
     * @param params
     * @return
     * @throws Exception
     * @author jhjeong@finger.co.kr
     * @modified 2018. 10. 29.
     */
    @RequestMapping(value="rcpHistoryExcelDown", method=RequestMethod.GET)
    public DownloadView rcpHistoryExcelDown(@RequestParam Map<String, Object> params) throws Exception {

    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 수납내역 엑셀다운로드 (신규)", user);
        params.put("chaCd", user);

        if (params.get("fCusGubnValue") != null && !"".equals(params.get("fCusGubnValue"))) {
            String value = params.get("fCusGubnValue").toString().trim();
            String[] valueList = value.split("\\s*,\\s*");
            params.put("cusGubnValue", valueList);
        }
        if (params.get("fSearchValue") != null && !"".equals(params.get("fSearchValue"))) {
            String value = params.get("fSearchValue").toString().trim();
            String[] valueList = value.split("\\s*,\\s*");
            params.put("searchValue", valueList);
        }

        params.put("dateGb", params.get("fDateGb"));
        params.put("sortGb", params.get("fSortGb"));
        params.put("cusGubn", params.get("fCusGubn"));
        params.put("searchGb", params.get("fSearchGb"));

        params.put("pageGubn", "E");

        if (params.get("payGb1").equals("ALL")) {
        	params.put("payList", null);
        } else {
            params.put("payList", org.springframework.util.StringUtils.commaDelimitedListToStringArray(params.get("payList").toString()));
        }

        if (params.get("svecdList") != null) {
            String[] svecdLists = org.springframework.util.StringUtils.commaDelimitedListToStringArray(params.get("svecdList").toString());
            if(svecdLists.length != 0 && svecdLists.length != 5) {
                params.put("svecdList", svecdLists);
            } else {
                params.remove("svecdList");
            }
        }

        // 고객구분항목
        List<CodeDTO> cusGbList = codeService.cusGubnCd(user);
        params.put("cusGbList", cusGbList);
        params.put("cusGbCnt", cusGbList.size());


        String prefixName = "수납내역[%s]_%s.xlsx";
        InputStream inputStream;
        try {
        	inputStream = receiptMgmtService.getReceiptHistoryExcel(params);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            inputStream = null;
        }
        
        return new DownloadView(String.format(prefixName, user, YMD_Times.format(new Date())), inputStream);
    }

    /**
     * 가상계좌 입금내역조회
     *
     * @param curPage
     * @param PAGE_SCALE
     * @param start
     * @param end
     * @param orderBy    정렬 방법. rcpDate_desc - 입금일 내림차순, rcpName_asc - 입금자명 오름차순
     * @return
     * @throws Exception
     */
    @RequestMapping("paymentList")
    public ModelAndView getPaymentList(@RequestParam(defaultValue = "1") int curPage,
                                       @RequestParam(defaultValue = "10") int PAGE_SCALE,
                                       @RequestParam(defaultValue = "1") int start,
                                       @RequestParam(defaultValue = "10") int end,
                                       @RequestParam(defaultValue = "RG") String orderBy) throws Exception {
        final ModelAndView mav = new ModelAndView();
        final HashMap<String, Object> map = new HashMap<String, Object>();
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        LOGGER.debug("기관 {} 가상계좌 입금내역조회", user);
        int count = 0;
        long totAmt = 0;
        int cardCount = 0;
        long cardTotAmt = 0;
        StrUtil strUtil = new StrUtil();

        try {
            NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(user);
            mav.addObject("orgSess", baseInfo);

            // 청구항목코드
            List<CodeDTO> claimItemCd = codeService.claimItemCd(user);
            map.put("claimItemCd", claimItemCd);
            map.put("chaCd", user);
            map.put("start", start);
            map.put("end", end);
            map.put("startDate", StrUtil.getCalMonthDateStr(-1));
            map.put("endDate", StrUtil.getCurrentDateStr());
            map.put("masMonth", StrUtil.getCurrentMonthStr());
            map.put("orderBy", orderBy);

            HashMap<String, Object> totValue = payMgmtService.getPaymentCount(map);

            if (totValue != null) {
                count = NumberUtils.toInt(totValue.get("cnt").toString(), 0);
                totAmt = NumberUtils.toLong(totValue.get("totamt").toString(), 0L);
            }

            PageVO page = new PageVO(count, curPage, PAGE_SCALE);
            map.put("start", start);
            map.put("end", end);
            map.put("orderBy", "pay");
            map.put("pageGubn", "D");

            List<PayMgmtDTO> list = payMgmtService.getPaymentList(map);

            //온라인카드 내역 건수
            HashMap<String, Object> cardTotValue = payMgmtService.getCardPayCount(map);
            if (totValue != null) {
                cardCount = NumberUtils.toInt(cardTotValue.get("cnt").toString(), 0);
                cardTotAmt = NumberUtils.toLong(cardTotValue.get("totamt").toString(), 0L);
            }

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(user);
            map.put("cusGbList", cusGbList);
            map.put("cusGbCnt", cusGbList.size());

            PageVO page2 = new PageVO(cardCount, curPage, PAGE_SCALE);

            //온라인카드 내역 리스트
            List<PayMgmtDTO> cardPayList = payMgmtService.getCardPayList(map);

            map.put("cardtDate", StrUtil.dateFormat(StrUtil.getCurrentDateStr()));
            map.put("cardfDate", StrUtil.dateFormat(StrUtil.getCalMonthDateStr(-1)));
            map.put("list", list);
            map.put("count", count);
            map.put("totAmt", totAmt);
            map.put("pager", page);
            map.put("pager2", page2);
            map.put("PAGE_SCALE", PAGE_SCALE);
            map.put("cusGbList", cusGbList);
            map.put("cardPayList", cardPayList);
            map.put("cardCount", cardCount);
            map.put("cardTotAmt", cardTotAmt);
            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        mav.addObject("map", map);
        mav.setViewName("org/receiptMgnt/paymentList");
        return mav;
    }

    /**
     * 가상계좌 입금내역 ajax
     *
     * @param body
     * @return
     * @throws Exception
     */
    @RequestMapping("paymentListAjax")
    @ResponseBody
    public Map<String, Object> getPaymentListAjax(@RequestBody PayMgmtDTO body) throws Exception {
        final Map<String, Object> map = new HashMap<>();
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        LOGGER.debug("기관 {} 가상계좌 입금내역 ajax", user);
        String count = "";
        String totAmt = "";
        try {
            map.put("chaCd", user);
            if(CmmnUtils.validateDateFormat2(body.getEndDate()) && CmmnUtils.validateDateFormat2(body.getStartDate())){
                map.put("startDate", body.getStartDate());
                map.put("endDate", body.getEndDate());
            } else {
                map.put("startDate", "");
                map.put("endDate", "");
            }
            map.put("cusGubn", StringEscapeUtils.escapeHtml4(body.getCusGubn()));

            if (body.getCusGubnValue() != null && !"".equals(body.getCusGubnValue())) {
                String value = body.getCusGubnValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("cusGubnValue", valueList);
            }

            if (body.getSearchValue() != null && !"".equals(body.getSearchValue())) {
                String value = body.getSearchValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("searchValue", valueList);
            }

            map.put("searchGb", body.getSearchGb());
            map.put("payItemCd", body.getPayItemCd());
            map.put("masMonth", body.getMasMonth());
            map.put("orderBy", body.getOrderBy());

            // FIXME
            HashMap<String, Object> totValue = payMgmtService.getPaymentCount(map);
            if (totValue != null) {
                count = totValue.get("cnt").toString();
                totAmt = totValue.get("totamt").toString();
            }

            PageVO page = new PageVO(Integer.parseInt(count), body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();

            map.put("start", start);
            map.put("end", end);
            map.put("orderBy", body.getOrderBy());
            map.put("pageGubn", "D");

            List<PayMgmtDTO> list = payMgmtService.getPaymentList(map);
            for (PayMgmtDTO payMgmtDTO : list) {
                if (payMgmtDTO.getVaNo() != null) {
                    String vano = payMgmtDTO.getVaNo().substring(0, 3) + "-" + payMgmtDTO.getVaNo().substring(3, 6) + "-" + payMgmtDTO.getVaNo().substring(6);
                    payMgmtDTO.setVaNo(vano);
                }
            }
            map.put("list", list);
            map.put("count", count);
            map.put("totAmt", totAmt);
            map.put("pager", page);
            map.put("PAGE_SCALE", body.getPageScale());
            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 온라인카드 입금내역 ajax
     *
     * @param body
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("cardPayListAjax")
    @ResponseBody
    public HashMap<String, Object> cardPayListAjax(@RequestBody PayMgmtDTO body,
                                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        int cardCount = 0;
        long cardTotAmt = 0;

        String user = authentication.getName();
        LOGGER.debug("기관 {} 온라인카드 입금내역 ajax", user);
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            map.put("chaCd", user);
            if(CmmnUtils.validateDateFormat2(body.getEndDate()) && CmmnUtils.validateDateFormat2(body.getStartDate())){
                map.put("startDate", body.getStartDate());
                map.put("endDate", body.getEndDate());
            } else {
                map.put("startDate", "");
                map.put("endDate", "");
            }
            map.put("statusCheck", body.getStatusCheck());
            map.put("masMonth", body.getMasMonth());
            map.put("search_orderBy", body.getOrderBy());
            map.put("seachCusGubn", StringUtils.upperCase(StringEscapeUtils.escapeHtml4(body.getSeachCusGubn()))); // 고객구분 selectbox

            if (body.getCusGubnValue() != null && !"".equals(body.getCusGubnValue())) {
                String value = body.getCusGubnValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("cusGubnValue", valueList);
            }

            if (body.getSearchValue() != null && !"".equals(body.getSearchValue())) {
                String value = body.getSearchValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("searchValue", valueList);
            }

            //온라인카드 내역 건수
            HashMap<String, Object> cardTotValue = payMgmtService.getCardPayCount(map);

            if (cardTotValue != null) {
                cardCount = NumberUtils.toInt(cardTotValue.get("cnt").toString(), 0);
                cardTotAmt = NumberUtils.toLong(cardTotValue.get("totamt").toString(), 0L);
            }

            PageVO page2 = new PageVO(cardCount, body.getCurPage(), body.getPageScale());
            int start = page2.getPageBegin();
            int end = page2.getPageEnd();
            map.put("start", start);
            map.put("end", end);

            List<PayMgmtDTO> cardPayList = payMgmtService.getCardPayList(map);
            map.put("cardPayList", cardPayList);
            map.put("cardCount", cardCount);
            map.put("cardTotAmt", cardTotAmt);
            map.put("pager2", page2);
            map.put("cardPageScale", body.getPageScale());

            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 가상계좌 입금내역 엑셀다운로드
     *
     * @param body
     * @param model
     * @return
     * @throws Exception
     */
    @Deprecated
    @RequestMapping("payExcelDown")
    public View payExcelDown(PayMgmtDTO body, Model model) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String chaCd = authentication.getName();
        LOGGER.debug("기관 {} 가상계좌 입금내역 엑셀다운로드", chaCd);
        try {
            final Map<String, Object> map = new HashMap<>();
            map.put("chaCd", chaCd);
            map.put("masMonth", body.getfMasMonth());
            map.put("startDate", body.getfStartDate());
            map.put("endDate", body.getfEndDate());
            map.put("cusGubn", body.getfCusGubn());

            if (!"".equals(body.getfCusGubnValue())) {
                String value = body.getfCusGubnValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("cusGubnValue", valueList);
            }

            if (!"".equals(body.getfSearchValue())) {
                String value = body.getfSearchValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("searchValue", valueList);
            }

            map.put("searchGb", body.getfSearchGb());
            map.put("pageGubn", "E");
            map.put("orderBy", body.getfOrderBy());

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            model.addAttribute("cusGbList", cusGbList);
            model.addAttribute("cusGbCnt", cusGbList.size());

            List<PayMgmtDTO> list = payMgmtService.getPaymentList(map);
            model.addAttribute("list", list);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }

        return new ExcelSavePaymentReg();
    }
    
    @RequestMapping(value="vasHistoryExcelDown", method=RequestMethod.GET)
    public DownloadView vasHistoryExcelDown(@RequestParam Map<String, Object> params) throws Exception {
    	final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String chaCd = authentication.getName();
        LOGGER.debug("기관 {} 가상계좌내역 엑셀다운로드", chaCd);
    	params.put("chaCd", chaCd);
    	
    	params.put("masMonth",  params.get("fMasMonth"));
        params.put("startDate", params.get("fStartDate"));
        params.put("endDate",   params.get("fEndDate"));
        params.put("cusGubn",   params.get("fCusGubn"));
        params.put("searchGb",  params.get("fSearchGb"));
        params.put("orderBy",   params.get("fOrderBy"));
    	
        if (params.get("fCusGubnValue") != null && !"".equals(params.get("fCusGubnValue"))) {
            String value = params.get("fCusGubnValue").toString().trim();
            String[] valueList = value.split("\\s*,\\s*");
            params.put("cusGubnValue", valueList);
        }
        if (params.get("fSearchValue") != null && !"".equals(params.get("fSearchValue"))) {
            String value = params.get("fSearchValue").toString().trim();
            String[] valueList = value.split("\\s*,\\s*");
            params.put("searchValue", valueList);
        }

        params.put("pageGubn", "E");
        
        // 고객구분항목
        List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
        params.put("cusGbList", cusGbList);
        params.put("cusGbCnt", cusGbList.size());
    	
    	
    	String prefixName = "가상계좌입금내역[%s]_%s.xlsx";
        InputStream inputStream;
        try {
        	inputStream = payMgmtService.getPaymentHistoryExcel(params);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            inputStream = null;
        }
        
        return new DownloadView(String.format(prefixName, chaCd, YMD_Times.format(new Date())), inputStream);
    }

    /**
     * 온라인카드 입금내역 엑셀다운로드
     *
     * @param request
     * @param response
     * @param body
     * @param model
     * @return
     * @throws Exception
     */
    @Deprecated
    @RequestMapping("cardExcelDown")
    public View cardExcelDown(HttpServletRequest request, HttpServletResponse response, HashMap<String, Object> body, Model model) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} 온라인카드 입금내역 엑셀다운로드", chaCd);
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("chaCd", chaCd);
            if (request.getParameter("fCardMonth") != null && request.getParameter("fCardMonth") != "") {
                map.put("masMonth", request.getParameter("fCardMonth"));
                map.put("startDate", "");
                map.put("endDate", "");
            } else {
                map.put("masMonth", "");
                map.put("startDate", request.getParameter("fCardfDate"));
                map.put("endDate", request.getParameter("fCardtDate"));
            }

            if (!"".equals(request.getParameter("fCardSearchValue"))) {
                String value = request.getParameter("fCardSearchValue").trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("searchValue", valueList);
            }

            if (!"".equals(request.getParameter("fCardCusGubnValue"))) {
                String value = request.getParameter("fCardCusGubnValue").trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("cusGubnValue", valueList);
            }

            map.put("statusCheck", request.getParameter("fStatusCheck"));
            map.put("search_orderBy", request.getParameter("fCardOrderBy"));
            map.put("pageGubn", "Excel");

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            model.addAttribute("cusGbList", cusGbList);
            model.addAttribute("cusGbCnt", cusGbList.size());

            List<PayMgmtDTO> cardPayList = payMgmtService.getCardPayList(map);
            model.addAttribute("list", cardPayList);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }

        return new ExcelSaveCardPay();
    }
    
    /**
     * 온라인카드 결제내역 엑셀다운로드-신규
     * @param params
     * @return
     * @throws Exception
     * @author jhjeong@finger.co.kr
     * @modified 2018. 10. 29.
     */
    @RequestMapping(value="cardHistoryExcelDown", method=RequestMethod.GET)
    public DownloadView cardHistoryExcelDown(@RequestParam Map<String, Object> params) throws Exception {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} 온라인카드 결제내역 엑셀다운로드-신규", chaCd);
    	params.put("chaCd", chaCd);
        if (params.get("fCardMonth") != null && StringUtils.isNotBlank((String) params.get("fCardMonth"))) {
        	params.put("masMonth", params.get("fCardMonth"));
        	params.put("startDate", "");
        	params.put("endDate", "");
        } else {
        	params.put("masMonth", "");
        	params.put("startDate", params.get("fCardfDate"));
        	params.put("endDate", params.get("fCardtDate"));
        }

        if (params.get("fCardSearchValue") != null && params.get("fCardSearchValue").toString() != "") {
            String value = params.get("fCardSearchValue").toString().trim();
            String[] valueList = value.split("\\s*,\\s*");
            params.put("searchValue", valueList);
        }

        if (params.get("fCardCusGubnValue") != null && params.get("fCardCusGubnValue").toString() != "") {
            String value = params.get("fCardCusGubnValue").toString().trim();
            String[] valueList = value.split("\\s*,\\s*");
            params.put("cusGubnValue", valueList);
        }

        params.put("statusCheck", params.get("fStatusCheck"));
        params.put("search_orderBy", params.get("fCardOrderBy"));
        params.put("pageGubn", "Excel");
        
        // 구분항목
        List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
        params.put("cusGbList", cusGbList);
        params.put("cusGbCnt", cusGbList.size());
    	
    	String prefixName = "온라인카드결제내역[%s]_%s.xlsx";
        InputStream inputStream;
        try {
        	inputStream = payMgmtService.getCardPayHistoryExcel(params);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            inputStream = null;
        }
        
        return new DownloadView(String.format(prefixName, chaCd, YMD_Times.format(new Date())), inputStream);
    }

    /**
     * 입금내역 영수증(CUER), 교육비납입증명서(cert1), 장기요양급여 납부확인서(cert2), 기부금(cert3)  인쇄
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "pdfMakeRcpBill*", method = RequestMethod.POST)
    public ModelAndView getPdfMakeRcpBill(@RequestParam(value = "billSect", defaultValue = "") String billSect,
                                          @RequestParam(value = "checkListValue", defaultValue = "") ArrayList<String> checkList,
                                          @RequestParam(value = "sFileName", defaultValue = "") String sFileName,
                                          @RequestParam(value = "sBrowserType") String sBrowserType,
                                          HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 입금내역 영수증(CUER), 교육비납입증명서(cert1), 장기요양급여 납부확인서(cert2), 기부금(cert3)  인쇄", user);
        String result = "";
        String webResult = "";

        try {
            map.put("billSect", billSect);
            map.put("chaCd", user);
            if (!checkList.isEmpty()) {
                map.put("notiMasList", checkList);
            }
            map.put("fileName", sFileName);

            LOGGER.debug("map >> " + map);
            String refCd = receiptMgmtService.getXchaoption(user);
            LOGGER.debug("refCd >> " + refCd);
            List<PdfReceiptMgmtDTO> list = null;

            if ("cut".equals(billSect)) {
                list = receiptMgmtService.selectRcpBillCut(map);
            } else if ("cert1".equals(billSect)) { //교육비 납입증명서
                list = receiptMgmtService.getEduList(map);
            } else if ("cert2".equals(billSect)) { // 장기요양급여 납부확인서
                list = receiptMgmtService.getRecpAccNoList(map);
                for (PdfReceiptMgmtDTO pdfReceiptMgmtDTO : list) {
                    pdfReceiptMgmtDTO.setCheckListValue(checkList);
                }
            } else if ("cert3".equals(billSect)) { // 기부금 영수증
                list = receiptMgmtService.getDntnList(map);
                for (PdfReceiptMgmtDTO pdfReceiptMgmtDTO : list) {
                    pdfReceiptMgmtDTO.setCheckListValue(checkList);
                }
            }

            String path = pdfTempPath;

            if (list.size() <= 0) {
                map.put("retCode", "9999");
                map.put("retMsg", "PDF 다운로드할 항목이 없습니다.");
            } else {
                File desti = new File(path);
                if (!desti.exists()) {
                    desti.mkdirs();
                }

                String strInFile = path + sFileName + ".pdf";
                File file = new File(strInFile);

                if (file.exists()) {
                    file.delete();
                }

                result = rctCreatePdf(request, response, list, strInFile, billSect);
                PdfViewer view = new PdfViewer();

                if ("0000".equals(result)) {
                    if (sBrowserType.equals("ie")) {
                        webResult = view.pdfIEDown(request, response, strInFile, sFileName);
                    } else {
                        webResult = view.webPdfViewer(request, response, strInFile);
                    }
                    if (file.exists()) {
                        file.delete();
                    }
                } else {
                    map.put("retCode", "9999");
                    map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
                }
                map.put("retCode", result);
                map.put("fileName", sFileName);
                map.put("retCode", "0000");
                map.put("retMsg", "정상");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        if (sBrowserType.equals("ie")) {
            return null;
        } else {
            mav.addObject("map", map);
            mav.setViewName("org/notiMgmt/pdfMakeBill");

            return mav;
        }
    }

    /**
     * html -> pdf 생성
     *
     * @param request
     * @param response
     * @param list
     * @param strInFile
     * @return
     * @throws Exception
     */
    public String rctCreatePdf(HttpServletRequest request, HttpServletResponse response, List<PdfReceiptMgmtDTO> list, String strInFile, String billSect) throws Exception {
        String result = "";
        try {
            LOGGER.debug("html -> pdf 생성");
            //pdf 문서 객체
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);

            //pdf 생성 객체
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(strInFile));

            document.open();
            document.add(new Chunk(""));
            XMLWorkerHelper helper = XMLWorkerHelper.getInstance();

            // css
            CSSResolver cssResolver = new StyleAttrCSSResolver();
            String pdfCss = request.getSession().getServletContext().getRealPath("/assets/css/pdf-form.min.css");
            CssFile cssFile = helper.getCSS(new FileInputStream(pdfCss));
            cssResolver.addCss(cssFile);

            String pdfFont = request.getSession().getServletContext().getRealPath("/assets/font/MALGUN.TTF");

            // HTML, 폰트 설정
            XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
            fontProvider.register(pdfFont, "MalgunGothic");
            CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

            PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
            HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
            CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
            XMLWorker worker = new XMLWorker(css, true);
            XMLParser xmlParser = new XMLParser(worker, Charset.forName("UTF-8"));

            String rootPath = request.getSession().getServletContext().getContext("/assets").getRealPath("");
            LOGGER.debug("rootPath " + rootPath);
            String htmlStr = "";

            if ("cut".equals(billSect)) {    // 영수증
                htmlStr = cutHtml(list);
            } else if ("cert1".equals(billSect)) { // 교육비납입증명서
                htmlStr = eduHtml(list);
            } else if ("cert2".equals(billSect)) { // 장기요양급여 납부확인서
                htmlStr = recpHtml(list);
            } else if ("cert3".equals(billSect)) { // 기부금영수증
                htmlStr = dntnHtml(list);
            }

            xmlParser.parse(new StringReader(htmlStr.toString()));
            //pdf 파일이 생성됨
            document.close();
            writer.close();
            result = "0000";
        } catch (Exception e) {
            result = "9999";
            LOGGER.error(e.getMessage());
        }
        return result;
    }

    /**
     * 교육비 납입증명서
     *
     * @param list
     * @return
     */
    public String eduHtml(List<PdfReceiptMgmtDTO> list) {
        LOGGER.debug("교육비 납입증명서");
        String htmlContentStr = "";
        int totalCnt = 0;
        StrUtil strUtil = new StrUtil();
        final DecimalFormat df = new DecimalFormat("#,##0");
        for (int i = 0; i < list.size(); i++) {
            htmlContentStr += "<!DOCTYPE html><html>" +
                    "<head>" +
                    "<title>신한 다모아 교육비납입증명서</title>" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" +
                    "</head>" +
                    "<body style=\"font-family:MalgunGothic; width:21cm;padding:14px 17px;\">" +
                    "<table class=\"table\">" +
                    "	<tr>" +
                    "		<td style=\"width:33%;\"></td>" +
                    "		<td style=\"width:33%; text-align:center;\">" +
                    "			<h3 style=\"font-size:15pt;\">교 육 비 납 입 증 명 서</h3>" +
                    "		</td>" +
                    "		<td style=\"width:33%;\"></td>" +
                    "	</tr>" +
                    "</table>" +
                    "<table class=\"table table-bordered table-top-bottom-bordered table-fixed-height table-has-padding-edu\">" +
                    "	<tr>" +
                    "		<td colspan=\"2\">&#9312; 상 호 " + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(list.get(i).getChaName())) + "</td>" +
                    "		<td>&#9313; 사업자등록번호 " + StrUtil.nullToVoid(list.get(i).getChaOffNo()) + "</td>" +
                    "	</tr>" +
                    "	<tr>" +
                    "		<td colspan=\"2\">&#9314; 대표자 " + StrUtil.nullToVoid(list.get(i).getOwner()) + "</td>" +
                    "		<td>&#9315; 전 화 번 호 " + StrUtil.nullToVoid(list.get(i).getOwnerTel()) + "</td>" +
                    "	</tr>" +
                    "	<tr>" +
                    "		<td colspan=\"3\">&#9316; 주 소 " + StrUtil.nullToVoid(list.get(i).getAddress()) + "</td>" +
                    "	</tr>" +
                    "	<tr>" +
                    "		<td rowspan=\"2\" style=\"width:10%;\">신청자</td>" +
                    "		<td style=\"width:50%;\">&#9317; 성명</td>" +
                    "		<td style=\"width:40%;\">&#9318; 주민등록번호</td>" +
                    "	</tr>" +
                    "	<tr>" +
                    "		<td colspan=\"2\">&#9319; 주소</td>" +
                    "	</tr>" +
                    "	<tr>" +
                    "		<td>대상자</td>" +
                    "		<td>&#9320; 성명 " + StrUtil.nullToVoid(list.get(i).getCusName()) + "</td>" +
                    "		<td>&#9321; 신청인과의 관계</td>" +
                    "	</tr>" +
                    "</table>" +
                    "<h3 style=\"text-align:center; margin-bottom:10px;\">&#8544;. 교육비 부담 명세</h3>" +
                    "<table class=\"table table-bordered table-top-bottom-bordered table-fixed-height table-has-padding-edu\">" +
                    "	<tr>" +
                    "		<th rowspan=\"2\">&#9322; 납부년월</th>" +
                    "		<th rowspan=\"2\">&#9323; 종 류</th>" +
                    "		<th rowspan=\"2\">&#9324; 구 분</th>" +
                    "		<th rowspan=\"2\">&#9325; 총교육비(A)</th>" +
                    "		<th colspan=\"2\">장학금 등 수혜액(B)</th>" +
                    "		<th rowspan=\"2\">공제대상<br/>교육비부담액 (C=A-B)</th>" +
                    "	</tr>" +
                    "	<tr>" +
                    "		<th>학비감면</th>" +
                    "		<th>직접지급액</th>" +
                    "	</tr>";
            for (PdfReceiptDetailsDTO each : list.get(i).getDetailsList()) {
                htmlContentStr += "	<tr>" +
                        "		<td style=\"text-align:center;\">" + each.getPaymentMonth() + "</td>" +
                        "		<td>&nbsp;</td>" +
                        "		<td></td>" ;
                if(each.getRcpAmount() == 0){
                    htmlContentStr +="		<td style=\"text-align:right;\"></td>";
                }else{
                    htmlContentStr +="		<td style=\"text-align:right;\">" + df.format(each.getRcpAmount()) + "</td>";
                }
                htmlContentStr +="		<td></td>" +
                        "		<td></td>" +
                        "		<td></td>" +
                        "	</tr>";
                totalCnt += each.getRcpAmount();
            }
            htmlContentStr += "	<tr>" +
                    "		<td style=\"text-align:center;\">계</td>" +
                    "		<td></td>" +
                    "		<td></td>" +
                    "		<td style=\"text-align:right;\">" + df.format(totalCnt) + "</td>" +
                    "		<td></td>" +
                    "		<td></td>" +
                    "		<td></td>" +
                    "	</tr>" +
                    "</table>" +
                    "<h3 style=\"text-align:center; margin-bottom:10px;\">&#8545;. 교복 구입 명세</h3>" +
                    "<table class=\"table table-bordered table-top-bottom-bordered table-fixed-height table-has-padding-edu\">" +
                    "	<tr>" +
                    "		<th>구입년월</th>" +
                    "		<th>품 목</th>" +
                    "		<th>수 량</th>" +
                    "		<th>단 가</th>" +
                    "		<th>금 액</th>" +
                    "	</tr>" +
                    "	<tr>" +
                    "		<td></td>" +
                    "		<td></td>" +
                    "		<td></td>" +
                    "		<td></td>" +
                    "		<td></td>" +
                    "	</tr>" +
                    "	<tr>" +
                    "		<td></td>" +
                    "		<td></td>" +
                    "		<td></td>" +
                    "		<td></td>" +
                    "		<td></td>" +
                    "	</tr>" +
                    "	<tr>" +
                    "		<td style=\"text-align:center;\">사용목적</td>" +
                    "		<td colspan=\"4\">교육비공제 신청용</td>" +
                    "	</tr>" +
                    "</table>" +
                    "<table class=\"table table-outline-bordered table-fixed-height table-outline-bordered-3 table-font-size\">" +
                    "	<tr>" +
                    "		<td colspan=\"2\">" +
                    "			&#8968;소득세법 시행령&#8971; 제113조제1항에 따라 위와 같이 교육비를 지출하였음을 증명해 주시기 바랍니다." +
                    "		</td>" +
                    "	</tr>" +
                    "	<tr>" +
                    "		<td colspan=\"2\" style=\"text-align:right;\">" + strUtil.getYear() + " 년    " + strUtil.getMonth() + "월   " + strUtil.getDate() + " 일</td>" +
                    "	</tr>" +
                    "	<tr>" +
                    "		<td style=\"text-align:right; width:50%;\">신청인</td>" +
                    "		<td style=\"text-align:right; width:50%;\">(서명 또는 인)</td>" +
                    "	</tr>" +
                    "	<tr>" +
                    "		<td colspan=\"2\" style=\"border-top:2px solid #a1a1a1;\">" +
                    "			위와 같이 교육비를 지출하였음을 증명합니다." +
                    "		</td>" +
                    "	</tr>" +
                    "	<tr>" +
                    "		<td colspan=\"2\" style=\"text-align:right;\">" + strUtil.getYear() + " 년    " + strUtil.getMonth() + "월   " + strUtil.getDate() + " 일</td>" +
                    "	</tr>" +
                    "	<tr>" +
                    "		<td style=\"text-align:right;\">확인자 " + StrUtil.nullToVoid(list.get(i).getOwner()) + "</td>" +
                    "		<td style=\"text-align:right;\">(서명 또는 인)</td>" +
                    "	</tr>" +
                    "</table>" +
                    "</body>" +
                    "</html>";
            totalCnt = 0;
        }
        return htmlContentStr;
    }

    /**
     * 장기요양급여 납부확인서
     *
     * @param list
     * @return
     * @throws Exception
     */
    public String recpHtml(List<PdfReceiptMgmtDTO> list) throws Exception {
        LOGGER.debug("장기요양급여 납부확인서");
        String htmlStr = "";
        StrUtil strUtil = new StrUtil();
        HashMap<String, Object> map = new HashMap<String, Object>();
        int totAmt = 0;
        String rcpAmt = "";
        final DecimalFormat df = new DecimalFormat("#,##0");
        for (int i = 0; i < list.size(); i++) {
            totAmt = 0;
            if("N".equals(list.get(i).getAmtChkTy())){
                map.put("rcpMasList", list.get(i).getCheckListValue());
            }else{
                map.put("notiMasList", list.get(i).getCheckListValue());
            }
            map.put("vano", list.get(i).getVano());
            map.put("paymentYear", list.get(i).getPayDay());
            List<PdfReceiptMgmtDTO> mlist;
            if(!StringUtils.isEmpty(list.get(i).getRoleCheck()) && "adm".equals(list.get(i).getRoleCheck())) {
                mlist = receiptMgmtService.getAdmRecpList(map);
            }else{
                mlist = receiptMgmtService.getRecpList(map);
            }
            htmlStr += "<!DOCTYPE html><html>" +
                    "<head>" +
                    "<title>신한 다모아 장기요양급여 납부확인서</title>" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" +
                    "</head>" +
                    "<body style=\"font-family:MalgunGothic; width:21cm;padding:14px 17px;\">" +
                    "<table class=\"table\">" +
                    "        <tr>" +
                    "            <td style=\"width:20%;\"></td>" +
                    "            <td style=\"width:58%; text-align:center;\">" +
                    "                <h3 style=\"font-size:15pt;\">장기요양급여비 납부 확인서</h3>" +
                    "            </td>" +
                    "            <td style=\"width:20%;\"></td>" +
                    "        </tr>" +
                    "</table>" +
                    "<p style=\"margin-bottom:10px;\">발급번호 : </p>" +
                    "    <table class=\"table table-bordered table-outline-bordered-2 table-top-bottom-bordered table-fixed-height table-has-padding\">" +
                    "        <tr>" +
                    "            <th style=\"width:23%; text-align:left;\">수급자 성명</th>" +
                    "            <td style=\"width:23%;\">" + list.get(i).getCusName() + "</td>" +
                    "            <th style=\"width:23%; text-align:left;\">주민등록번호</th>" +
                    "            <td style=\"width:31%;\"></td>" +
                    "        </tr>" +
                    "        <tr>" +
                    "            <th style=\"text-align:left;\">장기요양기관명</th>" +
                    "            <td>" + StringEscapeUtils.escapeXml11(list.get(i).getChaName()) + "</td>" +
                    "            <th style=\"text-align:left;\">고유번호<br/>(사업자등록번호)</th>" +
                    "            <td>" + list.get(i).getChaOffNo() + "</td>" +
                    "        </tr>" +
                    "        <tr>" +
                    "            <th style=\"text-align:left;\">장기요양기관 주소<br/>(전화번호)</th>" +
                    "            <td>" + list.get(i).getAddress() + "</td>" +
                    "            <th style=\"text-align:left;\">대표자 성명</th>" +
                    "            <td>" + list.get(i).getOwner() + "</td>" +
                    "        </tr>" +
                    "    </table>" +
                    "    <table class=\"table table-bordered table-outline-bordered-2 table-top-bottom-bordered table-fixed-height table-has-padding\" style=\"font-size:8pt;\">" +
                    "        <tr>" +
                    "            <th colspan=\"10\" style=\"text-align:center; font-size:10pt;\">" + list.get(i).getPayDay() + " 년 장기요양급여비 납부내역</th>" +
                    "        </tr>" +
                    "        <tr>" +
                    "            <th rowspan=\"4\">구분</th>" +
                    "            <th colspan=\"9\">급여비 내역</th>" +
                    "        </tr>" +
                    "        <tr>" +
                    "            <th rowspan=\"3\">" +
                    "                &#9312; 총액<br/>" +
                    "                (&#9313; + &#9314; + &#9315;)" +
                    "            </th>" +
                    "            <th colspan=\"5\">급여</th>" +
                    "            <th>비급여</th>" +
                    "            <th colspan=\"2\">수급자 부담총액</th>" +
                    "        </tr>" +
                    "        <tr>" +
                    "            <th rowspan=\"2\">&#9313;<br/>공단<br/>부담액</th>" +
                    "            <th colspan=\"4\">&#9314; 수급자 부담액</th>" +
                    "            <th rowspan=\"2\">&#9315;<br/>수급자<br/>부담액</th>" +
                    "            <th rowspan=\"2\">&#9316;<br/>수급자<br/>부담액<br/>(&#9314;+&#9315;)</th>" +
                    "            <th rowspan=\"2\">&#9317;<br/>납부일자</th>" +
                    "        </tr>" +
                    "        <tr>" +
                    "            <th>총계</th>" +
                    "            <th>카드</th>" +
                    "            <th>현금<br/>영수증</th>" +
                    "            <th>현금</th>" +
                    "        </tr>";
            for (int k = 1; k <= mlist.size(); k++) {
                if (mlist.get(k - 1) == null) {
                    LOGGER.debug("null >>>");
                    rcpAmt = "";
                    totAmt +=0;
                } else if(k == 1){
                    rcpAmt = StrUtil.strComma(mlist.get(k - 1).getRcpAmt());
                    totAmt += mlist.get(k - 1).getRcpAmount();
                } else if(mlist.get(k - 1).getPayMonth() != mlist.get(k - 2).getPayMonth()){
                    rcpAmt = StrUtil.strComma(mlist.get(k - 1).getRcpAmt());
                    totAmt += mlist.get(k - 1).getRcpAmount();
                } else{
                    rcpAmt = "";
                    totAmt +=0;
                }
                htmlStr += "<tr>" +
                        "            <td style=\"text-align:center; width:70px;\">" + k + "월</td>" +
                        "            <td style=\"text-align:right;\">" + rcpAmt + "</td>" +
                        "            <td>&nbsp;</td>" +
                        "            <td>&nbsp;</td>" +
                        "            <td>&nbsp;</td>" +
                        "            <td>&nbsp;</td>" +
                        "            <td>&nbsp;</td>" +
                        "            <td>&nbsp;</td>" +
                        "            <td>&nbsp;</td>" +
                        "            <td>&nbsp;</td>" +
                        "        </tr>";
            }
            htmlStr += "<tr>" +
                    "<td style=\"text-align:center;\">계</td>" +
                    "<td style=\"text-align:right;\">" + df.format(totAmt) + "</td>" +
                    "<td></td>" +
                    "<td></td>" +
                    "<td></td>" +
                    "<td></td>" +
                    "<td></td>" +
                    "<td></td>" +
                    "<td></td>" +
                    "<td></td>" +
                    "</tr>" +
                    "<tr>" +
                    "            <td colspan=\"7\" style=\"text-align:center;\">&#9318; 소득공제 대상액 총계 (=&#9314;)</td>" +
                    "            <td colspan=\"3\">&nbsp;</td>" +
                    "        </tr>" +
                    "    </table>" +
                    "    <p style=\"font-size:11pt; margin-bottom:10px;\">" +
                    "        &#8251; 비급여항목(식사재료비, 상급침실이용료, 이미용비)은 소득공제 대상에 해당되지 않음" +
                    "    </p>" +
                    "    <p style=\"text-align:center; margin-bottom:20px;\">" +
                    "        " + strUtil.getYear() + " 년 " + strUtil.getMonth() + " 월 " + strUtil.getDate() + " 일" +
                    "    </p>" +
                    "    <p style=\"text-align:center;font-size:12pt; margin-bottom:20px;\">" +
                    "        장기요양기관 장 <small>(인)</small>" +
                    "    </p>" +
                    "    <table class=\"table table-outline-bordered-2 table-fixed-height\" style=\"margin-bottom:25px;\">" +
                    "        <tr>" +
                    "            <td>" +
                    "                이 납부확인서는 &#8968;소득세법&#8971; 에 따른 의료비 공제신청에 사용할 수 있습니다." +
                    "            </td>" +
                    "        </tr>" +
                    "        <tr class=\"border-top-1-a1a1a1\">" +
                    "            <td>" +
                    "                알림 : 현금영수증 문의 126 인터넷 홈페이지 : http://www.taxsave.go.kr" +
                    "            </td>" +
                    "        </tr>" +
                    "    </table>" +
                    "</body>" +
                    "</html>";
        }
        return htmlStr;
    }

    /**
     * 기부금
     *
     * @param list
     * @return
     */
    public String dntnHtml(List<PdfReceiptMgmtDTO> list) throws Exception {
        LOGGER.debug("기부금");
        String htmlStr = "";
        HashMap<String, Object> map = new HashMap<String, Object>();
        int idx = 0;
        int sidx = 0;
        int midx = 0;
        String payDay = "";
        String rcpAmt = "";
        for (int i = 0; i < list.size(); i++) {
            if("N".equals(list.get(i).getAmtChkTy())){
                map.put("rcpMasList", list.get(i).getCheckListValue());
            }else{
                map.put("notiMasList", list.get(i).getCheckListValue());
            }
            map.put("vano", list.get(i).getVano());
            map.put("paymentYear", list.get(i).getPayDay());
            List<PdfReceiptMgmtDTO> mlist;
            if(!StringUtils.isEmpty(list.get(i).getRoleCheck()) && "adm".equals(list.get(i).getRoleCheck())) {
                map.put("cert3", "cert3");
                mlist = receiptMgmtService.getAdmRecpList(map);
            }else{
                map.put("cert3", "cert3");
                mlist = receiptMgmtService.getRecpList(map);
            }
            if(mlist.size()>6){
                if(mlist.size()%6 == 0){
                    midx = mlist.size()/6;
                }else{
                    midx = (mlist.size()/6)+1;
                }
                sidx = 0;
                idx = 0;
                for (int s =0;s<midx;s++) {
                    sidx = s*6;
                    idx = (s+1)*6;
                    htmlStr += "<!DOCTYPE html><html>" +
                            "<head>" +
                            "<title>신한 다모아 기부금영수증</title>" +
                            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" +
                            "</head>" +
                            "<body style=\"font-family:MalgunGothic; width:21cm;padding:14px 17px;\">" +
                            "<table class=\"table\">" +
                            "    <tr>" +
                            "        <td style=\"width:25%;\">" +
                            "            <table class=\"table-bordered\">" +
                            "                <tr>" +
                            "                    <td style=\"padding:9px;\">일련번호</td>" +
                            "                    <td style=\"width:100px;\"></td>" +
                            "                </tr>" +
                            "            </table>" +
                            "        </td>" +
                            "        <td style=\"width:50%; text-align:center;\">" +
                            "            <h3 style=\"font-size:15pt;\">기부금 영수증</h3>" +
                            "        </td>" +
                            "        <td style=\"width:25%;\"></td>" +
                            "    </tr>" +
                            "</table>" +
                            "<p style=\"margin-bottom:10px;\">※ 아래의 작성방법을 읽고 작성하여 주시기 바랍니다.</p>" +
                            "<table class=\"table table-bordered table-top-bottom-bordered table-fixed-height table-has-padding\">" +
                            "    <tr>" +
                            "        <th colspan=\"2\" style=\"text-align:left;\">1. 기부자</th>" +
                            "    </tr>" +
                            "    <tr>" +
                            "        <td style=\"width:50%;\">성명(법인명) " + list.get(i).getCusName() + "</td>" +
                            "        <td style=\"width:50%;\">" +
                            "            주민등록번호<br/>(사업자등록번호)" +
                            "        </td>" +
                            "    </tr>" +
                            "    <tr>" +
                            "        <td colspan=\"2\">주소(소재지)</td>" +
                            "    </tr>" +
                            "</table>" +
                            "<table class=\"table table-bordered table-top-bottom-bordered table-fixed-height table-has-padding\">" +
                            "    <tr>" +
                            "        <th colspan=\"2\" style=\"text-align:left;\">2.기부금 단체</th>" +
                            "    </tr>" +
                            "    <tr>" +
                            "        <td style=\"width:50%;\">단체명</td>" +
                            "        <td style=\"width:50%;\">" +
                            "            사업자등록번호<br/>(고유번호)" +
                            "        </td>" +
                            "    </tr>" +
                            "    <tr>" +
                            "        <td style=\"width:50%;\">소재지 " + list.get(i).getAddress() + "</td>" +
                            "        <td style=\"width:50%;\">" +
                            "            기부금공제대상<br/>기부금단체 근거법령" +
                            "        </td>" +
                            "    </tr>" +
                            "</table>" +
                            "<table class=\"table table-bordered table-top-bottom-bordered table-fixed-height table-has-padding\">" +
                            "    <tr>" +
                            "        <th colspan=\"2\" style=\"text-align:left;\">3.기부자 모집처(언론기관 등)</th>" +
                            "    </tr>" +
                            "    <tr>" +
                            "        <td style=\"width:50%;\">단체명 " + StringEscapeUtils.escapeXml11(list.get(i).getChaName()) + "</td>" +
                            "        <td style=\"width:50%;\">사업자등록번호 " + list.get(i).getChaOffNo() + "</td>" +
                            "    </tr>" +
                            "    <tr>" +
                            "        <td colspan=\"2\">소재지</td>" +
                            "    </tr>" +
                            "</table>" +
                            "<table class=\"table table-bordered table-top-bottom-bordered table-fixed-height table-has-padding\" style=\"font-size:8pt;\">" +
                            "    <thead>" +
                            "        <tr>" +
                            "            <th colspan=\"11\" style=\"text-align:left; font-size:10pt;\">" +
                            "                4.기부내용" +
                            "            </th>" +
                            "        </tr>" +
                            "        <tr>" +
                            "            <th rowspan=\"3\">유형</th>" +
                            "            <th rowspan=\"3\">코드</th>" +
                            "            <th rowspan=\"3\">구분</th>" +
                            "            <th rowspan=\"3\">년월일</th>" +
                            "            <th colspan=\"3\">내용</th>" +
                            "            <th colspan=\"4\">기부금액</th>" +
                            "        </tr>" +
                            "        <tr>" +
                            "            <th rowspan=\"2\">품명</th>" +
                            "            <th rowspan=\"2\">수량</th>" +
                            "            <th rowspan=\"2\">단가</th>" +
                            "            <th rowspan=\"2\">합계</th>" +
                            "            <th rowspan=\"2\" style=\"text-align:center;\">공제대상<br/>기부금액</th>" +
                            "            <th colspan=\"2\">공제제외 기부금</th>" +
                            "        </tr>" +
                            "        <tr>" +
                            "            <th>기부장려금<br/>신청금액</th>" +
                            "            <th>기타</th>" +
                            "        </tr>" +
                            "    </thead>" ;
                    for (int k = sidx; k < idx; k++) {
                        if(k<mlist.size()){
                            payDay = StrUtil.nullToVoid(StrUtil.dateFormat(mlist.get(k).getPayDay()));
                            rcpAmt = StrUtil.nullToVoid(StrUtil.strComma(mlist.get(k).getRcpAmt()));
                        }else{
                            payDay = "";
                            rcpAmt = "";
                        }
                            htmlStr += "<tr>" +
                                    "        <td></td>" +
                                    "        <td></td>" +
                                    "        <td></td>" +
                                    "        <td>" + payDay + "</td>" +
                                    "        <td></td>" +
                                    "        <td></td>" +
                                    "        <td></td>" +
                                    "        <td style=\"text-align:right;\">" + rcpAmt + "</td>" +
                                    "        <td></td>" +
                                    "        <td></td>" +
                                    "        <td></td>" +
                                    "    </tr>";
                    }
                    htmlStr += "</table>" +
                            "<table class=\"table table-outline-bordered table-fixed-height table-has-padding\" style=\"margin-bottom:60px;\">" +
                            "    <tr>" +
                            "        <td colspan=\"2\">" +
                            "            &#8968;소득세법&#8971; 제34조, &#8968;조세특례제한법&#8971; 제75조&middot;제76조&middot;제88조의4 및 &#8968;법인세법&#8971; 제24조에 따른 기부금을 위와 같이 기부하였음을 증명하여 주시기 바랍니다." +
                            "        </td>" +
                            "    </tr>" +
                            "    <tr>" +
                            "        <td colspan=\"2\" style=\"text-align:right;\">" + StrUtil.getYear() + " 년   " + StrUtil.getMonth() + " 월  " + StrUtil.getDate() + "  일</td>" +
                            "    </tr>" +
                            "    <tr>" +
                            "        <td style=\"text-align:right; width:50%;\">신청인</td>" +
                            "        <td style=\"text-align:right; width:50%;\">(서명 또는 인)</td>" +
                            "    </tr>" +
                            "    <tr>" +
                            "        <td colspan=\"2\" style=\"border-top:1px solid #a1a1a1;\">" +
                            "            위와 같이 기부금을 기부받았음을 증명합니다." +
                            "        </td>" +
                            "    </tr>" +
                            "    <tr>" +
                            "        <td colspan=\"2\" style=\"text-align:right;\">" + StrUtil.getYear() + " 년   " + StrUtil.getMonth() + " 월  " + StrUtil.getDate() + "  일</td>" +
                            "    </tr>" +
                            "    <tr>" +
                            "        <td style=\"text-align:right;\">기부금 수령인 " + list.get(i).getOwner() + "</td>" +
                            "        <td style=\"text-align:right;\">(서명 또는 인)</td>" +
                            "    </tr>" +
                            "</table>" +
                            "</body>" +
                            "</html>";
                }
            }else {
                idx = 6;
                htmlStr += "<!DOCTYPE html><html>" +
                        "<head>" +
                        "<title>신한 다모아 기부금영수증</title>" +
                        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" +
                        "</head>" +
                        "<body style=\"font-family:MalgunGothic; width:21cm;padding:14px 17px;\">" +
                        "<table class=\"table\">" +
                        "    <tr>" +
                        "        <td style=\"width:25%;\">" +
                        "            <table class=\"table-bordered\">" +
                        "                <tr>" +
                        "                    <td style=\"padding:9px;\">일련번호</td>" +
                        "                    <td style=\"width:100px;\"></td>" +
                        "                </tr>" +
                        "            </table>" +
                        "        </td>" +
                        "        <td style=\"width:50%; text-align:center;\">" +
                        "            <h3 style=\"font-size:15pt;\">기부금 영수증</h3>" +
                        "        </td>" +
                        "        <td style=\"width:25%;\"></td>" +
                        "    </tr>" +
                        "</table>" +
                        "<p style=\"margin-bottom:10px;\">※ 아래의 작성방법을 읽고 작성하여 주시기 바랍니다.</p>" +
                        "<table class=\"table table-bordered table-top-bottom-bordered table-fixed-height table-has-padding\">" +
                        "    <tr>" +
                        "        <th colspan=\"2\" style=\"text-align:left;\">1. 기부자</th>" +
                        "    </tr>" +
                        "    <tr>" +
                        "        <td style=\"width:50%;\">성명(법인명) " + list.get(i).getCusName() + "</td>" +
                        "        <td style=\"width:50%;\">" +
                        "            주민등록번호<br/>(사업자등록번호)" +
                        "        </td>" +
                        "    </tr>" +
                        "    <tr>" +
                        "        <td colspan=\"2\">주소(소재지)</td>" +
                        "    </tr>" +
                        "</table>" +
                        "<table class=\"table table-bordered table-top-bottom-bordered table-fixed-height table-has-padding\">" +
                        "    <tr>" +
                        "        <th colspan=\"2\" style=\"text-align:left;\">2.기부금 단체</th>" +
                        "    </tr>" +
                        "    <tr>" +
                        "        <td style=\"width:50%;\">단체명</td>" +
                        "        <td style=\"width:50%;\">" +
                        "            사업자등록번호<br/>(고유번호)" +
                        "        </td>" +
                        "    </tr>" +
                        "    <tr>" +
                        "        <td style=\"width:50%;\">소재지 " + list.get(i).getAddress() + "</td>" +
                        "        <td style=\"width:50%;\">" +
                        "            기부금공제대상<br/>기부금단체 근거법령" +
                        "        </td>" +
                        "    </tr>" +
                        "</table>" +
                        "<table class=\"table table-bordered table-top-bottom-bordered table-fixed-height table-has-padding\">" +
                        "    <tr>" +
                        "        <th colspan=\"2\" style=\"text-align:left;\">3.기부자 모집처(언론기관 등)</th>" +
                        "    </tr>" +
                        "    <tr>" +
                        "        <td style=\"width:50%;\">단체명 " + StringEscapeUtils.escapeXml11(list.get(i).getChaName()) + "</td>" +
                        "        <td style=\"width:50%;\">사업자등록번호 " + list.get(i).getChaOffNo() + "</td>" +
                        "    </tr>" +
                        "    <tr>" +
                        "        <td colspan=\"2\">소재지</td>" +
                        "    </tr>" +
                        "</table>" +
                        "<table class=\"table table-bordered table-top-bottom-bordered table-fixed-height table-has-padding\" style=\"font-size:8pt;\">" +
                        "    <thead>" +
                        "        <tr>" +
                        "            <th colspan=\"11\" style=\"text-align:left; font-size:10pt;\">" +
                        "                4.기부내용" +
                        "            </th>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <th rowspan=\"3\">유형</th>" +
                        "            <th rowspan=\"3\">코드</th>" +
                        "            <th rowspan=\"3\">구분</th>" +
                        "            <th rowspan=\"3\">년월일</th>" +
                        "            <th colspan=\"3\">내용</th>" +
                        "            <th colspan=\"4\">기부금액</th>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <th rowspan=\"2\">품명</th>" +
                        "            <th rowspan=\"2\">수량</th>" +
                        "            <th rowspan=\"2\">단가</th>" +
                        "            <th rowspan=\"2\">합계</th>" +
                        "            <th rowspan=\"2\" style=\"text-align:center;\">공제대상<br/>기부금액</th>" +
                        "            <th colspan=\"2\">공제제외 기부금</th>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <th>기부장려금<br/>신청금액</th>" +
                        "            <th>기타</th>" +
                        "        </tr>" +
                        "    </thead>";
                for (int k = 0; k < idx; k++) {
                    if(k<mlist.size()){
                        payDay = StrUtil.nullToVoid(StrUtil.dateFormat(mlist.get(k).getPayDay()));
                        rcpAmt = StrUtil.nullToVoid(StrUtil.strComma(mlist.get(k).getRcpAmt()));
                    }else{
                        payDay = "";
                        rcpAmt = "";
                    }
                        htmlStr += "<tr>" +
                                "        <td></td>" +
                                "        <td></td>" +
                                "        <td></td>" +
                                "        <td>" + payDay + "</td>" +
                                "        <td></td>" +
                                "        <td></td>" +
                                "        <td></td>" +
                                "        <td style=\"text-align:right;\">" + rcpAmt + "</td>" +
                                "        <td></td>" +
                                "        <td></td>" +
                                "        <td></td>" +
                                "    </tr>";
                }
                htmlStr += "</table>" +
                        "<table class=\"table table-outline-bordered table-fixed-height table-has-padding\" style=\"margin-bottom:60px;\">" +
                        "    <tr>" +
                        "        <td colspan=\"2\">" +
                        "            &#8968;소득세법&#8971; 제34조, &#8968;조세특례제한법&#8971; 제75조&middot;제76조&middot;제88조의4 및 &#8968;법인세법&#8971; 제24조에 따른 기부금을 위와 같이 기부하였음을 증명하여 주시기 바랍니다." +
                        "        </td>" +
                        "    </tr>" +
                        "    <tr>" +
                        "        <td colspan=\"2\" style=\"text-align:right;\">" + StrUtil.getYear() + " 년   " + StrUtil.getMonth() + " 월  " + StrUtil.getDate() + "  일</td>" +
                        "    </tr>" +
                        "    <tr>" +
                        "        <td style=\"text-align:right; width:50%;\">신청인</td>" +
                        "        <td style=\"text-align:right; width:50%;\">(서명 또는 인)</td>" +
                        "    </tr>" +
                        "    <tr>" +
                        "        <td colspan=\"2\" style=\"border-top:1px solid #a1a1a1;\">" +
                        "            위와 같이 기부금을 기부받았음을 증명합니다." +
                        "        </td>" +
                        "    </tr>" +
                        "    <tr>" +
                        "        <td colspan=\"2\" style=\"text-align:right;\">" + StrUtil.getYear() + " 년   " + StrUtil.getMonth() + " 월  " + StrUtil.getDate() + "  일</td>" +
                        "    </tr>" +
                        "    <tr>" +
                        "        <td style=\"text-align:right;\">기부금 수령인 " + list.get(i).getOwner() + "</td>" +
                        "        <td style=\"text-align:right;\">(서명 또는 인)</td>" +
                        "    </tr>" +
                        "</table>" +
                        "</body>" +
                        "</html>";
            }
        }
        return htmlStr;
    }

    /**
     * 영수증폼
     *
     * @param list
     * @return
     */
    public String cutHtml(List<PdfReceiptMgmtDTO> list) {
        LOGGER.debug("영수증폼");
        String htmlStr = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");
        Date date = new Date();
        String dateString = format.format(date);

        for (int i = 0; i < list.size(); i++) {
            htmlStr += "<!DOCTYPE html><html>" +
                    "<head>" +
                    "<title>신한 다모아 영수증</title>" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" +
                    "</head>" +
                    "<body style=\"font-family:MalgunGothic; width:21cm;padding:14px 17px;\">" +
                    "<p class=\"receipt-title\" style=\"margin-top:80px;\">" +
                    "영수증(보관용) - " + list.get(i).getMasMonth() + "월분</p>" +
                    "<table>" +
                    "    <tr>" +
                    "        <td style=\"width:10%\"></td>" +
                    "        <td>" +
                    "		 	<table class=\"table table-bordered table-top-bottom-bordered table-receipt\" style=\"margin:0 auto;\">" +
                    "    			<tr>" +
                    "        			<th>등록구분</th>" +
                    "        			<td>" + StrUtil.nullToVoid(list.get(i).getCusKey()) + "</td>" +
                    "    			</tr>" +
                    "    			<tr>" +
                    "        			<th>성명</th>" +
                    "        			<td>" + StrUtil.nullToVoid(list.get(i).getCusName()) + "</td>" +
                    "    			</tr>" +
                    "    			<tr>" +
                    "        			<th>고객구분</th>" +
                    "        			<td>" + StrUtil.nullToVoid(list.get(i).getrCusGubn()) + "</td>" +
                    "    			</tr>" +
                    "    			<tr>" +
                    "        			<th>금액</th>" +
                    "        			<td>" + StrUtil.strComma(list.get(i).getRcpAmt()) + "원</td>" +
                    "    			</tr>" +
                    "    			<tr>" +
                    "        			<th>납부일자</th>" +
                    "        			<td>" + StrUtil.dateFormat(list.get(i).getPayDay()) + "</td>" +
                    "    			</tr>" +
                    "			</table>" +
                    "        </td>" +
                    "        <td style=\"width:10%\"></td>" +
                    "    </tr>" +
                    "</table>" +
                    "<p class=\"text-9pt\" style=\"margin-top:30px; margin-bottom:10px;\">위와 같이 영수함.</p>" +
                    "<p class=\"text-9pt\" style=\"margin-bottom:10px;\">" + dateString + "</p>" +
                    "<p class=\"issue-org\" style=\"margin-bottom:50px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(list.get(i).getChaName())) + "</p>" +
                    "<table style=\"margin-bottom:30px;\">" +
                    "    <tr>" +
                    "        <td style=\"border-top:1px dashed #d6d6d6; width:80%;\">&nbsp;</td>" +
                    "    </tr>" +
                    "</table>" +
                    "<p class=\"receipt-title\">영 수 증(납부자용) - " + list.get(i).getMasMonth() + "월분</p>" +
                    "<table>" +
                    "    <tr>" +
                    "        <td style=\"width:10%\"></td>" +
                    "        <td>" +
                    "<table class=\"table table-bordered table-top-bottom-bordered table-receipt\" style=\"margin:0 auto;\">" +
                    "    <tr>" +
                    "        <th>등록구분</th>" +
                    "        <td>" + StrUtil.nullToVoid(list.get(i).getCusKey()) + "</td>" +
                    "    </tr>" +
                    "    <tr>" +
                    "        <th>성명</th>" +
                    "        <td>" + StrUtil.nullToVoid(list.get(i).getCusName()) + "</td>" +
                    "    </tr>" +
                    "    <tr>" +
                    "        <th>고객구분</th>" +
                    "        <td>" + StrUtil.nullToVoid(list.get(i).getrCusGubn()) + "</td>" +
                    "    </tr>" +
                    "    <tr>" +
                    "        <th>금액</th>" +
                    "        <td>" + StrUtil.strComma(list.get(i).getRcpAmt()) + "원</td>" +
                    "    </tr>" +
                    "    <tr>" +
                    "        <th>납부일자</th>" +
                    "        <td>" + StrUtil.dateFormat(list.get(i).getPayDay()) + "</td>" +
                    "    </tr>" +
                    "</table>" +
                    "        </td>" +
                    "        <td style=\"width:10%\">&nbsp;</td>" +
                    "    </tr>" +
                    "</table>" +
                    "<p class=\"text-9pt\" style=\"margin-top:30px; margin-bottom:10px;\">위와 같이 영수함.</p>" +
                    "<p class=\"text-9pt\" style=\"margin-bottom:10px;\">" + dateString + "</p>" +
                    "<p class=\"issue-org\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(list.get(i).getChaName())) + "</p>" +
                    "</body>" +
                    "</html>";
        }
        LOGGER.debug("html >>> " + htmlStr.toString());

        return htmlStr;
    }

    /**
     * pdf download
     *
     * @param fileName
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getPdfViewer")
    @ResponseBody
    public ModelAndView getPdfViewer(@RequestParam(value = "fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        LOGGER.debug("기관 {} pdf download", user);
        ModelAndView mav = new ModelAndView();
        String strInFile = fileName + ".pdf";
        LOGGER.debug("fileName " + fileName);
        FileInputStream fis = null;
        BufferedOutputStream bos = null;

        try {
            // 파일 다운로드 설정
            response.setContentType("application/pdf");

            response.setHeader("Content-Transper-Encoding", "binary");
            response.setHeader("Content-Disposition", "attachment; filename=" + strInFile);

            String path = pdfPath;

            fis = new FileInputStream(path + fileName + ".pdf");
            int size = fis.available(); //지정 파일에서 읽을 수 있는 바이트 수를 반환
            byte[] buf = new byte[size]; //버퍼설정
            int readCount = fis.read(buf);
            response.flushBuffer();
            bos = new BufferedOutputStream(response.getOutputStream());
            bos.write(buf, 0, readCount);
            bos.flush();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            try {
                if (fis != null) fis.close(); //close는 꼭! 반드시!
                if (bos != null) bos.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }

        mav.setViewName("org/notiMgmt/notiInq");

        return mav;
    }

    /*
     * 	카드취소 위한 수납내역 조회
     * */
    @RequestMapping("selectRcpMas")
    public ModelAndView selectNotiMas(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        LOGGER.debug("기관 {} 카드취소 위한 수납내역 조회", user);
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            if (!request.getParameter("cancelRcpMasCd").equals("")) {
                reqMap.put("rcpMasCd", request.getParameter("cancelRcpMasCd"));
                map.put("rcpMasCd", request.getParameter("cancelRcpMasCd"));
            } else {
                map.put("notiMasCd", request.getParameter("cancelNotiMasCd"));
                map.put("sveCd", "OCD");
                ReceiptMgmtDTO rcpCd = receiptMgmtService.getRcpCd(map);
                reqMap.put("rcpMasCd", rcpCd.getRcpMasCd());
                map.put("rcpMasCd", rcpCd.getRcpMasCd());
                LOGGER.debug("rcpCd.getRcpMasCd() >>>>>> " + rcpCd.getRcpMasCd());
            }
            // 고지내역 리스트 조회
            ReceiptMgmtDTO result = receiptMgmtService.selectRcpMas(reqMap);


            map.put("result", result);
            map.put("retCode", "0000");

            LOGGER.debug("map >>>>>> " + map);


        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        mav.addObject("map", map);
        mav.setViewName("org/receiptMgnt/cancelRequest");

        return mav;
    }

    /*
     *	카드취소
     * */
    @RequestMapping("goCancel")
    public ModelAndView runPay(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        LOGGER.debug("기관 {} 카드취소", user);
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();

        // 결재 instance 생성
        NicePayWebConnector connector = new NicePayWebConnector();
        // 로그 디렉토리 설정
        String root = request.getSession().getServletContext().getRealPath("/");
        //String logPath = "D:/suhlee/shinhanDamoa/src/main/resources/properties/";   // 모듈 WEB-INF Path 설정
        //String logPath = "C:/workspace/shinhanDamoa/src/main/resources/properties/";   // 모듈 WEB-INF Path 설정
        String logPath = root + "WEB-INF/classes/properties/";

        try {
            LOGGER.debug(request.getParameter("CancelPwd"));
            request.setCharacterEncoding("utf-8");
            connector.setNicePayHome(logPath);                                  // 로그 디렉토리 생성
            // 요청 파라미터 설정
            connector.setRequestData(request);                                  // 요청 페이지 파라메터 셋팅
            // 추가 파라미터 설정
            connector.addRequestData("actionType", "CL0");                      // 서비스모드 설정(결제 서비스 : PY0 , 취소 서비스 : CL0)
            connector.addRequestData("MallIP", request.getRemoteAddr());        // 상점 고유 ip
            connector.addRequestData("CancelPwd", request.getParameter("CancelPwd"));                    // 취소 비밀번호
            connector.requestAction();

			/*
            *******************************************************
			* <취소 결과 필드>
			*******************************************************
			*/
            boolean cancelSuccess = false;
            if (connector.getResultData("ResultCode").equals("2001"))
                cancelSuccess = true;    // 결과코드 (취소성공: 2001, 취소성공(LGU 계좌이체):2211)
            //if(connector.getResultData("ResultCode").equals("2211")) cancelSuccess = true;  // 결과코드 (취소성공: 2001, 취소성공(LGU 계좌이체):2211)

            map.put("resultCode", connector.getResultData("ResultCode"));       // 결과코드 (정상 결과코드:3001)
            map.put("resultMsg", connector.getResultData("ResultMsg"));            // 결과메시지

            if (cancelSuccess) {
                map.put("pgServiceId", request.getParameter("MID"));
                map.put("payDay", request.getParameter("payDay"));
                map.put("bnkMsgNo", request.getParameter("bnkMsgNo"));

                LOGGER.debug("map====" + map);

                //카드취소대상 원장코드 조회
                ReceiptMgmtDTO data = receiptMgmtService.getCancelRcp(map);
                map.put("rcpMasCd", data.getRcpMasCd());
                map.put("notiMasCd", data.getNotiMasCd());
                map.put("chaCd", data.getChaCd());

                int upRcpMas = receiptMgmtService.updateRcpMas(map);
                if (upRcpMas > 0) {
                    receiptMgmtService.updateRcpDet(map);
                    receiptMgmtService.updateNotiBill(map);
                }

				/*
                *******************************************************
				* <취소 결과 필드>
				*******************************************************
				*/
                map.put("cancelAmt", connector.getResultData("CancelAmt"));        // 취소금액
                map.put("cancelDate", connector.getResultData("CancelDate"));      // 취소일
                map.put("cancelTime", connector.getResultData("CancelTime"));      // 취소시간
                map.put("cancelNum", connector.getResultData("CancelNum"));        // 취소번호
                map.put("payMethod", connector.getResultData("PayMethod"));        // 취소 결제수단
                map.put("mid", connector.getResultData("MID"));                    // 상점 ID
                map.put("tid", connector.getResultData("TID"));                    // TID
                map.put("authDate", connector.getResultData("AuthDate"));          // 거래시간

            } else {
                map.put("errorCD", connector.getResultData("ErrorCD"));            // 상세 에러코드
                map.put("errorMsg", connector.getResultData("ErrorMsg"));          // 상세 에러메시지
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        mav.addObject("map", map);
        mav.setViewName("org/receiptMgnt/cancelResult");

        return mav;
    }

    /**
     * 현장수납등록
     *
     * @param body
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("actualReceiptSaveAjax")
    @ResponseBody
    public Map<String, Object> actualReceiptSaveAjax(@RequestBody HashMap<String, List<ActualRegDTO>> body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        LOGGER.debug("actualReceiptSaveAjax >>> ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 현장수납등록", user);
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            for (ActualRegDTO obj : body.get("rec")) {
                NotiDTO dto = new NotiDTO();

                obj.setChaCd(user);
                map.put("chaCd", user);
                map.put("sveCd", obj.getSveCd());
                map.put("afterCd", obj.getAfterCd());
                map.put("notiMasCd", obj.getNotiMasCd());
                //map.put("rcpMasCd", obj.getRcpMasCd());
                map.put("beforeCd", obj.getBeforeCd());
                map.put("dcsAmt", obj.getDcsAmt().equals("") ? "0" : obj.getDcsAmt());
                map.put("dcdAmt", obj.getDcdAmt().equals("") ? "0" : obj.getDcdAmt());

                if (!map.get("dcsAmt").equals("0")) {
                    // 현금영수증 사전 데이터 셋팅
                    NotiDTO notiDto = receiptMgmtService.getCashData(obj.getVano());

                    dto.setVano(obj.getVano());
                    dto.setSt(obj.getAfterCd());
                    dto.setNotimascd(obj.getNotiMasCd());
                    dto.setChacd(user);
                    dto.setRcpAmt(Integer.parseInt(map.get("dcsAmt").toString()));
                    dto.setNoTaxYn(notiDto.getNoTaxYn());
                    dto.setChaoffno(notiDto.getChaoffno());
                    dto.setAmtChkTy(notiDto.getAmtChkTy());
                    dto.setRcpReqTy("M"); //현금영수증자동발급여부(A:자동, M:수동)
                    dto.setChatrty(notiDto.getChatrty());
                    dto.setCusOffNo(notiDto.getCusOffNo()); //XCUSMAS 현금영수증발행고객정보 (핸드폰 번호)XNOTIMAS CUSOFFNO
                    dto.setCusType(notiDto.getCusType()); // 발급용도 1:소득공제 2:지출증빙
                    dto.setConfirm(notiDto.getConfirm());    //인증번호유형 11: 휴대폰번호, 12현금영수증카드번호, 13:주민번호, 21:사업자번호
                }

                if (receiptMgmtService.updateAct(map, dto) == false) {
                    map.put("retCode", "9999");
                    map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
                } else {
                    map.put("retCode", "0000");
                    map.put("retMsg", "정상");
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 직접수납관리 ajax
     *
     * @param body
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("actualReceiptRegAjax")
    @ResponseBody
    public Map<String, Object> getActualReceiptRegAjax(@RequestBody ReceiptMgmtDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int count = 0;
        long totAmt = 0;
        long sumAmt = 0;
        long unAmt = 0;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 직접수납관리 ajax", user);
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<String> notiStList = new ArrayList<String>();
        try {
            map.put("chaCd", user);
            map.put("cusGubn", body.getCusGubn());
            if (!StrUtil.nullToVoid(body.getCusGubnValue()).equals("")) {
                map.put("cusGubnValue", body.getCusGubnValue().replaceAll(",", "|").trim());
            }
            if (!StrUtil.nullToVoid(body.getSearchValue()).equals("")) {
                map.put("searchValue", body.getSearchValue().replaceAll(",", "|").trim());
            }
            map.put("searchGb", body.getSearchGb());
            map.put("masMonth", body.getMasMonth());
            LOGGER.debug("body.getGubn() " + body.getGubn());
            if (body.getGubn() == 1) { // 수납
                notiStList.add("PA03");
                notiStList.add("PA05");
                map.put("notiStList", notiStList);
                HashMap<String, Object> totValue = receiptMgmtService.getActCount(map);

                if (totValue != null) {
                    count = NumberUtils.toInt(totValue.get("cnt").toString(), 0);
                    totAmt = NumberUtils.toLong(totValue.get("totamt").toString(), 0L);
                    sumAmt = NumberUtils.toLong(totValue.get("sumamt").toString(), 0L);
                    unAmt = NumberUtils.toLong(totValue.get("unamt").toString(), 0L);
                }

                PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
                int start = page.getPageBegin();
                int end = page.getPageEnd();

                map.put("start", start);
                map.put("end", end);
                map.put("orderBy", body.getOrderBy());
                map.put("pageGubn", "D");

                LOGGER.debug("test111 >>>>>>>>>> " + map);

                List<ReceiptMgmtDTO> list = receiptMgmtService.getActList(map);
                map.put("list", list);
                map.put("count", count);
                map.put("totAmt", totAmt);
                map.put("sumAmt", sumAmt);
                map.put("unAmt", unAmt);
                map.put("pager", page);
                map.put("PAGE_SCALE", body.getPageScale());

            } else { // 미납
                notiStList.add("PA02");
                notiStList.add("PA04");
                map.put("notiStList", notiStList);
                HashMap<String, Object> totValue = receiptMgmtService.getActCount(map);

                if (totValue != null) {
                    count = NumberUtils.toInt(totValue.get("cnt").toString(), 0);
                    totAmt = NumberUtils.toLong(totValue.get("totamt").toString(), 0L);
                    sumAmt = NumberUtils.toLong(totValue.get("sumamt").toString(), 0L);
                    unAmt = NumberUtils.toLong(totValue.get("unamt").toString(), 0L);
                }

                PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
                int start = page.getPageBegin();
                int end = page.getPageEnd();

                map.put("start", start);
                map.put("end", end);
                map.put("orderBy", body.getOrderBy());
                map.put("pageGubn", "D");

                LOGGER.debug("test0000 >>>>>>>>>> " + map);

                List<ReceiptMgmtDTO> list = receiptMgmtService.getActList(map);
                map.put("list", list);
                map.put("count", count);
                map.put("totAmt", totAmt);
                map.put("sumAmt", sumAmt);
                map.put("unAmt", unAmt);
                map.put("pager", page);
                map.put("PAGE_SCALE", body.getPageScale());
            }

            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 입급내역 상세조회
     *
     * @param rcpMasCd 입금내역 아이디
     * @return
     */
    @RequestMapping("paymentDetailsAjax")
    @ResponseBody
    public Map<String, Object> getPaymentDetailsAjax(@RequestParam(value = "rcpMasCd") String rcpMasCd) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        LOGGER.debug("기관 {} 입급내역 상세조회", user);
        LOGGER.info("수납내역 상세조회: {}", rcpMasCd);

        final ReceiptMgmtDTO customer = receiptMgmtService.getCustomerByRcpMasCd(rcpMasCd);
        final Map<String, Object> noticeMaster = receiptMgmtService.getNoticeMasterByRcpMasCd(rcpMasCd);
        final List<Map<String, Object>> noticeDetailsList = receiptMgmtService.getNoticeDetailsByRcpMasCd(rcpMasCd);

        final Map<String, Object> map = new HashMap<>();
        if(customer.getVano() != null){
            customer.setVano(customer.getVano().substring(0, 3) + "-" + customer.getVano().substring(3, 6) + "-" + customer.getVano().substring(6));
        }

        map.put("customer", customer);
        map.put("noticeMaster", noticeMaster);
        map.put("noticeDetailsList", noticeDetailsList);

        return map;
    }

    /**
     * 직접수납관리 - 현장 수납 등록
     *
     * @param curPage
     * @param PAGE_SCALE
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    @Deprecated
    @RequestMapping("actualReceiptReg")
    public ModelAndView getActualReceiptReg(@RequestParam(defaultValue = "1") int curPage,
                                            @RequestParam(defaultValue = "10") int PAGE_SCALE,
                                            @RequestParam(defaultValue = "1") int start,
                                            @RequestParam(defaultValue = "10") int end
    ) throws Exception {
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        StrUtil strUtil = new StrUtil();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 직접수납관리 - 현장 수납 등록", user);
        NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(user);
        List<String> notiStList = new ArrayList<String>();
        notiStList.add("PA02");
        notiStList.add("PA04");
        int count = 0;
        long totAmt = 0;
        long sumAmt = 0;
        long unAmt = 0;
        map.put("startDate", "");
        map.put("endDate", "");
        map.put("notiStList", notiStList);
        map.put("cusGubnValue", "");
        map.put("cusGubn", "");
        map.put("searchValue", "");
        map.put("masMonth", StrUtil.getCurrentMonthStr());
        map.put("chaCd", user);
        map.put("searchGb", "");
        map.put("dateGb", "M");
        map.put("orderBy", "month");
        LOGGER.debug("getActualReceiptReg >> " + map);

        HashMap<String, Object> totValue = receiptMgmtService.getActCount(map);
        if (totValue != null) {
            count = NumberUtils.toInt(totValue.get("cnt").toString(), 0);
            totAmt = NumberUtils.toLong(totValue.get("totamt").toString(), 0L);
            sumAmt = NumberUtils.toLong(totValue.get("sumamt").toString(), 0L);
            unAmt = NumberUtils.toLong(totValue.get("unamt").toString(), 0L);
        }

        LOGGER.debug("count >> " + count);
        LOGGER.debug("totAmt >> " + totAmt);
        PageVO page = new PageVO(count, curPage, PAGE_SCALE);
        map.put("start", start);
        map.put("end", end);
        map.put("pageGubn", "D");

        List<ReceiptMgmtDTO> list = receiptMgmtService.getActList(map);
        map.put("list", list);
        map.put("count", count);
        map.put("totAmt", totAmt);
        map.put("sumAmt", sumAmt);
        map.put("unAmt", unAmt);
        map.put("pager", page);
        map.put("PAGE_SCALE", PAGE_SCALE);
        mav.addObject("orgSess", baseInfo);
        mav.addObject("map", map);

        LOGGER.debug("map >>> " + map);
        mav.setViewName("org/receiptMgnt/actualReceiptReg");
        return mav;
    }

    /**
     * 환불내역 조회
     *
     * @param body
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("refundReceiptRegAjax")
    @ResponseBody
    public Map<String, Object> getRefundReceiptRegAjax(@RequestBody ReceiptMgmtDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int count = 0;
        long totAmt = 0;
        long sumAmt = 0;
        long unAmt = 0;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 환불내역 조회", user);
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<String> notiStList = new ArrayList<String>();
        try {
            map.put("chaCd", user);
            map.put("cusGubn", body.getCusGubn());
            if (!StrUtil.nullToVoid(body.getCusGubnValue()).equals("")) {
                map.put("cusGubnValue", body.getCusGubnValue().replaceAll(",", "|").trim());
            }
            if (!StrUtil.nullToVoid(body.getSearchValue()).equals("")) {
                map.put("searchValue", body.getSearchValue().replaceAll(",", "|").trim());
            }
            map.put("searchGb", body.getSearchGb());
            map.put("masMonth", body.getMasMonth());
            map.put("amtChkTy", body.getAmtChkTy());
            map.put("rePayYn", body.getRePayYN());
            map.put("orderBy", body.getOrderBy());
            map.put("startDate", body.getStartDate());
            map.put("endDate", body.getEndDate());
            LOGGER.debug("map >>> " + map);

            if (body.getRePayYN().equals("N")) { //환불가능
                HashMap<String, Object> totValue = receiptMgmtService.getReplyNCount(map);

                if (totValue != null) {
                    count = NumberUtils.toInt(totValue.get("cnt").toString(), 0);
                    totAmt = NumberUtils.toLong(totValue.get("totamt").toString(), 0L);
                    sumAmt = NumberUtils.toLong(totValue.get("sumamt").toString(), 0L);
                    unAmt = NumberUtils.toLong(totValue.get("unamt").toString(), 0L);
                }

                PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
                int start = page.getPageBegin();
                int end = page.getPageEnd();

                map.put("start", start);
                map.put("end", end);
                map.put("orderBy", body.getOrderBy());
                map.put("pageGubn", "D");

                List<ReceiptMgmtDTO> list = receiptMgmtService.getReplyNList(map);
                map.put("list", list);

                map.put("count", count);
                map.put("totAmt", totAmt);
                map.put("sumAmt", sumAmt);
                map.put("unAmt", unAmt);
                map.put("pager2", page);
                map.put("PAGE_SCALE", body.getPageScale());

            } else { // 환불완료
                //HashMap<String, Object> totValue = receiptMgmtService.getEndReplyYCount(map);
                HashMap<String, Object> totValue = receiptMgmtService.getEndReplyYCount(map);
                if (totValue != null) {
                    count = Integer.parseInt(totValue.get("cnt").toString());
                    totAmt = Integer.parseInt(totValue.get("totamt").toString());
                }

                PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
                int start = page.getPageBegin();
                int end = page.getPageEnd();

                map.put("start", start);
                map.put("end", end);
                map.put("orderBy", body.getOrderBy());
                map.put("pageGubn", "D");

                List<ReceiptMgmtDTO> list = receiptMgmtService.getEndReplyYList(map);
                map.put("list", list);

                map.put("count", count);
                map.put("totAmt", totAmt);
                map.put("pager2", page);
                map.put("PAGE_SCALE", body.getPageScale());

            }

            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 환불등록
     *
     * @param body
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("actualRepaySaveAjax")
    @ResponseBody
    public Map<String, Object> actualRepaySaveAjax(@RequestBody HashMap<String, List<ActualRegDTO>> body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 환불등록", user);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            LOGGER.debug("actualRepaySaveAjax >>>>" + body.get("rec").size());

            for (ActualRegDTO obj : body.get("rec")) {
                LOGGER.debug("please!---- ");
                // 수납테이블 rcpMasCd 찾기
                String rcpMasCd = receiptMgmtService.getRcpMasCd(obj.getNotiMasCd());

                obj.setChaCd(user);
                map.put("chaCd", user);
                map.put("rePayAmt", obj.getRePayAmt());
                map.put("rcpMasCd", rcpMasCd);
                map.put("notiMasCd", obj.getNotiMasCd());
                LOGGER.debug("map >>>>>>>>> " + map);

                if (receiptMgmtService.insertRepay(map) <= 0) {
                    map.put("retCode", "9999");
                    map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
                } else {
                    map.put("retCode", "0000");
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 직접수납내역(현장수납, 환불내역) 엑셀다운로드
     *
     * @param request
     * @param response
     * @param body
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("actlExcelDown")
    public View actlExcelDown(HttpServletRequest request, HttpServletResponse response, ReceiptMgmtDTO body, Model model) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} 직접수납내역(현장수납, 환불내역) 엑셀다운로드", chaCd);
        List<String> notiStList = new ArrayList<String>();
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("chaCd", chaCd);
            map.put("masMonth", body.getMasMonth());
            map.put("searchGb", body.getfSearchGb());

            if (!StrUtil.nullToVoid(body.getfCusGubnValue()).equals("")) {
                map.put("cusGubnValue", body.getfCusGubnValue().replaceAll(",", "|").trim());
            }
            if (!StrUtil.nullToVoid(body.getfSearchValue()).equals("")) {
                map.put("searchValue", body.getfSearchValue().replaceAll(",", "|").trim());
            }

            map.put("cusGubn", body.getfCusGubn());
            map.put("orderBy", body.getfOrderBy());
            map.put("pageGubn", "E");
            map.put("rePayYn", body.getfGubn());

            LOGGER.debug("actlExcelDown >>  " + map);

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            model.addAttribute("cusGbList", cusGbList);
            model.addAttribute("cusGbCnt", cusGbList.size());
            if (body.getfType().equals("cash")) { // 현장수납
                if (body.getfGubn().equals("0")) { // 미납
                    notiStList.add("PA02");
                    notiStList.add("PA04");
                    map.put("notiStList", notiStList);
                    LOGGER.debug("test > " + map);
                    List<ReceiptMgmtDTO> list = receiptMgmtService.getActList(map);
                    model.addAttribute("list", list);
                    model.addAttribute("type", "PA02");
                } else { // 완납
                    notiStList.add("PA03");
                    notiStList.add("PA05");
                    map.put("notiStList", notiStList);
                    List<ReceiptMgmtDTO> list = receiptMgmtService.getActList(map);
                    model.addAttribute("list", list);
                    model.addAttribute("type", "PA04");
                }
            } else { // 환불내역
                map.put("amtChkTy", body.getfAmtChkTy());
                if (body.getfGubn().equals("N")) { //환불가능
                    List<ReceiptMgmtDTO> list = receiptMgmtService.getReplyNList(map);
                    model.addAttribute("list", list);
                    model.addAttribute("type", "02");
                } else { //환불완료
                    List<ReceiptMgmtDTO> list = receiptMgmtService.getEndReplyYList(map);
                    model.addAttribute("list", list);
                    model.addAttribute("type", "03");
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
        if (body.getfType().equals("cash")) { // 현장수납
            return new ExcelSaveRcpMasReq();
        } else {
            return new ExcelSaveCashMasReq();
        }

    }

    /**
     * 수납관리 > 현금영수증
     */
    @RequestMapping("cashReceipt")
    public ModelAndView getCashReceipt(@RequestParam(value = "curPage", defaultValue = "1") int curPage,
                                       @RequestParam(value = "PAGE_SCALE", defaultValue = "10") int PAGE_SCALE,
                                       @RequestParam(value = "start", defaultValue = "1") int start,
                                       @RequestParam(value = "end", defaultValue = "10") int end,
                                       @RequestParam(value = "sortGb", defaultValue = "RG") String sortGb) throws Exception {
        final Map<String, Object> map = new HashMap<>();
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        LOGGER.debug("기관 {} 수납관리 > 현금영수증", user);
        final ModelAndView mav = new ModelAndView("org/receiptMgnt/cashReceipt");
        try {
            final NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(user);

            int cnt = 0;
            int appCnt = 0;
            long totAmt = 0;

            map.put("startDate", "");
            map.put("endDate", "");
            map.put("sortGb", sortGb);
            map.put("payList", null);
            map.put("svecdList", null);
            map.put("cusGubnValue", "");
            map.put("cusGubn", "");
            map.put("searchValue", "");
            map.put("payItemCd", "");
            map.put("masMonth", StrUtil.getCurrentMonthStr());
            map.put("chaCd", user);
            map.put("searchGb", "");
            map.put("orderBy", "pay");
            map.put("cashMasSt", "ALL");

            final Map<String, Object> totValue = receiptMgmtService.getCashMasCount(map);
            if (totValue != null) {
                cnt = NumberUtils.toInt(totValue.get("cnt").toString(), 0);
                appCnt = NumberUtils.toInt(totValue.get("appcnt").toString(), 0);
                totAmt = NumberUtils.toLong(totValue.get("totamt").toString(), 0L);
            }

            // 청구항목코드
            final List<CodeDTO> claimItemCd = codeService.claimItemCd(baseInfo.getChacd());
            map.put("claimItemCd", claimItemCd);

            final PageVO page = new PageVO(cnt, curPage, PAGE_SCALE);
            map.put("start", start);
            map.put("end", end);
            map.put("pageGubn", "D");
            map.put("count", cnt);
            map.put("appCnt", appCnt);
            map.put("totAmt", totAmt);
            map.put("pager", page);
            map.put("PAGE_SCALE", PAGE_SCALE);

            mav.addObject("orgSess", baseInfo);
            mav.addObject("map", map);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
        return mav;
    }

    /**
     * 수납관리 > 현금영수증 AJAX
     */
    @RequestMapping("cashReceiptAjax")
    @ResponseBody
    public Map<String, Object> getCashReceiptAjax(@RequestBody ReceiptMgmtDTO body) throws Exception {
        final HashMap<String, Object> map = new HashMap<>();
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        LOGGER.debug("기관 {} 수납관리 > 현금영수증 AJAX", user);
        try {
            map.put("selectedDate", body.getSelectedDate());
            map.put("startDate", body.getStartDate());
            map.put("endDate", body.getEndDate());
            map.put("sortGb", body.getSortGb());
            map.put("payList", body.getPayList());
            map.put("cashMasSt", body.getCashMasSt());
            if (!body.getSvecdList().isEmpty()) {
                map.put("svecdList", body.getSvecdList());
            } else {
                map.put("svecdList", null);
            }
            map.put("cusGubn", body.getCusGubn());

            if (body.getCusGubnValue() != null && !"".equals(body.getCusGubnValue())) {
                String value = body.getCusGubnValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("cusGubnValue", valueList);
            }

            if (body.getSearchValue() != null && !"".equals(body.getSearchValue())) {
                String value = body.getSearchValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("searchValue", valueList);
            }

            map.put("payItemCd", body.getPayItemCd());
            map.put("masMonth", body.getMasMonth());
            map.put("chaCd", user);
            map.put("searchGb", body.getSearchGb());
            map.put("orderBy", body.getOrderBy());

            map.put("rcpMasSt", body.getRcpMasSt());

            final HashMap<String, Object> totValue = receiptMgmtService.getCashMasCount(map);
            // 개수
            final int count = Integer.parseInt(totValue.get("cnt").toString());
            // 발행건수
            final int appCnt = Integer.parseInt(totValue.get("appcnt").toString());
            // 입금금액
            final long totPayAmt = Long.parseLong(totValue.get("totpayamt").toString());
            // 발행금액
            final long totAmt = Long.parseLong(totValue.get("totamt").toString());

            final PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
            final int start = page.getPageBegin();
            final int end = page.getPageEnd();

            map.put("start", start);
            map.put("end", end);
            map.put("pageGubn", "D");

            final List<ReceiptMgmtDTO> list = receiptMgmtService.getCashMasList(map);
            map.put("list", list);
            map.put("count", count);
            map.put("appCnt", appCnt);
            map.put("totPayAmt", totPayAmt);
            map.put("totAmt", totAmt);
            map.put("pager", page);
            map.put("PAGE_SCALE", body.getPageScale());
            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 현금영수증 > 발행이력조회
     */
    @RequestMapping("selectCashHistDetail")
    @ResponseBody
    public HashMap<String, Object> selectCashHistDetail(@RequestBody ReceiptMgmtDTO body) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String chaCd = authentication.getName();
            LOGGER.debug("기관 {} 현금영수증 > 발행이력조회", chaCd);

            map.put("chaCd", chaCd);
            if (body.getVano() != null) {
                map.put("vano", StringUtils.replaceAll(body.getVano(), "-", ""));
            } else {
                map.put("vano", body.getVano());
            }
            map.put("cashMasCd", body.getCashMasCd());

            //고객기본정보
            CustReg01DTO cusInfo = custMgmtService.selectDetailCustReg(map);
            String cusName = cusInfo.getCusName();
            String vano = "";
            if (cusInfo.getVano() != null) {
                vano = cusInfo.getVano().substring(0, 3) + "-" + cusInfo.getVano().substring(3, 6) + "-" + cusInfo.getVano().substring(6);
            }
            String cusMail = cusInfo.getCusMail();
            String cusGubn1 = cusInfo.getCusGubn1();
            String cusGubn2 = cusInfo.getCusGubn2();
            String cusGubn3 = cusInfo.getCusGubn3();
            String cusGubn4 = cusInfo.getCusGubn4();
            cusInfo.setCusName(cusName);
            cusInfo.setVano(vano);
            cusInfo.setCusMail(cusMail);
            cusInfo.setCusGubn1(cusGubn1);
            cusInfo.setCusGubn2(cusGubn2);
            cusInfo.setCusGubn3(cusGubn3);
            cusInfo.setCusGubn4(cusGubn4);
            map.put("info", cusInfo);

            //현금영수증 발행이력 리스트
            HashMap<String, Object> fwCashReceiptCount = receiptMgmtService.countCashHistList(map);
            // 페이지 관련 설정
            PageVO page = new PageVO(NumberUtils.toInt(fwCashReceiptCount.get("cnt").toString(), 0), body.getCurPage(), 10);
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            map.put("modalPager", page);
            map.put("modalNo", 1);
            map.put("start", start);
            map.put("end", end);

            //현금영수증 발행이력 리스트
            List<FwCashReceiptMasterDTO> fwCashReceiptMasterList = receiptMgmtService.getCashHistList(map);
            for (FwCashReceiptMasterDTO cashReceiptMasterDTO : fwCashReceiptMasterList) {
                cashReceiptMasterDTO.setResponseMessage(cashReceiptMasterDTO.getResponseCode());
                String formattedDate = cashReceiptMasterDTO.getTxDate().substring(0, 4) + "." + cashReceiptMasterDTO.getTxDate().substring(4, 6) + "." + cashReceiptMasterDTO.getTxDate().substring(6, 8);
                cashReceiptMasterDTO.setTxDate(formattedDate);
            }
            map.put("cashHist", fwCashReceiptMasterList);
            map.put("cashHistCount", fwCashReceiptCount.get("cnt"));

            map.put("retCode", "0000");
            map.put("retMsg", "정상");

        } catch (Exception e) {
            LOGGER.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }


    /**
     * 현금영수증 내역 엑셀다운로드
     *
     * @param request
     * @param response
     * @param body
     * @param model
     * @return
     * @throws Exception
     */
    @Deprecated
    @RequestMapping("cashExcelDown")
    public View cashExcelDown(HttpServletRequest request, HttpServletResponse response, ReceiptMgmtDTO body, Model model) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} 현금영수증 내역 엑셀다운로드", chaCd);
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("chaCd", chaCd);
            map.put("selectedDate", body.getSelectedDate());
            map.put("startDate", body.getStartDate());
            map.put("endDate", body.getEndDate());
            map.put("masMonth", body.getMasMonth());
            map.put("cusGubn", body.getfCusGubn());

            if (!"".equals(body.getfCusGubnValue())) {
                String value = body.getfCusGubnValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("cusGubnValue", valueList);
            }

            if (!"".equals(body.getfSearchValue())) {
                String value = body.getfSearchValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("searchValue", valueList);
            }

            map.put("searchGb", body.getfSearchGb());
            map.put("pageGubn", "E");
            map.put("orderBy", body.getfOrderBy());
            map.put("cashMasSt", body.getfCashMasSt());
            if (!body.getSvecdList().isEmpty()) {
                map.put("svecdList", body.getSvecdList());
            }
            LOGGER.debug("cashExcelDown >>  " + map);

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            model.addAttribute("cusGbList", cusGbList);
            model.addAttribute("cusGbCnt", cusGbList.size());

            List<ReceiptMgmtDTO> list = receiptMgmtService.getCashMasList(map);
            model.addAttribute("list", list);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }

        return new ExcelSaveCashReg();
    }
    
    /**
     * 현금영수증 발행내역 엑셀다운로드
     *
     * @param params
     * @return
     * @throws Exception
     * @author jhjeong@finger.co.kr
     * @modified 2018. 10. 29.
     */
    @RequestMapping(value="cashReceiptExcelDown", method=RequestMethod.GET)
    public DownloadView cashReceiptExcelDown(@RequestParam Map<String, Object> params) throws Exception {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} 현금영수증 발행내역 엑셀다운로드", chaCd);
        params.put("chaCd", chaCd);
        
        params.put("cusGubn", params.get("fCusGubn"));
        if (params.get("fCusGubnValue") != null && !"".equals(params.get("fCusGubnValue"))) {
            String value = params.get("fCusGubnValue").toString().trim();
            String[] valueList = value.split("\\s*,\\s*");
            params.put("cusGubnValue", valueList);
        }
        params.put("searchGb", params.get("fSearchGb"));
        if (params.get("fSearchValue") != null && !"".equals(params.get("fSearchValue"))) {
            String value = params.get("fSearchValue").toString().trim();
            String[] valueList = value.split("\\s*,\\s*");
            params.put("searchValue", valueList);
        }
        params.put("pageGubn", "E");
        params.put("orderBy", params.get("fOrderBy"));
        params.put("cashMasSt", params.get("fCashMasSt"));
        
        if (params.get("svecdList") != null) {
        	params.put("svecdList", org.springframework.util.StringUtils.commaDelimitedListToStringArray(params.get("svecdList").toString()));
        }
        
        // 구분항목
        List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
        params.put("cusGbList", cusGbList);
        params.put("cusGbCnt", cusGbList.size());

        params.put("rcpMasSt", params.get("fRcpMasSt"));
    	
    	String prefixName = "현금영수증내역[%s]_%s.xlsx";
        InputStream inputStream;
        try {
        	inputStream = receiptMgmtService.getCashReceiptHistoryExcel(params);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            inputStream = null;
        }
        
        return new DownloadView(String.format(prefixName, chaCd, YMD_Times.format(new Date())), inputStream);
    }
    
    /**
     * 국세청 양식 현금영수증 발행내역 엑셀다운로드
     * @param params
     * @return
     * @throws Exception
     * @author sajoh@finger.co.kr
     * @modified 2019. 05. 13
     */
    @RequestMapping(value="ntsCashReceiptExcelDown", method=RequestMethod.GET)
    public DownloadView ntsCashReceiptExcelDown(@RequestParam Map<String, Object> params) throws Exception {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("국세청 양식 현금영수증 발행내역 엑셀다운로드: chaCd={}", chaCd);
        
        params.put("chaCd", chaCd);
    	
    	String prefixName = "현금영수증 발행내역[%s]_%s.xlsx";
    	InputStream inputStream;
        try {
         	inputStream = receiptMgmtService.getNtsCashReceiptHistoryExcel(params);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            inputStream = null;
        }
    	
    	return new DownloadView(String.format(prefixName, chaCd, YMD_Times.format(new Date())), inputStream);
    }

    /**
     * 현금영수증 발행
     */
    @RequestMapping("insertCashMas")
    @ResponseBody
    public Map<String, Object> insertCashMas(@RequestBody ReceiptMgmtDTO body) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        LOGGER.debug("기관 {} 현금영수증 발행", user);
        final HashMap<String, Object> map = new HashMap<String, Object>();
        final String sendDateString = new SimpleDateFormat("yyyyMMdd").format(DateUtils.addDays(new Date(), 1));

        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("chaCd", user);
            BankReg01DTO info = bankMgmtService.selectChaListInfo(reqMap);

            NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(user);
            long tax = 0;
            if (baseInfo.getNoTaxYn().equals("Y")) {
                tax = Double.valueOf(Math.round(body.getRcpAmt() / 11.0)).longValue();
            }

            map.put("chaCd", user);
            map.put("job", StringEscapeUtils.escapeHtml4(body.getJob()));
            map.put("cashMasCd", body.getCashMasCd());
            map.put("senddt", sendDateString);
            map.put("rcpAmt", body.getRcpAmt());
            if("Y".equals(info.getMandRcpYn()) && StringUtils.isBlank(body.getCusOffNo())){
                map.put("cusType", "1");
                map.put("cusOffNo", "0100001234");
                map.put("confirm", "11");
            }else{
                map.put("cusType", getCusType("", body.getCusOffNo()));
                map.put("cusOffNo", StringEscapeUtils.escapeHtml4(body.getCusOffNo()));
                map.put("confirm", getConfirm("", body.getCusOffNo()));
            }
            map.put("tax", tax);

            if (receiptMgmtService.insertCashMas(map) <= 0) {
                map.put("retCode", "9999");
                map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
            } else {
                map.put("retCode", "0000");
                map.put("retMsg", "정상");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 현금영수증 재발행
     */
    @RequestMapping("updateCashMas")
    @ResponseBody
    public Map<String, Object> updateCashMas(@RequestBody ReceiptMgmtDTO body) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        LOGGER.debug("기관 {} 현금영수증 재발행", user);
        final HashMap<String, Object> map = new HashMap<String, Object>();
        final String sendDateString = new SimpleDateFormat("yyyyMMdd").format(DateUtils.addDays(new Date(), 1));

        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("chaCd", user);
            BankReg01DTO info = bankMgmtService.selectChaListInfo(reqMap);

            NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(user);
            long tax = 0;
            if (baseInfo.getNoTaxYn().equals("Y")) {
                tax = Double.valueOf(Math.round(body.getRcpAmt() / 11.0)).longValue();
            }

            map.put("chaCd", user);
            map.put("job", body.getJob());
            map.put("tax", tax);
            map.put("rcpAmt2", body.getRcpAmt());
            map.put("senddt", sendDateString);
            map.put("cashMasCd", body.getCashMasCd());

            if("Y".equals(info.getMandRcpYn()) && StringUtils.isBlank(body.getCusOffNo())){
                map.put("cusOffNo2", "0100001234");
                map.put("cusType2", "1");
                map.put("confirm2", "11");
            }else{
                map.put("cusOffNo2", body.getCusOffNo());
                map.put("cusType2", getCusType("", body.getCusOffNo()));
                map.put("confirm2", getConfirm("", body.getCusOffNo()));
            }

            final List<ReceiptMgmtDTO> list = receiptMgmtService.getChaMasCd(map);
            final ReceiptMgmtDTO receiptMgmt = list.get(0);

            map.put("cusType", getCusType("", receiptMgmt.getCusOffNo()));
            map.put("confirm", getConfirm("", receiptMgmt.getCusOffNo()));

            if (receiptMgmtService.updateCashMas(map) <= 0) {
                map.put("retCode", "9999");
                map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
            } else {
                map.put("retCode", "0000");
                map.put("retMsg", "정상");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 발행요청 철회(취소), 발행요청 상태 -> 미발행 상태
     */
    @RequestMapping("doInsertIssueDelete")
    @ResponseBody
    public Map<String, Object> doInsertIssueDelete(@RequestBody ReceiptMgmtDTO body) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        LOGGER.debug("기관 {} 현금영수증 발행요청 철회(취소)", user);
        final HashMap<String, Object> map = new HashMap<String, Object>();
        final String sendDateString = new SimpleDateFormat("yyyyMMdd").format(DateUtils.addDays(new Date(), 1));

        try {
            map.put("chaCd", user);
            map.put("cashMasCd", body.getCashMasCd());
            map.put("senddt", sendDateString);

            if (receiptMgmtService.doInsertIssueDelete(map) <= 0) {
                map.put("retCode", "9999");
                map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
            } else {
                map.put("retCode", "0000");
                map.put("retMsg", "정상");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 취소 ( 현금영수증 발행완료 후 ), 발행 -> 발행 취소 요청 접수
     */
    @RequestMapping("deleteCashMas")
    @ResponseBody
    public Map<String, Object> deleteCashMas(@RequestBody ReceiptMgmtDTO body) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        LOGGER.debug("기관 {} 현금영수증 취소 ( 현금영수증 발행완료 후 )", user);
        final HashMap<String, Object> map = new HashMap<String, Object>();
        final String sendDateString = new SimpleDateFormat("yyyyMMdd").format(DateUtils.addDays(new Date(), 1));

        try {
            map.put("chaCd", user);
            map.put("job", body.getJob());
            map.put("cashMasCd", body.getCashMasCd());
            map.put("senddt", sendDateString);

            final List<ReceiptMgmtDTO> list = receiptMgmtService.getChaMasCd(map);
            final ReceiptMgmtDTO receiptMgmt = list.get(0);

            map.put("cusType", getCusType("", receiptMgmt.getCusOffNo()));
            map.put("confirm", getConfirm("", receiptMgmt.getCusOffNo()));

            if (receiptMgmtService.deleteCashMas(map) <= 0) {
                map.put("retCode", "9999");
                map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
            } else {
                map.put("retCode", "0000");
                map.put("retMsg", "정상");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 재발행요청 철회, 발행 -> 재발행 -> 재발행 요청 철회 -> 발행
     * reInsertIssueDelete
     */
    @RequestMapping("reInsertIssueDelete")
    @ResponseBody
    public Map<String, Object> reInsertIssueDelete(@RequestBody ReceiptMgmtDTO body) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        LOGGER.debug("기관 {} 현금영수증 재발행요청 철회", user);
        final HashMap<String, Object> map = new HashMap<String, Object>();
        final String sendDateString = new SimpleDateFormat("yyyyMMdd").format(DateUtils.addDays(new Date(), 1));

        try {
            map.put("chaCd", user);
            map.put("cashMasCd", body.getCashMasCd());
            map.put("senddt", sendDateString);

            if (receiptMgmtService.reInsertIssueDelete(map) <= 0) {
                map.put("retCode", "9999");
                map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
            } else {
                map.put("retCode", "0000");
                map.put("retMsg", "정상");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 취소요청 철회, 발행 -> 취소 -> 취소요청 철회 -> 발행
     */
    @RequestMapping("noDeleteIssue")
    @ResponseBody
    public Map<String, Object> noDeleteIssue(@RequestBody ReceiptMgmtDTO body) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        LOGGER.debug("기관 {} 현금영수증 취소요청 철회", user);
        final HashMap<String, Object> map = new HashMap<String, Object>();
        final String sendDateString = new SimpleDateFormat("yyyyMMdd").format(DateUtils.addDays(new Date(), 1));

        try {
            map.put("chaCd", user);
            map.put("cashMasCd", body.getCashMasCd());
            map.put("senddt", sendDateString);

            if (receiptMgmtService.noDeleteIssue(map) <= 0) {
                map.put("retCode", "9999");
                map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
            } else {
                map.put("retCode", "0000");
                map.put("retMsg", "정상");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 현금영수증 팝업
     */
    @RequestMapping("getCashMasCd")
    @ResponseBody
    public Map<String, Object> getCashMasCd(@RequestBody ReceiptMgmtDTO body) throws Exception {
        final HashMap<String, Object> map = new HashMap<>();
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        LOGGER.debug("기관 {} 현금영수증 팝업", user);
        try {
            map.put("chaCd", user);
            map.put("cashMasCd", body.getCashMasCd());

            final List<ReceiptMgmtDTO> list = receiptMgmtService.getChaMasCd(map);
            map.put("list", list);

            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 청구항목 상세보기
     *
     * @param body
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getReceiptDetailAjax")
    @ResponseBody
    public Map<String, Object> getReceiptDetailAjax(@RequestBody ReceiptMgmtDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 청구항목 상세보기", user);
        try {
            map.put("chaCd", user);
            map.put("masMonth", body.getMasMonth());
            map.put("notiMasCd", body.getNotiMasCd());

            LOGGER.debug("getReceiptDetailAjax >>> " + map);

            List<ReceiptMgmtDTO> list = receiptMgmtService.getReceiptDetail(map);

            long totalPayAmt = 0;
            long totalRcpAmt = 0;

            for (int i = 0; i < list.size(); i++) {
                totalPayAmt += NumberUtils.toLong(list.get(i).getPayItemAmt(), 0L);
                totalRcpAmt += list.get(i).getRcpAmt();
            }

            map.put("count", list.size());
            map.put("list", list);
            map.put("totalPayAmt", totalPayAmt);
            map.put("totalRcpAmt", totalRcpAmt);
            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 항목별 입금내역
     *
     * @author mljeong@finger.co.kr
     */
    @RequestMapping("payItemList")
    public ModelAndView getPayItemList(@RequestParam(defaultValue = "1") int curPage,
                                       @RequestParam(defaultValue = "10") int PAGE_SCALE,
                                       @RequestParam(defaultValue = "1") int start,
                                       @RequestParam(defaultValue = "10") int end) throws Exception {
        ModelAndView mav = new ModelAndView();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 항목별 입금내역", user);
        NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(user);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("chaCd", user);
        map.put("masMonth", StrUtil.getCurrentMonthStr());
        map.put("start", start);
        map.put("end", end);

        // 입금통장명
        List<CodeDTO> moneyPassbookName = codeService.moneyPassbookName(user);
        // 청구항목코드
        List<CodeDTO> claimItemCd = codeService.claimItemCd(user);
        // 구분항목
        List<CodeDTO> cusGbList = codeService.cusGubnCd(user);
        map.put("cusGbList", cusGbList);
        map.put("cusGbCnt", cusGbList.size());

        HashMap<String, Object> totValue = receiptMgmtService.getPayItemListCnt(map);
        int count = 0;
        long totAmt = 0;
        if (totValue != null) {
            count = Integer.parseInt(totValue.get("cnt").toString());
            totAmt = NumberUtils.toLong(totValue.get("totamt").toString());
        }

        List<PayMgmtDTO> list = receiptMgmtService.getPayItemList(map);

        PageVO page = new PageVO(count, curPage, PAGE_SCALE);
        map.put("pager", page);
        map.put("PAGE_SCALE", PAGE_SCALE);

        map.put("list", list);
        map.put("totalCnt", count);
        map.put("totalAmt", totAmt);
        mav.addObject("orgSess", baseInfo);
        mav.addObject("claimItem", claimItemCd);
        mav.addObject("moneyPassbookName", moneyPassbookName);
        mav.addObject("map", map);
        mav.setViewName("org/receiptMgnt/payItemList");
        return mav;
    }

    /**
     * @author mljeong@finger.co.kr
     */
    @RequestMapping("payItemListAjax")
    @ResponseBody
    public Map<String, Object> getPayItemListAjax(@RequestBody ReceiptMgmtDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 항목별 입금내역 ajax", user);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("chaCd", user);
        map.put("masMonth", body.getMasMonth());
        map.put("startDate", body.getStartDate());
        map.put("endDate", body.getEndDate());
        map.put("searchGb", body.getSearchGb());
        if (body.getSearchValue() != null && !"".equals(body.getSearchValue())) {
            String value = body.getSearchValue().trim();
            String[] valueList = value.split("\\s*,\\s*");
            map.put("searchValue", valueList);
        }
        map.put("svecdList", body.getSvecdList());
        map.put("cusGubn", body.getCusGubn());
        if (body.getCusGubnValue() != null && !"".equals(body.getCusGubnValue())) {
            String value = body.getCusGubnValue().trim();
            String[] valueList = StringEscapeUtils.escapeHtml4(value).split("\\s*,\\s*");
            map.put("cusGubnValue", valueList);
        }
        map.put("payItem", body.getPayItem());
        map.put("adjFiregKey", body.getAdjFiregKey());
        map.put("sortGb", body.getSortGb());

        try {
            HashMap<String, Object> totValue = receiptMgmtService.getPayItemListCnt(map);
            int count = 0;
            long totAmt = 0;
            if (totValue != null) {
                count = Integer.parseInt(totValue.get("cnt").toString());
                totAmt = NumberUtils.toLong(totValue.get("totamt").toString());
            }

            PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();

            map.put("start", start);
            map.put("end", end);
            List<PayMgmtDTO> list = receiptMgmtService.getPayItemList(map);
            for (PayMgmtDTO payMgmtDTO : list) {
                if (payMgmtDTO.getVaNo() != null) {
                    String vano = payMgmtDTO.getVaNo().substring(0, 3) + "-" + payMgmtDTO.getVaNo().substring(3, 6) + "-" + payMgmtDTO.getVaNo().substring(6);
                    payMgmtDTO.setVaNo(vano);
                }
            }
            map.put("list", list);
            map.put("totalCnt", count);
            map.put("totalAmt", totAmt);
            map.put("orderBy", body.getOrderBy());
            map.put("pageGubn", "D");
            map.put("pager", page);
            map.put("PAGE_SCALE", body.getPageScale());

            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 항목별 입금내역 엑셀다운로드
     */
    @Deprecated
    @RequestMapping("payItemExcelDown")
    public View payItemExcelDownload(HttpServletRequest request, HttpServletResponse response, ReceiptMgmtDTO body, Model model) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} 항목별 입금내역 엑셀다운로드",  chaCd);
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("chaCd", chaCd);
            map.put("masMonth", body.getMasMonth());
            map.put("startDate", body.getStartDate());
            map.put("endDate", body.getEndDate());
            map.put("searchGb", body.getfSearchGb());
            if (!"".equals(body.getfSearchValue())) {
                String value = body.getfSearchValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("searchValue", valueList);
            }
            map.put("svecdList", body.getSvecdList());
            map.put("cusGubn", body.getfCusGubn());
            if (!"".equals(body.getfCusGubnValue())) {
                String value = body.getfCusGubnValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("cusGubnValue", valueList);
            }
            map.put("payItem", body.getfPayItem());
            map.put("adjFiregKey", body.getfAdjFiregKey());
            map.put("sortGb", body.getfSortGb());



            NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(chaCd);
            if (baseInfo.getAdjAccYn().equals("Y")) {
                model.addAttribute("adjAccYn", "Y");
            } else {
                model.addAttribute("adjAccYn", "N");
            }

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            model.addAttribute("cusGbList", cusGbList);

            List<PayMgmtDTO> list = receiptMgmtService.getPayItemExcelList(map);
            model.addAttribute("list", list);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
        return new ExcelSavePayItem();
    }
    
    /**
     * 항목별 입금내역 엑셀다운로드 - 신규
     * @param params
     * @return
     * @throws Exception
     * @author jhjeong@finger.co.kr
     * @modified 2018. 10. 29.
     */
    @RequestMapping(value="payitemHistoryExcelDown", method=RequestMethod.GET)
    public DownloadView payitemHistoryExcelDown(@RequestParam Map<String, Object> params) throws Exception {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        params.put("chaCd", chaCd);
        LOGGER.debug("기관 {} 항목별 입금내역 엑셀다운로드",  chaCd);
        if (params.get("fCusGubnValue") != null && !"".equals(params.get("fCusGubnValue"))) {
            String value = params.get("fCusGubnValue").toString().trim();
            String[] valueList = value.split("\\s*,\\s*");
            params.put("cusGubnValue", valueList);
        }
        if (params.get("fSearchValue") != null && !"".equals(params.get("fSearchValue"))) {
            String value = params.get("fSearchValue").toString().trim();
            String[] valueList = value.split("\\s*,\\s*");
            params.put("searchValue", valueList);
        }
        params.put("searchGb", params.get("fSearchGb"));
        params.put("cusGubn", params.get("fCusGubn"));
        params.put("payItem", params.get("fPayItem"));
        params.put("adjFiregKey", params.get("fAdjFiregKey"));
        params.put("sortGb", params.get("fSortGb"));

        NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(chaCd);
        params.put("adjAccYn", baseInfo.getAdjAccYn());
        
        if (params.get("svecdList") != null) {
        	params.put("svecdList", org.springframework.util.StringUtils.commaDelimitedListToStringArray(params.get("svecdList").toString()));
        }
        
        // 구분항목
        List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
        params.put("cusGbList", cusGbList);
        params.put("cusGbCnt", cusGbList.size());
    	
    	String prefixName = "항목별입금내역[%s]_%s.xlsx";
        InputStream inputStream;
        try {
        	inputStream = payMgmtService.getPayItemHistoryExcel(params);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            inputStream = null;
        }
        
        return new DownloadView(String.format(prefixName, chaCd, YMD_Times.format(new Date())), inputStream);
    }

    private String getConfirm(String confirm, String identityNo) {
          if (StringUtils.length(identityNo) == 10 && !identityNo.startsWith("0")) {
            // 사업자 등록번호
            return "21";
        } else if (StringUtils.length(identityNo) >= 16) {
            // 카드 번호
            return "12";
        } else {
            // 전화번호
            return "11";
        }
    }

    private String getCusType(String cusType, String identityNo) {
        if (StringUtils.length(identityNo) == 10 && !identityNo.startsWith("0")) {
            // 사업자 등록번호
            return "2";
        } else {
            return "1";
        }
    }

    /**
     * 과거 입금내역 영수증(CUER), 교육비납입증명서(cert1), 장기요양급여 납부확인서(cert2), 기부금(cert3)  인쇄
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "admPastPayPdfMakeRcpBill", method = RequestMethod.POST)
    public ModelAndView getAdmPdfMakeRcpBill(@RequestParam(value = "billSect", defaultValue = "") String billSect,
                                          @RequestParam(value = "checkListValue", defaultValue = "") ArrayList<String> checkList,
                                          @RequestParam(value = "sChaCd", defaultValue = "") String chaCd,
                                          @RequestParam(value = "sBrowserType") String sBrowserType,
                                          @RequestParam(value = "amtChkTy") String amtChkTy,
                                          HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        String result = "";
        String webResult = "";

        try {
            map.put("billSect", billSect);
            map.put("chaCd", chaCd);
            if (!checkList.isEmpty()) {
                if("N".equals(amtChkTy)){
                    map.put("rcpMasList", checkList);
                }else if("Y".equals(amtChkTy)){
                    map.put("notiMasList", checkList);
                }
            }
            map.put("fileName", chaCd);
            map.put("amtChkTy", amtChkTy);

            String refCd = receiptMgmtService.getXchaoption(chaCd);
            List<PdfReceiptMgmtDTO> list = null;

            if ("cut".equals(billSect)) {
                if("N".equals(amtChkTy)){
                    list = receiptMgmtService.selectAdmBillCutWithoutNotice(map);
                }else {
                    list = receiptMgmtService.selectAdmBillCut(map);
                }
            } else if ("cert1".equals(billSect)) { //교육비 납입증명서
                list = receiptMgmtService.getAdmEduList(map);
            } else if ("cert2".equals(billSect)) { // 장기요양급여 납부확인서
                list = receiptMgmtService.getAdmRcpAccNoList(map);
                for (PdfReceiptMgmtDTO pdfReceiptMgmtDTO : list) {
                    pdfReceiptMgmtDTO.setCheckListValue(checkList);
                    pdfReceiptMgmtDTO.setRoleCheck("adm");
                    pdfReceiptMgmtDTO.setAmtChkTy(amtChkTy);
                }
            } else if ("cert3".equals(billSect)) { // 기부금 영수증
                list = receiptMgmtService.getAdmDntnList(map);
                for (PdfReceiptMgmtDTO pdfReceiptMgmtDTO : list) {
                    pdfReceiptMgmtDTO.setCheckListValue(checkList);
                    pdfReceiptMgmtDTO.setRoleCheck("adm");
                    pdfReceiptMgmtDTO.setAmtChkTy(amtChkTy);
                }
            }

            String path = pdfTempPath;

            if (list.size() <= 0) {
                map.put("retCode", "9999");
                map.put("retMsg", "PDF 다운로드할 항목이 없습니다.");
            } else {
                File desti = new File(path);
                if (!desti.exists()) {
                    desti.mkdirs();
                }

                String strInFile = path + chaCd + ".pdf";
                File file = new File(strInFile);

                if (file.exists()) {
                    file.delete();
                }

                result = rctCreatePdf(request, response, list, strInFile, billSect);
                PdfViewer view = new PdfViewer();

                if ("0000".equals(result)) {
                    if (sBrowserType.equals("ie")) {
                        webResult = view.pdfIEDown(request, response, strInFile, chaCd);
                    } else {
                        webResult = view.webPdfViewer(request, response, strInFile);
                    }
                    if (file.exists()) {
                        file.delete();
                    }
                } else {
                    map.put("retCode", "9999");
                    map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
                }
                map.put("retCode", result);
                map.put("fileName", chaCd);
                map.put("retCode", "0000");
                map.put("retMsg", "정상");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        if (sBrowserType.equals("ie")) {
            return null;
        } else {
            mav.addObject("map", map);
            mav.setViewName("sys/additional/history/pastPaymentHistList");

            return mav;
        }
    }


    /**
     * 현금영수증 발행 / 재발행 시 고객정보에 발행정보 반영
     */
    @RequestMapping("updateCashMasInfo")
    @ResponseBody
    public Map<String, Object> updateCashMasInfo(@RequestBody ReceiptMgmtDTO body) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        final HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            map.put("chaCd", user);
            map.put("cusType2", getCusType("", StringEscapeUtils.escapeHtml4(body.getCusOffNo())));
            map.put("confirm2", getConfirm("", StringEscapeUtils.escapeHtml4(body.getCusOffNo())));
            map.put("cusOffNo2", StringEscapeUtils.escapeHtml4(body.getCusOffNo()));
            map.put("vano", body.getVano());

            if (custMgmtService.updateCashMasInfo(map) <= 0) {
                map.put("retCode", "9999");
                map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
            } else {
                map.put("retCode", "0000");
                map.put("retMsg", "정상");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

}
