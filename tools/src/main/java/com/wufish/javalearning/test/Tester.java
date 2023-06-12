package com.wufish.javalearning.test;

import static java.util.stream.Collectors.joining;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
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
    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
    private static sun.misc.Unsafe UNSAFE;
    private static long parkBlockerOffset;
    private static long SEED;
    private static long PROBE;
    private static long SECONDARY;

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
        //IntStream.range(0, 10).forEach(x -> System.out.println(x));
        String format = LocalDateTime.now()
                .plusMinutes(30).withMinute(0).withSecond(0)
                .atZone(ZoneId.systemDefault())
                .format(DEFAULT_DATE_TIME_FORMATTER);
        String collect = ThreadLocalRandom.current().ints(10, 0, 10)
                .mapToObj(String::valueOf).collect(joining(","));
        System.out.println(collect);
        ArrayList<Integer> list1 = Lists.newArrayList(1, 2, 3, 4, 5, 6);
        List<Integer> list2 = list1;
        for (int i = 0; i < list2.size(); i++) {
            if (i == 3) {
                list1 = new ArrayList<>();
            }
            System.out.println(list2.get(i));
        }
        System.out.println(list2);

        String format1 = LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.BASIC_ISO_DATE);
        String format2 = LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE);
        int days = Period.between(LocalDate.parse("2020-12-22", DateTimeFormatter.ISO_LOCAL_DATE), LocalDate.now()).getDays();
        long between = ChronoUnit.DAYS.between(LocalDate.parse("2020-12-22", DateTimeFormatter.ISO_LOCAL_DATE), LocalDate.now());
        String name = StandardCharsets.UTF_8.name();
        System.out.println();
    }

    @Test
    public void parkTest() throws InterruptedException {
        /*Thread thread = new Thread(() -> {
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
        }*/
        CompletableFuture[] futures = IntStream.range(0, 10)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                            System.out.println(i + "start");
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println(i + "end");
                        }).thenAccept(v -> {
                            System.out.println(i + "accept");
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        })
                ).toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
        //System.out.println("end");
    }
}
