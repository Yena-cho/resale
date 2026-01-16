package com.finger.shinhandamoa.data.table.mapper;


import com.finger.shinhandamoa.sys.highwayManager.dto.SysHighwayListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface HighwayMapper {

    int sysHighwayRcpLoseListCnt(HashMap<String,Object> map) throws Exception ;

    List<HashMap<String,Object>> sysHighwayRcpLoseList(HashMap<String,Object> map) throws Exception ;

    void vanoSend(HashMap<String,Object> reqMap) throws Exception;

    int sysHighwaySendVanoListCnt(HashMap<String,Object> map) throws Exception ;

    List<HashMap<String,Object>> sysHighwaySendVanoList(HashMap<String,Object> map) throws Exception ;

    int highwayUsedCnt(SysHighwayListDTO sysHighwayListDTO) throws Exception ;
    int highwayUsedTotalCnt(SysHighwayListDTO sysHighwayListDTO) throws Exception ;
    int highwayAssignTotalCnt(SysHighwayListDTO sysHighwayListDTO) throws Exception ;

}
