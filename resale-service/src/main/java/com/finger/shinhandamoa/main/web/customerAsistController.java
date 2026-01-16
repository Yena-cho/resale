package com.finger.shinhandamoa.main.web;

import java.io.File;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.main.dto.NoticeDTO;
import com.finger.shinhandamoa.main.service.CustomerAsistService;
import com.finger.shinhandamoa.vo.PageVO;

/**
 * @author by puki
 * @date 2018. 3. 30.
 * @desc 최초생성
 */
@Controller
@RequestMapping("common/customerAsist/**")
public class customerAsistController {

    private static final Logger logger = LoggerFactory.getLogger(customerAsistController.class);

    @Inject
    private CustomerAsistService customerAsistService;

    // 업로드 디렉토리
    @Value("${file.path.upload}")
    private String uploadPath;

    /*
     * 고객지원 > 공지사항
     */
    @RequestMapping("noticeList")
    public ModelAndView NoticeList(@RequestParam(defaultValue = "day") String search_orderBy,
                                   @RequestParam(defaultValue = "all") String search_option,
                                   @RequestParam(defaultValue = "") String keyword,
                                   @RequestParam(defaultValue = "1") int curPage,
                                   @RequestParam(defaultValue = "10") int PAGE_SCALE,
                                   @RequestParam(defaultValue = "7") int bbs) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().toString();    // 권한 Collection

        if (role.contains("ROLE_ADMIN")) {            // 기관관리자
            bbs = 8;
        } else if (role.contains("ROLE_USER")) {    // 납부자
            bbs = 6;
        } else {
            bbs = 7;
        }

        Map<String, Object> reqMap = new HashMap<String, Object>();

        reqMap.put("search_option", search_option);
        reqMap.put("keyword", keyword);
        reqMap.put("bbs", bbs);

        // total count
        int totValue = customerAsistService.noticeTotalCount(reqMap);
        // only total count
        int onlyCount = customerAsistService.noticeOnlyCount(reqMap);

        // 페이지 관련 설정
        PageVO page = new PageVO(totValue, curPage, PAGE_SCALE);
        int start = page.getPageBegin();
        int end = page.getPageEnd();

        reqMap.put("search_orderBy", search_orderBy);
        reqMap.put("search_option", search_option);
        reqMap.put("keyword", keyword);
        reqMap.put("start", start);
        reqMap.put("end", end);

        List<NoticeDTO> list = customerAsistService.noticeListAll(reqMap);

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("count", onlyCount);
        map.put("pager", page);    // 페이징 처리를 위한 변수
        map.put("bbs", bbs);
        map.put("curPage", curPage);
        mav.addObject("map", map);

        mav.setViewName("common/customerAsist/noticeList");

