package com.wufish.javalearning.alibaba;

import lombok.ToString;

import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

/**
 * @Author 58
 * @Create time: 2020/9/17 21:42
 * @Description:
 */
public class Tester2_1 {
    public static void main(String[] args) {
        String res = find("Welcome4(ToAlibaba(To3)2)2");
        System.out.println(res);
    }

    public static String find(String s) {
        if (s == null || s.length() < 1) {
            throw new IllegalArgumentException();
        }

        Map<String, Integer> map = new TreeMap<>();

        Stack<Word> stack = new Stack<>();

        int index = 0;
        while (index < s.length()) {
            Word nextWord = findNextWord(s, index);
            index = index + nextWord.len;
            stack.push(nextWord);
        }

        Stack<Word> coefficientStack = new Stack<>();

        int coefficient = 1;
        while (!stack.isEmpty()) {
            Word word = stack.pop();
            if (word.value.equals(")")) {
                coefficient = coefficient * word.num;
                coefficientStack.push(word);
            } else if (word.value.equals("(")) {
                coefficient = coefficient / coefficientStack.pop().num;
            } else {
                map.put(word.value, map.getOrDefault(word.value, 0) + word.num * coefficient);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            sb.append(entry.getKey());
            if (entry.getValue() > 1) {
                sb.append(entry.getValue());
            }
        }
        return sb.toString();
    }

    private static Word findNextWord(String s, int start) {
        if (s.charAt(start) == '(') {
            return new Word("(", 0, 1);
        }
        if (s.charAt(start) == ')') {
            int end = s.length();
            for (int i = start + 1; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c < '0' || c > '9') {
                    end = i;
                    break;
                }
            }
            int num = 1;
            if (end > start + 1) {
                num = Integer.parseInt(s.substring(start + 1, end));
            }
            return new Word(")", num, end - start);
        }

        String temp = "";
        int num = 1;

        int numIndex = 0;
        int end = 0;

        for (int i = start; i < s.length(); i++) {
            // 表示单词开头。
            char c = s.charAt(i);

            if (c >= '0' && c <= '9') {
                if (numIndex == 0) {
                    numIndex = i;
                }
                end = i + 1;
            }

            if (c == '(' || c == ')' || (i != start && c >= 'A' && c <= 'Z')) {
                end = i;
                break;
            }
        }
        if (end == 0) {
            end = s.length();
        }

        if (numIndex > 0) {
            num = Integer.parseInt(s.substring(numIndex, end));
            temp = s.substring(start, numIndex);
        } else {
            temp = s.substring(start, end);
        }
        return new Word(temp, num, end - start);
    }

    @ToString
    private static class Word {
        String value;
        int num;
        int len;

        Word(String value, int num, int len) {
            this.value = value;
            this.num = num;
            this.len = len;
        }
    }
}
