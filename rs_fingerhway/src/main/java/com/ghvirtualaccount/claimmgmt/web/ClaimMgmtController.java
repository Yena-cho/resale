package com.ghvirtualaccount.claimmgmt.web;

import java.io.InputStream;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ghvirtualaccount.claimmgmt.service.ClaimMgmtService;
import com.ghvirtualaccount.cmmn.StrUtil;
import com.ghvirtualaccount.cmmn.XlsxBuilder;
import com.ghvirtualaccount.cmmn.XlsxEventParser;
import com.ghvirtualaccount.vo.ParamVO;
import com.ghvirtualaccount.vo.PayerInfoMasterInfoVO;
import com.ghvirtualaccount.vo.PayerInfoVO;
import com.google.gson.Gson;

@Controller
public class ClaimMgmtController {

	private static Logger logger = LoggerFactory.getLogger(ClaimMgmtController.class);
	
	@Resource(name="claimMgmtService")
	private ClaimMgmtService claimMgmtService;
	
	@Value("${claim.pagesize}")
    private int pgsize;
	
	//--------------------------------------------------------------
	// 청구등록 화면으로 이동
	//--------------------------------------------------------------
	@RequestMapping(value="/claimMgmt.do", method=RequestMethod.POST)
    public String claimMgmt(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request) throws Exception {

		Gson gson = new Gson();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx claimMgmt start xxxxxxxxxx");

		paramVO.setPageIdx(1);
		paramVO.setPageSize(pgsize);
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			//등록된 건을 조회, 기본적으로 조회 기간을 최근 3개월로 paramVO에 강제 세팅 한다.
			StrUtil stringUtil = new StrUtil();
			
			paramVO.setStartMonth(stringUtil.getCalMonthStr(-3));
			paramVO.setEndMonth(stringUtil.getCurrentMonthStr());
			
			
			int totCnt = claimMgmtService.getPayerInfoMasterListCnt(paramVO);
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
				List<PayerInfoMasterInfoVO> payerInfoMasterInfoList = claimMgmtService.getPayerInfoMasterList(paramVO);
				model.addAttribute("payerInfoMasterInfoList", payerInfoMasterInfoList);					
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
		
		return "claimmgmt/claimReg";
    }

