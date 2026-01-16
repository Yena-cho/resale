package com.finger.shinhandamoa.sys.addServiceMgmt.web;

import com.finger.shinhandamoa.org.receiptmgmt.web.DirectReceiptMgmtController;
import com.finger.shinhandamoa.sys.addServiceMgmt.dto.AddServiceMgmtDTO;
import com.finger.shinhandamoa.sys.addServiceMgmt.service.AddServiceMgmtService;
import com.finger.shinhandamoa.vo.PageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author
 * @date
 * @desc
 */
@RequestMapping("sys/addServiceMgmtA/**")
@Controller
public class AtMgmtController {
    private static final Logger logger = LoggerFactory.getLogger(AtMgmtController.class);

    private static final Charset CHARSET_EUC_KR = Charset.forName("EUC-KR");

    @Inject
    private AddServiceMgmtService addServiceMgmtService;

    /**
     * 부가서비스관리 > 알림톡서비스신청관리
     */
    @RequestMapping("atRegManage")
    public ModelAndView atRegManage() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("sys/additional/atMgmt/atRegManage");
        return mav;
    }

    /**
     * 	알림톡 신청내역조회(Ajax)
     * */
    @RequestMapping("atRegManageAjax")
    @ResponseBody
    public HashMap<String, Object> atRegManageAjax(@RequestBody AddServiceMgmtDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            reqMap.put("fDate", body.getfDate());
            reqMap.put("tDate", body.gettDate());
            reqMap.put("chaCd", body.getChaCd());
            reqMap.put("chaName", body.getChaName());
            reqMap.put("searchGb", body.getSearchGb());
            reqMap.put("searchValue", body.getSearchValue());
            reqMap.put("statusCheck", body.getStatusCheck());
            reqMap.put("search_orderBy", body.getSearchOrderBy());
            reqMap.put("curPage", body.getCurPage());
            reqMap.put("PAGE_SCALE", body.getPageScale());

            //알림톡 신청 갯수
            HashMap<String, Object> totValue = addServiceMgmtService.atRegListCount(reqMap);
            int count = Integer.parseInt(totValue.get("CNT").toString());

            //알림톡 신청 대기 건수
            HashMap<String, Object> waitValue = addServiceMgmtService.atRegListWaitCount(reqMap);
            int waitCount = Integer.parseInt(waitValue.get("WAITCNT").toString());

            PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());

            int start = page.getPageBegin();
            int end = page.getPageEnd();

            reqMap.put("start", start);
            reqMap.put("end", end);
            reqMap.put("count", count);

            //알림톡 신청내역 조회
            List<AddServiceMgmtDTO> list = addServiceMgmtService.atRegList(reqMap);

            map.put("list", list);
            map.put("count", count);
            map.put("waitCount", waitCount);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("retCode", "0000");
            map.put("retMsg", "정상");

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 	알림톡 발송기관명 수정(Ajax)
     * */
    @RequestMapping("updateAtChaName")
    @ResponseBody
    public HashMap<String, Object> updateAtChaName(@RequestBody AddServiceMgmtDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            reqMap.put("chaCd", body.getChaCd());
            reqMap.put("atChaName", body.getAtChaName());
            reqMap.put("atHisNo", body.getAtHisNo());

            addServiceMgmtService.updateAtChaName(reqMap);
            addServiceMgmtService.updateAtChaNameHis(reqMap);
            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /**
     * 	알림톡 승인 or 거절(Ajax)
     * */
    @RequestMapping("updateAtAcptDt")
    @ResponseBody
    public HashMap<String, Object> updateAtAcptDt(@RequestBody AddServiceMgmtDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            reqMap.put("chaCd", body.getChaCd());
            reqMap.put("statusCheck", body.getStatusCheck());
            reqMap.put("atHisNo", body.getAtHisNo());

            addServiceMgmtService.updateAtAcptDt(reqMap);
            addServiceMgmtService.updateAtAcptHis(reqMap);
            map.put("retCode", "0000");

            if("Y".equals(body.getStatusCheck())) {
                map.put("retMsg", "승인");
            } else {
                map.put("retMsg", "거절");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /**
     * 	알림톡 해지(Ajax)
     * */
    @RequestMapping("deleteAtYn")
    @ResponseBody
    public HashMap<String, Object> deleteAtYn(@RequestBody AddServiceMgmtDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            reqMap.put("chaCd", body.getChaCd());
            reqMap.put("atHisNo", body.getAtHisNo());

            addServiceMgmtService.deleteAtYn(reqMap);
            addServiceMgmtService.deleteAtYnHis(reqMap);
            map.put("retCode", "0000");
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }

    /**
     * 부가서비스관리 > 알림톡서비스이용내역
     */
    @RequestMapping("atUseList")
    public ModelAndView atUseList() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("sys/additional/atMgmt/atUseList");
        return mav;
    }

    /**
     * 	알림톡 이용내역조회(Ajax)
     * */
    @RequestMapping("atUseListAjax")
    @ResponseBody
    public HashMap<String, Object> atUseListAjax(@RequestBody AddServiceMgmtDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            reqMap.put("fDate", body.getfDate());
            reqMap.put("tDate", body.gettDate());
            reqMap.put("chaCd", body.getChaCd());
            reqMap.put("chaName", body.getChaName());
            reqMap.put("searchGb", body.getSearchGb());
            reqMap.put("searchValue", body.getSearchValue());
            reqMap.put("msgType", body.getMsgType());
            reqMap.put("statusCheck", body.getStatusCheck());
            reqMap.put("search_orderBy", body.getSearchOrderBy());
            reqMap.put("curPage", body.getCurPage());
            reqMap.put("PAGE_SCALE", body.getPageScale());

            //알림톡 이용내역 갯수
            HashMap<String, Object> totValue = addServiceMgmtService.atUseListCount(reqMap);
            int totalCount = Integer.parseInt(totValue.get("TOTALCNT").toString());

            //알림톡 발송 성공 건수
            HashMap<String, Object> successValue = addServiceMgmtService.atUseListSuccessCount(reqMap);
            int successCount = Integer.parseInt(successValue.get("SUCCESSCNT").toString());

            //알림톡 발송 실패 건수
            HashMap<String, Object> failValue = addServiceMgmtService.atUseListFailCount(reqMap);
            int failCount = Integer.parseInt(failValue.get("FAILCNT").toString());

            PageVO page = new PageVO(totalCount, body.getCurPage(), body.getPageScale());

            int start = page.getPageBegin();
            int end = page.getPageEnd();

            reqMap.put("start", start);
            reqMap.put("end", end);
            reqMap.put("count", totalCount);

            //알림톡 내역 조회
            List<AddServiceMgmtDTO> list = addServiceMgmtService.atUseList(reqMap);

            map.put("list", list);
            map.put("totalCount", totalCount);
            map.put("successCount", successCount);
            map.put("failCount", failCount);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("retCode", "0000");
            map.put("retMsg", "정상");

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 알림톡 신청내역 엑셀파일 다운로드
     */
    @RequestMapping("atRegExcelDown")
    public View atRegExcelDown(HttpServletRequest request, HttpServletResponse response, AddServiceMgmtDTO body, Model model) throws Exception {
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        try {
            reqMap.put("fDate", request.getParameter("fDate"));
            reqMap.put("tDate", request.getParameter("tDate"));
            reqMap.put("chaCd", request.getParameter("fChaCd"));
            reqMap.put("chaName", request.getParameter("fChaName"));
            reqMap.put("searchGb", request.getParameter("fSearchGb"));
            reqMap.put("searchValue", request.getParameter("fSearchValue"));
            reqMap.put("statusCheck", request.getParameter("fStatusCheck"));
            reqMap.put("search_orderBy", request.getParameter("fSearchOrderBy"));
            reqMap.put("pageGubn", "Excel");

            //알림톡 신청내역 조회
            List<AddServiceMgmtDTO> list = addServiceMgmtService.atRegList(reqMap);

            model.addAttribute("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return new ExcelSaveAtReg();
    }

    /**
     * 알림톡 이용내역 엑셀파일 다운로드
     */
    @RequestMapping("atUseExcelDown")
    public View atUseExcelDown(HttpServletRequest request, Model model) throws Exception {
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        try {
            reqMap.put("fDate", request.getParameter("fDate"));
            reqMap.put("tDate", request.getParameter("tDate"));
            reqMap.put("chaCd", request.getParameter("fChaCd"));
            reqMap.put("chaName", request.getParameter("fChaName"));
            reqMap.put("msgType", request.getParameter("fMsgType"));
            reqMap.put("searchGb", request.getParameter("fSearchGb"));
            reqMap.put("searchValue", request.getParameter("fSearchValue"));
            reqMap.put("statusCheck", request.getParameter("fStatusCheck"));
            reqMap.put("search_orderBy", request.getParameter("fSearchOrderBy"));
            reqMap.put("pageGubn", "Excel");

            //알림톡 신청내역 조회
            List<AddServiceMgmtDTO> list = addServiceMgmtService.atUseList(reqMap);

            model.addAttribute("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return new ExcelSaveAtUse();
    }
}
