package kr.co.finger.damoa.model.corp;

import kr.co.finger.damoa.commons.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.text.ParseException;
import java.util.*;
@Deprecated
public class Data  {
    private String kubun = "D";
    private String corpCode= "";	//기관코드
    private String corpName= "";	//기관(업체)명
    private String corpNewDate= "";	//기관 신규일자
    private String bizNo= "";	//사업자번호
    private String cusName= "";	//고객(대표자)명
    private String cusNo= "";	//고객(대표전화)번호
    private String postNo="";   //우편번호
    private String dongGtAddr = ""; //동이상주소
    private String dongltAddr = ""; //동이하주소
    private String bizAddress= "";	//업체 주소
    private String holiday01 = "";	//휴일거래여부  0:불가 1:가능
    private String corpState = "";	//기관상태여부 ( 1:신규 2:할당 3:해지 4:거래중지 )
    private String agencyCode1= "";	//대리점코드1
    private String agencyName1= "";	//대리점명1
    private String moAccountNo1= "";	//입금모계좌1
    private String feeAccountNo1= "";	//수수료정산계좌번호1
    private String agencyCode2= "";	//대리점코드2
    private String agencyName2= "";	//대리점명2
    private String moAccountNo2= "";	//입금모계좌2
    private String feeAccountNo2= "";	//수수료정산계좌번호2
    private String agencyCode3= "";	//대리점코드3
    private String agencyName3= "";	//대리점명3
    private String moAccountNo3= "";	//입금모계좌3
    private String feeAccountNo3= "";	//수수료정산계좌번호3
    private String agencyCode4= "";	//대리점코드4
    private String agencyName4= "";	//대리점명4
    private String moAccountNo4= "";	//입금모계좌4
    private String feeAccountNo4= "";	//수수료정산계좌번호4
    private String agencyCode5= "";	//대리점코드5
    private String agencyName5= "";	//대리점명5
    private String moAccountNo5= "";	//입금모계좌5
    private String feeAccountNo5= "";	//수수료정산계좌번호5
    private String feeAmount= "";	//입금수수료
    private String feeRatio= "";	//수수료배분율
    private String feeStartDate= "";	//수수료시작일자
    private String feeEndDate= "";	//수수료만기일자
    private String grbrNo= "";	//지점번호
    private String grbrName= "";	//지점명
    private String filler= "";	//필러

    public String getPostNo() {
        return postNo;
    }

    public void setPostNo(String postNo) {
        this.postNo = postNo;
    }

    public String getDongGtAddr() {
        return dongGtAddr;
    }

    public void setDongGtAddr(String dongGtAddr) {
        this.dongGtAddr = dongGtAddr;
    }

    public String getDongltAddr() {
        return dongltAddr;
    }

    public void setDongltAddr(String dongltAddr) {
        this.dongltAddr = dongltAddr;
    }

    public String getKubun() {
        return kubun;
    }

