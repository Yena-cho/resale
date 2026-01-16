package com.finger.shinhandamoa.sys.setting.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.finger.shinhandamoa.sys.setting.dao.AutoTranMgmtDAO;
import com.finger.shinhandamoa.sys.setting.dto.AutoDTO;

import kr.co.finger.damoa.commons.Damoas;
import kr.co.finger.msgio.msg.BatchData;
import kr.co.finger.msgio.msg.EB13;
import kr.co.finger.msgio.msg.EB14;
import kr.co.finger.msgio.msg.EB21;
import kr.co.finger.msgio.msg.EB22;
import kr.co.finger.msgio.msg.NewData;

/**
 * @author  by puki
 * @date    2018. 5. 23.
 * @desc    최초생성
 * @version 
 * 
 */
@Service
public class AutoTranMgmtServiceImpl implements AutoTranMgmtService {
	
	@Inject
	private AutoTranMgmtDAO autoTranMgmtDao;
	
	@Autowired
    private PlatformTransactionManager transactionManager;
    
    DefaultTransactionDefinition def = null;
    TransactionStatus status = null;

	@Override
	public int selAutoTranCount(HashMap<String, Object> map) throws Exception {
		return autoTranMgmtDao.selAutoTranCount(map);
	}

	@Override
	public List<AutoDTO> selAutoTranList(HashMap<String, Object> map) throws Exception {
		return autoTranMgmtDao.selAutoTranList(map);
	}

	@Override
	public List<Map<String, Object>> findNewAutomaticWithdrawal(HashMap<String, Object> map) throws Exception {
		return autoTranMgmtDao.findNewAutomaticWithdrawal(map);
	}

	@Transactional
	@Override
	public void updateEBI13Result(EB13 eb13) throws Exception {
		try {
			def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);
            
			List<NewData> dataList = eb13.getDataList();
	        for (NewData data : dataList) {
	        	autoTranMgmtDao.updateEBI13Result(data.toEB13UpdateParam());
	        }
	        transactionManager.commit(status);
		} catch(Exception e) {
			transactionManager.rollback(status);
			e.printStackTrace();
		}
	}

	@Override
	public int selAutoTranTargetCnt(HashMap<String, Object> map) throws Exception {
		return autoTranMgmtDao.selAutoTranTargetCnt(map);
	}

	@Override
	public List<AutoDTO> selAutoTranTarget(HashMap<String, Object> map) throws Exception {
		return autoTranMgmtDao.selAutoTranTarget(map);
	}

	@Override
	public void applyFinish(HashMap<String, Object> map) throws Exception {
		autoTranMgmtDao.applyFinish(map);
	}

	@Override
	public int selAccSumCnt(HashMap<String, Object> map) throws Exception {
		return autoTranMgmtDao.selAccSumCnt(map);
	}

	@Override
	public List<AutoDTO> selAccSumList(HashMap<String, Object> map) throws Exception {
		return autoTranMgmtDao.selAccSumList(map);
	}

	@Override
	public List<Map<String, Object>> findMonthlyAutomaticWithdrawal(String month) {
		return autoTranMgmtDao.findMonthlyAutomaticWithdrawal(month);
	}

	@Transactional
	@Override
	public void updateEB21Result(EB21 eb21, String previousMonth) throws Exception {
		try {
			def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);
            
			List<BatchData> dataList = eb21.getDataList();
	        for (BatchData data : dataList) {
	        	autoTranMgmtDao.updateEB21Result(data.toEB21UpdateParam(previousMonth));
	        }
	        
	        transactionManager.commit(status);
		} catch(Exception e) {
			transactionManager.rollback(status);
			e.printStackTrace();
		}
	}

	@Override
	public int selAutoProcOrgCnt(HashMap<String, Object> map) throws Exception {
		return autoTranMgmtDao.selAutoProcOrgCnt(map);
	}

	@Override
	public List<AutoDTO> selAutoProcOrgList(HashMap<String, Object> map) throws Exception {
		return autoTranMgmtDao.selAutoProcOrgList(map);
	}

	@Override
	public int selAutoProcAccCnt(HashMap<String, Object> map) throws Exception {
		return autoTranMgmtDao.selAutoProcAccCnt(map);
	}

	@Override
	public List<AutoDTO> selAutoProcAccList(HashMap<String, Object> map) throws Exception {
		return autoTranMgmtDao.selAutoProcAccList(map);
	}

	@Override
	public int selAutoOrgFailCnt(HashMap<String, Object> map) throws Exception {
		return autoTranMgmtDao.selAutoOrgFailCnt(map);
	}

	@Override
	public int selAutoAccFailCnt(HashMap<String, Object> map) throws Exception {
		return autoTranMgmtDao.selAutoAccFailCnt(map);
	}

	@Transactional
    public void updateEB14Result(EB14 eb14) {
		try {
			def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);
	        
            List<NewData> dataList = eb14.getDataList();
	        for (NewData data : dataList) {
	        	autoTranMgmtDao.updateEB14Result(data.toUpdateParam());
	        }
	        
	        transactionManager.commit(status);
		} catch(Exception e) {
			transactionManager.rollback(status);
			e.printStackTrace();
		}
    }
	
	@Transactional
    public void updateEB22Result(EB22 eb22, String previousMonth) {
		try {
			def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);
            
			Map<String, Object> map = new HashMap<String, Object>();
	        List<BatchData> dataList = eb22.getDataList();
	        for (BatchData data : dataList) {
	        	map.put("RCP_DT", data.toEB22UpdateParam(previousMonth).get("RCP_DT"));
	            autoTranMgmtDao.updateEB22Result(data.toEB22UpdateParam(previousMonth), Damoas.findCmsError(data.getErrorCode()));
	        }
	        map.put("MONTH", previousMonth);
	        autoTranMgmtDao.updateEB22Success(map);
	        
	        transactionManager.commit(status);
		} catch(Exception e) {
			transactionManager.rollback(status);
			e.printStackTrace();
		}
    }

	@Override
	public void updateCmsReq(Map<String, Object> map) throws Exception {
		autoTranMgmtDao.updateCmsReq(map);
	}

    @Override
    public int selAutoTranRegCount(HashMap<String, Object> map) throws Exception {
        return autoTranMgmtDao.selAutoTranRegCount(map);
    }

    @Override
    public List<AutoDTO> selAutoTranRegList(HashMap<String, Object> map) throws Exception {
        return autoTranMgmtDao.selAutoTranRegList(map);
    }

	@Override
	public String getCmsFileName(HashMap<String, Object> map) throws Exception{
		return autoTranMgmtDao.getCmsFileName(map);
	}

	@Override
	public void UpdateAutoTranSt(HashMap<String, Object> map) throws Exception {
		autoTranMgmtDao.UpdateAutoTranSt(map);
	}

	@Override
	public void UpdateFeeTranSt(HashMap<String, Object> map) throws Exception {
		autoTranMgmtDao.UpdateFeeTranSt(map);
	}

    @Override
    public void insertAutoTranFeeInfo(Map<String, Object> map) throws Exception {
		autoTranMgmtDao.insertAutoTranFeeInfo(map);
    }

}
