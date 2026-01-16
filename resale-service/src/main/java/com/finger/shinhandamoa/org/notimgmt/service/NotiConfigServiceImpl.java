package com.finger.shinhandamoa.org.notimgmt.service;

import java.util.HashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finger.shinhandamoa.org.notimgmt.dao.NotiMgmtConfigDAO;
import com.finger.shinhandamoa.org.notimgmt.dao.NotiMgmtDAO;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiConfigDTO;

/**
 * 
 * @author suhlee
 * @date    2018. 4. 23.
 * @desc    고지서 설정
 */
@Service
@Transactional
public class NotiConfigServiceImpl implements NotiConfigService {

	private static final Logger logger = LoggerFactory.getLogger(NotiMgmtServiceImpl.class);
	
	@Resource(name = "notiConfigDao")
	private NotiMgmtConfigDAO notiConfigDao;
	
	/**
	 * 고지서설정 조회
	 * @param chacd
	 * @return
	 * @throws Exception
	 */
	@Override
	public NotiConfigDTO selectXbillForm(String chaCd) throws Exception {
		return notiConfigDao.selectXbillForm(chaCd);
	}
	
	/**
	 * 고지서설정 Ajax 조회
	 * @param chacd
	 * @return
	 * @throws Exception
	 */
	@Override
	public NotiConfigDTO selectedXbillForm(HashMap<String, Object> map) throws Exception {
		return notiConfigDao.selectedXbillForm(map);
	}
	
	/**
	 * 고지서설정 수정 및 등록
	 * @param chacd
	 * @return
	 * @throws Exception
	 */
	@Override
	public int saveNotiConfig(HashMap<String, Object> map) throws Exception {
		return notiConfigDao.saveNotiConfig(map);
	}
	
	/**
	 * 고지서설정 수납기관, 연락처 조회
	 * @param chacd
	 * @return
	 * @throws Exception
	 */
	@Override
	public HashMap<String, Object> selectXchalist(String chaCd) throws Exception {
		return notiConfigDao.selectXchalist(chaCd);
	}
}
