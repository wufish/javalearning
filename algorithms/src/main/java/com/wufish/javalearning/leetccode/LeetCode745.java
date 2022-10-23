package com.wufish.javalearning.leetccode;

import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.cn/problems/prefix-and-suffix-search/submissions/
 *
 * @author wuzhijun05 <wuzhijun05@kuaishou.com>
 * Created on 2022-07-14
 */
public class LeetCode745 {
    public static void main(String[] args) {
        WordFilter wordFilter = new WordFilter(new String[]{
                "c", "c", "c", "i", "c", "c", "c", "c", "c", "i", "c", "c", "c", "c",
                "c", "i", "c", "i", "c", "c", "c", "c", "c", "c", "c", "i", "c", "c",
                "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c",
                "c", "c", "c", "c", "c", "c", "c", "c", "i", "c", "c", "c", "c", "c",
                "c", "c", "c", "c", "c", "c", "c", "c", "i", "c", "c", "c", "c", "c",
                "c", "i", "c", "c", "c", "c", "i", "i", "c", "c", "c", "c", "c", "c",
                "c", "c", "i", "c", "i", "c", "c", "c", "c", "c", "c", "c", "c", "c",
                "c", "c", "c", "c", "c", "c", "c", "i", "c", "c", "c", "c", "c", "c",
                "i", "c", "i", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c",
                "c", "c", "i", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c",
                "c", "c", "c", "c", "c", "c", "c", "i", "c", "c", "c", "i", "c", "c"});
        int f = wordFilter.f("c", "c");
        System.out.println();
    }

    static class WordFilter {
        Map<String, Integer> dictionary;

        public WordFilter(String[] words) {
            dictionary = new HashMap<>();
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                int m = word.length();
                for (int prefixLength = 1; prefixLength <= m; prefixLength++) {
                    for (int suffixLength = 1; suffixLength <= m; suffixLength++) {
                        dictionary.put(word.substring(0, prefixLength) + "#" + word.substring(m - suffixLength), i);
                    }
                }
            }
        }

        public int f(String pref, String suff) {
            return dictionary.getOrDefault(pref + "#" + suff, -1);
        }
    }
}
