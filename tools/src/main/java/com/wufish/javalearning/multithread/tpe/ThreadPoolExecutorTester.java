package com.wufish.javalearning.multithread.tpe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wuzhijun05 <wuzhijun05@kuaishou.com>
 * Created on 2023-08-14
 */
public class ThreadPoolExecutorTester {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            executorService.execute(() -> {
                System.out.println(finalI + ":" + Thread.currentThread().getName());
            });
        }
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            executorService.execute(() -> {
                // 如果task 运行了就不会被中断
                // 线程池只会在从任务队列获取task的时候才会相应中断，或者task内部抛出异常
                while (true) {
                    System.out.println(finalI + "f:" + Thread.currentThread().getName());
                }
            });
        }
        executorService.shutdownNow();
        System.out.println(executorService.isShutdown());
        System.out.println(executorService.isTerminated());
        System.out.println();
    }
}
