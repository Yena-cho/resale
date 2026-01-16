package kr.co.finger.damoa.shinhan.agent.util;

import kr.co.finger.damoa.commons.Constants;
import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.model.msg.Code;
import kr.co.finger.damoa.model.rcp.ChacdInfo;
import kr.co.finger.damoa.model.rcp.NotiDet;
import kr.co.finger.damoa.model.rcp.NotiMas;
import kr.co.finger.damoa.model.rcp.PayStateCode;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * 돈 분배하기..
 * PA02 미납
 * PA03 수납
 * PA04 일부납
 * PA05 초과납
 */
public class DamoaBizUtilTest {

    private ChacdInfo falsefalse = new ChacdInfo(false,false);
    private String now = DateUtils.to14NowString();
    private NotiMas createMas(String day, String time) {
        return new NotiMas(day,time);
    }
    private NotiMas createMas(String day, String time,String startdate,String enddate,boolean doAmountCheck) {
        NotiMas notiMas = new NotiMas(day,time,startdate,enddate);
        if (doAmountCheck) {
            notiMas.setCheckAmount("Y");
        } else {
            notiMas.setCheckAmount("N");

        }
        return notiMas;
    }
    private NotiDet createDet(String key, String amount) {
        return new NotiDet(key,amount);
    }
    private NotiDet createDet(String key, String amount,String mas,String det) {
        return new NotiDet(key,amount,mas,det);
    }
    /**
     * 청구1
     * 0원 수납
     * 미납
     */
    @Test
    public void test청구1_0원수납_미납() {
        List<NotiMas> notiMasList = notiOne();

        DamoaBizUtil.divideAmount("0", now,notiMasList, falsefalse);
        assertEquals(notiMasList.get(0).getSt(), "PA02");
    }

    @Test
    public void test청구1_미납() {
        List<NotiMas> notiMasList = notiOne();
        DamoaBizUtil.divideAmount("10", now,notiMasList, falsefalse);
        assertEquals(notiMasList.get(0).getSt(), "PA04");
    }

    @Test
    public void test청구1_부분납() {
        List<NotiMas> notiMasList = notiOne();
        DamoaBizUtil.divideAmount("100", now,notiMasList, falsefalse);
        assertEquals(notiMasList.get(0).getSt(), "PA04");
    }
    @Test
    public void test청구1_부분납2() {
        List<NotiMas> notiMasList = notiOne();
        DamoaBizUtil.divideAmount("200", now,notiMasList, falsefalse);
        assertEquals(notiMasList.get(0).getSt(), "PA04");
    }
    @Test
    public void test청구1_부분납3() {
        List<NotiMas> notiMasList = notiOne();
        DamoaBizUtil.divideAmount("400", now,notiMasList, falsefalse);
        assertEquals(notiMasList.get(0).getSt(), "PA04");
    }

    @Test
    public void test청구1_부분납4() {
        List<NotiMas> notiMasList = notiOne();
        DamoaBizUtil.divideAmount("700", now,notiMasList, falsefalse);
        assertEquals(notiMasList.get(0).getSt(), "PA04");
    }
    @Test
    public void test청구1_수납() {
        List<NotiMas> notiMasList = notiOne();
        DamoaBizUtil.divideAmount("800", now,notiMasList, falsefalse);
        assertEquals(notiMasList.get(0).getSt(), "PA03");
    }
    @Test
    public void test청구1_초과납() {
        List<NotiMas> notiMasList = notiOne();
        DamoaBizUtil.divideAmount("900", now,notiMasList, falsefalse);
        assertEquals(notiMasList.get(0).getSt(), Constants.OVER_PAY);
    }

    private List<NotiMas> notiOne() {
        List<NotiMas> notiMasList = new ArrayList<>();
        NotiMas _notiMas =  createMas("20180321","202141");
        _notiMas.add(createDet("001","100"));
        _notiMas.add(createDet("002","300"));
        _notiMas.add(createDet("003","400"));
        notiMasList.add(_notiMas);
        return notiMasList;
    }

