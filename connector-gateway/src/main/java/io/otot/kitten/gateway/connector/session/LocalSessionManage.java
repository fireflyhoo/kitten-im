package io.otot.kitten.gateway.connector.session;

import io.otot.kitten.gateway.connector.network.SessionChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public interface LocalSessionManage {


    public SessionChannel get(String key) ;


    public SessionChannel put(String key, SessionChannel session) ;

    public void remove(String key) ;


}
