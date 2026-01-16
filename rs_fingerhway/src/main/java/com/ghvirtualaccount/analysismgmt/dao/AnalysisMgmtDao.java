package com.ghvirtualaccount.analysismgmt.dao;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.ghvirtualaccount.vo.MsgSendHistInfoVO;
import com.ghvirtualaccount.vo.MsgUseStateInfoVO;
import com.ghvirtualaccount.vo.ParamVO;
import com.ghvirtualaccount.vo.TbMsgSendHistVO;
import com.ghvirtualaccount.vo.TotMsgSendHistVO;
import com.ghvirtualaccount.vo.VAcntAnalysisTotVO;
import com.ghvirtualaccount.vo.VAcntAnalysisVO;

@Repository("analysisMgmtDao")
public class AnalysisMgmtDao {

	@Resource
	private SqlSessionTemplate sqlSession;

	//가상계좌수납통계 일별 총계
	public VAcntAnalysisTotVO selectVAccountDayReceiptTot(ParamVO paramVO) throws Exception{
		return sqlSession.selectOne("analysisMgmtDao.selectVAccountDayReceiptTot",paramVO);
	}
	//가상계좌수납통계 일별 리스트 조회
	public List<VAcntAnalysisVO> selectVAccountDayReceiptList(ParamVO paramVO) throws Exception{
		return sqlSession.selectList("analysisMgmtDao.selectVAccountDayReceiptList",paramVO);
	}
	//가상계좌수납통계 일별 엑셀 조회
	public List<VAcntAnalysisVO> selectVAccountDayReceiptExcel(ParamVO paramVO) throws Exception{
		return sqlSession.selectList("analysisMgmtDao.selectVAccountDayReceiptExcel",paramVO);
	}
	//가상계좌수납통계 월별 총계
	public VAcntAnalysisTotVO selectVAccountMonthReceiptTot(ParamVO paramVO) throws Exception{
		return sqlSession.selectOne("analysisMgmtDao.selectVAccountMonthReceiptTot",paramVO);
	}
	//가상계좌수납통계 월별 리스트 조회
	public List<VAcntAnalysisVO> selectVAccountMonthReceiptList(ParamVO paramVO) throws Exception{
		return sqlSession.selectList("analysisMgmtDao.selectVAccountMonthReceiptList",paramVO);
	}
	//가상계좌수납통계 월별 엑셀 조회
	public List<VAcntAnalysisVO> selectVAccountMonthReceiptExcel(ParamVO paramVO) throws Exception{
		return sqlSession.selectList("analysisMgmtDao.selectVAccountMonthReceiptExcel",paramVO);
	}
	//가상계좌이용현황 총계
	public VAcntAnalysisTotVO selectVAccountUseStateTot(ParamVO paramVO) throws Exception{
		return sqlSession.selectOne("analysisMgmtDao.selectVAccountUseStateTot",paramVO);
	}
	//가상계좌이용현황 리스트 조회
	public List<VAcntAnalysisVO> selectVAccountUseStateList(ParamVO paramVO) throws Exception{
		return sqlSession.selectList("analysisMgmtDao.selectVAccountUseStateList",paramVO);
	}
	//문자이용현황 리스트 카운트  조회
	public int selectMsgUseStateListCnt(ParamVO paramVO) throws Exception{
		return sqlSession.selectOne("analysisMgmtDao.selectMsgUseStateListCnt",paramVO);
	}
	//문자이용현황 합계 조회
	public TotMsgSendHistVO selectMsgUseStateTot(ParamVO paramVO) throws Exception{
		return sqlSession.selectOne("analysisMgmtDao.selectMsgUseStateTot",paramVO);
	}
	//문자이용현황 리스트 조회
	public List<MsgUseStateInfoVO> selectMsgUseStateList(ParamVO paramVO) throws Exception{
		return sqlSession.selectList("analysisMgmtDao.selectMsgUseStateList",paramVO);
	}
	//문자발송내역 리스트 카운트 조회
	public int selectMsgSendHistListCnt(ParamVO paramVO) throws Exception{
		return sqlSession.selectOne("analysisMgmtDao.selectMsgSendHistListCnt",paramVO);
	}
	//문자발송내역 리스트 카운트 조회
	public List<MsgSendHistInfoVO> selectMsgSendHistList(ParamVO paramVO) throws Exception{
		return sqlSession.selectList("analysisMgmtDao.selectMsgSendHistList",paramVO);
	}
	//문자발송내역  조회
	public TbMsgSendHistVO selectMsgSendHist(String msgSendHistId) throws Exception{
		return sqlSession.selectOne("analysisMgmtDao.selectMsgSendHist",msgSendHistId);
	}
	
}
