package kr.co.finger.damoa.shinhan.agent.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import kr.co.finger.damoa.commons.Constants;
import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.shinhan.agent.dao.DamoaDao;
import kr.co.finger.msgagent.client.NettyProducer;
import kr.co.finger.msgagent.layout.MessageFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class ExceptionHandler {

    @Autowired
    private MessageFactory messageFactory;
    @Autowired
    private NettyProducer nettyProducer;

    @Autowired
    private DamoaDao damoaDao;
    private Map<String, String> TEMP = new HashMap<>();


    @PostConstruct
    public void init() {
        TEMP.put("V143", "개설기관 업무 종료");
        TEMP.put("V141", "개설기관 SYSTEM 장애");
        TEMP.put("V785", "기타거래불가");
        TEMP.put("V818", "해당기관 없음");
        TEMP.put("V841", "해지기관");
        TEMP.put("V405", "수취인 성명 오류");
        TEMP.put("V891", "해당가상계좌번호확인하세요");
        TEMP.put("V819", "기관코드 오류");
        TEMP.put("V302", "MESSAGE 코드 오류");
        TEMP.put("V768", "TIMEOUT 발생");
        TEMP.put("V407", "총 금액 상이");
        TEMP.put("V859", "수납기간이 아닙니다");
        TEMP.put("V801", "취급기관 코드 오류");
        TEMP.put("V601", "원거래 없음");
        TEMP.put("V848", "이미 취소되었음");
        TEMP.put("V607", "기 취소(출금)된 거래임");
        TEMP.put("V816", "해당계좌 없음");
        TEMP.put("V713", "거래금액 0임");
    }

    private Logger LOG = LoggerFactory.getLogger(getClass());
    public  void handle(Exception e,MsgIF inputMsg,String msg) {
        try {
            LOG.info("INPUT "+inputMsg.toString());
            if (e instanceof DamoaException) {
                DamoaException damoaException = (DamoaException)e;
                if ("FXXX".equalsIgnoreCase(damoaException.getCode())) {
                    LOG.info("FXXX");
                    handleFormatException(inputMsg,damoaException.getIndex());
                } else if (damoaException.getCode().startsWith("F")) {
                    LOG.info("F ..."+damoaException);
                    handleFormatException(inputMsg,damoaException.getCode());
                }else{
                    if ("V785".equalsIgnoreCase(damoaException.getCode().trim())) {
                        handleException(inputMsg,damoaException.getCode(),damoaException.getMsg());
                    }else{
                        LOG.info("NOT V785..."+damoaException);
                        handleException(inputMsg,damoaException.getCode(),getMsg(damoaException.getCode()));
                    }
                }

            } else {
                LOG.error(e.getMessage(),e);
                handleException(inputMsg,"V121","중계기관시스템에러");
            }

        } catch (Exception e1) {
            LOG.error("내부시스템 예외 "+e1.getMessage(),e1);
        }
    }

    private String getMsg(String code) {
        if (StringUtils.isEmpty(code)) {
            return "";
        }else {
            code = code.trim();
            if (TEMP.containsKey(code)) {
                return TEMP.get(code);
            } else {
                return "";
            }
        }
    }

    private void handleException(MsgIF inputMsg, String code,String msg) throws Exception {
        LOG.error("handleException "+code+"\t"+msg);
        MsgIF response = (MsgIF) inputMsg.clone();
        response.response();
        response.setResCode(split(code));
        response.setResMsg(msg);
        String responseMsg = messageFactory.encode(response);
        String msgType = getMsgType(response);
        String dealSeqNo = response.getDealSeqNo().trim();
        writeNettyProducer(nettyProducer,responseMsg,msgType,dealSeqNo);

    }
    String getMsgType(MsgIF msgIF) {
        return msgIF.getMsgTypeCode() + msgIF.getDealTypeCode();
    }

    private void writeNettyProducer(NettyProducer nettyProducer, String responseMsg, String msgType, String dealSeqNo) throws Exception {
        nettyProducer.write(responseMsg, new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                try {
                    insertTransDataAcct(damoaDao, msgType, dealSeqNo, Constants.SEND_DESTINATION, responseMsg, LOG);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }

            }
        });
    }
    void insertTransDataAcct(DamoaDao damoaDao,String typeCode, String dealSeqNo, String destination, String msg, Logger LOG) {
        damoaDao.insertTransDataAcct(typeCode, dealSeqNo,destination, msg);
    }
    private void handleFormatException(MsgIF inputMsg,int index) throws Exception {
        String _index = pad(index);
        String errorCode = "F" + _index;
        String errorMsg = "FIELD 번호 "+_index+" FORMAT 오류";
        String msgTypeCode = inputMsg.getMsgTypeCode();
        if ("0210".equalsIgnoreCase(msgTypeCode) || "0710".equalsIgnoreCase(msgTypeCode) || "0810".equalsIgnoreCase(msgTypeCode)) {
            return;
        }

        write(inputMsg,errorCode,errorMsg);
    }
    private void handleFormatException(MsgIF inputMsg,String code) throws Exception {
        String _index = pad(code.substring(1));
        String errorCode = code;
        String errorMsg = "FIELD 번호 "+_index+" FORMAT 오류";
        String msgTypeCode = inputMsg.getMsgTypeCode();
        if ("0210".equalsIgnoreCase(msgTypeCode) || "0710".equalsIgnoreCase(msgTypeCode) || "0810".equalsIgnoreCase(msgTypeCode)) {
            return;
        }


        write(inputMsg,errorCode,errorMsg);
    }


    private void write(MsgIF inputMsg, String resCode, String resMsg) throws Exception {
        MsgIF response = response(inputMsg);
        response.setResCode(split(resCode));
        response.setResMsg(resMsg);
        String msgType = getMsgType(response);
        String dealSeqNo = response.getDealSeqNo().trim();
        String responseMsg = messageFactory.encode(response);
        writeNettyProducer(nettyProducer,responseMsg,msgType,dealSeqNo);

    }

    private String split(String resCode) {
        if (StringUtils.isEmpty(resCode)) {
            return "";
        } else {
            if (resCode.length() > 4) {
                return resCode.substring(0, 4);
            } else {
                return resCode;
            }
        }
    }
    private MsgIF response(MsgIF inputMsg) throws CloneNotSupportedException {
        MsgIF response = (MsgIF) inputMsg.clone();
        response.response();
        return response;
    }


    private String pad(int index) {
        return StringUtils.leftPad(index + "", 3, "0");
    }
    private String pad(String index) {
        return StringUtils.leftPad(index + "", 3, "0");
    }
}
