package com.finger.shinhandamoa.bank.web;

import com.finger.shinhandamoa.bank.dto.BankReg01DTO;
import com.finger.shinhandamoa.bank.service.BankMgmtService;
import com.finger.shinhandamoa.common.SessionUtil;
import com.finger.shinhandamoa.data.table.mapper.ChaMapper;
import com.finger.shinhandamoa.data.table.model.Cha;
import com.finger.shinhandamoa.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.RolesAllowed;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  홍길동
 * @date    2018. 4. 6.
 * @desc    은행관리자
 * @version 
 * 
 */
@Controller
@RolesAllowed({"ROLE_BANKADMIN"})
@RequestMapping("bank")
public class BankMgmtController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BankMgmtController.class);

	@Autowired	
	private BankMgmtService bankMgmtService;

	@Autowired
	private ChaMapper chaMapper;

	/*
	 * 지점검색팝업 AJAX
	 */
	@SuppressWarnings("rawtypes")
	
	@ResponseBody
	@RolesAllowed({"ROLE_BANKADMIN"})
	@RequestMapping("getBranchListAjax")
	public HashMap getBranchListAjax(@RequestBody BankReg01DTO body) throws Exception {
		
		LOGGER.info("getBranchListAjax");
		
		HashMap<String, Object> reqMap = new HashMap<String, Object>();		
		int PAGE_SCALE = 5;
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		reqMap.put("agtCd"  , body.getAgtCd());			
		reqMap.put("agtName", body.getAgtName());		
		
		// total count
		int count = bankMgmtService.getBranchListTotalCount(reqMap);
		
		PageVO page = new PageVO(count, body.getCurPage(), PAGE_SCALE);
		int start  = page.getPageBegin();
		int end    = page.getPageEnd();

		reqMap.put("count", count);
		reqMap.put("start", start);
		reqMap.put("end", end);							
	
		List<BankReg01DTO> list = bankMgmtService.getBranchListAll(reqMap);
		
		map.put("count", count);			
		map.put("modalPager", page);
		map.put("modalNo", 66);			
		map.put("PAGE_SCALE", PAGE_SCALE);
		map.put("list", list);
		
		return map;		
	}
	
	/*
	 * 기관검색팝업 AJAX
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RolesAllowed({"ROLE_BANKADMIN"})
	@RequestMapping("getCollectorListAjax")
	public HashMap getCollectorListAjax(@RequestBody BankReg01DTO body) throws Exception {
		
		LOGGER.info("getCollectorListAjax");
		
		HashMap<String, Object> reqMap = new HashMap<String, Object>();
		int PAGE_SCALE = 5;
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		reqMap.put("chaCd"  , body.getChaCd());			
		reqMap.put("chaName", body.getChaName());	
		reqMap.put("chaGb",   body.getChaGb());
		
		// total count
		int count = bankMgmtService.getCollectorListTotalCount(reqMap);
		
		PageVO page = new PageVO(count, body.getCurPage(), PAGE_SCALE);
		int start = page.getPageBegin();
		int end = page.getPageEnd();

		reqMap.put("count", count);
		reqMap.put("start", start);
		reqMap.put("end", end);					
		
		List<BankReg01DTO> list = bankMgmtService.getCollectorListAll(reqMap);

		map.put("count", count);		
		map.put("modalPager", page);
		map.put("modalNo", 55);		
		map.put("list", list);
		
		return map;		
	}
	
	/**
	 * 은행관리 > 가입승인관리, 이용기관조회 > 조회
	 */
	@RolesAllowed({"ROLE_BANKADMIN"})
	@RequestMapping("getNewChaList")
	@ResponseBody
	public Map<String, Object> getNewChaList(@RequestBody BankReg01DTO body) throws Exception {
		LOGGER.info("getNewChaList : 은행관리 >> 가입승인관리, 이용기관조회 >> 조회");
		Map<String, Object> resultMap = new HashMap<>();
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("calDateFrom", 	body.getCalDateFrom());
			paramMap.put("calDateTo", 		body.getCalDateTo());
			paramMap.put("agtCd", 			body.getAgtCd());
			paramMap.put("agtName", 		body.getAgtName());
			paramMap.put("chaCd", 			body.getChaCd());
			paramMap.put("chaName", 		body.getChaName());
			paramMap.put("chast"   , 		body.getChast());
			paramMap.put("chatrty", 		body.getChatrty());
			paramMap.put("amtChkTy", 		body.getAmtChkTy());
			paramMap.put("chaCloseChk",     body.getChaCloseChk());
			paramMap.put("searchGb", 		body.getSearchGb());
			paramMap.put("searchOrderBy",	body.getSearchOrderBy());
			paramMap.put("accNoCnt", 		body.getAccNoCnt());

			// total count
			int count = bankMgmtService.getNewChaListTotalCount(paramMap);
			
			// 페이지 관련 설정
			PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
			int start = page.getPageBegin();
			int end = page.getPageEnd();
			paramMap.put("count", count);
			paramMap.put("start", start);
			paramMap.put("end", end);
            resultMap.put("count", count);

			List<BankReg01DTO> list = bankMgmtService.getNewChaListAll(paramMap);
			
			resultMap.put("list", list);
			resultMap.put("pager", page); 	// 페이징 처리를 위한 변수
			resultMap.put("retCode", "0000");
			resultMap.put("retMsg", "정상");
			
		}catch(Exception e) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.error(e.getMessage(), e);
			} else {
				LOGGER.error(e.getMessage());
			}
			
			resultMap.put("retCode", "9999");
			resultMap.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
		}
		
		return resultMap;
	}	
	
	/*
	 * 기관관리 > 기관조회
	 */
	@RolesAllowed({"ROLE_BANKADMIN"})
	@RequestMapping("chaList")
	public String chaList() throws Exception {

		LOGGER.info("chaList");
		
		if (!SessionUtil.hasRole("ROLE_BANKADMIN")) {
			return "common/denied";
		}
		
		return "bank/chaMgmt/chaList";
	}
	
	/**
	 * 기관관리 > 기관상세조회(Ajax)
	 * 
	 */
	@RolesAllowed({"ROLE_BANKADMIN"})
	@RequestMapping("selectChaListInfoAjax")
	@ResponseBody
	public HashMap<String, Object> selectChaListInfoAjax(@RequestBody BankReg01DTO body) throws Exception {

		LOGGER.info("selectChaListInfoAjax : 가입승인관리 >> 기관명 눌렀을때 화면 조회");
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("chaCd" , body.getChaCd());
			BankReg01DTO info = bankMgmtService.selectChaListInfo(map);
			List<HashMap<String, Object>> agencyList = bankMgmtService.getAgencyList(map);
			
			map.put("info", info);
			map.put("agencyList", agencyList);
			map.put("retCode", "0000");
			map.put("retMsg", "정상");
		} catch(Exception e) {
			LOGGER.error(e.getMessage());
			
			map.put("retCode", "9999");
			map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
		}
		
		return map;
	}

	/*updateChaInfo
	 * 기관관리 > 기관정보수정(단건)
	 */
	@ResponseBody
	@RolesAllowed({"ROLE_BANKADMIN"})
	@RequestMapping("updateChaInfo")
	public HashMap<String, Object> updateChaInfo(@RequestBody BankReg01DTO dto) throws Exception {

		LOGGER.info("updateChaInfo");

		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String user = authentication.getName();
			dto.setMaker(user);
			dto.setJobType("UB");

			bankMgmtService.updateXChaList(dto);

			map.put("retCode", "0000");
			map.put("retMsg", "정상");

		}catch(Exception e) {
			LOGGER.error(e.getMessage());

			map.put("retCode", "9999");
			map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
		}

		return map;
	}

	/*
	 * 은행관리 > 가입승인관리 > 승인
	 */
	@ResponseBody
	@RolesAllowed({"ROLE_BANKADMIN"})
	@RequestMapping("updateChaListAssign")
	public Map<String, Object> updateChaListAssign(@RequestBody BankReg01DTO body) throws Exception {
		LOGGER.info("updateChaListAssign : 가입승인관리 >> 승인");
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final String user = authentication.getName();

		Map<String, Object> resultMap = new HashMap<>();
		try {
			Cha essentialItem = chaMapper.selectByPrimaryKey(body.getChaCd());

            if(StringUtils.isBlank(essentialItem.getAmtchkty())){
				resultMap.put("retCode", "99999");
				resultMap.put("itemMsg", "이용방식");
			}else if(StringUtils.isBlank(String.valueOf(essentialItem.getAccnocnt()))){
				resultMap.put("retCode", "99999");
				resultMap.put("itemMsg", "발급요청좌수");
			}else if(StringUtils.isBlank(essentialItem.getOwner())){
				resultMap.put("retCode", "99999");
				resultMap.put("itemMsg", "대표자명");
			}else if(StringUtils.isBlank(essentialItem.getOwnertel())){
				resultMap.put("retCode", "99999");
				resultMap.put("itemMsg", "대표 전화번호");
			}else if(StringUtils.isBlank(essentialItem.getChrtelno())){
				resultMap.put("retCode", "99999");
				resultMap.put("itemMsg", "담당자 전화번호");
			}else if(StringUtils.isBlank(essentialItem.getChrhp())){
				resultMap.put("retCode", "99999");
				resultMap.put("itemMsg", "담당자 핸드폰번호");
			}else if(StringUtils.isBlank(essentialItem.getAgtcd())){
				resultMap.put("retCode", "88888");
				resultMap.put("itemMsg", "지점코드");
			}else{
            	final HashMap<String, Object> paramMap = new HashMap<>();
				paramMap.put("chaCd", body.getChaCd());
				paramMap.put("maker", user);
				paramMap.put("chaSt", "ST01");
                bankMgmtService.updateChaSt(paramMap);

                resultMap.put("retCode", "0000");
                resultMap.put("retMsg", "정상");
            }
		}catch(Exception e) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.error(e.getMessage(), e);
			} else {
				LOGGER.error(e.getMessage());
			}

			resultMap.put("retCode", "9999");
			resultMap.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
		}

		return resultMap;
	}

	/*
	 * 은행관리  > 가입승인관리 > 승인거부
	 */
	@ResponseBody
	@RolesAllowed({"ROLE_BANKADMIN"})
	@RequestMapping("updateChaListResign")
	public Map<String, Object> updateChaListResign(@RequestBody BankReg01DTO body) throws Exception {
		LOGGER.info("updateChaListResign : 가입승인관리 >> 승인거부");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String user = authentication.getName();

		final Map<String, Object> map = new HashMap<>();
		try {
		
			final HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("chaCd", body.getChaCd());
			paramMap.put("maker", user);
			paramMap.put("chaSt", "ST07");

			bankMgmtService.updateChaSt(paramMap);

			map.put("retCode", "0000");
			map.put("retMsg", "정상");
		}catch(Exception e) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.error(e.getMessage(), e);
			} else {
				LOGGER.error(e.getMessage());
			}
			
			map.put("retCode", "9999");
			map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
		}

		return map;		
	}

	/* updateChaListInfoAjax
	 * 가입승인관리 >> 기관정보수정
	 */
	@ResponseBody
	@RolesAllowed({"ROLE_BANKADMIN"})
	@RequestMapping("updateChaListInfoAjax")
	public Map<String, Object> updateChaListInfoAjax(@RequestBody BankReg01DTO dto) throws Exception {

		LOGGER.info("updateChaListInfoAjax : 가입승인관리 >> 기관명 눌렀을때 화면 수정");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String user = authentication.getName();

		final Map<String, Object> map = new HashMap<>();
		try {
			final HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("user", user);
			paramMap.put("chaCd", dto.getChaCd());
			paramMap.put("amtChkTy", dto.getAmtChkTy());
			paramMap.put("accNoCnt", dto.getAccNoCnt());
			paramMap.put("owner", dto.getOwner());
			paramMap.put("ownerTel", dto.getOwnerTel());
			paramMap.put("chrTelNo", dto.getChrTelNo());
			paramMap.put("chrHp", dto.getChrHp());
			paramMap.put("remark", dto.getRemark());
			paramMap.put("rcpCntFee", dto.getRcpCntFee());
			paramMap.put("rcpBnkFee", dto.getRcpBnkFee());

			bankMgmtService.updateChaListInfo(paramMap);

			map.put("retCode", "0000");
			map.put("retMsg", "정상");
		}catch(Exception e) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.error(e.getMessage(), e);
			} else {
				LOGGER.error(e.getMessage());
			}

			map.put("retCode", "9999");
			map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
		}

		return map;
	}

	/*
	 * 기관관리 > 수수료조회
	 */
	@RolesAllowed({"ROLE_BANKADMIN"})
	@RequestMapping("feeList")
	public String feeList() throws Exception {

		LOGGER.info("feeList");
		
		if (!SessionUtil.hasRole("ROLE_BANKADMIN")) {
			return "common/denied";
		}
		
		return "bank/fee/feeList";
	}
	
	/**
	 * 기관관리 > 수수료조회
	 */
	@RolesAllowed({"ROLE_BANKADMIN"})
	@RequestMapping("getBankFeeList")
	@ResponseBody
	public HashMap<String, Object> getBankFeeList(@RequestBody BankReg01DTO body) throws Exception {
		
		LOGGER.info("getBankFeeList");
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("chaCd", body.getChaCd());
			map.put("agtCd", body.getAgtCd());
			map.put("startDate", body.getStartDate());
			map.put("endDate", body.getEndDate());
			map.put("searchOrderBy", body.getSearchOrderBy());
			map.put("chaName", body.getChaName());
			map.put("agtName", body.getAgtName());
							
			// total count
			BankReg01DTO dto = bankMgmtService.getBankFeeListTotalCount(map);
			int count = dto.getTotCnt();
			long totNoticnt = dto.getTotNotiCnt();
			long totNotifee = dto.getTotNotiFee();
			long totRcpcnt  = dto.getTotRcpCnt();
			long totRcpfee  = dto.getTotRcpFee();
			long totFeeSum  = dto.getTotFeeSum();
			
			// 페이지 관련 설정
			PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
			int start = page.getPageBegin();
			int end = page.getPageEnd();
			map.put("count", count);
			map.put("start", start);
			map.put("end", end);	
			
			List<BankReg01DTO> list = bankMgmtService.getBankFeeListAll(map);
			map.put("list", list);			
			map.put("count", count);
			map.put("pager", page); 	// 페이징 처리를 위한 변수
			
			map.put("totNotiCnt", totNoticnt);
			map.put("totNotiFee", totNotifee);
			map.put("totRcpCnt" , totRcpcnt );
			map.put("totRcpFee" , totRcpfee );
			map.put("totFeeSum" , totFeeSum );

			map.put("retCode", "0000");
			map.put("retMsg", "정상");
		
		}catch(Exception e) {
			LOGGER.error(e.getMessage());
			
			map.put("retCode", "9999");
			map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
		}
		
		return map;
	}
}
