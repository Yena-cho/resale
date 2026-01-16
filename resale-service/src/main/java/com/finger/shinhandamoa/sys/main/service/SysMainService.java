package com.finger.shinhandamoa.sys.main.service;

import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.org.main.dto.XmontSumDTO;
import com.finger.shinhandamoa.payer.payment.dto.PaymentDTO;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
public interface SysMainService {


	// 청구월별수납현황 
	public XmontSumDTO getSysMainInfo01(Map<String, Object> map) throws Exception;	

	// 메인 페이지 납부율조회 
	public List<XmontSumDTO> selectPaymentRatioList(Map<String, Object> map) throws Exception;	
	
	//메인 페이지 납부집계조회
	public List<XmontSumDTO> selectPaymentSummary(Map<String, Object> map) throws Exception;
}
