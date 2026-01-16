package com.finger.shinhandamoa.sys.rcpMgmt.service;

import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysInvoiceDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysSmsUsageDTO;

public interface SysSmsUsageService {

	int sysSmsUsageFaCount(Map<String, Object> map);

	int sysSmsUsageTotalCount(Map<String, Object> map);

	List<SysSmsUsageDTO> sysSmsUsageListAll(Map<String, Object> map);


}
