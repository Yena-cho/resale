package kr.co.finger.shinhandamoa.sys.controller;

import com.finger.shinhandamoa.org.transformer.DownloadView;
import kr.co.finger.msgio.msg.AutoWithdrawalHelper;
import kr.co.finger.msgio.msg.EB13;
import kr.co.finger.msgio.msg.EB14;
import kr.co.finger.msgio.msg.EI13;
import kr.co.finger.shinhandamoa.sys.service.KftcPayerService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 금융결제원 납부자
 * 
 * @author wisehouse@finger.co.kr
 */
@Controller
public class KftcPayerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KftcPayerController.class);
    
    @Autowired
    private KftcPayerService kftcPayerService;

    /**
     * 금결원 납부자 등록파일 다운로드 (EB13)
     *
     */
    @RequestMapping(value="/sys/rest/kftc-payer/eb13", method= RequestMethod.GET)
    public DownloadView downloadEB13() throws Exception {
        final EB13 item = kftcPayerService.getEB13(Arrays.asList("K00001", "K00002"));
        final String file = AutoWithdrawalHelper.encodeEB13(item);

        final DownloadView downloadView = new DownloadView(item.getHeader().getFileName(), new ByteArrayInputStream(file.getBytes(Charset.forName("KSC5601"))));
        downloadView.setContentType(DownloadView.CONTENT_TYPE_OCTET_STREAM);
        return downloadView;
    }

    /**
     * 금융결제원 납부자 출금동의서 파일 다운로드 (EI13)
     */
    @RequestMapping(value="/sys/rest/kftc-payer/ei13", method= RequestMethod.GET)
    public DownloadView downloadEI13() throws Exception {
        final EI13 item = kftcPayerService.getEI13(Arrays.asList("K00001", "K00002"));
        final byte[] file = AutoWithdrawalHelper.encodeEI13(item);

        final DownloadView downloadView = new DownloadView("EI13" + item.getHeader().getReqData().substring(2), new ByteArrayInputStream(file));
        downloadView.setContentType(DownloadView.CONTENT_TYPE_OCTET_STREAM);
        return downloadView;
    }

    @RequestMapping(value="/sys/rest/kftc-payer/eb14", method= RequestMethod.POST)
    @ResponseBody
    public ModelMap uploadEB14(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        byte[] bytes = multipartFile.getBytes();
        final File file = File.createTempFile("EB14", ".tmp");
        FileUtils.writeByteArrayToFile(file, bytes);

        AutoWithdrawalHelper.setCharset(Charset.forName("KSC5601"));
        final EB14 eb14 = AutoWithdrawalHelper.decodeEB14(file.getAbsolutePath());
        kftcPayerService.writeEB14(eb14);

        final ModelMap modelMap = new ModelMap();
        modelMap.put("retCode", "0000");
        
        return modelMap;
    }
}
