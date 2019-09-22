package com.wufish.javalearning.swordoffer.ch03;

/**
 * ## 数值的整数次方
 * <p>
 * ### 题目描述
 * 给定一个 `double` 类型的浮点数 `base` 和 `int` 类型的整数 `exponent`。求 `base`的 `exponent` 次方。
 * <p>
 * ### 解法
 * 注意判断值数是否小于 0。另外 0 的 0 次方没有意义，也需要考虑一下，看具体题目要求。
 */
public class Q16_Power {
    /**
     * 计算数值的整数次方
     *
     * @param base     底数
     * @param exponent 指数
     * @return 数值的整数次方
     */
    public double pow(double base, int exponent) throws IllegalArgumentException {
        // doble比较大小
        if (Double.compare(base, 0.0D) == 0) {
            // 异常参数base
            throw new IllegalArgumentException();
        }
        if (exponent == 0) {
            return 1.0D;
        }
        if (exponent == -1) {
            return 1.0 / base;
        }
        /*int abs = Math.abs(exponent);
        double result = 1.0D;
        for (int i = 0; i < abs; i++) {
            result *= result;
        }
        return exponent < 0 ? 1.0 / result : result;*/
        double result = pow(base, exponent >> 1);
        result *= result;
        if ((exponent & 1) == 1) {
            // 奇数
            result *= base;
        }
        return result;
    }

    /**
     * ### 测试用例
     * 1. 把底数和指数分别设为正数、负数和零。
     *
     * @param args
     */
    public static void main(String[] args) {
        Q16_Power q16_power = new Q16_Power();
        System.out.println(q16_power.pow(2, 2));
        System.out.println(q16_power.pow(2, 3));
        System.out.println(q16_power.pow(2, -2));
        System.out.println(q16_power.pow(2, -3));
    }
}
