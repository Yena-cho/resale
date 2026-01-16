package kr.co.finger.msgagent.codec;

import io.netty.handler.codec.FixedLengthFrameDecoder;

public class FirmbakingMsgDecoder extends FixedLengthFrameDecoder {
    /**
     * Creates a new instance.
     * @param frameLength the length of the frame
     */
    public FirmbakingMsgDecoder(int frameLength) {
        super(frameLength);
    }
}
