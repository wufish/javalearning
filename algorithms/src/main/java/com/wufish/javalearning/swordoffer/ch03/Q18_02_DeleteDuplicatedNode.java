package com.wufish.javalearning.swordoffer.ch03;

/**
 * ## 删除链表中重复的节点
 * <p>
 * ### 题目描述
 * 在一个排序的链表中，存在重复的结点，请删除该链表中重复的结点，
 * 重复的结点不保留，返回链表头指针。 例如，链表`1->2->3->3->4->4->5` 处理后为 `1->2->5`。
 * <p>
 * ### 解法
 * #### 解法一：递归
 */
public class Q18_02_DeleteDuplicatedNode {
    static class ListNode {
        int val;
        ListNode next;

        public ListNode(int val) {
            this.val = val;
        }
    }

    /**
     * 递归删除链表重复的节点，找到下一个不重复的节点
     *
     * @param pHead 链表头节点
     * @return 删除节点后的链表
     */
    public ListNode deleteDuplication(ListNode pHead) {
        if (pHead == null || pHead.next == null) {
            return pHead;
        }
        if (pHead.val == pHead.next.val) {
            if (pHead.next.next == null) {
                return null;
            }
            if (pHead.next.next.val == pHead.val) {
                // next.next后面可能还有重复的数据，所以保留一个节点继续递归
                return deleteDuplication(pHead.next);
            }
            return deleteDuplication(pHead.next.next);
        }
        pHead.next = deleteDuplication(pHead.next);
        return pHead;
    }

    public ListNode deleteDuplicationII(ListNode pHead) {
        if (pHead == null || pHead.next == null) {
            return pHead;
        }
        ListNode pre = null;
        ListNode cur = pHead;
        while (cur != null) {
            if (cur.next != null && cur.val == cur.next.val) {
                int val = cur.val;
                while (cur.next != null && cur.next.val == val) {
                    cur = cur.next;
                }
                if (pre == null) {
                    // 头结点重复，删除
                    pHead = cur.next;
                } else {
                    pre.next = cur.next;
                }
            } else {
                pre = cur;
            }
            cur = cur.next;
        }
        return pHead;
    }

    /**
     * ### 测试用例
     * 1. 功能测试（重复的节点位于链表的头部/中间/尾部；链表中没有重复的节点）；
     * 2. 特殊输入测试（指向链表头节点的为空指针；链表中所有节点都是重复的）。
     *
     * @param args
     */
    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(1);
        ListNode node3 = new ListNode(2);
        ListNode node4 = new ListNode(3);
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        Q18_02_DeleteDuplicatedNode test = new Q18_02_DeleteDuplicatedNode();
        ListNode node = test.deleteDuplication(head);
        System.out.println();
    }
}
