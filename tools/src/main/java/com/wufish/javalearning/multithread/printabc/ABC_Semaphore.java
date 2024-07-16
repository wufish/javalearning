package com.wufish.javalearning.multithread.printabc;

import java.util.concurrent.Semaphore;

/**
 * @Author wzj
 * @Create time: 2018/06/10 17:36
 * @Description:Semaphore是用来保护一个或者多个共享资源的访问，Semaphore内部维护了一个计数器，其值为可以访问的共享资源的个数。 一个线程要访问共享资源，先获得信号量，如果信号量的计数器值大于1，意味着有共享资源可以访问，则使其计数器值减去1，再访问共享资源。
 * 如果计数器值为0,线程进入休眠。当某个线程使用完共享资源后，释放信号量，并将信号量内部的计数器加1，之前进入休眠的线程将被唤醒并再次试图获得信号量。
 */
public class ABC_Semaphore {
    // 以A开始的信号量,初始信号量数量为1
    private static Semaphore semaphoreA = new Semaphore(1);
    // B、C信号量,A完成后开始,初始信号数量为0
    private static Semaphore semaphoreB = new Semaphore(0);
    private static Semaphore semaphoreC = new Semaphore(0);

    private static void print(int flag, Semaphore current, Semaphore next) {
        try {
            for (int i = 0; i < 10; i++) {
                current.acquire();
                printFlag(flag);
                next.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void printFlag(int flag) {
        String str = flag == 0 ? "A" : flag == 1 ? "B" : "C";
        System.out.println(str);
    }

    public static void main(String[] args) {
        new Thread(() -> print(0, semaphoreA, semaphoreB)).start();
        new Thread(() -> print(1, semaphoreB, semaphoreC)).start();
        new Thread(() -> print(2, semaphoreC, semaphoreA)).start();
    }
}
