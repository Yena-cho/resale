package com.finger.shinhandamoa.org.claimMgmt.service;

import com.finger.shinhandamoa.data.table.mapper.ChaMapper;
import com.finger.shinhandamoa.data.table.model.Cha;
import com.finger.shinhandamoa.org.claimMgmt.dao.ClaimItemDAO;
import com.finger.shinhandamoa.org.claimMgmt.dto.ChaDTO;
import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimItemDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author  by puki
 * @date    2018. 4. 13.
 * @desc    최초생성
 * @version 
 * 
 */
@Service
public class ClaimItemServiceImpl implements ClaimItemService {

	@Inject
	private ClaimItemDAO claimItemDao;
	
	@Autowired
	private ChaMapper chaMapper;
	
	@Override
	public List<ClaimItemDTO> claimItemList(String chaCd, int start, int end) throws Exception {
		Map<String, Object> map = new  HashMap<String, Object>();
		map.put("chaCd", chaCd);
		map.put("start", start);
		map.put("end", end);
		
		return claimItemDao.claimItemList(map);
	}

	@Override
	public int claimItemTotalCount(String chaCd) throws Exception {
		return claimItemDao.claimItemTotalCount(chaCd);
	}
	
	@Override
	public List<ClaimItemDTO> moneyPassbookList(String chaCd) throws Exception {
		return claimItemDao.moneyPassbookList(chaCd);
	}

	@Override
	public void deleteClaimItem(String chaCd, String payItemCd) throws Exception {
		Map<String, Object> map = new  HashMap<String, Object>();
		map.put("chaCd", chaCd);
		map.put("payItemCd", payItemCd);
		
		claimItemDao.deleteClaimItem(map);
	}

	@Override
	public ClaimItemDTO detailClaimItem(String chaCd, String payItemCd) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("chaCd", chaCd);
		map.put("payItemCd", payItemCd);
		
		return claimItemDao.detailClaimItem(map);
	}

	@Override
	public void modifyClaimItem(ClaimItemDTO dto) throws Exception {
		dto.setAdjfiRegKey(StringUtils.defaultString(dto.getAdjfiRegKey()));
		dto.setMakeIp(StringUtils.EMPTY);
		dto.setDisabledYN("N");

		Map<String, Object> map = new  HashMap<String, Object>();
		map.put("chaCd", dto.getChaCd());

		List<ClaimItemDTO> ptritemOrder = claimItemDao.claimItemList(map);
		Map<String, Object> map2 = new  HashMap<String, Object>();
		for(int i = 0; i < ptritemOrder.size(); i++){
			map2.put("ptritemOrder", i);
			map2.put("chaCd", ptritemOrder.get(i).getChaCd());
			map2.put("payItemCd", ptritemOrder.get(i).getPayItemCd());
			claimItemDao.updatePtritemOrder(map2);
		}

		claimItemDao.modifyClaimItem(dto);
	}

	@Override
	public String payItemNameCheck(Map<String, Object> map) throws Exception {
		return claimItemDao.payItemNameCheck(map);
	}

	@Override
	public void updatePayItem(Map<String, Object> map) throws Exception {
		claimItemDao.updatePayItem(map);
	}

	@Override
	public int cusPayItemCnt(Map<String, Object> map) throws Exception {
		return claimItemDao.cusPayItemCnt(map);
	}

	@Override
	public String selXadjGroup(Map<String, Object> map) throws Exception {
		return claimItemDao.selXadjGroup(map);
	}

	@Override
	public ChaDTO getCha(String chaCd) {
		final Cha cha = chaMapper.selectByPrimaryKey(chaCd);
		
		final ChaDTO chaDTO = new ChaDTO();
		chaDTO.setEnabledCheckAmount(StringUtils.equals(cha.getAmtchkty(), "Y"));
		chaDTO.setEnabledMultipleAccount(StringUtils.equals(cha.getAdjaccyn(), "Y"));
		chaDTO.setEnabledPartialPayment(StringUtils.equals(cha.getPartialPayment(), "Y"));
		chaDTO.setEnabledCheckPeriod(StringUtils.equals(cha.getRcpDueChk(), "Y"));
		
		return chaDTO;
	}
}
