package com.finger.shinhandamoa.sys.cashreceipt.service;

import com.finger.shinhandamoa.common.ListResult;
import com.finger.shinhandamoa.common.PageBounds;
import com.finger.shinhandamoa.common.XlsxBuilder;
import com.finger.shinhandamoa.data.table.mapper.*;
import com.finger.shinhandamoa.data.table.model.*;
import com.finger.shinhandamoa.sys.addServiceMgmt.dao.AddServiceCashDAO;
import com.finger.shinhandamoa.sys.addServiceMgmt.dto.XCashmasDTO;
import com.finger.shinhandamoa.sys.cashreceipt.mapper.CashReceiptMapper;
import kr.co.finger.shinhandamoa.domain.model.CashReceiptDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 현금영수증 서비스
 *
 * @author wisehouse@finger.co.kr
 */
@Service("sys-cash-receipt-service")
public class CashReceiptService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CashReceiptService.class);

	@Resource(name = "addServiceCashDao")
	private AddServiceCashDAO addServiceCashDao;

	@Autowired
	private CashReceiptMapper cashReceiptMapper;

	@Autowired
	private ChaMapper chaMapper;

	@Autowired
	private CustomerMasterMapper customerMasterMapper;

	@Autowired
	private CashReceiptMasterMapper cashReceiptMasterMapper;

	@Autowired
	private ReceiptMasterMapper receiptMasterMapper;

	@Autowired
	private ReceiptDetailsMapper receiptDetailsMapper;

	@Autowired
    private NoticeMasterMapper noticeMasterMapper;

	@Autowired
	private NoticeDetailsTypeMapper noticeDetailsTypeMapper;

	@Autowired
	private CashHstMapper cashHstMapper;

	/**
	 * 현금영수증 이용내역 조회
	 */
	public List<XCashmasDTO> getCashHistory(HashMap<String, Object> map) throws Exception {
		return addServiceCashDao.getCashHistory(map);
	}

	/**
	 * 현금영수증 이용내역 카운트
	 */
	public int getCashHistoryCount(HashMap<String, Object> map) throws Exception {
		return addServiceCashDao.getCashHistoryCount(map);
	}

	public ListResult<ClientWithCashReceiptStatusDTO> getStatusByClient(String startDate, String endDate, String clientId, String clientName, String status, String enableCashReceipt, String enableAutomaticCashReceipt, String orderBy, PageBounds pageBounds, String mandRcpYn) {
		final String orderByClause;
		switch (StringUtils.defaultString(orderBy)) {
			case "clientId":
				orderByClause = "A.CHACD ASC";
				break;
			case "clientName":
				orderByClause = "A.CHANAME ASC, A.CHACD ASC";
				break;
			default:
				orderByClause = "A.CHACD ASC";
				break;
		}
		final List<ClientWithCashReceiptStatusDTO> itemList = cashReceiptMapper.getClientWithCashReceiptStatusList(startDate, endDate, clientId, clientName, status, enableCashReceipt, enableAutomaticCashReceipt, orderByClause, pageBounds.toRowBounds(), mandRcpYn);
		final long totalItemCount = cashReceiptMapper.countClientWithCashReceiptStatus(startDate, endDate, clientId, clientName, status, enableCashReceipt, enableAutomaticCashReceipt, mandRcpYn);

		return new ListResult<>(totalItemCount, itemList);
	}

	public ListResult<ReceiptWithCashReceiptStatusDTO> getStatusByReceipt(String startDate, String endDate, String startDate2, String endDate2, String chaCd, String chaName, String sveCd, String createCashReceipt, String notIssuedErrorCashReceipt, String orderBy, PageBounds pageBounds) {
		final String orderByClause;
		switch (StringUtils.defaultString(orderBy)) {
			case "regDt":
				orderByClause = "A.REGDT DESC, B.CHACD ASC";
				break;
			case "chacd":
				orderByClause = "B.CHACD ASC, A.REGDT DESC";
				break;
			default:
				orderByClause = "A.REGDT DESC, B.CHACD ASC";
				break;
		}

		final List<ReceiptWithCashReceiptStatusDTO> itemList = cashReceiptMapper.getReceiptWithCashReceiptStatusList(startDate, endDate, startDate2, endDate2, chaCd, chaName, sveCd, createCashReceipt, notIssuedErrorCashReceipt, orderByClause, pageBounds.toRowBounds());
		final long totalItemCount = cashReceiptMapper.countReceiptWithCashReceiptStatus(startDate, endDate, startDate2, endDate2, chaCd, chaName, sveCd, createCashReceipt, notIssuedErrorCashReceipt);

		return new ListResult<>(totalItemCount, itemList);
	}

	public ListResult<IssueWithCashReceiptStatusDTO> getStatusByIssueCashReceipt(String startDate, String endDate, String startDate2, String endDate2, String startDate3, String endDate3, String chaCd, String chaName, String sveCd, String createErrorCashReceipt, String duplicationIssuedErrorCashReceipt, String notIssuedErrorCashReceipt, String orderBy, PageBounds pageBounds) {
		final String orderByClause;
		switch (StringUtils.defaultString(orderBy)) {
			case "regDt":
				orderByClause = "A.REGDT DESC, B.CHACD ASC";
				break;
			case "chacd":
				orderByClause = "B.CHACD ASC, A.REGDT DESC";
				break;
			default:
				orderByClause = "A.REGDT DESC, B.CHACD ASC";
				break;
		}

		final List<IssueWithCashReceiptStatusDTO> itemList = cashReceiptMapper.getIssueWithCashReceiptStatusList(startDate, endDate, startDate2, endDate2, startDate3, endDate3, chaCd, chaName, sveCd, createErrorCashReceipt, duplicationIssuedErrorCashReceipt, notIssuedErrorCashReceipt, orderByClause, pageBounds.toRowBounds());
		final long totalItemCount = cashReceiptMapper.countIssueWithCashReceiptStatus(startDate, endDate, startDate2, endDate2, startDate3, endDate3, chaCd, chaName, sveCd, createErrorCashReceipt, duplicationIssuedErrorCashReceipt, notIssuedErrorCashReceipt);

		return new ListResult<>(totalItemCount, itemList);
	}

    public ListResult<DetailPopUpCashmasCdDTO> datailPopUpCashmasCd(String cashmasCd) {
        final List<DetailPopUpCashmasCdDTO> itemList = cashReceiptMapper.datailPopUpCashmasCd(cashmasCd);
        return new ListResult<>(0, itemList);
    }

	public ListResult<DetailPopUpChaNameDTO> datailPopUpChaName(String chaCd) {
		final List<DetailPopUpChaNameDTO> itemList = cashReceiptMapper.datailPopUpChaName(chaCd);
		return new ListResult<>(0, itemList);
	}

	public ListResult<DetailPopUpRegDtDTO> datailPopUpRegDt(String rcpmasCd) {
		final List<DetailPopUpRegDtDTO> itemList = cashReceiptMapper.datailPopUpRegDt(rcpmasCd);
		return new ListResult<>(0, itemList);
	}

	/**
	 *
	 * @param chaCd
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public long createWithClientAndPeriod(String chaCd, String paymentMedia, String startDate, String endDate) {
		long count = cashReceiptMapper.countNoCashReceiptReceipt(chaCd, paymentMedia, startDate, endDate);
		LOGGER.info("총 {}건", count);

		final int pageSize = 100;
		final RowBounds rowBounds = new RowBounds(0, pageSize);
		while(true) {
			final List<String> receiptList = cashReceiptMapper.selectNoCashReceiptReceipt(chaCd, paymentMedia, startDate, endDate, rowBounds);
			if(receiptList.isEmpty()) {
				break;
			}

			for (String each : receiptList) {
				final List<CashReceiptMaster> itemList = createCashReceiptWithReceipt(each);
                for (CashReceiptMaster eachCashReceipt : itemList) {
                    cashReceiptMasterMapper.insert(eachCashReceipt);
                }
			}
		}

		return count;
	}

	/**
	 *
	 * @param chaCd
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public long requestWithClientAndPeriod(String chaCd, String paymentMedia, String startDate, String endDate) {
		long count = cashReceiptMapper.countReadyCashReceipt(chaCd, paymentMedia, startDate, endDate);
		LOGGER.info("총 {}건", count);

		final int pageSize = 100;
		long actualCount = 0;
		for(int i=1;; i++) {
			PageBounds pageBounds = new PageBounds(i, pageSize);
			final List<String> cashReceiptList = cashReceiptMapper.selectReadyCashReceipt(chaCd, paymentMedia, startDate, endDate, pageBounds.toRowBounds());
			if(cashReceiptList.isEmpty()) {
				break;
			}


			for (String each : cashReceiptList) {
				try {
					LOGGER.info("[현금영수증#{}]", each);
					final CashReceiptMaster cashReceiptMaster = cashReceiptMasterMapper.selectByPrimaryKey(each);
					if(cashReceiptMaster == null) {
                        LOGGER.warn("데이터 없음");
                    }
					if(StringUtils.isBlank(cashReceiptMaster.getCusoffno())) {
                        LOGGER.debug("식별번호 없음");
                        continue;
                    }

					if(cashReceiptMaster.getRcpamt() < 1) {
                        continue;
                    }

					if(StringUtils.isNotBlank(cashReceiptMaster.getJob())) {
                        continue;
                    }

                    String sendDateString = new SimpleDateFormat("yyyyMMdd").format(DateUtils.addDays(new Date(), 1));

					cashReceiptMaster.setJob(CashReceiptDO.REQUEST_TYPE_ISSUE);
                    cashReceiptMaster.setCashmasst(CashReceiptDO.STATUS_ST05);
					cashReceiptMaster.setCusoffno(StringUtils.trim(cashReceiptMaster.getCusoffno()));
					cashReceiptMaster.setConfirm(getConfirm("", StringUtils.trim(cashReceiptMaster.getCusoffno())));
					cashReceiptMaster.setCustype(getCusType("", StringUtils.trim(cashReceiptMaster.getCusoffno())));
					cashReceiptMaster.setSenddt(sendDateString);
					cashReceiptMaster.setMakedt(new Date());

					cashReceiptMasterMapper.updateByPrimaryKey(cashReceiptMaster);

					//현금영수증 이용내역 이력 등록 (관리자 일괄)
					CashHst cashHst = new CashHst();
					cashHst.setChacd(cashReceiptMaster.getChacd());
					cashHst.setCashmascd(cashReceiptMaster.getCashmascd());
					cashHst.setRequestUser("damoaadm");
					cashHst.setRequestDate(new Date());
					cashHst.setRequestTypeCd("I");
					cashHst.setResultCd("3");
					cashHst.setCusoffno(cashReceiptMaster.getCusoffno());
					cashHst.setRcpamt(cashReceiptMaster.getRcpamt());
					cashHst.setTax(cashReceiptMaster.getTax());
					cashHstMapper.insertSelective(cashHst);

					actualCount++;
				} catch (Exception e) {
					if(LOGGER.isDebugEnabled()) {
					    LOGGER.error(e.getMessage(), e);
					} else {
					    LOGGER.error(e.getMessage());
					}

				}
			}
		}

		return actualCount;
	}

	private String getConfirm(String confirm, String identityNo) {
		if(StringUtils.isNotBlank(confirm)) {
			return confirm;
		}

		if (StringUtils.length(identityNo) == 10 && !identityNo.startsWith("0")) {
			// 사업자 등록번호
			return "21";
		} else if (StringUtils.length(identityNo) >= 16) {
			// 카드 번호
			return "12";
		} else {
			// 전화번호
			return "11";
		}
	}

	private String getCusType(String cusType, String identityNo) {
		if(StringUtils.isNotBlank(cusType)) {
			return cusType;
		}

		if (StringUtils.length(identityNo) == 10 && !identityNo.startsWith("0")) {
			// 사업자 등록번호
			return "2";
		} else {
			return "1";
		}
	}

	private String getReceiptDetailsClientIdentityNo(Cha cha, NoticeDetailsType noticeDetailsType) {
        final String clientIdentityNo = cha.getChaoffno();
        if(noticeDetailsType == null) {
            return clientIdentityNo;
        }

        return noticeDetailsType.getChaoffno();
    }

	private List<CashReceiptMaster> createCashReceiptWithReceipt(String rcpMasCd) {
	    // 결제 조회
		final ReceiptMaster receiptMaster = receiptMasterMapper.selectByPrimaryKey(rcpMasCd);

		// 결제 상세 조회
		final ReceiptDetailsExample receiptDetailsExample = new ReceiptDetailsExample();
		receiptDetailsExample.createCriteria().andRcpmascdEqualTo(rcpMasCd);
		final List<ReceiptDetails> receiptDetailsList = receiptDetailsMapper.selectByExample(receiptDetailsExample);

        final Cha cha = chaMapper.selectByPrimaryKey(receiptMaster.getChacd());

		// 사업자 등록번호별 결제금액 조회
        final Map<String, Long> amountMap = new HashMap<>();
        for (ReceiptDetails each : receiptDetailsList) {
            final NoticeDetailsType noticeDetailsType = noticeDetailsTypeMapper.selectByPrimaryKey(each.getPayitemcd());

            final String eachIdentityNo = getReceiptDetailsClientIdentityNo(cha, noticeDetailsType);

            if(!amountMap.containsKey(eachIdentityNo)) {
                amountMap.put(eachIdentityNo, 0L);
            }

            if(noticeDetailsType != null && StringUtils.equals(noticeDetailsType.getRcpitemyn(), "N")) {
                continue;
            }

            long amount = amountMap.get(eachIdentityNo);
            amount += each.getPayitemamt();
            amountMap.put(eachIdentityNo, amount);
        }
        
        if(receiptDetailsList.isEmpty()) {
        	amountMap.put(cha.getChaoffno(), receiptMaster.getRcpamt());
		}

		final CustomerMaster customerMaster = customerMasterMapper.selectByPrimaryKey(receiptMaster.getVano());
        final NoticeMaster noticeMaster = noticeMasterMapper.selectByPrimaryKey(receiptMaster.getNotimascd());

		if("Y".equals(cha.getMandRcpYn()) && StringUtils.isBlank(customerMaster.getCusoffno())) {
			customerMaster.setCusoffno("0100001234");
			customerMaster.setConfirm("11");
			customerMaster.setCustype("1");
		}

        // 현금영수증 발행번호
        String customerIdentityNo = customerMaster != null ? customerMaster.getCusoffno() : noticeMaster.getCusoffno();
        customerIdentityNo = StringUtils.defaultString(customerIdentityNo);
        customerIdentityNo = customerIdentityNo.trim();

        final String confirm = getConfirm(customerMaster == null ? StringUtils.EMPTY : customerMaster.getConfirm(), customerIdentityNo);
        final String cusType = getCusType(customerMaster == null ? StringUtils.EMPTY : customerMaster.getCustype(), customerIdentityNo);

        final List<CashReceiptMaster> itemList = new ArrayList<>();
        for (Map.Entry<String, Long> eachEntry : amountMap.entrySet()) {
            final String clientIdentityNo = eachEntry.getKey();
            final long receiptAmount = eachEntry.getValue();

            final long tax;
            if(StringUtils.equals(cha.getNotaxyn(), "N")) {
                tax = 0;
            } else {
                tax = Double.valueOf(Math.round(receiptAmount/ 11.0)).longValue();
            }

            final CashReceiptMaster cashReceiptMaster = new CashReceiptMaster();
            cashReceiptMaster.setRcpmascd(receiptMaster.getRcpmascd());
            cashReceiptMaster.setChacd(receiptMaster.getChacd());
            cashReceiptMaster.setChaoffno(clientIdentityNo);
            cashReceiptMaster.setCusoffno(customerIdentityNo);
            cashReceiptMaster.setConfirm(confirm);
            cashReceiptMaster.setCustype(cusType);
            cashReceiptMaster.setAmtchkty(cha.getAmtchkty());
            cashReceiptMaster.setNotaxyn(cha.getNotaxyn());
            cashReceiptMaster.setRcpreqty(cha.getRcpreqty());
            cashReceiptMaster.setChatrty(cha.getChatrty());
            cashReceiptMaster.setRcpamt(receiptAmount);
            cashReceiptMaster.setTip(0L);
            cashReceiptMaster.setTax(tax);
            cashReceiptMaster.setCashmasst(CashReceiptDO.STATUS_ST02);
            cashReceiptMaster.setMakedt(new Date());
            cashReceiptMaster.setMaker("damoaadm");
            cashReceiptMaster.setRegdt(new Date());

            itemList.add(cashReceiptMaster);
        }

		return itemList;
	}

	public InputStream getStatusByClientExcel(String startDate, String endDate, String clientId, String clientName, String status, String enableCashReceipt, String enableAutomaticCashReceipt, String orderBy, String mandRcpYn) throws IOException {
		final String orderByClause;
		switch (StringUtils.defaultString(orderBy)) {
			case "clientId":
				orderByClause = "A.CHACD ASC";
				break;
			case "clientName":
				orderByClause = "A.CHANAME ASC, A.CHACD ASC";
				break;
			default:
				orderByClause = "A.CHACD ASC";
				break;
		}

		XlsxBuilder xlsxBuilder = new XlsxBuilder();
		xlsxBuilder.newSheet("데이터");
		xlsxBuilder.addHeader(0, 1, 0, 0, "기관코드");
		xlsxBuilder.addHeader(0, 1, 1, 1, "기관명");
		xlsxBuilder.addHeader(0, 1, 2, 2, "상태");
		xlsxBuilder.addHeader(0, 1, 3, 3, "현금영수증 발급여부");
		xlsxBuilder.addHeader(0, 1, 4, 4, "현금영수증 자동발급여부");
		xlsxBuilder.addHeader(0, 1, 5, 5, "현금영수증 의무발행업체여부");
		xlsxBuilder.addHeader(0, 0, 6, 9, "가상계좌");
		xlsxBuilder.addHeader(0, 0, 10, 13, "창구현금");
		xlsxBuilder.addHeader(0, 0, 14, 17, "온라인현금");

		xlsxBuilder.addHeader(1, 1, 6, 6, "미생성");
		xlsxBuilder.addHeader(1, 1, 7, 7, "미발행");
		xlsxBuilder.addHeader(1, 1, 8, 8, "발행중");
		xlsxBuilder.addHeader(1, 1, 9, 9, "발행");
		xlsxBuilder.addHeader(1, 1, 10, 10, "미생성");
		xlsxBuilder.addHeader(1, 1, 11, 11, "미발행");
		xlsxBuilder.addHeader(1, 1, 12, 12, "발행중");
		xlsxBuilder.addHeader(1, 1, 13, 13, "발행");
		xlsxBuilder.addHeader(1, 1, 14, 14, "미생성");
		xlsxBuilder.addHeader(1, 1, 15, 15, "미발행");
		xlsxBuilder.addHeader(1, 1, 16, 16, "발행중");
		xlsxBuilder.addHeader(1, 1, 17, 17, "발행");

		for(int i=1;;i++) {
			final PageBounds pageBounds = new PageBounds(i, 100);
			final List<ClientWithCashReceiptStatusDTO> itemList = cashReceiptMapper.getClientWithCashReceiptStatusList(startDate, endDate, clientId, clientName, status, enableCashReceipt, enableAutomaticCashReceipt, orderByClause, pageBounds.toRowBounds(), mandRcpYn);
			if(itemList.isEmpty()) {
				break;
			}

			for (ClientWithCashReceiptStatusDTO each : itemList) {
				xlsxBuilder.newDataRow();

				xlsxBuilder.addData(0, each.getId());
				xlsxBuilder.addData(1, each.getName());
				xlsxBuilder.addData(2, each.getStatusName());
				xlsxBuilder.addData(3, each.getEnableCashReceipt());
				xlsxBuilder.addData(4, each.getEnableAutomaticCashReceipt());
				xlsxBuilder.addData(5, each.getMandRcpYn());
				xlsxBuilder.addData(6, each.getVasNoCashReceiptCount());
				xlsxBuilder.addData(7, each.getVasReadyCashReceiptCount());
				xlsxBuilder.addData(8, each.getVasRequestCashReceiptCount());
				xlsxBuilder.addData(9, each.getVasIssueCashReceiptCount());
				xlsxBuilder.addData(10,each.getDcsNoCashReceiptCount() );
				xlsxBuilder.addData(11, each.getDcsReadyCashReceiptCount());
				xlsxBuilder.addData(12, each.getDcsRequestCashReceiptCount());
				xlsxBuilder.addData(13, each.getDcsIssueCashReceiptCount());
				xlsxBuilder.addData(14, each.getDvaNoCashReceiptCount());
				xlsxBuilder.addData(15, each.getDvaReadyCashReceiptCount());
				xlsxBuilder.addData(16, each.getDvaRequestCashReceiptCount());
				xlsxBuilder.addData(17, each.getDvaIssueCashReceiptCount());
			}
		}

		final File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".tmp");
		xlsxBuilder.writeTo(new FileOutputStream(tempFile));
		return new FileInputStream(tempFile);
	}

	public InputStream getStatusByReceiptExcel(String startDate, String endDate, String startDate2, String endDate2, String chaCd, String chaName, String sveCd, String createCashReceipt, String notIssuedErrorCashReceipt, String orderBy) throws IOException {
		final String orderByClause;
		switch (StringUtils.defaultString(orderBy)) {
			case "regDt":
				orderByClause = "A.REGDT DESC, B.CHACD ASC";
				break;
			case "chacd":
				orderByClause = "B.CHACD ASC, A.REGDT DESC";
				break;
			default:
				orderByClause = "A.REGDT DESC, B.CHACD ASC";
				break;
		}

		XlsxBuilder xlsxBuilder = new XlsxBuilder();
		xlsxBuilder.newSheet("데이터");
		xlsxBuilder.addHeader(0, 1, 0, 0, "기관코드");
		xlsxBuilder.addHeader(0, 1, 1, 1, "기관명");
		xlsxBuilder.addHeader(0, 1, 2, 2, "거래번호");
		xlsxBuilder.addHeader(0, 1, 3, 3, "거래일시");
		xlsxBuilder.addHeader(0, 1, 4, 4, "수납일자");
		xlsxBuilder.addHeader(0, 1, 5, 5, "수납상태");
		xlsxBuilder.addHeader(0, 1, 6, 6, "발행여부");
		xlsxBuilder.addHeader(0, 1, 7, 7, "가상계좌 자동발헹여부");
		xlsxBuilder.addHeader(0, 1, 8, 8, "수기수납 발헹여부");
		xlsxBuilder.addHeader(0, 1, 9, 9, "수기수납 자동발헹여부");
		xlsxBuilder.addHeader(0, 1, 10, 10, "면세여부");
		xlsxBuilder.addHeader(0, 1, 11, 11, "현금영수증번호");
		xlsxBuilder.addHeader(0, 1, 12, 12, "상태");
		xlsxBuilder.addHeader(0, 1, 13, 13, "승인일자");
		xlsxBuilder.addHeader(0, 1, 14, 14, "승인번호");
		xlsxBuilder.addHeader(0, 1, 15, 15, "매출금액");
		xlsxBuilder.addHeader(0, 1, 16, 16, "세금");
		xlsxBuilder.addHeader(0, 1, 17, 17, "서비스");
		xlsxBuilder.addHeader(0, 1, 18, 18, "발급번호");
		xlsxBuilder.addHeader(0, 1, 19, 19, "발급번호구분");
		xlsxBuilder.addHeader(0, 1, 20, 20, "발급자구분");
		xlsxBuilder.addHeader(0, 1, 21, 21, "마지막 조작자");
		xlsxBuilder.addHeader(0, 1, 22, 22, "마지막 조작일시");

		for(int i=1;;i++) {
			final PageBounds pageBounds = new PageBounds(i, 100);
			final List<ReceiptWithCashReceiptStatusDTO> itemList = cashReceiptMapper.getReceiptWithCashReceiptStatusList(startDate, endDate, startDate2, endDate2, chaCd, chaName, sveCd, createCashReceipt, notIssuedErrorCashReceipt, orderByClause, pageBounds.toRowBounds());
			if(itemList.isEmpty()) {
				break;
			}

			for (ReceiptWithCashReceiptStatusDTO each : itemList) {
				xlsxBuilder.newDataRow();

				xlsxBuilder.addData(0, each.getChaCd());
				xlsxBuilder.addData(1, each.getChaName());
				xlsxBuilder.addData(2, each.getRcpmasCd());
				xlsxBuilder.addData(3, each.getRegDt());
				xlsxBuilder.addData(4, each.getPayDay());

				if(each.getRcpmasSt().equals("PA03")){
					xlsxBuilder.addData(5, "수납");
				}else if(each.getRcpmasSt().equals("PA09")){
					xlsxBuilder.addData(5, "취소");
				}else{
					xlsxBuilder.addData(5, "");
				}

				if(each.getRcpreqYn().equals("Y")){
					xlsxBuilder.addData(6, "발행");
				}else{
					xlsxBuilder.addData(6, "발행안함");
				}

				if(each.getRcpReqty().equals("A")){
					xlsxBuilder.addData(7, "자동");
				}else{
					xlsxBuilder.addData(7, "수동");
				}

				if(each.getRcpReqsvety().equals("00")){
					xlsxBuilder.addData(8, "발행안함");
				}else if(each.getRcpReqsvety().equals("01")){
					xlsxBuilder.addData(8, "발행");
				}else{
					xlsxBuilder.addData(8, "");
				}

				if(each.getMnlrcpReqty().equals("A")){
					xlsxBuilder.addData(9, "자동");
				}else{
					xlsxBuilder.addData(9, "수동");
				}

				if(each.getNotaxYn().equals("Y")){
					xlsxBuilder.addData(10, "과세");
				}else{
					xlsxBuilder.addData(10, "면세");
				}

				xlsxBuilder.addData(11, each.getCashmasCd());

				if(each.getCashMasst().equals("ST02")){
					xlsxBuilder.addData(12, "미발행");
				}else if(each.getCashMasst().equals("ST03")){
					xlsxBuilder.addData(12, "발행");
				}else if(each.getCashMasst().equals("ST04")){
					xlsxBuilder.addData(12, "발행중");
				}else if(each.getCashMasst().equals("ST05")){
					xlsxBuilder.addData(12, "발행요청");
				}else{
					xlsxBuilder.addData(12, "");
				}

				xlsxBuilder.addData(13, each.getAppDay());
				xlsxBuilder.addData(14, each.getAppNo());
				xlsxBuilder.addData(15, each.getRcpAmt());
				xlsxBuilder.addData(16, each.getTax());
				xlsxBuilder.addData(17, each.getTip());
				xlsxBuilder.addData(18, each.getCusoffNo());

				if(each.getConfirm().equals("11")){
					xlsxBuilder.addData(19, "휴대폰번호");
				}else if(each.getConfirm().equals("12")){
					xlsxBuilder.addData(19, "현금영수증카드번호");
				}else if(each.getConfirm().equals("21")){
					xlsxBuilder.addData(19, "사업자등록번호");
				}else if(each.getConfirm().equals("13")){
					xlsxBuilder.addData(19, "주민번호");
				}else{
					xlsxBuilder.addData(19, "");
				}

				if(each.getCusType().equals("1")){
					xlsxBuilder.addData(20, "소득공제");
				}else if(each.getCusType().equals("2")){
					xlsxBuilder.addData(20, "지출증빙");
				}else{
					xlsxBuilder.addData(20, "");
				}

				xlsxBuilder.addData(21, each.getMaker());

				String makeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(each.getMakeDt());
				xlsxBuilder.addData(22, makeDate);
			}
		}

		final File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".tmp");
		xlsxBuilder.writeTo(new FileOutputStream(tempFile));
		return new FileInputStream(tempFile);
	}

	public HashMap<String, Object> getCashReceiptHistory(HashMap<String,Object> map, PageBounds pageBounds) {
		final HashMap<String,Object> totalItemCount = addServiceCashDao.countCashReceiptHistory(map);

		final List<CashReceiptHistoryDTO> itemList = addServiceCashDao.getCashReceiptHistory(map, pageBounds.toRowBounds());
        for (CashReceiptHistoryDTO each : itemList) {
        	if(!StringUtils.isBlank(each.getTxTypeCode()) && "0".equals(each.getTxTypeCode())){
        		each.setTxTypeCode("승인");
			}else if(!StringUtils.isBlank(each.getTxTypeCode()) && "1".equals(each.getTxTypeCode())){
				each.setTxTypeCode("취소");
			}
            each.setRequestDt(kr.co.finger.shinhandamoa.common.DateUtils.formatDate(each.getRequestDate(), "yyyy.MM.dd"));
            each.setTaxFreeAmount(each.getTxAmount() - each.getTax());
            each.setReqStatus("");
            each.setResponseMessage(each.getResponseCode());
        }

		HashMap<String, Object> returnMap = new HashMap<>();
		returnMap.put("itemList", itemList);
		returnMap.put("totalItemCount", totalItemCount.get("CNT"));
		returnMap.put("totalItemAmount", totalItemCount.get("TOTAMT"));

		return returnMap;
	}

	public InputStream getStatusByIssueCashReceiptExcel(String startDate, String endDate, String startDate2, String endDate2, String startDate3, String endDate3, String chaCd, String chaName, String sveCd, String createErrorCashReceipt, String duplicationIssuedErrorCashReceipt, String notIssuedErrorCashReceipt, String orderBy) throws IOException {
        final String orderByClause;
        switch (StringUtils.defaultString(orderBy)) {
            case "regDt":
                orderByClause = "A.REGDT DESC, B.CHACD ASC";
                break;
            case "chacd":
                orderByClause = "B.CHACD ASC, A.REGDT DESC";
                break;
            default:
                orderByClause = "A.REGDT DESC, B.CHACD ASC";
                break;
        }

        XlsxBuilder xlsxBuilder = new XlsxBuilder();
        xlsxBuilder.newSheet("데이터");
        xlsxBuilder.addHeader(0, 1, 0, 0, "현금영수증번호");
        xlsxBuilder.addHeader(0, 1, 1, 1, "기관코드");
        xlsxBuilder.addHeader(0, 1, 2, 2, "기관명");
        xlsxBuilder.addHeader(0, 1, 3, 3, "고객명");
        xlsxBuilder.addHeader(0, 1, 4, 4, "거래일시");
        xlsxBuilder.addHeader(0, 1, 5, 5, "발행여부");
        xlsxBuilder.addHeader(0, 1, 6, 6, "가상계좌 자동발행 여부");
        xlsxBuilder.addHeader(0, 1, 7, 7, "수기수납 발행여부");
        xlsxBuilder.addHeader(0, 1, 8, 8, " 수기수납 자동발행 여부");
        xlsxBuilder.addHeader(0, 1, 9, 9, "면세여부");
        xlsxBuilder.addHeader(0, 1, 10, 10, "요청일자");
        xlsxBuilder.addHeader(0, 1, 11, 11, "매출금액");
        xlsxBuilder.addHeader(0, 1, 12, 12, "부과세액");
        xlsxBuilder.addHeader(0, 1, 13, 13, "발급번호");
        xlsxBuilder.addHeader(0, 1, 14, 14, "발급번호구분");
        xlsxBuilder.addHeader(0, 1, 15, 15, "발급자구분");
        xlsxBuilder.addHeader(0, 1, 16, 16, "작업구분");
        xlsxBuilder.addHeader(0, 1, 17, 17, "승일일자");
        xlsxBuilder.addHeader(0, 1, 18, 18, "승인번호");
        xlsxBuilder.addHeader(0, 1, 19, 19, "결과코드");
        xlsxBuilder.addHeader(0, 1, 20, 20, "마지막 조작자");
        xlsxBuilder.addHeader(0, 1, 21, 21, "오류정보");
        xlsxBuilder.addHeader(0, 1, 22, 22, "상태");

        for (int i = 1; ; i++) {
            final PageBounds pageBounds = new PageBounds(i, 100);
            final List<IssueWithCashReceiptStatusDTO> itemList = cashReceiptMapper.getIssueWithCashReceiptStatusList(startDate, endDate, startDate2, endDate2, startDate3, endDate3, chaCd, chaName, sveCd, createErrorCashReceipt, duplicationIssuedErrorCashReceipt, notIssuedErrorCashReceipt, orderByClause, pageBounds.toRowBounds());
            if (itemList.isEmpty()) {
                break;
            }

            for (IssueWithCashReceiptStatusDTO each : itemList) {
                xlsxBuilder.newDataRow();

                xlsxBuilder.addData(0, each.getCashmasCd());
                xlsxBuilder.addData(1, each.getChaCd());
                xlsxBuilder.addData(2, each.getChaName());
                xlsxBuilder.addData(3, each.getCusName());
                xlsxBuilder.addData(4, each.getRegDt());

                if(each.getRcpreqYn().equals("Y")){
					xlsxBuilder.addData(5, "발행");
				}else{
					xlsxBuilder.addData(5, "발행안함");
				}

                if(each.getRcpReqty().equals("A")){
					xlsxBuilder.addData(6, "자동");
				}else{
					xlsxBuilder.addData(6, "수동");
				}

                if(each.getRcpReqsvety().equals("00")){
					xlsxBuilder.addData(7, "발행안함");
				}else if(each.getRcpReqsvety().equals("01")){
					xlsxBuilder.addData(7, "발행");
				}else{
					xlsxBuilder.addData(7, "");
				}

				if(each.getMnlrcpReqty().equals("A")){
					xlsxBuilder.addData(8, "자동");
				}else{
					xlsxBuilder.addData(8, "수동");
				}

				if(each.getNotaxYn().equals("Y")){
					xlsxBuilder.addData(9, "과세");
				}else{
					xlsxBuilder.addData(9, "면세");
				}

                xlsxBuilder.addData(10, each.getSendDt());
                xlsxBuilder.addData(11, each.getRcpAmt());
                xlsxBuilder.addData(12, each.getTax());
                xlsxBuilder.addData(13, each.getCusoffNo());

				if(each.getConfirm().equals("11")){
					xlsxBuilder.addData(14, "휴대폰번호");
				}else if(each.getConfirm().equals("12")){
					xlsxBuilder.addData(14, "현금영수증카드번호");
				}else if(each.getConfirm().equals("21")){
					xlsxBuilder.addData(14, "사업자등록번호");
				}else if(each.getConfirm().equals("13")){
					xlsxBuilder.addData(14, "주민번호");
				}else{
					xlsxBuilder.addData(14, "");
				}

				if(each.getCusType().equals("1")){
					xlsxBuilder.addData(15, "소득공제");
				}else if(each.getCusType().equals("2")){
					xlsxBuilder.addData(15, "지출증빙");
				}else{
					xlsxBuilder.addData(15, "");
				}

				xlsxBuilder.addData(16, each.getJob());
                xlsxBuilder.addData(17, each.getAppDay());
                xlsxBuilder.addData(18, each.getAppNo());
                xlsxBuilder.addData(19, each.getValue());
                xlsxBuilder.addData(20, each.getMaker());

                String errorInfo = "";

                if(each.getJob() == null){
					errorInfo = "작업구분 없음";
				}

				if(each.getCusType() == null || each.getConfirm() == null){
					errorInfo = "발행번호 구분 없음";
				}else{
					errorInfo = "발행번호 없음";
				}

				if(each.getCusType().equals("1")){
					if(!(each.getConfirm().equals("11")) && !(each.getConfirm().equals("12"))){
						errorInfo = "발행번호 구분 오류";
					}
				}else if(each.getCusType().equals("2")){
					if(!(each.getConfirm().equals("21"))){
						errorInfo = "발행번호 구분 오류";
					}
				}

				if(each.getCusType().equals("1") && each.getConfirm().equals("11")){
					if(!((each.getCusoffNo().length() == 10 || each.getCusoffNo().length() == 11) && !(each.getCusoffNo().substring(0,2).equals("01")))){
						errorInfo = "발행번호 오류(휴대폰번호)";
					}
				}else if(each.getCusType().equals("1") && each.getConfirm().equals("12")){
					if(each.getCusoffNo().length() != 18){
						errorInfo = "발행번호 오류(현금영수증카드번호)";
					}
				}else if(each.getCusType().equals("2") && each.getConfirm().equals("21")){
					if(!(each.getCusoffNo().length() == 10 && !(each.getCusoffNo().substring(0,1).equals("0")))){
						errorInfo = "발행번호 오류(사업자번호)";
					}
				}

				xlsxBuilder.addData(21, errorInfo);

				if(each.getCashMasst().equals("ST02")){
					xlsxBuilder.addData(22, "미발행");
				}else if(each.getCashMasst().equals("ST03")){
					xlsxBuilder.addData(22, "발행");
				}else if(each.getCashMasst().equals("ST04")){
					xlsxBuilder.addData(22, "발행중");
				}else if(each.getCashMasst().equals("ST05")){
					xlsxBuilder.addData(22, "발행요청");
				}else{
					xlsxBuilder.addData(22, "");
				}
            }
        }

        final File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".tmp");
        xlsxBuilder.writeTo(new FileOutputStream(tempFile));
        return new FileInputStream(tempFile);
    }
}
