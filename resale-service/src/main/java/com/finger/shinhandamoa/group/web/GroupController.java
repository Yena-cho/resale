package com.finger.shinhandamoa.group.web;

import com.finger.shinhandamoa.bank.dto.BankReg01DTO;
import com.finger.shinhandamoa.common.RunAsAuthentication;
import com.finger.shinhandamoa.common.ShaEncoder;
import com.finger.shinhandamoa.group.dto.GroupDTO;
import com.finger.shinhandamoa.group.service.GroupService;
import com.finger.shinhandamoa.org.mypage.dto.MyPageDTO;
import com.finger.shinhandamoa.org.mypage.service.MyPageService;
import com.finger.shinhandamoa.sys.chaMgmt.service.SysChaService;
import com.finger.shinhandamoa.vo.PageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

/**
 * @author by mljeong@finger.co.kr
 * @date 2018. 4. 6.
 * @desc
 */
@Controller
@RequestMapping("group/**")
public class GroupController {
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupService groupService;

    @Autowired
    protected UserDetailsService userDetailsService;

    @Inject
    private ShaEncoder shaEncoder;

    @Inject
    private MyPageService myPageService;

    @Inject
    private SysChaService sysChaService;


    /**
     * 가맹점 목록
     */
    @RequestMapping("groupList")
    public ModelAndView groupList() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("group/groupList");

