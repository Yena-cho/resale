package com.finger.shinhandamoa.org.notimgmt.web;

import com.finger.shinhandamoa.common.CmmnUtils;
import com.finger.shinhandamoa.common.PdfViewer;
import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.org.notimgmt.dto.*;
import com.finger.shinhandamoa.org.notimgmt.service.NotiConfigService;
import com.finger.shinhandamoa.org.notimgmt.service.NotiMgmtPrintService;
import com.finger.shinhandamoa.org.notimgmt.service.NotiMgmtService;
import com.finger.shinhandamoa.util.dao.LayOutDAO;
import com.finger.shinhandamoa.util.dto.LayOutDTO;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 고지관리 컨트롤러
 *
 * @author LSH
 * @author wisehouse@finger.co.kr
 */
@Controller
@Slf4j
@RequestMapping("org/notiMgmt/**")
public class NotiMgmtController {

    private static final Logger logger = LoggerFactory.getLogger(NotiMgmtController.class);

    @Inject
    private NotiMgmtService notiMgmtService;

    @Inject
    private NotiConfigService notiConfigService;

    @Inject
    private NotiMgmtPrintService notiMgmtPrintService;

    // 업로드 디렉토리
    @Value("${file.path.logo}")
    private String uploadPath;

    //pdf  디렉토리
    @Value("${file.path.pdf}")
    private String pdfPath;

    // pdftemp 디렉토리
    @Value("${file.path.pdfTemp}")
    private String pdfTempPath;

    // 로고 업로드 디렉토리
    @Value("${file.path.logo}")
    private String logoPath;

    @Inject
    private LayOutDAO layOutDao;

