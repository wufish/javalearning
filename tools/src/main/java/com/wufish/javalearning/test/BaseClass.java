package com.wufish.javalearning.test;

/**
 * Created on 2020-12-05
 */
public abstract class BaseClass<K, V> {
    private int a = 1;
    private int b = 2;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public void print() {
        Class<Object> baseGeneric = ClassUtils.getBaseGeneric(getClass(), 0);
        System.out.println(baseGeneric.getSimpleName());
    }
}
