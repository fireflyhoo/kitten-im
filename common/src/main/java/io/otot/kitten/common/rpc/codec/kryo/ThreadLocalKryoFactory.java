package io.otot.kitten.common.rpc.codec.kryo;

import com.esotericsoftware.kryo.Kryo;

/**
 * @author fireflyhoo
 */
public class ThreadLocalKryoFactory extends  KryoFactory {

    private final ThreadLocal<Kryo> holder = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            return createKryo();
        }
    };

    @Override
    public Kryo getKryo() {
        return holder.get();
    }
}
