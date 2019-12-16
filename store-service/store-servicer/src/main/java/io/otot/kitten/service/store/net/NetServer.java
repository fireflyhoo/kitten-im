package io.otot.kitten.service.store.net;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import io.otot.kitten.service.store.config.ConfigManager;
import io.otot.kitten.service.store.grpc.helloworld.GreeterGrpc;
import io.otot.kitten.service.store.grpc.helloworld.HelloWorldProto;

import java.io.IOException;

public class NetServer {

    private Server server;

    public void start() {
        server = ServerBuilder.forPort(ConfigManager.INSTANCE.getConfig().getPort())
                .addService(new GreeterImpl())
                .build();
        try {
            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class GreeterImpl extends GreeterGrpc.GreeterImplBase {
        @Override
        public void sayHello(HelloWorldProto.HelloRequest request, StreamObserver<HelloWorldProto.HelloReply> responseObserver) {

            responseObserver.onNext(HelloWorldProto.HelloReply.newBuilder().setMessage("我就是测试一下看看行不行").build());
            responseObserver.onCompleted();
        }
    }


}
