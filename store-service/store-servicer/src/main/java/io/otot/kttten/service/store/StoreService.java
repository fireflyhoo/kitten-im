package io.otot.kttten.service.store;

public interface StoreService {
    /****
     * 初始化
     */
    void start();


    /***
     *  停止
     */
    void stop();

    /**
     * 保存数据
     * @param key
     * @param data
     */
    void put(long key, byte[] data);


    /****
     *通过 key 获取数据
     * @param key
     * @return
     */
    byte[] get(long key);

    /**
     * 删除
     * @param key
     */
    void remove(long key);


}
