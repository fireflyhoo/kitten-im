package io.otot.kttten.service;

public class GrpcClient {
    public static void main(String[] args) throws InterruptedException {
//        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("127.0.0.1",8088)
//                .usePlaintext().build();
//        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(managedChannel);
//        long start = System.currentTimeMillis();
//        CountDownLatch countDownLatch = new CountDownLatch(100);
//        for ( int i = 0;i< 100;i++
//             ) {
//            new Thread(()->{
//                try{
//                    for (int j=0;j< 1000000;j++){
//                        HelloWorldProto.HelloReply myResponse = blockingStub.sayHello(HelloWorldProto.HelloRequest.newBuilder().setName("osososo").build());
//
//                    }
//                }catch (Exception e){
//
//                }finally {
//                    countDownLatch.countDown();
//                }
//            }).start();
//        }
//        countDownLatch.await();
//        long time = System.currentTimeMillis() -start;
//        System.out.println(time);
//        System.out.println(1000000.0/time * 1000 * 100);

    }
}
