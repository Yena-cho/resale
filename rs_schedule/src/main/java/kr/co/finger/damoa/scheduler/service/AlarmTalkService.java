package kr.co.finger.damoa.scheduler.service;

import kr.co.finger.damoa.scheduler.service.model.AlarmTalkDTO;
import kr.co.finger.damoa.scheduler.service.model.InstantMessageDTO;
import kr.co.finger.shinhandamoa.data.table.mapper.AtreqMapper;
import kr.co.finger.shinhandamoa.data.table.mapper.SmsreqMapper;
import kr.co.finger.shinhandamoa.data.table.model.Atreq;
import kr.co.finger.shinhandamoa.data.table.model.AtreqExample;
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
public class AlarmTalkService {

    @Autowired
    private SmsreqMapper smsreqMapper;

    @Autowired
    private AtreqMapper atreqMapper;

    public long count(String sendStatusCd) throws Exception {
        final AtreqExample example = new AtreqExample();
        final AtreqExample.Criteria criteria = example.createCriteria();

        criteria.andSendstatuscdEqualTo(sendStatusCd);
        criteria.andSenddateBetween(DateUtils.parseDate("20200601000000", "yyyyMMddHHmmss"), DateUtils.addMinutes(new Date(), -2));

        return atreqMapper.countByExample(example);
    }

    public List<AlarmTalkDTO> getList(String sendStatusCd, RowBounds rowBounds) throws Exception {
        final AtreqExample example = new AtreqExample();
        final AtreqExample.Criteria criteria = example.createCriteria();

        example.setOrderByClause("SENDDATE DESC");
        criteria.andSendstatuscdEqualTo(sendStatusCd);
        criteria.andSenddateBetween(DateUtils.parseDate("20200601000000", "yyyyMMddHHmmss"), DateUtils.addMinutes(new Date(), -2));

        final List<Atreq> voList = atreqMapper.selectByExampleWithRowbounds(example, rowBounds);

        final List<AlarmTalkDTO> itemList = new ArrayList<>();
        for (Atreq each : voList) {
            AlarmTalkDTO dto = new AlarmTalkDTO();
            dto.setCmid(each.getCmid());
            dto.setSendStatusCd(each.getSendstatuscd());
            dto.setSendResultCd(each.getSendresultcd());
            itemList.add(dto);
        }

        return itemList;
    }

    public void updateStatus(String cmid, String sendStatusCd, String atStatus) {
        final Date now = new Date();
        final Atreq atreq = new Atreq();
        atreq.setCmid(cmid);
        atreq.setSendstatuscd(sendStatusCd);
        atreq.setSendresultcd(atStatus);

        atreqMapper.updateByPrimaryKeySelective(atreq);
    }
}
