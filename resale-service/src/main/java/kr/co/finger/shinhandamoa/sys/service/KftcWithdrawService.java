package kr.co.finger.shinhandamoa.sys.service;

import kr.co.finger.msgio.msg.*;
import kr.co.finger.shinhandamoa.domain.model.KftcWithdrawDO;
import kr.co.finger.shinhandamoa.domain.repository.KftcWithdrawRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 금융결제원 출금이체 서비스
 * 
 * @author wisehouse@finger.co.kr
 */
@Service
public class KftcWithdrawService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KftcWithdrawService.class);
    
    @Autowired
    private KftcWithdrawRepository kftcWithdrawRepository;
    
    // 핑거이용기관번호
    @Value("${autoWithdrawal.corpCode}")
    private String fingerCorpCode;

    // 핑거 은행코드+지점코드
    @Value("${autoWithdrawal.bankCode}")
    private String fingerBankCode;
    
    // 핑거 계좌번호
    @Value("${autoWithdrawal.accountNo}")
    private String fingerAccountNo;

    public EB21 getEB21(String payday) throws Exception {
        final List<KftcWithdrawDO> itemList = kftcWithdrawRepository.getList(Arrays.asList("K00001", "K00002"));
        
        final Date now = new SimpleDateFormat("yyyyMMdd").parse(payday);
        final String now_mmdd = new SimpleDateFormat("MMdd").format(now);
        final String now_yymmdd = new SimpleDateFormat("yyMMdd").format(now);
        final String fileName = "EB21" + now_mmdd;

        final EB21 eb21 = new EB21();

        // HEADER
        final BatchHeader header = new BatchHeader();
        header.setCorpCode(fingerCorpCode);
        header.setFileName(fileName);
        header.setWithdrawalDate(now_yymmdd);
        header.setMainBankCd(fingerBankCode);
        header.setDepositAccountNo(fingerAccountNo);

        eb21.setHeader(header);
        
        // DATA
        final String eb21Id = StringUtils.rightPad(header.getWithdrawalDate(), 10, ' ');

        int totalCount = 0;
        int totalAmount = 0;
        for (int i=0; i<itemList.size(); i++) {
            final KftcWithdrawDO each = itemList.get(i);

            final BatchData batchData = new BatchData();
            // 일련번호
            batchData.setSeqNo(String.valueOf(i+1));
            // 기관코드
            batchData.setCorpCode(fingerCorpCode);
            // 출금참가기관(은행)점 코드
            batchData.setWithdrawalBankCode(each.getPayerKftcCode() + "0000");
            // 출금계좌번호
            batchData.setWithdrawalAccntNo(each.getPayerAccountNo());
            // 출금의뢰금액
            batchData.setWithdrawalAmout(String.valueOf(each.getAmount()));
            // 예금주생년월일 또는 사업자등록번호
            batchData.setCompRegNo(each.getPayerIdentityNo());
            // 출금결과 - 출금여부
            batchData.setResultCode(" ");
            // 출금결과 - 불능코드
            batchData.setErrorCode("    ");
            // 통장기재내용
            batchData.setPassBookInfo(each.getRemark());
            // TODO 자금종류
            batchData.setFundType("  ");
            // 납부자번호
            batchData.setPayerNo(each.getPayerNo());
            // 이용기관 사용 영역
            batchData.setTemp("     ");
            // 출금형태
            switch (each.getWithdrawTypeCd()) {
                case "K50001":
                    batchData.setWithdrawalType("0");
                    break;
                case "K50002":
                    batchData.setWithdrawalType("1");
                    break;
                case "K50003":
                    batchData.setWithdrawalType("2");
                    break;
                case "K50004":
                    batchData.setWithdrawalType("3");
                    break;
                case "K50005":
                    batchData.setWithdrawalType("4");
                    break;
                case "K50006":
                    batchData.setWithdrawalType("5");
                    break;
                case "K50007":
                    batchData.setWithdrawalType("6");
                    break;
            }
            // 전화번호
            batchData.setPhoneNo(each.getContactNo());
            
            eb21.addDataList(batchData);
            
            totalCount++;
            totalAmount += each.getAmount();

            final String dataNo = StringUtils.leftPad(String.valueOf(i +1), 8, '0');
            each.register(eb21Id, dataNo);
            kftcWithdrawRepository.update(each);
        }

        // TRAILER
        final EB21BatchTrailer trailer = new EB21BatchTrailer();
        trailer.setCorpCode(fingerCorpCode);
        trailer.setFileName(fileName);
        trailer.setTatalRecordCount(String.valueOf(totalCount));
        trailer.setAllCount(String.valueOf(totalCount));
        trailer.setAllAmount(String.valueOf(totalAmount));
        trailer.setPartialCount(String.valueOf(0));
        trailer.setPartialAmount(String.valueOf(0));

        eb21.setTrailer(trailer);

        return eb21;
    }
    
    public void writeEB22(EB22 eb22) {
        final String eb21Id = StringUtils.rightPad(eb22.getHeader().getWithdrawalDate(), 10, ' ');

        final Map<String, BatchData> dataMap = new HashMap<>();
        for (BatchData each : eb22.getDataList()) {
            LOGGER.debug("자동이체 출금관리 > 결과등록 > each.getSeqNo() : {}", each.getSeqNo());
            dataMap.put(each.getSeqNo(), each);
        }

        final List<KftcWithdrawDO> itemList = kftcWithdrawRepository.getByEb21Id(eb21Id);
        for (KftcWithdrawDO each : itemList) {
            LOGGER.debug("자동이체 출금관리 > 결과등록 > each.getDataNo() : {}", each.getDataNo());
            if(dataMap.containsKey(each.getDataNo())) {
                final BatchData eachData = dataMap.get(each.getDataNo());
                final String errorCode = eachData.getErrorCode();
                
                each.updateResult("N", "K3" + errorCode);
            } else {
                each.updateResult("Y", "K30000");
            }
            
            kftcWithdrawRepository.update(each);
        }
    }
}
