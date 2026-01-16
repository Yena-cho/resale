package com.finger.shinhandamoa.payer.cashreceipt.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finger.shinhandamoa.payer.cashreceipt.dao.CashReceiptDAO;
import com.finger.shinhandamoa.payer.cashreceipt.dto.CashReceiptDTO;
import com.finger.shinhandamoa.payer.payment.dao.PaymentDAO;
import com.finger.shinhandamoa.payer.payment.dto.PaymentDTO;

/**
 * @author  
 * @date    
 * @desc    
 * @version 
 * 
 */
@Service
public class CashReceiptServiceImpl implements CashReceiptService {

	@Autowired
	private CashReceiptDAO cashreceiptdao;

	//현금영수증내역 총 갯수 
	@Override
	public HashMap<String, Object> cashReceiptReqListTotalCount(HashMap<String, Object> reqMap) throws Exception {
		return cashreceiptdao.cashReceiptReqListTotalCount(reqMap);
	}
	
	//현금영수증내역 리스트
	@Override
	public List<CashReceiptDTO> cashReceiptReqList(HashMap<String, Object> reqMap) throws Exception {
		return cashreceiptdao.cashReceiptReqList(reqMap);
	}

	@Override
	public CashReceiptDTO cashInfo(HashMap<String, Object> reqMap) throws Exception {
		return cashreceiptdao.cashInfo(reqMap);
	}	
}
