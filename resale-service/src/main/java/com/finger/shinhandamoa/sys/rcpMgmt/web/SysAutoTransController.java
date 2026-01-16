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

import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysAutoTransDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysInvoiceDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.service.SysAutoTransService;
import com.finger.shinhandamoa.vo.PageVO;


@Controller
public class SysAutoTransController {
	
	private static final Logger logger = LoggerFactory.getLogger(SysAutoTransController.class);
	
	@Inject
	private SysAutoTransService sysAutoTransService;
	
	@Value("${fim.server.host}")
    private String host;
    
    @Value("${fim.server.port}")
    private int port;
    
    @Value("${fim.accessToken}")
    private String accessToken;
    
    @RequestMapping("sys/rcpMgmt/autoTrans")
	public ModelAndView autoTransList(
			@RequestParam(defaultValue = "10") int PAGE_SCALE, 
			@RequestParam(defaultValue = "1") int curPage) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		/*Map<String, Object> reqMap = new HashMap<String, Object>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		reqMap.put("startday", "19880101");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(new Date());
		reqMap.put("endday", today);
		reqMap.put("starttime", "");
		reqMap.put("endtime", "");
		// 실패건수
		//int faValue = sysAutoTransService.sysAutoTransFaCount(reqMap);
		// 미납건수
		//int naValue = sysAutoTransService.sysAutoTransFaCount(reqMap);
		// total count
		//int totValue = sysAutoTransService.sysAutoTransTotalCount(reqMap);

		PageVO page = new PageVO(100, curPage, PAGE_SCALE);
		int start = page.getPageBegin();
		int end = page.getPageEnd();
		reqMap.put("start", start);
		reqMap.put("end", end);

		List<SysAutoTransDTO> list = sysAutoTransService.sysAutoTransListAll(reqMap);
		map.put("list", list);
		map.put("acount", 100);
		map.put("ncount", 50);
		map.put("dcount", 50);
		map.put("pager", page); // 페이징 처리를 위한 변수
		map.put("curPage", curPage);
		map.put("pageScale", PAGE_SCALE);

		map.put("searchOrderBy", "comNm");

		map.put("retCode", "0000");
		mav.addObject("map", map);*/

		mav.setViewName("sys/rcp/rcpMgmt/autoTrans");

		return mav;
	}
    
	@RequestMapping("sys/rcpMgmt/autoTransList")
	@ResponseBody
	public HashMap<String, Object> AjaxAutoTransList(@RequestBody SysAutoTransDTO body, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>();

		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("searchOrderBy", body.getSearchOrderBy());
		if (body.getStartday() == "" || body.getStartday() == null) {
			reqMap.put("startday", "19880101");
		} else {
			reqMap.put("startday", body.getStartday());
		}
		if (body.getEndday() == "" || body.getEndday() == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String today = sdf.format(new Date());
			reqMap.put("endday", today);
		} else {
			reqMap.put("endday", body.getEndday());
		}
		reqMap.put("starttime", body.getStarttime());
		reqMap.put("endtime", body.getEndtime());
		reqMap.put("transSt", body.getTransSt());
		reqMap.put("comCd", body.getComCd());
		reqMap.put("comNm", body.getComNm());
		reqMap.put("searchOption", body.getSearchOption());
		reqMap.put("keyword", body.getKeyword());

		// 실패건수
		//int faValue = sysAutoTransService.sysAutoTransFaCount(reqMap);
		// 미납건수
		//int naValue = sysAutoTransService.sysAutoTransFaCount(reqMap);
		// total count
		//int totValue = sysAutoTransService.sysAutoTransTotalCount(reqMap);

		PageVO page = new PageVO(500, body.getCurPage(), body.getPageScale());
		int start = page.getPageBegin();
		int end = page.getPageEnd();
		reqMap.put("start", start);
		reqMap.put("end", end);

		List<SysAutoTransDTO> list = sysAutoTransService.sysAutoTransListAll(reqMap);
		ModelAndView mav = new ModelAndView();
		map.put("list", list);
		map.put("acount", 500); //전체건수
		map.put("ncount", 50); //미납건수
		map.put("fcount", 50); //실패건수
		map.put("pager", page); // 페이징 처리를 위한 변수
		map.put("curPage", body.getCurPage());
		map.put("pageScale", body.getPageScale());

/*		map.put("searchOrderBy", body.getSearchOrderBy());
		map.put("transSt", body.getTransSt());
		map.put("comCd", body.getComCd());
		map.put("comNm", body.getComNm());
		map.put("searchOption", body.getSearchOption());
		map.put("keyword", body.getKeyword());*/
		map.put("retCode", "0000");
		mav.addObject("map", map);

		return map;

	}
	
	@RequestMapping("sys/rcpMgmt/autotransexcelDown")
	public View payExcelDown(HttpServletRequest request, HttpServletResponse response, SysAutoTransDTO body, Model model) throws Exception {
		
		try {
			Map<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("searchOrderBy", body.getSearchOrderBy());
			if (body.getStartday() == "" || body.getStartday() == null) {
				reqMap.put("startday", "19880101");
			} else {
				reqMap.put("startday", body.getStartday());
			}
			if (body.getEndday() == "" || body.getEndday() == null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String today = sdf.format(new Date());
				reqMap.put("endday", today);
			} else {
				reqMap.put("endday", body.getEndday());
			}
			reqMap.put("starttime", body.getStarttime());
			reqMap.put("endtime", body.getEndtime());
			reqMap.put("transSt", body.getTransSt());
			reqMap.put("comCd", body.getComCd());
			reqMap.put("comNm", body.getComNm());
			reqMap.put("searchOption", body.getSearchOption());
			reqMap.put("keyword", body.getKeyword());
			// total count
			//int totValue = sysAutoTransService.sysAutoTransTotalCount(reqMap);

			reqMap.put("start", 1);
			reqMap.put("end", 200);
			//reqMap.put("end", totValue);
			
			List<SysAutoTransDTO> list = sysAutoTransService.sysAutoTransListAll(reqMap);

			model.addAttribute("list", list);
		}catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return new ExcelSaveSysAutoTransReg();
	}
    
}
