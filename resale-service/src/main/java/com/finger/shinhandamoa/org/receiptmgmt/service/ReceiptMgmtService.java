package com.finger.shinhandamoa.org.receiptmgmt.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.org.receiptmgmt.dto.*;

public interface ReceiptMgmtService {

	/**
	 * 수납관리 리스트
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<ReceiptMgmtDTO> getReceiptList(Map<String, Object> map) throws Exception;
	
	/**
	 * 수납관리 리스트 카운트
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getReceiptCount(Map<String, Object> map) throws Exception;
	
	/**
	 * 카드결제 취소(환불)
	 * @param map
	 * @return
	 */
	public int insertRepay(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 영수증 출력시 XCHAOPTION 테이블 조회해서 refcd 값 추출
	 * @param chaCd
	 * @return
	 * @throws Exception
	 */
	public String getXchaoption(String chaCd) throws Exception;
	
	/**
	 * GETXCHAOPTION 테이블이   refcd값이 01
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<PdfReceiptMgmtDTO> selectRcpBillCutDet(HashMap<String, Object> map) throws Exception;
	
	/**
	 * GETXCHAOPTION 테이블이   refcd값이 01 아닌경우
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<PdfReceiptMgmtDTO> selectRcpBillCut(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 교육비납입증명서
	 * @param map
	 * @return
	 */
	public List<PdfReceiptMgmtDTO> getEduList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 장기요양급여 납부확인서
	 * @param map
	 * @return
	 */
	public List<PdfReceiptMgmtDTO> getRecpList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 장기요양 계좌리스트
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<PdfReceiptMgmtDTO> getRecpAccNoList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 기부금 영수증
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<PdfReceiptMgmtDTO> getDntnList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 수납내역 조회
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public ReceiptMgmtDTO selectRcpMas(HashMap<String, Object> reqMap) throws Exception;

	/**
	 * 카드취소대상 원장코드 조회
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public ReceiptMgmtDTO getCancelRcp(HashMap<String, Object> map) throws Exception;

	/**
	 * 수납정보 수정
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateRcpMas(HashMap<String, Object> map) throws Exception;

	/**
	 * 수납상세항목 수정
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void updateRcpDet(HashMap<String, Object> map) throws Exception;

	/**
	 * 고지정보 수정
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void updateNotiBill(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 현장수납 카운트
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getActCount(Map<String, Object> map) throws Exception;
	
	/**
	 * 현장수납 완납 카운트
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getActCount2(Map<String, Object> map) throws Exception;
	
	/**
	 * 현장수납 리스트
	 * @param map
	 * @return
	 */
	public List<ReceiptMgmtDTO> getActList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 현장수납 리스트 완납
	 * @param map
	 * @return
	 */
	public List<ReceiptMgmtDTO> getActList2(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 현장수납관리 수납테이블 저장
	 * @param map
	 * @return
	 */
	public int insertXrcpMas(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 현장수납관리 수납상세테이블 저장
	 * @param map
	 * @return
	 */
	public int insertXrcpDet(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 현장수납관리 수납테이블 수정
	 * @param map
	 * @return
	 */
	public int updateXrcpMas(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 현장수납관리 수납상세테이블 수정
	 * @param map
	 * @return
	 */
	public int updateXrcpDet(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 현장수납관리 청구테이블 update
	 * @param map
	 * @return
	 */
	public int updateXnotiMas(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 현장수납관리 청구상세테이블 update
	 * @param map
	 * @return
	 */
	public int updateXnotiDet(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 시퀀스
	 * @return
	 * @throws Exception
	 */
	public ReceiptMgmtDTO getSeq(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 현장수납관리 수정
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean updateAct(HashMap<String, Object> map, NotiDTO dto) throws Exception;
	
	/**
	 * 환불가능 내역 조회 AMTCHKTY :[IR방식:Y]
	 * @param map
	 * @return
	 */
	public List<ReceiptMgmtDTO> getReplyYList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 환불가능 내역 조회 AMTCHKTY :[SET방식:N]
	 * @param map
	 * @return
	 */
	public List<ReceiptMgmtDTO> getReplyNList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 환불완료 내역 조회 AMTCHKTY :[IR방식:Y]
	 * @param map
	 * @return
	 */
	public List<ReceiptMgmtDTO> getEndReplyYList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 환불완료 내역 조회 AMTCHKTY :[SET방식:N]
	 * @param map
	 * @return
	 */
	public List<ReceiptMgmtDTO> getEndReplyNList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 환불가능 내역 카운트 조회 AMTCHKTY :[IR방식:Y]
	 * @param map
	 * @return
	 */
	public HashMap<String, Object> getReplyYCount(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 환불가능 내역 카운트 조회 AMTCHKTY :[SET방식:N]
	 * @param map
	 * @return
	 */
	public HashMap<String, Object> getReplyNCount(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 환불완료 내역 카운트 조회 AMTCHKTY :[IR방식:Y]
	 * @param map
	 * @return
	 */
	public HashMap<String, Object> getEndReplyYCount(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 환불완료 내역 카운트 조회 AMTCHKTY :[SET방식:N]
	 * @param map
	 * @return
	 */
	public HashMap<String, Object> getEndReplyNCount(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 현금영수증 내역 카운트
	 * @param map
	 * @return
	 */
	public HashMap<String, Object> getCashMasCount(Map<String, Object> map) throws Exception;
	
	/**
	 * 현금영수증 내역 리스트
	 * @param map
	 * @return
	 */
	public List<ReceiptMgmtDTO> getCashMasList(Map<String, Object> map) throws Exception;
	
	/**
	 * 현금영수증 변경
	 * 
	 * @param map
	 * @return
	 */
	public int insertCashMas(HashMap<String, Object> map) throws Exception;

	public int updateCashMas(HashMap<String, Object> map) throws Exception;

	public int deleteCashMas(HashMap<String, Object> map) throws Exception;

	public int noDeleteIssue(HashMap<String, Object> map) throws Exception;

	public int reInsertIssueDelete(HashMap<String, Object> map) throws Exception;

	public int doInsertIssueDelete(HashMap<String, Object> map) throws Exception;

	public int updateCashMasU(HashMap<String, Object> map) throws Exception;
	
	public ReceiptMgmtDTO selectCashMas(HashMap<String, Object> map) throws Exception;

	public List<ReceiptMgmtDTO> getChaMasCd(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 청구항목 상세보기
	 * @param map
	 * @return
	 */
	public List<ReceiptMgmtDTO> getReceiptDetail(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 현금영수증 사전 데이터
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public NotiDTO getCashData(String vaNo) throws Exception;
	
	/**
	 * 현금영수증 인서트
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int insertCashMaster(Map<String, Object> map) throws Exception;
	
	public ReceiptMgmtDTO getXrcpDetList(Map<String, Object> map) throws Exception;
	
	public ReceiptMgmtDTO getRcpCd(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 가상계좌 금액
	 * @param map
	 * @return
	 */
	public ReceiptMgmtDTO getVasAmt(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 현금영수증 사전 데이터
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public NotiDTO insertCashMas(NotiDTO dto) throws Exception;
	
	/**
	 * rcpmascd 찾기
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public String getRcpMasCd(String notiMasCd) throws Exception;

	/**
	 * 고객 조회
	 * 
	 * @param rcpMasCd
	 * @return
	 */
	ReceiptMgmtDTO getCustomerByRcpMasCd(String rcpMasCd);

	/**
	 * 청구원장 조회
	 * 
	 * @param rcpMasCd
	 * @return
	 */
	Map<String,Object> getNoticeMasterByRcpMasCd(String rcpMasCd);

	/**
	 * 청구원장 내역조회
	 * 
	 * @param rcpMasCd
	 * @return
	 */
	List<Map<String,Object>> getNoticeDetailsByRcpMasCd(String rcpMasCd);

	/**
	 * 항목별 입금내역
	 */
	HashMap<String, Object> getPayItemListCnt(HashMap<String, Object> map) throws Exception;

	/**
	 * 항목별 입금내역
	 */
	List<PayMgmtDTO> getPayItemList(HashMap<String, Object> map) throws Exception;

	/**
	 * 항목별 입금내역 엑셀파일
	 */
	@Deprecated
	List<PayMgmtDTO> getPayItemExcelList(HashMap<String, Object> map) throws Exception;

	/**
	 * 수납관리 > 수납내역 엑셀 다운로드 - 신규
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 10. 29.
	 */
	public InputStream getReceiptHistoryExcel(Map<String, Object> params) throws Exception;

	/**
	 * 수납관리 > 현금영수증 내역 엑셀 다운로드 - 신규
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 10. 29.
	 */
	public InputStream getCashReceiptHistoryExcel(Map<String, Object> params) throws Exception;
	
	public InputStream getNtsCashReceiptHistoryExcel(Map<String, Object> params) throws Exception;

    List<PdfReceiptMgmtDTO> getAdmRecpList(HashMap<String, Object> map) throws Exception;

	List<PdfReceiptMgmtDTO> selectAdmBillCut(HashMap<String, Object> map) throws Exception;

	List<PdfReceiptMgmtDTO> getAdmEduList(HashMap<String, Object> map) throws Exception;

	List<PdfReceiptMgmtDTO> getAdmRcpAccNoList(HashMap<String, Object> map) throws Exception;

	List<PdfReceiptMgmtDTO> getAdmDntnList(HashMap<String, Object> map) throws Exception;

	List<PdfReceiptMgmtDTO> selectAdmBillCutWithoutNotice(HashMap<String, Object> map) throws Exception;

	/**
	 * 현금영수증 재발행 내역 리스트 조회
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<FwCashReceiptMasterDTO> getCashHistList(HashMap<String,Object> map) throws Exception;

	HashMap<String, Object> countCashHistList(HashMap<String,Object> map) throws  Exception;
}
