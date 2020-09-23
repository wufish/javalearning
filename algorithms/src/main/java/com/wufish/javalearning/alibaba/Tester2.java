package com.wufish.javalearning.alibaba;

import java.util.*;

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
        System.out.println(getStatistic("Welcome4(ToAlibaba(To3)2)2"));
        System.out.println(getStatistic("Welcome4(ToAlibaba(To3)2)Hello2"));
        System.out.println(getStatistic("World3Hello"));
    }

    private static String getStatistic(String s) {
        List<Pair> dfs = dfs(s, new int[]{0});
        TreeMap<String, Integer> map = new TreeMap<>();
        for (Pair df : dfs) {
            map.put(df.word, map.getOrDefault(df.word, 0) + df.num);
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(entry.getValue() == 1 ? "" : entry.getValue());
        }
        return sb.toString();
    }

    /**
     * 深度遍历, 获取从当前左括号到匹配的括号之间单词的个数
     * start 作为索引，引用传递，用于保存当前位置
     * <p>
     * Welcome4(ToAlibaba(To3)2)2
     * World3Hello
     */
    private static List<Pair> dfs(String s, int[] start) {
        int multi = 0;
        StringBuilder sb = new StringBuilder();
        List<Pair> list = new ArrayList<>();
        while (start[0] < s.length()) {
            char c = s.charAt(start[0]);
            if ('0' <= c && c <= '9') {
                // 数字
                multi = 10 * multi + (c - '0');
            } else if (c == '(') {
                if (sb.length() > 0) {
                    list.add(new Pair(sb.toString(), multi == 0 ? 1 : multi));
                    sb = new StringBuilder();
                    multi = 0;
                }
                // 括号内键值对
                start[0]++;
                List<Pair> pairs = dfs(s, start);
                for (Pair pair : pairs) {
                    pair.in = true;
                    list.add(pair);
                }
            } else if (c == ')') {
                // 数字之前存在单词
                if (sb.length() > 0) {
                    list.add(new Pair(sb.toString(), multi == 0 ? 1 : multi));
                    return list;
                }
                // 对数字前面的单词计数
                if (multi != 0) {
                    for (Pair pair : list) {
                        if (pair.in) {
                            pair.num *= multi;
                        }
                    }
                }
                return list;
            } else if ('A' <= c && c <= 'Z') {
                // 大写字母
                if (sb.length() > 0) {
                    list.add(new Pair(sb.toString(), multi == 0 ? 1 : multi));
                    sb = new StringBuilder();
                    multi = 0;
                }
                sb.append(c);
            } else {
                // 小写字母
                sb.append(c);
            }
            start[0]++;
        }
        if (sb.length() > 0) {
            list.add(new Pair(sb.toString(), multi == 0 ? 1 : multi));
        } else if (multi != 0) {
            // 最后已数字结尾
            for (Pair pair : list) {
                if (pair.in) {
                    pair.num *= multi;
                }
            }
        }
        return list;
    }

    private static class Pair {
        // 单词
        String word;
        // 单词计数
        int num;
        // 是否在括号内
        boolean in;

        public Pair(String word, int num) {
            this.word = word;
            this.num = num;
        }

        public Pair(String word, int num, boolean in) {
            this.word = word;
            this.num = num;
            this.in = in;
        }
    }
}
