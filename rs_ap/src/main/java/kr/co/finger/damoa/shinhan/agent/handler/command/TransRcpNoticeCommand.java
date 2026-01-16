package kr.co.finger.damoa.shinhan.agent.handler.command;

import kr.co.finger.damoa.shinhan.agent.domain.repository.ReceiptMasterRepository;
import kr.co.finger.damoa.shinhan.agent.handler.command.NoticeMsgCommand;
import kr.co.finger.damoa.shinhan.agent.util.CommunicateUtil;
import kr.co.finger.damoa.shinhan.agent.validation.notice.NoticeMessageValidatorChain;
import kr.co.finger.shinhandamoa.data.table.model.xrcpmas.TransNotiHistDTO;
import kr.co.finger.shinhandamoa.data.table.model.xrcpmas.TransNotiMasDTO;
import kr.co.finger.shinhandamoa.data.table.model.xrcpmas.XrcpmasDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class TransRcpNoticeCommand {
    private static Logger LOGGER = LoggerFactory.getLogger(NoticeMsgCommand.class);

    @Autowired
    private ReceiptMasterRepository receiptMasterRepository;

    private final NoticeMessageValidatorChain validator;

    private final CommunicateUtil communicateUtil;

    public TransRcpNoticeCommand(CommunicateUtil communicateUtil) {
        this.communicateUtil = communicateUtil;
        validator = new NoticeMessageValidatorChain();
    }

    // 수납 통지 기관에게 수납결과 전송하기
    public void transRcpnNotice(String reqUri, XrcpmasDTO xrcpmasDTO) {

        TransNotiMasDTO transNotiMas = new TransNotiMasDTO();

        transNotiMas.setRcpMasCd(xrcpmasDTO.getRcpMasCd());
        transNotiMas.setRcpMasSt(xrcpmasDTO.getRcpMasSt());
        transNotiMas.setPayDay(xrcpmasDTO.getPayDay());
        transNotiMas.setPayTime(xrcpmasDTO.getPaytime());
        transNotiMas.setRcpAmt(xrcpmasDTO.getRcpAmt());
        transNotiMas.setRcpUsrName(xrcpmasDTO.getRcpUsrName());
        transNotiMas.setChaCd(xrcpmasDTO.getChaCd());
        transNotiMas.setVano(xrcpmasDTO.getVano());
        transNotiMas.setSvecd(xrcpmasDTO.getSvecd());
        transNotiMas.setTranTotCnt(1);
        transNotiMas.setTranSuccYn("N");

        receiptMasterRepository.createTransNotiMas(transNotiMas);

        LOGGER.info("입금 통지 중계 서버와 통신 시작 chaCd ===> {}", transNotiMas.getChaCd());
        LOGGER.info("입금 통지 중계 서버와 통신 성공 reqUri ===> {}", reqUri);

        // CompletableFuture<CommunicateDTO.Response> communicateResult = communicateUtil.communicateWithNotiServer(baseUrl, notiSendUri, xrcpmasDTO);
        // CompletableFuture<CommunicateDTO.Response> communicateResult = communicateUtil.communicateWithNotiServer(reqUri, xrcpmasDTO);
        CompletableFuture<Boolean> communicateResult = communicateUtil.communicateWithNotiServer(reqUri, xrcpmasDTO);
        communicateResult.thenAccept(communicateSuccess -> {
            LOGGER.info("입금 통지 중계 서버와 통신 성공 start ===> {}", communicateSuccess);

            // 수납 통지 결과 성공인 경우 업데이트
            transNotiMas.setTranSuccYn("Y");
            receiptMasterRepository.updateTransNotiMas(transNotiMas);

            // 수납 통지 이력 저장
            TransNotiHistDTO transNotiHist = new TransNotiHistDTO();
            transNotiHist.setRcpMasCd(transNotiMas.getRcpMasCd());
            transNotiHist.setRcpMasSt(transNotiMas.getRcpMasSt());
            transNotiHist.setTranCnt(1);
            transNotiHist.setTranSuccYn("Y");
            receiptMasterRepository.createTransNotiHist(transNotiHist);

            LOGGER.info("입금 통지 중계 서버와 통신 성공 end  ===> {}", communicateSuccess);

        }).exceptionally(throwable -> {

            // 수납 통지 결과 실패인 경우 업데이트
            transNotiMas.setTranSuccYn("N");
            receiptMasterRepository.updateTransNotiMas(transNotiMas);

            // 수납 통지 이력 저장
            TransNotiHistDTO transNotiHist = new TransNotiHistDTO();
            transNotiHist.setRcpMasCd(transNotiMas.getRcpMasCd());
            transNotiHist.setRcpMasSt(transNotiMas.getRcpMasSt());
            transNotiHist.setTranCnt(1);
            transNotiHist.setTranSuccYn("N");
            receiptMasterRepository.createTransNotiHist(transNotiHist);

            LOGGER.error("입금 통지 중계 서버와 통신 실패 ===> {}", throwable.getMessage());
            return null;
        });
    }
}
