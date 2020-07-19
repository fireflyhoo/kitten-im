package io.otot.kitten.common.rpc.codec;

import io.netty.handler.codec.LengthFieldPrepender;

public class EnvelopeParcel extends LengthFieldPrepender {
    public   static  final  String NAME = "ReactorRpcEnvelopeParcel";

    //长度4个字节
    private static final int LENG_BYETS = 4;

    public EnvelopeParcel() {
        super(LENG_BYETS);
    }
}
