package com.finger.shinhandamoa.sys.rcpMgmt.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysSmsUsageDTO;

@Repository
public class SysSmsUsageDAOImpl implements SysSmsUsageDAO{
	
	@Autowired
	private SqlSession sqlSession;

	@Override
	public int sysSmsUsageFaCount(Map<String, Object> map) {
		return sqlSession.selectOne("SysRcpStat.sysSmsUsageFaCount", map);
	}

	@Override
	public int sysSmsUsageTotalCount(Map<String, Object> map) {
		return sqlSession.selectOne("SysRcpStat.sysSmsUsageTotalCount", map);
	}

	@Override
	public List<SysSmsUsageDTO> sysSmsUsageListAll(Map<String, Object> map) {
		return sqlSession.selectList("SysRcpStat.sysSmsUsageListAll", map);
	}



	
}
