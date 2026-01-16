package com.finger.shinhandamoa.sys.rcpMgmt.dao;

import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.sys.cust.dto.SysCustDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysAutoTransDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysInvoiceDTO;

public interface SysAutoTransDAO {

	int sysAutoTransFaCount(Map<String, Object> map);

	int sysAutoTransTotalCount(Map<String, Object> map);

	List<SysAutoTransDTO> sysAutoTransListAll(Map<String, Object> map);


	

}
