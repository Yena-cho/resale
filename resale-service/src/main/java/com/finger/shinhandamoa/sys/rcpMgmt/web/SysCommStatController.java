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

import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysCommStatDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.service.SysCommStatService;

@Controller
public class SysCommStatController {
	
	private static final Logger logger = LoggerFactory.getLogger(SysCommStatController.class);
	
	@Inject
	private SysCommStatService sysCommStatService;
	
	@Value("${fim.server.host}")
    private String host;
    
    @Value("${fim.server.port}")
    private int port;
    
    @Value("${fim.accessToken}")
    private String accessToken;
    
    @RequestMapping("sys/rcpMgmt/commission-status")
	public ModelAndView commStatList(
			@RequestParam(defaultValue = "10") int PAGE_SCALE, 
			@RequestParam(defaultValue = "1") int curPage) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		/*Map<String, Object> reqMap = new HashMap<String, Object>();			
		HashMap<String, Object> map = new HashMap<String, Object>();

					reqMap.put("startday", "19880101");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					String today = sdf.format(new Date());
					reqMap.put("endday", today);
					reqMap.put("comSt", "");

		// total count
		List<SysCommStatDTO> list = sysCommStatService.sysCommStatTotalCount(reqMap);
		
 		PageVO page = new PageVO(totValue, body.getCurPage(), body.getPageScale());
		int start = page.getPageBegin();
		int end = page.getPageEnd();			
			reqMap.put("start", start);
			reqMap.put("end", end);
		//전체이용기관 납부 수수료
	 	List<SysCommStatDTO> alist = sysCommStatService.sysCommStatListAll(reqMap);
	 	List<SysCommStatDTO> alisttot = sysCommStatService.sysCommStatListTot(reqMap);
	 	//핑거 수수료
		List<SysCommStatDTO> fglist = sysCommStatService.sysCommStatListFG(reqMap);
		List<SysCommStatDTO> fglisttot = sysCommStatService.sysCommStatListFGTot(reqMap);
				map.put("list", list);
				map.put("alist", alist);
				map.put("alisttot", alisttot);
				map.put("fglist", fglist); 		
				map.put("fglisttot", fglisttot);
				//map.put("searchOrderBy", body.getSearchOrderBy());
 				map.put("comSt", body.getComSt());
				map.put("comCd", body.getComCd());
				map.put("comNm", body.getComNm());
				map.put("retCode", "0000");
				mav.addObject("map", map); */
				mav.setViewName("sys/rcp/rcpMgmt/commission-status");

		return mav;
	}
    
    @RequestMapping("sys/rcpMgmt/commStatList")
 	@ResponseBody
 	public HashMap<String, Object> ajaxCommStatList(@RequestBody SysCommStatDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
 		
 		HashMap<String, Object> map = new HashMap<String, Object>();
 		
		Map<String, Object> reqMap = new HashMap<String, Object>();
 				//reqMap.put("searchOrderBy", body.getSearchOrderBy());
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
 				reqMap.put("comSt", body.getComSt());
 				reqMap.put("comCd", body.getComCd());
 				reqMap.put("comNm", body.getComNm());

 		// total count
 		List<SysCommStatDTO> list = sysCommStatService.sysCommStatTotalCount(reqMap);
 		
/* 		PageVO page = new PageVO(totValue, body.getCurPage(), body.getPageScale());
 		int start = page.getPageBegin();
 		int end = page.getPageEnd();			
 			reqMap.put("start", start);
 			reqMap.put("end", end);*/
 		//전체이용기관 납부 수수료
 	 	List<SysCommStatDTO> alist = sysCommStatService.sysCommStatListAll(reqMap);
 	 	List<SysCommStatDTO> alisttot = sysCommStatService.sysCommStatListTot(reqMap);
 	 	//핑거 수수료
 		List<SysCommStatDTO> fglist = sysCommStatService.sysCommStatListFG(reqMap);
 		List<SysCommStatDTO> fglisttot = sysCommStatService.sysCommStatListFGTot(reqMap);
 		ModelAndView mav = new ModelAndView();
 				map.put("list", list);
 				map.put("alist", alist);
 				map.put("alisttot", alisttot);
 				map.put("fglist", fglist); 		
 				map.put("fglisttot", fglisttot);
 				//map.put("searchOrderBy", body.getSearchOrderBy());
/* 				map.put("comSt", body.getComSt());
 				map.put("comCd", body.getComCd());
 				map.put("comNm", body.getComNm());*/
 				map.put("retCode", "0000");
 				mav.addObject("map", map); 
 		
 		return map;

 	}
    
    @RequestMapping("sys/rcpMgmt/commstatexcelDown")
	public View payExcelDown(HttpServletRequest request, HttpServletResponse response, SysCommStatDTO body, Model model) throws Exception {
		
		try {
			Map<String, Object> reqMap = new HashMap<String, Object>();
	 				//reqMap.put("searchOrderBy", body.getSearchOrderBy());
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
	 				reqMap.put("comSt", body.getComSt());
	 				reqMap.put("comCd", body.getComCd());
	 				reqMap.put("comNm", body.getComNm());

	 		// total count
	 		List<SysCommStatDTO> list = sysCommStatService.sysCommStatTotalCount(reqMap);
	 		
			reqMap.put("start", 1);
			reqMap.put("end", 200);
			//reqMap.put("end", totValue);
	 		//전체이용기관 납부 수수료
	 	 	List<SysCommStatDTO> alist = sysCommStatService.sysCommStatListAll(reqMap);
	 	 	List<SysCommStatDTO> alisttot = sysCommStatService.sysCommStatListTot(reqMap);
	 	 	//핑거 수수료
	 		List<SysCommStatDTO> fglist = sysCommStatService.sysCommStatListFG(reqMap);
	 		List<SysCommStatDTO> fglisttot = sysCommStatService.sysCommStatListFGTot(reqMap);

	 		if(body.getNo() == 1 || body.getNo() == 3) {
	 			model.addAttribute("distinct", 1);
	 			model.addAttribute("list", alist);
	 			model.addAttribute("tlist", alisttot);
	 		}else {
	 			model.addAttribute("distinct", 2);
	 			model.addAttribute("list", fglist);
	 			model.addAttribute("tlist", fglisttot);
	 		}
		}catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return new ExcelSaveSysCommStatReg();
	}
    
}
