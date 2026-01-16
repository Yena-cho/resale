package com.finger.shinhandamoa.sys.chaMgmt.service;

import com.finger.shinhandamoa.sys.chaMgmt.dto.SysChaDTO;
import com.finger.shinhandamoa.sys.chaMgmt.dto.SysChaMgmtDTO;
import com.finger.shinhandamoa.sys.setting.dto.ChaUpdateDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 기관 관리 서비스
 * 
 * @author wisehouse@finger.co.kr
 */
public interface SysChaService {
    // 기관검색 - total count
    public int selChaListCnt(HashMap<String, Object> map) throws Exception;

    // 기관검색
    public List<SysChaDTO> selChaList(HashMap<String, Object> map) throws Exception;

    // 신규 기관 목록 - total count
    public int selNewChaCnt(HashMap<String, Object> map) throws Exception;

    // 신규 기관 목록
    public List<SysChaDTO> selNewChaList(HashMap<String, Object> map) throws Exception;

    // 변경대기목록 - total count
    public int getChangeChaListCnt(SysChaDTO body) throws Exception;

    // 변경대기목록
    public List<SysChaDTO> getChangeChaList(SysChaDTO body, int count, int start, int end) throws Exception;
    
    // 변경대기목록 > 상세보기
    public SysChaDTO changeChaListInfo(SysChaDTO body) throws Exception;

    // 변경대기목록 > 실행
    public void updateChangeChaInfo(SysChaDTO dto) throws Exception;

    // 고객분류 수정
    public HashMap<String, Object> updateChaInfo(SysChaDTO dto) throws Exception;

    // 이용 기관 목록 - total count
    public int selChaCount(HashMap<String, Object> map) throws Exception;

    // 이용 기관 목록
    public List<SysChaDTO> selChaInfoList(HashMap<String, Object> map) throws Exception;

    // 담당자 핸드폰번호, 이메일 조회
    public List<SysChaDTO> selChrInfoList(HashMap<String, Object> map) throws Exception;

    // sms seq 조회
    public int selSmsSeq() throws Exception;

    // sms 발송내역
    public void insertSmsSysList(HashMap<String, Object> map) throws Exception;

    // sms 발송정보
    public void insertSmsSysMng(HashMap<String, Object> map) throws Exception;

    // 기관검증
    public SysChaDTO selectJobhistoryLast(HashMap<String, Object> map) throws Exception;

    // 기관검증 - 휴폐업조회
    public List<SysChaDTO> selCloseChaList(HashMap<String, Object> map) throws Exception;

    // 기관검증 - TOTAL COUNT
    public int selCloseChaCount(HashMap<String, Object> map) throws Exception;

    // 기관검증 - 강제종료
    public void updateJobhistory(HashMap<String, Object> map) throws Exception;

    //이용승인 후 webuser create
    public void insertWebUser(HashMap<String, Object> map) throws Exception;

    // 기관관리 - 다계좌목록 등록, 수정, 삭제
    public void insertXadjgroupList(SysChaMgmtDTO dto) throws Exception;

    List<Map<String, Object>> getGroupList(String chaCd, String chaName, String groupId, String groupName, String status, int pageNo, int pageSize, String orderBy);

    long countGroup(String chaCd, String chaName, String groupId, String groupName, String status);

    String getNewGroupId();

    void createGroup(Map<String, String> chaGroupMap);

    void createWebUser(HashMap<String, Object> webUserMap) throws Exception;

    Map<String,Object> getChaGroup(String groupId);

    Map<String,Object> getWebUser(String chaCd);

    List<Map<String,Object>> getChaListByGroupId(String groupId);

    void removeChaFromGroup(String groupId, String chaCd);

    List<Map<String,Object>> getChaList(String query, int pageNo, int pageSize);

    void moveChaToGroup(String groupId, String chaCd);

    void modifyGroup(Map<String, String> chaGroupMap);

    void modifyWebUser(HashMap<String, Object> webUserMap);

	public void resetFailCntAdm(HashMap<String, Object> reqMap) throws Exception;

	public void resetPwAdm(HashMap<String, Object> reqMap) throws Exception;

    void updateSessionMax(HashMap<String, Object> reqMap) throws Exception;


    // PG 변경대기목록 - total count
    public int pgUpdateChaInfoListCnt(ChaUpdateDTO body) throws Exception;

    // PG 변경대기목록 - List
    public List<ChaUpdateDTO> pgUpdateChaInfoList(ChaUpdateDTO body) throws Exception;

    // PG 변경대기목록 - 업데이트
    public void pgUpdateCha(ChaUpdateDTO body) throws Exception;


}
