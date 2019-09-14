package com.wufish.javalearning.swordoffer.ch02;

/**
 * ## 替换空格
 * <p>
 * ### 题目描述
 * 请实现一个函数，将一个字符串中的每个空格替换成 `%20`。
 * 例如，当字符串为 `We Are Happy`，则经过替换之后的字符串为 `We%20Are%20Happy`。
 */
public class Q05_ReplaceSpaces {
    /**
     * 测试用例
     * 输入的字符串包含空格（空格位于字符串的最前面/最后面/中间；字符串有多个连续的空格）；
     * 输入的字符串中没有空格；
     * 特殊输入测试（字符串是一个空指针；字符串是一个空字符串；字符串只有一个空格字符；字符串中有多个连续空格）。
     *
     * @param args
     */
    public static void main(String[] args) {
    }

    /**
     * StringBuilder
     *
     * @param str
     * @return
     */
    public String replaceSpace1(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            sb.append(c == ' ' ? "%20" : c);
        }
        return sb.toString();
    }

    /**
     * @param str
     * @return
     */
    public String replaceSpace2(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(str);
        int length = stringBuffer.length();
        for (int i = 0; i < length; i++) {
            if(stringBuffer.charAt(i)==' '){
                stringBuffer.append("  ");
            }
        }
        // p 指向原字符串末尾
        int p = length - 1;

        // q 指向现字符串末尾
        int q = stringBuffer.length() - 1;

        while (p >= 0) {
            char ch = stringBuffer.charAt(p--);
            if (ch == ' ') {
                stringBuffer.setCharAt(q--, '0');
                stringBuffer.setCharAt(q--, '2');
                stringBuffer.setCharAt(q--, '%');
            } else {
                stringBuffer.setCharAt(q--, ch);
            }
        }
        return stringBuffer.toString();
    }
}
