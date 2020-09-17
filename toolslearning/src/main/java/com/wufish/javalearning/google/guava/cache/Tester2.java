package com.wufish.javalearning.google.guava.cache;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * 2.单词统计
 * 给定一个字符串，计算字符串中每个单词出现的数量，并排序输出。
 * a. 单词一个大写字母开始，接着跟随0个或任意个小写字母；
 * b. 如果单词数量大于 1，单词后会跟着数字表示单词的数量。如果数量等于 1 则不会跟数字。例如，Hello2World 和 是合法，但 Hello1World2 这个表达是不合法的；
 * c. Hello2表示HelloHello；
 * d. (Hello2World2)3 可以等于Hello2World2Hello2World2Hello2World2；
 * 输出格式为：第一个（按字典序）单词，跟着它的数量（如果单词数量为1，则不输出），然后是第二个单词的名字（按字典序），跟着它的数量（如果单词数量为1，则不输出），以此类推。
 * 示例1：
 * 输入：字符串 = "World3Hello"
 * 输出: "HelloWorld3"
 * 解释: 单词数量是 {'Hello': 1, 'World': 3}。
 * 示例 2:
 * 输入: 字符串 = "Welcome4(ToAlibaba(To3)2)2"
 * 输出: "Alibaba2To14Welcome4"
 * 解释: 单词数量是 {'Alibaba': 2,  'To': 14', Welcome': 4,}。
 * 注意:
 * 字符串的长度在[1, 100000]之间。
 * 字符串只包含字母、数字和圆括号，并且题目中给定的都是合法的字符串。
 */
public class Tester2 {
    static Map<String, Integer> wordMap = new HashMap<>();

    public static void main(String[] args) {
        print("Alibaba2To14Welcome4");
    }

    private static void print(String s) {
        dfs(s, 0);
        // 根据字符串排序排序，存入优先级队列
        PriorityQueue<String> queue = new PriorityQueue<>((a, b) -> {
            int compare = a.compareTo(b);
            return compare == 0 ? wordMap.get(a) - wordMap.get(b) : compare;
        });
        // 按顺序保存字符创结果
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < queue.size(); i++) {
            String str = queue.poll();
            int count = wordMap.getOrDefault(str, 0);
            sb.append(str).append(count > 1 ? count + "" : "");
        }
        System.out.println(sb.toString());
    }

    /**
     * 输入
     * "Welcome4(ToAlibaba(To3)2)2"
     * 对字符串解码
     * 输出
     * {'Alibaba': 2,  'To': 14', Welcome': 4,}。
     * 返回数组，0 是下一个索引，1 是出现次数
     */
    private static String[] dfs(String s, int i) {
        StringBuilder res = new StringBuilder();
        int multi = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            // 遇到数字， 累加
            if (c >= '0' && c <= '9') {
                multi = multi * 10 + (c - '0');
            } else if (c == '(') {
                // 左括号，计算之前字符串的长度
                String[] tmp = dfs(s, i + 1);
                String nowStr = res.toString();
                i = Integer.parseInt(tmp[0]);
                int nexCount = Integer.parseInt(tmp[1]);
                // 累积数
                if (multi == 0) {
                    // 当前 res 和之后的字符一起连接, 需要取后面的数字
                    wordMap.put(nowStr, wordMap.getOrDefault(nowStr, 1) * nexCount == 0 ? 1 : nexCount);
                } else {
                    wordMap.put(nowStr, wordMap.getOrDefault(nowStr, 1) * wordMap.get(tmp[0]));
                }
            } else if (c >= 'A' && c <= 'Z') {
                // 单词连续
                if (StringUtils.isNotBlank(res.toString())) {
                    String[] tmp = dfs(s, i + 1);
                    String nowStr = res.toString();
                    i = Integer.parseInt(tmp[0]);
                    int nexCount = Integer.parseInt(tmp[1]);
                    // 累积数
                    if (multi == 0) {
                        // 当前 res 和之后的字符一起连接, 需要取后面的数字
                        wordMap.put(nowStr, wordMap.getOrDefault(nowStr, 1) * nexCount == 0 ? 1 : nexCount);
                    } else {
                        wordMap.put(nowStr, wordMap.getOrDefault(nowStr, 1) * wordMap.getOrDefault(tmp[0], 1));
                    }
                } else {
                    res.append(c);
                }
            } else if (c == ')') {
                // Welcome4(ToAlibaba(To3)2)2
                // 右括号结束
                wordMap.put(res.toString(), multi == 0 ? 1 : multi);
                return new String[]{String.valueOf(i), res.toString(), multi == 0 ? "1" : multi + ""};
            } else {
                // 小写字母
                res.append(c);
            }
            i++;
        }
        return new String[]{s.length() + "", "1"};
    }
}
