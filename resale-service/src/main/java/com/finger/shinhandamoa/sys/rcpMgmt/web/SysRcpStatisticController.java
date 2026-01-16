package com.finger.shinhandamoa.sys.rcpMgmt.web;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;

import com.finger.shinhandamoa.sys.main.web.SysMainController;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysRcpStatDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.service.SysRcpStatService;


@Controller
@RequestMapping("sys/rcpMgmt/**")
public class SysRcpStatisticController {
	
	private static final Logger logger = LoggerFactory.getLogger(SysMainController.class);
	
	@Autowired
	private SysRcpStatService sysRcpStatService;
	
	@Value("${fim.server.host}")	//@value 확인필요
    private String host;
    
    @Value("${fim.server.port}")	//@value 확인필요
    private int port;
    
    @Value("${fim.accessToken}")	//@value 확인필요
    private String accessToken;
	
	/*
	 * 정산관리 > 수납통계 화면
	 */    
	@RequestMapping("rcpStatisticList")
	public String sysRcpStatisticListView(ModelMap model){
		return "sys/rcp/rcpMgmt/rcpStatisticList";
	}
	
	//월별 전체 수납 리스트 조회
	@ResponseBody
	@RequestMapping("monthlyRcpList")
	public HashMap<String, Object> getSysMonthRcpStList(@RequestBody SysRcpStatDTO body) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> reqMap = new HashMap<String, Object>();
		
