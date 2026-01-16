package kr.co.finger.damoa.shinhan.agent.dao;

import kr.co.finger.damoa.model.msg.AggregateMessage;
import kr.co.finger.damoa.model.msg.NoticeMessage;
import kr.co.finger.shinhandamoa.data.table.model.TransApiRelayData;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 전문서버 DB 처리 Dao 클래스.
 * 여러 데이터를 한번에 저장하는 기능때문에 SqlSession 객체를 이용함.
 */
public interface DamoaDao {
    /**
     * 전문테이블에 입력.
     *
     * @param map the map
     */
    public void insertTransDataAcct(Map<String, Object> map);

    /**
     * 가상계좌 예금주 찾기.
     *
     * @param vano the vano
     * @return string
     */
    public String findVirtualAccountName(String vano);

    /**
     * 전문일련번호 추출
     *
     * @return long
     */
    public long findDealSeq();

    /**
     * vareq 테이블 입력
     *
     * @param map the map
     */
    void insertVaReq(Map<String, Object> map);

    /**
     * vareq 테이블 업데이트
     *
     * @param map the map
     */
    void updateVaReq(Map<String, Object> map);

    /**
     * 수납 마스터 입력
     *
     * @param map the map
     */
    void insertRcpMaster(Map<String, Object> map);

    /**
     * 수납 디테일 입력
     *
     * @param map the map
     */
    void insertRcpDetail(Map<String, Object> map);

    /**
     * 현금영수증 입력
     *
     * @param map the map
     */
    void insertCashMaster(Map<String, Object> map);

    /**
     * 전문테이블에 입력
     *
     * @param typeCode    전문종류
     * @param dealSeqNo   전문일련번호
     * @param destination R 받은전문, S 보낸전문
     * @param msg         전문..
     */
    //void insertTransDataAcct(String typeCode,String dealSeqNo, String destination, String msg);
    TransApiRelayData insertTransDataAcct(String typeCode,String dealSeqNo, String destination, String msg);

    /**
     * 집계전문 데이터 수집
     *
     * @param corpCd       기관코드
     * @param startRcpDate 시작일
     * @param endRcpDate   종료일
     * @return list
     */
    public List<Map<String, Object>> findAggregateMsgInfo(String corpCd, String startRcpDate,String endRcpDate);

    /**
     * 집계전문테이블 입력
     *
     * @param nowString    시간
     * @param corpCd       기관
     * @param vaNo         가상계좌번호
     * @param depositAmout 입금액
     * @param amountOfFee  수수료
     * @param type         CNCL (취소), RCP(수납)
     * @param dealSeqNo    전문일련번호
     */
    void insertDepositMsgHistory(String nowString,String corpCd,String vaNo,String depositAmout, String amountOfFee,String type,String dealSeqNo);

    /**
     * rcpmascd 시퀀스 조회한다.
     *
     * @return string
     */
    String findRcpMasCd();

    /**
     * 기관정보 수집
     * 다른 곳에서 같은 쿼리를 사용하여 데이터 처리하니..
     * 항목을 추가하는 것은 괜찮으나 삭제는 곤란함.
     * @param corpCode 기관코드
     * @return map
     */
    Map<String,Object> findChaSimpleInfo(String corpCode);

    /**
     * 가상계좌와 매핑된 기관과 사용자 조회
     *
     * @param vano 가상계좌번호
     * @return map
     */
    Map<String,Object> findChaCusInfo(String vano);

    /**
     * 입금취소를 위한 수납정보 수집.
     *
     * @param chacd 기관코드
     * @param vano  가상계좌번호
     * @return list
     */
    List<Map<String,Object>> findLastRcp(String chacd, String vano);

    /**
     * 청구상세 업데이트
     *
     * @param notimascd 청구번호
     * @param state     상태코드 PA00:임시, PA02:미납, PA03:수납, PA04:일부납, PA05:초과납
     */
    void updateNotimas(String notimascd, String state);

    /**
     * 수납마스터 업데이트
     *
     * @param rcpmascd 수납번호
     * @param state    상태코드 수납:PA03  취소:PA09
     */
    void updateRcpMas(String rcpmascd,String state);

    /**
     * 현금영수증 취소
     * 쿼리는 수정해야 함. TODO
     *
     * @param rcpmasd the rcpmasd
     */
    void updateCashCancel(String rcpmasd);

    /**
     * 청구상세 업데이트
     *
     * @param notidetcd 청구상세번호
     * @param st        PA00:임시, PA02:미납, PA03:수납, PA04:일부납, PA05:초과납
     */
    void updateNotidetWithNotidetcd(String notidetcd, String st);

    /**
     * 상세수납 상태 처리.
     *
     * @param rcpmasd the rcpmasd
     * @param st      수납:PA03  취소:PA09
     */
    void updateRcpDet(String rcpmasd, String st);

    /**
     * 은행 집계데이터 저장.
     *
     * @param aggregateMessage the aggregate message
     * @throws ParseException the parse exception
     */
    void insertRcpSum(AggregateMessage aggregateMessage) throws ParseException;

    /**
     * 은행 집계데이터 저장되어 있는지 확인..
     *
     * @param aggregateMessage the aggregate message
     * @return map
     * @throws ParseException the parse exception
     */
    Map<String,Object> findRcpSum(AggregateMessage aggregateMessage) throws ParseException;

