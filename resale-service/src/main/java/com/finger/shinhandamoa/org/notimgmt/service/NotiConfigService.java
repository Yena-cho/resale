package com.finger.shinhandamoa.org.notimgmt.service;

import java.util.HashMap;

import com.finger.shinhandamoa.org.notimgmt.dto.NotiConfigDTO;

public interface NotiConfigService {

	/**
	 * 고지서설정 조회
	 * @param chacd
	 * @return
	 * @throws Exception
	 */
	public NotiConfigDTO selectXbillForm(String chaCd) throws Exception;
	
	/**
	 * 고지서설정 조회 Ajax
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public NotiConfigDTO selectedXbillForm(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 고지서설정 수정 및 등록
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int saveNotiConfig(HashMap<String, Object> map) throws Exception;
	
	
	/**
	 * 고지서설정 수납기관, 연락처 조회
	 * @param chacd
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> selectXchalist(String chaCd) throws Exception;
}
