package com.finger.shinhandamoa.sys.rcpMgmt.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.sys.rcpMgmt.dao.SysCommStatDAO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysCommStatDTO;

@Service
public class SysCommStatServiceImpl implements SysCommStatService{
	
	@Inject
	private SysCommStatDAO sysCommStatDAO;

	@Override
	public List<SysCommStatDTO> sysCommStatTotalCount(Map<String, Object> map) {
		return sysCommStatDAO.sysCommStatTotalCount(map);
	}

	@Override
	public List<SysCommStatDTO> sysCommStatListAll(Map<String, Object> map) {
		return sysCommStatDAO.sysCommStatListAll(map);
	}

	@Override
	public List<SysCommStatDTO> sysCommStatListFG(Map<String, Object> map) {
		return sysCommStatDAO.sysCommStatListFG(map);
	}

	@Override
	public List<SysCommStatDTO> sysCommStatListTot(Map<String, Object> map) {
		return sysCommStatDAO.sysCommStatListTot(map);
	}

	@Override
	public List<SysCommStatDTO> sysCommStatListFGTot(Map<String, Object> map) {
		return sysCommStatDAO.sysCommStatListFGTot(map);
	}



	
}
