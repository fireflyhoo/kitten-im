package io.otot.kitten.gateway.connector.utils;


import com.google.gson.Gson;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * xhl
 * @author fireflyhoo
 */
public class SerializationTools {
    private static Gson gson = new Gson();
    public static  <T>T deserialize(byte[] data ,Class<T> clazz){
       return gson.fromJson(new String(data,UTF_8),clazz);
    }
}
