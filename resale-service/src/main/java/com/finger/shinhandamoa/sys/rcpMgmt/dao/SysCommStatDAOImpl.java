package com.finger.shinhandamoa.sys.rcpMgmt.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysCommStatDTO;

@Repository
public class SysCommStatDAOImpl implements SysCommStatDAO{
	
	@Autowired
	private SqlSession sqlSession;

	@Override
	public List<SysCommStatDTO> sysCommStatTotalCount(Map<String, Object> map) {
		return sqlSession.selectList("SysRcpStat.sysCommStatTotalCount", map);
	}

	@Override
	public List<SysCommStatDTO> sysCommStatListAll(Map<String, Object> map) {
		return sqlSession.selectList("SysRcpStat.sysCommStatListAll", map);
	}

	@Override
	public List<SysCommStatDTO> sysCommStatListFG(Map<String, Object> map) {
		return sqlSession.selectList("SysRcpStat.sysCommStatListFG", map);
	}

	@Override
	public List<SysCommStatDTO> sysCommStatListTot(Map<String, Object> map) {
		return sqlSession.selectList("SysRcpStat.sysCommStatListTot", map);
	}

	@Override
	public List<SysCommStatDTO> sysCommStatListFGTot(Map<String, Object> map) {
		return sqlSession.selectList("SysRcpStat.sysCommStatListFGTot", map);
	}




	
}
