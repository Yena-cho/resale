package com.finger.shinhandamoa.org.claimMgmt.web;

import com.finger.shinhandamoa.common.*;
import com.finger.shinhandamoa.common.FingerIntegrateMessaging.MessageType;
import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimDTO;
import com.finger.shinhandamoa.org.claimMgmt.service.ClaimExcelService;
import com.finger.shinhandamoa.org.claimMgmt.service.ClaimItemService;
import com.finger.shinhandamoa.org.claimMgmt.service.ClaimService;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiSendDTO;
import com.finger.shinhandamoa.org.notimgmt.service.NotiService;
import com.finger.shinhandamoa.util.dto.CodeDTO;
import com.finger.shinhandamoa.util.service.CodeService;
import com.finger.shinhandamoa.util.service.VarParamInfoService;
import com.finger.shinhandamoa.vo.PageVO;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by puki
 * @date 2018. 4. 10.
 * @desc 최초생성
 */
@Controller
@RequestMapping("org/claimMgmt/**")
public class ClaimController {

    private static final Logger logger = LoggerFactory.getLogger(ClaimController.class);

    // 청구항목 Service
    @Inject
    private ClaimService claimService;

    @Inject
    private ClaimExcelService claimExcelService;

    @Inject
    private ClaimItemService claimItemService;

    @Inject
    private VarParamInfoService varParamInfoService;

    @Inject
    private NotiService notiService;

    // 공통코드 Service
    @Inject
    private CodeService codeService;

    @Value("${fim.server.host}")
    private String host;

    @Value("${fim.server.port}")
    private int port;

    @Value("${fim.accessToken}")
    private String accessToken;

    @Value("${finger.alarmTalk.host}")
    private String atUrl;

    @Value("${finger.alarmTalk.serviceKey}")
    private String atServiceKey;

    /*
     * 청구관리 > 청구등록
     */
    @RequestMapping("claimReg")
    public ModelAndView claimRegList(@RequestParam(defaultValue = "N") String delYN,
                                     @RequestParam(defaultValue = "cusName") String search_orderBy,
                                     @RequestParam(defaultValue = "1") int curPage,
                                     @RequestParam(defaultValue = "10") int PAGE_SCALE
    ) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        String notiMasSt = "PA00";
        logger.debug("기관 {} 청구등록", chaCd);
        ModelAndView mav = new ModelAndView();
        try {
            String masMonth = claimService.selectClaimMonth(chaCd, notiMasSt);
            HashMap<String, Object> cmap = new HashMap<String, Object>();
            cmap.put("chaCd", chaCd);
            cmap.put("masMonth", masMonth);
            cmap.put("notiMasSt", notiMasSt);
            cmap.put("search_orderBy", search_orderBy);

            // total count
            int count = claimService.selectClaimTotalCnt(cmap);

            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("count", count);

            // 청구월
            map.put("claimMonth", masMonth);

            // 청구항목코드
            List<CodeDTO> claimItemCd = codeService.claimItemCd(chaCd);
            map.put("claimItemCd", claimItemCd);

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            map.put("cusGbList", cusGbList);
            map.put("cusGbCnt", cusGbList.size());
            map.put("delYN", delYN);

            mav.addObject("map", map);
            mav.setViewName("org/claimMgmt/claimReg");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        return mav;
    }

