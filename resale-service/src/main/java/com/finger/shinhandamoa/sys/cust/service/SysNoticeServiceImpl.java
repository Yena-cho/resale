package com.finger.shinhandamoa.sys.cust.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.sys.cust.dao.SysNoticeDAO;
import com.finger.shinhandamoa.sys.cust.dto.SysNoticeDTO;

@Service
public class SysNoticeServiceImpl implements SysNoticeService{

	@Inject
	private SysNoticeDAO sysNoticeDAO;

	@Override
	public int sysSmsCustCount(Map<String, Object> map) {
		return sysNoticeDAO.sysSmsCustCount(map);
	}

	@Override
	public int sysInsertSmsReq(HashMap<String, Object> map) {
		return sysNoticeDAO.sysInsertSmsReq(map);
	}

	@Override
	public List<SysNoticeDTO> sysGetCustList(Map<String, Object> map) {
		return sysNoticeDAO.sysGetCustList(map);
	}

	@Override
	public int sysSmsMngCount(Map<String, Object> map) {
		return sysNoticeDAO.sysSmsMngCount(map);
	}

	@Override
	public List<SysNoticeDTO> sysSmsMngList(Map<String, Object> map) {
		return sysNoticeDAO.sysSmsMngList(map);
	}

	@Override
	public List<SysNoticeDTO> sysSmsMngFailCount(Map<String, Object> map) {
		return sysNoticeDAO.sysSmsMngFailCount(map);
	}

	@Override
	public List<SysNoticeDTO> selNoticeList(Map<String, Object> map) {
		return sysNoticeDAO.selNoticeList(map);
	}

	@Override
	public int updateStatus(HashMap<String, Object> map) {
		return sysNoticeDAO.updateStatus(map);
	}

	@Override
	public int sysInsertSmsList(HashMap<String, Object> map) {
		return sysNoticeDAO.sysInsertSmsList(map);
	}

	@Override
	public int maxSmsSeq(Map<String, Object> map) {
		return sysNoticeDAO.maxSmsSeq(map);
	}

	@Override
	public int updateFailCnt(HashMap<String, Object> map) {
		return sysNoticeDAO.updateFailCnt(map);
	}

	@Override
	public List<SysNoticeDTO> selSendList(Map<String, Object> map) {
		return sysNoticeDAO.selSendList(map);
	}

	@Override
	public List<SysNoticeDTO> sysGetCustEmList(Map<String, Object> map) {
		
		return sysNoticeDAO.sysGetCustEmList(map);
	}

	@Override
	public int maxEmailSeq(Map<String, Object> map) {
		return sysNoticeDAO.maxEmailSeq(map);
	}

	@Override
	public int sysInsertEmailReq(HashMap<String, Object> map) {
		return sysNoticeDAO.sysInsertEmailReq(map);
	}

	@Override
	public void sysInsertEmailHist(HashMap<String, Object> map) {
		sysNoticeDAO.sysInsertEmailHist(map);
	}

	@Override
	public int sysEmailMngCount(Map<String, Object> map) {
		return sysNoticeDAO.sysEmailMngCount(map);
	}

	@Override
	public List<SysNoticeDTO> selEmNoticeList(Map<String, Object> map) {
		return sysNoticeDAO.selEmNoticeList(map);
	}

	@Override
	public List<SysNoticeDTO> sysEmFailCount(Map<String, Object> map) {
		return sysNoticeDAO.sysEmFailCount(map);
	}

	@Override
	public void sysEmFailCountUpd(Map<String, Object> map) {
		sysNoticeDAO.sysEmFailCountUpd(map);
	}
	
}
