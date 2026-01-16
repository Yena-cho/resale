package com.finger.shinhandamoa.home.data;

import java.util.HashMap;
import java.util.List;

import com.finger.shinhandamoa.main.dto.NoticeDTO;

public interface DataDAO {
	// WEBUSER 비밀번호 암호화
	public void orgPasswordUpdate(HashMap<String, Object> map) throws Exception;
	// XCUSMAS pass_vano, pass_cushp 암호화 
	public void orgCusPassUpdate(HashMap<String, Object> map);
	// 테스트 데이터  생성
	public void cusInfoShaEncoder(HashMap<String, Object> map) throws Exception;
	// payer 비밀번호 암호화
	public void payerPasswordUpdate(HashMap<String, Object> map) throws Exception;
	
	
	
	public List<NoticeDTO> list(HashMap<String, Object> map) throws Exception;
	public void updateCrypt(HashMap<String, Object> map) throws Exception;
	public void insertCrypt(HashMap<String, Object> map) throws Exception;
}
