package kr.co.finger.msgagent.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

import static kr.co.finger.msgagent.codec.DecodingState.*;

public class VirtualAccountMsgDecoder extends ReplayingDecoder<DecodingState> {

    private Charset charset;
    private Bean bean;
    private Logger LOG = LoggerFactory.getLogger(getClass());
    public VirtualAccountMsgDecoder(Charset charset) {
        super(READ_HEADER);
        this.charset = charset;
        bean = new Bean();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
            case READ_HEADER:
                state(readHeader(in));
                checkpoint(state());
                break;
            case SKIP_HEADER:
                skipHeader(in);
                checkpoint(PAYLOAD_LENGTH);
                break;
            case PAYLOAD_LENGTH:
                readLength(in);
                checkpoint(PAYLOAD);
                break;
//            case WAIT_PAYLOAD:
//                LOG.info("WAIT_PAYLOAD "+in);
//                state(waitPayload(in));
//                checkpoint(state());
//                break;
            case PAYLOAD:
                try {
                    readPayload(in,out);
                } catch (Exception e){
                    throw e;
                }finally {
                    this.reset();
                }
                break;
            default:
                throw new Exception("Unknown decoding state: ");
        }
    }

//    private DecodingState waitPayload(ByteBuf in) {
//        String length = bean.getLength();
//        int _length = 0;
//        if ("POLLING".equals(bean.getType())) {
//            _length = Integer.valueOf(length);
//        } else {
//            _length = Integer.valueOf(length) - 4;
//        }
//
//        byte[] bytes = new byte[_length];
//        in.readBytes(bytes);
//        bean.setMessage(new String(bytes,charset));
//        return PAYLOAD;
//    }

//    private int getLength(ByteBuf byteBuf) {
//        byte[] bytes = new byte[4];
//        byteBuf.readBytes(bytes);
//        return Integer.valueOf(new String(bytes, this.charset));
//    }

    private void readLength(ByteBuf in) {
        String header = read4Bytes(in);
        bean.setLength(header);

    }

    private void readPayload(ByteBuf in, List<Object> out) {
        String length = bean.getLength();
        int _length = 0;

        if ("POLLING".equals(bean.getType())) {
            String content = getString(in, Integer.valueOf(bean.getLength()));
            out.add(content);
            bean.setOk(true);
        } else {
            String content = getString(in, Integer.valueOf(bean.getLength())-4);
            out.add(length + content);
            bean.setOk(true);
        }
    }

    private void skipHeader(ByteBuf in) {
        byte[] bytes = new byte[26];
        in.readBytes(bytes);
//        System.out.println(new String(bytes,charset));
    }

    private DecodingState readHeader(ByteBuf buffer) {
        String header = read4Bytes(buffer);
        if ("DHRT".equals(header)) {
            bean.setType("DHRT");
            return DecodingState.SKIP_HEADER;
        } else {
            bean.setType("POLLING");
            this.bean.setLength(header);
            return DecodingState.PAYLOAD;
        }
    }

    private String getString(ByteBuf byteBuf, int length) {
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        return new String(bytes, this.charset);
    }

    private void reset() {
        if (bean.isOk()) {
            checkpoint(READ_HEADER);
            this.bean = new Bean();
        } else {

        }

    }

    private String read4Bytes(ByteBuf byteBuf) {
        byte[] bytes = new byte[4];
        byteBuf.readBytes(bytes);
        return new String(bytes, charset);
    }

}
