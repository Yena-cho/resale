package com.finger.shinhandamoa.org.claimMgmt.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimDTO;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiSendDTO;

/**
 * @author  by puki
 * @date    2018. 4. 12.
 * @desc    최초생성
 * @version 
 * 
 */
public interface ClaimDAO {

	// 청구월
	public String selectClaimMonth(Map<String, Object> map) throws Exception;
	// 청구등록 - 청구대상 목록
	public List<ClaimDTO> selectClaimAll(Map<String, Object> map) throws Exception;
	// 청구등록 - 청구대상 목록 total count
	public int selectClaimTotalCnt(Map<String, Object> map) throws Exception;
	// 청구등록 - 청구대상 목록 고객 count
	public int selectClaimClientCnt(Map<String, Object> map) throws Exception;
	// 청구등록 - 기존 임시 데이터 삭제
	public void deleteClaimXnotimas(Map<String, Object> map) throws Exception; // 청구서
	public void deleteClaimXnotidet(Map<String, Object> map) throws Exception; // 청구서 상세 항목
	// 청구 선택 삭제
	public void selClaimDeleteMas(Map<String, Object> map) throws Exception; // 청구서
	public void selClaimDeleteDet(Map<String, Object> map) throws Exception; // 청구서 상세 항목
	// 청구정보 상세
	public ClaimDTO selectClaimDetail(String notiDetCd) throws Exception;
	// 청구 등록 합계
	public long selClaimRegSum(Map<String, Object> map) throws Exception;
	// 청구 등록 임시저장 - 청구서
	public void updateClaimTempMas(Map<String, Object> map) throws Exception;
	// 청구 등록 임시저장 - 청구서 상세
	public void updateClaimTempDet(Map<String, Object> map) throws Exception;
	// 청구 등록 - 청구서
	public void claimMasUpdate(Map<String, Object> map) throws Exception;
	// 청구 등록 - 청구서 상세
	public void claimDetUpdate(Map<String, Object> map) throws Exception;
	// 청구 취소 - 데이터 추출
	public ClaimDTO selDetToMasNo(String notiDetCd) throws Exception;
	// 청구 일괄취소 - 데이터 추출
	public List<ClaimDTO> selMasDetNoList(Map<String, Object> map) throws Exception;
	// 개별청구등록 - 고객검색 
	public List<ClaimDTO> searchCusName(Map<String, Object> map) throws Exception;
	// 개별청구등록 - 고객검색 total count
	public int searchCusNameCnt(Map<String, Object> map) throws Exception;
	// 개별청구등록 - 고객정보 조회 
	public ClaimDTO searchCustomer(Map<String, Object> map) throws Exception;
	// 개별청구등록 - 청구항목 등록 확인
	public ClaimDTO selPayItem(Map<String, Object> map) throws Exception;
	// 개별청구등록
	public void insClaimRegMas(Map<String, Object> map) throws Exception;
	// 개별청구등록
	public void insClaimRegDet(Map<String, Object> map) throws Exception;
	// 개별청구등록 - 청구중복확인
	public int selClaimRegConfirm(Map<String, Object> map) throws Exception;	
	// 개별 등록 수정 - 청구항목
	public List<ClaimDTO> selItemList(Map<String, Object> map) throws Exception;
	// 청구등록 수정 - 청구항목 삭제 
	public void claimItemDelete(String notiDetCd) throws Exception;
	// 이전청구정보 불러오기 - 이전청구월정보 
	public List<ClaimDTO> selBeforeMasYear(String chaCd) throws Exception;
	public List<ClaimDTO> selBeforeMasMonth(String chaCd, String year) throws Exception;
	// 이전청구정보 불러오기 - 청구대상추가
	public void beforeXnotimasInsert(Map<String, Object> map) throws Exception;
	public void beforeXnotidetInsert(Map<String, Object> map) throws Exception;
	// 청구시 sms 자동발송 여부
	public String autoSmsSendYn(Map<String, Object> map) throws Exception;
	// 청구시 SMS 자동발송 데이터 추출
	public List<NotiSendDTO> autoSmsList(Map<String, Object> map) throws Exception;
	// 청구일괄취소 
	public void updateCancelMasAll(Map<String, Object> map) throws Exception;
	public void updateCancelDetAll(Map<String, Object> map) throws Exception;
	// 청구번호 seq
	public ClaimDTO selNotiMasCd(Map<String, Object> map) throws Exception;
	// 청구번호 SELECT
	public String selectNotiMasCd(Map<String, Object> map) throws Exception;
	// 청구항목 중복 확인
	public int selDupPayItem(Map<String, Object> map) throws Exception;
	// 청구등록 - masMonth
	public String selctMasMonth(Map<String, Object> map) throws Exception;
	// 청구등록 - xnotimas
	public void updateClaimNotiMas(Map<String, Object> map) throws  Exception;
	// 청구등록 - xnotimas
	public void updateClaimNotiDet(Map<String, Object> map) throws  Exception;
	// 일괄적용 - MAS
	public int updateMasBundle(Map<String, Object> map) throws Exception;
	// 일괄적용 - DET
	public int updateDetBundle(Map<String, Object> map) throws Exception;
	// 청구조회 total count
	public int selectClaimDetCount(Map<String, Object> map) throws Exception;
	// 청구조회 일괄취소 count
	public int selectClaimCancelCount(Map<String, Object> map) throws Exception;
	// 청구조회
	public List<ClaimDTO> selectClaimDetList(Map<String, Object> map) throws Exception;
	// 청구항목 상세보기
	public List<ClaimDTO> selectPayItemDetailView(Map<String, Object> map) throws Exception;
	// 청구 항목 삭제시, 청구항목  건수 확인
	public int selDetCdCnt(Map<String, Object> map) throws Exception;
	// 이전청구월 선택시 등록 가능한 건수 확인
	public int useClaimCnt(Map<String, Object> map) throws Exception;
	//청구수정 update 전에 전체삭제
	public void selClaimDeleteDetAll(HashMap<String, Object> smap) throws Exception;
	public void selClaimDeleteDetB(HashMap<String, Object> smap) throws Exception;

	public void updateClaimDay(Map<String, Object> map) throws Exception;

    void updateCusPreClaim(HashMap<String,Object> cmap) throws Exception;

	public String autoAtSendYn(Map<String, Object> map) throws Exception;

	public List<NotiSendDTO> autoAtList(Map<String, Object> map) throws Exception;
}