    /**
     * 거래일련번호 중복체크..
     *
     * @param dealSeqNo 거래일련번호
     * @param nowDate   현재시간.
     * @return list
     */
    List<Map<String, Object>> containsSeqNo(String dealSeqNo, String nowDate);

    /**
     * 취소 원거래 건이 있는지 확인...
     *
     * @param dealSeqNo   거래일련번호
     * @param nowDate     현재시간
     * @param msgTypeCode 전문타입
     * @return list
     */
    List<Map<String, Object>> containsCanCel(String dealSeqNo, String nowDate, String msgTypeCode);

    /**
     * 취소 원거래 건이 있는지 확인...
     *
     * @param transdt 거래일
     * @param trcode 전문코드
     * @param dealSeqNo   거래일련번호
     * @return list
     */
    int countTransDataAcct(String transdt, String trcode, String dealSeqNo);

    /**
     * 가상계좌번호를 확인함.
     *
     * @param corpCode  기관코드
     * @param accountNo 가상계좌번호
     * @return map
     */
    Map<String,Object> findAccountNo(String corpCode,String accountNo);

    /**
     * 취소시에 원거래 전문 확인...
     *
     * @param dealSeqNo 거래일련번호
     * @param nowDate   현재시간
     * @param typeCode  전문종류..
     * @return list
     */
    List<Map<String, Object>> containsSeqNoAtCancel(String dealSeqNo, String nowDate,String typeCode);

    /**
     * 수납마스터 취소처리.
     *
     * @param rcpMasCd 수납번호
     */
    void updateRcpCancel(String rcpMasCd);

    /**
     * 수납디테일 취소처리
     *
     * @param rcpMasCd 수납번호
     */
    void updateRcpDetCancel(String rcpMasCd);

    /**
     * 취소할 수납 정보 수집...
     *
     * @param rcpMasCd 수납번호
     * @return list
     */
    List<Map<String, Object>> findRcpCanCelInfo(String rcpMasCd);

    /**
     * 취소할 청구 정보 수집...
     *
     * @param notimasCd 청구번호
     * @return list
     */
    List<Map<String, Object>> findNotiCanCelInfo(String notimasCd);

    /**
     * 원거래 전문데이터 수집..
     *
     * @param dealSeqNo 거래일련번호
     * @param typeCode  전문타입..
     * @return list
     */
    List<String> findOriginalMsg(String dealSeqNo, String typeCode);

    /**
     * 취소할 수납건 조회...
     *
     * @param dealSeqNo 거래일련번호
     * @param payDay    수납일...
     * @return list
     */
    List<Map<String,Object>> findCancel(String dealSeqNo,String payDay);

    /**
     * 입금(이체), 취소 처리건 중에 성공적인 전문데이터 처리..
     *
     * @param typeCode    전문종류..
     * @param dealSeqNo   거래일련번호
     * @param destination S(보낸전문) ,R(받은전문)
     * @param msg         전문
     */
    public void insertOkTransDataAcct(String typeCode, String dealSeqNo, String destination, String msg);

    /**
     * 기관상태 테이블에 입력
     *
     * @param nowDate 날짜
     * @param corpCd  기관코드
     * @param status  상태 (START,FINISH)
     */
    void insertCorpCdStatus(String nowDate, String corpCd,String status);

    /**
     * 기관상태 테이블에서 기관 정보가 몇개 있는지 확인
     *
     * @param nowDate 현재날짜
     * @param corpCd  기관코드
     * @return integer
     */
    Integer findCorpCd(String nowDate, String corpCd);

    /**
     * 기관상태 테이블 업데이트
     *
     * @param nowDate 날짜
     * @param corpCd  기관코드
     * @param status  START,FINISH
     */
    void updateCorpCdStatus(String nowDate, String corpCd, String status);

    /**
     * 기관상태 조회
     *
     * @param nowDate 날짜
     * @param corpCd  기관코드..
     * @return string
     */
    String findCorpCdStatus(String nowDate, String corpCd);

    /**
     * 모든 기관 상태 종료로 변경
     *
     * @param now 날짜
     */
    void updateAllFinishStatus(String now);

    /**
     * 현금영수증에서 사용할 간단한 기관 정보 수집
     *
     * @param chacd 기관코드
     * @return map
     */
    Map<String,Object> findChaInfo(String chacd);

    /**
     * sms 테이블 입력
     *
     * @param imap the imap
     */
    void insertSmsReq(Map<String, Object> imap);

    /**
     * 청구 정보 수집 (수납된 내용 적용이 됨). 조회필드가 적음.
     *
     * @param corpCode       기관코드
     * @param virtualAccount 가상계좌번호
     * @return list
     */
    List<Map<String,Object>> findSimpleNotiMasDet3(String corpCode, String virtualAccount);

    /**
     * 청구 정보 수집 (수납된 내용 적용이 됨). 조회필드가 많음.
     *
     * @param corpCode       기관코드
     * @param virtualAccount 가상계좌번호
     * @return list
     */
    List<Map<String,Object>> findNotiMasDet3(String corpCode, String virtualAccount);

    /**
     * 취소시에 입금이력 삭제..
     *
     * @param noticeMessage 입력(이체) 정보를 가진 객체.
     */
    void removeDepositMsgHistory(NoticeMessage noticeMessage);

    void deleteCashmas(String rcpMascd);

    Map<String, Object> findAccountInfo(String fgcd, String accountNo);

    Map<String, Object> getLimitPerVano(String vano);

}
