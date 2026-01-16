package com.finger.shinhandamoa.sys.addServiceMgmt.web;

import com.finger.shinhandamoa.bank.dto.BankReg01DTO;
import com.finger.shinhandamoa.bank.service.BankMgmtService;
import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.common.XlsxBuilder;
import com.finger.shinhandamoa.data.table.mapper.CustomerMasterMapper;
import com.finger.shinhandamoa.data.table.mapper.NoticeMasterMapper;
import com.finger.shinhandamoa.data.table.model.CustomerMaster;
import com.finger.shinhandamoa.data.table.model.CustomerMasterExample;
import com.finger.shinhandamoa.data.table.model.NoticeMaster;
import com.finger.shinhandamoa.data.table.model.NoticeMasterExample;
import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimDTO;
import com.finger.shinhandamoa.org.claimMgmt.service.ClaimExcelService;
import com.finger.shinhandamoa.org.receiptmgmt.web.DirectReceiptMgmtController;
import com.finger.shinhandamoa.org.transformer.DownloadView;
import com.finger.shinhandamoa.sys.addServiceMgmt.dto.AddServiceMgmtDTO;
import com.finger.shinhandamoa.sys.addServiceMgmt.dto.XNotimasreqDTO;
import com.finger.shinhandamoa.sys.addServiceMgmt.service.AddServiceMgmtService;
import com.finger.shinhandamoa.util.dto.CodeDTO;
import com.finger.shinhandamoa.util.service.CodeService;
import com.finger.shinhandamoa.vo.PageVO;
import kr.co.finger.shinhandamoa.common.XlsxEventParser;
import kr.co.finger.shinhandamoa.common.XlsxUserParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author
 * @date
 * @desc
 */
@RequestMapping("sys/addServiceMgmt/**")
@Controller
public class AddServiceMgmtController {
    private static final Logger logger = LoggerFactory.getLogger(AddServiceMgmtController.class);

    private static final Charset CHARSET_EUC_KR = Charset.forName("EUC-KR");

    @Inject
    private AddServiceMgmtService addServiceMgmtService;

    // 업로드 디렉토리
    @Value("${file.path.upload}")
    private String uploadPath;

    @Value("${file.path.ftp}")
    private String ftpFilePath;

    @Value("${ftp.url}")
    private String ftpUrl;

    @Value("${ftp.user}")
    private String ftpUser;

    @Value("${ftp.password}")
    private String ftpPassword;

    @Value("${ftp.sendPath}")
    private String sendPath;

    @Inject
    private BankMgmtService bankMgmtService;

    @Inject
    private ClaimExcelService claimExcelService;

    @Inject
    private CodeService codeService;

    @Autowired
    private CustomerMasterMapper customerMasterMapper;