        return mav;
    }


    @RequestMapping("ajaxNoticeList")
    @ResponseBody
    public HashMap<String, Object> AjaxNoticeList(@RequestBody NoticeDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {

            Map<String, Object> reqMap = new HashMap<String, Object>();

            reqMap.put("keyword", body.getKeyWord());
            reqMap.put("searcgOrderBy", body.getSearchOrderBy());
            reqMap.put("search_option", body.getSearchOption());
            reqMap.put("keyword", body.getKeyWord());

            int bbs;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String role = authentication.getAuthorities().toString();    // 권한 Collection

            if (role.contains("ROLE_ADMIN")) {            // 기관관리자
                bbs = 8;
            } else if (role.contains("ROLE_USER")) {    // 납부자
                bbs = 6;
            } else {
                bbs = 7;
            }
            reqMap.put("bbs", bbs);

            // total count
            int totValue = customerAsistService.noticeTotalCount(reqMap);
            //only count
            int onlyCount = customerAsistService.noticeOnlyCount(reqMap);

            // 페이지 관련 설정
            PageVO page = new PageVO(totValue, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            reqMap.put("start", start);
            reqMap.put("end", end);
            List<NoticeDTO> list = customerAsistService.ajaxNoticeListAll(reqMap);
            map.put("list", list);
            map.put("count", onlyCount);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("search_orderBy", body.getSearchOrderBy());
            map.put("search_option", body.getSearchOption());
            map.put("curPage", body.getCurPage());
            map.put("keyword", body.getKeyWord());
            map.put("PAGE_SCALE", body.getPageScale());
            map.put("bbs", body.getBbs());
            map.put("retCode", "0000");

        } catch (Exception e) {
            logger.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }


    @RequestMapping("noticeDetail")
    public ModelAndView NoticeDetail(@RequestParam(defaultValue = "") long no,
                                     @RequestParam(defaultValue = "7") int bbs, @RequestParam(defaultValue = "1") int curPage) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().toString();    // 권한 Collection

        if (role.contains("ROLE_ADMIN")) {            // 기관관리자
            bbs = 8;
        } else if (role.contains("ROLE_USER")) {    // 납부자
            bbs = 6;
        } else {
            bbs = 7;
        }

        //본글 가져오기
        NoticeDTO dto = customerAsistService.noticeDetail(no);
//		if(dto.getIshtml() != 1) {
//			dto.setContents(dto.getContents().replaceAll("<", "&lt;"));
//			dto.setContents(dto.getContents().replaceAll(">", "&gt;"));
//			dto.setContents(dto.getContents().replaceAll("\r\n", "<BR>"));
//			dto.setContents(dto.getContents().replaceAll("\u0020", "&nbsp;"));
//		}else{
//			dto.setContents(dto.getContents().replaceAll("<pre>", "<br>"));
//			dto.setContents(dto.getContents().replaceAll("<br>", "\r\n"));
//			dto.setContents(dto.getContents().replaceAll("<", "&lt;"));
//			dto.setContents(dto.getContents().replaceAll(">", "&gt;"));
//			dto.setContents(dto.getContents().replaceAll("&nbsp;", "\u0020"));
//		}

        // 이전글, 다음글
        List<NoticeDTO> list = customerAsistService.nextList(no, bbs, dto.getIsfix());

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("curPage", curPage);
        mav.addObject("map", map);
        mav.addObject("dto", dto);
        mav.setViewName("common/customerAsist/noticeDetail");

        return mav;
    }

    /*
     * 고객지원 > 자주하는 질문
     */
    @RequestMapping("faqList")
    public ModelAndView FaqList() throws Exception {

        ModelAndView mav = new ModelAndView();

        mav.setViewName("common/customerAsist/faqList");

        return mav;
    }

    @RequestMapping("ajaxFaqList")
    @ResponseBody
    public HashMap<String, Object> FaqList(@RequestBody NoticeDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> reqMap = new HashMap<String, Object>();
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String role = authentication.getAuthorities().toString().replace("[", "").replace("]", "");

            //로그인 정보를 받아서 기관 코드 및 bbs 값에 해당하는 자주하는 질문
            int bbs = body.getBbs();
            String isLogin = "n";
            if (isLogin == "y") {
                bbs = 8;
            } else {
                bbs = 5555;
            }

            reqMap.put("role", role);
            reqMap.put("bbs", bbs);
            reqMap.put("search_option", body.getSearchOption());
            reqMap.put("keyword", body.getKeyWord());
            reqMap.put("icon", body.getIcon());

            // total count
            int count = customerAsistService.faqTotalCount(reqMap);
            // 페이지 관련 설정
            PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();

            reqMap.put("search_orderBy", body.getSearchOrderBy());
            reqMap.put("start", start);
            reqMap.put("end", end);

            //noticedto 로 수정 테이블 하나로 같이씀
            List<NoticeDTO> list = customerAsistService.faqListAll(reqMap);

            map.put("list", list);
            map.put("count", count);
            map.put("pager", page);
            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return map;
    }

    /*
     * 고객지원 > 고객센터
     */
    @RequestMapping("customerCenter")
    public String CustomerCenterPage() {

        return "common/customerAsist/customerCenter";
    }

    /*
     * QnA리스트
     */
    @RequestMapping("qnaList")
    public ModelAndView qnalist(@RequestParam(defaultValue = "day") String search_orderBy,
                                @RequestParam(defaultValue = "all") String search_option,
                                @RequestParam(defaultValue = "") String keyword,
                                @RequestParam(defaultValue = "1") int curPage,
                                @RequestParam(defaultValue = "10") int PAGE_SCALE,
                                @RequestParam(defaultValue = "10") int bbs) throws Exception {

        Map<String, Object> reqMap = new HashMap<String, Object>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String code = authentication.getName();

        //로그인 정보에서 기관 코드 받아서 넣어야함
        reqMap.put("code", code);
        reqMap.put("search_option", search_option);
        reqMap.put("keyword", keyword);
        reqMap.put("bbs", bbs);

        // total count
        int totValue = customerAsistService.qnaTotalCount(reqMap);

        // 페이지 관련 설정
        PageVO page = new PageVO(totValue, curPage, PAGE_SCALE);
        int start = page.getPageBegin();
        int end = page.getPageEnd();

        reqMap.put("search_orderBy", search_orderBy);
        reqMap.put("search_option", search_option);
        reqMap.put("keyword", keyword);
        reqMap.put("start", start);
        reqMap.put("end", end);

        List<NoticeDTO> list = customerAsistService.qnaListAll(reqMap);


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
        map.put("bbs", bbs);
        mav.addObject("map", map);
        mav.setViewName("common/customerAsist/qnaList");

        return mav;
    }

    /*
     * ajaxQnA리스트
     */
    @RequestMapping("ajaxqnaList")
    @ResponseBody
    public HashMap<String, Object> AjaxQnaList(@RequestBody NoticeDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {

            Map<String, Object> reqMap = new HashMap<String, Object>();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String code = authentication.getName();

            reqMap.put("search_option", body.getSearchOption());
            reqMap.put("keyword", body.getKeyWord());
            reqMap.put("bbs", 10);
            reqMap.put("code", code);

            // total count
            int totValue = customerAsistService.qnaTotalCount(reqMap);

            // 페이지 관련 설정
            PageVO page = new PageVO(totValue, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();

            reqMap.put("search_orderBy", body.getSearchOrderBy());
            reqMap.put("search_option", body.getSearchOption());
            reqMap.put("start", start);
            reqMap.put("end", end);

            List<NoticeDTO> list = customerAsistService.qnaListAll(reqMap);

            map.put("list", list);
            map.put("count", totValue);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("search_orderBy", body.getSearchOrderBy());
            map.put("search_option", body.getSearchOption());
            map.put("curPage", body.getCurPage());
            map.put("keyword", body.getKeyWord());
            map.put("PAGE_SCALE", body.getPageScale());
            map.put("bbs", body.getBbs());
            map.put("retCode", "0000");

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /*
     * QnA 상세
     */
    @RequestMapping("qnaDetail")
    public ModelAndView qnaDetail(@RequestParam(defaultValue = "") long no,
                                  @RequestParam(defaultValue = "10") int bbs,
                                  HttpServletRequest request) throws Exception {
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String code = authentication.getName();

        reqMap.put("no", no);
        reqMap.put("bbs", bbs);
        reqMap.put("code", code);
        // 이전글, 다음글
        List<NoticeDTO> list = customerAsistService.qnaNextList(reqMap);
        for (NoticeDTO nlist : list) {
            nlist.setContents(StringEscapeUtils.escapeHtml4(nlist.getContents()));
            nlist.setTitle(StringEscapeUtils.escapeHtml4(nlist.getTitle()));
            nlist.setContents(nlist.getContents().replaceAll("\u0020", "&nbsp;"));
            nlist.setTitle(nlist.getTitle().replaceAll("\u0020", "&nbsp;"));
            nlist.setContents(nlist.getContents().replaceAll("\r\n", "<BR>"));
            nlist.setTitle(nlist.getTitle().replaceAll("\r\n", "<BR>"));
        }
        // 답변글 가져오기
        List<NoticeDTO> repleList = customerAsistService.repleList(reqMap);
        for (NoticeDTO rlist : repleList) {
            rlist.setContents(StringEscapeUtils.escapeHtml4(rlist.getContents()));
            rlist.setTitle(StringEscapeUtils.escapeHtml4(rlist.getTitle()));
            rlist.setContents(rlist.getContents().replaceAll("\u0020", "&nbsp;"));
            rlist.setTitle(rlist.getTitle().replaceAll("\u0020", "&nbsp;"));
            rlist.setContents(rlist.getContents().replaceAll("\r\n", "<BR>"));
            rlist.setTitle(rlist.getTitle().replaceAll("\r\n", "<BR>"));
        }
        //본문 가져오기
        NoticeDTO dto = customerAsistService.qnaDetail(reqMap);
        dto.setWriter(StringEscapeUtils.escapeHtml4(dto.getWriter()));
        dto.setEmail(StringEscapeUtils.escapeHtml4(dto.getEmail()));
        dto.setData7(StringEscapeUtils.escapeHtml4(dto.getData7()));
        dto.setContents(StringEscapeUtils.escapeHtml4(dto.getContents()));
        dto.setTitle(StringEscapeUtils.escapeHtml4(dto.getTitle()));
        dto.setContents(dto.getContents().replaceAll("\u0020", "&nbsp;"));
        dto.setTitle(dto.getTitle().replaceAll("\u0020", "&nbsp;"));
        dto.setContents(dto.getContents().replaceAll("\r\n", "<BR>"));
        dto.setTitle(dto.getTitle().replaceAll("\r\n", "<BR>"));
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("repleList", repleList);
        mav.addObject("map", map);    //이전글 다음글
        mav.addObject("dto", dto);    //해당글
        mav.setViewName("common/customerAsist/noticeDetail");

        mav.setViewName("common/customerAsist/qnaDetail");

        return mav;
    }

    /*
     * QnA 글 삭제하기
     */
    @RequestMapping("qnaDelete")
    public ModelAndView qnaDelete(@RequestParam(defaultValue = "") long no,
                                  @RequestParam(defaultValue = "day") String search_orderBy,
                                  @RequestParam(defaultValue = "all") String search_option,
                                  @RequestParam(defaultValue = "") String keyword,
                                  @RequestParam(defaultValue = "1") int curPage,
                                  @RequestParam(defaultValue = "10") int PAGE_SCALE,
                                  @RequestParam(defaultValue = "10") int bbs,
                                  HttpServletRequest request) throws Exception {
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String code = authentication.getName();

        reqMap.put("no", no);
        reqMap.put("bbs", bbs);
        reqMap.put("code", code);
        //해당글의 파일삭제
        String path = uploadPath;                //리얼페스 받아오기

        NoticeDTO dto = customerAsistService.qnaDetail(reqMap);
        logger.info(path);
        logger.info("삭제할 파일이름" + dto.getFileid());
        if (dto.getFileid() != "") {
            File file = new File(path + dto.getFileid());

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
        try {
            customerAsistService.qnaDelete(reqMap);
        } catch (Exception e) {
            logger.info("=== QnA delete Fail.... === ");
        }
        reqMap.put("search_option", search_option);
        reqMap.put("keyword", keyword);


        //삭제후 글 리스트 다시 받아오기
        int totValue = customerAsistService.qnaTotalCount(reqMap);
        // 페이지 관련 설정
        PageVO page = new PageVO(totValue, curPage, PAGE_SCALE);
        int start = page.getPageBegin();
        int end = page.getPageEnd();

        reqMap.put("search_orderBy", search_orderBy);
        reqMap.put("search_option", search_option);
        reqMap.put("keyword", keyword);
        reqMap.put("start", start);
        reqMap.put("end", end);

        List<NoticeDTO> list = customerAsistService.qnaListAll(reqMap);

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
        map.put("bbs", bbs);
        mav.addObject("map", map);
        mav.setViewName("common/customerAsist/qnaList");

        return mav;
    }

    /*
     * QnA 글쓰기 페이지 진입
     */
    @RequestMapping("qnaWrite")
    public ModelAndView qnaWrite() throws Exception {

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        NoticeDTO dto = new NoticeDTO();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String code = authentication.getName();

        dto = customerAsistService.getPhoneNumber(code);
        String phoneNumber = "";
        if (dto.getChrhp() == "" || dto.getChrhp() == null) {
            phoneNumber = dto.getChrhp();
        } else {
            phoneNumber = dto.getOwnertel();
        }

        map.put("phoneNumber", phoneNumber);
        mav.addObject("map", map);
        mav.setViewName("common/customerAsist/qnaWrite");
        return mav;
    }

    /*
     * QnA 글쓰기 완료
     */
    @RequestMapping("qnaInsert")
    @ResponseBody
    public HashMap<String, Object> qnaInsert(MultipartHttpServletRequest multi, HttpServletRequest request) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            //일일 등록한 QnA 수 확인
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("id", request.getParameter("loginId"));

            int count = customerAsistService.getDailyQnaCount(reqMap);

            if(count >= 5) {
                map.put("retCode", "8888");
                map.put("retMsg", "일일등록 갯수를 초과하였습니다.");
                return map;
            }

            //기관 코드 받아오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String code = authentication.getName();

            //저장 경로 설정
            String path = uploadPath;

            //업로드 되는 파일명
            String newFileName = "";
            //경로 폴더 없으면 폴더 생성
            File dir = new File(path);
            if (dir.exists() == false) {
                dir.mkdirs();
            }

            Iterator<String> files = multi.getFileNames();
            while (files.hasNext()) {
                String uploadFile = files.next();
                MultipartFile mFile = multi.getFile(uploadFile);
                String fileName = mFile.getOriginalFilename();

                int pos = fileName.lastIndexOf(".");
                String ext = fileName.substring(pos + 1);
                ext = ext.toLowerCase();
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                if (fileName != "") {
                    newFileName = uuid;
                    if (!ext.equals("jpg") && !ext.equals("pdf") && !ext.equals("gif") && !ext.equals("xls") && !ext.equals("xlsx")) {
                        map.put("retCode", "9999");
                        map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
                        return map;
                    }
                } else {
                    newFileName = uuid;
                }
                //임시 추후 테이블 에 uuid 까지 넣기로 하면 위에 걸로 파일 저장해야함
                //확장자체크
                //프로젝트 서버 폴더에 파일 옴기기
                if (newFileName != "") {
                    mFile.transferTo(new File(path + uuid)); //20180621 파일 uuid 만으로 서버에 저장
                }
                try {
                    String title = StringEscapeUtils.unescapeHtml4(multi.getParameter("title"));
                    String contents = StringEscapeUtils.unescapeHtml4(multi.getParameter("contents"));
                    //디비에 넣기
                    //카테고리 어떻게 할건지 추가 필요
                    //reqMap.put("bbs", body.getBbs());
                    reqMap.put("id", multi.getParameter("loginId"));
                    reqMap.put("writer", multi.getParameter("writer"));
                    reqMap.put("email", multi.getParameter("email"));
                    reqMap.put("title", title);
                    reqMap.put("contents", contents);
                    if (newFileName != null) {
                        reqMap.put("fileid", newFileName); //코드값 uuid
                        reqMap.put("fileext", "." + ext); //확장자만
                        reqMap.put("fileName", fileName);
                        reqMap.put("filesize", mFile.getSize());
                    } else {
                        reqMap.put("fileid", ""); //코드값 uuid
                        reqMap.put("fileext", ""); //확장자만
                        reqMap.put("fileName", "");
                        reqMap.put("filesize", 0);
                    }

                    reqMap.put("ip", "");
                    reqMap.put("data1", multi.getParameter("phoneNumber"));
                    reqMap.put("data7", multi.getParameter("category"));
                    reqMap.put("code", code);

                    customerAsistService.qnaInsert(reqMap);

                    map.put("retCode", "0000");

                } catch (Exception e) {
                    logger.error(e.getMessage());
                    map.put("retCode", "9999");
                    map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
                }
            }


        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /*
     * 파일 다운로드
     */
    @RequestMapping("fileDownload")
    public void fileDownload(@RequestParam Map<String, Object> paramMap, HttpServletResponse response, HttpServletRequest request) throws Exception {

        try {
            String realFilename = (String) paramMap.get("filename");
            String virtureFilename = ((String) paramMap.get("fileid")).substring(0, 32);

            byte fileByte[] = FileUtils.readFileToByteArray(new File(uploadPath + virtureFilename));

            response.setContentType("application/octet-stream");
            response.setContentLength(fileByte.length);
            response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(realFilename, "UTF-8") + "\";");
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.getOutputStream().write(fileByte);

        } catch (Exception e) {
            response.setContentType("text/html;charset=UTF-8");

            PrintWriter out = response.getWriter();
            out.println("<script src=\"/assets/js/jquery.min.js\"></script>");
            out.println("<script src=\"/assets/js/sweetalert.js\"></script>");
            out.println("<script language=JavaScript>");
            out.println("$(document).ready(function(){");
            out.println("	swal('', '선택 하신 파일을 찾을 수 없습니다.', 'error')");
            out.println("		.then(function(result) {");
            out.println("			history.back();");
            out.println("	});");
            out.println("});");
            out.println("</script>");
        } finally {
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }

    }

    /*
     * QnA 글수정 페이지 진입
     */
    @RequestMapping("qnaUpdate")
    public ModelAndView qnaUpdate(@RequestParam(defaultValue = "") long no) throws Exception {

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        NoticeDTO dto = new NoticeDTO();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String code = authentication.getName();

        reqMap.put("no", no);
        reqMap.put("code", code);
        dto = customerAsistService.getPhoneNumber(code);
        logger.info("=== phnumber === " + dto.getOwnertel());
        logger.info("=== phnumber === " + dto.getChrhp());
        String phoneNumber = "";
        if (dto.getChrhp() == "" || dto.getChrhp() == null) {
            phoneNumber = dto.getChrhp();
        } else {
            phoneNumber = dto.getOwnertel();
        }

        dto = customerAsistService.qnaDetail(reqMap);
        dto.setContents(StringEscapeUtils.escapeHtml4(dto.getContents()));
        dto.setTitle(StringEscapeUtils.escapeHtml4(dto.getTitle()));
        dto.setContents(dto.getContents().replaceAll("\u0020", "&nbsp;"));
        dto.setTitle(dto.getTitle().replaceAll("\u0020", "&nbsp;"));
//			dto.setContents(dto.getContents().replaceAll("\r\n", "<BR>"));
        dto.setTitle(dto.getTitle().replaceAll("\r\n", "<BR>"));
        map.put("phoneNumber", phoneNumber);
        map.put("dto", dto);
        mav.addObject("map", map);
        mav.setViewName("common/customerAsist/qnaUpdate");
        return mav;
    }

    /*
     * QnA 글수정
     */
    @RequestMapping("qnaUpdateExcute")
    @ResponseBody
    public HashMap<String, Object> qnaUpdateExcute(@RequestBody NoticeDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            String title = StringEscapeUtils.unescapeHtml4(body.getTitle());
            String contents = StringEscapeUtils.unescapeHtml4(body.getContents());
            ;
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("title", title);
            reqMap.put("contents", contents);
            reqMap.put("filename", body.getFilename());
            reqMap.put("filesize", body.getFilesize());
            reqMap.put("ip", "");
            reqMap.put("no", body.getNo());

            customerAsistService.qnaUpdateExcute(reqMap);
            map.put("retCode", "0000");

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /*
     * QnA 글수정_등록 파일 삭제
     */
    @RequestMapping("qnaUpdateRemoveFile")
    @ResponseBody
    public HashMap<String, Object> qnaUpdateRemoveFile(@RequestBody NoticeDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String code = authentication.getName();
            //카테고리 어떻게 할건지 추가 필요
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            //reqMap.put("bbs", body.getBbs());
            reqMap.put("no", body.getNo());
            reqMap.put("filename", "");
            reqMap.put("fileid", ""); //코드값 uuid
            reqMap.put("fileext", ""); //확장자만
            reqMap.put("code", code);

            //해당글의 파일삭제
            //String root = request.getSession().getServletContext().getRealPath("/");
            String path = uploadPath;                //업로드 페스 변경 20180518
            NoticeDTO dto = customerAsistService.qnaDetail(reqMap);
            if (dto.getFileid() != "") {
                File file = new File(path + dto.getFileid()); //20180621 파일 uuid 만으로 서버에 저장

                if (file.exists()) {
                    if (file.delete()) {
                        map.put("retCode", "0000");
                        logger.info("파일 삭제 성공");
                    } else {
                        map.put("retCode", "9999");
                        logger.info("파일 삭제 실패");
                    }
                } else {
                    map.put("retCode", "0000");
                    logger.info("파일이 존재하지 않습니다.");
                }
            }

            //테이블에 파일 명 지우기
            customerAsistService.qnaUpdateRemoveFile(reqMap);

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /*
     * QnA 글수정_첨부 파일 변경
     */
    @RequestMapping("qnaUpdateChangeFile")
    @ResponseBody
    public HashMap<String, Object> qnaUpdateChangeFile(MultipartHttpServletRequest multi, HttpServletRequest request) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            long nono = Long.parseLong(multi.getParameter("no"));
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String code = authentication.getName();
            //카테고리 어떻게 할건지 추가 필요
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("no", nono);
            reqMap.put("code", code);
            NoticeDTO dto = customerAsistService.qnaDetail(reqMap);
            String path = uploadPath;
            //수정페이지에서 파일 정보 가져오기
            Iterator<String> files = multi.getFileNames();
            while (files.hasNext()) {
                String uploadFile = files.next();
                MultipartFile mFile = multi.getFile(uploadFile);
                String fileName = mFile.getOriginalFilename();
                //String oldFilename = dto.getFilename().substring(32); //기존 파일이름
                String newFilename = fileName;    //새로등록한 파일이름
                //확장자 추출
                int pos = fileName.lastIndexOf(".");
                String ext = fileName.substring(pos + 1);
                ext = ext.toLowerCase();
                //파일 변경 안했을때
                if (newFilename == "") {
                    //수정시 파일 추가하지 않앗고 이전 글에도 파일이 없던경우
                    if (dto.getFilename() == "" || dto.getFilename() == null) {
                        reqMap.put("filename", "");
                        reqMap.put("filesize", 0);
                        reqMap.put("fileid", ""); //코드값 uuid
                        reqMap.put("fileext", ""); //확장자만
                    } else {
                        reqMap.put("filename", dto.getFilename());
                        reqMap.put("filesize", dto.getFilesize());
                        reqMap.put("fileid", dto.getFileid()); //코드값 uuid
                        reqMap.put("fileext", "." + ext); //확장자만
                    }
                    String title = StringEscapeUtils.unescapeHtml4(multi.getParameter("title"));
                    String contents = StringEscapeUtils.unescapeHtml4(multi.getParameter("contents"));
                    reqMap.put("title", title);
                    reqMap.put("contents", contents);
                    reqMap.put("no", multi.getParameter("no"));
                    reqMap.put("ip", "");
                    reqMap.put("data7", multi.getParameter("category"));

                    customerAsistService.qnaUpdateChangeFile(reqMap);

                    map.put("retCode", "0000");
                } else if (dto.getFilename() == "" || dto.getFilename() == null) {
                    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                    if (!ext.equals("jpg") && !ext.equals("pdf") && !ext.equals("gif") && !ext.equals("xls") && !ext.equals("xlsx")) {
                        map.put("retCode", "9999");
                        map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
                        return map;
                    } else {
                        //새로운 파일 디비에 추가하기
                        reqMap.put("filename", newFilename);
                        reqMap.put("filesize", mFile.getSize());
                        reqMap.put("fileid", uuid); //코드값 uuid
                        reqMap.put("fileext", "." + ext); //확장자만
                        String title = StringEscapeUtils.unescapeHtml4(multi.getParameter("title"));
                        String contents = StringEscapeUtils.unescapeHtml4(multi.getParameter("contents"));
                        reqMap.put("title", title);
                        reqMap.put("contents", contents);
                        reqMap.put("no", multi.getParameter("no"));
                        reqMap.put("ip", "");
                        reqMap.put("data7", multi.getParameter("category"));

                        customerAsistService.qnaUpdateChangeFile(reqMap);

                        //새로운 파일 서버에 업로드하기
                        mFile.transferTo(new File(path + uuid));//20180621 파일 uuid 만으로 서버에 저장
                        map.put("retCode", "0000");
                    }
                } else {
                    //기존 첨부 파일 지우기
                    if (dto.getFilename() != "") {
                        File file = new File(path + dto.getFileid()); //20180621 파일 uuid 만으로 서버에 저장

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
                    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                    if (!ext.equals("jpg") && !ext.equals("pdf") && !ext.equals("gif") && !ext.equals("xls") && !ext.equals("xlsx")) {
                        map.put("retCode", "9999");
                        map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
                        return map;
                    } else {
                        String title = StringEscapeUtils.unescapeHtml4(multi.getParameter("title"));
                        String contents = StringEscapeUtils.unescapeHtml4(multi.getParameter("contents"));
                        //새로운 파일 디비에 추가하기
                        reqMap.put("filename", newFilename);
                        reqMap.put("filesize", mFile.getSize());
                        reqMap.put("fileid", uuid); //코드값 uuid
                        reqMap.put("fileext", "." + ext); //확장자만
                        reqMap.put("title", title);
                        reqMap.put("contents", contents);
                        reqMap.put("no", multi.getParameter("no"));
                        reqMap.put("ip", "");
                        reqMap.put("data7", multi.getParameter("category"));
                        customerAsistService.qnaUpdateChangeFile(reqMap);

                        //새로운 파일 서버에 업로드하기
                        mFile.transferTo(new File(path + uuid)); //20180621 파일 uuid 만으로 서버에 저장
                        map.put("retCode", "0000");
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }
}
