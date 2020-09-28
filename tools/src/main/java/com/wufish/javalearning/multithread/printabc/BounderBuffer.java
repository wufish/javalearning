package com.wufish.javalearning.multithread.printabc;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author 58
 * @Create time: 2020/6/21 21:26
 * @Description:
 */
public class BounderBuffer {
    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEnpty = lock.newCondition();
    final Object[] items = new Object[100];
    int putptr, takeptr, count;

    private static final Object objLock = new Object();

    public static void main(String[] args) {
        /*try {
            objLock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
       /* CyclicBarrier cyclicBarrier = new CyclicBarrier(10, () -> {
            System.out.println("generation end");
        });*/
        /*for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        System.out.println("current await:" + Thread.currentThread().getName());
                        cyclicBarrier.await();
                        System.out.println("current signal:" + Thread.currentThread().getName());
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }*/

        /*try {
            Thread.sleep(10000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        /*Semaphore semaphore = new Semaphore(10);
        new Thread(() -> {
            while (true) {
                try {
                    System.out.println("current acquire:" + Thread.currentThread().getName());
                    if (!semaphore.tryAcquire(100, TimeUnit.MILLISECONDS)) {
                        System.out.println("current acquired:" + Thread.currentThread().getName());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true) {
            semaphore.release();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        Thread.currentThread().interrupt();
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < countDownLatch.getCount(); i++) {
            new Thread(new CLThread(countDownLatch), "player" + i).start();
        }
        System.out.println("等待所有人准备");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("游戏开始");

        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        for (int i = 0; i < cyclicBarrier.getParties(); i++) {
            new Thread(new CycThread(cyclicBarrier), "队友" + i).start();
        }
        System.out.println("main function is finished.");
    }

    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                notFull.await();
            }
            items[putptr] = x;
            if (++putptr == items.length) putptr = 0;
            count++;
            notEnpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEnpty.await();
            }
            Object x = items[takeptr];
            if (++takeptr == items.length) takeptr = 0;
            count--;
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }

    private static class CLThread implements Runnable {
        private CountDownLatch countDownLatch;

        public CLThread(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            int i = ThreadLocalRandom.current().nextInt(1000, 3001);
            try {
                TimeUnit.MILLISECONDS.sleep(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " 已经准备好了, 所使用的时间为 " +
                    ((double) i / 1000) + "s");
            countDownLatch.countDown();
        }
    }

    private static class CycThread implements Runnable {
        private CyclicBarrier cyclicBarrier;

        public CycThread(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            // 4轮障碍物
            for (int i = 0; i < 4; i++) {
                int randomNum = ThreadLocalRandom.current().nextInt(1000, 3001);
                try {
                    TimeUnit.MILLISECONDS.sleep(randomNum);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " 已经通过了障碍物" + i
                        + ", 所使用的时间为 " +
                        ((double) randomNum / 1000) + "s");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
