package com.finger.shinhandamoa.sys.cust.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.sys.cust.dto.SysNoticeDTO;

public interface SysNoticeService {

	int sysSmsCustCount(Map<String, Object> map);

	int sysInsertSmsReq(HashMap<String, Object> map);

	List<SysNoticeDTO> sysGetCustList(Map<String, Object> map);

	int sysSmsMngCount(Map<String, Object> map);

	List<SysNoticeDTO> sysSmsMngList(Map<String, Object> map);

	List<SysNoticeDTO> sysSmsMngFailCount(Map<String, Object> map);

	List<SysNoticeDTO> selNoticeList(Map<String, Object> map);

	int updateStatus(HashMap<String, Object> map);

	int sysInsertSmsList(HashMap<String, Object> map);

	int maxSmsSeq(Map<String, Object> map);

	int updateFailCnt(HashMap<String, Object> map);

	List<SysNoticeDTO> selSendList(Map<String, Object> map);

	List<SysNoticeDTO> sysGetCustEmList(Map<String, Object> map);

	int maxEmailSeq(Map<String, Object> map);

	int sysInsertEmailReq(HashMap<String, Object> map);

	void sysInsertEmailHist(HashMap<String, Object> map);

	int sysEmailMngCount(Map<String, Object> map);

	List<SysNoticeDTO> selEmNoticeList(Map<String, Object> map);

	List<SysNoticeDTO> sysEmFailCount(Map<String, Object> map);

	void sysEmFailCountUpd(Map<String, Object> map);


	
}
