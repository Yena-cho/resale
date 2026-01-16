package com.finger.shinhandamoa.sys.rcpMgmt.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.sys.cust.dto.SysCustDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysInvoiceDTO;

@Repository
public class SysInvoiceDAOImpl implements SysInvoiceDAO{
	
	@Autowired
	private SqlSession sqlSession;

	@Override
	public int sysInvoiceFaCount(Map<String, Object> map) {
		return sqlSession.selectOne("SysRcpStat.sysInvoiceFaCount", map);
	}

	@Override
	public int sysInvoiceTotalCount(Map<String, Object> map) {
		return sqlSession.selectOne("SysRcpStat.sysInvoiceFaCount", map);
	}

	@Override
	public List<SysInvoiceDTO> sysInvoiceListAll(Map<String, Object> map) {
		return sqlSession.selectList("SysRcpStat.sysInvoiceListAll", map);
	}

	
}
