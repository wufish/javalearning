package com.wufish.javalearning.swordoffer.ch02;

/**
 * 变态跳台阶
 * 题目描述
 * 一只青蛙一次可以跳上1级台阶，也可以跳上2级……它也可以跳上n级。求该青蛙跳上一个n级的台阶总共有多少种跳法。
 * <p>
 * 解法
 * 跳上 n-1 级台阶，可以从 n-2 级跳 1 级上去，也可以从 n-3 级跳 2 级上去...也可以从 0 级跳上去。那么
 * <p>
 * f(n-1) = f(0) + f(1) + ... + f(n-2)
 * 跳上 n 级台阶，可以从 n-1 级跳 1 级上去，也可以从 n-2 级跳 2 级上去...也可以从 0 级跳上去。那么
 * <p>
 * f(n) = f(0) + f(1) + ... + f(n-2) + f(n-1)  ②
 * <p>
 * ②-①：
 * f(n) - f(n-1) = f(n-1)
 * f(n) = 2f(n-1)
 * 所以 f(n) 是一个等比数列：
 * <p>
 * f(n) = 2^(n-1)
 */
public class Q10_03_JumpFloorII {
    /**
     * 青蛙跳台阶II
     *
     * @param target 跳上的那一级台阶
     * @return 多少种跳法
     */
    public int JumpFloorII(int target) {
        return (int) Math.pow(2, target - 1);
    }
}
