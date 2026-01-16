package kr.co.finger.shinhandamoa.domain.repository;

import com.finger.shinhandamoa.data.table.mapper.KftcPayerMapper;
import com.finger.shinhandamoa.data.table.model.KftcPayer;
import com.finger.shinhandamoa.data.table.model.KftcPayerExample;
import kr.co.finger.shinhandamoa.domain.model.KftcPayerDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 금융결제원 납부자 저장소
 * 
 * @author wisehouse@finger.co.kr
 */
@Repository
public class KftcPayerRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(KftcPayerRepository.class);
    
    @Autowired
    private KftcPayerMapper kftcPayerMapper;

    public long count(List<String> statusCdList) {
        final KftcPayerExample example = new KftcPayerExample();
        example.createCriteria().andStatusCdIn(statusCdList);
        
        return kftcPayerMapper.countByExample(example);
    }

    public List<KftcPayerDO> getList(List<String> statusCdList) {
        final KftcPayerExample example = new KftcPayerExample();
        example.setOrderByClause("ID ASC");
        example.createCriteria().andStatusCdIn(statusCdList);

        final List<KftcPayer> voList = kftcPayerMapper.selectByExample(example);
        final List<KftcPayerDO> doList = new ArrayList<>();
        for (KftcPayer each : voList) {
            doList.add(new KftcPayerDO(each));
        }
        
        return doList;
    }

    public void update(KftcPayerDO kftcPayerDO) {
        final KftcPayer kftcPayer = kftcPayerDO.getKftcPayer();
        kftcPayerMapper.updateByPrimaryKey(kftcPayer);
    }

    public KftcPayerDO get(String payerId) throws Exception {
        final KftcPayer kftcPayer = kftcPayerMapper.selectByPrimaryKey(payerId);
        if(kftcPayer == null) {
            throw new Exception("없는 payerId");
        }
        return new KftcPayerDO(kftcPayer);
    }

    public List<KftcPayerDO> getListByEb13Id(String eb13Id) {
        final KftcPayerExample example = new KftcPayerExample();
        example.setOrderByClause("ID ASC");
        example.createCriteria().andEb13IdEqualTo(eb13Id);

        final List<KftcPayer> voList = kftcPayerMapper.selectByExample(example);
        final List<KftcPayerDO> doList = new ArrayList<>();
        for (KftcPayer each : voList) {
            doList.add(new KftcPayerDO(each));
        }

        return doList;
    }
}
