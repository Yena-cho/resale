package com.ghvirtualaccount.receiptmgmt.dao;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.ghvirtualaccount.vo.ParamVO;
import com.ghvirtualaccount.vo.PayerInfoVO;
import com.ghvirtualaccount.vo.ReceiptTotVO;
import com.ghvirtualaccount.vo.TbReceiptLastDownDtVO;

@Repository("receiptMgmtDao")
public class ReceiptMgmtDao {

	@Resource
	private SqlSessionTemplate sqlSession;

	public ReceiptTotVO selectReceiptStateListCnt(ParamVO paramVO) throws Exception{
		return sqlSession.selectOne("receiptMgmtDao.selectReceiptStateListCnt",paramVO);
	}
	
	public ReceiptTotVO selectReceiptStateReceiptTot(ParamVO paramVO) throws Exception{
		return sqlSession.selectOne("receiptMgmtDao.selectReceiptStateReceiptTot",paramVO);
	}
	
	public ReceiptTotVO selectReceiptStateDefaultTot(ParamVO paramVO) throws Exception{
		return sqlSession.selectOne("receiptMgmtDao.selectReceiptStateDefaultTot",paramVO);
	}
	
//	public TotMsgSendHistVO selectMsgUseStateTot(ParamVO paramVO) throws Exception{
//		return sqlSession.selectOne("receiptMgmtDao.selectMsgUseStateTot",paramVO);
//	}
//	public TotMsgSendHistVO selectMsgUseStateTot(ParamVO paramVO) throws Exception{
//	return sqlSession.selectOne("receiptMgmtDao.selectMsgUseStateTot",paramVO);
//}
//	public TotMsgSendHistVO selectMsgUseStateTot(ParamVO paramVO) throws Exception{
//	return sqlSession.selectOne("receiptMgmtDao.selectMsgUseStateTot",paramVO);
//}
	public List<PayerInfoVO> selectReceiptStateList(ParamVO paramVO) throws Exception{
		return sqlSession.selectList("receiptMgmtDao.selectReceiptStateList",paramVO);
	}

	public List<PayerInfoVO> selectReceiptStateExcel(ParamVO paramVO) throws Exception{
		return sqlSession.selectList("receiptMgmtDao.selectReceiptStateExcel",paramVO);
	}
	
	public PayerInfoVO selectClaimItem(String trno) throws Exception{
		return sqlSession.selectOne("receiptMgmtDao.selectClaimItem",trno);
	}

	public ReceiptTotVO selectReceiptTot(ParamVO paramVO) throws Exception{
		return sqlSession.selectOne("receiptMgmtDao.selectReceiptTot",paramVO);
	}
	
	public List<PayerInfoVO> selectReceiptList(ParamVO paramVO) throws Exception{
		return sqlSession.selectList("receiptMgmtDao.selectReceiptList",paramVO);
	}

	public List<PayerInfoVO> selectReceiptExcel(ParamVO paramVO) throws Exception{
		return sqlSession.selectList("receiptMgmtDao.selectReceiptExcel",paramVO);
	}
	
	public ReceiptTotVO selectDefaultTot(ParamVO paramVO) throws Exception{
		return sqlSession.selectOne("receiptMgmtDao.selectDefaultTot",paramVO);
	}
	
	public List<PayerInfoVO> selectDefaultList(ParamVO paramVO) throws Exception{
		return sqlSession.selectList("receiptMgmtDao.selectDefaultList",paramVO);
	}

	public List<PayerInfoVO> selectDefaultExcel(ParamVO paramVO) throws Exception{
		return sqlSession.selectList("receiptMgmtDao.selectDefaultExcel",paramVO);
	}
	
	public TbReceiptLastDownDtVO selectReceiptLastDownDt() throws Exception{
		return sqlSession.selectOne("receiptMgmtDao.selectReceiptLastDownDt");
	}
	
	public void updateReceiptLastDownDt(TbReceiptLastDownDtVO paramVO) throws Exception{
		sqlSession.update("receiptMgmtDao.updateReceiptLastDownDt",paramVO);
	}
}
