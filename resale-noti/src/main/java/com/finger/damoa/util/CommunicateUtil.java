package com.finger.damoa.util;

import com.finger.damoa.dto.communicate.CommunicateDTO;
import com.finger.damoa.dto.xrcpmas.XrcpmasDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.List;

/**
 * 입금 통지 중계 서버와 통신하기 위한 util
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CommunicateUtil {

    private final WebClient webClient;

    public Mono<CommunicateDTO.Response> communicateWithNotiServer(String baseUrl, String uri, List<XrcpmasDTO> xrcpmasList) {

            Mono<CommunicateDTO.Response> response = webClient.mutate()
                    .baseUrl(baseUrl)
                    .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
                    .defaultHeader(HttpHeaders.CONNECTION, "keep-alive")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build()
                    .post()
                    .uri(uri)
                    .bodyValue(xrcpmasList)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new HttpClientErrorException(HttpStatus.NOT_FOUND, "서버를 찾을 수 없습니다.")))
                    .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "중계 서버 에러입니다.")))
                    .bodyToMono(CommunicateDTO.Response.class)
//                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(3))) // 통신 실패시 3초 간격으로 3번 재시도
                    .doOnError(error -> { // 비동기 예외 처리
                        if(error instanceof WebClientRequestException) { // webclient 통신 요청 오류
                            WebClientRequestException requestException = (WebClientRequestException) error;
                            log.error("외부 기관 통신 서버를 찾을 수 없습니다. : {}", requestException.getMessage());
                        } else if(error instanceof WebClientResponseException) { // webclient 통신 후 응답 오류
                            WebClientResponseException responseException = (WebClientResponseException) error;
                            log.error("외부 기관 통신 오류입니다. :: {} : {}", responseException.getStatusCode(), responseException.getMessage());
                        } else { // 그 외 오류
                            log.error("communicateWithNotiServer error :: {}", error.getMessage());
                        }
                    });
            ;
            response.subscribe(responseResult -> {
                log.info("responseResult : {}", responseResult);
            });

            return response;
    }
}