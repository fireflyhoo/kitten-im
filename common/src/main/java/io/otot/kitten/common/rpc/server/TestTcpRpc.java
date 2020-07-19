package io.otot.kitten.common.rpc.server;

import io.otot.kitten.common.rpc.codec.EnvelopeParcel;
import io.otot.kitten.common.rpc.codec.EnvelopeSlicer;
import io.otot.kitten.common.rpc.codec.MessageDuplexCodec;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

import java.util.concurrent.atomic.AtomicBoolean;

public class TestTcpRpc {
    private static AtomicBoolean closing = new AtomicBoolean(false);

    public static void main(String[] args) {
        DisposableServer server = TcpServer.create()
                .doOnConnection(connection ->{
                    connection.addHandler(EnvelopeSlicer.NAME, new EnvelopeSlicer());
                    connection.addHandler(EnvelopeParcel.NAME, new EnvelopeParcel());
                    connection.addHandlerLast(MessageDuplexCodec.NAME, new MessageDuplexCodec(closing));
                        }

                )
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
