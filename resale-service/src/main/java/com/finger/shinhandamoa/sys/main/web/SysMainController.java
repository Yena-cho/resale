package com.finger.shinhandamoa.sys.main.web;

import java.io.*;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finger.shinhandamoa.data.file.mapper.SimpleFileMapper;
import com.finger.shinhandamoa.data.table.mapper.FwFileMapper;
import com.finger.shinhandamoa.data.table.model.FwFile;
import com.finger.shinhandamoa.data.table.model.WithdrawAgreement;
import com.finger.shinhandamoa.service.WithdrawAgreementService;
import com.finger.shinhandamoa.sys.setting.service.AutoTranMgmtService;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.finger.shinhandamoa.bank.dto.BankReg01DTO;
import com.finger.shinhandamoa.common.PdfViewer;
import com.finger.shinhandamoa.org.main.dto.XmontSumDTO;
import com.finger.shinhandamoa.sys.main.service.SysMainService;

/**
 * @author  
 * @date    2018. 4. 6.
 * @desc    
 * @version 
 * 
 */
@Controller
@RequestMapping("sys")
public class SysMainController {

	private static final Logger logger = LoggerFactory.getLogger(SysMainController.class);
	
	@Autowired
	private SysMainService sysMainService;
	
	@Value("${file.path.cms}")
	private String cmsPath;
	
	@Value("${file.path.upload}")
	private String uploadPath;

	@Inject
	private AutoTranMgmtService autoTranMgmtService;
	
	/*
	 * 기관관리 > 메인화면
	 */
	@RequestMapping("main/index")
	public ModelAndView sysMain() throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("sys/main/index");

		return mav;
	}	
	
	/**
	 * 기관메인 > 집계정보조회1
	 * 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("main/getSysMainInfo01Ajax")
	@ResponseBody
	public HashMap<String, Object> getSysMainInfo01Ajax(@RequestBody BankReg01DTO body) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {

			XmontSumDTO info = sysMainService.getSysMainInfo01(map);
			map.put("info", info); 
			map.put("retCode", "0000");
			map.put("retMsg", "정상");
			
		}catch(Exception e) {
			map.put("retCode", "9999");
			map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
		}
		
		return map;
	}	
	
	/*
	 * 납부집계 AJAX
	 */
	@ResponseBody
	@RequestMapping("main/selectPaymentRatioList")
	public HashMap selectPaymentRatioList(@RequestBody BankReg01DTO body) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
				
		List<XmontSumDTO> list = sysMainService.selectPaymentRatioList(map);
		map.put("list", list);
		
		return map;		
	}
	
	/*
	 * 납부집계 AJAX
	 */
	@ResponseBody
	@RequestMapping("main/selectPaymentSummary")
	public HashMap selectPaymentSummary(@RequestBody BankReg01DTO body) throws Exception {
		
		HashMap<String, Object> reqMap = new HashMap<String, Object>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		reqMap.put("pageScale", 5);		
		
		List<XmontSumDTO> list = sysMainService.selectPaymentSummary(reqMap);
		map.put("list", list);
		
		return map;		
	}
	
	/*
	 * 이미지 불러오기
	 */
	@ResponseBody
	@RequestMapping(value = "common/base64Images", method = RequestMethod.GET)
	public String base64image(HttpServletRequest request, @RequestParam("fileTy") String fileType, @RequestParam("fileName") String fileName) throws Exception {
		String base64Image = "";
		Base64InputStream in = null;
		BufferedInputStream bis = null;

		String pathType = "";
		HashMap<String, Object> map = new HashMap<String, Object>();

		if("cms".equals(fileType)) {
			map.put("chaCd", fileName);
			fileName = autoTranMgmtService.getCmsFileName(map);
			pathType = cmsPath;
		} else if("upload".equals(fileType)) {
			pathType = uploadPath;
		}
		
		File file = new File(pathType, fileName);
		try {
			in = new Base64InputStream(new FileInputStream(file), true);
			bis = new BufferedInputStream(in);
			byte[] buffer = new byte[4096];
			int read = 0;
			while ((read = bis.read(buffer)) != -1) {
				base64Image += new String(buffer, 0, read);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			if (in  != null) try {in.close();  in  = null;} catch(IOException ex){}
			if (bis != null) try {bis.close(); bis = null;} catch(IOException ex){}
		}
		return base64Image;
	}
	
	/*
	 * pdf 보여주기
	 */
	@RequestMapping(value = "common/pdfView", method = RequestMethod.POST)
	public ModelAndView test(HttpServletRequest request, HttpServletResponse response
					, @RequestParam("browserType") String browserType
					, @RequestParam("fileId") String fileId
					, @RequestParam("fileExt") String fileExt)  throws Exception {

		ModelAndView mav = new ModelAndView();
	
		try {
			String strInFile = uploadPath + fileId + fileExt;
			PdfViewer view = new PdfViewer();
			
			if(browserType.equals("ie")) {
				view.pdfIEDown(request, response, strInFile, fileId);
			} else {
				view.webPdfViewer(request, response, strInFile);
			}
			mav.setViewName("sys/additional/smsMgmt/pdfView");
		} catch(Exception e) {
			e.printStackTrace();
		}
	
		if(browserType.equals("ie")) {
			return null;
		}else {
			return mav;
		}
	}

	@Autowired
	private WithdrawAgreementService withdrawAgreementService;

	@RequestMapping(value="streamWithdrawAgreement", method = RequestMethod.GET)
	public void streamWithdrawAgreement(@RequestParam(value="id") String withdrawAgreementId, HttpServletResponse response) throws IOException {
		FwFile fwFile  = withdrawAgreementService.getFileInformation(withdrawAgreementId);
		InputStream is = withdrawAgreementService.getFile(withdrawAgreementId);

		response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fwFile.getLength()));
		response.setHeader(HttpHeaders.CONTENT_TYPE, fwFile.getMimeType());

		IOUtils.copy(is, response.getOutputStream());
	}

	@Autowired
	private FwFileMapper fwFileMapper;
	@Autowired
	private SimpleFileMapper simpleFileMapper;
	@RequestMapping(value="editorStream", method = RequestMethod.GET)
	public void editorStreamNotice(@RequestParam(value="id") String id, HttpServletResponse response) throws IOException {
		FwFile fwFile  = fwFileMapper.selectByPrimaryKey(id);

		InputStream is = simpleFileMapper.load("BOARD_NOTICE", id);

		response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fwFile.getLength()));
		response.setHeader(HttpHeaders.CONTENT_TYPE, fwFile.getMimeType());

		IOUtils.copy(is, response.getOutputStream());
	}
	@RequestMapping(value="editorStream/Faq", method = RequestMethod.GET)
	public void editorStreamFaq(@RequestParam(value="id") String id, HttpServletResponse response) throws IOException {
		FwFile fwFile  = fwFileMapper.selectByPrimaryKey(id);

		InputStream is = simpleFileMapper.load("BOARD_FAQ", id);

		response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fwFile.getLength()));
		response.setHeader(HttpHeaders.CONTENT_TYPE, fwFile.getMimeType());

		IOUtils.copy(is, response.getOutputStream());
	}
}
