package com.finger.shinhandamoa.org.receiptmgmt.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finger.shinhandamoa.common.XlsxBuilder;
import com.finger.shinhandamoa.org.receiptmgmt.PayMgmtDAO;
import com.finger.shinhandamoa.org.receiptmgmt.ReceiptMgmtDAO;
import com.finger.shinhandamoa.org.receiptmgmt.dto.PayMgmtDTO;
import com.finger.shinhandamoa.util.dto.CodeDTO;

/**
 * 입금내역
 * @author suhlee
 *
 */
@Service
@Transactional
public class PayMgmtServiceImpl implements PayMgmtService {

	@Resource(name = "payMgmtDao")
	private PayMgmtDAO payMgmtDao;
	
	@Resource(name = "receiptMgmtDao")
    private ReceiptMgmtDAO receiptMgmtDao;
	
	final SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
	final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	/**
	 * 가상계좌 입금내역 카운트
	 */
	@Override
	public HashMap<String, Object> getPaymentCount(Map<String, Object> map) throws Exception {
		return payMgmtDao.getPaymentCount(map);
	}

	/**
	 * 가상계좌 입금내역 리스트
	 */
	@Override
	public List<PayMgmtDTO> getPaymentList(Map<String, Object> map) throws Exception {
		return payMgmtDao.getPaymentList(map);
	}

	@Override
	public HashMap<String, Object> getCardPayCount(HashMap<String, Object> map) throws Exception {
		return payMgmtDao.getCardPayCount(map);
	}

	@Override
	public List<PayMgmtDTO> getCardPayList(HashMap<String, Object> map) throws Exception {
		return payMgmtDao.getCardPayList(map);
	}

	/**
	 * 가상계좌 입금내역 엑셀다운-신규
	 */
	@Override
	public InputStream getPaymentHistoryExcel(Map<String, Object> params) throws Exception {
		XlsxBuilder xlsxBuilder = new XlsxBuilder();
		xlsxBuilder.newSheet("가상계좌 입금내역");
		
		// 온라인카드 입금내역
		List<PayMgmtDTO> paymentList = payMgmtDao.getPaymentList(params);
		// 고객구분 목록
		@SuppressWarnings("unchecked")
		List<CodeDTO> cdList = (List<CodeDTO>) params.get("cusGbList");
		
		// 헤더생성
		int columnNo = 0;
		xlsxBuilder.addHeader(0, columnNo++, "NO");
		xlsxBuilder.addHeader(0, columnNo++, "청구월");
		xlsxBuilder.addHeader(0, columnNo++, "입금일시");
		xlsxBuilder.addHeader(0, columnNo++, "고객명");
		xlsxBuilder.addHeader(0, columnNo++, "가상계좌번호");
		
		// 고객구분
		for (CodeDTO code : cdList) {
			xlsxBuilder.addHeader(0, columnNo++, code.getCodeName());
		}
		
		xlsxBuilder.addHeader(0, columnNo++, "입금액");
		xlsxBuilder.addHeader(0, columnNo++, "입금자명");
		xlsxBuilder.addHeader(0, columnNo++, "입금은행");
		
		int rows = 1;
		for (PayMgmtDTO data : paymentList) {
			columnNo = 0;
			xlsxBuilder.newDataRow();
			xlsxBuilder.addData(columnNo++, rows++);
			
			final Date date = yyyyMMddHHmmss.parse(data.getPayDay()+data.getPayTime());
			xlsxBuilder.addData(columnNo++, data.getMasMonth());
			xlsxBuilder.addData(columnNo++, sdf.format(date));
			
			xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusName()));
			xlsxBuilder.addData(columnNo++, data.getVaNo().substring(0,3) + "-" + data.getVaNo().substring(3,6) + "-" + data.getVaNo().substring(6));
			
