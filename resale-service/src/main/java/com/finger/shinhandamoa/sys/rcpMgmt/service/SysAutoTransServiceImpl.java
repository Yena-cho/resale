package com.finger.shinhandamoa.sys.rcpMgmt.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.sys.rcpMgmt.dao.SysAutoTransDAO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysAutoTransDTO;

@Service
public class SysAutoTransServiceImpl implements SysAutoTransService{
	
	@Inject
	private SysAutoTransDAO sysAutoTransDAO;

	@Override
	public int sysAutoTransFaCount(Map<String, Object> map) {
		return sysAutoTransDAO.sysAutoTransFaCount(map);
	}

	@Override
	public int sysAutoTransTotalCount(Map<String, Object> map) {
		return sysAutoTransDAO.sysAutoTransTotalCount(map);
	}

	@Override
	public List<SysAutoTransDTO> sysAutoTransListAll(Map<String, Object> map) {
		return sysAutoTransDAO.sysAutoTransListAll(map);
	}



	
}
