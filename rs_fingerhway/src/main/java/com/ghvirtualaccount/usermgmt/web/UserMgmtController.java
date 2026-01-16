package com.ghvirtualaccount.usermgmt.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ghvirtualaccount.usermgmt.service.UserMgmtService;
import com.ghvirtualaccount.vo.ParamVO;
import com.ghvirtualaccount.vo.TbBelongInfoVO;
import com.ghvirtualaccount.vo.TbUserVO;
import com.ghvirtualaccount.vo.UserInfoVO;
import com.google.gson.Gson;

@SuppressWarnings("rawtypes")
@Controller
public class UserMgmtController {

	private static Logger logger = LoggerFactory.getLogger(UserMgmtController.class);
	
	@Resource(name="userMgmtService")
	private UserMgmtService userMgmtService;
	
    @Value("${userinfo.pagesize}")
    private int pgsize;
	
	//--------------------------------------------------------------
	// 사용자목록 조회 - Ajax 사용 안 함
	//--------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getUserInfoList.do", method=RequestMethod.POST)
    public String getUserInfoList(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request) throws Exception {
		
		Gson gson = new Gson();
		Map resultMap = new HashMap();
		
		logger.debug("xxxxxxxx getUserList start xxxxxxxxxx");
		paramVO.setPageIdx(1);
		paramVO.setPageSize(pgsize);
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			logger.debug("xxxxxxxx paramVO.getPageSize() : "+paramVO.getPageSize());
			
			int totCnt = userMgmtService.getUserInfoListCnt(paramVO);
			int maxPage = 0;
			if (totCnt > 0) {
				maxPage = totCnt / paramVO.getPageSize();
				if((totCnt % paramVO.getPageSize()) > 0){
					maxPage++;
				}				
			}
			
			resultMap.put("totCnt", totCnt);
//			resultMap.put("pageIdx", paramVO.getPageIdx());
//			resultMap.put("pageSize", paramVO.getPageSize());
			resultMap.put("maxPage", maxPage);
			resultMap.put("searchCondition", paramVO);

			paramVO.setStartIdx((paramVO.getPageSize()*(paramVO.getPageIdx()-1)));
			paramVO.setEndIdx(paramVO.getPageSize()*paramVO.getPageIdx());
			
			List userInfoList = userMgmtService.getUserInfoList(paramVO);
			
			model.addAttribute("resultMap", resultMap);
			model.addAttribute("userInfoList", userInfoList);
			model.addAttribute("paramVO", paramVO);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			throw e;
			
		}
		
		resultMap.put("retCode", "0000");
		resultMap.put("retMsg", "정상적으로 처리 되었습니다.");
		
