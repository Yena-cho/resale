package com.finger.shinhandamoa.sys.rcpMgmt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.sys.rcpMgmt.dao.SysRcpStatDAO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysRcpStatDTO;

@Service
public class SysRcpStatServiceImpl implements SysRcpStatService{
	
	@Autowired
	private SysRcpStatDAO sysRcpStatDAO;
	
	//월별 전체수납통계 (WEB, API)
	@Override
	public List<SysRcpStatDTO> totalCustStCount(HashMap<String, Object> map) throws Exception {
		return sysRcpStatDAO.totalCustStCount(map);
	}
	//월별 수납 리스트
	@Override
	public List<SysRcpStatDTO> mothlyRcpList(HashMap<String, Object> map) throws Exception {
		return sysRcpStatDAO.mothlyRcpList(map);
	}
	//월별 수납 합계
	@Override
	public List<SysRcpStatDTO> mothlyRcpListTot(HashMap<String, Object> map) throws Exception {
		return sysRcpStatDAO.mothlyRcpListTot(map);
	}
	//일별 전체수납통계 (WEB, API)
	@Override
	public List<SysRcpStatDTO> totalCustStDCount(HashMap<String, Object> map) throws Exception {
		return sysRcpStatDAO.totalCustStDCount(map);
	}
	//일별 수납 리스트
	@Override
	public List<SysRcpStatDTO> daylyRcpList(HashMap<String, Object> map) throws Exception {
		return sysRcpStatDAO.daylyRcpList(map);
	}
	//일별 수납 합계
	@Override
	public List<SysRcpStatDTO> daylyRcpListTot(HashMap<String, Object> map) throws Exception {
		return sysRcpStatDAO.daylyRcpListTot(map);
	}


	
}
