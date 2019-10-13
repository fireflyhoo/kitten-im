package io.otot.kitten.gateway.connector.queue;

import com.lmax.disruptor.EventFactory;

/***
 * 事件构造器
 * @author fireflyhoo
 */
public class CommandEventFactory implements EventFactory<CommandEvent> {
    @Override
    public CommandEvent newInstance() {
        return new CommandEvent();
    }
}
