package com.finger.shinhandamoa.sys.main.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.org.main.dto.XmontSumDTO;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
@Repository
public class SysMainDAOImpl implements SysMainDAO {

	@Inject
	private SqlSession sqlSession;
	

	@Override
	public XmontSumDTO getSysMainInfo01(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("SysMainDao.getSysMainInfo01", map);
	}

	@Override
	public List<XmontSumDTO> selectPaymentSummary(Map<String, Object> reqMap) throws Exception {
		return sqlSession.selectList("SysMainDao.selectPaymentSummary", reqMap);
	}

	@Override
	public List<XmontSumDTO> selectPaymentRatioList(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("SysMainDao.selectPaymentRatioList", map);
	}

	@Override
	public List<String> selectRcpMasCdNextVal()throws Exception {
		return sqlSession.selectList("SysMainDao.selectRcpMasCdNextVal");
	}
	
	
}
