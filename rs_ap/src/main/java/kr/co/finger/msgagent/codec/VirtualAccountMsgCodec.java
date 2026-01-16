package kr.co.finger.msgagent.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

public class VirtualAccountMsgCodec extends ByteToMessageCodec<String> {
    //
    private Charset charset = CharsetUtil.UTF_8;
    private int lengthSize = 4;

    private Logger LOG = LoggerFactory.getLogger(getClass());
    public VirtualAccountMsgCodec() {
    }

    public VirtualAccountMsgCodec(Charset charset, int lengthSize) {
        this.charset = charset;
        this.lengthSize = lengthSize;
    }

    public VirtualAccountMsgCodec(Charset charset) {
        this.charset = charset;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        byte[] bytes = msg.getBytes(charset);
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List out) throws Exception {
        // 4바이트가 전문길이이므로 전문길이를 확인후 전문길이만큼 패킷이 모이게 되면 전문을 읽는다.
        if (byteBuf.readableBytes() < lengthSize) {
            return;
        }
        byteBuf.markReaderIndex();
        // 전문길이를 확인할 수 있는 상태
        String header = read4Bytes(byteBuf);
        if ("DHRT".equals(header)) {
//            skipHeader(byteBuf);
            byteBuf.resetReaderIndex();
            // 전문길이를 확인. 단 readerIndex가 변경되지 않게 함.
            if (byteBuf.readableBytes() < 30 + lengthSize) {
                return;
            }

            // 전문길이만큼 데이터가 모인 상태.
            String input = getStringAtDhrt(byteBuf);
            // 전문을 읽어 다음 단계로 진행하게 함.
            out.add(input);

        } else {
            int _length = Integer.valueOf(header);
            byteBuf.resetReaderIndex();
            // 전문길이를 확인. 단 readerIndex가 변경되지 않게 함.
            if (byteBuf.readableBytes() < _length + lengthSize) {
                return;
            }
            // 전문길이만큼 데이터가 모인 상태.
            String input = getString(byteBuf, _length);
            // 전문을 읽어 다음 단계로 진행하게 함.
            out.add(input);
        }

    }

    private String getString(ByteBuf byteBuf, int length) {
        byte[] bytes = new byte[length];
        // 통신망 헤더 4 바이트  삭제
        byteBuf.readInt();
        // 전문 읽기수행.
        byteBuf.readBytes(bytes);
        return new String(bytes, charset);
    }
    private String getStringAtDhrt(ByteBuf byteBuf) {
        // 30바이트 skip.
        skipHeader(byteBuf);
        // 길이 읽기
        String _4bytes = read4Bytes(byteBuf);
        int length = Integer.valueOf(_4bytes);
        byte[] bytes = new byte[length];
        // 전문 읽기수행.
        byteBuf.readBytes(bytes);
        return _4bytes+new String(bytes, charset);
    }

    private void skipHeader(ByteBuf byteBuf) {
        byte[] bytes = new byte[30];
        byteBuf.readBytes(bytes);


    }
    private int getLength(ByteBuf byteBuf) {
        byte[] bytes = new byte[lengthSize];
        byteBuf.readBytes(bytes);
        return Integer.valueOf(new String(bytes, charset));
    }

    private String read4Bytes(ByteBuf byteBuf) {
        byte[] bytes = new byte[lengthSize];
        byteBuf.readBytes(bytes);
        return new String(bytes, charset);
    }

}
