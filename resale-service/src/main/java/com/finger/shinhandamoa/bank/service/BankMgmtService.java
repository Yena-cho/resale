package com.finger.shinhandamoa.bank.service;

import com.finger.shinhandamoa.bank.dto.BankReg01DTO;
import com.finger.shinhandamoa.common.CompanyStatusLookupAPI.IFX1002;
import com.finger.shinhandamoa.data.table.model.KftcCode;
import com.finger.shinhandamoa.sys.chaMgmt.dto.SysChaMgmtDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author wisehouse@finger.co.kr
 */
public interface BankMgmtService {
	/**
	 * 지점검색 total count
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int getBranchListTotalCount(HashMap<String, Object> map) throws Exception;

	/**
	 * 지점검색
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<BankReg01DTO> getBranchListAll(HashMap<String, Object> map) throws Exception;

	/**
	 * 기관검색 total count
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int getCollectorListTotalCount(HashMap<String, Object> map) throws Exception;

	/**
	 * 기관검색
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<BankReg01DTO> getCollectorListAll(HashMap<String, Object> map) throws Exception;

	/**
	 * 가입승인관리 total count
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int getNewChaListTotalCount(HashMap<String, Object> map) throws Exception;

	/**
	 * 가입승인관리
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<BankReg01DTO> getNewChaListAll(HashMap<String, Object> map) throws Exception;

	/**
	 * 기관정보 상세보기
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	BankReg01DTO selectChaListInfo(HashMap<String, Object> map) throws Exception;

	/**
	 * 기관정보 상세보기
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	SysChaMgmtDTO selectChaListDetail(HashMap<String, Object> map) throws Exception;

	/**
	 * 기관정보 상세보기 - 다계좌
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<HashMap<String, Object>> getAgencyList(HashMap<String, Object> map) throws Exception;

	/**
	 * 가입승인관리 - 승인코드 변경
	 * 
	 * @param map
	 * @throws Exception
	 */
	void updateChaSt(HashMap<String, Object> map) throws Exception;

	/**
	 * 수수료조회 - total count
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	BankReg01DTO getBankFeeListTotalCount(HashMap<String, Object> map) throws Exception;

	/**
	 * 수수료조회
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<BankReg01DTO> getBankFeeListAll(HashMap<String, Object> map) throws Exception;
	/* 기관정보 수정 */
	void updateXChaList(BankReg01DTO dto) throws Exception;

	void updateChaListInfo(HashMap<String, Object> map) throws Exception;

	IFX1002 chkChaConfirmManual(BankReg01DTO dto) throws Exception;

    void insertJobhistory(HashMap<String, Object> map) throws Exception;

	void updateWebuser(SysChaMgmtDTO dto) throws Exception;

    List<KftcCode> getBnkList(Map<String, Object> paramMap) throws Exception;

	/**
	 * 기관정보 등록
	 *
	 * @param dto
	 * @throws Exception
	 */
    String insertXchalist(SysChaMgmtDTO dto) throws Exception;

	/**
	 * 기관정보 수정
	 *
	 * @param dto
	 * @throws Exception
	 */
	void updateXchaList(SysChaMgmtDTO dto) throws Exception;
}
