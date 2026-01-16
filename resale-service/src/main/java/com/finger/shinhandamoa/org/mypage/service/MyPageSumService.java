package com.finger.shinhandamoa.org.mypage.service;

import com.finger.shinhandamoa.org.mypage.dto.MyPageSumDTO;
import com.finger.shinhandamoa.org.mypage.dto.PaymentStatisticsDTO;

import java.util.HashMap;
import java.util.List;

/**
 * 마이페이지 이용료 조회
 * @author suhlee
 *
 */

public interface MyPageSumService {

	/**
	 * 마이페이지 이용료조회(월별)
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<MyPageSumDTO> getXmonthSum(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 마이페이지 이용료조회 카운트(월별)
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getXmonthSumCount(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 이번달 이용현황
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public MyPageSumDTO getXmonthState(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 수수료 출금 내역
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<MyPageSumDTO> getXmonthSumInfo(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 다모아 이용료 영수증
	 * @param map
	 * @return
	 */
	public MyPageSumDTO getRcpBill(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 일별 수납 현황
	 * @param map
	 * @return
	 */
	public List<MyPageSumDTO> getDayRcpState(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 일별 수납 total count
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public MyPageSumDTO getDayRcpTotal(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 일별 수납 상세
	 * @param map
	 * @return
	 */
	public List<PaymentStatisticsDTO> getRcpDetail(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 월별 수납 현황 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<MyPageSumDTO> getMonthRcpState(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 월별 수납 현황 토탈
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public MyPageSumDTO getMonthRcpStateTotal(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 청구 미납 차트
	 * @param map
	 * @return
	 */
	public List<MyPageSumDTO> getXnotiChart(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 결제방법 차트
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public MyPageSumDTO getPayMethodChart(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 월별 미납, 수납 차트
	 * @param map
	 * @return
	 */
	public List<MyPageSumDTO> getMonthChart(HashMap<String, Object> map) throws Exception;
}
