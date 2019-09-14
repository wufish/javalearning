package com.wufish.javalearning.swordoffer.ch02;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 从尾到头打印链表
 * 题目描述
 * 输入一个链表，按链表值从尾到头的顺序返回一个 ArrayList。
 * <p>
 * 要求：是否改变原来的链表结构
 * 考点：循环、递归和栈
 */
public class Q06_PrintListInReversedOrder {
    static class ListNode {
        int value;
        ListNode next;
    }

    /**
     * 栈辅助
     *
     * @param listNode
     * @return
     */
    public ArrayList<Integer> printListFromTailToHead1(ListNode listNode) {
        ArrayList<Integer> res = new ArrayList<>();
        if (listNode == null) {
            return res;
        }
        Stack<Integer> stack = new Stack<>();
        while (listNode != null) {
            stack.push(listNode.value);
            listNode = listNode.next;
        }
        while (!stack.isEmpty()) {
            res.add(stack.pop());
        }
        return res;
    }

    /**
     * 递归，但是考虑递归深度
     *
     * @param listNode
     * @return
     */
    public ArrayList<Integer> printListFromTailToHead2(ListNode listNode) {
        ArrayList<Integer> res = new ArrayList<>();
        if (listNode == null) {
            return res;
        }
        add(listNode, res);
        return res;
    }

    private void add(ListNode listNode, ArrayList<Integer> res) {
        if (listNode.next != null) {
            add(listNode.next, res);
        }
        res.add(listNode.value);
    }
}
