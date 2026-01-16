package com.finger.shinhandamoa.sys.bbs.web;

import com.finger.shinhandamoa.common.SimpleXSSSanitizer;
import com.finger.shinhandamoa.data.file.mapper.SimpleFileMapper;
import com.finger.shinhandamoa.data.table.mapper.FwFileMapper;
import com.finger.shinhandamoa.data.table.model.FwFile;
import com.finger.shinhandamoa.main.dto.NoticeDTO;
import com.finger.shinhandamoa.main.service.CustomerAsistService;
import com.finger.shinhandamoa.sys.bbs.dto.BannerDTO;
import com.finger.shinhandamoa.sys.bbs.dto.PopupDTO;
import com.finger.shinhandamoa.sys.bbs.dto.SysDTO;
import com.finger.shinhandamoa.sys.bbs.service.SysAsistService;
import com.finger.shinhandamoa.vo.PageVO;
import kr.co.finger.damoa.commons.io.HttpClientHelper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author by puki
 * @date 2018. 5. 11.
 * @desc 최초생성
 */
@Controller
public class SysBbsController {

    private static final Logger logger = LoggerFactory.getLogger(SysBbsController.class);

    @Inject
    private SysAsistService sysAsistService;

    @Inject
    private CustomerAsistService customerAsistService;

    @Autowired
    private FwFileMapper fwFileMapper;
    @Autowired
    private SimpleFileMapper simpleFileMapper;

    @Value("${server.path.local}")
    private String serverPathLocal;

    // 업로드 디렉토리
    @Value("${file.path.upload}")
    private String uploadPath;

    // 팝업 디렉토리
    @Value("${file.path.popup}")
    private String popupPath;

    // 베너 디렉토리
    @Value("${file.path.banner}")
    private String bannerPath;


    //전문테스트용
    @RequestMapping(value = "/msgtest", method = RequestMethod.POST)
    public ModelAndView msgtest(Locale locale, ModelAndView mv) throws Exception {

		/*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().toString();	// 권한 Collection
		ModelAndView mav = new ModelAndView();

		if(role.contains("ROLE_SYSADMIN")) {	 // 신한다모아관리자
			mav.setViewName("org/custMgmt/msg");
			mav.addObject("types", typeMap());
		} else {									 // 로그인전 메인 화면
			mav.setViewName("redirect:/main");
		}*/

        // @modified 2018. 07. 28  jhjeong@finger.co.kr
        // ip 대역을 체크. 허용대역은 211.232.21.39, 58.151.154.189, 58.151.154.190  만 허용
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String remoteIP = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
            remoteIP = request.getHeader("X-Forwarded_For");
        }
        if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
            remoteIP = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
            remoteIP = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
            remoteIP = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
            remoteIP = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
            remoteIP = request.getRemoteAddr();
        }

        logger.info("remoteIP = " + remoteIP);

        if (remoteIP.startsWith("127.0.0.1") || remoteIP.startsWith("10.20.30.")
                || remoteIP.startsWith("211.232.21.39")
                || remoteIP.startsWith("58.151.154.189")
                || remoteIP.startsWith("58.151.154.190")) {
            mv.setViewName("org/custMgmt/msg");
        } else {
            mv.setViewName("redirect:common/denied");
        }

        return mv;
    }

    private String[] typeMap() {
        return new String[]{"REQ", "START", "AGGREGATE", "LONG"};
    }

    @RequestMapping(value = "/msgtest2", method = RequestMethod.POST)
    public ModelAndView msgtest2(Locale locale, ModelAndView mv, @RequestParam(defaultValue = "REQ") String type,
                                 @RequestParam(defaultValue = "") String corpCode,
                                 @RequestParam(defaultValue = "") String vano,
                                 @RequestParam(defaultValue = "") String amount, @RequestParam(defaultValue = "") String result) throws Exception {
		/*
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String role = authentication.getAuthorities().toString();	// 권한 Collection

		ModelAndView mav = new ModelAndView();

		if(role.contains("ROLE_SYSADMIN")) {	 // 신한다모아관리자
			String vaBean = HttpClientHelper.callAsString(serverPathLocal+"/request/message2/?type=REQ&corpCode="+corpCode+"&vano="+vano+"&amount="+amount+"");
			mav.setViewName("org/custMgmt/msg");
			mav.addObject("result", vaBean);
		} else {									 // 로그인전 메인 화면
			mav.setViewName("redirect:/main");
		}
		*/
        // @modified 2018. 07. 28  jhjeong@finger.co.kr
        // ip 대역을 체크. 허용대역은 211.232.21.39, 58.151.154.189, 58.151.154.190  만 허용
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String remoteIP = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
            remoteIP = request.getHeader("X-Forwarded_For");
        }
        if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
            remoteIP = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
            remoteIP = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
            remoteIP = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
            remoteIP = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
            remoteIP = request.getRemoteAddr();
        }

        logger.info("remoteIP = " + remoteIP);

        if (remoteIP.startsWith("127.0.0.1") || remoteIP.startsWith("10.20.30.")
                || remoteIP.startsWith("211.232.21.39")
                || remoteIP.startsWith("58.151.154.189")
                || remoteIP.startsWith("58.151.154.190")) {
            String vaBean = HttpClientHelper.callAsString(serverPathLocal + "/request/message2/?type=REQ&corpCode=" + corpCode + "&vano=" + vano + "&amount=" + amount + "");
            mv.setViewName("org/custMgmt/msg");
            mv.addObject("result", vaBean);
        } else {
            mv.setViewName("redirect:common/denied");
        }

        return mv;
    }


    /*
     * 신한다모아관리자 > 로그인화면
     */
    @RequestMapping(value = "/sys", method = RequestMethod.GET)
    public String bankHome(Locale locale, Model model) throws Exception {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String remoteIP = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
            remoteIP = request.getHeader("X-Forwarded_For");
        }
        if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
            remoteIP = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
            remoteIP = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
            remoteIP = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
            remoteIP = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
            remoteIP = request.getRemoteAddr();
        }

        logger.info("remoteIP = " + remoteIP);
//        int pos = remoteIP.lastIndexOf(".");
//        String rIpEnd = remoteIP.substring(pos + 1);
//        if (remoteIP.startsWith("127.0.0.1") || remoteIP.startsWith("59.7.254." + rIpEnd) || remoteIP.startsWith("10.20.30." + rIpEnd) || remoteIP.startsWith("10.20.20." + rIpEnd)
//                || remoteIP.startsWith("221.151.63.193") || remoteIP.startsWith("58.151.154." + rIpEnd) || remoteIP.startsWith("0:0:0:0:0:0:0:1") || remoteIP.startsWith("10.30.0." + rIpEnd)) {
            return "sys/index";
