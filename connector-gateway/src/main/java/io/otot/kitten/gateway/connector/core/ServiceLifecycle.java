package io.otot.kitten.gateway.connector.core;

/********
 * 服务的生命周期
 * @author fireflyhoo
 */
public interface ServiceLifecycle {

    /**
     * 启动
     */
    void start();


    /***
     * 停止
     */
    void stop();
}
