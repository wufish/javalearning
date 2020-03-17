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
 * put 操作：如果key存在，则删除节点，然后插入尾部；如果key不存在，则判定是否满了，如果未满，则插入尾部，否则删除第一个节点，再插入尾部
 * get 操作：如果key存在，删除节点，插入尾部；否则返回null
 */
public class LRUCache<K> {
    private int capacity;
    private Map<K, LRUNode> map = new HashMap<>();
    private LRUNode head = new LRUNode(null, null);
    private LRUNode tail = new LRUNode(null, null);

    /**
     * Instantiates a new Lru cache.
     *
     * @param capacity the capacity
     */
    public LRUCache(int capacity) {
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
        if (map.containsKey(key)) {
            remove(map.get(key));
        }
        if (map.size() >= capacity) {
            remove(head.next);
        }
        insert(new LRUNode(key, value));
    }

    /**
     * Get.
     *
     * @param key the key
     */
    public Object get(K key) {
        if (map.containsKey(key)) {
            LRUNode node = map.get(key);
            remove(node);
            insert(node);
            return node.value;
        } else {
            return null;
        }
    }

    /**
     * Remove.
     *
     * @param node the node
     */
    private void remove(LRUNode node) {
        map.remove(node.key);
        node.pre.next = node.next;
        node.next.pre = node.pre;
    }

    /**
     * Insert.
     *
     * @param node the node
     */
    private void insert(LRUNode node) {
        map.put(node.key, node);
        node.pre = tail.pre;
        tail.pre = node;
        tail.pre.next = node;
        node.pre.next = node;
        node.next = tail;
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
