package com.finger.shinhandamoa.sys.rcpMgmt.dao;

import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysSmsUsageDTO;

public interface SysSmsUsageDAO {

	int sysSmsUsageFaCount(Map<String, Object> map);

	int sysSmsUsageTotalCount(Map<String, Object> map);

	List<SysSmsUsageDTO> sysSmsUsageListAll(Map<String, Object> map);

	

}
