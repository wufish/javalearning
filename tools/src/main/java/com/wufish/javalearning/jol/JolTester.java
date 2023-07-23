package com.wufish.javalearning.jol;

import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;

/**
 * @author wuzhijun05 <wuzhijun05@kuaishou.com>
 * Created on 2023-07-23
 */
public class JolTester {
    @Test
    public void testBiasLock() throws InterruptedException {
        // 这里不能和5s后共用一个对象，4s前是无锁不可偏向
        final Object obj = new Object();
        // jvm开始前4s，对象是无锁，不可偏向的，001
        System.out.println("刚开始对象布局：\n" + ClassLayout.parseInstance(obj).toPrintable());
        // mark work: 0x0000000000000001

        //JKD8延迟4S开启偏向锁, 通过-XX:BiasedLockingStartupDelay=0 取消
        Thread.sleep(5000);

        final Object monitor = new Object();
        // 延迟4s后，新对象变成可偏向的，延迟4s后，对象变成无锁可偏向的, 101
        System.out.println("延迟5秒后对象布局-无锁可偏向：\n" + ClassLayout.parseInstance(monitor).toPrintable());
        // mark work: 0x0000000000000005，延迟4s后，对象变成可偏向的

        // 偏向锁，mark word 记录线程id
        synchronized (monitor) {
            System.out.println("对象加锁后的布局-偏向锁：\n" + ClassLayout.parseInstance(monitor).toPrintable());
            // mark work: 0x00007f7994809805
        }
        // 偏向锁释放后，mark word 不变
        System.out.println("对象释放锁后的布局-偏向锁：\n" + ClassLayout.parseInstance(monitor).toPrintable());
        // mark work: 0x00007f7994809805

        // 执行hashCode()方法后，锁对象撤销了偏向锁，变为了无锁不可偏向状态
        System.out.println(monitor.hashCode());
        System.out.println("执行hash后的对象布局-无锁不可偏向:\n" + ClassLayout.parseInstance(monitor).toPrintable());
        // mark work: 0x00000076b1e9b801，末尾001
    }

    @Test
    public void testThinLock() throws InterruptedException {
        //JKD8延迟4S开启偏向锁, 通过-XX:BiasedLockingStartupDelay=0 取消
        Thread.sleep(5000);

        final Object monitor = new Object();
        System.out.println("延迟5秒后对象布局-无锁可偏向：\n" + ClassLayout.parseInstance(monitor).toPrintable());
        // mark work: 0x0000000000000005，延迟4s后，对象变成可偏向的

        synchronized (monitor) {
            System.out.println("对象加锁后的布局-偏向锁：\n" + ClassLayout.parseInstance(monitor).toPrintable());
            // mark work: 0x00007fe96f80d8050x00007fe96f80d805，末尾101，偏向锁
        }

        Thread t1 = new Thread(() -> {
            synchronized (monitor) {
                System.out.println("轻量级锁：\n" + ClassLayout.parseInstance(monitor).toPrintable());
                // mark work: 0x0000700010f07908，末尾00，轻量级锁
            }
        });
        t1.start();
        t1.join();

        System.out.println("锁释放：\n" + ClassLayout.parseInstance(monitor).toPrintable());
        // mark work: 0x0000000000000001，无锁不可偏向，后面再获取锁就是轻量级锁开始了
    }

    @Test
    public void testFatLock() throws InterruptedException {
        //JKD8延迟4S开启偏向锁, 通过-XX:BiasedLockingStartupDelay=0 取消
        Thread.sleep(5000);

        final Object monitor = new Object();
        System.out.println("延迟5秒后对象布局-无锁可偏向：\n" + ClassLayout.parseInstance(monitor).toPrintable());
        // mark work: 0x0000000000000005，延迟4s后，对象变成可偏向的

        Thread t1 = new Thread(() -> {
            synchronized (monitor) {
                System.out.println("t1-重量级锁：" + ClassLayout.parseInstance(monitor).toPrintable());
                // mark work: 0x00007fdb1c22cbea，重量级锁
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (monitor) {
                System.out.println("t2-重量级锁：" + ClassLayout.parseInstance(monitor).toPrintable());
                // mark work: 0x00007fdb1c22cbea，重量级锁
            }
        });
        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println("锁释放后" + ClassLayout.parseInstance(monitor).toPrintable());
        // mark work: 0x00007fdb1c22cbea，重量级锁, 所释放后mark word 不变
    }

    @Test
    public void testUpgradeLock() throws InterruptedException {
        Thread.sleep(5000);

        final Object monitor = new Object();
        // 延迟4s后，新对象变成可偏向的，延迟4s后，对象变成无锁可偏向的, 101
        System.out.println("延迟5秒后对象布局-无锁可偏向：\n" + ClassLayout.parseInstance(monitor).toPrintable());
        // mark work: 0x0000000000000005，延迟4s后，对象变成可偏向的

        synchronized (monitor) {
            System.out.println("偏向锁：\n" + ClassLayout.parseInstance(monitor).toPrintable());
            // mark work: 0x00007fe96f80d8050x00007fe96f80d805，末尾101，偏向锁
        }

        Thread t1 = new Thread(() -> {
            synchronized (monitor) {
                System.out.println("sss：" + ClassLayout.parseInstance(monitor).toPrintable());
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (monitor) {
                System.out.println("t2-重量级锁：" + ClassLayout.parseInstance(monitor).toPrintable());
                // mark work: 0x00007fdb1c22cbea，重量级锁
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        // 偏向锁释放后，mark word 不变
        for (int i = 0; i < 10; i++) {
            System.out.println("xxx" + i + "：\n" + ClassLayout.parseInstance(monitor).toPrintable());
        }
        System.out.println("t2：\n" + ClassLayout.parseInstance(monitor).toPrintable());
        // mark work: 0x00007f7994809805

        Thread.sleep(2000);
        System.out.println("t2：\n" + ClassLayout.parseInstance(monitor).toPrintable());
    }
}
