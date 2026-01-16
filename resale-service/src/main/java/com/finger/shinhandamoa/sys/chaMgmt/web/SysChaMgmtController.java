package com.finger.shinhandamoa.sys.chaMgmt.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finger.shinhandamoa.bank.dto.BankReg01DTO;
import com.finger.shinhandamoa.bank.service.BankMgmtService;
import com.finger.shinhandamoa.common.CompanyStatusLookupAPI.IFX1002;
import com.finger.shinhandamoa.common.*;
import com.finger.shinhandamoa.common.FingerIntegrateMessaging.MessageType;
import com.finger.shinhandamoa.data.file.mapper.SimpleFileMapper;
import com.finger.shinhandamoa.data.table.mapper.ChaMapper;
import com.finger.shinhandamoa.data.table.mapper.FwFileMapper;
import com.finger.shinhandamoa.data.table.mapper.WithdrawAgreementMapper;
import com.finger.shinhandamoa.data.table.model.Cha;
import com.finger.shinhandamoa.data.table.model.FwFile;
import com.finger.shinhandamoa.data.table.model.KftcCode;
import com.finger.shinhandamoa.data.table.model.WithdrawAgreement;
import com.finger.shinhandamoa.group.dto.GroupDTO;
import com.finger.shinhandamoa.org.notimgmt.service.NotiService;
import com.finger.shinhandamoa.service.WithdrawAgreementService;
import com.finger.shinhandamoa.sys.chaMgmt.dto.ChaGroup;
import com.finger.shinhandamoa.sys.chaMgmt.dto.ChaVO;
import com.finger.shinhandamoa.sys.chaMgmt.dto.SysChaDTO;
import com.finger.shinhandamoa.sys.chaMgmt.dto.SysChaMgmtDTO;
import com.finger.shinhandamoa.sys.chaMgmt.service.SysChaService;
import com.finger.shinhandamoa.sys.setting.dto.ChaUpdateDTO;
import com.finger.shinhandamoa.sys.setting.dto.VanoPgDTO;
import com.finger.shinhandamoa.sys.setting.service.AutoTranMgmtService;
import com.finger.shinhandamoa.sys.setting.service.VanoMgmtService;
import com.finger.shinhandamoa.vo.PageVO;
import com.finger.shinhandamoa.vo.UserDetailsVO;
import kr.co.finger.damoa.commons.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 기관 관리
 *
 * @author wisehouse@finger.co.kr
 * @since 2018. 4. 6.
 */
