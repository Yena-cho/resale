package com.finger.shinhandamoa.sys.addServiceMgmt.dao;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import com.finger.shinhandamoa.common.ListResult;
import com.finger.shinhandamoa.sys.cashreceipt.service.CashReceiptHistoryDTO;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.sys.addServiceMgmt.dto.XCashmasDTO;

@Repository("addServiceCashDao")
public class AddServiceCashDAO {

	@Inject
	private SqlSession sqlSession;
	
	/**
	 * 현금영수증 이용내역 조회
	 * @param map
	 * @return
	 */
	public List<XCashmasDTO> getCashHistory(HashMap<String, Object> map) {
		return sqlSession.selectList("AdditionalService.getCashHistory", map);
	}
	
	/**
	 * 현금영수증 이용내역 카운트
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int getCashHistoryCount(HashMap<String, Object> map) {
		return sqlSession.selectOne("AdditionalService.getCashHistoryCount", map);
	}

    public List<CashReceiptHistoryDTO> getCashReceiptHistory(HashMap<String,Object> map, RowBounds rowBounds) {
		return sqlSession.selectList("AdditionalService.getCashReceiptHistory", map, rowBounds);
    }

	public HashMap<String,Object> countCashReceiptHistory(HashMap<String, Object> map) {
		return sqlSession.selectOne("AdditionalService.countCashReceiptHistory", map);
	}
}
