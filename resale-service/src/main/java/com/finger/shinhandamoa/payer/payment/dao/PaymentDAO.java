package com.finger.shinhandamoa.payer.payment.dao;

import java.util.HashMap;
import java.util.List;

import com.finger.shinhandamoa.payer.payment.dto.PaymentDTO;


/**
 * @author  by PYS
 * @date    2018. 4. 12.
 * @desc    
 * @version 
 * 
 */
public interface PaymentDAO {

	public HashMap<String, Object> payListTotalCount(HashMap<String, Object> map) throws Exception;
	public List<PaymentDTO> payList(HashMap<String, Object> map) throws Exception;
	public PaymentDTO selectChaInfo(String vaNo) throws Exception;
	public List<PaymentDTO> chkPayList(HashMap<String, Object> reqMap) throws Exception;
	public HashMap<String, Object> payListMaxMonth(HashMap<String, Object> reqMap) throws Exception;
}
