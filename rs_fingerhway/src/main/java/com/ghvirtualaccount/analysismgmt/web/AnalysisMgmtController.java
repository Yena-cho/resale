package com.ghvirtualaccount.analysismgmt.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ghvirtualaccount.analysismgmt.service.AnalysisMgmtService;
import com.ghvirtualaccount.cmmn.StrUtil;
import com.ghvirtualaccount.cmmn.XlsxBuilder;
import com.ghvirtualaccount.vo.MsgSendHistInfoVO;
import com.ghvirtualaccount.vo.MsgUseStateInfoVO;
import com.ghvirtualaccount.vo.ParamVO;
import com.ghvirtualaccount.vo.TbMsgSendHistVO;
import com.ghvirtualaccount.vo.TotMsgSendHistVO;
import com.ghvirtualaccount.vo.VAcntAnalysisTotVO;
import com.ghvirtualaccount.vo.VAcntAnalysisVO;
import com.google.gson.Gson;

@Controller
public class AnalysisMgmtController {

	private static Logger logger = LoggerFactory.getLogger(AnalysisMgmtController.class);
	
	@Resource(name="analysisMgmtService")
	private AnalysisMgmtService analysisMgmtService;
	
    @Value("${analysisday.pagesize}")
    private int adpgsize;
    @Value("${vaccountuse.pagesize}")
    private int vapgsize;
    @Value("${msguse.pagesize}")
    private int mupgsize;
    
	//--------------------------------------------------------------
	// 가상계좌 일별수납통계 화면으로 이동
	//--------------------------------------------------------------
	@RequestMapping(value="/vAccountDayReceipt.do", method=RequestMethod.POST)
    public String vAccountDayReceipt(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, final HttpServletRequest request) throws Exception{

		logger.debug("xxxxxxxx vAccountDayReceipt start xxxxxxxxxx");
		
		Gson gson = new Gson();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		//조회조건이 있으면 조회해서 넘겨 준다.
		if(paramVO.getStartDate()==null||paramVO.getStartDate().equals("")){
			//등록된 건을 조회, 기본적으로 조회 기간을 최근 3개월로 paramVO에 강제 세팅 한다.
			StrUtil stringUtil = new StrUtil();
			
			paramVO.setStartDate(stringUtil.getCalMonthDateStr(-3));
			paramVO.setEndDate(stringUtil.getCurrentDateStr());
			
		}
		paramVO.setPageIdx(1);
		paramVO.setPageSize(adpgsize);
		
		//조회 조건이 있을 경우 조회 해서 넘겨준다(탭 처리 때문)
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			VAcntAnalysisTotVO vAcntAnalysisTot = analysisMgmtService.getVAccountDayReceiptTot(paramVO);
			int totCnt = vAcntAnalysisTot.getTotCnt();
			int maxPage = 0;
			
			//조회된 건수가 있으면
			if (totCnt > 0) {
				maxPage = totCnt / paramVO.getPageSize();
				if((totCnt % paramVO.getPageSize()) > 0){
					maxPage++;
				}

				paramVO.setStartIdx((paramVO.getPageSize()*(paramVO.getPageIdx()-1)));
				paramVO.setEndIdx(paramVO.getPageSize()*paramVO.getPageIdx());
				
				model.addAttribute("vAcntAnalysisTot", vAcntAnalysisTot);
				
				//목록 조회
				List<VAcntAnalysisVO> vAcntAnalysisList = analysisMgmtService.getVAccountDayReceiptList(paramVO);
				model.addAttribute("vAcntAnalysisList", vAcntAnalysisList);					
			}

			resultMap.put("totCnt", totCnt);
			resultMap.put("maxPage", maxPage);
			resultMap.put("startIdx", paramVO.getStartIdx());
			
			model.addAttribute("resultMap", resultMap);
			
		} catch (Exception e) {
			logger.error(e.getMessage());

			throw e;
			
		}
		
		//파라미터 유지
		model.addAttribute("paramVO", paramVO);
		
