package com.finger.damoa.biz;

import com.finger.damoa.biz.noti.service.XrcpmasNotiService;
import com.finger.damoa.dto.StandardResponseDTO;
import com.finger.damoa.dto.xrcpmas.XrcpmasDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/api")
public class XrcpmasNotiController {
    private final XrcpmasNotiService xrcpmasNotiService;

    @PostMapping("/notiPayment")
    public StandardResponseDTO xrcpmasNotiInfo(@RequestBody List<XrcpmasDTO> xrcpmasList) throws Exception {
        return StandardResponseDTO.success(xrcpmasNotiService.xrcpmasNotiInfo(xrcpmasList));
    }

}
