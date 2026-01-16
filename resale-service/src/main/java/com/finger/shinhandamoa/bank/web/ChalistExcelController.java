package com.finger.shinhandamoa.bank.web;

import java.util.HashMap;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;

import com.finger.shinhandamoa.bank.dto.BankReg01DTO;
import com.finger.shinhandamoa.bank.service.BankMgmtService;

/**
 * @author by puki
 * @date   2018. 4. 10.
 * @desc   은행관리자
 * @version
 * 
 */
@Controller
@RolesAllowed({"ROLE_BANKADMIN"})
@RequestMapping("bank/web/**")
public class ChalistExcelController {
	
	private static final Logger logger = LoggerFactory.getLogger(ChalistExcelController.class);

	// 업로드 디렉토리
	@Value("${file.path.temp}")
	private String uploadPath;
	
	@Inject
	private BankMgmtService bankMgmtService;
	
	/*
	 * 은행관리자 > 기관목록 > 파일다운
	 */
	@ResponseBody
	@RequestMapping("excelSaveChalist")
	public View excelSaveChalist(BankReg01DTO dto, Model model) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("calDateFrom", 	dto.getCalDateFrom());			
			map.put("calDateTo", 	dto.getCalDateTo());
			map.put("agtCd", 		dto.getAgtCd());
			map.put("agtName", 		dto.getAgtName());
			map.put("chaCd", 		dto.getChaCd());
			map.put("chaName", 		dto.getChaName());
			map.put("adjaccyn", 	dto.getAdjaccyn());
			map.put("chast"   , 	dto.getChast());	
			map.put("searchGb", 	dto.getSearchGb());	
			map.put("searchOrderBy", dto.getSearchOrderBy());	
			
			// total count
			int count = bankMgmtService.getNewChaListTotalCount(map);
			map.put("start", 1);
			map.put("end", count);		
			
			List<BankReg01DTO> list = bankMgmtService.getNewChaListAll(map);
			model.addAttribute("list", list);
			model.addAttribute("flag", dto.getFlag());
		} catch(Exception e) {
			logger.error(e.getMessage());
		}

		return new ExcelSaveChalist();
	}
	
	/**
	 * 은행관리자 > 수수료조회(Ajax)
	 */
	@ResponseBody
	@RequestMapping("excelSaveFeeList")
	public View excelSaveFeeList(BankReg01DTO dto, Model model) throws Exception {	

		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("chaCd", dto.getChaCd());
			map.put("agtCd", dto.getAgtCd());
			map.put("startDate", dto.getStartDate());
			map.put("endDate", dto.getEndDate());
			map.put("searchOrderBy", dto.getSearchOrderBy());	
		
			BankReg01DTO cnt = bankMgmtService.getBankFeeListTotalCount(map);
			
			map.put("start", 1);
			map.put("end", cnt.getTotCnt());		
			List<BankReg01DTO> list = bankMgmtService.getBankFeeListAll(map);
			
			model.addAttribute("list", list);

			map.put("retCode", "0000");
			map.put("retMsg", "정상");
		
		}catch(Exception e) {
			logger.error(e.getMessage());
			
			map.put("retCode", "9999");
			map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
		}
		
		return new ExcelSaveChalist();
	}	
}
