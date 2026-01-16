package com.finger.shinhandamoa.org.custMgmt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.finger.shinhandamoa.common.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.finger.shinhandamoa.org.custMgmt.dao.CustExcelDAO;
import com.finger.shinhandamoa.org.custMgmt.dao.CustMgmtDAO;
import com.finger.shinhandamoa.org.custMgmt.dto.CustReg01DTO;

/**
 * @author by puki
 * @date 2018. 4. 12.
 * @desc 최초생성
 */
@Service
public class CustExcelServiceImpl implements CustExcelService {

    private static final Logger logger = LoggerFactory.getLogger(CustExcelServiceImpl.class);

    @Inject
    private CustExcelDAO custExcelDAO;

    @Inject
    private CustMgmtDAO custMgmtDAO;

    @Autowired
    private PlatformTransactionManager transactionManager;

    DefaultTransactionDefinition def = null;
    TransactionStatus status = null;

    @Override
    public List<CustReg01DTO> selectExcelSaveCustReg(Map<String, Object> map) throws Exception {
        return custExcelDAO.selectExcelSaveCustReg(map);
    }

    @Override
    public List<CustReg01DTO> excelList(Map<String, Object> map) throws Exception {
        return custExcelDAO.excelList(map);
    }

    @SuppressWarnings("unused")
    @Transactional
    @Override
    public int custExcelInsert(CustReg01DTO dto) throws Exception {
        int updateCnt = 0;
        int failCnt = 0;
        int resCnt = 0;

        try {
            logger.debug(">>>>>>>>>> :: 고객 대량 등록 정상 내역 저장 ServiceImpl");

            def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);

            String retVal = "";
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("chaCd", dto.getChaCd());
            map.put("cusName", StrUtil.nullToVoid(dto.getCusName()));
            map.put("cusKey", StrUtil.nullToVoid(dto.getCusKey()));
            map.put("rcpGubn", StrUtil.nullToVoid(dto.getRcpGubn()));                           // 납부대상 (Y=대상 / N=제외)
            map.put("cusHp", StrUtil.nullToVoid(dto.getCusHp()));                               // 연락처 (휴대폰)
            map.put("cusMail", StrUtil.nullToVoid(dto.getCusMail()));                           // 이메일
            map.put("cusGubn1", StrUtil.nullToVoid(dto.getCusGubn1()));
            map.put("cusGubn2", StrUtil.nullToVoid(dto.getCusGubn2()));
            map.put("cusGubn3", StrUtil.nullToVoid(dto.getCusGubn3()));
            map.put("cusGubn4", StrUtil.nullToVoid(dto.getCusGubn4()));
            map.put("cusType", StrUtil.nullToVoid(dto.getCusType()));                           // 발금용도 (1=소득공제 / 2=지출증빙)
            map.put("confirm", StrUtil.nullToVoid(dto.getConfirm()));                           // 현금영수증발급방법 (11=휴대폰번호 / 21=사업자번호)
            map.put("cusOffNo", StrUtil.nullToVoid(dto.getCusOffNo()));                         // 현금영수증발급 번호
            map.put("memo", StrUtil.nullToVoid(dto.getMemo()));                                 // 메모 (고객 특이사항)

            String vano = "";
            /**
             * 가상계좌 채번
             *
             * vanoInfo.getUseYn     Y 사용중 | N 미사용 ( 고객 상태와는 별개 )
             * vanoInfo.getVano      가상계좌번호
             */
            HashMap<String, Object> vanoInfoMap = new HashMap<String, Object>();
            vanoInfoMap.put("chaCd", dto.getChaCd());
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
            // 고객번호 입력 시 고객 상태
            HashMap<String, Object> cusChk = new HashMap<String, Object>();
            cusChk.put("chaCd", dto.getChaCd());

            // 가상계좌번호 입력 시 고객 상태
            HashMap<String, Object> cusVanoChk = new HashMap<String, Object>();
            cusVanoChk.put("chaCd", dto.getChaCd());

            if (vanoInfo == null) {
                // 채번 실패
                if (!"".equals(dto.getVano())) {
                    // 가상계좌를 입력 했으나, 기관에 부여된 가상계좌번호가 아님.
                    dto.setResult("사용할 수 없는 가상계좌");
                    failCnt++;
                } else {
                    // 가상계좌 미입력 했으나, 기관에 부여된 가상계좌가 없음.
                    dto.setResult("사용가능한 가상계좌가 없음");
                    failCnt++;
                }
            } else {
                // 채번 성공

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
                                // 가상계좌 미입력 시
                                vano = StrUtil.nullToVoid(vanoInfo.getVano());
                            } else {
                                // 가상계좌 입력 시
                                vano = StrUtil.nullToVoid(dto.getVano());
                            }
                            resCnt++;
                        } else {
                            // 고객번호 중복
                            dto.setResult("사용중인 고객번호");
                            failCnt++;
                        }
                    } else {
                        // 고객번호 미입력 시, 고객번호는 신규이므로
                        if ("".equals(dto.getVano())) {
                            // 가상계좌 미입력 시
                            vano = StrUtil.nullToVoid(vanoInfo.getVano());
                        } else {
                            // 가상계좌 입력 시
                            vano = StrUtil.nullToVoid(dto.getVano());
                        }
                        resCnt++;
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
                    cusVanoChk.put("vano", dto.getVano());

                    CustReg01DTO retXcusMasVano = new CustReg01DTO();
                    retXcusMasVano = custMgmtDAO.selectDetailCustReg(cusVanoChk);

                    if ("Y".equals(retXcusMasVano.getDisabled())) {
                        // 가상계좌가 사용중이나, 고객 상태가 삭제
                        dto.setResult("삭제된 고객");
                        failCnt++;
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
                                        map.put("vano", dto.getVano());
                                        updateCnt++;
                                    } else {
                                        // 입력한 고객번호값과 불일치 시, 이미 사용중인 고객번호
                                        dto.setResult("사용중인 고객번호");
                                        failCnt++;
                                    }
                                } else {
                                    // 사용중인 가상계좌이면서 삭제된 고객이나, 이미 사용했던 고객 번호임으로 중복 번호
                                    dto.setResult("사용중인 고객번호");
                                    failCnt++;
                                }
                            } else {
                                // 신규 고객번호이나 사용중인 가상계좌번호 임으로 고객 수정
                                map.put("vano", dto.getVano());
                                map.put("cusKey", dto.getCusKey());
                                updateCnt++;
                            }
                        } else {
                            // 고객번호값 미입력 시, 사용중인 가상계좌에 미입력 고객번호임으로 고객 수정
                            map.put("vano", dto.getVano());
                            updateCnt++;
                        }
                    }
                }
            }

            if (failCnt > 0) {
                logger.debug(">>>>>>>>>> :: 실패");
                custExcelFailInsert(dto);
            } else if (updateCnt > 0) {
                logger.debug(">>>>>>>>>> :: 가상계좌번호 중복 건, 기존 고객 수정");
                custExcelDAO.custExcelUpdate(map);
            } else {
                logger.debug(">>>>>>>>>> :: 가상계좌번호 미중복 건, 신규 고객 등록");

                dto.setSmsYn("N");
                dto.setMailYn("N");
                dto.setDisabled("I");
                dto.setVano(vano);
                custExcelDAO.custExcelInsert(dto);

                map.put("chaCd", dto.getChaCd());
                map.put("vano", vano);
                map.put("cusCd", dto.getCusKey());
                map.put("useYn", 'Y');
                custMgmtDAO.updateValist(map);
            }

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error(e.getMessage());
        }
        return failCnt;
    }

    @Override
    public void custExcelFailInsert(CustReg01DTO dto) throws Exception {
        custExcelDAO.custExcelFailInsert(dto);
    }

    @Override
    public void custFailDelete(String chaCd) throws Exception {
        custExcelDAO.custFailDelete(chaCd);
    }

    @Override
    public List<CustReg01DTO> failList(String chaCd, int start, int end) throws Exception {
        return custExcelDAO.failList(chaCd, start, end);
    }

    @Override
    public int failTotalCount(String chaCd) throws Exception {
        return custExcelDAO.failTotalCount(chaCd);
    }

    @Override
    public List<CustReg01DTO> failExcelList(Map<String, Object> map) throws Exception {
        return custExcelDAO.failExcelList(map);
    }

}
