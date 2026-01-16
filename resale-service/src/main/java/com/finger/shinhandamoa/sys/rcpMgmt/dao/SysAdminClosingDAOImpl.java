package com.finger.shinhandamoa.sys.rcpMgmt.dao;

import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysAdminClosingDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.web.SysAdminClosingController;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SysAdminClosingDAOImpl implements SysAdminClosingDAO{

	private static final Logger logger = LoggerFactory.getLogger(SysAdminClosingController.class);

	@Autowired
	private SqlSession sqlSession;

	/* 일마감 건수 */
	@Override
	public int getDayCloseCount(Map<String, Object> map) {
		return sqlSession.selectOne("AdminClosing.getDayCloseCount", map);
	}

	/* 월마감 건수 */
	@Override
	public int getMonthCloseCount(Map<String, Object> map) {
		return sqlSession.selectOne("AdminClosing.getMonthCloseCount", map);
	}

	/* 일마감 내역 조회 */
	@Override
	public List<SysAdminClosingDTO> getDayCloseList(Map<String, Object> map) throws Exception{
		logger.info("DAOIMPL!!");
		return sqlSession.selectList("AdminClosing.getDayCloseList", map);
	}

	/* 월마감 내역 조회*/
	@Override
	public List<SysAdminClosingDTO> getMonthCloseList(Map<String, Object> map) {
		return sqlSession.selectList("AdminClosing.getMonthCloseList", map);
	}

	@Override
	public void dayCloseGo(Map<String, Object> map) {
		sqlSession.selectOne("AdminClosing.dayCloseGo", map);
	}

	@Override
	public void monthCloseGo(Map<String, Object> map) {
		sqlSession.selectOne("AdminClosing.monthCloseGo", map);	
	}

	@Override
	public int countMonthlySettle(String month, String clientId, String clientName) {
		final Map<String, String> param = new HashMap<>();
		param.put("month", month);
		param.put("chaCd", clientId);
		param.put("chaName", clientName);
		return sqlSession.selectOne("AdminClosing.countMonthlySettle", param);
	}

	@Override
	public List<Map<String, Object>> selectMonthlySettle(String month, String clientId, String clientName, String orderBy, RowBounds rowBounds) {
		final Map<String, String> param = new HashMap<>();
		param.put("month", month);
		param.put("chaCd", clientId);
		param.put("chaName", clientName);
		param.put("orderBy", orderBy);
		return sqlSession.selectList("AdminClosing.selectMonthlySettle", param, rowBounds);
	}

	@Override
	public List<SysAdminClosingDTO> getVirtualAccountCalculateCount(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("AdminClosing.getVirtualAccountCalculateCount", map);
	}

	@Override
	public List<SysAdminClosingDTO> getVirtualAccountCalculateList(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("AdminClosing.getVirtualAccountCalculateList", map);
	}

	@Override
	public List<SysAdminClosingDTO> getVirtualAccountCalculateListExcel(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("AdminClosing.getVirtualAccountCalculateListExcel", map);
	}

	@Override
	public List<SysAdminClosingDTO> getVirtualAccountSettleDetCount(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("AdminClosing.getVirtualAccountSettleDetCount", map);
	}

	@Override
	public List<SysAdminClosingDTO> getVirtualAccountSettleDetList(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("AdminClosing.getVirtualAccountSettleDetList", map);
	}

	@Override
	public List<SysAdminClosingDTO> getVirtualAccountSettleMas(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("AdminClosing.getVirtualAccountSettleMas", map);
	}

	@Override
	public void updateVirtualAccountSettleMas(HashMap<String, Object> map) {
		sqlSession.selectList("AdminClosing.updateVirtualAccountSettleMas", map);
	}

	@Override
	public void insertVirtualAccountSettleDet(HashMap<String, Object> map) {
		sqlSession.selectList("AdminClosing.insertVirtualAccountSettleDet", map);
	}
}
