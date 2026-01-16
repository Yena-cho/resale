package kr.co.finger.shinhandamoa.service;

import kr.co.finger.shinhandamoa.domain.model.ClientDO;
import kr.co.finger.shinhandamoa.domain.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 기관 서비스
 * 
 * @author wisehouse@finger.co.kr
 */
@Component
public class ClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);
    
    @Autowired
    private ClientRepository clientRepository;
    
    /**
     * 사용하는 기관코드인지 확인
     * 
     * @param clientId 기관코드
     */
    public boolean exists(String clientId) {
        return clientRepository.exists(clientId);
    }

    public ClientDTO getClient(String clientId) {
        final ClientDO clientDO = clientRepository.get(clientId);
        final ClientDTO clientDTO = new ClientDTO();
        return clientDTO;
    }

    public void deleteClient(String clientId) {
        final ClientDO clientDO = clientRepository.get(clientId);
        clientDO.delete();
        
        clientRepository.update(clientDO);
    }

    public void modifyPaymentFee(String clientId, int paymentFee, int paymentBankFee) {
        final ClientDO clientDO = clientRepository.get(clientId);
        clientDO.setReceiptFee(paymentFee);
        clientDO.setReceiptBankFee(paymentBankFee);

        clientRepository.update(clientDO);
    }

    public void createClient(String clientId, String clientName, String deleteYn, String createDt, String clientIdentityNo, String branchCode) {
        final ClientDO clientDO = ClientDO.newInstance(clientId, clientName, deleteYn, createDt, clientIdentityNo, branchCode);

        try {
            clientRepository.create(clientDO);
        } catch (Exception e) {
            LOGGER.error("기관 생성에 실패");
            if(LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());
            }
        }
    }

    public void createAgency(String clientId, String agencyCode, String agencyName) {
        final ClientDO clientDO = clientRepository.get(clientId);

        clientDO.createAgency(agencyCode, agencyName);
        clientRepository.update(clientDO);
    }

    public void deleteAgency(String clientId, String agencyCode, String agencyName) {
        final ClientDO clientDO = clientRepository.get(clientId);
        
        clientDO.deleteAgency(agencyCode, agencyName);
        clientRepository.update(clientDO);
    }

    public void terminateClient(String clientId) {
        final ClientDO clientDO = clientRepository.get(clientId);
        
        if(clientDO == null) {
            LOGGER.warn("해지 대상 기관 없음 [{}]", clientId);
            return;
        }
        
        clientDO.terminate();
        clientRepository.update(clientDO);
    }

    public void holdClient(String clientId) {
        final ClientDO clientDO = clientRepository.get(clientId);

        if(clientDO == null) {
            LOGGER.warn("정지 대상 기관 없음 [{}]", clientId);
            return;
        }

        clientDO.hold();
        clientRepository.update(clientDO);
    }
}
