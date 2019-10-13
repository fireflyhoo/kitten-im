package io.otot.kitten.gateway.connector.queue;


import com.lmax.disruptor.RingBuffer;

/***
 * 命令提供者
 * @author fireflyhoo
 */
public class CommandProvider {

    private final RingBuffer<CommandEvent> ringBuffer;


    public CommandProvider(RingBuffer<CommandEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;

    }


    /***
     * 发布数据
     * @param data 数据
     */
    public void shootData(Object data) {
        // Grab the next sequence
        long sequence = ringBuffer.next();
        try {
            // Get the entry in the Disruptor
            CommandEvent event = ringBuffer.get(sequence);
            // Fill with data
            event.setData(data);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
