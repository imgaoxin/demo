package org.test.demo.top.simple_rpc_framework.rpc_netty.serialize;

public interface Serializer<T>{
    /**
     * 计算对象序列化的长度，主要用于申请存放序列化数据的字节数组
     * @param entry 待序列化的对象
     * @return 对象序列化后的长度
     */
    int size(T entry);

    /**
     * 序列化对象。将给定的对象序列化成字节数组
     * @param entry 待序列化的对象
     * @param bytes 存放序列化数据的字节数组
     * @param offset 数组的偏移量，从这个位置开始写入序列化数据
     * @param length 对象序列化数据存放需要的长度，也就是{@link Serializer#size(java.lang.Object)}
     */
    void serialize (T entry, byte[] bytes, int offset, int length);

    /**
     * 反序列化对象
     * @param bytes 存放序列化数据的字节数组
     * @param offset 数组的偏移量，存放此序列化数据的开始位置
     * @param length 序列化数据的长度
     * @return 反序列化后生成的对象
     */
    T parse(byte[] bytes, int offset, int length);

    /**
     * 用一个字节标识对象类型，每种类型的数据应该具有不同的类型值
     * @return 对象类型字节标识
     */
    byte type();
    
    /**
     * 返回序列化对象的Class对象。
     * @return
     */
    Class<T> getSerializeClass();
}