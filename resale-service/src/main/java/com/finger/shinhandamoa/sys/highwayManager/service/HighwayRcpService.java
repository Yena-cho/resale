package com.finger.shinhandamoa.sys.highwayManager.service;


import com.finger.shinhandamoa.sys.highwayManager.dto.SysHighwayListDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public interface HighwayRcpService {


    /**
     *  누락된 수납 정보 리스트를 가벼옴
     */
    public List<HashMap<String,Object>> sysHighwayRcpLoseList (HashMap<String,Object> map )throws Exception ;

    public int sysHighwayRcpLoseListCnt (HashMap<String,Object> map )throws Exception ;

    public void vanoSend (HashMap<String,Object> map )throws Exception ;

    public int sysHighwaySendVanoListCnt (HashMap<String,Object> map )throws Exception ;

    public List<HashMap<String,Object>> sysHighwaySendVanoList(HashMap<String,Object> map) throws Exception ;

    Map<String, Object> highwayUsedVanoInfo(SysHighwayListDTO sysHighwayListDTO)throws Exception;


}
