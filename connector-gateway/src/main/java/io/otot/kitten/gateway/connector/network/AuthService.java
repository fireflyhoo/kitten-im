package io.otot.kitten.gateway.connector.network;

/**
 * 对连接进行认证服务
 *
 * @author fireflyhoo
 */
public interface AuthService {
    /***
     * 认证Token
     * @param appKey
     * @param accessToken
     * @return 认证成功返回 userKey
     */
    String auth(String appKey, String accessToken) throws Exception;
}
