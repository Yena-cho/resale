package com.finger.shinhandamoa.util.service;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.util.dao.CodeDAO;
import com.finger.shinhandamoa.util.dto.CodeDTO;


/**
 * @author by puki
 * @date 2018. 4. 13.
 * @desc 공통코드 ServiceImpl
 */
@Service
public class CodeServiceImpl implements CodeService {

    @Inject
    private CodeDAO codeDao;

    // 청구항목
    @Override
    public List<CodeDTO> claimItemCd(String chaCd) throws Exception {
        return codeDao.claimItemCd(chaCd);
    }

    @Override
    public List<CodeDTO> cusGubnCd(String chaCd) throws Exception {
        return codeDao.cusGubnCd(chaCd);
    }

    @Override
    public int cusGubnCdCnt(String chaCd) throws Exception {
        return codeDao.cusGubnCdCnt(chaCd);
    }

    @Override
    public List<CodeDTO> moneyPassbookName(String chaCd) throws Exception {
        return codeDao.moneyPassbookName(chaCd);
    }

    @Override
    public List<CodeDTO> claimItemForExcelTrans(HashMap<String, Object> map) throws Exception {
        return codeDao.claimItemForExcelTrans(map);
    }
}
