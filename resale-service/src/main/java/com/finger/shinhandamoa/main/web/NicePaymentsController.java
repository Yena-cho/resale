package com.finger.shinhandamoa.main.web;

import com.finger.shinhandamoa.org.receiptmgmt.dto.ReceiptMgmtDTO;
import com.finger.shinhandamoa.org.receiptmgmt.service.ReceiptMgmtService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * 나이스페이먼츠 결제 통보 API
 * 
 * @author wisehouse@finger.co.kr
 */
@RestController
@RequestMapping(value="/resource/nice-payments")
public class NicePaymentsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(NicePaymentsController.class);
    
    @Autowired
    private ReceiptMgmtService receiptMgmtService;

    @RequestMapping(value="/payment", method= RequestMethod.POST)
    public String index(HttpServletRequest request) throws Exception {
        final Enumeration parameterNames = request.getParameterNames();
        while(parameterNames.hasMoreElements()) {
            String parameterName = (String) parameterNames.nextElement();
            LOGGER.info("Parameter {}: {}", parameterName, request.getParameter(parameterName));
        }
        
        final String cancelDate = request.getParameter("CancelDate");
        if(StringUtils.isBlank(cancelDate)) {
            return "OK";
        }

        final String mid = request.getParameter("MID");
        final String payday = "20" + StringUtils.substring(request.getParameter("AuthDate"),0, 6);
        final String bnkmsgno = request.getParameter("AuthCode");
        cancelPayment(mid, payday, bnkmsgno);
        
        return "OK";
    }
    
    private void cancelPayment(String mid, String payday, String bnkmsgno) throws Exception {
        // FIXME 로그는 나중에 쓰지
        final HashMap<String, Object> map = new HashMap<>();
        map.put("pgServiceId", mid);
        map.put("payDay", payday);
        map.put("bnkMsgNo", bnkmsgno);
        
        final ReceiptMgmtDTO data = receiptMgmtService.getCancelRcp(map);
        map.put("rcpMasCd", data.getRcpMasCd());
        map.put("notiMasCd", data.getNotiMasCd());
        map.put("chaCd", data.getChaCd());

        final int upRcpMas = receiptMgmtService.updateRcpMas(map);
        if (upRcpMas > 0) {
            receiptMgmtService.updateRcpDet(map);
            receiptMgmtService.updateNotiBill(map);
        }
    }
}