@Controller
@RequestMapping("sys")
public class SysChaMgmtController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysChaMgmtController.class);
    @Autowired
    protected UserDetailsService userDetailsService;
    @Autowired
    private ShaEncoder shaEncoder;
    @Autowired
    private ChaMapper chaMapper;
    @Inject
    private SysChaService sysChaService;
    @Inject
    private BankMgmtService bankMgmtService;
    @Inject
    private NotiService notiService;
    @Inject
    private AutoTranMgmtService autoTranMgmtService;
    @Inject
    private VanoMgmtService vanoMgmtService;
    @Value("${fim.server.host}")
    private String host;

    @Value("${fim.server.port}")
    private int port;

    @Value("${fim.accessToken}")
    private String accessToken;

    @Value("${file.path.cms}")
    private String cmsPath;

    @Value("${send.telNo}")
    private String sendtelNo;
    @Value("${pg.allow.url}")
    private String pgAllowUrl;

    @Autowired
    private PlatformTransactionManager transactionManager;

    DefaultTransactionDefinition def = null;
    TransactionStatus status = null;

    /**
     * 기관검색팝업 AJAX
     */
    @ResponseBody
    @RequestMapping("chaMgmt/getCollectorListAjax")
    public HashMap getCollectorListAjax(@RequestBody SysChaDTO body) throws Exception {

        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        int curPage = body.getCurPage();
        int PAGE_SCALE = 5;

        HashMap<String, Object> map = new HashMap<String, Object>();

        reqMap.put("chaCd", body.getChaCd());
        reqMap.put("chaName", body.getChaName());
        if (body.getSearchGb() != null) {
            reqMap.put("searchGb", body.getSearchGb());
        }
        // total count
        int count = sysChaService.selChaListCnt(reqMap);

        PageVO page = new PageVO(count, curPage, PAGE_SCALE);
        int start = page.getPageBegin();
        int end = page.getPageEnd();
        reqMap.put("count", count);
        reqMap.put("start", start);
        reqMap.put("end", end);

        List<SysChaDTO> list = sysChaService.selChaList(reqMap);

        map.put("count", count);
        map.put("pager", page);
        map.put("modalNo", 55);
        map.put("list", list);

        return map;
    }

    /**
     * 기관관리 > 신규기관조회
     */
    @RequestMapping("chaMgmt/newChaList")
    public ModelAndView chnewChaListaList() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("sys/cha/chaMgmt/newChaList");

        return mav;
    }

    /**
     * 기관관리 > 신규기관목록 > 기관등록조회(Ajax)
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("chaMgmt/getNewChaList")
    @ResponseBody
    public HashMap<String, Object> getNewChaList(@RequestBody SysChaDTO body) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {

            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("chaCd", body.getChaCd());
            reqMap.put("chaName", body.getChaName());
            reqMap.put("amtchkTy", body.getAmtchkTy());
            reqMap.put("chaCloseChk", body.getChaCloseChk());
            reqMap.put("chatrty", body.getChatrty());
            reqMap.put("calDateFrom", body.getCalDateFrom());
            reqMap.put("calDateTo", body.getCalDateTo());
            reqMap.put("searchOrderBy", body.getSearchOrderBy());

            // total count
            int count = sysChaService.selNewChaCnt(reqMap);
            // 페이지 관련 설정
            PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            reqMap.put("count", count);
            reqMap.put("start", start);
            reqMap.put("end", end);

            List<SysChaDTO> list = sysChaService.selNewChaList(reqMap);
            map.put("list", list);
            map.put("count", count);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("path", cmsPath);

            map.put("retCode", "0000");
            map.put("retMsg", "정상");

        } catch (Exception e) {
            e.printStackTrace();

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    @Autowired
    private WithdrawAgreementService withdrawAgreementService;
    @Autowired
    private FwFileMapper fwFileMapper;
    @Autowired
    private SimpleFileMapper simpleFileMapper;
    @Autowired
    private WithdrawAgreementMapper withdrawAgreementMapper;
//    /**
//     * 출금동의서 업로드 처리
//     */
//    @RequestMapping("chaMgmt/uploadCmsFile")
//    @ResponseBody
//    public HashMap<String, Object> uploadCmsFile(MultipartHttpServletRequest request) throws Exception {
//        HashMap<String, Object> map = new HashMap<String, Object>();
//
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            String user = authentication.getName();
//
//            MultipartFile inputFile = request.getFile("file");
//            String chaCd = request.getParameter("chaCd");
//            String flag = request.getParameter("flag");
//            String fileName = inputFile.getOriginalFilename();
//
//            //확장자 추출
//            int pos = fileName.lastIndexOf( "." );
//            String ext = fileName.substring( pos );
//            File file = new File(cmsPath + chaCd + ext);
//            File file1 = rename(file); // 파일명 중복시 파일명 변경
//
//            //출금동의서 파일 생성
//            byte [] byteArr=inputFile.getBytes();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            baos.write(byteArr);
//
//            FwFile fwFile = new FwFile();
//            fwFile.setBucket("WITHDRAW_AGREEMENT");
//            fwFile.setName(chaCd + ext);
//            if(ext.toLowerCase().equals(".jpg")){
//                fwFile.setMimeType("image/jpeg");
//            }else if(ext.toLowerCase().equals(".tif")){
//                fwFile.setMimeType("image/tiff");
//            }else{
//                fwFile.setMimeType("audio/mp3");
//            }
//            fwFile.setLength((long) baos.toByteArray().length);
//            fwFile.setCreateDate(new Date());
//            fwFile.setCreateUser(chaCd);
//            fwFileMapper.insert(fwFile);
//
//            simpleFileMapper.store(fwFile.getBucket(), fwFile.getId(), new ByteArrayInputStream(baos.toByteArray()));
//
//            // 3.2. 출금 동의서 등록
//            WithdrawAgreement withdrawAgreement = new WithdrawAgreement();
//            if(ext.toLowerCase().equals(".jpg")){
//                withdrawAgreement.setType("W00001");
//            }else if(ext.toLowerCase().equals(".tif")){
//                withdrawAgreement.setType("W00001");
//            }else{
//                withdrawAgreement.setType("W00002");
//            }
//            withdrawAgreement.setStatus("A00002");
//            withdrawAgreement.setFileId(fwFile.getId());
//            withdrawAgreement.setCreateDate(new Date());
//            withdrawAgreement.setCreateUser(chaCd);
//            withdrawAgreement.setAgreeUserType("W10001");
//            withdrawAgreement.setAgreeUser(chaCd);
//            withdrawAgreementMapper.insert(withdrawAgreement);
//            final Cha cha = chaMapper.selectByPrimaryKey(chaCd);
//            // 4. 기관 변경
//            // 4.1. 상태 변경 (미동의 => 승인대기)
//            cha.setFingerFeeAgreeStatus("A00002");
//            // 4.2. 출금동의서 설정
//            cha.setWithdrawAgreementId(withdrawAgreement.getId());
//            chaMapper.updateByPrimaryKey(cha);
//
//            map.put("maker", user);
//            map.put("flag", flag);            // 수정구분 - 출금동의서 등록
//
//            map.put("chaCd", chaCd);
//            map.put("cmsFileName", file1.getName());
//            map.put("cmsReqSt", "CST01"); 	// 미신청
//            map.put("cmsAppGubn", "CM01");	// 신규
//            map.put("chkCms", "Y");			// CMS출금동의서수령여부
//            map.put("finFeeAgreeSt", "A00002"); // 동의상태
//            map.put("withdrawAgreementId", withdrawAgreement.getId());
//
//            sysChaService.updateChaInfo(map);
//
//            map.put("retCode", "0000");
//            map.put("retMsg", "정상");
//        } catch (Exception e) {
//            e.printStackTrace();
//            map.put("retCode", "9999");
//            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
//        }
//
//        return map;
//    }


    /**
     * 파일명 중복방지
     */
    public File rename(File f) {             //File f는 원본 파일
        if (createNewFile(f)) return f;        //생성된 f가 중복되지 않으면 리턴

        String name = f.getName();
        String body = null;
        String ext = null;

        int dot = name.lastIndexOf(".");
        if (dot != -1) {                              //확장자가 없을때
            body = name.substring(0, dot);
            ext = name.substring(dot);
        } else {                                     //확장자가 있을때
            body = name;
            ext = "";
        }

        int count = 0;
        //중복된 파일이 있을때
        //파일이름뒤에 a숫자.확장자 이렇게 들어가게 되는데 숫자는 9999까지 된다.
        while (!createNewFile(f) && count < 9999) {
            count++;
            String newName = body + "(" + count + ")" + ext;
            f = new File(f.getParent(), newName);
        }
        return f;
    }

    private boolean createNewFile(File f) {
        try {
            return f.createNewFile();                        //존재하는 파일이 아니면
        } catch (IOException ignored) {
            return false;
        }
    }
