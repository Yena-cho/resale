package com.finger.shinhandamoa.org.notimgmt.service;

import java.util.HashMap;
import java.util.List;

import com.finger.shinhandamoa.org.notimgmt.dto.NotiConfigDTO;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtPrintResDTO;

public interface NotiMgmtPrintService {

	/**
	 * 고지서 출력의뢰 주소조회
	 * @param chacd
	 * @return
	 * @throws Exception
	 */
	public NotiMgmtPrintResDTO selectReqAddress(String chaCd) throws Exception;
	
	/**
	 * 고지출력 의뢰건수 체크
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int printCount(HashMap<String, Object> map) throws Exception;
	/**
	 * 고지서 출력의뢰 리스트
	 * @param chacd
	 * @return
	 * @throws Exception
	 */
	public List<NotiMgmtPrintResDTO> pageSelectReqNotiMas(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 선택한 월 고지서 출력의뢰 건수
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,Object> selectMonReqNotiMas(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 고지서 출력의뢰 리스트 카운트
	 * @param chaCd
	 * @return
	 * @throws Exception
	 */
	public int countReqNotiMas(String chaCd) throws Exception;
	/**
	 * 고지서출력의뢰 
	 * @param body
	 * @return Exception
	 */
	public int selectXnotimasReq(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 고지서출력의뢰 삭제
	 * * @param body
	 * @return Exception
	 */
	public int deleteXnotimasReq(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 고지서출력의뢰 저장
	 * @param body
	 * @return Exception
	 */
	public int saveXnotimasReq(HashMap<String, Object> map) throws Exception;


	/**
	 * 고지서 출력의뢰 상세 정보 저장
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int saveXnotimasReqDet(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 고지내용 row check
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public NotiConfigDTO cont1LengthCheck(HashMap<String, Object> map) throws Exception;
}
