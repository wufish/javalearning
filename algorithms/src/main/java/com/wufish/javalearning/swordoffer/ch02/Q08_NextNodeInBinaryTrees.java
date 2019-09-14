package com.wufish.javalearning.swordoffer.ch02;

/**
 * 二叉树的下一个结点
 * 题目描述
 * 给定一个二叉树和其中的一个结点，请找出中序遍历顺序的下一个结点并且返回。
 * 注意，树中的结点不仅包含左右子结点，同时包含指向父结点的指针。
 * <p>
 * 解法
 * 对于结点 pNode：
 * <p>
 * 1. 如果它有右子树，则右子树的最左结点就是它的下一个结点；
 * 2. 如果它没有右子树，判断它与父结点 pNode.next 的位置情况：
 * 2.1 如果它是父结点的左孩子，那么父结点 pNode.next 就是它的下一个结点；
 * 2.2 如果它是父结点的右孩子，一直向上寻找，直到找到某个结点，它是它父结点的左孩子，那么该父结点就是 pNode 的下一个结点,否则为null。
 * <p>
 * 考点：
 * 中序，画图举例
 */
public class Q08_NextNodeInBinaryTrees {
    static class TreeLinkNode {
        int val;
        TreeLinkNode left;
        TreeLinkNode right;
        TreeLinkNode parent;

        TreeLinkNode(int val) {
            this.val = val;
        }
    }

    public TreeLinkNode GetNext(TreeLinkNode pNode) {
        if (pNode == null) {
            return null;
        }
        // 1. 判断有没有右节点
        if (pNode.right != null) {
            TreeLinkNode t = pNode.right;
            while (t.left != null) {
                t = t.left;
            }
            return t;
        }
        // 2. 是不是父节点的左子树
        if (pNode.parent != null && pNode.parent.left == pNode) {
            return pNode.parent;
        }
        // 3. 找到父节点是左子树的节点
        TreeLinkNode tmp = pNode;
        while (tmp.parent != null) {
            if (tmp.parent.left == tmp) {
                return tmp.parent;
            }
            tmp = tmp.parent;
        }
        return null;
    }

    /**
     * 测试用例
     * 1. 普通二叉树（完全二叉树；不完全二叉树）；
     * 2. 特殊二叉树（所有结点都没有左/右子结点；只有一个结点的二叉树；二叉树的根结点为空）；
     * 3. 不同位置的结点的下一个结点（下一个结点为当前结点的右子结点、右子树的最左子结点、父结点、跨层的父结点等；当前结点没有下一个结点）。
     *
     * @param args
     */
    public static void main(String[] args) {

    }
}
