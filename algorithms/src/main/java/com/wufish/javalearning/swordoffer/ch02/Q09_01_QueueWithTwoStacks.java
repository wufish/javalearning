package com.wufish.javalearning.swordoffer.ch02;

import java.util.Stack;

/**
 * 用两个栈实现队列
 * 题目描述
 * 用两个栈来实现一个队列，完成队列的 Push 和 Pop 操作。 队列中的元素为 int 类型。
 * <p>
 * 解法
 * Push 操作，每次都存入 stack1； Pop 操作，每次从 stack2 取：
 * <p>
 * stack2 栈不为空时，不能将 stack1 元素倒入；
 * stack2 栈为空时，需要一次将 stack1 元素全部倒入。
 */
public class Q09_01_QueueWithTwoStacks {
    private Stack<Integer> stack1 = new Stack<>();
    private Stack<Integer> stack2 = new Stack<>();

    public void offer(int value) {
        stack1.push(value);
    }

    public int poll() {
        if (stack2.isEmpty()) {
            if (stack1.isEmpty()) {
                return -1;
            }
            while (!stack1.isEmpty()) {
                stack2.push(stack1.pop());
            }
        }
        return stack2.pop();
    }

    /**
     * 测试用例
     * 1. 往空的队列里添加、删除元素；
     * 2. 往非空的队列添加、删除元素；
     * 3. 连续删除元素直至队列为空。
     *
     * @param args
     */
    public static void main(String[] args) {
        Q09_01_QueueWithTwoStacks test = new Q09_01_QueueWithTwoStacks();
        System.out.println(test.poll());
        System.out.println(test.poll());
        test.offer(1);
        test.offer(2);
        System.out.println(test.poll());
        System.out.println(test.poll());
        System.out.println(test.poll());
        System.out.println(test.poll());
    }
}
