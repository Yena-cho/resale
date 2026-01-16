package com.finger.shinhandamoa.sys.rcpMgmt.service;

import com.finger.shinhandamoa.common.PageBounds;
import com.finger.shinhandamoa.sys.rcpMgmt.dao.SysAdminClosingDAO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysAdminClosingDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.web.SysAdminClosingController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 마감 서비스
 * 
 * @author wisehouse@finger.co.kr
 */
@Service
public class SysAdminClosingServiceImpl implements SysAdminClosingService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SysAdminClosingController.class);
	
	public static final String MONTHLY_CLOSE_ORDER_BY_CLIENT_NAME_ASC = "clientName_asc";
	public static final String MONTHLY_CLOSE_ORDER_BY_CLIENT_ID_ASC = "clientId_asc";
	public static final String MONTHLY_CLOSE_ORDER_BY_MONTH_DESC = "month_desc";
	
	@Inject
	private SysAdminClosingDAO sysAdminClosingDAO;

	@Override
	public int getDayCloseCount(Map<String, Object> map) {
		return sysAdminClosingDAO.getDayCloseCount(map);
	}

	@Override
	public int getMonthCloseCount(Map<String, Object> map) {
		return sysAdminClosingDAO.getMonthCloseCount(map);
	}
	
	@Override
	public List<SysAdminClosingDTO> getDayCloseList(Map<String, Object> map) throws Exception {
		LOGGER.info("SERVICEIMPL!!!");
		return sysAdminClosingDAO.getDayCloseList(map);
	}

	@Override
	public List<SysAdminClosingDTO> getMonthCloseList(Map<String, Object> map) {
		return sysAdminClosingDAO.getMonthCloseList(map);
	}

	@Override
	public void dayCloseGo(Map<String, Object> map) {
		sysAdminClosingDAO.dayCloseGo(map);
		
	}

	@Override
	public void monthCloseGo(Map<String, Object> map) {
		sysAdminClosingDAO.monthCloseGo(map);
	}

	@Override
	public int countMonthlySettle(String month, String clientId, String clientName) {
		return sysAdminClosingDAO.countMonthlySettle(month, clientId, clientName);
	}

	@Override
	public List<Map<String, Object>> getMonthlySettleList(String month, String clientId, String clientName, String orderBy, PageBounds pageBounds) {
		final String orderByClause;
		switch(orderBy) {
			case MONTHLY_CLOSE_ORDER_BY_CLIENT_NAME_ASC:
				orderByClause = "B.CHANAME ASC, A.CHACD ASC, A.MONTH DESC";
				break;
			case MONTHLY_CLOSE_ORDER_BY_CLIENT_ID_ASC:
				orderByClause = "A.CHACD ASC, A.MONTH DESC";
				break;
			case MONTHLY_CLOSE_ORDER_BY_MONTH_DESC:
				orderByClause = "A.MONTH DESC, B.CHANAME ASC, A.CHACD ASC";
				break;
			default:
				orderByClause = "B.CHANAME ASC, A.CHACD ASC, A.MONTH DESC";
				break;
		}
		
		return sysAdminClosingDAO.selectMonthlySettle(month, clientId, clientName, orderByClause, pageBounds.toRowBounds());
	}

	@Override
	public List<SysAdminClosingDTO> getVirtualAccountCalculateCount(HashMap<String, Object> map) throws Exception {
		return sysAdminClosingDAO.getVirtualAccountCalculateCount(map);
	}

	@Override
	public List<SysAdminClosingDTO> getVirtualAccountCalculateList(HashMap<String, Object> map) throws Exception {
		return sysAdminClosingDAO.getVirtualAccountCalculateList(map);
	}

	@Override
	public List<SysAdminClosingDTO> getVirtualAccountCalculateListExcel(HashMap<String, Object> map) throws Exception {
		return sysAdminClosingDAO.getVirtualAccountCalculateListExcel(map);
	}

	@Override
	public List<SysAdminClosingDTO> getVirtualAccountSettleDetCount(HashMap<String, Object> map) throws Exception {
		return sysAdminClosingDAO.getVirtualAccountSettleDetCount(map);
	}

	@Override
	public List<SysAdminClosingDTO> getVirtualAccountSettleDetList(HashMap<String, Object> map) throws Exception {
		return sysAdminClosingDAO.getVirtualAccountSettleDetList(map);
	}

	@Override
	public List<SysAdminClosingDTO> getVirtualAccountSettleMas(HashMap<String, Object> map) throws Exception {
		return sysAdminClosingDAO.getVirtualAccountSettleMas(map);
	}

	@Override
	public void updateVirtualAccountSettleMas(HashMap<String, Object> map) {
		sysAdminClosingDAO.updateVirtualAccountSettleMas(map);

	}

	@Override
	public void insertVirtualAccountSettleDet(HashMap<String, Object> map) {
		sysAdminClosingDAO.insertVirtualAccountSettleDet(map);

	}

}