		return "analysismgmt/vAccountDayReceipt";
    }

	//--------------------------------------------------------------
	// 가상계좌 일별수납통계 엑셀 다운로드
	//--------------------------------------------------------------
	@RequestMapping(value="/vAccountDayReceiptExcel.do", method=RequestMethod.POST)
	@ResponseBody
    public void vAccountDayReceiptExcel(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx vAccountDayReceiptExcel start xxxxxxxxxx");
		
		try {

			//합계 조회
			VAcntAnalysisTotVO vAcntAnalysisTot = analysisMgmtService.getVAccountDayReceiptTot(paramVO);
			//목록 조회
			List<VAcntAnalysisVO> vAcntAnalysisList = analysisMgmtService.getVAccountDayReceiptList(paramVO);
			
			if(vAcntAnalysisList.size()>0){
				//엑셀 생성
		        XlsxBuilder xlsxBuilder = new XlsxBuilder();
		        
		        xlsxBuilder.newSheet("일별수납통계");
		        xlsxBuilder.addHeader(0, 0, 0, 3, "경기고속도로가상계좌수납통계");
		        xlsxBuilder.addHeader(2, 2, 0, 0, "▶구분:일별현황");
		        xlsxBuilder.addHeader(3, 3, 0, 0, "▶"+paramVO.getStartDate()+"~"+paramVO.getEndDate());
		        
		        xlsxBuilder.addHeader(5, 6, 0, 0, "NO.");
		        xlsxBuilder.addHeader(5, 6, 1, 1, "입금일자");
		        xlsxBuilder.addHeader(5, 5, 2, 3, "가상계좌 입금");
		        xlsxBuilder.addHeader(6, 6, 2, 2, "건수");
		        xlsxBuilder.addHeader(6, 6, 3, 3, "금액");
		        
	
		        int j=0;
		        //데이터 첫 행은 합계
		        xlsxBuilder.addData(j++, "");
                xlsxBuilder.addData(j++, "총합");
                xlsxBuilder.addData(j++, vAcntAnalysisTot.getTotReceiptCnt());
                xlsxBuilder.addData(j++, vAcntAnalysisTot.getTotReceiptAmt());
                
                int i = 0;
		        for (VAcntAnalysisVO vAcntAnalysisVO : vAcntAnalysisList) {
		        	j=0;
		        	
		        	xlsxBuilder.newDataRow();

	                xlsxBuilder.addData(j++, ++i);
	                xlsxBuilder.addData(j++, vAcntAnalysisVO.getPayday());
	                xlsxBuilder.addData(j++, vAcntAnalysisVO.getAcntCnt());
	                xlsxBuilder.addData(j++, vAcntAnalysisVO.getRcpamt());
		        }
	
		        String fileName = new String("일별수납통계_" + System.currentTimeMillis());
		        response.setContentType("application/vnd.ms-excel");
		    	
		    	response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("euc-kr"),"8859_1")+".xlsx");
		    	
		    	xlsxBuilder.writeTo(response.getOutputStream());
		    	
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			resultMap.put("retCode", "9999");
			resultMap.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
			
		}
		
		resultMap.put("retCode", "0000");
		resultMap.put("retMsg", "정상적으로 등록 하였습니다.");
		
    }
	//--------------------------------------------------------------
	// 가상계좌 월별수납통계 화면으로 이동
	//--------------------------------------------------------------
	@RequestMapping(value="/vAccountMonthReceipt.do", method=RequestMethod.POST)
    public String vAccountMonthReceipt(@ModelAttribute("paramVO") ParamVO paramVO, 
    		final Model model, final HttpServletRequest request) throws Exception {

		logger.debug("xxxxxxxx vAccountMonthReceipt start xxxxxxxxxx");
		Gson gson = new Gson();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		//무조건 조회 하도록 수정 - 2018-03-12
		if(paramVO.getStartMonth()==null||paramVO.getStartMonth().equals("")){
			//등록된 건을 조회, 기본적으로 조회 기간을 최근 6개월로 paramVO에 강제 세팅 한다.
			StrUtil stringUtil = new StrUtil();
			
			paramVO.setStartMonth(stringUtil.getCalMonthStr(-6));
			paramVO.setEndMonth(stringUtil.getCurrentMonthStr());
			
		}
		paramVO.setPageIdx(1);
		paramVO.setPageSize(adpgsize);
		
		//조회 조건이 있을 경우 조회 해서 넘겨준다(탭 처리 때문)
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			VAcntAnalysisTotVO vAcntAnalysisTot = analysisMgmtService.getVAccountMonthReceiptTot(paramVO);
			int totCnt = vAcntAnalysisTot.getTotCnt();
			int maxPage = 0;
			
			//조회된 건수가 있으면
			if (totCnt > 0) {
				maxPage = totCnt / paramVO.getPageSize();
				if((totCnt % paramVO.getPageSize()) > 0){
					maxPage++;
				}

				paramVO.setStartIdx((paramVO.getPageSize()*(paramVO.getPageIdx()-1)));
				paramVO.setEndIdx(paramVO.getPageSize()*paramVO.getPageIdx());
				
				model.addAttribute("vAcntAnalysisTot", vAcntAnalysisTot);
				
				//목록 조회
				List<VAcntAnalysisVO> vAcntAnalysisList = analysisMgmtService.getVAccountMonthReceiptList(paramVO);
				model.addAttribute("vAcntAnalysisList", vAcntAnalysisList);					
			}

			resultMap.put("totCnt", totCnt);
			resultMap.put("maxPage", maxPage);
			
			model.addAttribute("resultMap", resultMap);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			throw e;
		}
		
		//파라미터 유지
		model.addAttribute("paramVO", paramVO);
		
		return "analysismgmt/vAccountMonthReceipt";
    }
	
	//--------------------------------------------------------------
	// 가상계좌 일별수납통계 엑셀 다운로드
	//--------------------------------------------------------------
	@RequestMapping(value="/vAccountMonthReceiptExcel.do", method=RequestMethod.POST)
	@ResponseBody
    public void vAccountMonthReceiptExcel(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx vAccountMonthReceiptExcel start xxxxxxxxxx");
		
		try {

			//합계 조회
			VAcntAnalysisTotVO vAcntAnalysisTot = analysisMgmtService.getVAccountMonthReceiptTot(paramVO);
			//목록 조회
			List<VAcntAnalysisVO> vAcntAnalysisList = analysisMgmtService.getVAccountMonthReceiptList(paramVO);
			
			if(vAcntAnalysisList.size()>0){
				//엑셀 생성
		        XlsxBuilder xlsxBuilder = new XlsxBuilder();
		        
		        xlsxBuilder.newSheet("월별수납통계");
		        xlsxBuilder.addHeader(0, 0, 0, 3, "경기고속도로가상계좌수납통계");
		        xlsxBuilder.addHeader(2, 2, 0, 0, "▶구분:월별현황");
		        xlsxBuilder.addHeader(3, 3, 0, 0, "▶"+paramVO.getStartMonth()+"~"+paramVO.getEndMonth());
		        
		        xlsxBuilder.addHeader(5, 6, 0, 0, "청구월");
		        xlsxBuilder.addHeader(5, 5, 1, 2, "수납률");
		        xlsxBuilder.addHeader(5, 5, 3, 4, "청구");
		        xlsxBuilder.addHeader(5, 5, 5, 6, "입금");
		        xlsxBuilder.addHeader(5, 5, 7, 8, "미납");
		        xlsxBuilder.addHeader(6, 6, 1, 1, "건수");
		        xlsxBuilder.addHeader(6, 6, 2, 2, "금액");
		        xlsxBuilder.addHeader(6, 6, 3, 3, "건수");
		        xlsxBuilder.addHeader(6, 6, 4, 4, "금액");
		        xlsxBuilder.addHeader(6, 6, 5, 5, "건수");
		        xlsxBuilder.addHeader(6, 6, 6, 6, "금액");
		        xlsxBuilder.addHeader(6, 6, 7, 7, "건수");
		        xlsxBuilder.addHeader(6, 6, 8, 8, "금액");
	
		        //데이터 첫 행은 합계
		        int j=0;
                xlsxBuilder.addData(j++, "총합");
                xlsxBuilder.addData(j++, vAcntAnalysisTot.getTotCntAvg());
                xlsxBuilder.addData(j++, vAcntAnalysisTot.getTotAmtAvg());
                xlsxBuilder.addData(j++, vAcntAnalysisTot.getTotClaimCnt());
                xlsxBuilder.addData(j++, vAcntAnalysisTot.getTotClaimAmt());
                xlsxBuilder.addData(j++, vAcntAnalysisTot.getTotReceiptCnt());
                xlsxBuilder.addData(j++, vAcntAnalysisTot.getTotReceiptAmt());
                xlsxBuilder.addData(j++, vAcntAnalysisTot.getTotDefaultCnt());
                xlsxBuilder.addData(j++, vAcntAnalysisTot.getTotDefaultAmt());
                
		        for (VAcntAnalysisVO vAcntAnalysisVO : vAcntAnalysisList) {
		        	j=0;
		        	xlsxBuilder.newDataRow();

	                xlsxBuilder.addData(j++, vAcntAnalysisVO.getRegMonth());
	                xlsxBuilder.addData(j++, vAcntAnalysisVO.getTotCntAvg());
	                xlsxBuilder.addData(j++, vAcntAnalysisVO.getTotAmtAvg());
	                xlsxBuilder.addData(j++, vAcntAnalysisVO.getTotClaimCnt());
	                xlsxBuilder.addData(j++, vAcntAnalysisVO.getTotClaimAmt());
	                xlsxBuilder.addData(j++, vAcntAnalysisVO.getTotReceiptCnt());
	                xlsxBuilder.addData(j++, vAcntAnalysisVO.getTotReceiptAmt());
	                xlsxBuilder.addData(j++, vAcntAnalysisVO.getTotDefaultCnt());
	                xlsxBuilder.addData(j++, vAcntAnalysisVO.getTotDefaultAmt());
		        }
	
		        String fileName = new String("월별수납통계_" + System.currentTimeMillis());
		        response.setContentType("application/vnd.ms-excel");
		    	
		    	response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("euc-kr"),"8859_1")+".xlsx");
		    	
		    	xlsxBuilder.writeTo(response.getOutputStream());
		    	
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			throw e;
			
		}
		
		resultMap.put("retCode", "0000");
		resultMap.put("retMsg", "정상적으로 등록 하였습니다.");
		
    }
	
	//--------------------------------------------------------------
	// 가상계좌 이용현황 화면으로 이동
	//--------------------------------------------------------------
	@RequestMapping(value="/vAccountUse.do", method=RequestMethod.POST)
    public String vAccountUse(@ModelAttribute("paramVO") ParamVO paramVO, 
    		final Model model, final HttpServletRequest request) throws Exception {

		logger.debug("xxxxxxxx getVAccountUse start xxxxxxxxxx");

		Gson gson = new Gson();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		//무조건 조회 하도록 수정 - 2018-03-12
		if(paramVO.getStartMonth()==null||paramVO.getStartMonth().equals("")){
			//등록된 건을 조회, 기본적으로 조회 기간을 최근 6개월로 paramVO에 강제 세팅 한다.
			StrUtil stringUtil = new StrUtil();
			
			paramVO.setStartMonth(stringUtil.getCalMonthStr(-6));
			paramVO.setEndMonth(stringUtil.getCurrentMonthStr());
			
		}
		paramVO.setPageIdx(1);
		paramVO.setPageSize(vapgsize);
		
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			VAcntAnalysisTotVO vAcntAnalysisTot = analysisMgmtService.getVAccountUseStateTot(paramVO);
			int totCnt = vAcntAnalysisTot.getTotCnt();
			int maxPage = 0;
			
			//조회된 건수가 있으면
			if (totCnt > 0) {
				maxPage = totCnt / paramVO.getPageSize();
				if((totCnt % paramVO.getPageSize()) > 0){
					maxPage++;
				}

				paramVO.setStartIdx((paramVO.getPageSize()*(paramVO.getPageIdx()-1)));
				paramVO.setEndIdx(paramVO.getPageSize()*paramVO.getPageIdx());
				
				model.addAttribute("vAcntAnalysisTot", vAcntAnalysisTot);
				
				//목록 조회
				List<VAcntAnalysisVO> vAcntAnalysisList = analysisMgmtService.getVAccountUseStateList(paramVO);
				model.addAttribute("vAcntAnalysisList", vAcntAnalysisList);					
			}

			resultMap.put("totCnt", totCnt);
			resultMap.put("maxPage", maxPage);
			resultMap.put("startIdx", paramVO.getStartIdx());
			
			model.addAttribute("resultMap", resultMap);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			throw e;
		}
		
		//파라미터 유지
		model.addAttribute("paramVO", paramVO);
		
		return "analysismgmt/vAccountUse";
		
    }
	
	//--------------------------------------------------------------
	// 가상계좌 이용현황 조회
	//--------------------------------------------------------------
	@RequestMapping(value="/getVAccountUse.do", method=RequestMethod.POST)
    public String getVAccountUse(@ModelAttribute("paramVO") ParamVO paramVO, 
    		final Model model, final HttpServletRequest request) throws Exception {

		logger.debug("xxxxxxxx getVAccountUse start xxxxxxxxxx");

		Gson gson = new Gson();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		paramVO.setPageSize(vapgsize);
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			VAcntAnalysisTotVO vAcntAnalysisTot = analysisMgmtService.getVAccountUseStateTot(paramVO);
			int totCnt = vAcntAnalysisTot.getTotCnt();
			int maxPage = 0;
			
			//조회된 건수가 있으면
			if (totCnt > 0) {
				maxPage = totCnt / paramVO.getPageSize();
				if((totCnt % paramVO.getPageSize()) > 0){
					maxPage++;
				}

				paramVO.setStartIdx((paramVO.getPageSize()*(paramVO.getPageIdx()-1)));
				paramVO.setEndIdx(paramVO.getPageSize()*paramVO.getPageIdx());
				
				model.addAttribute("vAcntAnalysisTot", vAcntAnalysisTot);
				
				//목록 조회
				List<VAcntAnalysisVO> vAcntAnalysisList = analysisMgmtService.getVAccountUseStateList(paramVO);
				model.addAttribute("vAcntAnalysisList", vAcntAnalysisList);					
			}

			resultMap.put("totCnt", totCnt);
			resultMap.put("maxPage", maxPage);
			resultMap.put("startIdx", paramVO.getStartIdx());
			
			model.addAttribute("resultMap", resultMap);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			throw e;
		}
		
		//파라미터 유지
		model.addAttribute("paramVO", paramVO);
		
		return "analysismgmt/vAccountUse";
    }
	
	//--------------------------------------------------------------
	// 문자 이용현황 화면으로 이동
	//--------------------------------------------------------------
	@RequestMapping(value="/msgUse.do", method=RequestMethod.POST)
    public String msgUse(@ModelAttribute("paramVO") ParamVO paramVO, 
    		final Model model, final HttpServletRequest request) throws Exception {

		Gson gson = new Gson();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx msgUse start xxxxxxxxxx");

		//무조건 조회 하도록 수정 - 2018-03-12
		if(paramVO.getStartMonth()==null||paramVO.getStartMonth().equals("")){
			//등록된 건을 조회, 기본적으로 조회 기간을 최근 6개월로 paramVO에 강제 세팅 한다.
			StrUtil stringUtil = new StrUtil();
			
			paramVO.setStartMonth(stringUtil.getCalMonthStr(-6));
			paramVO.setEndMonth(stringUtil.getCurrentMonthStr());
			
		}
		paramVO.setPageIdx(1);
		paramVO.setPageSize(mupgsize);
		
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			int totCnt = analysisMgmtService.getMsgUseStateListCnt(paramVO);
			int maxPage = 0;
			
			//조회된 건수가 있으면
			if (totCnt > 0) {
				maxPage = totCnt / paramVO.getPageSize();
				if((totCnt % paramVO.getPageSize()) > 0){
					maxPage++;
				}

				paramVO.setStartIdx((paramVO.getPageSize()*(paramVO.getPageIdx()-1)));
				paramVO.setEndIdx(paramVO.getPageSize()*paramVO.getPageIdx());
				
				//합계 조회
				TotMsgSendHistVO totMsgSendHistVO = analysisMgmtService.getMsgUseStateTot(paramVO);
				model.addAttribute("totMsgSendHistVO", totMsgSendHistVO);
				//목록 조회
				List<MsgUseStateInfoVO> msgUseStateInfoList = analysisMgmtService.getMsgUseStateList(paramVO);
				model.addAttribute("msgUseStateInfoList", msgUseStateInfoList);					
			}

			resultMap.put("totCnt", totCnt);
			resultMap.put("maxPage", maxPage);
			resultMap.put("startIdx", paramVO.getStartIdx());
//				resultMap.put("searchCondition", paramVO);
			
			model.addAttribute("resultMap", resultMap);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			throw e;
		}
		
		//파라미터 유지
		model.addAttribute("paramVO", paramVO);
		
		return "analysismgmt/msgUse";
    }
	
	//--------------------------------------------------------------
	// 문자 발송내역 화면으로 이동
	//--------------------------------------------------------------
	@RequestMapping(value="/msgSendList.do", method=RequestMethod.POST)
    public String msgSendList(@ModelAttribute("paramVO") ParamVO paramVO, 
    		final Model model, final HttpServletRequest request) throws Exception {

		Gson gson = new Gson();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx msgSendList start xxxxxxxxxx");

		//조회조건이 있으면 조회해서 넘겨 준다.
		if(paramVO.getStartDate()==null||paramVO.getStartDate().equals("")){
			//등록된 건을 조회, 기본적으로 조회 기간을 최근 3개월로 paramVO에 강제 세팅 한다.
			StrUtil stringUtil = new StrUtil();
			
			paramVO.setStartDate(stringUtil.getCalMonthDateStr(-3));
			paramVO.setEndDate(stringUtil.getCurrentDateStr());
			paramVO.setSuccessYn("A");
			
		}
		paramVO.setPageIdx(1);
		paramVO.setPageSize(mupgsize);
		
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			int totCnt = analysisMgmtService.getMsgSendHistListCnt(paramVO);
			int maxPage = 0;
			
			//조회된 건수가 있으면
			if (totCnt > 0) {
				maxPage = totCnt / paramVO.getPageSize();
				if((totCnt % paramVO.getPageSize()) > 0){
					maxPage++;
				}

				paramVO.setStartIdx((paramVO.getPageSize()*(paramVO.getPageIdx()-1)));
				paramVO.setEndIdx(paramVO.getPageSize()*paramVO.getPageIdx());
				
				//목록 조회
				List<MsgSendHistInfoVO> msgUseStateInfoList = analysisMgmtService.getMsgSendHistList(paramVO);
				model.addAttribute("msgUseStateInfoList", msgUseStateInfoList);					
			}

			resultMap.put("totCnt", totCnt);
			resultMap.put("maxPage", maxPage);
			resultMap.put("startIdx", paramVO.getStartIdx());
//				resultMap.put("searchCondition", paramVO);
			
			model.addAttribute("resultMap", resultMap);

		} catch (Exception e) {
			logger.error(e.getMessage());
			
			throw e;
		}
		
		//파라미터 유지
		model.addAttribute("paramVO", paramVO);
		
		return "analysismgmt/msgSendList";
    }

	//--------------------------------------------------------------
	// 메세지전송 팝업
	//--------------------------------------------------------------
	@RequestMapping(value="/msgContentPop.do", method=RequestMethod.POST)
    public String msgContentPop(String msgSendHistId, final Model model, final HttpServletRequest request) throws Exception {
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx msgContentPop start xxxxxxxxxx");

		try {

			//메세지전송이력 조회
			TbMsgSendHistVO tbMsgSendHistVO = analysisMgmtService.getMsgSendHist(msgSendHistId);
			model.addAttribute("tbMsgSendHistVO", tbMsgSendHistVO);					

			resultMap.put("retCode", "0000");
			resultMap.put("retMsg", "정상적으로 조회 되었습니다.");
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			throw e;
		}
		
		return "analysismgmt/msgContentPop";
    }

}
