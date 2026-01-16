package kr.co.finger.damoa.scheduler.service;

import kr.co.finger.damoa.scheduler.dao.BillsSendDao;
import kr.co.finger.damoa.scheduler.model.LayOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 고지서 출력 의뢰 서비스
 *
 * @author denny91@finger.co.kr
 * @author wisehouse@finger.co.kr
 */
@Service
public class BillsSendService {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private BillsSendDao billsSendDao;

    @Value("${damoa.ftpFilePath}")
    private String ftpPath;

    public static String nullToVoid(Object obj) {
        if (obj == null) {
            return "";
        }
        return (String) obj;
    }

    public void selFtpData() throws Exception {
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        String today = date.format(new Date());

        File dirPath = new File(ftpPath + today); // 디렉토리 존재 확인 후 생성
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        HashMap<String, Object> map = new HashMap<String, Object>();

        LOGGER.info("고지서 출력의뢰 조회");
        final LayOut com = billsSendDao.printMsg(map);
        final List<LayOut> list = billsSendDao.selChaCd(map);
        LOGGER.info("고지서 출력의뢰 {}건", list.size());
        

        StringBuilder sb = new StringBuilder();

        // 파일명
        String fileName = today + ".txt";
        BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ftpPath + today + File.separator + fileName), "EUC-KR"));

        try {
            int cnt = 0;
            // Header layout
            sb.append(com.getHeader());            // Header 1
            sb.append(com.getDummy());            // dummy  7
            sb.append(com.getReqDate());        // 의뢰날짜  8

            sb.append("\n");

                for (int j = 0; j < list.size(); j++) {
                    map.put("chaCd", list.get(j).getChaCd());
                    map.put("billGubn", list.get(j).getBillGubn());
                    map.put("masMonth", list.get(j).getMasMonth());
                    map.put("sort1Cd", list.get(j).getSort1Cd());
                    map.put("sort2Cd", list.get(j).getSort2Cd());
                    
                    LOGGER.info("고지서 조회 [CHACD={}]", list.get(j).getChaCd());

                    List<LayOut> bodyList = billsSendDao.printReqData(map);
                    List<LayOut> msgList = billsSendDao.printMsgData(map);

                    LOGGER.info("고지서 조회 [CHACD={}]: {}건", list.get(j).getChaCd(), bodyList.size());
                    
                    // Body layout
                    for (int y = 0; y < bodyList.size(); y++) {
                        LayOut dto = bodyList.get(y);

                        String msgTile = byteArrayPad(dto.getMsgTitle(), 20, " ");    // 메시지 타이틀
                        String recName = byteArrayPad(dto.getRecName(), 30, " ");    // 수신자명
                        sb.append(com.getbHeader());                                // Header 1
                        sb.append(String.format("%07d", (cnt + 1)));                    // seq 7
                        sb.append(dto.getChaCd());                                    // 코드 8
                        sb.append(dto.getNotiMascd());                                // dummy 20
                        sb.append(msgTile);                                        // 타이틀 20
                        sb.append(recName);                                        // 수신자명 30

                        // 메시지영역
                        for (int a = 0; a < 10; a++) {
                            sb.append(byteArrayPad(nullToVoid(msgList.get(a).getMsg()).replaceAll("\\r|\\n", ""), 160, " "));
                        }
                        String offName = byteArrayPad(dto.getOffName(), 30, " ");

                        sb.append(offName);                            // 학원명 30
                        sb.append(byteArrayPad(nullToVoid(dto.getOffTel()), 15, " "));    // 학원전화번호 15
                        sb.append(dto.getVano());                    // 계좌번호 20
                        sb.append(dto.getPayYear());                // 납기년 4
                        sb.append(dto.getPayMonth());                // 납기월 2
                        sb.append(dto.getPayDay());                // 납기일 2
                        sb.append(dto.getGrpName());                // 그룹명	80
                        // 항목영역
                        List<LayOut> itemList = billsSendDao.printItemData(map);
                        for (int i = 0; i < itemList.size(); i++) {
                            if (dto.getNotiMascd().equals(itemList.get(i).getNotiMascd())) {
                                // 항목명1~10, 항목금액1~10, 비고1~10
                                String item1 = byteArrayPad(itemList.get(i).getItem1(), 30, " ");
                                String item2 = byteArrayPad(itemList.get(i).getItem2(), 30, " ");
                                String item3 = byteArrayPad(itemList.get(i).getItem3(), 30, " ");
                                String item4 = byteArrayPad(itemList.get(i).getItem4(), 30, " ");
                                String item5 = byteArrayPad(itemList.get(i).getItem5(), 30, " ");
                                String item6 = byteArrayPad(itemList.get(i).getItem6(), 30, " ");
                                String item7 = byteArrayPad(itemList.get(i).getItem7(), 30, " ");
                                String item8 = byteArrayPad(itemList.get(i).getItem8(), 30, " ");
                                String item9 = byteArrayPad(itemList.get(i).getItem9(), 30, " ");
                                String etc1 = byteArrayPad(itemList.get(i).getEtc1(), 50, " ");
                                String etc2 = byteArrayPad(itemList.get(i).getEtc2(), 50, " ");
                                String etc3 = byteArrayPad(itemList.get(i).getEtc3(), 50, " ");
                                String etc4 = byteArrayPad(itemList.get(i).getEtc4(), 50, " ");
                                String etc5 = byteArrayPad(itemList.get(i).getEtc5(), 50, " ");
                                String etc6 = byteArrayPad(itemList.get(i).getEtc6(), 50, " ");
                                String etc7 = byteArrayPad(itemList.get(i).getEtc7(), 50, " ");
                                String etc8 = byteArrayPad(itemList.get(i).getEtc8(), 50, " ");
                                String etc9 = byteArrayPad(itemList.get(i).getEtc9(), 50, " ");
                                sb.append(item1);                        // 항목명 30
                                sb.append(itemList.get(i).getAmt1());    // 항목금액 12
                                sb.append(etc1);                        // 항목비고 50
                                sb.append(item2);
                                sb.append(itemList.get(i).getAmt2());
                                sb.append(etc2);
                                sb.append(item3);
                                sb.append(itemList.get(i).getAmt3());
                                sb.append(etc3);
                                sb.append(item4);
                                sb.append(itemList.get(i).getAmt4());
                                sb.append(etc4);
                                sb.append(item5);
                                sb.append(itemList.get(i).getAmt5());
                                sb.append(etc5);
                                sb.append(item6);
                                sb.append(itemList.get(i).getAmt6());
                                sb.append(etc6);
                                sb.append(item7);
                                sb.append(itemList.get(i).getAmt7());
                                sb.append(etc7);
                                sb.append(item8);
                                sb.append(itemList.get(i).getAmt8());
                                sb.append(etc8);
                                sb.append(item9);
                                sb.append(itemList.get(i).getAmt9());
                                sb.append(etc9);
                            }
                        }
                        sb.append(byteArrayPad(dto.getMasMonth(), 30, " ")); // 납부년월 30
                        sb.append(dto.getPayItemAmt());                     // 합계금액 12
                        sb.append(byteArrayPad("", 50, " "));                 // 비고 50
                        String zipCd = byteArrayPad(dto.getZipCd(), 6, " ");
                        String addr = byteArrayPad(dto.getAddr(), 150, " ");
                        String offName2 = byteArrayPad(dto.getOffName2(), 180, " ");
                        String addMsg = byteArrayPad(dto.getAddMsg(), 120, " ");
                        sb.append(zipCd);                                     // 우편번호 6
                        sb.append(addr);                                     // 주소 150
                        sb.append(offName2);                                 // 학원명 180
                        sb.append(dto.getHp());                             // 핸드폰번호 15
                        sb.append(addMsg);                                     // 추가메시지 120
                        // 비고영역
                        map.put("notiMasCd", dto.getNotiMascd());
                        List<LayOut> remarkList = billsSendDao.remarkMsgData(map);
                        for (int a = 0; a < 3; a++) {
                            sb.append(byteArrayPad(nullToVoid(remarkList.get(a).getMsg()).replaceAll("\\r|\\n", ""), 120, " "));
                        }

                        sb.append(byteArrayPad(dto.getOwnerTel(), 15, " "));    // 대표번호 15
                        sb.append(byteArrayPad(dto.getAddr2(), 150, " "));        // 기관주소 150
                        sb.append("\n");
                        cnt++;
                    }
                }
            String suffix = String.format("%07d", cnt);

            // Tailer layout
            sb.append(com.gettHeader());        // Header
            sb.append(suffix);                    // 의뢰건수

            fw.write(sb.toString());
            
            LOGGER.info("파일 생성 완료: {}", fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fw.close();
        }
    }

    private String byteArrayPad(String string, int minLength, String padString) { //, Charset charset
        Charset charset = Charset.forName("EUC-KR");

        byte[] bytes = string.getBytes(charset);
        int size = minLength - bytes.length;
        if (size <= 0) {
            return string;
        } else {
            byte[] _bytes = padString.getBytes(charset);
            byte[] results = bytes;
            for (int i = 0; i < size; i++) {
                results = concat(results, _bytes);
            }

            return new String(results, charset);
        }
    }

    private byte[] concat(byte[]... arrays) {
        int length = 0;
        byte[][] var2 = arrays;
        int pos = arrays.length;

        for (int var4 = 0; var4 < pos; ++var4) {
            byte[] array = var2[var4];
            length += array.length;
        }

        byte[] result = new byte[length];
        pos = 0;
        byte[][] var9 = arrays;
        int var10 = arrays.length;

        for (int var6 = 0; var6 < var10; ++var6) {
            byte[] array = var9[var6];
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }

        return result;
    }

    public void updateReqSt() throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();

        List<LayOut> list = billsSendDao.selChaCd(map);
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                map.put("chaCd", list.get(i).getChaCd());
                map.put("masMonth", list.get(i).getMasMonth());

                billsSendDao.updateReqSt(map);
            }
        }
    }


}
