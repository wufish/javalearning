package com.wufish.javalearning.swap;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Lru cache.
 *
 * @param <K> the type parameter
 * @Author 58
 * @Create time : 2020-03-17 20:00
 * @Description: desc \r\n
 * <p>
 * put 操作：如果key存在，则将节点移到尾部；如果key不存在，则判定是否满了，如果未满，则插入尾部，否则删除第一个节点，再插入尾部
 * get 操作：如果key存在，将节点移到尾部；否则返回null
 */
public class LRUCacheV2<K> {
    private int capacity;
    private Map<K, LRUNode> map = new HashMap<>();
    private LRUNode head = new LRUNode(null, null);
    private LRUNode tail = new LRUNode(null, null);

    /**
     * Instantiates a new Lru cache.
     *
     * @param capacity the capacity
     */
    public LRUCacheV2(int capacity) {
        this.capacity = capacity;
        head.next = tail;
        tail.pre = head;
    }

    /**
     * Put.
     *
     * @param key   the key
     * @param value the value
     */
    public void put(K key, Object value) {
        LRUNode node = map.get(key);
        if (node != null) {
            node.value = value;
            if (node.next == tail) {
                // 刚访问，已经在尾部
                return;
            }
            // 断开当前连接
            node.pre.next = node.next;
            node.next.pre = node.pre;
            moveToTail(node);
        } else {
            node = new LRUNode(key, value);
            map.put(key, node);
            moveToTail(node);
        }
    }

    /**
     * Get.
     *
     * @param key the key
     */
    public Object get(K key) {
        LRUNode node = map.get(key);
        if (node != null) {
            if (node.next == tail) {
                // 刚访问，已经在尾部
                return node.value;
            }
            // 断开当前连接
            node.pre.next = node.next;
            node.next.pre = node.pre;
            moveToTail(node);
            return node.value;
        }
        return null;
    }

    private void moveToTail(LRUNode node) {
        // 插入尾部
        tail.pre.next = node;
        node.next = tail;
        node.pre = tail.pre;
        tail.pre = node;
    }


    /**
     * The type Lru node.
     */
    class LRUNode {
        private K key;
        private Object value;
        private LRUNode pre;
        private LRUNode next;

        /**
         * Instantiates a new Lru node.
         *
         * @param key   the key
         * @param value the value
         */
        public LRUNode(K key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
}
