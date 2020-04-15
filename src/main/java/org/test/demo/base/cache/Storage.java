package org.test.demo.base.cache;

/**
 * KV 存储抽象
 */
public interface Storage<K,V> {
    /**
     * 根据提供的 key 来访问数据
     * @param key 数据 key
     * @return 数据 value
     */
    V get(K key);
}
