package com.finger.shinhandamoa.org.notimgmt.web;

import com.finger.shinhandamoa.common.*;
import com.finger.shinhandamoa.common.FingerIntegrateMessaging.MessageType;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiSendDTO;
import com.finger.shinhandamoa.org.notimgmt.service.NotiService;
import com.finger.shinhandamoa.sys.addServiceMgmt.dto.AddServiceMgmtDTO;
import com.finger.shinhandamoa.util.dto.CodeDTO;
import com.finger.shinhandamoa.util.service.CodeService;
import com.finger.shinhandamoa.util.service.VarParamInfoService;
import com.finger.shinhandamoa.vo.PageVO;
import com.finger.shinhandamoa.vo.UserDetailsVO;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.View;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 고지관리
 *
 * @author puki
 * @author wisehouse@finger.co.kr
 */
@Controller
@RequestMapping("org/notiMgmt/**")
public class NotiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotiController.class);

    @Inject
    private NotiService notiService;

    @Inject
    private VarParamInfoService varParamInfoService;

    @Inject
    private CodeService codeService;

    // 업로드 디렉토리
    @Value("${file.path.upload}")
    private String uploadPath;

    @Value("${fim.server.host}")
    private String host;

    @Value("${fim.server.port}")
    private int port;

    @Value("${fim.accessToken}")
    private String accessToken;

    @Value("${finger.alarmTalk.host}")
    private String atUrl;

    @Value("${finger.alarmTalk.serviceKey}")
    private String atServiceKey;

    @ResponseBody
    @RequestMapping("selSmsUseYn")
    public HashMap selSmsUseYn() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();
        LOGGER.debug("기관 {} 문자사용여부확인", chaCd);
        try {
            String useYn = notiService.selSmsUseYn(chaCd);

            map.put("map", useYn);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
        return map;
    }

    /**
     * sms고지 발송을 위한 고객정보 by YEJ
     *
     * @param
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("smsRecNo")
    public HashMap selSmsRecNo(@RequestBody NotiSendDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} sms고지 발송을 위한 고객정보", chaCd);
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            HashMap<String, Object> imap = new HashMap<String, Object>();
            imap.put("chaCd", chaCd);

            if (!dto.getStrList().isEmpty()) {
                imap.put("strList", dto.getStrList());

                List<NotiSendDTO> cuslist = notiService.selCusInfo(imap);
                map.put("list", cuslist);
            }

            // 발신번호
            final List<NotiSendDTO> sendList = notiService.selSendNo(chaCd);
            map.put("sendList", sendList);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

        return map;
    }

    /**
     * sms고지 발송을 위한 발송문구정보 by YEJ
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("selSmsMsg")
    public HashMap selectSmsMsg(@RequestBody NotiSendDTO dto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} sms고지 발송을 위한 발송문구정보", chaCd);
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> smap = new HashMap<String, Object>();
        try {
            smap.put("chaCd", chaCd);

            map.put("msg", notiService.selSmsMsgbox(smap));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

        return map;
    }

    /**
     * 문자메시지 서비스 이용 등록을 위한 기관 정보 by YEJ
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("smsCertificate")
    public HashMap selSmsCertificate(@RequestBody NotiSendDTO dto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} 문자메시지 서비스 이용 등록을 위한 기관 정보", chaCd);
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            if (dto.getChaCd() != null && dto.getChaCd() != "") {
                chaCd = dto.getChaCd();
            }

            map.put("map", notiService.selOrgInfo(chaCd));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

        return map;
    }

    /**
     * 문자메시지 서비스 이용 등록 by YEJ
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "smsCertificateIns", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public void inssmsCertificateIns(MultipartHttpServletRequest request) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetailsVO userDetails = (UserDetailsVO) principal;

        LOGGER.debug("기관 {}  문자메시지 서비스 이용 등록", user);

        File uploadDir = new File(uploadPath); // 업로드 디렉토리
        if (uploadDir.exists() == false) {
            uploadDir.mkdirs();
        }

        MultipartFile uploadFile = request.getFile("file");
        String fileName = uploadFile.getOriginalFilename();

        //확장자 추출
        int pos = fileName.lastIndexOf(".");
        String ext = fileName.substring(pos + 1);
        String uuid = UUID.randomUUID().toString().replaceAll("-", ""); //사용할 경우
        try {
            if (!ext.equals("jpg") && !ext.equals("pdf") && !ext.equals("gif") && !ext.equals("png") && !ext.equals("tif")) {

            } else {
                map.put("bbs", 10);
                map.put("step", "0");
                map.put("password", "1");
                map.put("title", "통신서비스 이용증명원 신청");
                map.put("fileName", fileName);
                map.put("fileSize", Integer.parseInt(request.getParameter("fileSize")));
                map.put("fileid", uuid);
                map.put("fileext", "." + ext);

                map.put("ip", "");
                map.put("isHtml", 0);
                map.put("isFix", 0);

                if (request.getParameter("fileno") != null && !request.getParameter("fileno").equals("0")) {
                    map.put("chacd", request.getParameter("regChacd"));
                    map.put("fileno", request.getParameter("fileno"));
                    notiService.updateFormInfo(map);
                } else {
                    map.put("chaCd", userDetails.getUsername());
                    map.put("writer", userDetails.getName());
                    map.put("email", userDetails.getEmail());
                    map.put("contents", request.getParameter("strList"));
                    map.put("smsSendTel", request.getParameter("strList"));
                    notiService.insertFormInfo(map);
                }

                uploadFile.transferTo(new File(uploadPath + uuid + "." + ext)); // 파일 보내기
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 문자메시지 서비스 이용 등록을 위한 기관 정보 by YEJ
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("selCusName")
    public HashMap selCusName(@RequestBody NotiSendDTO dto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} 문자메시지 서비스 이용 등록을 위한 기관 정보", chaCd);
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            HashMap<String, Object> smap = new HashMap<String, Object>();
            smap.put("chaCd", chaCd);
            smap.put("cusHp", dto.getCusHp());

            map.put("map", notiService.selCusName(smap));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
        return map;
    }

    /**
     * 문자메시지 발송 by YEJ
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("smsMsgSend")
    public HashMap<String, Object> smsMsgSend(@RequestBody NotiSendDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} 문자메시지 발송", chaCd);
        HashMap<String, Object> map = new HashMap<String, Object>();
        int dailySendLimit = 0;
        int dailyRepeatedCount = 0;
        int monthlyRepeatedMean = 0;
        int dailySameMsgSendCnt = 0;
        Date nowDate = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentTime = transFormat.format(nowDate);
        int currentHour = Integer.parseInt(currentTime.substring(8, 10));

        try {
            FingerIntegrateMessaging fingerIntegrateMessaging = new FingerIntegrateMessaging();

            fingerIntegrateMessaging.setHost(host);
            fingerIntegrateMessaging.setPort(port);
            fingerIntegrateMessaging.setAccessToken(accessToken);

            if(chaCd.length() != 8){
                map.put("resultCd", "0000");
                return map;
            }

            for (int i = 0; i < dto.getStrList().size(); i++) {
                String hpNo = dto.getStrList().get(i);
                String notiMasCd = dto.getIdxList().get(i);

                if (notiMasCd.substring(0, 3).equals("fix")) {
                    notiMasCd = "00000000000000000000";
                }

                HashMap<String, Object> smap = new HashMap<String, Object>();
                smap.put("chaCd", chaCd);
                smap.put("msg", StringEscapeUtils.unescapeHtml4(dto.getMsg()));
                smap.put("cusHp", hpNo);
                smap.put("notiMasCd", notiMasCd);

                String msg = varParamInfoService.setVarChange(smap); // 가변변수 치환
                smap.put("convertedMsg", msg);

                int mLength = msg.getBytes("utf-8").length;

                MessageType messageType;
                String type = "";
                if (mLength > 80) {
                    messageType = MessageType.MMS;
                    type = "1";
                } else {
                    messageType = MessageType.SMS;
                    type = "0";
                }

                //일일 갯수 5천개 넘어가면 무효 시간 23~4시 사이도 무효
                dailySendLimit = notiService.selectDailySentCount(smap);
                //일일 동일 메세지 3회 발송 제한
                dailySameMsgSendCnt = notiService.selectDailySameMsgCnt(smap);

                if (dailySendLimit > 5000) {
                    LOGGER.debug("기관 {} 문자메시지 5000건 초과", chaCd);
                    map.put("resultCd", "0001");
                    break;
                } else if (currentHour > 22 || currentHour < 4) {
                    LOGGER.debug("기관 {} 문자메시지 시간 초과", chaCd);
                    map.put("resultCd", "0002");
                    break;
                } else if (dailySameMsgSendCnt >= 2) {
                    LOGGER.debug("기관 {} 수신번호 {} 동일 문자메시지 발송 3회 이상", chaCd, hpNo.trim());
                    map.put("resultCd", "0003");
                } else {

                    HashMap<String, Object> imap = new HashMap<String, Object>();
                    imap.put("chaCd", chaCd);
                    imap.put("cusHp", hpNo.trim());
                    dailyRepeatedCount = notiService.selectDailyRepeatedCount(imap);
                    monthlyRepeatedMean = notiService.selectMonthlyRepeatedMean(imap);

                    if(dailyRepeatedCount > 499) {
                        LOGGER.debug("기관 {} 문자메시지 일일 동일번호 {} 초과", chaCd, hpNo.trim());
                    } else if (monthlyRepeatedMean> 9){
                        LOGGER.debug("기관 {} 문자메시지 한달간 동일번호 {} 초과", chaCd, hpNo.trim());
                    } else {
                        final ListResult<FingerIntegrateMessaging.Message> listResult = fingerIntegrateMessaging.createMessage(
                                messageType, "", StringEscapeUtils.unescapeHtml4(msg), null, dto.getTelNo(), hpNo, dto.getWriter()); // sms 발송
                        if (listResult.getItemList() != null) {
                            imap.put("smsReqCd", listResult.getItemList().get(0).getId());
                            imap.put("fiCd", "088");            // 은행코드
                            imap.put("telNo", dto.getTelNo());
                            imap.put("status", "0");            // 전송상태 0 : 대기
                            imap.put("rslt", "");                // 결과코드  >>>>> 코드값 확인
                            imap.put("msg", msg);
                            imap.put("type", type);                // 메시지전송타입 SMS(0), LMS(1)
                            imap.put("sendCnt", "0");            // 시도횟수
                            imap.put("smsReqSt", "ST10");            // 상태 ST07, ST08  >>>>> 코드값 확인
                            imap.put("chatrTy", "01");            // 가맹점접속형태 Web(01), 전문(03)
                            imap.put("notiMasCd", notiMasCd);
                            // sms 전송 후처리
                            notiService.insertSmsReq(imap);
                        }
                    }

                    map.put("resultCd", "0000 ");
                    LOGGER.debug("re");
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            //resultCd 가 null 인경우를 방지하기위해
             if(CmmnUtils.empty(map.get("resultCd"))){
                map.put("resultCd", "0000 ");
            }
        }

        return map;
    }

    /**
     * 문자메시지 발송이력 by YEJ
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("sendSmsList")
    public HashMap sendSmsList(@RequestBody NotiSendDTO dto) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String chaCd = authentication.getName();
        LOGGER.debug("기관 {} 문자메시지 발송이력", chaCd);
        final HashMap<String, Object> cmap = new HashMap<String, Object>();
        cmap.put("chaCd", chaCd);
        cmap.put("stDt", dto.getStDt());
        cmap.put("edDt", dto.getEdDt());
        cmap.put("selGubn", dto.getSelGubn());
        cmap.put("keyword", dto.getKeyword());
        cmap.put("search_orderBy", dto.getSearch_orderBy());
        cmap.put("status", dto.getStatus());

        final HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            final FingerIntegrateMessaging fingerIntegrateMessaging = new FingerIntegrateMessaging();
            fingerIntegrateMessaging.setHost(host);
            fingerIntegrateMessaging.setPort(port);
            fingerIntegrateMessaging.setAccessToken(accessToken);

            // total count
            final int count = notiService.sendSmsTotalCount(cmap);
            map.put("count", count);

            // 페이지 관련 설정
            final PageVO page = new PageVO(count, dto.getCurPage(), 10);
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            cmap.put("start", start);
            cmap.put("end", end);

            final List<NotiSendDTO> list = notiService.sendSmsList(cmap);

            map.put("list", list);
            map.put("modalPager", page);
            map.put("modalNo", 1);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("excelSendSmsListDownload")
    public View excelSendSmsListDownload(HttpServletRequest request, HttpServletResponse response, NotiSendDTO dto, Model model) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            LOGGER.debug("기관 {} 고지서조회/출력 > 문제메시지고지 > 발송내역 파일저장", chaCd);
            final HashMap<String, Object> cmap = new HashMap<String, Object>();
            cmap.put("chaCd", chaCd);
            cmap.put("stDt", dto.getExcelStartDt());
            cmap.put("edDt", dto.getExcelEndDt());
            cmap.put("selGubn", dto.getExcelGubun());
            cmap.put("keyword", dto.getExcelKeyword());
            cmap.put("search_orderBy", dto.getExcelOrderBy());
            cmap.put("status", dto.getExcelSmsStatus());

            List<NotiSendDTO> list = notiService.excelSendSmsListDownload(cmap);
            model.addAttribute("list", list);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }

        return new ExcelSaveNoti();
    }

    /**
     * email 발송을 위한 고객정보 by YEJ
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("emailRecNo")
    public HashMap selEmailRecNo(@RequestBody NotiSendDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} email 발송을 위한 고객정보", chaCd);
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            HashMap<String, Object> imap = new HashMap<String, Object>();
            imap.put("chaCd", chaCd);
            LOGGER.debug(dto.getSearch_orderBy());
            imap.put("search_orderBy", dto.getSearch_orderBy());
            if (!dto.getStrList().isEmpty()) {
                imap.put("strList", dto.getStrList());
                List<NotiSendDTO> list = notiService.selEmailCusInfo(imap);

                map.put("list", list);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

        return map;
    }

    /**
     * email 발송을 위한 기관템플릿 유무 by YEJ
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("selEmailUseYn")
    public HashMap selEmailUseYn() throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} email 발송을 위한 기관템플릿 유무", chaCd);

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            HashMap<String, Object> imap = new HashMap<String, Object>();
            imap.put("chaCd", chaCd);

            map.put("cnt", notiService.selEmailUseYn(imap));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

        return map;
    }

    /**
     * email 발송을 위한 안내문구 by YEJ
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("billName")
    public HashMap selBillName(@RequestBody NotiSendDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} email 발송을 위한 안내문구", chaCd);

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            HashMap<String, Object> imap = new HashMap<String, Object>();
            imap.put("chaCd", chaCd);
            imap.put("billGubn", dto.getBillGubn());

            map.put("map", notiService.selBillName(imap));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

        return map;
    }

    /**
     * email 발송 by YEJ
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("emailMsgSend")
    public void emailMsgSend(@RequestBody NotiSendDTO dto) throws Exception {
        // 세션 사용자 조회
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetailsVO vo = (UserDetailsVO) authentication.getPrincipal();
        LOGGER.debug("기관 {} email 발송", authentication.getName());
        final HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            map.put("chaCd", vo.getUsername());
            map.put("billGubn", dto.getBillGubn());

            for (int i = 0; i < dto.getIdxList().size(); i++) {
                final StringBuilder sb = new StringBuilder();
                map.put("notiMasCd", dto.getIdxList().get(i));

                // 안내문구 및 고객정보 가져오기
                final NotiSendDTO sdto = notiService.selMailCont(map);
                sdto.setCusName(StringEscapeUtils.unescapeHtml4(sdto.getCusName()));
                sdto.setBillCont(StringEscapeUtils.unescapeHtml4(sdto.getBillCont()));
                sdto.setBillCont2(StringEscapeUtils.unescapeHtml4(sdto.getBillCont2()));
                sdto.setBillName(StringEscapeUtils.unescapeHtml4(sdto.getBillName()));
                sdto.setContents(StringEscapeUtils.unescapeHtml4(sdto.getContents()));
                sdto.setRemark(StringEscapeUtils.unescapeHtml4(sdto.getRemark()));
                sdto.setSubject(StringEscapeUtils.unescapeHtml4(sdto.getSubject()));
                // 기관 고객구분 사용
                final List<CodeDTO> codeList = codeService.cusGubnCd(vo.getUsername());
                // 해당고객 고객구분
                final NotiSendDTO gdto = notiService.selCusGubn(sdto.getVano());
                gdto.setCusName(StringEscapeUtils.unescapeHtml4(gdto.getCusName()));
                gdto.setCusGubn1(StringEscapeUtils.unescapeHtml4(gdto.getCusGubn1()));
                gdto.setCusGubn2(StringEscapeUtils.unescapeHtml4(gdto.getCusGubn2()));
                gdto.setCusGubn3(StringEscapeUtils.unescapeHtml4(gdto.getCusGubn3()));
                gdto.setCusGubn4(StringEscapeUtils.unescapeHtml4(gdto.getCusGubn4()));
                gdto.setRemark(StringEscapeUtils.unescapeHtml4(gdto.getRemark()));


                sdto.setSubject(sdto.getSubject().replace("\\", ""));
                sdto.setCusals(StringUtils.defaultString(sdto.getCusals()).replace("\\", ""));
                sdto.setContents(sdto.getContents().replace("\\", ""));
                sdto.setBillCont2(sdto.getBillCont2().replace("\\", ""));
                sdto.setRemark(sdto.getRemark().replace("\\", ""));

                // 전문,템플릿내용
                sb.append("@s@"); // 그룹헤더
                sb.append("+");
                sb.append("f");
                sb.append("+");
                sb.append("25");
                sb.append("+");
                sb.append(sdto.getCusName());    // 고객명상단
                sb.append("+");
                sb.append(sdto.getChaName().replaceAll("\\+", ""));    // 기관명상단
                sb.append("+");
                sb.append(sdto.getChaName().replaceAll("\\+", ""));    // 기관명하단
                sb.append("+");
                sb.append(sdto.getAddr().replaceAll("\\+", ""));        // 기관주소하단
                sb.append("+");
                sb.append(sdto.getOwnerTel());    // 기관대표번호하단
                sb.append("+");
                sb.append(sdto.getChrMail());    // 기관이메일하단
                sb.append("+");
                sb.append(sdto.getChaName().replaceAll("\\+", ""));    // 기관명제목
                sb.append("+");
                sb.append(sdto.getSubject().replaceAll("\\+", ""));    // 고지서제목
                sb.append("+");
                sb.append(sdto.getCusName().replaceAll("\\+", ""));    // 고객명인사용
                sb.append("+");
                sb.append(sdto.getCusals().replaceAll("\\+", ""));    // 별칭
                sb.append("+");
                String str = "";
                for (int a = 0; a < codeList.size(); a++) {
                    str += codeList.get(a).getCodeName();
                    if (gdto != null) {
                        if (a == 0) {
                            str += " : " + StrUtil.nullToVoid(gdto.getCusGubn1());
                        } else if (a == 1) {
                            str += " : " + StrUtil.nullToVoid(gdto.getCusGubn2());
                        } else if (a == 2) {
                            str += " : " + StrUtil.nullToVoid(gdto.getCusGubn3());
                        } else if (a == 3) {
                            str += " : " + StrUtil.nullToVoid(gdto.getCusGubn4());
                        }
                    } else {
                        str += "| ";
                    }
                    if (a < (codeList.size() - 1)) {
                        str += "| ";
                    }
                }

                sb.append(str);                            // 고객구분
                sb.append("+");
                sb.append(sdto.getContents().replaceAll("\\r\\n|\\r|\\n", "<br/>"));                            // 고지내용1
                sb.append("+");
                sb.append(sdto.getChaName().replaceAll("\\+", ""));            // 기관명
                sb.append("+");
                sb.append(sdto.getTelNo());            // 기관대표전화번호 ( 고지서설정번호)
                sb.append("+");
                sb.append(sdto.getMasYear() + "년 ");    // 청구월
                sb.append(sdto.getMasMonth() + "월");
                sb.append("+");
                sb.append(sdto.getPayAmt());            // 청구금액
                sb.append("+");
                sb.append(sdto.getVano().substring(0, 3) + "-" + sdto.getVano().substring(3, 6) + "-" + sdto.getVano().substring(6));                // 납부계좌번호
                sb.append("+");
                sb.append(sdto.getPrintDate());            // 납부마감일
                sb.append("+");
                sb.append(sdto.getBillCont2());            // 고지내용2
                sb.append("+");
                sb.append(sdto.getChaName().replaceAll("\\+", ""));            // 기관명하단
                sb.append("+");
                sb.append(sdto.getAddr());                // 기관주소
                sb.append("+");
                sb.append(sdto.getOwnerTel());            // 기관대표번호하단
                sb.append("+");
                sb.append(sdto.getChrMail());            // 기관이메일
                sb.append("+");
                sb.append(sdto.getLogoNm());            // 이미지로고명
                sb.append("+");
                sb.append(StrUtil.nullToVoid(sdto.getRemark().replaceAll("\\r\\n|\\r|\\n", "<br/>")));            // remark
                sb.append(System.getProperty("line.separator"));

                final List<NotiSendDTO> list = notiService.selEmailItem(map);
                for (int l = 0; l < list.size(); l++) {
                    list.get(l).setPtrName(StringEscapeUtils.unescapeHtml4(list.get(l).getPtrName()));
                }

                sb.append("@g1@");                        // 그룹코드
                sb.append("+");
                sb.append("r");                            // 그룹타입
                sb.append("+");
                sb.append("3");                            // 그룹필드수
                sb.append("+");
                for (int j = 0; j < list.size(); j++) {
                    if (j > 0) {
                        sb.append(System.getProperty("line.separator"));
                    }
                    sb.append(list.get(j).getItemName());    // 내역
                    sb.append("+");
                    sb.append(list.get(j).getItemAmt());    // 금액
                    sb.append("+");
                    sb.append(StrUtil.nullToVoid(list.get(j).getPtrName()));    // 비고
                }

                // 메일전송
                map.put("ecareNo", dto.getEcareNo());        // 이케어번호(100:고지, 200:승인, 300:공지)
                map.put("channel", "M");                        // 채널
                map.put("tmplType", "J");                        // 템플릿타입
                map.put("receiverId", sdto.getVano());            // 수신자id
                map.put("receiverNm", sdto.getCusName());        // 수신자명
                map.put("receiver", dto.getStrList().get(i));    // 수신자mail
                map.put("senderNm", vo.getName());            // 발신자명
                map.put("sender", vo.getEmail());            // 발신자mail
                map.put("subject", sdto.getSubject());        // 제목
                map.put("sendFg", "R");                        // 발송여부플래그
                map.put("jonmun", sb.toString());            // 전문내용

                map.put("masMonth", sdto.getMasYear() + sdto.getMasMonth()); // 청구월
                map.put("slot2", "1");                        // 고지구분(1:청구고지, 2:납부고지, 3:미납고지)

                String seq = notiService.selEmailSeq(dto.getEcareNo());
                map.put("seq", seq);

                notiService.insertEmailSend(map);    // 메일전송
                notiService.insertEmailHist(map);    // 메일이력등록
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    /**
     * email 발송 이력 조회 by YEJ
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("sendEmailList")
    public HashMap selSendSmsList(@RequestBody NotiSendDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} email 발송 이력 조회", chaCd);
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            HashMap<String, Object> smap = new HashMap<String, Object>();
            smap.put("chaCd", chaCd);
            smap.put("stDt", dto.getStDt());
            smap.put("edDt", dto.getEdDt());
            smap.put("selGubn", dto.getSelGubn());
            smap.put("keyword", dto.getKeyword());
            smap.put("search_orderBy", dto.getSearch_orderBy());

            LOGGER.debug(dto.getSearch_orderBy());
            // total count
            int count = notiService.sendEmailTotalCount(smap);
            map.put("count", count);

            // 페이지 관련 설정
            PageVO page = new PageVO(count, dto.getCurPage(), 10);
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            smap.put("start", start);
            smap.put("end", end);

            List<NotiSendDTO> list = notiService.sendEmailList(smap);
            map.put("list", list);
            map.put("modalPager", page);
            map.put("modalNo", 2);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return map;
    }

    /**
     * email 수정 by YEJ
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("emailUpdate")
    public void emailUpdate(@RequestBody NotiSendDTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} email 수정", chaCd);
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            map.put("chaCd", chaCd);
            for (int i = 0; i < dto.getIdxList().size(); i++) {
                String notiMasCd = dto.getIdxList().get(i);
                String cusMail = dto.getStrList().get(i);
                map.put("notiMasCd", notiMasCd);
                map.put("cusMail", cusMail);

                notiService.emailInfoUpdate(map);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 알림톡 사용여부
     */
    @ResponseBody
    @RequestMapping("selAtUseYn")
    public HashMap selAtUseYn() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();
        LOGGER.debug("기관 {} 알림톡사용여부확인", chaCd);
        try {
            String useYn = notiService.selAtUseYn(chaCd);

            map.put("map", useYn);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
        return map;
    }

    /**
     * 알림톡 이용신청할 기관명
     */
    @ResponseBody
    @RequestMapping("selAtChaInfo")
    public HashMap selAtChaInfo(@RequestBody NotiSendDTO dto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} 알림톡 서비스 등록할 기관명", chaCd);
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            if (dto.getChaCd() != null && dto.getChaCd() != "") {
                chaCd = dto.getChaCd();
            }

            map.put("map", notiService.selAtChaInfo(chaCd));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

        return map;
    }

    /**
     * 알림톡 서비스 이용등록 신청
     */
    @ResponseBody
    @RequestMapping(value = "atCertificateIns")
    public void atCertificateIns(@RequestBody AddServiceMgmtDTO body) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();
        LOGGER.debug("기관 {} 알림톡 서비스 이용등록 신청", chaCd);

        try {
            map.put("chaCd", chaCd);
            map.put("atChaName", body.getAtChaName());

            notiService.atCertificateIns(map);
            notiService.atApply(map);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 알림톡 메시지 템플릿 셋팅
     */
    @ResponseBody
    @RequestMapping("setAtMsg")
    public HashMap setAtMsg(@RequestBody NotiSendDTO dto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} 알림톡 고지를 위한 메시지 템플릿 셋팅", chaCd);

        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            List<NotiSendDTO> msgMap = notiService.setAtMsg();

            map.put("claimMsg", msgMap.get(0));
            map.put("unpaidMsg", msgMap.get(1));
            map.put("depositMsg", msgMap.get(2));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.debug("기관 {} 알림톡 고지를 위한 메시지 템플릿 셋팅 실패", chaCd);
            throw e;
        }

        return map;
    }

    /**
     * 알림톡 고지를 위한 고객 정보
     */
    @ResponseBody
    @RequestMapping("selAtRecNo")
    public HashMap selAtRecNo(@RequestBody NotiSendDTO dto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        LOGGER.debug("기관 {} 알림톡 고지를 위한 고객정보", chaCd);
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            HashMap<String, Object> imap = new HashMap<String, Object>();
            imap.put("chaCd", chaCd);

            if (!dto.getStrList().isEmpty()) {
                imap.put("strList", dto.getStrList());

                // 고객 전화번호
                List<NotiSendDTO> cuslist = notiService.selCusInfo(imap);
                map.put("list", cuslist);
            }

            // 발송기관명
            map.put("map", notiService.selAtChaInfo(chaCd));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

        return map;
    }

    /**
     * 알림톡 발송
     */
    @ResponseBody
    @RequestMapping("atMsgSend")
    public Map<String, Object> atMsgSend(@RequestBody NotiSendDTO body) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        Map<String, Object> reqMap = new HashMap<String, Object>();

        FingerTalk fingerTalk = new FingerTalk();
        fingerTalk.setUrl(atUrl);
        fingerTalk.setServiceKey(atServiceKey);

        LOGGER.debug("기관 {} 알림톡 발송", chaCd);

        try {
            List<String> atCusInfoList = body.getAtCusInfoList();
            List<String> notiMasCdList = body.getNotiMasCdList();
            String msgBody = body.getContents().replaceAll("<br>", "\r\n");
            String chaTelNo = body.getSendPhone();
            String templateCode = body.getTemplateCode();
            String msgType = body.getMsgType();

            for (int i = 0; i < atCusInfoList.size(); i++) {
                String atCusHpNo = atCusInfoList.get(i);
                String notiMasCd = notiMasCdList.get(i);

                if (notiMasCd.substring(0, 3).equals("fix")) {
                    notiMasCd = "00000000000000000000";
                }

                HashMap<String, Object> smap = new HashMap<String, Object>();
                smap.put("chaCd", chaCd);
                smap.put("msg", StringEscapeUtils.unescapeHtml4(msgBody));
                smap.put("cusHp", atCusHpNo);
                smap.put("notiMasCd", notiMasCd);

                String msg = varParamInfoService.setVarChange(smap); // 가변변수 치환

                final FingerTalk.Message sendMsg = fingerTalk.createMessage(atServiceKey, atCusHpNo, chaTelNo, msg, templateCode);      // 알람톡 발송

                // 알림톡 발송 성공 후 DB 저장
                HashMap<String, Object> imap = new HashMap<String, Object>();

                if (sendMsg != null) {
                    imap.put("cmid", sendMsg.getResultObject().getCmid());
                    imap.put("trxKey", sendMsg.getTrxKey());
                    imap.put("chaCd", chaCd);
                    imap.put("chaTelNo", chaTelNo.replaceAll("-", ""));
                    imap.put("cusTelNo", atCusHpNo.replaceAll("-", ""));
                    imap.put("sendMsg", msg);
                    imap.put("msgType", msgType);
                    imap.put("sendStatusCd", "0");
                    imap.put("sendResultCd", "0");
                    imap.put("notimasCd", notiMasCd);
                    imap.put("paymasCd", "");
                    imap.put("maker", chaCd);
                    imap.put("reportDate", "");

                    notiService.insertAtReq(imap);
                }
            }

            reqMap.put("retCode", "0000");
        } catch (Exception e) {
            LOGGER.info("알림톡 발송 실패");
            LOGGER.info(e.getMessage());

            reqMap.put("retCode", "0001");
        }

        return reqMap;
    }

    @ResponseBody
    @RequestMapping("sendAtList")
    public HashMap sendAtList(@RequestBody NotiSendDTO dto) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String chaCd = authentication.getName();
        LOGGER.debug("기관 {} 알림톡 발송이력", chaCd);

        final HashMap<String, Object> map = new HashMap<String, Object>();

        final HashMap<String, Object> cmap = new HashMap<String, Object>();
        cmap.put("chaCd", chaCd);
        cmap.put("stDt", dto.getStDt());
        cmap.put("edDt", dto.getEdDt());
        cmap.put("selGubn", dto.getSelGubn());
        cmap.put("keyword", dto.getKeyword());
        cmap.put("search_orderBy", dto.getSearch_orderBy());
        cmap.put("status", dto.getStatus());
        cmap.put("msgType", dto.getMsgType());

        try {
            // total count
            final int count = notiService.sendAtTotalCount(cmap);
            map.put("count", count);

            // 페이지 관련 설정
            final PageVO page = new PageVO(count, dto.getCurPage(), 10);
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            cmap.put("start", start);
            cmap.put("end", end);

            final List<NotiSendDTO> list = notiService.sendAtList(cmap);

            map.put("list", list);
            map.put("modalPager", page);
            map.put("modalNo", 3);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return map;
    }
}