    /*
     * 청구관리 > 청구등록 > 화면내 조회
     */
    @ResponseBody
    @RequestMapping("claimRegAjax")
    public HashMap<String, Object> claimRegAjaxList(@RequestBody ClaimDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        String selGb = "M";

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            logger.debug("기관 {} 청구조회", chaCd);
            String notiMasSt = "PA00";
            String masMonth = claimService.selectClaimMonth(chaCd, notiMasSt);

            HashMap<String, Object> cmap = new HashMap<String, Object>();
            cmap.put("chaCd", chaCd);
            cmap.put("selGb", selGb);
            cmap.put("masMonth", masMonth);
            cmap.put("notiMasSt", dto.getNotiMasSt());
            cmap.put("search_orderBy", dto.getSearch_orderBy());

            // total count
            int count = claimService.selectClaimTotalCnt(cmap);
            map.put("count", count);

            // 고객건수
            int clientCount = claimService.selectClaimClientCnt(cmap);
            map.put("clientCount", clientCount);

            //청구금액
            map.put("payAmtSum", claimService.selClaimRegSum(cmap));

            // 페이지 관련 설정
            PageVO page = new PageVO(count, dto.getCurPage(), dto.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            cmap.put("start", start);
            cmap.put("end", end);

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            map.put("cusGbCnt", cusGbList.size());

            List<ClaimDTO> list = claimService.selectClaimAll(cmap);
            String vano = "";
            for (ClaimDTO claimDTO : list) {
                if (claimDTO.getVano() != null) {
                    vano = claimDTO.getVano().substring(0, 3) + "-" + claimDTO.getVano().substring(3, 6) + "-" + claimDTO.getVano().substring(6);
                    claimDTO.setVano(vano);
                }
            }

            map.put("list", list);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("delYN", dto.getDelYN());
            map.put("search_orderBy", dto.getSearch_orderBy());
            map.put("PAGE_SCALE", dto.getPageScale());
            map.put("masMonth", masMonth);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return map;
    }

    /*
     * 청구관리 > 청구등록 > 대량등록 실패내역
     */
    @ResponseBody
    @RequestMapping("claimRegModalAjax")
    public HashMap<String, Object> claimRegAjaxModalList(@RequestBody ClaimDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            logger.debug("기관 {} 청구실패내역", chaCd);
            // 청구 실패 목록
            int count = claimExcelService.failTotalCount(chaCd);
            // 페이지 관련 설정
            PageVO page = new PageVO(count, dto.getCurModalPage(), 5);
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            map.put("modalPager", page);
            map.put("modalNo", 2);

            List<ClaimDTO> list = claimExcelService.failList(chaCd, start, end);
            String vano = "";
            for (ClaimDTO claimDTO : list) {
                if (claimDTO.getVano() != null) {
                    vano = claimDTO.getVano().substring(0, 3) + "-" + claimDTO.getVano().substring(3, 6) + "-" + claimDTO.getVano().substring(6);
                    claimDTO.setVano(vano);
                }
            }
            map.put("failList", list);
            map.put("failCount", count);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return map;
    }

    /*
     * 청구관리 > 청구등록 - 기존 임시 데이터 삭제
     */
    @ResponseBody
    @RequestMapping("claimRegDel")
    public void claimRegDel(@RequestBody ClaimDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        try {
            // 기존 임시 데이터 삭제
            logger.debug("기관 {} 팝업임시청구대상삭제" + dto.getItemList(), chaCd);
            claimService.deleteClaim(chaCd, dto.getNotiMasSt());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    /*
     * 청구관리 > 청구 대상 삭제
     * FIXME @author jhjeong@finger.co.kr
     * PA00 임시상태의 청구항목을 삭제
     * 삭제시 청구마스터가 청구항목0건으로 남는 오류가 있어 수정함
     */
    @ResponseBody
    @RequestMapping("claimDel")
    public Map<String, Object> claimDelete(@RequestBody ClaimDTO dto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        try {
            logger.debug("기관 {} 개별임시청구대상삭제" + dto.getItemList(), chaCd);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        return claimService.selectedToDelClaimDetList(dto.getItemList());
    }

    /*
     * 청구관리 > 청구 대상 상세
     */
    @ResponseBody
    @RequestMapping("detailClaim")
    public HashMap<String, Object> claimRegDetail(String notiMasCd, String viewType) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> smap = new HashMap<String, Object>();
        try {
            logger.debug("기관 {} 청구상세 {}", chaCd, notiMasCd);
            ClaimDTO dto = claimService.selectClaimDetail(notiMasCd);

            if (dto.getVano() != null) {
                dto.setVano(dto.getVano().substring(0, 3) + "-" + dto.getVano().substring(3, 6) + "-" + dto.getVano().substring(6));
            } else {
                dto.setVano(StrUtil.nullToVoid(dto.getVano()));
            }

            smap.put("notiMasCd", dto.getNotiMasCd());
            if (StrUtil.nullToVoid(viewType).equals("V")) {
                smap.put("viewType", viewType);
            }

            // 청구항목코드
            List<CodeDTO> claimItemCd = codeService.claimItemCd(chaCd);
            map.put("claimItemCd", claimItemCd);

            // 청구항목정보
            List<ClaimDTO> list = claimService.selItemList(smap);
            map.put("det", dto);
            map.put("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return map;
    }

    /*
     * 청구관리 > 청구등록 > 임시저장
     */
    @ResponseBody
    @RequestMapping("claimTmpSave")
    public void claimTmpSave(@RequestParam(value = "itemList[]") List<String> list,
                             @RequestParam(value = "idxList[]") List<String> idxList) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        try {
            logger.debug("기관 {} 청구임시저장", chaCd);
            HashMap<String, Object> map = new HashMap<String, Object>();

            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < idxList.size(); j++) {
                    if (i == j) {

                        map.put("chaCd", chaCd);
                        map.put("notiDetCd", list.get(i));
                        map.put("payItemAmt", Integer.parseInt(idxList.get(j)));

                        claimService.updateClaimTemp(map);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    /*
     * 청구관리 > 청구등록 > 일괄 적용
     */
    @ResponseBody
    @RequestMapping("claimRef")
    public HashMap updateClaimModify(@RequestBody ClaimDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        
        logger.info("===== updateClaimModify()  chaCd = {}", chaCd); 

        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            logger.debug("기관 {} 청구등록 일괄적용", chaCd);
            String masSt = "PA00";  // 임시

            // 기존 청구월
            String masMonth = claimService.selectClaimMonth(chaCd, masSt);

            map.put("chaCd", chaCd);
            
            //XNOTIMAS
            map.put("upMonth", dto.getMasMonth());
            map.put("startDate", dto.getStartDate());    // 입금가능기간
            map.put("endDate", dto.getEndDate());
            map.put("printDate", dto.getPrintDate());    // 고지서용 표시일            
            map.put("masMonth", masMonth);            

            //XNOTIDET
            map.put("payItemCd", dto.getPayItemCd());    // 청구항목
            map.put("payItemAmt", dto.getPayItemAmt());	 // 청구금액

            int rst = claimService.updateClaimSave(map);
            map.put("updateCnt", rst);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return map;
    }

    /*
     * 청구관리 > 청구등록 > 청구등록
     */
    @ResponseBody
    @RequestMapping("claimInsert")
    public HashMap<String, Object> insertClaimReg() throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            logger.debug("기관 {} 청구등록 임시 > 미납", chaCd);
            String masSt = "PA00";  // 임시
            String selGb = "M";     // 청구월 기준
            String uMasSt = "PA02";  // 미납

            HashMap<String, Object> smap = new HashMap<String, Object>();
            smap.put("chaCd", chaCd);
            smap.put("selGb", selGb);
            smap.put("masSt", masSt);
            smap.put("notiMasSt", uMasSt);
            String masMonth = claimService.selctMasMonth(smap);
            smap.put("masMonth", masMonth);

            // 청구시 sms 자동 발송인 경우 - sms발송
            String notSmsYn = claimService.autoSmsSendYn(smap);
            if (notSmsYn.equals("A")) {
                notSmsSendAuto(smap); // 청구 sms 자동발송
            }

            // 청구시 알림톡 자동 발송인 경우 - 알림톡 발송
            String notAtYn = claimService.autoAtSendYn(smap);
            if (notAtYn.equals("A")) {
                notAtSendAuto(smap); // 청구 알림톡 자동발송
            }

            HashMap<String, Object> cmap = new HashMap<String, Object>();
            cmap.put("chaCd", chaCd);
            cmap.put("selGb", selGb);
            cmap.put("masMonth", masMonth);
            cmap.put("notiMasSt", "PA00");
            cmap.put("start", 1);
            cmap.put("end", Integer.MAX_VALUE);
            cmap.put("strList", Arrays.asList("N"));
            List<ClaimDTO> list = claimService.selectClaimAll(cmap);
            // 고객정보업데이트
            for (ClaimDTO claimDTO : list) {
                cmap.put("cusName", claimDTO.getCusName());
                if (claimDTO.getVano() != null) {
                    cmap.put("vano", StringUtils.replaceAll(claimDTO.getVano(), "-", ""));
                } else {
                    cmap.put("vano", StrUtil.nullToVoid(claimDTO.getVano()));
                }
                if (CmmnUtils.empty(claimDTO.getCusGubn1()) && CmmnUtils.empty(claimDTO.getCusGubn2()) && CmmnUtils.empty(claimDTO.getCusGubn3()) && CmmnUtils.empty(claimDTO.getCusGubn4())) {
                } else {
                    cmap.put("cusGubn1", StringUtils.defaultString(claimDTO.getCusGubn1(), ""));
                    cmap.put("cusGubn2", StringUtils.defaultString(claimDTO.getCusGubn2(), ""));
                    cmap.put("cusGubn3", StringUtils.defaultString(claimDTO.getCusGubn3(), ""));
                    cmap.put("cusGubn4", StringUtils.defaultString(claimDTO.getCusGubn4(), ""));
                }
                claimService.updateCusPreClaim(cmap);
            }

            //PA00 -> PA02
            claimService.updateClaimNoti(smap);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return map;
    }

    /*
     * 청구관리 > 청구등록 > 개별청구등록 > 고객검색
     */
    @ResponseBody
    @RequestMapping("searchCusName")
    public HashMap<String, Object> searchSingleCusName(ClaimDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            logger.debug("기관 {} 개별청구 고객검색", chaCd);
            HashMap<String, Object> cmap = new HashMap<String, Object>();
            cmap.put("chaCd", chaCd);
            cmap.put("selCusCk", dto.getSelCusCk());
            cmap.put("cusName", dto.getCusName());
            cmap.put("tbgubn", dto.getTbgubn());

            int count = claimService.searchCusNameCnt(cmap);
            map.put("count", count);

            // 페이지 관련 설정
            PageVO page = new PageVO(count, dto.getCurPage(), 15);
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            cmap.put("start", start);
            cmap.put("end", end);

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            map.put("cusGbList", cusGbList);
            map.put("cusGbCnt", cusGbList.size());

            List<ClaimDTO> list = claimService.searchCusName(cmap);
            String vano = "";
            for (ClaimDTO claimDTO : list) {
                if (claimDTO.getVano() != null) {
                    vano = claimDTO.getVano().substring(0, 3) + "-" + claimDTO.getVano().substring(3, 6) + "-" + claimDTO.getVano().substring(6);
                    claimDTO.setVano(vano);
                }
            }
            map.put("list", list);
            map.put("modalPager", page);
            map.put("modalNo", 1);
            map.put("cusName", dto.getCusName());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return map;
    }

    /*
     * 청구관리 > 청구등록 > 개별청구등록 > 고객검색 > 상세정보
     */
    @ResponseBody
    @RequestMapping("cusNameInfo")
    public HashMap<String, Object> customerInfoSel(String vano) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            logger.debug("기관 {} 개별청구 고객검색상세", chaCd);
            HashMap<String, Object> smap = new HashMap<String, Object>();

            // 청구항목코드
            List<CodeDTO> claimItemCd = codeService.claimItemCd(chaCd);
            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            map.put("cusGbList", cusGbList);
            map.put("cusGbCnt", cusGbList.size());

            map.put("claimItemCd", claimItemCd);

            smap.put("chaCd", chaCd);
            if (vano != null) {
                smap.put("vano", StringUtils.replaceAll(vano, "-", ""));
            } else {
                smap.put("vano", StrUtil.nullToVoid(vano));
            }
            ClaimDTO dto = claimService.searchCustomer(smap);
            if (dto.getVano() != null) {
                vano = dto.getVano().substring(0, 3) + "-" + dto.getVano().substring(3, 6) + "-" + dto.getVano().substring(6);
                dto.setVano(vano);
            }
            map.put("dto", dto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return map;
    }

    @Autowired
    private PlatformTransactionManager transactionManager;

    DefaultTransactionDefinition def = null;
    TransactionStatus status = null;

    /*
     * 청구관리 > 청구등록 > 개별청구등록
     */
    @Transactional
    @ResponseBody
    @RequestMapping("singleAdd")
    public HashMap<String, Object> singleClaimInsert(@RequestBody ClaimDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            logger.debug("기관 {} 개별청구등록", chaCd);
            def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

            status = transactionManager.getTransaction(def);

            dto.setChaCd(chaCd);

            HashMap<String, Object> smap = new HashMap<String, Object>();
            smap.put("chaCd", chaCd);
            if (dto.getVano() != null) {
                smap.put("vano", StringUtils.replaceAll(dto.getVano(), "-", ""));
            } else {
                smap.put("vano", StrUtil.nullToVoid(dto.getVano()));
            }

            if (dto.getCusName() != null && !dto.getCusName().equals("") && dto.getCusName().getBytes().length < 51) {
                smap.put("cusName", StrUtil.nullToVoid(dto.getCusName()));
            } else {
                throw new Exception("올바른 고객명을 입력해주세요.");
            }
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            boolean c1 = false;
            switch (cusGbList.size()) {
                case 1:
                    c1 = CmmnUtils.notEmpty(dto.getCusGubn1());
                    break;
                case 2:
                    c1 = CmmnUtils.notEmpty(dto.getCusGubn1()) && CmmnUtils.notEmpty(dto.getCusGubn2());
                    break;
                case 3:
                    c1 = CmmnUtils.notEmpty(dto.getCusGubn1()) && CmmnUtils.notEmpty(dto.getCusGubn2()) && CmmnUtils.notEmpty(dto.getCusGubn3());
                    break;
                case 4:
                    c1 = CmmnUtils.notEmpty(dto.getCusGubn1()) && CmmnUtils.notEmpty(dto.getCusGubn2()) && CmmnUtils.notEmpty(dto.getCusGubn3()) && CmmnUtils.notEmpty(dto.getCusGubn4());
                    break;
                default:
            }
            if (c1) {
                smap.put("cusGubn1", StrUtil.nullToVoid(dto.getCusGubn1()));
                smap.put("cusGubn2", StrUtil.nullToVoid(dto.getCusGubn2()));
                smap.put("cusGubn3", StrUtil.nullToVoid(dto.getCusGubn3()));
                smap.put("cusGubn4", StrUtil.nullToVoid(dto.getCusGubn4()));
                smap.put("cusGubn", "changed");
            }
            if(dto.getMasMonth().length() > 6 || !StringUtils.isNumeric(dto.getMasMonth())){
                throw new Exception("올바른 청구월을 입력해주세요.");
            }
            smap.put("masMonth", StrUtil.nullToVoid(dto.getMasMonth()));
            if(dto.getMasDay().length() > 8 || !StringUtils.isNumeric(dto.getMasDay())){
                throw new Exception("올바른 청구일을 입력해주세요.");
            }
            smap.put("masDay", StrUtil.nullToVoid(dto.getMasDay()));
            if(dto.getStartDate().length() > 8 || !StringUtils.isNumeric(dto.getStartDate())){
                throw new Exception("올바른 수납시작일을 입력해주세요.");
            }
            smap.put("startDate", StrUtil.nullToVoid(dto.getStartDate()));
            if(dto.getEndDate().length() > 8 || !StringUtils.isNumeric(dto.getEndDate())){
                throw new Exception("올바른 수납마감일을 입력해주세요.");
            }
            smap.put("endDate", StrUtil.nullToVoid(dto.getEndDate()));
            if(!StringUtils.isBlank(dto.getPrintDate()) && (dto.getPrintDate().length() > 8 || !StringUtils.isNumeric(dto.getPrintDate()))){
                throw new Exception("올바른 고지서표기일을 입력해주세요.");
            }
            smap.put("printDate", StrUtil.nullToVoid(dto.getPrintDate()));
            smap.put("remark", StringEscapeUtils.unescapeHtml4(StrUtil.nullToVoid(dto.getRemark())));
            smap.put("notiDetSt", "PA00");
            smap.put("notiMasSt", "PA00");
            int idx = 9;
            int cnt = 0;
            int no = 0;
            String dNotiMasCd = claimService.selectNotiMasCd(smap);
            if (dNotiMasCd != null) { // 추가
                smap.put("notiMasCd", dNotiMasCd);
            } else { // 신규청구 또는 추가청구
                String masNo = claimService.selNotiMasCd(smap).getNotiMasCd();
                smap.put("notiMasCd", masNo);
            }

            claimService.insClaimRegMas(smap); // 청구 mas table merge
            for (int i = 0; i < dto.getIdxList().size(); i++) {
                smap.put("payItemCd", dto.getIdxList().get(i));

                int num = claimService.selDupPayItem(smap); // 중복항목확인--
                cnt = claimItemService.cusPayItemCnt(smap); // 항목갯수
                if (cnt < 9 && num == 0) {
                    smap.put("payItemAmt", Integer.parseInt(dto.getItemList().get(i)));
                    smap.put("ptrItemRemark", StringEscapeUtils.unescapeHtml4(dto.getStrList().get(i)));

                    claimService.insClaimRegDet(smap);
                } else {
                    if (cnt > 8) {
                        idx++;
                    } else if (num > 0) {
                        no++;
                    }
                }
            }

            transactionManager.commit(status);

            map.put("useCnt", no);
            map.put("itemCnt", idx);

        } catch (Exception e) {
            transactionManager.rollback(status);

            logger.error(e.getMessage());

            throw e;
        }

        return map;
    }

    /*
     * 청구관리 > 청구등록 > 청구등록수정 > 항목삭제
     */
    @ResponseBody
    @RequestMapping("chargeItemDel")
    public HashMap<String, Object> chargeItemDelete(String notiDetCd) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            logger.debug("기관 {} 청구등록수정 > 항목삭제", chaCd);
            ClaimDTO dto = claimService.selDetToMasNo(notiDetCd);
            map.put("notiMasCd", dto.getNotiMasCd());
            map.put("notiDetCd", notiDetCd);
            int cnt = claimService.selDetCdCnt(map);
            if (cnt == 1) {
//				claimService.selClaimDelete(map);
                map.put("retCode", "0001");
            } else {
                claimService.delClaimItem(map);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
        return map;
    }

    /*
     * 청구관리 > 청구등록 > 청구등록수정
     */
    @ResponseBody
    @RequestMapping("chargeItemSave")
    public void modifyClaimItem(@RequestBody ClaimDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        try {
            logger.debug("기관 {} 청구등록수정", chaCd);
            dto.setChaCd(chaCd);

            HashMap<String, Object> smap = new HashMap<String, Object>();
            smap.put("chaCd", chaCd);
            if (dto.getVano() != null) {
                smap.put("vano", StringUtils.replaceAll(dto.getVano(), "-", ""));
            } else {
                smap.put("vano", StrUtil.nullToVoid(dto.getVano()));
            }
            smap.put("masMonth", dto.getMasMonth());
            smap.put("startDate", dto.getStartDate());
            smap.put("endDate", dto.getEndDate());
            smap.put("printDate", dto.getPrintDate());
            smap.put("remark", StringEscapeUtils.unescapeHtml4(dto.getRemark()));
            smap.put("notiMasCd", dto.getNotiMasCd());
            smap.put("notiDetSt", dto.getNotiDetSt());
            smap.put("notiMasSt", dto.getNotiMasSt());
            smap.put("notiDetCd", dto.getIdxList());
            claimService.insClaimRegMas(smap); // 청구서 등록 및 수정

            // 청구서 상세 등록
            // 수정할경우 모든 det 삭제후 다시 insert (동적테이블 때문에)
            // claimService.selClaimDeleteDetAll(smap);
            // 2018. 11. 8. 일부납일 경우 때문에, 수정 한 det만 삭제후 재등록
            // 2018. 11. 9. 재등록 시 수정값에 완납만 남아있을 경우 청구서는 완납으로 변경한다.
            logger.debug("청구수정시det일단삭제" + smap);
            claimService.selClaimDeleteDetB(smap);

            for (int i = 0; i < dto.getIdxList().size(); i++) {
                // 중복은 update, 신규 insert
                smap.put("payItemCd", dto.getIdxList().get(i));
                smap.put("payItemAmt", Integer.parseInt(dto.getItemList().get(i)));
                smap.put("ptrItemRemark", StringEscapeUtils.unescapeHtml4(dto.getStrList().get(i)));

                claimService.insClaimRegDet(smap);
            }
            logger.debug("청구수정시claimMasUpdate" + smap);
            claimService.claimMasUpdate(smap);

        } catch (Exception e) {
            logger.error(e.getMessage());

            throw e;
        }
    }

    /*
     * 청구관리 > 청구등록 > 이전청구정보 불러오기 - 이전청구월정보 year
     */
    @ResponseBody
    @RequestMapping("beforeMasYear")
    public HashMap<String, Object> selBeforeMasYear() throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            logger.debug("기관 {} 이전청구월정보 year", chaCd);
            List<ClaimDTO> list = claimService.selBeforeMasYear(chaCd);
            map.put("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        return map;
    }

    /*
     * 청구관리 > 청구등록 > 이전청구정보 불러오기 - 이전청구월정보 month
     */
    @ResponseBody
    @RequestMapping("beforeMasMonth")
    public HashMap<String, Object> selBeforeMasMonth(@RequestBody ClaimDTO dto) throws Exception {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            logger.debug("기관 {} 이전청구월정보 month", chaCd);
            List<ClaimDTO> list = claimService.selBeforeMasMonth(chaCd, dto.getYear());
            map.put("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        return map;
    }

    /*
     * 청구관리 > 청구등록 > 이전청구정보 불러오기 - 등록
     */
    @ResponseBody
    @RequestMapping("copyClaimInsert")
    public HashMap<String, Object> copyClaimRegInsert(@RequestBody ClaimDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<>();

        try {
            logger.debug("기관 {} 이전청구정보 불러오기 등록", chaCd);
            map.put("chaCd", chaCd);
            map.put("month", dto.getMonth());
            map.put("masMonth", dto.getMasMonth());

            // 청구를 복사한다
            claimService.copyNotice(chaCd, dto.getMonth(), dto.getMasMonth());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return map;
    }

    /*
     * 청구관리 > 청구등록 > 청구 sms 자동발송
     */
    public void notSmsSendAuto(HashMap<String, Object> map) throws Exception {
        try {

            FingerIntegrateMessaging fingerIntegrateMessaging = new FingerIntegrateMessaging();
            fingerIntegrateMessaging.setHost(host);
            fingerIntegrateMessaging.setPort(port);
            fingerIntegrateMessaging.setAccessToken(accessToken);

            List<NotiSendDTO> autoList = claimService.autoSmsList(map);
            for (NotiSendDTO dto : autoList) {
                logger.debug("기관 {} 청구 sms 자동발송", dto.getChaCd());
                HashMap<String, Object> smap = new HashMap<String, Object>();
                smap.put("chaCd", dto.getChaCd());
                smap.put("msg", dto.getMsg());
                smap.put("cusHp", dto.getCusHp());
                smap.put("notiMasCd", dto.getNotiMasCd());

                String hpNo = dto.getCusHp();
                String notiMasCd = dto.getNotiMasCd();

                String msg = varParamInfoService.setVarChange(smap); // 가변변수 치환
                int mLength = msg.getBytes("utf-8").length;

                MessageType messageType;
                String type = "";
                if (mLength > 80) {
                    messageType = MessageType.MMS;
                    type = "1";
                } else {
                    messageType = MessageType.SMS;
                    type = "0";
                }
                final ListResult<FingerIntegrateMessaging.Message> listResult = fingerIntegrateMessaging.createMessage(
                        messageType, "", StringEscapeUtils.unescapeHtml4(msg), null, dto.getTelNo().split(",")[0], hpNo, dto.getWriter()); // sms 발송

                HashMap<String, Object> imap = new HashMap<String, Object>();

                if (listResult != null) {
                    imap.put("smsReqCd", listResult.getItemList().get(0).getId());
                    imap.put("chaCd", dto.getChaCd());
                    imap.put("fiCd", "088");            // 은행코드
                    imap.put("cusHp", hpNo);
                    imap.put("telNo", dto.getTelNo().split(",")[0]);
                    imap.put("status", "0");            //전송상태 0 : 대기
                    imap.put("rslt", "");                // 결과코드  >>>>> 코드값 확인
                    imap.put("msg", msg);
                    imap.put("type", type);                // 메시지전송타입 SMS(0), UrlPush(1)
                    imap.put("sendCnt", "0");            // 시도횟수
                    imap.put("smsReqSt", "");            // 상태 ST07, ST08  >>>>> 코드값 확인
                    imap.put("chatrTy", "01");            // 가맹점접속형태 Web(01), 전문(03)
                    imap.put("notiMasCd", notiMasCd);
                    // sms 전송 후처리
                    notiService.insertSmsReq(imap);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /*
     * 청구관리 > 청구등록 > 이전 청구정보 불러오기 > 이전청구월 선택 시 등록 가능한 건수 확인
     */
    @ResponseBody
    @RequestMapping("useClaimCnt")
    public HashMap<String, Object> useClaimCnt(@RequestBody ClaimDTO dto) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String chaCd = authentication.getName();
            logger.debug("기관 {} 이전청구월 선택 시 등록 가능한 건수 확인", chaCd);

            long cnt = claimService.countNoticeForCopy(chaCd, dto.getMonth());

            map.put("cnt", cnt);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return map;
    }

    /**
     * 청구관리 > 청구조회 > 납부기한수정
     */
    @ResponseBody
    @RequestMapping("updateClaimDay")
    public void updateClaimDay(@RequestBody ClaimDTO dto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        dto.setChaCd(chaCd);

        try {
            logger.debug("기관 {} 납부기한수정", chaCd);
            HashMap<String, Object> smap = new HashMap<String, Object>();
            smap.put("chaCd", chaCd);
            if (!"".equals(dto.getMasMonth())) {
                smap.put("masMonth", dto.getMasMonth());
            }
            if (!"".equals(dto.getStartDate())) {
                smap.put("startDate", dto.getStartDate());
            }
            if (!"".equals(dto.getEndDate())) {
                smap.put("endDate", dto.getEndDate());
            }
            if (!"".equals(dto.getPrintDate())) {
                smap.put("printDate", dto.getPrintDate());
            } else {
                smap.put("printDate", dto.getEndDate());
            }
            if (!"".equals(dto.getNotiMasCd())) {
                String value = dto.getNotiMasCd().trim();
                String[] each = value.split(",");
                smap.put("notiMasCd", each);
            }

            /**
             * 청구서 등록 및 수정
             */
            logger.debug("smap: {}", smap);
            claimService.updateClaimDay(smap);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.error(e.getMessage(), e);
            } else {
                logger.error(e.getMessage());
            }

            throw e;
        }
    }

    /*
     * 청구관리 > 청구등록 > 청구 알림톡 자동발송
     */
    public void notAtSendAuto(HashMap<String, Object> map) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        FingerTalk fingerTalk = new FingerTalk();
        fingerTalk.setUrl(atUrl);
        fingerTalk.setServiceKey(atServiceKey);

        try {
            List<NotiSendDTO> autoList = claimService.autoAtList(map);

            for (NotiSendDTO dto : autoList) {
                logger.debug("기관 {} 청구 알림톡 자동발송", dto.getChaCd());

                HashMap<String, Object> smap = new HashMap<String, Object>();
                smap.put("chaCd", dto.getChaCd());
                smap.put("msg", dto.getSendMsg().replaceAll("<br>", "\r\n"));
                smap.put("cusHp", dto.getCusHp());
                smap.put("notiMasCd", dto.getNotiMasCd());

                String atCusHpNo = dto.getCusHp();
                String chaTelNo = dto.getChaTelNo();
                String templateCode = dto.getTemplateCode();
                String msgType = dto.getMsgType();

                String msg = varParamInfoService.setVarChange(smap); // 가변변수 치환

                final FingerTalk.Message sendMsg = fingerTalk.createMessage(atServiceKey, atCusHpNo, chaTelNo, msg, templateCode);      // 알람톡 발송

                // 알림톡 발송 성공 후 DB 저장
                HashMap<String, Object> imap = new HashMap<String, Object>();

                if (sendMsg != null) {
                    imap.put("cmid", sendMsg.getResultObject().getCmid());
                    imap.put("trxKey", sendMsg.getTrxKey());
                    imap.put("chaCd", chaCd);
                    imap.put("chaTelNo", chaTelNo.replaceAll("-", ""));
                    imap.put("cusTelNo", atCusHpNo.replaceAll("-", ""));
                    imap.put("sendMsg", msg);
                    imap.put("msgType", msgType);
                    imap.put("sendStatusCd", "0");
                    imap.put("sendResultCd", "0");
                    imap.put("notimasCd", dto.getNotiMasCd());
                    imap.put("paymasCd", "");
                    imap.put("maker", chaCd);
                    imap.put("reportDate", "");

                    notiService.insertAtReq(imap);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
