package com.finger.shinhandamoa.payer.payment.service;

import java.util.HashMap;
import java.util.List;

import com.finger.shinhandamoa.payer.payment.dto.PaymentDTO;


/**
 * @author  by PYS
 * @date    2018. 4. 11.
 * @desc    납부자 납부관리 
 * @version 
 * 
 */
public interface PaymentService {
	
	//기관정보 조회
	public PaymentDTO selectChaInfo(String vaNo) throws Exception;
	//납부내역 총 갯수 조회
	public HashMap<String, Object> payListTotalCount(HashMap<String, Object> map) throws Exception;
	//납부내역 리스트 조회
	public List<PaymentDTO> payList(HashMap<String, Object> map) throws Exception;
	//납부확인증 리스트 조회
	public List<PaymentDTO> chkPayList(HashMap<String, Object> reqMap) throws Exception;
	// 납부자 납부내역 
	public HashMap<String, Object> payListMaxMonth(HashMap<String, Object> reqMap) throws Exception;
}
