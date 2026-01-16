package com.finger.shinhandamoa.sys.setting.web;

import com.finger.shinhandamoa.bank.dto.BankReg01DTO;
import com.finger.shinhandamoa.bank.service.BankMgmtService;
import com.finger.shinhandamoa.common.FileService;
import com.finger.shinhandamoa.data.file.mapper.SimpleFileMapper;
import com.finger.shinhandamoa.data.table.mapper.*;
import com.finger.shinhandamoa.data.table.model.*;
import com.finger.shinhandamoa.service.WithdrawAgreementService;
import com.finger.shinhandamoa.sys.setting.dto.AutoDTO;
import com.finger.shinhandamoa.sys.setting.service.AutoTranMgmtService;
import com.finger.shinhandamoa.vo.PageVO;
import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.msgio.layout.LayoutFactory;
import kr.co.finger.msgio.msg.AutoWithdrawalHelper;
import kr.co.finger.msgio.msg.EB21;
import kr.co.finger.msgio.msg.EB22;
import kr.co.finger.shinhandamoa.domain.model.ClientDO;
import kr.co.finger.shinhandamoa.domain.repository.ClientRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author  by puki
 * @date    2018. 5. 23.
 * @desc    최초생성
 * @version 
 * 
 */
@Controller
@RequestMapping("sys/auto/**")
public class AutoTranMgmtController {
	private static final Logger logger = LoggerFactory.getLogger(AutoTranMgmtController.class);
	
	@Value("${file.path.cms}")
	private String cmsPath;
	
	@Value("${file.path.auto}")
	private String autoPath;
	
	@Value("${autoWithdrawal.corpCode}")
	private String corpCode;
	
	@Value("${autoWithdrawal.accountNo}")
	private String accountNo;
	
	@Value("${autoWithdrawal.bankCode}")
	private String bankCode;
	
	@Value("${autoWithdrawal.shinhanCode}")
	private String shinhanCode;
	
	@Value("${autoWithdrawal.fileDir}")
	private String fileDir;
	
	@Value("${autoWithdrawal.dueDay}")
	private String dueDay;
	
	@Value("${file.path.temp}")
	private String tempPath;
	
	@Inject
	private AutoTranMgmtService autoTranMgmtService;
	
	@Inject
    private FileService fileService;

    @Inject
    private BankMgmtService bankMgmtService;
	
	@Autowired
    private LayoutFactory withdrawalLayoutFactory;

	@Autowired
	private KftcWithdrawMapper kftcWithdrawMapper;

    @Autowired
    private KftcPayerMapper kftcPayerMapper;
    
    @Autowired
    private ClientRepository clientRepository;
    
	/*
	 * 설정관리 > 자동이체신청관리
	 */
	@RequestMapping("autoTran")
	public ModelAndView selAppAuthList() throws Exception {
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("sys/setting/autoTranMgmt/autoTranList");
		return mav;
	}

    @RequestMapping("autoTranReg")
    public ModelAndView selAppAuthRegList() throws Exception {

        ModelAndView mav = new ModelAndView();

        mav.setViewName("sys/setting/autoTranMgmt/autoTranReg");
        return mav;
    }
	
	/*
	 * 설정관리 > 자동이체신청관리 > 화면내조회
	 */
	@ResponseBody
	@RequestMapping("autoTranSel")
	public HashMap<String, Object> appAuthList(@RequestBody AutoDTO dto) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("startDt", dto.getStartDt());
			map.put("endDt", dto.getEndDt());
			map.put("chaCd", dto.getChaCd());
			map.put("chaName", dto.getChaName());
			map.put("searchOption", dto.getSearchOption());
			map.put("keyword", dto.getKeyword());
			map.put("stList", dto.getStList());
			map.put("cmsList", dto.getCmsList());
			map.put("orderBy", dto.getOrderBy());
			
			int count = autoTranMgmtService.selAutoTranCount(map);
			map.put("count", count);
			
			PageVO page = new PageVO(count, dto.getCurPage(), dto.getPageScale());
			int start = page.getPageBegin();
			int end = page.getPageEnd();	
			map.put("start", start);
			map.put("end", end);
			
