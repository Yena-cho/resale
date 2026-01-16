package com.finger.shinhandamoa.sys.setting.web;

import java.io.*;
import java.util.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finger.shinhandamoa.org.custMgmt.dto.CustReg01DTO;
import com.finger.shinhandamoa.sys.cust.dto.SysCustDTO;
import com.finger.shinhandamoa.sys.setting.dao.VanoMgmtDAO;
import com.finger.shinhandamoa.sys.setting.dto.TransInfoDTO;
import com.finger.shinhandamoa.sys.setting.dto.VanoMgmt2DTO;
import com.finger.shinhandamoa.sys.setting.dto.VanoPgDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtBaseDTO;
import com.finger.shinhandamoa.org.notimgmt.service.NotiMgmtService;
import com.finger.shinhandamoa.sys.setting.dto.VanoMgmtDTO;
import com.finger.shinhandamoa.sys.setting.service.VanoMgmtService;
import com.finger.shinhandamoa.vo.PageVO;

/**
 * @author  홍길동
 * @date    2018. 4. 6.
 * @desc    은행관리자
 * @version 
 * 
 */
@Controller
@RequestMapping("/sys/vanoMgmt")
public class VanoMgmtController {

	private static final Logger logger = LoggerFactory.getLogger(VanoMgmtController.class);

	@Inject
	private NotiMgmtService notiMgmtService;

	@Inject
	private VanoMgmtDAO vanoMgmtDAO;
	@Inject
	private VanoMgmtService vanoMgmtService;

	@Value("${pg.sendvano.url}")
	private String pgSendVanoUrl;



	/**
	 * 가상계좌 관리 > 가상계좌 이용현황
	 */
	@RequestMapping("vanoOwnsList")
	public ModelAndView vanoOwnsList() throws Exception {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("sys/setting/vanoMgmt/vanoOwnsList");

		return mav;
	}

	/**
	 * 가상계좌 관리 > 기관별 가상계좌 보유현황
	 */
	@RequestMapping("chaVanoOwnsList")
	public ModelAndView chaVanoOwnsList(@RequestParam(defaultValue = "regdt") String searchOrderBy,
										@RequestParam(defaultValue = "1") int curPage,
										@RequestParam(defaultValue = "10") int pageScale) throws Exception {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("sys/setting/vanoMgmt/chaVanoOwnsList");

		return mav;
	}


	/**
	 * 가상계좌 관리 > 가상계좌 거래내역
	 */
	@RequestMapping("vanoTranHisList")
	public ModelAndView vanoTranHisList(@RequestParam(defaultValue = "regdt") String searchOrderBy,
										@RequestParam(defaultValue = "1") int curPage,
										@RequestParam(defaultValue = "10") int pageScale) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("sys/setting/vanoMgmt/vanoTranHisList");

