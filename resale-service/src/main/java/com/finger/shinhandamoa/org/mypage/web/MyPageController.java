package com.finger.shinhandamoa.org.mypage.web;

import com.finger.shinhandamoa.common.FTPHelper;
import com.finger.shinhandamoa.common.ShaEncoder;
import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimItemDTO;
import com.finger.shinhandamoa.org.claimMgmt.service.ClaimItemService;
import com.finger.shinhandamoa.org.mypage.dto.MyPageDTO;
import com.finger.shinhandamoa.org.mypage.service.MyPageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * @author BY PYS
 * @date 2018. 04. 23.
 * @desc 마이페이지
 */
@Controller
@Slf4j
@RequestMapping("org/myPage/**")
public class MyPageController {

    private static final Logger logger = LoggerFactory.getLogger(MyPageController.class);

    @Inject
    private MyPageService myPageService;
    @Inject
    private ClaimItemService claimItemService;

    @Inject
    private ShaEncoder shaEncoder;

    // 로고 업로드 디렉토리
    @Value("${file.path.logo}")
    private String logoPath;

    // 로고 업로드 효성 ftp path
    @Value("${ftp.sendPath}")
    private String ftpLogoPath;

    @Value("${ftp.url}")
    private String ftpUrl;

    @Value("${ftp.user}")
    private String ftpUser;

    @Value("${ftp.password}")
    private String ftpPassword;

