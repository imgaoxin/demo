package org.test.demo.base.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于支持访问顺序的LinkedHashMap实现
 *
 * @author gx
 * @create 2019-09-16 11:19
 */
public class MyLruCacheV2<K, V> extends LruCache<K, V> {
    private LinkedHashMap<K, V> cache;
    private Lock keyLock = new ReentrantLock(false);

    public MyLruCacheV2(int capacity, Storage<K, V> lowSpeedStorage) {
        super(capacity, lowSpeedStorage);
        cache = new InnerLinkedHashMap<>(capacity, 0.75f, true);
    }

    public Set<K> getKeys(){
        return cache.keySet();
    }

    @Override
    public V get(K key) {
        return getVal(key);
    }

    private V getVal(K key) {
        V val;
        keyLock.lock();
        try {
            val = cache.get(key);
        } finally {
            keyLock.unlock();
        }

        if (val == null) {
            V v = lowSpeedStorage.get(key);
            if (v == null) {
                return null;
            }

            keyLock.lock();
            try {
                cache.put(key, v);
            } finally {
                keyLock.unlock();
            }
            return v;
        }
        return val;
    }

    final class InnerLinkedHashMap<A, B> extends LinkedHashMap<A, B> {

        private static final long serialVersionUID = 8292419400748790629L;

        InnerLinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder) {
            super(initialCapacity, loadFactor, accessOrder);
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<A, B> eldest) {
            return this.size() > capacity;
        }
    }

    public static void main(String[] args) {
        StorageImpl<String, String> storage = new StorageImpl<>(8);
        storage.put("a", "1");
        storage.put("b", "2");
        storage.put("c", "3");
        storage.put("d", "4");
        storage.put("e", "5");
        storage.put("f", "6");

        MyLruCacheV2<String, String> cache = new MyLruCacheV2<>(4, storage);
        cache.get("a");
        cache.get("d");
        cache.get("c");
        cache.get("d");

        for (String key : cache.getKeys()) {
            System.out.println(key);
        }
    }
}
