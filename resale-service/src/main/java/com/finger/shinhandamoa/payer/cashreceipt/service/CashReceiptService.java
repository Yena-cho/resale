package com.finger.shinhandamoa.payer.cashreceipt.service;

import java.util.HashMap;
import java.util.List;

import com.finger.shinhandamoa.payer.cashreceipt.dto.CashReceiptDTO;
import com.finger.shinhandamoa.payer.payment.dto.PaymentDTO;


/**
 * @author  
 * @date    
 * @desc     
 * @version 
 * 
 */
public interface CashReceiptService {

	//현금영수증내역 총 갯수
	public HashMap<String, Object> cashReceiptReqListTotalCount(HashMap<String, Object> reqMap) throws Exception;
	
	//현금영수증내역 리스트
	public List<CashReceiptDTO> cashReceiptReqList(HashMap<String, Object> reqMap) throws Exception;

	public CashReceiptDTO cashInfo(HashMap<String, Object> reqMap) throws Exception;
	
}
