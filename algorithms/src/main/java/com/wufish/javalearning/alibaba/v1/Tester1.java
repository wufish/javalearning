package com.wufish.javalearning.alibaba.v1;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * // 1. 单例队列容器消费
 * // 实现一个单例队列容器，提供三个方法 offer，poll，size
 * // 写三个线程，
 * // 线程1将字符串a每隔1s挨个添加到容器中，
 * // 线程2将字符串b每隔1s挨个添加到容器中，
 * // 线程1与线程2交替执行
 * // 线程3监听容器变化，调用poll每隔1s挨个输出
 * // 示例：
 * // 输入：a = "hloaiaa", b = "el,lbb"
 * // 输出：hello,alibaba
 */
public class Tester1 {
    public static void main(String[] args) {
        SingletonQueue singletonQueue = new SingletonQueue();
        singletonQueue.offer("hloaiaa", "el,lbb");
        // 第三个线程 阻塞获取队列的字符
        Thread threadC = new Thread(() -> {
            Character ch = null;
            while ((ch = singletonQueue.poll()) != null) {
                // 打印字符
                System.out.println(ch);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadC.start();
        try {
            threadC.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private static class SingletonQueue {
        private static Lock lock = new ReentrantLock();
        private static Condition conditionA = lock.newCondition();
        private static Condition conditionB = lock.newCondition();
        private static LinkedBlockingQueue<Character> queue = new LinkedBlockingQueue<>();
        private int size = 0;
        private String a;
        private String b;
        private SingletonQueue.ThreadA threadA = new SingletonQueue.ThreadA();
        private SingletonQueue.ThreadB threadB = new SingletonQueue.ThreadB();

        public SingletonQueue() {
        }

        /**
         * 入队字符
         *
         * @param a
         * @param b
         */
        public void offer(String a, String b) {
            this.a = a;
            this.b = b;
            threadA.start();
            threadB.start();
        }

        /**
         * 取单个字符
         *
         * @return
         */
        public Character poll() {
            try {
                Character take = queue.poll(2, TimeUnit.SECONDS);
                return take;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 返回队列长度
         *
         * @return
         */
        public int size() {
            return queue.size();
        }

        class ThreadA extends Thread {
            @Override
            public void run() {
                if (StringUtils.isNotBlank(a)) {
                    try {
                        // 线程 A 获取锁
                        lock.lock();
                        for (int i = 0; i < a.length(); i++) {
                            // 遍历字符串 a，如果当前 size 是偶数（保证顺序，a 优先），
                            // 则a 的字符入队，否则阻塞直到 b 入完队列
                            if (size % 2 != 0) {
                                conditionA.await();
                            }
                            // 入队
                            queue.offer(a.charAt(i));
                            size++;
                            // 唤醒 B
                            conditionB.signal();
                            // sleep 1s
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }

        class ThreadB extends Thread {
            @Override
            public void run() {
                if (StringUtils.isNotBlank(b)) {
                    try {
                        lock.lock();
                        // 遍历字符串 b，如果当前 size 是奇数（保证顺序），
                        // 则b 的字符入队，否则阻塞直到 a 入完队列
                        for (int i = 0; i < b.length(); i++) {
                            if (size % 2 != 1) {
                                conditionB.await();
                            }
                            queue.offer(b.charAt(i));
                            size++;
                            // 唤醒 A
                            conditionA.signal();
                            // sleep 1s
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }
    }
}
