package com.finger.shinhandamoa.payer.notification.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.payer.notification.dto.NotificationDTO;


/**
 * @author  by PYS
 * @date    2018. 4. 11.
 * @desc    납부자 고지관리 
 * @version 
 * 
 */
public interface NotificationService {
	
	//기관정보 조회                                           
	public NotificationDTO selectChaInfo(String vano) throws Exception;
	//마지막 청구월 구하기
	public NotificationDTO maxMonth(String vano) throws Exception;
	//고지내역 총갯수 조회
	public HashMap<String, Object> notificationTotalCount(HashMap<String, Object> map) throws Exception;
	//고지내역 리스트 조회
	public List<NotificationDTO> notificationList(HashMap<String, Object> map) throws Exception;
	//고지내역 조회
	public NotificationDTO selectNotiMas(HashMap<String, Object> reqMap) throws Exception;
	//수납코드 채번
	public NotificationDTO getRcpMasCd(HashMap<String, Object> map) throws Exception;
	// 청구 전체 금액
	public NotificationDTO totalNotiMasAmt(HashMap<String, Object> map) throws Exception;
	//수납내역 등록
	public int insertRcpMas(HashMap<String, Object> map) throws Exception;
	//수납내역 항목등록
	public int insertRcpDet(HashMap<String, Object> map) throws Exception;
	//고지정보 수정
	public void updateNotiBill(HashMap<String, Object> map) throws Exception;
	//고지상세항목 조회 
	public List<NotificationDTO> notiDetList(HashMap<String, Object> reqMap) throws Exception;
	//모바일청구서 항목 개수
	public HashMap<String, Object> mDetNotificationTotalCount(HashMap<String, Object> reqMap) throws Exception;
	//모바일청구서 항목 조회
	public List<NotificationDTO> mDetNotificationList(HashMap<String, Object> reqMap) throws Exception;
	//모바일청구서 고지 개수
	public HashMap<String, Object> mMasNotificationTotalCount(HashMap<String, Object> reqMap) throws Exception;
	//모바일청구서 고지 조회
	public List<NotificationDTO> mMasNotificationList(HashMap<String, Object> reqMap) throws Exception;
	
	/**
	 * 청구항목상세+수납결과
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 10. 15.
	 */
	public Map<String, Object> getNoticeDetailList(HashMap<String, Object> params) throws Exception;
	
	/**
	 * 청구항목 분할납부 카드결제
	 * @param params
	 * @return
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 10. 15.
	 */
	public Map<String, Object> selectNotiDetails(Map<String, Object> params);

	public void resetPickRcpYn(String notimasCd) throws Exception;
	public void updatePickRcpYn(Map<String, Object> map) throws Exception;
}
