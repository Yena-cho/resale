package com.finger.shinhandamoa.sys.highwayManager.service;


import com.finger.shinhandamoa.data.table.mapper.HighwayMapper;
import com.finger.shinhandamoa.sys.highwayManager.dto.SysHighwayListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HighwayRcpServiceImpl implements HighwayRcpService {


    @Autowired
    private HighwayMapper highwayMapper;

    @Override
    public int sysHighwayRcpLoseListCnt(HashMap<String, Object> map) throws Exception {

        int result = highwayMapper.sysHighwayRcpLoseListCnt(map);
        return result;
    }

    @Override
    public List<HashMap<String, Object>> sysHighwayRcpLoseList(HashMap<String, Object> map) throws Exception {

        List<HashMap<String, Object>> result = highwayMapper.sysHighwayRcpLoseList(map);

        return result;
    }

    @Override
    public void vanoSend(HashMap<String, Object> map) throws Exception {
        highwayMapper.vanoSend(map);
    }

    @Override
    public int sysHighwaySendVanoListCnt(HashMap<String, Object> map) throws Exception {

        int result = highwayMapper.sysHighwaySendVanoListCnt(map);
        return result;
    }

    @Override
    public List<HashMap<String,Object>> sysHighwaySendVanoList(HashMap<String, Object> map) throws Exception {

        List<HashMap<String, Object>>  result = highwayMapper.sysHighwaySendVanoList(map);
        return result;
    }

    @Override
    public Map<String, Object> highwayUsedVanoInfo(SysHighwayListDTO sysHighwayListDTO){
        Map<String, Object> result = new HashMap<>();


        try {
            //고속도로 해당 기간 가상계좌 총건수
            int highwayUsedTotalCnt = highwayMapper.highwayUsedTotalCnt(sysHighwayListDTO);

            //고속도로 해당 기간 가상계좌 사용건수
            int highwayUsedCnt = highwayMapper.highwayUsedCnt(sysHighwayListDTO);

            //총건수- 사용건수 = 미사용건수
            int highwayUnUsedCnt = highwayUsedTotalCnt - highwayUsedCnt;


            //기간당 고속도로 할당건수
            int highwayAssignTotalCnt = highwayMapper.highwayAssignTotalCnt(sysHighwayListDTO);

            result.put("chaCd", sysHighwayListDTO.getChaCd());
            result.put("highwayUnUsedCnt", String.valueOf(highwayUnUsedCnt));
            result.put("highwayAssignTotalCnt", String.valueOf(highwayAssignTotalCnt));

            return result;
        }catch (Exception e ){
            e.printStackTrace();
        }
        return result;

    }



}
