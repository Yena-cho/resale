package com.finger.shinhandamoa.org.claimMgmt.service;

import com.finger.shinhandamoa.org.claimMgmt.dao.ClaimExcelDAO;
import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimDTO;
import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * @author by puki
 * @date 2018. 4. 12.
 * @desc 최초생성
 */
@Service
public class ClaimExcelServiceImpl implements ClaimExcelService {

    private static final Logger logger = LoggerFactory.getLogger(ClaimExcelServiceImpl.class);

    @Inject
    private ClaimExcelDAO claimExcelDao;

    @Override
    public List<ClaimDTO> excelList(Map<String, Object> map) throws Exception {
        return claimExcelDao.excelList(map);
    }

    @Override
    public List<ClaimDTO> excelBeforeList(Map<String, Object> map) throws Exception {
        return claimExcelDao.excelBeforeList(map);
    }

    @Override
    public void claimExcelFailInsert(Map<String, Object> map) throws Exception {
        if ("EXC".equals(map.get("flag"))) {
            map.put("cusHp", "");
            map.put("cusMail", "");
            map.put("cusOffNo", "");
        }
        if ("null".equals((String) map.get("payItemCd")) || map.get("payItemCd") == null) {
            map.put("payItemCd", "");
        }

        claimExcelDao.claimExcelFailInsert(map);
    }

    @Override
    public void claimFailDelete(String chaCd) throws Exception {
        claimExcelDao.claimFailDelete(chaCd);
    }

    @Override
    public List<ClaimDTO> failList(String chaCd, int start, int end) throws Exception {
        return claimExcelDao.failList(chaCd, start, end);
    }

    @Override
    public int failTotalCount(String chaCd) throws Exception {
        return claimExcelDao.failTotalCount(chaCd);
    }

    @Override
    public List<ClaimDTO> failExcelList(Map<String, Object> map) throws Exception {
        return claimExcelDao.failExcelList(map);
    }

    @Override
    public List<ClaimDTO> excelSave(Map<String, Object> map) throws Exception {
        return claimExcelDao.excelSave(map);
    }

    @Override
    public String selNotiMasCd(Map<String, Object> map) throws Exception {
        return claimExcelDao.selNotiMasCd(map);
    }

    @Override
    public String selClaimConfirm(Map<String, Object> map) throws Exception {
        return claimExcelDao.selClaimConfirm(map);
    }

    @Override
    public List<ClaimDTO> excelItemList(Map<String, Object> map) throws Exception {
        return claimExcelDao.excelItemList(map);
    }

    @Override
    public List<ClaimDTO> excelCusList(Map<String, Object> map) throws Exception {
        return claimExcelDao.excelCusList(map);
    }

    @Override
    public List<ClaimDTO> selExcelBeforeMonth(Map<String, Object> map) throws Exception {
        return claimExcelDao.selExcelBeforeMonth(map);
    }

    @Override
    public int bulkUploadExcel(List<Map<String, Object>> list, int rowNo, int rowNum) throws Exception {
        int failCnt = claimExcelDao.bulkUploadExcel(list, rowNo, rowNum);
        return failCnt;
    }

    @Override
    public List<ClaimDTO> excelMasterList(Map<String, Object> map) throws Exception {
        return claimExcelDao.excelMasterList(map);
    }

    @Override
    public List<ClaimItemDTO> selectDetailsForExcel(String notimasCd) throws Exception {
        return claimExcelDao.selectDetailsForExcel(notimasCd);
    }

    @Override
    public List<ClaimDTO> selectClaimFailMasterExcel(String chaCd) throws Exception {
        return claimExcelDao.selectClaimFailMasterExcel(chaCd);
    }

    @Override
    public List<ClaimItemDTO> selectFailDetailsForExcel(Map<String, Object> map) throws Exception {
        return claimExcelDao.selectFailDetailsForExcel(map);
    }

    @Override
    public ClaimDTO listForExcelTrans(Map<String, Object> map) throws Exception {
        return claimExcelDao.listForExcelTrans(map);
    }
}
