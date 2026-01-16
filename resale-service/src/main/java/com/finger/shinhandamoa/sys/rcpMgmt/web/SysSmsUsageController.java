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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysInvoiceDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysSmsUsageDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.service.SysSmsUsageService;
import com.finger.shinhandamoa.vo.PageVO;

@Controller
public class SysSmsUsageController {
	
	private static final Logger logger = LoggerFactory.getLogger(SysSmsUsageController.class);
	
	@Inject
	private SysSmsUsageService sysSmsUsageService;
	
	@Value("${fim.server.host}")
    private String host;
    
    @Value("${fim.server.port}")
    private int port;
    
    @Value("${fim.accessToken}")
    private String accessToken;
    
    @RequestMapping("sys/rcpMgmt/sms-service-usage")
	public ModelAndView smsUsage(
			@RequestParam(defaultValue = "10") int PAGE_SCALE, 
			@RequestParam(defaultValue = "1") int curPage) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		/*Map<String, Object> reqMap = new HashMap<String, Object>();			
		HashMap<String, Object> map = new HashMap<String, Object>();
				reqMap.put("startday", "19880101");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String today = sdf.format(new Date());
				reqMap.put("endday", today);
	
	// 실패건수
	//int faValue = sysSmsUsageService.sysSmsUsageFaCount(reqMap);
	// total count
	//int totValue = sysSmsUsageService.sysSmsUsageTotalCount(reqMap);
	
	PageVO page = new PageVO(10, curPage, PAGE_SCALE);
	int start = page.getPageBegin();
	int end = page.getPageEnd();			
		reqMap.put("start", start);
		reqMap.put("end", end);
	
	List<SysSmsUsageDTO> list = sysSmsUsageService.sysSmsUsageListAll(reqMap);
			map.put("list", list);
			map.put("acount", 10);
			map.put("dcount", 20);
			map.put("pager", page); 	// 페이징 처리를 위한 변수
			map.put("curPage", curPage);
			map.put("pageScale", PAGE_SCALE);
			
			map.put("searchOrderBy", "day");

			map.put("retCode", "0000");
			mav.addObject("map", map); */

		mav.setViewName("sys/rcp/rcpMgmt/sms-service-usage");

		return mav;
	}
    
    @RequestMapping("sys/rcpMgmt/smsUseList")
 	@ResponseBody
 	public HashMap<String, Object> AjaxSmsUsageList(@RequestBody SysSmsUsageDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
 		
 		HashMap<String, Object> map = new HashMap<String, Object>();
 		
 		Map<String, Object> reqMap = new HashMap<String, Object>();
 				reqMap.put("searchOrderBy", body.getSearchOrderBy());
 				if(body.getStartday() == "" || body.getStartday() == null) {
 					reqMap.put("startday", "19880101");
 				}else {
 					reqMap.put("startday", body.getStartday());
 				}
 				if(body.getEndday() == "" || body.getEndday() == null) {
 					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
 					String today = sdf.format(new Date());
 					reqMap.put("endday", today);
 				}else {
 					reqMap.put("endday", body.getEndday());
 				}
 				reqMap.put("sendSt", body.getSendSt());
 				reqMap.put("comCd", body.getComCd());
 				reqMap.put("comNm", body.getComNm());
 				reqMap.put("searchOption", body.getSearchOption());
 				reqMap.put("keyword", body.getKeyword());
 		
 		// 실패건수
 		//int faValue = sysSmsUsageService.sysSmsUsageFaCount(reqMap);
 		// total count
 		//int totValue = sysSmsUsageService.sysSmsUsageTotalCount(reqMap);
 		
 		PageVO page = new PageVO(20, body.getCurPage(), body.getPageScale());
 		int start = page.getPageBegin();
 		int end = page.getPageEnd();			
 			reqMap.put("start", start);
 			reqMap.put("end", end);
 		
 		List<SysSmsUsageDTO> list = sysSmsUsageService.sysSmsUsageListAll(reqMap);
 		ModelAndView mav = new ModelAndView();
 				map.put("list", list);
 				map.put("acount", 30);
 				map.put("fcount", 40);
 				map.put("pager", page); 	// 페이징 처리를 위한 변수
 				map.put("curPage", body.getCurPage());
 				map.put("pageScale", body.getPageScale());
 				
/* 				map.put("searchOrderBy", body.getSearchOrderBy());
 				map.put("sendSt", body.getSendSt());
 				map.put("comCd", body.getComCd());
 				map.put("comNm", body.getComNm());
 				map.put("searchOption", body.getSearchOption());
 				map.put("keyword", body.getKeyword());*/
 				map.put("retCode", "0000");
 				mav.addObject("map", map); 
 		
 		return map;

 	}
    
    @RequestMapping("sys/rcpMgmt/smsuseexcelDown")
	public View payExcelDown(HttpServletRequest request, HttpServletResponse response, SysSmsUsageDTO body, Model model) throws Exception {
		
		try {
	 		Map<String, Object> reqMap = new HashMap<String, Object>();
	 				reqMap.put("searchOrderBy", body.getSearchOrderBy());
	 				if(body.getStartday() == "" || body.getStartday() == null) {
	 					reqMap.put("startday", "19880101");
	 				}else {
	 					reqMap.put("startday", body.getStartday());
	 				}
	 				if(body.getEndday() == "" || body.getEndday() == null) {
	 					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	 					String today = sdf.format(new Date());
	 					reqMap.put("endday", today);
	 				}else {
	 					reqMap.put("endday", body.getEndday());
	 				}
	 				reqMap.put("sendSt", body.getSendSt());
	 				reqMap.put("comCd", body.getComCd());
	 				reqMap.put("comNm", body.getComNm());
	 				reqMap.put("searchOption", body.getSearchOption());
	 				reqMap.put("keyword", body.getKeyword());
	 		
	 		// 실패건수
	 		//int faValue = sysSmsUsageService.sysSmsUsageFaCount(reqMap);
	 		// total count
	 		//int totValue = sysSmsUsageService.sysSmsUsageTotalCount(reqMap);
	 		
				reqMap.put("start", 1);
				reqMap.put("end", 200);
				//reqMap.put("end", totValue);
				
	 		List<SysSmsUsageDTO> list = sysSmsUsageService.sysSmsUsageListAll(reqMap);

			model.addAttribute("list", list);
		}catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return new ExcelSaveSysSmsUsageReg();
	}
    
}
