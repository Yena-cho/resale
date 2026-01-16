package kr.co.finger.shinhandamoa.service;

import kr.co.finger.damoa.commons.biz.CompanyStatusLookupAPI;
import kr.co.finger.damoa.scheduler.task.daily.ClientAgencyDTO;
import kr.co.finger.shinhandamoa.domain.model.ClientDO;
import kr.co.finger.shinhandamoa.domain.model.ClientInfomationPullHistoryDO;
import kr.co.finger.shinhandamoa.domain.model.ClientInfomationPullHistoryExample;
import kr.co.finger.shinhandamoa.domain.repository.ClientInformationPullHistoryRepository;
import kr.co.finger.shinhandamoa.domain.repository.ClientRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 기관 정보 연동 서비스
 * 
 * @author wisehouse@finger.co.kr
 */
@Component
public class ClientInfomationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientInfomationService.class);
    
    @Autowired
    private ClientInformationPullHistoryRepository clientInformationPullHistoryRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Value("${scrapping.host}")
    private String scrappingHost;

    @Value("${scrapping.port}")
    private int scrappingPort;

    public void deleteByPullDt(String pullDt) {
        clientInformationPullHistoryRepository.deleteByPullDt(pullDt);
    }
    
    public void create(ClientInfomationPullHistoryDTO clientInfomationPullHistoryDTO) {
        final ClientInfomationPullHistoryExample example = generateExample(clientInfomationPullHistoryDTO);
        final ClientDO clientDO = clientRepository.get(example.getClientId());
        
        final ClientInfomationPullHistoryDO clientInfomationPullHistoryDO = ClientInfomationPullHistoryDO.newInstance(example, clientDO);
        
        clientInformationPullHistoryRepository.create(clientInfomationPullHistoryDO);
    }
    
    private ClientInfomationPullHistoryExample generateExample(ClientInfomationPullHistoryDTO clientInfomationPullHistoryDTO) {
        final ClientInfomationPullHistoryExample example = new ClientInfomationPullHistoryExample();
        example.setPullDt(clientInfomationPullHistoryDTO.getPullDt());
        example.setClientId(clientInfomationPullHistoryDTO.getClientId());
        example.setClientName(clientInfomationPullHistoryDTO.getClientName());
        example.setClientDeleteYn(clientInfomationPullHistoryDTO.getClientDeleteYn());
        example.setRelayClientId(clientInfomationPullHistoryDTO.getRelayClientId());
        example.setClientCreateDt(clientInfomationPullHistoryDTO.getClientCreateDt());
        example.setClientIdentityNo(clientInfomationPullHistoryDTO.getClientIdentityNo());
        example.setClientStatus(clientInfomationPullHistoryDTO.getClientStatus());
        example.setAgencyCode01(clientInfomationPullHistoryDTO.getAgencyCode01());
        example.setAgencyName01(clientInfomationPullHistoryDTO.getAgencyName01());
        example.setAgencyStatus01(clientInfomationPullHistoryDTO.getAgencyStatus01());
        example.setAgencyCode02(clientInfomationPullHistoryDTO.getAgencyCode02());
        example.setAgencyName02(clientInfomationPullHistoryDTO.getAgencyName02());
        example.setAgencyStatus02(clientInfomationPullHistoryDTO.getAgencyStatus02());
        example.setAgencyCode03(clientInfomationPullHistoryDTO.getAgencyCode03());
        example.setAgencyName03(clientInfomationPullHistoryDTO.getAgencyName03());
        example.setAgencyStatus03(clientInfomationPullHistoryDTO.getAgencyStatus03());
        example.setAgencyCode04(clientInfomationPullHistoryDTO.getAgencyCode04());
        example.setAgencyName04(clientInfomationPullHistoryDTO.getAgencyName04());
        example.setAgencyStatus04(clientInfomationPullHistoryDTO.getAgencyStatus04());
        example.setAgencyCode05(clientInfomationPullHistoryDTO.getAgencyCode05());
        example.setAgencyName05(clientInfomationPullHistoryDTO.getAgencyName05());
        example.setAgencyStatus05(clientInfomationPullHistoryDTO.getAgencyStatus05());
        example.setAgencyCode06(clientInfomationPullHistoryDTO.getAgencyCode06());
        example.setAgencyName06(clientInfomationPullHistoryDTO.getAgencyName06());
        example.setAgencyStatus06(clientInfomationPullHistoryDTO.getAgencyStatus06());
        example.setAgencyCode07(clientInfomationPullHistoryDTO.getAgencyCode07());
        example.setAgencyName07(clientInfomationPullHistoryDTO.getAgencyName07());
        example.setAgencyStatus07(clientInfomationPullHistoryDTO.getAgencyStatus07());
        example.setAgencyCode08(clientInfomationPullHistoryDTO.getAgencyCode08());
        example.setAgencyName08(clientInfomationPullHistoryDTO.getAgencyName08());
        example.setAgencyStatus08(clientInfomationPullHistoryDTO.getAgencyStatus08());
        example.setAgencyCode09(clientInfomationPullHistoryDTO.getAgencyCode09());
        example.setAgencyName09(clientInfomationPullHistoryDTO.getAgencyName09());
        example.setAgencyStatus09(clientInfomationPullHistoryDTO.getAgencyStatus09());
        example.setAgencyCode10(clientInfomationPullHistoryDTO.getAgencyCode10());
        example.setAgencyName10(clientInfomationPullHistoryDTO.getAgencyName10());
        example.setAgencyStatus10(clientInfomationPullHistoryDTO.getAgencyStatus10());
        example.setPaymentFeeAmount(clientInfomationPullHistoryDTO.getPaymentFeeAmount());
        example.setFingerFeeRate(clientInfomationPullHistoryDTO.getFingerFeeRate());
        example.setBranchCode(clientInfomationPullHistoryDTO.getBranchCode());

        return example;
    }

    public void switchToStandby(String pullDt, String clientId) {
        ClientInfomationPullHistoryDO clientInfomationPullHistoryDO = clientInformationPullHistoryRepository.get(pullDt, clientId);
        clientInfomationPullHistoryDO.switchStatusToStandby();
        
        clientInformationPullHistoryRepository.update(clientInfomationPullHistoryDO);
    }

    public boolean execute(String pullDt, String clientId) throws Exception {
        final ClientInfomationPullHistoryDO clientInfomationPullHistoryDO = clientInformationPullHistoryRepository.get(pullDt, clientId);
        boolean flag = true;
        try {
            if(StringUtils.equals(clientInfomationPullHistoryDO.getTypeCd(), ClientInfomationPullHistoryDO.TYPE_CD_CREATE)) {
                createClient(clientInfomationPullHistoryDO.getClientId(), clientInfomationPullHistoryDO.getClientName(), clientInfomationPullHistoryDO.getClientDeleteYn(), clientInfomationPullHistoryDO.getClientCreateDt(), clientInfomationPullHistoryDO.getClientIdentityNo(), clientInfomationPullHistoryDO.getBranchCode());
            }

            final ClientDO clientDO = clientRepository.get(clientInfomationPullHistoryDO.getClientId());

            if (clientDO == null) {
                LOGGER.error("없는 기관 [{}]", clientInfomationPullHistoryDO.getClientId());
    
                clientInfomationPullHistoryDO.switchStatusToFail();
                clientInformationPullHistoryRepository.update(clientInfomationPullHistoryDO);
                throw new Exception("없는 기관");
            }

            // 기관 상태
            switch (clientInfomationPullHistoryDO.getClientStatus()) {
                case "3":
                    LOGGER.info("기관 해지 [{}]", clientInfomationPullHistoryDO.getClientId());
    
                    clientDO.terminate();
                    break;
                case "4":
                    LOGGER.info("기관 중지 [{}]", clientInfomationPullHistoryDO.getClientId());
    
                    clientDO.hold();
                    break;
            }

            // 기관명 변경
            if(!StringUtils.equals(clientDO.getName(), clientInfomationPullHistoryDO.getClientName())) {
                LOGGER.info("[{}] 기관명: {} => {}", clientInfomationPullHistoryDO.getClientId(), clientDO.getName(), clientInfomationPullHistoryDO.getClientName());
                clientDO.modifyName(clientInfomationPullHistoryDO.getClientName());
            }

            // 기관 삭제
            if (!clientDO.deleted() && StringUtils.equals(clientInfomationPullHistoryDO.getClientDeleteYn(), "Y")) {
                LOGGER.info("기관 삭제 [{}]", clientInfomationPullHistoryDO.getClientId());
                clientDO.delete();
            }

            // 대리점 변경
            final List<ClientAgencyDTO> clientAgencyList = new ArrayList<>();
            clientAgencyList.add(new ClientAgencyDTO(clientInfomationPullHistoryDO.getAgency01Code(), clientInfomationPullHistoryDO.getAgency01Name(), clientInfomationPullHistoryDO.getAgency01Status()));
            clientAgencyList.add(new ClientAgencyDTO(clientInfomationPullHistoryDO.getAgency02Code(), clientInfomationPullHistoryDO.getAgency02Name(), clientInfomationPullHistoryDO.getAgency02Status()));
            clientAgencyList.add(new ClientAgencyDTO(clientInfomationPullHistoryDO.getAgency03Code(), clientInfomationPullHistoryDO.getAgency03Name(), clientInfomationPullHistoryDO.getAgency03Status()));
            clientAgencyList.add(new ClientAgencyDTO(clientInfomationPullHistoryDO.getAgency04Code(), clientInfomationPullHistoryDO.getAgency04Name(), clientInfomationPullHistoryDO.getAgency04Status()));
            clientAgencyList.add(new ClientAgencyDTO(clientInfomationPullHistoryDO.getAgency05Code(), clientInfomationPullHistoryDO.getAgency05Name(), clientInfomationPullHistoryDO.getAgency05Status()));
            clientAgencyList.add(new ClientAgencyDTO(clientInfomationPullHistoryDO.getAgency06Code(), clientInfomationPullHistoryDO.getAgency06Name(), clientInfomationPullHistoryDO.getAgency06Status()));
            clientAgencyList.add(new ClientAgencyDTO(clientInfomationPullHistoryDO.getAgency07Code(), clientInfomationPullHistoryDO.getAgency07Name(), clientInfomationPullHistoryDO.getAgency07Status()));
            clientAgencyList.add(new ClientAgencyDTO(clientInfomationPullHistoryDO.getAgency08Code(), clientInfomationPullHistoryDO.getAgency08Name(), clientInfomationPullHistoryDO.getAgency08Status()));
            clientAgencyList.add(new ClientAgencyDTO(clientInfomationPullHistoryDO.getAgency09Code(), clientInfomationPullHistoryDO.getAgency09Name(), clientInfomationPullHistoryDO.getAgency09Status()));
            clientAgencyList.add(new ClientAgencyDTO(clientInfomationPullHistoryDO.getAgency10Code(), clientInfomationPullHistoryDO.getAgency10Name(), clientInfomationPullHistoryDO.getAgency10Status()));

            for (ClientAgencyDTO eachAgency : clientAgencyList) {
                if(eachAgency == null) {
                    continue;
                }
                
                if(eachAgency.getStatus() == null) {
                    continue;
                }
                
                switch (eachAgency.getStatus()) {
                    case "1":
                    case "2":
                        LOGGER.info("대리점 등록 [{}, {}]", clientInfomationPullHistoryDO.getClientId(), eachAgency.getCode());
                        clientDO.createAgency(eachAgency.getCode(), eachAgency.getName());
                        break;
                    case "3":
                    case "4":
                        LOGGER.info("대리점 삭제 [{}, {}]", clientInfomationPullHistoryDO.getClientId(), eachAgency.getCode());
                        clientDO.deleteAgency(eachAgency.getCode(), eachAgency.getName());
                        break;
                }
            }

            // 수수료
            final int paymentFee = clientInfomationPullHistoryDO.getPaymentFeeAmount();
            final int fingerFeeRate = clientInfomationPullHistoryDO.getFingerFeeRate();
            final int asisPaymentFee = clientInfomationPullHistoryDO.getAsisPaymentFeeAmount();
            final int asisFingerFeeRate = clientInfomationPullHistoryDO.getAsisFingerFeeRate();
            if(paymentFee != asisPaymentFee || fingerFeeRate != asisFingerFeeRate) {
                LOGGER.info("[{}] 수수료 적용 [{}원, {}%]", clientInfomationPullHistoryDO.getClientId(), paymentFee, fingerFeeRate);
                final int paymentBankFee = clientInfomationPullHistoryDO.getPaymentFeeAmount() * (100 - fingerFeeRate) / 100;
                clientDO.setReceiptFee(paymentFee);
                clientDO.setReceiptBankFee(paymentBankFee);
            }
            
            if(!StringUtils.equals(clientInfomationPullHistoryDO.getAsisBranchCode(), clientInfomationPullHistoryDO.getBranchCode())) {
                LOGGER.info("[{}] 관리지점 변경 {} => {}", clientInfomationPullHistoryDO.getClientId(), clientInfomationPullHistoryDO.getAsisBranchCode(), clientInfomationPullHistoryDO.getBranchCode());
                clientDO.modifyBranchCode(clientInfomationPullHistoryDO.getBranchCode());
            }

            clientRepository.update(clientDO);

            clientInfomationPullHistoryDO.switchStatusToSuccess();
            clientInformationPullHistoryRepository.update(clientInfomationPullHistoryDO);
        } catch (Exception e) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());
            }
            clientInfomationPullHistoryDO.switchStatusToFail();
            clientInformationPullHistoryRepository.update(clientInfomationPullHistoryDO);
            flag = false;
        }
        return flag;
    }

    public void createClient(String clientId, String clientName, String deleteYn, String createDt, String clientIdentityNo, String branchCode) {
        if(clientRepository.get(clientId) != null) {
            return;
        }

        LOGGER.info("신규 기관 [{}]", clientIdentityNo);

        final CompanyStatusLookupAPI companyStatusLookupAPI = new CompanyStatusLookupAPI(scrappingHost, scrappingPort);

        final ClientDO clientDO = ClientDO.newInstance(clientId, clientName, deleteYn, createDt, clientIdentityNo, branchCode);
        clientDO.lookupStatusOnNTS(companyStatusLookupAPI);

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

    public List<ClientInfomationPullHistoryDTO> getStandbyList() {
        final List<ClientInfomationPullHistoryDO> doList = clientInformationPullHistoryRepository.findByStatus(ClientInfomationPullHistoryDO.STATUS_CD_STANDBY);
        final List<ClientInfomationPullHistoryDTO> dtoList = new ArrayList<>(doList.size());

        for (ClientInfomationPullHistoryDO each : doList) {
            ClientInfomationPullHistoryDTO dto = new ClientInfomationPullHistoryDTO();
            dto.setPullDt(each.getPullDt());
            dto.setClientId(each.getClientId());
            dto.setClientName(each.getClientName());
            dto.setClientStatus(each.getClientStatus());
            
            dtoList.add(dto);
        }
        
        return dtoList;
    }
}
