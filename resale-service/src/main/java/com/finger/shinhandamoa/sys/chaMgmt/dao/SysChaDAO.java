package com.finger.shinhandamoa.sys.chaMgmt.dao;

import com.finger.shinhandamoa.sys.chaMgmt.dto.SysChaDTO;
import com.finger.shinhandamoa.sys.setting.dto.ChaUpdateDTO;
import org.apache.ibatis.session.RowBounds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SysChaDAO {

	// 기관검색 - total count
	public int selChaListCnt(HashMap<String, Object> map) throws  Exception;
	// 기관검색
	public List<SysChaDTO> selChaList(HashMap<String, Object> map) throws  Exception;
	// 신규 기관 목록 - total count
	public int selNewChaCnt(HashMap<String, Object> map) throws Exception;
	// 신규 기관 목록
	public List<SysChaDTO> selNewChaList(HashMap<String, Object> map) throws Exception;
	// 변경대기목록 - total count
	public int getChangeChaListCnt(HashMap<String, Object> map) throws Exception;
	// 변경대기목록
	public List<SysChaDTO> getChangeChaList(HashMap<String, Object> map) throws Exception;	
	// 변경대기목록 > 상세보기
	public SysChaDTO changeChaListInfo(HashMap<String, Object> map) throws Exception;
	// 변경대기목록 > 실행
	public void updateChangeChaInfo(HashMap<String, Object> map) throws Exception;
	// 고객분류 수정
	public void updateChaInfo(HashMap<String, Object> map) throws Exception;
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
	// 로그인정보 등록
	public void insertWebUser(HashMap<String, Object> map) throws Exception;
	// 다계좌등록
	void insertXadjgroup(HashMap<String, Object> map) throws Exception;
	// 다계좌삭제
	void deleteXadjgroup(HashMap<String, Object> map) throws Exception;
	// 가상계좌 정산목록 계좌번호 변경
	void updateSettleAccno(HashMap<String, Object> map) throws Exception;

    long countGroup(String chaCd, String chaName, String groupId, String groupName, String status);

    List<Map<String,Object>> selectGroup(String chaCd, String chaName, String groupId, String groupName, String status, int offset, int limit, String orderBy);

    long nextChaGroupId();

	void insertXChaGroup(Map<String, String> chaGroupMap);


    Map<String,Object> selectXChaGroupByGroupId(String groupId);

	Map<String,Object> selectWebUserByChaCd(String chaCd);

	List<Map<String,Object>> selectXChaListByGroupId(String groupId);

	void blankChaGroupIdOnXChaList(String chaCd);

	List<Map<String,Object>> selectXChaList(String query, RowBounds rowBounds);

	void updateChaGroupIdOnXChaList(String chaCd, String groupId);

	void updateXChaGroup(Map<String, String> chaGroupMap);

	void updateWebUser(HashMap<String, Object> webUserMap);

	public void resetFailCntAdm(HashMap<String, Object> reqMap) throws Exception;

	public void resetPwAdm(HashMap<String, Object> reqMap) throws Exception;

    void updateSessionMax(HashMap<String,Object> reqMap) throws Exception;

	// PG 변경대기목록 - total count
	public int pgUpdateChaInfoListCnt(ChaUpdateDTO body) throws Exception;

	// PG 변경대기목록 - List
	public List<ChaUpdateDTO> pgUpdateChaInfoList(ChaUpdateDTO body) throws Exception;

	// PG 변경대기목록 - 업데이트
	public ChaUpdateDTO pgUpdateChaBefore(ChaUpdateDTO body) throws Exception;

	// PG 변경대기목록 - 업데이트
	public void pgUpdateCha(ChaUpdateDTO body) throws Exception;

	// PG 변경대기목록 - 업데이트 후처리
	public void pgUpdateChaAfter(ChaUpdateDTO body) throws Exception;

	void deleteXchalist(HashMap<String,Object> reqMap) throws Exception;

}
