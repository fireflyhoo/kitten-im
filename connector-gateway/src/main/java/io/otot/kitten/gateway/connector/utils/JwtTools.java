package io.otot.kitten.gateway.connector.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

public class JwtTools {
    private final static String secret = "im.yayatao.com";
    private final static String ISSUER = "im-yayatao";

    public static class JwtUser {
        private String appKey;
        private String userKey;


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

        public JwtUser(String appKey, String userKey) {
            this.appKey = appKey;
            this.userKey = userKey;
        }
    }

    public static String getToken(String appKey, String userKey, long expires) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        //创建jwt
        JWTCreator.Builder builder = JWT.create().
                withIssuer(ISSUER). //发行人
                withExpiresAt(new Date(System.currentTimeMillis() + expires)); //过期时间点
        builder.withClaim("appKey", appKey);
        builder.withClaim("userKey", userKey);
        //签名加密
        return builder.sign(algorithm);
    }

    public static JwtUser verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        //解密
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        DecodedJWT jwt = verifier.verify(token);
        Map<String, Claim> map = jwt.getClaims();
        return new JwtUser(map.get("appKey").asString(), map.get("userKey").asString());
    }


    public static void main(String[] args) {
        for(int i = 0;i< 100;i++){
            //index.html?user-key={user_key}&im-token={im_token}
            System.out.println("/index.html?app-key="+10086+"&user-key=dingding"+i + "&im-token="+JwtTools.getToken("10086", "dingding"+i, 1000 * 3600 * 24 * 100));
        }
    }
}
