package org.test.demo.base.cache;

import java.util.HashMap;

/**
 * @author gx
 * @create 2019-09-16 10:09
 */
public class StorageImpl<K, V> implements Storage<K, V> {
    private HashMap<K, V> cache;

    public StorageImpl(int capacity) {
        // 初始容量
        cache = new HashMap<>(capacity);
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }
}
