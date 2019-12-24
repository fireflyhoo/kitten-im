package io.otot.kitten.service.store.net;

import com.google.protobuf.ByteString;
import com.google.protobuf.ProtocolStringList;
import io.grpc.stub.StreamObserver;
import io.otot.kitten.service.store.grpc.JRPCServiceProto;
import io.otot.kitten.service.store.utils.SerializeTools;

import java.lang.reflect.Method;
import java.util.List;

public class JPRCMethodProvider {
    private Method method;
    private Object target;

    public JPRCMethodProvider(Method method, Object target) {
        this.method = method;
        this.target = target;
    }

    /**
     * 执行rpc请求
     * @param request
     * @param responseObserver
     */
    public void doExecute(JRPCServiceProto.JRPCRequest request, StreamObserver<JRPCServiceProto.JRPCResponse> responseObserver) {
        List<ByteString> args = request.getArgumentsList();
        ProtocolStringList signs = request.getMethodSignList();
        int serializeId = request.getSerialize();
        Object[] _args = new Object[args.size()];
        for(int i=0; i< args.size(); i++){
            try {
                Object obj  =   SerializeTools.deserialize(serializeId,signs.get(i),args.get(i).toByteArray());
                Object reps = method.invoke(target, _args);
                JRPCServiceProto.JRPCResponse resp = JRPCServiceProto.JRPCResponse.newBuilder()
                        .setSuccessful(true)
                        .setBody(ByteString
                                .copyFrom(SerializeTools.serialize(reps)))
                        .setSerialize(0).build();
            } catch (Exception e) {
                responseObserver.onError(e);
            }
        }

    }
}
