package com.finger.damoa.dto.communicate;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;

/**
 * 외부 통신용 DTO
 * @author volka
 */
public class CommunicateDTO {

    @Getter
    @Builder
    public static class Request {

        @NonNull
        private final String url;
        private final String authorization;
        private final Map<String, Object> bodyMap;
    }

    // TO DO : 기관에게 응답받을 데이터 매핑해야함 (현재 외부 IP인 텔레그램으로 요청했음)
    @Data
    public static class Response {
//        private String code;
//        private String message;
//        private Object content;
//        private Result result;
          private String ok;
//        private Map<List<Object>, Object> result;
    }

    @Data
    public static class MobileAuthResponse {
        private String code;
        private String message;
        private Map<String, String> content;
    }

    @Data
    public static class Result {
        private String resultCode;
        private String resultMsg;
        private Object resultObject;
    }

}
