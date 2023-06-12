package com.wufish.javalearning.jvm;

/**
 * @author wuzhijun05 <wuzhijun05@kuaishou.com>
 * Created on 2023-06-12
 */
public class JvmClassAnalyze {
    protected static final int parent = 1000;

    public <T> void testType(T t) {
        System.out.println("Hello, type");
    }

    public void sayHello(Human human) {
        System.out.println("Hello, human");
    }

    public void sayHello(Man man) {
        System.out.println("Hello, man");
    }

    public void sayHello(Woman man) {
        System.out.println("Hello, woman");
    }

    interface Human {
        default void sayHello() {
            System.out.println("Hello, human");
        }
    }

    static class Man implements Human {
        @Override
        public void sayHello() {
            System.out.println("Hello, man");
        }
    }

    static class Woman implements Human {
        @Override
        public void sayHello() {
            System.out.println("Hello, woman");
        }
    }
}
