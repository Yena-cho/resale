package com.ghvirtualaccount.main.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ghvirtualaccount.main.service.MainService;
import com.ghvirtualaccount.vo.MenuInfoVO;
import com.ghvirtualaccount.vo.ParamVO;
import com.ghvirtualaccount.vo.UserInfoVO;
import com.google.gson.Gson;

@SuppressWarnings("rawtypes")
@Controller
public class MainController {

	private static Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Resource(name="mainService")
	private MainService mainService;
	
	//--------------------------------------------------------------
	// 로그인
	//--------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/login.do", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public Map login(@RequestBody ParamVO paramVO, HttpServletRequest request) throws Exception {
		
		Gson gson = new Gson();
		Map resultMap = new HashMap();
		
		logger.debug("xxxxxxxx login start xxxxxxxxxx");
		
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			resultMap = mainService.login(paramVO);
			
			//로그인 정보 세션 변수에 저장
			if(resultMap.get("userInfoVO")==null){
				
				resultMap.put("retCode", "0001");
				resultMap.put("retMsg", "ID 또는 패스워드가 정확하지 않습니다.");
				
				return resultMap;
			} else {
				if(((UserInfoVO)resultMap.get("userInfoVO")).getUseYn().equals("N")){
					resultMap.put("retCode", "0002");
					resultMap.put("retMsg", "이용이 중지된 사용자 입니다. 관리자에게 문의하시기 바랍니다.");
					
					return resultMap;
				} else {
					HttpSession session = request.getSession();
					session.setAttribute("userInfo", (UserInfoVO)resultMap.get("userInfoVO"));
					session.setAttribute("menuInfo", (List<MenuInfoVO>)resultMap.get("menuInfo"));	
				}

			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			resultMap.put("retCode", "9999");
			resultMap.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
			
		}
		
		resultMap.put("retCode", "0000");
		resultMap.put("retMsg", "정상");
		
		return resultMap;
    }
	
	//--------------------------------------------------------------
	// 메인 화면으로 이동
	//--------------------------------------------------------------
	@RequestMapping(value="/main.do")
    public String goMain(final Model model, final HttpServletRequest request) throws Exception {
		
		return "main/index";
    }

	//--------------------------------------------------------------
	// 로그 아웃
	//--------------------------------------------------------------
	@RequestMapping(value="/logout.do")
    public String logout(final Model model, final HttpServletRequest request) throws Exception {
		
		HttpSession session = request.getSession();
		session.invalidate(); 
		
		return "main/index";
    }
	
}
