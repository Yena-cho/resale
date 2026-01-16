package com.finger.shinhandamoa.sys.rcpMgmt.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.sys.rcpMgmt.dao.SysSmsUsageDAO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysSmsUsageDTO;

@Service
public class SysSmsUsageServiceImpl implements SysSmsUsageService{

	@Autowired
	private SysSmsUsageDAO sysSmsUsageDAO;
	
	@Override
	public int sysSmsUsageFaCount(Map<String, Object> map) {
		return sysSmsUsageDAO.sysSmsUsageFaCount(map);
	}

	@Override
	public int sysSmsUsageTotalCount(Map<String, Object> map) {
		return sysSmsUsageDAO.sysSmsUsageTotalCount(map);
	}

	@Override
	public List<SysSmsUsageDTO> sysSmsUsageListAll(Map<String, Object> map) {
		return sysSmsUsageDAO.sysSmsUsageListAll(map);
	}




	
}
