package com.wufish.javalearning.swordoffer.ch02;

/**
 * 重建二叉树
 * 题目描述
 * 输入某二叉树的前序遍历和中序遍历的结果，请重建出该二叉树。
 * 假设输入的前序遍历和中序遍历的结果中都不含重复的数字。
 * 例如输入前序遍历序列 {1,2,4,7,3,5,6,8} 和中序遍历序列 {4,7,2,1,5,3,8,6}，
 * 则重建二叉树并返回。
 * <p>
 * 解法：
 * <p>
 * 在二叉树的前序遍历序列中，第一个数字总是根结点的值。
 * 在中序遍历序列中，根结点的值在序列的中间，左子树的结点位于根结点左侧，而右子树的结点位于根结点值的右侧。
 * <p>
 * 遍历中序序列，找到根结点，递归构建左子树与右子树。
 * <p>
 * 注意添加特殊情况的 if 判断。
 * <p>
 * 考点：
 * 不同遍历算法、复杂问题分解递归
 */
public class Q07_ConstructBinaryTree {
    static class BinaryTreeNode {
        int value;
        BinaryTreeNode left;
        BinaryTreeNode right;

        public BinaryTreeNode(int value) {
            this.value = value;
        }
    }

    /**
     * 测试用例
     * 1. 普通二叉树（完全二叉树；不完全二叉树）；
     * 2. 特殊二叉树（所有结点都没有左/右子结点；只有一个结点的二叉树）；
     * 2. 特殊输入测试（二叉树根结点为空；输入的前序序列和中序序列不匹配）。
     *
     * @param args
     */
    public static void main(String[] args) {

    }

    public BinaryTreeNode reConstructBinaryTree(int[] pre, int[] in) {
        if (pre == null || in == null || pre.length != in.length) {
            return null;
        }
        int len = pre.length;
        return constructBinaryTree(pre, 0, len - 1, in, 0, len - 1);
    }

    private BinaryTreeNode constructBinaryTree(int[] pre, int startPre, int endPre, int[] in, int startIn, int endIn) {
        // 前序第一个是节点
        BinaryTreeNode node = new BinaryTreeNode(pre[startPre]);
        // 只有一个节点
        if (startPre == endPre) {
            if (startIn == endIn) {
                return node;
            }
            throw new IllegalArgumentException("Invalid input!");
        }
        // 中序找到根节点
        int nodeIndex = startIn;
        while (in[nodeIndex] != node.value) {
            nodeIndex++;
            if (nodeIndex > endIn) {
                throw new IllegalArgumentException("Invalid input!");
            }
        }
        //递归构建左右字数
        int len = nodeIndex - startIn;
        if (len > 0) {
            node.left = constructBinaryTree(pre, startPre + 1, startPre + len, in, startIn, nodeIndex - 1);
        }
        if (nodeIndex < endIn) {
            node.right = constructBinaryTree(pre, startPre + len + 1, endPre, in, nodeIndex + 1, endIn);
        }
        return node;
    }

}
