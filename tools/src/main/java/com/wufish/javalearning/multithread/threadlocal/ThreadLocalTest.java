package com.wufish.javalearning.multithread.threadlocal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuzhijun05 <wuzhijun05@kuaishou.com>
 * Created on 2023-03-14
 */
public class ThreadLocalTest {
    private static final List<Long> LIST_VALUE = new ArrayList<Long>();
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            final long fi = i;
            new Thread(() -> runTest(fi)).start();
        }
        Thread.sleep(10000);
        System.out.println(LIST_VALUE.size());
    }

    private static void runTest(long i) {
        ThreadLocal<List<Long>> tl = ThreadLocal.withInitial(() -> LIST_VALUE);
        List<Long> val = tl.get();
        val.add(i);
        System.out.println(val.size());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
