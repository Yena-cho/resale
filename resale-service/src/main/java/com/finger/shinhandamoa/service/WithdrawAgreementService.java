package com.finger.shinhandamoa.service;

import com.finger.shinhandamoa.common.CooconArsAPI;
import com.finger.shinhandamoa.data.file.mapper.SimpleFileMapper;
import com.finger.shinhandamoa.data.table.mapper.ChaMapper;
import com.finger.shinhandamoa.data.table.mapper.CooconARSMapper;
import com.finger.shinhandamoa.data.table.mapper.FwFileMapper;
import com.finger.shinhandamoa.data.table.mapper.WithdrawAgreementMapper;
import com.finger.shinhandamoa.data.table.model.Cha;
import com.finger.shinhandamoa.data.table.model.CooconARS;
import com.finger.shinhandamoa.data.table.model.FwFile;
import com.finger.shinhandamoa.data.table.model.WithdrawAgreement;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 출금 동의 서비스
 * 
 * @author wisehouse@finger.co.kr
 */
@Service
public class WithdrawAgreementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WithdrawAgreementService.class);
    
    @Autowired
    private ChaMapper chaMapper;
    
    @Autowired
    private CooconARSMapper cooconARSMapper;
    
    @Autowired
    private WithdrawAgreementMapper withdrawAgreementMapper;
    
    @Autowired
    private FwFileMapper fwFileMapper;
    
    @Autowired
    private SimpleFileMapper simpleFileMapper;

    @Value("${coocon.ars.access-token}")
    private String accessToken;
    
    @Value("${coocon.ars.org-cd}")
    private String corpCode;
    
    @Value("${coocon.ars.url}")
    private String file;
    

    /**
     * ARS 출금동의
     * 
     * @param clientId 기관번호
     * @return
     */
    public Map<String, String> doAgreementWithARS(final String clientId) {
        try {
        final Cha cha = chaMapper.selectByPrimaryKey(clientId);
        
        final String contactNo = StringUtils.replace(cha.getChrhp(), "-", "");
        
        // 1. ARS 인증
        final String currentTimeMillisString = String.valueOf(System.currentTimeMillis());
        final String authNo = StringUtils.substring(currentTimeMillisString, currentTimeMillisString.length()-2, currentTimeMillisString.length());
        final String message = String.format("신한다모아 관리수수료 출금동의를 시작합니다. %s 사업자번호 <digit>%s</digit> 로 개설된 계좌 신한은행 <digit>%s</digit> 로 수수료가 자동이체 됩니다.", cha.getChaname(), cha.getChaoffno(), cha.getFingerFeeAccountNo());
        final String trCd = "2300";

        final CooconArsAPI cooconArsAPI = new CooconArsAPI(corpCode, accessToken, file);
        final CooconArsAPI.Output output = cooconArsAPI.createArs(trCd, contactNo, authNo, message, true, String.format("%s.mp3", currentTimeMillisString));
        
        if(output == null) {
            final Map<String, String> resultMap = new HashMap<>();
            resultMap.put("code", "9999");
            resultMap.put("message", "서버 오류");
            
            return resultMap;
        }

        final Map<String, String> resultMap = new HashMap<>();
        resultMap.put("code", output.getRSLT_CD());
        resultMap.put("message", output.getRSLT_MSG());
        
        // 2. 쿠콘 데이터 등록
        final CooconARS cooconARS = new CooconARS();
        cooconARS.setReqTrCd(output.getInput().getTR_CD());
        cooconARS.setReqOrgCd(output.getInput().getORG_CD());
        cooconARS.setReqTxDate(output.getInput().getDATE());
        cooconARS.setReqPhone(output.getInput().getPHONE());
        cooconARS.setReqAuthNo(output.getInput().getAUTH_NO());
        cooconARS.setReqFileSaveYn(output.getInput().getFILE_SAVE_YN());
        cooconARS.setReqFileNm(output.getInput().getFILE_NM());
        cooconARS.setReqAuthInquery(output.getInput().getAUTH_INQUERY());
        cooconARS.setRespRsltCd(output.getRSLT_CD());
        cooconARS.setRespRsltMsg(output.getRSLT_MSG());

            if(output.getRESP_DATA() == null) {
                resultMap.put("code", "9999");
                resultMap.put("message", "서버 오류");

                return resultMap;
            }

        if(!output.getRESP_DATA().isEmpty()) {
            CooconArsAPI.ResponseData respData = output.getRESP_DATA().get(0);
            cooconARS.setRespTrCd(respData.getTR_CD());
            cooconARS.setRespTxtNo(respData.getTXT_NO());
        }
        cooconARSMapper.insert(cooconARS);

        // 인증에 실패하면 아웃
        if(!StringUtils.equals(output.getRSLT_CD(), "0000")) {
            LOGGER.info("인증 실패");
            return resultMap;
        }
        
        // 녹취 데이터가 없으면 아웃
        if(output.getRESP_DATA().isEmpty()) {
            LOGGER.info("데이터 없음");
            return resultMap;
        }
        
        // 인증에 성공하면
        // 3. 출금 동의서 생성
        // 3.1. 파일 생성
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            CooconArsAPI.ResponseData responseData = output.getRESP_DATA().get(0);
            String recordData = responseData.getRECORD_DATA();
            for(int i = 0; i< recordData.length(); i+=2) {
                int b = Integer.parseInt(recordData.substring(i, i+2), 16);
                baos.write(b);
            }

            FwFile fwFile = new FwFile();
            fwFile.setBucket("WITHDRAW_AGREEMENT");
            fwFile.setName(responseData.getTXT_NO() + ".mp3");
            fwFile.setMimeType("audio/mpeg3");
            fwFile.setLength((long) baos.toByteArray().length);
            fwFile.setCreateDate(new Date());
            fwFile.setCreateUser(clientId);
            fwFileMapper.insert(fwFile);

            simpleFileMapper.store(fwFile.getBucket(), fwFile.getId(), new ByteArrayInputStream(baos.toByteArray()));


            // 3.2. 출금 동의서 등록 
            WithdrawAgreement withdrawAgreement = new WithdrawAgreement();
            withdrawAgreement.setType("W00002");
            withdrawAgreement.setStatus("A00002");
            withdrawAgreement.setFileId(fwFile.getId());
            withdrawAgreement.setCreateDate(new Date());
            withdrawAgreement.setCreateUser(clientId);
            withdrawAgreement.setAgreeUserType("W10001");
            withdrawAgreement.setAgreeUser(clientId);
            withdrawAgreementMapper.insert(withdrawAgreement);

            // 4. 기관 변경
            // 4.1. 상태 변경 (미동의 => 승인대기)
            cha.setFingerFeeAgreeStatus("A00002");
            // 4.2. 출금동의서 설정
            cha.setWithdrawAgreementId(withdrawAgreement.getId());
            chaMapper.updateByPrimaryKey(cha);

            return resultMap;
        } catch (Exception e) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.warn(e.getMessage(), e);
            } else {
                LOGGER.warn(e.getMessage());
            }

            final Map<String, String> resultMap = new HashMap<>();
            resultMap.put("code", "9999");
            resultMap.put("message", "서버 오류");
            
            return resultMap;
        }
    }

    public FwFile getFileInformation(String withdrawAgreementId) {
        WithdrawAgreement withdrawAgreement = withdrawAgreementMapper.selectByPrimaryKey(withdrawAgreementId);
        FwFile fwFile = fwFileMapper.selectByPrimaryKey(withdrawAgreement.getFileId());

        return fwFile;
    }

    public InputStream getFile(String withdrawAgreementId) throws IOException {
        WithdrawAgreement withdrawAgreement = withdrawAgreementMapper.selectByPrimaryKey(withdrawAgreementId);
        InputStream is = simpleFileMapper.load("WITHDRAW_AGREEMENT", withdrawAgreement.getFileId());

        return is;
    }
}
