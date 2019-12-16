package io.otot.kitten.service.store.rocksdb;


import io.otot.kitten.service.store.StoreService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class StoreServiceImplTest {

    private  StoreService storeService ;

    @Before
    public void setUp() throws Exception {
        storeService = new StoreServiceImpl();
        storeService.start();
    }

    @After
    public void tearDown() throws Exception {
        storeService.stop();
    }

    @Test
    public void put() {
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int j=0;j < 100;j++){
            int finalJ = j;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i =0; i< 100000;i ++){
                        storeService.put(23232+i + finalJ *10000 ,("为了持续提升Mockito以及更进一步的提升单元测试体验,我们希望你升级到Mockito2.0.Mockito遵循语意化的版本控制，" +
                                "除非有非常大的改变才会变化主版本号。在一个库的生命周期中,为了引入一系列有用的特性，修改已存在的行为或者API等重大变更是在所难免的" +
                                "。因此，我们希望你能够爱上 Mockito 2.0!重要变更 :Mockito从Hamcrest中解耦，自定义的matchers API也发生了改变,查看ArgumentMatcher " +
                                "的基本原理以及迁移指南。跟着我们的示例来mock 一个List,因为大家都知道它的接口（例如add(),get(),lear()）。" +
                                "不要mock一个真实的List类型,使用一个真实的实例来替代。"+
                                " 为了持续提升Mockito以及更进一步的提升单元测试体验,我们希望你升级到Mockito2.0.Mockito遵循语意化的版本控制，" +
                                "除非有非常大的改变才会变化主版本号。在一个库的生命周期中,为了引入一系列有用的特性，修改已存在的行为或者API等重大变更是在所难免的" +
                                "。因此，我们希望你能够爱上 Mockito 2.0!重要变更 :Mockito从Hamcrest中解耦，自定义的matchers API也发生了改变,查看ArgumentMatcher " +
                                "的基本原理以及迁移指南。跟着我们的示st类型,使用一个真实的实例来替代。多久啊付款了大家发送多少st类型,使用一个真实的实例来替代。多久啊付款了大家发送多少例来mock 一个List,因为大家都知道它的接口（例如add(),get(),lear()）。" +
                                "不要mock一个真实的List类型,使用一个真实的实例来替代。多久啊付款了大家发送多少st类型,使用一个真实的实例来替代。多久啊付款了大家发送多少st类型,使用一个真实的实例来替代。多久啊付款了大家发送多少st类型,使用一个真实的实例来替代。多久啊付款了大家发送多少了复健科老地方发;阿拉斯加付款房贷卡发大飞机阿斯利康的"+i).getBytes());
                    }
                    countDownLatch.countDown();
                }
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("put data tps:"+ 10000000 /((endTime-start) /1000));
    }

    @Test
    public void get() {
        long start = System.currentTimeMillis();
        for (int i= 0;i< 10000000;i++){
            storeService.get(i);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("get data tps:"+ 10000000 /((endTime-start) /1000));
        System.out.println(endTime-start);
    }



    @Test
    public void remove() {
        long start = System.currentTimeMillis();
        for (int i= 0;i< 10000000;i++){
            storeService.get(i);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("get data tps:"+ 10000000 /((endTime-start) /1000.0));

    }
}
