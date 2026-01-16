package com.finger.shinhandamoa.sys.rcpMgmt.service;

import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysCommStatDTO;

public interface SysCommStatService {

	List<SysCommStatDTO> sysCommStatTotalCount(Map<String, Object> map);

	List<SysCommStatDTO> sysCommStatListAll(Map<String, Object> map);

	List<SysCommStatDTO> sysCommStatListFG(Map<String, Object> map);

	List<SysCommStatDTO> sysCommStatListTot(Map<String, Object> map);

	List<SysCommStatDTO> sysCommStatListFGTot(Map<String, Object> map);

	
}
