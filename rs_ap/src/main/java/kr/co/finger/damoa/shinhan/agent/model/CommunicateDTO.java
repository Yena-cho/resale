package kr.co.finger.damoa.shinhan.agent.model;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;

/**
 * 외부 통신용 DTO
 */
public class CommunicateDTO {

    @Getter
    @Builder
    public static class Request{
        @NonNull
        private final String url;
        private final String authorization;
        private final String contentType;
        private final Map<String, Object> bodyMap;
    }

    @Data
    public static class Response{
        private String code;
        private String message;
        private Object content;
    }

}