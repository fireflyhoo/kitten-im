package io.otot.kitten.common.rpc.codec;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.otot.kitten.common.rpc.client.ClientMessage;
import io.otot.kitten.common.rpc.client.ServerMessage;
import io.otot.kitten.common.rpc.codec.kryo.KryoFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 消息解析
 *
 * @author fireflyhoo
 */
public class MessageDuplexCodec extends ChannelDuplexHandler {

    private final AtomicBoolean closing;


    public static final String NAME = "ReactorRpcDuplexCodec";

    public MessageDuplexCodec(AtomicBoolean closing) {
        this.closing = closing;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            ServerMessage serverMessage = decode((ByteBuf) msg);
            ctx.fireChannelRead(serverMessage);
        } else if (msg instanceof ServerMessage) {
            ctx.fireChannelRead(msg);
        } else {
            System.out.println("无法解析消息");
        }
    }


    /**
     * 解码消息内容
     *
     * @param in
     * @return
     */
    private ServerMessage decode(ByteBuf in) {
        if (in == null) {
            return null;
        }
        Input input = new Input(new ByteBufInputStream(in));
        Kryo kryo = KryoFactory.getDefaultFactory().getKryo();
        return (ServerMessage) kryo.readClassAndObject(input);
    }


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ClientMessage) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Output output = new Output(baos);
            KryoFactory.getDefaultFactory().getKryo().writeClassAndObject(output, msg);
            output.flush();
            output.close();

            byte[] b = baos.toByteArray();
            ByteBuf byteBuf = ctx.alloc().buffer(b.length);
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ctx.write(byteBuf, promise);
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

        // Server has closed the connection without us wanting to close it
        // Typically happens if we send data asynchronously (i.e. previous command didn't complete).
        if (closing.compareAndSet(false, true)) {
            //logger.warn("Connection has been closed by peer");
        }

        ctx.fireChannelInactive();
    }
}
