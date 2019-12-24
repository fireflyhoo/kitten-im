package io.otot.kitten.service.store.utils;

import com.alibaba.fastjson.JSON;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SerializeTools {

    private static Map<String, Class> classMap = new ConcurrentHashMap<>(16);

    /***
     * 反序列化
     * @param serializeId
     * @param type
     * @param bytes
     * @return
     */
    public static Object deserialize(int serializeId, String type, byte[] bytes) throws ClassNotFoundException {
        Class clazz = classMap.get(type);
        if (clazz == null) {
            if ("int".equals(type)) {
                clazz = int.class;
            } else {
                clazz = Class.forName(type);
            }
            classMap.put(type, clazz);
        }
        return JSON.parseObject(String.valueOf(bytes), clazz);
    }


    /***
     * 序号话
     * @param reps
     * @return
     */
    public static byte[] serialize(Object reps) {
        return JSON.toJSONBytes(reps);
    }
}
