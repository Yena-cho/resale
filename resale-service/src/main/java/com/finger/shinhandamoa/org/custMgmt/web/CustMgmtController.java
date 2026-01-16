package com.finger.shinhandamoa.org.custMgmt.web;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import com.finger.shinhandamoa.common.CmmnUtils;
import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.org.custMgmt.dao.CustMgmtDAO;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.finger.shinhandamoa.org.custMgmt.dto.CustReg01DTO;
import com.finger.shinhandamoa.org.custMgmt.service.CustMgmtService;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtBaseDTO;
import com.finger.shinhandamoa.org.notimgmt.service.NotiMgmtService;
import com.finger.shinhandamoa.util.dto.CodeDTO;
import com.finger.shinhandamoa.util.service.CodeService;
import com.finger.shinhandamoa.vo.PageVO;

/**
 * @author
 * @date 2018. 4. 6.
 * @desc 고객관리
 */
@Controller
@RequestMapping("org/custMgmt/**")
public class CustMgmtController {

    private static final Logger logger = LoggerFactory.getLogger(CustMgmtController.class);
    @Autowired
    private CustMgmtService custMgmtService;

    @Inject
    private NotiMgmtService notiMgmtService;

    @Inject
    private CodeService codeService;

    @Autowired
    private CustMgmtDAO custMgmtDAO;

    /*
     * 고객관리 > 고객등록 화면
     */
    @RequestMapping("custReg")
    public ModelAndView CustReg01List(@RequestParam(defaultValue = "regDt") String searchOrderBy,
                                      @RequestParam(defaultValue = "1") int curPage,
                                      @RequestParam(defaultValue = "10") int pageScale) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        HashMap<String, Object> map = new HashMap<String, Object>();
        ModelAndView mav = new ModelAndView();
        try {
            logger.debug("기관 {} 고객등록", chaCd);
            map.put("chaCd", chaCd);
            /*
             * 고객등록화면은 임시고객만 조회되도록 함
             * disabled = 'I' 만 조회
             */
            map.put("disabled", "I");
            NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(chaCd);
            mav.addObject("orgSess", baseInfo);
            // total count
            int count = custMgmtService.custReg01TotalCount(map);
            map.put("count", count);
            map.put("start", 1);
            map.put("end", count);

            List<CustReg01DTO> list = custMgmtService.custReg01ListAll(map);
            for (CustReg01DTO custReg01DTO : list) {
                String vano = custReg01DTO.getVano().substring(0, 3) + "-" + custReg01DTO.getVano().substring(3, 6) + "-" + custReg01DTO.getVano().substring(6);
                custReg01DTO.setVano(vano);
            }
            map.put("list", list);
            mav.addObject("map", map);
            mav.setViewName("org/custMgmt/custReg");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return mav;
    }

    /**
     * 고객관리 > 고객등록조회(Ajax)
     */
    @RequestMapping("getCustRegList")
    @ResponseBody
    public HashMap<String, Object> getCustRegList(@RequestBody CustReg01DTO body) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String chaCd = authentication.getName();
            logger.debug("기관 {} 고객등록조회", chaCd);
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("chaCd", chaCd);
            reqMap.put("searchOrderBy", body.getSearchOrderBy());
            /*
             * 고객등록화면은 임시고객만 조회되도록 함
             * disabled = 'I' 만 조회
             */
            reqMap.put("disabled", "I");

            // total count
            int count = custMgmtService.custReg01TotalCount(reqMap);
            // 페이지 관련 설정
            PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            reqMap.put("count", count);
            reqMap.put("start", start);
            reqMap.put("end", end);

            List<CustReg01DTO> list = custMgmtService.custReg01ListAll(reqMap);
            for (CustReg01DTO lista : list) {
                if (lista.getVano() != null) {
                    String vano = lista.getVano().substring(0, 3) + "-" + lista.getVano().substring(3, 6) + "-" + lista.getVano().substring(6);
                    lista.setVano(vano);
                }
                lista.setCusName((StringEscapeUtils.escapeHtml4(lista.getCusName())));
                lista.setCusGubn1(StringEscapeUtils.escapeHtml4(lista.getCusGubn1()));
                lista.setCusGubn2(StringEscapeUtils.escapeHtml4(lista.getCusGubn2()));
                lista.setCusGubn3(StringEscapeUtils.escapeHtml4(lista.getCusGubn3()));
                lista.setCusGubn4(StringEscapeUtils.escapeHtml4(lista.getCusGubn4()));
                lista.setMemo(StringEscapeUtils.escapeHtml4(lista.getMemo()));
            }

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

