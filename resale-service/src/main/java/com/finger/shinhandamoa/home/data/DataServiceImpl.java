package com.finger.shinhandamoa.home.data;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.main.dto.NoticeDTO;

@Service
public class DataServiceImpl implements DataService {

	@Autowired
	private DataDAO dataDao;

	@Override
	public void orgPasswordUpdate(HashMap<String, Object> map) throws Exception {
		dataDao.orgPasswordUpdate(map);
	}
	
	@Override
	public void orgCusPassUpdate(HashMap<String, Object> map) throws Exception {
		dataDao.orgCusPassUpdate(map);
		
	}

	@Override
	public void cusInfoShaEncoder(HashMap<String, Object> map) throws Exception {
		dataDao.cusInfoShaEncoder(map);
	}

	@Override
	public void payerPasswordUpdate(HashMap<String, Object> map) throws Exception {
		dataDao.payerPasswordUpdate(map);
	}

	@Override
	public List<NoticeDTO> list(HashMap<String, Object> map) throws Exception {
		return dataDao.list(map);
	}

	@Override
	public void updateCrypt(HashMap<String, Object> map) throws Exception {
		dataDao.updateCrypt(map);
	}

	@Override
	public void insertCrypt(HashMap<String, Object> map) throws Exception {
		dataDao.insertCrypt(map);
	}	
}
