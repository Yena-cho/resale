package com.finger.shinhandamoa.org.notimgmt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finger.shinhandamoa.org.notimgmt.dao.NotiMgmtDAO;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtBaseDTO;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtPdfResDTO;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtResDTO;

/**
 * @author suhlee
 * @date 2018. 4. 11.
 * @desc 고지서 출력/조회
 */
@Service
@Transactional
public class NotiMgmtServiceImpl implements NotiMgmtService {

    private static final Logger logger = LoggerFactory.getLogger(NotiMgmtServiceImpl.class);

    @Resource(name = "notiMgmtDao")
    private NotiMgmtDAO notiMgmtDao;

    /**
     * 기관관리자 세션
     */
    @Override
    public NotiMgmtBaseDTO selectNotiBaseInfo(String user) throws Exception {
        return notiMgmtDao.selectNotiBaseInfo(user);
    }

    /**
     * 고지서출력/조회 리스트
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public List<NotiMgmtResDTO> getNotiMgmtList(Map<String, Object> map) throws Exception {
        return notiMgmtDao.getNotiMgmtList(map);
    }

    /**
     * 고지서출력/조회 카운트
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public HashMap<String, Object> getNotiMgmtTotalCount(Map<String, Object> map) throws Exception {
        return notiMgmtDao.getNotiMgmtTotalCount(map);
    }

    /**
     * 고지서 출력양식 조회
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public String getExistsBillForm(HashMap<String, Object> map) throws Exception {
        return notiMgmtDao.getExistsBillForm(map);
    }

    /**
     * pdf 리스트
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public List<NotiMgmtPdfResDTO> getNotiBillList(HashMap<String, Object> map) throws Exception {
        return notiMgmtDao.getNotiBillList(map);
    }

    @Override
    public List<NotiMgmtPdfResDTO> getPrivewBillList(HashMap<String, Object> map) throws Exception {
        return notiMgmtDao.getPrivewBillList(map);
    }
}