    /*
     * 고객관리 > 고객등록 > 작성중인내역 삭제
     */
    @ResponseBody
    @RequestMapping("deleteCustomer")
    public HashMap<String, Object> deleteCustomer(CustReg01DTO dto) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String chaCd = authentication.getName();
            logger.debug("기관 {} 고객등록 > 작성중인내역 삭제", chaCd);
            map.put("chaCd", chaCd);
            map.put("cusCd", "");
            map.put("useDt", "");

            custMgmtService.deleteXcusMas(map);

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
     * 고객관리 > 고객등록 > 고객선택삭제
     */
    @ResponseBody
    @RequestMapping("deleteCustInfo")
    public HashMap<String, Object> deleteCustInfo(@RequestParam(value = "itemList[]") List<String> list) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String chaCd = authentication.getName();
            logger.debug("기관 {} 고객선택삭제", chaCd);
            for (String vano : list) {
                map.put("chaCd", chaCd);
                if (vano != null) {
                    map.put("vano", StringUtils.replaceAll(vano, "-", ""));
                } else {
                    map.put("vano", vano);
                }
                map.put("useYn", "N");
                map.put("cusCd", "");
                map.put("useDt", "");

                custMgmtService.deleteCusInfo(map);
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
     * 고객관리 > 고객임시등록(단건)
     */
    @ResponseBody
    @RequestMapping("regCustInfo")
    public HashMap<String, Object> regCustInfo(@RequestBody CustReg01DTO dto) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String chaCd = authentication.getName();

            logger.debug("기관 {} 고객임시등록", chaCd);
            dto.setMaker(chaCd);
            if (dto.getVano() != null) {
                dto.setVano(StringUtils.replaceAll(dto.getVano(), "-", ""));
            }
            // 실제 임시고객등록 서비스
            String retCode = custMgmtService.insertXCustMas(dto);

            map.put("retCode", retCode);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return map;
    }

    /*
     * 고객관리 > 고객임시등록(단건) > 정보수정
     */
    @ResponseBody
    @RequestMapping("updateCustInfo")
    public HashMap<String, Object> updateCustInfo(@RequestBody CustReg01DTO dto) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        CustReg01DTO retXcusMas = new CustReg01DTO();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String user = authentication.getName();
            logger.debug("기관 {} 고객임시등록(단건) > 정보수정", user);

            dto.setMaker(user);
            dto.setJobType("U");
            if (dto.getVano() != null) {
                dto.setVano(StringUtils.replaceAll(dto.getVano(), "-", ""));
            }

            String cusChk = "";

            /**
             * 수정 시, 가상계좌번호가 없을 수 없고, 입력할 수 없으니 입력한 고객번호가 중복인지 아닌지 체크
             */
            HashMap<String, Object> cusKeyChk = new HashMap<String, Object>();

            if (!"".equals(dto.getCusKey())) {
                cusKeyChk.put("chaCd", user);
                cusKeyChk.put("cusKey", StrUtil.nullToVoid(dto.getCusKey()));
                retXcusMas = custMgmtDAO.selectDetailCustReg(cusKeyChk);

                if (retXcusMas != null) {
                    // 고객 상태값이 있음.

                    if ("Y".equals(retXcusMas.getDisabled())) {
                        // 고객이 사용중이 아닐 땐 삭제된 고객
                        cusChk = "delCus";
                    } else {
                        // 고객이 사용중일 때

                        if (!dto.getVano().equals(retXcusMas.getVano())) {
                            // 사용중인 고객의 가상계좌와 입력 가상계좌가 동일하지 않을 때
                            cusChk = "useCus";
                        }
                    }
                }
            }

