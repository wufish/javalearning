package com.wufish.javalearning.cache;

import com.sun.org.apache.xpath.internal.operations.String;

import java.util.*;

/**
 * @Author wzj@58ganji.com
 * @Create time: 2020/7/4 0004
 * @Description:
 */
public class LRU {
    public static void main(String[] args) {
        LRU cache = new LRU(2 /* 缓存容量 */);

        cache.put(1, 1);
        cache.put(2, 2);
        int i = cache.get(1);// 返回  1
        cache.put(3, 3);    // 该操作会使得关键字 2 作废
        int i1 = cache.get(2);// 返回 -1 (未找到)
        cache.put(4, 4);    // 该操作会使得关键字 1 作废
        int i2 = cache.get(1);// 返回 -1 (未找到)
        int i3 = cache.get(3);// 返回  3
        int i4 = cache.get(4);// 返回  4
        System.out.println();
    }

    class Node {
        private int key;
        private int val;
        private Node pre;
        private Node next;

        public Node(int key, int val) {
            this.key = key;
            this.val = val;
        }
    }

    private int capacity;
    private Map<Integer, Node> map = new HashMap<>();
    private Node head = new Node(-1, -1);
    private Node tail = new Node(-1, -1);

    public LRU(int capacity) {
        this.capacity = capacity;
        head.next = tail;
        tail.pre = head;
    }

    public int get(int key) {
        if (map.containsKey(key)) {
            int val = map.get(key).val;
            put(key, val);
            return val;
        } else {
            return -1;
        }
    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {
            Node tmpNode = map.get(key);
            // 移除节点
            remove(tmpNode);
            // 插入头
            insert(tmpNode);
            tmpNode.val = value;
        } else {
            // 判断是否满
            if (map.size() >= capacity) {
                // 移除尾节点
                remove(tail.pre);
            }
            Node nnode = new Node(key, value);
            // 插入头
            insert(nnode);
        }
    }

    private void insert(Node node) {
        Node hn = head.next;
        head.next = node;
        node.next = hn;
        hn.pre = node;
        node.pre = head;
        map.put(node.key, node);
    }

    private void remove(Node node) {
        node.pre.next = node.next;
        node.next.pre = node.pre;
        map.remove(node.key);
    }
}