//        } else {
//            return "redirect:common/denied";
//        }

    }

    /*
     * 신한다모아관리자 > index 화면
     */
    @RequestMapping("sys/index")
    public ModelAndView manageCollecter() throws Exception {

        ModelAndView mav = new ModelAndView();

        mav.setViewName("sys/main/index");
        //mav.setViewName("sys/bbs/bbsMgmt/counselList");

        return mav;
    }


    /*
     * 신한다모아관리자 > 공지사항 관리 리스트
     */
    @RequestMapping("sys/noticeSetting")
    public ModelAndView noticeSetting(@RequestParam(defaultValue = "day") String search_orderBy,
                                      @RequestParam(defaultValue = "all") String search_option,
                                      @RequestParam(defaultValue = "") String keyword,
                                      @RequestParam(defaultValue = "1") int curPage,
                                      @RequestParam(defaultValue = "10") int PAGE_SCALE,
                                      @RequestParam(defaultValue = "0") int bbs) throws Exception {

        Map<String, Object> reqMap = new HashMap<String, Object>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(new Date());

        reqMap.put("search_option", search_option);
        reqMap.put("keyword", keyword);
        reqMap.put("startday", "19880101");
        reqMap.put("endday", today);
        reqMap.put("bbs", bbs);

        // total count
        int totValue = sysAsistService.noticeTotalCount(reqMap);
        logger.info("totValue : " + totValue);
        PageVO page = new PageVO(totValue, curPage, PAGE_SCALE);
        int start = page.getPageBegin();
        int end = page.getPageEnd();

        reqMap.put("search_orderBy", search_orderBy);
        reqMap.put("start", start);
        reqMap.put("end", end);

        List<SysDTO> list = sysAsistService.noticeListAll(reqMap);

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("count", totValue);
        map.put("pager", page);    // 페이징 처리를 위한 변수
        map.put("search_orderBy", search_orderBy);
        map.put("search_option", search_option);
        map.put("curPage", curPage);
        map.put("keyword", keyword);
        map.put("PAGE_SCALE", PAGE_SCALE);
        mav.addObject("map", map);

        mav.setViewName("sys/bbs/bbsMgmt/noticeSetting");

        return mav;
    }

    /*
     * 신한다모아관리자 > ajax 공지사항 관리 리스트(페이지이동)
     */
    @RequestMapping("sys/ajaxNoticeSetting")
    @ResponseBody
    public HashMap<String, Object> AjaxNoticeList(@RequestBody SysDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();

        try {

            Map<String, Object> reqMap = new HashMap<String, Object>();
            //총개수 구하기
            reqMap.put("searchOrderBy", body.getSearchOrderBy());
            reqMap.put("search_option", body.getSearchOption());
            reqMap.put("keyword", body.getKeyword());
            reqMap.put("startday", body.getStartday());
            reqMap.put("endday", body.getEndday());
            //bbs 설정 :  로그인전 : 7, 기관담당자 : 8, 납부자 6
            int bbs = 0;
			/*if(body.getOption1() == "true" && body.getOption2() == "false" && body.getOption3() == "false") {
				bbs = 7;
			}else if(body.getOption1() == "false" && body.getOption2() == "true" && body.getOption3() == "false") {
				bbs = 8;
			}else if(body.getOption1() == "false" && body.getOption2() == "false" && body.getOption3() == "true") {
				bbs = 6;
			}else if(body.getOption1() == "true" && body.getOption2() == "true" && body.getOption3() == "false") {
				bbs = 11;
			}else if(body.getOption1() == "true" && body.getOption2() == "false" && body.getOption3() == "true") {
				bbs = 9;
			}else if(body.getOption1() == "false" && body.getOption2() == "true" && body.getOption3() == "true") {
				bbs = 10;
			}else if(body.getOption1() == "true" && body.getOption2() == "true" && body.getOption3() == "true") {
				bbs = 9999;
			}else {
				bbs = 9990;
			}*/
            bbs = noticeRollByBbs(body.getOption1(), body.getOption2(), body.getOption3());
            reqMap.put("bbs", bbs);

            int totValue = sysAsistService.noticeTotalCount(reqMap);

            // 페이지 관련 설정
            PageVO page = new PageVO(totValue, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            reqMap.put("start", start);
            reqMap.put("end", end);

            List<SysDTO> list = sysAsistService.noticeListAll(reqMap);

            ModelAndView mav = new ModelAndView();
            map.put("list", list);
            map.put("count", totValue);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("search_orderBy", body.getSearchOrderBy());
            map.put("search_option", body.getSearchOption());
            map.put("curPage", body.getCurPage());
            map.put("keyword", body.getCurPage());
            map.put("PAGE_SCALE", body.getPageScale());
            map.put("bbs", bbs);
            map.put("retCode", "0000");
            mav.addObject("map", map);

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /*
     * 공지사항 글쓰기
     */
    @RequestMapping("sys/doNoticeWrite")
    @ResponseBody
    public HashMap<String, Object> qnaInsert(MultipartHttpServletRequest multi) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            // 업로드 디렉토리 생성
            File uploadDir = new File(uploadPath);
            if (uploadDir.exists() == false) {
                uploadDir.mkdirs();
            }
            //저장 경로 설정
            //String root = multi.getSession().getServletContext().getRealPath("/");
            String path = uploadPath;
            //업로드 되는 파일명
            String newFileName = "";

            Iterator<String> files = multi.getFileNames();
            while (files.hasNext()) {
                String uploadFile = files.next();
                MultipartFile mFile = multi.getFile(uploadFile);
                String fileName = mFile.getOriginalFilename();

                //확장자 추출
                int pos = fileName.lastIndexOf(".");
                String ext = fileName.substring(pos + 1);

                Map<String, Object> reqMap = new HashMap<String, Object>();
                reqMap.put("id", multi.getParameter("loginId"));
                reqMap.put("writer", multi.getParameter("writer"));
                reqMap.put("email", multi.getParameter("email"));
                reqMap.put("title", multi.getParameter("title"));
                reqMap.put("contents", multi.getParameter("contents"));
                reqMap.put("filesize", mFile.getSize());
                reqMap.put("ip", "");
                reqMap.put("isfix", Integer.parseInt(multi.getParameter("isfix")));

                if (multi.getParameter("login").equals("true")) {
                    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                    if (fileName != "") {
                        newFileName = uuid;
                    } else {
                        newFileName = uuid;
                    }
                    reqMap.put("filename", fileName);
                    reqMap.put("fileid", uuid); //코드값 uuid
                    reqMap.put("fileext", "." + ext); //확장자만
                    reqMap.put("bbs", 7);

                    sysAsistService.doNoticeWrite(reqMap);
                    //프로젝트 서버 폴더에 파일 옴기기
                    if (newFileName != "") {
                        //mFile.transferTo(new File(path+newFileName)); 20180621 파일 uuid 만으로 서버에 저장
                        mFile.transferTo(new File(path + uuid));
                    }
                }
                if (multi.getParameter("org").equals("true")) {
                    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                    if (fileName != "") {
                        newFileName = uuid;
                    } else {
                        newFileName = uuid;
                    }
                    reqMap.put("filename", fileName);
                    reqMap.put("fileid", uuid); //코드값 uuid
                    reqMap.put("fileext", "." + ext); //확장자만
                    reqMap.put("bbs", 8);
                    sysAsistService.doNoticeWrite(reqMap);
                    //프로젝트 서버 폴더에 파일 옴기기
                    if (newFileName != "") {
                        //mFile.transferTo(new File(path+newFileName)); 20180621 파일 uuid 만으로 서버에 저장
                        mFile.transferTo(new File(path + uuid));
                    }
                }
                if (multi.getParameter("submit").equals("true")) {
                    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                    if (fileName != "") {
                        newFileName = uuid;
                    } else {
                        newFileName = uuid;
                    }
                    reqMap.put("filename", fileName);
                    reqMap.put("fileid", uuid); //코드값 uuid
                    reqMap.put("fileext", "." + ext); //확장자만
                    reqMap.put("bbs", 6);
                    sysAsistService.doNoticeWrite(reqMap);
                    //프로젝트 서버 폴더에 파일 옴기기
                    if (newFileName != "") {
                        //mFile.transferTo(new File(path+newFileName)); 20180621 파일 uuid 만으로 서버에 저장
                        mFile.transferTo(new File(path + uuid));
                    }
                }
            }
            map.put("retCode", "0000");

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /*
     * 신한 다모아 관리자 자주하는 질문 관리
     */
    @RequestMapping("sys/noticeUpWr")
    public ModelAndView noticeUpWr(@RequestParam(defaultValue = "0") long no) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        ModelAndView mav = new ModelAndView();
        if (no == 0) {
            //글쓰기로 들어온 경우
            mav.setViewName("sys/bbs/bbsMgmt/noticeWrite");
        } else {
            //수정하기로 들어온경우
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("no", no);
            SysDTO dto = sysAsistService.selectByNo(reqMap);
            map.put("dto", dto);
            mav.addObject("map", map);
            mav.setViewName("sys/bbs/bbsMgmt/noticeUpdate");
        }

        return mav;
    }

    /*
     * 신한 다모아 자주하는질문관리 수정 글쓰기
     */
    @RequestMapping("sys/faqUpWr")
    public ModelAndView faqUpWr(@RequestParam(defaultValue = "0") long no) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        ModelAndView mav = new ModelAndView();
        if (no == 0) {
            //글쓰기로 들어온 경우
            mav.setViewName("sys/bbs/bbsMgmt/faqWrite");
        } else {
            //수정하기로 들어온경우
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("no", no);
            SysDTO dto = sysAsistService.selectByNo(reqMap);
            dto.setContents(dto.getContents().replaceAll("<br/>", "\r\n"));
            map.put("dto", dto);
            mav.addObject("map", map);
            mav.setViewName("sys/bbs/bbsMgmt/faqUpdate");
        }

        return mav;
    }

    @RequestMapping("sys/faqWrite")
    @ResponseBody
    public HashMap<String, Object> faqWrite(@RequestBody SysDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {

            body.setContents(body.getContents().replaceAll("\\r|\\n", "<br/>"));

            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("id", body.getId());
            reqMap.put("writer", body.getWriter());
            reqMap.put("email", body.getEmail());
            reqMap.put("title", body.getTitle());
            reqMap.put("contents", body.getContents());
            reqMap.put("ip", "");

            if (body.getOption1().equals("true")) {
                if (body.getCategory().equals("req")) {
                    reqMap.put("bbs", 51);
                } else if (body.getCategory().equals("pay")) {
                    reqMap.put("bbs", 52);
                } else if (body.getCategory().equals("site")) {
                    reqMap.put("bbs", 53);
                } else if (body.getCategory().equals("noti")) {
                    reqMap.put("bbs", 54);
                } else if (body.getCategory().equals("receice")) {
                    reqMap.put("bbs", 55);
                } else if (body.getCategory().equals("additional")) {
                    reqMap.put("bbs", 56);
                } else if (body.getCategory().equals("cash")) {
                    reqMap.put("bbs", 57);
                } else {
                    map.put("retCode", "9999");
                    return map;
                }
                sysAsistService.doFaqWrite(reqMap);
            }
            if (body.getOption2().equals("true")) {
                if (body.getCategory().equals("req")) {
                    reqMap.put("bbs", "61");
                } else if (body.getCategory().equals("pay")) {
                    reqMap.put("bbs", "62");
                } else if (body.getCategory().equals("site")) {
                    reqMap.put("bbs", "63");
                } else if (body.getCategory().equals("noti")) {
                    reqMap.put("bbs", "64");
                } else if (body.getCategory().equals("receive")) {
                    reqMap.put("bbs", "65");
                } else if (body.getCategory().equals("additional")) {
                    reqMap.put("bbs", "66");
                } else if (body.getCategory().equals("cash")) {
                    reqMap.put("bbs", "67");
                } else {
                    map.put("retCode", "9999");
                    return map;
                }
                sysAsistService.doFaqWrite(reqMap);
            }
            if (body.getOption3().equals("true")) {
                if (body.getCategory().equals("req")) {
                    reqMap.put("bbs", "71");
                } else if (body.getCategory().equals("pay")) {
                    reqMap.put("bbs", "72");
                } else if (body.getCategory().equals("site")) {
                    reqMap.put("bbs", "73");
                } else if (body.getCategory().equals("noti")) {
                    reqMap.put("bbs", "74");
                } else if (body.getCategory().equals("receive")) {
                    reqMap.put("bbs", "75");
                } else if (body.getCategory().equals("additional")) {
                    reqMap.put("bbs", "76");
                } else if (body.getCategory().equals("cash")) {
                    reqMap.put("bbs", "77");
                } else {
                    map.put("retCode", "9999");
                    return map;
                }
                sysAsistService.doFaqWrite(reqMap);
            }
            map.put("retCode", "0000");

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    @RequestMapping("sys/faqSetting")
    public ModelAndView qnaSetting(@RequestParam(defaultValue = "day") String search_orderBy,
                                   @RequestParam(defaultValue = "all") String search_option,
                                   @RequestParam(defaultValue = "") String keyword,
                                   @RequestParam(defaultValue = "1") int curPage,
                                   @RequestParam(defaultValue = "10") int PAGE_SCALE,
                                   @RequestParam(defaultValue = "all") String role,
                                   @RequestParam(defaultValue = "all") String category) throws Exception {

        ModelAndView mav = new ModelAndView();

        mav.setViewName("sys/bbs/bbsMgmt/faqSetting");

        return mav;
    }

    /*
     * 신한다모아관리자 > faq  관리 리스트(페이지이동)
     */
    @RequestMapping("sys/ajaxFaqList")
    @ResponseBody
    public HashMap<String, Object> AjaxQnaList(@RequestBody SysDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {


        HashMap<String, Object> map = new HashMap<String, Object>();

        try {

            Map<String, Object> reqMap = new HashMap<String, Object>();
            //총개수 구하기
            reqMap.put("searcgOrderBy", body.getSearchOrderBy());
            reqMap.put("search_option", body.getSearchOption());
            reqMap.put("keyword", body.getKeyword());

//			List<String> daylist = startEndDay(body.getStartday(), body.getEndday());
            reqMap.put("startday", body.getStartday());
            reqMap.put("endday", body.getEndday());

            //bbs 설정
            String role = "";
            if (body.getOption0() == "ture" && body.getOption1() == "false" && body.getOption2() == "false" && body.getOption3() == "false") {
                role = "all";
            } else if (body.getOption1() == "true" && body.getOption2() == "false" && body.getOption3() == "false") {
                role = "login";
            } else if (body.getOption1() == "false" && body.getOption2() == "true" && body.getOption3() == "false") {
                role = "org";
            } else if (body.getOption1() == "false" && body.getOption2() == "false" && body.getOption3() == "true") {
                role = "submit";
            } else if (body.getOption1() == "true" && body.getOption2() == "true" && body.getOption3() == "false") {
                role = "lo";
            } else if (body.getOption1() == "true" && body.getOption2() == "false" && body.getOption3() == "true") {
                role = "ls";
            } else if (body.getOption1() == "false" && body.getOption2() == "true" && body.getOption3() == "true") {
                role = "os";
            } else if (body.getOption1() == "true" && body.getOption2() == "true" && body.getOption3() == "true") {
                role = "all";
            } else {
                role = "all";
            }

            reqMap.put("role", role);
            reqMap.put("category", body.getCategory());
            int totValue = sysAsistService.faqTotalCount(reqMap);

            // 페이지 관련 설정
            PageVO page = new PageVO(totValue, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            reqMap.put("start", start);
            reqMap.put("end", end);

            List<SysDTO> list = sysAsistService.faqListAll(reqMap);

            ModelAndView mav = new ModelAndView();
            map.put("list", list);
            map.put("count", totValue);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("search_orderBy", body.getSearchOrderBy());
            map.put("search_option", body.getSearchOption());
            map.put("curPage", body.getCurPage());
            map.put("keyword", body.getCurPage());
            map.put("PAGE_SCALE", body.getPageScale());
            map.put("retCode", "0000");
            mav.addObject("map", map);

            reqMap.put("PAGE_SCALE", body.getPageScale());

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /*
     * 게시판 글 삭제
     */
    @RequestMapping("sys/ajaxDelete")
    @ResponseBody
    public HashMap<String, Object> ajaxDelete(@RequestBody NoticeDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            NoticeDTO dto = customerAsistService.noticeDetail(body.getNo());
            //해당글의 파일삭제
            //String root = request.getSession().getServletContext().getRealPath("/");
            String path = uploadPath;                //리얼페스 받아오기
            logger.info(path);
            logger.info("삭제할 파일이름" + dto.getFilename());//20180621 파일 uuid 만으로 서버에 저장

            if (dto.getFilename() != "" && dto.getFilename() != null) {
                File file = new File(path + dto.getFilename());//20180621 파일 uuid 만으로 서버에 저장

                if (file.exists()) {
                    if (file.delete()) {
                        logger.info("파일 삭제 성공");
                    } else {
                        logger.info("파일 삭제 실패");
                    }
                } else {
                    logger.info("파일이 존재하지 않습니다.");
                }
            }

            if (dto.getFileid() != "" && dto.getFileid() != null) {
                File file = new File(path + dto.getFileid());//20180621 파일 uuid 만으로 서버에 저장

                if (file.exists()) {
                    if (file.delete()) {
                        logger.info("파일 삭제 성공");
                    } else {
                        logger.info("파일 삭제 실패");
                    }
                } else {
                    logger.info("파일이 존재하지 않습니다.");
                }
            }

            //해당글 테이블에서 삭제
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("no", body.getNo());
            customerAsistService.qnaDelete(reqMap);
            map.put("retCode", "0000");

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /*
     * faq 수정
     */
    @RequestMapping("sys/faqUpdate")
    @ResponseBody
    public HashMap<String, Object> faqUpdate(@RequestBody SysDTO body, HttpServletRequest request) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            body.setContents(body.getContents().replaceAll("\\r|\\n", "<br/>"));
            reqMap.put("no", body.getNo());
            reqMap.put("writer", body.getWriter());
            reqMap.put("title", body.getTitle());
            reqMap.put("contents", body.getContents());
            reqMap.put("ip", "");

            sysAsistService.faqUpdate(reqMap);

            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /*
     * 공지사항 수정 파일 x
     */
    @RequestMapping("sys/noticeUpdateNoFile")
    @ResponseBody
    public HashMap<String, Object> notieUpdate_nofile(@RequestBody SysDTO body, HttpServletRequest request) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();

            reqMap.put("title", body.getTitle());
            reqMap.put("writer", body.getWriter());
            reqMap.put("contents", body.getContents());
            reqMap.put("ip", "");
            reqMap.put("no", body.getNo());
            reqMap.put("isfix", body.getIsfix());

            sysAsistService.faqUpdate(reqMap);

            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /*
     * 공지사항 수정 파일 o
     */
    @RequestMapping("sys/noticeUpdate")
    @ResponseBody
    public HashMap<String, Object> noticeUpdate(MultipartHttpServletRequest multi, HttpServletRequest request) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        try {
            String no = multi.getParameter("no");
            reqMap.put("no", no);
            SysDTO dto = sysAsistService.selectByNo(reqMap);

            //해당글의 기존 파일 삭제
            //String root = request.getSession().getServletContext().getRealPath("/");
            String path = uploadPath;                //리얼페스 받아오기
            if (dto.getFileid() != "" || dto.getFileid() == null) {
                File file = new File(path + dto.getFileid()); //20180621 파일 uuid 만으로 서버에 저장

                if (file.exists()) {
                    if (file.delete()) {
                        logger.info("파일 삭제 성공");
                    } else {
                        logger.info("파일 삭제 실패");
                    }
                } else {
                    logger.info("파일이 존제하지 않습니다.");
                }
            }
            //새로 등록된 파일로 등록
            //업로드 되는 파일명
            String newFileName = "";

            Iterator<String> files = multi.getFileNames();
            while (files.hasNext()) {
                String uploadFile = files.next();
                MultipartFile mFile = multi.getFile(uploadFile);
                String fileName = mFile.getOriginalFilename();
                //확장자 추출
                int pos = fileName.lastIndexOf(".");
                String ext = fileName.substring(pos + 1);
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                if (fileName != "") {
                    newFileName = uuid;
                } else {
                    newFileName = uuid;
                }
                //임시 추후 테이블 에 uuid 까지 넣기로 하면 위에 걸로 파일 저장해야함

                //프로젝트 서버 폴더에 파일 옴기기
                mFile.transferTo(new File(path + newFileName.substring(0, 32))); //20180621 파일 uuid 만으로 서버에 저장

                //해당 테이블 수정
                reqMap.put("title", multi.getParameter("title"));
                reqMap.put("contents", multi.getParameter("contents"));
                reqMap.put("filename", fileName);
                reqMap.put("filesize", mFile.getSize());
                reqMap.put("fileid", newFileName); //코드값 uuid
                reqMap.put("fileext", "." + ext); //확장자만
                reqMap.put("ip", "");
                reqMap.put("isfix", multi.getParameter("isfix"));

                sysAsistService.noticeUpdate(reqMap);
            }

            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    @RequestMapping("sys/qnaSetting")
    public ModelAndView qnaSetting(@RequestParam(defaultValue = "day") String search_orderBy,
                                   @RequestParam(defaultValue = "all") String search_option,
                                   @RequestParam(defaultValue = "") String keyword,
                                   @RequestParam(defaultValue = "1") int curPage,
                                   @RequestParam(defaultValue = "10") int PAGE_SCALE,
                                   @RequestParam(defaultValue = "10") int bbs) throws Exception {

        Map<String, Object> reqMap = new HashMap<String, Object>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(new Date());

        reqMap.put("search_option", search_option);
        reqMap.put("keyword", keyword);
        reqMap.put("startday", "19880101");
        reqMap.put("endday", today);
        reqMap.put("code", "");
        reqMap.put("chaname", "");
        reqMap.put("reple", "wait");

        // total count
        int totValue = sysAsistService.qnaTotalCount(reqMap);
        PageVO page = new PageVO(totValue, curPage, PAGE_SCALE);
        int start = page.getPageBegin();
        int end = page.getPageEnd();

        reqMap.put("search_orderBy", search_orderBy);
        reqMap.put("start", start);
        reqMap.put("end", end);

        List<SysDTO> list = sysAsistService.qnaListAll(reqMap);

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("count", totValue);
        map.put("pager", page);    // 페이징 처리를 위한 변수
        map.put("search_orderBy", search_orderBy);
        map.put("search_option", search_option);
        map.put("curPage", curPage);
        map.put("keyword", keyword);
        map.put("PAGE_SCALE", PAGE_SCALE);
        mav.addObject("map", map);

        mav.setViewName("sys/bbs/bbsMgmt/qnaSetting");

        return mav;
    }


    @RequestMapping("sys/ajaxQnaSetting")
    @ResponseBody
    public HashMap<String, Object> ajaxQnaSetting(@RequestBody SysDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> reqMap = new HashMap<String, Object>();

        try {
            //총개수 구하기
            reqMap.put("search_option", body.getSearchOption());
            reqMap.put("keyword", body.getKeyword());
            reqMap.put("category", body.getCategory());
            if (body.getStartday() == "" || body.getStartday() == null) {
                reqMap.put("startday", "19880101");
            } else {
                reqMap.put("startday", body.getStartday());
            }
            if (body.getEndday() == "" || body.getEndday() == null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String today = sdf.format(new Date());
                reqMap.put("endday", today);
            } else {
                reqMap.put("endday", body.getEndday());
            }
            if (body.getOption0().equals("ture") && body.getOption1().equals("false") && body.getOption2().equals("false")) {
                reqMap.put("reple", "all");
            } else if (body.getOption1().equals("true") && body.getOption2().equals("false")) {
                reqMap.put("reple", "wait");
            } else if (body.getOption1().equals("false") && body.getOption2().equals("true")) {
                reqMap.put("reple", "complete");
            } else {
                reqMap.put("reple", "all");
            }
            reqMap.put("code", body.getCode());
            reqMap.put("chaname", body.getChaname());

            int totValue = sysAsistService.qnaTotalCount(reqMap);

            // 페이지 관련 설정
            PageVO page = new PageVO(totValue, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            reqMap.put("start", start);
            reqMap.put("end", end);

            List<SysDTO> list = sysAsistService.qnaListAll(reqMap);

            ModelAndView mav = new ModelAndView();
            map.put("list", list);
            map.put("count", totValue);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("search_orderBy", body.getSearchOrderBy());
            map.put("search_option", body.getSearchOption());
            map.put("curPage", body.getCurPage());
            map.put("keyword", body.getCurPage());
            map.put("PAGE_SCALE", body.getPageScale());
            map.put("retCode", "0000");
            mav.addObject("map", map);

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    @RequestMapping("sys/qnaUpWr")
    public ModelAndView qnaUpWr(@RequestParam(defaultValue = "") long no) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("no", no);
        SysDTO dto = sysAsistService.selectByNo(reqMap);
        SysDTO repledto = sysAsistService.selectByFromno(reqMap);
        dto.setContents(dto.getContents().replaceAll("<br/>", "\n"));
        dto.setTitle(StringEscapeUtils.escapeHtml4(dto.getTitle()));
        if (repledto != null) {
            repledto.setContents(repledto.getContents().replaceAll("<br/>", "\n"));
        }
        map.put("dto", dto);
        map.put("repledto", repledto);

        ModelAndView mav = new ModelAndView();
        mav.addObject("map", map);
        mav.setViewName("sys/bbs/bbsMgmt/qnaUpWr");

        return mav;
    }

    @RequestMapping("sys/qnaInsert")
    @ResponseBody
    public HashMap<String, Object> qnaInsert(@RequestBody SysDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        try {
            //email이 없는 관리자도 있음...
            String email = "";
            if (body.getEmail() == null) {
                email = "";
            } else {
                email = body.getEmail();
            }
            body.setContents(body.getContents().replaceAll("\\r|\\n", "<br/>"));
            reqMap.put("fromno", body.getFromno());
            reqMap.put("step", body.getStep());
            reqMap.put("id", body.getId());
            reqMap.put("password", body.getPassword());
            reqMap.put("writer", body.getWriter());
            reqMap.put("title", body.getTitle());
            reqMap.put("contents", body.getContents());
            reqMap.put("ip", "");
            reqMap.put("code", body.getCode());
            reqMap.put("email", email);

            sysAsistService.qnaInsert(reqMap);

            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    @RequestMapping("sys/qnaUpdate")
    @ResponseBody
    public HashMap<String, Object> qnaUpdate(@RequestBody SysDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        try {
            //logger.info(body.getNo());
            body.setContents(body.getContents().replaceAll("\\r|\\n", "<br/>"));
            reqMap.put("no", body.getNo());
            reqMap.put("contents", body.getContents());
            reqMap.put("ip", "");
            sysAsistService.qnaUpdqte(reqMap);

            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    @RequestMapping("sys/popupSetting")
    public ModelAndView popupSetting(@RequestParam(defaultValue = "all") String search_option,
                                     @RequestParam(defaultValue = "") String keyword,
                                     @RequestParam(defaultValue = "1") int curPage,
                                     @RequestParam(defaultValue = "10") int PAGE_SCALE) throws Exception {
        Map<String, Object> reqMap = new HashMap<String, Object>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(new Date());

        reqMap.put("search_option", search_option);
        reqMap.put("keyword", keyword);
        reqMap.put("startday", "19880101");
        reqMap.put("endday", today);

        // total count
        int totValue = sysAsistService.popupTotalCount(reqMap);
        PageVO page = new PageVO(totValue, curPage, PAGE_SCALE);
        int start = page.getPageBegin();
        int end = page.getPageEnd();

        reqMap.put("searchOrderBy", "regDt");
        reqMap.put("start", start);
        reqMap.put("end", end);

        List<PopupDTO> list = sysAsistService.popupListAll(reqMap);

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("count", totValue);
        map.put("pager", page);    // 페이징 처리를 위한 변수
        map.put("searchOrderBy", "regDt");
        map.put("search_option", search_option);
        map.put("curPage", curPage);
        map.put("keyword", keyword);
        map.put("PAGE_SCALE", PAGE_SCALE);
        mav.addObject("map", map);

        mav.setViewName("sys/bbs/bbsMgmt/popupSetting");
        return mav;
    }

    @RequestMapping("sys/ajaxPopupSetting")
    @ResponseBody
    public HashMap<String, Object> ajaxPopupSetting(@RequestBody PopupDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            if (body.getStartDt() == "" || body.getStartDt() == null) {
                reqMap.put("startday", "19880101");
            } else {
                reqMap.put("startday", body.getStartDt());
            }
            if (body.getEndDt() == "" || body.getEndDt() == null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String today = sdf.format(new Date());
                reqMap.put("endday", today);
            } else {
                reqMap.put("endday", body.getEndDt());
            }
            reqMap.put("searchOption", body.getSearchOption());
            reqMap.put("keyword", body.getKeyword());
            if (body.getOption2().equals("true")) {
                reqMap.put("exposYn", 'Y');
            } else if (body.getOption3().equals("true")) {
                reqMap.put("exposYn", 'N');
            } else {
                reqMap.put("exposYn", 'a');
            }
            reqMap.put("pageScale", body.getPageScale());
            reqMap.put("searchOrderBy", body.getSearchOrderBy());

            // total count
            int totValue = sysAsistService.popupTotalCount(reqMap);
            logger.info("totValue : " + totValue);
            PageVO page = new PageVO(totValue, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();

            reqMap.put("start", start);
            reqMap.put("end", end);

            List<PopupDTO> list = sysAsistService.popupListAll(reqMap);
            ModelAndView mav = new ModelAndView();
            map.put("list", list);
            map.put("count", totValue);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("searchOrderBy", body.getSearchOrderBy());
            map.put("search_option", body.getSearchOption());
            map.put("curPage", body.getCurPage());
            map.put("keyword", body.getKeyword());
            map.put("PAGE_SCALE", body.getPageScale());
            mav.addObject("map", map);

            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    @RequestMapping("sys/popupUpdate")
    public ModelAndView popupUpdate(@RequestParam(defaultValue = "0") long no) throws Exception {
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("no", no);
        PopupDTO dto = sysAsistService.popupDetail(reqMap);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("dto", dto);
        mav.addObject("map", map);
        mav.setViewName("sys/bbs/bbsMgmt/popupUpdate");
        return mav;
    }

    @RequestMapping("sys/popupWrite")
    public ModelAndView popupWrite() throws Exception {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("sys/bbs/bbsMgmt/popupWrite");
        return mav;
    }

    @RequestMapping("sys/exposynCount")
    @ResponseBody
    public HashMap<String, Object> exposynCount(@RequestBody PopupDTO body) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("no", body.getNo());
        try {
            int exposynCount = sysAsistService.exposynCount(reqMap);

            map.put("exposynCount", exposynCount);
            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    @RequestMapping("sys/updateExposeN")
    @ResponseBody
    public HashMap<String, Object> updateExposeN(@RequestBody PopupDTO body) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        try {
            reqMap.put("no", body.getNo());
            sysAsistService.updateExposeN(reqMap);
            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /**
     * 팝업 글쓰기 시작
     *
     * @param multi
     * @return
     * @throws Exception
     */
    @RequestMapping("sys/doPopupWrite")
    @ResponseBody
    public HashMap<String, Object> doPopupWrite(MultipartHttpServletRequest multi) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        try {
            Iterator<String> files = multi.getFileNames();
            while (files.hasNext()) {
                String uploadFile = files.next();
                MultipartFile mFile = multi.getFile(uploadFile);
                String fileName = mFile.getOriginalFilename();
                String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

                Map<String, Object> reqMap = new HashMap<String, Object>();
                reqMap.put("adm_id", multi.getParameter("loginId"));
                reqMap.put("title", multi.getParameter("title"));
                reqMap.put("contents", multi.getParameter("contents"));
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                reqMap.put("exposyn", multi.getParameter("exposyn"));
                reqMap.put("dxpstype", multi.getParameter("dxpstype"));
                reqMap.put("url", multi.getParameter("url"));
                if (multi.getParameter("dxpstype").equals("C")) {
                    reqMap.put("start_dt", "");
                    reqMap.put("end_dt", "");
                } else {
                    reqMap.put("start_dt", multi.getParameter("start_dt").replace(".", ""));
                    reqMap.put("end_dt", multi.getParameter("end_dt").replace(".", ""));
                }
                //서버로 파일 이동
                byte[] byteArr = mFile.getBytes();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write(byteArr);

                FwFile fwFile = new FwFile();
                fwFile.setBucket("BOARD_POPUP");
                fwFile.setName(fileName);

                if (ext.toLowerCase().equals("jpg") || ext.toLowerCase().equals("jpeg")) {
                    fwFile.setMimeType("image/jpeg");
                } else if (ext.toLowerCase().equals("gif")) {
                    fwFile.setMimeType("image/gif");
                } else if (ext.toLowerCase().equals("png")) {
                    fwFile.setMimeType("image/png");
                } else {
                    fwFile.setMimeType("image/bmp");
                }
                fwFile.setLength((long) baos.toByteArray().length);
                fwFile.setCreateDate(new Date());
                fwFile.setCreateUser(user);
                fwFileMapper.insert(fwFile);
                simpleFileMapper.store(fwFile.getBucket(), fwFile.getId(), new ByteArrayInputStream(baos.toByteArray()));

                reqMap.put("filename", fwFile.getId());
                reqMap.put("filesize", mFile.getSize());
                sysAsistService.popupWrite(reqMap);
            }
            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /*
     * 팝업 글 삭제
     */
    @RequestMapping("sys/ajaxPopupDelete")
    @ResponseBody
    public HashMap<String, Object> ajaxPopupDelete(@RequestBody PopupDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("no", body.getNo());

            //해당글 테이블에서 삭제
            sysAsistService.popupDelete(reqMap);
            map.put("retCode", "0000");

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /*
     * 팝업 수정 파일 x
     */
    @RequestMapping("sys/popupUpdateNoFile")
    @ResponseBody
    public HashMap<String, Object> popupUpdateNoFile(@RequestBody PopupDTO body) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();

            reqMap.put("no", body.getNo());
            reqMap.put("title", body.getTitle());
            reqMap.put("contents", body.getContents());
            reqMap.put("dxpstype", body.getDxpstype());
            reqMap.put("exposyn", body.getExposyn());
            reqMap.put("adm_id", body.getAdmId());
            if (body.getDxpstype().equals("C")) {
                reqMap.put("start_dt", "");
                reqMap.put("end_dt", "");
            } else {
                reqMap.put("start_dt", body.getStartDt().replace(".", ""));
                reqMap.put("end_dt", body.getEndDt().replace(".", ""));
            }
            reqMap.put("url", body.getUrl());
            sysAsistService.popupUpdateNofile(reqMap);

            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /*
     * 팝업 수정 파일 o
     */
    @RequestMapping("sys/doPopupUpdate")
    @ResponseBody
    public HashMap<String, Object> doPopupUpdate(MultipartHttpServletRequest multi) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        try {
            reqMap.put("no", multi.getParameter("no"));

            //업로드 되는 파일명
            Iterator<String> files = multi.getFileNames();
            while (files.hasNext()) {
                String uploadFile = files.next();
                MultipartFile mFile = multi.getFile(uploadFile);
                String fileName = mFile.getOriginalFilename();
                String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
                //프로젝트 서버 폴더에 파일 옴기기
                byte[] byteArr = mFile.getBytes();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write(byteArr);

                FwFile fwFile = new FwFile();
                fwFile.setBucket("BOARD_POPUP");
                fwFile.setName(fileName);

                if (ext.toLowerCase().equals("jpg") || ext.toLowerCase().equals("jpeg")) {
                    fwFile.setMimeType("image/jpeg");
                } else if (ext.toLowerCase().equals("gif")) {
                    fwFile.setMimeType("image/gif");
                } else if (ext.toLowerCase().equals("png")) {
                    fwFile.setMimeType("image/png");
                } else {
                    fwFile.setMimeType("image/bmp");
                }
                fwFile.setLength((long) baos.toByteArray().length);
                fwFile.setCreateDate(new Date());
                fwFile.setCreateUser(user);
                fwFileMapper.insert(fwFile);
                simpleFileMapper.store(fwFile.getBucket(), fwFile.getId(), new ByteArrayInputStream(baos.toByteArray()));

                reqMap.put("no", multi.getParameter("no"));
                reqMap.put("title", multi.getParameter("title"));
                reqMap.put("contents", multi.getParameter("contents"));
                reqMap.put("filename", fwFile.getId());
                reqMap.put("filesize", mFile.getSize());
                reqMap.put("dxpstype", multi.getParameter("dxpstype"));
                reqMap.put("exposyn", multi.getParameter("exposyn"));
                reqMap.put("adm_id", multi.getParameter("adm_id"));
                reqMap.put("url", multi.getParameter("url"));

                if (multi.getParameter("dxpstype").equals("C")) {
                    reqMap.put("start_dt", "");
                    reqMap.put("end_dt", "");
                } else {
                    reqMap.put("start_dt", multi.getParameter("start_dt").replace(".", ""));
                    reqMap.put("end_dt", multi.getParameter("end_dt").replace(".", ""));
                }
                sysAsistService.popupUpdateWithFile(reqMap);
            }
            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /**
     * 공지사항 권한에 따른 bbs 값
     *
     * @param option1
     * @param option2
     * @param option3
     * @return
     */
    public int noticeRollByBbs(String option1, String option2, String option3) {
        int bbs;
        if (option1 == "true" && option2 == "false" && option3 == "false") {
            bbs = 7;
        } else if (option1 == "false" && option2 == "true" && option3 == "false") {
            bbs = 8;
        } else if (option1 == "false" && option2 == "false" && option3 == "true") {
            bbs = 6;
        } else if (option1 == "true" && option2 == "true" && option3 == "false") {
            bbs = 11;
        } else if (option1 == "true" && option2 == "false" && option3 == "true") {
            bbs = 9;
        } else if (option1 == "false" && option2 == "true" && option3 == "true") {
            bbs = 10;
        } else if (option1 == "true" && option2 == "true" && option3 == "true") {
            bbs = 9999;
        } else {
            bbs = 9990;
        }
        return bbs;
    }

    @RequestMapping("sys/popup")
    public ModelAndView popup() throws Exception {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("sys/bbs/bbsMgmt/popup");
        return mav;
    }

    /**
     * 공지사항 엑셀 다운로드
     *
     * @param request
     * @param response
     * @param body
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("sys/excelSaveNotice")
    public ExcelSaveNotice excelSaveNotice(HttpServletRequest request, HttpServletResponse response, SysDTO body, Model model) throws Exception {
        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("search_option", body.getExSearchOption());
            reqMap.put("keyword", body.getExKeyword());
            reqMap.put("startday", body.getExStartday());
            reqMap.put("endday", body.getExEndday());
            int bbs = noticeRollByBbs(body.getExOption1(), body.getExOption2(), body.getExOption3());
            reqMap.put("bbs", bbs);
            reqMap.put("start", 1);
            reqMap.put("end", body.getExCount());

            List<SysDTO> list = sysAsistService.noticeListAll(reqMap);

            model.addAttribute("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return new ExcelSaveNotice();
    }

    /**
     * faq 엑셀 다운로드
     *
     * @param request
     * @param response
     * @param body
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("sys/excelSaveFaq")
    public ExcelSaveFaq excelSaveFaq(HttpServletRequest request, HttpServletResponse response, SysDTO body, Model model) throws Exception {
        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();

            reqMap.put("search_option", body.getExSearchOption());
            reqMap.put("keyword", body.getExKeyword());

            //List<String> daylist = startEndDay(body.getExStartday(), body.getExEndday());
            reqMap.put("startday", body.getExStartday());
            reqMap.put("endday", body.getExEndday());
            reqMap.put("start", 1);
            reqMap.put("end", body.getExCount());


            //bbs 설정
            String role = "";
            if (body.getExOption0() == "ture" && body.getExOption1() == "false" && body.getExOption2() == "false" && body.getExOption3() == "false") {
                role = "all";
            } else if (body.getExOption1() == "true" && body.getExOption2() == "false" && body.getExOption3() == "false") {
                role = "login";
            } else if (body.getExOption1() == "false" && body.getExOption2() == "true" && body.getExOption3() == "false") {
                role = "org";
            } else if (body.getExOption1() == "false" && body.getExOption2() == "false" && body.getExOption3() == "true") {
                role = "submit";
            } else if (body.getExOption1() == "true" && body.getExOption2() == "true" && body.getExOption3() == "false") {
                role = "lo";
            } else if (body.getExOption1() == "true" && body.getExOption2() == "false" && body.getExOption3() == "true") {
                role = "ls";
            } else if (body.getExOption1() == "false" && body.getExOption2() == "true" && body.getExOption3() == "true") {
                role = "os";
            } else if (body.getExOption1() == "true" && body.getExOption2() == "true" && body.getExOption3() == "true") {
                role = "all";
            } else {
                role = "all";
            }

            reqMap.put("role", role);
            reqMap.put("category", body.getExCategory());

            List<SysDTO> list = sysAsistService.faqListAll(reqMap);

            model.addAttribute("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return new ExcelSaveFaq();
    }

    /**
     * qna 엑셀 다운로드
     *
     * @param request
     * @param response
     * @param body
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("sys/excelSaveQna")
    public ExcelSaveQna excelSaveQna(HttpServletRequest request, HttpServletResponse response, SysDTO body, Model model) throws Exception {
        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();

            reqMap.put("search_option", body.getExSearchOption());
            reqMap.put("keyword", body.getExKeyword());

            //List<String> daylist = startEndDay(body.getExStartday(), body.getExEndday());
            reqMap.put("startday", body.getExStartday());
            reqMap.put("endday", body.getExEndday());
            reqMap.put("start", 1);
            reqMap.put("end", body.getExCount());


            //bbs 설정
            String role = "";
            if (body.getExOption0() == "ture" && body.getExOption1() == "false" && body.getExOption2() == "false" && body.getExOption3() == "false") {
                role = "all";
            } else if (body.getExOption1() == "true" && body.getExOption2() == "false" && body.getExOption3() == "false") {
                role = "login";
            } else if (body.getExOption1() == "false" && body.getExOption2() == "true" && body.getExOption3() == "false") {
                role = "org";
            } else if (body.getExOption1() == "false" && body.getExOption2() == "false" && body.getExOption3() == "true") {
                role = "submit";
            } else if (body.getExOption1() == "true" && body.getExOption2() == "true" && body.getExOption3() == "false") {
                role = "lo";
            } else if (body.getExOption1() == "true" && body.getExOption2() == "false" && body.getExOption3() == "true") {
                role = "ls";
            } else if (body.getExOption1() == "false" && body.getExOption2() == "true" && body.getExOption3() == "true") {
                role = "os";
            } else if (body.getExOption1() == "true" && body.getExOption2() == "true" && body.getExOption3() == "true") {
                role = "all";
            } else {
                role = "all";
            }

            reqMap.put("role", role);
            reqMap.put("category", body.getExCategory());
            reqMap.put("code", body.getExChacode());
            reqMap.put("chaname", body.getExChaname());

            List<SysDTO> list = sysAsistService.qnaListAll(reqMap);

            model.addAttribute("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return new ExcelSaveQna();
    }

    //단일파일업로드
    @RequestMapping("/noticePhotoUpload")
    public String photoUpload(HttpServletRequest request, SysDTO vo, @RequestParam("BNM") String bucket) throws Exception {
        String callback = vo.getCallback();
        String callback_func = vo.getCallback_func();
        String file_result = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        try {
            if (vo.getFiledata() != null && vo.getFiledata().getOriginalFilename() != null && !vo.getFiledata().getOriginalFilename().equals("")) {
                //파일이 존재하면
                String original_name = vo.getFiledata().getOriginalFilename();
                String ext = original_name.substring(original_name.lastIndexOf(".") + 1);

                MultipartFile file = vo.getFiledata();
                ///////////////// 서버에 파일쓰기 /////////////////
                byte[] byteArr = file.getBytes();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write(byteArr);

                FwFile fwFile = new FwFile();
                if (bucket.equals("notice")) {
                    fwFile.setBucket("BOARD_NOTICE");
                } else {
                    fwFile.setBucket("BOARD_FAQ");
                }
                fwFile.setName(original_name);

                if (ext.toLowerCase().equals("jpg") || ext.toLowerCase().equals("jpeg")) {
                    fwFile.setMimeType("image/jpeg");
                } else if (ext.toLowerCase().equals("gif")) {
                    fwFile.setMimeType("image/gif");
                } else if (ext.toLowerCase().equals("png")) {
                    fwFile.setMimeType("image/png");
                } else {
                    fwFile.setMimeType("image/bmp");
                }
                fwFile.setLength((long) baos.toByteArray().length);
                fwFile.setCreateDate(new Date());
                fwFile.setCreateUser(user);
                fwFileMapper.insert(fwFile);

                simpleFileMapper.store(fwFile.getBucket(), fwFile.getId(), new ByteArrayInputStream(baos.toByteArray()));

                file_result += "&bNewLine=true";
                // img 태그의 title 속성을 원본파일명으로 적용시켜주기 위함
                file_result += "&sFileName=" + original_name;
                if (bucket.equals("notice")) {
                    file_result += "&sFileURL=" + "/sys/editorStream/Notice?id=" + fwFile.getId();
                } else {
                    file_result += "&sFileURL=" + "/sys/editorStream/Faq?id=" + fwFile.getId();
                }
            } else {
                file_result += "&errstr=error";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "redirect:" + callback + "?callback_func=" + callback_func + file_result;
    }

    //다중파일업로드
    @ResponseBody
    @RequestMapping("/noticeMultiplePhotoUpload")
    public void multiplePhotoUpload(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        try {
            MultipartFile inputFile = request.getFile("file");
            String bucketInfo = request.getParameter("bucket");
            String sFileInfo = "";
            //파일명을 받는다 - 일반 원본파일명
            String filename = inputFile.getOriginalFilename();
            String ext = filename.substring(filename.lastIndexOf(".") + 1);

            byte[] byteArr = inputFile.getBytes();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(byteArr);

            FwFile fwFile = new FwFile();
            if (bucketInfo.equals("notice")) {
                fwFile.setBucket("BOARD_NOTICE");
            } else {
                fwFile.setBucket("BOARD_FAQ");
            }
            fwFile.setName(filename);

            if (ext.toLowerCase().equals("jpg") || ext.toLowerCase().equals("jpeg")) {
                fwFile.setMimeType("image/jpeg");
            } else if (ext.toLowerCase().equals("gif")) {
                fwFile.setMimeType("image/gif");
            } else if (ext.toLowerCase().equals("png")) {
                fwFile.setMimeType("image/png");
            } else {
                fwFile.setMimeType("image/bmp");
            }
            fwFile.setLength((long) baos.toByteArray().length);
            fwFile.setCreateDate(new Date());
            fwFile.setCreateUser(user);
            fwFileMapper.insert(fwFile);

            simpleFileMapper.store(fwFile.getBucket(), fwFile.getId(), new ByteArrayInputStream(baos.toByteArray()));
//				///////////////// 서버에 파일쓰기 /////////////////
            // 정보 출력
            sFileInfo += "&bNewLine=true";
            // img 태그의 title 속성을 원본파일명으로 적용시켜주기 위함
            sFileInfo += "&sFileName=" + filename;
            if (bucketInfo.equals("notice")) {
                sFileInfo += "&sFileURL=" + "/sys/editorStream?id=" + fwFile.getId();
            } else {
                sFileInfo += "&sFileURL=" + "/sys/editorStream/Faq?id=" + fwFile.getId();
            }
            PrintWriter print = response.getWriter();
            print.print(sFileInfo);
            print.flush();
            print.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 배너관리 > 배너
     */
    @RequestMapping("sys/banner")
    public ModelAndView banner() throws Exception {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("sys/bbs/bbsMgmt/banner");
        return mav;
    }

    /**
     * 배너 관리 > 목록
     */
    @RequestMapping("sys/bannerSetting")
    public ModelAndView bannerSetting(@RequestParam(defaultValue = "all") String search_option,
                                      @RequestParam(defaultValue = "") String keyword,
                                      @RequestParam(defaultValue = "1") int curPage,
                                      @RequestParam(defaultValue = "10") int PAGE_SCALE) throws Exception {
        Map<String, Object> reqMap = new HashMap<String, Object>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(new Date());

        reqMap.put("search_option", search_option);
        reqMap.put("keyword", keyword);
        reqMap.put("startDate", "19880101");
        reqMap.put("endDate", today);

        int totValue = sysAsistService.bannerTotalCount(reqMap);
        PageVO page = new PageVO(totValue, curPage, PAGE_SCALE);
        int start = page.getPageBegin();
        int end = page.getPageEnd();

        reqMap.put("searchOrderBy", "createDate");
        reqMap.put("start", start);
        reqMap.put("end", end);

        List<BannerDTO> list = sysAsistService.bannerListAll(reqMap);
        for (BannerDTO each : list) {
            if ("".equals(each.getTitle()) || each.getTitle() == "" || each.getTitle() == null) {
            } else {
                each.setTitle(each.getTitle().replaceAll("<br/>", "\r\n"));
            }

            if ("".equals(each.getContent()) || each.getContent() == "" || each.getContent() == null) {
            } else {
                each.setContent(each.getContent().replaceAll("<br/>", "\r\n"));
            }
        }

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("count", totValue);
        map.put("pager", page);
        map.put("searchOrderBy", "createDate");
        map.put("search_option", search_option);
        map.put("curPage", curPage);
        map.put("keyword", keyword);
        map.put("PAGE_SCALE", PAGE_SCALE);
        mav.addObject("map", map);

        mav.setViewName("sys/bbs/bbsMgmt/bannerSetting");
        return mav;
    }

    @RequestMapping("sys/ajaxBannerSetting")
    @ResponseBody
    public HashMap<String, Object> ajaxBannerSetting(@RequestBody BannerDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            if ("".equals(body.getStartDate()) || body.getStartDate() == null) {
                reqMap.put("startDate", "19880101");
            } else {
                reqMap.put("startDate", body.getStartDate());
            }

            if ("".equals(body.getEndDate()) || body.getEndDate() == null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String today = sdf.format(new Date());
                reqMap.put("endDate", today);
            } else {
                reqMap.put("endDate", body.getEndDate());
            }

            reqMap.put("searchOption", body.getSearchOption());
            reqMap.put("keyword", body.getKeyword());
            reqMap.put("showYn", body.getShowYn());
            reqMap.put("viewType", body.getViewType());
            reqMap.put("searchOrderBy", body.getSearchOrderBy());
            reqMap.put("pageScale", body.getPageScale());

            // total count
            int totValue = sysAsistService.bannerTotalCount(reqMap);
            PageVO page = new PageVO(totValue, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();

            reqMap.put("start", start);
            reqMap.put("end", end);

            List<BannerDTO> list = sysAsistService.bannerListAll(reqMap);
            for (BannerDTO each : list) {
                if ("".equals(each.getTitle()) || each.getTitle() == "" || each.getTitle() == null) {
                } else {
                    each.setTitle(each.getTitle().replaceAll("<br/>", "\r\n"));
                }

                if ("".equals(each.getContent()) || each.getContent() == "" || each.getContent() == null) {
                } else {
                    each.setContent(each.getContent().replaceAll("<br/>", "\r\n"));
                }
            }

            ModelAndView mav = new ModelAndView();
            map.put("list", list);
            map.put("count", totValue);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("search_orderBy", body.getSearchOrderBy());
            map.put("search_option", body.getSearchOption());
            map.put("curPage", body.getCurPage());
            map.put("keyword", body.getKeyword());
            map.put("PAGE_SCALE", body.getPageScale());
            mav.addObject("map", map);

            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /**
     * 배너 관리 > 글쓰기
     */
    @RequestMapping("sys/bannerWrite")
    public ModelAndView bannerWrite() throws Exception {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("sys/bbs/bbsMgmt/bannerWrite");
        return mav;
    }

    /**
     * 배너 관리 > 배너 파일 등록
     */
    @RequestMapping("sys/doBannerWrite")
    @ResponseBody
    public HashMap<String, Object> doBannerWrite(MultipartHttpServletRequest multi) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> reqMap = new HashMap<String, Object>();

        try {
            Iterator<String> files = multi.getFileNames();

            while (files.hasNext()) {
				/* FW_FILE 테이블에 INSERT */
                String uploadFile = files.next();
                MultipartFile mFile = multi.getFile(uploadFile);
                String fileName = mFile.getOriginalFilename();
                String chaCd = multi.getParameter("loginId");

                byte[] byteArr = mFile.getBytes();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write(byteArr);

                // 확장자 추출
                int pos = fileName.lastIndexOf(".");
                String ext = fileName.substring(pos);

                FwFile fwFile = new FwFile();
                fwFile.setBucket("BANNER");
                fwFile.setName(fileName);
                if (ext.toLowerCase().equals(".jpg")) {
                    fwFile.setMimeType("image/jpeg");
                } else if (ext.toLowerCase().equals(".png")) {
                    fwFile.setMimeType("image/png");
                }
                fwFile.setLength((long) baos.toByteArray().length);
                fwFile.setCreateDate(new Date());
                fwFile.setCreateUser(chaCd);
                fwFileMapper.insert(fwFile);

                simpleFileMapper.store(fwFile.getBucket(), fwFile.getId(), new ByteArrayInputStream(baos.toByteArray()));

				/* BANNER 테이블 INSERT */
                if ("S10001".equals(multi.getParameter("viewTypeCd"))) {
                    // 2025-10-24 조성운 추가  사유 : 20251024 취약점 지적사항
                    // ===== XSS 필터링 적용 =====
                    String name = multi.getParameter("pcName");
                    String title = multi.getParameter("pcTitle");
                    String content = multi.getParameter("pcContent");

                    // XSS 방어: 위험한 태그 및 스크립트 제거
                    if (name != null && !name.isEmpty()) {
                        name = SimpleXSSSanitizer.sanitizeEnhanced(name);
                    }

                    if (title != null && !title.isEmpty()) {
                        title = SimpleXSSSanitizer.sanitizeEnhanced(title);
                        title = title.replaceAll("\r\n", "<br/>");
                    }

                    if (content != null && !content.isEmpty()) {
                        content = SimpleXSSSanitizer.sanitizeEnhanced(content);
                        content = content.replaceAll("\r\n", "<br/>");
                    }
                    // =============================

                    // PC용 저장
                    reqMap.put("name", name);
                    reqMap.put("title", title);
                    reqMap.put("content", content);
                    reqMap.put("fileId", fwFile.getId());
                    reqMap.put("showYn", multi.getParameter("showPcYN"));
                    reqMap.put("createUser", multi.getParameter("loginId"));
                    reqMap.put("modifyUser", multi.getParameter("loginId"));
                    reqMap.put("viewTypeCd", "S10001");
                    reqMap.put("orderNo", Integer.parseInt(multi.getParameter("orderNo")));
                } else if ("S10002".equals(multi.getParameter("viewTypeCd"))) {
                    // 모바일용 저장
                    // 2025-10-24 조성운 추가  사유 : 20251024 취약점 지적사항
                    // ===== XSS 필터링 적용 =====
                    String name = multi.getParameter("mobileName");
                    String title = multi.getParameter("mobileTitle");
                    String content = multi.getParameter("mobileContent");

                    // XSS 방어: 위험한 태그 및 스크립트 제거
                    if (name != null && !name.isEmpty()) {
                        name = SimpleXSSSanitizer.sanitizeEnhanced(name);
                    }

                    if (title != null && !title.isEmpty()) {
                        title = SimpleXSSSanitizer.sanitizeEnhanced(title);
                        title = title.replaceAll("\r\n", "<br/>");
                    }

                    if (content != null && !content.isEmpty()) {
                        content = SimpleXSSSanitizer.sanitizeEnhanced(content);
                        content = content.replaceAll("\r\n", "<br/>");
                    }
                    // =============================

                    // 모바일용 저장
                    reqMap.put("name", name);
                    reqMap.put("title", title);
                    reqMap.put("content", content);
                    reqMap.put("fileId", fwFile.getId());
                    reqMap.put("showYn", multi.getParameter("showMobileYN"));
                    reqMap.put("createUser", multi.getParameter("loginId"));
                    reqMap.put("modifyUser", multi.getParameter("loginId"));
                    reqMap.put("viewTypeCd", "S10002");
                    reqMap.put("orderNo", Integer.parseInt(multi.getParameter("orderNo")));
                }

                sysAsistService.bannerWrite(reqMap);
            }
            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /**
     * 배너 수정
     */
    @RequestMapping("sys/bannerUpdate")
    public ModelAndView bannerUpdate(@RequestParam(defaultValue = "0") long id) throws Exception {
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("id", String.valueOf(id));
        BannerDTO list = sysAsistService.bannerDetail(reqMap);

        String title;
        if ("".equals(list.getTitle()) || list.getTitle() == "" || list.getTitle() == null) {
            title = list.getTitle();
        } else {
            title = list.getTitle().replaceAll("<br/>", "\r\n");
        }
        list.setTitle(title);

        String content;
        if ("".equals(list.getContent()) || list.getContent() == "" || list.getContent() == null) {
            content = list.getContent();
        } else {
            content = list.getContent().replaceAll("<br/>", "\r\n");
        }
        list.setContent(content);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        mav.addObject("map", map);
        mav.setViewName("sys/bbs/bbsMgmt/bannerUpdate");
        return mav;
    }

    @RequestMapping("sys/doBannerUpdate")
    @ResponseBody
    public HashMap<String, Object> doBannerUpdate(MultipartHttpServletRequest multi) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        try {
            Long id = Long.parseLong(multi.getParameter("id"));
            reqMap.put("id", String.valueOf(id));
            BannerDTO list = sysAsistService.bannerDetail(reqMap);

            Iterator<String> files = multi.getFileNames();

            String listFileName = list.getFileName();
            String multiFileName = multi.getParameter("fileName");

            if (!listFileName.equals(multiFileName)) {
                // 파일을 새로 등록 할 경우
                // FW_FILE 테이블 파일 삭제 후 등록
                String uploadFile = files.next();
                MultipartFile mFile = multi.getFile(uploadFile);
                String fileName = mFile.getOriginalFilename();
                String chaCd = multi.getParameter("loginId");
                String fileId = multi.getParameter("fileId");

                fwFileMapper.deleteByPrimaryKey(fileId);

                byte[] byteArr = mFile.getBytes();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write(byteArr);

                // 확장자 추출
                int pos = fileName.lastIndexOf(".");
                String ext = fileName.substring(pos);

                FwFile fwFile = new FwFile();
                fwFile.setBucket("BANNER");
                fwFile.setName(fileName);
                if (ext.toLowerCase().equals(".jpg")) {
                    fwFile.setMimeType("image/jpeg");
                } else if (ext.toLowerCase().equals(".png")) {
                    fwFile.setMimeType("image/png");
                }
                fwFile.setLength((long) baos.toByteArray().length);
                fwFile.setCreateDate(new Date());
                fwFile.setCreateUser(chaCd);
                fwFileMapper.insert(fwFile);

                simpleFileMapper.store(fwFile.getBucket(), fwFile.getId(), new ByteArrayInputStream(baos.toByteArray()));

                reqMap.put("fileId", fwFile.getId());
            }

            // 2025-10-24 조성운 추가  사유 : 20251024 취약점 지적사항
            /* BANNER 테이블 UPDATE */

            // ===== XSS 필터링 적용 =====
            String name = multi.getParameter("name");
            String title = multi.getParameter("title");
            String content = multi.getParameter("content");

            // name 필터링
            if (name != null && !name.isEmpty()) {
                name = SimpleXSSSanitizer.sanitizeEnhanced(name);
            }

            // title 필터링
            if (title != null && !title.isEmpty()) {
                title = SimpleXSSSanitizer.sanitizeEnhanced(title);
                title = title.replaceAll("\r\n", "<br/>");
            } else {
                title = "";
            }

            // content 필터링
            if (content != null && !content.isEmpty()) {
                content = SimpleXSSSanitizer.sanitizeEnhanced(content);
                content = content.replaceAll("\r\n", "<br/>");
            } else {
                content = "";
            }
            // =============================

            reqMap.put("name", name);
            reqMap.put("title", title);
            reqMap.put("content", content);

            reqMap.put("showYn", multi.getParameter("showYn"));
            reqMap.put("viewTypeCd", multi.getParameter("viewTypeCd"));
            reqMap.put("modifyUser", multi.getParameter("loginId"));
            reqMap.put("orderNo", Integer.parseInt(multi.getParameter("orderNo")));

            sysAsistService.bannerUpdate(reqMap);

            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /**
     * 배너 삭제
     */
    @RequestMapping("sys/doBannerDelete")
    @ResponseBody
    public HashMap<String, Object> doBannerDelete(@RequestBody BannerDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        int bannerTypeCount = 0;

        try {
            reqMap.put("id", String.valueOf(body.getId()));
            BannerDTO dto = sysAsistService.bannerDetail(reqMap);

            reqMap.put("viewTypeCd", dto.getViewTypeCd());
            bannerTypeCount = sysAsistService.bannerTypeCount(reqMap);

            if (bannerTypeCount < 2) {
                map.put("retCode", "0001");
            } else {
                // FW_FILE 테이블 파일 삭제
                fwFileMapper.deleteByPrimaryKey(dto.getFileId());

                // BANNER 테이블 삭제
                sysAsistService.bannerDelete(reqMap);
                map.put("retCode", "0000");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /**
     * 배너 내용
     */
    @RequestMapping("sys/bannerInfo")
    @ResponseBody
    public HashMap<String, Object> bannerInfo(@RequestBody BannerDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        reqMap.put("fileId", body.getFileId());
        BannerDTO list = sysAsistService.bannerInfo(reqMap);

        ModelAndView mav = new ModelAndView();
        map.put("list", list);
        mav.addObject("getList", map);
        map.put("retCode", "0000");
        return map;
    }

    /**
     * 배너 이미지 불러오기
     */
    @RequestMapping(value = "sys/banner/image", method = RequestMethod.GET)
    public void getBannerImg(@RequestParam(value = "id") String id, HttpServletResponse response) throws Exception {
        FwFile fwFile = sysAsistService.getFileInfo(id);
        InputStream is = sysAsistService.getFile(id);

        response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fwFile.getLength()));
        response.setHeader(HttpHeaders.CONTENT_TYPE, fwFile.getMimeType());

        IOUtils.copy(is, response.getOutputStream());
    }

    /**
     * 배너 노출순서 확인
     */
    @RequestMapping("sys/orderCheck")
    @ResponseBody
    public HashMap<String, Object> orderCheck(@RequestBody BannerDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        int orderCheck = 0;

        try {
            reqMap.put("viewTypeCd", body.getViewTypeCd());
            reqMap.put("orderNo", body.getOrderNo());
            orderCheck = sysAsistService.orderCheck(reqMap);

            if (orderCheck == 0) {
                // 사용가능
                map.put("retCode", "0000");
            } else {
                // 사용불가
                map.put("retCode", "0001");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }
}