    public void setKubun(String kubun) {
        this.kubun = kubun;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCorpNewDate() {
        return corpNewDate;
    }

    public void setCorpNewDate(String corpNewDate) {
        this.corpNewDate = corpNewDate;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusNo() {
        return cusNo;
    }

    public void setCusNo(String cusNo) {
        this.cusNo = cusNo;
    }

    public String getBizAddress() {
        return bizAddress;
    }

    public void setBizAddress(String bizAddress) {
        this.bizAddress = bizAddress;
    }

    public String getHoliday01() {
        return holiday01;
    }

    public void setHoliday01(String holiday01) {
        this.holiday01 = holiday01;
    }

    public String getCorpState() {
        return corpState;
    }

    public void setCorpState(String corpState) {
        this.corpState = corpState;
    }

    public String getAgencyCode1() {
        return agencyCode1;
    }

    public void setAgencyCode1(String agencyCode1) {
        this.agencyCode1 = agencyCode1;
    }

    public String getAgencyName1() {
        return agencyName1;
    }

    public void setAgencyName1(String agencyName1) {
        this.agencyName1 = agencyName1;
    }

    public String getMoAccountNo1() {
        return moAccountNo1;
    }

    public void setMoAccountNo1(String moAccountNo1) {
        this.moAccountNo1 = moAccountNo1;
    }

    public String getFeeAccountNo1() {
        return feeAccountNo1;
    }

    public void setFeeAccountNo1(String feeAccountNo1) {
        this.feeAccountNo1 = feeAccountNo1;
    }

    public String getAgencyCode2() {
        return agencyCode2;
    }

    public void setAgencyCode2(String agencyCode2) {
        this.agencyCode2 = agencyCode2;
    }

    public String getAgencyName2() {
        return agencyName2;
    }

    public void setAgencyName2(String agencyName2) {
        this.agencyName2 = agencyName2;
    }

    public String getMoAccountNo2() {
        return moAccountNo2;
    }

    public void setMoAccountNo2(String moAccountNo2) {
        this.moAccountNo2 = moAccountNo2;
    }

    public String getFeeAccountNo2() {
        return feeAccountNo2;
    }

    public void setFeeAccountNo2(String feeAccountNo2) {
        this.feeAccountNo2 = feeAccountNo2;
    }

    public String getAgencyCode3() {
        return agencyCode3;
    }

    public void setAgencyCode3(String agencyCode3) {
        this.agencyCode3 = agencyCode3;
    }

    public String getAgencyName3() {
        return agencyName3;
    }

    public void setAgencyName3(String agencyName3) {
        this.agencyName3 = agencyName3;
    }

    public String getMoAccountNo3() {
        return moAccountNo3;
    }

    public void setMoAccountNo3(String moAccountNo3) {
        this.moAccountNo3 = moAccountNo3;
    }

    public String getFeeAccountNo3() {
        return feeAccountNo3;
    }

    public void setFeeAccountNo3(String feeAccountNo3) {
        this.feeAccountNo3 = feeAccountNo3;
    }

    public String getAgencyCode4() {
        return agencyCode4;
    }

    public void setAgencyCode4(String agencyCode4) {
        this.agencyCode4 = agencyCode4;
    }

    public String getAgencyName4() {
        return agencyName4;
    }

    public void setAgencyName4(String agencyName4) {
        this.agencyName4 = agencyName4;
    }

    public String getMoAccountNo4() {
        return moAccountNo4;
    }

    public void setMoAccountNo4(String moAccountNo4) {
        this.moAccountNo4 = moAccountNo4;
    }

    public String getFeeAccountNo4() {
        return feeAccountNo4;
    }

    public void setFeeAccountNo4(String feeAccountNo4) {
        this.feeAccountNo4 = feeAccountNo4;
    }

    public String getAgencyCode5() {
        return agencyCode5;
    }

    public void setAgencyCode5(String agencyCode5) {
        this.agencyCode5 = agencyCode5;
    }

    public String getAgencyName5() {
        return agencyName5;
    }

    public void setAgencyName5(String agencyName5) {
        this.agencyName5 = agencyName5;
    }

    public String getMoAccountNo5() {
        return moAccountNo5;
    }

    public void setMoAccountNo5(String moAccountNo5) {
        this.moAccountNo5 = moAccountNo5;
    }

    public String getFeeAccountNo5() {
        return feeAccountNo5;
    }

    public void setFeeAccountNo5(String feeAccountNo5) {
        this.feeAccountNo5 = feeAccountNo5;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getFeeRatio() {
        return feeRatio;
    }

    public void setFeeRatio(String feeRatio) {
        this.feeRatio = feeRatio;
    }

    public String getFeeStartDate() {
        return feeStartDate;
    }

    public void setFeeStartDate(String feeStartDate) {
        this.feeStartDate = feeStartDate;
    }

    public String getFeeEndDate() {
        return feeEndDate;
    }

    public void setFeeEndDate(String feeEndDate) {
        this.feeEndDate = feeEndDate;
    }

    public String getGrbrNo() {
        return grbrNo;
    }

    public void setGrbrNo(String grbrNo) {
        this.grbrNo = grbrNo;
    }

    public String getGrbrName() {
        return grbrName;
    }

    public void setGrbrName(String grbrName) {
        this.grbrName = grbrName;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("kubun", kubun)
                .append("corpCode", corpCode)
                .append("corpName", corpName)
                .append("corpNewDate", corpNewDate)
                .append("bizNo", bizNo)
                .append("cusName", cusName)
                .append("cusNo", cusNo)
                .append("postNo", postNo)
                .append("dongGtAddr", dongGtAddr)
                .append("dongltAddr", dongltAddr)
                .append("bizAddress", bizAddress)
                .append("holiday01", holiday01)
                .append("corpState", corpState)
                .append("agencyCode1", agencyCode1)
                .append("agencyName1", agencyName1)
                .append("moAccountNo1", moAccountNo1)
                .append("feeAccountNo1", feeAccountNo1)
                .append("agencyCode2", agencyCode2)
                .append("agencyName2", agencyName2)
                .append("moAccountNo2", moAccountNo2)
                .append("feeAccountNo2", feeAccountNo2)
                .append("agencyCode3", agencyCode3)
                .append("agencyName3", agencyName3)
                .append("moAccountNo3", moAccountNo3)
                .append("feeAccountNo3", feeAccountNo3)
                .append("agencyCode4", agencyCode4)
                .append("agencyName4", agencyName4)
                .append("moAccountNo4", moAccountNo4)
                .append("feeAccountNo4", feeAccountNo4)
                .append("agencyCode5", agencyCode5)
                .append("agencyName5", agencyName5)
                .append("moAccountNo5", moAccountNo5)
                .append("feeAccountNo5", feeAccountNo5)
                .append("feeAmount", feeAmount)
                .append("feeRatio", feeRatio)
                .append("feeStartDate", feeStartDate)
                .append("feeEndDate", feeEndDate)
                .append("grbrNo", grbrNo)
                .append("grbrName", grbrName)
                .append("filler", filler)
                .toString();
    }


    public Map<String, Object> toXChalist() {
        Map<String, Object> param = new HashMap<>();
        param.put("CHACD", corpCode);
        param.put("CHANAME", corpName);
        param.put("CHA_NEW_REG_DT", convet(corpNewDate));
        param.put("CHAOFFNO", bizNo);
        param.put("OWNER", cusName);
        param.put("OWNERTEL", cusNo);
        param.put("CHAADDRESS1", dongGtAddr);
        // TODO 상세주소와 우편번호를 처리할 수 있도록 수정해야 함.
        param.put("CHAADDRESS2", dongltAddr);
        param.put("CHAZIPCODE", postNo);
        param.put("CHAST", findChast(corpState));
        param.put("RCPCNTFEE", feeAmount);
        param.put("RCPBNKFEE", calculate(feeAmount,feeRatio)+"");
        param.put("AGTCD", grbrNo);
        param.put("FEEACCNO", feeAccountNo1);
        param.put("ADJACCYN", findManyAccount());   //다계좌
        //TODO
        param.put("RCPREQYN", "N");     //현금영수증신청여부
        //TODO
        param.put("RCPREQTY", "N");     //현금영수증자동발급여부
        param.put("RCPDTDUPYN", "Y");   //납부기간중복[Y:허용, N:불가, P:부분납]
        param.put("USESMSYN", "N");   //기관 SMS 발송기능 사용여부 [N:미사용,  W:신청중, Y:사용가능, C:취소
        param.put("NOTSMSYN", "N");   //청구시SMS전송방식(M:수동,   N:미사용,  A:자동 )
        param.put("RCPSMSYN", "N");   //납부시SMS전송여부(Y:사용 , N:미사용)
        param.put("RCPSMSTY", "N");   //납부시SMS전송방식(M:수동,   N:미사용,  A:자동 )
        param.put("USEMAILYN", "Y");   //전자메일사용여부
        param.put("AMTCHKTY", "Y");   //납부금액체크[IR방식:Y, SET방식:N]
        param.put("CUSNAMETY", "U");   //납부자명:U/기관명:C TODO
        param.put("NOTAXYN", "N");   //과세대상업체 여부
        param.put("CHATRTY", "01");   //기관 접속형태 웹(01),전문(03)
        param.put("CUSGUBNYN1", "N");   //고지서출력여부1
        param.put("CUSGUBNYN2", "N");   //고지서출력여부2
        param.put("CUSGUBNYN3", "N");   //고지서출력여부3
        param.put("CUSGUBNYN4", "N");   //고지서출력여부4
        param.put("CHASVCYN", "N");   //약관동의여부
        param.put("REGDT", DateUtils.toDTType());   //등록일시
        param.put("BILLIMGTY", "N");   //고지서로고위치
        param.put("USEPGYN", "N");   //온라인 카드결제 사용여부
        param.put("CHKCASH", "Y");   //현금영수증신청서수령여부
        param.put("CHKOFF", "Y");   //사업자등록증수령여부
//        param.put("CHKCMS", "N");   //'사용안함 CMS출금동의서수령여부
//        param.put("USECMSYN", "N");   //'사용안함 CMS사용여부
//        param.put("NOTMINLIMIT", "100");   //청구정액범위
//        param.put("NOTMINFEE", "15000");   //청구정액수수료
//        param.put("NOTCNTFEE", "150");   //청구건당수수료
//        param.put("BASEFEEYN", "N");   //어린이집기본수수료[컬럼사용안함]
        param.put("PRECHAST", "10");   //해피콜미완료:10, 해피콜완료:20, 교육진행완료:30, 미사용:40, 사용동의:50, 사용보류:60, 사용중:70, 해지:80


        return param;
    }

    private String findManyAccount() {
        int count = 0;
        if (hasAccount(moAccountNo1)) {
            count++;
        }
        if (hasAccount(moAccountNo2)) {
            count++;
        }
        if (hasAccount(moAccountNo3)) {
            count++;
        }
        if (hasAccount(moAccountNo4)) {
            count++;
        }
        if (hasAccount(moAccountNo5)) {
            count++;
        }

        if (count >= 2) {
            return "Y";
        } else {
            return "N";
        }
    }

    private boolean hasAccount(String moAccountNo) {
        if (moAccountNo == null) {
            return false;
        } else {
            if (StringUtils.isEmpty(moAccountNo.trim())) {
                return false;
            } else {
                return true;
            }
        }

    }
    private long calculate(String amount,String feeRatio) {
        Long _amount = Long.valueOf(amount);
        Long _ratio = Long.valueOf(feeRatio);
        Long fingerFee = _amount * _ratio / 100;
        return _amount - fingerFee;
    }

    private String findChast(String corpState) {
        if ("1".equalsIgnoreCase(corpState)) {
            return "ST01";
        } else if ("2".equalsIgnoreCase(corpState)) {
            //할당?
            return "ST06";
        } else if ("3".equalsIgnoreCase(corpState)) {
            // 해지
            return "ST02";
        } else {
            // 거래중지.
            return "ST08";
        }
    }

    private String convet(String date8) {
        Date date = null;
        try {
            date = DateUtils.toDate(date8, "yyyyMMdd");
        } catch (ParseException e) {

            date = new Date();
        }
        return DateUtils.toDTType(date);
    }

    public List<Map<String,Object>> toXadjGroup(List<String> strings) {
        // insert 할지 update 할지 처리해야 함. TYPE 으로 처리해야 함.
        List<Map<String, Object>> mapList = new ArrayList<>();
        handleXadjGroup(mapList, agencyCode1, agencyName1, moAccountNo1, strings);
        handleXadjGroup(mapList, agencyCode2, agencyName2, moAccountNo2, strings);
        handleXadjGroup(mapList, agencyCode3, agencyName3, moAccountNo3, strings);
        handleXadjGroup(mapList, agencyCode4, agencyName4, moAccountNo4, strings);
        handleXadjGroup(mapList, agencyCode5, agencyName5, moAccountNo5, strings);

        handleDeleted(corpCode,mapList,strings, agencyCode1, agencyCode2, agencyCode3, agencyCode4, agencyCode5);

        return mapList;
    }

    private void handleDeleted(String corpCode,List<Map<String, Object>> mapList, List<String> strings, String agencyCode1, String agencyCode2, String agencyCode3, String agencyCode4, String agencyCode5) {

        Map<String, String> TEMP = new HashMap<>();
        TEMP.put(agencyCode1, agencyCode1);
        TEMP.put(agencyCode2, agencyCode2);
        TEMP.put(agencyCode3, agencyCode3);
        TEMP.put(agencyCode4, agencyCode4);
        TEMP.put(agencyCode5, agencyCode5);


        for (String agenCyCode:  strings) {
            if (TEMP.containsKey(agenCyCode)) {

            } else {
                mapList.add(deleteMap(corpCode, agenCyCode));
            }
        }

    }

    private Map<String,Object> deleteMap(String corpCode,String agencyCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("CHACD", corpCode);
        map.put("ADJFIREGKEY", agencyCode);
        map.put("TYPE", "D");

        return map;
    }

    private void handleXadjGroup(List<Map<String, Object>> mapList, String agencyCode, String agencyName, String moAccountNo, List<String> strings) {
        if (StringUtils.isEmpty(agencyCode)) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        if (hasAgencyCode(strings, agencyCode)) {
            map.put("TYPE", "U");
        } else {
            map.put("TYPE", "I");
            map.put("GRPADJNAME", agencyName);
        }
        String date = DateUtils.toDTType();
        map.put("ADJFIREGKEY", agencyCode);
        map.put("CHACD", corpCode);
        map.put("MAKER", "CorpInfoAgent");
        map.put("REGDT", date);
        map.put("MAKEDT", date);
        //TODO 계좌번호...

        mapList.add(map);
    }

    private boolean hasAgencyCode(List<String> strings, String agencyCode) {
        return strings.contains(agencyCode);
    }


    public Map<String,Object> toFeeParamMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("CHACD",corpCode);
        map.put("FEE_START_DT",feeStartDate);
        map.put("FEE_END_DT",feeEndDate);

        return map;
    }

    public Map<String,Object> toFeeHistoryParamMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("CHACD",corpCode);
        map.put("FEE_START_DT",feeStartDate);
        map.put("FEE_END_DT",feeEndDate);
        map.put("RCP_CNT_FEE",feeAmount);
        map.put("RCP_BNK_RATE",feeRatio);
        map.put("RCP_BNK_FEE",calculate(feeAmount,feeRatio)+"");
        map.put("REGDT", DateUtils.toDTType());
        return map;
    }


}
