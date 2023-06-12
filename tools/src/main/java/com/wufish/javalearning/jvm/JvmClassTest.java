package com.wufish.javalearning.jvm;

/**
 * Created on 2023-06-12
 */
public class JvmClassTest extends JvmClassAnalyze implements JvmClassPrint {
    private static final int child = 99;

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
        tester.printJvm();
        System.out.println();
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

    @Override
    public void printJvm() {
        System.out.println("print jvm");
    }
}
