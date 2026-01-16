package kr.co.finger.shinhandamoa.domain.repository;

import kr.co.finger.damoa.scheduler.ThreadSession;
import kr.co.finger.damoa.scheduler.ThreadSessionHolder;
import kr.co.finger.shinhandamoa.data.table.mapper.DamoaClientInfoPullHistMapper;
import kr.co.finger.shinhandamoa.data.table.model.DamoaClientInfoPullHist;
import kr.co.finger.shinhandamoa.data.table.model.DamoaClientInfoPullHistExample;
import kr.co.finger.shinhandamoa.data.table.model.DamoaClientInfoPullHistKey;
import kr.co.finger.shinhandamoa.domain.model.ClientInfomationPullHistoryDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 기관 정보 연동 이력
 *
 * @author wisehouse@finger.co.kr
 */
@Repository
public class ClientInformationPullHistoryRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientInformationPullHistoryRepository.class);

    @Autowired
    private DamoaClientInfoPullHistMapper damoaClientInfoPullHistMapper;

    public void deleteByPullDt(String pullDt) {
        DamoaClientInfoPullHistExample example = new DamoaClientInfoPullHistExample();
        example.createCriteria().andPullDtEqualTo(pullDt);

        damoaClientInfoPullHistMapper.deleteByExample(example);
    }

    public void create(ClientInfomationPullHistoryDO clientInfomationPullHistoryDO) {
        final ThreadSession session = ThreadSessionHolder.getInstance().getSession(ClientInfomationPullHistoryDO.class.getSimpleName());
        final String username = session.getAttribute(ThreadSession.SESSION_ATTRIBUTE_KEY_OWNER);
        final Date now = new Date();

        final DamoaClientInfoPullHist vo = clientInfomationPullHistoryDO.getDamoaClientInfoPullHist();
        vo.setCreateDate(now);
        vo.setCreateUser(username);
        vo.setModifyDate(now);
        vo.setModifyUser(username);

        damoaClientInfoPullHistMapper.insert(vo);
    }

    public ClientInfomationPullHistoryDO get(String pullDt, String clientId) {
        final DamoaClientInfoPullHistKey key = new DamoaClientInfoPullHistKey();
        key.setPullDt(pullDt);
        key.setClientId(clientId);

        final DamoaClientInfoPullHist vo = damoaClientInfoPullHistMapper.selectByPrimaryKey(key);
        if (vo == null) {
            return null;
        }

        return new ClientInfomationPullHistoryDO(vo);
    }

    public void update(ClientInfomationPullHistoryDO clientInfomationPullHistoryDO) {
        final ThreadSession session = ThreadSessionHolder.getInstance().getSession(ClientInfomationPullHistoryDO.class.getSimpleName());
        final String username = session.getAttribute(ThreadSession.SESSION_ATTRIBUTE_KEY_OWNER);
        final Date now = new Date();

        final DamoaClientInfoPullHist vo = clientInfomationPullHistoryDO.getDamoaClientInfoPullHist();
        vo.setModifyDate(now);
        vo.setModifyUser(username);

        damoaClientInfoPullHistMapper.updateByPrimaryKey(vo);
    }

    public List<ClientInfomationPullHistoryDO> findByStatus(String status) {
        final DamoaClientInfoPullHistExample example = new DamoaClientInfoPullHistExample();
        example.createCriteria().andStatusCdEqualTo(status);
        example.setOrderByClause("CREATE_DATE ASC");

        final List<DamoaClientInfoPullHist> voList = damoaClientInfoPullHistMapper.selectByExample(example);
        final List<ClientInfomationPullHistoryDO> doList = new ArrayList<>(voList.size());
        for (DamoaClientInfoPullHist each : voList) {
            doList.add(new ClientInfomationPullHistoryDO(each));
        }

        return doList;
    }
}
