package com.finger.shinhandamoa.sys.cust.web;

import com.finger.shinhandamoa.common.FingerIntegrateMessaging;
import com.finger.shinhandamoa.common.FingerIntegrateMessaging.Message;
import com.finger.shinhandamoa.common.FingerIntegrateMessaging.MessageType;
import com.finger.shinhandamoa.common.ListResult;
import com.finger.shinhandamoa.org.notimgmt.service.NotiService;
import com.finger.shinhandamoa.sys.cust.dto.SysNoticeDTO;
import com.finger.shinhandamoa.sys.cust.service.SysNoticeService;
import com.finger.shinhandamoa.vo.PageVO;
import com.finger.shinhandamoa.vo.UserDetailsVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class SysNoticeMgmtController {
	
	private static final Logger logger = LoggerFactory.getLogger(SysNoticeMgmtController.class);
	
	@Inject
	private NotiService notiService;
	
	@Inject
	private SysNoticeService sysNoticeService;
	
	@Value("${fim.server.host}")
    private String host;
    
    @Value("${fim.server.port}")
    private int port;
    
    @Value("${fim.accessToken}")
    private String accessToken;
    
    @Value("${send.telNo}")
    private String telNo;
    
    @RequestMapping("sys/email-sms-list")
	public ModelAndView counselSetting(
			@RequestParam(defaultValue = "10") int PAGE_SCALE, 
			@RequestParam(defaultValue = "1") int curPage) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		Map<String, Object> reqMap = new HashMap<String, Object>();			
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		// total count
		int totCount = sysNoticeService.sysSmsMngCount(reqMap); //distinct
				
		PageVO page = new PageVO(totCount, curPage, PAGE_SCALE);
			int start = page.getPageBegin();
			int end = page.getPageEnd();
			reqMap.put("start", start);
			reqMap.put("end", end);
		
			List<SysNoticeDTO> selNotiList = sysNoticeService.selNoticeList(reqMap);
			String chast = "";
			for(int i=0; selNotiList.size() > i; i++) {				
			if("ST06".equals(selNotiList.get(i).getUserStatus())) {
				chast="정상이용 기관";
			}else if("ST02".equals(selNotiList.get(i).getUserStatus())) {
				chast="해지 기관";
			}else if("ST08".equals(selNotiList.get(i).getUserStatus())) {
				chast="정지 기관";
			}else if("ST06,ST02".equals(selNotiList.get(i).getUserStatus())) {
				chast="정상/해지 기관";
			}else if("ST06,ST08".equals(selNotiList.get(i).getUserStatus())) {
				chast="정상/정지 기관";
			}else if("ST02,ST08".equals(selNotiList.get(i).getUserStatus())) {
				chast="해지/정지 기관";
			}else {
				chast="전체 기관";
			}
			selNotiList.get(i).setUserStatus(chast);
			}
			
			map.put("list", selNotiList);
			map.put("count", totCount);
			map.put("pager", page); // 페이징 처리를 위한 변수
			
			// 나중에 성공한것 update
			List<SysNoticeDTO> emFailList = sysNoticeService.sysEmFailCount(reqMap);
			if(emFailList.isEmpty()==false) {
			for(int f = 0; f < emFailList.size(); f++) {
				reqMap.put("fcount", 0);
				reqMap.put("emailSeq", emFailList.get(f).getEmailSeq());
				sysNoticeService.sysEmFailCountUpd(reqMap);
			}
			}
			//실패 건수 update
			reqMap.put("sucorfail", "R");
			emFailList = sysNoticeService.sysEmFailCount(reqMap);
			if(emFailList.isEmpty()==false) {
			for(int f = 0; f < emFailList.size(); f++) {
				reqMap.put("fcount", emFailList.get(f).getEmFailCnt());
				reqMap.put("emailSeq", emFailList.get(f).getEmailSeq());
				sysNoticeService.sysEmFailCountUpd(reqMap);
			}
			}
			
			int totEmCount = sysNoticeService.sysEmailMngCount(reqMap); //distinct
			
			PageVO epage = new PageVO(totEmCount, curPage, PAGE_SCALE);
			start = epage.getPageBegin();
			end = epage.getPageEnd();
			reqMap.put("start", start);
			reqMap.put("end", end);
			
			
			List<SysNoticeDTO> selEmNotiList = sysNoticeService.selEmNoticeList(reqMap);
			chast = "";
			for(int i=0; selEmNotiList.size() > i; i++) {				
			if(selEmNotiList.get(i).getEmailChast().equals("ST06")) {
				chast="정상이용 기관";
			}else if(selEmNotiList.get(i).getEmailChast().equals("ST02")) {
				chast="해지 기관";
			}else if(selEmNotiList.get(i).getEmailChast().equals("ST08")) {
				chast="정지 기관";
			}else if(selEmNotiList.get(i).getEmailChast().equals("ST06,ST02")) {
				chast="정상/해지 기관";
			}else if(selEmNotiList.get(i).getEmailChast().equals("ST06,ST08")) {
				chast="정상/정지 기관";
			}else if(selEmNotiList.get(i).getEmailChast().equals("ST02,ST08")) {
				chast="해지/정지 기관";
			}else {
				chast="전체 기관";
			}
			selEmNotiList.get(i).setEmailChast(chast);
			}
			
			map.put("emlist", selEmNotiList);
			map.put("emCount", totEmCount);
			map.put("epager", epage); // 페이징 처리를 위한 변수
			
			map.put("curPage", curPage);
			map.put("PAGE_SCALE", PAGE_SCALE);
			mav.addObject("map", map);
		

		mav.setViewName("sys/cust/custMgmt/email-sms-list");

		return mav;
	}
    
    @ResponseBody
	@RequestMapping("sys/sysSmsCustCount")
	public HashMap<String, Object> sysSmsCustCount(@RequestBody SysNoticeDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> reqMap = new HashMap<String, Object>();

		int iDay = 0;
		if (body.getUserDate().equals("1")) {
			iDay = -3;
		}
		if (body.getUserDate().equals("2")) {
			iDay = -6;
		}

		Calendar temp = new GregorianCalendar(Locale.KOREA);
			temp.setTime(new Date());
			temp.add(Calendar.MONTH, iDay);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String startday = sdf.format(temp.getTime());
		String today = sdf.format(new Date());
			reqMap.put("endday", today);
			reqMap.put("startday", startday);
			reqMap.put("option1", body.getUserStatus());
			reqMap.put("option2", body.getUserDate());
			reqMap.put("option3", body.getUserClass());
		int snDTOCount = sysNoticeService.sysSmsCustCount(reqMap);

		ModelAndView mav = new ModelAndView(); //지우기
			map.put("snDTOCount", snDTOCount);
			map.put("retCode", "0000");
			mav.addObject("map", map);

		return map;
	}
	
	@ResponseBody
	@RequestMapping("sys/sysSmsMsgSend")
	public HashMap<String, Object> sysSmsMsgSend(@RequestBody SysNoticeDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
				
		HashMap<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		
		int iDay = 0;
		String usety="";
		if (body.getUserDate().equals("1")) {
			iDay = -3;
			usety="03";
		}else if (body.getUserDate().equals("2")) {
			iDay = -6;
			usety="06";
		}else {
			usety="00";
		}
		String chast = "";
		if(body.getUserStatus().equals("1")) {
			chast="ST06";
		}else if(body.getUserStatus().equals("2")) {
			chast="ST02";
		}else if(body.getUserStatus().equals("3")) {
			chast="ST08";
		}else if(body.getUserStatus().equals("4")) {
			chast="ST06,ST02";
		}else if(body.getUserStatus().equals("5")) {
			chast="ST06,ST08";
		}else if(body.getUserStatus().equals("6")) {
			chast="ST02,ST08";
		}else {
			chast="ST06,ST02,ST08";
		}
		String chaty="";
		if (body.getUserDate().equals("01")) {
			chaty="01";
		}else if (body.getUserDate().equals("03")) {
			chaty="03";
		}else {
			chaty="01,03";
		}
		Calendar temp = new GregorianCalendar(Locale.KOREA);
			temp.setTime(new Date());
			temp.add(Calendar.MONTH, iDay);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String startday = sdf.format(temp.getTime());
		String today = sdf.format(new Date());
			reqMap.put("endday", today);
			reqMap.put("startday", startday);
			reqMap.put("option1", body.getUserStatus());
			reqMap.put("option2", body.getUserDate());
			reqMap.put("option3", body.getUserClass());
		List<SysNoticeDTO> list = sysNoticeService.sysGetCustList(reqMap);
		
		
		try {
			FingerIntegrateMessaging fingerIntegrateMessaging = new FingerIntegrateMessaging();
			
			fingerIntegrateMessaging.setHost(host);
			fingerIntegrateMessaging.setPort(port);
			fingerIntegrateMessaging.setAccessToken(accessToken);
			
			String title = body.getTitle();
			String smsMsg = body.getSmsMsg();		
			
			int mLength = smsMsg.getBytes("utf-8").length;
			
			MessageType messageType;
			String type = "";
			if(mLength>80){
				messageType = MessageType.MMS;
				type = "1";
			} else {
				messageType = MessageType.SMS;
				type = "0";
			}
			HashMap<String, Object> omap = new HashMap<String, Object>();
			int smsseq = sysNoticeService.maxSmsSeq(reqMap);
			omap.put("smsty", "10");
			omap.put("smsseq", smsseq);
			omap.put("title", title);
			omap.put("msg", smsMsg);
			omap.put("sendTelno", telNo);
			omap.put("msgty", type);
			omap.put("chast", chast);
			omap.put("usety", usety);
			omap.put("chaty", chaty);
			omap.put("sendCnt", list.size());
			int failcnt = 0;
			omap.put("failCnt", failcnt);
			omap.put("sendId", body.getId());
			// sms 전송 후 mng insert
			int result=sysNoticeService.sysInsertSmsReq(omap);			
			
			for(int i = 0; i < list.size(); i++) {
				String hpNo = list.get(i).getHpNo();
				String receiver =""; //list.get(i).getChaCd();
				if(hpNo == null) {
					hpNo = "번호없음";
				}
				final ListResult<FingerIntegrateMessaging.Message> listResult = fingerIntegrateMessaging.createMessage( 
						messageType, title, smsMsg, null, telNo , hpNo, receiver); // sms 발송 
						//문자타입/타이틀/내용/날짜/발송번호/받는번호/받는사람
				HashMap<String, Object> imap = new HashMap<String, Object>();
				if(listResult.getItemList() != null) {
					imap.put("smsReqCd", listResult.getItemList().get(0).getId());
					imap.put("smsty", "10");
					imap.put("smsseq", smsseq);
					imap.put("chaCd", list.get(i).getChaCd());
					imap.put("rcvChrhp", hpNo);
					if(hpNo.equals("번호없음")) {
						imap.put("status", "1"); 			//전송상태 1 : 실패	
					}else{
						imap.put("status", "0"); 			//전송상태 0 : 정상
					}
					// sms 전송 후처리
					int listr=sysNoticeService.sysInsertSmsList(imap);
					map.put("listr", listr);
					
				}
			} //FOR

			map.put("result", result);
			//FAILCNT update
			List<SysNoticeDTO> sendList = sysNoticeService.selSendList(reqMap);
			HashMap<String, Object> cmap = new HashMap<String, Object>();
			for(SysNoticeDTO smsNo : sendList) {
				int id = Integer.parseInt(smsNo.getSmsReqCd());
				Message message = fingerIntegrateMessaging.getResult(id);
				
				if(message.getStatus().getCode().equals("0")) { // 정상
					cmap.put("smsReqCd", smsNo.getSmsReqCd());
					cmap.put("status", "2");
				} else if(message.getStatus().getCode().equals("1")) { // 실패 1
					cmap.put("smsReqCd", smsNo.getSmsReqCd());
					cmap.put("status", "3");
				} else {
					cmap.put("smsReqCd", smsNo.getSmsReqCd());
					cmap.put("status", "0");
				}
				// sms 발송 상태 업데이트
				sysNoticeService.updateStatus(cmap);
			}//for
			sysNoticeService.updateFailCnt(omap);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		map.put("retCode", "0000");
		return map;
	}
	
	@ResponseBody
	@RequestMapping("sys/ajaxSysSmsList")
	public HashMap<String, Object> ajaxSysSmsList(@RequestBody SysNoticeDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> reqMap = new HashMap<String, Object>();			
		
		// total count
		int totCount = sysNoticeService.sysSmsMngCount(reqMap); //distinct
				
		PageVO page = new PageVO(totCount, body.getCurPage(), body.getPageScale());
			int start = page.getPageBegin();
			int end = page.getPageEnd();
			reqMap.put("start", start);
			reqMap.put("end", end);
		
			List<SysNoticeDTO> selNotiList = sysNoticeService.selNoticeList(reqMap);
			String chast = "";
			for(int i=0; selNotiList.size() > i; i++) {
			if(selNotiList.get(i).getUserStatus().equals("ST06")) {
				chast="정상이용 기관";
			}else if(selNotiList.get(i).getUserStatus().equals("ST02")) {
				chast="해지 기관";
			}else if(selNotiList.get(i).getUserStatus().equals("ST08")) {
				chast="정지 기관";
			}else if(selNotiList.get(i).getUserStatus().equals("ST06,ST02")) {
				chast="정상/해지 기관";
			}else if(selNotiList.get(i).getUserStatus().equals("ST06,ST08")) {
				chast="정상/정지 기관";
			}else if(selNotiList.get(i).getUserStatus().equals("ST02,ST08")) {
				chast="해지/정지 기관";
			}else {
				chast="전체 기관";
			}
			selNotiList.get(i).setUserStatus(chast);
			}
		HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("list", selNotiList);
			map.put("count", totCount);
			map.put("pager", page); // 페이징 처리를 위한 변수
			map.put("curPage", body.getCurPage());
			map.put("PAGE_SCALE", body.getPageScale());
			map.put("retCode", "0000");

		return map;
	}
    
	
	@ResponseBody
	@RequestMapping("sys/sysEmailMsgSend")
	public Map<String, Object> emailMsgSend(@RequestBody SysNoticeDTO dto, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO vo = (UserDetailsVO) authentication.getPrincipal();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		
		int iDay = 0;
		String usety="";
		if (dto.getUserDate().equals("1")) {
			iDay = -3;
			usety="03";
		}else if (dto.getUserDate().equals("2")) {
			iDay = -6;
			usety="06";
		}else {
			usety="00";
		}
		String chast = "";
		if(dto.getUserStatus().equals("1")) {
			chast="ST06";
		}else if(dto.getUserStatus().equals("2")) {
			chast="ST02";
		}else if(dto.getUserStatus().equals("3")) {
			chast="ST08";
		}else if(dto.getUserStatus().equals("4")) {
			chast="ST06,ST02";
		}else if(dto.getUserStatus().equals("5")) {
			chast="ST06,ST08";
		}else if(dto.getUserStatus().equals("6")) {
			chast="ST02,ST08";
		}else {
			chast="ST06,ST02,ST08";
		}
		String chaty="";
		if (dto.getUserDate().equals("01")) {
			chaty="01";
		}else if (dto.getUserDate().equals("03")) {
			chaty="03";
		}else {
			chaty="01,03";
		}
		Calendar temp = new GregorianCalendar(Locale.KOREA);
			temp.setTime(new Date());
			temp.add(Calendar.MONTH, iDay);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String startday = sdf.format(temp.getTime());
		String today = sdf.format(new Date());
			reqMap.put("endday", today);
			reqMap.put("startday", startday);
			reqMap.put("option1", dto.getUserStatus());
			reqMap.put("option2", dto.getUserDate());
			reqMap.put("option3", dto.getUserClass());
		List<SysNoticeDTO> list = sysNoticeService.sysGetCustEmList(reqMap);
		
		HashMap<String, Object> omap = new HashMap<String, Object>();
		omap.put("title", dto.getEmailTitle());
		omap.put("msg", dto.getEmailMsg());
		omap.put("chast", chast);
		omap.put("usety", usety);
		omap.put("chaty", chaty);
		omap.put("sendCnt", list.size());
		int failcnt = 0;
		omap.put("failCnt", failcnt);
		omap.put("chaCd", dto.getId());
		int smsseq = sysNoticeService.maxEmailSeq(reqMap);
		omap.put("emailseq", smsseq);
		String emailType = "10";
		omap.put("emailtype", emailType);
					

		for(int i = 0; i <list.size(); i++) {
			String chaCd = list.get(i).getChaCd();
			String chaName = list.get(i).getChaName();
			String cusMail = list.get(i).getCusMail();
			String mail = list.get(i).getMail();
			String loginId = vo.getLoginId();
			String email = vo.getEmail();
			String emailTitle = dto.getEmailTitle();
			String emailMsg = dto.getEmailMsg();
			String emailChast = list.get(i).getEmailChast();

			sendNoticeEmail(smsseq, chaCd, chaName, cusMail, mail, loginId, email, emailTitle, emailMsg, emailChast, "신한다모아");
		}//for
		int result=sysNoticeService.sysInsertEmailReq(omap);
		reqMap.put("result", result);
		reqMap.put("retCode", "0000");
		
		return reqMap;
	}

	public void sendNoticeEmail(int smsseq, String chaCd, String chaName, String cusMail, String mail, String loginId, String email, String emailTitle, String emailMsg, String emailChast, String senderName) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();

		StringBuilder sb = new StringBuilder();
		// 전문,템플릿내용
		sb.append("@s@"); // 그룹헤더
		sb.append("+");
		sb.append("f");
		sb.append("+");
		sb.append("1");
		sb.append("+");
		//sb.append(dto.getEmailMsg()); 	// 내용
		sb.append(emailMsg.replaceAll("\\r|\\n", "<br/>"));
//		sb.append(System.getProperty("line.separator"));

		String content = sb.toString();

		// 메일전송
		map.put("ecareNo", "300");		// 이케어번호
		map.put("channel", 	  "M");						// 채널
		map.put("tmplType",   "J");						// 템플릿타입
		map.put("receiverId", chaCd);			// 수신자id
		map.put("receiverNm", chaName);		// 수신자명
		map.put("receiver",   cusMail +"@"+ mail);	// 수신자mail
		//map.put("senderNm",   dto.getId());			// 발신자명
		map.put("senderNm", senderName);			// 발신자명
		map.put("sender", email);			// 발신자mail
		map.put("subject", emailTitle);		// 제목
		map.put("sendFg", 	  "R");						// 발송여부플래그
		map.put("jonmun", content); 			// 전문내용
		map.put("emailtype", "10");
		map.put("emailseq", smsseq);
		map.put("masMonth",   ""); // 청구월
		map.put("slot2", 	  "");						// 고지구분(1:청구고지, 2:납부고지, 3:미납고지)

		String seq = notiService.selEmailSeq("300");
		map.put("seq", seq);
		logger.info("==============>>>>>>"+sb.toString());

		notiService.insertEmailSend(map);	// 메일전송

		map.put("chast", emailChast);
//		sysNoticeService.sysInsertEmailHist(map);	// 메일이력등록
	}

	@ResponseBody
	@RequestMapping("sys/ajaxSysEmailList")
	public HashMap<String, Object> ajaxSysEmailList(@RequestBody SysNoticeDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> reqMap = new HashMap<String, Object>();			
		
		int totEmCount = sysNoticeService.sysEmailMngCount(reqMap); //distinct
				
		PageVO page = new PageVO(totEmCount, body.getCurPage(), body.getPageScale());
			int start = page.getPageBegin();
			int end = page.getPageEnd();
			reqMap.put("start", start);
			reqMap.put("end", end);
		
			List<SysNoticeDTO> selEmNotiList = sysNoticeService.selEmNoticeList(reqMap);
			String chast = "";
			for(int i=0; selEmNotiList.size() > i; i++) {				
			if(selEmNotiList.get(i).getEmailChast().equals("ST06")) {
				chast="정상이용 기관";
			}else if(selEmNotiList.get(i).getEmailChast().equals("ST02")) {
				chast="해지 기관";
			}else if(selEmNotiList.get(i).getEmailChast().equals("ST08")) {
				chast="정지 기관";
			}else if(selEmNotiList.get(i).getEmailChast().equals("ST06,ST02")) {
				chast="정상/해지 기관";
			}else if(selEmNotiList.get(i).getEmailChast().equals("ST06,ST08")) {
				chast="정상/정지 기관";
			}else if(selEmNotiList.get(i).getEmailChast().equals("ST02,ST08")) {
				chast="해지/정지 기관";
			}else {
				chast="전체 기관";
			}
			selEmNotiList.get(i).setEmailChast(chast);
			}
			
		HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("elist", selEmNotiList);
			map.put("ecount", totEmCount);
			map.put("epager", page); // 페이징 처리를 위한 변수
			map.put("curPage", body.getCurPage());
			map.put("PAGE_SCALE", body.getPageScale());
			map.put("retCode", "0000");

		return map;
	}

	@RequestMapping("sys/email-sms-direct")
	public ModelAndView emailSmsDirectMessage( ) throws Exception{
		ModelAndView mav = new ModelAndView();

		mav.setViewName("sys/cust/custMgmt/email-sms-direct");

		return mav;
	}

	@ResponseBody
	@RequestMapping("sys/sysEmailDirectSend")
	public Map<String, Object> sysEmailDirectSend(@RequestBody SysNoticeDTO body) throws Exception{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> reqMap = new HashMap<String, Object>();

		int smsseq = sysNoticeService.maxEmailSeq(new HashMap<>());
		String chaCd = "99999999";
		String chaName = "관리자공지";
		String[] emailAdrList = body.getEmailAdr().split(",");
		List<String> emailList = Arrays.asList(emailAdrList);

		logger.info("관리자 개별 이메일 발송 {} 건" , emailList.size());

		try{
			for (String eadr : emailList) {
				String email = eadr.trim();
				logger.debug("수신주소 {}", email);
				String emailFront = StringUtils.substringBefore(email, "@");
				String emailBack = StringUtils.substringAfter(email, "@");
				sendNoticeEmail(smsseq, chaCd, chaName, emailFront, emailBack, "damoaadm", "damoa@finger.co.kr", body.getEtitle(), body.getEcontent(), "ST06", "신한다모아");
			}
		} catch (Exception e){
			logger.info("이메일 개별발송 실패");
			logger.info(e.getMessage());
			reqMap.put("retCode", "0001");
			return reqMap;
		}

		logger.info("관리자 개별 이메일 발송 완료");
		reqMap.put("retCode", "0000");
		return reqMap;
	}

	@ResponseBody
	@RequestMapping("sys/sysSmsDirectSend")
	public Map<String, Object> sysSmsDirectSend(@RequestBody SysNoticeDTO body) throws Exception{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> reqMap = new HashMap<String, Object>();

		FingerIntegrateMessaging fingerIntegrateMessaging = new FingerIntegrateMessaging();
		fingerIntegrateMessaging.setHost(host);
		fingerIntegrateMessaging.setPort(port);
		fingerIntegrateMessaging.setAccessToken(accessToken);

		String[] hpNoList = body.getPhoneNo().split(",");
		List<String> hoList = Arrays.asList(hpNoList);

		logger.info("관리자 개별 문자 발송 {} 건" , hoList.size());
		try{
			for (String hpno : hoList) {
				logger.debug("수신번호 {}", hpno);
				final ListResult<FingerIntegrateMessaging.Message> listResult = fingerIntegrateMessaging.createMessage(
						FingerIntegrateMessaging.MessageType.MMS, body.getStitle(), body.getScontent(), null, telNo, hpno.trim(), "신한다모아"); // sms 발송

				HashMap<String, Object> imap = new HashMap<String, Object>();
				// 관리자에서 보내는 개별문자 전송을 smsreq 테이블에 기록
				if (listResult != null) {

					imap.put("smsReqCd", listResult.getItemList().get(0).getId());
					imap.put("chaCd", "10000000");
					imap.put("fiCd", "088");            // 은행코드
					imap.put("cusHp", hpno.trim());
					imap.put("telNo", telNo);
					imap.put("status", "0");            //전송상태 0 : 대기
					imap.put("rslt", "");                // 결과코드  >>>>> 코드값 확인
					imap.put("msg", body.getScontent());
					imap.put("type", "1");                // 메시지전송타입 SMS(0), UrlPush(1)
					imap.put("sendCnt", 0);            // 시도횟수
					imap.put("smsReqSt", "");
					// 상태 ST07, ST08  >>>>> 코드값 확인
					imap.put("chatrTy", "01");            // 가맹점접속형태 Web(01), 전문(03)
					imap.put("notiMasCd", "00000000000000000000");
					// sms 전송 후처리
					notiService.insertSmsReq(imap);
				}

			}
		} catch (Exception e){
			logger.info("문자 개별발송 실패");
			logger.info(e.getMessage());
			reqMap.put("retCode", "0001");
			return reqMap;
		}

		logger.info("관리자 개별 문자 발송 완료");
		reqMap.put("retCode", "0000");
		return reqMap;
	}

}