		return "usermgmt/userList";
    }

	//--------------------------------------------------------------
	// 사용자상세 조회
	//--------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getUserInfo.do", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public Map getUserInfo(@RequestBody ParamVO paramVO, final HttpServletRequest request) throws Exception {
		
		Gson gson = new Gson();
		Map resultMap = new HashMap();
		
		logger.debug("xxxxxxxx getUserList start xxxxxxxxxx");
		
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			UserInfoVO userInfo = userMgmtService.getUserInfo(paramVO);
			
			//로그인 정보 세션 변수에 저장
			if(userInfo==null){
				
				resultMap.put("retCode", "0001");
				resultMap.put("retMsg", "조회된 결과가 없습니다.");
				
				return resultMap;
			} else {
				resultMap.put("userInfo", userInfo);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			resultMap.put("retCode", "9999");
			resultMap.put("retMsg", e.getMessage());
			
		}
		
		resultMap.put("retCode", "0000");
		resultMap.put("retMsg", "정상적으로 처리 되었습니다.");
		
		return resultMap;
    }
	
	//--------------------------------------------------------------
	// 사용자 관리 화면으로 이동
	//--------------------------------------------------------------
	@RequestMapping(value="/userInfoMgmt.do", method=RequestMethod.POST)
    public String userInfoMgmt(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request) throws Exception {
		
		logger.debug("xxxxxxxx userInfoMgmt start xxxxxxxxxx");
		model.addAttribute("paramVO", paramVO);
		
		return "usermgmt/myInfo";
    }
	
	//--------------------------------------------------------------
	// 사용자 현황 화면으로 이동
	//--------------------------------------------------------------
	@RequestMapping(value="/userList.do", method=RequestMethod.POST)
    public String goUserList(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request) throws Exception {

		logger.debug("xxxxxxxx userList start xxxxxxxxxx");
		
		Gson gson = new Gson();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		paramVO.setUseYn("A");
		paramVO.setPageIdx(1);
		paramVO.setPageSize(pgsize);
		
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			logger.debug("xxxxxxxx paramVO.getPageSize() : "+paramVO.getPageSize());
			
			int totCnt = userMgmtService.getUserInfoListCnt(paramVO);
			int maxPage = 0;
			if (totCnt > 0) {
				maxPage = totCnt / paramVO.getPageSize();
				if((totCnt % paramVO.getPageSize()) > 0){
					maxPage++;
				}				
			}
			
			resultMap.put("totCnt", totCnt);
//			resultMap.put("pageIdx", paramVO.getPageIdx());
//			resultMap.put("pageSize", paramVO.getPageSize());
			resultMap.put("maxPage", maxPage);
			resultMap.put("searchCondition", paramVO);
			resultMap.put("startIdx", paramVO.getStartIdx());

			paramVO.setStartIdx((paramVO.getPageSize()*(paramVO.getPageIdx()-1)));
			paramVO.setEndIdx(paramVO.getPageSize()*paramVO.getPageIdx());
			
			List userInfoList = userMgmtService.getUserInfoList(paramVO);
			
			model.addAttribute("resultMap", resultMap);
			model.addAttribute("userInfoList", userInfoList);
			model.addAttribute("paramVO", paramVO);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			throw e;
			
		}
		
		resultMap.put("retCode", "0000");
		resultMap.put("retMsg", "정상적으로 처리 되었습니다.");
		
		return "usermgmt/userList";
    }
	
	//--------------------------------------------------------------
	// 사용자 상세 화면으로 이동
	//--------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/userDetail.do", method=RequestMethod.POST)
    public String userDetail(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request) throws Exception {
		
		Gson gson = new Gson();
		Map resultMap = new HashMap();
		
		logger.debug("xxxxxxxx userDetail start xxxxxxxxxx");
		
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			UserInfoVO userInfoVO = userMgmtService.getUserInfo(paramVO);
			
			model.addAttribute("userInfoVO", userInfoVO);
			
			resultMap.put("retCode", "0000");
			resultMap.put("retMsg", "정상적으로 처리 되었습니다.");
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			throw e;
			
		}
		
		model.addAttribute("resultMap", resultMap);
		model.addAttribute("paramVO", paramVO);
		
		return "usermgmt/userDetail";
    }

	//--------------------------------------------------------------
	// 사용자 수정 화면으로 이동
	//--------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/userModify.do", method=RequestMethod.POST)
    public String userModify(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request) throws Exception {
		
		Gson gson = new Gson();
		Map resultMap = new HashMap();
		
		logger.debug("xxxxxxxx userModify start xxxxxxxxxx");
		
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			List<TbBelongInfoVO> belongInfoList = userMgmtService.getBelongInfoList();
			model.addAttribute("belongInfoList", belongInfoList);
			
			UserInfoVO userInfoVO = userMgmtService.getUserInfo(paramVO);
			model.addAttribute("userInfoVO", userInfoVO);
			
			resultMap.put("retCode", "0000");
			resultMap.put("retMsg", "정상적으로 처리 되었습니다.");
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			throw e;
			
		}
		
		model.addAttribute("resultMap", resultMap);
		model.addAttribute("paramVO", paramVO);
		
		return "usermgmt/userModify";
    }
	
	//--------------------------------------------------------------
	// 사용자 정보 삭제
	//--------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/removeUser.do", method=RequestMethod.POST)
    public String removeUser(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request) throws Exception {
		
		Gson gson = new Gson();
		Map resultMap = new HashMap();
		
		logger.debug("xxxxxxxx removeUser start xxxxxxxxxx");
		
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			userMgmtService.removeUserInfo(paramVO);
			
			resultMap.put("retCode", "0000");
			resultMap.put("retMsg", "정상적으로 처리 되었습니다.");
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			throw e;
			
		}
		
		model.addAttribute("resultMap", resultMap);
		model.addAttribute("paramVO", paramVO);
		
		return getUserInfoList(paramVO,model,request);
    }
	
	//--------------------------------------------------------------
	// 사용자 정보 수정
	//--------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/modifyUserInfo.do", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public Map modifyUserInfo(@RequestBody TbUserVO tbUserVO, 
    		final HttpServletRequest request) throws Exception {
		
		Gson gson = new Gson();
		Map resultMap = new HashMap();
		
		logger.debug("xxxxxxxx modifyUserInfo start xxxxxxxxxx");
		
		try {
			logger.debug(gson.toJson(tbUserVO, TbUserVO.class));
			
			userMgmtService.modifyUserInfo(tbUserVO);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			resultMap.put("retCode", "9999");
			resultMap.put("retMsg", e.getMessage());
			
		}
		
		resultMap.put("retCode", "0000");
		resultMap.put("retMsg", "정상적으로 처리 되었습니다.");
		
		return resultMap;
    }
	
	//--------------------------------------------------------------
	// 사용자 등록 화면으로 이동
	//--------------------------------------------------------------
	@RequestMapping(value="/userReg.do", method=RequestMethod.POST)
    public String userReg(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request) throws Exception {

		logger.debug("xxxxxxxx userReg start xxxxxxxxxx");
		
		try {
			List<TbBelongInfoVO> belongInfoList = userMgmtService.getBelongInfoList();
			model.addAttribute("belongInfoList", belongInfoList);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			throw e;
		}
		
		model.addAttribute("paramVO", paramVO);
		
		return "usermgmt/userReg";
    }
	
	//--------------------------------------------------------------
	// 사용자ID 중복체크
	//--------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/dupUserCheck.do", method=RequestMethod.POST)
	@ResponseBody
    public Map dupUserCheck(@RequestBody ParamVO paramVO, final Model model, 
    		final HttpServletRequest request) throws Exception {
		
		Gson gson = new Gson();
		Map resultMap = new HashMap();
		
		logger.debug("xxxxxxxx dupUserCheck start xxxxxxxxxx");
		
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			UserInfoVO userInfoVO = userMgmtService.getUserInfo(paramVO);
			
			if(userInfoVO!=null){
				resultMap.put("retCode", "0001");
				resultMap.put("retMsg", "이미 등록된 사용자ID 입니다.");
			} else {
				resultMap.put("retCode", "0000");
				resultMap.put("retMsg", "정상적으로 처리 되었습니다.");				
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			resultMap.put("retCode", "9999");
			resultMap.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
			
		}
		
		model.addAttribute("resultMap", resultMap);
		
		return resultMap;
    }
	
	//--------------------------------------------------------------
	// 사용자 정보 등록
	//--------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/addUserInfo.do", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public Map addUserInfo(@RequestBody TbUserVO tbUserVO, HttpServletRequest request) throws Exception {
		
		Gson gson = new Gson();
		Map resultMap = new HashMap();
		
		logger.debug("xxxxxxxx addUserInfo start xxxxxxxxxx");
		
		try {
			logger.debug(gson.toJson(tbUserVO, TbUserVO.class));
			
			//ID 중복여부 판단
			ParamVO paramVO = new ParamVO();
			paramVO.setUserId(tbUserVO.getUserId());
			UserInfoVO userInfoVO = userMgmtService.getUserInfo(paramVO);
			
			if(userInfoVO!=null){
				resultMap.put("retCode", "0001");
				resultMap.put("retMsg", "이미 등록된 사용자ID 입니다.");
				
				return resultMap;
			} 
			
			userMgmtService.addUserInfo(tbUserVO);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			resultMap.put("retCode", "9999");
			resultMap.put("retMsg", e.getMessage());
			
		}
		
		resultMap.put("retCode", "0000");
		resultMap.put("retMsg", "정상적으로 처리 되었습니다.");
		
		return resultMap;
    }
}