			for (int n=0; n < cdList.size(); n++) {
				if (n == 0)	xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn1()));
				if (n == 1)	xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn2()));
				if (n == 2)	xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn3()));
				if (n == 3)	xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn4()));
			}
			
			xlsxBuilder.addData(columnNo++, NumberUtils.toLong(data.getRcpAmt(), 0));
			xlsxBuilder.addData(columnNo++, data.getRcpUserName());
			
			if(data.getBnkCd() != null && !data.getBnkCd().equals(" ")) {
				xlsxBuilder.addData(columnNo++, data.getBnkCd());
			}else {
				xlsxBuilder.addData(columnNo++, data.getFicd());
			}
		}
		
		final File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".tmp");
		xlsxBuilder.writeTo(new FileOutputStream(tempFile));
		return new FileInputStream(tempFile); 
	}
	
	/**
	 * 온라인카드 결제내역 엑셀다운-신규
	 */
	public InputStream getCardPayHistoryExcel(Map<String, Object> params) throws Exception {
		XlsxBuilder xlsxBuilder = new XlsxBuilder();
		xlsxBuilder.newSheet("온라인카드 결제내역");
		
		// 카드결제내역
		List<PayMgmtDTO> paymentList = payMgmtDao.getCardPayList((HashMap<String, Object>) params);
		
		// 고객구분 목록
		@SuppressWarnings("unchecked")
		List<CodeDTO> cdList = (List<CodeDTO>) params.get("cusGbList");
		
		// 헤더생성
		int columnNo = 0;
		xlsxBuilder.addHeader(0, columnNo++, "NO");
		xlsxBuilder.addHeader(0, columnNo++, "청구월");
		xlsxBuilder.addHeader(0, columnNo++, "고객명");

		// 고객구분
		for (CodeDTO code : cdList) {
			xlsxBuilder.addHeader(0, columnNo++, code.getCodeName());
		}
		
		xlsxBuilder.addHeader(0, columnNo++, "청구금액");
		xlsxBuilder.addHeader(0, columnNo++, "결제금액");
		xlsxBuilder.addHeader(0, columnNo++, "결제상태");
		xlsxBuilder.addHeader(0, columnNo++, "결제일시");
		
		// 데이터처리
		int rows = 1;
		for (PayMgmtDTO data : paymentList) {
			columnNo = 0;
			xlsxBuilder.newDataRow();
			xlsxBuilder.addData(columnNo++, rows++);
			xlsxBuilder.addData(columnNo++, data.getMasMonth());
			
			
			xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusName()));
			
			for (int n=0; n < cdList.size(); n++) {
				if (n == 0)	xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn1()));
				if (n == 1)	xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn2()));
				if (n == 2)	xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn3()));
				if (n == 3)	xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn4()));
			}
			
			xlsxBuilder.addData(columnNo++, NumberUtils.toLong(data.getPayItemAmt(), 0));
			xlsxBuilder.addData(columnNo++, NumberUtils.toLong(data.getRcpAmt(), 0));
			
			final String rcpMasStNm;
			if(data.getRcpMasSt().equals("PA03")) {
				rcpMasStNm = "승인";
    		}else {
    			rcpMasStNm = "취소";
    		}
			xlsxBuilder.addData(columnNo++, rcpMasStNm);
			
			final Date date = yyyyMMddHHmmss.parse(data.getPayDay()+data.getPayTime());
			xlsxBuilder.addData(columnNo++, sdf.format(date));
		}
		
		final File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".tmp");
		xlsxBuilder.writeTo(new FileOutputStream(tempFile));
		return new FileInputStream(tempFile); 
	}
	

    /**
     * 수납관리 > 항목별 입금내역 엑셀 다운로드 - 신규
     */
    @Override
    public InputStream getPayItemHistoryExcel(Map<String, Object> params) throws Exception {
    	XlsxBuilder xlsxBuilder = new XlsxBuilder();
		xlsxBuilder.newSheet("수납내역");
		
		// 고객구분 목록
		@SuppressWarnings("unchecked")
		List<CodeDTO> cdList = (List<CodeDTO>) params.get("cusGbList");
		
		// 항목별입금내역
		List<PayMgmtDTO> list = receiptMgmtDao.getPayItemExcelList((HashMap<String, Object>) params);
		
		// 헤더생성
		int columnNo = 0;
		xlsxBuilder.addHeader(0, columnNo++, "청구월");
		xlsxBuilder.addHeader(0, columnNo++, "입금일시");
		xlsxBuilder.addHeader(0, columnNo++, "고객명");
		xlsxBuilder.addHeader(0, columnNo++, "고객번호");
		xlsxBuilder.addHeader(0, columnNo++, "가상계좌번호");

		// 고객구분
		for (CodeDTO code : cdList) {
			xlsxBuilder.addHeader(0, columnNo++, code.getCodeName());
		}
		
		xlsxBuilder.addHeader(0, columnNo++, "청구항목명");
		xlsxBuilder.addHeader(0, columnNo++, "청구금액(원)");
		xlsxBuilder.addHeader(0, columnNo++, "비고");
		xlsxBuilder.addHeader(0, columnNo++, "입금금액(원)");
		xlsxBuilder.addHeader(0, columnNo++, "입금수단");
		if (params.get("adjAccYn").toString().equalsIgnoreCase("Y")) {
			xlsxBuilder.addHeader(0, columnNo++, "입금통장");
		}
		
		// 데이터처리
		int rows = 1;
		for (PayMgmtDTO data : list) {
			columnNo = 0;
			xlsxBuilder.newDataRow();
			xlsxBuilder.addData(columnNo++, data.getMasMonth());
			
			final Date date = yyyyMMddHHmmss.parse(data.getPayDay()+data.getPayTime());
			xlsxBuilder.addData(columnNo++, sdf.format(date));
			
			xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusName()));
			xlsxBuilder.addData(columnNo++, data.getCusKey());
			xlsxBuilder.addData(columnNo++, data.getVaNo());

			for (int n=0; n < cdList.size(); n++) {
				if (n == 0)	xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn1()));
				if (n == 1)	xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn2()));
				if (n == 2)	xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn3()));
				if (n == 3)	xlsxBuilder.addData(columnNo++, StringEscapeUtils.unescapeHtml4(data.getCusGubn4()));
			}
			
			xlsxBuilder.addData(columnNo++, data.getPayItemName());
			xlsxBuilder.addData(columnNo++, data.getNotiAmt());
			xlsxBuilder.addData(columnNo++, data.getPtrItemRemark());
			xlsxBuilder.addData(columnNo++, data.getPayAmt());
			xlsxBuilder.addData(columnNo++, data.getSveCd());
			
			if (params.get("adjAccYn").toString().equalsIgnoreCase("Y")) {
				xlsxBuilder.addData(columnNo++, data.getGrpadjName());
			}
		}
		
    	final File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".tmp");
		xlsxBuilder.writeTo(new FileOutputStream(tempFile));
		return new FileInputStream(tempFile);
    }
}
