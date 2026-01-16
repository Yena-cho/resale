package com.finger.shinhandamoa.home.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.finger.shinhandamoa.sys.bbs.dto.BannerDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.home.dto.LoginDTO;
import com.finger.shinhandamoa.main.dto.NoticeDTO;
import com.finger.shinhandamoa.payer.cashreceipt.dto.CashReceiptDTO;
import com.finger.shinhandamoa.payer.notification.dto.NotificationDTO;
import com.finger.shinhandamoa.payer.payment.dto.PaymentDTO;
import com.finger.shinhandamoa.sys.bbs.dto.PopupDTO;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
@Repository
public class HomeDAOImpl implements HomeDAO {

	@Inject
	private SqlSession sqlSession;
	
	@Override
	public List<NoticeDTO> nList(Map<String, Object> reqMap) throws Exception {
		return sqlSession.selectList("MainDao.noticeList", reqMap);
	}

	@Override
	public List<NoticeDTO> fList(Map<String, Object> reqMap) throws Exception {
		return sqlSession.selectList("MainDao.faqList", reqMap);
	}

	@Override
	public List<NoticeDTO> qList(Map<String, Object> reqMap) throws Exception {
		return sqlSession.selectList("MainDao.qnaList", reqMap);
	}
	
	@Override
	public List<PopupDTO> pList(Map<String, Object> reqMap) throws Exception {
		return sqlSession.selectList("MainDao.popupList", reqMap);
	}

	@Override
	public List<BannerDTO> pcBannerList(Map<String, Object> reqMap) throws Exception {
		return sqlSession.selectList("MainDao.pcBannerList", reqMap);
	}

	@Override
	public List<BannerDTO> mobileBannerList(Map<String, Object> reqMap) throws Exception {
		return sqlSession.selectList("MainDao.mobileBannerList", reqMap);
	}
	
	@Override
	public void telReservation(Map<String, Object> map) throws Exception {
		sqlSession.insert("MainDao.telReservation", map);
	}

	@Override
	public LoginDTO selIdSearch(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("MainDao.idSearch", map);
	}

	@Override
	public LoginDTO selPasswordSearch(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("MainDao.passwordSearch", map);
	}


	@Override
	public String selIdOverlap(String loginId) throws Exception {
		return sqlSession.selectOne("MainDao.selIdOverlap", loginId);
	}

	@Override
	public void updateIdPassword(Map<String, Object> map) throws Exception {
		sqlSession.update("MainDao.idPasswordUpdate", map);
	}

	@Override
	public void mergeOtpNo(Map<String, Object> map) throws Exception {
		sqlSession.insert("MainDao.mergeOtpNo", map);
	}

	@Override
	public void updatePassword(Map<String, Object> map) throws Exception {
		sqlSession.update("MainDao.updatePassword", map);
	}

	@Override
	public void updateChaSvcYn(Map<String, Object> map) throws Exception {
		sqlSession.update("MainDao.updateChaSvcYn", map);
	}

	@Override
	public NotificationDTO getNotification(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectOne("MainDao.getNotification", reqMap);
	}

	@Override
	public List<PaymentDTO> selectPaymentSummary(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectList("MainDao.selectPaymentSummary", reqMap);
	}

	@Override
	public CashReceiptDTO getCashReceipt(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectOne("MainDao.getCashReceipt", reqMap);
	}

	@Override
	public List<LoginDTO> selAutoChaName(String username) throws Exception {
		return sqlSession.selectList("MainDao.selAutoChaName", username);
	}

	@Override
	public int selLoginItem(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("MainDao.selLoginItem", map);
	}

	@Override
	public String selChaSvcYn(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("MainDao.selChaSvcYn", map);
	}

	@Override
	public int reciptCnt(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("MainDao.reciptCnt", map);
	}

	@Override
	public LoginDTO selCmsChaInfo(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("MainDao.selCmsChaInfo", map);
	}

}
