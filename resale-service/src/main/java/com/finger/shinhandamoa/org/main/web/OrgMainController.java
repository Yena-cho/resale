package com.finger.shinhandamoa.org.main.web;

import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.home.service.HomeService;
import com.finger.shinhandamoa.main.dto.NoticeDTO;
import com.finger.shinhandamoa.org.claimMgmt.service.ClaimService;
import com.finger.shinhandamoa.org.main.dto.XmontSumDTO;
import com.finger.shinhandamoa.org.main.service.OrgMainService;
import com.finger.shinhandamoa.payer.payment.dto.PaymentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;


/**
 * @author  by puki
 * @date    2018. 4. 6.
 * @desc    
 * @version 
 * 
 */
@Controller
@RequestMapping("org/main/**")
public class OrgMainController {
	
	private static final Logger logger = LoggerFactory.getLogger(OrgMainController.class);
	
	@Inject
	private HomeService homeService;
	
	@Inject
	private OrgMainService orgMainService;
	
	@Inject
	private ClaimService claimService;
	
	/*
	 * 	기관관리자 Main 페이지
	 * */
	@RequestMapping("index")
	public ModelAndView orgMainPage() throws Exception {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final String user = authentication.getName();

		final HashMap<String, Object> reqMap = new HashMap<>();
		final HashMap<String, Object> map = new HashMap<>();
		final ModelAndView mav = new ModelAndView();

		try {
			logger.debug("기관 {} 기관관리자 Main", user);
			reqMap.put("role", "ROLE_ADMIN");
			reqMap.put("chaCd", user);
			reqMap.put("startday", StrUtil.getCalMonthDateStr(-3));
			reqMap.put("endday", StrUtil.getCurrentDateStr());

			final List<NoticeDTO> nList = homeService.nList(reqMap); // 공지사항
			map.put("nList", nList);

			final List<NoticeDTO> qList = homeService.qList(reqMap);     // 서비스문의
			map.put("qList", qList);

			final String notiMasSt = "PA02"; // PA00이 아닌 것
			final String masMonth = claimService.selectClaimMonth(user, notiMasSt);
			map.put("masMonth", masMonth);

			final List<PaymentDTO> pList = orgMainService.selectPaymentSummary(reqMap);     //메인 페이지 납부집계조회
			map.put("pList", pList);

			mav.addObject("map", map);
			mav.setViewName("org/main/index");
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		
		return mav;
	}
	
	/*
	 * 청구월별 수납현황
	 */
	@ResponseBody
	@RequestMapping("common/selXmontSum")
	public HashMap<String, Object> selXmontSum(@RequestBody XmontSumDTO dto) throws Exception {

		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final String user = authentication.getName();	
		
		final HashMap<String, Object> map  = new HashMap<>();
		final HashMap<String, Object> smap = new HashMap<>();
		try {
			logger.debug("기관 {} main 청구월별 수납현황", user);
			smap.put("chaCd", user);
			smap.put("month", dto.getMonth());
			final XmontSumDTO retVal = orgMainService.selXmontSum(smap);
			if (retVal != null) {
				map.put("month", dto.getMonth());
				map.put("noticnt", retVal.getNoticnt());
				map.put("notiamt", retVal.getNotiamt());
				map.put("notifee", retVal.getNotifee());
				map.put("rcpcnt", retVal.getRcpcnt());
				map.put("rcpamt", retVal.getRcpamt());
				map.put("rcpfee", retVal.getRcpfee());
				map.put("rcpbnkfee", retVal.getRcpbnkfee());
				map.put("smscnt", retVal.getSmscnt());
				map.put("smsfee", retVal.getSmsfee());
				map.put("finishyn", retVal.getFinishyn());
				map.put("rcpcntNot", retVal.getRcpcntNot());
				map.put("rcpamtNot", retVal.getRcpamtNot());
				map.put("rcpfeeNot", retVal.getRcpfeeNot());
				map.put("rcpRatio1", retVal.getRcpRatio1());
				map.put("rcpRatio2", retVal.getRcpRatio2());
				map.put("overRcpYn", retVal.getOverRcpYn());
				map.put("day", retVal.getDay());
				map.put("today", retVal.getToday());
				map.put("result", retVal.getResult());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		
		return map;
	}	

}
