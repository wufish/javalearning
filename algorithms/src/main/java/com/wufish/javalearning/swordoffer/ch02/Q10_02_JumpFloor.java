package com.wufish.javalearning.swordoffer.ch02;

/**
 * 跳台阶
 * 题目描述
 * 一只青蛙一次可以跳上1级台阶，也可以跳上2级。求该青蛙跳上一个n级的台阶总共有多少种跳法（先后次序不同算不同的结果）。
 * <p>
 * 解法
 * 跳上 n 级台阶，可以从 n-1 级跳 1 级上去，也可以从 n-2 级跳 2 级上去。所以
 * f(n) = f(n-1) + f(n-2)
 *
 * @see Q10_01_Fibonacci
 */
public class Q10_02_JumpFloor {
}
