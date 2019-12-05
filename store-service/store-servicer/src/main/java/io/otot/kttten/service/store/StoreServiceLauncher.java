package io.otot.kttten.service.store;

import com.sun.org.apache.xpath.internal.operations.String;
import io.otot.kttten.service.store.config.ConfigManager;
import io.otot.kttten.service.store.config.StoreConfig;
import io.otot.kttten.service.store.rocksdb.StoreServiceImpl;

/****
 * 消息存储服务
 */
public class StoreServiceLauncher {



    public static void main(String[] args) {

        StoreConfig config = ConfigManager.INSTANCE.getConfig();

        StoreService storeService = new StoreServiceImpl();
        storeService.start();
        storeService.put(1000l,"里是什么".getBytes());
        System.out.println( new java.lang.String(storeService.get(10001)));
    }
}
