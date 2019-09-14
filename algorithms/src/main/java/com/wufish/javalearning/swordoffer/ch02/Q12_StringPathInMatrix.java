package com.wufish.javalearning.swordoffer.ch02;

/**
 * @Author wzj
 * @Create time: 2019/09/14 16:34
 * @Description:## 矩阵中的路径
 * <p>
 * ### 题目描述
 * 请设计一个函数，用来判断在一个矩阵中是否存在一条包含某字符串所有字符的路径。
 * 路径可以从矩阵中的任意一个格子开始，每一步可以在矩阵中向左，向右，向上，向下移动一个格子。
 * 如果一条路径经过了矩阵中的某一个格子，则之后不能再次进入这个格子。 例如 `a b c e s f c s a d e e`
 * 这样的 `3 X 4` 矩阵中包含一条字符串`"bcced"`的路径，但是矩阵中不包含`"abcb"`路径，
 * 因为字符串的第一个字符b占据了矩阵中的第一行第二个格子之后，路径不能再次进入该格子。
 * <p>
 * ### 解法
 * 回溯法。首先，任选一个格子作为路径起点。假设格子对应的字符为 ch，并且对应路径上的第 i 个字符。若相等，到相邻格子寻找路径上的第 i+1 个字符。重复这一过程。
 */
public class Q12_StringPathInMatrix {
    /**
     * 判断矩阵中是否包含某条路径
     *
     * @param matrix 矩阵
     * @param rows   行数
     * @param cols   列数
     * @param str    路径
     * @return bool
     */
    public boolean hasPath(char[] matrix, int rows, int cols, char[] str) {
        if (matrix == null || rows < 1 || cols < 1 || str == null || str.length == 0) {
            return false;
        }
        boolean[] visited = new boolean[matrix.length];
        int pathLength = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (hasPathCore(matrix, rows, cols, i, j, str, pathLength, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasPathCore(char[] matrix, int rows, int cols, int i, int j, char[] str, int pathLength, boolean[] visited) {
        if (pathLength == str.length) {
            return true;
        }
        boolean hasPath = false;
        if (i >= 0 && i < rows && j >= 0 && j < cols && matrix[i * cols + j] == str[pathLength] && !visited[i * cols + j]) {
            pathLength++;
            visited[i * cols + j] = true;
            hasPath = hasPathCore(matrix, rows, cols, i - 1, j, str, pathLength, visited)
                    || hasPathCore(matrix, rows, cols, i + 1, j, str, pathLength, visited)
                    || hasPathCore(matrix, rows, cols, i, j - 1, str, pathLength, visited)
                    || hasPathCore(matrix, rows, cols, i, j + 1, str, pathLength, visited);
            if (!hasPath) {
                --pathLength;
                visited[i * cols + j] = false;
            }
        }
        return hasPath;
    }

    /**
     * ### 测试用例
     * 1. 功能测试（在多行多列的矩阵中存在或者不存在路径）；
     * 2. 边界值测试（矩阵只有一行或者一列；矩阵和路径中的所有字母都是相同的）；
     * 3. 特殊输入测试（输入空指针）。
     *
     * @param args
     */
    public static void main(String[] args) {

    }
}
