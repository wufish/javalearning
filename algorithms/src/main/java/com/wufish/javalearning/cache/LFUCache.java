package com.wufish.javalearning.cache;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Lfu cache.
 *
 * @param <K> the type parameter
 * @Author wufish
 * @Create time : 2020-03-17 18:24
 * @Description: put 操作： get 操作： evict操作：remove
 */
public class LFUCache<K> {
    private int capacity;
    private ConcurrentHashMap<K, LFUNode> cache;

    /**
     * Instantiates a new Lfu cache.
     *
     * @param capacity the capacity
     */
    public LFUCache(int capacity) {
        this.capacity = capacity;
        cache = new ConcurrentHashMap<>(capacity);
    }

    /**
     * Put.
     *
     * @param key   the key
     * @param value the value
     */
    public void put(K key, Object value) {
        LFUNode v = cache.get(key);
        if (v == null) {
            if (cache.size() >= capacity) {
                evictKey();
            }
            v = new LFUNode(key, value);
            cache.put(key, v);
        } else {
            v.hitcount++;
            v.value = value;
            v.lasttime = System.currentTimeMillis();
        }
    }

    /**
     * Get object.
     *
     * @param key the key
     * @return the object
     */
    public Object get(K key) {
        LFUNode v = cache.get(key);
        if (v != null) {
            v.hitcount++;
            v.lasttime = System.currentTimeMillis();
            return v.value;
        }
        return null;
    }

    /**
     * Remove object.
     *
     * @param key the key
     * @return the object
     */
    public Object remove(K key) {
        LFUNode v = cache.remove(key);
        if (v != null) {
            return v.value;
        }
        return null;
    }

    private Object evictKey() {
        LFUNode min = Collections.min(cache.values());
        if (min != null) {
            return remove(min.key);
        }
        return null;
    }

    /**
     * The type Lfu node.
     */
    class LFUNode implements Comparable<LFUNode> {
        private K key;
        private Object value;
        private int hitcount;
        private long lasttime;

        /**
         * Instantiates a new Lfu node.
         *
         * @param key   the key
         * @param value the value
         */
        public LFUNode(K key, Object value) {
            this.key = key;
            this.value = value;
            hitcount = 1;
            lasttime = System.currentTimeMillis();
        }

        @Override
        public int compareTo(LFUNode o) {
            int compare = Integer.compare(this.hitcount, o.hitcount);
            if (compare == 0) {
                return Long.compare(this.lasttime, o.lasttime);
            } else {
                return compare;
            }
        }
    }
}
