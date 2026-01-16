package com.finger.shinhandamoa.sys.chaMgmt.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finger.shinhandamoa.bank.dao.BankMgmtDAOImpl;
import com.finger.shinhandamoa.bank.service.BankMgmtService;
import com.finger.shinhandamoa.sys.chaMgmt.dao.SysChaDAO;
import com.finger.shinhandamoa.sys.chaMgmt.dto.SysChaDTO;
import com.finger.shinhandamoa.sys.chaMgmt.dto.SysChaMgmtDTO;
import com.finger.shinhandamoa.sys.setting.dto.ChaUpdateDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysChaServiceImpl implements SysChaService {

    @Inject
    private SysChaDAO sysChaDao;

    @Inject
    private BankMgmtService bankMgmtService;

    @Value("${pg.allow.url}")
    private String pgAllowUrl;

    @Override
    public int selChaListCnt(HashMap<String, Object> map) throws Exception {
        return sysChaDao.selChaListCnt(map);
    }

    @Override
    public List<SysChaDTO> selChaList(HashMap<String, Object> map) throws Exception {
        return sysChaDao.selChaList(map);
    }

    @Override
    public int selNewChaCnt(HashMap<String, Object> map) throws Exception {
        return sysChaDao.selNewChaCnt(map);
    }

    @Override
    public List<SysChaDTO> selNewChaList(HashMap<String, Object> map) throws Exception {
        return sysChaDao.selNewChaList(map);
    }

    @Override
    public int getChangeChaListCnt(SysChaDTO body) throws Exception {
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        reqMap.put("calDateFrom", body.getCalDateFrom());
        reqMap.put("calDateTo", body.getCalDateTo());
        reqMap.put("chaCd", body.getChaCd());
        reqMap.put("chaName", body.getChaName());
        reqMap.put("statusCd", body.getStatusCd());
        reqMap.put("typeCd", body.getTypeCd());
        reqMap.put("searchOrderBy", body.getSearchOrderBy());

        return sysChaDao.getChangeChaListCnt(reqMap);
    }

    @Override
    public List<SysChaDTO> getChangeChaList(SysChaDTO body, int count, int start, int end) throws Exception {
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        reqMap.put("calDateFrom", body.getCalDateFrom());
        reqMap.put("calDateTo", body.getCalDateTo());
        reqMap.put("chaCd", body.getChaCd());
        reqMap.put("chaName", body.getChaName());
        reqMap.put("statusCd", body.getStatusCd());
        reqMap.put("typeCd", body.getTypeCd());
        reqMap.put("searchOrderBy", body.getSearchOrderBy());

        reqMap.put("start", start);
        reqMap.put("end", end);

        return sysChaDao.getChangeChaList(reqMap);
    }

    @Override
    public SysChaDTO changeChaListInfo(SysChaDTO body) throws Exception {
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        reqMap.put("pullDt", body.getPullDt());
        reqMap.put("clientId", body.getClientId());

        return sysChaDao.changeChaListInfo(reqMap);
    }

    @Override
    public void updateChangeChaInfo(SysChaDTO dto) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("pullDt", dto.getPullDt());
        map.put("clientId", dto.getClientId());

        sysChaDao.updateChangeChaInfo(map);
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public HashMap<String, Object> updateChaInfo(SysChaDTO dto) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<>();
        try {

            reqMap.put("chaCd", dto.getChaCd());
            reqMap.put("chast", dto.getChast());
            reqMap.put("maker", dto.getMaker());
            reqMap.put("flag", dto.getFlag()); // 수정구분 - 고객분류수정
            //자동이체관리 에서 수정할 시
            reqMap.put("bnkCd", dto.getBnkCd());

            CloseableHttpClient httpClient = HttpClients.createDefault();

            String url = pgAllowUrl;

            if (dto.getChast().equals("ST07")) { //승인거절일 경우
                log.info("승인거절상태====기관정보삭제 시작 후 PG 전달 ");
                reqMap.put("vactStatus", "N");
                sysChaDao.deleteXchalist(reqMap); //기관삭제
            } else if (dto.getChast().equals("ST06")) {

                log.info("승인성공상태====기관상태 사용으로 변경 후 PG 전달 ");
                reqMap.put("vactStatus", "Y");
                sysChaDao.updateChaInfo(reqMap); //기관상태 승인으로변경

            } else {
                log.info("승인거절상태====기관상태값 올바르지않음  ");
                map.put("retCode", "8888");
                map.put("retMsg", "기관 처리 상태값이 올바르지 않습니다. ");
                return map;
            }

            reqMap.put("mchtId", dto.getMchtId());

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(reqMap);
            HttpPost httpPost = new HttpPost(url);

            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            httpPost.setEntity(new StringEntity(jsonBody));


            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity httpEntity = response.getEntity();
            Map<String, Object> responseMap = new HashMap<>();
            if (httpEntity != null) {
                String reponseBody = EntityUtils.toString(httpEntity);
                responseMap = objectMapper.readValue(reponseBody, new TypeReference<Map<String, Object>>() {
                });


            }

            httpClient.close();


            log.info("resultCode {}",responseMap.get("code"));

            if (responseMap != null) {
                if (responseMap.get("code").equals("OK")) {
                    map.put("retCode", "0000");
                    map.put("retMsg", "정상");
                    return map;
                } else {
                    throw new Exception(); //통신 실패시 예외 던지기
                }

            } else {
                throw new Exception(); //통신 실패시  예외 던지기
            }
        }catch (Exception e){
            e.getMessage().toString();
            map.put("retCode", "9999");
            map.put("retMsg", "승인결과 PG송신중 에러발생");
            throw e;
        }


    }

    @Override
    public int selChaCount(HashMap<String, Object> map) throws Exception {
        return sysChaDao.selChaCount(map);
    }

    @Override
    public List<SysChaDTO> selChaInfoList(HashMap<String, Object> map) throws Exception {
        return sysChaDao.selChaInfoList(map);
    }

    @Override
    public List<SysChaDTO> selChrInfoList(HashMap<String, Object> map) throws Exception {
        return sysChaDao.selChrInfoList(map);
    }

    @Override
    public int selSmsSeq() throws Exception {
        return sysChaDao.selSmsSeq();
    }

    @Override
    public void insertSmsSysList(HashMap<String, Object> map) throws Exception {
        sysChaDao.insertSmsSysList(map);
    }

    @Override
    public void insertSmsSysMng(HashMap<String, Object> map) throws Exception {
        sysChaDao.insertSmsSysMng(map);
    }

    @Override
    public SysChaDTO selectJobhistoryLast(HashMap<String, Object> map) throws Exception {
        return sysChaDao.selectJobhistoryLast(map);
    }

    @Override
    public List<SysChaDTO> selCloseChaList(HashMap<String, Object> map) throws Exception {
        return sysChaDao.selCloseChaList(map);
    }

    @Override
    public int selCloseChaCount(HashMap<String, Object> map) throws Exception {
        return sysChaDao.selCloseChaCount(map);
    }

    @Override
    public void updateJobhistory(HashMap<String, Object> map) throws Exception {
        sysChaDao.updateJobhistory(map);
    }

    @Override
    public void insertWebUser(HashMap<String, Object> map) throws Exception {
        sysChaDao.insertWebUser(map);
    }

    @Override
    public void insertXadjgroupList(SysChaMgmtDTO dto) throws Exception {
        final HashMap<String, Object> reqMap = new HashMap<>();
        String chacd = dto.getChaCd();
        reqMap.put("chaCd", chacd);

        List<HashMap<String, Object>> preAccountList = bankMgmtService.getAgencyList(reqMap);
        List<HashMap<String, Object>> accountList = dto.getAccountList();

        List<String> newList = accountList.stream().map(x -> x.get("adjfiregKey").toString()).collect(Collectors.toList());
        for(HashMap<String, Object> pre : preAccountList) {
            String key = (String) pre.get("adjfiregKey");
            if(!newList.contains(key)) {
                sysChaDao.deleteXadjgroup(pre);
            }
        }

        for (HashMap<String, Object> each : accountList) {
            each.put("chaCd", chacd);
            each.put("maker", dto.getMaker());
            sysChaDao.insertXadjgroup(each);
            sysChaDao.updateSettleAccno(each);
        }
    }

    @Override
    public List<Map<String, Object>> getGroupList(String chaCd, String chaName, String groupId, String groupName, String status, int pageNo, int pageSize, String orderBy) {
        final int offset = pageNo * pageSize - pageSize;
        final String orderByClause;
        switch (orderBy) {
            case "id_asc":
                orderByClause = "A.CHAGROUPID ASC";
                break;
            case "name_asc":
                orderByClause = "A.CHAGROUPNAME ASC, A.CHAGROUPID ASC";
                break;
            case "memberCount_asc":
                orderByClause = "\"memberCount\" ASC, A.CHAGROUPNAME ASC, A.CHAGROUPID ASC";
                break;
            case "memberCount_desc":
                orderByClause = "\"memberCount\" DESC, A.CHAGROUPNAME ASC, A.CHAGROUPID ASC";
                break;
            default:
                orderByClause = "A.CHAGROUPNAME ASC, CHAGROUPID ASC";
                break;
        }
        
        return sysChaDao.selectGroup(chaCd, chaName, groupId, groupName, status, offset, pageSize, orderByClause);
    }

    @Override
    public long countGroup(String chaCd, String chaName, String groupId, String groupName, String status) {
        return sysChaDao.countGroup(chaCd, chaName, groupId, groupName, status);
    }

    @Override
    public String getNewGroupId() {
        return String.valueOf(sysChaDao.nextChaGroupId());
    }

    @Override
    public void createGroup(Map<String, String> chaGroupMap) {
        sysChaDao.insertXChaGroup(chaGroupMap);
    }

    @Override
    public void createWebUser(HashMap<String, Object> webUserMap) throws Exception {
        sysChaDao.insertWebUser(webUserMap);
    }

    @Override
    public Map<String, Object> getChaGroup(String groupId) {
        return sysChaDao.selectXChaGroupByGroupId(groupId);
    }

    @Override
    public Map<String, Object> getWebUser(String chaCd) {
        return sysChaDao.selectWebUserByChaCd(chaCd);
    }

    @Override
    public List<Map<String, Object>> getChaListByGroupId(String groupId) {
        return sysChaDao.selectXChaListByGroupId(groupId);
    }

    @Override
    public void removeChaFromGroup(String groupId, String chaCd) {
        sysChaDao.blankChaGroupIdOnXChaList(chaCd);
    }

    @Override
    public List<Map<String, Object>> getChaList(String query, int pageNo, int pageSize) {
        return sysChaDao.selectXChaList(query, new RowBounds((pageNo -1) * pageSize, pageSize));
    }

    @Override
    public void moveChaToGroup(String groupId, String chaCd) {
        sysChaDao.updateChaGroupIdOnXChaList(chaCd, groupId);
    }

    @Override
    public void modifyGroup(Map<String, String> chaGroupMap) {
        sysChaDao.updateXChaGroup(chaGroupMap);
    }

    @Override
    public void modifyWebUser(HashMap<String, Object> webUserMap) {
        sysChaDao.updateWebUser(webUserMap);
    }

	@Override
	public void resetFailCntAdm(HashMap<String, Object> reqMap) throws Exception {
		sysChaDao.resetFailCntAdm(reqMap);
	}

	@Override
	public void resetPwAdm(HashMap<String, Object> reqMap) throws Exception {
		sysChaDao.resetPwAdm(reqMap);
	}

    @Override
    public void updateSessionMax(HashMap<String, Object> reqMap) throws Exception {
        sysChaDao.updateSessionMax(reqMap);
    }

    @Override
    public int pgUpdateChaInfoListCnt(ChaUpdateDTO dto) throws Exception {
        return sysChaDao.pgUpdateChaInfoListCnt(dto);
    }

    @Override
    public List<ChaUpdateDTO> pgUpdateChaInfoList(ChaUpdateDTO dto) throws Exception {
        return sysChaDao.pgUpdateChaInfoList(dto);
    }

    @Override
    @Transactional
    public void pgUpdateCha(ChaUpdateDTO dto) throws Exception {
        //1. 업데이트전 기관 변경 정보 세팅
        ChaUpdateDTO reqDto = sysChaDao.pgUpdateChaBefore(dto);
        if(reqDto.getAmtchkty().equals("Y")){
            reqDto.setRcpDueChk("Y"); // 승인 방식일 경우 수납기간 rcp_due_chk 체크를 'Y' 으로
        }else {
            reqDto.setRcpDueChk("N");    //한도나 , 통지 방식일 경우 수납기간 rcp_due_chk 체크를 'N' 으로
        }
        //2. 업데이트
        sysChaDao.pgUpdateCha(reqDto);
        //3. 변경대기 목록의 상태를 변경
        sysChaDao.pgUpdateChaAfter(dto);

    }
}
