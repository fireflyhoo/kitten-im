package io.otot.kitten.gateway.connector.model;

/**
 *  消息
 * @author fireflyhoo
 */
public class ImMessage {
    private  String appKey;
    private  String sender;
    private  MsgType type;
    private  String target;
    private  byte[] body;

    public  enum  MsgType{
        PTP(1,"点对点"),
        GROUP(2,"群聊"),
        ACK(3,"消息确收"),
        HEARTBEAT(4,"心跳")
        ;

        private final int code;
        private final String notice;

        MsgType(int code, String notice) {
            this.code = code;
            this.notice = notice;
        }
    }
}
