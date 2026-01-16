package kr.co.finger.damoa.shinhan.agent.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import kr.co.finger.damoa.model.msg.MessageCode;
import kr.co.finger.damoa.model.msg.PollingMessage;
import kr.co.finger.msgagent.layout.MessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * IDLE 상태 처리 클래스.
 * 5분간 IDLE 상태이면 TEST-CALL 전문 전송.
 * TEST-CALL 전문 전송후 30초간 응답이 없으면 세션종료.
 * 아래처럼 구현함.
 *
 * IdelStateHandler에서 IDLE 시간을 30초로 설정됨..
 * WRITE_IDLE시에 30 * 10 이 되면 TEST-CALL 전문 전송.
 * READ_IDLE에서 30 * 11 이 되면 세션 종료 시킴.
 */
public class DamoaIdleHandler {
    private int index;
    private int count;
    /**
     * 이 클래스가 처리하는 상태.
     * Server는 보내고 Client는 받기만 하기 때문에 이렇게 구현됨.
     */
    private MessageFactory messageFactory;
    private Logger LOG = LoggerFactory.getLogger(getClass());

    public DamoaIdleHandler(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    public void whenIdle(ChannelHandlerContext ctx, IdleStateEvent event) throws Exception {
        LOG.info("******************** DamoaIdleHandler whenIdle... ********************");
        if (event.state() == IdleState.WRITER_IDLE) {
            count++;
            LOG.info("WRITER_IDLE ... "+count);
            if (count == 10) {
                String type = MessageCode.POLLING_TYPE_REQUEST.getId();
                PollingMessage pollingMessage = PollingMessage.newMessage(type,toString(new Date(),"MMddHHmmss"));
                LOG.info(">>> [" + pollingMessage.getDesc() + "]" + pollingMessage);
                String msg = messageFactory.encodeStartWithLength(pollingMessage,pollingMessage.getPollingType());
                LOG.info(">>> ]"+msg+"[");
                ctx.channel().writeAndFlush(msg);
            } else if (count > 10) {
                ctx.channel().close();
            }
        }
    }

     private String toString(Date date) {
        return toString(date, "yyyyMMddHHmmss");
    }
    private String toString(Date date,String format) {
        return new SimpleDateFormat(format).format(date);
    }
    public void init() {
        LOG.info("******************** DamoaIdleHandler init ********************");
        count=0;
    }
}
