package com.finger.shinhandamoa.org.main.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.org.main.dao.OrgMainDAO;
import com.finger.shinhandamoa.org.main.dto.XmontSumDTO;
import com.finger.shinhandamoa.payer.payment.dto.PaymentDTO;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
@Service
public class OrgMainServiceImpl implements OrgMainService {

	@Inject
	OrgMainDAO orgMainDAO;
	

	
	// 청구월별수납현황
	@Override	
	public XmontSumDTO selXmontSum(Map<String, Object> map) throws Exception{
		return orgMainDAO.selXmontSum(map);
	}
	
	// 메인 페이지 납부율조회
	@Override
	public XmontSumDTO selectPaymentRatio(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return orgMainDAO.selectPaymentRatio(map);
	}
	
	//메인 페이지 납부집계조회
	@Override
	public List<PaymentDTO> selectPaymentSummary(Map<String, Object> map) throws Exception
	{
		return orgMainDAO.selectPaymentSummary(map);
	}



}
