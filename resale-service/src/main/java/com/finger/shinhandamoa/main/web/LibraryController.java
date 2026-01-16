package com.finger.shinhandamoa.main.web;

import java.io.File;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
@Controller
public class LibraryController {
	
	private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);

	@Value("${file.path.library}")
	private String path;
	
	/*
	 * 자료실 > 자료실
	 */
	@RequestMapping("common/library")
	public ModelAndView library() throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("common/library/library");
		
		return mav;
	}
	
	/*
	 * 파일 다운로드
	 */
	@RequestMapping("common/library/download")
	public void fileDownload(@RequestParam String fileName, HttpServletResponse response, HttpServletRequest request) throws Exception {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String auth = authentication.getAuthorities().toString();
		String str = request.getParameter("flag");
		//param에서 파일이름을 바로 받을경우 파라미터를 조작하여 시스템파일을 다운로드할수있음
        //막기위해서 str, fileName에 따라 filePath 따로 설정
        String filePath = "";
//		if ("all".equals(str) && auth.contains("ROLE_ADMIN")) {
//			// 기관관리자
//			fileName = "damoaAll.zip";
//		} else if ("all".equals(str) && auth.contains("ROLE_USER")) {
//			// 납부자
//			fileName = "damoaAllPayer.zip";
//		} else if ("all".equals(str) && auth.contains("ROLE_ANONYMOUS")) {
//			// 로그인전
//			fileName = "damoaAllOrg.zip";
//		}

		if ("all".equals(str)) {
            filePath = "damoaAll.zip";
		} else if ("bank".equals(str)) {
            filePath = "bankDeal.pdf";
		} else if ("cms".equals(str)) {
            filePath = "cmsRequest.pdf";
		} else if ("info".equals(str)) {
            filePath = "credit.pdf";
		} else if ("withdraw".equals(str)) {
            filePath = "autoWithdrawal.pdf";
		} else if ("payer".equals(str)) {
            filePath = "payerManual.pdf";
		} else if ("org".equals(str)) {
            filePath = "orgManual.pdf";
		} else if ("kakaoAt".equals(str)) {
			filePath = "kakaoAt.pdf";
		}

		byte fileByte[] = FileUtils.readFileToByteArray(new File(path + filePath));

		String file = "";
		// 파일명 변경
		if ("all".equals(str)) {
			file = "서비스이용신청서.zip";
		} else if ("bank".equals(str)) {
			file = "은행거래신청서.pdf";
		} else if ("cms".equals(str)) {
			file = "신한기업CMS신청서.pdf";
		} else if ("info".equals(str)) {
			file = "다모아신청서_신용정보제공.pdf";
		} else if ("withdraw".equals(str)) {
			file = "자동출금동의서.pdf";
		} else if ("payer".equals(str)) {
			file = "신한_다모아_납부자_매뉴얼.pdf";
		} else if ("org".equals(str)) {
			file = "신한_다모아_기관담당자_매뉴얼.pdf";
		} else if ("kakaoAt".equals(str)) {
			file = "부가서비스해지신청서.pdf";
		}

		response.setContentType("application/octet-stream");
		response.setContentLength(fileByte.length);
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(file, "UTF-8") + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.getOutputStream().write(fileByte);

		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
}
