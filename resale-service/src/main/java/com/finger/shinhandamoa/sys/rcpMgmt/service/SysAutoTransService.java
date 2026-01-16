package com.finger.shinhandamoa.sys.rcpMgmt.service;

import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysAutoTransDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysInvoiceDTO;

public interface SysAutoTransService {

	int sysAutoTransFaCount(Map<String, Object> map);

	int sysAutoTransTotalCount(Map<String, Object> map);

	List<SysAutoTransDTO> sysAutoTransListAll(Map<String, Object> map);

	
}
