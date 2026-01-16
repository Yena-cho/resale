package kr.co.finger.shinhandamoa.sys.controller;

import com.finger.shinhandamoa.org.transformer.DownloadView;
import kr.co.finger.msgio.msg.AutoWithdrawalHelper;
import kr.co.finger.msgio.msg.EB21;
import kr.co.finger.msgio.msg.EB22;
import kr.co.finger.shinhandamoa.sys.service.KftcWithdrawService;
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

/**
 * 금융결제원 출금이체
 *
 * @author wisehouse@finger.co.kr
 */
@Controller
@RequestMapping(value = "/sys/rest/kftc-withdraw")
public class KftcWithdrawController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KftcWithdrawController.class);

    @Autowired
    private KftcWithdrawService kftcWithdrawService;

    /**
     * 출금이체(3일) 다운로드
     */
    @RequestMapping(value = "/eb21", method = RequestMethod.GET)
    public DownloadView downloadEB21(@RequestParam(value = "payday") String payday) throws Exception {
        final EB21 item = kftcWithdrawService.getEB21(payday);
        final String file = AutoWithdrawalHelper.encodeEB21(item);

        final DownloadView downloadView = new DownloadView(item.getHeader().getFileName(), new ByteArrayInputStream(file.getBytes(Charset.forName("KSC5601"))));
        downloadView.setContentType(DownloadView.CONTENT_TYPE_OCTET_STREAM);
        return downloadView;
    }

    /**
     * 출금이체(3일) 결과 업로드
     *
     * @return
     */
    @RequestMapping(value = "/eb22", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap updateEB22(@RequestParam(value = "file") MultipartFile multipartFile) throws Exception {
        byte[] bytes = multipartFile.getBytes();
        final File file = File.createTempFile("EB22", ".tmp");
        FileUtils.writeByteArrayToFile(file, bytes);

        AutoWithdrawalHelper.setCharset(Charset.forName("KSC5601"));
        final EB22 eb22 = AutoWithdrawalHelper.decodeEB22(file.getAbsolutePath());
        kftcWithdrawService.writeEB22(eb22);

        final ModelMap modelMap = new ModelMap();
        modelMap.put("retCode", "0000");

        return modelMap;
    }
}
