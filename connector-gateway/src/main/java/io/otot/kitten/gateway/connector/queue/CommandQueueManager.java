package io.otot.kitten.gateway.connector.queue;

import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import io.otot.kitten.gateway.connector.core.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/***
 * 命令队列
 * @author fireflyhoo
 */
public class CommandQueueManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandQueueManager.class);

    private final int BUFFER_SIZE = 1024 * 2 * 2;

    private Disruptor<CommandEvent> disruptor;

    private CommandProvider provider;


    public CommandProvider provider() {
        return provider;
    }

    public void start(WorkHandler[] dcs) {
        CommandEventFactory factory = new CommandEventFactory();
        // 初始化Disruptor
        disruptor = new Disruptor<>(factory,
                BUFFER_SIZE,
                new NamedThreadFactory("disruptor-customer")
        );

        // 启动消费者
        disruptor.handleEventsWithWorkerPool(dcs);
        disruptor.setDefaultExceptionHandler(new IgnoreExceptionHandler());
        disruptor.start();
        // 启动生产者
        RingBuffer<CommandEvent> ringBuffer = disruptor.getRingBuffer();
        provider = new CommandProvider(ringBuffer);
        LOGGER.info("启动请求消息队列初始化成功......");
    }



    public void stop() {
        disruptor.shutdown();
    }

}
