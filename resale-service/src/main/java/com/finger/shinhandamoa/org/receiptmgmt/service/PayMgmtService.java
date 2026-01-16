package com.finger.shinhandamoa.org.receiptmgmt.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.org.receiptmgmt.dto.PayMgmtDTO;

public interface PayMgmtService {

	/**
	 * 가상계좌 입금내역 카운트
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getPaymentCount(Map<String, Object> map) throws Exception;
	
	/**
	 * 가상계좌 입금내역 리스트
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<PayMgmtDTO> getPaymentList(Map<String, Object> map) throws Exception;

	/**
	 * 온라인카드 입금내역 건수
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getCardPayCount(HashMap<String, Object> map) throws Exception;

	/**
	 * 온라인카드 입금내역 리스트
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<PayMgmtDTO> getCardPayList(HashMap<String, Object> map) throws Exception;

	/**
	 * 가상계좌 입금내역 엑셀다운-신규
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 10. 29.
	 */
	public InputStream getPaymentHistoryExcel(Map<String, Object> params) throws Exception;
	
	/**
	 * 온라인카드 결제내역 엑셀다운-신규
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 10. 29.
	 */
	public InputStream getCardPayHistoryExcel(Map<String, Object> params) throws Exception;
	

	/**
	 * 수납관리 > 항목별 입금내역 엑셀 다운로드 - 신규
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 10. 29.
	 */
	public InputStream getPayItemHistoryExcel(Map<String, Object> params) throws Exception;
}
