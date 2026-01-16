package com.finger.shinhandamoa.sys.rcpMgmt.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.sys.rcpMgmt.dao.SysInvoiceDAO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysInvoiceDTO;

@Service
public class SysInvoiceServiceImpl implements SysInvoiceService{
	
	@Autowired
	private SysInvoiceDAO sysInvoiceDAO;

	@Override
	public int sysInvoiceFaCount(Map<String, Object> map) {
		return sysInvoiceDAO.sysInvoiceFaCount(map);
	}

	@Override
	public int sysInvoiceTotalCount(Map<String, Object> map) {
		return sysInvoiceDAO.sysInvoiceTotalCount(map);
	}

	@Override
	public List<SysInvoiceDTO> sysInvoiceListAll(Map<String, Object> map) {
		return sysInvoiceDAO.sysInvoiceListAll(map);
	}



	
}