            String cusName = dto.getCusName();
            String memo = dto.getMemo();
            String cusGubn1 = dto.getCusGubn1();
            String cusGubn2 = dto.getCusGubn2();
            String cusGubn3 = dto.getCusGubn3();
            String cusGubn4 = dto.getCusGubn4();
            dto.setCusName(cusName);
            dto.setMemo(memo);
            if (cusGubn1.length() > 29) {
                dto.setCusGubn1(cusGubn1.substring(0, 29));
            } else {
                dto.setCusGubn1(cusGubn1);
            }
            if (cusGubn2.length() > 29) {
                dto.setCusGubn2(cusGubn2.substring(0, 29));
            } else {
                dto.setCusGubn2(cusGubn2);
            }
            if (cusGubn3.length() > 29) {
                dto.setCusGubn3(cusGubn3.substring(0, 29));
            } else {
                dto.setCusGubn3(cusGubn3);
            }
            if (cusGubn4.length() > 29) {
                dto.setCusGubn4(cusGubn4.substring(0, 29));
            } else {
                dto.setCusGubn4(cusGubn4);
            }


            // 미납상태의 청구원장 고객명 변경
            if (!dto.getBeCusName().equals(dto.getCusName())) {
                map.put("vano", dto.getVano());
                map.put("cusName", cusName);
                custMgmtService.updateNotiMasCusName(map);
            }

