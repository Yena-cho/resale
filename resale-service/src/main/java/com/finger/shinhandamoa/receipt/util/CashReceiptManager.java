package com.finger.shinhandamoa.receipt.util;

import org.apache.ibatis.session.SqlSession;

import com.finger.shinhandamoa.data.ConnectionFactory;
import com.finger.shinhandamoa.data.mapper.CashReceiptMapper;
import com.finger.shinhandamoa.data.model.CashReceiptMaster;
import com.finger.shinhandamoa.data.model.CashReceiptMasterExample;

/**
 * 현금영수증 KIS 전송데이터 
 * 파일 -> DB 저장 관련 유틸
 * @author jhjeong@finger.co.kr
 * @modified 2018. 10. 25.
 */
public class CashReceiptManager {

	final SqlSession sqlSession;
	final CashReceiptMapper mapper;
	
	public CashReceiptManager() {
		sqlSession = ConnectionFactory.getSqlSessionFactory().openSession(false);
		mapper = sqlSession.getMapper(CashReceiptMapper.class);
	}
	
	public void close() {
		sqlSession.close();
	}
	
	public void commit() {
		sqlSession.commit();
	}
	
	public void commitAfterClose() {
		sqlSession.commit();
		sqlSession.close();
	}
	
	/**
	 * FW_CASH_RECEIPT_MASTER 테이블의 데이터를 생성
	 * @param record
	 * @return
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 10. 25.
	 */
	public int insertCashReceiptData(CashReceiptMaster record) {
		return mapper.insert(record);
	}

	/**
	 * FW_CASH_RECEIPT_MASTER 테이블의 데이터를 생성후 PK를 recode에 담는다.
	 * @param record
	 * @return
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 10. 25.
	 */
	public int insertCashReceiptDataSelective(CashReceiptMaster record) {
		return mapper.insertSelective(record);
	}
	
	/**
	 * FW_CASH_RECEIPT_MASTER 테이블의 행 카운트
	 * @param example 조회조건
	 * @return
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 10. 25.
	 */
	public long countCashReceiptDataByExample(CashReceiptMasterExample example) {
		return mapper.countByExample(example);
	}
}
