package com.wufish.javalearning.leetccode;

import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.cn/problems/length-of-longest-fibonacci-subsequence/
 *
 * @author wuzhijun05 <wuzhijun05@kuaishou.com>
 * Created on 2022-07-09
 */
public class Leetcode873 {
    public static void main(String[] args) {
        int res = new Solution().lenLongestFibSubseq1(new int[]{1, 2, 3, 4, 5, 6, 7, 8});
        System.out.println();
    }

    static class Solution {
        public int lenLongestFibSubseq1(int[] arr) {
            int n = arr.length, res = 0;
            Map<Integer, Integer> elemIndexMap = new HashMap<>();
            for (int i = 0; i < n; i++) {
                elemIndexMap.put(arr[i], i);
            }
            int[][] dp = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = i - 1; j >= 0; j--) {
                    int k = elemIndexMap.getOrDefault(arr[i] - arr[j], -1);
                    if (k == -1 || k >= j) {
                        continue;
                    }
                    dp[i][j] = Math.max(3, dp[j][k] + 1);
                    res = Math.max(res, dp[i][j]);
                }
            }
            return res;
        }

        public int lenLongestFibSubseq2(int[] arr) {
            // 剪枝
            int n = arr.length, res = 0;
            Map<Integer, Integer> elemIndexMap = new HashMap<>();
            for (int i = 0; i < n; i++) {
                elemIndexMap.put(arr[i], i);
            }
            int[][] dp = new int[n][n];
            for (int i = 0; i < n; i++) {
                // 1. 剪枝，j+2
                for (int j = i - 1; j >= 0 && j + 2 > res; j--) {
                    // 2. 剪枝，数组单调递增，只取 a_k < a_j 的
                    if (arr[i] - arr[j] >= arr[j]) {
                        break;
                    }
                    int k = elemIndexMap.getOrDefault(arr[i] - arr[j], -1);
                    if (k == -1) {
                        continue;
                    }
                    dp[i][j] = Math.max(3, dp[j][k] + 1);
                    res = Math.max(res, dp[i][j]);
                }
            }
            return res;
        }
    }
}
