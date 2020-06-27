package io.otot.kitten.gateway.connector.logic;

import com.lmax.disruptor.WorkHandler;
import io.otot.kitten.common.model.ImMessage;
import io.otot.kitten.gateway.connector.network.NetworkEventHandler;
import io.otot.kitten.gateway.connector.network.SessionChannel;
import io.otot.kitten.gateway.connector.queue.CommandEvent;
import io.otot.kitten.gateway.connector.queue.CommandQueueManager;
import io.otot.kitten.gateway.connector.session.GlobalSessionManage;
import io.otot.kitten.gateway.connector.session.LocalSessionManage;
import io.otot.kitten.gateway.connector.utils.ImKeyTools;
import io.otot.kitten.gateway.connector.utils.SerializationTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/****
 * 业务处理服务
 * @author fireflyhoo
 */
@Service
public class BusinessService implements NetworkEventHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(BusinessService.class);

    private CommandQueueManager commandQueueManager;

    /**
     * 本地会话信息
     */
    @Autowired
    private LocalSessionManage sessionManage;

    @Autowired
    private GlobalSessionManage globalSessionManage;

    @Value("${servicerKey}")
    private String servicerKey;

    @Value("${connector.businessCoreSize}")
    private int businessCoreSize;


    public BusinessService() {
        this.commandQueueManager = new CommandQueueManager();
        if (businessCoreSize <= 0) {
            throw new IllegalArgumentException("业务处理核心最少为一个");
        }
        CommandExecuteWorkHandler[] dos = new CommandExecuteWorkHandler[businessCoreSize];
        for (int i = 0; i < businessCoreSize; i++) {
            dos[i] = new CommandExecuteWorkHandler();
        }
        this.commandQueueManager.start(dos);

    }

    @Override
    public void onConnect(SessionChannel channel) {
//        //异步开线程处理
        this.commandQueueManager.provider().shootData((Command) () -> {
            String sessionKey = channel.getAppKey() + "@" + channel.getUserKey();
            SessionChannel oldChannel = sessionManage.put(sessionKey, channel);
            if (oldChannel != null) {
                oldChannel.close();
            }
        });
    }

    @Override
    public void onMessage(SessionChannel channel, byte[] data) {
        this.commandQueueManager.provider().shootData((Command) () -> {
            //处理逻辑
            ImMessage message = SerializationTools.deserialize(data, ImMessage.class);
            ImMessage.MsgType type = ImMessage.MsgType.valueOf(message.getType());
            switch (type) {
                case UP_LINE:
                    onUpLine(channel, message);
                    break;
                case DOWN_LINE:
                    onDownLine(channel, message);
                    break;
                case ECHO:
                    onEcho(channel, message);
                    break;
                case HEARTBEAT:
                    onHeartbeat(channel, message);
                    break;
                default:
                    onBusiness(channel, message);
            }
        });
    }

    /***
     * 需要处理想业务消息
     * @param channel
     * @param message
     */
    private void onBusiness(SessionChannel channel, ImMessage message) {
        ImMessage.MsgType type = ImMessage.MsgType.valueOf(message.getType());
        if (type == ImMessage.MsgType.PTP) {
            String key = ImKeyTools.getKey(message.getAppKey(), message.getTarget());
            SessionChannel targetSession = sessionManage.get(key);
            if (targetSession != null && targetSession.isOpen()) {
                //目标和当前用户在同一个服务器且在线
                targetSession.send(SerializationTools.serialize(message));
                return;
            }
        }
        handleBusinecssProcessor(channel, message);
    }

    /***
     * 进行业务消息的处理
     * @param channel
     * @param message
     */
    private void handleBusinecssProcessor(SessionChannel channel, ImMessage message) {
        //TODO 处理信息
        LOGGER.info("都要处理消息的内容,{},{}", channel, message);
    }

    /**
     * 下线消息
     *
     * @param channel
     * @param message
     */
    private void onDownLine(SessionChannel channel, ImMessage message) {
        globalSessionManage.logout(servicerKey, channel.getAppKey(), channel.getUserKey(), channel.getUUID());
    }

    /***
     * 客户端发送上线消息
     * @param channel
     * @param message
     */
    private void onUpLine(SessionChannel channel, ImMessage message) {
        globalSessionManage.login(servicerKey, channel.getAppKey(), channel.getUserKey(), channel.getUUID());
    }

    /***
     * 心跳消息 3s 一次
     * @param channel
     * @param message
     */
    private void onHeartbeat(SessionChannel channel, ImMessage message) {
        //检查会话是否正常
        globalSessionManage.heartbeat(channel.getAppKey(), channel.getUserKey(), channel.getUUID());
    }

    /***
     * 回音消息
     * @param channel
     * @param message
     */
    private void onEcho(SessionChannel channel, ImMessage message) {
        //回音消息直接原样返回
        channel.send(SerializationTools.serialize(message));
    }

    @Override
    public void onClose(SessionChannel channel) {
        this.commandQueueManager.provider().shootData((Command) () -> {
            String sessionKey = channel.getAppKey() + "@" + channel.getUserKey();
            sessionManage.remove(sessionKey);
        });
    }


    interface Command {
        void execute();
    }

    class CommandExecuteWorkHandler implements WorkHandler<CommandEvent> {
        @Override
        public void onEvent(CommandEvent commandEvent) throws Exception {
            if (commandEvent.getData() != null) {
                final Object commd = commandEvent.getData();
                if (commd instanceof Command) {
                    ((Command) commd).execute();
                }
            }
            commandEvent.setData(null);
        }
    }
}
