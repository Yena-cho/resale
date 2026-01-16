package com.finger.damoa.biz.noti.service.impl;

import com.finger.damoa.biz.noti.service.XrcpmasNotiService;
import com.finger.damoa.dto.communicate.CommunicateDTO;
import com.finger.damoa.dto.xrcpmas.XrcpmasDTO;
import com.finger.damoa.util.CommunicateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class XrcpmasNotiServiceImpl implements XrcpmasNotiService {

    @Autowired
    private CommunicateUtil communicateUtil;

    @Value("${noti-server.rqst-url}")
    private String baseUrl;

    private static String notiSendUri = "";

    @Override
    public Mono<CommunicateDTO.Response> xrcpmasNotiInfo(List<XrcpmasDTO> xrcpmasList) {

        // TO DO : 요청 URL, URI, BODY 외부 기관의 형식에 맞춰서 수정해야함
        String strDTO = xrcpmasList.toString();
        String strURL = baseUrl + strDTO;

        Mono<CommunicateDTO.Response> response = communicateUtil.communicateWithNotiServer(strURL, notiSendUri, xrcpmasList);
        response.subscribe(responseResult -> {
            log.info("입금 통지 중계 서버와 통신 성공 {}", responseResult.getOk());
        }, communicateError -> {
            log.error("입금 통지 중계 서버와 통신 실패 {}", communicateError.getMessage());
        });

        return response;
    }
}



