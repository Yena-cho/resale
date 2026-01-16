package com.finger.shinhandamoa.sys.rcpMgmt.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.org.custMgmt.dao.CustMgmtDAO;
import com.finger.shinhandamoa.sys.cust.dto.SysCustDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysInvoiceDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysRcpStatDTO;

@Repository
public class SysRcpStatDAOImpl implements SysRcpStatDAO{
	
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<SysRcpStatDTO> totalCustStCount(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("SysRcpStat.totalCustStCount", map);
	}
	@Override
	public List<SysRcpStatDTO> mothlyRcpList(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("SysRcpStat.mothlyRcpList", map);
	}
	@Override
	public List<SysRcpStatDTO> mothlyRcpListTot(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("SysRcpStat.mothlyRcpListTot", map);
	}
	@Override
	public List<SysRcpStatDTO> totalCustStDCount(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("SysRcpStat.totalCustStDCount", map);
	}
	@Override
	public List<SysRcpStatDTO> daylyRcpList(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("SysRcpStat.daylyRcpList", map);
	}
	@Override
	public List<SysRcpStatDTO> daylyRcpListTot(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("SysRcpStat.daylyRcpListTot", map);
	}	
}
