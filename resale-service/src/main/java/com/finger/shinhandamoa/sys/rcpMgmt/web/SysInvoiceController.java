package com.finger.shinhandamoa.sys.rcpMgmt.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysInvoiceDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.service.SysInvoiceService;
import com.finger.shinhandamoa.vo.PageVO;

@Controller
public class SysInvoiceController {
	
	private static final Logger logger = LoggerFactory.getLogger(SysInvoiceController.class);

	@Inject
	private SysInvoiceService sysInvoiceService;
	
	@Value("${fim.server.host}")
    private String host;
    
    @Value("${fim.server.port}")
    private int port;
    
    @Value("${fim.accessToken}")
    private String accessToken;
    
    //관리자 세금계산서
    @RequestMapping("sys/rcpMgmt/invoice-issue-list")
	public ModelAndView invoiceList(
			@RequestParam(defaultValue = "10") int PAGE_SCALE, 
			@RequestParam(defaultValue = "1") int curPage) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		/*Map<String, Object> reqMap = new HashMap<String, Object>();			
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String today = sdf.format(new Date());
		
				reqMap.put("searchOrderBy", "comNm");
				reqMap.put("useday", today);
				reqMap.put("issueSt", "all");
				
		// 불능건수
		//int faValue = sysInvoiceService.sysInvoiceFaCount(reqMap);
		// total count
		//int totValue = sysInvoiceService.sysInvoiceTotalCount(reqMap);
		
		PageVO page = new PageVO(90, curPage, PAGE_SCALE);
		int start = page.getPageBegin();
		int end = page.getPageEnd();			
			reqMap.put("start", start);
			reqMap.put("end", end);
		
		List<SysInvoiceDTO> list = sysInvoiceService.sysInvoiceListAll(reqMap);
				map.put("list", list);
				map.put("acount", 90);
				map.put("dcount", 90);
				map.put("pager", page); 	// 페이징 처리를 위한 변수
				map.put("curPage", curPage);
				map.put("pageScale",PAGE_SCALE);
				map.put("searchOrderBy", "comNm");
				map.put("retCode", "0000");
				mav.addObject("map", map); 
		*/

		mav.setViewName("sys/rcp/rcpMgmt/invoice-issue-list");

		return mav;
	}
    
    @RequestMapping("sys/rcpMgmt/invoiceSearch")
	@ResponseBody
	public HashMap<String, Object> AjaxInvoiceList(@RequestBody SysInvoiceDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String today = sdf.format(new Date());
		
		Map<String, Object> reqMap = new HashMap<String, Object>();
				reqMap.put("searchOrderBy", body.getSearchOrderBy());
			if(body.getUseDate() == "" || body.getUseDate() == null) {
				reqMap.put("useday", today);
			}else {
				reqMap.put("useday", body.getUseDate());
			}
			
				reqMap.put("issueSt", body.getIssueSt());
				reqMap.put("comCd", body.getComCd());
				reqMap.put("comNm", body.getComNm());
		
		// 불능건수
		//int faValue = sysInvoiceService.sysInvoiceFaCount(reqMap);
		// total count
		//int totValue = sysInvoiceService.sysInvoiceTotalCount(reqMap);
		
		PageVO page = new PageVO(200, body.getCurPage(), body.getPageScale());
		int start = page.getPageBegin();
		int end = page.getPageEnd();			
			reqMap.put("start", start);
			reqMap.put("end", end);
		
		List<SysInvoiceDTO> list = sysInvoiceService.sysInvoiceListAll(reqMap);
		ModelAndView mav = new ModelAndView();
				map.put("list", list);
				map.put("acount", 100);
				map.put("dcount", 200);
				map.put("pager", page); 	// 페이징 처리를 위한 변수
				map.put("curPage", body.getCurPage());
				map.put("pageScale", body.getPageScale());
//				map.put("searchOrderBy", body.getSearchOrderBy());
//				map.put("issueSt", body.getIssueSt());
//				map.put("comCd", body.getComCd());
//				map.put("comNm", body.getComNm());
				map.put("retCode", "0000");
				mav.addObject("map", map); 
		
		return map;

	}
    
	@ResponseBody
	@RequestMapping("")
	public void fileUpload(MultipartHttpServletRequest request) throws Exception {
		
/*		MultipartFile inputFile = request.getFile("file"); 
		
		File dirPath = new File(tempPath); // 디렉토리 존재 확인 후 생성
		if (!dirPath.exists()) {
			dirPath.mkdirs(); 
		}
		
		File file = new File(tempPath +  inputFile.getOriginalFilename()); 
		
		try {
			inputFile.transferTo(file);
		} catch(IllegalStateException  e) {
			throw new RuntimeException(e.getMessage(),e);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		try {
			String previousMonth = DateUtils.previousMonth();

	        String msg = FileUtils.readFileToString(file, Charset.forName("EUC-KR"));
	        EB22 eb22 = (EB22)withdrawalLayoutFactory.decode(msg, "EB22");

	        autoTranMgmtService.updateEB22Result(eb22, previousMonth);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			file.delete();
		}*/
	}
	
	@RequestMapping("sys/rcpMgmt/invoiceexcelDown")
	public View payExcelDown(HttpServletRequest request, HttpServletResponse response, SysInvoiceDTO body, Model model) throws Exception {
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			String today = sdf.format(new Date());
			
			Map<String, Object> reqMap = new HashMap<String, Object>();
					reqMap.put("searchOrderBy", body.getSearchOrderBy());
				if(body.getUseDate() == "" || body.getUseDate() == null) {
					reqMap.put("useday", today);
				}else {
					reqMap.put("useday", body.getUseDate());
				}
					reqMap.put("issueSt", body.getIssueSt());
					reqMap.put("comCd", body.getComCd());
					reqMap.put("comNm", body.getComNm());
			
			// 불능건수
			//int faValue = sysInvoiceService.sysInvoiceFaCount(reqMap);
			// total count
			//int totValue = sysInvoiceService.sysInvoiceTotalCount(reqMap);
					
				reqMap.put("start", 1);
				reqMap.put("end", 200);
				//reqMap.put("end", totValue);
			
			List<SysInvoiceDTO> list = sysInvoiceService.sysInvoiceListAll(reqMap);
			model.addAttribute("list", list);
		}catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return new ExcelSaveSysInvoiceReg();
	}
    
}
