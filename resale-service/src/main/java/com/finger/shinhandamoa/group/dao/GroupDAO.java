package com.finger.shinhandamoa.group.dao;

import java.util.HashMap;
import java.util.List;

import com.finger.shinhandamoa.bank.dto.BankReg01DTO;
import com.finger.shinhandamoa.group.dto.GroupDTO;
import org.apache.ibatis.session.RowBounds;

public interface GroupDAO {
    public int selGroupCount(HashMap<String, Object> map) throws Exception;
    public List<GroupDTO> selGroupList(HashMap<String, Object> map) throws Exception;
    public GroupDTO selGroupInfo(HashMap<String, Object> map) throws Exception;

    public int selGroupPaymentCount(HashMap<String, Object> map) throws Exception;
    public long selGroupPaymentSum(HashMap<String, Object> map) throws Exception;
    public List<GroupDTO> selGroupPaymentList(HashMap<String, Object> map) throws Exception;
    public List<GroupDTO> selGroupPaymentExcelList(HashMap<String, Object> map) throws Exception;

    public int getCollectorListTotalCount(HashMap<String, Object> map) throws Exception;
    public List<BankReg01DTO> getCollectorListAll(HashMap<String, Object> map) throws Exception;
}
