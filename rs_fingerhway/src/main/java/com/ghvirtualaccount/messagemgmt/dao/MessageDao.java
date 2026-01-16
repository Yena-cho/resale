package com.ghvirtualaccount.messagemgmt.dao;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.ghvirtualaccount.vo.TbMsgSendHistVO;

@Repository("messageDao")
public class MessageDao {

	@Resource
	private SqlSessionTemplate sqlSession;
	
	public void insertMsgSendHist(TbMsgSendHistVO tbMsgSendHistVO) throws Exception{
		sqlSession.insert("MessageDao.insertMsgSendHist",tbMsgSendHistVO);
	}
}
