package kr.co.finger.shinhandamoa.sys.controller;

import com.finger.shinhandamoa.org.transformer.DownloadView;
import kr.co.finger.shinhandamoa.sys.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.InputStream;

/**
 * 이용기관 컨트롤러
 * 
 * @author wisehouse@finger.co.kr
 */
@Controller
@RequestMapping(value="/sys/rest/client")
public class ClientController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientController.class);
    
    @Autowired
    private ClientService clientService;
    
    @RequestMapping(value="/download/invoice")
    public DownloadView downloadInInvoiceFormat() {
        final InputStream inputStream = clientService.getClientExcelInInvoiceFormat();
        return new DownloadView("세금계산서_이용기관등록.xlsx", inputStream);
    }
}
