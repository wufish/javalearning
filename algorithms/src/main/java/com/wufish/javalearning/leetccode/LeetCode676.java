package com.wufish.javalearning.leetccode;

import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.cn/problems/implement-magic-dictionary/submissions/
 *
 * @author wuzhijun05 <wuzhijun05@kuaishou.com>
 * Created on 2022-07-11
 */
public class LeetCode676 {
    public static void main(String[] args) {
        MagicDictionary magicDictionary = new MagicDictionary();
        magicDictionary.buildDict(new String[]{"hello","hallo","leetcode"});
        boolean res1 = magicDictionary.search("hello");
        boolean res2 = magicDictionary.search("hhllo");
        boolean res3 = magicDictionary.search("hell");
        boolean res4 = magicDictionary.search("lettcoded");
        System.out.println();
    }

    static class MagicDictionary {
        TrieNode root = new TrieNode();

        public void buildDict(String[] dictionary) {
            for (String dict : dictionary) {
                TrieNode cur = root;
                for (int i = 0; i < dict.length(); i++) {
                    Character c = dict.charAt(i);
                    cur.charMap.putIfAbsent(c, new TrieNode());
                    cur = cur.charMap.get(c);
                }
                cur.charMap.put('#', new TrieNode());
            }
        }

        public boolean search(String searchWord) {
            // 递归搜索
            return dfs(root, searchWord, 0, false);
        }

        private boolean dfs(TrieNode root, String searchWord, int index, boolean hasDiff) {
            if (index == searchWord.length()) {
                return hasDiff && root.isEnd();
            }
            TrieNode cur = root;
            Character c = searchWord.charAt(index);
            if (cur.charMap.containsKey(c)) {
                if (dfs(cur.charMap.get(c), searchWord, index + 1, hasDiff)) {
                    return true;
                }
            }
            if (hasDiff) {
                return false;
            }
            for (Map.Entry<Character, TrieNode> entry : cur.charMap.entrySet()) {
                if (entry.getKey() != c && dfs(entry.getValue(), searchWord, index + 1, true)) {
                    return true;
                }
            }
            return false;
        }

        class TrieNode {
            public Map<Character, TrieNode> charMap = new HashMap<>();

            public boolean isEnd() {
                return charMap.containsKey('#');
            }
        }
    }
}
