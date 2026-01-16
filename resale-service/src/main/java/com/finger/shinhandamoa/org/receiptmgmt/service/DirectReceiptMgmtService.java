package com.finger.shinhandamoa.org.receiptmgmt.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.data.table.model.NoticeDetailsType;
import com.finger.shinhandamoa.org.receiptmgmt.dto.ReceiptMgmtDTO;
import com.finger.shinhandamoa.org.receiptmgmt.dto.RefundRcpDTO;

public interface DirectReceiptMgmtService {

	/**
	 * 마지막 청구월 조회
	 * @param chacd
	 * @param status
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 11.
	 */
	public Map<String, String> getNotiMasMonth(String chacd, String... status) throws Exception;
	
	/**
	 * 청구항목 목록조회
	 * @param chacd 기관코드
	 * @return
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 11.
	 */
	public List<NoticeDetailsType> selectPayItems(String chacd);
	
	/**
	 * 직접수납/완료 청구목록조회
	 * @param params
	 * @return
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 11.
	 */
	public List<ReceiptMgmtDTO> selectNoticeCatalog(Map<String, Object> params) throws Exception;
	
	/**
	 * 직접수납/완료 목록건수, 청구금액 조회
	 * @param params
	 * @return
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 11.
	 */
	public Map<String, Object> selectNoticeCatalogCount(Map<String, Object> params) throws Exception;
	
	/**
	 * 직접수납 저장처리
	 * @param params
	 * @return
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 11.
	 */
	public Map<String, Object> processReceipt(Map<String, Object> params) throws Exception;

	/**
	 * 직접수납 취소처리
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 14.
	 */
	public Map<String, Object> processReceiptCancel(Map<String, Object> params) throws Exception;

	/**
	 * 수기수납내역 엑셀다운로드
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 18.
	 */
	public InputStream getDirectReceiptExcel(Map<String, Object> params) throws Exception;
	
	/**
	 * 수기환불등록/완료 청구목록조회
	 * @param params
	 * @return
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 19.
	 */
	public List<RefundRcpDTO> selectRefundReceiptCatalog(Map<String, Object> params) throws Exception;
	
	/**
	 * 수기환불등록/완료 목록건수, 청구금액 조회
	 * @param params
	 * @return
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 19.
	 */
	public Map<String, Object> selectRefundReceiptCatalogCount(Map<String, Object> params) throws Exception;
	
	/**
	 * 수기환불등록 저장처리
	 * @param params
	 * @return
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 19.
	 */
	public Map<String, Object> processRefundReceipt(Map<String, Object> params) throws Exception;

	/**
	 * 수기환불완료 취소처리
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 19.
	 */
	public Map<String, Object> processRefundReceiptCancel(Map<String, Object> params) throws Exception;
	
	/**
	 * 수기환불내역 엑셀다운로드
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 19.
	 */
	public InputStream getRefundReceiptExcel(Map<String, Object> params) throws Exception;

}
