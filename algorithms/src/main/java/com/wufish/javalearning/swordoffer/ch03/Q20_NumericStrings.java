package com.wufish.javalearning.swordoffer.ch03;

/**
 * ## 表示数值的字符串
 * <p>
 * ### 题目描述
 * 请实现一个函数用来判断字符串是否表示数值（包括整数和小数）。
 * 例如，字符串"+100","5e2","-123","3.1416"和"-1E-16"都表示数值。
 * 但是"12e","1a3.14","1.2.3",
 * "+-5"和"12e+4.3"都不是。
 * <p>
 * ### 解法
 * <p>
 * #### 解法一
 * <p>
 * 利用正则表达式匹配即可。
 * ```
 * []  ： 字符集合
 * ()  ： 分组
 * ?   ： 重复 0 ~ 1
 * +   ： 重复 1 ~ n
 * *   ： 重复 0 ~ n
 * .   ： 任意字符
 * \\. ： 转义后的 .
 * \\d ： 数字
 * ```
 */
public class Q20_NumericStrings {
    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    public boolean isNumeric(char[] str) {
        return str != null
                && str.length != 0
                && new String(str).matches("[+-]?\\d+(\\.\\d+)?([eE][+-]?\\d+)?");
    }

    /**
     * 判断是否是数值
     * 表示数值的字符串遵循模式`A[.[B]][e|EC]`或者`.B[e|EC]`，
     * 其中A为数值的整数部分，B紧跟小数点为数值的小数部分，C紧跟着e或者E为数值的指数部分。
     * 上述A和C都有可能以 `+` 或者 `-`
     * 开头的0~9的数位串，B也是0~9的数位串，但前面不能有正负号。
     *
     * @param str
     * @return
     */
    public boolean isNumeric(char[] str) {
        if (str == null || str.length < 1) {
            return false;
        }
    }

    /**
     * ### 测试用例
     * <p>
     * 1. 功能测试（正数或者负数；包含或者不包含整数部分的数值；包含或者不包含效数部分的值；
     * 包含或者不包含指数部分的值；各种不能表达有效数值的字符串）；
     * 2. 特殊输入测试（输入字符串和模式字符串是空指针、空字符串）。
     *
     * @param args
     */
    public static void main(String[] args) {
        Q20_NumericStrings test = new Q20_NumericStrings();
        boolean numeric = test.isNumeric(new char[]{'+'});
        System.out.println();
    }
}
