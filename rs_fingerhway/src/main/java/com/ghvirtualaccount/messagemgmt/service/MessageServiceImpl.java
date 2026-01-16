package com.ghvirtualaccount.messagemgmt.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ghvirtualaccount.messagemgmt.dao.MessageDao;
import com.ghvirtualaccount.vo.TbMsgSendHistVO;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

	private static Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
	
	@Resource(name="messageDao")
	private MessageDao messageDao;
	
	@Override
	public void addMsgSendHist(TbMsgSendHistVO tbMsgSendHistVO) throws Exception{
		
		logger.debug("========= addMsgSendHist start ========");
		messageDao.insertMsgSendHist(tbMsgSendHistVO);
		
	}

}
