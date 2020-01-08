package io.otot.kitten.service.store;

import java.io.InputStream;

/****
 * 消息存储服务
 */
public class StoreServiceLauncher {

    public static void main(String[] args) {
        InputStream ino = StoreServiceLauncher.class.getClassLoader().getResourceAsStream("log4j.xml");
        System.out.println(ino.getClass());
    }
}
