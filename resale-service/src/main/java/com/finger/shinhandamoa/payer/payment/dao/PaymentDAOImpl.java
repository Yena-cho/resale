package com.finger.shinhandamoa.payer.payment.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.payer.payment.dto.PaymentDTO;


/**
 * @author  by PYS
 * @date    2018. 4. 12.
 * @desc    
 * @version 
 * 
 */
@Repository
public class PaymentDAOImpl implements PaymentDAO {

	private static final Logger logger = LoggerFactory.getLogger(PaymentDAOImpl.class);
	
	@Autowired
	private SqlSession sqlSession;
	
	// 가맹점정보 가져오기
	@Override
	public PaymentDTO selectChaInfo(String vaNo) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vaNo", vaNo);
		return sqlSession.selectOne("Payment.selectChaInfo", map);
	}
		
	// 납부내역 총 개수 가져오기
	@Override
	public HashMap<String, Object> payListTotalCount(HashMap<String, Object> map) throws Exception {
		
		return sqlSession.selectOne("Payment.payListTotalCount", map);
	}

	// 납부내역 리스트 조회
	@Override
	public List<PaymentDTO> payList(HashMap<String, Object> map) throws Exception {
			
		return sqlSession.selectList("Payment.payList", map);
	}

	@Override
	public List<PaymentDTO> chkPayList(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectList("Payment.chkPayList", reqMap);
	}

	@Override
	public HashMap<String, Object> payListMaxMonth(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectOne("Payment.payListMaxMonth", reqMap);
	}

}
