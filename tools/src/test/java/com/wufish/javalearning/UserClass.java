package com.wufish.javalearning;

/**
 * Created on 2020-12-05
 */
public class UserClass extends BaseClass<String, Long> {
    private int c = 3;
    private int d = 4;

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }
}
