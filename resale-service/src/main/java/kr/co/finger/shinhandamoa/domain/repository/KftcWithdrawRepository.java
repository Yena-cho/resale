package kr.co.finger.shinhandamoa.domain.repository;

import com.finger.shinhandamoa.data.table.mapper.KftcWithdrawMapper;
import com.finger.shinhandamoa.data.table.model.KftcWithdraw;
import com.finger.shinhandamoa.data.table.model.KftcWithdrawExample;
import kr.co.finger.shinhandamoa.domain.model.KftcWithdrawDO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 금융결제원 출금이체 저장소
 * 
 * @author wisehouse@finger.co.kr
 */
@Repository
public class KftcWithdrawRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(KftcWithdrawRepository.class);
    
    @Autowired
    private KftcWithdrawMapper kftcWithdrawMapper;

    public List<KftcWithdrawDO> getList(List<String> statusList) {
        final KftcWithdrawExample example = new KftcWithdrawExample();
        example.createCriteria().andStatusCdIn(statusList).andPayerNoIsNotNull();
        example.setOrderByClause("ID ASC");
        
        final List<KftcWithdraw> voList = kftcWithdrawMapper.selectByExample(example);
        final List<KftcWithdrawDO> doList = new ArrayList<>();
        for (KftcWithdraw each : voList) {
            doList.add(new KftcWithdrawDO(each));
        }
        
        return doList;
    }

    public int count(List<String> strings) {
        return 0;
    }

    public long sumAmount(List<String> strings) {
        return 0;
    }

    public void update(KftcWithdrawDO kftcWithdrawDO) {
        KftcWithdraw kftcWithdraw = kftcWithdrawDO.getKftcWithdraw();
        kftcWithdrawMapper.updateByPrimaryKey(kftcWithdraw);
    }
    
    public KftcWithdrawDO getByEb21IdAndDataNo(String eb21Id, String dataNo) throws Exception {
        final KftcWithdrawExample example = new KftcWithdrawExample();
        example.createCriteria()
                .andEb21IdEqualTo(StringUtils.rightPad(eb21Id, 10, ' '))
                .andDataNoEqualTo(dataNo);

        final List<KftcWithdraw> voList = kftcWithdrawMapper.selectByExample(example);
        if(voList.size() != 1) {
            throw new Exception();
        }
        
        return new KftcWithdrawDO(voList.get(0));
    }

    public List<KftcWithdrawDO> getByEb21Id(String eb21Id) {
        final KftcWithdrawExample example = new KftcWithdrawExample();
        example.createCriteria()
                .andEb21IdEqualTo(StringUtils.rightPad(eb21Id, 10, ' '));

        final List<KftcWithdraw> voList = kftcWithdrawMapper.selectByExample(example);
        final List<KftcWithdrawDO> doList = new ArrayList<>();
        for (KftcWithdraw each : voList) {
            doList.add(new KftcWithdrawDO(each));
        }

        return doList;
    }
}
