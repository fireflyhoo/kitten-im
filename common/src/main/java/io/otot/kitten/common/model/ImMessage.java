package io.otot.kitten.common.model;

/**
 *  消息
 * @author fireflyhoo
 */
public class ImMessage {

    /****
     * 消息id
     */
    private String messageId;

    /***
     * 消息类型
     */
    private  int type;


    /***
     * 应用ID
     */
    private  String appKey;

    /**
     * 发送者
     */
    private  String sender;

    /**
     * 接收者
     */
    private  String target;


    /***
     * 时间戳
     */
    private  long time;

    /***
     * 消息体
     */
    private  byte[] body;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public static enum  MsgType{
        UP_LINE(1,"上线"),
        DOWN_LINE(2, "下线"),
        PTP(3,"点对点"),
        GROUP(4,"群聊"),
        ACK(5,"消息确收"),
        HEARTBEAT(6,"心跳"),
        ECHO(7,"回声消息"),
        CMD(8,"命令消息")
        ;

        private final int code;
        private final String notice;

        public int getCode() {
            return code;
        }

        public String getNotice() {
            return notice;
        }

        MsgType(int code, String notice) {
            this.code = code;
            this.notice = notice;
        }

        public static MsgType valueOf(int code){
            for (MsgType value : MsgType.values()) {
                if(value.code == code){
                    return value;
                }
            }
            throw new IllegalArgumentException("消息类型"+code +"不支持");
        }
    }
}
