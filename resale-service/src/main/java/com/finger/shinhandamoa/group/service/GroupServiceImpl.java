package com.finger.shinhandamoa.group.service;

import java.util.HashMap;
import java.util.List;

import com.finger.shinhandamoa.bank.dto.BankReg01DTO;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.group.dao.GroupDAO;
import com.finger.shinhandamoa.group.dto.GroupDTO;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupDAO groupDao;

    @Override
    public int selGroupCount(HashMap<String, Object> map) throws Exception {
        return groupDao.selGroupCount(map);
    }

    @Override
    public List<GroupDTO> selGroupList(HashMap<String, Object> map) throws Exception {
        return groupDao.selGroupList(map);
    }

    @Override
    public GroupDTO selGroupInfo(HashMap<String, Object> map) throws Exception {
        return groupDao.selGroupInfo(map);
    }

    @Override
    public int selGroupPaymentCount(HashMap<String, Object> map) throws Exception {
        return groupDao.selGroupPaymentCount(map);
    }

    @Override
    public long selGroupPaymentSum(HashMap<String, Object> map) throws Exception {
        return groupDao.selGroupPaymentSum(map);
    }

    @Override
    public List<GroupDTO> selGroupPaymentList(HashMap<String, Object> map) throws Exception {
        return groupDao.selGroupPaymentList(map);
    }

    @Override
    public List<GroupDTO> selGroupPaymentExcelList(HashMap<String, Object> map) throws Exception {
        return groupDao.selGroupPaymentExcelList(map);
    }

    @Override
    public int getCollectorListTotalCount(HashMap<String, Object> map) throws Exception {
        return groupDao.getCollectorListTotalCount(map);
    }

    @Override
    public List<BankReg01DTO> getCollectorListAll(HashMap<String, Object> map) throws Exception {
        return groupDao.getCollectorListAll(map);
    }
}
