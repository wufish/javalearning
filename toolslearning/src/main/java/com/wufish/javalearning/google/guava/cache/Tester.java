package com.wufish.javalearning.google.guava.cache;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author 58
 * @Create time: 2020/7/1 17:17
 * @Description:
 */
public class Tester {
    // # 笔试题目

// 1. 单例队列容器消费
// 实现一个单例队列容器，提供三个方法 offer，poll，size
// 写三个线程，
// 线程1将字符串a每隔1s挨个添加到容器中，
// 线程2将字符串b每隔1s挨个添加到容器中，
// 线程1与线程2交替执行
// 线程3监听容器变化，调用poll每隔1s挨个输出
// 示例：
// 输入：a = "hloaiaa", b = "el,lbb"
// 输出：hello,alibaba

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
        private ThreadA threadA = new ThreadA();
        private ThreadB threadB = new ThreadB();

        public SingletonQueue() {
        }

        /**
         * 入队字符
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

// 2.单词统计
// 给定一个字符串，计算字符串中每个单词出现的数量，并排序输出。
// a. 单词一个大写字母开始，接着跟随0个或任意个小写字母；
// b. 如果单词数量大于 1，单词后会跟着数字表示单词的数量。如果数量等于 1 则不会跟数字。例如，Hello2World 和 是合法，但 Hello1World2 这个表达是不合法的；
// c. Hello2表示HelloHello；
// d. (Hello2World2)3 可以等于Hello2World2Hello2World2Hello2World2；
// 输出格式为：第一个（按字典序）单词，跟着它的数量（如果单词数量为1，则不输出），然后是第二个单词的名字（按字典序），跟着它的数量（如果单词数量为1，则不输出），以此类推。
// 示例1：
// 输入：字符串 = "World3Hello"
// 输出: "HelloWorld3"
// 解释: 单词数量是 {'Hello': 1, 'World': 3}。
// 示例 2:
// 输入: 字符串 = "Welcome4(ToAlibaba(To3)2)2"
// 输出: "Alibaba2To14Welcome4"
// 解释: 单词数量是 {'Alibaba': 2,  'To': 14', Welcome': 4,}。
// 注意:
// 字符串的长度在[1, 100000]之间。
// 字符串只包含字母、数字和圆括号，并且题目中给定的都是合法的字符串。

    // 3. 题目：贴墙纸
// 你是一位装修工，根据设计师的要求给客户的客厅背景墙贴墙纸。
// 假设背景墙面积为 n x m，装修风格为现代极简风格，需要使用尽可能少的 不同颜色的 正方形 墙纸包 来铺满墙面。
// 假设正方形墙纸包块的规格不限，边长都是整数。
// 请你帮设计师计算一下，最少需要用到多少块方形墙纸包？
// 示例 1：
// 输入：n = 2, m = 3
// 输出：3
// 解释：3 块墙纸包就可以铺满墙面。
// 2 块 1x1 墙纸包
// 1 块 2x2 墙纸包
// 示例 2：
// 输入：n = 5, m = 8
// 输出：5
// 示例 3：
// 输入：n = 11, m = 13
// 输出：6
// 提示：
// 1 <= n <= 13
// 1 <= m <= 13

}

