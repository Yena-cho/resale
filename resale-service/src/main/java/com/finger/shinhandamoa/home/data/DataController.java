package com.finger.shinhandamoa.home.data;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finger.shinhandamoa.common.ShaEncoder;
import com.finger.shinhandamoa.home.dto.LoginDTO;
import com.finger.shinhandamoa.main.dto.NoticeDTO;

@Controller
public class DataController {

	private  static final Logger logger = LoggerFactory.getLogger(DataController.class);
	
	@Autowired
	private DataService dataService;
	
	@Inject
	private ShaEncoder shaEncoder;
	
	/*
	 * WEBUSER > DB이관 후 비밀번호 초기화 및 암호화 수행
	 */
	@RequestMapping("common/orgPasswordUpdate")
	private HashMap<String, Object> orgPasswordUpdate() throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		logger.info("======###=======");
		
		dataService.orgPasswordUpdate(map);
		
		return map;
	}
	
	
	/*
	 * XCUSMAS > DB이관 후 납부자 로그인을 위해 사용되는 가상계좌 비밀번호 초기화 및 암호화 수행
	 */
	@RequestMapping("common/orgCusPassUpdate")
	private HashMap<String, Object> orgCusPassUpdate() throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		logger.info("======###=======");
		
		dataService.orgCusPassUpdate(map);
		
		return map;
	}
	
	
	/*
	 * 테스트 데이터 생성 - 아이디, password 동일하게 수정 - webuser
	 * */
	@ResponseBody
	@RequestMapping("common/login/cusInfoShaEncoder")
	public HashMap<String, Object> cusInfoShaEncoder(@RequestBody LoginDTO dto) throws  Exception {
		
		HashMap<String, Object> map  = new HashMap<String, Object>();
		
		map.put("loginId", "29999999");
		map.put("loginPw", shaEncoder.encoding("29999999"));
		
		dataService.cusInfoShaEncoder(map);
		
		return map;
	}
	
	/*
	 * XCUSMAS > 비밀번호 암호화
	 * */
	@ResponseBody
	@RequestMapping("common/payerPasswordUpdate")
	public HashMap<String, Object> payerPasswordUpdate() throws  Exception {
		
		HashMap<String, Object> map  = new HashMap<String, Object>();
		
		logger.info("======###=======");
		
		dataService.payerPasswordUpdate(map);
		
		return map;
	}
	
	
	@RequestMapping("common/cryptTest")
	public HashMap<String, Object> cryptTest() throws Exception {
		HashMap<String, Object> map  = new HashMap<String, Object>();
		
		map.put("email", "022345678");
		dataService.updateCrypt(map);
		
//		List<NoticeDTO> list = dataService.list(map);
//		logger.info("=============>>>>" + list.get(0).getData1());
//		logger.info("=============>>>>" + list.get(0).getEmail());
		
//		dataService.insertCrypt(map);
		
		
		return map;
	}
	
}
