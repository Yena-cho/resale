package kr.co.finger.shinhandamoa.domain.repository;

import com.finger.shinhandamoa.data.table.mapper.ChaMapper;
import com.finger.shinhandamoa.data.table.model.Cha;
import com.finger.shinhandamoa.data.table.model.ChaExample;
import kr.co.finger.shinhandamoa.domain.model.ClientDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 이용기관 저장소
 * 
 * @author wisehouse@finger.co.kr
 */
@Repository
public class ClientRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRepository.class);
    
    @Autowired
    private ChaMapper chaMapper;

    /**
     * 금융결제원 납부자 번호로 이용기관을 가져온다
     * 
     * @return
     */
    public ClientDO getByKftcPayerNo(String kftcPayerNo) throws Exception {
        final ChaExample example =  new ChaExample();
        example.createCriteria().andFingerFeeOwnerNoEqualTo(kftcPayerNo);

        List<Cha> itemList = chaMapper.selectByExample(example);
        if(itemList.size() != 1) {
            throw new Exception("납부자번호["+kftcPayerNo+"]에 해당하는 이용기관이 없음");
        }
        
        return new ClientDO(itemList.get(0));
    }

    public void update(ClientDO clientDO) {
        final Cha cha = clientDO.getCha();
        
        chaMapper.updateByPrimaryKey(cha);
    }

    public ClientDO get(String clientId) throws Exception {
        final Cha cha = chaMapper.selectByPrimaryKey(clientId);
        if(cha == null) {
            throw new Exception("이용기관없음[" + clientId + "]");
        }
        
        return new ClientDO(cha);
    }

    /**
     * 정상 이용기관 조회
     * 
     * @return
     */
    public List<ClientDO> findActive() {
        final ChaExample example = new ChaExample();
        example.createCriteria().andChastEqualTo("ST06");
        example.setOrderByClause("CHACD ASC");
        
        final List<Cha> voList = chaMapper.selectByExample(example);
        final List<ClientDO> doList = new ArrayList<>();
        for (Cha each : voList) {
            doList.add(new ClientDO(each));
        }
        
        return doList;
    }
}
