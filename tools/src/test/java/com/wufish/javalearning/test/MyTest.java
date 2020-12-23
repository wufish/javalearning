package com.wufish.javalearning.test;

import java.util.HashMap;
import java.util.List;

public class MyTest {
    final int a = 100;

    public MyTest() {
        System.out.println(a);
    }

    class Allocator {
        private List<Object> als;
        // 一次性申请所有资源
        synchronized void apply(
                Object from, Object to){
            // 经典写法
            while(als.contains(from) ||
                    als.contains(to)){
                try{
                    wait();
                }catch(Exception e){
                }
            }
            als.add(from);
            als.add(to);
        }
        // 归还资源
        synchronized void free(
                Object from, Object to){
            als.remove(from);
            als.remove(to);
            notifyAll();
        }
    }

    public static void main(String[] args) {
        MyTest test = new MyTest();
        HashMap<Integer, Integer> hashMap = new HashMap<>(2);
        hashMap.put(3, 3);
        hashMap.put(7, 7);
        new Thread(new Runnable() {
            @Override
            public void run() {
                hashMap.put(5, 5);
            }
        }).start();
        int i = Integer.highestOneBit(3);
        Node[] nodes = new Node[10];
        Node e = nodes[0] = new Node();
        nodes[0] = null;
        System.out.println(e.value);
        System.out.println();
    }

   /* int a;
    volatile int v1 = 1;
    volatile int v2 = 2;

    void readAndWrite() {
        int i = v1;// 第一个volatile读
        int j = v2;// 第二个volatile读
        a = i + j;// 普通写
        v1 = i + 1;// 第一个volatile写
        v2 = j + 1;// 第二个volatile写
    }*/

    static class Node {
        int value;
    }
}
