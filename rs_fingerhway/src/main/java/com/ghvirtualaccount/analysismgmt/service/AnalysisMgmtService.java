package com.ghvirtualaccount.analysismgmt.service;

import java.util.List;

import com.ghvirtualaccount.vo.MsgSendHistInfoVO;
import com.ghvirtualaccount.vo.MsgUseStateInfoVO;
import com.ghvirtualaccount.vo.ParamVO;
import com.ghvirtualaccount.vo.TbMsgSendHistVO;
import com.ghvirtualaccount.vo.TotMsgSendHistVO;
import com.ghvirtualaccount.vo.VAcntAnalysisTotVO;
import com.ghvirtualaccount.vo.VAcntAnalysisVO;

public interface AnalysisMgmtService {

	//가상계좌수납통계 일별 총계
	public VAcntAnalysisTotVO getVAccountDayReceiptTot(ParamVO paramVO) throws Exception;
	
	//가상계좌수납통계 일별 리스트 조회
	public List<VAcntAnalysisVO> getVAccountDayReceiptList(ParamVO paramVO) throws Exception;
	
	//가상계좌수납통계 일별 엑셀 조회
	public List<VAcntAnalysisVO> getVAccountDayReceiptExcel(ParamVO paramVO) throws Exception;
	
	//가상계좌수납통계 월별 총계
	public VAcntAnalysisTotVO getVAccountMonthReceiptTot(ParamVO paramVO) throws Exception;
	
	//가상계좌수납통계 월별 리스트 조회
	public List<VAcntAnalysisVO> getVAccountMonthReceiptList(ParamVO paramVO) throws Exception;
	
	//가상계좌수납통계 월별 엑셀 조회
	public List<VAcntAnalysisVO> getVAccountMonthReceiptExcel(ParamVO paramVO) throws Exception;
		
	//가상계좌이용현황 총계
	public VAcntAnalysisTotVO getVAccountUseStateTot(ParamVO paramVO) throws Exception;
	
	//가상계좌이용현황 리스트 조회
	public List<VAcntAnalysisVO> getVAccountUseStateList(ParamVO paramVO) throws Exception;
	
	//메세지이용현황 카운트 조회
	public int getMsgUseStateListCnt(ParamVO paramVO) throws Exception;
	
	//메세지이용현황 합계 조회
	public TotMsgSendHistVO getMsgUseStateTot(ParamVO paramVO) throws Exception;

	//메세지이용현황 리스트 조회
	public List<MsgUseStateInfoVO> getMsgUseStateList(ParamVO paramVO) throws Exception;
	
	//메세지발송내역 카운트 조회
	public int getMsgSendHistListCnt(ParamVO paramVO) throws Exception;
	
	//메세지발송내역 리스트 조회
	public List<MsgSendHistInfoVO> getMsgSendHistList(ParamVO paramVO) throws Exception;
	
	//메세지발송내역 조회
	public TbMsgSendHistVO getMsgSendHist(String msgSendHistId) throws Exception;
}
