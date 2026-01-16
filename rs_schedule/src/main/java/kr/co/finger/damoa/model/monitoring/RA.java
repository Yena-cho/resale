package kr.co.finger.damoa.model.monitoring;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RA {
    Header header;
    DataPart dataPart;
    Trailer trailer;


    @Data
    @Builder
    public static class Header {

        String hdrRecG;
        String hdrFileC;
        String hdrBnkC;
        String hdrJobDt;
        String hdrVacOrgCdNo;
        String hdrFil;

    }

    @Data
    @Builder
    public static class DataPart {

        String dataRecG;
        String seqNo;
        String trxC;
        String hawiMchtBizno;
        String hawiMchtNm;
        String hawiMchtUpjongNm;
        String epayKjAgeYn;
        String drDt;
        String hjiDt;
        String respC;
        String fil;

    }

    @Data
    @Builder
    public static class Trailer {
        public Trailer() {
        }

        public Trailer(String trailerRecG, String trailerTcnt, String trailerNrmCnt, String trailerErrCnt, String trailerFil) {
            this.trailerRecG = trailerRecG;
            this.trailerTcnt = trailerTcnt;
            this.trailerNrmCnt = trailerNrmCnt;
            this.trailerErrCnt = trailerErrCnt;
            this.trailerFil = trailerFil;
        }

        String trailerRecG;
        String trailerTcnt;
        String trailerNrmCnt;
        String trailerErrCnt;
        String trailerFil;
    }
}
