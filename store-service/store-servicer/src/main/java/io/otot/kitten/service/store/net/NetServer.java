package io.otot.kitten.service.store.net;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import io.otot.kitten.service.store.config.ConfigManager;
import io.otot.kitten.service.store.grpc.JRPCServiceGrpc;
import io.otot.kitten.service.store.grpc.JRPCServiceProto;
import io.otot.kitten.service.store.utils.MethodTools;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NetServer {

    private Server server;

    private Map<String, JPRCMethodProvider> methodProviders = new ConcurrentHashMap<>();

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

    class GreeterImpl extends JRPCServiceGrpc.JRPCServiceImplBase {
        @Override
        public void execute(JRPCServiceProto.JRPCRequest request, StreamObserver<JRPCServiceProto.JRPCResponse> responseObserver) {
            String _interface = request.getInterface();
            String _method = request.getMethod();
            List<String> _signs = request.getMethodSignList();
            String singsKey = MethodTools.getSingsKey(_interface, _method, _signs);
            JPRCMethodProvider methodProvider = methodProviders.get(singsKey);
            if (methodProvider != null) {
                methodProvider.doExecute(request, responseObserver);
            } else {
                responseObserver.onError(new IllegalArgumentException("can'not find this method JPRCMethodProvider"));
            }
        }
    }
}
