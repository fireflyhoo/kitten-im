package io.otot.kitten.gateway.connector.session.impl;

import io.otot.kitten.gateway.connector.session.GSession;
import io.otot.kitten.gateway.connector.session.GlobalSessionManage;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 全局会话内存 mock
 */
@Service
public class GlobalSessionManageMemoryMock implements GlobalSessionManage {
    private Map<String,GSession> gSessionMap = new ConcurrentHashMap<>();

    @Override
    public GSession login(String serverKey,String appKey, String userKey,  String uid) {
        String key = getKey(appKey,userKey);
        GSession newSession = new GSession();
        newSession.setLastActiveTime(System.currentTimeMillis());
        newSession.setAppKey(appKey);
        newSession.setUserKey(userKey);
        newSession.setServerKey(serverKey);
        newSession.setUuid(uid);
        return gSessionMap.put(key,newSession);
    }

    @Override
    public void logout(String servicerKey, String appKey, String userKey,  String uid) {
        String key = getKey(appKey,userKey);
        GSession gsession =   gSessionMap.get(key);
        if(uid.equalsIgnoreCase(gsession.getUuid())){
            gSessionMap.remove(key);
        }

    }

    @Override
    public GSession getSession(String appKey, String userKey) {
        return gSessionMap.get(getKey(appKey,userKey));
    }

    @Override
    public boolean heartbeat(String appKey, String userKey, String uuid) {
        String key = getKey(appKey,userKey);
        GSession gSession = gSessionMap.get(key);
        if(uuid.equalsIgnoreCase(gSession.getUuid())){
            gSession.setLastActiveTime(System.currentTimeMillis());
            return true;
        }
        return false;
    }

    private String getKey(String appKey, String userKey) {
        return  appKey +"@"+ userKey;
    }
}
