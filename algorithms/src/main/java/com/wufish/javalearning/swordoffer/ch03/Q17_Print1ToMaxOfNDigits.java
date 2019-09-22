package com.wufish.javalearning.swordoffer.ch03;

/**
 * ## 打印从 1 到最大的 n 位数
 * <p>
 * ### 题目描述
 * 输入数字 `n`，按顺序打印出从 `1` 最大的 `n` 位十进制数。比如输入 `3`，则打印出 `1、2、3` 一直到最大的 3 位数即 999。
 * <p>
 * ### 解法
 * 此题需要注意 n 位数构成的数字可能超出最大的 int 或者 long long 能表示的范围。因此，采用字符数组来存储数字。
 * <p>
 * 关键是：
 * - 对字符数组表示的数进行递增操作
 * - 输出数字（0开头的需要把0去除）
 */
public class Q17_Print1ToMaxOfNDigits {
    /**
     * 打印从1到最大的n位数
     *
     * @param n n位
     */
    public void print1ToMaxOfNDigits(int n) {
        if (n < 1) {
            return;
        }
        /*char[] number = new char[n];
        for (int i = 0; i < n; i++) {
            number[i] = '0';
        }
        /*while (!increment(number)) {
            printNumber(number);
        }*/
        char[] number = new char[n];
        for (int i = 0; i < n; i++) {
            number[i] = '0';
        }
        printRecursively(number, number.length, 0);
    }

    private void printNumber(char[] number) {
        int length = number.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (number[i] != '0') {
                for (int j = i; j < length; j++) {
                    sb.append(number[j]);
                }
                break;
            }
        }
        System.out.println(sb.toString());
    }

    private boolean increment(char[] number) {
        boolean res = false;
        int length = number.length;
        int carry = 1;
        for (int i = length - 1; i >= 0; i--) {
            int num = number[i] - '0' + carry;
            if (num > 9) {
                if (i == 0) {
                    res = true;
                    break;
                }
                number[i] = '0';
            } else {
                number[i]++;
                break;
            }
        }
        return res;
    }

    /**
     * 数字排列，递归
     * @param number
     * @param length
     * @param index
     */
    private void printRecursively(char[] number, int length, int index) {
        if (index >= length) {
            printNumber(number);
            return;
        }
        // 避免全是0
        int start = index == length - 1 ? 1 : 0;
        for (int i = start; i < 10; i++) {
            number[index] = (char) (i + '0');
            printRecursively(number, length, index + 1);
        }
    }

    /**
     * ### 测试用例
     * 1. 功能测试（输入 1、2、3......）；
     * 2. 特殊输入测试（输入 -1、0）。
     *
     * @param args
     */
    public static void main(String[] args) {
        Q17_Print1ToMaxOfNDigits test = new Q17_Print1ToMaxOfNDigits();
        test.print1ToMaxOfNDigits(1);
    }
}
