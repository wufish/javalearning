package com.wufish.javalearning.swordoffer.ch02;

/**
 * @Author wzj
 * @Create time: 2019/09/14 16:44
 * @Description: ## 机器人的移动范围
 * <p>
 * ### 题目描述
 * 地上有一个`m`行和`n`列的方格。一个机器人从坐标`0,0`的格子开始移动，每一次只能向左，右，上，下四个方向移动一格，
 * 但是不能进入行坐标和列坐标的数位之和大于`k`的格子。 例如，当`k`为`18`时，机器人能够进入方格`（35,37）`，
 * 因为`3+5+3+7 = 18`。但是，它不能进入方格`（35,38）`，因为`3+5+3+8 = 19`。请问该机器人能够达到多少个格子？
 * <p>
 * ### 解法
 * 从坐标(0, 0) 开始移动，当它准备进入坐标(i, j)，判断是否能进入，
 * 如果能，再判断它能否进入 4 个相邻的格子 (i-1, j), (i+1, j), (i, j-1), (i, j+1)。
 */
public class Q13_RobotMove {
    public int movingCount(int k, int rows, int cols) {
        if (k < 0 || rows < 1 || cols < 1) {
            return 0;
        }
        boolean[] visited = new boolean[rows * cols];
        return getCount(k, rows, cols, 0, 0, visited);
    }

    public int getCount(int k, int rows, int cols, int i, int j, boolean[] visited) {
        if (check(k, i, j, rows, cols, visited)) {
            visited[i * cols + j] = true;
            return 1 + getCount(k, rows, cols, i - 1, j, visited)
                    + getCount(k, rows, cols, i + 1, j, visited)
                    + getCount(k, rows, cols, i, j - 1, visited)
                    + getCount(k, rows, cols, i, j + 1, visited);
        }
        return 0;
    }

    private boolean check(int k, int i, int j, int rows, int cols, boolean[] visited) {
        return i >= 0
                && i < rows
                && j >= 0
                && j < cols
                && !visited[i * cols + j]
                && getDigitSum(i) + getDigitSum(j) <= k;
    }

    private int getDigitSum(int i) {
        int res = 0;
        while (i > 0) {
            res += i % 10;
            i /= 10;
        }
        return res;
    }

    /**
     * ### 测试用例
     * 1. 功能测试（方格为多行多列；k 为正数）；
     * 2. 边界值测试（方格只有一行或者一列；k = 0）；
     * 3. 特殊输入测试（k < 0）。
     *
     * @param args
     */
    public static void main(String[] args) {

    }
}
