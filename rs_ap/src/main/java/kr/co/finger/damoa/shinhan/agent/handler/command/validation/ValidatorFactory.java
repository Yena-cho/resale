package kr.co.finger.damoa.shinhan.agent.handler.command.validation;


import java.util.HashMap;
import java.util.Map;

public class ValidatorFactory {
    private static ValidatorFactory ourInstance = new ValidatorFactory();

    public static ValidatorFactory getInstance() {
        return ourInstance;
    }

    private static Map<String,Validator> TEMP = new HashMap<>();

    static {
        TEMP.put("MsgCorCode", new MsgCorpCodeValidator());
        TEMP.put("SndRcvFlag", new SndRsvFlagValidator());
        TEMP.put("MsgTypeCode", new MsgTypeCodeValidator());
        TEMP.put("DealTypeCode", new DealTypeCodeValidator());
        TEMP.put("MsgSndDate", new MsgSndDateValidator());
        TEMP.put("DeadlineYn", new DeadlineYnValidator());
        TEMP.put("OccurGubun", new OccurGubunValidator());
        TEMP.put("MediaGubun", new MediaGubunValidator());
        TEMP.put("DealSeqNo", new DealSeqNoValidator());
        TEMP.put("DepositCorpCode", new DepositCorpCodeValidator());
        TEMP.put("DepositAccountNo", new DepositAccountNoValidator());
//        TEMP.put("DepositAccountName", new DepositAccountNameValidator());
        TEMP.put("CorpCode", new CorpCodeValidator());
        TEMP.put("DealDate", new DealDateValidator());
        TEMP.put("AdminInfo", new AdminInfoValidator());
        TEMP.put("InstitutionCode", new InstitutionCodeValidator());
        TEMP.put("AggregateDealDate", new AggregateDealDateValidator());
        TEMP.put("TransactionAmount", new TransactionAmountValidator());
        TEMP.put("ResaleCorpCd", new ResaleCorpCdValidator());
    }

    private ValidatorFactory() {
    }

    public Validator getValidator(String key) {
        return TEMP.get(key);
    }

}
