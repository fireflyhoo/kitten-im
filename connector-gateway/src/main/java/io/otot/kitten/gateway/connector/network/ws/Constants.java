package io.otot.kitten.gateway.connector.network.ws;

import io.netty.util.AttributeKey;

public class Constants {
    public static AttributeKey<String> key = AttributeKey.valueOf("__channelKey");
    public static AttributeKey<String> appKey = AttributeKey.valueOf("__appKey");
    public static AttributeKey<String> userKey = AttributeKey.valueOf("__userKey");

}
