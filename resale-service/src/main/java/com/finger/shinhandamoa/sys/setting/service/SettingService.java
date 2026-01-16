package com.finger.shinhandamoa.sys.setting.service;

import java.util.HashMap;
import java.util.List;

import com.finger.shinhandamoa.sys.setting.dto.AdminInfoDTO;

/**
 * @author  by puki
 * @date    2018. 5. 21.
 * @desc    최초생성
 * @version 
 * 
 */
public interface SettingService {

	// 계정.권한관리 목록 total count
	public int selAccAuthCnt(HashMap<String, Object> map) throws Exception;
	// 계정.권한관리 목록 
	public List<AdminInfoDTO> selAccAuthList(HashMap<String, Object> map) throws Exception;
	// 계정.권한관리 상세보기
	public AdminInfoDTO selAccAuthDetail(String admId) throws Exception;
	// 계정.권한관리 등록 및 수정
	public void accAuthModify(HashMap<String, Object> map) throws Exception;
	// 계정.권한관리 삭제
	public void accAuthDelete(String admId) throws Exception;
	// 계정.권한관리 - 아이디중복확인
	public int selAdmId(String admId) throws Exception;
}
