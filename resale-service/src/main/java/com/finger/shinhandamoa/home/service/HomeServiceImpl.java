package com.finger.shinhandamoa.home.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.finger.shinhandamoa.data.file.mapper.SimpleFileMapper;
import com.finger.shinhandamoa.data.table.mapper.FwFileMapper;
import com.finger.shinhandamoa.data.table.model.FwFile;
import com.finger.shinhandamoa.sys.bbs.dto.BannerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.home.dao.HomeDAO;
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
@Service
public class HomeServiceImpl implements HomeService {

	@Inject
	HomeDAO homeDao;

	@Autowired
	private SimpleFileMapper simpleFileMapper;

	@Autowired
	private FwFileMapper fwFileMapper;
	
	@Override
	public List<NoticeDTO> nList(Map<String, Object> reqMap) throws Exception {
		return homeDao.nList(reqMap);
	}

	@Override
	public List<NoticeDTO> fList(Map<String, Object> reqMap) throws Exception {
		return homeDao.fList(reqMap);
	}
	
	@Override
	public List<NoticeDTO> qList(Map<String, Object> reqMap) throws Exception {
		return homeDao.qList(reqMap);
	}
	
	@Override
	public List<PopupDTO> pList(Map<String, Object> reqMap) throws Exception {
		return homeDao.pList(reqMap);
	}

	@Override
	public List<BannerDTO> pcBannerList(Map<String, Object> reqMap) throws Exception {
		return homeDao.pcBannerList(reqMap);
	}

	@Override
	public List<BannerDTO> mobileBannerList(Map<String, Object> reqMap) throws Exception {
		return homeDao.mobileBannerList(reqMap);
	}
	
	@Override
	public void telReservation(Map<String, Object> map) throws Exception {
		homeDao.telReservation(map);
	}

	@Override
	public LoginDTO selIdSearch(Map<String, Object> map) throws Exception {
		return homeDao.selIdSearch(map);
	}

	@Override
	public LoginDTO selPasswordSearch(Map<String, Object> map) throws Exception {
		return homeDao.selPasswordSearch(map);
	}
	
	@Override
	public String selIdOverlap(String loginId) throws Exception {
		return homeDao.selIdOverlap(loginId);
	}

	@Override
	public void updateIdPassword(Map<String, Object> map) throws Exception {
		homeDao.updateIdPassword(map);
	}

	@Override
	public void mergeOtpNo(Map<String, Object> map) throws Exception {
		homeDao.mergeOtpNo(map);
	}

	@Override
	public void updatePassword(Map<String, Object> map) throws Exception {
		homeDao.updatePassword(map);
	}

	@Override
	public void updateChaSvcYn(Map<String, Object> map) throws Exception {
		homeDao.updateChaSvcYn(map);
	}

	@Override
	public NotificationDTO getNotification(HashMap<String, Object> reqMap) throws Exception {
		return homeDao.getNotification(reqMap);
	}

	@Override
	public List<PaymentDTO> selectPaymentSummary(HashMap<String, Object> reqMap) throws Exception {
		return homeDao.selectPaymentSummary(reqMap);
	}

	@Override
	public CashReceiptDTO getCashReceipt(HashMap<String, Object> reqMap) throws Exception {
		return homeDao.getCashReceipt(reqMap);
	}

	@Override
	public List<LoginDTO> selAutoChaName(String username) throws Exception {
		return homeDao.selAutoChaName(username);
	}

	@Override
	public int selLoginItem(HashMap<String, Object> map) throws Exception {
		return homeDao.selLoginItem(map);
	}

	@Override
	public String selChaSvcYn(HashMap<String, Object> map) throws Exception {
		return homeDao.selChaSvcYn(map);
	}

	@Override
	public int reciptCnt(HashMap<String, Object> map) throws Exception {
		return homeDao.reciptCnt(map);
	}

	@Override
	public LoginDTO selCmsChaInfo(Map<String, Object> map) throws Exception {
		return homeDao.selCmsChaInfo(map);
	}

	@Override
	public FwFile getFileInfo(String id) {
		FwFile fwFile = fwFileMapper.selectByPrimaryKey(id);

		return fwFile;
	}

	@Override
	public InputStream getFile(String id) throws IOException {
		InputStream is = simpleFileMapper.load("BANNER", id);

		return is;
	}
}
