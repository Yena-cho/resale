package com.finger.shinhandamoa.org.main.dao;

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
public interface OrgMainDAO {


	// 청구월별수납현황 
	public XmontSumDTO selXmontSum(Map<String, Object> map) throws Exception;	

	// 메인 페이지 납부율조회
	public XmontSumDTO selectPaymentRatio(Map<String, Object> map) throws Exception;	
	
	//메인 페이지 납부집계조회
	public List<PaymentDTO> selectPaymentSummary(Map<String, Object> reqMap) throws Exception;

} 
