package com.finger.shinhandamoa.payer.main.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import com.finger.shinhandamoa.payer.payment.service.PaymentService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.home.service.HomeService;
import com.finger.shinhandamoa.main.dto.NoticeDTO;
import com.finger.shinhandamoa.payer.cashreceipt.dto.CashReceiptDTO;
import com.finger.shinhandamoa.payer.notification.dto.NotificationDTO;
import com.finger.shinhandamoa.payer.notification.service.NotificationService;
import com.finger.shinhandamoa.payer.payment.dto.PaymentDTO;
import com.finger.shinhandamoa.vo.UserDetailsVO;

@Controller
@RequestMapping("payer/main/**")
public class PayerMainController {
	
	private static final Logger logger = LoggerFactory.getLogger(PayerMainController.class);
	
	@Inject
	private HomeService homeService;
	
	@Inject
	private NotificationService notificationService;

	/*
	 * 	납부자 Main 페이지
	 * */
	@RequestMapping("index")
	public ModelAndView payerMainPage(@RequestParam(defaultValue = "false") String second) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> reqMap = new HashMap<String, Object>();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetailsVO userDetails = (UserDetailsVO)principal;
		String vaNo = userDetails.getVano();
		String unCus = StringUtils.defaultString(userDetails.getUnCus());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String chaCd = authentication.getName();
		String tmasMonth = StrUtil.getCurrentMonthStr();
		String fmasMonth = StrUtil.getCurrentMonthStr();
		
		//마지막 청구월 가져오기
		NotificationDTO maxMonth = notificationService.maxMonth(vaNo);

		reqMap.put("role", "ROLE_USER");
		reqMap.put("chaCd", chaCd);
		reqMap.put("vaNo", vaNo);
		reqMap.put("tmasMonth", tmasMonth);
		reqMap.put("fmasMonth", fmasMonth);
		reqMap.put("maxMonth", maxMonth.getMasMonth());

		if(!unCus.equals("cus") && unCus != null){
			reqMap.put("noCus", "noCus");
			mav.setViewName("redirect:/payer/payment/payList");
		}else{
			mav.setViewName("payer/main/index");
		}
		if(second.equals("true")){
			mav.setViewName("payer/main/index");
		}
		
		List<NoticeDTO> nList = homeService.nList(reqMap);  // 공지사항
		List<NoticeDTO> fList = homeService.fList(reqMap);	// 자주하는질문
		NotificationDTO chaInfo = notificationService.selectChaInfo(vaNo);	//기관정보
		NotificationDTO notification = homeService.getNotification(reqMap); //고지정보
		List<PaymentDTO> payment = homeService.selectPaymentSummary(reqMap); //납부내역
		CashReceiptDTO receipt = homeService.getCashReceipt(reqMap); //현금영수증내역
		
		map.put("maxMonth", maxMonth.getMasMonth());
		map.put("nList", nList); 
		map.put("fList", fList);
		map.put("chaInfo", chaInfo);
		map.put("notification", notification);
		map.put("payment", payment);
		map.put("receipt", receipt);
		
		mav.addObject("map", map);

		return mav;
	}
	
}