    @Autowired
    private NoticeMasterMapper noticeMasterMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectReceiptMgmtController.class);

    /*
     *  문자서비스 신청내역조회
     */
    @RequestMapping("smsRegManage")
    public ModelAndView smsRegManage(@RequestParam(defaultValue = "reqDt") String search_orderBy,
                                     @RequestParam(defaultValue = "W") String statusCheck,
                                     @RequestParam(defaultValue = "1") int curPage,
                                     @RequestParam(defaultValue = "10") int PAGE_SCALE) throws Exception {

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        reqMap.put("statusCheck", statusCheck);

        //문자신청 내역 갯수
        HashMap<String, Object> totValue = addServiceMgmtService.smsRegListCount(reqMap);
        int count = Integer.parseInt(totValue.get("CNT").toString());

        //문자신청 내역 대기 건수
        HashMap<String, Object> waitValue = addServiceMgmtService.smsRegListWaitCount(reqMap);
        int waitCount = Integer.parseInt(waitValue.get("WAITCNT").toString());

        PageVO page = new PageVO(count, curPage, PAGE_SCALE);

        int start = page.getPageBegin();
        int end = page.getPageEnd();

        reqMap.put("start", start);
        reqMap.put("end", end);
        reqMap.put("count", count);

        //문자신청 내역 조회
        List<AddServiceMgmtDTO> list = addServiceMgmtService.smsRegList(reqMap);

        map.put("list", list);
        map.put("count", count);
        map.put("waitCount", waitCount);
        map.put("pager", page);    // 페이징 처리를 위한 변수
        map.put("uploadPath", uploadPath);
        map.put("statusCheck", statusCheck);

        mav.addObject("map", map);
        mav.setViewName("sys/additional/smsMgmt/smsRegManage");

        return mav;
    }

    /*
     * 	문자서비스 신청내역조회(Ajax)
     * */
    @RequestMapping("smsRegManageAjax")
    @ResponseBody
    public HashMap<String, Object> smsRegManageAjax(@RequestBody AddServiceMgmtDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            reqMap.put("chaName", body.getChaName());
            reqMap.put("chaCd", body.getChaCd());
            reqMap.put("tMonth", body.gettMonth());
            reqMap.put("fMonth", body.getfMonth());
            reqMap.put("curPage", body.getCurPage());
            reqMap.put("PAGE_SCALE", body.getPageScale());
            reqMap.put("search_orderBy", body.getSearchOrderBy());
            reqMap.put("searchGb", body.getSearchGb());
            reqMap.put("searchValue", body.getSearchValue());
            reqMap.put("statusCheck", body.getStatusCheck());


            //문자신청 내역 갯수
            HashMap<String, Object> totValue = addServiceMgmtService.smsRegListCount(reqMap);
            int count = Integer.parseInt(totValue.get("CNT").toString());

            //문자신청 내역 대기 건수
            HashMap<String, Object> waitValue = addServiceMgmtService.smsRegListWaitCount(reqMap);
            int waitCount = Integer.parseInt(waitValue.get("WAITCNT").toString());

            PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());

            int start = page.getPageBegin();
            int end = page.getPageEnd();

            reqMap.put("start", start);
            reqMap.put("end", end);
            reqMap.put("count", count);

            //문자신청 내역 조회
            List<AddServiceMgmtDTO> list = addServiceMgmtService.smsRegList(reqMap);

            map.put("list", list);
            map.put("count", count);
            map.put("waitCount", waitCount);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("retCode", "0000");
            map.put("retMsg", "정상");
            map.put("uploadPath", uploadPath);
            map.put("statusCheck", body.getStatusCheck());

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /*
     * 문자서비스신청서 삭제 처리
     */
    @RequestMapping("deleteSmsCertificate")
    @ResponseBody
    public HashMap<String, Object> deleteCmsFile(@RequestBody AddServiceMgmtDTO dto) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        try {

            String chaCd = dto.getChaCd();
            String no = dto.getNo();
            reqMap.put("chaCd", chaCd);
            reqMap.put("no", no);
            AddServiceMgmtDTO list = addServiceMgmtService.smsRegFileInfo(reqMap);
            File file = new File(uploadPath + list.getFileid() + list.getFileext());
            if (file.exists()) {
                if (file.delete()) {
                    logger.info("파일 삭제 성공");
                    addServiceMgmtService.updateSmsRegInfo(reqMap);
                } else {
                    logger.info("파일 삭제 실패");
                }
            } else {
                logger.info("파일이 존재하지 않습니다.");
            }
            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;

    }

    /*
     * 	제출서류 발신번호 조회(Ajax)
     * */
    @RequestMapping("getCallerNum")
    @ResponseBody
    public HashMap<String, Object> getCallerNum(@RequestBody AddServiceMgmtDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {

            reqMap.put("no", body.getNo());

            //제출서류 발신번호 조회
            AddServiceMgmtDTO data = addServiceMgmtService.getCallerNum(reqMap);

            map.put("no", body.getNo());
            map.put("data", data);
            map.put("retCode", "0000");
            map.put("retMsg", "정상");


        } catch (Exception e) {
            logger.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /*
     * 	발신번호 수정(Ajax)
     * */
    @RequestMapping("updateCallerNum")
    @ResponseBody
    public HashMap<String, Object> updateCallerNum(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {

            reqMap.put("no", request.getParameter("no"));
            reqMap.put("chaCd", request.getParameter("chaCd"));
            reqMap.put("smsSendTel", request.getParameter("strList"));
            reqMap.put("contents", request.getParameter("strList"));
            logger.info("=====" + request.getParameter("strList"));
            logger.info("=====" + request.getParameter("no"));
            logger.info("=====" + request.getParameter("chaCd"));

            logger.info("=====" + reqMap);

            //발신번호 수정
            addServiceMgmtService.updateCallerNum(reqMap);

            map.put("retCode", "0000");
            map.put("retMsg", "정상");


        } catch (Exception e) {
            logger.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /*
     * 	문자신청여부 수정(Ajax)
     * */
    @RequestMapping("updateUseSmsYn")
    @ResponseBody
    public HashMap<String, Object> updateUseSmsYn(@RequestBody AddServiceMgmtDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {

            reqMap.put("chaCd", body.getChaCd());
            reqMap.put("no", body.getNo());
            //제출서류 발신번호 조회
            AddServiceMgmtDTO data = addServiceMgmtService.getCallerNum(reqMap);
//			String smsSendTel = data.getSmsSendTel().split(",")[0];
            String smsSendTel = data.getSmsSendTel();
            reqMap.put("smsSendTel", smsSendTel);
            //sms사용여부 수정
            addServiceMgmtService.updateUseSmsYn(reqMap);

            map.put("retCode", "0000");
            map.put("retMsg", "정상");


        } catch (Exception e) {
            logger.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /*
     * 	문자서비스 해지(Ajax)
     * */
    @RequestMapping("deleteUseSmsYn")
    @ResponseBody
    public HashMap<String, Object> deleteUseSmsYn(@RequestBody AddServiceMgmtDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {

            reqMap.put("chaCd", body.getChaCd());
            reqMap.put("no", body.getNo());
            //sms사용여부 수정
            addServiceMgmtService.deleteUseSmsYn(reqMap);

            map.put("retCode", "0000");
            map.put("retMsg", "정상");


        } catch (Exception e) {
            logger.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /*
     * 이용내역 조회 > 고지서 출력의뢰 내역
     */
    @RequestMapping("notiPrintHistory")
    public ModelAndView notiPrintHistory(@RequestParam(defaultValue = "") String search_option,
                                         @RequestParam(defaultValue = "") String keyword,
                                         @RequestParam(defaultValue = "1") int curpage,
                                         @RequestParam(defaultValue = "10") int PAGE_SCALE) throws Exception {
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        reqMap.put("startday", StrUtil.getCalMonthDateStr(-1));
        reqMap.put("endday", StrUtil.getCurrentDateStr());
        reqMap.put("keyword", keyword);
        reqMap.put("chacd", "");
        reqMap.put("chaname", "");
        reqMap.put("reqst", "");
        reqMap.put("reqstList", null);
        reqMap.put("search_option", "regdt");
        reqMap.put("dlvrTypeCd", "all");

        int totValue = addServiceMgmtService.notiPrintCount(reqMap);
        int failValue = addServiceMgmtService.failNotiPrintCount(reqMap);
        int printCount = addServiceMgmtService.notiReqPrintCount(reqMap);

        // 페이지 관련 설정
        PageVO page = new PageVO(totValue, curpage, PAGE_SCALE);
        int start = page.getPageBegin();
        int end = page.getPageEnd();
        reqMap.put("start", start);
        reqMap.put("end", end);
        reqMap.put("opder_option", "regdt");

        List<XNotimasreqDTO> list = addServiceMgmtService.notiPrintListAll(reqMap);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("failCount", failValue);
        map.put("printCount", printCount);
        map.put("count", totValue);
        map.put("list", list);
        map.put("curpage", curpage);
        map.put("pager", page);    // 페이징 처리를 위한 변수
        mav.addObject("map", map);
        mav.setViewName("sys/additional/history/notiPrintHistory");

        return mav;
    }

    /*
     * 이용내역조회 > 고지서 출력의뢰 내역
     */
    @RequestMapping("ajaxNotiPrintHistory")
    @ResponseBody
    public HashMap<String, Object> ajaxNoticePrintHistory(@RequestBody XNotimasreqDTO body) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        LOGGER.debug("관리자 {} 고지서 출력의뢰 내역", body);

        try {
            reqMap.put("search_option", body.getSearchoption());
            reqMap.put("keyword", body.getKeyword());
            reqMap.put("startday", body.getStartday());
            reqMap.put("endday", body.getEndday());
            reqMap.put("chacd", body.getChacd());
            reqMap.put("chaname", body.getChaname());
            reqMap.put("reqstList", body.getReqstList());
            reqMap.put("dlvrTypeCd", body.getDlvrTypeCd());
            reqMap.put("opder_option", body.getOrderOption());

            int totValue = addServiceMgmtService.notiPrintCount(reqMap);
            int failValue = addServiceMgmtService.failNotiPrintCount(reqMap);
            int printCount = addServiceMgmtService.notiReqPrintCount(reqMap);

            // 페이지 관련 설정
            PageVO page = new PageVO(totValue, body.getCurpage(), body.getPagescale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            reqMap.put("start", start);
            reqMap.put("end", end);

            List<XNotimasreqDTO> list = addServiceMgmtService.notiPrintListAll(reqMap);

            ModelAndView mav = new ModelAndView();

            map.put("list", list);
            map.put("failCount", failValue);
            map.put("printCount", printCount);
            map.put("count", totValue);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("search_option", body.getSearchoption());
            map.put("curpage", body.getCurpage());
            map.put("keyword", body.getKeyword());
            map.put("PAGE_SCALE", body.getPagescale());
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
     *  이용내역조회 > 처리 취소로 수정
     */
    @RequestMapping("notiPrintUpdate")
    @ResponseBody
    public HashMap<String, Object> notiPrintUpdate(@RequestBody XNotimasreqDTO body) throws Exception {
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();

        LOGGER.debug("관리자 {} 고지서 요청 취소", body);
        try {
            reqMap.put("notimasreqcd", body.getNotimasreqcd());
            reqMap.put("maker", user);

            addServiceMgmtService.notiPrintUpdate(reqMap);
            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }


        return map;
    }

    /*
     * 긴급출력요청
     */
    @RequestMapping("quickPrint")
    @ResponseBody
    public HashMap<String, Object> quickPrint(@RequestBody XNotimasreqDTO body) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();

        LOGGER.debug("관리자 {} 고지서 긴급출력 요청", body.getNotimasreqcd());

        // 고지서 출력 전송
        try {
            reqMap.put("maker", user);
            reqMap.put("notimasreqcd", body.getNotimasreqcd());

            addServiceMgmtService.quickPrint(reqMap);
            map.put("retCode", "0000");

        } catch (Exception e) {
            e.printStackTrace();
            map.put("retCode", "9999");
        }
        return map;
    }

    /*
     * 재전송
     */
    @RequestMapping("rePrint")
    @ResponseBody
    public HashMap<String, Object> rePrint(@RequestBody XNotimasreqDTO body) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();

        LOGGER.debug("관리자 {} 고지서 재전송 요청", body.getNotimasreqcd());

        // 고지서 출력 전송
        try {
            reqMap.put("maker", user);
            reqMap.put("notimasreqcd", body.getNotimasreqcd());

            addServiceMgmtService.rePrint(reqMap);
            map.put("retCode", "0000");

        } catch (Exception e) {
            e.printStackTrace();
            map.put("retCode", "9999");
        }
        return map;
    }


    /*
     * 이용내역 조회 > 온라인카드결제 이용내역
     */
    @RequestMapping("cardPayHistory")
    public ModelAndView cardPayHistory(@RequestParam(defaultValue = "payDt") String search_orderBy,
                                       @RequestParam(defaultValue = "1") int curPage,
                                       @RequestParam(defaultValue = "10") int PAGE_SCALE) throws Exception {
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        String fMonth = StrUtil.getCalMonthDateStr(-1);
        String tMonth = StrUtil.getCurrentDateStr();

        reqMap.put("fMonth", fMonth);
        reqMap.put("tMonth", tMonth);

        //온라인카드결제 이용내역 건수
        HashMap<String, Object> totValue = addServiceMgmtService.cardPayHistoryCount(reqMap);
        int count = Integer.parseInt(totValue.get("CNT").toString());

        PageVO page = new PageVO(count, curPage, PAGE_SCALE);

        int start = page.getPageBegin();
        int end = page.getPageEnd();

        reqMap.put("start", start);
        reqMap.put("end", end);

        //온라인카드 결제내역 리스트조회
        List<AddServiceMgmtDTO> list = addServiceMgmtService.cardPayHistoryList(reqMap);

        map.put("fMonth", StrUtil.dateFormat(fMonth));
        map.put("tMonth", StrUtil.dateFormat(tMonth));
        map.put("list", list);
        map.put("count", count);
        map.put("pager", page);    // 페이징 처리를 위한 변수

        mav.addObject("map", map);
        mav.setViewName("sys/additional/history/cardPayHistory");

        return mav;
    }

    /*
     * 	이용내역 조회 > 온라인카드결제 이용내역 (Ajax)
     * */
    @RequestMapping("cardPayHistoryAjax")
    @ResponseBody
    public HashMap<String, Object> cardPayHistoryAjax(@RequestBody AddServiceMgmtDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {

            reqMap.put("chaName", body.getChaName());
            reqMap.put("chaCd", body.getChaCd());
            reqMap.put("tMonth", body.gettMonth());
            reqMap.put("fMonth", body.getfMonth());
            reqMap.put("curPage", body.getCurPage());
            reqMap.put("PAGE_SCALE", body.getPageScale());
            reqMap.put("search_orderBy", body.getSearchOrderBy());
            reqMap.put("searchValue", body.getSearchValue());
            reqMap.put("statusCheck", body.getStatusCheck());

            //온라인카드결제 이용내역 건수
            HashMap<String, Object> totValue = addServiceMgmtService.cardPayHistoryCount(reqMap);
            int count = Integer.parseInt(totValue.get("CNT").toString());

            PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());

            int start = page.getPageBegin();
            int end = page.getPageEnd();

            reqMap.put("start", start);
            reqMap.put("end", end);

            //온라인카드 결제내역 리스트조회
            List<AddServiceMgmtDTO> list = addServiceMgmtService.cardPayHistoryList(reqMap);

            map.put("list", list);
            map.put("count", count);
            map.put("pager", page);    // 페이징 처리를 위한 변수

            map.put("list", list);
            map.put("count", count);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("retCode", "0000");
            map.put("retMsg", "정상");
            map.put("statusCheck", body.getStatusCheck());

        } catch (Exception e) {
            logger.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    //문자서비스 신청관리 엑셀다운
    @RequestMapping("smsRegExcelDown")
    public View smsRegExcelDown(HttpServletRequest request, HttpServletResponse response, AddServiceMgmtDTO body, Model model) throws Exception {

        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("fMonth", request.getParameter("fStartDate"));
            reqMap.put("tMonth", request.getParameter("fEndDate"));
            reqMap.put("chaCd", request.getParameter("fChaCd"));
            reqMap.put("chaName", request.getParameter("fChaName"));
            reqMap.put("search_orderBy", request.getParameter("fSearchOrderBy"));
            reqMap.put("statusCheck", request.getParameter("fStatusCheck"));
            reqMap.put("searchGb", request.getParameter("fSearchGb"));
            reqMap.put("searchValue", request.getParameter("fSearchValue"));
            reqMap.put("pageGubn", "Excel");
            logger.info("EXCELDOWNLOAD >>  " + reqMap);

            //문자신청 내역 조회
            List<AddServiceMgmtDTO> list = addServiceMgmtService.smsRegList(reqMap);

            model.addAttribute("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return new ExcelSaveSmsReg();
    }

    //온라인카드결제 이용내역 엑셀다운
    @RequestMapping("cardHistoryExcelDown")
    public View cardHistoryExcelDown(HttpServletRequest request, HttpServletResponse response, AddServiceMgmtDTO body, Model model) throws Exception {

        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("fMonth", request.getParameter("fStartDate"));
            reqMap.put("tMonth", request.getParameter("fEndDate"));
            reqMap.put("chaCd", request.getParameter("fChaCd"));
            reqMap.put("chaName", request.getParameter("fChaName"));
            reqMap.put("search_orderBy", request.getParameter("fSearchOrderBy"));
            reqMap.put("statusCheck", request.getParameter("fStatusCheck"));
            reqMap.put("searchValue", request.getParameter("fSearchValue"));
            reqMap.put("pageGubn", "Excel");
            logger.info("EXCELDOWNLOAD >>  " + reqMap);

            //온라인카드 결제내역 리스트조회
            List<AddServiceMgmtDTO> list = addServiceMgmtService.cardPayHistoryList(reqMap);

            model.addAttribute("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return new ExcelSaveCardPay();
    }

    @RequestMapping("excelSaveNotiPrint")
    public ExcelSaveNotiPrint excelSaveNotiPrint(HttpServletRequest request, HttpServletResponse response, XNotimasreqDTO body, Model model) throws Exception {
        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();

            reqMap.put("search_option", body.getExSearchOption());
            reqMap.put("keyword", body.getExKeyword());

            List<String> daylist = startEndDay(body.getExStartday(), body.getExEndday());
            reqMap.put("startday", daylist.get(0));
            reqMap.put("endday", daylist.get(1));
            reqMap.put("start", 1);
            reqMap.put("end", body.getExCount());
            reqMap.put("chacd", body.getExChacd());
            reqMap.put("chaname", body.getExChaName());
            reqMap.put("reqstList", body.getExReqstList());
            reqMap.put("dlvrTypeCd", body.getExDlvrTypeCd());
            reqMap.put("opder_option", body.getExOrderOption());
            List<XNotimasreqDTO> list = addServiceMgmtService.notiPrintListAll(reqMap);

            model.addAttribute("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
        return new ExcelSaveNotiPrint();
    }

    public List<String> startEndDay(String startday, String endday) {
        List<String> list = new ArrayList<String>();
        if (startday == null || startday.equals("")) {
            startday = "19880101";
            list.add(0, startday);
        } else {
            list.add(startday);
        }
        if (endday == null || endday.equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String today = sdf.format(new Date());
            endday = today;
            list.add(1, endday);
        } else {
            list.add(endday);
        }
        return list;
    }

    /*
     * 부가서비스관리 > 엑셀양식변환
     */
    @RequestMapping("excelTrans")
    public ModelAndView excelTrans(@RequestParam(defaultValue = "") String chaCd) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("sys/additional/fileMgmt/excelTrans");
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        reqMap.put("chaCd", chaCd);

        BankReg01DTO list = bankMgmtService.selectChaListInfo(reqMap);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        mav.addObject("map", map);

        return mav;
    }

    @RequestMapping("excelFileTransClaim")
    public View excelFileTransClaim(MultipartHttpServletRequest request, HttpServletResponse response, ClaimDTO dto, Model model) throws Exception {
        MultipartFile excelFile = request.getFile("upload-file");
        String chaCd = request.getParameter("chaCd");
        String masMonth = request.getParameter("masMonth");

        String overlapCd = "";
        String prtItmNm = "";
        int overlapCnt = 0;

        InputStream inputDocument = null;
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<>();
        retMap.put("chaCd", chaCd);

        inputDocument = excelFile.getInputStream();

        List<List<Object>> data = XlsxUserParser.parse(inputDocument, 1, 13);
        Collections.sort(data, new Comparator<List<Object>>() {
            @Override
            public int compare(List<Object> o1, List<Object> o2) {
                final String c1 = String.valueOf(o1.get(0));
                final String c2 = String.valueOf(o1.get(10));
                final String c3 = String.valueOf(o2.get(0));
                final String c4 = String.valueOf(o2.get(10));

                if (!c1.equals(c3)) {
                    return c1.compareTo(c3);
                }

                return c2.compareTo(c4);
            }
        });

        try {
            for (List<Object> each : data) {// i를 2부터 시작해야 세번째 행부터 데이터가 입력된다.
                HashMap<String, Object> rmap = new HashMap<String, Object>();
                String cusName = "";
                String startDt = "";
                String endDt = "";
                String prtDt = "";
                String ptrItemName = "";
                String payItemAmt = "";
                String ptrItemRemark = "";

                if (each.get(0) != null && each.get(0) != "" && !each.get(0).equals("")) {
                    cusName = (String) each.get(0);
                    startDt = StringUtils.substring(String.valueOf(each.get(6)), 0, 10);
                    endDt = StringUtils.substring(String.valueOf(each.get(7)), 0, 10);
                    prtDt = StringUtils.substring(String.valueOf(each.get(8)), 0, 10);
                    ptrItemName = StringUtils.substring((String) each.get(10), 5, 7) + "월";
                    payItemAmt = String.valueOf(each.get(11));
                    ptrItemRemark = StringUtils.substring((String) each.get(12), 0, 25);

                    rmap.put("chaCd", chaCd);
                    rmap.put("masMonth", masMonth);
                    rmap.put("cusName", cusName);
                    rmap.put("startDt", startDt.replace("-", ""));
                    rmap.put("endDt", endDt.replace("-", ""));
                    rmap.put("prtDt", prtDt.replace("-", ""));
                    if (ptrItemName.startsWith("0")) {
                        ptrItemName = ptrItemName.substring(1, 3);
                        rmap.put("Q123", "Q123");
                    }
                    rmap.put("prtItemName", ptrItemName);
                    rmap.put("payItemAmt", payItemAmt);
                    rmap.put("prtItemRemark", ptrItemRemark);
                    retMap.put("cusName", StringUtils.trim(cusName));
                    retMap.put("prtItemName", ptrItemName);
                    ClaimDTO cdto = claimExcelService.listForExcelTrans(retMap);
                    List<CodeDTO> itemCdList = codeService.claimItemForExcelTrans(retMap);
                    rmap.put("cdto", cdto);
                    rmap.put("itemCdList", itemCdList);
                    if (cdto != null && ptrItemName != null) {
                        if (overlapCd.equals(cdto.getVano()) && prtItmNm.equals(ptrItemName)) {
                            overlapCnt++;
                            rmap.put("prtItemNm", itemCdList.get(overlapCnt).getCodeName());
                            rmap.put("prtItemCd", itemCdList.get(overlapCnt).getCode());
                            overlapCd = cdto.getVano();
                            prtItmNm = ptrItemName;
                        } else {
                            overlapCnt = 0;
                            rmap.put("prtItemNm", itemCdList.get(overlapCnt).getCodeName());
                            rmap.put("prtItemCd", itemCdList.get(overlapCnt).getCode());
                            overlapCd = cdto.getVano();
                            prtItmNm = ptrItemName;
                        }
                    } else {
                        rmap.put("prtItemNm", "고객정보없음");
                        rmap.put("prtItemCd", "고객정보없음'");
                    }
                    rmap.put("remark", " ");
                } else {
                }
                list.add(rmap);
            } // for loop(i) end (Rows)
            List<ClaimDTO> iList = claimExcelService.excelItemList(retMap);
            List<CodeDTO> cusGubnCd = codeService.cusGubnCd(chaCd); // 고객구분항목
            model.addAttribute("list", list);
            model.addAttribute("iList", iList);
            model.addAttribute("chaCd", chaCd);
            model.addAttribute("cusGbList", cusGubnCd);
            model.addAttribute("cusGbCnt", cusGubnCd.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            inputDocument.close();
            logger.info(">>> 파일삭제완료 <<<");
        }
        return new ExcelSaveTransClaim();
    }

    @ResponseBody
    @RequestMapping("excelFileTransCust")
    public View excelFileTransCust(MultipartHttpServletRequest request, HttpServletResponse response, ClaimDTO dto, Model model) throws Exception {

        MultipartFile excelFile = request.getFile("upload-file");
        String chaCd = request.getParameter("chaCd");

        String fileName = excelFile.getOriginalFilename();
        int pos = fileName.toLowerCase().lastIndexOf(".");
        String ext = fileName.substring(pos + 1);

        InputStream inputStream = excelFile.getInputStream();
        List<List<Object>> data = XlsxEventParser.parse(inputStream, 1, 13);

        Collections.sort(data, new Comparator<List<Object>>() {
            @Override
            public int compare(List<Object> o1, List<Object> o2) {
                final String c1 = String.valueOf(o1.get(0));
                final String c2 = String.valueOf(o1.get(10));
                final String c3 = String.valueOf(o2.get(0));
                final String c4 = String.valueOf(o2.get(10));

                if (!c1.equals(c3)) {
                    return c1.compareTo(c3);
                }

                return c2.compareTo(c4);
            }
        });

        TreeMap<String, List<Object>> map = new TreeMap<>();
        for (List<Object> each : data) {
            final String c1 = String.valueOf(each.get(0));

            map.put(c1, each);
        }

        final XlsxBuilder xlsxBuilder = new XlsxBuilder();
        xlsxBuilder.newSheet("데이터");
        xlsxBuilder.addHeader(0, 0, 0, 0, "고객명(필수)");
        xlsxBuilder.addHeader(0, 0, 1, 1, "가상계좌번호");
        xlsxBuilder.addHeader(0, 0, 2, 2, "고객번호");
        xlsxBuilder.addHeader(0, 0, 3, 3, "납부대상 (Y=대상 / N=제외)");
        xlsxBuilder.addHeader(0, 0, 4, 4, "연락처");
        xlsxBuilder.addHeader(0, 0, 5, 5, "이메일");
        xlsxBuilder.addHeader(0, 0, 6, 6, "선생님");
        xlsxBuilder.addHeader(0, 0, 7, 7, "학년");
        xlsxBuilder.addHeader(0, 0, 8, 8, "반");
        xlsxBuilder.addHeader(0, 0, 9, 9, "학번");
        xlsxBuilder.addHeader(0, 0, 10, 10, "발급용도 (1=소득공제 / 2=지출증빙)");
        xlsxBuilder.addHeader(0, 0, 11, 11, "현금영수증발급방법 (11=휴대폰번호 / 12=현금영수증카드번호 / 21=사업자번호)");
        xlsxBuilder.addHeader(0, 0, 12, 12, "현금영수증발급 번호");
        xlsxBuilder.addHeader(0, 0, 13, 13, "메모 (고객 특이사항)");

        for (List<Object> each : map.values()) {
            CustomerMasterExample example = new CustomerMasterExample();
            example.createCriteria().andChacdEqualTo(chaCd).andCusnameEqualTo(String.valueOf(each.get(0)).trim());

            List<CustomerMaster> customerMasterList = customerMasterMapper.selectByExample(example);
            String vano = null;
            CustomerMaster cm = null;
            if (customerMasterList.isEmpty()) {
                // 등록된 고객이 없을 때
                vano = StringUtils.EMPTY;
            } else if (customerMasterList.size() == 1) {
                // 등록된 고객이 하나밖에 없을 때
                vano = customerMasterList.get(0).getVano();
                cm = customerMasterList.get(0);
            } else {
                // 등록된 고객이 다수일 때
                List<String> accountList = new ArrayList<>();
                Map<String, CustomerMaster> cmMap = new HashMap<>();
                for (CustomerMaster eachCustomerMaster : customerMasterList) {
                    accountList.add(eachCustomerMaster.getVano());
                    cmMap.put(eachCustomerMaster.getVano(), eachCustomerMaster);
                }

                NoticeMasterExample nmExample = new NoticeMasterExample();
                nmExample.createCriteria().andVanoIn(accountList);
                nmExample.setOrderByClause("MASDAY DESC");

                List<NoticeMaster> noticeMasterList = noticeMasterMapper.selectByExampleWithRowbounds(nmExample, new RowBounds(0, 1));
                if (noticeMasterList.isEmpty()) {
                    vano = customerMasterList.get(0).getVano();
                } else {
                    vano = noticeMasterList.get(0).getVano();
                }

                cm = cmMap.get(vano);
            }

            String cashReceipt = String.valueOf(each.get(9)).replace("-", "").trim();
            String cashReceiptGu = cashReceipt;
            String issueUse = null;

            //사업자 0으로 시작하는 10자리, 핸드폰 01로 시작하는 10-11자리, 카드 16
            if (cashReceiptGu.substring(0, 2).equals("01") && (cashReceiptGu.length() == 10 || cashReceiptGu.length() == 11)) {
                cashReceiptGu = "11";
                issueUse = "1";
            } else if (Character.toString(cashReceiptGu.charAt(0)) != "0" && cashReceiptGu.length() == 10) {
                cashReceiptGu = "21";
                issueUse = "2";
            } else {
                cashReceiptGu = "12";
                issueUse = "1";
            }

            String rltStr = String.valueOf(each.get(3));

            xlsxBuilder.newDataRow();
            xlsxBuilder.addData(0, String.valueOf(each.get(0))); //고객명
            xlsxBuilder.addData(1, vano); //가상계좌번호
            if (cm != null) {
                xlsxBuilder.addData(2, cm.getCuskey()); //고객번호
                xlsxBuilder.addData(3, cm.getRcpgubn()); //납부대상
                xlsxBuilder.addData(4, cm.getCushp()); //연락
                xlsxBuilder.addData(5, cm.getCusmail()); //이메일
            }
            xlsxBuilder.addData(6, String.valueOf(each.get(4)));//선생님
            xlsxBuilder.addData(7, String.valueOf(each.get(2)));//학년

            xlsxBuilder.addData(8, substringWithByteLength(rltStr, 30, CHARSET_EUC_KR));
            xlsxBuilder.addData(9, String.valueOf(each.get(5)));//학번
            xlsxBuilder.addData(10, issueUse); //발급용도
            xlsxBuilder.addData(11, cashReceiptGu); //현금영수증발급방법
            xlsxBuilder.addData(12, String.valueOf(cashReceipt)); //현금영수증발급번호
        }

        final File file = File.createTempFile("지트지트지트", "tmp");
        xlsxBuilder.writeTo(new FileOutputStream(file));

        return new DownloadView("고객등록_" + chaCd + ".xlsx", new FileInputStream(file));
    }

    public String substringWithByteLength(String string, int byteLength, Charset charset) {
        String result = StringUtils.EMPTY;
        for (int i = 0; i < string.length(); i++) {
            String substring = string.substring(0, i + 1);
            if (substring.getBytes(charset).length > byteLength) {
                break;
            }

            result = substring;
        }

        return result;
    }

    /**
     * 부가서비스관리 > 과거수납내역조회
     */
    @RequestMapping("pastRcpHistList")
    public ModelAndView pastRcpHistList() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("sys/additional/history/pastRcpHistList");

        return mav;
    }

    /**
     * 부가서비스관리 > 과거수납내역조회 (Ajax)
     */
    @RequestMapping("pastRcpHistListAjax")
    @ResponseBody
    public HashMap<String, Object> pastRcpHistListAjax(@RequestBody AddServiceMgmtDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {

            reqMap.put("chaCd", body.getChaCd());
            reqMap.put("tMonth", body.gettMonth());
            reqMap.put("fMonth", body.getfMonth());
            reqMap.put("curPage", body.getCurPage());
            reqMap.put("PAGE_SCALE", body.getPageScale());
            reqMap.put("searchOrderBy", body.getSearchOrderBy());
            reqMap.put("statusCheck", body.getStatusCheck());

            //과거수납내역조회 건수
            HashMap<String, Object> totValue = addServiceMgmtService.pastRcpHistListCount(reqMap);
            int count = Integer.parseInt(totValue.get("CNT").toString());

            PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());

            int start = page.getPageBegin();
            int end = page.getPageEnd();

            reqMap.put("start", start);
            reqMap.put("end", end);

            //과거수납내역조회 리스트조회
            List<AddServiceMgmtDTO> list = addServiceMgmtService.pastRcpHistList(reqMap);

            map.put("list", list);
            map.put("count", count);
            map.put("pager", page);    // 페이징 처리를 위한 변수

            map.put("retCode", "0000");
            map.put("retMsg", "정상");
            map.put("statusCheck", body.getStatusCheck());

        } catch (Exception e) {
            logger.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    //과거수납내역조회 엑셀다운
    @RequestMapping("pastRcpHistExcelDown")
    public View pastRcpHistExcelDown(HttpServletRequest request, HttpServletResponse response, AddServiceMgmtDTO body, Model model) throws Exception {

        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("fMonth", request.getParameter("fStartDate"));
            reqMap.put("tMonth", request.getParameter("fEndDate"));
            reqMap.put("chaCd", request.getParameter("fChaCd"));
            reqMap.put("searchOrderBy", request.getParameter("fSearchOrderBy"));
            reqMap.put("statusCheck", request.getParameter("fStatusCheck"));
            reqMap.put("pageGubn", "Excel");


            //과거수납내역조회 조회
            List<AddServiceMgmtDTO> list = addServiceMgmtService.pastRcpHistList(reqMap);

            model.addAttribute("list", list);
            model.addAttribute("chaCd", request.getParameter("fChaCd"));
            model.addAttribute("statusCheck", request.getParameter("fStatusCheck"));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return new ExcelSavePastRcpHist();
    }

    /**
     * 부가서비스관리 > 과거입금내역조회
     */
    @RequestMapping("pastPaymentHistList")
    public ModelAndView pastPaymentHistList() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("sys/additional/history/pastPaymentHistList");
        return mav;
    }

    /**
     * 부가서비스관리 > 과거입금내역조회 (Ajax)
     */
    @RequestMapping("pastPaymentHistListAjax")
    @ResponseBody
    public HashMap<String, Object> pastPaymentHistListAjax(@RequestBody AddServiceMgmtDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            reqMap.put("chaCd", body.getChaCd());
            reqMap.put("tMonth", body.gettMonth());
            reqMap.put("fMonth", body.getfMonth());
            reqMap.put("curPage", body.getCurPage());
            reqMap.put("PAGE_SCALE", body.getPageScale());
            reqMap.put("searchOrderBy", body.getSearchOrderBy());

//			String[] rcpmasst = {"PA03", "PA04", "PA05"};
//			reqMap.put("rcpMasSt", Arrays.asList());

            //과거입금내역조회 건수
            HashMap<String, Object> totValue = addServiceMgmtService.pastPaymentHistListCount(reqMap);
            int count = Integer.parseInt(totValue.get("CNT").toString());

            PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());

            int start = page.getPageBegin();
            int end = page.getPageEnd();

            reqMap.put("start", start);
            reqMap.put("end", end);

            //과거입금내역조회 리스트조회
            List<AddServiceMgmtDTO> list = addServiceMgmtService.pastPaymentHistList(reqMap);

            map.put("list", list);
            map.put("count", count);
            map.put("pager", page);    // 페이징 처리를 위한 변수

            map.put("retCode", "0000");
            map.put("retMsg", "정상");

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    //과거입금내역조회 엑셀다운
    @RequestMapping("pastPaymentHistExcelDown")
    public View pastPaymentHistExcelDown(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("fMonth", request.getParameter("fStartDate"));
            reqMap.put("tMonth", request.getParameter("fEndDate"));
            reqMap.put("chaCd", request.getParameter("fChaCd"));
            reqMap.put("searchOrderBy", request.getParameter("fSearchOrderBy"));
            reqMap.put("pageGubn", "Excel");

            //과거입금내역조회 리스트조회
            List<AddServiceMgmtDTO> list = addServiceMgmtService.pastPaymentHistList(reqMap);

            model.addAttribute("list", list);
            model.addAttribute("chaCd", request.getParameter("fChaCd"));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return new ExcelSavePastPaymentHist();
    }
}
