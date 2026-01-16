package com.finger.shinhandamoa.sys.cust.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.sys.cust.dto.SysNoticeDTO;

@Repository
public class SysNoticeDAOImpl implements SysNoticeDAO {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public int sysSmsCustCount(Map<String, Object> map) {
		return sqlSession.selectOne("SysNotice.sysSmsCustCount", map);
	}

	@Override
	public int sysInsertSmsReq(HashMap<String, Object> map) {
		return sqlSession.insert("SysNotice.sysInsertSmsReq", map);
	}

	@Override
	public List<SysNoticeDTO> sysGetCustList(Map<String, Object> map) {
		return sqlSession.selectList("SysNotice.sysGetCustList", map);
	}

	@Override
	public int sysSmsMngCount(Map<String, Object> map) {
		return sqlSession.selectOne("SysNotice.sysSmsMngCount", map);
	}

	@Override
	public List<SysNoticeDTO> sysSmsMngList(Map<String, Object> map) {
		return sqlSession.selectList("SysNotice.sysSmsMngList", map);
	}

	@Override
	public List<SysNoticeDTO> sysSmsMngFailCount(Map<String, Object> map) {
		return sqlSession.selectList("SysNotice.sysSmsMngFailCount",map);
	}

	@Override
	public List<SysNoticeDTO> selNoticeList(Map<String, Object> map) {
		return sqlSession.selectList("SysNotice.selNoticeList", map);
	}

	@Override
	public int updateStatus(HashMap<String, Object> map) {
		return sqlSession.update("SysNotice.updateStatus", map);
	}
	@Override
	public int sysInsertSmsList(HashMap<String, Object> map) {
		return sqlSession.insert("SysNotice.sysInsertSmsList", map);
	}

	@Override
	public int maxSmsSeq(Map<String, Object> map) {
		return sqlSession.selectOne("SysNotice.maxSmsSeq", map);
	}

	@Override
	public int updateFailCnt(HashMap<String, Object> map) {
		int value=sqlSession.selectOne("SysNotice.selFailCnt", map);
		map.put("fcount", value);
		return sqlSession.update("SysNotice.updateFailCnt", map);
	}

	@Override
	public List<SysNoticeDTO> selSendList(Map<String, Object> map) {
		return sqlSession.selectList("SysNotice.selSendList", map);
	}

	@Override
	public List<SysNoticeDTO> sysGetCustEmList(Map<String, Object> map) {
		return sqlSession.selectList("SysNotice.sysGetCustEmList", map);
	}

	@Override
	public int maxEmailSeq(Map<String, Object> map) {
		return sqlSession.selectOne("SysNotice.maxEmailSeq", map);
	}

	@Override
	public int sysInsertEmailReq(HashMap<String, Object> map) {
		return sqlSession.insert("SysNotice.sysInsertEmailReq", map);
	}

	@Override
	public void sysInsertEmailHist(HashMap<String, Object> map) {
		sqlSession.insert("SysNotice.sysInsertEmailHist", map);
	}

	@Override
	public int sysEmailMngCount(Map<String, Object> map) {
		return sqlSession.selectOne("SysNotice.sysEmailMngCount", map);
	}

	@Override
	public List<SysNoticeDTO> selEmNoticeList(Map<String, Object> map) {
		return sqlSession.selectList("SysNotice.selEmNoticeList", map);
	}

	@Override
	public List<SysNoticeDTO> sysEmFailCount(Map<String, Object> map) {
		return sqlSession.selectList("SysNotice.sysEmFailCount", map);
	}

	@Override
	public void sysEmFailCountUpd(Map<String, Object> map) {
		sqlSession.update("SysNotice.sysEmFailCountUpd", map);
	}
	
}
