package kr.co.finger.damoa.scheduler.service;

import kr.co.finger.damoa.scheduler.service.model.InstantMessageDTO;
import kr.co.finger.shinhandamoa.data.table.mapper.SmsreqMapper;
import kr.co.finger.shinhandamoa.data.table.model.Smsreq;
import kr.co.finger.shinhandamoa.data.table.model.SmsreqExample;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InstantMessageService {

    @Autowired
    private SmsreqMapper smsreqMapper;

    public long count(String messageStatus) throws Exception {
        final SmsreqExample example = new SmsreqExample();
        final SmsreqExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(messageStatus);
        criteria.andRegdtBetween(DateUtils.parseDate("20210316000000", "yyyyMMddHHmmss"), org.apache.commons.lang3.time.DateUtils.addMinutes(new Date(), -2));
        example.or().andStatusEqualTo("3").andReqdateGreaterThan(DateUtils.addMinutes(new Date(), -10));

        return smsreqMapper.countByExample(example);
    }

    public List<InstantMessageDTO> getList(String messageStatus, RowBounds rowBounds) throws Exception {
        final SmsreqExample example = new SmsreqExample();
        final SmsreqExample.Criteria criteria = example.createCriteria();
        example.setOrderByClause("REQDATE DESC");
        criteria.andStatusEqualTo(messageStatus);
        criteria.andRegdtBetween(DateUtils.parseDate("20210316000000", "yyyyMMddHHmmss"), org.apache.commons.lang3.time.DateUtils.addMinutes(new Date(), -2));
        example.or().andStatusEqualTo("3").andReqdateGreaterThan(DateUtils.addMinutes(new Date(), -10));

        final List<Smsreq> voList = smsreqMapper.selectByExampleWithRowbounds(example, rowBounds);

        final List<InstantMessageDTO> itemList = new ArrayList<>();
        for (Smsreq each : voList) {
            InstantMessageDTO dto = new InstantMessageDTO();
            dto.setSmsReqCd(each.getSmsreqcd());

            itemList.add(dto);
        }
        return itemList;
    }

    public void updateStatus(String smsReqCd, String status) {
        final Date now = new Date();

        final Smsreq smsreq = new Smsreq();
        smsreq.setSmsreqcd(smsReqCd);
        smsreq.setStatus(status);
        smsreq.setReportdate(now);

        smsreqMapper.updateByPrimaryKeySelective(smsreq);
    }
}
