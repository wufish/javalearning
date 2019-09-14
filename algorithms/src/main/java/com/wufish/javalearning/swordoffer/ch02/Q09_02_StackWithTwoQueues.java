package com.wufish.javalearning.swordoffer.ch02;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 用两个队列实现栈
 * 题目描述
 * 用两个队列来实现一个栈，完成栈的 Push 和 Pop 操作。 栈中的元素为 int 类型。
 * <p>
 * 解法
 * Push 操作，每次都存入 queue1； Pop 操作，每次从 queue1 取：
 * <p>
 * 将 queue1 中的元素依次倒入 queue2，直到 queue1 剩下一个元素，这个元素就是要 pop 出去的；
 * 将 queue1 与 queue2 进行交换，这样保证每次都从 queue1 中存取元素，queue2 只起到辅助暂存的作用。
 *
 *
 */
public class Q09_02_StackWithTwoQueues {
    private Queue<Integer> queue1 = new LinkedList<>();
    private Queue<Integer> queue2 = new LinkedList<>();

    public void push(int value){
        queue1.offer(value);
    }

    public int pop(){
        if (queue1.isEmpty()) {
            throw new RuntimeException("Empty stack");
        }
        while (queue1.size() > 1) {
            queue2.offer(queue1.poll());
        }
        int val = queue1.poll();
        // 交换队列
        Queue<Integer> t = this.queue1;
        queue1 = queue2;
        queue2 = t;
        return val;
    }
}
