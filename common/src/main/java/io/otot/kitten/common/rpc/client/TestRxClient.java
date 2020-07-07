package io.otot.kitten.common.rpc.client;

import io.netty.handler.timeout.ReadTimeoutHandler;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.NettyOutbound;
import reactor.netty.tcp.TcpClient;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

public class TestRxClient {
    public static void main(String[] args) throws IOException {
        Connection connection = TcpClient.create()
                .doOnConnected(conn -> {
                    conn.addHandler(new ReadTimeoutHandler(3, TimeUnit.SECONDS));
                })
                .host("127.0.0.1")
                .port(9889)
                .handle(((inbound, outbound) -> {
                    final NettyOutbound hello = outbound.sendString(Mono.just("hello"));

                    inbound.receive().asString().subscribe((s)->{
                        System.out.println("clinet " + s);
                    });
                   return outbound;
                }))
                .connectNow();

        connection.onDispose().block();
        System.in.read();
        System.in.read();
    }
}
