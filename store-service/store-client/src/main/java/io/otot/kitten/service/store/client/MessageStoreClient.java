package io.otot.kitten.service.store.client;

/**
 *
 * @author fireflyhoo
 */
public interface MessageStoreClient {

    /***
     * 存储消息数据
     * @param data
     * @return
     */
    long put(byte[] data);


    /***
     * 把消息索引塞入用户的收件箱
     * @param appKey
     * @param userKey
     * @param msgId
     */
    void putInbox(String appKey, String userKey, long msgId);


    /***
     * 确收消息
     * @param appKey
     * @param userKey
     * @param msgId
     */
    void ackInbox(String appKey, String userKey, long msgId);


}
