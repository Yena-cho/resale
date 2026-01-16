package com.finger.shinhandamoa.org.claimMgmt.service;

import com.finger.shinhandamoa.data.table.mapper.CustomerMasterMapper;
import com.finger.shinhandamoa.data.table.mapper.NoticeDetailsMapper;
import com.finger.shinhandamoa.data.table.mapper.NoticeDetailsTypeMapper;
import com.finger.shinhandamoa.data.table.mapper.NoticeMasterMapper;
import com.finger.shinhandamoa.data.table.model.*;
import com.finger.shinhandamoa.org.claimMgmt.dao.ClaimDAO;
import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimDTO;
import com.finger.shinhandamoa.org.claimMgmt.dto.NoticeDTO;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiSendDTO;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author by puki
 * @date 2018. 4. 12.
 * @desc 최초생성
 */
@Service
public class ClaimServiceImpl implements ClaimService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClaimServiceImpl.class);

    @Inject
    private ClaimDAO claimDao;

    @Autowired
    private NoticeMasterMapper noticeMasterMapper;

    @Autowired
    private NoticeDetailsMapper noticeDetailsMapper;

    @Autowired
    private CustomerMasterMapper customerMasterMapper;

    @Autowired
    private NoticeDetailsTypeMapper noticeDetailsTypeMapper;

    @Autowired
    private PlatformTransactionManager transactionManager;

    DefaultTransactionDefinition def = null;
    TransactionStatus status = null;

    @Override
    public String selectClaimMonth(String chaCd, String notiMasSt) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("chaCd", chaCd);
        map.put("notiMasSt", notiMasSt);

        return claimDao.selectClaimMonth(map);
    }

    @Override
    public List<ClaimDTO> selectClaimAll(Map<String, Object> map) throws Exception {
        return claimDao.selectClaimAll(map);
    }

    @Override
    public int selectClaimTotalCnt(Map<String, Object> map) throws Exception {

        return claimDao.selectClaimTotalCnt(map);
    }

    @Override
    public int selectClaimClientCnt(Map<String, Object> map) throws Exception {

        return claimDao.selectClaimClientCnt(map);
    }

    @Transactional
    @Override
    public void deleteClaim(String chaCd, String notiMasSt) throws Exception {

        try {
            def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("chaCd", chaCd);
            map.put("notiMasSt", notiMasSt);

            claimDao.deleteClaimXnotidet(map); // 청구서 상세 항목
            claimDao.deleteClaimXnotimas(map); // 청구서

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            e.printStackTrace();
        }
    }

    @Transactional
    @Override
    public void selClaimDelete(Map<String, Object> map) throws Exception {
        try {
            def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);

            claimDao.selClaimDeleteMas(map); // 청구서
            claimDao.selClaimDeleteDet(map); // 청구서 상세 항목

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            e.printStackTrace();
        }
    }

    @Override
    public void delClaimItem(Map<String, Object> map) throws Exception {
        claimDao.selClaimDeleteDet(map); // 청구서 상세 항목
    }

    @Override
    public ClaimDTO selectClaimDetail(String notiMasCd) throws Exception {
        return claimDao.selectClaimDetail(notiMasCd);
    }

    @Override
    public long selClaimRegSum(Map<String, Object> map) throws Exception {
        return claimDao.selClaimRegSum(map);
    }

    @Transactional
    @Override
    public int updateClaimSave(Map<String, Object> map) throws Exception {
    	int updateRst = 0;
        try {
            def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);

            //XNOTIMAS
            if( !StringUtils.isEmpty((String)map.get("startDate"))) {
            	updateRst += claimDao.updateMasBundle(map);
            	LOGGER.info("MasBundle UpdateRst = {}", updateRst);
            } else {
            	claimDao.updateMasBundle(map);
            }
            
            //XNOTIDET (청구항목코드가 있을경우에만 det update)
            	if( !StringUtils.isEmpty((String)map.get("payItemCd"))) {
            		updateRst += claimDao.updateDetBundle(map);
            		LOGGER.info("DetBundle UpdateRst = {}", updateRst);
            	}
            

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            updateRst = 0;
            e.printStackTrace();
        }
        
        return updateRst;
    }

    @Override
    public void updateClaimTemp(Map<String, Object> map) throws Exception {
        claimDao.updateClaimTempDet(map);
    }

    @Transactional
    @Override
    public void claimInsert(Map<String, Object> map) throws Exception {
        try {
            def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);

            claimDao.claimMasUpdate(map);
            claimDao.claimDetUpdate(map);

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            e.printStackTrace();
        }
    }

    @Override
    public ClaimDTO selDetToMasNo(String notiDetCd) throws Exception {
        return claimDao.selDetToMasNo(notiDetCd);
    }

    @Override
    public List<ClaimDTO> selMasDetNoList(Map<String, Object> map) throws Exception {
        return claimDao.selMasDetNoList(map);
    }

    @Override
    public List<ClaimDTO> searchCusName(Map<String, Object> map) throws Exception {
        return claimDao.searchCusName(map);
    }

    @Override
    public int searchCusNameCnt(Map<String, Object> map) throws Exception {
        return claimDao.searchCusNameCnt(map);
    }

    @Override
    public ClaimDTO searchCustomer(Map<String, Object> map) throws Exception {
        return claimDao.searchCustomer(map);
    }

    @Override
    public int selClaimRegConfirm(Map<String, Object> map) throws Exception {
        return claimDao.selClaimRegConfirm(map);
    }

    @Override
    public void insClaimRegMas(Map<String, Object> map) throws Exception {
        claimDao.insClaimRegMas(map);
    }

    @Override
    public void insClaimRegDet(Map<String, Object> map) throws Exception {
        claimDao.insClaimRegDet(map);
    }

    @Override
    public List<ClaimDTO> selItemList(Map<String, Object> map) throws Exception {
        return claimDao.selItemList(map);
    }

    @Override
    public void claimItemDelete(String notiDetCd) throws Exception {
        claimDao.claimItemDelete(notiDetCd);
    }

    @Override
    public List<ClaimDTO> selBeforeMasYear(String chaCd) throws Exception {
        return claimDao.selBeforeMasYear(chaCd);
    }

    @Override
    public List<ClaimDTO> selBeforeMasMonth(String chaCd, String year) throws Exception {
        return claimDao.selBeforeMasMonth(chaCd, year);
    }

    @Override
    public void beforeXnotimasInsert(Map<String, Object> map) throws Exception {
        claimDao.beforeXnotimasInsert(map);
    }

    @Override
    public void beforeXnotidetInsert(Map<String, Object> map) throws Exception {
        claimDao.beforeXnotidetInsert(map);
    }

    @Override
    public String autoSmsSendYn(Map<String, Object> map) throws Exception {
        return claimDao.autoSmsSendYn(map);
    }

    @Override
    public List<NotiSendDTO> autoSmsList(Map<String, Object> map) throws Exception {
        return claimDao.autoSmsList(map);
    }

    @Transactional
    @Override
    public void updateCancelAll(Map<String, Object> map) throws Exception {
        try {
            def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);

            claimDao.updateCancelMasAll(map);
            claimDao.updateCancelDetAll(map);

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            e.printStackTrace();
        }
    }

    @Override
    public ClaimDTO selNotiMasCd(Map<String, Object> map) throws Exception {
        return claimDao.selNotiMasCd(map);
    }

    @Override
    public String selectNotiMasCd(Map<String, Object> map) throws Exception {
        return claimDao.selectNotiMasCd(map);
    }

    @Override
    public int selDupPayItem(Map<String, Object> map) throws Exception {
        return claimDao.selDupPayItem(map);
    }

    @Transactional
    @Override
    public void updateClaimNoti(Map<String, Object> map) throws Exception {
        try {
            def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);

            claimDao.updateClaimNotiMas(map);
            claimDao.updateClaimNotiDet(map);

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            e.printStackTrace();
        }
    }

    @Override
    public String selctMasMonth(Map<String, Object> map) throws Exception {
        return claimDao.selctMasMonth(map);
    }

    @Override
    public int selectClaimDetCount(Map<String, Object> map) throws Exception {
        return claimDao.selectClaimDetCount(map);
    }

    @Override
    public int selectClaimCancelCount(Map<String, Object> map) throws Exception {
        return claimDao.selectClaimCancelCount(map);
    }

    @Override
    public List<ClaimDTO> selectClaimDetList(Map<String, Object> map) throws Exception {
        return claimDao.selectClaimDetList(map);
    }

    @Override
    public List<ClaimDTO> selectPayItemDetailView(Map<String, Object> map) throws Exception {
        return claimDao.selectPayItemDetailView(map);
    }

    @Override
    public int selDetCdCnt(Map<String, Object> map) throws Exception {
        return claimDao.selDetCdCnt(map);
    }

    @Override
    public int useClaimCnt(Map<String, Object> map) throws Exception {
        return claimDao.useClaimCnt(map);
    }

    @Override
    public void selClaimDeleteDetAll(HashMap<String, Object> smap) throws Exception {
        claimDao.selClaimDeleteDetAll(smap);
    }

    @Override
    public void selClaimDeleteDetB(HashMap<String, Object> smap) throws Exception {
        claimDao.selClaimDeleteDetB(smap);
    }

    @Override
    public void copyNotice(String chaCd, String fromMonth, String toMonth) {
        final NoticeMasterExample noticeMasterExample = new NoticeMasterExample();
        noticeMasterExample.createCriteria().andChacdEqualTo(chaCd).andMasmonthEqualTo(fromMonth).andNotiCanYnEqualTo("N").andNotimascdNotEqualTo("PA00");

        final List<NoticeMaster> noticeMasterList = noticeMasterMapper.selectByExample(noticeMasterExample);
        final List<NoticeDTO> noticeList = new ArrayList<>();
        for (NoticeMaster each : noticeMasterList) {
            final String vano = each.getVano();
            final CustomerMaster customerMaster = customerMasterMapper.selectByPrimaryKey(vano);

            // 고객 없을 때
            if (customerMaster == null) {
                continue;
            }

            // 정상 아닐 때
            if (StringUtils.equalsIgnoreCase(customerMaster.getDisabled(), "Y")) {
                continue;
            }

            // 납부 제외일 때
            if (StringUtils.equalsIgnoreCase(customerMaster.getRcpgubn(), "N")) {
                continue;
            }

            final NoticeDetailsExample noticeDetailsExample = new NoticeDetailsExample();
            noticeDetailsExample.createCriteria().andNotimascdEqualTo(each.getNotimascd());

            final List<NoticeDetails> detailsList = ListUtils.select(noticeDetailsMapper.selectByExample(noticeDetailsExample), object -> {
                final String payitemcd = object.getPayitemcd();
                final NoticeDetailsType noticeDetailsType = noticeDetailsTypeMapper.selectByPrimaryKey(payitemcd);

                if (noticeDetailsType == null) {
                    return false;
                }

                if (!StringUtils.equalsIgnoreCase(noticeDetailsType.getPayitemst(), "ST01")) {
                    return false;
                }

                return true;
            });

            if (detailsList == null || detailsList.isEmpty()) {
                continue;
            }

            noticeList.add(new NoticeDTO(each, detailsList));
        }

        // 전처리
        final Date now = new Date();
        final String today = new SimpleDateFormat("yyyyMMdd").format(now);

        for (NoticeDTO each : noticeList) {
            LOGGER.info("청구[{}]: {}", each.getNoticeMaster().getNotimascd(), each);
            each.getNoticeMaster().setNotimascd("");
            each.getNoticeMaster().setMasmonth(toMonth);
            each.getNoticeMaster().setMasday(today);
            each.getNoticeMaster().setStartdate(addMonthDate(each.getNoticeMaster().getStartdate()));
            each.getNoticeMaster().setEnddate(addMonthDate(each.getNoticeMaster().getEnddate()));
            each.getNoticeMaster().setPrintdate(StringUtils.defaultIfBlank(addMonthDate(each.getNoticeMaster().getPrintdate()), addMonthDate(each.getNoticeMaster().getEnddate())));
            each.getNoticeMaster().setNotimasst("PA00");
            each.getNoticeMaster().setRegdt(now);
            each.getNoticeMaster().setMaker(chaCd);
            each.getNoticeMaster().setMakedt(now);

            for (NoticeDetails eachItem : each.getItemList()) {
                eachItem.setNotidetcd("");
                eachItem.setNotimascd("");
                eachItem.setNotidetst("PA00");
                eachItem.setMakedt(now);
                eachItem.setMaker(chaCd);
                eachItem.setRegdt(now);
            }
        }

        // 등록
        for (NoticeDTO each : noticeList) {
            NoticeMaster noticeMaster = each.getNoticeMaster();
            noticeMasterMapper.insert(noticeMaster);

            for (NoticeDetails eachItem : each.getItemList()) {
                eachItem.setNotimascd(noticeMaster.getNotimascd());
                noticeDetailsMapper.insert(eachItem);
            }
        }
    }

    //납부기한, 고지서용 표지마감일 한달 추가
    public String addMonthDate(String beDate){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try{
            date = dateFormat.parse(beDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(cal.get(Calendar.DAY_OF_MONTH) == cal.getActualMaximum(Calendar.DAY_OF_MONTH)){//월의 마지막 일
            cal.add(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        }else{
            cal.add(Calendar.MONTH, 1);
        }
        return dateFormat.format(cal.getTime());
    }

    @Override
    public long countNoticeForCopy(String chaCd, String fromMonth) {
        final NoticeMasterExample noticeMasterExample = new NoticeMasterExample();
        noticeMasterExample.createCriteria().andChacdEqualTo(chaCd).andMasmonthEqualTo(fromMonth).andNotiCanYnEqualTo("N").andNotimascdNotEqualTo("PA00");

        final List<NoticeMaster> noticeMasterList = noticeMasterMapper.selectByExample(noticeMasterExample);
        final List<NoticeDTO> noticeList = new ArrayList<>();
        for (NoticeMaster each : noticeMasterList) {
            final String vano = each.getVano();
            final CustomerMaster customerMaster = customerMasterMapper.selectByPrimaryKey(vano);

            // 고객 없을 때
            if (customerMaster == null) {
                continue;
            }

            // 정상 아닐 때
            if (StringUtils.equalsIgnoreCase(customerMaster.getDisabled(), "Y")) {
                continue;
            }

            // 납부 제외일 때
            if (StringUtils.equalsIgnoreCase(customerMaster.getRcpgubn(), "N")) {
                continue;
            }

            final NoticeDetailsExample noticeDetailsExample = new NoticeDetailsExample();
            noticeDetailsExample.createCriteria().andNotimascdEqualTo(each.getNotimascd());

            final List<NoticeDetails> detailsList = ListUtils.select(noticeDetailsMapper.selectByExample(noticeDetailsExample), object -> {
                final String payitemcd = object.getPayitemcd();
                final NoticeDetailsType noticeDetailsType = noticeDetailsTypeMapper.selectByPrimaryKey(payitemcd);

                if (noticeDetailsType == null) {
                    return false;
                }

                if (!StringUtils.equalsIgnoreCase(noticeDetailsType.getPayitemst(), "ST01")) {
                    return false;
                }

                return true;
            });

            if (detailsList == null || detailsList.isEmpty()) {
                continue;
            }

            noticeList.add(new NoticeDTO(each, detailsList));
        }

        return noticeList.size();
    }

    @Override
    public void updateClaimDay(Map<String, Object> map) throws Exception {
        claimDao.updateClaimDay(map);
    }

	/* (non-Javadoc)
	 * @see com.finger.shinhandamoa.org.claimMgmt.service.ClaimService#selectedToDelClaimDetList(java.util.List)
	 */
	@Override
	public Map<String, Object> selectedToDelClaimDetList(List<String> notidetcdList) throws Exception {
		
		Map<String, Object> result = new HashMap<>();
		// 여기에서 삭제되는 청구는 상태가 'PA00'인 임시 상태의 값이기 때문에 delete로 처리한다.
		try {
			int detailCount = 0;
			int masterCount = 0;
			for (String notidetcd : notidetcdList) {
				final NoticeDetails detail = noticeDetailsMapper.selectByPrimaryKey(notidetcd);
				NoticeDetailsExample example = new NoticeDetailsExample();
				example.createCriteria().andNotimascdEqualTo(detail.getNotimascd());
				if (noticeDetailsMapper.countByExample(example) > 1) {
					// 상세항목이 1건이상일 경우 상세항목만 삭제
					detailCount += noticeDetailsMapper.deleteByPrimaryKey(notidetcd);
				} else {
					// 상세항목이 1건인 경우 청구마스터도 삭제
					masterCount += noticeMasterMapper.deleteByPrimaryKey(detail.getNotimascd());
					detailCount += noticeDetailsMapper.deleteByPrimaryKey(notidetcd);
				}
			}
			
			result.put("retCode", "0000");
			if (masterCount > 0) {
				result.put("retMsg", String.format("청구항목 [%d]건, 청구[%d]건을 삭제했습니다.", detailCount, masterCount) );
			} else {
				result.put("retMsg", String.format("청구항목 [%d]건을 삭제했습니다.", detailCount) );
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.put("retCode", "9999");
			result.put("retMsg", String.format("청구항목 삭제처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.\n%s", e.getMessage()) );
		}
		
		return result;
	}

    @Override
    public void claimMasUpdate(Map<String, Object> map) throws Exception {
        claimDao.claimMasUpdate(map);
    }

    @Override
    public void updateCusPreClaim(HashMap<String, Object> cmap) throws Exception {
        claimDao.updateCusPreClaim(cmap);
    }

    @Override
    public String autoAtSendYn(Map<String, Object> map) throws Exception {
        return claimDao.autoAtSendYn(map);
    }

    @Override
    public List<NotiSendDTO> autoAtList(Map<String, Object> map) throws Exception {
        return claimDao.autoAtList(map);
    }
}


