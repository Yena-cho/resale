package com.finger.shinhandamoa.util.dao;

import java.util.HashMap;
import java.util.List;

import com.finger.shinhandamoa.util.dto.LayOutDTO;

/**
 * @author  by puki
 * @date    2018. 5. 13.
 * @desc    최초생성
 * @version 
 * 
 */
public interface LayOutDAO {
	// 출력의뢰 기관
	public List<LayOutDTO> selChaCd(HashMap<String, Object> map) throws Exception; 
	// 출력의뢰  데이터 - 공통
	public LayOutDTO printMsg(HashMap<String, Object> map) throws Exception; 
	// 출력의뢰 데이터 - 메시지
	public List<LayOutDTO> printMsgData(HashMap<String, Object> map) throws Exception;
	// 출력의뢰 데이터 - 청구항목
	public List<LayOutDTO> printItemData(HashMap<String, Object> map) throws Exception;
	// 출력의뢰 데이터 생성 - body
	public List<LayOutDTO> printReqData(HashMap<String, Object> map) throws Exception;
	// 출력의뢰 ftp 전송 후 상태코드 수정
	public void updateReqSt(HashMap<String, Object> map) throws Exception;
	// 출력의뢰 remark 
	public List<LayOutDTO> remarkMsgData(HashMap<String, Object> map) throws Exception;
}
