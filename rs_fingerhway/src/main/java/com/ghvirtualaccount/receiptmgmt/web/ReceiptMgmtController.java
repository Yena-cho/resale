package com.ghvirtualaccount.receiptmgmt.web;

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

import com.ghvirtualaccount.cmmn.StrUtil;
import com.ghvirtualaccount.cmmn.XlsxBuilder;
import com.ghvirtualaccount.receiptmgmt.service.ReceiptMgmtService;
import com.ghvirtualaccount.vo.ParamVO;
import com.ghvirtualaccount.vo.PayerInfoVO;
import com.ghvirtualaccount.vo.ReceiptTotVO;
import com.ghvirtualaccount.vo.TbReceiptLastDownDtVO;
import com.google.gson.Gson;

@Controller
public class ReceiptMgmtController {

	private static Logger logger = LoggerFactory.getLogger(ReceiptMgmtController.class);
	
    @Value("${receiptstate.pagesize}")
    private int rspgsize;
    @Value("${receipt.pagesize}")
    private int rpgsize;
    @Value("${default.pagesize}")
    private int dpgsize;
    
	@Resource(name="receiptMgmtService")
	private ReceiptMgmtService receiptMgmtService;
	
	//--------------------------------------------------------------
	// 수납관리 화면으로 이동
	//--------------------------------------------------------------
	@RequestMapping(value="/receiptMgmt.do", method=RequestMethod.POST)
    public String goReceivemgmt(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request) throws Exception {
		
		Gson gson = new Gson();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx goReceivemgmt start xxxxxxxxxx");

		//당월을 paramVO에 강제 세팅 한다.
		StrUtil stringUtil = new StrUtil();
		
		paramVO.setStartMonth(stringUtil.getCurrentMonthStr());
		paramVO.setSuccessYn("Y");
		paramVO.setSearchInd("CAR");
		paramVO.setOrder("PAYDAY");
		paramVO.setPageIdx(1);
		paramVO.setPageSize(rspgsize);

		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			ReceiptTotVO receiptTotVO = receiptMgmtService.getReceiptStateTot(paramVO);
			model.addAttribute("receiptTot", receiptTotVO);
			
			int totCnt = receiptTotVO.getTotCnt();
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
//					TotMsgSendHistVO totMsgSendHistVO = receiptMgmtService.getMsgUseStateTot(paramVO);
//					model.addAttribute("totMsgSendHistVO", totMsgSendHistVO);
				
				//목록 조회
				List<PayerInfoVO> payerInfoList = receiptMgmtService.getReceiptStateList(paramVO);
				model.addAttribute("payerInfoList", payerInfoList);					
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
		
		return "receiptmgmt/receiptPresent";
    }
	
	//--------------------------------------------------------------
	// 입금목록 화면으로 이동
	//--------------------------------------------------------------
	@RequestMapping(value="/receiptList.do", method=RequestMethod.POST)
    public String goReceiptList(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request) throws Exception {
		
		Gson gson = new Gson();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx receiptList start xxxxxxxxxx");

		//등록된 건을 조회, 기본적으로 조회 기간을 8일전부터 1일전까지 paramVO에 강제 세팅 한다.
		StrUtil stringUtil = new StrUtil();
		
		paramVO.setStartDate(stringUtil.getCalDayDateStr(-8));
		paramVO.setEndDate(stringUtil.getCalDayDateStr(-1));
		paramVO.setPageIdx(1);
		paramVO.setPageSize(rpgsize);
		
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			TbReceiptLastDownDtVO tbReceiptLastDownDtVO = receiptMgmtService.getReceiptLastDownDt();
			model.addAttribute("tbReceiptLastDownDtVO", tbReceiptLastDownDtVO);
			
			ReceiptTotVO receiptTotVO = receiptMgmtService.getReceiptTot(paramVO);
			model.addAttribute("receiptTot", receiptTotVO);
			
			int totCnt = receiptTotVO.getTotCnt();
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
//					TotMsgSendHistVO totMsgSendHistVO = receiptMgmtService.getMsgUseStateTot(paramVO);
//					model.addAttribute("totMsgSendHistVO", totMsgSendHistVO);
				
				//목록 조회
				List<PayerInfoVO> payerInfoList = receiptMgmtService.getReceiptList(paramVO);
				model.addAttribute("payerInfoList", payerInfoList);					
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
		
		return "receiptmgmt/receiptLIst";
    }

