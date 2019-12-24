package io.otot.kitten.service.store.utils;

import com.google.common.base.Joiner;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class MethodTools {

    /***
     * 获取方法的签名
     * @param anInterface
     * @param method
     * @param signs
     * @return
     */
    public static String getSingsKey(String anInterface, String method, List<String> signs) {
        return anInterface + "." + method + "@" + Joiner.on(",").join(signs);
    }


    /***
     * 获取方法的签名
     * @param method
     * @return
     */
    public static String getSingsKey(Method method) {
        String anInterface = method.getDeclaringClass().getName();
        String anMethod = method.getName();
        List<String> signs = new ArrayList<>();
        for (Parameter _param : method.getParameters()) {
            signs.add(_param.getType().getName());
        }

        return anInterface + "." + anMethod + "@" + Joiner.on(",").join(signs);
    }
}
