package io.otot.kitten.gateway.connector.kernel;

import io.otot.kitten.gateway.connector.network.EventHandler;
import io.otot.kitten.gateway.connector.network.NetworkService;
import io.otot.kitten.gateway.connector.network.SessionChannel;
import io.otot.kttten.service.session.client.SessionClent;
import io.otot.kttten.service.session.model.ImSession;

/****
 * 业务处理服务
 * @author fireflyhoo
 */
public class BusinessService implements EventHandler {
    private  NetworkService networkService;
    private SessionClent sessionClent;

    public BusinessService(NetworkService networkService) {
        this.networkService = networkService;
    }


    @Override
    public void onConnect(SessionChannel channel) {
        String sessionKey = channel.getAppKey() + "@" + channel.getUserKey();
        SessionChannel oldChannel = networkService.setChannel(sessionKey, channel);
        String servicerKey = null;
        String uid = "";
        if(oldChannel!=null){
            oldChannel.close();
        }
        ImSession session = sessionClent.login(servicerKey, sessionKey, uid);
        if(session != null){

        }

    }

    @Override
    public void onMessage(SessionChannel channel, byte[] data) {

    }

    @Override
    public void onClose(SessionChannel channel) {
        String sessionKey = channel.getAppKey() + "@" + channel.getUserKey();
        String servicerKey = null;
        this.networkService.removeChannel(sessionKey);
        sessionClent.logout(servicerKey,channel.getUserKey(),channel.getAppKey());
    }
}
