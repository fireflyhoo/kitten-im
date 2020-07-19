package io.otot.kitten.common.rpc.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class EnvelopeSlicer extends LengthFieldBasedFrameDecoder {

    public   static  final  String NAME = "ReactorRpcEnvelopeSlicer";

    private static final int MAX_SIZE = 1024 * 1024 *4;

    //长度4个字节
    private static final int LENG_BYETS = 4;

    public EnvelopeSlicer() {
        super(EnvelopeSlicer.MAX_SIZE, 1, EnvelopeSlicer.LENG_BYETS);
    }
}
