package io.otot.kitten.gateway.connector.network;


import io.otot.kitten.gateway.connector.core.ServiceLifecycle;

/***
 * 网络服务
 * @author fireflyhoo
 */
public interface NetworkService extends ServiceLifecycle {

    /**
     * 启动网络服务
     */
    @Override
    void start();

    /***
     * 停止网络服务
     */
    @Override
    void stop();


    /***
     * 设置事件处理句柄
     * @param handler
     */
    void setEventHandler(EventHandler handler);


    /**
     * 通过channelKey 会话管道
     * @param key
     * @return
     */
    SessionChannel getChannel(String key);


    /***
     * 设置通道缓存
     * @param key
     * @param channel
     * @return 如对应的key已经有老的通道直接返回
     */
    SessionChannel setChannel(String key,SessionChannel channel);



    /***
     * 移除通道
     * @param key appKey+'@'+userKey
     * @return
     */
    SessionChannel removeChannel(String key);



}