	//--------------------------------------------------------------
	// 청구등록 조회
	//--------------------------------------------------------------
	@RequestMapping(value="/getPayerInfoMasterList.do", method=RequestMethod.POST)
    public String getPayerInfoMasterList(@ModelAttribute("paramVO") ParamVO paramVO, final Model model, 
    		final HttpServletRequest request) throws Exception {

		Gson gson = new Gson();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx claimMgmt start xxxxxxxxxx");
		paramVO.setPageSize(pgsize);
		try {
			logger.debug(gson.toJson(paramVO, ParamVO.class));
			
			int totCnt = claimMgmtService.getPayerInfoMasterListCnt(paramVO);
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
				List<PayerInfoMasterInfoVO> payerInfoMasterInfoList = claimMgmtService.getPayerInfoMasterList(paramVO);
				model.addAttribute("payerInfoMasterInfoList", payerInfoMasterInfoList);					
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
		
		return "claimmgmt/claimReg";
    }
	
	//--------------------------------------------------------------
	// 파일 등록
	//--------------------------------------------------------------
	@RequestMapping(value="/claimReg.do", method=RequestMethod.POST)
	@ResponseBody
    public Map<String,Object> claimReg( @RequestParam("excelFile") MultipartFile mFile, 
    		@RequestParam("regUserId") String regUserId, final Model model, HttpServletRequest request) throws Exception {
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx claimReg start xxxxxxxxxx");
		
		try {
			InputStream inputStream = mFile.getInputStream();

			//Excel workbook 생성 후 서비스 호출
			List<List<Object>> itemList = XlsxEventParser.parse(inputStream, 2, 50);
			
			logger.debug("xxxxxxxx itemList.size() : "+itemList.size());
			logger.debug("xxxxxxxx itemList.get(0).size() : "+itemList.get(0).size());
			
			Map<String,Object> returnMap = claimMgmtService.validatePayerInfo(itemList);
			
			if(returnMap != null){
				resultMap.put("retCode", (String)returnMap.get("retCode"));
				resultMap.put("retMsg", (String)returnMap.get("retMsg"));
				
				return resultMap;
			} else {
				claimMgmtService.regPayerInfo(itemList,regUserId);
			}
			
			resultMap.put("retCode", "0000");
			resultMap.put("retMsg", "정상적으로 등록 하였습니다.");
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			resultMap.put("retCode", "9999");
			resultMap.put("retMsg", e.getMessage());
			
			//resultMap.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
			
		}
		
		return resultMap;
    }
	
	//--------------------------------------------------------------
	// 엑셀 다운로드
	//--------------------------------------------------------------
	@RequestMapping(value="/downloadPayerInfoList.do", method=RequestMethod.POST)
	@ResponseBody
    public void downloadPayerInfoList( @RequestParam("payerInfoId") String payerInfoId, final Model model, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		logger.debug("xxxxxxxx downloadPayerInfoList start xxxxxxxxxx");
		
		try {

			//목록 조회
			List<PayerInfoVO> payerInfoList = claimMgmtService.getPayerInfoList(payerInfoId);
			
			if(payerInfoList.size()>0){
				//엑셀 생성
		        XlsxBuilder xlsxBuilder = new XlsxBuilder();
		        
		        xlsxBuilder.newSheet("고지서정보");
		        xlsxBuilder.addHeader(0, 0, 0, 50, "통합안내문 발송 데이터");
		        xlsxBuilder.addHeader(1, 1, 0, 50, "발송인 : (우)18329               경기도 화성시 봉담읍 최루백로 243-89");
		        
//		        xlsxBuilder.setColumnAlign(1, 0, HorizontalAlignment.LEFT);
		        
		        xlsxBuilder.addHeader(2, 2, 0, 0, "차량번호");
		        xlsxBuilder.addHeader(2, 2, 1, 1, "차량주");
		        xlsxBuilder.addHeader(2, 2, 2, 2, "납부기한");
		        xlsxBuilder.addHeader(2, 2, 3, 3, "주소");
		        xlsxBuilder.addHeader(2, 2, 4, 4, "지로번호");
		        xlsxBuilder.addHeader(2, 2, 5, 5, "고객조회번호");
		        xlsxBuilder.addHeader(2, 2, 6, 6, "총금액");
		        xlsxBuilder.addHeader(2, 2, 7, 7, "고지금&코드");
		        xlsxBuilder.addHeader(2, 2, 8, 8, "지로코드");
		        xlsxBuilder.addHeader(2, 2, 9, 9, "발생일시1");
		        xlsxBuilder.addHeader(2, 2, 10, 10, "발생원인1");
		        xlsxBuilder.addHeader(2, 2, 11, 11, "통행요금1");
		        xlsxBuilder.addHeader(2, 2, 12, 12, "통행장소1");
		        xlsxBuilder.addHeader(2, 2, 13, 13, "발생일시2");
		        xlsxBuilder.addHeader(2, 2, 14, 14, "발생원인2");
		        xlsxBuilder.addHeader(2, 2, 15, 15, "통행장소2");
		        xlsxBuilder.addHeader(2, 2, 16, 16, "통행요금2");
		        xlsxBuilder.addHeader(2, 2, 17, 17, "발생일시3");
		        xlsxBuilder.addHeader(2, 2, 18, 18, "발생원인3");
		        xlsxBuilder.addHeader(2, 2, 19, 19, "통행장소3");
		        xlsxBuilder.addHeader(2, 2, 20, 20, "통행요금3");
		        xlsxBuilder.addHeader(2, 2, 21, 21, "발생일시4");
		        xlsxBuilder.addHeader(2, 2, 22, 22, "발생원인4");
		        xlsxBuilder.addHeader(2, 2, 23, 23, "통행장소4");
		        xlsxBuilder.addHeader(2, 2, 24, 24, "통행요금4");
		        xlsxBuilder.addHeader(2, 2, 25, 25, "발생일시5");
		        xlsxBuilder.addHeader(2, 2, 26, 26, "발생원인5");
		        xlsxBuilder.addHeader(2, 2, 27, 27, "통행장소5");
		        xlsxBuilder.addHeader(2, 2, 28, 28, "통행요금5");
		        xlsxBuilder.addHeader(2, 2, 29, 29, "발생일시6");
		        xlsxBuilder.addHeader(2, 2, 30, 30, "발생원인6");
		        xlsxBuilder.addHeader(2, 2, 31, 31, "통행장소6");
		        xlsxBuilder.addHeader(2, 2, 32, 32, "통행요금6");
		        xlsxBuilder.addHeader(2, 2, 33, 33, "발생일시7");
		        xlsxBuilder.addHeader(2, 2, 34, 34, "발생원인7");
		        xlsxBuilder.addHeader(2, 2, 35, 35, "통행장소7");
		        xlsxBuilder.addHeader(2, 2, 36, 36, "통행요금7");
		        xlsxBuilder.addHeader(2, 2, 37, 37, "발생일시8");
		        xlsxBuilder.addHeader(2, 2, 38, 38, "발생원인8");
		        xlsxBuilder.addHeader(2, 2, 39, 39, "통행장소8");
		        xlsxBuilder.addHeader(2, 2, 40, 40, "통행요금8");
		        xlsxBuilder.addHeader(2, 2, 41, 41, "발생일시9");
		        xlsxBuilder.addHeader(2, 2, 42, 42, "발생원인9");
		        xlsxBuilder.addHeader(2, 2, 43, 43, "통행장소9");
		        xlsxBuilder.addHeader(2, 2, 44, 44, "통행요금9");
		        xlsxBuilder.addHeader(2, 2, 45, 45, "발생일시10");
		        xlsxBuilder.addHeader(2, 2, 46, 46, "발생원인10");
		        xlsxBuilder.addHeader(2, 2, 47, 47, "통행장소10");
		        xlsxBuilder.addHeader(2, 2, 48, 48, "통행요금10");
		        xlsxBuilder.addHeader(2, 2, 49, 49, "운헁기간");
		        xlsxBuilder.addHeader(2, 2, 50, 50, "가상계좌");
	
//		        for (int i=0;i<payerInfoList.size(); i++) {
		        for (PayerInfoVO payerInfoVO : payerInfoList) {

		        	xlsxBuilder.newDataRow();

	                xlsxBuilder.addData(0, payerInfoVO.getCarNum());
	                xlsxBuilder.addData(1, payerInfoVO.getCarOwnerNm());
	                xlsxBuilder.addData(2, payerInfoVO.getPayDueDt());
	                xlsxBuilder.addData(3, payerInfoVO.getCarOwnerAddr());
	                xlsxBuilder.addData(4, payerInfoVO.getGiroNum());
	                xlsxBuilder.addData(5, payerInfoVO.getCustInqNum());
	                xlsxBuilder.addData(6, payerInfoVO.getTotAmt());
	                xlsxBuilder.addData(7, payerInfoVO.getNotiAmtCd());
	                xlsxBuilder.addData(8, payerInfoVO.getGiroCd());
	                xlsxBuilder.addData(9, payerInfoVO.getOccurDttm1());
	                xlsxBuilder.addData(10, payerInfoVO.getOccurReason1());
	                //1번 항목만 통행요금과 통행장소 항목 위치가 바뀜
	                xlsxBuilder.addData(11, payerInfoVO.getPassAmt1());
	                xlsxBuilder.addData(12, payerInfoVO.getPassPlace1());
	                xlsxBuilder.addData(13, payerInfoVO.getOccurDttm2());
	                xlsxBuilder.addData(14, payerInfoVO.getOccurReason2());
	                xlsxBuilder.addData(15, payerInfoVO.getPassPlace2());
	                xlsxBuilder.addData(16, payerInfoVO.getPassAmt2());
	                xlsxBuilder.addData(17, payerInfoVO.getOccurDttm3());
	                xlsxBuilder.addData(18, payerInfoVO.getOccurReason3());
	                xlsxBuilder.addData(19, payerInfoVO.getPassPlace3());
	                xlsxBuilder.addData(20, payerInfoVO.getPassAmt3());
	                xlsxBuilder.addData(21, payerInfoVO.getOccurDttm4());
	                xlsxBuilder.addData(22, payerInfoVO.getOccurReason4());
	                xlsxBuilder.addData(23, payerInfoVO.getPassPlace4());
	                xlsxBuilder.addData(24, payerInfoVO.getPassAmt4());
	                xlsxBuilder.addData(25, payerInfoVO.getOccurDttm5());
	                xlsxBuilder.addData(26, payerInfoVO.getOccurReason5());
	                xlsxBuilder.addData(27, payerInfoVO.getPassPlace5());
	                xlsxBuilder.addData(28, payerInfoVO.getPassAmt5());
	                xlsxBuilder.addData(29, payerInfoVO.getOccurDttm6());
	                xlsxBuilder.addData(30, payerInfoVO.getOccurReason6());
	                xlsxBuilder.addData(31, payerInfoVO.getPassPlace6());
	                xlsxBuilder.addData(32, payerInfoVO.getPassAmt6());
	                xlsxBuilder.addData(33, payerInfoVO.getOccurDttm7());
	                xlsxBuilder.addData(34, payerInfoVO.getOccurReason7());
	                xlsxBuilder.addData(35, payerInfoVO.getPassPlace7());
	                xlsxBuilder.addData(36, payerInfoVO.getPassAmt7());
	                xlsxBuilder.addData(37, payerInfoVO.getOccurDttm8());
	                xlsxBuilder.addData(38, payerInfoVO.getOccurReason8());
	                xlsxBuilder.addData(39, payerInfoVO.getPassPlace8());
	                xlsxBuilder.addData(40, payerInfoVO.getPassAmt8());
	                xlsxBuilder.addData(41, payerInfoVO.getOccurDttm9());
	                xlsxBuilder.addData(42, payerInfoVO.getOccurReason9());
	                xlsxBuilder.addData(43, payerInfoVO.getPassPlace9());
	                xlsxBuilder.addData(44, payerInfoVO.getPassAmt9());
	                xlsxBuilder.addData(45, payerInfoVO.getOccurDttm10());
	                xlsxBuilder.addData(46, payerInfoVO.getOccurReason10());
	                xlsxBuilder.addData(47, payerInfoVO.getPassPlace10());
	                xlsxBuilder.addData(48, payerInfoVO.getPassAmt10());
	                xlsxBuilder.addData(49, payerInfoVO.getRunDt());
	                xlsxBuilder.addData(50, payerInfoVO.getVirtualAcntNum());
	                
		        }
	
		        String fileName = new String("고지서정보_" + System.currentTimeMillis());
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

