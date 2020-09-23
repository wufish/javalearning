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
        System.out.println(convert("Welcome4(ToAlibaba(To3)2)2"));
        System.out.println(convert("Welcome4(ToAlibaba(To)2)2"));
        System.out.println(convert("Welcome4(ToAlibaba2)2"));
        System.out.println(convert("Welcome(ToAlibaba2(To2))2"));
        System.out.println(convert("Welcome(ToAlibaba2(To2)3)2"));
        System.out.println();
    }

    private static String convert(String s) {
        List<Word> dfs = dfs(s, new int[]{0});
        TreeMap<String, Integer> wordMap = new TreeMap<>();
        for (Word df : dfs) {
            wordMap.put(df.word, wordMap.getOrDefault(df.word, 0) + df.num);
        }
        StringBuilder res = new StringBuilder();
        for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
            res.append(entry.getKey()).append(entry.getValue() == 1 ? "" : entry.getValue());
        }
        return res.toString();
    }

    /**
     * 字符串处理成单词列表
     */
    private static List<Word> dfs(String s, int[] start) {
        StringBuilder sb = new StringBuilder();
        List<Word> wordList = new LinkedList<>();
        int multi = 0;
        while (start[0] < s.length()) {
            char c = s.charAt(start[0]);
            if ('0' <= c && c <= '9') {
                multi = 10 * multi + (c - '0');
            } else if (c == '(') {
                if (sb.length() > 0) {
                    wordList.add(new Word(sb.toString(), multi == 0 ? 1 : multi));
                    sb = new StringBuilder();
                    multi = 0;
                }
                start[0]++;
                List<Word> dfs = dfs(s, start);
                for (Word df : dfs) {
                    df.in = true;
                    wordList.add(df);
                }
            } else if (c == ')') {
                if (sb.length() > 0) {
                    wordList.add(new Word(sb.toString(), multi == 0 ? 1 : multi));
                    return wordList;
                }
                for (Word word : wordList) {
                    if (word.in) {
                        word.num *= multi;
                    }
                }
                return wordList;
            } else if ('A' <= c && c <= 'Z') {
                if (sb.length() > 0) {
                    wordList.add(new Word(sb.toString(), multi == 0 ? 1 : multi));
                    sb = new StringBuilder();
                    multi = 0;
                }
                sb.append(c);
            } else {
                sb.append(c);
            }
            start[0]++;
        }
        if (sb.length() > 0) {
            wordList.add(new Word(sb.toString(), multi == 0 ? 1 : multi));
            sb = new StringBuilder();
            multi = 0;
        }
        if (multi > 0) {
            for (Word word : wordList) {
                if (word.in) {
                    word.num *= multi;
                }
            }
        }
        return wordList;
    }

    static class Word {
        String word;
        int num;
        boolean in;

        public Word(String word, int num) {
            this.word = word;
            this.num = num;
        }

        public Word(String word, int num, boolean in) {
            this.word = word;
            this.num = num;
            this.in = in;
        }
    }
}
