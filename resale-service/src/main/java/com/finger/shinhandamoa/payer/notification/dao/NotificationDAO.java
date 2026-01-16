package com.finger.shinhandamoa.payer.notification.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.payer.notification.dto.NotificationDTO;


/**
 * @author  by PYS
 * @date    2018. 4. 12.
 * @desc    
 * @version 
 * 
 */
public interface NotificationDAO {

	public HashMap<String, Object> notificationTotalCount(HashMap<String, Object> map) throws Exception;
	public NotificationDTO selectChaInfo(String vano) throws Exception;
	public NotificationDTO maxMonth(String vano) throws Exception;
	public List<NotificationDTO> notificationList(HashMap<String, Object> map) throws Exception;
	public NotificationDTO selectNotiMas(HashMap<String, Object> reqMap) throws Exception;
	public NotificationDTO getRcpMasCd(HashMap<String, Object> map) throws Exception;
	public NotificationDTO totalNotiMasAmt(HashMap<String, Object> map) throws Exception;
	public int insertRcpMas(HashMap<String, Object> map) throws Exception;
	public int insertRcpDet(HashMap<String, Object> map) throws Exception;
	public void updateNotiBill(HashMap<String, Object> map) throws Exception;
	public List<NotificationDTO> notiDetList(HashMap<String, Object> reqMap) throws Exception;
	public HashMap<String, Object> mDetNotificationTotalCount(HashMap<String, Object> reqMap) throws Exception;
	public List<NotificationDTO> mDetNotificationList(HashMap<String, Object> reqMap) throws Exception;
	public HashMap<String, Object> mMasNotificationTotalCount(HashMap<String, Object> reqMap) throws Exception;
	public List<NotificationDTO> mMasNotificationList(HashMap<String, Object> reqMap) throws Exception;
	public void resetPickRcpYn(String notimasCd) throws Exception;
	public void updatePickRcpYn(Map<String, Object> map) throws Exception;
}
