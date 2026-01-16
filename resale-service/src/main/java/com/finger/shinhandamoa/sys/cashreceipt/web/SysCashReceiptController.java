package com.finger.shinhandamoa.sys.cashreceipt.web;

import com.finger.shinhandamoa.sys.cashreceipt.service.CashReceiptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 핑거관리자 / 현금영수증 관리
 *
 * @author suhlee
 * @author wisehouse@finger.co.kr
 */
@Controller("sys-cash-receipt")
@RequestMapping("/sys/cash-receipt")
public class SysCashReceiptController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SysCashReceiptController.class);

	@Autowired
    @Qualifier("sys-cash-receipt-service")
	private CashReceiptService cashReceiptService;

    /**
     * 현금영수증 발행 내역 조회
     *
     * @return
     * @throws Exception
     */
	@RequestMapping("")
	public ModelAndView getIndex() throws Exception {
		final ModelAndView mav = new ModelAndView();
		mav.setViewName("sys/cash-receipt/index");

		return mav;
	}

    /**
     * 기관별 현금영수증 발행 현황 조회
     * @return
     */
	@RequestMapping(value="/status-by-client", method= RequestMethod.GET)
	public String getStatusByClient() {
		return "sys/cash-receipt/status-by-client";
	}

	/**
	 * 수납별 현황 조회
	 * @return
	 */
	@RequestMapping(value="/status-by-receipt", method= RequestMethod.GET)
	public String getStatusByReceipt() {
		return "sys/cash-receipt/status-by-receipt";
	}

	/**
	 * 현금영수증 이용내역조회
	 * @return
	 */
	@RequestMapping(value="/cash-receipt-history", method= RequestMethod.GET)
	public String cashReceiptHistory() {
		return "sys/cash-receipt/cash-receipt-history";
	}

	/**
	 * 발행 현황 조회
	 * @return
	 */
	@RequestMapping(value="/status-by-issue", method= RequestMethod.GET)
	public String getStatusByIssueCashReceipt() {
		return "sys/cash-receipt/status-by-issue";
	}

	/**
	 * 발행 현황 조회 - 현금영수증번호 상세보기 팝업
	 * @return
	 */
	@RequestMapping(value="/datailPopUpCashmasCd", method= RequestMethod.GET)
	public String datailPopUpCashmasCd() {
		return "sys/cash-receipt/datailPopUpCashmasCd";
	}

	/**
	 * 발행 현황 조회 - 기관명 상세보기 팝업
	 * @return
	 */
	@RequestMapping(value="/datailPopUpChaName", method= RequestMethod.GET)
	public String datailPopUpChaName() {
		return "sys/cash-receipt/datailPopUpChaName";
	}

	/**
	 * 발행 현황 조회 - 거래일시 상세보기 팝업
	 * @return
	 */
	@RequestMapping(value="/datailPopUpRegDt", method= RequestMethod.GET)
	public String datailPopUpRegDt() {
		return "sys/cash-receipt/datailPopUpRegDt";
	}
}
