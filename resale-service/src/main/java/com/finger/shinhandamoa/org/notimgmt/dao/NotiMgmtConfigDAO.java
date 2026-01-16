package com.finger.shinhandamoa.org.notimgmt.dao;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.org.notimgmt.dto.NotiConfigDTO;

@Repository("notiConfigDao")
public class NotiMgmtConfigDAO {

	@Inject
	private SqlSession sqlSession;
	
	/**
	 * 고지서 설정 조회
	 * @param chacd
	 * @return
	 */
	public NotiConfigDTO selectXbillForm(String chaCd) {
		return sqlSession.selectOne("Organization.selectXbillForm", chaCd);
	}
	
	/**
	 * 고지서설정 조회 Ajax
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public NotiConfigDTO selectedXbillForm(HashMap<String, Object> map) {
		return sqlSession.selectOne("Organization.selectedXbillForm", map);
	}
	
	/**
	 * 고지서설정 수정 및 등록
	 * @param map
	 */
	public int saveNotiConfig(HashMap<String, Object> map) {
		return sqlSession.update("Organization.saveNotiConfig", map);
	}
	
	/**
	 *  고지서설정 수납기관, 연락처 조회
	 * @param chacd
	 * @return
	 */
	public HashMap<String, Object> selectXchalist(String chaCd) {
		return sqlSession.selectOne("Organization.selectXchalist", chaCd);
	}
}
