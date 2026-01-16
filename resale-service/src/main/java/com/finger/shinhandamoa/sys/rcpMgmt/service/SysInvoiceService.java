package com.finger.shinhandamoa.sys.rcpMgmt.service;

import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysInvoiceDTO;

public interface SysInvoiceService {

	int sysInvoiceFaCount(Map<String, Object> map);

	int sysInvoiceTotalCount(Map<String, Object> map);

	List<SysInvoiceDTO> sysInvoiceListAll(Map<String, Object> map);


	
}
