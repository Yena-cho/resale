package com.finger.shinhandamoa.sys.rcpMgmt.service;

import java.util.HashMap;
import java.util.List;

import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysRcpStatDTO;

public interface SysRcpStatService {
	
	// 정산관리 > 수납통계 > 월별 전체수납통계 (WEB, API)
	public List<SysRcpStatDTO> totalCustStCount(HashMap<String, Object> map) throws Exception;
	// 정산관리 > 수납통계 > 월별 수납 리스트
	public List<SysRcpStatDTO> mothlyRcpList(HashMap<String, Object> map) throws Exception;
	// 정산관리 > 수납통계 > 월별 수납 합계  
	public List<SysRcpStatDTO> mothlyRcpListTot(HashMap<String, Object> map) throws Exception;
	// 정산관리 > 수납통계 > 일별 전체수납통계 (WEB, API)
	public List<SysRcpStatDTO> totalCustStDCount(HashMap<String, Object> map) throws Exception;
	// 정산관리 > 수납통계 > 일별 수납 리스트
	public List<SysRcpStatDTO> daylyRcpList(HashMap<String, Object> map) throws Exception;
	// 정산관리 > 수납통계 > 일별 수납 합계
	public List<SysRcpStatDTO> daylyRcpListTot(HashMap<String, Object> map) throws Exception;
	
}