        return mav;
    }

    @ResponseBody
    @RequestMapping("ajaxGroupList")
    public HashMap<String, Object> selGroupList(@RequestBody GroupDTO dto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String groupId = authentication.getName();
        logger.debug("그룹기관 {} 가맹점 목록", groupId);
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            map.put("chaGroupId", groupId);
            map.put("searchOrderBy", dto.getSearchOrderBy());

            int count = groupService.selGroupCount(map);
            map.put("count", count);

            PageVO page = new PageVO(count, dto.getCurPage(), 10);
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            map.put("start", start);
            map.put("end", end);

            List<GroupDTO> list = groupService.selGroupList(map);
            map.put("list", list);
            map.put("pager", page);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return map;
    }

    @RequestMapping("moveAuth")
    public String changeAuth(HttpServletRequest request, HttpServletResponse response, GroupDTO dto) throws Exception {
        final SecurityContext securityContext = SecurityContextHolder.getContext();

        final Authentication groupAuthentication = securityContext.getAuthentication();
        final RunAsAuthentication runAsAuthentication = new RunAsAuthentication(groupAuthentication);

        final UserDetails ckUserDetails = userDetailsService.loadUserByUsername("GROUP:///:" + dto.getLoginId());
        final Authentication orgAuthentication = new UsernamePasswordAuthenticationToken(ckUserDetails, ckUserDetails.getPassword(), ckUserDetails.getAuthorities());
        runAsAuthentication.runAs(orgAuthentication);

        securityContext.setAuthentication(runAsAuthentication);

        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

        //화면이동으로 지원하는 경우는 로그아웃대상에서 빠지도록 해당 세션 lock(use lockyn Y로)
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("sessionId" , session.getId());
        reqMap.put("username", ckUserDetails.getUsername());
        sysChaService.updateSessionMax(reqMap);

        return "redirect:/";
    }

    /**
     * 가맹점 기본 정보 수정
     */
    @RequestMapping("groupInfo")
    public ModelAndView groupInfo() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String groupId = authentication.getName();
        logger.debug("그룹기관 {} 가맹점 기본 정보 수정", groupId);
        ModelAndView mav = new ModelAndView();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("groupId", groupId);

        GroupDTO gDto = groupService.selGroupInfo(map);

        mav.addObject("map", gDto);
        mav.setViewName("group/groupInfo");

        return mav;
    }

    @ResponseBody
    @RequestMapping("updatePwd")
    public HashMap<String, Object> updatePwd(@RequestBody MyPageDTO dto) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        String passWrd = "";

        try {

            reqMap.put("chaCd", dto.getChaCd());
            reqMap.put("pwd", shaEncoder.encoding(dto.getPwd()));            // 현재
            reqMap.put("chgPwd1", shaEncoder.encoding(dto.getChgPwd1()));    // 변경
            reqMap.put("chgPwd2", dto.getChgPwd2());

            // 현재 비밀번호 조회
            passWrd = myPageService.selectPwd(reqMap);

            if (!passWrd.equals(reqMap.get("pwd"))) {
                map.put("retCode", "8888");
            } else if (passWrd.equals(reqMap.get("chgPwd1"))) {
                map.put("retCode", "9999");
            } else {
                myPageService.updatePwd(reqMap);
                map.put("retCode", "0000");
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return map;
    }

    /**
     * 입금 내역 조회
     */
    @RequestMapping("groupPaymentList")
    public ModelAndView groupPaymentList() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("group/groupPaymentList");

        return mav;
    }

    /**
     * 입금 내역 조회
     */
    @ResponseBody
    @RequestMapping("ajaxGroupPaymentList")
    public HashMap<String, Object> selGroupPaymentList(@RequestBody GroupDTO dto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String groupId = authentication.getName();
        logger.debug("그룹기관 {} 가맹점 입금 내역 조회", groupId);
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            map.put("chaGroupId", groupId);
            map.put("startDate", dto.getStartDate());
            map.put("endDate", dto.getEndDate());
            map.put("masMonth", dto.getMasMonth());
            map.put("chaCd", dto.getChaCd());
            map.put("searchGb", dto.getSearchGb());

            if (!"".equals(dto.getSearchValue())) {
                String value = dto.getSearchValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("searchValue", valueList);
            }

            map.put("cusGubn", dto.getCusGubn());
            if (dto.getCusGubnValue() != null && !"".equals(dto.getCusGubnValue())) {
                String value = dto.getCusGubnValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("cusGubnValue", valueList);
            }

            map.put("searchSveCd", dto.getSearchSveCd());
            map.put("searchOrderBy", dto.getSearchOrderBy());

            int count = groupService.selGroupPaymentCount(map);
            map.put("count", count);

            long totalCnt = groupService.selGroupPaymentSum(map);
            map.put("totalCnt", totalCnt);

            PageVO page = new PageVO(count, dto.getCurPage(), 10);
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            map.put("start", start);
            map.put("end", end);

            List<GroupDTO> list = groupService.selGroupPaymentList(map);
            map.put("list", list);
            map.put("pager", page);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return map;
    }

    /**
     * 입금내역조회 엑셀 파일 저장
     */
    @ResponseBody
    @RequestMapping("excelSave")
    public View excelSsaveDownload(HttpServletRequest request, HttpServletResponse response, GroupDTO dto, Model model) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String groupId = authentication.getName();
        logger.debug("그룹기관 {} 가맹점 입금내역조회 엑셀 파일 저장", groupId);
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("chaGroupId", groupId);
            map.put("startDate", dto.getStartDate());
            map.put("endDate", dto.getEndDate());
            map.put("masMonth", dto.getMasMonth());
            map.put("chaCd", dto.getChaCd());
            map.put("searchGb", dto.getSearchGb());
            map.put("searchValue", dto.getSearchValue().replaceAll(",", "|").trim());
            map.put("cusGubn", dto.getCusGubn());
            map.put("cusGubnValue", dto.getCusGubnValue().replaceAll(",", "|").trim());
            map.put("searchSveCd", dto.getSearchSveCd());
            map.put("searchOrderBy", dto.getSearchOrderBy());

            List<GroupDTO> list = groupService.selGroupPaymentExcelList(map);

            model.addAttribute("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return new ExcelSaveGroupPayment();
    }


    /**
     * 가맹점 검색
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RolesAllowed({"ROLE_BANKADMIN"})
    @RequestMapping("getCollectorListAjax")
    public HashMap getCollectorListAjax(@RequestBody BankReg01DTO body) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String groupId = authentication.getName();
        logger.debug("그룹기관 {} 가맹점 검색", groupId);
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        int PAGE_SCALE = 5;

        HashMap<String, Object> map = new HashMap<String, Object>();
        reqMap.put("chaCd", body.getChaCd());
        reqMap.put("chaName", body.getChaName());
        reqMap.put("chaGb", body.getChaGb());
        reqMap.put("chaGroupId", groupId);

        // total count
        int count = groupService.getCollectorListTotalCount(reqMap);

        PageVO page = new PageVO(count, body.getCurPage(), PAGE_SCALE);
        int start = page.getPageBegin();
        int end = page.getPageEnd();

        reqMap.put("count", count);
        reqMap.put("start", start);
        reqMap.put("end", end);

        List<BankReg01DTO> list = groupService.getCollectorListAll(reqMap);
        map.put("count", count);
        map.put("modalPager", page);
        map.put("modalNo", 55);
        map.put("list", list);

        return map;
    }
}
