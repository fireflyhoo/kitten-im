package io.otot.kitten.gateway.connector.kernel;

import com.lmax.disruptor.WorkHandler;
import io.otot.kitten.gateway.connector.model.ImMessage;
import io.otot.kitten.gateway.connector.network.EventHandler;
import io.otot.kitten.gateway.connector.network.NetworkService;
import io.otot.kitten.gateway.connector.network.SessionChannel;
import io.otot.kitten.gateway.connector.node.NodeService;
import io.otot.kitten.gateway.connector.queue.CommandEvent;
import io.otot.kitten.gateway.connector.queue.CommandQueueManager;
import io.otot.kitten.gateway.connector.utils.SerializationTools;
import io.otot.kttten.service.session.client.SessionClent;
import io.otot.kttten.service.session.model.ImSession;
import io.otot.kttten.service.store.client.MessageStoreClient;

/****
 * 业务处理服务
 * @author fireflyhoo
 */
public class BusinessService implements EventHandler {
    private final CommandQueueManager commandQueueManager;
    private final NetworkService networkService;
    private final String servicerKey ;
    private SessionClent sessionClent;
    private NodeService nodeService;
    private MessageStoreClient storeClient;


    public BusinessService(final String servicerKey, int businessCoreSize, NetworkService networkService, SessionClent sessionClent, NodeService nodeService) {
        this.servicerKey = servicerKey;
        this.networkService = networkService;
        this.sessionClent = sessionClent;
        this.nodeService = nodeService;
        this.commandQueueManager = new  CommandQueueManager();
        if(businessCoreSize <= 0){
            throw new IllegalArgumentException("业务处理核心最少为一个");
        }
        CommandExecuteWorkHandler[] dos = new CommandExecuteWorkHandler[businessCoreSize];
        for (int i=0;i< businessCoreSize;i++){
            dos[i] = new CommandExecuteWorkHandler();
        }
        this.commandQueueManager.start(dos);

    }

    interface Command{
         void execute();
    }


    class  CommandExecuteWorkHandler implements WorkHandler<CommandEvent> {
        @Override
        public void onEvent(CommandEvent commandEvent) throws Exception {
            if(commandEvent.getData() != null ){
                final  Object commd = commandEvent.getData();
                if(commd instanceof  Command){
                    ((Command) commd).execute();
                }
            }
            commandEvent.setData(null);
        }
    }

    @Override
    public void onConnect(SessionChannel channel) {
        //异步开线程处理
        this.commandQueueManager.provider().shootData((Command) () -> {
            String sessionKey = channel.getAppKey() + "@" + channel.getUserKey();
            SessionChannel oldChannel = networkService.setChannel(sessionKey, channel);
            if(oldChannel!=null){
                oldChannel.close();
            }
            ImSession session = sessionClent.login(servicerKey, channel.getUserKey(),channel.getAppKey(), channel.getUUID());
            if(session != null && !servicerKey.equals(session.getServerKey())){
                nodeService.getNodeClient(session.getServerKey()).notifyRepetitionLogin(session.getAppKey(),session.getUserKey(),servicerKey);
            }
        });
    }

    @Override
    public void onMessage(SessionChannel channel, byte[] data) {
        this.commandQueueManager.provider().shootData((Command) () -> {
            //处理逻辑
            ImMessage message  = SerializationTools.deserialize(data,ImMessage.class);
            long msgId =  storeClient.put(data);
            if(message.getType() == ImMessage.MsgType.PTP.getCode()){
               storeClient.putInbox(channel.getAppKey(),message.getTarget(),msgId);
                //点对点消息
               ImSession targetSession =  sessionClent.getSession(channel.getAppKey(),message.getTarget());
               String serviceKey  =  targetSession.getServerKey();
               nodeService.getNodeClient(serviceKey).notifyMessageGiveOut(channel.getAppKey(),message.getTarget());
            }else  if(message.getType() == ImMessage.MsgType.ACK.getCode()){

                //确收消息
                storeClient.actInbox(channel.getAppKey(),channel.getUserKey(),msgId);
            }else if(message.getType() == ImMessage.MsgType.HEARTBEAT.getCode()){
                if(!sessionClent.heartbeat(channel.getAppKey(),channel.getUserKey(),channel.getUUID())){
                    //当前连接已经无效
                    channel.send("{\"code\":\"-1\",\"msg\":\"用户在其他地方登入\"}".getBytes());
                    channel.close();
                }
            }
        });
    }

    @Override
    public void onClose(SessionChannel channel) {
        this.commandQueueManager.provider().shootData((Command) () -> {
            String sessionKey = channel.getAppKey() + "@" + channel.getUserKey();
            networkService.removeChannel(sessionKey);
            sessionClent.logout(servicerKey,channel.getUserKey(),channel.getAppKey(),channel.getUUID());
        });
    }
}
