package io.otot.kitten.gateway.connector.model;

/**
 *  消息
 * @author fireflyhoo
 */
public class ImMessage {
    private  String appKey;
    private  String sender;
    private  int type;
    private  String target;
    private  byte[] body;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }


    public static enum  MsgType{
        PTP(1,"点对点"),
        GROUP(2,"群聊"),
        ACK(3,"消息确收"),
        HEARTBEAT(4,"心跳")
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
    }
}
