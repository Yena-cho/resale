package com.ghvirtualaccount.analysismgmt.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ghvirtualaccount.analysismgmt.dao.AnalysisMgmtDao;
import com.ghvirtualaccount.vo.MsgSendHistInfoVO;
import com.ghvirtualaccount.vo.MsgUseStateInfoVO;
import com.ghvirtualaccount.vo.ParamVO;
import com.ghvirtualaccount.vo.TbMsgSendHistVO;
import com.ghvirtualaccount.vo.TotMsgSendHistVO;
import com.ghvirtualaccount.vo.VAcntAnalysisTotVO;
import com.ghvirtualaccount.vo.VAcntAnalysisVO;

@Service("analysisMgmtService")
public class AnalysisMgmtServiceImpl implements AnalysisMgmtService {

	private static Logger logger = LoggerFactory.getLogger(AnalysisMgmtServiceImpl.class);
	
	@Resource(name="analysisMgmtDao")
	private AnalysisMgmtDao analysisMgmtDao;
	
	//가상계좌수납통계 일별 총계
	@Override
	public VAcntAnalysisTotVO getVAccountDayReceiptTot(ParamVO paramVO) throws Exception{
		return analysisMgmtDao.selectVAccountDayReceiptTot(paramVO);
	}
	//가상계좌수납통계 일별 리스트 조회
	@Override
	public List<VAcntAnalysisVO> getVAccountDayReceiptList(ParamVO paramVO) throws Exception{
		return analysisMgmtDao.selectVAccountDayReceiptList(paramVO);
	}
	//가상계좌수납통계 일별 엑셀 조회
	@Override
	public List<VAcntAnalysisVO> getVAccountDayReceiptExcel(ParamVO paramVO) throws Exception{
		return analysisMgmtDao.selectVAccountDayReceiptExcel(paramVO);
	}
	//가상계좌수납통계 월별 총계
	@Override
	public VAcntAnalysisTotVO getVAccountMonthReceiptTot(ParamVO paramVO) throws Exception{
		return analysisMgmtDao.selectVAccountMonthReceiptTot(paramVO);
	}
	//가상계좌수납통계 월별 리스트 조회
	@Override
	public List<VAcntAnalysisVO> getVAccountMonthReceiptList(ParamVO paramVO) throws Exception{
		return analysisMgmtDao.selectVAccountMonthReceiptList(paramVO);
	}
	//가상계좌수납통계 월별 엑셀 조회
	@Override
	public List<VAcntAnalysisVO> getVAccountMonthReceiptExcel(ParamVO paramVO) throws Exception{
		return analysisMgmtDao.selectVAccountMonthReceiptExcel(paramVO);
	}
	//가상계좌이용현황 총계
	@Override
	public VAcntAnalysisTotVO getVAccountUseStateTot(ParamVO paramVO) throws Exception{
		return analysisMgmtDao.selectVAccountUseStateTot(paramVO);
	}
	//가상계좌이용현황 리스트 조회
	@Override
	public List<VAcntAnalysisVO> getVAccountUseStateList(ParamVO paramVO) throws Exception{
		return analysisMgmtDao.selectVAccountUseStateList(paramVO);
	}
	
	@Override
	public int getMsgUseStateListCnt(ParamVO paramVO) throws Exception{
		
		logger.debug("========= getMsgUseStateListCnt start ========");
		return analysisMgmtDao.selectMsgUseStateListCnt(paramVO);
		
	}
	@Override
	public TotMsgSendHistVO getMsgUseStateTot(ParamVO paramVO) throws Exception{
		
		logger.debug("========= getMsgUseStateTot start ========");
		return analysisMgmtDao.selectMsgUseStateTot(paramVO);
		
	}
	@Override
	public List<MsgUseStateInfoVO> getMsgUseStateList(ParamVO paramVO) throws Exception{
		
		logger.debug("========= getMsgUseStateList start ========");
		return analysisMgmtDao.selectMsgUseStateList(paramVO);
		
	}
	@Override
	public int getMsgSendHistListCnt(ParamVO paramVO) throws Exception{
		
		logger.debug("========= getMsgSendHistListCnt start ========");
		return analysisMgmtDao.selectMsgSendHistListCnt(paramVO);
		
	}
	@Override
	public List<MsgSendHistInfoVO> getMsgSendHistList(ParamVO paramVO) throws Exception{
		
		logger.debug("========= addMsgSendHist start ========");
		return analysisMgmtDao.selectMsgSendHistList(paramVO);
		
	}
	@Override
	public TbMsgSendHistVO getMsgSendHist(String msgSendHistId) throws Exception{
		
		logger.debug("========= getMsgSendHist start ========");
		return analysisMgmtDao.selectMsgSendHist(msgSendHistId);
		
	}
	
}
