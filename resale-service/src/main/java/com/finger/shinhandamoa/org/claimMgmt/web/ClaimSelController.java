package com.finger.shinhandamoa.org.claimMgmt.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimDTO;
import com.finger.shinhandamoa.org.claimMgmt.service.ClaimService;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtBaseDTO;
import com.finger.shinhandamoa.org.notimgmt.service.NotiMgmtService;
import com.finger.shinhandamoa.util.dto.CodeDTO;
import com.finger.shinhandamoa.util.service.CodeService;
import com.finger.shinhandamoa.vo.PageVO;


/**
 * @author by puki
 * @date 2018. 4. 22.
 * @desc 최초생성
 */
@Controller
@RequestMapping("org/claimMgmt/**")
public class ClaimSelController {

    private static final Logger logger = LoggerFactory.getLogger(ClaimSelController.class);

    // 청구항목 Service
    @Inject
    private ClaimService claimService;

    @Inject
    private NotiMgmtService notiMgmtService;

    // 공통코드 Service
    @Inject
    private CodeService codeService;

    /*
     * 청구관리 > 청구 조회
     */
    @RequestMapping("claimSel")
    public ModelAndView claimRegSelect(@RequestParam(defaultValue = "PA02") String notiMasSt,
                                       @RequestParam(defaultValue = "cusName") String search_orderBy,
                                       @RequestParam(defaultValue = "1") int curPage,
                                       @RequestParam(defaultValue = "10") int PAGE_SCALE) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            logger.debug("기관 {} 청구관리 > 청구 조회", chaCd);
            // 청구월
            String masMonth = claimService.selectClaimMonth(chaCd, notiMasSt);
            map.put("claimMonth", masMonth);
            map.put("masMonth", masMonth);

            NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(chaCd);
            mav.addObject("orgSess", baseInfo);

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            map.put("cusGbList", cusGbList);
            map.put("cusGbCnt", cusGbList.size());

            mav.addObject("map", map);
            mav.setViewName("org/claimMgmt/claimInq");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return mav;
    }

    /*
     * 청구관리 > 청구 조회
     */
    @ResponseBody
    @RequestMapping("claimSelAjax")
    public HashMap<String, Object> claimRegAjaxList(@RequestBody ClaimDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            logger.debug("기관 {} 청구관리 > 청구 조회", chaCd);
            Map<String, Object> cmap = new HashMap<String, Object>();
            cmap.put("chaCd", chaCd);
            cmap.put("selGb", dto.getSelGb());
            if (dto.getSelGb().equals("M")) { // 조회구분 - 청구월
                cmap.put("masMonth", dto.getMasMonth());
            } else {                         // 조회구분 - 기간별
                cmap.put("masStDt", dto.getMasStDt());
                cmap.put("masEdDt", dto.getMasEdDt());
            }

            cmap.put("notiMasSt", dto.getNotiMasSt());
            cmap.put("cusGubn", dto.getCusGubn()); // 검색구분
            if (!"".equals(dto.getSearchValue())) {
                String value = dto.getSearchValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                cmap.put("searchValue", valueList);
            }

            cmap.put("seachCusGubn", dto.getSeachCusGubn()); // 고객구분 selectbox

            if (dto.getCusGubnValue() != null && !"".equals(dto.getCusGubnValue())) {
                String value = dto.getCusGubnValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                cmap.put("cusGubnValue", valueList);
            }

            cmap.put("payList", dto.getPayList());

            cmap.put("strList", dto.getStrList());
            cmap.put("search_orderBy", dto.getSearch_orderBy());

            // total count
            int count = claimService.selectClaimDetCount(cmap);
            map.put("count", count);

            // 청구상태가 정상이면서 수납상태가 미납인 청구서 total count
            int cancelCount = claimService.selectClaimCancelCount(cmap);
            map.put("cancelCount", cancelCount);

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            map.put("cusGbList", cusGbList);
            map.put("cusGbCnt", cusGbList.size());

            // 청구대상목록 - 페이지 관련 설정
            PageVO page = new PageVO(count, dto.getCurPage(), dto.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            cmap.put("start", start);
            cmap.put("end", end);

            map.put("payAmtSum", claimService.selClaimRegSum(cmap));
            List<ClaimDTO> list = claimService.selectClaimDetList(cmap);
            String vano = "";
            for (ClaimDTO claimDTO : list) {
                if (claimDTO.getVano() != null) {
                    vano = claimDTO.getVano().substring(0, 3) + "-" + claimDTO.getVano().substring(3, 6) + "-" + claimDTO.getVano().substring(6);
                    claimDTO.setVano(vano);
                }
            }
            map.put("list", list);
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

    /*
     * 청구관리 > 청구 조회 > 청구취소
     */
    @ResponseBody
    @RequestMapping("claimCancel")
    public HashMap<String, Object> claimCancel(@RequestParam(value = "itemList[]") List<String> list) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String chaCd = authentication.getName();
            logger.debug("기관 {} 청구관리 > 청구취소", chaCd);
            String notiCanYn = "Y"; // 취소

            for (String str : list) {
                map.put("chaCd", chaCd);
                map.put("notiMasCd", str);
                map.put("notiCanYn", notiCanYn);
                logger.debug("청구개별삭제" + str);
                claimService.claimInsert(map);
            }
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
     * 청구관리 > 청구 조회 > 일괄취소
     */
    @ResponseBody
    @RequestMapping("claimAllCancel")
    public HashMap<String, Object> claimAllCancel(@RequestBody ClaimDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        String notiCanYn = "Y";    // 취소

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            logger.debug("기관 {} 청구조회 > 일괄취소", chaCd);
            map.put("chaCd", chaCd);
            map.put("masMonth", dto.getMasMonth());
            map.put("masStDt", dto.getMasStDt());
            map.put("masEdDt", dto.getMasEdDt());
            map.put("notiCanYn", notiCanYn);

            map.put("cusGubn", dto.getCusGubn()); // 검색구분
            if (!"".equals(dto.getSearchValue())) {
                String value = dto.getSearchValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("searchValue", valueList);
            }
            map.put("seachCusGubn", dto.getSeachCusGubn()); // 고객구분 selectbox
            if (dto.getCusGubnValue() != null && !"".equals(dto.getCusGubnValue())) {
                String value = dto.getCusGubnValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("cusGubnValue", valueList);
            }
            map.put("payList", dto.getPayList());
            map.put("strList", dto.getStrList());

            logger.debug("청구일괄삭제" + dto.getMasMonth() + dto.getMasStDt() + dto.getMasEdDt());
            claimService.updateCancelAll(map);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        return map;
    }

    /*
     * 청구관리 > 청구 조회 > 청구항목 상세보기
     */
    @ResponseBody
    @RequestMapping("payItemDetailView")
    public HashMap<String, Object> payItemDetailView(@RequestBody ClaimDTO dto) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        try {
            logger.debug("기관 {} 청구 조회 > 청구항목 상세보기", chaCd);
            map.put("notiMasCd", dto.getNotiMasCd());

            List<ClaimDTO> list = claimService.selectPayItemDetailView(map);

            long totalPayAmt = 0;
            long totalRcpAmt = 0;

            for (int i = 0; i < list.size(); i++) {
                totalPayAmt += NumberUtils.toLong(list.get(i).getPayItemAmt());
                totalRcpAmt += NumberUtils.toLong(list.get(i).getRcpAmt());
            }

            map.put("list", list);
            map.put("totalPayAmt", totalPayAmt);
            map.put("totalRcpAmt", totalRcpAmt);
            map.put("retCode", "0000");
            map.put("retMsg", "정상");

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;

    }
}
