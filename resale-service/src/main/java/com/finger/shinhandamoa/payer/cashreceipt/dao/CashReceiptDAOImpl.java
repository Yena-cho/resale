package com.finger.shinhandamoa.payer.cashreceipt.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.payer.cashreceipt.dto.CashReceiptDTO;
import com.finger.shinhandamoa.payer.notification.dto.NotificationDTO;


/**
 * @author 
 * @date   
 * @desc    
 * @version 
 * 
 */
@Repository
public class CashReceiptDAOImpl implements CashReceiptDAO {

	private static final Logger logger = LoggerFactory.getLogger(CashReceiptDAOImpl.class);
	
	@Autowired
	private SqlSession sqlSession;

	//현금영수증내역 총 갯수
	@Override
	public HashMap<String, Object> cashReceiptReqListTotalCount(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectOne("CashReceipt.cashReceiptReqListTotalCount", reqMap);
	}
	
	//현금영수증내역 리스트
	@Override
	public List<CashReceiptDTO> cashReceiptReqList(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectList("CashReceipt.cashReceiptReqList", reqMap);
	}

	@Override
	public CashReceiptDTO cashInfo(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectOne("CashReceipt.cashInfo", reqMap);
	}
	
}
