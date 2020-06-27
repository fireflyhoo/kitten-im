package io.otot.kitten.gateway.connector.session;

public class GSession {
    /***
     * 会话当前连接的服务ID
     */
    private  String serverKey;



    /**
     * 会话的应用ID
     */
    private  String  appKey;

    /***
     * 会话的用户ID
     */
    private  String  userKey;

    /***
     * 管道ID
     */
    private  String uuid;


    /******
     * 最后活跃时间
     */
    private  long lastActiveTime;


    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(long lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }
}
