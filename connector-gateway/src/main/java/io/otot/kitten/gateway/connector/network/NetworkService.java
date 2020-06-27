package io.otot.kitten.gateway.connector.network;


import io.otot.kitten.gateway.connector.core.ServiceLifecycle;

/***
 * 网络服务
 * @author fireflyhoo
 */
public interface NetworkService extends ServiceLifecycle {

    /***
     * 设置端口
     * @param port
     */
    void setPort(int port);

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
    void setEventHandler(NetworkEventHandler handler);


}
