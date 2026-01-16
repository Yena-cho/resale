package kr.co.finger.damoa.shinhan.agent.util;

import kr.co.finger.damoa.commons.Constants;
import kr.co.finger.damoa.commons.Damoas;
import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.damoa.model.rcp.ChacdInfo;
import kr.co.finger.damoa.model.rcp.NotiDet;
import kr.co.finger.damoa.model.rcp.NotiMas;
import kr.co.finger.damoa.shinhan.agent.domain.model.ReceiptDetailsDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.ReceiptMasterDO;
import kr.co.finger.damoa.shinhan.agent.model.AggregateBean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class DamoaBizUtil {
    private static Logger LOG = LoggerFactory.getLogger(DamoaBizUtil.class);

    private DamoaBizUtil() {
    }

    /**
     * 처리해야 할 청구건이 여러건 일 수 있다.
     * 조회된 데이터를 기반으로 청구 데이터로 변환한다.
     *
     * @param mapList
     * @return
     */
    public static List<NotiMas> aggregate(List<Map<String, Object>> mapList) {
        List<NotiMas> notiMasList = new ArrayList<>();

        Map<String, NotiMas> map = new HashMap<>();
        for (Map<String, Object> _map : mapList) {
            NotiMas notiMas = new NotiMas();
            notiMas.setupFrom(_map);
            NotiDet notiDet = new NotiDet();
            notiDet.setupFrom(_map);

            String notimascd = notiMas.getNotimascd();
            handleMap(map, notimascd, notiMasList, notiMas, notiDet);
        }

        return notiMasList;
    }

    public static List<NotiMas> aggregateSimply(List<Map<String, Object>> mapList) {
        List<NotiMas> notiMasList = new ArrayList<>();

        Map<String, NotiMas> map = new HashMap<>();
        for (Map<String, Object> _map : mapList) {
            NotiMas notiMas = new NotiMas();
            notiMas.simpleSetupFrom(_map);
            NotiDet notiDet = new NotiDet();
            notiDet.simpleSetupFrom(_map);

            String notimascd = notiMas.getNotimascd();
            handleMap(map, notimascd, notiMasList, notiMas, notiDet);
        }
        return notiMasList;
    }

    /**
     * 청구금액과 이미 납부된 데이터를 조합함..
     *
     * @param notiMasList
     * @param rcpInfos    수납금액 데이터.  notimas + notidet (key), totalAmount
     * @return
     */
    public static void aggregate(List<NotiMas> notiMasList, Map<String, String> rcpInfos) {
        for (NotiMas notiMas : notiMasList) {
            for (NotiDet notiDet : notiMas.getNotiDets()) {
                notiDet.aggregate(rcpInfos);
            }
        }
    }

    private static void handleMap(Map<String, NotiMas> map, String notimascd, List<NotiMas> notiMasList, NotiMas notiMas, NotiDet notiDet) {
        if (map.containsKey(notimascd)) {
            NotiMas _notiMas = map.get(notimascd);
            _notiMas.add(notiDet);
        } else {
            notiMas.add(notiDet);
            map.put(notiMas.getNotimascd(), notiMas);
            notiMasList.add(notiMas);
        }
    }

    /**
     * 들어온 돈을 청구건으로 분배함.
     * <p>
     * 금액과 납부기간 함께 체크.
     * 4월미납
     * 항목1:100(미납)
     * 항목2:500(미납)
     * 5월청구(5.1 ~ 5.10)
     * 항목1:100(미납)
     * 항목2:500(미납)
     * 항목3:400(미납)
     * <p>
     * 현재 5월9일이라면
     * 금액체크/납부기간체크시 1000원 가능
     * 금액체크/납부기간OPEN 600원, 1000원 가능
     * <p>
     * 금액OPEN/납부기간체크 5월분 납부처리. 아무금액이나 가능
     * 금액OPEN/납부기간OPEN 4월분부터 납부처리. 아무금액이나 가능.
     *
     * @param transactionAmount 수납금액
     * @param msgSndDate        수납일자
     * @param notiMasList
     * @param chacdInfo
     */
    public static void divideAmount(String transactionAmount, String msgSndDate, List<NotiMas> notiMasList, ChacdInfo chacdInfo) {
        int size = notiMasList.size();
        if (size == 0) {
            return;
        }

        calculate(notiMasList);
        Long _amount = Damoas.toLong(transactionAmount);
        AtomicLong amount = new AtomicLong(_amount);
        boolean isDoDateCheck = chacdInfo.isDoDateCheck();
        boolean isDoAmountCheck = chacdInfo.isDoAmountCheck();

        LOG.info("금액 일자 체크 여부 "+chacdInfo);
        if (isDoDateCheck && isDoAmountCheck) {
            // 납부기간에 맞는 청구건에 대해서
            // 납부기간이 맞는 것중에 청구액이 맞는 걸 찾아 처리함.
            for (NotiMas notiMas : notiMasList) {
                if (notiMas.validateDateNAmount(_amount, msgSndDate)) {
                    notiMas.divideAmountExactly(amount);
                    break;
                } else {
                    LOG.debug("날짜,금액 모두 맞지 않음." + notiMas.getTotalAmount() + " " + _amount + " " + msgSndDate);
                }
            }

            // amount 는 0 이어야 함.
        } else if (isDoDateCheck == false && isDoAmountCheck) {
            for (NotiMas notiMas : notiMasList) {
                if (notiMas.validateAmount(_amount)) {
                    notiMas.divideAmountExactly(amount);
                    break;
                }
            }
        } else if (isDoDateCheck && isDoAmountCheck == false) {
            for (NotiMas notiMas : notiMasList) {
                if (notiMas.validateDate(msgSndDate)) {
                    notiMas.divideAmount(amount);
                }
            }

        } else {
            for (NotiMas notiMas : notiMasList) {
                notiMas.divideAmount(amount);
            }
        }

        handleFinally(amount, notiMasList);
    }

    public static boolean isRetryMsg(String userWorkArea2) {
        final String message = StringUtils.trim(userWorkArea2);
        if ("SAF".equalsIgnoreCase(message) || "VTM".equalsIgnoreCase(message)) {
            return true;
        }else {
            return false;
        }
    }
    /**
     * divideAmount와 동일하지만...
     * 금액체크여부는 Notimas의 금액체크여부를 참조한다.
     * 기간체크여부는 기관정보에서...
     *
     * @param transactionAmount
     * @param msgSndDate
     * @param notiMasList
     * @param chacdInfo
     */
    public static void divideAmount2(String transactionAmount, String msgSndDate, List<NotiMas> notiMasList, ChacdInfo chacdInfo) {
        int size = notiMasList.size();
        if (size == 0) {
            return;
        }

        calculate(notiMasList);
        Long _amount = Damoas.toLong(transactionAmount);
        AtomicLong amount = new AtomicLong(_amount);
        boolean isDoDateCheck = chacdInfo.isDoDateCheck();

        if (isDoDateCheck) {
            // 납부기간에 맞는 청구건에 대해서
            // 납부기간이 맞는 것중에 청구액이 맞는 걸 찾아 처리함.
            for (NotiMas notiMas : notiMasList) {
                if ("Y".equalsIgnoreCase(notiMas.getCheckAmount())) {
                    if (notiMas.validateDateNAmount(_amount, msgSndDate)) {
                        notiMas.divideAmountExactly(amount);
                        break;
                    } else {
                        LOG.debug("날짜,금액 모두 맞지 않음." + notiMas.getTotalAmount() + " " + _amount + " " + msgSndDate);
                    }
                } else {
                    if (notiMas.validateDate(msgSndDate)) {
                        notiMas.divideAmount(amount);
                    }
                }

            }

            // amount 는 0 이어야 함.
        } else {
            for (NotiMas notiMas : notiMasList) {
                if ("Y".equalsIgnoreCase(notiMas.getCheckAmount())) {
                    if (notiMas.validateAmount(_amount)) {
                        notiMas.divideAmountExactly(amount);
                        break;
                    }
                } else {
                    notiMas.divideAmount(amount);
                }
            }
        }

        handleFinally(amount, notiMasList);
    }

    /**
     * 마지막으로 과납인 경우에 마지막 청구건에 대해서 금액 계산함..
     * TODO 특별한 계좌를 찾아 셋팅할 수도 있음..
     * @param amount
     * @param notiMasList
     */
    private static void handleFinally(AtomicLong amount, List<NotiMas> notiMasList) {
        int size = notiMasList.size();
        if (amount.get() > 0) {
            NotiMas notiMas = notiMasList.get(size - 1);
            if (Constants.OK_PAY.equalsIgnoreCase(notiMas.getSt())) {
                notiMas.setSt(Constants.OVER_PAY);
                List<NotiDet> notiDets = notiMas.getNotiDets();
                NotiDet notiDet = notiDets.get(notiDets.size() - 1);
                if (notiDet != null) {
                    notiDet.setSt(Constants.OVER_PAY);
                    notiDet.setAmount(overAmount(amount.get(), notiDet.getAmount()));
                }
            }
        }
    }

    private static long overAmount(long amount, long remaining) {
        return amount + remaining;
    }

    public static void calculate(List<NotiMas> notiMasList) {
        for (NotiMas notiMas : notiMasList) {
            notiMas.calculate();
        }
    }

    public static boolean checkAmount(List<NotiMas> notiMasList, long transactionAmount) {
        for (NotiMas notiMas : notiMasList) {
            if (notiMas.getTotalAmount() == transactionAmount) {
                return true;
            }
        }

        return false;
    }

    public static TreeMap<String, Long> aggregateByAccountId(List<ReceiptMasterDO> receiptMasterList) {
        final TreeMap<String, Long> map = new TreeMap<>();

        for (ReceiptMasterDO eachMaster : receiptMasterList) {
            for (ReceiptDetailsDO eachDetails : eachMaster.getDetailsList()) {
                try {
                    final String key = eachDetails.getPayItem().getAccountId();
                    
                    if (!map.containsKey(key)) {
                        map.put(key, 0L);
                    }
                    
                    Long amount = map.get(key);
                    Long sum = amount + eachDetails.getAmount();
                    map.put(key, sum);
                } catch (Exception e) {
                    // skip..
                    if(LOG.isDebugEnabled()) {
                        LOG.error(e.getMessage(), e);
                    } else {
                        LOG.error(e.getMessage());
                    }
                    
                }

            }
        }

        return map;
    }


    public static AggregateBean agrregate(List<Map<String, Object>> mapList) {
        int totalCount = 0;
        long totalAmount = 0;
        int cancelCount = 0;
        long cancelAmount = 0;
        int feeCount = 0;
        long feeAmount = 0;
        for (Map<String, Object> map : mapList) {

            long amount = Maps.getLongValue(map, "rcpamt");
            String type = Maps.getValue(map, "rcpmasst");
            if ("CNCL".equalsIgnoreCase(type)) {
                //취소인경우..
                cancelCount++;
                cancelAmount += amount;
            } else {
                // 입금,입금이체
                totalCount++;
                totalAmount += amount;
            }

            long _fee = Maps.getLongValue(map, "feeamt");
            if (_fee > 0) {
                feeCount = 0;
                feeAmount = 0;
            }

        }

        return new AggregateBean(totalCount, totalAmount, cancelCount, cancelAmount, feeCount, feeAmount);
    }


    public static void touchFile(String type) {
        String date = DateUtils.toDateString(new Date());
        if ("FINISH".equalsIgnoreCase(type)) {
            String finishFilePaht = "./FINISH_"+date;
            touchFile(new File(finishFilePaht));
        } else if ("START".equalsIgnoreCase(type)) {
            String startFilePath = "./START_" + date;
            touchFile(new File(startFilePath));
        } else {

        }
    }

    private static void touchFile(File file) {
        try {
            FileUtils.touch(file);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
