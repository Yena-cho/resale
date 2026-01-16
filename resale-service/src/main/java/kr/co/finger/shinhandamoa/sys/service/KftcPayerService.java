package kr.co.finger.shinhandamoa.sys.service;


import com.finger.shinhandamoa.common.ImageUtils;
import com.finger.shinhandamoa.data.file.mapper.SimpleFileMapper;
import com.finger.shinhandamoa.data.table.mapper.FwFileMapper;
import com.finger.shinhandamoa.data.table.mapper.WithdrawAgreementMapper;
import com.finger.shinhandamoa.data.table.model.FwFile;
import com.finger.shinhandamoa.data.table.model.WithdrawAgreement;
import kr.co.finger.msgio.msg.*;
import kr.co.finger.shinhandamoa.domain.model.ClientDO;
import kr.co.finger.shinhandamoa.domain.model.KftcPayerDO;
import kr.co.finger.shinhandamoa.domain.repository.ClientRepository;
import kr.co.finger.shinhandamoa.domain.repository.KftcPayerRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 금융결제원 납부자 서비스
 * 
 * @author wisehouse@finger.co.kr
 */
@Service
public class KftcPayerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KftcPayerService.class);

    @Autowired
    private KftcPayerRepository kftcPayerRepository;
    
    @Autowired
    private ClientRepository clientRepository;

    @Value("${autoWithdrawal.corpCode}")
    private String fingerCorpCode;    // 핑거이용기관번호
    
    @Autowired
    private WithdrawAgreementMapper withdrawAgreementMapper;
    
    @Autowired
    private SimpleFileMapper fileMapper;
    
    @Autowired
    private FwFileMapper fwFileMapper;
    

    @Transactional
    public EB13 getEB13(List<String> statusCdList) throws Exception {
        final long totalItemCount = kftcPayerRepository.count(statusCdList);
        final List<KftcPayerDO> doList = kftcPayerRepository.getList(statusCdList);

        final Date target = DateUtils.addHours(new Date(), -12);
        final String fileName = "EB13" + new SimpleDateFormat("MMdd").format(target);
        final String now_yyMMdd = new SimpleDateFormat("yyMMdd").format(target);

        final NewHeader newHeader = new NewHeader();
        newHeader.setCorpCode(fingerCorpCode);
        newHeader.setFileName(fileName);
        newHeader.setReqData(now_yyMMdd);
        
        final NewTrailer newTrailer = new NewTrailer();
        newTrailer.setCorpCode(fingerCorpCode);
        newTrailer.setFileName(fileName);
        newTrailer.setTotalRecordCount(new DecimalFormat("00000000").format(totalItemCount));
        // TODO 신규/변경/해지/임의해지 구분해야 한다
        newTrailer.setNewReqCount(new DecimalFormat("00000000").format(totalItemCount));
        newTrailer.setModiReqCount("00000000");
        newTrailer.setMoveReqCount("00000000");
        newTrailer.setTempMoveReqCount("00000000");
        
        final EB13 eb13 = new EB13();
        eb13.setHeader(newHeader);

        for (int i=0; i<doList.size(); i++) {
            final KftcPayerDO each = doList.get(i);
            final ClientDO clientDO = clientRepository.getByKftcPayerNo(each.getId());

            final NewData newData = new NewData();
            newData.setSeqNo(new DecimalFormat("00000000").format(i+1));
            newData.setCorpCode(fingerCorpCode);
            newData.setReqDate(now_yyMMdd);
            // TODO 신규/해지/임의해지 모두 처리해야 한다
            newData.setReqType("1");
            newData.setPayerNo(each.getId());
            newData.setBankCode(each.getPayerKftcCode() + "0000");
            newData.setWithdrawalAccountNo(each.getPayerAccountNo());
            newData.setCompRegNo(each.getPayerIdentityNo());
            newData.setDealerCode("    ");
            // TODO 코드에 따라서 처리하도록 수정
            newData.setFundType("  ");
            newData.setResultCode(" ");
            newData.setErrorCode("    ");
            newData.setPhoneNo(each.getPayerContactNo());
            
            eb13.addDataList(newData);

            clientDO.registerAsKftcPayer();
            clientRepository.update(clientDO);
            
            each.register(fileName);
            kftcPayerRepository.update(each);
        }
        
        eb13.setTrailer(newTrailer);
        
        return eb13;
    }

    public EI13 getEI13(List<String> statusCdList) throws Exception {
        final Date target = DateUtils.addHours(new Date(), -12);
        final String fileName = "EB13" + new SimpleDateFormat("MMdd").format(target);
        final String now_yyMMdd = new SimpleDateFormat("yyMMdd").format(target);
        final String now_yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(target);

        final long totalItemCount = kftcPayerRepository.count(statusCdList);
        final List<KftcPayerDO> doList = kftcPayerRepository.getList(statusCdList);

        final EvidenceHeader header = new EvidenceHeader();
        header.setReqData(now_yyyyMMdd);
        header.setCorpCode(fingerCorpCode);
        header.setTotalEvidenceCount(String.valueOf(totalItemCount));

        final EvidenceTrailer trailer = new EvidenceTrailer();
        trailer.setCorpCode(fingerCorpCode);
        trailer.setTotalDataRecordCount(String.valueOf(totalItemCount));

        final EI13 ei13 = new EI13();
        ei13.setHeader(header);

        long totalLength = 0;
        for (int i=0; i<doList.size(); i++) {
            final KftcPayerDO each = doList.get(i);
            final ClientDO clientDO = clientRepository.getByKftcPayerNo(each.getId());
            String withdrawAgreementId = clientDO.getWithdrawAgreementId();
            WithdrawAgreement withdrawAgreement = withdrawAgreementMapper.selectByPrimaryKey(withdrawAgreementId);
            String fileId = withdrawAgreement.getFileId();
            String withdrawAgreementType = withdrawAgreement.getType();
            FwFile fwFile = fwFileMapper.selectByPrimaryKey(fileId);

            EvidenceData data = new EvidenceData();
            data.setSeqNo(String.valueOf(i+1));
            data.setCorpCode(fingerCorpCode);
            data.setPayerNo(each.getId());
            data.setBankCd(each.getPayerKftcCode());
            data.setAccountNo(each.getPayerAccountNo());
            data.setReqData(now_yyyyMMdd);
            
            switch(withdrawAgreementType) {
                case "W00001":
                    data.setEvidenceType("1");
                    break;
                case "W00002":
                    data.setEvidenceType("5");
                    break;
            }
            
            data.setEvidenceExt(StringUtils.substringAfterLast(fwFile.getName(), "."));

            byte[] bytes = getBytes(fwFile);
            data.setEvidenceLength(String.valueOf(bytes.length));
            data.setupEvidenceData(bytes, LOGGER);
            totalLength += data.size();

            ei13.addDataList(data);

            clientDO.registerAsKftcPayer();
            clientRepository.update(clientDO);

            each.register(fileName);
            kftcPayerRepository.update(each);
        }

        trailer.setTotalDataBlockCount(String.valueOf(totalLength/1024));
        ei13.setTrailer(trailer);

        return ei13;
    }

    private byte[] getBytes(FwFile file) throws IOException {
        if(file.getLength() < 300 * 1000) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(fileMapper.load("WITHDRAW_AGREEMENT", file.getId()), output);
            byte[] bytes = output.toByteArray();
            LOGGER.debug("파일 용량 : {}", bytes.length);
            return bytes;
        }

        LOGGER.warn("파일 용량이 300KB 보다 큼");
        File inputFile = File.createTempFile("EI14", ".tmp");
        File outputFile = File.createTempFile("EI14", ".tmp");
        FileUtils.forceDelete(outputFile);
        FileUtils.copyInputStreamToFile(fileMapper.load("WITHDRAW_AGREEMENT", file.getId()), inputFile);
        ImageUtils.resizeJpeg(inputFile, outputFile, 1024);

        byte[] bytes = IOUtils.toByteArray(new FileInputStream(outputFile));
        LOGGER.debug("파일 용량 : {}", bytes.length);
        return bytes;
    }

    /**
     * EB14 처리
     * 
     * @param eb14
     * @throws Exception
     */
    public void writeEB14(EB14 eb14) throws Exception {
        final String fileName = eb14.getHeader().getFileName();
        final String eb13Id = "EB13" + StringUtils.substringAfter(fileName, "EB14");
        
        // 1. 불능 응답 건 처리
        // 1.1. 기관 상태 변경
        // 1.2. 자동이체 신청 상태 변경
        // 1.3. 동의 상태 변경
        for (NewData each : eb14.getDataList()) {
            final String payerId = StringUtils.trim(each.getPayerNo());
            
            final ClientDO clientDO = clientRepository.getByKftcPayerNo(payerId);
            
            final String resultCode = each.getResultCode();
            final String errorCode = each.getErrorCode();

            final KftcPayerDO kftcPayerDO = kftcPayerRepository.get(payerId);
            kftcPayerDO.updateResult(resultCode, "K3" + errorCode, fileName);
            clientDO.failToRegisterAsKftcPayer();
            
            final WithdrawAgreement withdrawAgreement = withdrawAgreementMapper.selectByPrimaryKey(clientDO.getWithdrawAgreementId());
            withdrawAgreement.setStatus("A00002");

            clientRepository.update(clientDO);
            kftcPayerRepository.update(kftcPayerDO);
        }
        
        // 불능 응답에 포함되지 않는 건은 정상으로 처리
        final List<KftcPayerDO> itemList = kftcPayerRepository.getListByEb13Id(eb13Id);
        for (KftcPayerDO each : itemList) {
            if(StringUtils.equals(each.getSuccessYn(), "N")) {
                continue;
            }
            
            final ClientDO clientDO = clientRepository.getByKftcPayerNo(each.getId());

            each.updateResult("Y", "K30000", fileName);
            clientDO.completeRegisterAsKftcPayer();

            clientRepository.update(clientDO);
            kftcPayerRepository.update(each);
        }
    }
}
