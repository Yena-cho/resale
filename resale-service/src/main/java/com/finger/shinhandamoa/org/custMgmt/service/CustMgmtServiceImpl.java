package com.finger.shinhandamoa.org.custMgmt.service;

import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.org.custMgmt.dao.CustMgmtDAO;
import com.finger.shinhandamoa.org.custMgmt.dto.CustReg01DTO;
import com.finger.shinhandamoa.org.custMgmt.web.CustExcelController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.HashMap;
import java.util.List;

/**
 * @author 홍길동
 * @date 2018. 3. 30.
 * @desc 최초생성
 */
@Service
public class CustMgmtServiceImpl implements CustMgmtService {
    private static final Logger logger = LoggerFactory.getLogger(CustExcelController.class);

    @Autowired
    private CustMgmtDAO custMgmtDAO;

    @Autowired
    private PlatformTransactionManager transactionManager;

    DefaultTransactionDefinition def = null;
    TransactionStatus status = null;

    // 고객관리-고객등록01목록조회
    @Override
    public int custReg01TotalCount(HashMap<String, Object> map) throws Exception {
        return custMgmtDAO.custReg01TotalCount(map);
    }

    @Override
    public List<CustReg01DTO> custReg01ListAll(HashMap<String, Object> map) throws Exception {
        return custMgmtDAO.custReg01ListAll(map);
    }

