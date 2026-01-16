package com.finger.shinhandamoa.org.notimgmt.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.org.notimgmt.dto.NotiConfigDTO;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtPrintResDTO;

@Repository("notiMgmtPrintDao")
public class NotiMgmtPrintDAO {

	@Inject
	private SqlSession sqlSession;
	
	/**
	 * 고지서 출력의뢰 주소조회
	 * @param chacd
	 * @return
	 */
	public NotiMgmtPrintResDTO selectReqAddress(String chaCd) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("chaCd", chaCd);
		return sqlSession.selectOne("Organization.selectReqAddress", map);
	}
	
	/**
	 * 고지서 출력의뢰 리스트
	 * @param chacd
	 * @return
	 */
	public List<NotiMgmtPrintResDTO> pageSelectReqNotiMas(HashMap<String,Object> map) {
		return sqlSession.selectList("Organization.pageSelectReqNotiMas", map);
	}
	
	/**
	 * 해당 월 출력의뢰 건수
	 * @param map
	 * @return
	 */
	public HashMap<String,Object> selectMonReqNotiMas(HashMap<String, Object> map) {
		return sqlSession.selectOne("Organization.selectMonReqNotiMas", map);
	}
	
	/**
	 * 고지서 출력의뢰 카운트
	 * @param chaCd
	 * @return
	 */
	public int countReqNotiMas(String chaCd) {
		return sqlSession.selectOne("Organization.countReqNotiMas", chaCd);
	}
	
	/**
	 * 고지서출력의뢰 
	 * @param body
	 * @return
	 */
	public int selectXnotimasReq(HashMap<String, Object> map) {
		return sqlSession.selectOne("Organization.selectXnotimasReq", map);
	}
	
	/**
	 * 고지서출력의뢰 삭제
	 * * @param body
	 * @return
	 */
	public int deleteXnotimasReq(HashMap<String, Object> map) {
		return sqlSession.update("Organization.deleteXnotimasReq", map);
	}
	
	/**
	 * 고지서출력의뢰 저장
	 * @param body
	 * @return
	 */
	public int saveXnotimasReq(HashMap<String, Object> map) {
		return sqlSession.insert("Organization.saveXnotimasReq", map);
	}

	public int saveXnotimasReqDet(HashMap<String, Object> map) {
		return sqlSession.insert("Organization.saveXnotimasReqDet", map);
	}
	
	public NotiConfigDTO cont1LengthCheck(HashMap<String, Object> map) {
		return sqlSession.selectOne("Organization.cont1LengthCheck", map);
	}
	
	public int printCount(HashMap<String, Object> map) {
		return sqlSession.selectOne("Organization.printCount", map);
	}
}
