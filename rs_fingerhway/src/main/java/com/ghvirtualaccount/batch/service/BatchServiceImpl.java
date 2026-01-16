package com.ghvirtualaccount.batch.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ghvirtualaccount.batch.dao.BatchDao;
import com.ghvirtualaccount.cmmn.FingerIntegrateMessaging;
import com.ghvirtualaccount.vo.TbMsgSendHistVO;

@Service("batchService")
public class BatchServiceImpl implements BatchService {

	private static Logger logger = LoggerFactory.getLogger(BatchServiceImpl.class);

    @Value("${fim.server.host}")
    private String host;
    
    @Value("${fim.server.port}")
    private int port;
    
    @Value("${fim.accessToken}")
    private String accessToken;
    
	@Resource(name="batchDao")
	private BatchDao batchDao;
	
	@Override
	public void returnVirtualAccount() throws Exception{
		
		logger.debug("========= returnVirtualAccount start ========");
		
		//납부기한이 지난 건 가상계좌 사용가능으로 업데이트
		int updateCnt = batchDao.updateTbVirtualAcntUseYn1();
		logger.debug("가상계좌 마감일 7일 이후 반납 건 수 : " + updateCnt);
		//수납건 가상계좌 사용가능으로 업데이트
		updateCnt = batchDao.updateTbVirtualAcntUseYn2();
		logger.debug("가상계좌 수납으로 반납 건 수 : " + updateCnt);
	}
	
	@Override
	public void checkSendMsgResult() throws Exception{
		
		logger.debug("========= checkSendMsgResult start ========");
		logger.debug("xxxxxxxx fim.server.host : "+host+" xxxxxxxxxx");
		
		//메세지 전송 이력 테이블 조회
		List<TbMsgSendHistVO> tbMsgSendHistList = batchDao.selectMsgSendHistNotProcessList();
		
		
		//메세지 전송 이력 결과 조회
		FingerIntegrateMessaging fingerIntegrateMessaging = new FingerIntegrateMessaging();
		fingerIntegrateMessaging.setHost(host);
		fingerIntegrateMessaging.setPort(port);
		fingerIntegrateMessaging.setAccessToken(accessToken);
		
		for(TbMsgSendHistVO tbMsgSendHistVO : tbMsgSendHistList){
			
			//임시로 발신자 번호는  15449350로 세팅 이후로는 클라이언트에서 받은 값으로 처리 하여야 한다.
			long id = tbMsgSendHistVO.getMsgId().longValue();
			final FingerIntegrateMessaging.Message message = fingerIntegrateMessaging.getResult(id);
			
			logger.debug("message: {}", message);
			
			if(message!=null&&!message.getStatus().getCode().equals("")){
				
				String code = message.getStatus().getCode();
				if(code.equals("0")){
					tbMsgSendHistVO.setSuccessYn("1");	//정상
				} else {
					tbMsgSendHistVO.setSuccessYn("9");	//오류
				}
				
				batchDao.updateTbMsgSendHistSuccessYn(tbMsgSendHistVO);
			}
			
		}
		

	}

}
