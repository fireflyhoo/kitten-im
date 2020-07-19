package io.otot.kitten.common.rpc.client;

import io.netty.channel.ChannelOption;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.tcp.TcpClient;
import reactor.util.annotation.Nullable;

import java.net.SocketAddress;
import java.time.Duration;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public interface ReactorRpcClient {

    /***
     *
     * @param address
     * @param context
     * @param connectTimeout
     * @return
     */
    default Mono<ReactorRpcClient> connect(SocketAddress address, ConnectionContext context, @Nullable Duration connectTimeout) {
        requireNonNull(address, "address must not be null");
        requireNonNull(context, "context must not be null");
        return TcpClient.newConnection().bootstrap(b -> {
            if (connectTimeout != null) {
                b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Math.toIntExact(connectTimeout.toMillis()));
            }
            return b.remoteAddress(address);
        }).connect().map(
                conn -> {
                    return new NettyReactorRpcClient(conn, context);
                }
        );
    }

    /***
     * 发送消息不等回来
     * @param message
     * @return
     */
    Mono<Void> sendOnly(SendOnlyMessage message);


    /***
     * 发送消息等响应回来
     * @param request
     * @param complete
     * @return
     */
    Flux<ServerMessage> exchange(ExchangeableMessage request, Predicate<ServerMessage> complete);


    Mono<ServerMessage> receiveOnly();

    Mono<Void> close();


    Mono<Void> forceClose();

    boolean isConnected();


}