		return mav;
	}


	/**
	 * 가상계좌 관리 > 가상계좌 발급관리
	 */
	@RequestMapping("vanoIssuedList")
	public ModelAndView vanoIssuedList(@RequestParam(defaultValue = "regdt") String searchOrderBy,
									   @RequestParam(defaultValue = "1") int curPage,
									   @RequestParam(defaultValue = "10") int pageScale) throws Exception {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String user = authentication.getName();

		NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(user);

		ModelAndView mav = new ModelAndView();
		mav.addObject("orgSess", baseInfo);
		mav.setViewName("sys/setting/vanoMgmt/vanoIssuedList");

		return mav;
	}

	/**
	 * 기관관리 > 기관등록조회(Ajax)
	 */

	@RequestMapping("getvanoOwnsList")
	@ResponseBody
	public HashMap<String, Object> getvanoOwnsList(@RequestBody VanoMgmtDTO body) throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>();

		try {

			HashMap<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("searchGb"      , body.getSearchGb());
			reqMap.put("searchValue"   , body.getSearchValue());
			reqMap.put("chaCd"         , body.getChacd());
			reqMap.put("stList"        , body.getStList());
			reqMap.put("fromNotUseCnt" , body.getFromNotUseCnt());
			reqMap.put("toNotUseCnt"   , body.getToNotUseCnt());
			reqMap.put("searchOrderBy" , body.getSearchOrderBy());

			logger.info("----- searchOrderBy= {}" , body.getSearchOrderBy());

			VanoMgmtDTO dto = vanoMgmtService.getSysMainInfo01(reqMap);
			// total count
			int count = vanoMgmtService.getVano01ListTotalCount(reqMap);

			// 페이지 관련 설정
			PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
			int start = page.getPageBegin();
			int end = page.getPageEnd();
			reqMap.put("count", count);
			reqMap.put("start", start);
			reqMap.put("end", end);

			List<VanoMgmtDTO> list =  vanoMgmtService.getVano01ListAll(reqMap);

			map.put("info", dto);
			map.put("list", list);
			map.put("count", count);
			map.put("pager", page);     // 페이징 처리를 위한 변수
			map.put("PAGE_SCALE", body.getPageScale());
			map.put("retCode", "0000");
			map.put("retMsg", "정상");

		}catch(Exception e) {
			logger.error(e.getMessage());

			map.put("retCode", "9999");
			map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
		}
		return map;
	}

	/**
	 * 가상계좌관리 > 가상계좌 발급요청관리 - 엑셀다운로드
	 */
	@RequestMapping("getVanoIssuedListExcel")
	public View getVanoIssuedListExcel(HttpServletRequest request, HttpServletResponse response, VanoMgmtDTO dto, Model model) throws Exception {

		try {

			HashMap<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("chacd"         , dto.getChacd());
			reqMap.put("chaname"       , dto.getChaname());
			reqMap.put("stList"        , dto.getStList());
			reqMap.put("searchOrderBy" , dto.getSearchOrderBy());

			logger.info("----- searchOrderBy= {}" , dto.getSearchOrderBy());

			List<VanoMgmtDTO> list = vanoMgmtService.getVanoIssuedListExcel(reqMap);

			model.addAttribute("list", list);
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
		return new ExcelSaveVanoIssue();
	}

	/**
	 * 가상계좌관리 > 보유현황 - 엑셀다운로드
	 */
	@RequestMapping("getVano01ListExcel")
	public View getVano01ListExcel(HttpServletRequest request, HttpServletResponse response, VanoMgmtDTO dto, Model model) throws Exception {

		try {

			HashMap<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("searchGb"      , dto.getSearchGb());
			reqMap.put("searchValue"   , dto.getSearchValue());
			reqMap.put("chaCd"         , dto.getChaCd());
			reqMap.put("stList"        , dto.getStList());
			reqMap.put("chatrtyList"   , dto.getChatrtyList());
			reqMap.put("fromNotUseCnt" , dto.getFromNotUseCnt());
			reqMap.put("toNotUseCnt"   , dto.getToNotUseCnt());
			reqMap.put("searchOrderBy" , dto.getSearchOrderBy());

			logger.info("----- searchOrderBy= {}" , dto.getSearchOrderBy());

			List<VanoMgmtDTO> list = vanoMgmtService.getVano01ListExcel(reqMap);

			model.addAttribute("list", list);
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
		return new ExcelSaveVanoOwns();
	}

	/**
	 * 가상계좌관리 > 기관별 가상계좌 보유현황 조회(Ajax)
	 */
	@RequestMapping("getChaVanoOwnsList")
	@ResponseBody
	public HashMap<String, Object> getChaVanoOwnsList(@RequestBody VanoMgmtDTO body) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();

		try {
			HashMap<String, Object> reqMap = new HashMap<String, Object>();

			reqMap.put("startday"      , body.getSearchStartday());        //가상계계좌 발급일
			reqMap.put("endday"        , body.getSearchEndday());
			reqMap.put("chacd"         , body.getChacd());
			reqMap.put("chaname"       , body.getChaname());
			reqMap.put("searchGb"      , body.getSearchGb());
			reqMap.put("searchValue"   , body.getSearchValue());
			reqMap.put("stList"        , body.getStList());
			reqMap.put("searchOrderBy" , body.getSearchOrderBy());

			//count
			List<VanoMgmt2DTO> countList = vanoMgmtService.getChaVanoListTotalCount(reqMap);
			int totalCount = Integer.parseInt(countList.get(0).getTotalcnt());
			int misassignCount = Integer.parseInt(countList.get(0).getMiscnt());

			logger.debug("----- ChaVanoListTotalCount = {}", totalCount);

			// 페이지 관련 설정
			PageVO page = new PageVO(totalCount, body.getCurPage(), body.getPageScale());
			int start = page.getPageBegin();
			int end = page.getPageEnd();
			reqMap.put("totalCount", totalCount);
			reqMap.put("misassignCount", misassignCount);
			reqMap.put("start", start);
			reqMap.put("end", end);

			List<VanoMgmt2DTO> list = vanoMgmtService.getChaVanoListAll(reqMap);

			map.putAll(reqMap);
			map.put("list", list);

			map.put("totalCount", totalCount);
			map.put("misassignCount", misassignCount);
			map.put("pager", page);     // 페이징 처리를 위한 변수
			map.put("retCode", "0000");
			map.put("retMsg", "정상");

		}catch(Exception e) {
			logger.error(e.getMessage());

			map.put("retCode", "9999");
			map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
		}

		return map;
	}

	/**
	 * 가상계좌관리 > 기관별 가상계좌 보유현황 > 엑셀다운로드
	 */
	@RequestMapping("getChaVanoListExcel")
	public View getChaVanoListExcel(HttpServletRequest request, HttpServletResponse response, VanoMgmtDTO dto, Model model) throws Exception {

		try {
			HashMap<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("startday"      , dto.getSearchStartday());
			reqMap.put("endday"        , dto.getSearchEndday());
			reqMap.put("searchGb"      , dto.getSearchGb());
			reqMap.put("searchValue"   , dto.getSearchValue());
			reqMap.put("chacd"         , dto.getChaCd());
			reqMap.put("chaname"       , dto.getChaName());
			reqMap.put("stList"        , dto.getStList());
			reqMap.put("searchOrderBy" , dto.getSearchOrderBy());

			List<VanoMgmt2DTO> list = vanoMgmtService.getChaVanoListExcel(reqMap);

			model.addAttribute("list", list);
			model.addAttribute("excelName", "기관별가상계좌보유현황");
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
		return new ExcelSaveChaVanoOwns();
	}

	/**
	 * 가상계좌관리 > 가상계좌 거래내역조회(Ajax)
	 */
	@RequestMapping("getVanoTranHisList")
	@ResponseBody
	public HashMap<String, Object> getVanoTranHisList(@RequestBody VanoMgmtDTO body) throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>();
		try {

			HashMap<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("chacd"         , body.getChacd());
			reqMap.put("chaname"       , body.getChaname());
			reqMap.put("searchGb"      , body.getSearchGb());
			reqMap.put("searchValue"   , body.getSearchValue());
			reqMap.put("paydayFrom"    , body.getPaydayFrom());
			reqMap.put("paydayTo"      , body.getPaydayTo());
			reqMap.put("paytimeFrom"   , body.getPaytimeFrom());
			reqMap.put("paytimeTo"     , body.getPaytimeTo());
			reqMap.put("searchOrderBy" , body.getSearchOrderBy());

			// total count
			int count = vanoMgmtService.getVanoTranHisListTotalCount(reqMap);
			// 페이지 관련 설정
			PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
			int start = page.getPageBegin();
			int end = page.getPageEnd();
			reqMap.put("count", count);
			reqMap.put("start", start);
			reqMap.put("end", end);

			List<VanoMgmtDTO> list =  vanoMgmtService.getVanoTranHisListAll(reqMap);

			map.putAll(reqMap);
			map.put("list", list);
			map.put("count", count);
			map.put("pager", page);     // 페이징 처리를 위한 변수
			map.put("retCode", "0000");
			map.put("retMsg", "정상");

		}catch(Exception e) {
			e.printStackTrace();

			map.put("retCode", "9999");
			map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
		}

		return map;
	}

	/**
	 * 가상계좌관리 > 가상계좌 거래내역 엑셀다운로드
	 */
	@ResponseBody
	@RequestMapping("getVanoTranHisListExcel")
	public View getVanoTranHisListExcel(HttpServletRequest request, HttpServletResponse response, VanoMgmtDTO dto, Model model) throws Exception {

		try {
			HashMap<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("chacd"         , dto.getChacd());
			reqMap.put("chaname"       , dto.getChaname());
			reqMap.put("searchGb"      , dto.getSearchGb());
			reqMap.put("searchValue"   , dto.getSearchValue());
			reqMap.put("paydayFrom"    , dto.getPaydayFrom());
			reqMap.put("paydayTo"      , dto.getPaydayTo());
			reqMap.put("paytimeFrom"   , dto.getPaytimeFrom());
			reqMap.put("paytimeTo"     , dto.getPaytimeTo());
			reqMap.put("searchOrderBy" , dto.getSearchOrderBy());

			List<VanoMgmtDTO> list = vanoMgmtService.getVanoTranHisListExcel(reqMap);

			model.addAttribute("list", list);
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
		return new ExcelSaveVanoTranHis();
	}

	/**
	 * 가상계좌관리 > 가상계좌 발급요청관리(Ajax)
	 */
	@RequestMapping("getVanoIssuedList")
	@ResponseBody
	public HashMap<String, Object> getVanoIssuedList(@RequestBody VanoMgmtDTO body) throws Exception {//, HttpServletRequest request, HttpServletResponse response

		HashMap<String, Object> map = new HashMap<String, Object>();
		try {

			HashMap<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("chacd"         , body.getChacd());
			reqMap.put("chaname"       , body.getChaname());
			reqMap.put("stList"        , body.getStList());
			reqMap.put("searchOrderBy" , body.getSearchOrderBy());

			// total count
			int count = vanoMgmtService.getVanoIssuedListTotalCount(reqMap);

			// 페이지 관련 설정
			PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
			int start = page.getPageBegin();
			int end = page.getPageEnd();
			reqMap.put("count", count);
			reqMap.put("start", start);
			reqMap.put("end", end);

			List<VanoMgmtDTO> valist = vanoMgmtService.getMisassignVanoCount();
			List<VanoMgmtDTO> list = vanoMgmtService.getVanoIssuedListAll(reqMap);

			map.putAll(reqMap);
			map.put("valist", valist);
			map.put("list", list);
			map.put("count", count);
			map.put("pager", page);     // 페이징 처리를 위한 변수
			map.put("retCode", "0000");
			map.put("retMsg", "정상");

		}catch(Exception e) {
			logger.error(e.getMessage());

			map.put("retCode", "9999");
			map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
		}

		return map;
	}

	/**
	 * 가상계좌관리 > 가상계좌 발급요청관리 > 발급요청
	 */
	@ResponseBody
	@RequestMapping("doVanoIssueReq")
	public HashMap<String, Object> doVanoIssueReq(@RequestBody VanoMgmtDTO body) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();

		try {
			map.put("fgcd", body.getFgcd());
			map.put("chacd", body.getChacd());
			map.put("acccnt", body.getAcccnt());
			map.put("remark", body.getRemark());
			map.put("chatrty", body.getChatrty());

			int cnt= vanoMgmtDAO.doVanoIssueReqCheck(map);

			if(cnt > 0 ){
				map.put("retCode", "9999"); //진행중인 고속도로 전송 대기중인 가상계좌 추가 채번 건이 존재합니다.

				return map;

			}

			vanoMgmtService.doVanoIssueReq(map);

			map.put("retCode", "0000");
		} catch(Exception e) {
			map.put("retCode", "0001");
			map.put("retMsg", "0001");
		}

		return map;
	}

	/**
	 * 가상계좌 관리 > 가상계좌거래전문내역
	 */
	@RequestMapping("vanoTranCheckList")
	public ModelAndView vanoTranCheckList() throws Exception {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("sys/setting/vanoMgmt/vanoTranCheckList");

		return mav;
	}

    /**
     * 가상계좌 관리 > 가상계좌거래전문내역
     */
    @RequestMapping("vanoTranInitCheck")
    public ModelAndView vanoTranInitCheck() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("sys/setting/vanoMgmt/vanoTranInitCheck");

        return mav;
    }

	/**
	 * 가상계좌관리 > 가상계좌 입금거래내역조회(Ajax)
	 */
	@RequestMapping("getVanoTranCheckList")
	@ResponseBody
	public HashMap<String, Object> getVanoTranCheckList(@RequestBody TransInfoDTO body) throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>();
		try {

			HashMap<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("chacd", body.getChacd());
			reqMap.put("vano", body.getVano());
			reqMap.put("startday", body.getStartday());
			reqMap.put("endday", body.getEndday());
			reqMap.put("succSt", body.getSuccSt());
			reqMap.put("sndrcvList", body.getSndrcvList());
			reqMap.put("tylist", body.getTyList());
			reqMap.put("searchOrderBy", body.getSearchOrderBy());

			// total count
			int count = vanoMgmtService.getVanoTranChkListCnt(reqMap);

			// 페이지 관련 설정
			PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
			int start = page.getPageBegin();
			int end = page.getPageEnd();
			reqMap.put("count", count);
			reqMap.put("start", start);
			reqMap.put("end", end);

			List<TransInfoDTO> list =  vanoMgmtService.getVanoTranChkList(reqMap);

			map.putAll(reqMap);
			map.put("list", list);
			map.put("count", count);
			map.put("pager", page);
			map.put("retCode", "0000");
			map.put("retMsg", "정상");

		}catch(Exception e) {
			e.printStackTrace();

			map.put("retCode", "9999");
			map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
		}

		return map;
	}

    /**
     * 가상계좌관리 > 가상계좌 입금거래내역조회(Ajax)
     */
    @RequestMapping("getVanoTranInitCheck")
    @ResponseBody
    public HashMap<String, Object> getVanoTranInitCheck(@RequestBody TransInfoDTO body) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {

            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("chacd", body.getChacd());
            reqMap.put("startday", body.getStartday());
            reqMap.put("endday", body.getEndday());
            reqMap.put("succSt", body.getSuccSt());
            reqMap.put("sndrcvSt", body.getSndrcvSt());
            reqMap.put("tylist", body.getTyList());
            reqMap.put("searchOrderBy", body.getSearchOrderBy());

            // total count
            int count = vanoMgmtService.getVanoTranInitChkCnt(reqMap);

            // 페이지 관련 설정
            PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            reqMap.put("count", count);
            reqMap.put("start", start);
            reqMap.put("end", end);

            List<TransInfoDTO> list =  vanoMgmtService.getVanoTranInitChk(reqMap);

            map.putAll(reqMap);
            map.put("list", list);
            map.put("count", count);
            map.put("pager", page);
            map.put("retCode", "0000");
            map.put("retMsg", "정상");

        }catch(Exception e) {
            e.printStackTrace();

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

	/**
	 * 가상계좌관리 > 기관별 가상계좌 보유현황 > 가상계좌 등록 - 엑셀등록(excel upload)
	 */
	@Transactional
	@SuppressWarnings("resource")
	@ResponseBody
	@RequestMapping("uploadExcelVirtualAccount")
	public HashMap<String, Object> excelUpload(MultipartHttpServletRequest request, boolean flag) throws Exception {
		HashMap<String, Object> retMap = new HashMap<String, Object>();

		logger.debug("가상계좌관리 > 기관별 가상계좌 보유현황 > 가상계좌 등록 - 엑셀등록(excel upload)");
		MultipartFile excelFile = request.getFile("file");
		String fgcd = request.getParameter("fgcd");

		File file = null;
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		file = new File(rootPath + excelFile.getOriginalFilename());

		try {
			excelFile.transferTo(file);
		} catch (IllegalStateException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		int pos = excelFile.getOriginalFilename().toLowerCase().lastIndexOf(".");
		String ext = excelFile.getOriginalFilename().substring(pos + 1);
		FileInputStream inputDocument = null;
		Workbook workbook = null;

		try {
			inputDocument = new FileInputStream(file);
			if (!ext.equals("xls") && !ext.equals("xlsx")) {
				retMap.put("resCode", "0001");
				return retMap;
			} else {
				if (file.getName().toLowerCase().endsWith("xlsx")) {
					workbook = new XSSFWorkbook(inputDocument);
				} else {
					workbook = new HSSFWorkbook(inputDocument);
				}
			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw e;
		}

		try {
			Sheet workSheet = workbook.getSheetAt(0);
			int rowSize = workSheet.getPhysicalNumberOfRows();

			for (int i = 0; i < rowSize; i++) {
				Row row = workSheet.getRow(i);
				Cell cell = row.getCell(0);
				if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
					rowSize = i;
					break;
				}
			}

			for (int i = 0; i < rowSize; i++) { // i를 1부터 시작해야 두번째 행부터 데이터가 입력된다.
				Row row = workSheet.getRow(i);
				int cellLength = 1; // 열의 총 개수

				// 셀서식을 텍스트로 초기화 한 엑셀 중 input값이 없는 row의 insert를 막기위함
				int no = 0;
				for (int j = 0; j < cellLength; j++) {
					Cell cell = row.getCell(j);
					if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						no++;
					}
				}

				final String[] columnValues = new String[cellLength];
				String valueStr = ""; // 엑셀에서 뽑아낸 데이터를 담아놓을 String 변수 선언 및 초기화
				CustReg01DTO dto = new CustReg01DTO(); // DB에 Insert하기 위해 valueStr 데이터를 옮겨담을 객체
				for (int j = 0; j < cellLength; j++) {
					Cell cell = row.getCell(j);
					try {
						// 셀에 있는 데이터들을 타입별로 분류해서 valueStr 변수에 담는다.
						if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) { // CELL_TYPE_BLANK로만 체크할 경우 비어있는  셀을 놓칠 수 있다.
							valueStr = "";
						} else {
							switch (cell.getCellType()) {
								case Cell.CELL_TYPE_STRING:
									valueStr = cell.getStringCellValue().trim();
									break;
								case Cell.CELL_TYPE_NUMERIC: // 날짜 형식이든 숫자 형식이든 다 CELL_TYPE_NUMERIC으로 인식.
									// 순수하게 숫자 데이터일 경우
									//Double numericCellValue = cell.getNumericCellValue();
									cell.setCellType(Cell.CELL_TYPE_STRING);
									String numericCellValue = cell.getStringCellValue();
									valueStr = numericCellValue + "";
									break;
								case Cell.CELL_TYPE_BOOLEAN:
									valueStr = cell.getBooleanCellValue() + "";
									break;
							}
						}
					} finally {
						columnValues[j] = StringUtils.trim(valueStr);
					}
				}

				HashMap<String, Object> reqMap = new HashMap<String, Object>();
				reqMap.put("vano", columnValues[0]);
				reqMap.put("fgcd", fgcd);
//				vanoMgmtService.insertVirtualAccount(reqMap);
			}

			retMap.put("resCode", "0000");
			retMap.put("rMsg", "0000");

			return retMap;
		} catch (Exception e) {
			logger.info("파일양식오류");
			logger.error(e.getMessage());
			throw e;
		} finally {
			inputDocument.close();
			logger.debug(">>>>>>>>>> :: 파일 삭제 완료");
			file.delete();
		}
	}


	/**
	 * 가상계좌관리 > 기관별 가상계좌 보유현황 > 가상계좌 등록 - cvs 등록 (cvs upload)
	 */
	@Transactional
	@SuppressWarnings("resource")
	@ResponseBody
	@RequestMapping("csvUpload")
	public HashMap<String, Object> csvUpload(MultipartHttpServletRequest request, boolean flag) throws Exception {

		HashMap<String, Object> retMap = new HashMap<>();
		HashMap<String, Object> reqMap ;
		MultipartFile csvFile =request.getFile("file");
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		String fgcd = request.getParameter("fgcd");


		File file = null;
		file = new File(rootPath + csvFile.getOriginalFilename());
		HashMap<String,Object> vanoMap = new HashMap<String,Object>();

		try {

			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()));
			List<HashMap<String,Object>> vanoList = new ArrayList<HashMap<String,Object>>(); //while 문 안에서만 유효해야한다
			int i = 0;

			while ((line = reader.readLine()) != null) {

				String stringArray[] = line.split(",");

				String vano = StringUtils.trim(stringArray[0]);

				reqMap = new HashMap<String, Object>(); //while 문 돌때마다 추가

				reqMap.put("vano", vano);
				reqMap.put("fgcd", fgcd);
				reqMap.put("settle_plag", "N");

				vanoList.add(reqMap); //i번째 리스트



				i ++;


			}
			int vanoListSize = vanoList.size();


			for(int k = 0; k <vanoList.size(); k += 1000){
				HashMap<String,Object> vanoMap1 = new HashMap<String,Object>();
				//List 에서 subList( index from값 , index to 값) k=0부터 vanoList.Size 까지)
				List<HashMap<String,Object>>sublist = vanoList.subList(k,Math.min(k + 1000, vanoList.size()));
				vanoMap1.put("vanoList", sublist);
				vanoMgmtService.insertVirtualAccount(vanoMap1);

			}

			reader.close(); //파일 삭제

			logger.debug(">>>>>>>>>> :: 파일 삭제 완료");

			file.delete();

			HashMap<String, Object> vanoReqMap = new HashMap<>();

			int VanoAccountresult = vanoMgmtService.getTempVanoAccount(vanoReqMap);

			if(vanoListSize != VanoAccountresult){
				logger.debug(">>>>>>>>>> :: 가상계좌Temp 테이블 과 업로드 값이 다릅니다.");

				logger.debug(">>>>>>>>>> :: 가상계좌Temp 테이블 N인값 삭제");

				vanoMgmtService.deleteVirtualAccount();

				retMap.put("resCode", "9998");
				retMap.put("rMsg", "가상계좌Temp 테이블 과 업로드 값이 다릅니다.");

				return retMap;

			}


			String AcNum = Integer.toString(VanoAccountresult);

			String result = "파일 업로드 성공 갯 수 "+AcNum ;

			retMap.put("resCode", "0000");
			retMap.put("rMsg", result);
			retMap.put("AcNum", AcNum);


		}catch (Exception e){
			logger.error(e.getMessage());

			retMap.put("resCode", "9999");
			retMap.put("rMsg", "업로드 실패 관리자에게 문의하세요");

		}






		return retMap;

	}


	@ResponseBody
	@RequestMapping("csvResult")
	public HashMap<String, Object> csvUploadResult(@RequestBody HashMap<String,Object> params , HttpServletRequest request, HttpServletResponse response) throws Exception {


		HashMap<String, Object> retMap = new HashMap<>();
		HashMap<String, Object> vanoReqMap = new HashMap<>();

		int VanoAccountresult = vanoMgmtService.getTempVanoAccount(vanoReqMap);

		String AcNum = Integer.toString(VanoAccountresult);
		String result = "파일 업로드 성공 갯 수 " + AcNum;


		retMap.put("retCode", "0000");
		retMap.put("rMsg", result);
		retMap.put("AcNum", AcNum);


		return retMap;
	}

	@ResponseBody
	@RequestMapping("csvResultInsert")
	public HashMap<String, Object> csvUploadResultInsert(@RequestBody HashMap<String,Object> params , HttpServletRequest request, HttpServletResponse response) throws Exception {


		HashMap<String, Object> retMap = new HashMap<>();
		HashMap<String, Object> vanoReqMap = new HashMap<>();
		try {

			vanoMgmtService.csvUploadResultInsert(vanoReqMap);


		}catch (Exception e){
			logger.error(e.getMessage());

		}

		int VanoAccountresult = vanoMgmtService.getTempVanoAccount(vanoReqMap);

		String AcNum = Integer.toString(VanoAccountresult);

		String result = "파일 업로드 성공 갯 수 " + AcNum;


		retMap.put("retCode", "0000");
		retMap.put("rMsg", result);
		retMap.put("AcNum", AcNum);

			return retMap;
		}



	@ResponseBody
	@RequestMapping("csvResultDelete")
	public HashMap<String, Object> csvResultDelete(@RequestBody HashMap<String,Object> params , HttpServletRequest request, HttpServletResponse response) throws Exception {


		HashMap<String, Object> retMap = new HashMap<>();
		HashMap<String, Object> vanoReqMap = new HashMap<>();


		try {
			vanoMgmtService.deleteVirtualAccount();
		}catch (Exception e){
			logger.error(e.getMessage());
			retMap.put("retCode", "9999");
			retMap.put("rMsg", "삭제실패");
			return retMap;
		}

		int VanoAccountresult = vanoMgmtService.getTempVanoAccount(vanoReqMap);

		String AcNum = Integer.toString(VanoAccountresult);


		retMap.put("retCode", "0000");
		retMap.put("rMsg", "삭제 성공");
		retMap.put("AcNum", AcNum);

		return retMap;
	}

	@ResponseBody
	@RequestMapping("vanoSendList")
	public HashMap<String, Object> vanoSendList(@RequestBody HashMap<String,Object> params , HttpServletRequest request, HttpServletResponse response) throws Exception {


		HashMap<String, Object> retMap = new HashMap<>();
		HashMap<String, Object> vanoReqMap = new HashMap<>();

		try {

			vanoMgmtService.deleteVirtualAccount();


		}catch (Exception e){
			logger.error(e.getMessage());
			retMap.put("retCode", "9999");
			retMap.put("rMsg", "삭제실패");
			return retMap;


		}

		int VanoAccountresult = vanoMgmtService.getTempVanoAccount(vanoReqMap);

		String AcNum = Integer.toString(VanoAccountresult);


		retMap.put("retCode", "0000");
		retMap.put("rMsg", "삭제 성공");
		retMap.put("AcNum", AcNum);

		return retMap;
	}

	/**
	 * PG 가상계좌 등록요청
	 */

	@RequestMapping("pgVanoSend")
	public ModelAndView chnewChaListaList() throws Exception {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("sys/pg/pg-send-vano");

		return mav;
	}


	/**
	 * PG 가상계좌 등록요청
	 */

	@ResponseBody
	@RequestMapping("pgVanoSendList")
	public HashMap<String, Object> pgVanoSendList(@RequestBody VanoPgDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {



		logger.debug("======pgVanoSendList==========={}",body);

		HashMap<String, Object> result = new HashMap<>();

		int totCount = vanoMgmtService.pgVanoSendListCnt(body); //distinct

		int totVanoCount = vanoMgmtDAO.pgVanoCheckCnt(body); //사용가능가상계좌수를조회

		PageVO page = new PageVO(totCount, body.getCurPage(), body.getPageScale());
		int start = page.getPageBegin();
		int end = page.getPageEnd();

		body.setStart(start);
		body.setEnd(end);

		logger.debug("start.toString() {}",start);
		logger.debug("end.toString() {}",end);



		List<VanoPgDTO> resultList =  vanoMgmtService.pgVanoSendList(body);


		result.put("resultList",resultList);
		result.put("totalItemCount",totCount);
		result.put("totVanoCount",totVanoCount);

		return result;


	}

	@ResponseBody
	@RequestMapping("pgVanoSendAjax")
	public HashMap<String, Object> pgVanoSendAjax(@RequestBody VanoPgDTO body, HttpServletRequest request) throws Exception {

		logger.debug("======pgVanoSendList==========={}",body);
		HashMap<String, Object> result = new HashMap<>();

		CloseableHttpClient httpClient = HttpClients.createDefault();

		String url = pgSendVanoUrl;

		Map<String,Object> reqMap = new HashMap<>();

		reqMap.put("mchtId",body.getMchtId());
		reqMap.put("pgvareqId",body.getPgvareqId());

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonBody = objectMapper.writeValueAsString(reqMap);
		HttpPost httpPost = new HttpPost(url);

		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		httpPost.setEntity(new StringEntity(jsonBody));


		HttpResponse response = httpClient.execute(httpPost);

		HttpEntity httpEntity = response.getEntity();
		Map<String,Object> responseMap = new HashMap<>();
		if(httpEntity != null){
			String reponseBody = EntityUtils.toString(httpEntity);
			responseMap  = objectMapper.readValue(reponseBody, new TypeReference<Map<String,Object>>(){});


		}

		httpClient.close();


		if(responseMap != null) {
			logger.info("responseCode====={}",responseMap.get("code"));

			if (responseMap.get("code").equals("OK")) {

				reqMap.put("appendYn","Y");

				logger.info("정상승인 상태 후처리 시작");
				vanoMgmtService.updateSendListStat(reqMap);
				logger.info("정상승인 상태 후처리 완료");

				result.put("retCode", "0000");
				result.put("retMsg", "정상승인");

				return result;

			}else {
				reqMap.put("appendYn","N");
				vanoMgmtService.updateSendListStat(reqMap);

				result.put("retCode", "9999");
				result.put("retMsg", "가상계좌 PG송신중 에러발생");

				return result;
			}


		}


		return result;

	}


}
