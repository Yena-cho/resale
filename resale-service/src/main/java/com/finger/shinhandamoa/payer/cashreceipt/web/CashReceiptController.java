package com.finger.shinhandamoa.payer.cashreceipt.web;


import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.payer.cashreceipt.dto.CashReceiptDTO;
import com.finger.shinhandamoa.payer.cashreceipt.service.CashReceiptService;
import com.finger.shinhandamoa.vo.PageVO;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;


/**
 * 현금영수증 컨트롤러
 * 
 * @author wisehouse@finger.co.kr
 * @since
 */

@Controller
@RequestMapping("payer/cashReceipt/**")
public class CashReceiptController {
	
	private static final Logger logger = LoggerFactory.getLogger(CashReceiptController.class);
	
	@Autowired
	private CashReceiptService cashReceiptService;
	
	/**
	 * 	현금영수증내역 리스트
	 */
	@RequestMapping("cashReceiptReqList")
	public ModelAndView cashReceiptReqList(@RequestParam(defaultValue = "1") int curPage,
										   @RequestParam(defaultValue = "10") int PAGE_SCALE,
										   @RequestParam(defaultValue = "1") int start,
										   @RequestParam(defaultValue = "10") int end,
										   @RequestParam(defaultValue = "masMonth") String search_orderBy,
										   @RequestParam(defaultValue = "") String vaNo,
										   @RequestParam(defaultValue = "") String chaCd,
										   @RequestParam(defaultValue = "") String cusName) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		
		HashMap<String, Object> reqMap = new HashMap<>();
		HashMap<String, Object> map = new HashMap<>();
		
		String tmasMonth = "";
		String fmasMonth = "";
		
		try {
			fmasMonth = StrUtil.getCalMonthDateStr(-1);
			tmasMonth = StrUtil.getCurrentDateStr();
			if(!StringUtils.isNumeric(chaCd) || chaCd.length() != 8){
				throw new Exception("비정상적인 접근입니다.");
			}
			if(!StringUtils.isNumeric(vaNo)){
				throw new Exception("비정상적인 접근입니다.");
			}
			reqMap.put("vaNo", vaNo);
			reqMap.put("chaCd", chaCd);
			reqMap.put("tmasMonth", tmasMonth);
			reqMap.put("fmasMonth", fmasMonth);
			
			//납부자 현금영수증정보
			final CashReceiptDTO cashInfo = cashReceiptService.cashInfo(reqMap);

			//현금영수증 내역 총갯수
			HashMap<String, Object> totValue = cashReceiptService.cashReceiptReqListTotalCount(reqMap);

			int count = Integer.parseInt(totValue.get("TOTCNT").toString());
			
			PageVO page = new PageVO(count, curPage, PAGE_SCALE);
			
			reqMap.put("start", start);
			reqMap.put("end", end);
			reqMap.put("search_orderBy", search_orderBy);
			
			List<CashReceiptDTO> list = cashReceiptService.cashReceiptReqList(reqMap);
			
			map.put("cashInfo", cashInfo);
			map.put("count", count);
			map.put("vaNo", StringEscapeUtils.escapeHtml4(vaNo));
			map.put("cusName", StringEscapeUtils.escapeHtml4(cusName));
			map.put("chaCd", StringEscapeUtils.escapeHtml4(chaCd));
			map.put("list", list);
			map.put("tmasMonth", tmasMonth);
			map.put("fmasMonth", fmasMonth);
			map.put("pager", page); 	// 페이징 처리를 위한 변수
			map.put("PAGE_SCALE", PAGE_SCALE);
			map.put("search_orderBy", search_orderBy);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		mav.addObject("map", map);
		mav.setViewName("payer/cashReceipt/cashReceiptReqList");
		
		return mav;
	}
	
	/*
	 * 	현금영수증내역 리스트 조회(AJAX)
	 * */
	@RequestMapping("cashReceiptReqListAjax")
	@ResponseBody
	public HashMap<String, Object> cashReceiptReqListAjax(@RequestBody CashReceiptDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		try {
			logger.info("PAGE_SCALE : " + body.getPageScale());
			HashMap<String, Object> reqMap = new HashMap<String, Object>();
			
			logger.info("vaNo : " + body.getVaNo());
			logger.info("chaCd : " + body.getChaCd());
			logger.info("tmasMonth : " + body.getTmasMonth());
			logger.info("fmasMonth : " + body.getFmasMonth());
			logger.info("status : " + body.getStatus());
			
			reqMap.put("vaNo", body.getVaNo());
			reqMap.put("chaCd", body.getChaCd());
			reqMap.put("tmasMonth", body.getTmasMonth());
			reqMap.put("fmasMonth", body.getFmasMonth());
			reqMap.put("curPage", body.getCurPage());
			reqMap.put("PAGE_SCALE", body.getPageScale());
			reqMap.put("search_orderBy", body.getSearchOrderBy());
			reqMap.put("status", body.getStatus());
			
			//현금영수증 내역 총갯수
			HashMap<String, Object> totValue = cashReceiptService.cashReceiptReqListTotalCount(reqMap);

			int count = Integer.parseInt(totValue.get("TOTCNT").toString());
		
			PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
			int start = page.getPageBegin();
			int end = page.getPageEnd();
			
			reqMap.put("start", start);
			reqMap.put("end", end);
			
			logger.info("111111count : " + count);
			// 현금영수증내역 리스트 조회
			List<CashReceiptDTO> list = cashReceiptService.cashReceiptReqList(reqMap);
			
			logger.info("12312313213123213 : " + count);
			map.put("vaNo", body.getVaNo());
			map.put("chaCd", body.getChaCd());
			map.put("list", list); 
			map.put("count", count);
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
}
