package kr.co.finger.damoa.shinhan.agent.model;

import kr.co.finger.damoa.model.msg.NoticeMessage;
import kr.co.finger.damoa.shinhan.agent.domain.model.ChaDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.CustomerDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.NoticeMasterDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.ReceiptMasterDO;

import java.util.Date;
import java.util.List;

/**
 * 입금 통지 스케줄러 대기열 데이터
 *
 * @author wisehouse@finger.co.kr
 */
public class NoticeMessageBean implements MessageBean {
    private Date now;
    private NoticeMessage noticeMessage;

    private ChaDO cha;
    private CustomerDO customer;
    private List<NoticeMasterDO> noticeMasterList;
    private List<ReceiptMasterDO> receiptMasterList;

    public NoticeMessageBean(NoticeMessage message, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList, List<ReceiptMasterDO> receiptMasterList) {
        this.noticeMessage = message;
        this.receiptMasterList = receiptMasterList;
        this.cha = cha;
        this.customer = customer;
        this.noticeMasterList = noticeMasterList;
        this.now = new Date();
    }

    public Date getNow() {
        return now;
    }

    public NoticeMessage getNoticeMessage() {
        return noticeMessage;
    }

    public List<ReceiptMasterDO> getReceiptMasterList() {
        return receiptMasterList;
    }

    public ChaDO getCha() {
        return cha;
    }

    public CustomerDO getCustomer() {
        return customer;
    }

    public List<NoticeMasterDO> getNoticeMasterList() {
        return noticeMasterList;
    }
}
