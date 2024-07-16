package com.wufish.javalearning.multithread.printabc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author wzj
 * @Create time: 2018/06/10 17:30
 * @Description:
 */
public class ABC_Condition {
    private static Lock lock = new ReentrantLock();
    private static Condition conditionA = lock.newCondition();
    private static Condition conditionB = lock.newCondition();
    private static Condition conditionC = lock.newCondition();
    private static int count = 0;

    private static void print(int flag, Condition current, Condition next) {
        try {
            lock.lock();
            for (int i = 0; i < 10; i++) {
                while (count % 3 != flag) {
                    current.await();
                }
                printFlag(flag);
                count++;
                next.signal();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private static void printFlag(int flag) {
        String str = flag == 0 ? "A" : flag == 1 ? "B" : "C";
        System.out.println(str);
    }

    public static void main(String[] args) {
        new Thread(() -> print(0, conditionA, conditionB)).start();
        new Thread(() -> print(1, conditionB, conditionC)).start();
        new Thread(() -> print(2, conditionC, conditionA)).start();
    }
}
