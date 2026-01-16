package com.ghvirtualaccount.receiptmgmt.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ghvirtualaccount.cmmn.StrUtil;
import com.ghvirtualaccount.receiptmgmt.dao.ReceiptMgmtDao;
import com.ghvirtualaccount.vo.ParamVO;
import com.ghvirtualaccount.vo.PayerInfoVO;
import com.ghvirtualaccount.vo.ReceiptTotVO;
import com.ghvirtualaccount.vo.TbReceiptLastDownDtVO;

@Service("receiptMgmtService")
public class ReceiptMgmtServiceImpl implements ReceiptMgmtService {

	private static Logger logger = LoggerFactory.getLogger(ReceiptMgmtService.class);
	
	@Resource(name="receiptMgmtDao")
	private ReceiptMgmtDao receiptMgmtDao;
	
	@Override
	public ReceiptTotVO getReceiptStateTot(ParamVO paramVO) throws Exception{
		
		logger.debug("========= getReceiptStateTot start ========");
		
		ReceiptTotVO receiptTotVO1 =  receiptMgmtDao.selectReceiptStateListCnt(paramVO);
		ReceiptTotVO receiptTotVO2 =  receiptMgmtDao.selectReceiptStateReceiptTot(paramVO);
		receiptTotVO1.setTotReceiptCnt(receiptTotVO2.getTotReceiptCnt());
		receiptTotVO1.setTotReceiptAmt(receiptTotVO2.getTotReceiptAmt());
		ReceiptTotVO receiptTotVO3 =  receiptMgmtDao.selectReceiptStateDefaultTot(paramVO);
		receiptTotVO1.setTotDefaultAmt(receiptTotVO3.getTotDefaultAmt());
		receiptTotVO1.setTotDefaultCnt(receiptTotVO3.getTotDefaultCnt());
		
		return receiptTotVO1;
	}
//	@Override
//	public TotMsgSendHistVO getMsgUseStateTot(ParamVO paramVO) throws Exception{
//		
//		logger.debug("========= getMsgUseStateTot start ========");
//		return receiptMgmtDao.selectMsgUseStateTot(paramVO);
//		
//	}
	@Override
	public List<PayerInfoVO> getReceiptStateList(ParamVO paramVO) throws Exception{
		
		logger.debug("========= getReceiptStateList start ========");
		//수납현황목록 조회
		List<PayerInfoVO> payerInfoVOList= receiptMgmtDao.selectReceiptStateList(paramVO);
		
		StrUtil util = new StrUtil();
		
		if(payerInfoVOList.size()>0){
			//문자 전송가능 여부를 세팅 한다.
			for(PayerInfoVO payerInfoVO:payerInfoVOList){

				//수납여부가 Y 인건
				if(payerInfoVO.getPayYn().equals("Y")){
					payerInfoVO.setMsgSendYn("N");
				//마감일자가 지난 건
				} else 	if(Integer.parseInt(payerInfoVO.getPayDueDt().replaceAll("\\.", "")) < Integer.parseInt(util.getCurrentDateStr())){
					payerInfoVO.setMsgSendYn("N");
				} else {
					payerInfoVO.setMsgSendYn("Y");
				}
			}
		}
		
		return payerInfoVOList;
		
	}
	@Override
	public List<PayerInfoVO> getReceiptStateExcel(ParamVO paramVO) throws Exception{
		
		logger.debug("========= getReceiptStateExcel start ========");
		return receiptMgmtDao.selectReceiptStateExcel(paramVO);
		
	}
	@Override
	public PayerInfoVO getClaimItem(String trno) throws Exception{
		
		logger.debug("========= getClaimItem start ========");
		return receiptMgmtDao.selectClaimItem(trno);
		
	}
	@Override
	public ReceiptTotVO getReceiptTot(ParamVO paramVO) throws Exception{
		
		logger.debug("========= getReceiptTot start ========");
		return receiptMgmtDao.selectReceiptTot(paramVO);
		
	}
	@Override
	public List<PayerInfoVO> getReceiptList(ParamVO paramVO) throws Exception{
		
		logger.debug("========= getReceiptList start ========");
		return receiptMgmtDao.selectReceiptList(paramVO);
		
	}
	@Override
	public List<PayerInfoVO> getReceiptExcel(ParamVO paramVO) throws Exception{
		
		logger.debug("========= getReceiptExcel start ========");
		//최종 수납목록 다운로드 테이블에 조회기간 저장.
		TbReceiptLastDownDtVO tbReceiptLastDownDtVO = new TbReceiptLastDownDtVO();
		tbReceiptLastDownDtVO.setInqStartDt(paramVO.getStartDate());
		tbReceiptLastDownDtVO.setInqEndDt(paramVO.getEndDate());
		receiptMgmtDao.updateReceiptLastDownDt(tbReceiptLastDownDtVO);
		
		return receiptMgmtDao.selectReceiptExcel(paramVO);
		
	}
	@Override
	public ReceiptTotVO getDefaultTot(ParamVO paramVO) throws Exception{
		
		logger.debug("========= getDefaultTot start ========");
		return receiptMgmtDao.selectDefaultTot(paramVO);
		
	}
	@Override
	public List<PayerInfoVO> getDefaultList(ParamVO paramVO) throws Exception{
		
		logger.debug("========= getDefaultList start ========");
		return receiptMgmtDao.selectDefaultList(paramVO);
		
	}
	@Override
	public List<PayerInfoVO> getDefaultExcel(ParamVO paramVO) throws Exception{
		
		logger.debug("========= getDefaultExcel start ========");
		return receiptMgmtDao.selectDefaultExcel(paramVO);
		
	}
	@Override
	public TbReceiptLastDownDtVO getReceiptLastDownDt() throws Exception{
		logger.debug("========= getReceiptLastDownDt start ========");
		return receiptMgmtDao.selectReceiptLastDownDt();
	}
		
}
