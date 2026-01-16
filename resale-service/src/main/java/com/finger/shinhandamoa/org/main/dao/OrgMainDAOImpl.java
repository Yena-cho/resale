package com.finger.shinhandamoa.org.main.dao;

import com.finger.shinhandamoa.org.main.dto.XmontSumDTO;
import com.finger.shinhandamoa.payer.payment.dto.PaymentDTO;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
@Repository
public class OrgMainDAOImpl implements OrgMainDAO {

	@Inject
	private SqlSession sqlSession;
	

	@Override
	public XmontSumDTO selXmontSum(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("OrgMainDao.selXmontSum", map);
	}

	@Override
	public List<PaymentDTO> selectPaymentSummary(Map<String, Object> reqMap) throws Exception {
		return sqlSession.selectList("OrgMainDao.selectPaymentSummary", reqMap, new RowBounds(0, 5));
	}

	@Override
	public XmontSumDTO selectPaymentRatio(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("OrgMainDao.selectPaymentRatio", map);
	}

}
