package com.finger.shinhandamoa.sys.setting.web;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.finger.shinhandamoa.common.ShaEncoder;
import com.finger.shinhandamoa.sys.setting.dto.AdminInfoDTO;
import com.finger.shinhandamoa.sys.setting.service.SettingService;
import com.finger.shinhandamoa.vo.PageVO;

/**
 * @author  by puki
 * @date    2018. 5. 21.
 * @desc    최초생성
 * @version 
 * 
 */
@RequestMapping("sys/setting/**")
@Controller
public class SettingController {

	private static final Logger logger = LoggerFactory.getLogger(SettingController.class);
	
	@Inject
	private SettingService settingService;
	
	@Inject
	private ShaEncoder shaEncoder;
	
	@RequestMapping("appAuth")
	public ModelAndView selAppAuthList() throws Exception {
		
		ModelAndView mav = new ModelAndView();

		mav.setViewName("sys/setting/accAuth/accAuthList");
		
		return mav;
	}
	
	/*
	 * 운영관리자 > 설정관리 > 계정/권한관리 > 조회
	 */
	@ResponseBody
	@RequestMapping("selAccAuth")
	public HashMap<String, Object> selAccAuthAjaxList(@RequestBody AdminInfoDTO dto) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> smap = new HashMap<String, Object>();
		smap.put("admGroup", dto.getAdmGroup());
		
		try {
			int count = settingService.selAccAuthCnt(smap);
			map.put("count", count);
			
			PageVO page = new PageVO(count, dto.getCurPage(), 10);
			int start = page.getPageBegin();
			int end = page.getPageEnd();	
			smap.put("start", start);
			smap.put("end", end);
			
			List<AdminInfoDTO> list = settingService.selAccAuthList(smap);
			map.put("list", list);
			map.put("pager", page);
			if(dto.getAdmGroup().equals("BA")) {
				map.put("modalNo", 2);
			} else {
				map.put("modalNo", 1);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/*
	 * 운영관리자 > 설정관리 > 계정/권한관리 > 상세조회
	 */
	@ResponseBody
	@RequestMapping("accDetail")
	public HashMap<String, Object> accAuthDetail(@RequestBody AdminInfoDTO dto) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("map", settingService.selAccAuthDetail(dto.getAdmId()));
		
		return map;
	}
	
	/*
	 * 운영관리자 > 설정관리 > 계정/권한관리 > 수정 및 등록
	 */
	@ResponseBody
	@RequestMapping("accModify")
	public void accAuthModify(@RequestBody AdminInfoDTO dto) throws Exception {
		
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("admId", dto.getAdmId());
			map.put("admPw", shaEncoder.encoding(dto.getAdmPw()));
			map.put("admName", dto.getAdmName());
			map.put("admHp", dto.getAdmHp());
			map.put("admEmail", dto.getAdmEmail());
			map.put("admRank", dto.getAdmRank());
			map.put("admDept", dto.getAdmDept());
			map.put("admStatus", dto.getAdmStatus());
			map.put("admGroup", dto.getAdmGroup());
			map.put("stat", dto.getStat());
			map.put("maker", dto.getMaker());
		
			settingService.accAuthModify(map);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 운영관리자 > 설정관리 > 계정/권한관리 > 삭제
	 */
	@ResponseBody
	@RequestMapping("accDelete")
	public void accAuthDelete(@RequestBody AdminInfoDTO dto) throws Exception {
		try {
			settingService.accAuthDelete(dto.getAdmId());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 운영관리자 > 설정관리 > 계정/권한관리 > 아이디중복확인
	 */
	@ResponseBody
	@RequestMapping("selAdmId")
	public HashMap<String, Object> selAdmId(@RequestBody AdminInfoDTO dto) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			int cnt = settingService.selAdmId(dto.getAdmId());
			map.put("map", cnt);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	
}
