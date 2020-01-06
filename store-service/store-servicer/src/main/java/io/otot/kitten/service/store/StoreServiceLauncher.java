package io.otot.kitten.service.store;

import io.otot.kitten.service.store.config.ConfigManager;
import io.otot.kitten.service.store.config.StoreConfig;
import io.otot.kitten.service.store.net.JPRCNetServer;
import io.otot.kitten.service.store.rocksdb.StoreServiceImpl;

import java.io.IOException;

/****
 * 消息存储服务
 */
public class StoreServiceLauncher {

    public static void main(String[] args) {
        StoreConfig config = ConfigManager.INSTANCE.getConfig();

        StoreService storeService = new StoreServiceImpl();
        storeService.start();
        storeService.put(1000l, "里是什么".getBytes());
        JPRCNetServer netServer = new JPRCNetServer(ConfigManager.INSTANCE.getConfig().getPort());
        netServer.start();

        try {
            System.in.read();
            System.in.read();
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
