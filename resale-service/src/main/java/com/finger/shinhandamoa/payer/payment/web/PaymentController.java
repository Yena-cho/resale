package com.finger.shinhandamoa.payer.payment.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finger.shinhandamoa.vo.UserDetailsVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.finger.shinhandamoa.common.PdfViewer;
import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.payer.payment.dto.PaymentDTO;
import com.finger.shinhandamoa.payer.payment.service.PaymentService;
import com.finger.shinhandamoa.vo.PageVO;
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

/**
 * @author  by PYS
 * @date    2018. 4. 10.
 * @desc    
 * @version 
 * 
 */

@Controller
@RequestMapping("payer/payment/**")
public class PaymentController {
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
	
	@Autowired
	private PaymentService paymentService;
	
	// pdftemp 디렉토리
	@Value("${file.path.pdfTemp}")
	private String pdfTempPath;
	/*
	 * 	납부내역 리스트 조회
	 * */
	@RequestMapping("payList")
	public ModelAndView payList(@RequestParam(defaultValue = "1") int curPage,
								@RequestParam(defaultValue = "10") int PAGE_SCALE,
								@RequestParam(defaultValue = "1") int start,
								@RequestParam(defaultValue = "10") int end,
								@RequestParam(defaultValue = "") String vaNo,
								@RequestParam(defaultValue = "rcpDt") String search_orderBy,
								@RequestParam(defaultValue = "") String chaCd,
								@RequestParam(defaultValue = "") String cusName) throws Exception {
		
			ModelAndView mav = new ModelAndView();
			HashMap<String, Object> reqMap = new HashMap<String, Object>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			String tmasMonth = "";
			String fmasMonth = "";
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			UserDetailsVO userDetails = (UserDetailsVO)principal;
			String loginId = userDetails.getLoginId();
			String unCus = StringUtils.defaultString(userDetails.getUnCus());
				vaNo = userDetails.getVano();
				chaCd = userDetails.getUsername();
				cusName = userDetails.getName();
		try {
	
			// 가맹점정보 가져오기
			PaymentDTO chaInfo = paymentService.selectChaInfo(vaNo);
			if(!unCus.equals("cus") && unCus != null){
				reqMap.put("noCus", "noCus");
			}
			reqMap.put("vaNo", vaNo);
			reqMap.put("chaCd", chaCd);
			HashMap<String, Object> masmonth = paymentService.payListMaxMonth(reqMap);
			tmasMonth = (String) masmonth.get("MASDAY");
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			Calendar cal = Calendar.getInstance();
			if(tmasMonth.equals("0")) {
				tmasMonth= df.format(cal.getTime());
			}else {
			Date date = df.parse(tmasMonth);
			// 날짜 더하기
			cal.setTime(date);
			cal.add(Calendar.MONTH, -1);
			fmasMonth = df.format(cal.getTime());
			}

			map.put("maxmonth", tmasMonth.substring(4, 6));
			reqMap.put("tmasMonth", tmasMonth);
			reqMap.put("fmasMonth", fmasMonth);
			map.put("tmasMonth", tmasMonth);
			map.put("fmasMonth", fmasMonth);

			// 납부내역 총 개수 조회
			HashMap<String, Object> totValue = paymentService.payListTotalCount(reqMap);

			int count = Integer.parseInt(totValue.get("CNT").toString());
			int totAmt = Integer.parseInt(totValue.get("TOTAMT").toString());
			map.put("count", count);
			map.put("totAmt", totAmt);

			PageVO page = new PageVO(count, curPage, PAGE_SCALE);
			reqMap.put("start", start);
			reqMap.put("end", end);
			reqMap.put("search_orderBy", search_orderBy);
			
			// 납부내역 리스트 조회
			List<PaymentDTO> list = paymentService.payList(reqMap);

			map.put("cusName", cusName);
			map.put("vaNo", vaNo);
			map.put("chaName", chaInfo.getChaName());
			map.put("chaCd", chaCd);
			map.put("usePgYn", chaInfo.getUsePgYn());
			map.put("rcpDtDupYn", chaInfo.getRcpDtDupYn());
			map.put("payList", list); 
			map.put("payCount", list.size());
			map.put("pager", page); 	// 페이징 처리를 위한 변수
			map.put("PAGE_SCALE", PAGE_SCALE);
			map.put("search_orderBy", search_orderBy);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	 
		mav.addObject("map", map);
		mav.setViewName("payer/payment/payList");
		
		return mav;
	}
	
	/*
	 * 	납부내역 리스트 조회(AJAX)
	 * */
	@RequestMapping("payListAjax")
	@ResponseBody
	public HashMap<String, Object> payListAjax(@RequestBody PaymentDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetailsVO userDetails = (UserDetailsVO)principal;
		String unCus = StringUtils.defaultString(userDetails.getUnCus());
		try {
			logger.info("PAGE_SCALE : " + body.getPageScale());
			HashMap<String, Object> reqMap = new HashMap<String, Object>();
			if(!unCus.equals("cus") && unCus != null){
				reqMap.put("noCus", "noCus");
			}
						
			reqMap.put("vaNo", body.getVaNo());
			reqMap.put("chaCd", body.getChaCd());
			reqMap.put("tmasMonth", body.getTmasMonth());
			reqMap.put("fmasMonth", body.getFmasMonth());
			reqMap.put("curPage", body.getCurPage());
			reqMap.put("PAGE_SCALE", body.getPageScale());
			reqMap.put("search_orderBy", body.getSearchOrderBy());
			
			// 납부내역 총 개수 조회
			HashMap<String, Object> totValue = paymentService.payListTotalCount(reqMap);

			int count = Integer.parseInt(totValue.get("CNT").toString());
			
			PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
			int start = page.getPageBegin();
			int end = page.getPageEnd();
			
			reqMap.put("start", start);
			reqMap.put("end", end);

			// 납부내역 리스트 조회
			List<PaymentDTO> list = paymentService.payList(reqMap);
			
			map.put("vaNo", body.getVaNo());
			map.put("chaName", body.getChaName());
			map.put("chaCd", body.getChaCd());
			map.put("payList", list); 
			map.put("count", count);
			map.put("payCount", list.size());
			map.put("tmasMonth", body.getTmasMonth());
			map.put("fmasMonth", body.getFmasMonth());
			map.put("retCode", "0000");
			map.put("retMsg", "정상");
			map.put("pager", page);
			map.put("PAGE_SCALE", body.getPageScale());
			map.put("search_orderBy", body.getSearchOrderBy());
			
		} catch (Exception e) {
			 logger.error(e.getMessage());
			 
			 map.put("retCode", "9999");
			 map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
		}
		
		return map;
	}
	
	/*
	 * 	납부확인증 리스트조회
	 * */
	@RequestMapping("chkPayList")
	public ModelAndView chkPayList(@RequestParam(defaultValue="") String pdfTmasMonth, 
								  @RequestParam(defaultValue="") String pdfFmasMonth,
								  @RequestParam(defaultValue="") String vaNo,
								  @RequestParam(defaultValue="") String chaCd,
								  @RequestParam(defaultValue="") ArrayList<String> checkListValue,
								  @RequestParam(defaultValue="") String sFileName,
								  @RequestParam(value="sBrowserType") String sBrowserType,
								  @RequestParam(value="sSearchOrderBy") String sSearchOrderBy,
								HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> map = new HashMap<String, Object>();
		String result = "";
		String webResult = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetailsVO userDetails = (UserDetailsVO)principal;
		String unCus = StringUtils.defaultString(userDetails.getUnCus());
		try {
			HashMap<String, Object> reqMap = new HashMap<String, Object>();
			if(!unCus.equals("cus") && unCus != null){
				reqMap.put("noCus", "noCus");
			}
			reqMap.put("vaNo", vaNo);
			reqMap.put("chaCd", chaCd);
			reqMap.put("tmasMonth", pdfTmasMonth);
			reqMap.put("fmasMonth", pdfFmasMonth);
			reqMap.put("search_orderBy", sSearchOrderBy);
		
			if(!checkListValue.isEmpty()) {
				reqMap.put("checkListValue", checkListValue);
			}
			
			HashMap<String, Object> totValue = paymentService.payListTotalCount(reqMap);

			int chkCount = Integer.parseInt(totValue.get("CNT").toString());
			int chkTotAmt = Integer.parseInt(totValue.get("TOTAMT").toString());
			
			String path = pdfTempPath;
			
			List<PaymentDTO> list = paymentService.chkPayList(reqMap);
			
			if(list.size() <= 0) {
				map.put("retCode", "9999");
				map.put("retMsg", "PDF 다운로드할 항목이 없습니다.");
			}else {
				File desti = new File(path);
				if(!desti.exists()) {
					desti.mkdirs();
				}
				
				String strInFile = path+sFileName+".pdf";
				File file = new File(strInFile);
				
				if (file.exists()) {
					file.delete();
				}
				
				result = paymentCreatePdf(request, response, list, strInFile, chkTotAmt);
				PdfViewer view = new PdfViewer();
				
				if("0000".equals(result)) {
					if(sBrowserType.equals("ie")) {
						webResult = view.pdfIEDown(request, response, strInFile, sFileName);
					}else {
						webResult = view.webPdfViewer(request, response, strInFile);
					}
					if (file.exists()) {
						file.delete();
					}
				}else {
					map.put("retCode", "9999");
					map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
				}
				
				map.put("result", result);
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
		mav.setViewName("payer/payment/pdfChkPayList");
		return mav;
	}
	
	/**
	 * html -> pdf 생성
	 * @param request
	 * @param response
	 * @param list
	 * @param strInFile
	 * @return
	 */
	public String paymentCreatePdf(HttpServletRequest request, HttpServletResponse response, List<PaymentDTO> list, String strInFile, int chkTotAmt) {
		String result="";
		try {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			UserDetailsVO userDetails = (UserDetailsVO)principal;
			String loginId = userDetails.getLoginId();

			StrUtil strUtil = new StrUtil();
			String currentDate = strUtil.getCurrentDateStr();
			String cusName = request.getParameter("cusName");
			String vaNo = request.getParameter("vaNo");
			String chaName = request.getParameter("chaName");

			//pdf 문서 객체
		    Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		        		
			//pdf 생성 객체
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(strInFile));
			
			document.open();
			XMLWorkerHelper helper = XMLWorkerHelper.getInstance();
			
			// css
			CSSResolver cssResolver = new StyleAttrCSSResolver();
			String pdfCss = request.getSession().getServletContext().getRealPath("/assets/css/pdf-form.min.css");
			CssFile cssFile = helper.getCSS(new FileInputStream(pdfCss));
			cssResolver.addCss(cssFile);
			
			String pdfFont = request.getSession().getServletContext().getRealPath("/assets/font/MALGUN.TTF");
			
			// HTML, 폰트 설정
			XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
			//fontProvider.register("C:/eGovFrame/workspace/projectName/src/main/webapp/font/MALGUN.TTF", "MalgunGothic");
			fontProvider.register(pdfFont, "MalgunGothic");
			CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
			HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
			htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
			
			PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
			HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
			CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
			XMLWorker worker = new XMLWorker(css, true);
			XMLParser xmlParser = new XMLParser(worker, Charset.forName("UTF-8"));
			
			String rootPath = request.getSession().getServletContext().getContext("/assets").getRealPath("") ;
			logger.info("rootPath " + rootPath);
			String htmlStr = "";
			
			htmlStr += "<html>" +
					"<head>" +
					"	<title>납부확인증</title>" +
					"	<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" +
					"</head>" +
					"<body style=\"font-family: MalgunGothic; width:21cm;padding:14px 17px;\">" +
					"	<table class=\"table\">" +
					"		<tr>" +
				    "			<td style=\"width:20%;\"></td>" +
				    "			<td style=\"width:60%; text-align:center;\">" +
				    "				<h3 style=\"font-size:15pt;\">신한DAMOA 납부확인증</h3>"+
				    "			</td>" +
				    "			<td style=\"width:20%;\"></td>" +
				    "		</tr>" +
				    "	</table>" +
				    "	<p style=\"font-size:13pt; font-weight:700; margin-bottom:15px;\">납부자정보</p>" +
				    "	<table class=\"table table-bordered table-outline-bordered-2 table-top-bottom-bordered table-fixed-height table-has-padding\">" +
				    "		<tr>" +
				    "			<th style=\"width:23%;\">납부자명</th>" +
				    "			<td>"+cusName+"</td>" +
				    "		</tr>" +
				    "		<tr>" +
				    "			<th>이용기관명</th>" +
				    "			<td>"+chaName+"</td>" +
				    "		</tr>" +
				    "		<tr>" +
				    "			<th>납부계좌</th>" +
				    "			<td>"+vaNo+"</td>" +
				    "		</tr>" +
				    "	</table>" +	
				    "	<p style=\"font-size:13pt; font-weight:700; margin-bottom:15px;\">납입현황</p>" +
				    "	<table class=\"table table-bordered table-outline-bordered-2 table-top-bottom-bordered table-fixed-height table-has-padding\">" +
			        "   	<tr>" +
			        "       	<th style=\"text-align:center;\">고지년월</th>" +
			        "           <th style=\"text-align:center;\">청구금액(원)</th>" +
			        "           <th style=\"text-align:center;\">납부금액(원)</th>" +
			        "           <th style=\"text-align:center;\">납부일자</th>" +
			        "           <th style=\"text-align:center;\">납부방법</th>" +
			        "           <th style=\"text-align:center;\">비고</th>" +
			        "       </tr>";
					for(int i=0; i<list.size(); i++) {
			htmlStr += "	<tr>" +
					"			<td style=\"text-align:center;\">"+StrUtil.dotDate(list.get(i).getMasMonth())+"</td>" +
					"			<td style=\"text-align:right;\">"+StrUtil.strComma(list.get(i).getPayItemAmt())+"</td>" +
					"			<td style=\"text-align:right;\">"+StrUtil.strComma(list.get(i).getRcpAmt())+"</td>" +
					"			<td style=\"text-align:center;\">"+StrUtil.dotDate(list.get(i).getPayDay2())+"</td>";
						if("VAS".equals(StrUtil.nullToVoid(list.get(i).getSveCd()))){
			htmlStr += "		<td style=\"text-align:center;\">가상계좌</td>";				
						}
						if("DCS".equals(StrUtil.nullToVoid(list.get(i).getSveCd()))){
			htmlStr += "		<td style=\"text-align:center;\">현금</td>";				
						}
						if("DCD".equals(StrUtil.nullToVoid(list.get(i).getSveCd()))){
			htmlStr += "		<td style=\"text-align:center;\">오프라인카드</td>";				
						}
						if("DVA".equals(StrUtil.nullToVoid(list.get(i).getSveCd()))){
			htmlStr += "		<td style=\"text-align:center;\">무통장입금</td>";				
						}
						if("OCD".equals(StrUtil.nullToVoid(list.get(i).getSveCd()))){
			htmlStr += "		<td style=\"text-align:center;\">온라인카드</td>";				
						}
			htmlStr += "		<td></td>"+
					"		</tr>";
					}
			htmlStr += "	<tr>" +
					"			<td colspan=\"2\" style=\"text-align:center; font-weight:700;\">납부액 합계(원)</td>" +
					"			<td colspan=\"4\" style=\"text-align:right; font-weight:700;\">"+StrUtil.strComma(Integer.toString(chkTotAmt))+"</td>" +
					"		</tr>"+
					"	</table>" +
					"	<p style=\"font-size:11pt; font-weight:100; text-align:center; margin-bottom:30px;\">발행일자 "+ currentDate.substring(0, 4) + "년 " + currentDate.substring(4, 6) + "월 " + currentDate.substring(6, 8) + "일 </p>" +
					"	<table class=\"table table-bordered table-fixed-height table-has-padding\">" +
			        "   	<tr>" +
			        "			<td style=\"padding:12px 10px; \">" +
			        "				<p style=\"font-size:13pt; font-weight:700; margin-bottom:15px;\">안내문</p>" +
			        "				<p>" +
			        "					&middot; 본 확인증은 납부여부의 확인용으로 법적 효력이 없습니다." +
			        "				</p>" +
			        "				<p>" +
			        "					&middot; 납부 내역의 현금영수증 및 납입증명서를 원하신다면 이용기관에 직접 문의하여 발급 받으시길 바랍니다." +
			        "				</p>" +
			        "			</td>" +
			        "   	</tr>" +
			        "	</table>" +	
					"</body>" +
					"</html>";
			
			logger.info("html >>> " + htmlStr.toString());
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
}
