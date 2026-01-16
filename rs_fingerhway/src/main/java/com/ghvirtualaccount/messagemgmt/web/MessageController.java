package com.ghvirtualaccount.messagemgmt.web;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ghvirtualaccount.cmmn.FingerIntegrateMessaging;
import com.ghvirtualaccount.cmmn.FingerIntegrateMessaging.MessageType;
import com.ghvirtualaccount.cmmn.ListResult;
import com.ghvirtualaccount.messagemgmt.service.MessageService;
import com.ghvirtualaccount.vo.ParamVO;
import com.ghvirtualaccount.vo.TbMsgSendHistVO;
import com.google.gson.Gson;

@SuppressWarnings("rawtypes")
@Controller
public class MessageController {

	private static Logger logger = LoggerFactory.getLogger(MessageController.class);
	
    @Value("${fim.server.host}")
    private String host;
    
    @Value("${fim.server.port}")
    private int port;
    
    @Value("${fim.accessToken}")
    private String accessToken;
    
	@Resource(name="messageService")
	private MessageService messageService;
	
	//--------------------------------------------------------------
	// 메세지 전송
	//--------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sendMsg.do", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public Map sendMsg(@RequestBody ParamVO paramVO, HttpServletRequest request) {
		
		Gson gson = new Gson();
		Map resultMap = new HashMap();
		
		logger.debug("xxxxxxxx sendMsg start xxxxxxxxxx");
		logger.debug("xxxxxxxx fim.server.host : "+host+" xxxxxxxxxx");
		
		
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			int mLength = paramVO.getMsgContent().getBytes("euc-kr").length;
			logger.debug("xxxxxxxx mLength : "+mLength+" xxxxxxxxxxxxxxx");
			
			FingerIntegrateMessaging fingerIntegrateMessaging = new FingerIntegrateMessaging();
			
			fingerIntegrateMessaging.setHost(host);
			fingerIntegrateMessaging.setPort(port);
			fingerIntegrateMessaging.setAccessToken(accessToken);
			
			MessageType messageType;
			
			if(mLength>80){
				messageType = MessageType.MMS;
			} else {
				messageType = MessageType.SMS;
			}
			
			
			//임시로 발신자 번호는  15449350로 세팅 이후로는 클라이언트에서 받은 값으로 처리 하여야 한다.
			final ListResult<FingerIntegrateMessaging.Message> listResult = fingerIntegrateMessaging.createMessage( 
					messageType, "", paramVO.getMsgContent(), null, paramVO.getSendNum(), paramVO.getRecvNum(), "경기고속도로");

			logger.debug("listResult: {}", listResult);
			
			
			if(listResult.getItemList()!=null){
				resultMap.put("listResult", listResult);
				
				//전송정보 저장
				TbMsgSendHistVO tbMsgSendHistVO = new TbMsgSendHistVO();
	
				//문자 길이에 따라 장단문 구분, 사용요금 세팅(요금 확인하여 세팅)
				switch(messageType){
					case MMS:
						tbMsgSendHistVO.setSmsLmsInd("L");
						tbMsgSendHistVO.setUseAmt(BigDecimal.valueOf(40));
						break;
					case SMS:
						tbMsgSendHistVO.setSmsLmsInd("S");
						tbMsgSendHistVO.setUseAmt(BigDecimal.valueOf(20));
						break;
					default:
						break;
				}
				tbMsgSendHistVO.setMsgContent(paramVO.getMsgContent());
				tbMsgSendHistVO.setRecvNum(paramVO.getRecvNum());
				tbMsgSendHistVO.setSendNum(paramVO.getSendNum());
				tbMsgSendHistVO.setSuccessYn("0");
				tbMsgSendHistVO.setRegUserId(paramVO.getUserId());
				tbMsgSendHistVO.setMsgId(new BigDecimal(listResult.getItemList().get(0).getId()));
				messageService.addMsgSendHist(tbMsgSendHistVO);
			} else {
				resultMap.put("retCode", "0001");
				resultMap.put("retMsg", "메세지 전송요청 중 오류가 발생 하였습니다.");
				
				return resultMap;
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			resultMap.put("retCode", "9999");
			resultMap.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
			
		}
		
		resultMap.put("retCode", "0000");
		resultMap.put("retMsg", "정상적으로 전송 요청 하였습니다.");
		
		return resultMap;
    }
	
	//--------------------------------------------------------------
	// 메세지전송 팝업
	//--------------------------------------------------------------
	@RequestMapping(value="/sendMsgPopup.do")
    public String sendMsgPopup(final Model model, final HttpServletRequest request) {
		
		return "messagemgmt/sendMsgPop";
    }
}