//
//    /*
//     * 출금동의서 삭제 처리
//     */
//    @RequestMapping("chaMgmt/deleteCmsFile")
//    @ResponseBody
//    public HashMap<String, Object> deleteCmsFile(@RequestBody SysChaDTO dto) throws Exception {
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            String user = authentication.getName();
//            String chaCd = dto.getChaCd();
//            String flag = dto.getFlag();
//            map.put("chaCd", chaCd);
//            map.put("flag", flag);            // 수정구분 - 출금동의서 등록
//            map.put("chkCms", "N");
//            map.put("cmsReqSt", "CST01");
//            map.put("cmsAppGubn", "");  // 신청구분
//            map.put("cmsFileName", "");
//            map.put("maker", user);
//            map.put("cmsReqDt", "");
//            map.put("finFeeAgreeSt", "A00001");
//            map.put("withdrawAgreementId", "");
//
//            sysChaService.updateChaInfo(map);
//
//            map.put("retCode", "0000");
//            map.put("retMsg", "정상");
//        } catch (Exception e) {
//            e.printStackTrace();
//            map.put("retCode", "9999");
//            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
//        }
//        return map;
//
//    }

    /**
     * 신규기관 엑셀 다운로드
     */
    @RequestMapping("chaMgmt/newChaListExcel")
    @ResponseBody
    public View excelDownload(HttpServletRequest request, HttpServletResponse response, SysChaDTO dto, Model model) throws Exception {

        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("chaCd", dto.getChaCd());
        reqMap.put("chaName", dto.getChaName());
        reqMap.put("amtchkTy", dto.getAmtchkTy());
        reqMap.put("chaCloseChk", dto.getChaCloseChk());
        reqMap.put("chatrty", dto.getChatrty());
        reqMap.put("calDateFrom", dto.getCalDateFrom());
        reqMap.put("calDateTo", dto.getCalDateTo());
        reqMap.put("searchOrderBy", dto.getSearchOrderBy());

        // total count
        int count = sysChaService.selNewChaCnt(reqMap);
        // 페이지 관련 설정
        reqMap.put("start", 1);
        reqMap.put("end", count);

        List<SysChaDTO> list = sysChaService.selNewChaList(reqMap);
        model.addAttribute("list", list);

        return new NewChaExcelDownload();
    }

    /**
     * 기관관리 > 이용 기관조회
     */
    @RequestMapping("chaMgmt/chaList")
    public ModelAndView chaList() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("sys/cha/chaMgmt/chaList");

        return mav;
    }

    /**
     * 기관관리 > 이용기관조회
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("chaMgmt/getChaList")
    @ResponseBody
    public HashMap<String, Object> getChaList(@RequestBody HashMap<String, Object> reqMap) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {

            // total count
            int count = sysChaService.selChaCount(reqMap);
            reqMap.put("count", count);
            // 페이지 관련 설정
            PageVO page = new PageVO(count, Integer.parseInt(reqMap.get("curPage").toString()), Integer.parseInt(reqMap.get("pageScale").toString()));
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            reqMap.put("start", start);
            reqMap.put("end", end);

            List<SysChaDTO> list = sysChaService.selChaInfoList(reqMap);
            map.put("list", list);
            map.put("count", count);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("path", cmsPath);

            map.put("retCode", "0000");
            map.put("retMsg", "정상");

        } catch (Exception e) {
            e.printStackTrace();

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 기관관리 > 기관상세조회(Ajax)
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("chaMgmt/selectChaListInfoAjax")
    @ResponseBody
    public HashMap<String, Object> selectChaListDetailAjax(@RequestBody SysChaDTO body) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();

            reqMap.put("chaCd", body.getChaCd());

            SysChaMgmtDTO info = bankMgmtService.selectChaListDetail(reqMap);
            List<HashMap<String, Object>> accountList = bankMgmtService.getAgencyList(reqMap);
            List<KftcCode> bnkList = bankMgmtService.getBnkList(map);

            map.put("info", info);
            map.put("accountList", accountList);
            map.put("bnkList", bnkList);

            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 기관관리 > 기관정보수정(단건) - 기존
     */
    @ResponseBody
    @RequestMapping("chaMgmt/updateChaInfo")
    public HashMap<String, Object> updateChaInfo(@RequestBody BankReg01DTO dto) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();

        final HashMap<String, Object> map = new HashMap<>();
        try {
            final String newOld = dto.getJobType();
            dto.setMaker(user);
            if (newOld.equals("N")) {
                dto.setJobType("N");
                bankMgmtService.updateXChaList(dto);
            } else if(newOld.equals("ACMS")){
                dto.setJobType("ACMS");
                bankMgmtService.updateXChaList(dto);
            } else {
                dto.setJobType("U");
                bankMgmtService.updateXChaList(dto);
                SysChaMgmtDTO sDto = new SysChaMgmtDTO();
                sDto.setChast(dto.getChast());
                sDto.setChaCd(dto.getChaCd());
                bankMgmtService.updateWebuser(sDto);
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

    /**
     * 기관관리 > 기관정보수정
     */
    @Transactional
    @ResponseBody
    @RequestMapping("chaMgmt/updateCha")
    public HashMap<String, Object> updateCha(@RequestBody SysChaMgmtDTO dto) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();

        final HashMap<String, Object> map = new HashMap<>();
        try {
            def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);

            dto.setMaker(user);
            bankMgmtService.updateXchaList(dto);

            sysChaService.insertXadjgroupList(dto);

            bankMgmtService.updateWebuser(dto);

            map.put("retCode", "0000");
            map.put("retMsg", "정상");

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            e.printStackTrace();
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 기관관리 > 이용기관조회 > 기관코드아이디로 기관사이트 로그인
     */
    @RequestMapping("orgMoveAuth")
    public String orgMoveAuth(HttpServletRequest request, HttpServletResponse response, GroupDTO dto) throws Exception {
        final SecurityContext securityContext = SecurityContextHolder.getContext();

        final Authentication systemAuthentication = securityContext.getAuthentication();
        final RunAsAuthentication runAsAuthentication = new RunAsAuthentication(systemAuthentication);

        final UserDetails ckUserDetails = userDetailsService.loadUserByUsername("ORG:///:" + dto.getLoginId());
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

        String role = orgAuthentication.getAuthorities().toString();    // 권한 Collection
        String url = "";
        if (role.contains("ROLE_GROUP_USER")) { // 그룹관리자
            url = "group/groupList";
        } else if (role.contains("ROLE_ADMIN")) { // 그룹사용자
            url = "org/main/index";
        }
        sysChaService.updateSessionMax(reqMap);
        return "redirect:/" + url;
    }

    /**
     * 기관관리 > 이용기관조회 > 잠금해제
     */
    @RequestMapping("chaMgmt/resetFailCntAdm")
    @ResponseBody
    public HashMap resetFailCntAdm(@RequestBody SysChaDTO dto) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {

            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("chaCd", dto.getChaCd());

            sysChaService.resetFailCntAdm(reqMap);
            map.put("retCode", "0000");
            map.put("retMsg", "해당기관의 로그인 실패 횟수가 초기화 되었습니다");

        } catch (Exception e) {
            e.printStackTrace();

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 기관관리 > 이용기관조회 > 비밀번호 초기화
     */
    @RequestMapping("chaMgmt/resetPwAdm")
    @ResponseBody
    public HashMap resetPwAdm(@RequestBody SysChaDTO dto) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {

            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("chaCd", dto.getChaCd());
            reqMap.put("loginPw", shaEncoder.encoding("1111"));

            sysChaService.resetPwAdm(reqMap);
            map.put("retCode", "0000");
            map.put("retMsg", "해당기관의 비밀번호가 1111 로 변경되었습니다");

        } catch (Exception e) {
            e.printStackTrace();

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 기관관리 > 이용기관조회 > 담당자  핸드폰번호 조회
     */
    @RequestMapping("chaMgmt/selChaSmsNo")
    @ResponseBody
    public HashMap<String, Object> selChaSmsNo(@RequestBody SysChaDTO dto) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            if (dto.getEcareNo() != "300") {
                map.put("chaCd", dto.getChaCd());
            }
            map.put("ecareNo", dto.getEcareNo());
            map.put("stList", dto.getStList());
            map.put("flag", dto.getFlag());

            List<SysChaDTO> list = sysChaService.selChrInfoList(map);

            map.put("list", list);
            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            e.printStackTrace();

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 기관관리 > 이용기관조회 > 문자발송
     */
    @ResponseBody
    @RequestMapping("chaMgmt/smsMsgSend")
    public HashMap<String, Object> smsMsgSend(@RequestBody SysChaDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        String telNo = sendtelNo;  //발송자 전화번호
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> imap = new HashMap<String, Object>();

        try {
            FingerIntegrateMessaging fingerIntegrateMessaging = new FingerIntegrateMessaging();

            fingerIntegrateMessaging.setHost(host);
            fingerIntegrateMessaging.setPort(port);
            fingerIntegrateMessaging.setAccessToken(accessToken);

            int smsSeq = sysChaService.selSmsSeq();
            int mLength = dto.getMsg().getBytes("utf-8").length;

            MessageType messageType;
            String type = "";
            if (mLength > 80) {
                messageType = MessageType.MMS;
                type = "1";
            } else {
                messageType = MessageType.SMS;
                type = "0";
            }

            imap.put("smsTy", "30");
            imap.put("smsSeq", smsSeq);
            imap.put("title", dto.getTitle());
            imap.put("msg", dto.getMsg());
            imap.put("sendTelNo", telNo);
            imap.put("msgTy", type);
            imap.put("sendId", user);
            sysChaService.insertSmsSysMng(imap);

            for (int i = 0; i < dto.getStList().size(); i++) {
                String hpNo = dto.getStList().get(i);
                String chaCd = dto.getValList().get(i);

                final ListResult<FingerIntegrateMessaging.Message> listResult = fingerIntegrateMessaging.createMessage(
                        messageType, "", dto.getMsg(), null, telNo, hpNo, "신한다모아"); // sms 발송

                if (listResult.getItemList() != null) {

                    imap.put("smsSysReqCd", listResult.getItemList().get(0).getId());
                    imap.put("chaCd", chaCd);
                    imap.put("rcvChrHp", chaCd);
                    imap.put("sendStatus", 2);

                    sysChaService.insertSmsSysList(imap);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
//
//    /**
//     * email 발송
//     *
//     * @throws Exception
//     */
//    @RequestMapping("chaMgmt/emailMsgSend")
//    @ResponseBody
//    public HashMap<String, Object> emailMsgSend(@RequestBody SysChaDTO dto) throws Exception {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsVO vo = (UserDetailsVO) authentication.getPrincipal();
//        HashMap<String, Object> map = new HashMap<String, Object>();
//
//        try {
//            map.put("billGubn", dto.getBillGubn());
//            String domainUrl = "http://www.shinhandamoa.com";
//
//            if (!"200".equals(dto.getEcareNo()) && !"300".equals(dto.getEcareNo())) {
//                map.put("retCode", "9999");
//                map.put("retMsg", "이케어번호가 입력되지 않았습니다.");
//            } else {
//
//                for (int i = 0; i < dto.getValList().size(); i++) {
//                    StringBuilder sb = new StringBuilder();
//
//                    map.put("chaCd", dto.getValList().get(i));
//                    String changeJunmun = dto.getJonmun().replaceAll("\r|\n", "<br/>");
//                    if ("200".equals(dto.getEcareNo())) {
//                        // 전문,템플릿내용
//                        sb.append("@s@"); // 그룹헤더
//                        sb.append("+");
//                        sb.append("f");
//                        sb.append("+");
//                        sb.append("4");
//                        sb.append("+");
//                        sb.append(changeJunmun);            // 메일내용
//                        sb.append("+");
//                        sb.append(domainUrl);                // 도메인
//                        sb.append("+");
//                        sb.append(dto.getValList().get(i));    // 아이디 기관코드
//                        sb.append("+");
//                        sb.append(dto.getChaOffNo());    // 비밀번호 기관코드+ 사업자번호 4자리
//
//                    } else if ("300".equals(dto.getEcareNo())) {
//                        // 전문,템플릿내용
//                        sb.append("@s@"); // 그룹헤더
//                        sb.append("+");
//                        sb.append("f");
//                        sb.append("+");
//                        sb.append("1");
//                        sb.append("+");
//                        sb.append(changeJunmun);        // 메일내용
//                    }
//
//                    String strJunmun = sb.toString();
//
//                    String mail = dto.getStList().get(i);
//                    // 메일전송
//                    map.put("ecareNo", dto.getEcareNo());        // 이케어번호(100:고지, 200:승인, 300:공지) 200
//                    map.put("channel", "M");                        // 채널
//                    map.put("tmplType", "J");                        // 템플릿타입
//                    map.put("receiverId", dto.getValList().get(i));    // 수신자id  기관코드
//                    map.put("receiverNm", dto.getNmList().get(i));    // 수신자명 ==> 기관명
//                    map.put("receiver", mail);                    // 수신자mail
//                    map.put("senderNm", vo.getName());            // 발신자명
//                    map.put("sender", vo.getEmail());            // 발신자mail
//                    map.put("subject", dto.getTitle());            // 제목    "가입승인"
//                    map.put("sendFg", "R");                        // 발송여부플래그
//                    map.put("jonmun", strJunmun);                // 전문내용
//
//                    map.put("masMonth", " ");                    // 청구월  빈값
//                    map.put("slot2", " ");                        // 고지구분(1:청구고지, 2:납부고지, 3:미납고지) 빈값
//
//                    String seq = notiService.selEmailSeq(dto.getEcareNo());
//                    map.put("seq", seq);
//
//                    notiService.insertEmailSend(map);    // 메일전송
//                    notiService.insertEmailHist(map);    // 메일이력등록
//
//                    if ("200".equals(dto.getEcareNo())) { // 해피콜 완료 update
//                        map.put("preChast", "20");    // 20:완료
//                        map.put("flag", "5");
//                        map.put("chaCd", dto.getValList().get(i));
//                        map.put("maker", vo.getLoginId());
//
//                        sysChaService.updateChaInfo(map);
//                    }
//                }
//            }
//
//            map.put("retCode", "0000");
//            map.put("retMsg", "정상");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            map.put("retCode", "9999");
//            map.put("retMsg", "정상");
//        }
//
//        return map;
//    }

    /**
     * 기관관리 > 이용기관조회 엑셀다운로드
     */
    @ResponseBody
    @RequestMapping("chaMgmt/getSysChaListExcel")
    public View getSysChaListExcel(HttpServletRequest request, HttpServletResponse response, SysChaDTO dto, Model model) throws Exception {
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("chaCd", dto.getChaCd());
        reqMap.put("chaName", dto.getChaName());
        reqMap.put("chast", dto.getChast());
        reqMap.put("chatrty", dto.getChatrty());
        reqMap.put("chaCloseChk", dto.getChaCloseChk());
        reqMap.put("calDateFrom", dto.getCalDateFrom());
        reqMap.put("calDateTo", dto.getCalDateTo());
        reqMap.put("searchOrderBy", dto.getSearchOrderBy());
        reqMap.put("searchGb", dto.getSearchGb());
        reqMap.put("searchValue", dto.getSearchValue());

        // total count
        int count = sysChaService.selChaCount(reqMap);
        reqMap.put("count", count);
        reqMap.put("start", 1);
        reqMap.put("end", count);
        reqMap.put("excel", "excel");

        List<SysChaDTO> list = sysChaService.selChaInfoList(reqMap);
        model.addAttribute("list", list);

        return new ExcelSaveSysChaList();
    }

    /**
     * 기관관리 > 이용기관 등록
     */
    @RequestMapping("chaMgmt/registCha")
    public ModelAndView registCha() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("sys/cha/chaMgmt/registCha");

        return mav;
    }

    /**
     * 기관관리 > 이용기관 등록(Ajax)
     */
    @Transactional
    @ResponseBody
    @RequestMapping("chaMgmt/registChaAjax")
    public HashMap<String, Object> registChaAjax(@RequestBody SysChaMgmtDTO dto) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();
        final String now = DateUtils.toDTType(new Date());
        dto.setMaker(user);
        dto.setRegDt(now);

        HashMap<String, Object> map = new HashMap<>();

        try {
            def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);

            dto.setMaker(user);
            String chacd = bankMgmtService.insertXchalist(dto);
            dto.setChaCd(chacd);

            if(StringUtils.isNotEmpty(chacd)) {
                HashMap<String, Object> smap = new HashMap<String, Object>();

                smap.put("loginid", chacd);
                smap.put("chacd", chacd);

                String lpw = "1111";
                smap.put("loginpw", shaEncoder.encoding(lpw));
                smap.put("idtype", "01");
                smap.put("maker", user);
                smap.put("regdt", now);
                smap.put("loginname", dto.getChaName());
                smap.put("idst", dto.getChast());
                sysChaService.insertWebUser(smap);

                sysChaService.insertXadjgroupList(dto);
            }

            map.put("retCode", "0000");
            map.put("retMsg", "정상");

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            e.printStackTrace();
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 기관관리 > 은행계좌목록(selectBox)
     */
    @ResponseBody
    @RequestMapping("chaMgmt/getBankList")
    public HashMap<String, Object> getBankList(@RequestBody Map<String, Object> paramMap) throws Exception {

        HashMap<String, Object> map = new HashMap<>();
        try {
            List<KftcCode> bankList = bankMgmtService.getBnkList(paramMap);
            map.put("bankList", bankList);
            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("retCode", "9999");
            map.put("retMsg", "작업중 오류가 발생하였습니다..");
        }

        return map;
    }

    /**
     * 기관관리 > 기관검증관리
     */
    @RequestMapping("chaMgmt/chaConfirm")
    public ModelAndView feeList() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("sys/cha/chaMgmt/chaConfirm");

        return mav;
    }

    /**
     * 기관관리 > 기관 검증 관리 > 기관 검증 이력 확인
     */
    @ResponseBody
    @RequestMapping("chaMgmt/getStatusJobHistory")
    public HashMap<String, Object> getStatusJobHistory(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        String reqJobid = StrUtil.nullToVoid(request.getParameter("jobid"));
        String reqStatus = StrUtil.nullToVoid(request.getParameter("status"));

        try {
            if ("".equals(reqJobid)) {
                map.put("retCode", "8888");
                map.put("retMsg", "jobid가 입력되지 않았습니다.");
            } else {
                map.put("jobId", reqJobid);
                if (!"".equals(reqStatus)) {
                    map.put("status", reqStatus);
                }
                SysChaDTO info = sysChaService.selectJobhistoryLast(map);

                map.put("jobhistory", info);
                map.put("retCode", "0000");
                map.put("retMsg", "정상");
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("retCode", "9999");
            map.put("retMsg", "작업중 오류가 발생하였습니다..");
        }
        return map;
    }

    /**
     * 기관관리 > 기관 검증 관리 > 부적합 기관 목록
     */
    @ResponseBody
    @RequestMapping("chaMgmt/selCloseChaList")
    public HashMap<String, Object> selCloseChaList(@RequestBody SysChaDTO dto) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            int count = sysChaService.selCloseChaCount(map);
            // 페이지 관련 설정
            PageVO page = new PageVO(count, dto.getCurPage(), dto.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            map.put("pager", page);
            map.put("count", count);
            map.put("start", start);
            map.put("end", end);

            List<SysChaDTO> list = sysChaService.selCloseChaList(map);
            map.put("list", list);

            map.put("retCode", "0000");
            map.put("retMsg", "정상");

        } catch (Exception e) {
            e.printStackTrace();
            map.put("retCode", "9999");
            map.put("retMsg", "작업중 오류가 발생하였습니다..");
        }

        return map;
    }

    /**
     * 기관관리 > 기관 검증 관리 > 부적합 기관 목록 > 파일저장
     */
    @ResponseBody
    @RequestMapping("chaMgmt/selCloseChaListExcel")
    public View selCloseChaListExcel(HttpServletRequest request, HttpServletResponse response, SysChaDTO dto, Model model) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();

        int count = sysChaService.selCloseChaCount(map);
        map.put("start", 1);
        map.put("end", count);

        List<SysChaDTO> list = sysChaService.selCloseChaList(map);

        model.addAttribute("list", list);

        return new CloseChaExcelDownload();
    }

    /**
     * 기관관리 > 기관 검증 관리 > 부적합 기관 목록 > 강제완료처리
     */
    @ResponseBody
    @RequestMapping("chaMgmt/updateStatusJobHistory")
    public HashMap<String, Object> updateStatusJobHistory(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        String reqJobid = StrUtil.nullToVoid(request.getParameter("jobid"));
        String reqStatus = StrUtil.nullToVoid(request.getParameter("status"));

        try {

            if ("".equals(reqJobid)) {
                map.put("retCode", "8888");
                map.put("retMsg", "jobid가 입력되지 않았습니다.");
            } else {
                map.put("jobid", reqJobid);
                if (!"".equals(reqStatus)) {
                    map.put("status", reqStatus);
                } else {
                    map.put("status", "2");
                }
                map.put("targetstatus", "1");
                map.put("totcnt", 0);
                sysChaService.updateJobhistory(map);

                map.put("retCode", "0000");
                map.put("retMsg", "정상");
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("retCode", "9999");
            map.put("retMsg", "작업중 오류가 발생하였습니다..");


        }
        return map;
    }

    /**
     * 고객관리 > 기관검증관리 > 수동검증
     */
    @ResponseBody
    @RequestMapping("chaMgmt/chkChaConfirmManual")
    public HashMap<String, Object> chkChaConfirmManual(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String reqStatus = StrUtil.nullToVoid(request.getParameter("status"));
        final HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        final String status = "2";   //완료
        final String targetstatus = "1";   //실행중
        int count = 0;

        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            final String user = authentication.getName();
            map.put("jobid", "chkChaConfirmManual");

            // 기관조회
            reqMap.put("chaCloseChk", "CON");
            count = bankMgmtService.getNewChaListTotalCount(reqMap);
            reqMap.put("start", 1);
            reqMap.put("end", count);
            final List<BankReg01DTO> list = bankMgmtService.getNewChaListAll(reqMap);

            if (list == null || list.size() == 0) {
                map.put("retCode", "8888");
                map.put("retMsg", "작업대상건이 존재하지 않습니다.");
            } else {

                LOGGER.info("=== list.size() : === " + list.size());
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        // 히스토리 남기고
                        try {
                            bankMgmtService.insertJobhistory(map);

                            //루프 돌면서 기관처리
                            //START OF FOR
                            BankReg01DTO dto = new BankReg01DTO();
                            int errCnt = 0;

                            for (int i = 0; i < list.size(); i++) {
                                dto.setChaCd(list.get(i).getChaCd());
                                dto.setChaOffNo(list.get(i).getChaOffNo());
                                dto.setMaker(user);
                                dto.setJobType("CONFIRM");
                                IFX1002 ifx1002 = bankMgmtService.chkChaConfirmManual(dto);

                                //정상 케이스  : 11:일반, 12:간이, 21:면세, 22:비영리
                                //부적합 케이스: 00:미확인/오류  , 99:기타, 31:휴업, 32:폐업
                                if ("11".equals(ifx1002.getTaxGbn()) || "12".equals(ifx1002.getTaxGbn())
                                        || "21".equals(ifx1002.getTaxGbn()) || "22".equals(ifx1002.getTaxGbn())) {
                                    dto.setChaCloseChk("N");
                                } else {
                                    dto.setChaCloseChk("Y");
                                    errCnt++;
                                }
                                dto.setChaCloseSt(ifx1002.getTaxGbn());
                                dto.setChaCloseVarDt(ifx1002.getCloseDate());

                                if ("11".equals(ifx1002.getTaxGbn())) { //정상
                                    dto.setChaCloseVarReson("일반정상  최종변경일자 : [" + ifx1002.getUpdateDate() + "] /폐업일자 : [" + ifx1002.getCloseDate() + "]");
                                } else if ("12".equals(ifx1002.getTaxGbn())) {
                                    dto.setChaCloseVarReson("간이 정상  최종변경일자 : [" + ifx1002.getUpdateDate() + "] /폐업일자 : [" + ifx1002.getCloseDate() + "]");
                                } else if ("21".equals(ifx1002.getTaxGbn())) {
                                    dto.setChaCloseVarReson("면세 정상  최종변경일자 : [" + ifx1002.getUpdateDate() + "] /폐업일자 : [" + ifx1002.getCloseDate() + "]");
                                } else if ("22".equals(ifx1002.getTaxGbn())) {
                                    dto.setChaCloseVarReson("비영리 정상  최종변경일자 : [" + ifx1002.getUpdateDate() + "] /폐업일자 : [" + ifx1002.getCloseDate() + "]");
                                } else if ("00".equals(ifx1002.getTaxGbn())) {
                                    dto.setChaCloseVarReson("미확인/오류상태  최종변경일자 : [" + ifx1002.getUpdateDate() + "] /폐업일자 : [" + ifx1002.getCloseDate() + "]");
                                } else if ("99".equals(ifx1002.getTaxGbn())) {
                                    dto.setChaCloseVarReson("기타 오류상태  최종변경일자 : [" + ifx1002.getUpdateDate() + "] /폐업일자 : [" + ifx1002.getCloseDate() + "]");
                                } else if ("31".equals(ifx1002.getTaxGbn())) {
                                    dto.setChaCloseVarReson("휴업상태  최종변경일자 : [" + ifx1002.getUpdateDate() + "] /폐업일자 : [" + ifx1002.getCloseDate() + "]");
                                } else if ("32".equals(ifx1002.getTaxGbn())) {
                                    dto.setChaCloseVarReson("폐업상태  최종변경일자 : [" + ifx1002.getUpdateDate() + "] /폐업일자 : [" + ifx1002.getCloseDate() + "]");
                                } else {
                                    dto.setChaCloseVarReson("상태코드 : [" + ifx1002.getTaxGbn() + "] / 최종변경일자 : [" + ifx1002.getUpdateDate() + "] /폐업일자 : [" + ifx1002.getCloseDate() + "]");
                                }

                                LOGGER.info("=== chkChaConfirmManual.dto : === " + dto);
                                bankMgmtService.updateXChaList(dto);
                            }
                            //END OF FOR
                            // 히스토리 종료.
                            map.put("status", status);
                            map.put("targetstatus", targetstatus);

                            map.put("totcnt", errCnt);
                            sysChaService.updateJobhistory(map);

                        } catch (Exception e1) {
                            LOGGER.error("수동검증중 오류 발생 ==>" + e1.getMessage());
                        }
                    }
                });

                thread.start();

                map.put("retCode", "0000");
                map.put("retMsg", "정상");
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("retCode", "9999");
            map.put("retMsg", "작업중 오류가 발생하였습니다..");
        }
        return map;
    }

    /**
     * 기관관리 > 기관그룹관리
     */
    @RequestMapping("chaMgmt/chaPartnerList")
    public ModelAndView chaPartnerList() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("sys/cha/chaMgmt/chaPartnerList");

        return mav;
    }

    /**
     * 기관관리 > 기관그룹목록조회
     */
    @RequestMapping(value = "chaMgmt/cha-group", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getChaGroupList(@RequestParam(value = "chaCd", defaultValue = "") final String chaCd,
                                               @RequestParam(value = "chaName", defaultValue = "") final String chaName,
                                               @RequestParam(value = "groupId", defaultValue = "") final String groupId,
                                               @RequestParam(value = "groupName", defaultValue = "") final String groupName,
                                               @RequestParam(value = "status", defaultValue = "") final String status,
                                               @RequestParam(value = "pageNo", defaultValue = "1") final int pageNo,
                                               @RequestParam(value = "pageSize", defaultValue = "10") final int pageSize,
                                               @RequestParam(value = "orderBy", defaultValue = "") final String orderBy) {
        final List<Map<String, Object>> itemList = sysChaService.getGroupList(chaCd, chaName, groupId, groupName, status, pageNo, pageSize, orderBy);
        final long totalItemCount = sysChaService.countGroup(chaCd, chaName, groupId, groupName, status);
        final long itemCount = itemList.size();

        final Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("itemList", itemList);
        resultMap.put("itemCount", itemCount);
        resultMap.put("totalItemCount", totalItemCount);

        return resultMap;
    }

    /**
     * 기관관리 > 기관그룹 목록 > 기관그룹아이디로 기관사이트 로그인
     */
    @RequestMapping("groupMoveAuth")
    public String groupMoveAuth(HttpServletRequest request, HttpServletResponse response, GroupDTO dto) throws Exception {
        final SecurityContext securityContext = SecurityContextHolder.getContext();

        final Authentication systemAuthentication = securityContext.getAuthentication();
        final RunAsAuthentication runAsAuthentication = new RunAsAuthentication(systemAuthentication);

        final UserDetails ckUserDetails = userDetailsService.loadUserByUsername("GROUP:///:" + dto.getGroupId());
        final Authentication groupAuthentication = new UsernamePasswordAuthenticationToken(ckUserDetails, ckUserDetails.getPassword(), ckUserDetails.getAuthorities());
        runAsAuthentication.runAs(groupAuthentication);

        securityContext.setAuthentication(runAsAuthentication);

        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        //필요한경우 그룹아이디도 중복로그인 설정여부 확인해서 막기
//        String concurrentBlockYn = "";
//        Map<String, Object> user = null;
//        reqMap.put("username", dto.getGroupId());
//        user = sysChaService.selectConcurrentBlockYn(reqMap);
//        concurrentBlockYn= user.get("CONCURRENTBLOCKYN").toString();

//        if(user != null && concurrentBlockYn.equals("N")){
            //화면이동으로 지원하는 경우는 로그아웃대상에서 빠지도록 해당 세션 lock(use lockyn Y로)
            reqMap.put("sessionId" , session.getId());
            reqMap.put("username", ckUserDetails.getUsername());
            sysChaService.updateSessionMax(reqMap);
//        }

        String role = groupAuthentication.getAuthorities().toString();    // 권한 Collection
        String url = "";
        if (role.contains("ROLE_GROUP_USER")) { // 그룹관리자
            url = "group/groupList";
        } else if (role.contains("ROLE_ADMIN")) { // 그룹사용자
            url = "main";
        }
        return "redirect:/" + url;
    }

    /**
     * 기관관리 > 기관그룹등록
     */
    @RequestMapping(value = "chaMgmt/cha-group", method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public ChaGroup getChaGroupList(@RequestBody ChaGroup chaGroup) throws Exception {
        // 기관그룹 아이디
        final String groupId = sysChaService.getNewGroupId();
        chaGroup.setId(groupId);

        // 기관그룹 테이블
        final Map<String, String> chaGroupMap = new HashMap<>();
        chaGroupMap.put("id", chaGroup.getId());
        chaGroupMap.put("name", chaGroup.getName());
        chaGroupMap.put("remark", chaGroup.getRemark());
        chaGroupMap.put("status", chaGroup.getStatus());
        chaGroupMap.put("transactionType", chaGroup.getTransactionType());
        sysChaService.createGroup(chaGroupMap);

        // 웹 유저 테이블
        final HashMap<String, Object> webUserMap = new HashMap<>();
        webUserMap.put("loginid", chaGroup.getId());
        webUserMap.put("chacd", chaGroup.getId());
        webUserMap.put("loginpw", shaEncoder.encoding(chaGroup.getPassword()));
        webUserMap.put("idtype", "03");
        webUserMap.put("maker", "damoaadm");
        webUserMap.put("regdt", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        webUserMap.put("loginname", chaGroup.getName());
        webUserMap.put("idst", StringUtils.equals(chaGroup.getStatus(), "ST01") ? "ST06" : "ST01");
        sysChaService.createWebUser(webUserMap);

        return chaGroup;
    }

    @RequestMapping(value = "chaMgmt/cha-group/{id}", method = RequestMethod.GET)
    @Transactional
    @ResponseBody
    public ChaGroup getChaGroup(@PathVariable(value = "id") String groupId) {
        final ChaGroup chaGroup = new ChaGroup();

        final Map<String, Object> chaGroupMap = sysChaService.getChaGroup(groupId);
        LOGGER.info("chaGroupMap: {}", chaGroupMap);
        chaGroup.setId(String.valueOf(chaGroupMap.get("CHAGROUPID")));
        chaGroup.setName(String.valueOf(chaGroupMap.get("CHAGROUPNAME")));
        chaGroup.setRemark(String.valueOf(chaGroupMap.get("REMARK") == null ? StringUtils.EMPTY : chaGroupMap.get("REMARK")));
        chaGroup.setStatus(String.valueOf(chaGroupMap.get("GRPST")));
        chaGroup.setTransactionType(String.valueOf(chaGroupMap.get("GRPTRTY")));

        final Map<String, Object> webUserMap = sysChaService.getWebUser(groupId);
        LOGGER.info("webUserMap: {}", webUserMap);

        final List<Map<String, Object>> chaMapList = sysChaService.getChaListByGroupId(groupId);
        LOGGER.info("chaMapList: {}", chaMapList);
        final List<ChaVO> chaVOList = new ArrayList<>();
        for (Map<String, Object> each : chaMapList) {
            final ChaVO chaVO = new ChaVO();
            chaVO.setChaCd(String.valueOf(each.get("CHACD")));
            chaVO.setName(String.valueOf(each.get("CHANAME")));
            chaVO.setOwner(String.valueOf(each.get("OWNER") == null ? "" : each.get("OWNER")));
            chaVO.setOwnerTel(String.valueOf(each.get("OWNERTEL") == null ? "" : each.get("OWNERTEL")));
            chaVO.setChaOffNo(String.valueOf(each.get("CHAOFFNO")));
            chaVO.setRegDt(String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format((Date)each.get("REGDT"))));

            chaVOList.add(chaVO);
        }
        chaGroup.setChaVOList(chaVOList);

        return chaGroup;
    }

    @RequestMapping(value = "chaMgmt/cha-group/{groupId}/{chaCd}", method = RequestMethod.DELETE)
    @Transactional
    @ResponseBody
    public String removeChaFromGroup(@PathVariable("groupId") String groupId,
                                     @PathVariable("chaCd") String chaCd) {
        sysChaService.removeChaFromGroup(groupId, chaCd);

        return "";
    }

    @RequestMapping(value = "chaMgmt/cha-group/{groupId}/{chaCd}", method = RequestMethod.PUT)
    @Transactional
    @ResponseBody
    public String moveChaToGroup(@PathVariable("groupId") String groupId,
                                 @PathVariable("chaCd") String chaCd) {
        sysChaService.moveChaToGroup(groupId, chaCd);

        return "";
    }

    @RequestMapping(value = "chaMgmt/cha", method = RequestMethod.GET)
    @ResponseBody
    public List<ChaVO> getChaList(@RequestParam(value="query", defaultValue = "") String query) {
        if(StringUtils.isBlank(query)) {
            return new ArrayList<>();
        }

        final int pageNo = 1;
        final int pageSize = 10;
        final List<Map<String, Object>> chaMapList = sysChaService.getChaList(query, pageNo, pageSize);

        final List<ChaVO> chaVOList = new ArrayList<>();
        for (Map<String, Object> each : chaMapList) {
            final ChaVO chaVO = new ChaVO();
            chaVO.setChaCd(String.valueOf(each.get("CHACD")));
            chaVO.setName(String.valueOf(each.get("CHANAME")));
            chaVO.setOwner(String.valueOf(each.get("OWNER") == null ? "" : each.get("OWNER")));
            chaVO.setOwnerTel(String.valueOf(each.get("OWNERTEL") == null ? "" : each.get("OWNERTEL")));
            chaVO.setChaOffNo(String.valueOf(each.get("CHAOFFNO")));
            chaVO.setRegDt(String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format((Date)each.get("REGDT"))));

            chaVOList.add(chaVO);
        }

        return chaVOList;
    }

    @RequestMapping(value="chaMgmt/cha-group/{id}", method=RequestMethod.PUT)
    @ResponseBody
    public String modifyChaGroup(@PathVariable(value="id") String groupId, @RequestBody ChaGroup chaGroup) {
        // 기관그룹 테이블
        final Map<String, String> chaGroupMap = new HashMap<>();
        chaGroupMap.put("id", groupId);
        chaGroupMap.put("name", chaGroup.getName());
        chaGroupMap.put("remark", chaGroup.getRemark());
        chaGroupMap.put("status", chaGroup.getStatus());
        chaGroupMap.put("transactionType", chaGroup.getTransactionType());
        sysChaService.modifyGroup(chaGroupMap);

        // 웹 유저 테이블
        final HashMap<String, Object> webUserMap = new HashMap<>();
        webUserMap.put("chacd", chaGroup.getId());
        if(StringUtils.isNotBlank(chaGroup.getPassword())) {
            webUserMap.put("loginpw", shaEncoder.encoding(chaGroup.getPassword()));
        }
        webUserMap.put("loginname", chaGroup.getName());
        webUserMap.put("idst", StringUtils.equals(chaGroup.getStatus(), "ST01") ? "ST06" : "ST01");
        sysChaService.modifyWebUser(webUserMap);

        return "";
    }

    /**
     * 기관관리 > 변경대기목록
     */
    @RequestMapping("chaMgmt/changeChaList")
    public ModelAndView changeChaList() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("sys/cha/chaMgmt/changeChaList");

        return mav;
    }

    /**
     * 기관관리 > 변경대기목록 (Ajax)
     */
    @RequestMapping("chaMgmt/getChangeChaList")
    @ResponseBody
    public HashMap<String, Object> getChangeChaList(@RequestBody SysChaDTO body) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        try {
            int count = sysChaService.getChangeChaListCnt(body);

            PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();

            List<SysChaDTO> list = sysChaService.getChangeChaList(body, count, start, end);
            map.put("list", list);
            map.put("count", count);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("path", cmsPath);

            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            e.printStackTrace();

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 변경대기목록 엑셀다운로드
     */
    @RequestMapping("chaMgmt/changeChaListExcel")
    @ResponseBody
    public View changeChaListExcel(HttpServletRequest request, HttpServletResponse response, SysChaDTO body, Model model) throws Exception {
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        int count = sysChaService.getChangeChaListCnt(body);
        int start = 1;
        int end = count;

        List<SysChaDTO> list = sysChaService.getChangeChaList(body, count, start, end);
        model.addAttribute("list", list);

        return new ChangeChaListExcel();
    }

    /**
     * 변경대기목록 > 상세보기
     */
    @RequestMapping("chaMgmt/changeChaListInfo")
    @ResponseBody
    public HashMap<String, Object> changeChaListInfo(@RequestBody SysChaDTO dto) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            SysChaDTO list = sysChaService.changeChaListInfo(dto);
            map.put("list", list);

            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            e.printStackTrace();

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }


    /**
     * 변경대기목록 > 실행버튼 ( STATUS_CD 를 C10002 대기 상태로 변경 )
     */
    @ResponseBody
    @RequestMapping("chaMgmt/updateChangeChaInfo")
    public HashMap<String, Object> updateChangeChaInfo(@RequestBody SysChaDTO dto) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            sysChaService.updateChangeChaInfo(dto);

            map.put("retCode", "0000");
            map.put("retMsg", "정상");

        } catch (Exception e) {
            e.printStackTrace();

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }


    /**
     * PG > 변경대기목록
     */
    @RequestMapping("chaMgmt/pgChaUpdate")
    public ModelAndView pgChaUpdateView() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("sys/pg/pg-cha-update");

        return mav;
    }

    /**
     * PG 변경전문 등록요청 리스트
     */

    @ResponseBody
    @RequestMapping("chaMgmt/pgChaUpdateList")
    public HashMap<String, Object> pgChaUpdateList(@RequestBody ChaUpdateDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {



        LOGGER.debug("======pgChaUpdateList==========={}",body);

        HashMap<String, Object> result = new HashMap<>();

        int totCount = sysChaService.pgUpdateChaInfoListCnt(body);


        PageVO page = new PageVO(totCount, body.getCurPage(), body.getPageScale());
        int start = page.getPageBegin();
        int end = page.getPageEnd();

        body.setStart(start);
        body.setEnd(end);

        LOGGER.debug("start.toString() {}",start);
        LOGGER.debug("end.toString() {}",end);



        List<ChaUpdateDTO> resultList =  sysChaService.pgUpdateChaInfoList(body);


        result.put("resultList",resultList);
        result.put("totalItemCount",totCount);

        return result;


    }

    @ResponseBody
    @RequestMapping("chaMgmt/pgChaUpdateAjax")
    public HashMap<String, Object> pgChaUpdateAjax(@RequestBody ChaUpdateDTO body, HttpServletRequest request) throws Exception {
        HashMap<String, Object> result = new HashMap<>();

        LOGGER.debug("======pgChaUpdateAjax==========={}",body);


        sysChaService.pgUpdateCha(body);



        result.put("retCode", "0000");
        result.put("retMsg", "정상승인");

        return result;

    }


    /**
     * PG 기관관리 > 기관상태 수정(단건)
     */
    @ResponseBody
    @RequestMapping("chaMgmt/updateChaInfoConfig")
    public HashMap<String, Object> updateChaInfoConfig(@RequestBody SysChaDTO dto) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> smap = new HashMap<String, Object>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        try {
            dto.setMaker(user);
            map = sysChaService.updateChaInfo(dto);

        } catch (Exception e) {
            e.printStackTrace();

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }







}
