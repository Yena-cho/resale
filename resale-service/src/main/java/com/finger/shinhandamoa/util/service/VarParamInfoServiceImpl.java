package com.finger.shinhandamoa.util.service;

import java.util.HashMap;

import javax.inject.Inject;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.util.dao.VarParamInfoDAO;

/**
 * @author  by puki
 * @date    2018. 5. 10.
 * @desc    최초생성
 * @version 
 * 
 */
@Service
public class VarParamInfoServiceImpl implements VarParamInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(VarParamInfoServiceImpl.class);
	
	@Inject
	private VarParamInfoDAO varParamInfoDao;

	public String setVarChange(HashMap<String, Object> map) throws Exception {
		
		String str = map.get("msg").toString();

		// 고객명
		if(str.contains("[#CUSNAME#]")) {		
			String cusName = varParamInfoDao.selCusName(map);
			if("".equals(cusName) || cusName == null) {
				cusName = "";
			}
			str = str.replace("[#CUSNAME#]", StringEscapeUtils.unescapeHtml4(cusName));
		}
		if(str.contains("[#고객명#]")) {		
			String cusName = varParamInfoDao.selCusName(map);
			if("".equals(cusName) || cusName == null) {
				cusName = "";
			}
			str = str.replace("[#고객명#]", StringEscapeUtils.unescapeHtml4(cusName));
		}
		if(str.contains("[$납부자명]")) {		
			String cusName = varParamInfoDao.selCusName(map);
			if("".equals(cusName) || cusName == null) {
				cusName = "";
			}
			str = str.replace("[$납부자명]", StringEscapeUtils.unescapeHtml4(cusName));
		}
		if(str.contains("[$NAME]")) {		
			String cusName = varParamInfoDao.selCusName(map);
			if("".equals(cusName) || cusName == null) {
				cusName = "";
			}
			str = str.replace("[$NAME]", StringEscapeUtils.unescapeHtml4(cusName));
		}
		// 청구월
		if(str.contains("[#MASMONTH#]")) { 	
			String masMonth = varParamInfoDao.selMasMonth(map);
			if("".equals(masMonth) || masMonth == null) {
				masMonth = "";
			}
			str = str.replace("[#MASMONTH#]", masMonth+" 월");
		}
		if(str.contains("[$청구월]")) { 	
			String masMonth = varParamInfoDao.selMasMonth(map);
			if("".equals(masMonth) || masMonth == null) {
				masMonth = "";
			}
			str = str.replace("[$청구월]", masMonth+" 월");
		}
		if(str.contains("[#청구월#]")) { 	
			String masMonth = varParamInfoDao.selMasMonth(map);
			if("".equals(masMonth) || masMonth == null) {
				masMonth = "";
			}
			str = str.replace("[#청구월#]", masMonth+" 월");
		}
		if(str.contains("[$PMONTH]")) { 	
			String masMonth = varParamInfoDao.selMasMonth(map);
			if("".equals(masMonth) || masMonth == null) {
				masMonth = "";
			}
			str = str.replace("[$PMONTH]", masMonth+" 월");
		}
		// 청구금액
		if(str.contains("[#PAYITEMAMT#]")) { 	
			String payItemAmt = String.valueOf(varParamInfoDao.selPayAmt(map));
			if("".equals(payItemAmt) || payItemAmt == null) {
				payItemAmt = "";
			}
			str = str.replace("[#PAYITEMAMT#]", payItemAmt);
		}
		if(str.contains("[#청구액#]")) { 	
			String payItemAmt = String.valueOf(varParamInfoDao.selPayAmt(map));
			if("".equals(payItemAmt) || payItemAmt == null) {
				payItemAmt = "";
			}
			str = str.replace("[#청구액#]", payItemAmt);
		}
		if(str.contains("[$청구금액]")) { 	
			String payItemAmt = String.valueOf(varParamInfoDao.selPayAmt(map));
			if("".equals(payItemAmt) || payItemAmt == null) {
				payItemAmt = "";
			}
			str = str.replace("[$청구금액]", payItemAmt);
		}
		if(str.contains("[$AMT]")) { 	
			String payItemAmt = String.valueOf(varParamInfoDao.selPayAmt(map));
			if("".equals(payItemAmt) || payItemAmt == null) {
				payItemAmt = "";
			}
			str = str.replace("[$AMT]", payItemAmt);
		}
		// 입금가능기간 
		if(str.contains("[#DEPOSITTIME#]")) { 	
			String insDate = varParamInfoDao.selInsDate(map);
			if("".equals(insDate) || insDate == null) {
				insDate = "";
			}
			str = str.replace("[#DEPOSITTIME#]", insDate);
		}
		if(str.contains("[#납부기한#]")) { 	
			String insDate = varParamInfoDao.selInsDate(map);
			if("".equals(insDate) || insDate == null) {
				insDate = "";
			}
			str = str.replace("[#납부기한#]", insDate);
		}
		// 모바일청구서 - URL확인
		if(str.contains("[#SMSNOTI#]")) {

			str = str.replace("[#SMSNOTI#]", "https://www.shinhandamoa.com/common/login#payer");
		}
		if(str.contains("[#모바일청구서#]")) { 	
			
			str = str.replace("[#모바일청구서#]", "https://www.shinhandamoa.com/common/login#payer");
		}
		// 납부계좌
		if(str.contains("[#PAYVANO#]")) { 	
			String vano = varParamInfoDao.selPayVano(map);
			if("".equals(vano) || vano == null) {
				vano = "";
			}
			str = str.replace("[#PAYVANO#]", "신한 " + vano);
		}
		if(str.contains("[#납부계좌#]")) { 	
			String vano = varParamInfoDao.selPayVano(map);
			if("".equals(vano) || vano == null) {
				vano = "";
			}
			str = str.replace("[#납부계좌#]", "신한 " + vano);
		}
		if(str.contains("[#납부계좌번호#]")) {
			String vano = varParamInfoDao.selPayVano(map);
			if("".equals(vano) || vano == null) {
				vano = "";
			}
			str = str.replace("[#납부계좌번호#]", "신한 " + vano);
		}
		if(str.contains("[$수납계좌번호]")) { 	
			String vano = varParamInfoDao.selPayVano(map);
			if("".equals(vano) || vano == null) {
				vano = "";
			}
			str = str.replace("[$수납계좌번호]", "신한 " + vano);
		}
		if(str.contains("[$ACCNO]")) { 	
			String vano = varParamInfoDao.selPayVano(map);
			if("".equals(vano) || vano == null) {
				vano = "";
			}
			str = str.replace("[$ACCNO]", "신한 " + vano);
		}
		// 미납액
		if(str.contains("[#DEFAULTAMT#]")) { 	
			String notAmt = String.valueOf(varParamInfoDao.selNonPay(map));
			if("".equals(notAmt) || notAmt == null) {
				notAmt = "";
			}
			str = str.replace("[#DEFAULTAMT#]", notAmt);
		}
		if(str.contains("[#미납액#]")) { 	
			String notAmt = String.valueOf(varParamInfoDao.selNonPay(map));
			if("".equals(notAmt) || notAmt == null) {
				notAmt = "";
			}
			str = str.replace("[#미납액#]", notAmt);
		}
		// 고지서용마감일
		if(str.contains("[#NOTIENDDATE#]")) { 	
			String printDate = varParamInfoDao.selPrintDate(map);
			if("".equals(printDate) || printDate == null) {
				printDate = "";
			}
			str = str.replace("[#NOTIENDDATE#]", printDate);
		}
		if(str.contains("[#고지서용마감일#]")) { 	
			String printDate = varParamInfoDao.selPrintDate(map);
			if("".equals(printDate) || printDate == null) {
				printDate = "";
			}
			str = str.replace("[#고지서용마감일#]", printDate);
		}
		if(str.contains("[$수납마감일]")) { 	
			String printDate = varParamInfoDao.selPrintDate(map);
			if("".equals(printDate) || printDate == null) {
				printDate = "";
			}
			str = str.replace("[$수납마감일]", printDate);
		}
		if(str.contains("[$LDATE]")) { 	
			String printDate = varParamInfoDao.selPrintDate(map);
			if("".equals(printDate) || printDate == null) {
				printDate = "";
			}
			str = str.replace("[$LDATE]", printDate);
		}
		// 고객구분
		if(str.contains("[#CUSGBN#]")) { 	
			String cusGubn = varParamInfoDao.selCusGubn(map);
			if("".equals(cusGubn) || cusGubn == null) {
				cusGubn = "";
			}
			str = str.replace("[#CUSGBN#]", StringEscapeUtils.unescapeHtml4(cusGubn));
		}
		if(str.contains("[#고객구분#]")) { 	
			String cusGubn = varParamInfoDao.selCusGubn(map);
			if("".equals(cusGubn) || cusGubn == null) {
				cusGubn = "";
			}
			str = str.replace("[#고객구분#]", StringEscapeUtils.unescapeHtml4(cusGubn));
		}
		if(str.contains("[#기관명#]")) {
			String chaName = varParamInfoDao.selChaInfo(map);
			if("".equals(chaName) || chaName == null) {
				chaName = "";
			}
			str = str.replace("[#기관명#]", chaName);
		}

		if(str.contains("[#기관대표전화번호#]")) {
			String chaTelNo = varParamInfoDao.selChaTel(map);
			if("".equals(chaTelNo) || chaTelNo == null) {
				chaTelNo = "";
			}
			str = str.replace("[#기관대표전화번호#]", chaTelNo);
		}
		if(str.contains("[#입금액#]")) {
			String payItemAmt = String.valueOf(varParamInfoDao.selAccPay(map));
			if("".equals(payItemAmt) || payItemAmt == null) {
				payItemAmt = "";
			}
			str = str.replace("[#입금액#]", payItemAmt);
		}

		return str;
	}
		
}
