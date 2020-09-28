package com.wufish.javalearning.sort;

/**
 * @Author 58
 * @Create time: 2020/9/28 09:50
 * @Description:
 */
public class LinkedSort {
    public static void main(String[] args) {
        ListNode t1 = new ListNode(1);
        ListNode t2 = new ListNode(3);
        ListNode t3 = new ListNode(4);
        ListNode t4 = new ListNode(2);
        ListNode t5 = new ListNode(5);
        ListNode t6 = new ListNode(2);
        t1.next = t2;
        t2.next = t3;
        t3.next = t4;
        t4.next = t5;
        t5.next = t6;
        ListNode sort = sortList2(t1);
        ListNode cur = sort;
        while (cur != null) {
            System.out.println(cur.val);
            cur = cur.next;
        }
        System.out.println();
    }

    private static ListNode sortList3(ListNode head) {
        if (head == null || head.next == null) return head;
        int length = 0;
        ListNode cur = head;
        while (cur != null) {
            length++;
            cur = cur.next;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        for (int gap = 1; gap < length; gap *= 2) {
            // pre 用于记录链表分隔节点
            ListNode pre = dummy;
            // cur 指针用于顺序遍历
            cur = pre.next;
            while (cur != null) {
                // 1. 保存第一个链表节点
                ListNode left = cur;
                // 用于断开链接
                ListNode tmpPre = pre;
                int count = gap;
                while (count > 0 && cur != null) {
                    count--;
                    cur = cur.next;
                    tmpPre = tmpPre.next;
                }
                if (count > 0 || cur == null) break;// <=gap 的，不处理已经是有序的了，直接走下一个 gap
                // 2. 保存中间节点用于断开链表
                ListNode mid = tmpPre;
                // 3. 保存第二个链表头结点
                ListNode right = cur;
                count = gap;
                while (count > 0 && cur != null) {
                    count--;
                    cur = cur.next;
                    tmpPre = tmpPre.next;
                }
                // 4. 中间断开链接
                mid.next = null;
                // 5. 断开右边链表
                tmpPre.next = null;
                // 6. 链表合并返回，头结点
                ListNode mergeHead = merge(left, right);
                // 7. 左边连接
                pre.next = mergeHead;
                // 遍历找到合并后的结果的最后一个节点，进行链接
                while (pre.next != null) {
                    pre = pre.next;
                }
                // 右边链接
                pre.next = cur;
            }
        }

        return dummy.next;
    }

    private static ListNode sortList2(ListNode head) {
        if (head == null || head.next == null) return head;
        int length = 0;
        ListNode cur = head;
        while (cur != null) {
            length++;
            cur = cur.next;
        }
        // 哑节点
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        for (int gap = 1; gap < length; gap *= 2) {
            ListNode pre = dummy;
            cur = pre.next;
            while (cur != null) {
                // 第一个待合并节点
                ListNode left = cur;
                int count = gap;
                while (count > 0 && cur != null) {
                    count--;
                    cur = cur.next;
                }
                // 第一个链表不够 gap
                if (count > 0 || cur == null) break;
                //
                ListNode right = cur;
                count = gap;
                while (count > 0 && cur != null) {
                    count--;
                    cur = cur.next;
                }
                int leftCount = gap, rightCount = gap - count;
                while (leftCount > 0 && rightCount > 0) {
                    if (left.val < right.val) {
                        pre.next = left;
                        left = left.next;
                        leftCount--;
                    } else {
                        pre.next = right;
                        right = right.next;
                        rightCount--;
                    }
                    pre = pre.next;
                }
                while (leftCount > 0) {
                    pre.next = left;
                    left = left.next;
                    pre = pre.next;
                    leftCount--;
                }
                while (rightCount > 0) {
                    pre.next = right;
                    right = right.next;
                    pre = pre.next;
                    rightCount--;
                }
                pre.next = cur;
            }
        }
        return dummy.next;
    }

    private static ListNode merge(ListNode left, ListNode right) {
        if (left == null) return right;
        if (right == null) return left;
        if (left.val < right.val) {
            left.next = merge(left.next, right);
            return left;
        } else {
            right.next = merge(left, right.next);
            return right;
        }
    }

    public static SortAlgorithm.ListNode sortList(SortAlgorithm.ListNode head) {
        if (head == null || head.next == null) return head;

        SortAlgorithm.ListNode mid = findMidNode(head);
        SortAlgorithm.ListNode rightHead = mid.next;
        // 断开链表
        mid.next = null;
        SortAlgorithm.ListNode left = sortList(head);
        SortAlgorithm.ListNode right = sortList(rightHead);
        return merge(left, right);
    }

    private static SortAlgorithm.ListNode findMidNode(SortAlgorithm.ListNode node) {
        if (node == null) return null;
        SortAlgorithm.ListNode fast = node.next, slow = node;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    private static SortAlgorithm.ListNode merge(SortAlgorithm.ListNode left, SortAlgorithm.ListNode right) {
        SortAlgorithm.ListNode dummy = new SortAlgorithm.ListNode(-1);
        SortAlgorithm.ListNode curr = dummy;
        while (left != null && right != null) {
            if (left.val < right.val) {
                curr.next = left;
                left = left.next;
            } else {
                curr.next = right;
                right = right.next;
            }
            curr = curr.next;
        }
        curr.next = left != null ? left : right;
        return dummy.next;
    }

    private static class ListNode {
        int val;
        ListNode next;

        public ListNode(int value) {
            this.val = value;
        }
    }
}
