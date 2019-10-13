package io.otot.kitten.gateway.connector.core;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class NamedThreadFactory implements ThreadFactory {
    public final String id;
    private final int priority;
    private final ClassLoader contextClassLoader;
    protected final AtomicLong n = new AtomicLong(1);

    public NamedThreadFactory(String id) {
        this(id, Thread.NORM_PRIORITY);
    }

    public NamedThreadFactory(String id, int priority) {
        this(id, priority, null);
    }

    public NamedThreadFactory(String id, int priority, ClassLoader contextClassLoader) {
        this.id = id;
        this.priority = priority;
        this.contextClassLoader = contextClassLoader;
    }

    @Override
    public Thread newThread(Runnable r) {
        String name = String.join("-", id, String.valueOf(n.getAndIncrement()));
        Thread thread = new Thread(r, name);
        thread.setPriority(priority);
        if (contextClassLoader != null) {
            thread.setContextClassLoader(contextClassLoader);
        }
        return thread;

    }

    public String getId() {
        return id;
    }
}
