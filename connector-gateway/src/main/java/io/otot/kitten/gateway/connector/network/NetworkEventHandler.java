package io.otot.kitten.gateway.connector.network;

/**
 * 事件处理句柄
 * @author fireflyhoo
 */
public interface NetworkEventHandler {
    /*****
     * 连接完成
     * @param channel
     */
   void onConnect(SessionChannel channel);


    /***
     * 收到消息数据包
     * @param channel
     * @param data
     */
   void onMessage(SessionChannel channel,byte[] data);


    /***
     * 连接关闭
     * @param channel
     */
   void onClose(SessionChannel channel);


    /**
     * 启动任务
     */
    void start();
}
