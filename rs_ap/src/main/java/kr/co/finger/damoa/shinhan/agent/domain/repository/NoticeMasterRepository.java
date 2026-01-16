package kr.co.finger.damoa.shinhan.agent.domain.repository;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.shinhan.agent.domain.model.NoticeDetailsDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.NoticeMasterDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.PayItemDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.ReceiptDetailsDO;
import kr.co.finger.shinhandamoa.data.table.mapper.XnotidetMapper;
import kr.co.finger.shinhandamoa.data.table.mapper.XnotimasMapper;
import kr.co.finger.shinhandamoa.data.table.mapper.XpayitemMapper;
import kr.co.finger.shinhandamoa.data.table.mapper.XrcpdetMapper;
import kr.co.finger.shinhandamoa.data.table.model.*;
import org.apache.commons.net.ntp.TimeStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 청구원장 저장소
 *
 * @author wisehouse@finger.co.kr
 */
@Repository
public class NoticeMasterRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeMasterRepository.class);

    @Autowired
    private XnotimasMapper xnotimasMapper;

    @Autowired
    private XnotidetMapper xnotidetMapper;

    @Autowired
    private XpayitemMapper xpayitemMapper;

    @Autowired
    private XrcpdetMapper xrcpdetMapper;

    /**
     * 미납, 일부납 청구 조회
     *
     * @param chaCd     기관코드
     * @param accountNo 계좌번호
     * @return
     */
    public List<NoticeMasterDO> findActiveByAccountNo(String chaCd, String accountNo) {
        final XnotimasExample example = new XnotimasExample();
        final XnotimasExample.Criteria criteria = example.createCriteria();
        criteria.andChacdEqualTo(chaCd);
        criteria.andVanoEqualTo(accountNo);
        criteria.andNotimasstIn(Arrays.asList("PA02", "PA04"));
        criteria.andNotiCanYnEqualTo("N");
        criteria.andEnddateGreaterThanOrEqualTo(DateUtils.format(DateUtils.now(), "yyyyMMdd"));
        example.setOrderByClause("MASDAY ASC, NOTIMASCD ASC");

        final List<Xnotimas> notimasList = xnotimasMapper.selectByExample(example);
        final List<NoticeMasterDO> noticeMasterList = new ArrayList<>();
        for (Xnotimas each : notimasList) {
            final NoticeMasterDO noticeMaster = new NoticeMasterDO(each);

            final XnotidetExample xnotidetExample = new XnotidetExample();
            xnotidetExample.createCriteria().andNotimascdEqualTo(each.getNotimascd());
            xnotidetExample.setOrderByClause("PTRITEMORDER ASC");

            final List<Xnotidet> xnotidetList = xnotidetMapper.selectByExample(xnotidetExample);

            for (Xnotidet eachXnotidet : xnotidetList) {
                final NoticeDetailsDO noticeDetails = new NoticeDetailsDO(eachXnotidet);

                // 청구항목구분
                final Xpayitem xpayitem = xpayitemMapper.selectByPrimaryKey(eachXnotidet.getPayitemcd());
                noticeDetails.setPayItem(new PayItemDO(xpayitem));

                // 수납원장상세
                final XrcpdetExample xrcpdetExample = new XrcpdetExample();
                // 수납상태인 것만 가져온다
                xrcpdetExample.createCriteria().andNotidetcdEqualTo(eachXnotidet.getNotidetcd()).andRcpdetstEqualTo("PA03");

                final List<Xrcpdet> xrcpdetList = xrcpdetMapper.selectByExample(xrcpdetExample);
                for (Xrcpdet eachXrcpdet : xrcpdetList) {
                    noticeDetails.addReceiptDetails(new ReceiptDetailsDO(eachXrcpdet));
                }

                noticeMaster.addDetails(noticeDetails);
            }

            noticeMasterList.add(noticeMaster);
        }

        return noticeMasterList;
    }

    public void updateNotice(NoticeMasterDO noticeMaster) {
        final Xnotimas xnotimas = noticeMaster.getXnotimas();
        xnotimasMapper.updateByPrimaryKey(xnotimas);

        for (NoticeDetailsDO each : noticeMaster.getDetailsList()) {
            final Xnotidet xnotidet = each.getXnotidet();
            xnotidetMapper.updateByPrimaryKey(xnotidet);
        }
    }

    public NoticeMasterDO findById(String noticeMasterId) {
        Xnotimas xnotimas = xnotimasMapper.selectByPrimaryKey(noticeMasterId);
        NoticeMasterDO noticeMaster = new NoticeMasterDO(xnotimas);

        final XnotidetExample xnotidetExample = new XnotidetExample();
        xnotidetExample.createCriteria().andNotimascdEqualTo(xnotimas.getNotimascd());
        xnotidetExample.setOrderByClause("PTRITEMORDER ASC");

        final List<Xnotidet> xnotidetList = xnotidetMapper.selectByExample(xnotidetExample);

        for (Xnotidet eachXnotidet : xnotidetList) {
            final NoticeDetailsDO noticeDetails = new NoticeDetailsDO(eachXnotidet);

            // 청구항목구분
            final Xpayitem xpayitem = xpayitemMapper.selectByPrimaryKey(eachXnotidet.getPayitemcd());
            noticeDetails.setPayItem(new PayItemDO(xpayitem));

            // 수납원장상세
            final XrcpdetExample xrcpdetExample = new XrcpdetExample();
            // 수납상태인 것만 가져온다
            xrcpdetExample.createCriteria().andNotidetcdEqualTo(eachXnotidet.getNotidetcd()).andRcpdetstEqualTo("PA03");

            final List<Xrcpdet> xrcpdetList = xrcpdetMapper.selectByExample(xrcpdetExample);
            for (Xrcpdet eachXrcpdet : xrcpdetList) {
                noticeDetails.addReceiptDetails(new ReceiptDetailsDO(eachXrcpdet));
            }

            noticeMaster.addDetails(noticeDetails);
        }

        return noticeMaster;
    }
}
