package io.otot.kitten.service.store.net;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import io.otot.kitten.service.store.grpc.JRPCServiceGrpc;
import io.otot.kitten.service.store.grpc.JRPCServiceProto;
import io.otot.kitten.service.store.utils.MethodTools;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JPRCNetServer {


    private int port = 6009;

    private Server server;

    private Map<String, JPRCMethodProvider> methodProviders = new ConcurrentHashMap<>();


    public JPRCNetServer(int port) {
        this.port = port;
    }

    public void addServerProvider(Class clazz, Object instance) {
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("注册的不是接口");
        }
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            String key = MethodTools.getSingsKey(method);
            methodProviders.put(key, new JPRCMethodProvider(method, instance));
        }
    }

    public void start() {
        server = ServerBuilder.forPort(port)
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
