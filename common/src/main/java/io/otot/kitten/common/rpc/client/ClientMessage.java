package io.otot.kitten.common.rpc.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.reactivestreams.Publisher;

public interface ClientMessage {

    /**
     * Encode a message into a {@link ByteBuf} data buffer without envelope header.
     *
     * @param allocator the {@link ByteBufAllocator} to use to get a {@link ByteBuf} data buffer to write into.
     * @param context   current MySQL connection context
     * @return a {@link Publisher} that produces {@link ByteBuf}s sliced by {@code Envelopes.MAX_ENVELOPE_SIZE}, which containing the encoded message.
     */
    Publisher<ByteBuf> encode(ByteBufAllocator allocator, ConnectionContext context);
}
