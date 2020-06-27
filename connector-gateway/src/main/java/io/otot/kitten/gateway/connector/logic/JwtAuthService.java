package io.otot.kitten.gateway.connector.logic;


import io.otot.kitten.gateway.connector.network.NetworkAuthService;
import io.otot.kitten.gateway.connector.utils.JwtTools;
import org.springframework.stereotype.Service;

/**
 * 通过jwt 验签名进行认证
 */
@Service
public class JwtAuthService implements NetworkAuthService {
    @Override
    public String auth(String appKey, String accessToken) throws Exception {
        JwtTools.JwtUser jwtUser = JwtTools.verifyToken(accessToken);
        return jwtUser.getUserKey();
    }
}
