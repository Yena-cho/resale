package com.finger.shinhandamoa.payer.notification.service;

import com.finger.shinhandamoa.data.table.mapper.ChaMapper;
import com.finger.shinhandamoa.data.table.mapper.NoticeDetailsMapper;
import com.finger.shinhandamoa.data.table.mapper.NoticeMasterMapper;
import com.finger.shinhandamoa.data.table.mapper.ReceiptDetailsMapper;
import com.finger.shinhandamoa.data.table.model.*;
import com.finger.shinhandamoa.payer.notification.dao.NotificationDAO;
import com.finger.shinhandamoa.payer.notification.dto.NoticeDetailDTO;
import com.finger.shinhandamoa.payer.notification.dto.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  by PYS
 * @date    2018. 4. 12.
 * @desc    
 * @version 
 * 
 */
@Service
public class NotificationServiceImpl implements NotificationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);
	
	@Autowired
	private ChaMapper chaMapper;
	
	@Autowired
	private NoticeMasterMapper noticeMasterMapper;

	@Autowired
	private NotificationDAO notificationdao;
	
	@Autowired
	private NoticeDetailsMapper noticeDetailsMapper;
	
	@Autowired
	private ReceiptDetailsMapper rcpDetailsMapper;

	@Override
	public HashMap<String, Object> notificationTotalCount(HashMap<String, Object> map) throws Exception {
		
		return notificationdao.notificationTotalCount(map);
	}
	
	@Override
	public NotificationDTO selectChaInfo(String vano) throws Exception {
		return notificationdao.selectChaInfo(vano);
	}
	
	@Override
	public NotificationDTO maxMonth(String vano) throws Exception {
		return notificationdao.maxMonth(vano);
	}
	
	@Override
	public List<NotificationDTO> notificationList(HashMap<String, Object> map) throws Exception {
		return notificationdao.notificationList(map);
	}

	@Deprecated
	@Override
	public NotificationDTO selectNotiMas(HashMap<String, Object> reqMap) throws Exception {
		return notificationdao.selectNotiMas(reqMap);
	}

	@Override
	public NotificationDTO getRcpMasCd(HashMap<String, Object> map) throws Exception {
		return notificationdao.getRcpMasCd(map);
	}

	@Override
	public NotificationDTO totalNotiMasAmt(HashMap<String, Object> map) throws Exception {
		return notificationdao.totalNotiMasAmt(map);
	}

	@Override
	public int insertRcpMas(HashMap<String, Object> map) throws Exception {
		return notificationdao.insertRcpMas(map);
	}

	@Override
	public int insertRcpDet(HashMap<String, Object> map) throws Exception {
		return notificationdao.insertRcpDet(map);
	}

	@Override
	public void updateNotiBill(HashMap<String, Object> map) throws Exception {
		notificationdao.updateNotiBill(map);
	}

	@Deprecated
	@Override
	public List<NotificationDTO> notiDetList(HashMap<String, Object> reqMap) throws Exception {
		return notificationdao.notiDetList(reqMap);
	}

	@Override
	public HashMap<String, Object> mDetNotificationTotalCount(HashMap<String, Object> reqMap) throws Exception {
		return notificationdao.mDetNotificationTotalCount(reqMap);
	}

	@Override
	public List<NotificationDTO> mDetNotificationList(HashMap<String, Object> reqMap) throws Exception {
		return notificationdao.mDetNotificationList(reqMap);
	}

	@Override
	public HashMap<String, Object> mMasNotificationTotalCount(HashMap<String, Object> reqMap) throws Exception {
		return notificationdao.mMasNotificationTotalCount(reqMap);
	}

	@Override
	public List<NotificationDTO> mMasNotificationList(HashMap<String, Object> reqMap) throws Exception {
		return notificationdao.mMasNotificationList(reqMap);
	}

	/**
	 * 청구상세항목 조회 Notifiacation.xml mapper에서 처리하는 부분을
	 * mybatis-generator mapper로 변경
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 10. 12.
	 */
	@Override
	public Map<String, Object> getNoticeDetailList(HashMap<String, Object> params) throws Exception {
		
		List<NoticeDetailDTO> list = new ArrayList<>();
		
		NoticeDetailsExample example = new NoticeDetailsExample();
		example.createCriteria().andNotimascdEqualTo(params.get("notimasCd").toString()).andNotiCanYnEqualTo("N");
				
		for (NoticeDetails each : noticeDetailsMapper.selectByExample(example)) {
			final String notidetcd = each.getNotidetcd();
			NoticeDetailDTO detail = new NoticeDetailDTO(each);
			
			ReceiptDetailsExample rcpExample = new ReceiptDetailsExample();
			rcpExample.createCriteria().andNotidetcdEqualTo(notidetcd).andRcpdetstEqualTo("PA03");
			long rcpamt = 0;
			String rcpmascd = null;
			String rcpdetcd = null;
			String rcpdetst = null;
			for (final ReceiptDetails rcpDetail : rcpDetailsMapper.selectByExample(rcpExample) ) {
				rcpamt += rcpDetail.getPayitemamt();
				rcpmascd = rcpDetail.getRcpmascd();
				rcpdetcd = rcpDetail.getRcpdetcd();
				rcpdetst = rcpDetail.getRcpdetst();
			}
			
			detail.setRcpamt(rcpamt);
			detail.setRcpmascd(rcpmascd);
			detail.setRcpdetcd(rcpdetcd);
			detail.setRcpdetst(rcpdetst);
			list.add(detail);
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
        map.put("retCode", "0000");
        map.put("retMsg", "정상");
        map.put("masMonth", params.get("masMonth").toString());
        map.put("subTot", list.size());
        map.put("notimasCd", params.get("notimasCd").toString());
		
		return map;
	}

	/* (non-Javadoc)
	 * @see com.finger.shinhandamoa.payer.notification.service.NotificationService#selectNotiDetails(java.util.Map)
	 */
	@Override
	public Map<String, Object> selectNotiDetails(Map<String, Object> params) {
		
		final String notimasCd  = params.get("notimasCd").toString();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<String> notidetcdList = new ArrayList<>();
			for (final String notidetcd : StringUtils.commaDelimitedListToStringArray(params.get("notidetList").toString())) {
				notidetcdList.add(notidetcd);
			}
			
			int notiCount = 0;
			long payItemsAmt = 0;
			String itemName = null;
			Map<String, Object> result = new HashMap<String, Object>();
			
			NoticeDetailsExample example = new NoticeDetailsExample();
			example.createCriteria().andNotimascdEqualTo(notimasCd).andNotidetcdIn(notidetcdList);
			example.setOrderByClause("PTRITEMORDER ASC, NOTIDETCD ASC");
			for (NoticeDetails each : noticeDetailsMapper.selectByExample(example)) {
				payItemsAmt += each.getPayitemamt();
				notiCount++;
				if (itemName == null) itemName = each.getPayitemname();
			}

			result.put("notiDetCount", notiCount);
			if (notiCount > 1) itemName += " 외 " + String.valueOf(notiCount-1) + "건";
			result.put("itemName", itemName);
			result.put("amt", payItemsAmt);

			NoticeMaster notiMaster = noticeMasterMapper.selectByPrimaryKey(notimasCd);
			result.put("cusKey",    notiMaster.getCuskey());
			result.put("cusName",   notiMaster.getCusname());
			result.put("cusHp",     notiMaster.getCushp());
			result.put("cusMail",   notiMaster.getCusmail());
			result.put("masMonth",  notiMaster.getMasmonth());
			result.put("endDate",   notiMaster.getEnddate());

			Cha cha = chaMapper.selectByPrimaryKey(notiMaster.getChacd());
			result.put("chaCd",        cha.getChacd());
			result.put("chaName",      cha.getChaname());
			result.put("usePgYn",      cha.getUsepgyn());
			result.put("pgServiceId",  cha.getPgserviceid());
			result.put("pgLicenKey",   cha.getPgLicenKey());
			result.put("rcpDtDupYn",   cha.getRcpdtdupyn());
			
            map.put("notimasCd", notimasCd);
            map.put("result", result);
            map.put("amt", payItemsAmt);
            map.put("notidetCd", notidetcdList);
            map.put("merchantKey", cha.getPgLicenKey());
            map.put("merchantID",  cha.getPgserviceid());
            map.put("retCode", "0000");
			map.put("retMsg", "정상");
		} catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("notimasCd", notimasCd);
            map.put("result", null);
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
		}
        
		return map;
	}

	@Override
	public void resetPickRcpYn(String notimasCd) throws Exception {
		notificationdao.resetPickRcpYn(notimasCd);
	}

	@Override
	public void updatePickRcpYn(Map<String, Object> map) throws Exception {
		notificationdao.updatePickRcpYn(map);
	}
}
