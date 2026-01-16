package kr.co.finger.shinhandamoa.domain.repository;

import kr.co.finger.shinhandamoa.data.table.mapper.WebuserMapper;
import kr.co.finger.shinhandamoa.data.table.mapper.XadjgroupMapper;
import kr.co.finger.shinhandamoa.data.table.mapper.XchalistMapper;
import kr.co.finger.shinhandamoa.data.table.model.*;
import kr.co.finger.shinhandamoa.domain.model.ClientDO;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 기관 저장소
 * 
 * @author wisehouse@finger.co.kr
 */
@Repository
public class ClientRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRepository.class);

    @Autowired
    private XchalistMapper xchalistMapper;
    
    @Autowired
    private XadjgroupMapper xadjgroupMapper;

    @Autowired
    private WebuserMapper webuserMapper;
    
    public List<ClientDO> findAll(RowBounds rowBounds) {
        final List<Xchalist> itemList = xchalistMapper.selectByExampleWithRowbounds(new XchalistExample(), rowBounds);
        final List<ClientDO> doList = new ArrayList<>(itemList.size());
        for (Xchalist each : itemList) {
            doList.add(new ClientDO(each, new ArrayList()));
        }
        
        return doList;
    }

    public ClientDO get(String clientId) {
        Xchalist xchalist = xchalistMapper.selectByPrimaryKey(clientId);
        if(xchalist == null) {
            return null;
        }
        
        XadjgroupExample example = new XadjgroupExample();
        example.createCriteria().andChacdEqualTo(clientId);
        example.setOrderByClause("ADJFIREGKEY ASC");
        List<Xadjgroup> xadjgroupList = xadjgroupMapper.selectByExample(example);
        
        return new ClientDO(xchalist, xadjgroupList);
    }

    public boolean exists(String clientId) {
        return xchalistMapper.selectByPrimaryKey(clientId) != null;
    }

    public void update(ClientDO client) {
        final Xchalist xchalist = client.getXchalist();
        xchalistMapper.updateByPrimaryKey(xchalist);

        //해지일경우 webuser도 update
        if("ST02".equals(client.getStatus())){
            LOGGER.debug("기관연동 해지기관 WEBUSER UPDATE {} ", client.getId());
            WebuserExample webuserExample = new WebuserExample();
            webuserExample.createCriteria().andChacdEqualTo(client.getXchalist().getChacd());
            List<Webuser> webuser = webuserMapper.selectByExample(webuserExample);
            if(webuser != null){
                webuser.get(0).setIdst("ST02");
                webuserMapper.updateByPrimaryKey(webuser.get(0));
            }
        }

        final List<Xadjgroup> createXadjgroupList = client.getCreateXadjgroupList();
        for (Xadjgroup each : createXadjgroupList) {
            xadjgroupMapper.insert(each);
        }
        final List<Xadjgroup> deleteXadjgroupList = client.getDeleteXadjgroupList();
        for (Xadjgroup each : deleteXadjgroupList) {
            xadjgroupMapper.deleteByPrimaryKey(each);
        }
    }

    public void create(ClientDO client) {
        if(exists(client.getId())) {
            LOGGER.warn("신규 기관 등록 실패 : 사용 중인 기관코드");
            return;
        }
        
        Xchalist xchalist = client.getXchalist();
        xchalistMapper.insertSelective(xchalist);

        final List<Xadjgroup> createXadjgroupList = client.getCreateXadjgroupList();
        for (Xadjgroup each : createXadjgroupList) {
            xadjgroupMapper.insert(each);
        }
    }
}