			List<AutoDTO> list = autoTranMgmtService.selAutoTranList(map);
			map.put("list", list);
			map.put("pager", page);
			map.put("path", cmsPath);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/*
	 * 자동이체관리 > 자동이체동의관리 > 화면내조회
	 */
	@ResponseBody
	@RequestMapping("autoTranRegSel")
	public HashMap<String, Object> autoTranRegSel(@RequestBody AutoDTO dto) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("startDt", dto.getStartDt());
			map.put("endDt", dto.getEndDt());
			map.put("chaCd", dto.getChaCd());
			map.put("chaName", dto.getChaName());
			map.put("searchOption", dto.getSearchOption());
			map.put("keyword", dto.getKeyword());
			map.put("consentTy", dto.getConsentTy());
			map.put("stList", dto.getStList());
			map.put("chaList", dto.getChaList());
            map.put("cmsList", dto.getCmsList());
			map.put("chatrty", dto.getChatrty());
			map.put("orderBy", dto.getOrderBy());

			int count = autoTranMgmtService.selAutoTranRegCount(map);
			map.put("count", count);

			PageVO page = new PageVO(count, dto.getCurPage(), dto.getPageScale());
			int start = page.getPageBegin();
			int end = page.getPageEnd();
			map.put("start", start);
			map.put("end", end);

			List<AutoDTO> list = autoTranMgmtService.selAutoTranRegList(map);
			map.put("list", list);
			map.put("pager", page);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/*
	 * 설정관리 > 자동이체동의관리 > 파일생성
	 */
	@RequestMapping("autoTranRegDownload")
	public View autoTranRegDownload(HttpServletRequest request, HttpServletResponse response, AutoDTO dto, Model model) throws Exception {

		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("startDt", dto.getStartDt());
			map.put("endDt", dto.getEndDt());
			map.put("chaCd", dto.getChaCd());
			map.put("chaName", dto.getChaName());
			map.put("searchOption", dto.getSearchOption());
			map.put("keyword", dto.getKeyword());
			map.put("consentTy", dto.getConsentTy());
			map.put("stList", dto.getStList());
			map.put("chaList", dto.getChaList());
			map.put("cmsList", dto.getCmsList());
			map.put("chatrty", dto.getChatrty());
			map.put("orderBy", dto.getOrderBy());

			int count = autoTranMgmtService.selAutoTranRegCount(map);
			map.put("start", 1);
			map.put("end", count);

			List<AutoDTO> list = autoTranMgmtService.selAutoTranRegList(map);
			model.addAttribute("list", list);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return new ATAgreementExcelDownload();
	}

	/*
	 * 설정관리 > 자동이체신청관리 > 파일생성
	 */
	@RequestMapping("autoTranDownload")
	public View autoTranExcelDownload(HttpServletRequest request, HttpServletResponse response, AutoDTO dto, Model model) throws Exception {
		
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("startDt", dto.getStartDt());
			map.put("endDt", dto.getEndDt());
			map.put("chaCd", dto.getChaCd());
			map.put("chaName", dto.getChaName());
			map.put("searchOption", dto.getSearchOption());
			map.put("keyword", dto.getKeyword());
			map.put("cmsList", dto.getCmsList());
			map.put("stList", dto.getStList());
			map.put("orderBy", dto.getOrderBy());
			
			int count = autoTranMgmtService.selAutoTranCount(map);
			map.put("start", 1);
			map.put("end", count);
			
			List<AutoDTO> list = autoTranMgmtService.selAutoTranList(map);
			model.addAttribute("list", list);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return new AutoTranExcelDownload();
	}
	
	/*
	 * 설정관리 > 자동이체대상생성  
	 */
	@RequestMapping("autoTranTarget")
    public ModelAndView appAuthCreate() throws Exception {

        ModelAndView mav = new ModelAndView();

        mav.setViewName("sys/setting/autoTranMgmt/autoTranTarget");
        return mav;
    }

