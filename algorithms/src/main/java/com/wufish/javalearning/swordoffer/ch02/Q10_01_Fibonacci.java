package com.wufish.javalearning.swordoffer.ch02;

/**
 * 斐波那契数列
 * 题目描述
 * 大家都知道斐波那契数列，现在要求输入一个整数n，请你输出斐波那契数列的第 n 项（从 0 开始，第 0 项为 0）。n<=39
 * <p>
 * 解法
 * 解法一
 * 采用递归方式，简洁明了，但效率很低，存在大量的重复计算。
 * <p>
 * 解法二
 * 从下往上计算，递推，时间复杂度 O(n)。
 * <p>
 * 解法三
 * 公式求解：[f(n),f(n-1);f(n-1),f(n-2)] = [1,1;1,0]^(n-1) 转化为n此房求解，n此方也可以分解为n/2的平方
 */
public class Q10_01_Fibonacci {
    /**
     * 递归,效率低,子递归大量重复计算
     *
     * @param n
     * @return
     */
    public int Fibonacci(int n) {
        if (n < 2) {
            return n;
        } else {
            return Fibonacci(n - 1) + Fibonacci(n - 2);
        }
    }

    /**
     * @param n
     * @return
     */
    public int FibonacciNew(int n) {
        if (n < 2) {
            return n;
        }
        int[] res = new int[n + 1];
        res[0] = 0;
        res[1] = 1;
        for (int i = 2; i <= n; i++) {
            res[i] = res[i - 1] + res[i - 2];
        }
        return res[n];
        /**
         * int[] res = new int[2];
         *         for (int i = 2; i <= n; i++) {
         *             int t = res[0] + res[1];
         *             res[0] = res[1];
         *             res[1] = t;
         *         }
         *         return res[1];
         */
    }

    /**
     * 测试用例
     * 1. 功能测试（如输入 3、5、10 等）；
     * 2. 边界值测试（如输入 0、1、2）；
     * 3. 性能测试（输入较大的数字，如 40、50、100 等）。
     *
     * @param args
     */
    public static void main(String[] args) {

    }
}
