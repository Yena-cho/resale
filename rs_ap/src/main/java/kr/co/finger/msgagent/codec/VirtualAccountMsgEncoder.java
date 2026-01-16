package kr.co.finger.msgagent.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.List;

import static kr.co.finger.msgagent.codec.DecodingState.*;

public class VirtualAccountMsgEncoder extends MessageToByteEncoder<String> {
    private Charset charset;

    public VirtualAccountMsgEncoder(Charset charset) {
        this.charset = charset;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        byte[] bytes = msg.getBytes(charset);
        out.writeBytes(bytes);
    }
}
