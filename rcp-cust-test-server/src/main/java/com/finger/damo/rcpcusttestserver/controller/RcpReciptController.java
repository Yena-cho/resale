package com.finger.damo.rcpcusttestserver.controller;

import com.finger.damo.rcpcusttestserver.dto.NoticeMessage;
import com.finger.damo.rcpcusttestserver.dto.QueryMessage;
import com.finger.damo.rcpcusttestserver.dto.XrcpmasDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RestController
public class RcpReciptController {


    @PostMapping("/notiPayment")
    public boolean xrcpmasNotiInfo(@RequestBody XrcpmasDTO xrcpMasDto) throws Exception {
        log.info("입금통지 전용기관 : 입금통지 : 수신결과 : {}", xrcpMasDto.toString());

        return true;
    }

    @PostMapping("/queryApi")
    public QueryMessage queryApiInfo(@RequestBody QueryMessage queryMessage) throws Exception {
        log.info("중계가관 : 수취조회 수신메세지 : {}", queryMessage.toString());

        queryMessage.setMsgTypeCode("0210");

        // 수취오류 테스트를 위한 설정
        //queryMessage.setResCode("V891"); //
        //queryMessage.setResMsg("해당가상계좌번호확인하세요");
        queryMessage.setResCode("V407"); //
        queryMessage.setResMsg("납부금액다름");



        log.info("중계가관 : 수취조회 응답메세지 : {}", queryMessage.toString());

        return queryMessage;
    }

    @PostMapping("/noticeApi")
    public NoticeMessage noticeApiInfo(@RequestBody NoticeMessage noticeMessage) throws Exception {
        log.info("중계기관 : 입금통지 수신메세지 : {}", noticeMessage.toString());

        noticeMessage.setMsgTypeCode("210"); // 입금응답코드 설정

        // 입금오류 테스트를 위한 설정
        noticeMessage.setResCode("V404"); //
        noticeMessage.setResMsg("수취계좌입금한도초과");

        log.info("중계기관 : 입금통지 응답메세지 : {}", noticeMessage.toString());

        return noticeMessage;
    }



}
