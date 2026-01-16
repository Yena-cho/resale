package com.finger.shinhandamoa.sys.setting.service;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.sys.setting.dao.SettingDAO;
import com.finger.shinhandamoa.sys.setting.dto.AdminInfoDTO;

/**
 * @author  by puki
 * @date    2018. 5. 21.
 * @desc    최초생성
 * @version 
 * 
 */
@Service
public class SettingServiceImpl implements SettingService {

	@Inject
	private SettingDAO settingDao;

	@Override
	public int selAccAuthCnt(HashMap<String, Object> map) throws Exception {
		return settingDao.selAccAuthCnt(map);
	}

	@Override
	public List<AdminInfoDTO> selAccAuthList(HashMap<String, Object> map) throws Exception {
		return settingDao.selAccAuthList(map);
	}

	@Override
	public AdminInfoDTO selAccAuthDetail(String admId) throws Exception {
		return settingDao.selAccAuthDetail(admId);
	}

	@Override
	public void accAuthModify(HashMap<String, Object> map) throws Exception {
		settingDao.accAuthModify(map);
	}

	@Override
	public void accAuthDelete(String admId) throws Exception {
		settingDao.accAuthDelete(admId);
	}

	@Override
	public int selAdmId(String admId) throws Exception {
		return settingDao.selAdmId(admId);
	}
	
}