    @RequestMapping("autoTranTargetCha")
    public ModelAndView autoTranTargetCha() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("sys/setting/autoTranMgmt/autoTranTargetCha");
        return mav;
    }

    @RequestMapping("autoTranTargetFee")
    public ModelAndView autoTranTargetFee() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("sys/setting/autoTranMgmt/autoTranTargetFee");
        return mav;
    }
	
	/*
	 * 설정관리 > 자동이체대상생성  > 화면내조회
	 */
	@ResponseBody
	@RequestMapping("selAutoTranTarget")
	public HashMap<String, Object> selAppAuthCreate(@RequestBody AutoDTO dto) throws Exception {
		final HashMap<String, Object> map = new HashMap<>();
		map.put("stList", dto.getStList());
		map.put("consentTy", dto.getConsentTy());
        map.put("startDt", dto.getStartDt());
        map.put("endDt", dto.getEndDt());
        map.put("chaCd", dto.getChaCd());
        map.put("chaName", dto.getChaName());
        map.put("searchOpt", dto.getSearchOption());
        map.put("keyword", dto.getKeyword());
		map.put("orderBy", dto.getOrderBy());

		int count = autoTranMgmtService.selAutoTranTargetCnt(map);
		map.put("count", count);
		
		PageVO page = new PageVO(count, dto.getCurPage(), dto.getPageScale());
		int start = page.getPageBegin();
		int end = page.getPageEnd();	
		map.put("start", start);
		map.put("end", end);
		
		List<AutoDTO> list = autoTranMgmtService.selAutoTranTarget(map);
		map.put("list", list);
		map.put("pager", page);
		map.put("modalNo", 1);
		map.put("autoPath", autoPath);
		
		return  map;
	}
	
	/*
	 * 설정관리 > 자동이체대상생성  > 금결월 신청완료
	 */
	@ResponseBody
	@RequestMapping("finish")
	public void applyFinish() throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		try {
			map.put("cmsReqSt", "CST03");	// 신청상태 - 신청완료
			map.put("cmsSt", "CST02");		// 신청상태 - 신청중
			
			autoTranMgmtService.applyFinish(map);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 설정관리 > 자동이체대상생성  > 수수료 출금 > 화면내조회
	 */
	@ResponseBody
	@RequestMapping("selAutoTranAccNo")
	public HashMap<String, Object> selAutoTranAccNo(@RequestBody AutoDTO dto) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("rcpSt", dto.getRcpSt());
        map.put("startDt", dto.getStartDt());
        map.put("endDt", dto.getEndDt());
        map.put("chaCd", dto.getChaCd());
        map.put("chaName", dto.getChaName());
        map.put("searchOpt", dto.getSearchOption());
        map.put("keyword", dto.getKeyword());
		map.put("stList", dto.getStList());
		map.put("orderBy", dto.getOrderBy());
		int count = autoTranMgmtService.selAccSumCnt(map);
		int failCnt = autoTranMgmtService.selAutoOrgFailCnt(map);
		map.put("count", count);
		map.put("failCnt", failCnt);

        PageVO page = new PageVO(count, dto.getCurPage(), dto.getPageScale());
        int start = page.getPageBegin();
        int end = page.getPageEnd();
        map.put("start", start);
        map.put("end", end);
		
		List<AutoDTO> list = autoTranMgmtService.selAccSumList(map);

		map.put("list", list);
		map.put("pager", page);
		map.put("modalNo", 2);
		map.put("autoPath", autoPath);


		return  map;
	}
	
	/*
	 * 설정관리 > 자동이체대상생성 > 수수료 출금  > 신청파일생성  fileCreate
	 */
	@ResponseBody
	@RequestMapping("createAcc")
	public HashMap<String, Object> createAcc() throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		try {
			String previousMonth = DateUtils.previousMonth();    //  이전달 구하는 로직 필요.
	        String thisMonth = DateUtils.thisMonth();
	        
	        //TODO 쿼리 변경 필요. 처리하지 않았거나 에러나 난 것들.
	        List<Map<String, Object>> maps = autoTranMgmtService.findMonthlyAutomaticWithdrawal(previousMonth);
	        
	        if (maps == null || maps.size() == 0) {
	            logger.info("처리할 자동이체 데이터가 없음. " + previousMonth);
	            
	            map.put("resultCode", "9999");
	            map.put("msg", "자동이체 등록할 신규기관이 없습니다.");
	        }
	        
	        String fileName = fileNameEB21(thisMonth); // 이번달 자동이체 파일명 구하는 메소드 구현.
	        
	        SimpleDateFormat sdf = new SimpleDateFormat("yy");
	        Calendar calendar = Calendar.getInstance();
	        String autoDay = sdf.format(calendar.getTime()) + fileName.substring(fileName.length()-4, fileName.length());
	        
	        EB21 eb21 = AutoWithdrawalHelper.convertEB21(maps, fileName, corpCode, bankCode, accountNo, shinhanCode, autoDay);
	        
	        String encoded = AutoWithdrawalHelper.encodeEB21(eb21);
	        
	        fileService.write(encoded, fileDir + "/" + fileName);
	        autoTranMgmtService.updateEB21Result(eb21, previousMonth);
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return map;
	}
	
	private String fileNameEB21(String thisMonth) {
        return "EB21" + thisMonth + dueDay;
    }
	
	/*
	 * 설정관리 > 자동이체대상생성  > 금결월 신청완료 - 수수료 출금
	 */
	@ResponseBody
	@RequestMapping("accFinish")
	public void accFinish() throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		try {
			map.put("cmsReqSt", "CST03");	// 신청상태 - 신청완료
			map.put("cmsSt", "CST02");		// 신청상태 - 신청중
			
			autoTranMgmtService.applyFinish(map);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 설정관리 > 자동이체관리 > 자동이체 처리현황
	 */
	@RequestMapping("autoTranStatus")
	public ModelAndView selAutoTranStatus() throws Exception {
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("sys/setting/autoTranMgmt/autoTranStatus");
		return mav;
	}
	
	/*
	 * 설정관리 > 자동이체관리 > 자동이체 처리현황 - 화면내조회(신규기관)
	 */
	@ResponseBody
	@RequestMapping("procOrg")
	private HashMap<String, Object> selAutoProcOrg(@RequestBody AutoDTO dto) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("startDt", dto.getStartDt());
		map.put("endDt", dto.getEndDt());
		map.put("stList", dto.getStList());
        map.put("consentTy", dto.getConsentTy());
        map.put("chaCd", dto.getChaCd());
        map.put("chaName", dto.getChaName());
        map.put("searchOpt", dto.getSearchOption());
        map.put("keyword", dto.getKeyword());
		
		int count = autoTranMgmtService.selAutoProcOrgCnt(map);
		int failCnt = autoTranMgmtService.selAutoOrgFailCnt(map);
		map.put("count", count);
		map.put("failCnt", failCnt);
		
		PageVO page = new PageVO(count, dto.getCurPage(), dto.getPageScale());
		int start = page.getPageBegin();
		int end = page.getPageEnd();	
		map.put("start", start);
		map.put("end", end);
		
		List<AutoDTO> list = autoTranMgmtService.selAutoProcOrgList(map);
		map.put("list", list);
		map.put("epager", page);
		
		return map;
	}
	
	/*
	 * 설정관리 > 자동이체관리 > 자동이체 처리현황 - 화면내조회(수수료출금)
	 */
	@ResponseBody
	@RequestMapping("procAcc")
	private HashMap<String, Object> selAutoProcAcc(@RequestBody AutoDTO dto) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("startDt", dto.getStartDt());
		map.put("endDt", dto.getEndDt());
		map.put("stList", dto.getStList());
        map.put("chaCd", dto.getChaCd());
        map.put("chaName", dto.getChaName());
        map.put("searchOpt", dto.getSearchOption());
        map.put("keyword", dto.getKeyword());

		int count = autoTranMgmtService.selAutoProcAccCnt(map);
		int failCnt  = autoTranMgmtService.selAutoAccFailCnt(map);
		map.put("count", count);
		map.put("failCnt", failCnt);
		
		PageVO page = new PageVO(count, dto.getCurPage(), dto.getPageScale());
		int start = page.getPageBegin();
		int end = page.getPageEnd();	
		map.put("start", start);
		map.put("end", end);
		
		List<AutoDTO> list = autoTranMgmtService.selAutoProcAccList(map);
		map.put("list", list);
		map.put("epager", page);
		
		return map;
	}
	
	/*
	 * 설정관리 > 자동이체관리 > 자동이체 처리현황 - 파일업로드(EB22)
	 */
	@ResponseBody
	@RequestMapping("autoFileUploadEB22")
	public void fileUploadEB22(MultipartHttpServletRequest request) throws Exception {
		
		MultipartFile inputFile = request.getFile("file"); 
		
		File dirPath = new File(tempPath); // 디렉토리 존재 확인 후 생성
		if (!dirPath.exists()) {
			dirPath.mkdirs(); 
		}
		
		File file = new File(tempPath +  inputFile.getOriginalFilename()); 
		
		try {
			inputFile.transferTo(file);
		} catch(IllegalStateException  e) {
			throw new RuntimeException(e.getMessage(),e);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		try {
			String previousMonth = DateUtils.previousMonth();

	        String msg = FileUtils.readFileToString(file, Charset.forName("EUC-KR"));
	        EB22 eb22 = (EB22)withdrawalLayoutFactory.decode(msg, "EB22");

	        autoTranMgmtService.updateEB22Result(eb22, previousMonth);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			file.delete();
		}
	}
	@Autowired
	private WithdrawAgreementService withdrawAgreementService;
	@Autowired
	private FwFileMapper fwFileMapper;
	@Autowired
	private SimpleFileMapper simpleFileMapper;
	@Autowired
	private WithdrawAgreementMapper withdrawAgreementMapper;
	@Autowired
	private ChaMapper chaMapper;
	
	/*
	 * 설정관리 > 자동이체관리 > 자동이체 신청관리 - 출금동의서 업로드
	 */
	@ResponseBody
	@RequestMapping("cmsChangeFile")
	public void cmsChangeFileUpload(MultipartHttpServletRequest request) throws Exception {
			MultipartFile inputFile = request.getFile("file");
			String chaCd 	= request.getParameter("chaCd");
			String feeAccNo = request.getParameter("feeAccNo");
            String fileName = inputFile.getOriginalFilename();
            //확장자 추출
            int pos = fileName.lastIndexOf( "." );
            String ext = fileName.substring( pos );

			File dirPath = new File(cmsPath); // 디렉토리 존재 확인 후 생성
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}
			File file = new File(cmsPath + chaCd + ext);
			File file1 = rename(file); // 파일명 중복시 파일명 변경

		//출금동의서 파일 생성
		byte [] byteArr=inputFile.getBytes();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(byteArr);

		FwFile fwFile = new FwFile();
		fwFile.setBucket("WITHDRAW_AGREEMENT");
		fwFile.setName(chaCd + ext);
		if(ext.toLowerCase().equals(".jpg")){
			fwFile.setMimeType("image/jpeg");
		}else if(ext.toLowerCase().equals(".tif")){
			fwFile.setMimeType("image/tiff");
		}else{
			fwFile.setMimeType("audio/mp3");
		}
		fwFile.setLength((long) baos.toByteArray().length);
		fwFile.setCreateDate(new Date());
		fwFile.setCreateUser(chaCd);
		fwFileMapper.insert(fwFile);

		simpleFileMapper.store(fwFile.getBucket(), fwFile.getId(), new ByteArrayInputStream(baos.toByteArray()));

		// 3.2. 출금 동의서 등록
		WithdrawAgreement withdrawAgreement = new WithdrawAgreement();
		if(ext.toLowerCase().equals(".jpg")){
			withdrawAgreement.setType("W00001");
		}else if(ext.toLowerCase().equals(".tif")){
			withdrawAgreement.setType("W00001");
		}else{
			withdrawAgreement.setType("W00002");
		}
		withdrawAgreement.setStatus("A00002");
		withdrawAgreement.setFileId(fwFile.getId());
		withdrawAgreement.setCreateDate(new Date());
		withdrawAgreement.setCreateUser(chaCd);
		withdrawAgreement.setAgreeUserType("W10001");
		withdrawAgreement.setAgreeUser(chaCd);
		withdrawAgreementMapper.insert(withdrawAgreement);
		final Cha cha = chaMapper.selectByPrimaryKey(chaCd);
		// 4. 기관 변경
		// 4.1. 상태 변경 (미동의 => 승인대기)
		cha.setFingerFeeAgreeStatus("A00002");
		// 4.2. 출금동의서 설정
		cha.setWithdrawAgreementId(withdrawAgreement.getId());
		chaMapper.updateByPrimaryKey(cha);
		
		try {
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("chaCd", chaCd);
			map.put("cmsFileName", file1.getName());
			map.put("cmsReqSt", "CST01"); 	// 미신청
			map.put("cmsAppGubn", "CM01");	// 신규
			map.put("chkCms", "Y");			// CMS출금동의서수령여부
			map.put("cmsReqFinDt", ""); 	// cms 승인완료일자
            if(feeAccNo != null){
                map.put("feeAccNo", feeAccNo);	// 수수료출금 계좌번호
            }
            map.put("cmsReqDt", "");
			map.put("finFeeAgreeSt", "A00002"); // 동의상태 승인대기로
			map.put("withdrawAgreementId", withdrawAgreement.getId());

			autoTranMgmtService.updateCmsReq(map);
			
		} catch(Exception e) {
			e.printStackTrace();
		} 
	}
	
	/*
	 * 설정관리 > 자동이체관리 > 자동이체 신청관리 - 해지
	 */
	@ResponseBody
	@RequestMapping("autoTranCancel")
	public void autoTranCancel(@RequestBody AutoDTO dto) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("chaCd", dto.getChaCd());
		map.put("cmsFileName", "");
		map.put("cmsReqSt", "CST01"); 	// 해지 : 미신청
		map.put("cmsAppGubn", "CM03");	// 해지
		map.put("chkCms", "N");			// CMS출금동의서수령여부
		map.put("cmsReqFinDt", ""); 	// cms 승인완료일자
		
		autoTranMgmtService.updateCmsReq(map);
	}

	/*
	 * 설정관리 > 자동이체관리 > 자동이체 신청관리 - 동의취소
	 */
	@ResponseBody
	@RequestMapping("updAgreement")
	public void updAgreement(@RequestBody AutoDTO dto) throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("chaCd", dto.getChaCd());
		if(dto.getChkcmsYN().equals("N")){
			map.put("cmsReqDt", "");
            map.put("finFeeAgreeSt", "A00001"); //미동의
        }else{
			map.put("cmsReqDt", "");
            map.put("finFeeAgreeSt", "A00000"); //동의
        }
		map.put("updAgreement", "notnull");

		autoTranMgmtService.updateCmsReq(map);
	}

    @ResponseBody
    @RequestMapping("UpdateAutoTranSt")
    public HashMap UpdateAutoTranSt(@RequestBody AutoDTO dto) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("stList", dto.getStList());
        map.put("cmsReqSt", "CST02");
		try{

			autoTranMgmtService.UpdateAutoTranSt(map);
			map.put("retCode", "0000");
		}catch (Exception e){
			map.put("retCode", "9999");
		}
		return map;
    }

	@ResponseBody
	@RequestMapping("UpdateFeeTranSt")
	public HashMap UpdateFeeTranSt(@RequestBody AutoDTO dto) throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>();
		String previousMonth = DateUtils.previousMonth();    //  이전달
		String thisMonth = DateUtils.thisMonth();
		map.put("stList", dto.getStList());
		map.put("rcpSt", "OST02");
		map.put("month", previousMonth);

		try{
			autoTranMgmtService.UpdateFeeTranSt(map);
			map.put("retCode", "0000");
		}catch (Exception e){
			map.put("retCode", "9999");
		}
		return map;
	}
	
	/*
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

	@Transactional
	@SuppressWarnings("resource")
	@ResponseBody
	@RequestMapping("insertAutoTranFeeInfo")
	public HashMap<String, Object> insertAutoTranFeeInfo(MultipartHttpServletRequest request, boolean flag) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String chaCd = authentication.getName();

		MultipartFile excelFile = request.getFile("file");
		String fileName = excelFile.getOriginalFilename();
		int pos = fileName.toLowerCase().lastIndexOf( "." );
		String ext = fileName.substring( pos + 1 );
		Date sCurTime = new Date();

		InputStream inputDocument = null;
		Workbook workbook = null;
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		try {
			inputDocument = excelFile.getInputStream();
			if(!ext.equals("xls") && !ext.equals("xlsx")){
				retMap.put("resCode", "0001");
				return retMap;
			}else{
				if (fileName.toLowerCase().endsWith("xlsx")) {
					workbook = new XSSFWorkbook(inputDocument);
				} else {
					workbook = new HSSFWorkbook(inputDocument);
				}
			}

		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());

			throw e;
		} catch (IOException e) {
			logger.error(e.getMessage());

			throw e;
		}

		try {
			Sheet workSheet = workbook.getSheetAt(0); // 첫번째 Sheet

			int rowSize = workSheet.getPhysicalNumberOfRows(); // 행의 총 개수 (행은 0부터 시작함)
			int failCnt = 0;

			logger.debug(rowSize+"**********************************************");

			List<Map<String, Object>> list = new ArrayList<>();

			// 작업한 엑셀의 마지막 row를 확인하기 위함 - commit을 한번만  수행하기 위해...
			Row rCnt = workSheet.getRow(1);
			int cellLen = (int) rCnt.getLastCellNum();
			int rowNum = 0;
			for (int i = 1; i < rowSize; i++) {
				Row row = workSheet.getRow(i);
				for (int j = 0; j < cellLen; j++) {
					Cell cell = row.getCell(j);
					if (i == j && (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK)) {
						rowNum++;
					}
				}
			}
			int fcnt = 0;
			for (int i = 1; i < rowSize; i++) { // i를 2부터 시작해야 세번째 행부터 데이터가 입력된다.
				HashMap<String, Object> map = new HashMap<String, Object>();
				boolean insFlag = true; // 파일등록 flag

				if (workSheet.getRow(i) == null) {
					continue;
				}

				Row rowCnt = workSheet.getRow(0); // 컬럼 수 확인을 위하여 header 수를 읽어온다.
				Row row = workSheet.getRow(i);

				int cellLength = (int) rowCnt.getLastCellNum(); // 열의 총 개수
				int idx = 0;

				// 셀서식을 텍스트로 초기화 한 엑셀 중 input값이 없는 row의 insert를 막기위함
				int cno = 0;
				for (int j = 0; j < cellLength; j++) {
					Cell cell = row.getCell(j);
					if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						cno++;
					}
				}

				final String[] columnValues = new String[cellLength];
				for (int j = 0; j < cellLength; j++) {
					String valueStr = ""; // 엑셀에서 뽑아낸 데이터를 담아놓을 String 변수 선언 및 초기화

					final Cell cell = row.getCell(j);
					try {
						if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) { // CELL_TYPE_BLANK로만 체크할 경우 비어있는  셀을 놓칠 수 있다.
							continue;
						}

						switch (cell.getCellType()) {
							case Cell.CELL_TYPE_STRING:
								valueStr = cell.getStringCellValue();
								break;
							case Cell.CELL_TYPE_NUMERIC: // 날짜 형식이든 숫자 형식이든 다 CELL_TYPE_NUMERIC으로 인식.
								if (DateUtil.isCellDateFormatted(cell)) { // 날짜 유형의 데이터일 경우,
									SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
									String formattedStr = dateFormat.format(cell.getDateCellValue());
									valueStr = formattedStr;
									break;
								} else { // 순수하게 숫자 데이터일 경우,
									valueStr = NumberToTextConverter.toText(cell.getNumericCellValue()); // String로  변형
									break;
								}
							case Cell.CELL_TYPE_BOOLEAN:
								valueStr = Boolean.toString(cell.getBooleanCellValue());
								break;
							default:
								valueStr = cell.getStringCellValue();
								break;
						}
					} finally {
						columnValues[j] = StringUtils.trim(valueStr);

					}
				}

				if(columnValues[0] != null && columnValues[0] != "" && !columnValues[0].equals("")) {
                    //출금일자
                    map.put("chaCd", columnValues[0]);
                    BankReg01DTO phlist = bankMgmtService.selectChaListInfo(map);
                    if(phlist.getFinFeeOwnNo() != null) {
                    	if(phlist.getChrHp() == null) {
							phlist.setChrHp("");
						}
						KftcWithdraw kftcWithdraw = new KftcWithdraw();
						kftcWithdraw.setPayerKftcCode(StringUtils.defaultString(phlist.getBnkCd()));
						kftcWithdraw.setPayerAccountNo(phlist.getFinFeeAccNo());
						kftcWithdraw.setAmount(Long.valueOf(columnValues[1]));
						kftcWithdraw.setPayerIdentityNo(phlist.getFinFeeAccIdent());
						kftcWithdraw.setRemark("핑거다모아관리비");
						kftcWithdraw.setFundTypeCd("K20000");
						kftcWithdraw.setPayerNo(phlist.getFinFeeOwnNo());
						kftcWithdraw.setWithdrawTypeCd("K50002");
						kftcWithdraw.setContactNo(phlist.getChrHp().replace("-", ""));
						kftcWithdraw.setCreateDate(sCurTime);
						kftcWithdraw.setCreateUser(chaCd);
						kftcWithdraw.setStatusCd("K00001");
						kftcWithdrawMapper.insert(kftcWithdraw);
					}else{
						fcnt++;
					}
                }else{

                }
			} // for loop(i) end (Rows)

			retMap.put("resCode", "0000");
			retMap.put("rMsg", "금결원에 등록되어있지 않은" + fcnt + "건 은 등록되지 않았습니다.");
			return retMap;
		} catch (Exception e) {
			e.printStackTrace();
			retMap.put("resCode", "9999");
			return retMap;
		} finally {
			inputDocument.close();
			logger.info(">>> 파일삭제완료 <<<");
		}
	}

	@ResponseBody
	@RequestMapping("autoTranFeeExcelDown")
	public View autoTranFeeExcelDown(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String chaCd = authentication.getName();

		return new AutoTranFeeExcelDownload();
	}

	/**
	 * 자동이체대상생성 / 일괄등록
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("autoTranReqAtOnce")
	public HashMap<String, Object> autoTranReqAtOnce(@RequestBody AutoDTO dto) throws Exception {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final String userId = authentication.getName();
		
        final Date now = new Date();
		final HashMap<String, Object> map = new HashMap<>();
        try{
//			map.put("stList", dto.getStList());
			// 미등록 기관만 등록
			map.put("stList", Arrays.asList("CST01"));

			map.put("consentTy", dto.getConsentTy());
			map.put("startDt", dto.getStartDt());
			map.put("endDt", dto.getEndDt());
			map.put("chaCd", dto.getChaCd());
			map.put("chaName", dto.getChaName());
			map.put("searchOpt", dto.getSearchOption());
			map.put("keyword", dto.getKeyword());

			map.put("start", 1);
			map.put("end", Integer.MAX_VALUE);

            final List<AutoDTO> list = autoTranMgmtService.selAutoTranTarget(map);
            map.put("list", list);

            for (AutoDTO autoDTO : list) {
                KftcPayer kftcPayer= new KftcPayer();
                kftcPayer.setApplyDate(autoDTO.getRegDt());
                kftcPayer.setApplyTypeCd("K10001");
                kftcPayer.setPayerKftcCode(autoDTO.getBankCd());
                kftcPayer.setPayerAccountNo(autoDTO.getFeeAccNo());
                kftcPayer.setPayerIdentityNo(autoDTO.getFinFeeAccIdent());
                kftcPayer.setFundTypeCd("K20000");
                kftcPayer.setContactNo(StringUtils.defaultString(autoDTO.getChrHp()).replace("-", ""));
                kftcPayer.setCreateDate(now);
                kftcPayer.setCreateUser(userId);
                kftcPayer.setStatusCd("K00001");
                kftcPayer.setUserDefine1("DAMOA");
                kftcPayer.setUserDefine2("CLIENT");
                kftcPayer.setUserDefine3(autoDTO.getChaCd());
                kftcPayerMapper.insert(kftcPayer);
                
                final ClientDO clientDO = clientRepository.get(autoDTO.getChaCd());
                clientDO.readyToRegisterAsKftcPayer(kftcPayer.getId());
                clientRepository.update(clientDO);
            }

            map.put("retCode", "0000");
        }catch (Exception e){
        	e.printStackTrace();
            map.put("retCode", "9999");
        }
        return map;
	}


    @ResponseBody
    @RequestMapping("cancelCMS")
    public HashMap cancelCMS(@RequestBody AutoDTO dto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        Date sCurTime = new Date();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try{
            List<String> stList = new ArrayList<String>();

            kftcPayerMapper.deleteByPrimaryKey(dto.getFinFeeOwnNo());

            map.put("stList", Arrays.asList(dto.getChaCd()));
             map.put("cmsReqSt", "CST01");
             map.put("finFeeOwnNo", "cancel");
            autoTranMgmtService.UpdateAutoTranSt(map);

            map.put("retCode", "0000");
        }catch (Exception e){
            map.put("retCode", "9999");
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("autoTranFeeCancel")
    public HashMap autoTranFeeCancel(@RequestBody AutoDTO dto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try{
            List<String> stList = dto.getStList();
            for (String s : stList) {
				kftcWithdrawMapper.deleteByPrimaryKey(s.toString());
			}
			map.put("retCode", "0000");
        }catch (Exception e){
            map.put("retCode", "9999");
        }
        return map;
    }

    /*
     * 설정관리 > 자동이체대상관리 > 파일생성
     */
    @RequestMapping("autoTranTargetChaDownload")
    public View autoTranTargetChaDownload(HttpServletRequest request, HttpServletResponse response, AutoDTO dto, Model model) throws Exception {

        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("startDt", dto.getStartDt());
            map.put("endDt", dto.getEndDt());
            map.put("chaCd", dto.getChaCd());
            map.put("chaName", dto.getChaName());
            map.put("searchOption", dto.getSearchOption());
            map.put("keyword", dto.getKeyword());
            map.put("consentTy", dto.getConsentTy());
            map.put("stList", dto.getStList());
            map.put("orderBy", dto.getOrderBy());

            int count = autoTranMgmtService.selAutoTranTargetCnt(map);
            map.put("count", count);

            map.put("start", 1);
            map.put("end" , count);

            List<AutoDTO> list = autoTranMgmtService.selAutoTranTarget(map);
            model.addAttribute("list", list);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return new ATFeeChaExcelDownload();
    }
}