            if ("delCus".equals(cusChk)) {
                map.put("retCode", "0005");
                map.put("retMsg", "삭제된 계좌의 고객구분값");
            } else if ("useCus".equals(cusChk)) {
                map.put("retCode", "0005");
                map.put("retMsg", "중복된 고객번호");
            } else {
                custMgmtService.updateXcusMas(dto);

                map.put("retCode", "0000");
                map.put("retMsg", "정상");
            }

        } catch (Exception e) {
            logger.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /*
     * 고객관리 > 고객선택등록
     */
    @ResponseBody
    @RequestMapping("updateCustList")
    public HashMap<String, Object> updateCustList(@RequestBody String str) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String chaCd = authentication.getName();
            logger.debug("기관 {} 고객선택등록", chaCd);
            map.put("chaCd", chaCd);
            map.put("disabled", "N");
            map.put("disab", "I");
            custMgmtService.updateCusmas(map);

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
     * 고객관리 > 고객조회 화면
     */
    @RequestMapping("custList")
    public ModelAndView CustListList() throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            logger.debug("기관 {} 고객조회", chaCd);
            NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(chaCd); // 고객구분용
            mav.addObject("orgSess", baseInfo);

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            map.put("cusGbList", cusGbList);
            map.put("cusGbCnt", cusGbList.size());

            mav.addObject("map", map);
            mav.setViewName("org/custMgmt/custList");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return mav;
    }

    /**
     * 고객관리 > 고객조회(Ajax)
     */
    @RequestMapping("getCustListList")
    @ResponseBody
    public HashMap<String, Object> getCustListList(@RequestBody CustReg01DTO body) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String user = authentication.getName();
            logger.debug("기관 {} 고객조회", user);
            map.put("chaCd", user);
            if(CmmnUtils.validateDateFormat2(body.getEndDate()) && CmmnUtils.validateDateFormat2(body.getStartDate())){
                map.put("startDate", body.getStartDate());
                map.put("endDate", body.getEndDate());
            } else {
                map.put("startDate", "");
                map.put("endDate", "");
            }
            map.put("searchGb", body.getSearchGb());

            if (!"".equals(body.getSearchValue())) {
                String value = body.getSearchValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("searchValue", valueList);
            }

            map.put("payList", body.getPayList());
            map.put("cusList", body.getCusList());
            map.put("monList", body.getMonList());
            map.put("cusGubn", body.getCusGubn());
            map.put("cusGubnValue", body.getCusGubnValueList());

            if (body.getCusGubnValue() != null && !"".equals(body.getCusGubnValue())) {
                String value = body.getCusGubnValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("cusGubnValue", valueList);
            }

            map.put("searchOrderBy", body.getSearchOrderBy());

            // total count
            int count = custMgmtService.custReg01TotalCount(map);
            // 페이지 관련 설정
            PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            map.put("count", count);
            map.put("start", start);
            map.put("end", end);

            List<CustReg01DTO> list = custMgmtService.custReg01ListAll(map);
            for (CustReg01DTO custReg01DTO : list) {
                String vano = custReg01DTO.getVano().substring(0, 3) + "-" + custReg01DTO.getVano().substring(3, 6) + "-" + custReg01DTO.getVano().substring(6);
                custReg01DTO.setVano(vano);
            }
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

    /*
     * 고객관리 > 고객조회 > 선택삭제
     */
    @RequestMapping("deleteUseCustInfo")
    @ResponseBody
    public HashMap<String, Object> deleteUseCustInfo(@RequestParam(value = "itemList[]") List<String> list) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String chaCd = authentication.getName();
            logger.debug("기관 {} 고객조회 > 선택삭제", chaCd);
            for (String str : list) {
                map.put("chaCd", chaCd);
                map.put("disabled", "Y");
                map.put("useYn", "N");
                if (str != null) {
                    map.put("vano", StringUtils.replaceAll(str, "-", ""));
                } else {
                    map.put("vano", str);
                }
                custMgmtService.deleteCusInfo(map);
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
     * 고객관리 > 고객선택 납부제외
     */
    @ResponseBody
    @RequestMapping("exceptCustInfo")
    public HashMap<String, Object> exceptCustInfo(@RequestParam(value = "itemList[]") List<String> list) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String chaCd = authentication.getName();
            logger.debug("기관 {} 고객조회 > 고객선택 납부제외", chaCd);
            for (String str : list) {
                map.put("chaCd", chaCd);
                if (str != null) {
                    map.put("vano", StringUtils.replaceAll(str, "-", ""));
                } else {
                    map.put("vano", str);
                }
                map.put("rcpGubn", "N");

                custMgmtService.updateCusmas(map);
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

    /**
     * 고객관리 > 고객상세조회
     */
    @RequestMapping("selectDetailCustReg")
    @ResponseBody
    public HashMap<String, Object> selectDetailCustReg(@RequestBody CustReg01DTO body) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String chaCd = authentication.getName();
            logger.debug("기관 {} 고객조회 > 고객상세조회", chaCd);

            map.put("chaCd", chaCd);
            if (body.getVano() != null) {
                map.put("vano", StringUtils.replaceAll(body.getVano(), "-", ""));
            } else {
                map.put("vano", body.getVano());
            }

            CustReg01DTO info = custMgmtService.selectDetailCustReg(map);
            String cusName = info.getCusName();
            String vano = "";
            if (info.getVano() != null) {
                vano = info.getVano().substring(0, 3) + "-" + info.getVano().substring(3, 6) + "-" + info.getVano().substring(6);
            }
            String cusMail = info.getCusMail();
            String memo = info.getMemo();
            String cusGubn1 = info.getCusGubn1();
            String cusGubn2 = info.getCusGubn2();
            String cusGubn3 = info.getCusGubn3();
            String cusGubn4 = info.getCusGubn4();
            info.setCusName(cusName);
            info.setVano(vano);
            info.setCusMail(cusMail);
            info.setMemo(memo);
            info.setCusGubn1(cusGubn1);
            info.setCusGubn2(cusGubn2);
            info.setCusGubn3(cusGubn3);
            info.setCusGubn4(cusGubn4);
            map.put("info", info);

            map.put("retCode", "0000");
            map.put("retMsg", "정상");

        } catch (Exception e) {
            logger.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 고객관리 > 고객등록 > 개별고객등록 > 가상계좌 확인
     */
    @RequestMapping("useCheckVano")
    @ResponseBody
    public HashMap<String, Object> useCheckVano(@RequestBody CustReg01DTO body) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        map.put("chaCd", chaCd);
        if (body.getVano() != null) {
            map.put("vano", StringUtils.replaceAll(body.getVano(), "-", ""));
        } else {
            map.put("vano", body.getVano());
        }
        String errCode = "";

        try {
            logger.debug("기관 {} 고객등록 > 개별고객등록 > 가상계좌 확인", chaCd);
            // 가상계좌 채번
            CustReg01DTO retVano = custMgmtDAO.getVanoInfo(map);

            if (retVano == null) {
                errCode = "0002";
            } else {
                if ("Y".equals(retVano.getUseYn())) {
                    errCode = "0003";
                } else {
                    errCode = "0001";
                }
            }

            map.put("errCode", errCode);
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
