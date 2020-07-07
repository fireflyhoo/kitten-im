package io.otot.kitten.common.rpc.server;

import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

public class TestTcpRpc {
    public static void main(String[] args) {
        DisposableServer server = TcpServer.create()
//                .doOnConnection(conn ->
//                        conn.addHandler(new ReadTimeoutHandler(3, TimeUnit.SECONDS))
//                )
                .handle((inbound, outbound) -> {
                            inbound.receive().asString().map((s)->{
                                System.out.println(s);
                                outbound.sendString(Mono.just(s+ " 你妹啊!")).then().subscribe();
                                return s;
                            }).then().subscribe();

                            outbound.sendString(Mono.just("hello "))
                                    .sendString(Mono.just("Hello Server  oo "))
                                    .then().subscribe();
                            return Mono.never();

                        }
                )
                .host("0.0.0.0")
                .port(9889)
                .bindNow();
        server.onDispose().block();
    }
}
