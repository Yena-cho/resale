package com.finger.shinhandamoa.org.claimMgmt.web;

import com.finger.shinhandamoa.org.claimMgmt.dto.ChaDTO;
import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimItemDTO;
import com.finger.shinhandamoa.org.claimMgmt.service.ClaimItemService;
import com.finger.shinhandamoa.util.dto.CodeDTO;
import com.finger.shinhandamoa.util.service.CodeService;
import com.finger.shinhandamoa.vo.PageVO;
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

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;

/**
 * 청구항목관리
 *
 * @author puki
 * @author wisehouse@finger.co.kr
 */
@Controller
@RequestMapping("org/claimMgmt/**")
public class ClaimItemController {

    private static final Logger logger = LoggerFactory.getLogger(ClaimItemController.class);

    @Inject
    private ClaimItemService claimItemService;

    // 공통코드 Service
    @Inject
    private CodeService codeService;

    /**
     * 청구관리 > 청구 항목 관리
     */
    @RequestMapping("claimItemMgmt")
    public ModelAndView claimItemList(@RequestParam(defaultValue = "1") int curPage, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        ModelAndView mav = new ModelAndView();

        try {
            logger.debug("기관 {} 청구 항목 관리", chaCd);
            // total count
            final int count = claimItemService.claimItemTotalCount(chaCd);

            // 페이지 관련 설정
            final PageVO page = new PageVO(count, curPage, pageSize);
            final int start = page.getPageBegin();
            final int end = page.getPageEnd();

            final HashMap<String, Object> map = new HashMap<>();

            // 청구목록
            final List<ClaimItemDTO> list = claimItemService.claimItemList(chaCd, start, end);
            // 내입금통장 정보
            final List<ClaimItemDTO> accList = claimItemService.moneyPassbookList(chaCd);
            // 입금통장명 - modal selectBox
            final List<CodeDTO> accNum = codeService.moneyPassbookName(chaCd);
            // 기관정보
            final ChaDTO cha = claimItemService.getCha(chaCd);
            map.put("list", list);
            map.put("cha", cha);
            map.put("accList", accList);
            map.put("accNum", accNum);
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("count", count);
            map.put("pageSize", pageSize);

            mav.addObject("map", map);
            mav.setViewName("org/claimMgmt/claimItemMgmt");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return mav;
    }

    /*
     * 청구관리 > 청구 항목 관리
     */
    @ResponseBody
    @RequestMapping("claimItemMgmtAjax")
    public HashMap<String, Object> claimItemAjaxList(@RequestBody ClaimItemDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<>();

        try {
            logger.debug("기관 {} 청구 항목 관리", chaCd);
            // total count
            int count = claimItemService.claimItemTotalCount(chaCd);

            // 페이지 관련 설정
            final PageVO page = new PageVO(count, dto.getCurPage(), 10);
            final int start = page.getPageBegin();
            final int end = page.getPageEnd();
            map.put("count", count);

            // 청구목록
            final List<ClaimItemDTO> list = claimItemService.claimItemList(chaCd, start, end);
            // 기관정보
            final ChaDTO cha = claimItemService.getCha(chaCd);

            map.put("cha", cha);
            map.put("list", list);
            map.put("pager", page);    // 페이징 처리를 위한 변수
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return map;
    }

    /*
     * 청구관리 > 청구 항목 관리 > 항목삭제
     */
    @ResponseBody
    @RequestMapping("deleteItem")
    public void deleteClaimItem(@RequestParam(value = "itemList[]") List<String> list) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        try {
            logger.debug("기관 {} 청구 항목 관리 > 항목삭제", chaCd);
            for (int i = 0; i < list.size(); i++) {
                claimItemService.deleteClaimItem(chaCd, list.get(i));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

    }

    /*
     * 청구관리 > 청구 항목 상세
     */
    @ResponseBody
    @RequestMapping("detailItem")
    public HashMap<String, Object> detailClaimDetail(String payItemCd) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<>();
        try {
            logger.debug("기관 {} 청구 항목 관리 > 청구 항목 상세", chaCd);
            map.put("detail", claimItemService.detailClaimItem(chaCd, payItemCd));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return map;
    }

    /*
     * 청구관리 > 청구 항목 관리 > 수정 및 등록
     */
    @ResponseBody
    @RequestMapping("modifyItem")
    public void modifyClaimItem(@RequestBody ClaimItemDTO dto) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String chaCd = authentication.getName();

        try {
            logger.debug("기관 {} 청구 항목 관리 > 수정 및 등록", chaCd);
            dto.setChaCd(chaCd);
            dto.setMakeIp("");

            claimItemService.modifyClaimItem(dto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /*
     * 청구관리 > 청구 항목 관리 > 청구항목명 중복 확인
     */
    @ResponseBody
    @RequestMapping("payItemNameCk")
    public HashMap<String, Object> payItemNameCheck(@RequestBody ClaimItemDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            logger.debug("기관 {} 청구 항목 관리 > 청구항목명 중복 확인", chaCd);
            map.put("chaCd", chaCd);
            map.put("adjaccyn", dto.getAdjaccyn());
            map.put("payItemName", dto.getPayItemName());
            map.put("adjfiRegKey", dto.getAdjfiRegKey());

            String test = claimItemService.payItemNameCheck(map);
            map.put("payName", test);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return map;
    }

    @ResponseBody
    @RequestMapping("orderByItem")
    public void orderByItemModify(@RequestParam(value = "itemList[]") List<String> itemList,
                                  @RequestParam(value = "idxList[]") List<String> idxList) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        try {
            // 모든 정렬순서 초기화로 변경 2018-07-22 @modified jhjeong@finger.co.kr
            HashMap<String, Object> map = new HashMap<String, Object>();
            logger.debug("기관 {} orderByItem 모든 정렬순서 초기화로", chaCd);
            for (int i = 0; i < itemList.size(); i++) {
                map.put("chaCd", chaCd);
                map.put("payItemCd", itemList.get(i));
                map.put("ptrItemOrder", Integer.parseInt(idxList.get(i))-1);
                claimItemService.updatePayItem(map);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /*
     * 청구관리 > 청구 항목 관리 > 입금통장 유무 확인
     */
    @ResponseBody
    @RequestMapping("selXadjGroup")
    public HashMap<String, Object> selXadjGroup() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            logger.debug("기관 {} 청구 항목 관리 > 입금통장 유무 확인", chaCd);
            map.put("chaCd", chaCd);
            String str = claimItemService.selXadjGroup(map);
            map.put("adj", str);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return map;
    }

}
