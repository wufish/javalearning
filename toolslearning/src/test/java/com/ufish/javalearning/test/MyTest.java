package com.ufish.javalearning.test;

import java.util.Arrays;

public class MyTest {
    private static int coinChange(int[] coins, int amount, int[] dp) {
        if (amount < 0) return -1;
        if (amount == 0) return 0;
        // 返回备忘录数据
        if (dp[amount] > 0) return dp[amount];
        int min = amount + 1;
        for (int coin : coins) {
            int t = coinChange(coins, amount - coin, dp);
            if (t >= 0 && t < min) min = t;
        }
        dp[amount] = min == amount ? -1 : min + 1;
        return dp[amount];
    }

    // 动态规划
    private static int dp(int[] nums) {
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);
        for (int i = 0; i < nums.length; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                    break;
                }
            }
        }
        int max = 0;
        for (int i = 0; i < dp.length; i++) {
            max = dp[i] > max ? dp[i] : max;
        }
        return max;
    }

    static int dp(int k, int n, int[][] mem){
        // base
        if(k == 1) {
            return n;
        }
        if(n == 0) {
            return 0;
        }
        // exist
        if(mem[k][n] > 0) return mem[k][n];
        int res = Integer.MAX_VALUE;
        for(int i=1; i<=n; i++){
            res = Math.min(res, 1 + Math.max(dp(k, n - i, mem), dp(k-1, i-1, mem)));
        }
        mem[k][n] = res;
        return res;
    }

    public static void main(String[] args) {
        int i = coinChange(new int[]{186, 419, 83, 408}, 6249, new int[6250]);
        int dp = dp(new int[]{1, 3, 6, 7, 9, 4, 10, 5, 6});
        int dp1 = dp(2, 6, new int[3][7]);
        System.out.println();
    }
}
