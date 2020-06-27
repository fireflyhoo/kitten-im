package io.otot.kitten.gateway.connector.session;


/****
 * 全局会话管理器 用于 处理  connector-gateway 多节点部署的情况
 */
public interface GlobalSessionManage {
    /***
     * 会话登入
     * @param serverKey 会话登入的服务器
     * @param appKey 会话应用key(多应用隔离)
     * @param userKey 会话用户的key
     * @param uid 会话唯一id
     * @return
     */
    public GSession login(String serverKey,String appKey, String userKey, String uid);

    /****
     * 会话登出
     * @param servicerKey 会话登出的服务器
     * @param appKey  会话应用Key(多应用隔离)
     * @param userKey 会话用户key
     * @param  uid 会话唯一id uuid
     */
    void logout(String servicerKey, String appKey, String userKey,String uid);


    /*****
     * 获取当前用户的会话信息
     * @param appKey
     * @param userKey
     * @return
     */
    GSession getSession(String appKey, String userKey);


    /***
     * 进行心跳处理
     * @param appKey
     * @param userKey
     * @param uuid
     * @return  如果当前连接仍然有效返回true
     */
    boolean heartbeat(String appKey, String userKey, String uuid);
}
