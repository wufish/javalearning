package com.wufish.javalearning.multithread.threadlocal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuzhijun05 <wuzhijun05@kuaishou.com>
 * Created on 2023-03-14
 */
public class ThreadLocalTest {
    private static final List<Long> LIST_VALUE = new ArrayList<>();
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            final long fi = i;
            new Thread(() -> runTest(fi)).start();
        }
        Thread.sleep(10000);
        System.out.println(LIST_VALUE.size());
    }

    private static void runTest(long i) {
        ThreadLocal<List<Long>> tl = ThreadLocal.withInitial(() -> LIST_VALUE);
        List<Long> val = tl.get();
        System.out.println(i + "-before-" + val.size());
        val.add(i);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(i + "-after-" + val.size());
    }
}
