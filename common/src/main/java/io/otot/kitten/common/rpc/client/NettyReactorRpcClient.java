package io.otot.kitten.common.rpc.client;

import io.otot.kitten.common.rpc.codec.EnvelopeParcel;
import io.otot.kitten.common.rpc.codec.EnvelopeSlicer;
import io.otot.kitten.common.rpc.codec.MessageDuplexCodec;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;
import reactor.netty.Connection;
import reactor.netty.FutureMono;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * @author fireflyhoo
 */
public class NettyReactorRpcClient implements ReactorRpcClient {

    private final AtomicBoolean closing = new AtomicBoolean();

    private static final BiConsumer<Object, SynchronousSink<ServerMessage>> INBOUND_HANDLE =
            NettyReactorRpcClient::inboundHandle;
    private final Connection connection;
    private final ConnectionContext context;
    private final EmitterProcessor<ServerMessage> responseProcessor = EmitterProcessor.create();


    public NettyReactorRpcClient(Connection connection, ConnectionContext context) {
        requireNonNull(connection, "connection must not be null");
        requireNonNull(context, "context must not be null");
        this.connection = connection;
        this.context = context;
        connection.addHandler(EnvelopeSlicer.NAME, new EnvelopeSlicer());
        connection.addHandler(EnvelopeParcel.NAME, new EnvelopeParcel());
        connection.addHandlerLast(MessageDuplexCodec.NAME, new MessageDuplexCodec(closing));
        Flux<ServerMessage> inbound = connection.inbound().receiveObject().handle(INBOUND_HANDLE);
        inbound.subscribe(this.responseProcessor::onNext, throwable -> {
            //打日志
            responseProcessor.onError(throwable);
            connection.dispose();
        }, this.responseProcessor::onComplete);
    }

    /***
     * 消息处理
     * @param msg
     * @param sink
     */
    private static void inboundHandle(Object msg, SynchronousSink<ServerMessage> sink) {
        if (msg instanceof ServerMessage) {
            sink.next((ServerMessage) msg);
        } else {
            // ReferenceCounted will released by Netty.
            sink.error(new IllegalStateException("Impossible inbound type: " + msg.getClass()));
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Function<T, T> identity() {
        return (Function<T, T>) Identity.INSTANCE;
    }

    @Override
    public Mono<Void> sendOnly(SendOnlyMessage message) {
        requireNonNull(message, "message must not be null");
        return Mono.<Mono<Void>>create(sink -> {
                    send(message);
                }
        ).flatMap(identity());

    }

    @Override
    public Flux<ServerMessage> exchange(ExchangeableMessage request, Predicate<ServerMessage> complete) {
        return send(request).thenMany(responseProcessor).handle(((serverMessage, sink) -> {
            if (complete.test(serverMessage)) {
                sink.next(serverMessage);
                sink.complete();
            } else {
                sink.next(serverMessage);
            }
        }));
    }


    private Mono<Void> send(Object message) {
        return FutureMono.from(connection.channel().writeAndFlush(message));
    }

    @Override
    public Mono<ServerMessage> receiveOnly() {
        return Mono.<Mono<ServerMessage>>create(sink -> {
            responseProcessor.next();
        }).flatMap(identity());
    }

    @Override
    public Mono<Void> close() {
        return forceClose();
    }

    @Override
    public Mono<Void> forceClose() {
        return FutureMono.deferFuture(() -> connection.channel().close());
    }

    @Override
    public boolean isConnected() {
         return !closing.get() && connection.channel().isOpen();
    }

    private static final class Identity implements Function<Object, Object> {

        private static final Identity INSTANCE = new Identity();

        @Override
        public Object apply(Object o) {
            return o;
        }
    }
}
