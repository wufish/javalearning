package com.wufish.javalearning.jvm;

/**
 * Created on 2023-06-12
 */
public class JvmClassTest {
    public static void main(String[] args) {
        Human man = new Man();
        Human woman = new Woman();
        // 方法重载，静态分派
        JvmClassTest tester = new JvmClassTest();
        tester.sayHello(man); // Hello, human
        // 方法覆写，动态分派
        man.sayHello(); // Hello, man
        woman.sayHello(); // Hello, woman
        //
        int res = tester.func(1, 2);
        System.out.println(res);
    }

    public int func(int x, int y) {
        String str = "print";
        str += " private test";
        System.out.println(str);
        int z = x + y;
        int a = 2;
        int b = 4;
        return z + a + b;
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
