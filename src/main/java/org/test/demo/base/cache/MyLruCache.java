package org.test.demo.base.cache;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 待优化：双向链表 + 哈希表实现O(1)复杂度操作
 * => LRU-K,LRU-2Q解决LRU-1的缓存污染问题（大量一次性最近访问数据）
 *
 * @author gx
 * @create 2019-08-29 17:22
 */
public class MyLruCache<K, V> extends LruCache<K, V> {
    /*
    链表存储数据key, 最近使用的key排在尾部, 存储不够时优先从头部清除
     */
    private List<K> keys = new LinkedList<>();
    private Map<K, V> cache = new HashMap<>(capacity);
    private Lock keyLock = new ReentrantLock(false);

    public MyLruCache(int capacity, Storage<K, V> lowSpeedStorage) {
        super(capacity, lowSpeedStorage);
    }

    public List<K> getKeys() {
        return keys;
    }

    @Override
    public V get(K key) {
        return getValue(key);
    }

    private V getValue(K key) {
        V v = cache.get(key);

        if (v == null) {
            //从低速缓存(磁盘...)获取数据
            v = lowSpeedStorage.get(key);

            if (v == null) return null;

            keyLock.lock();
            try {
                //需要保证容量判断与删除、添加操作的连续性
                if (keys.size() >= capacity) remove();

                add(key, v);
            } finally {
                keyLock.unlock();
            }

            return v;
        }

        keyLock.lock();
        try {
            //并行执行环境keys.get(keys.size() - 1)有越界或索引偏移风险(remove，add)
            if (key != keys.get(keys.size() - 1)) move(key);
        } finally {
            keyLock.unlock();
        }

        return v;
    }

    // 瓶颈: LinkedList remove object 是O(n)复杂度
    // 改进: 可以通过记录达到一定访问次数再移动，来降低性能损耗
    private void move(K key) {
        keys.remove(key);
        keys.add(key);
    }

    private void add(K key, V value) {
        cache.put(key, value);
        keys.add(key);
    }

    private void remove() {
        K remove = keys.remove(0);
        cache.remove(remove);
    }

    public static void main(String[] args) {
        StorageImpl<String, String> storage = new StorageImpl<>(16);
        storage.put("a", "1");
        storage.put("b", "2");
        storage.put("c", "3");
        storage.put("d", "4");
        storage.put("e", "5");
        storage.put("f", "6");

        MyLruCache<String, String> cache = new MyLruCache<>(4, storage);
        cache.get("a");
        cache.get("b");
        cache.get("c");
        cache.get("d");
        cache.get("c");

        for (String key : cache.getKeys()) {
            System.out.println(key);
        }
    }
}
