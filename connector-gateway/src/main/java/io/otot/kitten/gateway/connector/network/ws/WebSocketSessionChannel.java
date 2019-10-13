package io.otot.kitten.gateway.connector.network.ws;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.otot.kitten.gateway.connector.network.SessionChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/***
 * SessionChannel WebSocket实现
 * @author fireflyhoo
 */
public class WebSocketSessionChannel implements SessionChannel {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketSessionChannel.class);


    private final Channel channel;

    private long lastActivityTime;

    public WebSocketSessionChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void send(byte[] bytes) {
        try {
            channel.writeAndFlush(new BinaryWebSocketFrame(Unpooled.wrappedBuffer(bytes))).sync().get();
        } catch (Exception e) {
            LOGGER.error("send message to clint err: {}", e);
        }
    }

    @Override
    public long getLastActivityTime() {
        return lastActivityTime;
    }

    @Override
    public void setLastActivityTime(long time) {
        this.lastActivityTime = time;
    }

    @Override
    public void close() {
        try {
            channel.close().sync().get();
        } catch (Exception e) {
            LOGGER.error("close channel err:{}",e);
        }
    }

    @Override
    public boolean isOpen() {
        return false;
    }
}
