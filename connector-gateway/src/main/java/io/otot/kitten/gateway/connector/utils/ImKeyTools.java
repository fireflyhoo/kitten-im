package io.otot.kitten.gateway.connector.utils;

public class ImKeyTools {

    /***
     * 获取会话的key
     * @param appKey
     * @param userKey
     * @return
     */
    public static  String getKey(String appKey, String userKey) {
        return  appKey +"@"+ userKey;
    }
}
