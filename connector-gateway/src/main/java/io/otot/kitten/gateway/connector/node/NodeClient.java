package io.otot.kitten.gateway.connector.node;

/**
 * @author fireflyhoo
 */
public interface NodeClient {

    /**
     * 通知重复登入
     * @param appKey 会话用户id
     * @param userKey 会话id
     * @param servicerKey 登入的服务id
     */
    void notifyRepetitionLogin(String appKey, String userKey, String servicerKey);


    /***
     * 通知消息下发
     * @param appKey
     * @param userKey
     */
    void notifyMessageGiveOut(String appKey, String userKey);
}
