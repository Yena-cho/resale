package com.finger.shinhandamoa.sys.rcpMgmt.dao;

import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.sys.cust.dto.SysCustDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysInvoiceDTO;

public interface SysInvoiceDAO {

	int sysInvoiceFaCount(Map<String, Object> map);

	int sysInvoiceTotalCount(Map<String, Object> map);

	List<SysInvoiceDTO> sysInvoiceListAll(Map<String, Object> map);

	

}
