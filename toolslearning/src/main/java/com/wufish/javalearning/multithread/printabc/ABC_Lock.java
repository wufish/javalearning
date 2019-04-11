package com.wufish.javalearning.multithread.printabc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author wzj
 * @Create time: 2018/06/10 17:09
 * @Description:
 */
public class ABC_Lock {
    private static Lock lock = new ReentrantLock();
    private static int count = 0;

    static class ThreadA extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; ) {
                try {
                    lock.lock();
                    // 多线程并发，不能用if，必须用循环测试等待条件，避免虚假唤醒
                    while (count % 3 == 0) {
                        System.out.print("A");
                        count++;
                        // 成功打印后i++
                        i++;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    static class ThreadB extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; ) {
                try {
                    lock.lock();
                    // 多线程并发，不能用if，必须用循环测试等待条件，避免虚假唤醒
                    while (count % 3 == 1) {
                        System.out.print("B");
                        count++;
                        // 成功打印后i++
                        i++;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    static class ThreadC extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; ) {
                try {
                    lock.lock();
                    // 多线程并发，不能用if，必须用循环测试等待条件，避免虚假唤醒
                    while (count % 3 == 2) {
                        System.out.print("C");
                        count++;
                        // 成功打印后i++
                        i++;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        new ThreadA().start();
        new ThreadB().start();
        new ThreadC().start();
    }

}
