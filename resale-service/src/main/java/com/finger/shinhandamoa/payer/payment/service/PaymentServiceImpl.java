package com.finger.shinhandamoa.payer.payment.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finger.shinhandamoa.payer.payment.dao.PaymentDAO;
import com.finger.shinhandamoa.payer.payment.dto.PaymentDTO;

/**
 * @author  by PYS
 * @date    2018. 4. 12.
 * @desc    
 * @version 
 * 
 */
@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentDAO paymentdao;
	
	@Override
	public PaymentDTO selectChaInfo(String vaNo) throws Exception {
		return paymentdao.selectChaInfo(vaNo);
	}
	
	@Override
	public HashMap<String, Object> payListTotalCount(HashMap<String, Object> map) throws Exception {		
		return paymentdao.payListTotalCount(map);
	}
	
	@Override
	public List<PaymentDTO> payList(HashMap<String, Object> map) throws Exception {
		return paymentdao.payList(map);
	}

	@Override
	public List<PaymentDTO> chkPayList(HashMap<String, Object> reqMap) throws Exception {
		return paymentdao.chkPayList(reqMap);
	}

	@Override
	public HashMap<String, Object> payListMaxMonth(HashMap<String, Object> reqMap) throws Exception {
		return paymentdao.payListMaxMonth(reqMap);
	}


}
