package com.finger.shinhandamoa.sys.setting.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.finger.shinhandamoa.sys.setting.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.sys.main.dao.SysMainDAO;
import com.finger.shinhandamoa.sys.setting.dao.VanoMgmtDAO;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
@Service
public class VanoMgmtServiceImpl implements VanoMgmtService {

	private static final Logger logger = LoggerFactory.getLogger(VanoMgmtServiceImpl.class);

	@Inject
	SysMainDAO sysMainDAO;

	@Inject
	VanoMgmtDAO vanoMgmtDAO;

	@Autowired
	private PlatformTransactionManager transactionManager;

	DefaultTransactionDefinition def = null;
	TransactionStatus status = null;

	// 청구월별수납현황
	@Override
	public VanoMgmtDTO getSysMainInfo01(Map<String, Object> map) throws Exception{
		return vanoMgmtDAO.getSysMainInfo01(map);
	}

	@Override
	public int getVano01ListTotalCount(HashMap<String, Object> map) throws Exception {
		return vanoMgmtDAO.getVano01ListTotalCount(map);
	}

	@Override
	public List<VanoMgmtDTO> getVano01ListAll(HashMap<String, Object> map) throws Exception {
		return vanoMgmtDAO.getVano01ListAll(map);
	}

	@Override
	public List<VanoMgmtDTO> getVano01ListExcel(HashMap<String, Object> map) throws Exception {
		return vanoMgmtDAO.getVano01ListExcel(map);
	}

	@Override
	public List<VanoMgmtDTO> getVanoIssuedListExcel(HashMap<String, Object> map) throws Exception {
		return vanoMgmtDAO.getVanoIssuedListExcel(map);
	}

	@Override
	public List<VanoMgmt2DTO> getChaVanoListTotalCount(HashMap<String, Object> map) throws Exception {
		return vanoMgmtDAO.getChaVanoListTotalCount(map);
	}

	@Override
	public List<VanoMgmt2DTO> getChaVanoListAll(HashMap<String, Object> map) throws Exception {
        return vanoMgmtDAO.getChaVanoListAll(map);
	}

	@Override
	public List<VanoMgmt2DTO> getChaVanoListExcel(HashMap<String, Object> map) throws Exception {
		return vanoMgmtDAO.getChaVanoListExcel(map);
	}

	@Override
	public void insertVirtualAccount(HashMap<String, Object> map) throws Exception {
		vanoMgmtDAO.insertVirtualAccount(map);
	}

	@Override
	public void deleteVirtualAccount() throws Exception {
		vanoMgmtDAO.deleteVirtualAccount();
	}

	@Transactional
	@Override
	public void csvUploadResultInsert(HashMap<String, Object> map) throws Exception{
		try {
			def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			status = transactionManager.getTransaction(def);
			vanoMgmtDAO.csvUploadResultInsert(map);
			vanoMgmtDAO.csvUploadResultInsertAfter(map);

		}catch (Exception e){
			transactionManager.rollback(status);
			e.printStackTrace();

		}

		}


		@Override
		public int getVanoTranHisListTotalCount(HashMap<String, Object> map) throws Exception {
			return vanoMgmtDAO.getVanoTranHisListTotalCount(map);
		}

		@Override
		public List<VanoMgmtDTO> getVanoTranHisListAll(HashMap<String, Object> map) throws Exception {
			return vanoMgmtDAO.getVanoTranHisListAll(map);
		}

		@Override
		public List<VanoMgmtDTO> getVanoTranHisListExcel(HashMap<String, Object> map) throws Exception {
			return vanoMgmtDAO.getVanoTranHisListExcel(map);
		}

		@Override
		public int getVanoIssuedListTotalCount(HashMap<String, Object> map) throws Exception {
			return vanoMgmtDAO.getVanoIssuedListTotalCount(map);
		}

		@Override
		public List<VanoMgmtDTO> getMisassignVanoCount() throws Exception {
			return vanoMgmtDAO.getMisassignVanoCount();
		}

		@Override
		public List<VanoMgmtDTO> getVanoIssuedListAll(HashMap<String, Object> map) throws Exception {
			return vanoMgmtDAO.getVanoIssuedListAll(map);
		}

		@Override
		public void insertValist(HashMap<String, Object> map) throws Exception {
			vanoMgmtDAO.insertValist(map);
		}


		@Override
		@Transactional
		public void doVanoIssueReq(HashMap<String, Object> map) throws Exception {

		//1. doVanoIssueReq 할당 후
		vanoMgmtDAO.doVanoIssueReq(map);

		//2. tb_virtual_acnt_temp에 insert한 토탈 count select
		HashMap<String ,Object> resultMap =vanoMgmtDAO.doVanoIssueReqAfterCount(map);


		//3. DTO set
		VanoReqDTO vanoReqDTO = new VanoReqDTO();

		vanoReqDTO.setVanoCount((Long) resultMap.get("totalcnt"));
		vanoReqDTO.setChaCd((String) map.get("chacd"));
		vanoReqDTO.setRemark((String) map.get("remark"));


		//4. 해당 건수 tb_virtual_acnt_temp 에 적재
		vanoMgmtDAO.doVanoIssueReqAfterInsert(vanoReqDTO);

		}

		@Override
		public int getVanoTranChkListCnt(HashMap<String, Object> reqMap) throws Exception {
			return vanoMgmtDAO.getVanoTranChkListCnt(reqMap);
		}

		@Override
		public List<TransInfoDTO> getVanoTranChkList(HashMap<String, Object> reqMap) throws Exception {
			return vanoMgmtDAO.getVanoTranChkList(reqMap);
		}

		@Override
		public int getVanoTranInitChkCnt(HashMap<String, Object> reqMap) throws Exception {
			return vanoMgmtDAO.getVanoTranInitChkCnt(reqMap);
		}

		@Override
		public List<TransInfoDTO> getVanoTranInitChk(HashMap<String, Object> reqMap) throws Exception {
			return vanoMgmtDAO.getVanoTranInitChk(reqMap);
		}

		@Override
		public int getTempVanoAccount(HashMap<String, Object> reqMap) throws Exception {
			return vanoMgmtDAO.getTempVanoAccount(reqMap);
		}
		@Override
		public int pgVanoSendListCnt(VanoPgDTO dto) throws Exception {
			return vanoMgmtDAO.pgVanoSendListCnt(dto);
		}

		@Override
		public List<VanoPgDTO> pgVanoSendList(VanoPgDTO dto) throws Exception {
			return vanoMgmtDAO.pgVanoSendList(dto);
		}

	@Override
	public void updateSendListStat(Map<String, Object> map) throws Exception {
		vanoMgmtDAO.updateSendListStat(map);
	}
}
