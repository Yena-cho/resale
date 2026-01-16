package com.finger.damoa.biz.noti.service;

import com.finger.damoa.dto.communicate.CommunicateDTO;
import com.finger.damoa.dto.xrcpmas.XrcpmasDTO;
import reactor.core.publisher.Mono;

import java.util.List;


public interface XrcpmasNotiService {

    Mono<CommunicateDTO.Response> xrcpmasNotiInfo(List<XrcpmasDTO> xrcpmasList);

}
