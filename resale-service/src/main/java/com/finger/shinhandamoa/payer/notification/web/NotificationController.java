package com.finger.shinhandamoa.payer.notification.web;

import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.payer.notification.dto.NotificationDTO;
import com.finger.shinhandamoa.payer.notification.service.NotificationService;
import com.finger.shinhandamoa.vo.PageVO;
import com.finger.shinhandamoa.vo.UserDetailsVO;
import kr.co.nicepay.module.lite.NicePayWebConnector;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 고지관리 컨트롤러
 * 
 */
@Controller
@RequestMapping("payer/notification/**")
public class NotificationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    // 팝업 디렉토리
    @Value("${server.path}")
    private String serverPath;

    /*
     * 	고지내역 리스트 조회
     * */
    @RequestMapping("notificationList")
    public ModelAndView NotificationList(@RequestParam(defaultValue = "1") int curPage,
                                         @RequestParam(defaultValue = "10") int PAGE_SCALE,
                                         @RequestParam(defaultValue = "1") int start,
                                         @RequestParam(defaultValue = "10") int end,
                                         @RequestParam(defaultValue = "masMonth") String search_orderBy,
                                         @RequestParam(defaultValue = "") String vaNo,
                                         @RequestParam(defaultValue = "") String chaCd,
                                         @RequestParam(defaultValue = "") String cusName) throws Exception {

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        String tmasMonth = "";
        String fmasMonth = "";

        try {

            tmasMonth = StrUtil.getCurrentMonthStr();
            fmasMonth = StrUtil.getCurrentMonthStr();

            // 가맹점정보 가져오기
            NotificationDTO chaInfo = notificationService.selectChaInfo(vaNo);
            //마지막 청구월 가져오기
            NotificationDTO maxMonth = notificationService.maxMonth(vaNo);
            if (maxMonth != null && !maxMonth.getMasMonth().equals("")) {
                reqMap.put("tmasMonth", maxMonth.getMasMonth());
                reqMap.put("fmasMonth", maxMonth.getMasMonth());
            } else {
                reqMap.put("tmasMonth", tmasMonth);
                reqMap.put("fmasMonth", fmasMonth);
            }

            if(!StringUtils.isNumeric(chaCd) || chaCd.length() != 8){
                throw new Exception("비정상적인 접근입니다.");
            }
            if(!StringUtils.isNumeric(vaNo)){
                throw new Exception("비정상적인 접근입니다.");
            }

            reqMap.put("vaNo", vaNo);
            reqMap.put("chaCd", chaCd);

            // 고지내역 총 개수 조회
            HashMap<String, Object> totValue = notificationService.notificationTotalCount(reqMap);
            int count = Integer.parseInt(totValue.get("CNT").toString());
            int subTot = Integer.parseInt(totValue.get("SUBTOT").toString());

            PageVO page = new PageVO(count, curPage, PAGE_SCALE);
            reqMap.put("start", start);
            reqMap.put("end", end);
            reqMap.put("search_orderBy", search_orderBy);

            List<NotificationDTO> list = notificationService.notificationList(reqMap);

            map.put("cusName", StringEscapeUtils.escapeHtml4(cusName));
            map.put("vaNo", StringEscapeUtils.escapeHtml4(vaNo));
            map.put("count", count);
            map.put("subTot", subTot);
            map.put("chaName", chaInfo.getChaName());
            map.put("chaCd", StringEscapeUtils.escapeHtml4(chaCd));
            map.put("usePgYn", chaInfo.getUsePgYn());
            map.put("rcpDtDupYn", chaInfo.getRcpDtDupYn());
            map.put("notificationList", list);
            map.put("tmasMonth", maxMonth.getMasMonth());
            map.put("fmasMonth", maxMonth.getMasMonth());
            map.put("partialPayment", chaInfo.getPartialPayment());
            map.put("amtchkty", chaInfo.getAmtchkty());
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("PAGE_SCALE", PAGE_SCALE);
            map.put("search_orderBy", search_orderBy);

        } catch (Exception e) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());
            }
        }

        mav.addObject("map", map);
        mav.setViewName("payer/notification/notificationList");

        return mav;
    }

    /*
     * 	고지내역 리스트 조회(Ajax)
     * */
    @RequestMapping("notificationListAjax")
    @ResponseBody
    public HashMap<String, Object> NotificationListAjax(@RequestBody NotificationDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            // 가맹점정보 가져오기
            NotificationDTO chaInfo = notificationService.selectChaInfo(body.getVaNo());

            reqMap.put("vaNo", body.getVaNo());
            reqMap.put("chaCd", body.getChaCd());
            reqMap.put("tmasMonth", StringEscapeUtils.escapeHtml4(body.getTmasMonth()));
            reqMap.put("fmasMonth", StringEscapeUtils.escapeHtml4(body.getFmasMonth()));
            reqMap.put("curPage", body.getCurPage());
            reqMap.put("PAGE_SCALE", body.getPageScale());
            reqMap.put("search_orderBy", StringEscapeUtils.escapeHtml4(body.getSearchOrderBy()));

            // 고지내역 총 개수 조회
            HashMap<String, Object> totValue = notificationService.notificationTotalCount(reqMap);
            int count = Integer.parseInt(totValue.get("CNT").toString());

            PageVO page = new PageVO(count, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();

            reqMap.put("start", start);
            reqMap.put("end", end);

            // 고지내역 리스트 조회
            List<NotificationDTO> list = notificationService.notificationList(reqMap);

            map.put("count", count);
            map.put("chaName", body.getChaName());
            map.put("chaCd", body.getChaCd());
            map.put("usePgYn", chaInfo.getUsePgYn());
            map.put("notificationList", list);
            map.put("tmasMonth", body.getTmasMonth());
            map.put("fmasMonth", body.getFmasMonth());
            map.put("pager", page);    // 페이징 처리를 위한 변수
            map.put("PAGE_SCALE", body.getPageScale());
            map.put("search_orderBy", body.getSearchOrderBy());
            map.put("retCode", "0000");
            map.put("retMsg", "정상");

        } catch (Exception e) {
            LOGGER.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /*
     *	카드결제 위한 고지내역 조회
     * */
    @Deprecated
    @RequestMapping("selectNotiMas")
    public ModelAndView selectNotiMas(@RequestParam(defaultValue = "") String notimasCd,
                                      @RequestParam(defaultValue = "") String accessGubn,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            reqMap.put("notimasCd", notimasCd);

            // 고지내역 조회
            NotificationDTO result = notificationService.selectNotiMas(reqMap);

            map.put("result", result);
            map.put("retCode", "0000");
            map.put("notimasCd", notimasCd);
            map.put("serverPath", serverPath);
            request.setAttribute("amt", result.getAmt());
            request.setAttribute("merchantKey", result.getPgLicenKey());
            request.setAttribute("merchantID", result.getPgServiceId());

        } catch (Exception e) {
            LOGGER.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        mav.addObject("map", map);

        if (accessGubn.equals("pc")) {
            mav.setViewName("payer/notification/payRequest");
        } else {
            mav.setViewName("payer/notification/payRequest_mobile");
        }

        return mav;
    }
    
    /**
     * 선택적 납부를 위한 카드결제창
     * @param params
     * @return
     * @author jhjeong@finger.co.kr
     * @modified 2018. 10. 15.
     */
    @RequestMapping("selectNotiDetails")
    public ModelAndView selectNotiDetails(@RequestParam Map<String, Object> params) throws Exception  {
    	ModelAndView mav = new ModelAndView();
    	
    	final String accessGubn = params.get("accessGubn").toString();
    	
    	Map<String, Object> map = notificationService.selectNotiDetails(params);
    	map.put("accessGubn", accessGubn);
    	map.put("serverPath", serverPath);
    	
    	mav.addObject("map", map);
    	
    	mav.addObject("amt", String.valueOf(map.get("amt")));
        mav.addObject("merchantKey", map.get("merchantKey"));
        mav.addObject("merchantID", map.get("merchantID"));

        String notidetCds = map.get("notidetCd") + "";
        String notidetCd = notidetCds.substring(1, notidetCds.length() - 1);
        mav.addObject("notidetCd", notidetCd);
    	
    	if (accessGubn.equals("pc")) {
            mav.setViewName("payer/notification/payRequest");
        } else {
            mav.setViewName("payer/notification/payRequest_mobile");
        }
    	
    	return mav;
    }

    /*
     *	카드결제
     * */
    @RequestMapping("goPay")
    public ModelAndView goPay(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();

        // 결재 instance 생성
        NicePayWebConnector connector = new NicePayWebConnector();
        // 로그 디렉토리 설정
        String root = request.getSession().getServletContext().getRealPath("/");
        String logPath = root + "WEB-INF/classes/properties/";

        try {
            request.setCharacterEncoding("utf-8");

            connector.setNicePayHome(logPath);                                  // 로그 디렉토리 생성
            // 요청 파라미터 설정
            connector.setRequestData(request);                                  // 요청 페이지 파라메터 셋팅
            // 추가 파라미터 설정
            SimpleDateFormat format = new SimpleDateFormat ( "yyyyMMddHHmmssSSS");
            Calendar cal = Calendar.getInstance(Locale.KOREA);
            String time = format.format(cal.getTime());
            String chacd = (String) request.getParameter("chaCd");
            String notimascd = (String) request.getParameter("notiMasCd");
            String moid = "moid"+chacd+notimascd+time;

            connector.addRequestData("actionType", "PY0");                      // 서비스모드 설정(결제 서비스 : PY0 , 취소 서비스 : CL0)
            connector.addRequestData("MallIP", request.getRemoteAddr());        // 상점 고유 ip
            connector.addRequestData("Moid", moid);        // 상점 주문번호 Moid
            connector.requestAction();

            LOGGER.info("온라인카드결제 {}" , moid);

            /*
             *******************************************************
             * <결제 성공 여부 확인>
             *******************************************************
             */
            boolean paySuccess = false;
            if (connector.getResultData("PayMethod").equals("CARD")) {
                if (connector.getResultData("ResultCode").equals("3001"))
                    paySuccess = true;                    // 신용카드(정상 결과코드:3001)
            } else if (connector.getResultData("PayMethod").equals("BANK")) {
                if (connector.getResultData("ResultCode").equals("4000"))
                    paySuccess = true;                    // 계좌이체(정상 결과코드:4000)	
            } else if (connector.getResultData("PayMethod").equals("CELLPHONE")) {
                if (connector.getResultData("ResultCode").equals("A000"))
                    paySuccess = true;                    // 휴대폰(정상 결과코드:A000)
            } else if (connector.getResultData("PayMethod").equals("VBANK")) {
                if (connector.getResultData("ResultCode").equals("4100"))
                    paySuccess = true;                    // 가상계좌(정상 결과코드:4100)
            } else if (connector.getResultData("PayMethod").equals("SSG_BANK")) {
                if (connector.getResultData("ResultCode").equals("0000"))
                    paySuccess = true;                        // SSG은행계좌(정상 결과코드:0000)
            }

            map.put("resultCode", connector.getResultData("ResultCode"));       // 결과코드 (정상 결과코드:3001)
            map.put("resultMsg", connector.getResultData("ResultMsg"));            // 결과메시지

            if (paySuccess) {

                // 수납코드 채번
                NotificationDTO data = notificationService.getRcpMasCd(map);
                map.put("rcpMasCd", data.getRcpMasCd());

                /*
                 *******************************************************
                 * <결제 결과 필드>
                 * 아래 응답 데이터 외에도 전문 Header와 개별부 데이터 Get 가능
                 *******************************************************
                 */
                map.put("authDate", connector.getResultData("AuthDate"));            // 승인일시 (YYMMDDHH24mmss)
                map.put("authCode", connector.getResultData("AuthCode"));            // 승인번호
                map.put("buyerName", connector.getResultData("BuyerName"));            // 구매자명
                map.put("mallUserID", connector.getResultData("MallUserID"));       // 회원사고객ID
                map.put("payMethod", connector.getResultData("PayMethod"));            // 결제수단
                map.put("mid", connector.getResultData("MID"));                    // 상점ID
                map.put("tid", connector.getResultData("TID"));                    // 거래ID
                map.put("moid", connector.getResultData("Moid"));                    // 주문번호
                map.put("amt", connector.getResultData("Amt"));                    // 금액
                map.put("goodsName", connector.getResultData("GoodsName"));            // 금액
                map.put("cardCode", connector.getResultData("CardCode"));            // 카드사 코드
                map.put("cardName", connector.getResultData("CardName"));            // 결제카드사명
                map.put("cardQuota", connector.getResultData("CardQuota"));            // 카드 할부개월 (00:일시불,02:2개월)
                map.put("bankCode", connector.getResultData("BankCode"));            // 은행 코드
                map.put("bankName", connector.getResultData("BankName"));            // 은행명
                map.put("rcptType", connector.getResultData("RcptType"));            // 현금 영수증 타입 (0:발행되지않음,1:소득공제,2:지출증빙)
                map.put("rcptAuthCode", connector.getResultData("RcptAuthCode"));   // 현금영수증 승인 번호
                map.put("rcptTID", connector.getResultData("RcptTID"));            // 현금 영수증 TID   
                map.put("carrier", connector.getResultData("Carrier"));            // 이통사구분
                map.put("dstAddr", connector.getResultData("DstAddr"));            // 휴대폰번호
                map.put("vbankBankCode", connector.getResultData("VbankBankCode")); // 가상계좌은행코드
                map.put("vbankBankName", connector.getResultData("VbankBankName")); // 가상계좌은행명
                map.put("vbankNum", connector.getResultData("VbankNum"));            // 가상계좌번호
                map.put("vbankExpDate", connector.getResultData("VbankExpDate"));   // 가상계좌입금예정일

                String frontPayDay = StrUtil.getYear().substring(0, 2);
                String backPayDay = connector.getResultData("AuthDate").substring(0, 6);
                String payDay = frontPayDay + backPayDay;

                map.put("chaName", request.getParameter("chaName"));
                map.put("payDay", payDay);
                map.put("payTime", connector.getResultData("AuthDate").substring(6));
                map.put("notiMasCd", request.getParameter("notiMasCd"));

                if (!"".equals(request.getParameter("notiDetCd"))) {
                    String value = request.getParameter("notiDetCd").trim();
                    String[] valueList = value.split("\\s*,\\s*");
                    map.put("notiDetCd", valueList);
                }

                map.put("chaCd", request.getParameter("chaCd"));

                /**
                 * 청구서 미납건 총액
                 * PA03     완납
                 * PA04     일부납
                 */
                NotificationDTO totalNotiMasAmt = notificationService.totalNotiMasAmt(map);

                if ((totalNotiMasAmt.getTotalAmt()).equals(connector.getResultData("Amt"))) {
                    map.put("notimasSt", "PA03");
                } else {
                    map.put("notimasSt", "PA04");
                }
                map.put("notidetSt", "PA03");

                // xrcpmas insert
                int insRcpMas = notificationService.insertRcpMas(map);
                if (insRcpMas != 0) {
                    // xrcpdet insert
                    int insRcpDet = notificationService.insertRcpDet(map);
                    if (insRcpDet != 0) {
                        // 고지내역 수정
                        notificationService.updateNotiBill(map);
                    }
                }

            } else {
                map.put("retCode", "999");
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        mav.addObject("map", map);
        mav.setViewName("payer/notification/payResult");

        return mav;
    }

    /*
     * 	모바일청구서
     * */
    @RequestMapping("mobileReciptList")
    public ModelAndView mobilReciptList() throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsVO userDetailsVO = (UserDetailsVO) authentication.getPrincipal();
        String vano = userDetailsVO.getVano();
        String chaCd = userDetailsVO.getUsername();
        String cusName = userDetailsVO.getName();
        String tel = userDetailsVO.getTel();

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            // 가맹점정보 가져오기
            NotificationDTO chaInfo = notificationService.selectChaInfo(vano);

            reqMap.put("vaNo", vano);
            reqMap.put("chaCd", chaCd);

            //모바일 고지내역 개수 조회
            HashMap<String, Object> totValue = notificationService.mMasNotificationTotalCount(reqMap);
            int count = Integer.parseInt(totValue.get("CNT").toString());

            //모바일 고지내역 조회
            List<NotificationDTO> masList = notificationService.mMasNotificationList(reqMap);

            //모바일청구서 상세항목 개수 조회
            HashMap<String, Object> detTotValue = notificationService.mDetNotificationTotalCount(reqMap);
            int detCount = Integer.parseInt(detTotValue.get("CNT").toString());
            int detSubTot = Integer.parseInt(detTotValue.get("SUBTOT").toString());

            //모바일청구서 상세항목 조회
            List<NotificationDTO> detList = notificationService.mDetNotificationList(reqMap);

            map.put("tel", tel);
            map.put("chaInfo", chaInfo);
            map.put("cusName", cusName);
            map.put("vaNo", vano);
            map.put("count", count);
            map.put("detCount", detCount);
            map.put("detSubTot", detSubTot);
            map.put("chaCd", chaCd);
            map.put("masList", masList);
            map.put("detList", detList);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        mav.addObject("map", map);
        mav.setViewName("payer/notification/mobileReceipt");

        return mav;
    }

    /*
     *	카드결제 모바일
     * */
    @RequestMapping("mGoPay")
    public ModelAndView mGoPay(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOGGER.warn("mGoPay #{}", 1);

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();

        // 로그 디렉토리 설정
        String root = request.getSession().getServletContext().getRealPath("/");
        String logPath = root + "WEB-INF/classes/properties/";

        LOGGER.info("logPath==" + logPath);

       try {

            LOGGER.warn("mGoPay #{}", 2);
            /*
             *******************************************************
             * <인증 결과>
             *******************************************************
             */
           String authResultCode = request.getParameter("AuthResultCode");                                         // 인증결과 : 0000(성공)
           String authResultMsg = request.getParameter("AuthResultMsg");                                          // 인증결과 메시지

            LOGGER.warn("mGoPay #{}", 3);
            if ("0000".equals(authResultCode)) {
                LOGGER.warn("mGoPay #{}", 4);
                // 결재 instance 생성
                NicePayWebConnector connector = new NicePayWebConnector();
                connector.setNicePayHome(logPath);                                  // 로그 디렉토리 생성
                // 요청 파라미터 설정
                connector.setRequestData(request);                                  // 요청 페이지 파라메터 셋팅
                // 추가 파라미터 설정
                SimpleDateFormat format = new SimpleDateFormat ( "yyyyMMddHHmmssSSS");
                Calendar cal = Calendar.getInstance(Locale.KOREA);
                String time = format.format(cal.getTime());
                String chacd = (String) request.getParameter("chaCd");
                String notimascd = (String) request.getParameter("notiMasCd");
                String moid = "moid"+chacd+notimascd+time;

                connector.addRequestData("actionType", "PY0");                      // 서비스모드 설정(결제 서비스 : PY0 , 취소 서비스 : CL0)
                connector.addRequestData("MallIP", request.getRemoteAddr());        // 상점 고유 ip
                connector.addRequestData("Moid", moid);        // 상점 주문번호 Moid
                connector.requestAction();

                LOGGER.info("모바일카드결제 {}" , moid);
                LOGGER.warn("mGoPay #{}", 5);

                /*
                 *******************************************************
                 * <결제 결과 필드>
                 * 아래 응답 데이터 외에도 전문 Header와 개별부 데이터 Get 가능
                 *******************************************************
                 */
                boolean paySuccess = false;
                if (connector.getResultData("PayMethod").equals("CARD")) {
                    if (connector.getResultData("ResultCode").equals("3001"))
                        paySuccess = true;                    // 신용카드(정상 결과코드:3001)
                } else if (connector.getResultData("PayMethod").equals("BANK")) {
                    if (connector.getResultData("ResultCode").equals("4000"))
                        paySuccess = true;                    // 계좌이체(정상 결과코드:4000)	
                } else if (connector.getResultData("PayMethod").equals("CELLPHONE")) {
                    if (connector.getResultData("ResultCode").equals("A000"))
                        paySuccess = true;                    // 휴대폰(정상 결과코드:A000)
                } else if (connector.getResultData("PayMethod").equals("VBANK")) {
                    if (connector.getResultData("ResultCode").equals("4100"))
                        paySuccess = true;                    // 가상계좌(정상 결과코드:4100)
                } else if (connector.getResultData("PayMethod").equals("SSG_BANK")) {
                    if (connector.getResultData("ResultCode").equals("0000"))
                        paySuccess = true;                        // SSG은행계좌(정상 결과코드:0000)
                }

                LOGGER.warn("mGoPay #{}", 6);
                map.put("resultCode", connector.getResultData("ResultCode"));       // 결과코드 (정상 결과코드:3001)
                map.put("resultMsg", connector.getResultData("ResultMsg"));            // 결과메시지

                LOGGER.warn("mGoPay #{}", 7);

                if (paySuccess) {

                    LOGGER.warn("mGoPay #{}", 8);
                    // 수납코드 채번
                    NotificationDTO data = notificationService.getRcpMasCd(map);
                    map.put("rcpMasCd", data.getRcpMasCd());

                    LOGGER.warn("mGoPay #{}", 9);
                    /*
                     *******************************************************
                     * <결제 결과 필드>
                     * 아래 응답 데이터 외에도 전문 Header와 개별부 데이터 Get 가능
                     *******************************************************
                     */
                    map.put("authDate", connector.getResultData("AuthDate"));            // 승인일시 (YYMMDDHH24mmss)
                    map.put("authCode", connector.getResultData("AuthCode"));            // 승인번호
                    map.put("buyerName", connector.getResultData("BuyerName"));            // 구매자명
                    map.put("mallUserID", connector.getResultData("MallUserID"));       // 회원사고객ID
                    map.put("payMethod", connector.getResultData("PayMethod"));            // 결제수단
                    map.put("mid", connector.getResultData("MID"));                    // 상점ID
                    map.put("tid", connector.getResultData("TID"));                    // 거래ID
                    map.put("moid", connector.getResultData("Moid"));                    // 주문번호
                    map.put("amt", connector.getResultData("Amt"));                    // 금액
                    map.put("goodsName", connector.getResultData("GoodsName"));            // 금액
                    map.put("cardCode", connector.getResultData("CardCode"));            // 카드사 코드
                    map.put("cardName", connector.getResultData("CardName"));            // 결제카드사명
                    map.put("cardQuota", connector.getResultData("CardQuota"));            // 카드 할부개월 (00:일시불,02:2개월)
                    map.put("bankCode", connector.getResultData("BankCode"));            // 은행 코드
                    map.put("bankName", connector.getResultData("BankName"));            // 은행명
                    map.put("rcptType", connector.getResultData("RcptType"));            // 현금 영수증 타입 (0:발행되지않음,1:소득공제,2:지출증빙)
                    map.put("rcptAuthCode", connector.getResultData("RcptAuthCode"));   // 현금영수증 승인 번호
                    map.put("rcptTID", connector.getResultData("RcptTID"));            // 현금 영수증 TID   
                    map.put("carrier", connector.getResultData("Carrier"));            // 이통사구분
                    map.put("dstAddr", connector.getResultData("DstAddr"));            // 휴대폰번호
                    map.put("vbankBankCode", connector.getResultData("VbankBankCode")); // 가상계좌은행코드
                    map.put("vbankBankName", connector.getResultData("VbankBankName")); // 가상계좌은행명
                    map.put("vbankNum", connector.getResultData("VbankNum"));            // 가상계좌번호
                    map.put("vbankExpDate", connector.getResultData("VbankExpDate"));   // 가상계좌입금예정일
                    LOGGER.warn("mGoPay #{}", 10);

                    String frontPayDay = StrUtil.getYear().substring(0, 2);
                    String backPayDay = connector.getResultData("AuthDate").substring(0, 6);
                    String payDay = frontPayDay + backPayDay;
                    LOGGER.warn("mGoPay #{}", 11);

                    map.put("chaName", request.getParameter("chaName"));
                    map.put("payDay", payDay);
                    map.put("payTime", connector.getResultData("AuthDate").substring(6));
                    map.put("notiMasCd", request.getParameter("notiMasCd"));
                    map.put("chaCd", request.getParameter("chaCd"));
                    LOGGER.warn("mGoPay #{}", 12);

                    // xrcpmas insert
                    int insRcpMas = notificationService.insertRcpMas(map);
                    if (insRcpMas != 0) {
                        // xrcpdet insert
                        int insRcpDet = notificationService.insertRcpDet(map);
                        if (insRcpDet != 0) {
                            //고지내역 수정
                            notificationService.updateNotiBill(map);
                        }
                    }
                    LOGGER.warn("mGoPay #{}", 13);
                } else {
                    map.put("retCode", "999");
                }
            } else {
                map.put("resultCode", authResultCode);
                map.put("resultMsg", authResultMsg);
            }

            LOGGER.warn("mGoPay #{}", 14);
        } catch (Exception e) {
            LOGGER.warn("mGoPay #{}", 15);
            LOGGER.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        mav.addObject("map", map);
        mav.setViewName("payer/notification/payResult_mobile");
        LOGGER.warn("mGoPay #{}", 16);

        return mav;
    }

    /*
     * 	고지상세항목 조회(Ajax)
     * */
    @Deprecated
    @RequestMapping("notiDetList")
    @ResponseBody
    public HashMap<String, Object> notiDetList(@RequestBody NotificationDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {

            reqMap.put("notimasCd", body.getNotimasCd());

            // 고지상세항목 리스트 조회
            List<NotificationDTO> list = notificationService.notiDetList(reqMap);

            map.put("list", list);
            map.put("retCode", "0000");
            map.put("retMsg", "정상");
            map.put("masMonth", body.getMasMonth());
            map.put("subTot", body.getSubTot());
            map.put("notimasCd", body.getNotimasCd());


        } catch (Exception e) {
            LOGGER.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }
    
    /*
     * 	고지상세항목/수납 조회(Ajax)
     * */
    @RequestMapping("noticeDetailList")
    @ResponseBody
    public Map<String, Object> noticeDetailList(@RequestBody HashMap<String, Object> params) throws Exception {
    	
    	LOGGER.debug("noticeDetailList " + params);

        Map<String, Object> map;
        try {
        	map = notificationService.getNoticeDetailList(params);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            map = new HashMap<String, Object>();
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 계좌이체 시 부분납 납부자 선택여부 저장
     */
    @ResponseBody
    @RequestMapping("notificationPickRcpYn")
    public void notificationPickRcpYn(@RequestBody NotificationDTO dto) throws Exception {

        try {
            Map<String, Object> map = new HashMap<String, Object>();

            String notimasCd = dto.getNotimasCd();
            notificationService.resetPickRcpYn(notimasCd);

            if (!"".equals(dto.getNotidetCd())) {
                String value = dto.getNotidetCd().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("notidetCd", valueList);
            }

            notificationService.updatePickRcpYn(map);
        } catch(Exception e) {
            LOGGER.error(e.getMessage());

            throw e;
        }
    }
}
