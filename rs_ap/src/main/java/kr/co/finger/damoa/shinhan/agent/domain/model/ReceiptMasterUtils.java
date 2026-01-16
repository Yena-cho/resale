package kr.co.finger.damoa.shinhan.agent.domain.model;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 수납원장 유틸리티
 * 
 * @author wisehouse@finger.co.kr
 */
public class ReceiptMasterUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptMasterUtils.class);
    
    private ReceiptMasterUtils() {
        new RuntimeException("금지된 생성자");
    }

    /**
     * 청구원장의 수취인명을 취합한다. 수취인명이 1개일 경우 수취인명을, 그 외의 경우는 StringUtils.EMPTY로 응답한다.
     * 
     * @param receiptMasterList 청구원장 목록
     * @return
     */
    public static String aggregateNoticeCustomerName(List<ReceiptMasterDO> receiptMasterList) {
        String name = StringUtils.EMPTY;
        for (ReceiptMasterDO each : receiptMasterList) {
            if(name == null) {
                name =each.getCustomerName();
                continue;
            }

            if(StringUtils.equals(name, each.getCustomerName())) {
                continue;
            }

            return StringUtils.EMPTY;
        }

        return name;
    }


}
