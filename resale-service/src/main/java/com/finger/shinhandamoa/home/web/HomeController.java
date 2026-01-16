package com.finger.shinhandamoa.home.web;

import com.finger.shinhandamoa.bank.dto.BankReg01DTO;
import com.finger.shinhandamoa.bank.service.BankMgmtService;
import com.finger.shinhandamoa.common.*;
import com.finger.shinhandamoa.common.FingerIntegrateMessaging.MessageType;
import com.finger.shinhandamoa.data.file.mapper.SimpleFileMapper;
import com.finger.shinhandamoa.data.table.mapper.FwFileMapper;
import com.finger.shinhandamoa.data.table.model.FwFile;
import com.finger.shinhandamoa.home.dto.LoginDTO;
import com.finger.shinhandamoa.home.dto.TelResDTO;
import com.finger.shinhandamoa.home.dto.UserOtpDTO;
import com.finger.shinhandamoa.home.service.HomeService;
import com.finger.shinhandamoa.main.dto.NoticeDTO;
import com.finger.shinhandamoa.org.mypage.service.MyPageService;
import com.finger.shinhandamoa.service.WithdrawAgreementService;
import com.finger.shinhandamoa.sys.addServiceMgmt.dto.AddServiceMgmtDTO;
import com.finger.shinhandamoa.sys.addServiceMgmt.service.AddServiceMgmtService;
import com.finger.shinhandamoa.sys.bbs.dto.BannerDTO;
import com.finger.shinhandamoa.sys.bbs.dto.PopupDTO;
import com.finger.shinhandamoa.vo.UserDetailsVO;
import com.google.gson.JsonArray;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author by puki
 * @date 2018. 3. 30.
 * @desc 최초생성
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Inject
    private HomeService homeService;
    @Inject
    private AddServiceMgmtService addServiceMgmtService;

    @Inject
    private ShaEncoder shaEncoder;

    @Inject
    private BankMgmtService bankMgmtService;

    @Inject
    private MyPageService myPageService;

    @Autowired
    private WithdrawAgreementService withdrawAgreementService;
    @Autowired
    private FwFileMapper fwFileMapper;
    @Autowired
    private SimpleFileMapper simpleFileMapper;

    @Value("${fim.server.host}")
    private String host;

    @Value("${fim.server.port}")
    private int port;

    @Value("${fim.accessToken}")
    private String accessToken;

    // 팝업 디렉토리
    @Value("${file.path.popup}")
    private String popupPath;

    @Value("${send.telNo}")
    private String sendTelNo;

    // 로고 업로드 디렉토리
    @Value("${file.path.logo}")
    private String logoPath;

    @Value("${file.path.cms}")
    private String cmsPath;

    @Value("${file.path.upload}")
    private String uploadPath;

    @Value("${file.path.library}")
    private String path;
    
    @Value("${file.path.pdfTemp}")
    private String pdfTempPath;


    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Locale locale, Model model, HttpServletRequest request) throws Exception {
        logger.info("Welcome home! The client locale is {}.", locale);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().toString();    // 권한 Collection

        String userAgent = request.getHeader("User-Agent");
        String[] mobileos = {"iPhone", "iPod", "Android", "BlackBerry", "Windows CE", "Nokia", "Webos", "Opera Mini", "SonyEricsson", "Opera Mobi", "IEMobile"};
        int j = -1;

        HashMap<String, Object> map = new HashMap<String, Object>();

        String url = "";
        if (role.contains("ROLE_ADMIN") || role.contains("ROLE_GROUP_USER")) {
            // 기관관리자, 그룹사용자
            url = "org/main/index";
        } else if (role.contains("ROLE_CHASVC")) {
            // 기관관리자 약관동의 전
            url = "common/login/memberAuth";
        } else if (role.contains("ROLE_CHACMS")) {
            // 기관관리자 ARS 동의 전
            url = "common/login/chaAgreeCms";
        } else if (role.contains("ROLE_USER")) {
            // 납부자
            UserDetailsVO userDetailsVO = (UserDetailsVO) authentication.getPrincipal();
            String vano = userDetailsVO.getVano();
            String unCus = StringUtils.defaultString(userDetailsVO.getUnCus());
            map.put("vano", vano);

            int cnt = homeService.reciptCnt(map);
            boolean mobileChk = false;

            if (cnt > 0) {
                if (userAgent != null && !userAgent.equals("")) {
                    for (int i = 0; i < mobileos.length; i++) {
                        j = userAgent.indexOf(mobileos[i]);
                        if (j > -1) {
                            mobileChk = true;
                            break;
                        }
                    }
                }
            }

            if (mobileChk) {
                if (!unCus.equals("cus") && unCus != null) {
                    url = "payer/main/index";
                } else {
                    url = "payer/notification/mobileReciptList";
                }
            } else {
                url = "payer/main/index";
            }

        } else if (role.contains("ROLE_BANKADMIN")) {
            // 은행관리자
            url = "bank/selManage";
        } else if (role.contains("ROLE_SYSADMIN")) {
            // 신한다모아관리자
            url = "sys/index";
        } else if (role.contains("ROLE_GROUP_ADMIN")) {
            // 그룹관리자
            url = "group/groupList";
        } else {
            // 로그인전 메인 화면
            url = "main";
        }

        return "redirect:/" + url;
    }

    @RequestMapping("main")
    public ModelAndView main() throws Exception {

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        reqMap.put("role", "NOLogin");
        List<NoticeDTO> nList = homeService.nList(reqMap);  // 공지사항
        List<NoticeDTO> fList = homeService.fList(reqMap);    // 자주하는질문
        List<PopupDTO> pList = homeService.pList(reqMap);    // 팝업리스트
        List<BannerDTO> pcBannerList = homeService.pcBannerList(reqMap);
        List<BannerDTO> mobileBannerList = homeService.mobileBannerList(reqMap);

        for (int i = 0; i < pList.size(); i++) {
            String popFileName = pList.get(i).getFilename();
            pList.get(i).setFilename(popFileName);
        }

        map.put("nList", nList);
        map.put("fList", fList);
        map.put("pList", pList);
        map.put("popupPath", popupPath);
        map.put("pcBannerList", pcBannerList);
        map.put("mobileBannerList", mobileBannerList);

        mav.addObject("map", map);
        mav.setViewName("index");

        return mav;
    }

    /*
     * 전화 상담 예약
     */
    @ResponseBody
    @RequestMapping("telResInsert")
    public void telReservation(@RequestBody TelResDTO dto) throws Exception {

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("bbs", dto.getBbs());
            map.put("id", dto.getId());
            map.put("writer", dto.getWriter());
            map.put("title", dto.getTitle());
            map.put("contents", dto.getContents());
            map.put("code", dto.getCode());
            map.put("data1", dto.getData1());
            map.put("data2", dto.getData2());
            map.put("data3", dto.getData3());
            map.put("data4", dto.getData4());

            homeService.telReservation(map);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /*
     * 로그인 화면 이동
     */
    @RequestMapping("common/login")
    public String loginPage() {

        return "common/login/login";
    }

    @RequestMapping("/loggedOut")
    public String autoLoggedOut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        return "error/loggedOut";
//        response.sendRedirect("/error/denied.jsp");
//        request.getRequestDispatcher("/WEB-INF/views/error/denied.jsp").forward(request, response);
    }


    /*
     * 아이디, 비밀번호 찾기 화면 이동
     */
    @RequestMapping("common/idReset")
    public String idReset() {
        return "common/login/lookupIdResetPw";
    }

    /*
     * 아이디 찾기
     */
    @ResponseBody
    @RequestMapping("common/idSearch")
    public HashMap<String, Object> selIdSearch(@RequestBody LoginDTO dto) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> smap = new HashMap<String, Object>();
        smap.put("chaOffNo", dto.getChaOffNo());
        smap.put("feeAccNo", dto.getFeeAccNo());

        map.put("id", homeService.selIdSearch(smap));

        return map;
    }

    /*
     * 비밀번호 찾기
     */
    @ResponseBody
    @RequestMapping("common/passwordSearch")
    public HashMap<String, Object> selPasswordSearch(@RequestBody LoginDTO dto) throws Exception {

        final HashMap<String, Object> map = new HashMap<>();
        final HashMap<String, Object> smap = new HashMap<>();
        smap.put("loginId", dto.getLoginId());
        smap.put("chaOffNo", dto.getChaOffNo());
        smap.put("feeAccNo", dto.getFeeAccNo());

        LoginDTO ldto = homeService.selPasswordSearch(smap);

        if (ldto == null) {
            int idx = homeService.selLoginItem(smap);
            map.put("idx", idx);
        }

        map.put("pw", ldto);

        return map;
    }

    /*
     * 첫로그인시 회원확인절차 화면 이동
     */
    @RequestMapping("common/login/memberAuth")
    public String memberAuth() {

        return "common/login/memberAuth";
    }

    /*
     * 회원확인 > 아이디 중복체크
     */
    @ResponseBody
    @RequestMapping("common/login/idOverlap")
    public HashMap<String, Object> selIdOverlap(@RequestBody LoginDTO dto) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("id", homeService.selIdOverlap(dto.getLoginId()));

        return map;
    }

    /*
     * 회원확인 > 아이디, 비밀번호 변경
     */
    @Transactional
    @ResponseBody
    @RequestMapping("common/login/customerUpdate")
    public HashMap<String, Object> updateIdPassword(@RequestBody LoginDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            String pattern = "^[a-zA-Z0-9_]{8,20}$";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(dto.getLoginId());
            if (m.find()) {
                map.put("loginId", dto.getLoginId());
            } else {
                map.put("retCode", "0001");
                return map;
            }
            map.put("chaCd", chaCd);
            map.put("nowPw", shaEncoder.encoding(dto.getNowPw()));
            map.put("loginPw", shaEncoder.encoding(dto.getLoginPw()));
            map.put("userId", dto.getUserId());

            // 현재 비밀번호 조회
            String passWrd = myPageService.selectPwd(map);

            if (!passWrd.equals(map.get("nowPw"))) { // 현재비밀번호
                map.put("retCode", "8888");
            } else if (passWrd.equals(map.get("loginPw"))) {
                map.put("retCode", "9999");
            } else {
                homeService.updateIdPassword(map);        // 비밀번호 변경
                homeService.updateChaSvcYn(map);        // 약관동의

                authentication.setAuthenticated(false); // 로그아웃 처리
                map.put("retCode", "0000");
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "7777");
            throw e;
        }
        return map;
    }

    /*
     * 비밀번호 변경 > 인증번호 발송
     */
    @ResponseBody
    @RequestMapping("common/login/optNoSend")
    public HashMap<String, Object> sendOtpNo(@RequestBody UserOtpDTO dto) throws Exception {

//		Gson gson = new Gson();
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {

            HashMap<String, Object> imap = new HashMap<String, Object>();

            // 인증번호 생성 - 6자리
            String otpNo = String.valueOf(StrUtil.generateNumber(6));
            String recvNo = sendTelNo; // "15449350";

            String content = "[신한 다모아] " + dto.getLoginId() + "님 비밀번호 재설정 인증번호[" + otpNo + "]";

            int mLength = content.getBytes("utf-8").length;

            FingerIntegrateMessaging fingerIntegrateMessaging = new FingerIntegrateMessaging();

            fingerIntegrateMessaging.setHost(host);
            fingerIntegrateMessaging.setPort(port);
            fingerIntegrateMessaging.setAccessToken(accessToken);

            MessageType messageType;

            if (mLength > 80) {
                messageType = MessageType.MMS;
            } else {
                messageType = MessageType.SMS;
            }

            final ListResult<FingerIntegrateMessaging.Message> listResult = fingerIntegrateMessaging.createMessage(
                    messageType, "", content, null, recvNo, dto.getHpNo(), "신한다모아"); // sms 발송

            if (listResult.getItemList() != null) {

                resultMap.put("listResult", listResult);

                imap.put("loginId", dto.getLoginId());
                imap.put("hpNo", dto.getHpNo());
                imap.put("otpNo", otpNo);
                imap.put("loginYn", dto.getLoginYn());

                resultMap.put("otpNo", otpNo);

                // webuserotp 테이블에 데이터 저장
                homeService.mergeOtpNo(imap);

            } else {
                resultMap.put("retCode", "0001");
                resultMap.put("retMsg", "메세지 전송요청 중 오류가 발생 하였습니다.");

                return resultMap;
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return resultMap;
    }

    /*
     *  비밀번호 변경 > 인증확인
     */
    @ResponseBody
    @RequestMapping("common/login/smsSuccess")
    public void smsSuccess(@RequestBody UserOtpDTO dto) throws Exception {

        try {

            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("loginId", dto.getLoginId());
            map.put("hpNo", dto.getHpNo());
            map.put("otpNo", dto.getOtpNo());
            map.put("loginYn", dto.getLoginYn());

            homeService.mergeOtpNo(map);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /*
     *  비밀번호 변경 > 비밀번호 변경
     */
    @ResponseBody
    @RequestMapping("common/login/passwordUpdate")
    public void updatePassword(@RequestBody LoginDTO dto) throws Exception {

        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("loginId", dto.getLoginId());
            map.put("loginPw", shaEncoder.encoding(dto.getLoginPw()));

            homeService.updatePassword(map);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    /*
     *  404 error
     */
    @RequestMapping("common/404")
    public String notPage() throws Exception {
        return "/error/404";
    }

    /*
     *  500 error
     */
    @RequestMapping("common/500")
    public String errorPage() throws Exception {
        return "/error/500";
    }

    /*
     *  denied error
     */
    @RequestMapping("common/denied")
    public String accessDeniedPage() throws Exception {
        return "/error/denied";
    }

    /*
     *  로그인 > 고객명 찾기 - 자동완성
     */
    @RequestMapping(value = "common/login/selAutoChaName", method = RequestMethod.POST)
    public void AutoTest(Locale locale, Model model, HttpServletRequest request,
                         HttpServletResponse resp, LoginDTO dto) throws IOException {

        try {

            String username = request.getParameter("term");

            List<LoginDTO> list = homeService.selAutoChaName(username);

            JsonArray ja = new JsonArray();
            for (int i = 0; i < list.size(); i++) {
                ja.add(list.get(i).getLoginName());
            }

            PrintWriter out = resp.getWriter();
            out.print(ja.toString());

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /*
     * footer > 개인정보처리방침
     */
    @RequestMapping(value = "common/personalInfo/v1", method = RequestMethod.GET)
    public String policy(Locale locale, Model model) throws Exception {
        return "/include/personal-info";
    }

    /*
     * footer > 서비스이용약관
     */
    @RequestMapping(value = "common/serviceInfo/v1", method = RequestMethod.GET)
    public String serviceInfo(Locale locale, Model model) throws Exception {

        return "/include/policy";
    }

    /*
     * footer > 전자금융거래약관
     */
    @RequestMapping(value = "common/electronicFinancialInfo/v1", method = RequestMethod.GET)
    public String electronicFinancialInfo(Locale locale, Model model) throws Exception {

        return "/include/electronic-financial";
    }

    /*
     * 첫로그인  > 기관정보상세조회
     */
    @ResponseBody
    @RequestMapping("common/login/orgInfoSearch")
    public HashMap<String, Object> orgInfoSearch() throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            map.put("chaCd", chaCd);
            BankReg01DTO dto = bankMgmtService.selectChaListInfo(map);
            List<HashMap<String, Object>> list = bankMgmtService.getAgencyList(map);

            map.put("dto", dto);
            map.put("list", list);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return map;
    }


    @ResponseBody
    @RequestMapping(value = "common/base64Images", method = RequestMethod.GET)
    public String base64image(HttpServletRequest request, @RequestParam("fileTy") String fileTy, @RequestParam("fileName") String fileName) throws Exception {
        String base64Image = "";
        Base64InputStream in = null;
        BufferedInputStream bis = null;
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        File file = null;
        if ("popup".equals(fileTy)) {
            file = new File(popupPath, fileName);
        } else if ("cms".equals(fileTy)) {
            file = new File(cmsPath, fileName);
        } else if (fileTy.equals("upload")) {
            reqMap.put("chaCd", request.getParameter("chaCd"));
            reqMap.put("no", request.getParameter("no"));
            reqMap.put("start", 1);
            reqMap.put("end", 10);
            List<AddServiceMgmtDTO> list = addServiceMgmtService.smsRegList(reqMap);
            if (list.size() > 0) {
                file = new File(uploadPath + list.get(0).getFileid() + list.get(0).getFileext());
            }
        }{
            file = new File(logoPath, fileName + ".jpg");
        }
        if (file.exists()) {
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
                if (in != null) try {
                    in.close();
                    in = null;
                } catch (IOException ex) {
                }
                if (bis != null) try {
                    bis.close();
                    bis = null;
                } catch (IOException ex) {
                }
            }
        } else {
            base64Image = "";
        }
        return base64Image;
    }

    @ResponseBody
    @RequestMapping("common/showFile")
    public String showFile(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();

        File file = null;
        String fileNm = "";
        String fileId = "";
        String fileExt = "";
        String fileTy = "";
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        fileTy = request.getParameter("fileTy");

        if (fileTy.equals("logo")) {
            fileNm = user + ".jpg";
            file = new File(logoPath + fileNm);
        } else if (fileTy.equals("popup")) {
            reqMap.put("role", "NOLogin");
            List<PopupDTO> pList = homeService.pList(reqMap); // 팝업리스트
            for (int i = 0; i < pList.size(); i++) {
                String dbFileName = pList.get(i).getFilename();
                pList.get(i).setFilename(dbFileName.substring(0, 32) + dbFileName.substring(dbFileName.lastIndexOf(".")));
            }
            fileNm = pList.get(0).getFilename();
            file = new File(popupPath + fileNm);
        } else if (fileTy.equals("upload")) {
            reqMap.put("chaCd", request.getParameter("chaCd"));
            reqMap.put("no", request.getParameter("no"));
            reqMap.put("start", 1);
            reqMap.put("end", 10);
            List<AddServiceMgmtDTO> list = addServiceMgmtService.smsRegList(reqMap);
            if (list.size() > 0) {
                fileNm = list.get(0).getFileName();
                fileId = list.get(0).getFileid();
                fileExt = list.get(0).getFileext();
                
                file = new File(uploadPath + list.get(0).getFileid() + list.get(0).getFileext());
            }
        }

        response.reset();
        response.setHeader("Expires", "-1");
        response.setHeader("content-disposition", "inline; filename=\"" + fileNm + "\"");
        response.setContentType(CmmnUtils.getMimeType(file));
        response.setContentLength((int) file.length());

        if (file != null && file.isFile()) {
            InputStream fis = null;
            BufferedImage image = null;
            try {
                OutputStream os = response.getOutputStream();

                if (fileTy.equals("upload")) {
                    fis = new FileInputStream(file);
                    
                    //pdf 파일 이미지 변환 처리
                    if(!StringUtils.isEmpty(fileExt) && ".pdf".equals(fileExt)) {
                    	fileNm = PdfViewer.conversionPdfToImg(fis, pdfTempPath, fileId);
                    	
                    	File pngFile = new File(fileNm);
                    	fis = null;
                    	
                    	response.setContentType(CmmnUtils.getMimeType(pngFile));
                    	response.setContentLength((int) pngFile.length());
                    	os = response.getOutputStream();
                    	
                    	int pngPos = pngFile.getName().lastIndexOf(".");
                        String pngExt = pngFile.getName().substring(pngPos + 1);
                        image = ImageIO.read(pngFile);
                        
                        ImageIO.write(image, pngExt.toUpperCase(), os);
                    } else { 
                    
                    	IOUtils.copy(fis, os);
                    }
                } else {
                    int pos = file.getName().lastIndexOf(".");
                    String ext = file.getName().substring(pos + 1);
                    image = ImageIO.read(file);
                    ImageIO.write(image, ext.toUpperCase(), os);
                }
                os.flush();
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
                if (image != null) {
                    try {
                        image = null;
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        }
        return fileNm;
    }

    @RequestMapping(value = "/exit-run-as")
    public String exitRunAs(HttpSession httpSession) {
        final SecurityContext securityContext = SecurityContextHolder.getContext();

        final Authentication authentication = securityContext.getAuthentication();
        if (authentication instanceof RunAsAuthentication == false) {
            return "redirect:/";
        }

        final RunAsAuthentication runAsAuthentication = (RunAsAuthentication) authentication;
        runAsAuthentication.exitRunAs();

        securityContext.setAuthentication(runAsAuthentication);
        httpSession.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

        final List<GrantedAuthority> grantedAuthorityCollection = new ArrayList<>(runAsAuthentication.getAuthorities());

        String url = null;
        for (GrantedAuthority each : grantedAuthorityCollection) {
            switch (each.getAuthority()) {
                case "ROLE_GROUP_USER":
                    url = "/group/groupList";
                    break;
                case "ROLE_ADMIN":
                    url = "/org/main/index";
                    break;
                case "ROLE_GROUP_ADMIN":
                    url = "/group/groupList";
                    break;
                case "ROLE_SYSADMIN":
                    url = "/sys/index";
                    break;
                default:
                    break;
            }
        }

        return "redirect:" + url;
    }

    /**
     * 자동출금 동의 화면 이동
     *
     * @author mljeong@finger.co.kr
     */
    @RequestMapping("common/login/chaAgreeCms")
    public String chaAgreeCms() {

        return "common/login/chaAgreeCms";
    }

    /**
     * 자동출금 기관 정보
     *
     * @author mljeong@finger.co.kr
     */
    @ResponseBody
    @RequestMapping("common/login/cmsChaInfo")
    public HashMap<String, Object> cmsChaInfo() throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("chaCd", chaCd);

        try {
            LoginDTO getChaInfo = homeService.selCmsChaInfo(map);

            map.put("chaName", getChaInfo.getChaName());
            map.put("chaOffNo", getChaInfo.getChaOffNo());
            map.put("chrHp", getChaInfo.getChrHp());
            map.put("feeBankCd", getChaInfo.getFeeBankCd());
            map.put("feeVano", getChaInfo.getFeeVano());

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return map;
    }

    /**
     * 배너 이미지
     */

    /**
     * 자동출금 ARS 동의
     *
     * @author mljeong@finger.co.kr
     */
    @ResponseBody
    @RequestMapping("common/login/telArs")
    public Map<String, String> telArs() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        Map<String, String> resultMap = withdrawAgreementService.doAgreementWithARS(chaCd);

        return resultMap;
    }

    /**
     * 자동출금 동의서 다운로드
     *
     * @author mljeong@finger.co.kr
     */
    @RequestMapping("common/login/download")
    public void fileDownload(@RequestParam String fileName, HttpServletResponse response, HttpServletRequest request) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String auth = authentication.getAuthorities().toString();

        byte fileByte[] = FileUtils.readFileToByteArray(new File(path + fileName));

        String file = "자동출금동의서.pdf";

        response.setContentType("application/octet-stream");
        response.setContentLength(fileByte.length);
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(file, "UTF-8") + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.getOutputStream().write(fileByte);

        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    /**
     * 모바일 안내장
     *
     * @return
     * @author mljeong@finger.co.kr @feat jhjeong@finger.co.kr
     * @modified 2018. 10. 26.
     */
    @RequestMapping("promo")
    public String mobilePromotion() {
        return "promotion/m1";
    }

    /**
     * 모바일 안내장 1
     *
     * @return
     * @author mljeong@finger.co.kr @feat jhjeong@finger.co.kr
     * @modified 2018. 10. 26.
     */
    @RequestMapping("promo/m1")
    public String mobilePromotionM1() {
        return "promotion/m1";
    }

    /**
     * 모바일 안내장 2
     *
     * @return
     * @author mljeong@finger.co.kr @feat jhjeong@finger.co.kr
     * @modified 2018. 10. 26.
     */
    @RequestMapping("promo/m2")
    public String mobilePromotionM2() {
        return "promotion/m2";
    }

    /**
     * 모바일 안내장 3
     *
     * @return
     * @author mljeong@finger.co.kr @feat jhjeong@finger.co.kr
     * @modified 2018. 10. 26.
     */
    @RequestMapping("promo/m3")
    public String mobilePromotionM3() {
        return "promotion/m3";
    }

    /**
     * 모바일 안내장 4
     *
     * @return
     * @author mljeong@finger.co.kr @feat jhjeong@finger.co.kr
     * @modified 2018. 10. 26.
     */
    @RequestMapping("promo/m4")
    public String mobilePromotionM4() {
        return "promotion/m4";
    }

    /**
     * 모바일 안내장 5
     *
     * @return
     * @author mljeong@finger.co.kr @feat jhjeong@finger.co.kr
     * @modified 2018. 10. 26.
     */
    @RequestMapping("promo/m5")
    public String mobilePromotionM5() {
        return "promotion/m5";
    }

    /**
     * 모바일 안내장 6
     *
     * @return
     * @author mljeong@finger.co.kr @feat jhjeong@finger.co.kr
     * @modified 2018. 10. 26.
     */
    @RequestMapping("promo/m6")
    public String mobilePromotionM6() {
        return "promotion/m6";
    }

    /**
     * 배너 이미지
     */
    @RequestMapping(value="banner/image", method = RequestMethod.GET)
    public void mainImage(@RequestParam(value="id") String id, HttpServletResponse response) throws Exception {
        FwFile fwFile  = homeService.getFileInfo(id);
        InputStream is = homeService.getFile(id);

        response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fwFile.getLength()));
        response.setHeader(HttpHeaders.CONTENT_TYPE, fwFile.getMimeType());

        IOUtils.copy(is, response.getOutputStream());
    }
    /**
     * 팝업 이미지
     */
    @RequestMapping(value="popupStream", method = RequestMethod.GET)
    public void popupStream(@RequestParam(value="id") String id, HttpServletResponse response) throws IOException {
        FwFile fwFile  = fwFileMapper.selectByPrimaryKey(id);
        InputStream is = simpleFileMapper.load("BOARD_POPUP", id);

        response.setHeader(org.springframework.http.HttpHeaders.CONTENT_LENGTH, String.valueOf(fwFile.getLength()));
        response.setHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE, fwFile.getMimeType());

        IOUtils.copy(is, response.getOutputStream());
    }

}
