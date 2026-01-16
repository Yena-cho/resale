package kr.co.finger.msgio.msg;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.msgio.layout.LayoutFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 자동이체 전문처리와 파일 관련 Helper..
 * 먼저 init 메소드를 호출하여 초기화를 한번 수행한다.
 */
public class AutoWithdrawalHelper {

    private static LayoutFactory layoutFactory;
    private static Charset charset;

    /**
     * 먼저 초기화 메소드를 한번 호출하게 한 후 사용하면 된다.
     * @param withdrawalMessageFilePath
     * @param _charset
     * @throws Exception
     */
    public static void init(String withdrawalMessageFilePath,Charset _charset) throws Exception {
        layoutFactory = new LayoutFactory();
        layoutFactory.initFactory(withdrawalMessageFilePath, _charset);
        charset = _charset;
    }
    private AutoWithdrawalHelper(){}

    public static void setLayoutFactory(LayoutFactory layoutFactory) {
        AutoWithdrawalHelper.layoutFactory = layoutFactory;
    }

    public static void setCharset(Charset charset) {
        AutoWithdrawalHelper.charset = charset;
    }

    public static EB21 convertEB21(List<Map<String, Object>> mapList, String fileName, String corpCode, String bankCode, String accountNo, String shinhanCode,String now) throws Exception {
        EB21 eb21 = new EB21();
        eb21.from(mapList, corpCode, bankCode, accountNo, fileName, now,shinhanCode);
        return eb21;
    }
    public static String encodeEB21(EB21 eb21) throws Exception {
        return layoutFactory.encode(eb21,"EB21");
    }


    public static EB22 decodeEB22(String filePath) throws IOException {
        String msg = read(filePath);
        return  (EB22)layoutFactory.decode(msg,"EB22");
    }

    public static EB11 decodeEB11(String filePath) throws IOException {
        String msg = read(filePath);
        return  (EB11)layoutFactory.decode(msg,"EB11");
    }
    public static EB12 decodeEB12(String filePath) throws IOException {
        String msg = read(filePath);
        return  (EB12)layoutFactory.decode(msg,"EB12");
    }

    public static  byte[] encodeEI13(List<Map<String,Object>> maps,String corpCode,Date now,String shinhanCode) throws Exception {
        EI13 ei13 = new EI13();
        ei13.from(maps, now, corpCode,shinhanCode);
        byte[] bytes = layoutFactory.encodeToByteArray(ei13,"EI13");
        return bytes;
    }

    public static  byte[] encodeEI13(EI13 ei13) throws Exception {
        byte[] bytes = layoutFactory.encodeToByteArray(ei13,"EI13");
        return bytes;
    }

    public static EB12 convertEB12(List<Map<String,Object>> maps,String corpCode,String bankCode,String fileName,Date _now) throws Exception {
        String now = DateUtils.toString(_now, "yyMMdd");
        EB12 eB12 = new EB12();
        eB12.from(maps,corpCode,bankCode,fileName,now);
        return eB12;
    }

    public static String encodeEB12(EB12 eB12) throws Exception {
        return layoutFactory.encode(eB12,"EB12");
    }
    public static EB13 convertEB13(List<Map<String,Object>> maps,String corpCode,String bankCode,String fileName,Date _now) throws Exception {
        String now = DateUtils.toString(_now, "yyMMdd");
        EB13 eB13 = new EB13();
        eB13.from(maps,corpCode,bankCode,fileName,now);
        return eB13;
    }

    public static String encodeEB13(EB13 eB13) throws Exception {
        return layoutFactory.encode(eB13,"EB13");
    }

    public static EB14 decodeEB14(String filePath) throws IOException {
        String msg = read(filePath);
        return  (EB14)layoutFactory.decode(msg,"EB14");
    }

    public static void write(String fuleFilePath,String content) throws IOException {
        FileUtils.write(new File(fuleFilePath), content, charset, false);
    }
    public static void write(byte[] msg, String fuleFilePath) throws IOException {
        FileUtils.writeByteArrayToFile(new File(fuleFilePath), msg);
    }

    public static EI13 decodeEI13(byte[] bytes) {
        return layoutFactory.decodeToEI13(bytes, "EI13");
    }

    private static String read(String filePath) throws IOException {
        return FileUtils.readFileToString(new File(filePath),charset);
    }
    private static String getDateFrom(String fileName) {
        return fileName.substring(4);
    }
}
