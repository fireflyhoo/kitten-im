package io.otot.kttten.service.session.model;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

/**
 * im 会话信息
 */

public class ImSession {
    /**
     * 会话的应用ID
     */
    private  String  appKey;
    /***
     * 会话的用户ID
     */
    private  String  userKey;

    /***
     * 会话当前连接的服务ID
     */
    private  String serverKey;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }
}