    @Test
    public void test청구_미납() {
        // 800, 6000, 1000
        List<NotiMas> notiMasList = notiMany();
        DamoaBizUtil.divideAmount("10", now,notiMasList, falsefalse);
        assertEquals(notiMasList.get(0).getSt(), Constants.PART_PAY);
        assertEquals(notiMasList.get(1).getSt(), Constants.NO_PAY);
        assertEquals(notiMasList.get(2).getSt(), Constants.NO_PAY);
    }
    @Test
    public void test청구_부분납() {
        // 800, 6000, 1000
        List<NotiMas> notiMasList = notiMany();
        DamoaBizUtil.divideAmount("100", now,notiMasList, falsefalse);
        assertEquals(notiMasList.get(0).getSt(), Constants.PART_PAY);
        assertEquals(notiMasList.get(1).getSt(), Constants.NO_PAY);
        assertEquals(notiMasList.get(2).getSt(), Constants.NO_PAY);
    }

    @Test
    public void test청구_부분납2() {
        // 800, 6000, 1000
        List<NotiMas> notiMasList = notiMany();
        DamoaBizUtil.divideAmount("800", now,notiMasList, falsefalse);
        assertEquals(notiMasList.get(0).getSt(), Constants.OK_PAY);
        assertEquals(notiMasList.get(1).getSt(), Constants.NO_PAY);
        assertEquals(notiMasList.get(2).getSt(), Constants.NO_PAY);
    }
    @Test
    public void test청구_부분납3() {
        // 800, 6000, 1000
        List<NotiMas> notiMasList = notiMany();
        DamoaBizUtil.divideAmount("1000", now,notiMasList, falsefalse);
        assertEquals(notiMasList.get(0).getSt(), Constants.OK_PAY);
        assertEquals(notiMasList.get(1).getSt(), Constants.PART_PAY);
        assertEquals(notiMasList.get(2).getSt(), Constants.NO_PAY);
    }
    @Test
    public void test청구_부분납4() {
        List<NotiMas> notiMasList = notiMany();
        DamoaBizUtil.divideAmount("1800", now,notiMasList, falsefalse);
        view(notiMasList);
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(0).getSt());
        assertEquals(PayStateCode.PART_PAY.getId(),notiMasList.get(1).getSt());
        assertEquals(PayStateCode.NO_PAY.getId(),notiMasList.get(2).getSt());
    }
    @Test
    public void test청구_부분납5() {
        // 800, 6000, 1000
        List<NotiMas> notiMasList = notiMany();
        DamoaBizUtil.divideAmount("7000", now,notiMasList, falsefalse);
        view(notiMasList);
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(0).getSt());
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(1).getSt());
        assertEquals(PayStateCode.PART_PAY.getId(),notiMasList.get(2).getSt());
    }
    @Test
    public void test청구_완납() {
        // 800, 6000, 1000
        List<NotiMas> notiMasList = notiMany();
        DamoaBizUtil.divideAmount("7800", now,notiMasList, falsefalse);
        view(notiMasList);
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(0).getSt());
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(1).getSt());
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(2).getSt());
    }
    @Test
    public void test청구_초과납() {
        List<NotiMas> notiMasList = notiMany();
        DamoaBizUtil.divideAmount("7900", now,notiMasList, falsefalse);
        view(notiMasList);
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(0).getSt());
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(1).getSt());
        assertEquals(PayStateCode.OVER_PAY.getId(),notiMasList.get(2).getSt());
    }
    @Test
    public void testDateTrueAmountTrue() {
        List<NotiMas> notiMasList = newNotiMany(true);
        DamoaBizUtil.divideAmount("2000","20180429",notiMasList, new ChacdInfo(true,true));
        view(notiMasList);
        assertEquals(PayStateCode.NO_PAY.getId(),notiMasList.get(0).getSt());
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(1).getSt());
        assertEquals(PayStateCode.NO_PAY.getId(),notiMasList.get(2).getSt());
    }
    @Test
    public void testDateFalseAmountTrue() {
        List<NotiMas> notiMasList = newNotiMany(true);
        DamoaBizUtil.divideAmount("1000","20180429",notiMasList, new ChacdInfo(false,true));
        view(notiMasList);
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(0).getSt());
        assertEquals(PayStateCode.NO_PAY.getId(),notiMasList.get(1).getSt());
        assertEquals(PayStateCode.NO_PAY.getId(),notiMasList.get(2).getSt());
    }
    @Test
    public void testDateTrueAmountFalse() {
        List<NotiMas> notiMasList = newNotiMany(false);
        DamoaBizUtil.divideAmount("4100","20180429",notiMasList, new ChacdInfo(true,false));
        view(notiMasList);
        assertEquals(PayStateCode.NO_PAY.getId(),notiMasList.get(0).getSt());
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(1).getSt());
        assertEquals(PayStateCode.OVER_PAY.getId(),notiMasList.get(2).getSt());
    }

    @Test
    public void testDateFalseAmountFalse() {
        List<NotiMas> notiMasList = newNotiMany(false);
        DamoaBizUtil.divideAmount("5100","20180429",notiMasList, new ChacdInfo(false,false));
        view(notiMasList);
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(0).getSt());
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(1).getSt());
        assertEquals(PayStateCode.OVER_PAY.getId(),notiMasList.get(2).getSt());
    }

    @Test
    public void test부분납DateFalseAmountFalse() {
        List<NotiMas> notiMasList = newNotiMany(false);
        Map<String,String> rcp = rcp();
        DamoaBizUtil.aggregate(notiMasList, rcp);
        DamoaBizUtil.divideAmount("4500","20180429",notiMasList, new ChacdInfo(false,false));
        view(notiMasList);
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(0).getSt());
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(1).getSt());
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(2).getSt());
    }

    @Test
    public void test부분납DateFalseAmountTrue1() {
        List<NotiMas> notiMasList = newNotiMany(true);
        Map<String,String> rcp = rcp();
        DamoaBizUtil.aggregate(notiMasList, rcp);
        DamoaBizUtil.divideAmount("2000","20180429",notiMasList, new ChacdInfo(false,true));
        view(notiMasList);
        assertEquals(PayStateCode.NO_PAY.getId(),notiMasList.get(0).getSt());
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(1).getSt());
        assertEquals(PayStateCode.NO_PAY.getId(),notiMasList.get(2).getSt());
    }
    @Test
    public void test부분납DateFalseAmountTrue2() {
        List<NotiMas> notiMasList = newNotiMany(true);
        Map<String,String> rcp = rcp();
        DamoaBizUtil.aggregate(notiMasList, rcp);
        DamoaBizUtil.divideAmount("1000","20180429",notiMasList, new ChacdInfo(false,true));
        view(notiMasList);
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(0).getSt());
        assertEquals(PayStateCode.NO_PAY.getId(),notiMasList.get(1).getSt());
        assertEquals(PayStateCode.NO_PAY.getId(),notiMasList.get(2).getSt());
    }
    @Test
    public void test부분납DateTrueAmountFalse1() {
        List<NotiMas> notiMasList = newNotiMany(false);
        Map<String,String> rcp = rcp();
        DamoaBizUtil.aggregate(notiMasList, rcp);
        DamoaBizUtil.divideAmount("3700","20180429",notiMasList, new ChacdInfo(true,false));
        view(notiMasList);
        assertEquals(PayStateCode.NO_PAY.getId(),notiMasList.get(0).getSt());
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(1).getSt());
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(2).getSt());
    }
    @Test
    public void test부분납DateTrueAmountFalse2() {
        List<NotiMas> notiMasList = newNotiMany(false);
        Map<String,String> rcp = rcp();
        DamoaBizUtil.aggregate(notiMasList, rcp);
        DamoaBizUtil.divideAmount("3600","20180429",notiMasList, new ChacdInfo(true,false));
        view(notiMasList);
        assertEquals(PayStateCode.NO_PAY.getId(),notiMasList.get(0).getSt());
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(1).getSt());
        assertEquals(PayStateCode.PART_PAY.getId(),notiMasList.get(2).getSt());
    }

    @Test
    public void test부분납DateTrueAmountTrue1() {
        // 1000,2000,2000
        List<NotiMas> notiMasList = newNotiMany(true);
        Map<String,String> rcp = rcp();
        DamoaBizUtil.aggregate(notiMasList, rcp);
        DamoaBizUtil.divideAmount("2000","20180429",notiMasList, new ChacdInfo(true,true));
        view(notiMasList);
        assertEquals(PayStateCode.NO_PAY.getId(),notiMasList.get(0).getSt());
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(1).getSt());
        assertEquals(PayStateCode.NO_PAY.getId(),notiMasList.get(2).getSt());
    }
    @Test
    public void test부분납DateTrueAmountTrue2() {
        // 1000,2000,2000
        List<NotiMas> notiMasList = newNotiMany(true);
        Map<String,String> rcp = rcp();
        DamoaBizUtil.aggregate(notiMasList, rcp);
        DamoaBizUtil.divideAmount("1000","20180429",notiMasList, new ChacdInfo(true,true));
        view(notiMasList);
        assertEquals(PayStateCode.NO_PAY.getId(),notiMasList.get(0).getSt());
        assertEquals(PayStateCode.NO_PAY.getId(),notiMasList.get(1).getSt());
        assertEquals(PayStateCode.NO_PAY.getId(),notiMasList.get(2).getSt());
    }

    @Test
    public void test부분납DatefalseAmountfalse() {
        // 1000,2000,2000
        // 200, 0, 0
        List<NotiMas> notiMasList = newNotiMany(false);
        Map<String,String> rcp = rcp2();
        DamoaBizUtil.aggregate(notiMasList, rcp);
        DamoaBizUtil.divideAmount("2000","20180429",notiMasList, new ChacdInfo(false,false));
        view(notiMasList);
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(0).getSt());
        assertEquals("800",notiMasList.get(0).calculateAmount());
        assertEquals(PayStateCode.PART_PAY.getId(),notiMasList.get(1).getSt());
        assertEquals("1200",notiMasList.get(1).calculateAmount());
        assertEquals("0",notiMasList.get(2).calculateAmount());
    }

    @Test
    public void test초과납DatefalseAmountfalse() {
        // 1000,2000,2000
        // 200, 0, 0
        List<NotiMas> notiMasList = newNotiMany(false);
        Map<String,String> rcp = rcp2();
        DamoaBizUtil.aggregate(notiMasList, rcp);
        DamoaBizUtil.divideAmount("5000","20180429",notiMasList, new ChacdInfo(false,false));
        view(notiMasList);
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(0).getSt());
        assertEquals("800",notiMasList.get(0).calculateAmount());
        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(1).getSt());
        assertEquals("2000",notiMasList.get(1).calculateAmount());
        assertEquals(PayStateCode.OVER_PAY.getId(),notiMasList.get(2).getSt());
        assertEquals("2200",notiMasList.get(2).calculateAmount());
        NotiMas notiMas = notiMasList.get(2);
        List<NotiDet> notiDetList = notiMas.getNotiDets();
        for (NotiDet notiDet : notiDetList) {
            System.out.println(notiDet.getAmount());
        }
    }

    private List<NotiMas> aggreate(List<NotiMas> notiMasList) {
        List<NotiMas> notiMasList1 = new ArrayList<>();
        for (NotiMas notiMas : notiMasList) {
            if ("PA03".equalsIgnoreCase(notiMas.getSt())) {
                continue;
            } else {
                notiMasList1.add(notiMas);
            }
        }

        return notiMasList1;
    }


    private void handle(List<NotiMas> notiMasList, Map<String, String> rcp) {
        //map.put("1111_1111002", "200");
        for (NotiMas notiMas : notiMasList) {
            String mascd = notiMas.getNotimascd();
            List<NotiDet> notiDetList = notiMas.getNotiDets();
            for (NotiDet notiDet : notiDetList) {
                String detcd = notiDet.getNotidetcd();
                String key = mascd+"_"+detcd;
                if (rcp.containsKey(key)) {
                    String value = rcp.get(key);
                    value = (Long.valueOf(value)+notiDet.getAmount())+"";
                    rcp.put(key, value);
                } else {
                    rcp.put(key, notiDet.getAmount()+"");
                }
            }
        }
    }

    private void viewDivided(List<NotiDet> _list) {
        Map<String, Long> longMap = new TreeMap<>();
        Map<String, String> group = group();
        for (NotiDet notiDet : _list) {
            String value = group.get(notiDet.getAdjfiregkey());
            if (longMap.containsKey(value)) {
                Long _long = longMap.get(value);
                longMap.put(value, (_long + notiDet.getAmount()));
            } else {
                longMap.put(value, (notiDet.getAmount()));
            }
        }


        for (String value : longMap.keySet()) {
            System.out.println(value + " : " + longMap.get(value));
        }
    }

    private Map<String, String> group() {
        Map<String, String> map = new HashMap<>();
        map.put("2000015880", "0000");
        map.put("2000015882", "0000");
        map.put("2000015884", "0002");
        map.put("2000015885", "0000");
        map.put("2000016542", "0001");

        return map;
    }

    private Map<String,String> empty() {
        Map<String, String> map = new HashMap<>();

        return map;
    }

