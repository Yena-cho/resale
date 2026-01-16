package com.ghvirtualaccount.batch.dao;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.ghvirtualaccount.vo.TbMsgSendHistVO;

@Repository("batchDao")
public class BatchDao {

	@Resource
	private SqlSessionTemplate sqlSession;
	
	public int updateTbVirtualAcntUseYn1() throws Exception{
		return sqlSession.update("batchDao.updateTbVirtualAcntUseYn1");
	}
	
	public int updateTbVirtualAcntUseYn2() throws Exception{
		return sqlSession.update("batchDao.updateTbVirtualAcntUseYn2");
	}
	
	public List<TbMsgSendHistVO> selectMsgSendHistNotProcessList() throws Exception{
		return sqlSession.selectList("batchDao.selectMsgSendHistNotProcessList");
	}
	
	public void updateTbMsgSendHistSuccessYn(TbMsgSendHistVO paramVO) throws Exception{
		sqlSession.update("batchDao.updateTbMsgSendHistSuccessYn",paramVO);
	}
}
