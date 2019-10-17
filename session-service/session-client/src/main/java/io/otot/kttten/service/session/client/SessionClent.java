package io.otot.kttten.service.session.client;

import io.otot.kttten.service.session.model.ImSession;

/***
 * 远程会话管理服务
 */
public interface SessionClent {
    /**
     * 会话登入
     *
     * @param serverKey
     * @param sessionKey
     * @param uid
     */
    public ImSession login(String serverKey, String sessionKey, String uid);

    /****
     * 会话登出
     * @param servicerKey
     * @param userKey
     * @param appKey
     */
    void logout(String servicerKey, String userKey, String appKey);
}
