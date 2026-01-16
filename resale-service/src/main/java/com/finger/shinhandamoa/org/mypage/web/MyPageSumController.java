package com.finger.shinhandamoa.org.mypage.web;

import com.finger.shinhandamoa.common.*;
import com.finger.shinhandamoa.org.mypage.dto.MyPageSumDTO;
import com.finger.shinhandamoa.org.mypage.dto.PaymentStatisticsDTO;
import com.finger.shinhandamoa.org.mypage.service.MyPageSumService;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtBaseDTO;
import com.finger.shinhandamoa.org.notimgmt.service.NotiMgmtService;
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
import kr.co.finger.shinhandamoa.common.DateUtils;
import kr.co.finger.shinhandamoa.sys.service.ClientSettleService;
import kr.co.finger.shinhandamoa.sys.service.DailyClientSettleDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 마이페이지 다모아 이용료 조회
 *
 * @author BY LSH
 * @author wisehouse@finger.co.kr
 * @date 2018. 05. 24.
 */
@Controller
@Slf4j
@RequestMapping("org/myPage/**")
public class MyPageSumController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyPageSumController.class);

    @Inject
    private MyPageSumService myPageSumService;

    @Inject
    private NotiMgmtService notiMgmtService;

    @Autowired
    private ClientSettleService clientSettleService;

    // pdftemp 디렉토리
    @Value("${file.path.pdfTemp}")
    private String pdfTempPath;

    /**
     * 마이페이지 이용료 내역
     *
     * @param curPage
     * @param PAGE_SCALE
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    @RequestMapping("monthSum")
    public ModelAndView getMonthSum(@RequestParam(defaultValue = "1") int curPage,
                                    @RequestParam(defaultValue = "10") int PAGE_SCALE,
                                    @RequestParam(defaultValue = "1") int start,
                                    @RequestParam(defaultValue = "10") int end) throws Exception {
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();

        LOGGER.debug("기관 {}  마이페이지 이용료 내역", user);

        int count = 0;
        map.put("chaCd", user);
        map.put("nowMonth", StrUtil.getCalMonthStr(0));
        map.put("startDate", StrUtil.getCalMonthStr(-1));
        map.put("endDate", StrUtil.getCalMonthStr(-1));

        MyPageSumDTO vo = myPageSumService.getXmonthState(map);

        LOGGER.info("monthSum vo >>> " + vo);

        map.put("month", StrUtil.getCalMonthStr(-1));
        map.put("startDate", StrUtil.getCalMonthStr(-12));
        map.put("endDate", StrUtil.getCalMonthStr(-1));

        HashMap<String, Object> totValue = myPageSumService.getXmonthSumCount(map);
        if (totValue != null && totValue.get("CNT") != null) {
            count = Integer.parseInt(totValue.get("CNT").toString());
        }

        PageVO page = new PageVO(count, curPage, PAGE_SCALE);
        map.put("start", start);
        map.put("end", end);

        List<MyPageSumDTO> list = myPageSumService.getXmonthSum(map);
        if (vo != null) {
            map.put("month", StrUtil.getCalMonthStr(-1));
            map.put("sumFee", vo.getSumFee());
            map.put("notiCnt", vo.getNotiCnt());
            map.put("notiFee", vo.getNotiFee());
            map.put("rcpCnt", vo.getRcpCnt());
            map.put("rcpFee", vo.getRcpFee());
            map.put("smsCnt", vo.getSmsCnt());
            map.put("lmsCnt", vo.getLmsCnt());
            map.put("atCnt", vo.getAtCnt());
            map.put("atFee", vo.getAtFee());
            map.put("prnCnt", vo.getPrnCnt());
            map.put("prnFee", vo.getPrnFee());
        }

        List<MyPageSumDTO> atmList = myPageSumService.getXmonthSumInfo(map);

        map.put("list", list);
        map.put("count", count);
        map.put("atmList", atmList);
        map.put("atmCount", atmList.size());
        map.put("pager", page);    // 페이징 처리를 위한 변수
        map.put("PAGE_SCALE", PAGE_SCALE);
        LOGGER.info("map >>> " + map);
        mav.addObject("map", map);
        mav.setViewName("org/myPage/monthSum");
        return mav;

    }

    /**
     * 마이페이지 이용료 내역 AJAX
     *
     * @param body
     * @return
     * @throws Exception
     */
    @RequestMapping("monthSumListAjax")
    @ResponseBody
    public HashMap<String, Object> getMonthSumListAjax(@RequestBody MyPageSumDTO body) throws Exception {
        final HashMap<String, Object> map = new HashMap<>();

        try {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            final String user = authentication.getName();
            LOGGER.debug("기관 {}  마이페이지 이용료 내역", user);
            int count = 0;

            map.put("chaCd", user);
            map.put("nowMonth", StrUtil.getCalMonthStr(0));
            // 이번달 이용 현황
            final MyPageSumDTO vo = myPageSumService.getXmonthState(map);

            map.put("month", StrUtil.getCalMonthStr(-1));
            if(CmmnUtils.validateDateFormat(body.getEndDate()) && CmmnUtils.validateDateFormat(body.getStartDate())){
                map.put("startDate", body.getStartDate());
                map.put("endDate", body.getEndDate());
            } else {
                map.put("startDate", "");
                map.put("endDate", "");
            }

            // 이용료 조회 (건수)
            final HashMap<String, Object> totValue = myPageSumService.getXmonthSumCount(map);
            if (totValue != null && totValue.get("cnt") != null) {
                count = Integer.parseInt(totValue.get("cnt").toString());
            }

            final PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
            map.put("start", page.getPageBegin());
            map.put("end", page.getPageEnd());

            // 이용료 조회 (데이터)
            final List<MyPageSumDTO> list = myPageSumService.getXmonthSum(map);

            if (vo != null) {
                map.put("notiCnt", vo.getNotiCnt());
                map.put("notiFee", vo.getNotiFee());
                map.put("rcpCnt", vo.getRcpCnt());
                map.put("rcpFee", vo.getRcpFee());
                map.put("smsCnt", vo.getSmsCnt());
                map.put("lmsCnt", vo.getLmsCnt());
                map.put("sumFee", vo.getSumFee());
                map.put("atCnt", vo.getAtCnt());
                map.put("atFee", vo.getAtFee());
            }

            map.put("list", list);
            map.put("count", count);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("PAGE_SCALE", body.getPageScale());
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
     * 마이페이지 일별 이용료 내역
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings("null")
    @RequestMapping("daySum")
    public ModelAndView getDaySum(@RequestParam(value = "setMonth", defaultValue = "") String setMonth) throws Exception {
        LOGGER.info("setMonth " + setMonth);
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        LOGGER.debug("기관 {}  마이페이지 이용료 내역", user);
        final Map<String, Object> map = new HashMap<>();
        final ModelAndView mav = new ModelAndView();
        try {
            final Date firstDate = DateUtils.parseDate(setMonth, "yyyyMM", new Date());
            final Date lastDate = DateUtils.getLastMonthDate(firstDate);
            final ListResult<DailyClientSettleDTO> listResult = clientSettleService.getDailySettleList(user, null, firstDate, lastDate, "settleDate_asc", new PageBounds(1, 100));

            final List<HashMap<String, Object>> mList = new ArrayList<>();
            for (DailyClientSettleDTO each : listResult.getItemList()) {
                final HashMap<String, Object> hMap = new HashMap<>();
                hMap.put("objSumDay", each.getSettleDate());
                hMap.put("notiCnt", each.getNoticeCount());
                hMap.put("notiFee", each.getNoticeFee());
                hMap.put("rcpCnt", each.getVaPaymentCount());
                hMap.put("rcpFee", each.getVaPaymentFee());
                hMap.put("smsCnt", each.getSmsCount());
                hMap.put("smsFee", each.getSmsFee());
                hMap.put("lmsCnt", each.getLmsCount());
                hMap.put("lmsFee", each.getLmsFee());
                hMap.put("lmsCnt", each.getLmsCount());
                hMap.put("lmsFee", each.getLmsFee());
                hMap.put("atCnt", each.getAtCount());
                hMap.put("atFee", each.getAtFee());
                hMap.put("prnCnt", each.getPrnCount());
                hMap.put("prnFee", each.getPrnFee());
                hMap.put("totFee", each.getNoticeFee() + each.getVaPaymentFee() + each.getSmsFee() + each.getLmsFee() + each.getPrnFee() + each.getAtFee());

                mList.add(hMap);
            }

            map.put("list", mList);
            map.put("count", mList.size());

            mav.addObject("map", map);
            mav.addObject("itemList", listResult.getItemList());
            mav.setViewName("org/myPage/daySum");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

        }

        return mav;

    }

    /**
     * 이용료 영수증 pdf 생성
     *
     * @param month
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "pdfMakeSumRcpBill*", method = RequestMethod.POST)
    public ModelAndView getPdfMakeSumRcpBill(@RequestParam(value = "month", defaultValue = "") String month,
                                             @RequestParam(value = "sBrowserType") String sBrowserType,
                                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        String result = "";

        try {
            LOGGER.debug("기관 {} 이용료 영수증 pdf 생성", user);
            String sFileName = "S_" + user + "_" + StrUtil.getCurrentDateStr();
            map.put("chaCd", user);
            map.put("month", month);
            map.put("fileName", sFileName);
            MyPageSumDTO vo = myPageSumService.getRcpBill(map);

            String path = pdfTempPath;

            if (vo == null) {
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

                result = sumRcpCreatePdf(request, response, vo, strInFile, "");
                PdfViewer view = new PdfViewer();

                if ("0000".equals(result)) {
                    if (sBrowserType.equals("ie")) {
                        view.pdfIEDown(request, response, strInFile, sFileName);
                    } else {
                        view.webPdfViewer(request, response, strInFile);
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
            LOGGER.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        if (sBrowserType.equals("ie")) {
            return null;
        } else {
            mav.addObject("map", map);
            mav.setViewName("org/myPage/pdfMakeSumRcpBill");
            return mav;
        }

    }

    /**
     * html -> pdf 생성
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String sumRcpCreatePdf(HttpServletRequest request, HttpServletResponse response, MyPageSumDTO vo, String strInFile, String billSect) throws Exception {
        String result = "";

        try {
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
            String logo = rootPath + "assets/imgs/common/finger.png";
            String month = "";
            String feeDt = "";
            final DecimalFormat df = new DecimalFormat("#,##0");
            if (vo.getMonth() != null && vo.getMonth().length() == 6) {
                month = vo.getMonth().substring(0, 4) + "년 " + vo.getMonth().substring(4, 6) + "월";
            }
            if (vo.getFeeDt() != null) {
                feeDt = vo.getFeeDt().substring(0, 4) + "년 " + vo.getFeeDt().substring(4, 6) + "월 " + vo.getFeeDt().substring(6, 8) + "일";
            }
            String htmlStr = "";

            if (vo != null) {
                htmlStr += "<!DOCTYPE html><html>" +
                        "<head>" +
                        "    <title>신한 다모아 수수료 영수증 폼</title>" +
                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" +
                        "</head>" +
                        "<body style=\"font-family: MalgunGothic; width:21cm;padding:14px 17px;\">" +
                        "    <p style=\"font-size: 20px;height: 27px;margin-top:100px;margin-bottom: 20px;text-align: center; font-weight:bold;\">" +
                        "        신한 다모아 입금수수료 영수증" +
                        "    </p>" +
                        "    <table style=\"width:100%;\">" +
                        "        <tr>" +
                        "            <td style=\"width:20%\"></td>" +
                        "            <td style=\"width:60%\">" +
                        "                <table style=\"border-top:2px solid rgb(30,32,34); border-bottom:1px solid rgb(186,190,212); font-size:12pt; width:100%;\">" +
                        "                    <tr>" +
                        "                        <th style=\"padding:15px 20px; border-right:1px solid rgb(186,190,212); border-bottom:1px solid rgb(186,190,212)\">업체명</th>" +
                        "                        <td style=\"padding:15px 20px; border-bottom:1px solid rgb(186,190,212)\">" + vo.getChaName() + "</td>" +
                        "                    </tr>" +
                        "                    <tr>" +
                        "                        <th style=\"padding:15px 20px; border-right:1px solid rgb(186,190,212); border-bottom:1px solid rgb(186,190,212)\">이용월</th>" +
                        "                        <td style=\"padding:15px 20px; border-bottom:1px solid rgb(186,190,212)\">" + month + "</td>" +
                        "                    </tr>" +
                        "                    <tr>" +
                        "                        <th style=\"padding:15px 20px; border-right:1px solid rgb(186,190,212); border-bottom:1px solid rgb(186,190,212)\">납부일자</th>" +
                        "                        <td style=\"padding:15px 20px; border-bottom:1px solid rgb(186,190,212)\">" + feeDt + "</td>" +
                        "                    </tr>" +
                        "                    <tr>" +
                        "                        <th style=\"padding:15px 20px; border-right:1px solid rgb(186,190,212); border-bottom:1px solid rgb(186,190,212)\">대상건수</th>" +
                        "                        <td style=\"padding:15px 20px; border-bottom:1px solid rgb(186,190,212)\">" + vo.getRcpCnt() + "건</td>" +
                        "                    </tr>" +
                        "                    <tr>" +
                        "                        <th style=\"padding:15px 20px; border-right:1px solid rgb(186,190,212); border-bottom:1px solid rgb(186,190,212)\">금액</th>" +
                        "						<td style=\"padding:15px 20px; border-bottom:1px solid rgb(186,190,212)\">" + df.format(vo.getRcpFee()) + "원</td>" +
                        "                    </tr>" +
                        "                </table>" +
                        "            </td>" +
                        "            <td style=\"width:20%\"></td>" +
                        "        </tr>" +
                        "    </table>" +
                        "    <p style=\"text-align:center; font-size: 9pt; margin-top:20px;\">다모아 이용료로 상기금액을 영수함.</p>" +
                        "    <p style=\"text-align:center; font-size: 9pt; margin-bottom:40px;\">" + StrUtil.getCurrentDateStrFormat() + "</p>" +
                        "    <p style=\"text-align:center;\">" +
                        "        <img src=\"" + logo + "\" style=\"height:22px;\"/>" +
                        "    </p>" +
                        "</body>" +
                        "</html>";
            }
            LOGGER.info("html >>> " + htmlStr.toString());
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
     * 마이페이지 수납현황분석
     */
    @RequestMapping("rcpState")
    public ModelAndView getRcpState() throws Exception {
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 마이페이지 수납현황분석", user);

        try {
            NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(user);

            map.put("chaCd", user);
            map.put("startDate", StrUtil.getCalMonthDateStr(-1));
            map.put("endDate", StrUtil.getCurrentDateStr());
            List<MyPageSumDTO> list = myPageSumService.getDayRcpState(map);

            if (list.size() > 0) {
                MyPageSumDTO vo = myPageSumService.getDayRcpTotal(map);
                map.put("vasCnt", vo.getVasCnt());
                map.put("vasAmt", vo.getVasAmt());
                map.put("dcsCnt", vo.getDcsCnt());
                map.put("dcsAmt", vo.getDcsAmt());
                map.put("ocdCnt", vo.getOcdCnt());
                map.put("ocdAmt", vo.getOcdAmt());
                map.put("dcdCnt", vo.getDcdCnt());
                map.put("dcdAmt", vo.getDcdAmt());
                map.put("cashCnt", vo.getCashCnt());
                map.put("cashAmt", vo.getCashAmt());
                map.put("totCnt", vo.getTotCnt());
                map.put("totAmt", vo.getTotAmt());
            }

            map.put("list", list);
            map.put("count", list.size());
            LOGGER.info("map >>> " + map);
            mav.addObject("map", map);
            mav.addObject("orgSess", baseInfo);
            mav.setViewName("org/myPage/rcpState");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
        return mav;
    }

    /**
     * 수납현황분석 데이터
     *
     * @param body
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("rcpStateAjax")
    @ResponseBody
    public HashMap<String, Object> getRcpStateAjax(@RequestBody MyPageSumDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String user = authentication.getName();
            LOGGER.debug("기관 {} 수납현황분석 데이터", user);
            map.put("chaCd", user);
            map.put("startDate", body.getStartDate());
            map.put("endDate", body.getEndDate());

            NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(user);

            List<MyPageSumDTO> list = null;

            if (body.getGubn().equals("day")) {
                list = myPageSumService.getDayRcpState(map);

                if (list.size() > 0) {
                    MyPageSumDTO vo = myPageSumService.getDayRcpTotal(map);
                    map.put("vasCnt", vo.getVasCnt());
                    map.put("vasAmt", vo.getVasAmt());
                    map.put("dcsCnt", vo.getDcsCnt());
                    map.put("dcsAmt", vo.getDcsAmt());
                    map.put("ocdCnt", vo.getOcdCnt());
                    map.put("ocdAmt", vo.getOcdAmt());
                    map.put("dcdCnt", vo.getDcdCnt());
                    map.put("dcdAmt", vo.getDcdAmt());
                    map.put("cashCnt", vo.getCashCnt());
                    map.put("cashAmt", vo.getCashAmt());
                    map.put("totCnt", vo.getTotCnt());
                    map.put("totAmt", vo.getTotAmt());
                }
            } else {
                list = myPageSumService.getMonthRcpState(map);

                if (list.size() > 0) {
                    //수납현황 토탈
                    MyPageSumDTO totVo = myPageSumService.getMonthRcpStateTotal(map);
                    map.put("vasCnt", totVo.getVasCnt());
                    map.put("vasAmt", totVo.getVasAmt());
                    map.put("dcsCnt", totVo.getDcsCnt());
                    map.put("dcsAmt", totVo.getDcsAmt());
                    map.put("ocdCnt", totVo.getOcdCnt());
                    map.put("ocdAmt", totVo.getOcdAmt());
                    map.put("dcdCnt", totVo.getDcdCnt());
                    map.put("dcdAmt", totVo.getDcdAmt());
                    map.put("cashCnt", totVo.getCashCnt());
                    map.put("cashAmt", totVo.getCashAmt());
                    map.put("totCnt", totVo.getTotCnt());
                    map.put("totAmt", totVo.getTotAmt());
                }

                // 청구 미납 차트
                if (list.size() > 0) {
//					List<MyPageSumDTO> cList = myPageSumService.getXnotiChart(map);
//					map.put("cList", cList);
//					logger.info("cList > " + cList);
                    // 월별 차트
                    List<MyPageSumDTO> mList = myPageSumService.getMonthChart(map);
                    map.put("mList", mList);
                    // 수납방법 차트
                    MyPageSumDTO payVo = myPageSumService.getPayMethodChart(map);
                    map.put("pstVas", payVo.getPstVas());
                    map.put("pstDcs", payVo.getPstDcs());
                    map.put("pstOcd", payVo.getPstOcd());
                    map.put("pstDcd", payVo.getPstDcd());
                }
            }
            map.put("list", list);
            map.put("count", list.size());
            map.put("orgSess", baseInfo);
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
     * 수납현황분석 다운로드
     *
     * @param request
     * @param response
     * @param body
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("rcpExcelDown")
    public View rcpExcelDown(HttpServletRequest request, HttpServletResponse response, MyPageSumDTO body, Model model) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        try {
            LOGGER.debug("기관 {} 수납현황분석 다운로드", chaCd);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("chaCd", chaCd);
            map.put("startDate", body.getfStartDate());
            map.put("endDate", body.getfEndDate());

            List<MyPageSumDTO> list = null;

            if (body.getGubn().equals("day")) {
                list = myPageSumService.getDayRcpState(map);

                MyPageSumDTO vo = myPageSumService.getDayRcpTotal(map);

                model.addAttribute("list", list);
                model.addAttribute("vo", vo);
            } else {
                list = myPageSumService.getMonthRcpState(map);
                model.addAttribute("list", list);

                MyPageSumDTO totVo = myPageSumService.getMonthRcpStateTotal(map);
                model.addAttribute("vo", totVo);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
        if (body.getGubn().equals("day")) {
            return new ExcelSaveRcpSumReq();
        } else {
            return new ExcelSaveMonthRcpSumReq();
        }
    }

    /**
     * 일별 수납현황 상세보기
     *
     * @param body
     * @return
     * @throws Exception
     */
    @RequestMapping("getRcpDetail")
    @ResponseBody
    public HashMap<String, Object> getRcpDetail(@RequestBody MyPageSumDTO body) throws Exception {
        final HashMap<String, Object> map = new HashMap<>();

        try {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            final String user = authentication.getName();
            LOGGER.debug("기관 {} 일별 수납현황 상세보기", user);
            final int count = 0;
            final String sveCd = "";
            map.put("chaCd", user);
            map.put("payDay", body.getPayDay());
            //수납방법코드, VAS-가상계좌,DCS-창구현금(현장),DCD-창구카드(현장),DVA-기관계좌입금,OCD-인터넷카드
            map.put("sveCd", body.getGubn());
            LOGGER.info("map >>>>> " + map);
            final List<PaymentStatisticsDTO> list = myPageSumService.getRcpDetail(map);
            for (PaymentStatisticsDTO each : list) {
                each.setBankName(StrUtil.bankNameChanger(StringUtils.defaultString(each.getKftcCode())));
            }

            map.put("list", list);
            map.put("count", list.size());
            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }


}
