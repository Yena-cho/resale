package com.finger.shinhandamoa.org.receiptmgmt.web;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.finger.shinhandamoa.data.table.model.NoticeDetailsType;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtBaseDTO;
import com.finger.shinhandamoa.org.notimgmt.service.NotiMgmtService;
import com.finger.shinhandamoa.org.receiptmgmt.dto.ReceiptMgmtDTO;
import com.finger.shinhandamoa.org.receiptmgmt.dto.RefundRcpDTO;
import com.finger.shinhandamoa.org.receiptmgmt.service.DirectReceiptMgmtService;
import com.finger.shinhandamoa.org.transformer.DownloadView;
import com.finger.shinhandamoa.vo.PageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * (직접수납관리) 수기수납내역 및 수기환불 관리 컨트롤러
 * @author jhjeong@finger.co.kr
 * @modified 2018. 9. 10.
 */
@Controller
@Slf4j
@RequestMapping("org/receiptMgnt/**")
public class DirectReceiptMgmtController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DirectReceiptMgmtController.class);

    @Inject
    private NotiMgmtService notiMgmtService;

    @Autowired
    private DirectReceiptMgmtService directReceiptMgmtService;

    /**
     * 수기수납내역 - 수납 등록
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("directReceiptReg")
    public ModelAndView getDirectReceiptReg(@RequestParam Map<String, String> params, ModelAndView mav) throws Exception {
        // 고객구분 조회        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 수기수납내역 - 수납 등록", user);
        try {
            NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(user);
            mav.addObject("orgSess", baseInfo);

            // 마지막 청구월 조회
            mav.addObject("masMonth", directReceiptMgmtService.getNotiMasMonth(user, "PA02", "PA04"));

            // 청구항목 조회
            List<NoticeDetailsType> items = directReceiptMgmtService.selectPayItems(user);
            mav.addObject("payItems", items);
            mav.addObject("listCount", 0);
            mav.addObject("sumAmt", 0);
            mav.addObject("list", null);
            mav.addObject("map", params);

            mav.setViewName("org/receiptMgnt/directReceiptReg");
        }catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
        return mav;
    }
    

    /**
     * 직접수납관리 ajax
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("directReceiptListAjax")
    @ResponseBody
    public Map<String, Object> getDirectReceiptListAjax(@RequestBody Map<String, Object> params) throws Exception {
        int count = 0;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 직접수납관리 ajax", user);
        // 결과 Map
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("chacd", user);
        params.put("chacd", user);
        try {
            
        	// 수납대상 청구항목 조회
        	Map<String, Object> totValue = directReceiptMgmtService.selectNoticeCatalogCount(params);
        	count = NumberUtils.toInt(totValue.get("cnt").toString(), 0);
        	map.put("listCount", count );
        	map.put("sumAmt", NumberUtils.toLong(totValue.get("sumamt").toString(), 0L) );
        	map.put("sumRcpAmt", NumberUtils.toLong(totValue.get("sumrcpamt").toString(), 0L) );
        	
        	int curPage =  NumberUtils.toInt( params.get("curPage").toString(), 1 );
        	int PAGE_SCALE = NumberUtils.toInt( params.get("pageScale").toString(), 10 );
        	// 페이징처리
        	PageVO pageVO = new PageVO(count, curPage, PAGE_SCALE);
        	map.put("pager", pageVO);
        	
        	int start = pageVO.getPageBegin();
        	int end = pageVO.getPageEnd();
        	params.put("start", start);
        	params.put("end", end);
        	map.put("curPage",   curPage);
        	map.put("pageScale", PAGE_SCALE);
        	
        	List<ReceiptMgmtDTO> list = directReceiptMgmtService.selectNoticeCatalog(params);
            for (ReceiptMgmtDTO receiptMgmtDTO : list) {
                if (receiptMgmtDTO.getVano() != null) {
                    String vano = receiptMgmtDTO.getVano().substring(0, 3) + "-" + receiptMgmtDTO.getVano().substring(3, 6) + "-" + receiptMgmtDTO.getVano().substring(6);
                    receiptMgmtDTO.setVano(vano);
                }
            }
        	map.put("list", list);

            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 수납등록 저장/수정 처리
     * xrcpmas에 데이터 생성 > xrcpdet에 청구항목별 입금데이터 처리 > 현금영수증 발행
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping("directReceiptSaveAjax")
    @ResponseBody
    public Map<String, Object> directReceiptSaveAjax(@RequestBody Map<String, Object> params) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 수납등록 저장/수정 처리", user);
        Map<String, Object> map = new HashMap<String, Object>();
        
        // 기관코드 매핑
        params.put("chacd", user);
        try {
        	map = directReceiptMgmtService.processReceipt(params);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }
    
    /**
     * 직접수납 취소처리
     * 
     * 취소된 수납에 대한 청구는 미납상태로 변경된다.
     * @param params
     * @return
     * @throws Exception
     * @author jhjeong@finger.co.kr
     * @modified 2018. 9. 14.
     */
    @RequestMapping("directReceiptCancelAjax")
    @ResponseBody
    public Map<String, Object> directReceiptCancelAjax(@RequestBody Map<String, Object> params) throws Exception {

    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 직접수납 취소처리", user);
        Map<String, Object> map = new HashMap<String, Object>();
    	
    	// 기관코드 매핑
    	params.put("chacd", user);
    	try {
    		map = directReceiptMgmtService.processReceiptCancel(params);
    	} catch (Exception e) {
    		LOGGER.error(e.getMessage(), e);
    		map.put("retCode", "9999");
    		map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
    	}
    	
    	return map;
    }

    /**
     * 직접수납내역(현장수납, 환불내역) 엑셀다운로드
     * @return
     * @throws Exception
     */
    @RequestMapping(value="directReceiptExcelDown", method=RequestMethod.GET)
    public DownloadView directReceiptExcelDown(@RequestParam Map<String, Object> params) throws Exception {

    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 직접수납내역(현장수납, 환불내역) 엑셀다운로드", user);
        params.put("chacd", user);
        
        String prefixName = "";
        if (params.get("pageTab").toString().equalsIgnoreCase("direct")) prefixName = "수기수납 등록내역[%s].xlsx";
        else  prefixName = "수기수납 완료내역[%s]_%s.xlsx";
        
        InputStream inputStream;
        try {
        	inputStream = directReceiptMgmtService.getDirectReceiptExcel(params);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            inputStream = null;
        }
        
        
        return new DownloadView(String.format(prefixName, user, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())), inputStream);
    }
    
    /**
     * 수기환불내역 메인화면
     * @param params
     * @param mav
     * @return
     * @throws Exception
     * @author jhjeong@finger.co.kr
     * @modified 2018. 9. 19.
     */
    @RequestMapping("refundReceipt")
    public ModelAndView refundReceipt(@RequestParam Map<String, String> params, ModelAndView mav) throws Exception {
        // 고객구분 조회        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 수기환불내역 메인화면", user);
        try {
            NotiMgmtBaseDTO baseInfo = notiMgmtService.selectNotiBaseInfo(user);
            mav.addObject("orgSess", baseInfo);

            // 마지막 청구월 조회
            mav.addObject("masMonth", directReceiptMgmtService.getNotiMasMonth(user, "PA03", "PA05"));

            // 청구항목 조회
            List<NoticeDetailsType> items = directReceiptMgmtService.selectPayItems(user);
            mav.addObject("payItems", items);
            mav.addObject("listCount", 0);
            mav.addObject("sumAmt", 0);
            mav.addObject("sumRefundAmt", 0);
            mav.addObject("sumRcpAmt", 0);
            mav.addObject("list", null);
            mav.addObject("map", params);

            mav.setViewName("org/receiptMgnt/refundReceipt");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

        return mav;
    }
    
    /**
     * 환불등록/완료  수납내역/환불내역 목록 조회
     * @param params
     * @return
     * @throws Exception
     * @author jhjeong@finger.co.kr
     * @modified 2018. 9. 19.
     */
    @RequestMapping("refundReceiptListAjax")
    @ResponseBody
    public Map<String, Object> refundReceiptListAjax(@RequestBody Map<String, Object> params) throws Exception {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String user = authentication.getName();
        LOGGER.debug("기관 {} 환불등록/완료  수납내역/환불내역 목록 조회", user);
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	// 기관코드 매핑
    	params.put("chacd", user);
    	try {
    		Map<String, Object> result = directReceiptMgmtService.selectRefundReceiptCatalogCount(params);
    		
        	int count = NumberUtils.toInt(result.get("listcount").toString(), 0);
        	map.put("listCount", count );
        	map.put("sumAmt", NumberUtils.toLong(result.get("sumpayitemamt").toString(), 0L) );
        	map.put("sumRcpAmt", NumberUtils.toLong(result.get("sumrcpamt").toString(), 0L) );
        	map.put("sumRcpPayitemAmt", NumberUtils.toLong(result.get("sumrcppayitemamt").toString(), 0L) );
        	map.put("sumRefundAmt", NumberUtils.toLong(result.get("sumrefundamt").toString(), 0L) );
        	
        	int curPage =  NumberUtils.toInt( params.get("curPage").toString(), 1 );
        	int PAGE_SCALE = NumberUtils.toInt( params.get("pageScale").toString(), 10 );
        	// 페이징처리
        	PageVO pageVO = new PageVO(count, curPage, PAGE_SCALE);
        	map.put("pager", pageVO);
        	
        	int start = pageVO.getPageBegin();
        	int end = pageVO.getPageEnd();
        	params.put("start", start);
        	params.put("end", end);
        	map.put("curPage",   curPage);
        	map.put("pageScale", PAGE_SCALE);
    		
        	List<RefundRcpDTO> list = directReceiptMgmtService.selectRefundReceiptCatalog(params);
            String vano = "";
            for (RefundRcpDTO refundRcpDTO : list) {
                if (refundRcpDTO.getVano() != null) {
                    vano = refundRcpDTO.getVano().substring(0, 3) + "-" + refundRcpDTO.getVano().substring(3, 6) + "-" + refundRcpDTO.getVano().substring(6);
                    refundRcpDTO.setVano(vano);
                }
            }
    		
    		map.put("list", list);

            map.put("retCode", "0000");
            map.put("retMsg", "정상");
    	} catch (Exception e) {
    		LOGGER.error(e.getMessage(), e);
    		map.put("retCode", "9999");
    		map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
    	}
    	
    	return map;
    }
    
    /**
     * 환불등록(저장)처리 Ajax 
     * @param params
     * @return
     * @throws Exception
     * @author jhjeong@finger.co.kr
     * @modified 2018. 9. 19.
     */
    @RequestMapping("refundReceiptSaveAjax")
    @ResponseBody
    public Map<String, Object> refundReceiptSaveAjax(@RequestBody Map<String, Object> params) throws Exception {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String user = authentication.getName();
        LOGGER.debug("기관 {} 환불등록(저장)처리 Ajax ", user);
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	// 기관코드 매핑
    	params.put("chacd", user);
    	try {
    		map = directReceiptMgmtService.processRefundReceipt(params);
    	} catch (Exception e) {
    		LOGGER.error(e.getMessage(), e);
    		map.put("retCode", "9999");
    		map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
    	}
    	
    	return map;
    }
    
    /**
     * 환불취소 처리 ajax
     * @param params
     * @return
     * @throws Exception
     * @author jhjeong@finger.co.kr
     * @modified 2018. 9. 19.
     */
    @RequestMapping("refundReceiptCancelAjax")
    @ResponseBody
    public Map<String, Object> refundReceiptCancelAjax(@RequestBody Map<String, Object> params) throws Exception {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String user = authentication.getName();
        LOGGER.debug("기관 {} 환불취소 처리 ajax", user);
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	// 기관코드 매핑
    	params.put("chacd", user);
    	try {
    		map = directReceiptMgmtService.processRefundReceiptCancel(params);
    	} catch (Exception e) {
    		LOGGER.error(e.getMessage(), e);
    		map.put("retCode", "9999");
    		map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
    	}
    	
    	return map;
    }
    
    @RequestMapping(value="refundReceiptExcelDown", method=RequestMethod.GET)
    public DownloadView refundReceiptExcelDown(@RequestParam Map<String, Object> params) throws Exception {

    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        LOGGER.debug("기관 {} 수기환불 완료내역 엑셀다운로드", user);
        params.put("chacd", user);
        
        String prefixName = "";
        if (params.get("pageTab").toString().equalsIgnoreCase("refund")) prefixName = "수기환불 등록내역[%s].xlsx";
        else  prefixName = "수기환불 완료내역[%s]_%s.xlsx";
        
        InputStream inputStream;
        try {
        	inputStream = directReceiptMgmtService.getRefundReceiptExcel(params);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            inputStream = null;
        }
        
        
        return new DownloadView(String.format(prefixName, user, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())), inputStream);
    }
}
