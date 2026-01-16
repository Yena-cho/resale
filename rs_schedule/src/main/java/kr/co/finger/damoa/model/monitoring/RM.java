package kr.co.finger.damoa.model.monitoring;

import lombok.Builder;
import lombok.Data;

public class RM {
    @Data
    @Builder
    public static class DataPart {
        String dataRecG;
        String seqNo;
        String trxG;
        String vrtlAcno;
        String hawiMchtBizno;
        String drDt;
        String hjiDt;
        String respC;
        String fil;
    }
}
