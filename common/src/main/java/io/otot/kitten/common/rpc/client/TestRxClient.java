package io.otot.kitten.common.rpc.client;

import io.netty.handler.timeout.ReadTimeoutHandler;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.tcp.TcpClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TestRxClient {
    public static void main(String[] args) throws IOException {
        Connection connection = TcpClient.create()
                .doOnConnected(conn -> {
                    conn.addHandler(new ReadTimeoutHandler(3, TimeUnit.SECONDS));
                })
                .host("127.0.0.1")
                .port(9889)
                .connectNow();

      //  connection.onDispose().block();
        connection.outbound().sendString(Mono.just("Goss")).then().subscribe();
        connection.inbound().receive().asString().map(s -> {
            System.out.println(s);
            connection.outbound().sendString(Mono.just("呵呵...")).then().subscribe();
            return s;
        }).then().subscribe();
        System.in.read();
        connection.channel();
    }
}