    /**
     * 마이페이지 > 고지설정진입
     * BY PYS
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("notiConfig")
    public ModelAndView notiConfig(@RequestParam(defaultValue = "") String chaCd) throws Exception {

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        MyPageDTO smsConfig = new MyPageDTO();
        MyPageDTO atConfig = new MyPageDTO();
        MyPageDTO billConfig1 = new MyPageDTO();
        MyPageDTO billConfig2 = new MyPageDTO();
        MyPageDTO billConfig3 = new MyPageDTO();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String code = authentication.getName();
        try {

            reqMap.put("chaCd", code);
            logger.debug("기관 {} 마이페이지 > 고지설정진입", code);

            //SMS설정 조회
            smsConfig = myPageService.getSmsConfig(reqMap);

            logger.info("USESMSYN" + StrUtil.nullToVoid(smsConfig.getUseSmsYn()) + "NOTSMSYN =" + StrUtil.nullToVoid(smsConfig.getNotSmsYn()) + "RCPSMSYN" + StrUtil.nullToVoid(smsConfig.getRcpSmsYn()) + "RCPSMSTY" + StrUtil.nullToVoid(smsConfig.getRcpSmsTy()));

            if (smsConfig == null) {
                smsConfig = new MyPageDTO();
                smsConfig.setNotSms(
                        "안녕하세요? [#고객명#]님. [#청구월#] 납부금액은 [#청구액#] 입니다. [#고지서용마감일#]까지 [#납부계좌#]로 입금해 주시기 바랍니다.\r\n"
                                + " 청구 상세내역 및 즉시납부 바로가기 \r\n" + "[#모바일청구서#]\r\n" + "");
                smsConfig.setRcpSms("안녕하세요? [#고객명#]님 [#기관명#] [#청구월#] 납부금액 [#청구액#] 입금이 확인되었습니다. \r\n" + "");
                smsConfig.setNotSms2(
                        "안녕하세요? [#고객명#]님 [#기관명#] [#청구월#] 미납금액 [#미납액#]을 [#고지서용마감일#] 입금해 주시기 바랍니다. \r\n" + "");
            } else if ((smsConfig.getNotSms() == null || smsConfig.getNotSms().length() == 0) && (smsConfig.getRcpSms() == null
                    || smsConfig.getRcpSms().length() == 0) && (smsConfig.getNotSms2() == null || smsConfig.getNotSms2().length() == 0)) {
                smsConfig.setNotSms(
                        "안녕하세요? [#고객명#]님. [#청구월#] 납부금액은 [#청구액#] 입니다. [#고지서용마감일#]까지 [#납부계좌#]로 입금해 주시기 바랍니다.\r\n"
                                + " 청구 상세내역 및 즉시납부 바로가기 \r\n" + "[#모바일청구서#]\r\n" + "");
                smsConfig.setRcpSms("안녕하세요? [#고객명#]님 [#기관명#] [#청구월#] 납부금액 [#청구액#] 입금이 확인되었습니다. \r\n" + "");
                smsConfig.setNotSms2(
                        "안녕하세요? [#고객명#]님 [#기관명#] [#청구월#] 미납금액 [#미납액#]을 [#고지서용마감일#] 입금해 주시기 바랍니다. \r\n" + "");
            }
            smsConfig.setNotSms(StringEscapeUtils.escapeHtml4(smsConfig.getNotSms()));
            smsConfig.setNotSms2(StringEscapeUtils.escapeHtml4(smsConfig.getNotSms2()));
            smsConfig.setRcpSms(StringEscapeUtils.escapeHtml4(smsConfig.getRcpSms()));
            smsConfig.setSmsMsgDef1(StringEscapeUtils.escapeHtml4(smsConfig.getSmsMsgDef1()));
            smsConfig.setSmsMsgDef2(StringEscapeUtils.escapeHtml4(smsConfig.getSmsMsgDef2()));

            // 알림톡설정 조회
            atConfig = myPageService.getAtConfig(reqMap);

            reqMap.put("billGubn", "01");
            //고지서설정 조회
            billConfig1 = myPageService.getBillConfig(reqMap);
            reqMap.put("billGubn", "02");
            billConfig2 = myPageService.getBillConfig(reqMap);
            reqMap.put("billGubn", "03");
            billConfig3 = myPageService.getBillConfig(reqMap);
            String emptybill = "안녕하세요, 고객님\r\n" +
                    "아래 청구 내용 및 금액을 확인하시고 납부마감일까지 입금 해주시기 바랍니다. \r\n" +
                    "은행 방문은 물론 인터넷 및 스마트뱅킹을 포함한 모든 방식으로 신한은행 가상계좌 번호로 납부가 가능합니다.\r\n" +
                    "가상계좌로 입금 시 유의할 점 : \r\n" +
                    "1. 해당 가상계좌는 납부고객별 고유의 입금 계좌번호이므로, 타인과 공유하여 사용하실 수 없습니다. \r\n" +
                    "2. 계좌번호와 금액을 정확히 확인 후 입금해주시기 바랍니다. \r\n" +
                    "3. 납입기한 내 입금을 하셔야 납부가 가능하오니 기한을 준수하여 납부해 주시기 바랍니다.\r\n" +
                    "4. 신한은행을 제외한 타 은행을 이용하여 입금 시 입금 수수료가 발생될 수 있습니다.\r\n" +
                    "\r\n" +
                    "※ 청구내용 및 납부방법 관련 문의 사항이 있으신 고객님께서는 아래로 문의 주시기 바랍니다.";

            if (billConfig1 == null) {
                billConfig1 = new MyPageDTO();

                billConfig1.setBillCont1(emptybill);
            }
            if (billConfig2 == null) {
                billConfig2 = new MyPageDTO();
//				billConfig2.setBillCont2(emptybill);
            }
            if (billConfig3 == null) {
                billConfig3 = new MyPageDTO();
//				billConfig3.setBillCont3(emptybill);
            }
            if ((billConfig1.getBillCont1() == null
                    || billConfig1.getBillCont1().length() == 0) && (billConfig2.getBillCont2() == null
                    || billConfig2.getBillCont2().length() == 0) && (billConfig3.getBillCont3() == null || billConfig3.getBillCont3().length() == 0)) {
                billConfig1.setBillCont1(emptybill);
            }
            billConfig1.setBillCont1(StringEscapeUtils.escapeHtml4(billConfig1.getBillCont1()));
            billConfig2.setBillCont2(StringEscapeUtils.escapeHtml4(billConfig2.getBillCont2()));
            billConfig3.setBillCont3(StringEscapeUtils.escapeHtml4(billConfig3.getBillCont3()));
            billConfig1.setBillName1(StringEscapeUtils.escapeHtml4(billConfig1.getBillName1()));
            billConfig1.setBillName2(StringEscapeUtils.escapeHtml4(billConfig1.getBillName2()));
            billConfig1.setBillName3(StringEscapeUtils.escapeHtml4(billConfig1.getBillName3()));


            map.put("chaCd", code);
            map.put("smsConfig", smsConfig);
            map.put("atConfig", atConfig);
            map.put("billConfig1", billConfig1);
            map.put("billConfig2", billConfig2);
            map.put("billConfig3", billConfig3);
            File file = new File(logoPath + chaCd + ".jpg");
            if (file.exists()) {
                map.put("logoImg", logoPath + chaCd + ".jpg");
            } else {
                map.put("logoImg", "");
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        mav.addObject("map", map);
        mav.setViewName("org/myPage/notiConfig");

        return mav;
    }

    /**
     * 마이페이지 > 고지설정 > SMS/LMS설정
     * BY PYS
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("updateSmsNoti")
    public void updateSmsNoti(@RequestBody MyPageDTO dto) throws Exception {

        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        try {
            logger.debug("기관 {} 고지설정 > SMS/LMS설정", dto.getChaCd());
            reqMap.put("useSmsYn", dto.getUseSmsYn());
            reqMap.put("notSmsYn", dto.getNotSmsYn());
            reqMap.put("rcpSmsTy", dto.getRcpSmsTy());
            reqMap.put("notSms", StringEscapeUtils.unescapeHtml4(dto.getNotSms())); //청구발송ㅁ
            reqMap.put("rcpSms", StringEscapeUtils.unescapeHtml4(dto.getRcpSms()));//입금발송ㅁ
            reqMap.put("notSms2", StringEscapeUtils.unescapeHtml4(dto.getNotSms2())); //미납발송 ㅁ
            reqMap.put("smsMsgDef1", StringEscapeUtils.unescapeHtml4(dto.getSmsMsgDef1())); //기본메세지1
            reqMap.put("smsMsgDef2", StringEscapeUtils.unescapeHtml4(dto.getSmsMsgDef2())); //기본메세지2
            reqMap.put("chaCd", dto.getChaCd());

            logger.info("USESMSYN" + StrUtil.nullToVoid(dto.getUseSmsYn()) + "NOTSMSYN =" + StrUtil.nullToVoid(dto.getNotSmsYn()) + "RCPSMSYN" + StrUtil.nullToVoid(dto.getRcpSmsYn()) + "RCPSMSTY" + StrUtil.nullToVoid(dto.getRcpSmsTy()));

            //SMS설정 수정
            myPageService.updateSmsNoti(reqMap);


        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    /**
     * 마이페이지 > 고지설정 > 고지서설정
     * BY PYS
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("saveBillForm")
    public HashMap<String, Object> updateBillForm(MultipartHttpServletRequest request) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap1 = new HashMap<String, Object>();
        HashMap<String, Object> reqMap2 = new HashMap<String, Object>();
        HashMap<String, Object> reqMap3 = new HashMap<String, Object>();
        String billGubn01 = "N";
        String billGubn02 = "N";
        String billGubn03 = "N";

        try {
            //기관 코드 받아오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String chaCd = authentication.getName();
            logger.debug("기관 {} 고지설정 > 고지서설정", chaCd);

            //저장 경로 설정
            String path = logoPath;
            String sendPath = ftpLogoPath;

            if (request.getParameter("imgExist").equals("N")) {
                File lFile = new File(logoPath + chaCd + ".jpg");  //로고 삭제후 세이브
                if (lFile.exists()) {
                    lFile.delete();
                }
            }

            //업로드 되는 파일명
            String newFileName = "";
            //경로 폴더 없으면 폴더 생성
            File dir = new File(path);
            if (dir.exists() == false) {
                dir.mkdirs();
            }

            logger.info("파일저장경로" + path);
            MultipartFile logoFile = request.getFile("uploadLogo");
            if (!logoFile.isEmpty()) {
                String fileName = logoFile.getOriginalFilename();

                //확장자 추출
                int pos = fileName.lastIndexOf(".");
                String ext = fileName.substring(pos + 1);

                if (ext.equals("jpg")) {
                    logger.info("실제 파일 이름 : " + fileName);
                    newFileName = chaCd + "." + ext;
                    logger.info("변형 파일 이름 : " + newFileName);

                    logoFile.transferTo(new File(path + newFileName)); // 파일 보내기

                    FTPHelper ftpHelper = new FTPHelper(ftpUrl, ftpUser, ftpPassword); //사용하는 확인 필요
                    ftpHelper.setBinaryTransfer(true);
                    ftpHelper.setCharset("EUC-KR");
                    ftpHelper.send(path + newFileName, sendPath + newFileName);
                } else {
                    map.put("retCode", "9999");
                    map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
                    return map;
                }
            }

            map.put("chaCd", request.getParameter("chaCd"));

            reqMap1.put("chaCd", request.getParameter("chaCd"));
            reqMap1.put("billGubn", "01");
            reqMap1.put("billName", StringEscapeUtils.unescapeHtml4(request.getParameter("billName1")));
            reqMap1.put("billCont", StringEscapeUtils.unescapeHtml4(request.getParameter("billCont1")));
            reqMap2.put("chaCd", request.getParameter("chaCd"));
            reqMap2.put("billGubn", "02");
            reqMap2.put("billName", StringEscapeUtils.unescapeHtml4(request.getParameter("billName2")));
            reqMap2.put("billCont", StringEscapeUtils.unescapeHtml4(request.getParameter("billCont2")));
            reqMap3.put("chaCd", request.getParameter("chaCd"));
            reqMap3.put("billGubn", "03");
            reqMap3.put("billName", StringEscapeUtils.unescapeHtml4(request.getParameter("billName3")));
            reqMap3.put("billCont", StringEscapeUtils.unescapeHtml4(request.getParameter("billCont3")));

            List<MyPageDTO> list = myPageService.selectBillForm(map);

            //기존에 고지서설정 저장정보가 없으면..
            if (list.size() == 0) {
                // 고지서설정내용 등록
                myPageService.insertBillForm(reqMap1);
                myPageService.insertBillForm(reqMap2);
                myPageService.insertBillForm(reqMap3);
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if ("01".equals(list.get(i).getBillGubn())) {
                        myPageService.updateBillForm(reqMap1);
                        billGubn01 = "Y";
                    } else if ("02".equals(list.get(i).getBillGubn())) {
                        myPageService.updateBillForm(reqMap2);
                        billGubn02 = "Y";
                    } else if ("03".equals(list.get(i).getBillGubn())) {
                        myPageService.updateBillForm(reqMap3);
                        billGubn03 = "Y";
                    }
                }
                if ("N".equals(billGubn01)) {
                    myPageService.insertBillForm(reqMap1);
                }
                if ("N".equals(billGubn02)) {
                    myPageService.insertBillForm(reqMap2);
                }
                if ("N".equals(billGubn03)) {
                    myPageService.insertBillForm(reqMap3);
                }
            }
            map.put("retCode", "0000");

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }
        return map;
    }


    /**
     * 마이페이지 > 기관정보관리
     * BY PYS
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("orgInfoMgmt")
    public ModelAndView orgInfoMgmt(@RequestParam(defaultValue = "") String chaCd) throws Exception {

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String code = authentication.getName();
        try {
            logger.debug("기관 {} 고지설정 > 기관정보관리", chaCd);
            reqMap.put("chaCd", code);

            // 기관정보 조회
            MyPageDTO result = myPageService.selectChaInfo(reqMap);
            result.setChaAddress1(StringEscapeUtils.escapeHtml4(result.getChaAddress1()));
            result.setChaAddress2(StringEscapeUtils.escapeHtml4(result.getChaAddress2()));
            if (result.getAdjAccYn().equals("Y")) {
                List<MyPageDTO> adjList = myPageService.adjList(reqMap);
                map.put("adjList", adjList);
            }
            List<ClaimItemDTO> accList = claimItemService.moneyPassbookList(code);
            map.put("accList", accList);
            map.put("chaCd", code);
            map.put("result", result);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        mav.addObject("map", map);
        mav.setViewName("org/myPage/orgInfoMgmt");

        return mav;
    }

    /**
     * 마이페이지 > 기관정보관리 수정
     * BY PYS
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("updateOrgInfo")
    public void updateOrgInfo(@RequestBody HashMap<String, List<MyPageDTO>> body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        try {
            MyPageDTO list = body.get("rec").get(0);
            logger.debug("기관 {} 기관정보관리 수정", list.getChaCd());
            reqMap.put("chaCd", list.getChaCd());
            reqMap.put("chrName", list.getChrName());
            reqMap.put("chrTelNo", list.getChrTelNo());
            reqMap.put("chrHp", list.getChrHp());
            reqMap.put("chrMail", list.getChrMail());
            reqMap.put("chaZipCode", list.getChaZipCode());
            reqMap.put("chaAddr1", list.getChaAddress1());
            reqMap.put("chaAddr2", list.getChaAddress2());
            myPageService.updateOrgInfo(reqMap);

            if (body.get("rec").size() > 1) {
                for (MyPageDTO obj : body.get("rec")) {
                    map.put("chaCd", obj.getChaCd());
                    map.put("grpadjname", obj.getGrpAdjName());
                    map.put("adjfiregkey", obj.getAdjFiRegkey());
                    myPageService.updateOrgAccInfo(map);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 마이페이지 > 서비스설정진입
     * BY PYS
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("serviceConfig")
    public ModelAndView serviceConfig(@RequestParam(defaultValue = "") String chaCd) throws Exception {

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String code = authentication.getName();
        try {

            reqMap.put("chaCd", code);
            logger.debug("기관 {} 마이페이지 > 서비스설정진입", code);

            MyPageDTO result = myPageService.selectChaInfo(reqMap);

            map.put("chaCd", code);
            map.put("result", result);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        mav.addObject("map", map);
        mav.setViewName("org/myPage/serviceConfig");

        return mav;
    }

    /**
     * 마이페이지 > 서비스설정 > 고객구분수정
     * BY PYS
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("updateServiceConfig")
    public void updateCusGubn(@RequestBody MyPageDTO dto) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        try {
            logger.debug("기관 {}  마이페이지 > 서비스설정 > 고객구분수정", dto.getChaCd());
            reqMap.put("chaCd", dto.getChaCd());
            reqMap.put("cusGubn1", dto.getCusGubn1());
            reqMap.put("cusGubn2", dto.getCusGubn2());
            reqMap.put("cusGubn3", dto.getCusGubn3());
            reqMap.put("cusGubn4", dto.getCusGubn4());
            reqMap.put("cusGubnYn1", dto.getCusGubnYn1());
            reqMap.put("cusGubnYn2", dto.getCusGubnYn2());
            reqMap.put("cusGubnYn3", dto.getCusGubnYn3());
            reqMap.put("cusGubnYn4", dto.getCusGubnYn4());

            myPageService.updateCusGubn(reqMap);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 마이페이지 > 기관정보관리 > 비밀번호변경
     * BY PYS
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("updatePwd")
    public HashMap<String, Object> updatePwd(@RequestBody MyPageDTO dto) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        String passWrd = "";
        int failCnt = 0;
        try {
            logger.debug("기관 {}  마이페이지 > 기관정보관리 > 비밀번호변경", dto.getChaCd());

            reqMap.put("chaCd", dto.getChaCd());
            reqMap.put("pwd", shaEncoder.encoding(dto.getPwd()));            // 현재
            reqMap.put("chgPwd1", shaEncoder.encoding(dto.getChgPwd1()));    // 변경
            reqMap.put("chgPwd2", dto.getChgPwd2());

            // 현재 비밀번호 조회
            passWrd = myPageService.selectPwd(reqMap);
            failCnt = myPageService.selectFailCnt(reqMap);
            if (failCnt == 5) {
                map.put("retCode", "5555");
            } else {
                if (!passWrd.equals(reqMap.get("pwd"))) {
                    reqMap.put("stat", "fail");
                    myPageService.updateFailCnt(reqMap);
                    map.put("retCode", "8888");
                } else if (passWrd.equals(reqMap.get("chgPwd1"))) {
                    map.put("retCode", "9999");
                } else {
                    reqMap.put("stat", "success");
                    myPageService.updateFailCnt(reqMap);
                    myPageService.updatePwd(reqMap);
                    map.put("retCode", "0000");
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return map;
    }

    /**
     * 알림톡 설정 수정
     */
    @ResponseBody
    @RequestMapping("updateAtNoti")
    public void updateAtNoti(@RequestBody MyPageDTO dto) throws Exception {
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        logger.debug("기관 {} 고지설정 > SMS/LMS설정", dto.getChaCd());
        try {
            reqMap.put("chaCd", dto.getChaCd());
            reqMap.put("notAtYn", dto.getNotAtYn());
            reqMap.put("rcpAtYn", dto.getRcpAtYn());

            //SMS설정 수정
            myPageService.updateAtNoti(reqMap);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }
}
