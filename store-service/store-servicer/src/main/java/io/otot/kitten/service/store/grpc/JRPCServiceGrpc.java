package io.otot.kitten.service.store.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: store_grpc.proto")
public final class JRPCServiceGrpc {

  private JRPCServiceGrpc() {}

  public static final String SERVICE_NAME = "grpc.JRPCService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<JRPCServiceProto.JRPCRequest,
      JRPCServiceProto.JRPCResponse> getExecuteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "execute",
      requestType = JRPCServiceProto.JRPCRequest.class,
      responseType = JRPCServiceProto.JRPCResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<JRPCServiceProto.JRPCRequest,
      JRPCServiceProto.JRPCResponse> getExecuteMethod() {
    io.grpc.MethodDescriptor<JRPCServiceProto.JRPCRequest, JRPCServiceProto.JRPCResponse> getExecuteMethod;
    if ((getExecuteMethod = JRPCServiceGrpc.getExecuteMethod) == null) {
      synchronized (JRPCServiceGrpc.class) {
        if ((getExecuteMethod = JRPCServiceGrpc.getExecuteMethod) == null) {
          JRPCServiceGrpc.getExecuteMethod = getExecuteMethod =
              io.grpc.MethodDescriptor.<JRPCServiceProto.JRPCRequest, JRPCServiceProto.JRPCResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "execute"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  JRPCServiceProto.JRPCRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  JRPCServiceProto.JRPCResponse.getDefaultInstance()))
              .setSchemaDescriptor(new JRPCServiceMethodDescriptorSupplier("execute"))
              .build();
        }
      }
    }
    return getExecuteMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static JRPCServiceStub newStub(io.grpc.Channel channel) {
    return new JRPCServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static JRPCServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new JRPCServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static JRPCServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new JRPCServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class JRPCServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void execute(JRPCServiceProto.JRPCRequest request,
                        io.grpc.stub.StreamObserver<JRPCServiceProto.JRPCResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getExecuteMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getExecuteMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                JRPCServiceProto.JRPCRequest,
                JRPCServiceProto.JRPCResponse>(
                  this, METHODID_EXECUTE)))
          .build();
    }
  }

  /**
   */
  public static final class JRPCServiceStub extends io.grpc.stub.AbstractStub<JRPCServiceStub> {
    private JRPCServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private JRPCServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected JRPCServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new JRPCServiceStub(channel, callOptions);
    }

    /**
     */
    public void execute(JRPCServiceProto.JRPCRequest request,
                        io.grpc.stub.StreamObserver<JRPCServiceProto.JRPCResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getExecuteMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class JRPCServiceBlockingStub extends io.grpc.stub.AbstractStub<JRPCServiceBlockingStub> {
    private JRPCServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private JRPCServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected JRPCServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new JRPCServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public JRPCServiceProto.JRPCResponse execute(JRPCServiceProto.JRPCRequest request) {
      return blockingUnaryCall(
          getChannel(), getExecuteMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class JRPCServiceFutureStub extends io.grpc.stub.AbstractStub<JRPCServiceFutureStub> {
    private JRPCServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private JRPCServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected JRPCServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new JRPCServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<JRPCServiceProto.JRPCResponse> execute(
        JRPCServiceProto.JRPCRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getExecuteMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_EXECUTE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final JRPCServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(JRPCServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_EXECUTE:
          serviceImpl.execute((JRPCServiceProto.JRPCRequest) request,
              (io.grpc.stub.StreamObserver<JRPCServiceProto.JRPCResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class JRPCServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    JRPCServiceBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return JRPCServiceProto.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("JRPCService");
    }
  }

  private static final class JRPCServiceFileDescriptorSupplier
      extends JRPCServiceBaseDescriptorSupplier {
    JRPCServiceFileDescriptorSupplier() {}
  }

  private static final class JRPCServiceMethodDescriptorSupplier
      extends JRPCServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    JRPCServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (JRPCServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new JRPCServiceFileDescriptorSupplier())
              .addMethod(getExecuteMethod())
              .build();
        }
      }
    }
    return result;
  }
}