	//--------------------------------------------------------------
	// 입금목록 조회용 화면으로 이동
	//--------------------------------------------------------------
	@RequestMapping(value="/receiptList_S.do", method=RequestMethod.POST)
    public String goReceiptListS(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request) throws Exception {
		
		Gson gson = new Gson();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx goReceiptListS start xxxxxxxxxx");

		//등록된 건을 조회, 기본적으로 조회 기간을 8일전부터 1일전까지 paramVO에 강제 세팅 한다.
		StrUtil stringUtil = new StrUtil();
		
		paramVO.setStartDate(stringUtil.getCalDayDateStr(-8));
		paramVO.setEndDate(stringUtil.getCalDayDateStr(-1));
		paramVO.setPageIdx(1);
		paramVO.setPageSize(rpgsize);
		
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			TbReceiptLastDownDtVO tbReceiptLastDownDtVO = receiptMgmtService.getReceiptLastDownDt();
			model.addAttribute("tbReceiptLastDownDtVO", tbReceiptLastDownDtVO);
			
			ReceiptTotVO receiptTotVO = receiptMgmtService.getReceiptTot(paramVO);
			model.addAttribute("receiptTot", receiptTotVO);
			
			int totCnt = receiptTotVO.getTotCnt();
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
//					TotMsgSendHistVO totMsgSendHistVO = receiptMgmtService.getMsgUseStateTot(paramVO);
//					model.addAttribute("totMsgSendHistVO", totMsgSendHistVO);
				
				//목록 조회
				List<PayerInfoVO> payerInfoList = receiptMgmtService.getReceiptList(paramVO);
				model.addAttribute("payerInfoList", payerInfoList);					
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
		
		return "receiptmgmt/receiptLIst_S";
    }
	
	//--------------------------------------------------------------
	// 미납목록 화면으로 이동
	//--------------------------------------------------------------
	@RequestMapping(value="/defaultList.do", method=RequestMethod.POST)
    public String goDefaultList(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request) throws Exception {
		
		Gson gson = new Gson();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx defaultList start xxxxxxxxxx");

		//당월을 paramVO에 강제 세팅 한다.
		StrUtil stringUtil = new StrUtil();
		
		paramVO.setStartMonth(stringUtil.getCurrentMonthStr());
		paramVO.setPageIdx(1);
		paramVO.setPageSize(dpgsize);
		
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			

			
			ReceiptTotVO receiptTotVO = receiptMgmtService.getDefaultTot(paramVO);
			model.addAttribute("receiptTot", receiptTotVO);
			
			int totCnt = receiptTotVO.getTotCnt();
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
//					TotMsgSendHistVO totMsgSendHistVO = receiptMgmtService.getMsgUseStateTot(paramVO);
//					model.addAttribute("totMsgSendHistVO", totMsgSendHistVO);
				
				//목록 조회
				List<PayerInfoVO> payerInfoList = receiptMgmtService.getDefaultList(paramVO);
				model.addAttribute("payerInfoList", payerInfoList);					
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
		
		return "receiptmgmt/defaultLIst";
    }
	
	//--------------------------------------------------------------
	// 수납현황 조회
	//--------------------------------------------------------------
	@RequestMapping(value="/getReceiptStateList.do", method=RequestMethod.POST)
    public String getReceiptStateList(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request) throws Exception {
		
		Gson gson = new Gson();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		
		logger.debug("xxxxxxxx getReceiptStateList start xxxxxxxxxx");

		paramVO.setPageSize(rspgsize);
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			ReceiptTotVO receiptTotVO = receiptMgmtService.getReceiptStateTot(paramVO);
			model.addAttribute("receiptTot", receiptTotVO);
			
			int totCnt = receiptTotVO.getTotCnt();
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
//					TotMsgSendHistVO totMsgSendHistVO = receiptMgmtService.getMsgUseStateTot(paramVO);
//					model.addAttribute("totMsgSendHistVO", totMsgSendHistVO);
				
				//목록 조회
				List<PayerInfoVO> payerInfoList = receiptMgmtService.getReceiptStateList(paramVO);
				model.addAttribute("payerInfoList", payerInfoList);					
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
		
		return "receiptmgmt/receiptPresent";
    }

