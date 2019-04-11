package com.wufish.javalearning.multithread.printabc;

/**
 * @Author wzj
 * @Create time: 2018/06/10 16:48
 * @Description:建立三个线程A、B、C，A线程打印10次字母A，B线程打印10次字母B,C线程打印10次字母C， 但是要求三个线程同时运行，并且实现交替打印，即按照ABCABCABC的顺序打印。
 */
public class ABC_Synch {
    private static class ThreadPrinter implements Runnable {
        private String name;
        private Object prev;
        private Object self;

        public ThreadPrinter(String name, Object prev, Object self) {
            this.name = name;
            this.prev = prev;
            this.self = self;
        }

        public void run() {
            int count = 10;
            // 多线程并发不能用if，必须使用while循环
            while (count > 0) {
                // 获取pre锁
                synchronized (prev) {
                    // 获取self锁
                    synchronized (self) {
                        System.out.print(name);
                        count--;
                        // 唤醒其他线程竞争self锁
                        self.notifyAll();
                    }
                    try {
                        if (count == 0) {
                            // 如果count==0,表示这是最后一次打印操作，通过notifyAll操作释放对象锁。
                            prev.notifyAll();
                        } else {
                            // 立即释放prev锁，当前线程休眠，等待唤醒
                            prev.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();
        ThreadPrinter pa = new ThreadPrinter("A", c, a);
        ThreadPrinter pb = new ThreadPrinter("B", a, b);
        ThreadPrinter pc = new ThreadPrinter("C", b, c);
        new Thread(pa).start();
        // 保证初始ABC的启动顺序
        Thread.sleep(10);
        new Thread(pb).start();
        Thread.sleep(10);
        new Thread(pc).start();
        Thread.sleep(10);
    }
}
