package io.otot.kttten.service.session.client;

import io.otot.kttten.service.session.model.ImSession;

/***
 * 远程会话管理服务
 */
public interface SessionClent {
    /***
     * 会话登入
     * @param serverKey 会话登入的服务器
     * @param userKey 会话用户的key
     * @param appKey 会话应用key(多应用隔离)
     * @param uid 会话唯一id
     * @return
     */
    public ImSession login(String serverKey, String userKey, String appKey, String uid);

    /****
     * 会话登出
     * @param servicerKey 会话登出的服务器
     * @param userKey 会话用户key
     * @param appKey  会话应用Key(多应用隔离)
     * @param  uid 会话唯一id uuid
     */
    void logout(String servicerKey, String userKey, String appKey,String uid);


    /*****
     * 获取当前用户的会话信息
     * @param appKey
     * @param userKey
     * @return
     */
    ImSession getSession(String appKey, String userKey);


    /***
     * 进行心跳处理
     * @param appKey
     * @param userKey
     * @param uuid
     * @return  如果当前连接仍然有效返回true
     */
    boolean heartbeat(String appKey, String userKey, String uuid);
}