    /**
     * 고지관리 > 고지서조회/출력 화면 진입
     * BY LSH
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("notiInq")
    public ModelAndView getNotiInq(HttpServletRequest request, @RequestParam(defaultValue = "1") int curPage,
                                   @RequestParam(defaultValue = "10") int PAGE_SCALE,
                                   @RequestParam(defaultValue = "1") int start,
                                   @RequestParam(defaultValue = "10") int end) throws Exception {

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        try {
            int count = 0;
            long totAmt = 0;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String role = authentication.getAuthorities().toString();
            String strRole = role.replace("[", "").replace("]", "");    // 권한 String
            String user = authentication.getName();
            logger.debug("기관 {} 고지관리 > 고지서조회/출력 화면 진입", user);
            NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(user);

            NotiConfigDTO settInfo = notiConfigService.selectXbillForm(baseInfo.getChacd());

            reqMap.put("masMonth", StrUtil.getCurrentMonthStr());
            reqMap.put("chaCd", baseInfo.getChacd());
            reqMap.put("payGb1", "ALL");
            reqMap.put("cusGubn", "all");
            reqMap.put("cusGubnValue", "");
            reqMap.put("rcpDtDupYn", baseInfo.getRcpDtDupYn());

            // pdf 파일생성된 경우 보여주기
            String path = pdfPath;

            String fileName = baseInfo.getChacd() + baseInfo.getBilliMgty();
            String strInFile = path + fileName + ".pdf";
            File file = new File(strInFile);
            if (!file.exists()) {
                fileName = "";
            }
            //MASCNT, TOTAMT
            HashMap<String, Object> totValue = notiMgmtService.getNotiMgmtTotalCount(reqMap);
            if (totValue != null) {
                count = NumberUtils.toInt(String.valueOf(totValue.get("mascnt")));
                totAmt = NumberUtils.toLong(String.valueOf(totValue.get("totamt")));
            }

            PageVO page = new PageVO(count, curPage, PAGE_SCALE);
            reqMap.put("start", start);
            reqMap.put("end", end);

            List<NotiMgmtResDTO> list = notiMgmtService.getNotiMgmtList(reqMap);

            map.put("list", list);
            map.put("pdfFile", fileName);
            map.put("count", count);
            map.put("totAmt", totAmt);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("PAGE_SCALE", PAGE_SCALE);
            map.put("settInfo", settInfo);
            mav.addObject("orgSess", baseInfo);

            map.put("retCode", "0000");

        } catch (Exception e) {
            logger.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        mav.addObject("map", map);
        mav.setViewName("org/notiMgmt/notiInq");

        return mav;
    }

    /**
     * 고지관리 > 고지서조회/출력 조회
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("getNotiIngList")
    @ResponseBody
    // request, response 대신 input , output vo  생성하면 됨(디비생성 이후)
    public HashMap<String, Object> getNotiIngList(@RequestBody NotiMgmtReqDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        HashMap<String, Object> map = new HashMap<String, Object>();
        logger.debug("기관 {} 고지관리 > 고지서조회/출력 조회", chaCd);
        try {
            int count = 0;
            long totAmt = 0;
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("masMonth", body.getMasMonth());
            reqMap.put("chaCd", body.getChaCd());
            reqMap.put("payGb1", body.getPayGb1());
            reqMap.put("rcpDtDupYn", body.getRcpDtDupYn());
            reqMap.put("cusGubn", body.getCusGubn());

            if (body.getCusGubnValue() != null && !"".equals(body.getCusGubnValue())) {
                String value = body.getCusGubnValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                reqMap.put("cusGubnValue", valueList);
            }

            reqMap.put("searchGb", body.getSearchGb());
            if (body.getSearchValue() != null && !"".equals(body.getSearchValue())) {
                String value = body.getSearchValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                reqMap.put("searchValue", valueList);
            }

            reqMap.put("curPage", body.getCurPage());
            reqMap.put("PAGE_SCALE", body.getPageScale());
            reqMap.put("search_orderBy", body.getSearch_orderBy());

            if (!"ALL".equals(body.getPayGb1()) && !body.getPayList().isEmpty()) {
                reqMap.put("payList", body.getPayList());
            }
            Map<String, Object> totValue = notiMgmtService.getNotiMgmtTotalCount(reqMap);
            if (totValue != null) {
                count = NumberUtils.toInt(String.valueOf(totValue.get("mascnt")));
                totAmt = NumberUtils.toLong(String.valueOf(totValue.get("totamt")));
            }

            PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();

            reqMap.put("start", start);
            reqMap.put("end", end);

            List<NotiMgmtResDTO> list = null;
            if (count > 0) {
                list = notiMgmtService.getNotiMgmtList(reqMap);
                for (NotiMgmtResDTO lista : list) {
                    if (lista.getVaNo() != null) {
                        String vano = lista.getVaNo().substring(0, 3) + "-" + lista.getVaNo().substring(3, 6) + "-" + lista.getVaNo().substring(6);
                        lista.setVaNo(vano);
                    }

                    lista.setCusName(StringEscapeUtils.escapeHtml4(lista.getCusName()));
                    lista.setCusGubn1(StringEscapeUtils.escapeHtml4(lista.getCusGubn1()));
                    lista.setCusGubn2(StringEscapeUtils.escapeHtml4(lista.getCusGubn2()));
                    lista.setCusGubn3(StringEscapeUtils.escapeHtml4(lista.getCusGubn3()));
                    lista.setCusGubn4(StringEscapeUtils.escapeHtml4(lista.getCusGubn4()));
                }
            }

            map.put("list", list);
            map.put("pager", page);
            map.put("count", count);
            map.put("totAmt", totAmt);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("PAGE_SCALE", body.getPageScale());
            map.put("retCode", "0000");
            map.put("retMsg", "정상");

        } catch (Exception e) {
            logger.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 고지관리 > 고지서 조회/출력
     * PDF 미리보기
     *
     * @author mljeong@finger.co.kr
     */
    public String notiCreatePdfPreview(HttpServletRequest request, HttpServletResponse response, List<NotiMgmtPdfResDTO> list, String strInFile, String user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        logger.debug("기관 {} 고지관리 > 고지서조회/출력 조회 PDF 미리보기", chaCd);
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
            @SuppressWarnings("static-access")

            // HTML, 폰트 설정
                    String pdfFont = request.getSession().getServletContext().getRealPath("/assets/font/GULIM.TTC");
            XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
            fontProvider.register(pdfFont, "Gulim");
            CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

            PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
            HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
            CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
            XMLWorker worker = new XMLWorker(css, true);
            XMLParser xmlParser = new XMLParser(worker, Charset.forName("UTF-8"));

            String rootPath = request.getSession().getServletContext().getContext("/assets").getRealPath("");
            String logo = rootPath + "assets/imgs/common/logo_pdf.png";
            String logoImg = "";
            String fileNm = user + ".jpg";
            File uploadDir = new File(uploadPath); // 업로드 디렉토리
            if (uploadDir.exists() == false) {
                uploadDir.mkdirs();
            }
            File logoFile = new File(uploadPath + fileNm);
            logger.debug("logoFile >>>>>>>>> " + logoFile);
            if (logoFile.isFile()) {
                logoImg = "<img src=\"" + logoFile + "\" style=\"width:184px; height:25px;\" />";
            } else {
                logoImg = "<div style=\"width:184px; height:25px;\">&nbsp;</div>";
            }

            String mImg = rootPath + "assets/imgs/email/icon-mobile-with-balloon.png";

            String htmlStr = "";
            ArrayList<String> cusGubn = new ArrayList<String>();
            for (int i = 0; i < list.size(); i++) {
                NotiMgmtPdfResDTO pdfDTO = list.get(i);
                String printDate = "";
                String masYear = "";
                String masMonth = "";
                if (pdfDTO.getPrintDate() != null && !pdfDTO.getPrintDate().equals("")) {
                    printDate = StrUtil.dateFormat(pdfDTO.getPrintDate());
                }
                if (!pdfDTO.getMasMonth().equals("")) {
                    masYear = pdfDTO.getMasMonth().substring(0, 4);
                    masMonth = pdfDTO.getMasMonth().substring(4, 6);
                }
                cusGubn = new ArrayList<String>();

                if ("Y".equals(pdfDTO.getCusGubnYn1()) && pdfDTO.getXcusGubn1() != null && pdfDTO.getCusGubn1() != null) {
                    cusGubn.add(StrUtil.nullToVoid(pdfDTO.getXcusGubn1()) + ":" + StrUtil.nullToVoid(pdfDTO.getCusGubn1()));
                }
                if ("Y".equals(pdfDTO.getCusGubnYn2()) && pdfDTO.getXcusGubn2() != null && pdfDTO.getCusGubn2() != null) {
                    cusGubn.add(StrUtil.nullToVoid(pdfDTO.getXcusGubn2()) + ":" + StrUtil.nullToVoid(pdfDTO.getCusGubn2()));
                }
                if ("Y".equals(pdfDTO.getCusGubnYn3()) && pdfDTO.getXcusGubn3() != null && pdfDTO.getCusGubn3() != null) {
                    cusGubn.add(StrUtil.nullToVoid(pdfDTO.getXcusGubn3()) + ":" + StrUtil.nullToVoid(pdfDTO.getCusGubn3()));
                }
                if ("Y".equals(pdfDTO.getCusGubnYn4()) && pdfDTO.getXcusGubn4() != null && pdfDTO.getCusGubn4() != null) {
                    cusGubn.add(StrUtil.nullToVoid(pdfDTO.getXcusGubn4()) + ":" + StrUtil.nullToVoid(pdfDTO.getCusGubn4()));
                }

                String itemName1 = StrUtil.nullToVoid(StringUtils.isEmpty(pdfDTO.getPtrItemName1()) ? pdfDTO.getPayItemName1() : pdfDTO.getPtrItemName1());
                String itemName2 = StrUtil.nullToVoid(StringUtils.isEmpty(pdfDTO.getPtrItemName2()) ? pdfDTO.getPayItemName2() : pdfDTO.getPtrItemName2());
                String itemName3 = StrUtil.nullToVoid(StringUtils.isEmpty(pdfDTO.getPtrItemName3()) ? pdfDTO.getPayItemName3() : pdfDTO.getPtrItemName3());
                String itemName4 = StrUtil.nullToVoid(StringUtils.isEmpty(pdfDTO.getPtrItemName4()) ? pdfDTO.getPayItemName4() : pdfDTO.getPtrItemName4());
                String itemName5 = StrUtil.nullToVoid(StringUtils.isEmpty(pdfDTO.getPtrItemName5()) ? pdfDTO.getPayItemName5() : pdfDTO.getPtrItemName5());
                String itemName6 = StrUtil.nullToVoid(StringUtils.isEmpty(pdfDTO.getPtrItemName6()) ? pdfDTO.getPayItemName6() : pdfDTO.getPtrItemName6());
                String itemName7 = StrUtil.nullToVoid(StringUtils.isEmpty(pdfDTO.getPtrItemName7()) ? pdfDTO.getPayItemName7() : pdfDTO.getPtrItemName7());
                String itemName8 = StrUtil.nullToVoid(StringUtils.isEmpty(pdfDTO.getPtrItemName8()) ? pdfDTO.getPayItemName8() : pdfDTO.getPtrItemName8());
                String itemName9 = StrUtil.nullToVoid(StringUtils.isEmpty(pdfDTO.getPtrItemName9()) ? pdfDTO.getPayItemName9() : pdfDTO.getPtrItemName9());
                String billCont = "";
                billCont = strReplace(pdfDTO.getBillCont1());
                billCont = StringEscapeUtils.escapeXml11(StringEscapeUtils.unescapeHtml4(billCont));

                /**
                 * 모든 고지서 양식 본문에 원신한체 적용하지 않는 것으로 확정됨.
                 * 2018. 06. 29. 신한은행 컴펌.
                 */
                htmlStr += "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" +
                        "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no, initial-scale=1.0\"/>" +
                        "<title>신한 다모아 고지서</title>" +
                        "<style>" +
                        "@font-face{" +
                        "   font-weight:lighter;" +
                        "   font-style: normal;" +
                        "}" +
                        "html {padding:11px 14px 0px 14px;}" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div style=\"margin:0 auto;font-family:'Gulim', sans-serif;\">" +
                        "    <table style=\"text-align:right; margin-bottom:5px; width:100%;\">" +
                        "        <tr>" +
                        "            <td style=\"text-align:left;\">" +
                        "                <img src=\"" + logo + "\" style=\"height:18px;\" />" +
                        "            </td>" +
                        "            <td style=\"text-align:right;\">" + logoImg +
                        "            </td>" +
                        "        </tr>" +
                        "    </table>" +
                        "    <p style=\"font-size:21pt;text-align:center; margin-bottom:15px; margin-top:0px;padding-bottom:0px;\">" +
                        "        <strong>" + StringEscapeUtils.escapeXml11(StringEscapeUtils.unescapeHtml4(StrUtil.nullToVoid(pdfDTO.getBillName()))) + "</strong>" +
                        "    </p>" +
                        "    <div style=\"height:245px; overflow:hidden;\">" +
                        "        <table style=\"width:100%;margin-top:0; margin-bottom:15px; line-height:16px;\">" +
                        "            <tr>" +
                        "                <td style=\"text-align:left; font-size:12pt;\">" +
                        "                    <strong>" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getCusName())) + "</strong>  " + StrUtil.nullToVoid(pdfDTO.getCusAls()) + "" +
                        "                </td>" +
                        "                <td style=\"text-align:right; font-size:9pt;\">";
                for (int k = 0; k < cusGubn.size(); k++) {
                    if (k == 0) {
                        htmlStr += StringEscapeUtils.escapeXml11(cusGubn.get(k));
                    } else {
                        htmlStr += "|" + StringEscapeUtils.escapeXml11(cusGubn.get(k));
                    }
                }
                htmlStr += "</td>" +
                        "            </tr>" +
                        "        </table>" +
                        "        <p style=\"padding-top:0px;margin-top:0; margin-bottom:6px;line-height:20px; font-size:12px;font-weight:lighter; letter-spacing:.05em; -webkit-letter-spacing:.1em;\">";
                if (pdfDTO.getBillCont1() != null && !pdfDTO.getBillCont1().equals("")) {
                    htmlStr += billCont.replaceAll("\n", "<br/>");
                } else {
                    htmlStr += "&nbsp;";
                }
                htmlStr += "</p>" +
                        "    </div>" +
                        "    <p style=\"margin-top:6px; margin-bottom:6px;line-height:23px;text-align:right;font-size:10pt;\">" +
                        "        " + StrUtil.nullToVoid(pdfDTO.getNotiChaName()) + " TEL) " + StrUtil.nullToVoid(pdfDTO.getTelNo()) + "" +
                        "    </p>" +
                        "    <table style=\"width:100%; border-top:2px solid #1e2022;margin-bottom:16px;border-bottom:1px solid #babed4;\">" +
                        "        <tr style=\"font-size:11pt;\">" +
                        "            <td style=\"padding:10px 0 7px;border-bottom:1px solid #e6e6e6;\">" +
                        "                <strong>" + " 납부하실 금액</strong>" +
                        "            </td>" +
                        "            <td style=\"text-align:right; padding:10px 0 7px;border-bottom:1px solid #e6e6e6;\">" +
                        "                <strong>" + StrUtil.strComma(pdfDTO.getSumAmt()) + "원</strong>" +
                        "            </td>" +
                        "        </tr>" +
                        "        <tr style=\"font-size:12pt;\">" +
                        "            <td style=\"padding:9px 0;\">" +
                        "                <strong>납부 계좌 : (신한) " + pdfDTO.getVaNo().substring(0, 3) + "-" + pdfDTO.getVaNo().substring(3, 6) + "-" + pdfDTO.getVaNo().substring(6) + "</strong>" +
                        "            </td>" +
                        "            <td style=\"padding:9px 0; text-align:right;\">" +
                        "                <strong>납부마감일 : " + printDate + " 까지</strong>" +
                        "            </td>" +
                        "        </tr>" +
                        "    </table>" +
                        "    <table style=\"width:100%; border:0; margin-bottom:2px;\" cellpadding=\"0\" cellspacing=\"0\">" +
                        "        <tr>" +
                        "            <td style=\"font-size:10pt;\">" +
                        "                청구서 상세내역" +
                        "            </td>" +
                        "            <td style=\"font-size:7pt; text-align:right; font-weight:lighter;\">" + StrUtil.nullToVoid(pdfDTO.getBillCont2()) + "</td>" +
                        "        </tr>" +
                        "    </table>" +
                        "    <table style=\"width:100%; border-top:2px solid #3779d0; font-size:10pt;\" cellpadding=\"0\" cellspacing=\"0\">" +
                        "        <thead>" +
                        "            <tr>" +
                        "                <th style=\"width:40%;padding:8px;background:#f9f9f9;border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:center;height:33px;\">내용</th>" +
                        "                <th style=\"width:20%;padding:8px;background:#f9f9f9;border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6; text-align:center;height:33px;\">금액(원)</th>" +
                        "                <th style=\"width:40%;padding:8px;background:#f9f9f9;border-bottom:1px solid #e6e6e6; text-align:center;height:33px;\">비고</th>" +
                        "            </tr>" +
                        "        </thead>" +
                        "        <tbody>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:22px;\">&nbsp;&nbsp; " + itemName1 + "</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:22px;\">" + StrUtil.strComma(pdfDTO.getPayItemAmt1()) + " &nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:22px;font-size: 9px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getPtrItemRemark1())) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:22px;\">&nbsp;&nbsp; " + itemName2 + "</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:22px;\">" + StrUtil.strComma(pdfDTO.getPayItemAmt2()) + " &nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:22px;font-size: 9px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getPtrItemRemark2())) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:22px;\">&nbsp;&nbsp; " + itemName3 + "</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:22px;\">" + StrUtil.strComma(pdfDTO.getPayItemAmt3()) + " &nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:22px;font-size: 9px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getPtrItemRemark3())) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:22px;\">&nbsp;&nbsp; " + itemName4 + "</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:22px;\">" + StrUtil.strComma(pdfDTO.getPayItemAmt4()) + " &nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:22px;font-size: 9px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getPtrItemRemark4())) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:22px;\">&nbsp;&nbsp; " + itemName5 + "</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:22px;\">" + StrUtil.strComma(pdfDTO.getPayItemAmt5()) + " &nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:22px;font-size: 9px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getPtrItemRemark5())) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:22px;\">&nbsp;&nbsp; " + itemName6 + "</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:22px;\">" + StrUtil.strComma(pdfDTO.getPayItemAmt6()) + " &nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:22px;font-size: 9px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getPtrItemRemark6())) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:22px;\">&nbsp;&nbsp; " + itemName7 + "</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:22px;\">" + StrUtil.strComma(pdfDTO.getPayItemAmt7()) + " &nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:22px;font-size: 9px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getPtrItemRemark7())) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:22px;\">&nbsp;&nbsp; " + itemName8 + "</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:22px;\">" + StrUtil.strComma(pdfDTO.getPayItemAmt8()) + " &nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:22px;font-size: 9px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getPtrItemRemark8())) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:22px;\">&nbsp;&nbsp; " + itemName9 + "</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:22px;\">" + StrUtil.strComma(pdfDTO.getPayItemAmt9()) + " &nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:22px;font-size: 9px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getPtrItemRemark9())) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:22px;\">&nbsp;&nbsp;</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:22px;\">&nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:22px;font-size: 8px;\">&nbsp;&nbsp;</td>" +
                        "            </tr>" +
                        "        </tbody>" +
                        "        <tfoot>" +
                        "            <tr>" +
                        "                <td style=\"background:#f6f9fe;border-top:1px solid #babed4;border-right:1px solid #e6e6e6;border-bottom:1px solid #babed4;padding:5px;text-align:center;font-size:10pt;height:29px;\">합계</td>" +
                        "                <td style=\"background:#f6f9fe;border-top:1px solid #babed4;border-right:1px solid #e6e6e6;border-bottom:1px solid #babed4;padding:5px;text-align:right;font-size:10pt;height:29px;\">" + StrUtil.strComma(pdfDTO.getSumAmt()) + "　</td>" +
                        "                <td style=\"background:#f6f9fe;border-top:1px solid #babed4;border-bottom:1px solid #babed4;padding:5px;font-size:10pt; height:29px;\">&nbsp;</td>" +
                        "            </tr>" +
                        "        </tfoot>" +
                        "    </table>" +
                        "    <div style=\"height:60px; overflow:hidden;\">" +
                        "        <p style=\"padding-top:0px;margin-top:0; margin-bottom:6px;line-height:16px; font-size:10px;font-weight:lighter; letter-spacing:.05em; -webkit-letter-spacing:.1em;\">";
                if (pdfDTO.getRemark() != null && !pdfDTO.getRemark().equals("")) {
                    String remark = "";
                    remark = StringEscapeUtils.escapeXml11(StringEscapeUtils.unescapeHtml4(strReplace(pdfDTO.getRemark())));
                    htmlStr += remark.replaceAll("\n", "<br/>");
                } else {
                    htmlStr += "&nbsp;";
                }
                htmlStr += "</p>" +
                        "    </div>" +
                        "    <table style=\"width:100%;font-size:8px;color:#666;\">" +
                        "        <tr>" +
                        "            <td style=\"height:100px;padding:5px 19px;text-align:center;\">" +
                        "                <img src=\"" + mImg + "\" style=\"height:45px; width:45px;\" />" +
                        "            </td>" +
                        "            <td style=\"padding:5px; font-size:11px;\">" +
                        "                <p style=\"margin-top:0px; margin-bottom:0px;color:#000;\"><strong>[모바일에서도 다~모아!]</strong></p>" +
                        "                <p style=\"margin-top:0px; margin-bottom:0px;\">" +
                        "                    내 납부 정보를 손쉽게 만날 수 있는 '신한 다모아 모바일 웹'을 경험해 보세요." +
                        "                </p>" +
                        "                <p style=\"margin-top:0; margin-bottom:0px;\">" +
                        "                    모바일 브라우저 주소창에 '신한 다모아'로 검색하세요!" +
                        "                </p>" +
                        "            </td>" +
                        "        </tr>" +
                        "    </table>" +
                        "    <div style=\"background:#f4f5f5;color:#666;padding:0px 8px 0px 0px;\">" +
                        "        <table style=\"width:100%; border:0;\">" +
                        "            <tr>" +
                        "                <td style=\"padding:10px; font-size:8pt;\">" +
                        "                    <p style=\"margin-top:0; margin-bottom:1px; color:#000;\">" +
                        "                        <strong>" + StrUtil.nullToVoid(pdfDTO.getNotiChaName()) + "</strong>" +
                        "                    </p>" +
                        "                    <p style=\"margin-top:0; margin-bottom:1px;\">" +
                        "                        주소 : " + StrUtil.nullToVoid(pdfDTO.getAddr()) + "" +
                        "                    </p>" +
                        "                    <p style=\"margin-top:0; margin-bottom:0;\">" +
                        "                        대표번호 : " + StrUtil.nullToVoid(pdfDTO.getOwnerTel()) +
                        "                    </p>" +
                        "                </td>" +
                        "            </tr>" +
                        "        </table>" +
                        "    </div>" +
                        "</div>" +
                        "<br/></body>" +
                        "</html>";
            }
            xmlParser.parse(new StringReader(htmlStr.toString()));
            //pdf 파일이 생성됨
            document.close();

            writer.close();
            result = "0000";
        } catch (Exception e) {
            result = "9999";
            logger.error(e.getMessage());
        }
        return result;
    }

    /**
     * 고지관리 > 고지서 출력의뢰
     * 미리보기
     * <p>
     * html -> pdf 생성
     *
     * @param request
     * @param response
     * @param list
     * @param strInFile
     * @return
     */
    public String notiCreatePdf(HttpServletRequest request, HttpServletResponse response, List<NotiMgmtPdfResDTO> list, String strInFile, String user) {
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
            @SuppressWarnings("static-access")

            // HTML, 폰트 설정
                    String pdfFont = request.getSession().getServletContext().getRealPath("/assets/font/GULIM.TTC");
            XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
            fontProvider.register(pdfFont, "Gulim");
            CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

            PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
            HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
            CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
            XMLWorker worker = new XMLWorker(css, true);
            XMLParser xmlParser = new XMLParser(worker, Charset.forName("UTF-8"));

            String rootPath = request.getSession().getServletContext().getContext("/assets").getRealPath("");
            String logo = rootPath + "assets/imgs/common/logo_pdf.png";
            String logoImg = "";
            String fileNm = user + ".jpg";
            File uploadDir = new File(uploadPath); // 업로드 디렉토리
            if (uploadDir.exists() == false) {
                uploadDir.mkdirs();
            }
            File logoFile = new File(uploadPath + fileNm);
            logger.debug("logoFile >>>>>>>>> " + logoFile);
            if (logoFile.isFile()) {
                logoImg = "<img src=\"" + logoFile + "\" style=\"width:184px; height:25px;\" />";
            } else {
                logoImg = "<div style=\"width:184px; height:25px;\">&nbsp;</div>";
            }

            String mImg = rootPath + "assets/imgs/email/icon-mobile-with-balloon.png";

            String htmlStr = "";
            ArrayList<String> cusGubn = new ArrayList<String>();

            /**
             * 출력건수가 많은 경우 다운로드 time-out
             * 2019.10.11
             */
            int listSize = 0;
            if(list.size() > 1300){
                listSize = 1300;
            }else{
                listSize = list.size();
            }

            for (int i = 0; i < listSize; i++) {
                NotiMgmtPdfResDTO pdfDTO = list.get(i);
                String printDate = "";
                String masYear = "";
                String masMonth = "";
                if (pdfDTO.getPrintDate() != null && !pdfDTO.getPrintDate().equals("")) {
                    printDate = StrUtil.dateFormat(pdfDTO.getPrintDate());
                }
                if (!pdfDTO.getMasMonth().equals("")) {
                    masYear = pdfDTO.getMasMonth().substring(0, 4);
                    masMonth = pdfDTO.getMasMonth().substring(4, 6);
                }

                cusGubn = new ArrayList<String>();

                if (pdfDTO.getCusGubn1() != null) {
                    cusGubn.add(StrUtil.nullToVoid(pdfDTO.getXcusGubn1()) + " : " + StrUtil.nullToVoid(pdfDTO.getCusGubn1()));
                }
                if (pdfDTO.getCusGubn2() != null) {
                    cusGubn.add(StrUtil.nullToVoid(pdfDTO.getXcusGubn2()) + " : " + StrUtil.nullToVoid(pdfDTO.getCusGubn2()));
                }
                if (pdfDTO.getCusGubn3() != null) {
                    cusGubn.add(StrUtil.nullToVoid(pdfDTO.getXcusGubn3()) + " : " + StrUtil.nullToVoid(pdfDTO.getCusGubn3()));
                }
                if (pdfDTO.getCusGubn4() != null) {
                    cusGubn.add(StrUtil.nullToVoid(pdfDTO.getXcusGubn4()) + " : " + StrUtil.nullToVoid(pdfDTO.getCusGubn4()));
                }

                String cusGubnString = "";

                for (int k = 0; k < cusGubn.size(); k++) {
                    if (k == 0) {
                        cusGubnString += StringEscapeUtils.escapeXml11(cusGubn.get(k));
                    } else {
                        cusGubnString += " " + StringEscapeUtils.escapeXml11(cusGubn.get(k));
                    }
                }

                if (cusGubnString.length() > 40) {
                    cusGubnString = cusGubnString.substring(0, 40);
                }

                String itemName1 = StrUtil.nullToVoid(StringUtils.isEmpty(pdfDTO.getPtrItemName1()) ? pdfDTO.getPayItemName1() : pdfDTO.getPtrItemName1());
                String itemName2 = StrUtil.nullToVoid(StringUtils.isEmpty(pdfDTO.getPtrItemName2()) ? pdfDTO.getPayItemName2() : pdfDTO.getPtrItemName2());
                String itemName3 = StrUtil.nullToVoid(StringUtils.isEmpty(pdfDTO.getPtrItemName3()) ? pdfDTO.getPayItemName3() : pdfDTO.getPtrItemName3());
                String itemName4 = StrUtil.nullToVoid(StringUtils.isEmpty(pdfDTO.getPtrItemName4()) ? pdfDTO.getPayItemName4() : pdfDTO.getPtrItemName4());
                String itemName5 = StrUtil.nullToVoid(StringUtils.isEmpty(pdfDTO.getPtrItemName5()) ? pdfDTO.getPayItemName5() : pdfDTO.getPtrItemName5());
                String itemName6 = StrUtil.nullToVoid(StringUtils.isEmpty(pdfDTO.getPtrItemName6()) ? pdfDTO.getPayItemName6() : pdfDTO.getPtrItemName6());
                String itemName7 = StrUtil.nullToVoid(StringUtils.isEmpty(pdfDTO.getPtrItemName7()) ? pdfDTO.getPayItemName7() : pdfDTO.getPtrItemName7());
                String itemName8 = StrUtil.nullToVoid(StringUtils.isEmpty(pdfDTO.getPtrItemName8()) ? pdfDTO.getPayItemName8() : pdfDTO.getPtrItemName8());
                String itemName9 = StrUtil.nullToVoid(StringUtils.isEmpty(pdfDTO.getPtrItemName9()) ? pdfDTO.getPayItemName9() : pdfDTO.getPtrItemName9());
                String billCont = "";
                billCont = strReplace(pdfDTO.getBillCont1());
                billCont = StringEscapeUtils.escapeXml11(StringEscapeUtils.unescapeHtml4(billCont));

                /**
                 * 모든 고지서 양식 본문에 원신한체 적용하지 않는 것으로 확정됨.
                 * 2018. 06. 29. 신한은행 컴펌.
                 */
                htmlStr += "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" +
                        "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no, initial-scale=1.0\"/>" +
                        "<title>신한 다모아 고지서</title>" +
                        "<style>" +
                        "@font-face{" +
                        "   font-weight:lighter;" +
                        "   font-style: normal;" +
                        "}" +
                        "html {padding:11pt 12pt 0px 12pt;}" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div style=\"margin:0 auto; font-family:'Gulim', sans-serif;\">" +
                        "    <table style=\"text-align:right; margin-bottom:7px; width:100%;\">" +
                        "        <tr>" +
                        "            <td style=\"text-align:left;\">" +
                        "                <img src=\"" + logo + "\" style=\"height:18px;\" />" +
                        "            </td>" +
                        "            <td style=\"text-align:right;\">" + logoImg +
                        "            </td>" +
                        "        </tr>" +
                        "    </table>" +
                        "    <p style=\"font-size:13pt;text-align:center; margin-bottom:8px;\">" +
                        "        " + StrUtil.nullToVoid(pdfDTO.getNotiChaName()) + "" +
                        "    </p>" +
                        "    <p style=\"font-size:20pt;text-align:center; margin-bottom:15px; margin-top:0px;padding-bottom:0px;\">" +
                        "        <strong>" + StringEscapeUtils.escapeXml11(StringEscapeUtils.unescapeHtml4(StrUtil.nullToVoid(pdfDTO.getBillName()))) + "</strong>" +
                        "    </p>" +
                        "    <div style=\"height:210px; overflow:hidden;\">" +
                        "        <table style=\"width:100%;margin-top:0; margin-bottom:9px; line-height:16px;\">" +
                        "            <tr>" +
                        "                <td style=\"text-align:left; font-size:12pt;\">" +
                        "                    <strong>" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getCusName())) + "</strong> " + StrUtil.nullToVoid(pdfDTO.getCusAls()) + "" +
                        "                </td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"text-align:right; font-size:9pt;\">" + cusGubnString + "</td>" +
                        "            </tr>" +
                        "        </table>" +
                        "        <p style=\"padding-top:0px;margin-top:0; margin-bottom:6px;line-height:16px; font-size:8.5pt;font-weight:lighter; letter-spacing:-.5pt;\">";
                                if (pdfDTO.getBillCont1() != null && !pdfDTO.getBillCont1().equals("")) {
                                    htmlStr += billCont.replaceAll("\n", "<br/>");
                                } else {
                                    htmlStr += "&nbsp;";
                                }
                        htmlStr += "</p>" +
                        "    </div>" +
                        "    <p style=\"margin-top:6px; margin-bottom:6px;line-height:23px;text-align:right;font-size:11pt;\">" +
                        "        " + StrUtil.nullToVoid(pdfDTO.getNotiChaName()) + " TEL) " + StrUtil.nullToVoid(pdfDTO.getTelNo()) + "" +
                        "    </p>" +
                        "    <table style=\"width:100%; border-top:2px solid #1e2022;margin-bottom:16px;border-bottom:1px solid #babed4;\">" +
                        "        <tr style=\"font-size:16pt;\">" +
                        "            <td style=\"padding:10px 0 7px;border-bottom:1px solid #e6e6e6;\">" +
                        "                <strong>" + masYear + "년 " + masMonth + "월" + " 납부하실 금액</strong>" +
                        "            </td>" +
                        "            <td style=\"text-align:right; padding:10px 0 7px;border-bottom:1px solid #e6e6e6;\">" +
                        "                <strong>" + StrUtil.strComma(pdfDTO.getSumAmt()) + "원</strong>" +
                        "            </td>" +
                        "        </tr>" +
                        "        <tr style=\"font-size:10pt;\">" +
                        "            <td style=\"padding:9px 0;\">" +
                        "                <strong>납부 계좌 : (신한) " + pdfDTO.getVaNo().substring(0, 3) + "-" + pdfDTO.getVaNo().substring(3, 6) + "-" + pdfDTO.getVaNo().substring(6) + "</strong>" +
                        "            </td>" +
                        "            <td style=\"padding:9px 0; text-align:right;\">" +
                        "                <strong>납부마감일 : " + printDate + " 까지</strong>" +
                        "            </td>" +
                        "        </tr>" +
                        "    </table>" +
                        "    <table style=\"width:100%; border:0;\" cellpadding=\"0\" cellspacing=\"0\">" +
                        "        <tr>" +
                        "            <td style=\"font-size:10pt; letter-spacing:-1pt;\">" +
                        "                청구서 상세내역" +
                        "            </td>" +
                        "            <td style=\"font-size:8pt; text-align:right; font-weight:lighter; letter-spacing:-1pt;\">" + StrUtil.nullToVoid(pdfDTO.getBillCont2()) + "</td>" +
                        "        </tr>" +
                        "    </table>" +
                        "    <table style=\"width:100%; border-top:2px solid #3779d0; font-size:8pt;\" cellpadding=\"0\" cellspacing=\"0\">" +
                        "        <thead>" +
                        "            <tr>" +
                        "                <th style=\"width:38%;padding:8px;background:#f9f9f9;border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:center;height:33px;\">내용</th>" +
                        "                <th style=\"width:20%;padding:8px;background:#f9f9f9;border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6; text-align:center;height:33px;\">금액(원)</th>" +
                        "                <th style=\"width:42%;padding:8px;background:#f9f9f9;border-bottom:1px solid #e6e6e6; text-align:center;height:33px;\">비고</th>" +
                        "            </tr>" +
                        "        </thead>" +
                        "        <tbody>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:21px;\">&nbsp;&nbsp; " + itemName1 + "</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:21px;\">" + StrUtil.strComma(pdfDTO.getPayItemAmt1()) + " &nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:21px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getPtrItemRemark1())) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:21px;\">&nbsp;&nbsp; " + itemName2 + "</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:21px;\">" + StrUtil.strComma(pdfDTO.getPayItemAmt2()) + " &nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:21px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getPtrItemRemark2())) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:21px;\">&nbsp;&nbsp; " + itemName3 + "</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:21px;\">" + StrUtil.strComma(pdfDTO.getPayItemAmt3()) + " &nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:21px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getPtrItemRemark3())) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:21px;\">&nbsp;&nbsp; " + itemName4 + "</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:21px;\">" + StrUtil.strComma(pdfDTO.getPayItemAmt4()) + " &nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:21px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getPtrItemRemark4())) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:21px;\">&nbsp;&nbsp; " + itemName5 + "</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:21px;\">" + StrUtil.strComma(pdfDTO.getPayItemAmt5()) + " &nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:21px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getPtrItemRemark5())) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:21px;\">&nbsp;&nbsp; " + itemName6 + "</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:21px;\">" + StrUtil.strComma(pdfDTO.getPayItemAmt6()) + " &nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:21px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getPtrItemRemark6())) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:21px;\">&nbsp;&nbsp; " + itemName7 + "</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:21px;\">" + StrUtil.strComma(pdfDTO.getPayItemAmt7()) + " &nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:21px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getPtrItemRemark7())) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:21px;\">&nbsp;&nbsp; " + itemName8 + "</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:21px;\">" + StrUtil.strComma(pdfDTO.getPayItemAmt8()) + " &nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:21px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getPtrItemRemark8())) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:21px;\">&nbsp;&nbsp; " + itemName9 + "</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:21px;\">" + StrUtil.strComma(pdfDTO.getPayItemAmt9()) + " &nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:21px;\">" + StringEscapeUtils.escapeXml11(StrUtil.nullToVoid(pdfDTO.getPtrItemRemark9())) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:left;padding:3px;height:21px;\">&nbsp;&nbsp;</td>" +
                        "                <td style=\"border-right:1px solid #e6e6e6;border-bottom:1px solid #e6e6e6;text-align:right;padding:3px;height:21px;\">&nbsp;&nbsp;</td>" +
                        "                <td style=\"border-bottom:1px solid #e6e6e6;text-align:center;padding:3px;height:21px;\">&nbsp;&nbsp;</td>" +
                        "            </tr>" +
                        "        </tbody>" +
                        "        <tfoot>" +
                        "            <tr>" +
                        "                <td style=\"background:#f6f9fe;border-top:1px solid #babed4;border-right:1px solid #e6e6e6;border-bottom:1px solid #babed4;padding:5px;text-align:center;font-size:10pt;height:29px;\">합계</td>" +
                        "                <td style=\"background:#f6f9fe;border-top:1px solid #babed4;border-right:1px solid #e6e6e6;border-bottom:1px solid #babed4;padding:5px;text-align:right;font-size:10pt;height:29px;\">" + StrUtil.strComma(pdfDTO.getSumAmt()) + "　</td>" +
                        "                <td style=\"background:#f6f9fe;border-top:1px solid #babed4;border-bottom:1px solid #babed4;padding:5px;font-size:10pt; height:29px;\">&nbsp;</td>" +
                        "            </tr>" +
                        "        </tfoot>" +
                        "    </table>" +
                        "    <div style=\"height:60px; overflow:hidden;\">" +
                        "        <p style=\"padding-top:0px;margin-top:0; margin-bottom:6px;line-height:20px; font-size:8.5pt;font-weight:lighter; letter-spacing:-.5pt;\">";
                if (pdfDTO.getFooter1() != null && !pdfDTO.getFooter1().equals("")) {
                    String footer1 = "";
                    String footer2 = "";
                    String footer3 = "";
                    String remark = "";

                    footer1 = StringEscapeUtils.escapeXml11(StringEscapeUtils.unescapeHtml4(strReplace(pdfDTO.getFooter1())));

                    if (pdfDTO.getFooter2() != null && !pdfDTO.getFooter2().equals("")) {
                        footer2 = StringEscapeUtils.escapeXml11(StringEscapeUtils.unescapeHtml4(strReplace(pdfDTO.getFooter2())));

                        if (pdfDTO.getFooter3() != null && !pdfDTO.getFooter3().equals("")) {
                            footer3 = StringEscapeUtils.escapeXml11(StringEscapeUtils.unescapeHtml4(strReplace(pdfDTO.getFooter3())));
                        } else {
                            footer3 = "";
                        }
                    } else {
                        footer2 = "";
                    }

                    remark = footer1 + "<br/>" + footer2 + "<br/>" + footer3;
                    htmlStr += remark;
                } else {
                    htmlStr += "&nbsp;";
                }
                htmlStr += "</p>" +
                        "    </div>" +
                        "    <table style=\"width:100%;font-size:7px;color:#666;\">" +
                        "        <tr>" +
                        "            <td style=\"height:100px;padding:5px 19px;text-align:center;\">" +
                        "                <img src=\"" + mImg + "\" style=\"height:45px; width:45px;\" />" +
                        "            </td>" +
                        "            <td style=\"padding:5px; font-size:11px;\">" +
                        "                <p style=\"margin-top:0px; margin-bottom:0px;color:#000;\"><strong>[모바일에서도 다~모아!]</strong></p>" +
                        "                <p style=\"margin-top:0px; margin-bottom:0px;\">" +
                        "                    내 납부 정보를 손쉽게 만날 수 있는 '신한 다모아 모바일 웹'을 경험해 보세요." +
                        "                </p>" +
                        "                <p style=\"margin-top:0; margin-bottom:0px;\">" +
                        "                    모바일 브라우저 주소창에 '신한 다모아'로 검색하세요!" +
                        "                </p>" +
                        "            </td>" +
                        "        </tr>" +
                        "    </table>" +
                        "    <div style=\"background:#f4f5f5;color:#666;padding:0px 8px 0px 0px;\">" +
                        "        <table style=\"width:100%; border:0;\">" +
                        "            <tr>" +
                        "                <td style=\"padding:10px; font-size:8pt;\">" +
                        "                    <p style=\"margin-top:0; margin-bottom:1px; color:#000;\">" +
                        "                        <strong>" + StrUtil.nullToVoid(pdfDTO.getNotiChaName()) + "</strong>" +
                        "                    </p>" +
                        "                    <p style=\"margin-top:0; margin-bottom:1px;\">" +
                        "                        주소 : " + StrUtil.nullToVoid(pdfDTO.getAddr()) + "" +
                        "                    </p>" +
                        "                    <p style=\"margin-top:0; margin-bottom:0;\">" +
                        "                        대표번호 : " + StrUtil.nullToVoid(pdfDTO.getOwnerTel()) +
                        "                    </p>" +
                        "                </td>" +
                        "            </tr>" +
                        "        </table>" +
                        "    </div>" +
                        "</div>" +
                        "<br/></body>" +
                        "</html>";
            }
            xmlParser.parse(new StringReader(htmlStr.toString()));
            //pdf 파일이 생성됨
            document.close();

            writer.close();
            result = "0000";
        } catch (Exception e) {
            result = "9999";
            logger.error(e.getMessage());
        }
        return result;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        logger.debug("기관 {} 고지조회 pdf download", chaCd);

        ModelAndView mav = new ModelAndView();
        String strInFile = fileName + ".pdf";
        logger.info("fileName " + fileName);
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
            logger.error(e.getMessage());
        } finally {
            try {
                if (fis != null) fis.close(); //close는 꼭! 반드시!
                if (bos != null) bos.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        mav.setViewName("org/notiMgmt/notiInq");

        return mav;
    }

    /**
     * pdf일괄 생성시 setting(ie가 아닌 기타 브라우저용)
     *
     * @param body
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("regPdfSetting")
    @ResponseBody
    public HashMap<String, Object> regPdfSetting(@RequestBody NotiMgmtPdfReqDTO body,
                                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        logger.debug("기관 {} pdf일괄 생성시 setting", user);
        String result = "";
        try {
            reqMap.put("chaCd", body.getChaCd());
            reqMap.put("billGubn", body.getBillGubn());
            reqMap.put("masMonth", body.getMasMonth());
            if (body.getSearchValue() != null && !"".equals(body.getSearchValue())) {
                String value = body.getSearchValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                reqMap.put("searchValue", valueList);
            }
            if (body.getCusGubnValue() != null && !"".equals(body.getCusGubnValue())) {
                String value = body.getCusGubnValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                reqMap.put("cusGubnValue", valueList);
            }
            reqMap.put("searchGb", body.getSearchGb());
            reqMap.put("cusGubn", body.getCusGubn());
            reqMap.put("search_orderBy", body.getSearch_orderBy());

            List<NotiMgmtPdfResDTO> list = notiMgmtService.getNotiBillList(reqMap);
            if (list.size() <= 0) {
                map.put("retCode", "9999");
                map.put("retMsg", "PDF 다운로드할 항목이 없습니다.");
            } else {
                String fileName = body.getChaCd();

                String path = pdfPath;

                File desti = new File(path);
                if (!desti.exists()) {
                    desti.mkdirs();
                }
                long time = System.currentTimeMillis();
                String strInFile = path + fileName + "_" + time + ".pdf";

                File file = new File(strInFile);

                if (file.exists()) {
                    file.delete();
                }

                result = notiCreatePdf(request, response, list, strInFile, user);

                map.put("pdfRetCode", result);
                map.put("fileName", body.getChaCd() + "_" + time);
                map.put("retCode", "0000");
                map.put("retMsg", "정상");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /**
     * 고지관리 > 고지서설정 화면 진입
     * BY LSH
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("notiConfig")
    public ModelAndView getNotiConfig() throws Exception {

        ModelAndView mav = new ModelAndView();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().toString();
        String strRole = role.replace("[", "").replace("]", "");    // 권한 String
        String user = authentication.getName();
        logger.debug("기관 {} 고지관리 > 고지서설정 화면 진입", user);
        try {
            NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(user);
            NotiConfigDTO settInfo = notiConfigService.selectXbillForm(baseInfo.getChacd());

            String emptybill = "안녕하세요, 고객님\r\n" +
                    "아래 청구 내용 및 금액을 확인하시고 납부마감일까지 입금 해주시기 바랍니다. \r\n" +
                    "은행 방문은 물론 인터넷 및 스마트뱅킹을 포함한 모든 방식으로 신한은행 가상계좌 번호로 납부가 가능합니다.\r\n" +
                    "가상계좌로 입금 시 유의할 점 : \r\n" +
                    "1. 해당 가상계좌는 납부고객별 고유의 입금 계좌번호이므로, 타인과 공유하여 사용하실 수 없습니다. \r\n" +
                    "2. 계좌번호와 금액을 정확히 확인 후 입금해주시기 바랍니다. \r\n" +
                    "3. 납입기한 내 입금을 하셔야 납부가 가능하오니 기한을 준수하여 납부해 주시기 바랍니다.\r\n" +
                    "4. 신한은행을 제외한 타 은행을 이용하여 입금 시 입금 수수료가 발생될 수 있습니다.\r\n" +
                    "\r\n" +
                    "※ 청구내용 및 납부방법 관련 문의 사항이 있으신 고객님께서는 아래로 문의 주시기 바랍니다.";

            if (settInfo == null) {
                settInfo = new NotiConfigDTO();
                settInfo.setBillCont1(emptybill);
            } else if ((settInfo.getBillCont1() == null || settInfo.getBillCont1().length() == 0) && (settInfo.getBillCont2() == null || settInfo.getBillCont2().length() == 0)) {
                settInfo.setBillCont1(emptybill);
            }
            HashMap<String, Object> map = notiConfigService.selectXchalist(baseInfo.getChacd());

            //settInfo.setNotiChaName((String)map.get("CHANAME"));
            String img = logoPath + user + ".jpg";
            mav.addObject("logoImg", img);
            mav.addObject("orgSess", baseInfo);
            mav.addObject("map", settInfo);

            mav.setViewName("org/notiMgmt/notiConfig");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return mav;
    }


    /**
     * 고지관리 > 고지서설정 조회
     * BY LSH
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("getNotiConfigAjax")
    @ResponseBody
    public Map<String, Object> getNotiConfigAjax(HttpServletRequest request, HttpServletResponse response,
                                                 @RequestBody NotiConfigDTO body) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            logger.debug("기관 {} 고지관리 > 고지서설정 조회", user);
            logger.info("chacd " + body.getChaCd() + " billgubn " + body.getBillGubn());
            map.put("chaCd", body.getChaCd());
            map.put("billGubn", body.getBillGubn());
            NotiConfigDTO settInfo = notiConfigService.selectedXbillForm(map);
            if (settInfo == null) {
                settInfo = new NotiConfigDTO();
            } else if ((settInfo.getBillCont1() == null || settInfo.getBillCont1().length() == 0) && (settInfo.getBillCont2() == null || settInfo.getBillCont2().length() == 0)) {
                settInfo = new NotiConfigDTO();
            }
            settInfo.setBillCont1(StringEscapeUtils.escapeHtml4(settInfo.getBillCont1()));
            settInfo.setBillCont2(StringEscapeUtils.escapeHtml4(settInfo.getBillCont2()));
            settInfo.setBillName(StringEscapeUtils.escapeHtml4(settInfo.getBillName()));
            settInfo.setCusAls(StringEscapeUtils.escapeHtml4(settInfo.getCusAls()));
            settInfo.setNotiChaName(StringEscapeUtils.escapeHtml4(settInfo.getNotiChaName()));
            map.put("map", settInfo);
            map.put("retCode", "0000");
            map.put("retMsg", "정상");

        } catch (Exception e) {
            logger.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }


    /**
     * 고지서설정 수정 및 등록
     *
     * @param body
     * @throws Exception
     */
    @RequestMapping("saveNotiConfig")
    @ResponseBody
    public HashMap<String, Object> saveNotiConfig(@RequestBody NotiConfigDTO body) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            logger.debug("기관 {} 고지서설정 수정 및 등록", chaCd);
            if(body.getChaCd().length() > 8 || !StringUtils.isNumeric(body.getChaCd())){
                throw new Exception("올바른 기관코드를 입력해주세요.");
            }
            map.put("chaCd", body.getChaCd());
            if(body.getBillCont1().getBytes().length > 2000){
                throw new Exception("올바른 출력내용을 입력해주세요.");
            }
            map.put("billCont1", StringEscapeUtils.unescapeHtml4(body.getBillCont1()));
            if(body.getBillCont2().getBytes().length > 120 ){
                throw new Exception("올바른 출력내용을 입력해주세요.");
            }
            map.put("billCont2", StringEscapeUtils.unescapeHtml4(body.getBillCont2()));
            if(body.getBillGubn().length() > 2 || !StringUtils.isNumeric(body.getBillGubn())){
                throw new Exception("올바른 양식구분을 입력해주세요.");
            }
            map.put("billGubn", body.getBillGubn());
            if(body.getBillName().getBytes().length > 50 ){
                throw new Exception("올바른 양식명을 입력해주세요.");
            }
            map.put("billName", StringEscapeUtils.unescapeHtml4(body.getBillName()));
            if(body.getCusAls().getBytes().length > 50 ){
                throw new Exception("올바른 별칭을 입력해주세요.");
            }
            map.put("cusAls", StringEscapeUtils.unescapeHtml4(body.getCusAls()));
            if(body.getNotiChaName().getBytes().length > 50 ){
                throw new Exception("올바른 출력기관명을 입력해주세요.");
            }
            map.put("notiChaName", StringEscapeUtils.unescapeHtml4(body.getNotiChaName()));
            if(body.getTelNo().length() > 13 ){
                throw new Exception("올바른 연락처를 입력해주세요.");
            }
            map.put("telNo", body.getTelNo());

            int flag = notiConfigService.saveNotiConfig(map);

            logger.info("flag " + flag);

            if (flag > 0) {
                map.put("retCode", "0000");
                map.put("retMsg", "정상");
            } else {
                map.put("retCode", "9999");
                map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            map.put("retCode", "9999");
            map.put("retMsg", e.getMessage());
        }

        return map;
    }

    /**
     * 고지관리 > 고지서출력의뢰 화면 진입
     * BY LSH
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("notiPrintReq")
    public ModelAndView getNotiPrintReq(@RequestParam(defaultValue = "1") String curPage) throws Exception {

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().toString();
        String strRole = role.replace("[", "").replace("]", "");    // 권한 String
        String user = authentication.getName();
        logger.debug("기관 {} 고지관리 > 고지서출력의뢰 화면 진입", user);
        try {
            NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(user);
            // total count
            int totValue = notiMgmtPrintService.countReqNotiMas(baseInfo.getChacd());
            // 페이지 관련 설정
            PageVO page = new PageVO(totValue, 1, 10);
            int start = page.getPageBegin();
            int end = page.getPageEnd();

            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("chaCd", baseInfo.getChacd());
            reqMap.put("start", start);
            reqMap.put("end", end);
            reqMap.put("masMonth", StrUtil.getCurrentMonthStr());

            // 기본 주소정보
            NotiMgmtPrintResDTO info = notiMgmtPrintService.selectReqAddress(baseInfo.getChacd());
            List<NotiMgmtPrintResDTO> list = notiMgmtPrintService.pageSelectReqNotiMas(reqMap);
            HashMap<String, Object> mResMap = notiMgmtPrintService.selectMonReqNotiMas(reqMap);

            map.put("masCnt", mResMap.get("MASCNT"));
            map.put("pager", page);
            map.put("curpage", "1");
            mav.addObject("map", map);
            mav.addObject("orgSess", baseInfo);
            mav.addObject("info", info);
            mav.addObject("list", list);
            mav.addObject("count", list.size());
            mav.setViewName("org/notiMgmt/notiPrintReq");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return mav;
    }

    @RequestMapping("ajaxMonthPrintReq")
    @ResponseBody
    public HashMap<String, Object> ajaxMonthPrintReq(@RequestBody HashMap<String, Object> body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String user = authentication.getName();
            logger.debug("기관 {} 고지관리 > 고지서출력의뢰 화면 ajaxMonthPrintReq", user);
            map.put("chaCd", user);
            if(CmmnUtils.validateDateFormat(String.valueOf(body.get("masMonth")))){
                map.put("masMonth", body.get("masMonth"));
            }else{
                map.put("masMonth", "");
            }

            HashMap<String, Object> mResMap = notiMgmtPrintService.selectMonReqNotiMas(map);
            map.put("masCnt", mResMap.get("MASCNT"));
            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /**
     * 고지관리 > 고지서출력의뢰 페이지 이동
     * BY LSH
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("agaxGetNotiPrintReq")
    @ResponseBody
    public HashMap<String, Object> getNotiPrintReq2(@RequestBody HashMap<String, Object> body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().toString();
        String strRole = role.replace("[", "").replace("]", "");    // 권한 String
        String user = authentication.getName();

        logger.debug("기관 {} 고지관리 > 고지서출력의뢰 페이지 이동", user);

        try {
            NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(user);
            // total count
            int totValue = notiMgmtPrintService.countReqNotiMas(baseInfo.getChacd());
            // 페이지 관련 설정
            Object o = body.get("curPage");
            PageVO page = new PageVO(totValue, Integer.valueOf(body.get("curPage").toString()), 10);
            int start = page.getPageBegin();
            int end = page.getPageEnd();

            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("chaCd", baseInfo.getChacd());
            reqMap.put("start", start);
            reqMap.put("end", end);
            List<NotiMgmtPrintResDTO> list = notiMgmtPrintService.pageSelectReqNotiMas(reqMap);

            map.put("list", list);
            map.put("count", list.size());
            map.put("pager", page);
            map.put("curpage", body.get("curPage").toString());
            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /**
     * 출력의뢰 요청
     *
     * @param body
     * @return
     * @throws Exception
     */
    @RequestMapping("saveNotiPrintReq")
    @ResponseBody
    @Transactional
    public HashMap<String, Object> saveNotiPrintReq(@RequestBody NotiMgmtPrintResDTO body) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();
        List<NotiMgmtPrintResDTO> list = null;

        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        logger.debug("기관 {} 출력의뢰 요청", chaCd);
        try {
            map.put("chaCd", StringEscapeUtils.escapeHtml4(body.getChaCd()));
            map.put("masMonth", StringEscapeUtils.escapeHtml4(body.getMasMonth()));
            map.put("billGubn", body.getBillGubn());
            map.put("sortTy", body.getSortTy());
            map.put("rcusGubn", body.getRcusGubn());
            map.put("cusGubnA", body.getCusGubnA());
            map.put("cusGubnB", body.getCusGubnB());
            map.put("reqName", body.getReqName());
            map.put("reqHp", StringEscapeUtils.escapeHtml4(body.getReqHp()));
            map.put("zipCode", body.getZipCode());
            map.put("address1", body.getAddress1());
            map.put("address2", StringEscapeUtils.escapeHtml4(body.getAddress2()));
            map.put("curpage", body.getCurpage());
            map.put("sort1Cd", body.getSort1Cd());
            map.put("sort2Cd", body.getSort2Cd());
            map.put("dlvrTypeCd", body.getDlvrTypeCd());

            /**
             * N20001 : 일반배송, BR01 : 요청 | N20002 : 퀵배송, BR03 : 처리중
             */
            if ("N20001".equals(body.getDlvrTypeCd())) {
                map.put("reqSt", "BR01");
            } else {
                map.put("reqSt", "BR03");
            }

            reqMap.put("chaCd", body.getChaCd());
            reqMap.put("start", 1);
            reqMap.put("end", 10);

            //출력건수 확인
            int printCount = notiMgmtPrintService.printCount(map);
            if (printCount > 0) {
                if (notiMgmtPrintService.selectXnotimasReq(map) > 0) {
                    map.put("retCode", "9999");
                    map.put("retMsg", "이미 출력의뢰중인 자료가 있습니다.");
                } else {
                    if (notiMgmtPrintService.saveXnotimasReq(map) > 0) {
                        if(notiMgmtPrintService.saveXnotimasReqDet(map) > 0) {
                            map.put("retCode", "0000");
                            map.put("retMsg", "고지서 출력의뢰 " + printCount + "건을 요청하였습니다.");

                            list = notiMgmtPrintService.pageSelectReqNotiMas(reqMap);
                            map.put("list", list);
                            map.put("count", list.size());
                        } else {
                            map.put("retCode", "9999");
                            map.put("retMsg", "출력 의뢰 작업중에 오류가 발생하였습니다.");
                        }
                    } else {
                        map.put("retCode", "9999");
                        map.put("retMsg", "출력 의뢰 작업중에 오류가 발생하였습니다.");
                    }
                }
            } else {
                map.put("retCode", "8888");
                map.put("retMsg", "고지서 출력 의뢰는 출력 건이 0건 이상인 경우 가능합니다.");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return map;
    }

    /**
     * 출력의뢰 요청취소
     *
     * @param body
     * @return
     * @throws Exception
     */
    @RequestMapping("cancleNotiPrintReq")
    @ResponseBody
    public HashMap<String, Object> cancleNotiPrintReq(@RequestBody NotiMgmtPrintResDTO body) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();
        List<NotiMgmtPrintResDTO> list = null;
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        logger.debug("기관 {} 출력의뢰 요청취소", chaCd);
        try {
            reqMap.put("chaCd", body.getChaCd());
            reqMap.put("start", 1);
            reqMap.put("end", 10);

            if (!body.getCdList().isEmpty()) {
                map.put("chaCd", chaCd);
                map.put("codeList", body.getCdList());
                try {
                    notiMgmtPrintService.deleteXnotimasReq(map);
                    map.put("retCode", "0000");
                    map.put("retMsg", "정상적으로 출력의뢰가 취소되었습니다.");

                    list = notiMgmtPrintService.pageSelectReqNotiMas(reqMap);
                    map.put("list", list);
                    map.put("count", list.size());
                } catch (Exception ex) {
                    map.put("retCode", "0000");
                    map.put("retMsg", "원장번호 " + body.getNotiMasReqCd() + "를삭제할 수 없습니다.");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return map;
    }

    /**
     * 고지문구 조회 9줄이상 넘어가면 수정하기로 유도 (고지내용 row check)
     *
     * @param body
     * @return
     * @throws Exception
     */
    @RequestMapping("cont1LengthCheck")
    @ResponseBody
    public HashMap<String, Object> cont1LengthCheck(@RequestBody NotiConfigDTO body) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        HashMap<String, Object> map = new HashMap<String, Object>();
        logger.debug("기관 {} 고지문구 조회 9줄이상 넘어가면 수정하기로 유도", chaCd);
        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("billGubn", body.getBillGubn());
            reqMap.put("chaCd", body.getChaCd());

            NotiConfigDTO dto = notiMgmtPrintService.cont1LengthCheck(reqMap);
            if (dto == null) {
                dto = new NotiConfigDTO();
                map.put("retCode", "9999");
                map.put("retMsg", "안내문구를 확인 해주세요.");
            } else if ((dto.getBillCont1() == null || dto.getBillCont1().length() == 0) && (dto.getBillCont2() == null || dto.getBillCont2().length() == 0)) {
                dto = new NotiConfigDTO();
                map.put("retCode", "9999");
                map.put("retMsg", "안내문구를 확인 해주세요.");
            } else {
                map.put("retCode", "0000");
                map.put("retMsg", "정상적으로 출력의뢰가 등록되었습니다.");
            }

            map.put("dto", dto);

        } catch (Exception e) {
            map.put("retCode", "9999");
            map.put("retMsg", "출력 의뢰 작업중에 오류가 발생하였습니다.");
        }
        return map;
    }

    /**
     * 고지관리 pdf 팝업 인쇄
     * BY LSH
     *
     * @return
     */
    @RequestMapping(value = "pdfMakeBill*", method = RequestMethod.POST)
    public ModelAndView getPdfMakeBill(Model model, @RequestParam(value = "chaTrTy", defaultValue = "") String chaTrTy,
                                       @RequestParam(value = "billgubnValue", defaultValue = "") String billgubnValue,
                                       @RequestParam(value = "checkListValue", defaultValue = "") ArrayList<String> checkList,
                                       @RequestParam(value = "masMonth", defaultValue = "") String masMonth,
                                       @RequestParam(value = "sFileName", defaultValue = "") String sFileName,
                                       @RequestParam(value = "sSearchValue") String sSearchValue,
                                       @RequestParam(value = "sSearchGb") String sSearchGb,
                                       @RequestParam(value = "sCusGubnValue") String sCusGubnValue,
                                       @RequestParam(value = "sCusGubn") String sCusGubn,
                                       @RequestParam(value = "sBrowserType") String sBrowserType,
                                       @RequestParam(value = "sSearchOrderBy") String sSearchOrderBy,
                                       @RequestParam(value = "pdfStatus") String pdfStatus,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        String result = "";
        String webResult = "";
        StrUtil strUtil = new StrUtil();
        logger.debug("기관 {} 고지관리 pdf 팝업 인쇄", user);
        try {
            map.put("chaCd", user);
            map.put("billGubn", billgubnValue);
            map.put("masMonth", masMonth);

            map.put("searchGb", sSearchGb);
            if (!"".equals(sSearchValue)) {
                String value = sSearchValue.trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("searchValue", valueList);
            }

            map.put("cusGubn", sCusGubn);

            if (!"".equals(sCusGubnValue)) {
                String value = sCusGubnValue.trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("cusGubnValue", valueList);
            }

            map.put("search_orderBy", sSearchOrderBy);
            if (!checkList.isEmpty()) {
                map.put("notiMasList", checkList);
            }
            map.put("fileName", sFileName);
            List<NotiMgmtPdfResDTO> list = notiMgmtService.getNotiBillList(map);

            map.put("list", list);
            if (list.size() <= 0) {
                map.put("retCode", "9999");
                map.put("retMsg", "PDF 다운로드할 항목이 없습니다.");
            } else {
                String path = pdfTempPath;

                File desti = new File(path);
                if (!desti.exists()) {
                    desti.mkdirs();
                }

                String strInFile = path + sFileName + ".pdf";
                File file = new File(strInFile);
                if (file.exists()) {
                    file.delete();
                }

                if ("pdfOK".equals(pdfStatus)) {
                    result = notiCreatePdfPreview(request, response, list, strInFile, user);
                } else {
                    result = notiCreatePdf(request, response, list, strInFile, user);
                }
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
            logger.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        mav.addObject("map", map);
        mav.setViewName("org/notiMgmt/pdfMakeBill");

        if (sBrowserType.equals("ie")) {
            return null;
        } else {
            return mav;
        }
    }

    /**
     * 고지관리 pdf 팝업 인쇄
     *
     * @return
     */
    @RequestMapping(value = "billPdfPreView*", method = RequestMethod.POST)
    public ModelAndView getPdfMakeBill(Model model, @RequestParam(value = "sBrowserType") String sBrowserType,
                                       @RequestParam(value = "sMasMonth") String sMasMonth,
                                       @RequestParam(value = "sNotiMasReqCd") String sNotiMasReqCd,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        String result = "";
        String webResult = "";
        String sFileName = user + sMasMonth;
        StrUtil strUtil = new StrUtil();
        logger.debug("기관 {} 고지관리 pdf 팝업 인쇄", user);

        try {
            map.put("chaCd", user);
            map.put("notiMasReqCd", sNotiMasReqCd);

            List<NotiMgmtPdfResDTO> pdflist = notiMgmtService.getPrivewBillList(map);
            String path = pdfTempPath;

            File desti = new File(path);
            if (!desti.exists()) {
                desti.mkdirs();
            }

            String strInFile = path + sFileName + ".pdf";
            File file = new File(strInFile);
            if (file.exists()) {
                file.delete();
            }

            result = notiCreatePdf(request, response, pdflist, strInFile, user);
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
        } catch (Exception e) {
            logger.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        mav.addObject("map", map);
        mav.setViewName("org/notiMgmt/pdfMakeBill");

        if (sBrowserType.equals("ie")) {
            return null;
        } else {
            return mav;
        }
    }

    public static String strReplace(String str) {
        StringBuffer buf = new StringBuffer();
        int savedpos = 0;
        String oldStr = "<";
        String newStr = "&lt;";
        while (true) {
            int pos = str.indexOf(oldStr, savedpos);
            if (pos != -1) {
                buf.append(str.substring(savedpos, pos));
                buf.append(newStr);

                savedpos = pos + oldStr.length();
                if (savedpos >= str.length())
                    break;
            } else
                break;
        }
        buf.append(str.substring(savedpos, str.length()));
        return buf.toString();
    }
}
