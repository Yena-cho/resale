package com.finger.shinhandamoa.sys.rcpMgmt.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.sys.cust.dto.SysCustDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysAutoTransDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysInvoiceDTO;

@Repository
public class SysAutoTransDAOImpl implements SysAutoTransDAO{
	
	@Autowired
	private SqlSession sqlSession;

	@Override
	public int sysAutoTransFaCount(Map<String, Object> map) {
		return sqlSession.selectOne("SysRcpStat.sysAutoTransFaCount", map);
	}

	@Override
	public int sysAutoTransTotalCount(Map<String, Object> map) {
		return sqlSession.selectOne("SysRcpStat.sysAutoTransTotalCount", map);
	}

	@Override
	public List<SysAutoTransDTO> sysAutoTransListAll(Map<String, Object> map) {
		return sqlSession.selectList("SysRcpStat.sysAutoTransListAll", map);
	}


	
}
