package io.otot.kitten.gateway.connector.queue;


/***
 * 队列数据
 * @author fireflyhoo
 */
public class CommandEvent {
    private  Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
