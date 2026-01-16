package com.finger.shinhandamoa.org.mypage.dao;

import com.finger.shinhandamoa.org.mypage.dto.MyPageSumDTO;
import com.finger.shinhandamoa.org.mypage.dto.PaymentStatisticsDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;


/**
 * 마이페이지 이용료 조회
 * @author suhlee
 *
 */
@Repository("myPageSumDAO")
public class MyPageSumDAO {

	@Inject
	private SqlSession sqlSession;
	
	public String test() {
		return sqlSession.selectOne("MyPageSum.test");
	}
	
	/**
	 * 마이페이지 이용료 조회 (월별)
	 * @param map
	 * @return
	 */
	public List<MyPageSumDTO> getXmonthSum(HashMap<String, Object> map) {
		return sqlSession.selectList("MyPageSum.getXmonthSum", map);
	}
	
	/**
	 * 마이페이지 이용료조회 카운트(월별)
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getXmonthSumCount(HashMap<String, Object> map) {
		return sqlSession.selectOne("MyPageSum.getXmonthSumCount", map);
	}
	
	/**
	 * 이번달 이용현황
	 * @param map
	 * @return
	 */
	public MyPageSumDTO getXmonthState(HashMap<String, Object> map) {
		return sqlSession.selectOne("MyPageSum.getXmonthState", map);
	}
	
	/**
	 * 수수료 출금 내역
	 * @param map
	 * @return
	 */
	public List<MyPageSumDTO> getXmonthSumInfo(HashMap<String, Object> map) {
		return sqlSession.selectList("MyPageSum.getXmonthSumInfo", map);
	}
	
	/**
	 * 다모아 이용료 영수증
	 * @param map
	 * @return
	 */
	public MyPageSumDTO getRcpBill(HashMap<String, Object> map) {
		return sqlSession.selectOne("MyPageSum.getRcpBill", map);
	}
	
	/**
	 * 일별 수납 현황
	 * @param map
	 * @return
	 */
	public List<MyPageSumDTO> getDayRcpState(HashMap<String, Object> map) {
		return sqlSession.selectList("MyPageSum.getDayRcpState", map);
	}
	
	/**
	 * 일별 수납 total count
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public MyPageSumDTO getDayRcpTotal(HashMap<String, Object> map) {
		return sqlSession.selectOne("MyPageSum.getDayRcpTotal", map);
	}
	
	/**
	 * 일별 수납 상세
	 * @param map
	 * @return
	 */
	public List<PaymentStatisticsDTO> getRcpDetail(HashMap<String, Object> map) {
		return sqlSession.selectList("MyPageSum.getRcpDetail", map);
	}
	
	/**
	 * 월별 수납 현황 
	 * @param map
	 * @return
	 */
	public List<MyPageSumDTO> getMonthRcpState(HashMap<String, Object> map) {
		return sqlSession.selectList("MyPageSum.getMonthRcpState", map);
	}
	
	/**
	 * 월별 수납 현황 토탈
	 * @param map
	 * @return
	 */
	public MyPageSumDTO getMonthRcpStateTotal(HashMap<String, Object> map) {
		return sqlSession.selectOne("MyPageSum.getMonthRcpStateTotal", map);
	}
	
	/**
	 * 청구 미납 차트
	 * @param map
	 * @return
	 */
	public List<MyPageSumDTO> getXnotiChart(HashMap<String, Object> map) {
		return sqlSession.selectList("MyPageSum.getXnotiChart", map);
	}
	
	/**
	 * 수납 방법 차트
	 * @param map
	 * @return
	 */
	public MyPageSumDTO getPayMethodChart(HashMap<String, Object> map) {
		return sqlSession.selectOne("MyPageSum.getPayMethodChart", map);
	}
	
	/**
	 * 월별 미납, 수납 차트
	 * @param map
	 * @return
	 */
	public List<MyPageSumDTO> getMonthChart(HashMap<String, Object> map) {
		return sqlSession.selectList("MyPageSum.getMonthChart", map);
	}
}
