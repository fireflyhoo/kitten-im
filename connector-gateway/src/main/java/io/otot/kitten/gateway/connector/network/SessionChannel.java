package io.otot.kitten.gateway.connector.network;

/****
 * 会话通道
 * @author fireflyhoo
 */
public interface SessionChannel {

    /***
     * 发送数据到通道
     * @param bytes
     */
    void send(byte[] bytes);

    /**
     * 获取最后活动时间
     *
     * @return
     */
    long getLastActivityTime();

    /***
     * 设置最后活动时间
     * @param time 最后激活时间
     */
    void setLastActivityTime(long time);


    /***
     * 关闭通道
     */
    void close();

    /***
     * 判断管道是否有效
     * @return
     */
    boolean isOpen();

    /***
     * 获取会话用户ID
     * @return
     */
    String getUserKey();


    /***
     * 获取会话应用ID
     * @return
     */
    public String getAppKey();

}
