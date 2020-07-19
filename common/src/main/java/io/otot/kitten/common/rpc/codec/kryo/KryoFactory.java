package io.otot.kitten.common.rpc.codec.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fireflyhoo
 */
public  abstract class KryoFactory {
    private final static KryoFactory threadFactory = new ThreadLocalKryoFactory();

    protected KryoFactory() {

    }

    public static KryoFactory getDefaultFactory() {
        return threadFactory;
    }

    public abstract Kryo getKryo();



    protected Kryo createKryo() {

        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.register(Arrays.asList("").getClass(), new DefaultSerializers.ArraysAsListSerializer());
        kryo.register(BigDecimal.class, new DefaultSerializers.BigDecimalSerializer());
        kryo.register(BigInteger.class, new DefaultSerializers.BigIntegerSerializer());

        kryo.register(BitSet.class, new DefaultSerializers.BitSetSerializer());


        kryo.register(HashMap.class);
        kryo.register(ArrayList.class);
        kryo.register(LinkedList.class);
        kryo.register(HashSet.class);
        kryo.register(TreeSet.class);
        kryo.register(Hashtable.class);
        kryo.register(Date.class);
        kryo.register(Calendar.class);
        kryo.register(ConcurrentHashMap.class);
        kryo.register(SimpleDateFormat.class);
        kryo.register(GregorianCalendar.class);
        kryo.register(Vector.class);
        kryo.register(BitSet.class);
        kryo.register(StringBuffer.class);
        kryo.register(StringBuilder.class);
        kryo.register(Object.class);
        kryo.register(Object[].class);
        kryo.register(String[].class);
        kryo.register(byte[].class);
        kryo.register(char[].class);
        kryo.register(int[].class);
        kryo.register(float[].class);
        kryo.register(double[].class);

        return kryo;
    }
}
