package com.finger.shinhandamoa.payer.notification.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.payer.notification.dto.NotificationDTO;


/**
 * @author  by PYS
 * @date    2018. 4. 12.
 * @desc    
 * @version 
 * 
 */
@Repository
public class NotificationDAOImpl implements NotificationDAO {

	private static final Logger logger = LoggerFactory.getLogger(NotificationDAOImpl.class);
	
	@Autowired
	private SqlSession sqlSession;
	
	// 가맹점정보 가져오기
	@Override
	public NotificationDTO selectChaInfo(String vano) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vano", vano);
		return sqlSession.selectOne("Notification.selectChaInfo", map);
	}
	
	@Override
	public NotificationDTO maxMonth(String vano) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vano", vano);
		return sqlSession.selectOne("Notification.maxMonth", map);
	}
	
	// 고지내역 총 개수 가져오기
	@Override
	public HashMap<String, Object> notificationTotalCount(HashMap<String, Object> map) throws Exception {		
		return sqlSession.selectOne("Notification.notificationTotalCount", map);
	}

	@Override
	public List<NotificationDTO> notificationList(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("Notification.notificationList", map);
	}

	@Override
	public NotificationDTO selectNotiMas(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectOne("Notification.selectNotiMas", reqMap);
	}

	@Override
	public NotificationDTO getRcpMasCd(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("Notification.getRcpMasCd", map);
	}

	@Override
	public NotificationDTO totalNotiMasAmt(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("Notification.totalNotiMasAmt", map);
	}

	@Override
	public int insertRcpMas(HashMap<String, Object> map) throws Exception {
		return sqlSession.insert("Notification.insertRcpMas", map);
	}

	@Override
	public int insertRcpDet(HashMap<String, Object> map) throws Exception {
		return sqlSession.insert("Notification.insertRcpDet", map);
	}

	@Override
	public void updateNotiBill(HashMap<String, Object> map) throws Exception {
		sqlSession.update("Notification.updateNotiMas", map);
		sqlSession.update("Notification.updateNotiDet", map);
	}

	@Override
	public List<NotificationDTO> notiDetList(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectList("Notification.notiDetList", reqMap);
	}

	@Override
	public HashMap<String, Object> mDetNotificationTotalCount(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectOne("Notification.mDetNotificationTotalCount", reqMap);
	}

	@Override
	public List<NotificationDTO> mDetNotificationList(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectList("Notification.mDetNotificationList", reqMap);
	}

	@Override
	public HashMap<String, Object> mMasNotificationTotalCount(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectOne("Notification.mMasNotificationTotalCount", reqMap);
	}

	@Override
	public List<NotificationDTO> mMasNotificationList(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectList("Notification.mMasNotificationList", reqMap);
	}

	@Override
	public void resetPickRcpYn(String notimasCd) throws Exception {
		sqlSession.update("Notification.resetPickRcpYn", notimasCd);
	}

	@Override
	public void updatePickRcpYn(Map<String, Object> map) throws Exception {
		sqlSession.update("Notification.updatePickRcpYn", map);
	}

}