    @Transactional
    @Override
    public void deleteXcusMas(HashMap<String, Object> map) throws Exception {
        try {
            def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);

            custMgmtDAO.updateVanoUseYn(map);    // 가상계좌 사용여부 'N'
            custMgmtDAO.deleteXcusMas(map);        // 고정정보 삭제

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error(e.getMessage());
        }
    }

    @Transactional
    @Override
    public void deleteCusInfo(HashMap<String, Object> map) throws Exception {
        try {
            def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);

            custMgmtDAO.updateVano(map);        // 가상계좌 사용여부 'N'
            custMgmtDAO.updateCusmas(map);      // disabled 변경
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error(e.getMessage());
        }
    }

    @Transactional
    @Override
    public String insertXCustMas(CustReg01DTO dto) throws Exception {
        String errCode = "0000";

        try {
            def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String chaCd = authentication.getName();

            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            String vano = "";

            /**
             * 가상계좌 채번
             *
             * vanoInfo.getUseYn     Y 사용중 | N 미사용 ( 고객 상태와는 별개 )
             * vanoInfo.getVano      가상계좌번호
             */
            HashMap<String, Object> vanoInfoMap = new HashMap<String, Object>();
            vanoInfoMap.put("chaCd", chaCd);
            if (!"".equals(dto.getVano())) {
                vanoInfoMap.put("vano", dto.getVano());
            }
            if ("".equals(dto.getVano())) {
                vanoInfoMap.put("useYn", "N");
            }
            CustReg01DTO vanoInfo = custMgmtDAO.getVanoInfo(vanoInfoMap);

            /**
             * 고객 목록 조회 - 고객 상태 및 가상계좌에 할당된 고객번호이 맞는지 확인용
             *
             * retXcusMas.getCusKey             고객번호
             * retXcusMas.getVano               가상계좌번호
             * retXcusMas.getDisabled           N 정상 |  Y 탈퇴
             */
            HashMap<String, Object> cusChk = new HashMap<String, Object>();
            HashMap<String, Object> cusVanoChk = new HashMap<String, Object>();
            cusChk.put("chaCd", chaCd);

            if (vanoInfo == null) {
                // 가상계좌 채번 실패

                if (!"".equals(dto.getVano())) {
                    // 가상계좌를 입력 했으나, 기관에 부여된 가상계좌번호가 아님.
                    errCode = "0001";
                } else {
                    // 가상계좌 미입력 했으나, 기관에 부여된 가상계좌가 없음.
                    errCode = "0002";
                }
            } else {
                // 가상계좌 채번 성공

                /**
                 * 신규등록     0000
                 * 가상계좌가 신규이면서, 고객번호도 신규 일 때
                 *
                 * 번호중복     0005
                 * 가상계좌의 사용여부와 고객 상태값 상관없이 고객 번호가 이미 사용 된 고객 번호 일 때
                 */
                if (!"Y".equals(vanoInfo.getUseYn())) {
                    // 가상계좌번호 신규 ( 사용중이 아님 )

                    if (!"".equals(dto.getCusKey())) {
                        // 고객번호 입력 시
                        cusChk.put("cusKey", dto.getCusKey());

                        CustReg01DTO retXcusMas = new CustReg01DTO();
                        retXcusMas = custMgmtDAO.selectDetailCustReg(cusChk);

                        if (retXcusMas == null) {
                            // 고객번호 신규

                            if ("".equals(dto.getVano())) {
                                vano = StrUtil.nullToVoid(vanoInfo.getVano());
                            } else {
                                vano = StrUtil.nullToVoid(dto.getVano());
                            }
                            errCode = "0000";
                        } else {
                            // 고객번호 중복
                            errCode = "0005";
                        }
                    } else {
                        // 고객번호 미입력 시, 고객번호는 신규이므로

                        if ("".equals(dto.getVano())) {
                            vano = StrUtil.nullToVoid(vanoInfo.getVano());
                        } else {
                            vano = StrUtil.nullToVoid(dto.getVano());
                        }
                        errCode = "0000";
                    }
                }

                /**
                 * 고객수정     0003
                 * 가상계좌가 사용중이면서, 고객번호가 신규이거나 입력한 가상계좌번호가 사용하고있는 고객번호 일 때
                 *
                 * 삭제고객     0004
                 * 가상계좌가 사용중이면서, 고객 상태가 삭제일 때
                 *
                 * 번호중복     0005
                 * 가상계좌의 사용여부와 고객 상태값 상관없이 고객 번호가 이미 사용 된 고객 번호 일 때
                 */
                if ("Y".equals(vanoInfo.getUseYn())) {
                    // 가상계좌 사용중

                    // 입력한 가상계좌의 고객 상태 확인
                    cusVanoChk.put("chaCd", chaCd);
                    cusVanoChk.put("vano", dto.getVano());

                    CustReg01DTO retXcusMasVano = new CustReg01DTO();
                    retXcusMasVano = custMgmtDAO.selectDetailCustReg(cusVanoChk);

                    if("Y".equals(retXcusMasVano.getDisabled())) {
                        // 가상계좌가 사용중이나, 고객 상태가 삭제
                        errCode = "0004";
                    } else {
                        if (!"".equals(dto.getCusKey())) {
                            // 고객번호값을 입력했을 때, 고객번호 중복 체크
                            cusChk.put("cusKey", dto.getCusKey());

                            CustReg01DTO retXcusMas = new CustReg01DTO();
                            retXcusMas = custMgmtDAO.selectDetailCustReg(cusChk);

                            if (retXcusMas != null) {
                                // 이미 사용중인 고객번호

                                if (!"Y".equals(retXcusMas.getDisabled())) {
                                    // 고객 상태가 삭제가 아니면서

                                    if ((retXcusMas.getVano()).equals(dto.getVano())) {
                                        // 입력한 고객번호값이 사용하는 가상계좌와 입력 가상계좌가 동일 할 때
                                        dto.setDisabled("N");
                                        dto.setJobType("U");
                                        vano = StrUtil.nullToVoid(dto.getVano());
                                        errCode = "0003";
                                    } else {
                                        // 입력한 고객번호값과 불일치 시, 이미 사용중인 고객번호
                                        errCode = "0005";
                                    }
                                } else {
                                    // 사용중인 가상계좌이면서 삭제된 고객이나, 이미 사용했던 고객 번호임으로 중복 번호
                                    errCode = "0005";
                                }
                            } else {
                                // 신규 고객번호이나 사용중인 가상계좌번호 임으로 고객 수정
                                dto.setDisabled("N");
                                dto.setJobType("U");
                                vano = StrUtil.nullToVoid(dto.getVano());
                                errCode = "0003";
                            }
                        } else {
                            // 고객번호값 미입력 시, 사용중인 가상계좌에 미입력 고객번호임으로 고객 수정
                            dto.setDisabled("N");
                            dto.setJobType("U");
                            vano = StrUtil.nullToVoid(dto.getVano());
                            errCode = "0003";
                        }
                    }
                }
            }

            if (errCode == "0000") {
                logger.debug(">>>>>>>>>> :: 고객 신규등록");
                // 4.고객원장 등록(xcusmas)
                dto.setChaCd(chaCd);
                dto.setVano(vano);
                dto.setNotiMasCd(vano);
                if ("".equals(StrUtil.nullToVoid(dto.getCusKey()))) {
                    dto.setCusKey(vano);
                }
                custMgmtDAO.insertXCustMas(dto);

                // 5.가상계좌번호원장 사용여부 (Y)업데이트
                reqMap = new HashMap<String, Object>();
                reqMap.put("chaCd", chaCd);
                reqMap.put("useYn", "Y");
                reqMap.put("vano", vano);
                reqMap.put("cusCd", dto.getCusKey());
                custMgmtDAO.updateValist(reqMap);
            } else if ("0003".equals(errCode)) {
                logger.debug(">>>>>>>>>> :: 기존고객 수정");
                dto.setChaCd(chaCd);
                dto.setVano(vano);
                dto.setNotiMasCd(vano);
                custMgmtDAO.updateXcusMas(dto);
            }

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error(e.getMessage());
        }
        return errCode;
    }

    @Override
    public void updateXcusMas(CustReg01DTO dto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        dto.setChaCd(chaCd);

        custMgmtDAO.updateXcusMas(dto);
    }

    @Override
    public void updateCusmas(HashMap<String, Object> map) throws Exception {
        custMgmtDAO.updateCusmas(map);
    }

    //@Override
    public CustReg01DTO selectDetailCustReg(HashMap<String, Object> map) throws Exception {
        return custMgmtDAO.selectDetailCustReg(map);
    }

    @Override
    public CustReg01DTO getVanoInfo(HashMap<String, Object> map) throws Exception {
        return custMgmtDAO.getVanoInfo(map);
    }

    @Override
    public void updateValist(CustReg01DTO dto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("chaCd", chaCd);
        reqMap.put("useYn", dto.getUseYn());
        reqMap.put("cusCd", dto.getCusKey());
        reqMap.put("vano", dto.getVano());
        custMgmtDAO.updateValist(reqMap);
    }

    @Override
    public void updateNotiMasCusName(HashMap<String, Object> map) throws Exception {
        custMgmtDAO.updateNotiMasCusName(map);
    }

    @Override
    public int updateCashMasInfo(HashMap<String, Object> map) throws Exception {
        return custMgmtDAO.updateCashMasInfo(map);
    }
}
