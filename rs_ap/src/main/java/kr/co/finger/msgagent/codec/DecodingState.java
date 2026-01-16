package kr.co.finger.msgagent.codec;

public enum DecodingState {
    READ_HEADER,
    SKIP_HEADER,
    TYPE,
    PAYLOAD_LENGTH,
    PAYLOAD,
    WAIT_PAYLOAD,
    FINISHED
}
