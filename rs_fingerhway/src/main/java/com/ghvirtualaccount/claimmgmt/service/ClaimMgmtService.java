package com.ghvirtualaccount.claimmgmt.service;

import java.util.List;
import java.util.Map;

import com.ghvirtualaccount.vo.ParamVO;
import com.ghvirtualaccount.vo.PayerInfoMasterInfoVO;
import com.ghvirtualaccount.vo.PayerInfoVO;

public interface ClaimMgmtService {

	public int getPayerInfoMasterListCnt(ParamVO paramVO) throws Exception;
	
	public int getAvailAcntCnt() throws Exception;
	
	public List<PayerInfoMasterInfoVO> getPayerInfoMasterList(ParamVO paramVO) throws Exception;
	
	public void regPayerInfo(List<List<Object>> itemList, String userId) throws Exception;
	
	public List<PayerInfoVO> getPayerInfoList(String payerInfoId) throws Exception;
	
	public Map<String,Object> validatePayerInfo(List<List<Object>> itemList) throws Exception;
}
