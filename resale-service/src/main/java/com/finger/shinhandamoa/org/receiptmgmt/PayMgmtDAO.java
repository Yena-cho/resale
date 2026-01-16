package com.finger.shinhandamoa.org.receiptmgmt;

import com.finger.shinhandamoa.org.receiptmgmt.dto.PayMgmtDTO;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("payMgmtDao")
public class PayMgmtDAO {

	@Inject
	private SqlSession sqlSession;
	
	/**
	 * 가상계좌 입금내역 카운트
	 * @param map
	 * @return
	 */
	public HashMap<String, Object> getPaymentCount(Map<String, Object> map) {
		return sqlSession.selectOne("ReceiptMgmt.getPaymentCount", map);
	}
	
	/**
	 * 가상계좌 입금내역 리스트
	 * @param map
	 * @return
	 */
	public List<PayMgmtDTO> getPaymentList(Map<String, Object> map) {
		int start = NumberUtils.toInt(String.valueOf(map.get("start")), 0);
		int end = NumberUtils.toInt(String.valueOf(map.get("end")), Integer.MAX_VALUE-1);
		int offset = start -1;
		int limit = end-start+1;
		
		return sqlSession.selectList("ReceiptMgmt.getPaymentList", map, new RowBounds(offset, limit));
	}

	/**
	 * 온라인카드 입금내역 건수
	 * @param map
	 * @return
	 */
	public HashMap<String, Object> getCardPayCount(HashMap<String, Object> map) {
		return sqlSession.selectOne("ReceiptMgmt.getCardPayCount", map);
	}

	/**
	 * 온라인카드 입금내역 리스트
	 * @param map
	 * @return
	 */
	public List<PayMgmtDTO> getCardPayList(HashMap<String, Object> map) {
		return sqlSession.selectList("ReceiptMgmt.getCardPayList", map);
	}
}