//    @Test
//    public void testErrorDatefalseAmountfalse() {
//        // 100,400,500
//        // 300, 0, 0
//        List<NotiMas> notiMasList = newErrorNotiMany(false);
//        Map<String,String> rcp = rcpError();
//        DamoaBizUtil.aggregate(notiMasList, rcp);
//        DamoaBizUtil.divideAmount("700","20180429",notiMasList, new ChacdInfo(false,false));
//        view(notiMasList);
//        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(0).getSt());
//        assertEquals("800",notiMasList.get(0).calculateAmount());
//        assertEquals(PayStateCode.OK_PAY.getId(),notiMasList.get(1).getSt());
//        assertEquals("2000",notiMasList.get(1).calculateAmount());
//        assertEquals(PayStateCode.OVER_PAY.getId(),notiMasList.get(2).getSt());
//        assertEquals("2200",notiMasList.get(2).calculateAmount());
//        NotiMas notiMas = notiMasList.get(2);
//        List<NotiDet> notiDetList = notiMas.getNotiDets();
//        for (NotiDet notiDet : notiDetList) {
//            System.out.println(notiDet.getAmount());
//        }
//    }
    private void view(List<NotiMas> notiMasList) {
        int size = notiMasList.size();

        for (int i = 0; i < size; i++) {
            NotiMas notiMas = notiMasList.get(i);
            List<NotiDet> notiDets = notiMas.getNotiDets();
            for (NotiDet notiDet : notiDets) {
                System.out.println(i+"\t"+findCode(notiMas.getSt())+"\t"+findCode(notiDet.getSt())+"\t"+notiDet.getAmount()+"\t"+notiDet.getAdjfiregkey());
            }
        }
    }

    private Code findCode(String id) {
        if (PayStateCode.NO_PAY.getId().equalsIgnoreCase(id)) {
            return PayStateCode.NO_PAY;
        } else if (PayStateCode.OK_PAY.getId().equalsIgnoreCase(id)) {
            return PayStateCode.OK_PAY;
        }else if (PayStateCode.PART_PAY.getId().equalsIgnoreCase(id)) {
            return PayStateCode.PART_PAY;
        } else if (PayStateCode.OVER_PAY.getId().equalsIgnoreCase(id)) {
            return PayStateCode.OVER_PAY;
        } else {
            return null;
        }
    }

    private List<NotiMas> notiMany() {
        List<NotiMas> notiMasList = new ArrayList<>();

        NotiMas notiMas =  createMas("20180321","202141");
        notiMas.add(createDet("001","100"));
        notiMas.add(createDet("002","300"));
        notiMas.add(createDet("003","400"));
        notiMasList.add(notiMas);

        notiMas = createMas("20180421","202141");
        notiMas.add(createDet("001","1000"));
        notiMas.add(createDet("002","2000"));
        notiMas.add(createDet("003","3000"));
        notiMasList.add(notiMas);

        notiMas = createMas("20180422","202141");
        notiMas.add(createDet("004","1000"));
        notiMasList.add(notiMas);

        return notiMasList;
    }

    private List<NotiMas> newNotiMany(boolean doAmountCheck) {
        List<NotiMas> notiMasList = new ArrayList<>();
        NotiMas notiMas =  createMas("20180321","202141","20180320","20180330",doAmountCheck);
        notiMas.setNotimascd("1111");
        notiMas.add(createDet("001","100","1111","1111001"));
        notiMas.add(createDet("002","400","1111","1111002"));
        notiMas.add(createDet("003","500","1111","1111003"));
        notiMasList.add(notiMas);
        notiMas = createMas("20180421","202141","20180420","20180430",doAmountCheck);
        notiMas.setNotimascd("2222");
        notiMas.add(createDet("001","100","2222","2222001"));
        notiMas.add(createDet("002","700","2222","2222002"));
        notiMas.add(createDet("003","1200","2222","2222003"));
        notiMasList.add(notiMas);
        notiMas = createMas("20180422","202141","20180420","20180430",doAmountCheck);
        notiMas.setNotimascd("3333");
        notiMas.add(createDet("004","2000","3333","3333004"));
        notiMasList.add(notiMas);
        return notiMasList;
    }

    private List<NotiMas> newNotiMany_2(boolean doAmountCheck) {
        List<NotiMas> notiMasList = new ArrayList<>();
        NotiMas notiMas =  createMas("20180501","202141","20180502","20180530",doAmountCheck);
        notiMas.setNotimascd("1111");
        notiMas.add(createDet("2000015880","180400","1111","1111001"));
        notiMas.add(createDet("2000015882","50000","1111","1111002"));
        notiMas.add(createDet("2000015884","11000","1111","1111003"));
        notiMas.add(createDet("2000015885","22000","1111","1111004"));
        notiMas.add(createDet("2000016542","20000","1111","1111005"));
        notiMasList.add(notiMas);
        notiMas = createMas("20180601","202141","20180620","20180730",doAmountCheck);
        notiMas.setNotimascd("2222");
        notiMas.add(createDet("2000015880","180400","2222","2222001"));
        notiMas.add(createDet("2000015882","50000","2222","2222002"));
        notiMas.add(createDet("2000015884","11000","2222","2222003"));
        notiMas.add(createDet("2000015885","22000","2222","2222004"));
        notiMas.add(createDet("2000016542","20000","2222","2222005"));
        notiMasList.add(notiMas);
        return notiMasList;
    }
    private List<NotiMas> newNotiMany_3(boolean doAmountCheck) {
        List<NotiMas> notiMasList = new ArrayList<>();
        NotiMas notiMas =  createMas("20180501","202141","20180502","20180530",doAmountCheck);
        notiMas.setNotimascd("1111");
        notiMas.add(createDet("2000015880","180400","1111","1111001"));
        notiMas.add(createDet("2000015882","50000","1111","1111002"));
        notiMas.add(createDet("2000015884","11000","1111","1111003"));
        notiMas.add(createDet("2000015885","22000","1111","1111004"));
        notiMas.add(createDet("2000016542","20000","1111","1111005"));
        notiMasList.add(notiMas);
        notiMas = createMas("20180601","202141","20180620","20180730",doAmountCheck);
        notiMas.setNotimascd("2222");
        notiMas.add(createDet("2000015880","180400","2222","2222001"));
        notiMas.add(createDet("2000015882","60000","2222","2222002"));
        notiMas.add(createDet("2000015884","11000","2222","2222003"));
        notiMas.add(createDet("2000015885","22000","2222","2222004"));
        notiMas.add(createDet("2000016542","20000","2222","2222005"));
        notiMasList.add(notiMas);
        return notiMasList;
    }
    private List<NotiMas> newNotiMany_1(boolean doAmountCheck) {
        List<NotiMas> notiMasList = new ArrayList<>();
        NotiMas notiMas =  createMas("20180501","202141","20180502","20180530",doAmountCheck);
        notiMas.setNotimascd("1111");
        notiMas.add(createDet("2000015880","180400","1111","1111001"));
        notiMas.add(createDet("2000015882","50000","1111","1111002"));
        notiMas.add(createDet("2000015884","11000","1111","1111003"));
        notiMas.add(createDet("2000015885","22000","1111","1111004"));
        notiMas.add(createDet("2000016542","20000","1111","1111005"));
        notiMasList.add(notiMas);
        return notiMasList;
    }

    private List<NotiMas> newErrorNotiMany(boolean doAmountCheck) {
        List<NotiMas> notiMasList = new ArrayList<>();
        NotiMas notiMas =  createMas("20180321","202141","20180320","20180330",doAmountCheck);
        notiMas.setNotimascd("1111");
        notiMas.add(createDet("001","100","1111","1111001"));
        notiMas.add(createDet("002","400","1111","1111002"));
        notiMas.add(createDet("003","500","1111","1111003"));
        notiMasList.add(notiMas);
        return notiMasList;
    }
    /**
     * 기존에 수납된 데이터 조회..
     * @return
     */
    private Map<String,String> rcp() {
        Map<String, String> map = new HashMap<>();
        map.put("1111_1111002", "200");
        map.put("2222_2222002", "300");

        return map;
    }

    /**
     * 기존에 수납된 데이터 조회..
     * @return
     */
    private Map<String,String> rcp2() {
        Map<String, String> map = new HashMap<>();
        map.put("1111_1111002", "200");

        return map;
    }


    /**
     * 기존에 수납된 데이터 조회..
     * @return
     */
    private Map<String,String> rcpError() {
        Map<String, String> map = new HashMap<>();
        map.put("1111_1111001", "300");

        return map;
    }
}
