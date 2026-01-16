package com.finger.shinhandamoa.sys.rcpMgmt.web;

import com.finger.shinhandamoa.common.PageBounds;
import com.finger.shinhandamoa.common.XlsxBuilder;
import com.finger.shinhandamoa.org.transformer.DownloadView;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysAdminClosingDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.service.SysAdminClosingService;
import com.finger.shinhandamoa.vo.PageVO;
import kr.co.finger.damoa.commons.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 마감
 *
 * @author wisehouse@finger.co.kr
 */
@Controller
public class SysAdminClosingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysAdminClosingController.class);

    @Autowired
    private SysAdminClosingService sysAdminClosingService;

    /**
     * 가상계좌 정산 화면
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "sys/rcpMgmt/va-calculate", method = RequestMethod.GET)
    public String getVirtualAccountCalculate() throws Exception {
        return "sys/rcp/rcpMgmt/va-calculate";
    }

    /**
     *  정산관리 > 가상계좌 정산 조회(Ajax)
     */
    @RequestMapping("/sys/rcpMgmt/getVirtualAccountCalculate")
    @ResponseBody
    public HashMap<String, Object> virtualAccountCalculate(@RequestBody SysAdminClosingDTO body) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();

            reqMap.put("startday"      , body.getStartDt());
            reqMap.put("endday"        , body.getEndDt());
            reqMap.put("chacd"         , body.getChaCd());
            reqMap.put("chaname"       , body.getChaName());
            reqMap.put("stList"        , body.getStList());
            reqMap.put("searchOrderBy" , body.getSearchOrderBy());

            List<SysAdminClosingDTO> countList = sysAdminClosingService.getVirtualAccountCalculateCount(reqMap);
            int totalCount = Integer.parseInt(countList.get(0).getTotalcnt());

            // 페이지 관련 설정
            PageVO page = new PageVO(totalCount, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();

            reqMap.put("start", start);
            reqMap.put("end", end);

            List<SysAdminClosingDTO> itemList = sysAdminClosingService.getVirtualAccountCalculateList(reqMap);

            map.put("totalItemCount", totalCount);
            map.put("itemCount", itemList.size());
            map.put("itemList", itemList);
            map.put("pager", page);

            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 정산관리 > 가상계좌 정산 > 엑셀다운로드
     */
    @ResponseBody
    @RequestMapping("/sys/rcpMgmt/getVirtualAccountCalculateExcel")
    public View virtualAccountCalculateExcel(HttpServletRequest request, HttpServletResponse response, SysAdminClosingDTO body, Model model) throws Exception {
        final XlsxBuilder xlsxBuilder = new XlsxBuilder();
        xlsxBuilder.newSheet("데이터");

        xlsxBuilder.addHeader(0, 1, 0, 0, "NO");
        xlsxBuilder.addHeader(0, 1, 1, 1, "입금일자");
        xlsxBuilder.addHeader(0, 1, 2, 2, "은행코드");
        xlsxBuilder.addHeader(0, 1, 3, 3, "기관코드");
        xlsxBuilder.addHeader(0, 1, 4, 4, "기관명");
        xlsxBuilder.addHeader(0, 1, 5, 5, "입금계좌번호");
        xlsxBuilder.addHeader(0, 1, 6, 6, "정산금액(원)");
        xlsxBuilder.addHeader(0, 1, 7, 7, "처리상태");
        xlsxBuilder.addHeader(0, 1, 8, 8, "처리일시");

        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();

            reqMap.put("startday"      , body.getSearchStartday());
            reqMap.put("endday"        , body.getSearchEndday());
            reqMap.put("chacd"         , body.getFchacd());
            reqMap.put("chaname"       , body.getFchaname());
            reqMap.put("stList"        , body.getStList());
            reqMap.put("searchOrderBy" , body.getSearchOrderBy());

            List<SysAdminClosingDTO> itemList = sysAdminClosingService.getVirtualAccountCalculateListExcel(reqMap);
            int i = 0;

            for (SysAdminClosingDTO each : itemList) {
                i++;
                xlsxBuilder.newDataRow();

                xlsxBuilder.addData(0, i);
                xlsxBuilder.addData(1, each.getRcpdate());
                xlsxBuilder.addData(2, each.getFgcd());
                xlsxBuilder.addData(3, each.getChaCd());
                xlsxBuilder.addData(4, each.getChaName());
                xlsxBuilder.addData(5, each.getSettleaccno());
                xlsxBuilder.addData(6, each.getSettleamount());

                String settlestatus = "";

                if(each.getSettlestatus().equals("W")){
                    settlestatus = "대기";
                }else if(each.getSettlestatus().equals("S")){
                    settlestatus = "성공";
                }else if(each.getSettlestatus().equals("F")){
                    settlestatus = "실패";
                }else{
                    settlestatus = "";
                }

                xlsxBuilder.addData(7, settlestatus);
                xlsxBuilder.addData(8, each.getSettledate());
            }
        } catch(Exception e) {
            LOGGER.error(e.getMessage());
        }
        
        final File file = File.createTempFile("VirtualAccountCalculate_", ".tmp");
        xlsxBuilder.writeTo(new FileOutputStream(file));

        DownloadView downloadView = new DownloadView("가상계좌 정산_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx", new FileInputStream(file));
        downloadView.setContentType(DownloadView.CONTENT_TYPE_XLSX);

        return downloadView;
    }

    /**
     *  정산관리 > 가상계좌 정산 조회 > 결과처리 > 성공, 실패
     */
    @RequestMapping("/sys/rcpMgmt/updateVirtualAccountResult")
    @ResponseBody
    public HashMap<String, Object> updateVirtualAccountResult(@RequestBody SysAdminClosingDTO body) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            map.put("settleseq", body.getSettleseq());
            map.put("settlestatus", body.getSettlestatus());

            sysAdminClosingService.updateVirtualAccountSettleMas(map);
            List<SysAdminClosingDTO> masList = sysAdminClosingService.getVirtualAccountSettleMas(map);

            if(body.getSettlestatus().equals("F")){
                map.put("resultmsg", body.getResultmsg());
            }

            map.put("rcpdate", masList.get(0).getRcpdate());
            map.put("fgcd", masList.get(0).getFgcd());
            map.put("chacd", masList.get(0).getChaCd());
            map.put("chaname", masList.get(0).getChaName());
            map.put("settledate", masList.get(0).getSettledate());
            map.put("settlestatus", masList.get(0).getSettlestatus());
            map.put("settleamount", masList.get(0).getSettleamount());
            map.put("settleaccno", masList.get(0).getSettleaccno());
            map.put("adjfiregkey", masList.get(0).getAdjfiregkey());
            map.put("fingerfeepayty", masList.get(0).getFingerfeepayty());
            map.put("regname", masList.get(0).getRegname());
            map.put("settlebankcd", masList.get(0).getSettlebankcd());
            map.put("settleseq", masList.get(0).getSettleseq());

            sysAdminClosingService.insertVirtualAccountSettleDet(map);

            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 정산관리 > 가상계좌 정산 > 이력보기
     */
    @RequestMapping("/sys/rcpMgmt/getVirtualAccountSettleDet")
    @ResponseBody
    public HashMap<String, Object> virtualAccountSettleDet(@RequestBody SysAdminClosingDTO body) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            HashMap<String, Object> reqMap = new HashMap<String, Object>();

            reqMap.put("settleseq" , body.getSettleseq());

            List<SysAdminClosingDTO> countList = sysAdminClosingService.getVirtualAccountSettleDetCount(reqMap);
            int totalCount = Integer.parseInt(countList.get(0).getTotalcnt());

            // 페이지 관련 설정
            PageVO page = new PageVO(totalCount, body.getCurPage(), body.getPageScale());
            int start = page.getPageBegin();
            int end = page.getPageEnd();

            reqMap.put("start", start);
            reqMap.put("end", end);

            List<SysAdminClosingDTO> itemList = sysAdminClosingService.getVirtualAccountSettleDetList(reqMap);

            map.put("totalItemCount", totalCount);
            map.put("itemCount", itemList.size());
            map.put("itemList", itemList);
            map.put("pager", page);

            map.put("retCode", "0000");
            map.put("retMsg", "정상");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());

            map.put("retCode", "9999");
            map.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return map;
    }

    /**
     * 월 마감 화면
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "sys/rcpMgmt/monthly-close", method = RequestMethod.GET)
    public String getMonthlyClosing() throws Exception {
        return "sys/rcp/rcpMgmt/monthly-close";
    }

    /**
     * 월마감 내역 조회
     */
    @RequestMapping(value = "sys/rest/rcpMgmt/monthly-close", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap monthCloseListAjax(@RequestParam(value = "month", defaultValue = "") String month,
                                       @RequestParam(value = "clientId", defaultValue = "") String clientId,
                                       @RequestParam(value = "clientName", defaultValue = "") String clientName,
                                       @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                       @RequestParam(value = "orderBy", defaultValue = "") String orderBy) throws Exception {
        final ModelMap modelMap = new ModelMap();

        try {
            final int totalItemCount = sysAdminClosingService.countMonthlySettle(month, clientId, clientName);
            final List<Map<String, Object>> itemList = sysAdminClosingService.getMonthlySettleList(month, clientId, clientName, orderBy, new PageBounds(pageNo, pageSize));

            modelMap.put("totalItemCount", totalItemCount);
            modelMap.put("itemCount", itemList.size());
            modelMap.put("itemList", itemList);

            modelMap.put("retCode", "0000");
            modelMap.put("retMsg", "정상");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());

            modelMap.put("retCode", "9999");
            modelMap.put("retMsg", "처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.");
        }

        return modelMap;
    }

    /**
     * 세금계산서 발행 엑셀 파일 다운로드
     */
    @RequestMapping(value = "/sys/rest/rcpMgmt/invoice/download", method = RequestMethod.GET)
    public View downloadInovice(@RequestParam(value = "month", defaultValue = "") String month,
                                @RequestParam(value = "clientId", defaultValue = "") String clientId,
                                @RequestParam(value = "clientName", defaultValue = "") String clientName,
                                @RequestParam(value = "orderBy", defaultValue = "") String orderBy) throws Exception {
        final XlsxBuilder xlsxBuilder = new XlsxBuilder();
        xlsxBuilder.newSheet("데이터");

        xlsxBuilder.addHeader(0, 0, "일자(8)");
        xlsxBuilder.addHeader(0, 1, "매출/매입구분(2)");
        xlsxBuilder.addHeader(0, 2, "거래처코드(30)");
        xlsxBuilder.addHeader(0, 3, "거래처명(100)");
        xlsxBuilder.addHeader(0, 4, "매출/매입계정코드(8)");
        xlsxBuilder.addHeader(0, 5, "공급가액(16)");
        xlsxBuilder.addHeader(0, 6, "부가세(16)");
        xlsxBuilder.addHeader(0, 7, "수금/지급구분(30)");
        xlsxBuilder.addHeader(0, 8, "적요코드(2)");
        xlsxBuilder.addHeader(0, 9, "적요(200)");
        xlsxBuilder.addHeader(0, 10, "부서코드(14)");
        xlsxBuilder.addHeader(0, 11, "프로젝트코드(14)");
        xlsxBuilder.addHeader(0, 12, "ecount");
        xlsxBuilder.addHeader(0, 13, "승인번호");
        xlsxBuilder.addHeader(0, 14, "발행일자");

        try {
            final int pageSize = 200;
            for (int pageNo = 1; ; pageNo++) {
                final List<Map<String, Object>> itemList = sysAdminClosingService.getMonthlySettleList(month, clientId, clientName, orderBy, new PageBounds(pageNo, pageSize));
                if (itemList.isEmpty()) {
                    break;
                }

                for (Map<String, Object> each : itemList) {
                    String businessRegisterNo = String.valueOf(each.get("CHAOFFNO"));
                    Number notifee = (Number) each.get("NOTIFEE");
                    Number smsFee = (Number) each.get("SMSFEE");
                    Number lmsFee = (Number) each.get("LMSFEE");
                    Number prnFee = (Number) each.get("PRNFEE");
                    Number atFree = (Number) each.get("ATFEE");
                    long fee = notifee.longValue() + smsFee.longValue() + lmsFee.longValue() + prnFee.longValue() + atFree.longValue();
                    if (fee == 0) {
                        continue;
                    }

                    xlsxBuilder.newDataRow();
                    xlsxBuilder.addData(0, StringUtils.EMPTY);
                    xlsxBuilder.addData(1, "11");
                    xlsxBuilder.addData(2, businessRegisterNo);
                    xlsxBuilder.addData(3, String.valueOf(each.get("CHANAME")));
                    xlsxBuilder.addData(4, StringUtils.EMPTY);
                    xlsxBuilder.addData(5, Math.round(Double.valueOf(fee).doubleValue() * 10 / 11));
                    xlsxBuilder.addData(6, Math.round(Double.valueOf(fee).doubleValue() / 11));
                    xlsxBuilder.addData(7, StringUtils.EMPTY);
                    xlsxBuilder.addData(8, StringUtils.EMPTY);
                    xlsxBuilder.addData(9, StringUtils.EMPTY);
                    xlsxBuilder.addData(10, "1801204020");
                    xlsxBuilder.addData(11, "17072301700");
                    xlsxBuilder.addData(12, "ecount");
                    xlsxBuilder.addData(13, StringUtils.EMPTY);
                    xlsxBuilder.addData(14, StringUtils.EMPTY);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        final File file = File.createTempFile("Invoice_", ".tmp");
        xlsxBuilder.writeTo(new FileOutputStream(file));

        final FileInputStream inputStream = new FileInputStream(file);
        
        final DownloadView downloadView = new DownloadView("세금계산서_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx", inputStream);
        downloadView.setContentType(DownloadView.CONTENT_TYPE_XLSX);
        return downloadView;
    }

    /**
     * 월마감 내역 엑셀다운로드
     */
    @RequestMapping(value = "/sys/rest/rcpMgmt/monthly-close/download", method = RequestMethod.GET)
    public View downloadMonthlyClose(@RequestParam(value = "month", defaultValue = "") String month,
                                     @RequestParam(value = "clientId", defaultValue = "") String clientId,
                                     @RequestParam(value = "clientName", defaultValue = "") String clientName,
                                     @RequestParam(value = "orderBy", defaultValue = "") String orderBy) throws Exception {
        final XlsxBuilder xlsxBuilder = new XlsxBuilder();
        xlsxBuilder.newSheet("데이터");

        xlsxBuilder.addHeader(0, 1, 0, 0, "마감월");
        xlsxBuilder.addHeader(0, 1, 1, 1, "은행코드");
        xlsxBuilder.addHeader(0, 1, 2, 2, "기관코드");
        xlsxBuilder.addHeader(0, 1, 3, 3, "기관명");
        xlsxBuilder.addHeader(0, 0, 4, 6, "청구");
        xlsxBuilder.addHeader(0, 0, 7, 11, "수납");
        xlsxBuilder.addHeader(0, 0, 12, 15, "문자메시지");
        /*xlsxBuilder.addHeader(0, 0, 15, 16, "알림톡");
        xlsxBuilder.addHeader(0, 0, 17, 18, "고지서출력의뢰");*/

        xlsxBuilder.addHeader(1, 1, 4, 4, "금액");
        xlsxBuilder.addHeader(1, 1, 5, 5, "건수");
        xlsxBuilder.addHeader(1, 1, 6, 6, "수수료");
        xlsxBuilder.addHeader(1, 1, 7, 7, "금액");
        xlsxBuilder.addHeader(1, 1, 8, 8, "건수");
        xlsxBuilder.addHeader(1, 1, 9, 9, "수수료");
        xlsxBuilder.addHeader(1, 1, 10, 10, "은행수수료");
        xlsxBuilder.addHeader(1, 1, 11, 11, "핑거수수료");
        xlsxBuilder.addHeader(1, 1, 12, 12, "SMS건수");
        xlsxBuilder.addHeader(1, 1, 13, 13, "SMS금액");
        xlsxBuilder.addHeader(1, 1, 14, 14, "LMS건수");
        xlsxBuilder.addHeader(1, 1, 15, 15, "LMS금액");
        /*xlsxBuilder.addHeader(1, 1, 15, 15, "건수");
        xlsxBuilder.addHeader(1, 1, 16, 16, "금액");
        xlsxBuilder.addHeader(1, 1, 17, 17, "출력건수");
        xlsxBuilder.addHeader(1, 1, 18, 18, "금액");*/

        try {
            final int pageSize = 200;
            for (int pageNo = 1; ; pageNo++) {
                final List<Map<String, Object>> itemList = sysAdminClosingService.getMonthlySettleList(month, clientId, clientName, orderBy, new PageBounds(pageNo, pageSize));
                if (itemList.isEmpty()) {
                    break;
                }

                for (Map<String, Object> each : itemList) {
                    xlsxBuilder.newDataRow();

                    xlsxBuilder.addData(0, String.valueOf(each.get("month")));
                    xlsxBuilder.addData(1, String.valueOf(each.get("chacd")));
                    xlsxBuilder.addData(2, String.valueOf(each.get("fgcd")));
                    xlsxBuilder.addData(3, String.valueOf(each.get("chaname")));
                    xlsxBuilder.addData(4, String.valueOf(each.get("notiamt")));
                    xlsxBuilder.addData(5, String.valueOf(each.get("noticnt")));
                    xlsxBuilder.addData(6, String.valueOf(each.get("notifee")));
                    xlsxBuilder.addData(7, String.valueOf(each.get("rcpamt")));
                    xlsxBuilder.addData(8, String.valueOf(each.get("rcpcnt")));
                    xlsxBuilder.addData(9, String.valueOf(each.get("rcpfee")));
                    xlsxBuilder.addData(10, String.valueOf(each.get("rcpbnkfee")));
                    xlsxBuilder.addData(11, String.valueOf(each.get("rcpfingerfee")));
                    xlsxBuilder.addData(12, String.valueOf(each.get("smscnt")));
                    xlsxBuilder.addData(13, String.valueOf(each.get("smsfee")));
                    xlsxBuilder.addData(14, String.valueOf(each.get("lmscnt")));
                    xlsxBuilder.addData(15, String.valueOf(each.get("lmsfee")));
                    /*xlsxBuilder.addData(15, String.valueOf(each.get("ATCNT")));
                    xlsxBuilder.addData(16, String.valueOf(each.get("ATFEE")));
                    xlsxBuilder.addData(17, String.valueOf(each.get("PRNCNT")));
                    xlsxBuilder.addData(18, String.valueOf(each.get("PRNFEE")));*/
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        final File file = File.createTempFile("MonthlyClose_", ".tmp");
        xlsxBuilder.writeTo(new FileOutputStream(file));

        DownloadView downloadView = new DownloadView("월 마감_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx", new FileInputStream(file));
        downloadView.setContentType(DownloadView.CONTENT_TYPE_XLSX);
        return downloadView;
    }

    /**
     * 일 마감 화면
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "sys/rcpMgmt/daily-close", method = RequestMethod.GET)
    public String getDailyClosing() throws Exception {
        return "sys/rcp/rcpMgmt/daily-close";
    }
}
