package org.test.demo.top.simple_rpc_framework.rpc_netty.serialize;

import java.util.HashMap;
import java.util.Map;

import org.test.demo.top.simple_rpc_framework.rpc_api.spi.ServiceSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class SerializeSupport {
    private static final Logger logger = LoggerFactory.getLogger(SerializeSupport.class);
    private static Map<Class<?>/* 序列化对象的类型 */, Serializer<?>/* 序列化器的实现 */> serializerMap = new HashMap<>();
    private static Map<Byte/* 序列化对象类型字节标识{@link Serializer#type()} */, Class<?>/* 序列化对象的类型 */> typeMap = new HashMap<>();

    static {
        for (Serializer<?> serializer : ServiceSupport.loadAll(Serializer.class)) {
            registerType(serializer.type(), serializer.getSerializeClass(), serializer);
            logger.info("Found serializer, class: {}, type: {}.", serializer.getSerializeClass().getCanonicalName(),
                    serializer.type());
        }
    }

    private static void registerType(byte type, Class<?> clazz, Serializer<?> serializer) {
        serializerMap.put(clazz, serializer);
        typeMap.put(type, clazz);
    }

    public static <E> E parse(byte[] buffer) {
        return parse(buffer, 0, buffer.length);
    }

    /**
     * 获取序列化对象类型字节标识
     * 
     * @param buffer
     * @return
     */
    private static byte parseEntryType(byte[] buffer) {
        // {@link Serializer#type()}
        return buffer[0];
    }

    private static <E> E parse(byte[] buffer, int offset, int length) {
        byte type = parseEntryType(buffer);
        Class<E> oClass = (Class<E>) typeMap.get(type);
        if (null == oClass) {
            throw new SerializeException(String.format("Unknown entry type: %d!", type));
        } else {
            return parse(buffer, offset + 1, length - 1, oClass);
        }
    }

    private static <E> E parse(byte[] buffer, int offset, int length, Class<E> oClass) {
        Object entry = serializerMap.get(oClass).parse(buffer, offset, length);
        if (oClass.isAssignableFrom(entry.getClass())) {
            return (E) entry;
        } else {
            throw new SerializeException("Type mismatch!");
        }
    }

    public static <E> byte[] serialize(E entry) {
        Serializer<E> serializer = (Serializer<E>) serializerMap.get(entry.getClass());
        if (serializer == null) {
            throw new SerializeException(String.format("Unknown entry class type: %s", entry.getClass().toString()));
        }
        byte[] bytes = new byte[serializer.size(entry) + 1];
        bytes[0] = serializer.type();
        serializer.serialize(entry, bytes, 1, bytes.length - 1);
        return bytes;
    }
}