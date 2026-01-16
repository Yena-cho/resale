package com.ghvirtualaccount.receiptmgmt.service;

import java.util.List;

import com.ghvirtualaccount.vo.ParamVO;
import com.ghvirtualaccount.vo.PayerInfoVO;
import com.ghvirtualaccount.vo.ReceiptTotVO;
import com.ghvirtualaccount.vo.TbReceiptLastDownDtVO;

public interface ReceiptMgmtService {

	public ReceiptTotVO getReceiptStateTot(ParamVO paramVO) throws Exception;
	
//	public TotMsgSendHistVO getMsgUseStateTot(ParamVO paramVO) throws Exception;

	public List<PayerInfoVO> getReceiptStateList(ParamVO paramVO) throws Exception;
	
	public List<PayerInfoVO> getReceiptStateExcel(ParamVO paramVO) throws Exception;
	
	public PayerInfoVO getClaimItem(String trno) throws Exception;
	
	public ReceiptTotVO getReceiptTot(ParamVO paramVO) throws Exception;
	
	public List<PayerInfoVO> getReceiptList(ParamVO paramVO) throws Exception;
	
	public List<PayerInfoVO> getReceiptExcel(ParamVO paramVO) throws Exception;
	
	public ReceiptTotVO getDefaultTot(ParamVO paramVO) throws Exception;
	
	public List<PayerInfoVO> getDefaultList(ParamVO paramVO) throws Exception;
	
	public List<PayerInfoVO> getDefaultExcel(ParamVO paramVO) throws Exception;
	
	public TbReceiptLastDownDtVO getReceiptLastDownDt() throws Exception;
	
}
