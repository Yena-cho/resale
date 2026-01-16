package com.finger.shinhandamoa.org.claimMgmt.service;

import com.finger.shinhandamoa.org.claimMgmt.dto.ChaDTO;
import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimItemDTO;

import java.util.List;
import java.util.Map;

/**
 * 청구항목 서비스
 * 
 * @author puki
 * @author wisehouse@finger.co.kr
 */
public interface ClaimItemService {
	/**
	 * 청구 항목 관리
	 * 
	 * @param chaCd
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	List<ClaimItemDTO> claimItemList(String chaCd, int start, int end) throws Exception;

	/**
	 * 청구 항목 관리 - total count
	 * 
	 * @param chaCd
	 * @return
	 * @throws Exception
	 */
	int claimItemTotalCount(String chaCd) throws Exception;

	/**
	 * 청구 항목 관리 - 내입금통장 정보
	 * 
	 * @param chaCd
	 * @return
	 * @throws Exception
	 */
	List<ClaimItemDTO> moneyPassbookList(String chaCd) throws Exception;

	/**
	 * 청구 항목 삭제
	 * 
 	 * @param chaCd
	 * @param payItemCd
	 * @throws Exception
	 */ 
	void deleteClaimItem(String chaCd, String payItemCd) throws Exception;

	/**
	 * 청구 항목 수정 - 기본정보 가져오기
	 * 
	 * @param chaCd
	 * @param payItemCd
	 * @return
	 * @throws Exception
	 */
	ClaimItemDTO detailClaimItem(String chaCd, String payItemCd) throws Exception;

	/**
	 * 청구 항목 수정 및  등록
	 * 
	 * @param dto
	 * @throws Exception
	 */
	void modifyClaimItem(ClaimItemDTO dto) throws Exception;

	/**
	 * 청구항목명 중복 체크
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	String payItemNameCheck(Map<String, Object> map) throws Exception;

	/**
	 * 청구항목 순서변경
	 * 
	 * @param map
	 * @throws Exception
	 */
	void updatePayItem(Map<String, Object> map) throws Exception;

	/**
	 * 청구항목 등록 갯수 확인 - 9개 이상 불가
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int cusPayItemCnt(Map<String, Object> map) throws Exception;

	/**
	 * 입금통장 유무 확인
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	String selXadjGroup(Map<String, Object> map) throws Exception;

	/**
	 * 기관정보 조회
	 * 
	 * @param chaCd
	 * @return
	 */
	ChaDTO getCha(String chaCd);
}
