package com.wufish.javalearning.alibaba;

/**
 * // 3. 题目：贴墙纸
 * // 你是一位装修工，根据设计师的要求给客户的客厅背景墙贴墙纸。
 * // 假设背景墙面积为 n x m，装修风格为现代极简风格，需要使用尽可能少的 不同颜色的 正方形 墙纸包 来铺满墙面。
 * // 假设正方形墙纸包块的规格不限，边长都是整数。
 * // 请你帮设计师计算一下，最少需要用到多少块方形墙纸包？
 * // 示例 1：
 * // 输入：n = 2, m = 3
 * // 输出：3
 * // 解释：3 块墙纸包就可以铺满墙面。
 * // 2 块 1x1 墙纸包
 * // 1 块 2x2 墙纸包
 * // 示例 2：
 * // 输入：n = 5, m = 8
 * // 输出：5
 * // 示例 3：
 * // 输入：n = 11, m = 13
 * // 输出：6
 * // 提示：
 * // 1 <= n <= 13
 * // 1 <= m <= 13
 */
public class Tester3 {

    private static int count = 0;

    public static void main(String[] args) {
        //dfs(5, 8);
        dfs(11, 13);
        System.out.println(count);
    }

    public static void dfs(int height, int width) {
        int min = Math.min(height, width);
        int max = Math.max(height, width);
        if (min <= (max / 2)) {
            count++;
            dfs(min, max - min);
        }else {
            count++;
            if (min != max) {
                dfs(min, max - min);
            }
        }

    }
}