	//--------------------------------------------------------------
	// 수납현황 Excel 다운로드
	//--------------------------------------------------------------
	@RequestMapping(value="/getReceiptStateExcel.do", method=RequestMethod.POST)
	@ResponseBody
    public void getReceiptStateExcel(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx getReceiptStateExcel start xxxxxxxxxx");
		
		try {

			//목록 조회
			List<PayerInfoVO> payerInfoList = receiptMgmtService.getReceiptStateExcel(paramVO);
			
			if(payerInfoList.size()>0){
				//엑셀 생성
		        XlsxBuilder xlsxBuilder = new XlsxBuilder();
		        
		        xlsxBuilder.newSheet("수납현황");
		        
		        xlsxBuilder.addHeader(0, 0, 0, 0, "No.");
		        xlsxBuilder.addHeader(0, 0, 1, 1, "차량번호");
		        xlsxBuilder.addHeader(0, 0, 2, 2, "차량주");
		        xlsxBuilder.addHeader(0, 0, 3, 3, "납부기한");
		        xlsxBuilder.addHeader(0, 0, 4, 4, "고객조회번호");
 		        xlsxBuilder.addHeader(0, 0, 5, 5, "총건수");
		        xlsxBuilder.addHeader(0, 0, 6, 6, "총금액");
		        xlsxBuilder.addHeader(0, 0, 7, 7, "가상계좌");
		        xlsxBuilder.addHeader(0, 0, 8, 8, "입금일시");
		        xlsxBuilder.addHeader(0, 0, 9, 9, "입금은행");
		        xlsxBuilder.addHeader(0, 0, 10, 10, "수납현황");
		        xlsxBuilder.addHeader(0, 0, 11, 11, "발생일시1");
		        xlsxBuilder.addHeader(0, 0, 12, 12, "발생원인1");
		        xlsxBuilder.addHeader(0, 0, 13, 13, "통행요금1");
		        xlsxBuilder.addHeader(0, 0, 14, 14, "통행장소1");
		        xlsxBuilder.addHeader(0, 0, 15, 15, "발생일시2");
		        xlsxBuilder.addHeader(0, 0, 16, 16, "발생원인2");
		        xlsxBuilder.addHeader(0, 0, 17, 17, "통행장소2");
		        xlsxBuilder.addHeader(0, 0, 18, 18, "통행요금2");
		        xlsxBuilder.addHeader(0, 0, 19, 19, "발생일시3");
		        xlsxBuilder.addHeader(0, 0, 20, 20, "발생원인3");
		        xlsxBuilder.addHeader(0, 0, 21, 21, "통행장소3");
		        xlsxBuilder.addHeader(0, 0, 22, 22, "통행요금3");
		        xlsxBuilder.addHeader(0, 0, 23, 23, "발생일시4");
		        xlsxBuilder.addHeader(0, 0, 24, 24, "발생원인4");
		        xlsxBuilder.addHeader(0, 0, 25, 25, "통행장소4");
		        xlsxBuilder.addHeader(0, 0, 26, 26, "통행요금4");
		        xlsxBuilder.addHeader(0, 0, 27, 27, "발생일시5");
		        xlsxBuilder.addHeader(0, 0, 28, 28, "발생원인5");
		        xlsxBuilder.addHeader(0, 0, 29, 29, "통행장소5");
		        xlsxBuilder.addHeader(0, 0, 30, 30, "통행요금5");
		        xlsxBuilder.addHeader(0, 0, 31, 31, "발생일시6");
		        xlsxBuilder.addHeader(0, 0, 32, 32, "발생원인6");
		        xlsxBuilder.addHeader(0, 0, 33, 33, "통행장소6");
		        xlsxBuilder.addHeader(0, 0, 34, 34, "통행요금6");
		        xlsxBuilder.addHeader(0, 0, 35, 35, "발생일시7");
		        xlsxBuilder.addHeader(0, 0, 36, 36, "발생원인7");
		        xlsxBuilder.addHeader(0, 0, 37, 37, "통행장소7");
		        xlsxBuilder.addHeader(0, 0, 38, 38, "통행요금7");
		        xlsxBuilder.addHeader(0, 0, 39, 39, "발생일시8");
		        xlsxBuilder.addHeader(0, 0, 40, 40, "발생원인8");
		        xlsxBuilder.addHeader(0, 0, 41, 41, "통행장소8");
		        xlsxBuilder.addHeader(0, 0, 42, 42, "통행요금8");
		        xlsxBuilder.addHeader(0, 0, 43, 43, "발생일시9");
		        xlsxBuilder.addHeader(0, 0, 44, 44, "발생원인9");
		        xlsxBuilder.addHeader(0, 0, 45, 45, "통행장소9");
		        xlsxBuilder.addHeader(0, 0, 46, 46, "통행요금9");
		        xlsxBuilder.addHeader(0, 0, 47, 47, "발생일시10");
		        xlsxBuilder.addHeader(0, 0, 48, 48, "발생원인10");
		        xlsxBuilder.addHeader(0, 0, 49, 49, "통행장소10");
		        xlsxBuilder.addHeader(0, 0, 50, 50, "통행요금10");
	
	
//		        for (int i=0;i<payerInfoList.size(); i++) {
		        int i=0;
		        for (PayerInfoVO payerInfoVO : payerInfoList) {

		        	xlsxBuilder.newDataRow();
		        	
		        	xlsxBuilder.addData(0, ++i);
	                xlsxBuilder.addData(1, payerInfoVO.getCarNum());
	                xlsxBuilder.addData(2, payerInfoVO.getCarOwnerNm());
	                xlsxBuilder.addData(3, payerInfoVO.getPayDueDt());
	                xlsxBuilder.addData(4, payerInfoVO.getCustInqNum());
	                xlsxBuilder.addData(5, payerInfoVO.getTotCnt());
	                xlsxBuilder.addData(6, payerInfoVO.getTotAmt());
	                xlsxBuilder.addData(7, payerInfoVO.getVirtualAcntNum());
	                xlsxBuilder.addData(8, payerInfoVO.getPayday());
	                xlsxBuilder.addData(9, payerInfoVO.getBankCdNm());
	                xlsxBuilder.addData(10, payerInfoVO.getPayYnNm());
	                xlsxBuilder.addData(11, payerInfoVO.getOccurDttm1());
	                xlsxBuilder.addData(12, payerInfoVO.getOccurReason1());
	                xlsxBuilder.addData(13, payerInfoVO.getPassAmt1());
	                xlsxBuilder.addData(14, payerInfoVO.getPassPlace1());
	                xlsxBuilder.addData(15, payerInfoVO.getOccurDttm2());
	                xlsxBuilder.addData(16, payerInfoVO.getOccurReason2());
	                xlsxBuilder.addData(17, payerInfoVO.getPassPlace2());
	                xlsxBuilder.addData(18, payerInfoVO.getPassAmt2());
	                xlsxBuilder.addData(19, payerInfoVO.getOccurDttm3());
	                xlsxBuilder.addData(20, payerInfoVO.getOccurReason3());
	                xlsxBuilder.addData(21, payerInfoVO.getPassPlace3());
	                xlsxBuilder.addData(22, payerInfoVO.getPassAmt3());
	                xlsxBuilder.addData(23, payerInfoVO.getOccurDttm4());
	                xlsxBuilder.addData(24, payerInfoVO.getOccurReason4());
	                xlsxBuilder.addData(25, payerInfoVO.getPassPlace4());
	                xlsxBuilder.addData(26, payerInfoVO.getPassAmt4());
	                xlsxBuilder.addData(27, payerInfoVO.getOccurDttm5());
	                xlsxBuilder.addData(28, payerInfoVO.getOccurReason5());
	                xlsxBuilder.addData(29, payerInfoVO.getPassPlace5());
	                xlsxBuilder.addData(30, payerInfoVO.getPassAmt5());
	                xlsxBuilder.addData(31, payerInfoVO.getOccurDttm6());
	                xlsxBuilder.addData(32, payerInfoVO.getOccurReason6());
	                xlsxBuilder.addData(33, payerInfoVO.getPassPlace6());
	                xlsxBuilder.addData(34, payerInfoVO.getPassAmt6());
	                xlsxBuilder.addData(35, payerInfoVO.getOccurDttm7());
	                xlsxBuilder.addData(36, payerInfoVO.getOccurReason7());
	                xlsxBuilder.addData(37, payerInfoVO.getPassPlace7());
	                xlsxBuilder.addData(38, payerInfoVO.getPassAmt7());
	                xlsxBuilder.addData(39, payerInfoVO.getOccurDttm8());
	                xlsxBuilder.addData(40, payerInfoVO.getOccurReason8());
	                xlsxBuilder.addData(41, payerInfoVO.getPassPlace8());
	                xlsxBuilder.addData(42, payerInfoVO.getPassAmt8());
	                xlsxBuilder.addData(43, payerInfoVO.getOccurDttm9());
	                xlsxBuilder.addData(44, payerInfoVO.getOccurReason9());
	                xlsxBuilder.addData(45, payerInfoVO.getPassPlace9());
	                xlsxBuilder.addData(46, payerInfoVO.getPassAmt9());
	                xlsxBuilder.addData(47, payerInfoVO.getOccurDttm10());
	                xlsxBuilder.addData(48, payerInfoVO.getOccurReason10());
	                xlsxBuilder.addData(49, payerInfoVO.getPassPlace10());
	                xlsxBuilder.addData(50, payerInfoVO.getPassAmt10());
	                
		        }
	
		        String fileName = new String("수납현황_" + System.currentTimeMillis());
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
	// 청구상세 팝업
	//--------------------------------------------------------------
	@RequestMapping(value="/claimDetailPop.do", method=RequestMethod.POST)
    public String claimDetailPop(@ModelAttribute("trno") String trno, final Model model, 
    		final HttpServletRequest request) throws Exception {
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx claimDetailPop start xxxxxxxxxx");

		try {
			//목록 조회
			PayerInfoVO payerInfo = receiptMgmtService.getClaimItem(trno);
			model.addAttribute("claimItemInfo", payerInfo);					

			resultMap.put("retCode", "0000");
			resultMap.put("retMsg", "정상적으로 등록 하였습니다.");
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			throw e;
		}
		
		//처리결과 세팅
		model.addAttribute("resultMap", resultMap);
		
		return "receiptmgmt/claimDetailPop";
    }
	
	//--------------------------------------------------------------
	// 메세지전송 팝업
	//--------------------------------------------------------------
	@RequestMapping(value="/receiptMsgPop.do", method=RequestMethod.POST)
    public String receiptMsgPop(@ModelAttribute("carOwnerNm") String carOwnerNm,
    		@ModelAttribute("occurReason1") String occurReason1,
    		@ModelAttribute("totAmt") String totAmt,
    		@ModelAttribute("virtualAcntNum") String virtualAcntNum,
    		@ModelAttribute("payDueDt") String payDueDt,
    		final Model model, final HttpServletRequest request) throws Exception {
		
		logger.debug("xxxxxxxx receiptMsgPop start xxxxxxxxxx");

		//파라미터 만들어 주고, 해당 페이지로 이동 한다.
		Map<String, Object> msgParam = new HashMap<String, Object>();
		
		msgParam.put("carOwnerNm", carOwnerNm.trim());
		msgParam.put("occurReason1", occurReason1.trim());
		msgParam.put("totAmt", totAmt.trim());
		msgParam.put("virtualAcntNum", virtualAcntNum.trim());
		msgParam.put("payDueDt", payDueDt.trim());
		
		model.addAttribute("msgParam", msgParam);
		
		return "receiptmgmt/receiptMsgPop";
    }
	
	//--------------------------------------------------------------
	// 입금목록 조회
	//--------------------------------------------------------------
	@RequestMapping(value="/getReceiptList.do", method=RequestMethod.POST)
    public String getReceiptList(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request) throws Exception {

		Gson gson = new Gson();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx getReceiptList start xxxxxxxxxx");

		paramVO.setPageSize(rpgsize);
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			TbReceiptLastDownDtVO tbReceiptLastDownDtVO = receiptMgmtService.getReceiptLastDownDt();
			model.addAttribute("tbReceiptLastDownDtVO", tbReceiptLastDownDtVO);
			
			ReceiptTotVO receiptTotVO = receiptMgmtService.getReceiptTot(paramVO);
			model.addAttribute("receiptTot", receiptTotVO);
			
			int totCnt = receiptTotVO.getTotCnt();
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
//					TotMsgSendHistVO totMsgSendHistVO = receiptMgmtService.getMsgUseStateTot(paramVO);
//					model.addAttribute("totMsgSendHistVO", totMsgSendHistVO);
				
				//목록 조회
				List<PayerInfoVO> payerInfoList = receiptMgmtService.getReceiptList(paramVO);
				model.addAttribute("payerInfoList", payerInfoList);					
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
		
		return "receiptmgmt/receiptLIst";
    }
	
	//--------------------------------------------------------------
	// 수납현황 Excel 다운로드
	//--------------------------------------------------------------
	@RequestMapping(value="/getReceiptExcel.do", method=RequestMethod.POST)
	@ResponseBody
    public void getReceiptExcel(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx getReceiptStateExcel start xxxxxxxxxx");
		
		try {

			//목록 조회
			List<PayerInfoVO> payerInfoList = receiptMgmtService.getReceiptExcel(paramVO);
			
			if(payerInfoList.size()>0){
				//엑셀 생성
		        XlsxBuilder xlsxBuilder = new XlsxBuilder();
		        
		        xlsxBuilder.newSheet("입금목록");
		        
//		        xlsxBuilder.addHeader(0, 0, 0, 0, "고객조회번호");
//		        xlsxBuilder.addHeader(0, 0, 1, 1, "금액");
//		        xlsxBuilder.addHeader(0, 0, 2, 2, "수납점명");
//		        xlsxBuilder.addHeader(0, 0, 3, 3, "수납일");
//		        xlsxBuilder.addHeader(0, 0, 4, 4, "수수료");
//		        xlsxBuilder.addHeader(0, 0, 5, 5, "처리구분");
	
		        for (int i=0;i<payerInfoList.size(); i++) {
//		        for (PayerInfoVO payerInfoVO : payerInfoList) {

		        	xlsxBuilder.newDataRow();
		        	PayerInfoVO payerInfoVO = payerInfoList.get(i);
		        	int j = 0;
		        	
		        	xlsxBuilder.addData(j++, (i+1));
	                xlsxBuilder.addData(j++, payerInfoVO.getCustInqNum());
	                xlsxBuilder.addData(j++, payerInfoVO.getTotAmt());
	                xlsxBuilder.addData(j++, payerInfoVO.getBankCdNm());
	                xlsxBuilder.addData(j++, payerInfoVO.getBchCdNm());
	                xlsxBuilder.addData(j++, payerInfoVO.getPayday());
	                xlsxBuilder.addData(j++, payerInfoVO.getFee());
	                xlsxBuilder.addData(j++, "신한은행가상계좌");
	                
		        }
	
		        String fileName = new String("입금목록_" + System.currentTimeMillis());
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
	// 미납목록 조회
	//--------------------------------------------------------------
	@RequestMapping(value="/getDefaultList.do", method=RequestMethod.POST)
    public String getDefaultList(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request) throws Exception {

		Gson gson = new Gson();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx getReceiptList start xxxxxxxxxx");

		paramVO.setPageSize(dpgsize);
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			ReceiptTotVO receiptTotVO = receiptMgmtService.getDefaultTot(paramVO);
			model.addAttribute("receiptTot", receiptTotVO);
			
			int totCnt = receiptTotVO.getTotCnt();
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
//					TotMsgSendHistVO totMsgSendHistVO = receiptMgmtService.getMsgUseStateTot(paramVO);
//					model.addAttribute("totMsgSendHistVO", totMsgSendHistVO);
				
				//목록 조회
				List<PayerInfoVO> payerInfoList = receiptMgmtService.getDefaultList(paramVO);
				model.addAttribute("payerInfoList", payerInfoList);					
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
		
		return "receiptmgmt/defaultLIst";
    }

	//--------------------------------------------------------------
	// 미납목록 Excel 다운로드
	//--------------------------------------------------------------
	@RequestMapping(value="/getDefaultExcel.do", method=RequestMethod.POST)
	@ResponseBody
    public void getDefaultExcel(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx getDefaultExcel start xxxxxxxxxx");
		
		try {

			//목록 조회
			List<PayerInfoVO> payerInfoList = receiptMgmtService.getDefaultExcel(paramVO);
			
			if(payerInfoList.size()>0){
				//엑셀 생성
		        XlsxBuilder xlsxBuilder = new XlsxBuilder();
		        
		        xlsxBuilder.newSheet("미납목록");
		        
		        xlsxBuilder.addHeader(0, 0, 0, 0, "차량번호");
		        xlsxBuilder.addHeader(0, 0, 1, 1, "차량주");
		        xlsxBuilder.addHeader(0, 0, 2, 2, "납부기한");
		        xlsxBuilder.addHeader(0, 0, 3, 3, "주소");
		        xlsxBuilder.addHeader(0, 0, 4, 4, "지로번호");
		        xlsxBuilder.addHeader(0, 0, 5, 5, "고객조회번호");
		        xlsxBuilder.addHeader(0, 0, 6, 6, "총금액");
		        xlsxBuilder.addHeader(0, 0, 7, 7, "고지금&코드");
		        xlsxBuilder.addHeader(0, 0, 8, 8, "지로코드");
		        xlsxBuilder.addHeader(0, 0, 9, 9, "발생일시1");
		        xlsxBuilder.addHeader(0, 0, 10, 10, "발생원인1");
		        xlsxBuilder.addHeader(0, 0, 11, 11, "통행요금1");
		        xlsxBuilder.addHeader(0, 0, 12, 12, "통행장소1");
		        xlsxBuilder.addHeader(0, 0, 13, 13, "발생일시2");
		        xlsxBuilder.addHeader(0, 0, 14, 14, "발생원인2");
		        xlsxBuilder.addHeader(0, 0, 15, 15, "통행장소2");
		        xlsxBuilder.addHeader(0, 0, 16, 16, "통행요금2");
		        xlsxBuilder.addHeader(0, 0, 17, 17, "발생일시3");
		        xlsxBuilder.addHeader(0, 0, 18, 18, "발생원인3");
		        xlsxBuilder.addHeader(0, 0, 19, 19, "통행장소3");
		        xlsxBuilder.addHeader(0, 0, 20, 20, "통행요금3");
		        xlsxBuilder.addHeader(0, 0, 21, 21, "발생일시4");
		        xlsxBuilder.addHeader(0, 0, 22, 22, "발생원인4");
		        xlsxBuilder.addHeader(0, 0, 23, 23, "통행장소4");
		        xlsxBuilder.addHeader(0, 0, 24, 24, "통행요금4");
		        xlsxBuilder.addHeader(0, 0, 25, 25, "발생일시5");
		        xlsxBuilder.addHeader(0, 0, 26, 26, "발생원인5");
		        xlsxBuilder.addHeader(0, 0, 27, 27, "통행장소5");
		        xlsxBuilder.addHeader(0, 0, 28, 28, "통행요금5");
		        xlsxBuilder.addHeader(0, 0, 29, 29, "발생일시6");
		        xlsxBuilder.addHeader(0, 0, 30, 30, "발생원인6");
		        xlsxBuilder.addHeader(0, 0, 31, 31, "통행장소6");
		        xlsxBuilder.addHeader(0, 0, 32, 32, "통행요금6");
		        xlsxBuilder.addHeader(0, 0, 33, 33, "발생일시7");
		        xlsxBuilder.addHeader(0, 0, 34, 34, "발생원인7");
		        xlsxBuilder.addHeader(0, 0, 35, 35, "통행장소7");
		        xlsxBuilder.addHeader(0, 0, 36, 36, "통행요금7");
		        xlsxBuilder.addHeader(0, 0, 37, 37, "발생일시8");
		        xlsxBuilder.addHeader(0, 0, 38, 38, "발생원인8");
		        xlsxBuilder.addHeader(0, 0, 39, 39, "통행장소8");
		        xlsxBuilder.addHeader(0, 0, 40, 40, "통행요금8");
		        xlsxBuilder.addHeader(0, 0, 41, 41, "발생일시9");
		        xlsxBuilder.addHeader(0, 0, 42, 42, "발생원인9");
		        xlsxBuilder.addHeader(0, 0, 43, 43, "통행장소9");
		        xlsxBuilder.addHeader(0, 0, 44, 44, "통행요금9");
		        xlsxBuilder.addHeader(0, 0, 45, 45, "발생일시10");
		        xlsxBuilder.addHeader(0, 0, 46, 46, "발생원인10");
		        xlsxBuilder.addHeader(0, 0, 47, 47, "통행장소10");
		        xlsxBuilder.addHeader(0, 0, 48, 48, "통행요금10");
		        xlsxBuilder.addHeader(0, 0, 49, 49, "운헁기간");
	
	
//		        for (int i=0;i<payerInfoList.size(); i++) {
		        for (PayerInfoVO payerInfoVO : payerInfoList) {

		        	int j = 0;
		        	xlsxBuilder.newDataRow();
		        	
	                xlsxBuilder.addData(j++, payerInfoVO.getCarNum());
	                xlsxBuilder.addData(j++, payerInfoVO.getCarOwnerNm());
	                xlsxBuilder.addData(j++, payerInfoVO.getPayDueDt());
	                xlsxBuilder.addData(j++, payerInfoVO.getCarOwnerAddr());
	                xlsxBuilder.addData(j++, payerInfoVO.getGiroNum());
	                xlsxBuilder.addData(j++, payerInfoVO.getCustInqNum());
	                xlsxBuilder.addData(j++, payerInfoVO.getTotAmt());
	                xlsxBuilder.addData(j++, payerInfoVO.getNotiAmtCd());
	                xlsxBuilder.addData(j++, payerInfoVO.getGiroCd());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurDttm1());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurReason1());
	                //1번 항목만 통행요금과 통행장소 항목 위치가 바뀜
	                xlsxBuilder.addData(j++, payerInfoVO.getPassAmt1());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassPlace1());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurDttm2());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurReason2());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassPlace2());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassAmt2());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurDttm3());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurReason3());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassPlace3());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassAmt3());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurDttm4());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurReason4());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassPlace4());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassAmt4());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurDttm5());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurReason5());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassPlace5());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassAmt5());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurDttm6());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurReason6());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassPlace6());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassAmt6());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurDttm7());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurReason7());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassPlace7());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassAmt7());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurDttm8());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurReason8());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassPlace8());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassAmt8());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurDttm9());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurReason9());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassPlace9());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassAmt9());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurDttm10());
	                xlsxBuilder.addData(j++, payerInfoVO.getOccurReason10());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassPlace10());
	                xlsxBuilder.addData(j++, payerInfoVO.getPassAmt10());
	                xlsxBuilder.addData(j++, payerInfoVO.getRunDt());
		        }
	
		        String fileName = new String("미납목록_" + System.currentTimeMillis());
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
	
}
