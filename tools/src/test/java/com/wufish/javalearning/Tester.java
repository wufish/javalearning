package com.wufish.javalearning;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.IntStream;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Before;
import org.junit.Test;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import sun.misc.Unsafe;

/**
 * The type Tester.
 *
 * @Author wzj
 * @Create time : 2019/04/19 16:19
 * @Description:
 */
public class Tester {
    private static sun.misc.Unsafe UNSAFE;
    private static long parkBlockerOffset;
    private static long SEED;
    private static long PROBE;
    private static long SECONDARY;
    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");

    private static void park(Object blocker) {
        Thread t = Thread.currentThread();
        setBlocker(t, blocker);
        UNSAFE.park(false, 0L);
        setBlocker(t, null);
    }

    private static void unpark(Thread thread) {
        if (thread != null)
            UNSAFE.unpark(thread);
    }

    private static void setBlocker(Thread t, Object arg) {
        // Even though volatile, hotspot doesn't need a write barrier here.
        UNSAFE.putObject(t, parkBlockerOffset, arg);
    }

    @Before
    public void init() throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        UNSAFE = (Unsafe) theUnsafe.get(null);
        try {
            Class<?> tk = Thread.class;
            parkBlockerOffset = UNSAFE.objectFieldOffset
                    (tk.getDeclaredField("parkBlocker"));
            SEED = UNSAFE.objectFieldOffset
                    (tk.getDeclaredField("threadLocalRandomSeed"));
            PROBE = UNSAFE.objectFieldOffset
                    (tk.getDeclaredField("threadLocalRandomProbe"));
            SECONDARY = UNSAFE.objectFieldOffset
                    (tk.getDeclaredField("threadLocalRandomSecondarySeed"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<?> submit = service.submit(() -> {

        });
        Futures.addCallback(submit, new FutureCallback<Object>() {
            @Override
            public void onSuccess(@Nullable Object o) {

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        }, Executors.newFixedThreadPool(2));
    }

    /**
     * Test.
     * {@literal}
     */
    @Test
    public void test() throws NoSuchFieldException, IllegalAccessException {
       /* UserClass userClass = new UserClass();
        userClass.print();
        ClassUtils.getFieldsValue(userClass);*/
        IntStream.range(0, 10).forEach(x -> System.out.println(x));
        String format = LocalDateTime.now()
                .plusMinutes(30).withMinute(0).withSecond(0)
                .atZone(ZoneId.systemDefault())
                .format(DEFAULT_DATE_TIME_FORMATTER);
        System.out.println();
    }

    @Test
    public void parkTest() throws InterruptedException {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                System.out.println("out : " + i);
                LockSupport.park();
            }
        });
        thread.start();
        Thread.sleep(2000);

        System.out.println("park start");

        while (true) {
            LockSupport.unpark(thread);
        }
        //System.out.println("end");
    }
}