		try {
			reqMap.put("startday", body.getStartday().substring(0,6));	//조회 시작월
			//reqMap.put("startday", "201804");	//조회 시작월
			reqMap.put("endday", body.getEndday().substring(0,6));		//조회 마감월
			reqMap.put("custSt", body.getComSt()); 		//기관분류
			reqMap.put("chaCd", body.getChaCd()); 		//기관코드
			System.out.println("##################### startday" + reqMap);
			System.out.println("##################### endday" + body.getEndday());
			
 			//전체  WEB API
			List<SysRcpStatDTO> totEmCount = sysRcpStatService.totalCustStCount(reqMap); 
			//월별수납현황
			List<SysRcpStatDTO> selMRcpList = sysRcpStatService.mothlyRcpList(reqMap);
			//월별수납 합계
			List<SysRcpStatDTO> selMRcpTotList = sysRcpStatService.mothlyRcpListTot(reqMap);
			
			map.put("list", totEmCount);
			map.put("alist", selMRcpList);
			map.put("tlist", selMRcpTotList);
			map.put("retCode", "0000");
			map.put("retMsg", "정상");
			
		}catch(Exception e) {
			logger.error(e.getMessage());
			map.put("retCode", "9999");
			map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
		}		
		return map;
	}
	//일별 전체 수납 리스트 조회
	@ResponseBody
	@RequestMapping("daylyRcpList")		
	public HashMap<String, Object> getSysDayRcpStlist(@RequestBody SysRcpStatDTO body) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> reqMap = new HashMap<String, Object>();				
		
		try {
			reqMap.put("startday", body.getStartday());	//조회 시작일
			reqMap.put("endday", body.getEndday());		//조회 마감일
			reqMap.put("custSt", body.getComSt()); 		//기관분류
			reqMap.put("chaCd", body.getChaCd()); 		//기관코드
			
 			//전체 WEB API
			List<SysRcpStatDTO> totEmCount = sysRcpStatService.totalCustStDCount(reqMap); 
			//일별수납현황
			List<SysRcpStatDTO> selRcpList = sysRcpStatService.daylyRcpList(reqMap);
			//일별수납 합계
			List<SysRcpStatDTO> selRcpTotList = sysRcpStatService.daylyRcpListTot(reqMap);
			
			map.put("list", totEmCount);
			map.put("alist", selRcpList);
			map.put("tlist", selRcpTotList);
			map.put("retCode", "0000");
			map.put("retMsg", "정상");
			
		}catch(Exception e) {
			logger.error(e.getMessage());
			map.put("retCode", "9999");
			map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
		}		
		return map;
	}
    /*
    @RequestMapping("sys/rcpMgmt/rcpStatisticList")
	public ModelAndView invoiceissuelist(
			@RequestParam(defaultValue = "10") int PAGE_SCALE, 
			@RequestParam(defaultValue = "1") int curPage) throws Exception{
		
		ModelAndView mav = new ModelAndView();
/*		Map<String, Object> reqMap = new HashMap<String, Object>();			
		HashMap<String, Object> map = new HashMap<String, Object>();
				reqMap.put("startday", "19880101");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String today = sdf.format(new Date());
				reqMap.put("endday", today);

				reqMap.put("comSt", "all");
	
			//전체 web api
		List<SysRcpStatDTO> totEmCount =sysRcpStatService.totalCustStCount(reqMap); 
		//월별수납현황
		List<SysRcpStatDTO> selMRcpList = sysRcpStatService.mothlyRcpList(reqMap);
		//월별수납 합계
		List<SysRcpStatDTO> selMRcpTotList = sysRcpStatService.mothlyRcpListTot(reqMap);
		
		map.put("list", totEmCount);
		map.put("alist", selMRcpList);
		map.put("tlist", selMRcpTotList);
		map.put("retCode", "0000");
		System.out.println(selMRcpList.size());
		System.out.println(selMRcpTotList.size());
		mav.addObject("map", map);
		mav.setViewName("sys/rcp/rcpMgmt/rcpStatisticList");
		return mav;
	}
    
    @ResponseBody
	@RequestMapping("sys/rcpMgmt/monthlyRcpList")
	public HashMap<String, Object> ajaxSysEmailList(@RequestBody SysRcpStatDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> reqMap = new HashMap<String, Object>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String today = sdf.format(new Date());
			if(body.getStartday() == "" || body.getStartday() == null) {
				reqMap.put("startday", today);
			}else {
				reqMap.put("startday", body.getStartday());
			}
			if(body.getEndday() == "" || body.getEndday() == null) {
				reqMap.put("endday", today);
			}else {
				reqMap.put("endday", body.getEndday());
			}
			reqMap.put("custSt", body.getComSt()); //기관분류
			
 			//전체 web api
			List<SysRcpStatDTO> totEmCount =sysRcpStatService.totalCustStCount(reqMap); 
			//월별수납현황
			List<SysRcpStatDTO> selMRcpList = sysRcpStatService.mothlyRcpList(reqMap);
			//월별수납 합계
			List<SysRcpStatDTO> selMRcpTotList = sysRcpStatService.mothlyRcpListTot(reqMap);
			
		HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("list", totEmCount);
			map.put("alist", selMRcpList);
			map.put("tlist", selMRcpTotList);
			map.put("retCode", "0000");
		return map;
	}*/
    
    @RequestMapping("rcpstatexcelDown")
   	public View payExcelDown(HttpServletRequest request, HttpServletResponse response, SysRcpStatDTO body, Model model) throws Exception {
    	
    		HashMap<String, Object> reMap = new HashMap<String, Object>();
    		HashMap<String, Object> map = new HashMap<String, Object>();
   		
   		try {
   			reMap.put("startday", body.getStartday());	//조회 시작일
			reMap.put("endday", body.getEndday());		//조회 마감일
			reMap.put("custSt", body.getComSt()); 		//기관분류
			reMap.put("chaCd", body.getChaCd()); 		//기관코드
			
 			//전체 WEB API
			List<SysRcpStatDTO> totEmCount = sysRcpStatService.totalCustStCount(reMap); 
			//월별수납현황
			List<SysRcpStatDTO> selMRcpList = sysRcpStatService.mothlyRcpList(reMap);
			//월별수납 합계
			List<SysRcpStatDTO> selMRcpTotList = sysRcpStatService.mothlyRcpListTot(reMap);
			
			map.put("list", totEmCount);
			map.put("alist", selMRcpList);
			map.put("tlist", selMRcpTotList);
			map.put("retCode", "0000");
			map.put("retMsg", "정상");
			
   		}catch(Exception e) {
   			logger.error(e.getMessage());
   			throw e;
   		}
   		
   		return new ExcelSaveSysRcpStatReg();
   	}
    
}
