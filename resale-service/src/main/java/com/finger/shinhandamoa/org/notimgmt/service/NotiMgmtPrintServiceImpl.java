package com.finger.shinhandamoa.org.notimgmt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finger.shinhandamoa.org.notimgmt.dao.NotiMgmtPrintDAO;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiConfigDTO;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtPrintResDTO;

@Service
@Transactional
public class NotiMgmtPrintServiceImpl implements NotiMgmtPrintService {

	@Resource(name = "notiMgmtPrintDao")
	private NotiMgmtPrintDAO notiMgmtPrintDao;
	
	/**
	 * 고지서 출력의뢰 주소조회
	 * @param chacd
	 * @return
	 * @throws Exception
	 */
	@Override
	public NotiMgmtPrintResDTO selectReqAddress(String chaCd) throws Exception {
		return notiMgmtPrintDao.selectReqAddress(chaCd);
	}
	
	/**
	 * 고지서 출력의뢰 리스트
	 * @param chacd
	 * @return
	 * @throws Exception
	 */
	public List<NotiMgmtPrintResDTO> pageSelectReqNotiMas(HashMap<String, Object> map) throws Exception {
		return notiMgmtPrintDao.pageSelectReqNotiMas(map);
	}
	
	/**
	 * 선택한 월 고지서 출력의뢰 건수
	 */
	public HashMap<String,Object> selectMonReqNotiMas(HashMap<String, Object> map) throws Exception {
		return notiMgmtPrintDao.selectMonReqNotiMas(map);
	}
	
	/**
	 * 고지서출력의뢰 조회
	 * @param body
	 * @return
	 */
	public int selectXnotimasReq(HashMap<String, Object> map) throws Exception {
		return notiMgmtPrintDao.selectXnotimasReq(map);
	}
	
	/**
	 * 고지서출력의뢰 삭제
	 * * @param body
	 * @return
	 */
	public int deleteXnotimasReq(HashMap<String, Object> map) throws Exception {
		return notiMgmtPrintDao.deleteXnotimasReq(map);
	}
	
	/**
	 * 고지서출력의뢰 저장
	 * @param body
	 * @return
	 */
	public int saveXnotimasReq(HashMap<String, Object> map) throws Exception {
		return notiMgmtPrintDao.saveXnotimasReq(map);
	}

	@Override
	public int saveXnotimasReqDet(HashMap<String, Object> map) throws Exception {
		return notiMgmtPrintDao.saveXnotimasReqDet(map);
	}

	/**
	 * 고지내용 row check
	 */
	@Override
	public NotiConfigDTO cont1LengthCheck(HashMap<String, Object> map) throws Exception {
		return notiMgmtPrintDao.cont1LengthCheck(map);
	}
	/**
	 * 고지출력의뢰 내역 카운트
	 */
	@Override
	public int countReqNotiMas(String chaCd) throws Exception {
		return notiMgmtPrintDao.countReqNotiMas(chaCd);
	}

	@Override
	public int printCount(HashMap<String, Object> map) throws Exception {
		return notiMgmtPrintDao.printCount(map);
	}
	
}
