package com.finger.shinhandamoa.sys.main.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.org.main.dto.XmontSumDTO;
import com.finger.shinhandamoa.payer.payment.dto.PaymentDTO;
import com.finger.shinhandamoa.sys.main.dao.SysMainDAO;
import com.finger.shinhandamoa.sys.main.web.SysMainController;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
@Service
public class SysMainServiceImpl implements SysMainService {

	private static final Logger logger = LoggerFactory.getLogger(SysMainServiceImpl.class);
	
	@Inject
	SysMainDAO sysMainDAO;
	

	
	// 청구월별수납현황
	@Override	
	public XmontSumDTO getSysMainInfo01(Map<String, Object> map) throws Exception{
		return sysMainDAO.getSysMainInfo01(map);
	}
	
	// 메인 페이지 납부율조회
	@Override
	public List<XmontSumDTO> selectPaymentRatioList(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return sysMainDAO.selectPaymentRatioList(map);
	}
	
	//메인 페이지 납부집계조회
	@Override
	public List<XmontSumDTO> selectPaymentSummary(Map<String, Object> map) throws Exception
	{
		//List retval = sysMainDAO.selectRcpMasCdNextVal();
		//logger.info("SysMainServiceImpl.selectPaymentSummary  ==>" + retval);
		return sysMainDAO.selectPaymentSummary(map);
	}



}
