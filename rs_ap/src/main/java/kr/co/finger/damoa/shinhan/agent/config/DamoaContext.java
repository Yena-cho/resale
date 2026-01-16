package kr.co.finger.damoa.shinhan.agent.config;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.damoa.model.msg.NoticeMessage;
import kr.co.finger.damoa.shinhan.agent.dao.DamoaDao;
import kr.co.finger.damoa.shinhan.agent.model.CancelBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DamoaContext {

    @Autowired
    private DamoaDao damoaDao;

    private Map<String, Date> CANCEL_SEQ = new HashMap<>();


    private Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * 취소 전문 조회
     * 
     * @param noticeMessage
     * @return
     */
    public boolean hasCancelKey(NoticeMessage noticeMessage) {
        // 전문 번호
        final String dealSeqNo = noticeMessage.getDealSeqNo();
        // 거래일
        final String transdt = DateUtils.toDateString(new Date());
        // 전문구분코드
        final String msgTypeCode = noticeMessage.getMsgTypeCode();
        // 거래구분코드
        final String dealTypecode = noticeMessage.getDealTypeCode();
        final String trcode = msgTypeCode + dealTypecode;

        final int size = damoaDao.countTransDataAcct(transdt, trcode, dealSeqNo);
        LOG.debug("취소 전문 개수: {}", size);
        
        switch (size) {
            case 1:
                return false;
            case 0:
                throw new RuntimeException("취소 조회 중 오류 발행");
            default:
                return true;
        }
    }

    public void insertCorpCdStatus(String corpCd, String status) {
        try {
            String nowDate = DateUtils.toDateString(new Date());
            Integer count = damoaDao.findCorpCd(nowDate, corpCd);
            if (count == null || count==0) {
                damoaDao.insertCorpCdStatus(nowDate, corpCd,status);
            } else {
                damoaDao.updateCorpCdStatus(nowDate, corpCd,status);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }

    }

    public String findCorpCdStatus(String corpCd) {
        String nowDate = DateUtils.toDateString(new Date());
        return damoaDao.findCorpCdStatus(nowDate, corpCd);
    }

    public Map<String,Object> findChaSimpleInfo(String corpCode) {
        Map<String,Object> map = damoaDao.findChaSimpleInfo(corpCode);
        return map;
    }

    public Map<String, Object> findAccountNo(String chacd,String accountNo) {
        Map<String,Object> map = damoaDao.findAccountNo(chacd,accountNo);
        return map;
    }

    public String findCusName(String vano) {
        String name = damoaDao.findVirtualAccountName(vano);
        return name;
    }



    private String cancelKey(String date, String dealSeqNo) {
        return date + "_" + dealSeqNo;
    }

    public CancelBean findCancel(String dealSeqNo, String date) {
        final List<Map<String,Object>> mapList = damoaDao.findCancel(dealSeqNo,date);
        if (mapList == null || mapList.size()==0) {
            LOG.info("수납건이 없음.. "+dealSeqNo+"\t"+date);
            return null;
        }

        CancelBean cancelBean = new CancelBean(dealSeqNo,date);
        for (Map<String, Object> map : mapList) {
            cancelBean.add(Maps.getValue(map, "rcpmascd"),Maps.getValue(map, "notimascd"));
        }
        return cancelBean;
    }


    /**
     * T전문테이블을 변경해서 CANCEL 여부를 적용해야 함.
     * TODO reset 처리 로직 봐야 함.
     * @param dealSeqNo
     * @param date
     */
    public void updateMsgCancel(String dealSeqNo,String date) {
        LOG.info("수납데이터가 없어 취소하지 못해 취소 거래일련번호 등록...[시간]"+date+"[거래일련번호]"+dealSeqNo);
        synchronized (CANCEL_SEQ) {
            CANCEL_SEQ.put(cancelKey(date,dealSeqNo),new Date());
        }
        //원거래 cancel 처리함.
//        damoaDao.updateOriginalMsgCancel(date, dealSeqNo);

    }

    public boolean hasCancelRequest(String dealSeqNo,String date) {
        synchronized (CANCEL_SEQ) {
            if (CANCEL_SEQ.containsKey(cancelKey(date,dealSeqNo))) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void cleanupCancelSeq() {
        synchronized (CANCEL_SEQ) {
            LOG.info("cleanupCancelSeq....");
            List<String> list = new ArrayList<>();
            String now = DateUtils.toDateString(new Date());
            for (String cancelKey : CANCEL_SEQ.keySet()) {

                Date date = CANCEL_SEQ.get(cancelKey);
                String _date = DateUtils.toDateString(date);
                if (now.equalsIgnoreCase(_date)) {
                    LOG.info("SKIP "+cancelKey+"\t"+_date);
                } else {
                    LOG.info("add to cleanup.. "+cancelKey);
                    list.add(cancelKey);
                }
            }

            for (String cancelKey : list) {
                LOG.info("remove "+cancelKey);
                CANCEL_SEQ.remove(cancelKey);
            }
        }
    }

    /**
     * 중계기관이 FINISH 가 되면 다른 모든 기관도 FINISH 처리함.
     * @param finish
     */
    public void updateAllFinishStatus(String finish) {
        String now = DateUtils.toDateString(new Date());
        damoaDao.updateAllFinishStatus(now);
    }

    private class Bean {
        public String key;
        public long elapsed;

        public Bean(String key, long elapsed) {
            this.key = key;
            this.elapsed = elapsed;
        }


    }

    public boolean containsKeyAtSeqNo(String dealSeqNo) {
        String nowDate = DateUtils.toDateString(new Date());
        List<Map<String, Object>> mapList= damoaDao.containsSeqNo(dealSeqNo,nowDate);

        int size = 0;
        if (mapList == null || mapList.size() == 0) {
            size = 0;
        }else {
            size = mapList.size();
        }


        LOG.info("containsKeyAtSeqNo "+size);
        if (size == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 동일한 요청이 있는지 확인
     * 
     * @param dealSeqNo
     * @param typeCode
     * @return
     */
    public boolean containsSeqNoAtCancel(String dealSeqNo,String typeCode) {
        final String nowDate = DateUtils.toDateString(new Date());
        List<Map<String,Object>> mapList = damoaDao.containsSeqNoAtCancel(dealSeqNo,nowDate,typeCode);
        if(mapList == null) {
            mapList = new ArrayList<>();
        }
        LOG.info("containsSeqNoAtCancel "+mapList.size());
        if (mapList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public Map<String, Object> findCorpCdByVanoAndFgcd(String fgcd, String accountNo) {
        Map<String,Object> map = damoaDao.findAccountInfo(fgcd,accountNo);
        return map;
    }

}
