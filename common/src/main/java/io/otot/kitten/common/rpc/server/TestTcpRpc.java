package io.otot.kitten.common.rpc.server;

import io.netty.handler.timeout.ReadTimeoutHandler;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

import java.util.concurrent.TimeUnit;

public class TestTcpRpc {
    public static void main(String[] args) {
        DisposableServer server = TcpServer.create()
                .doOnConnection(conn ->
                        conn.addHandler(new ReadTimeoutHandler(3, TimeUnit.SECONDS))
                )
                .handle((inbound, outbound) -> {
                            outbound.sendString(Mono.just("hello"));
                            outbound.send(inbound.receive().map((bs) -> {
                                System.out.println(bs.readableBytes());
                                return bs;
                            }));

                            System.out.println("tests");
                            return outbound.sendString(Mono.just("xxxxxosos"));

                        }
                )
                .host("0.0.0.0")
                .port(9889)
                .bindNow();
        server.onDispose().block();
    }
}
