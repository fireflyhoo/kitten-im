package io.otot.kitten.gateway.connector.session.impl;

import io.otot.kitten.gateway.connector.network.SessionChannel;
import io.otot.kitten.gateway.connector.session.LocalSessionManage;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LocalSessionManageImpl implements LocalSessionManage {
    private Map<String, SessionChannel> activitySessions = new ConcurrentHashMap<>();


    @Override
    public SessionChannel get(String key) {
        return activitySessions.get(key);
    }


    @Override
    public SessionChannel put(String key, SessionChannel session) {
       return activitySessions.put(key, session);
    }

    @Override
    public void remove(String key) {
        activitySessions.remove(key);
    }
}
