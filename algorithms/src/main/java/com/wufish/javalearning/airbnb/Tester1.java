package com.wufish.javalearning.airbnb;

import java.util.ArrayList;
import java.util.Stack;

import com.google.common.collect.Lists;

/**
 * @Author wzj
 * @Create time: 2020/8/24 0024
 * @Description:
 */
public class Tester1 {
    public static void main(String[] args) {
        ArrayList<String> xmls = Lists.newArrayList(
                "abc",
                "abc<a></a>",
                "abc<a></>",
                "abc<a></b>",
                "abc<b></a>",
                "abc<a></a><b></b>",
                "abc<b><a>",
                "abc<b>a>"
        );
        for (String xml : xmls) {
            System.out.println(xmlValid(xml));
        }
        System.out.println();
    }

    private static String xmlValid(String xml) {
        int len = xml.length();
        int i = 0;
        Stack<String> stk = new Stack<>();
        boolean leftStart = false;
        while (i < len) {
            char c = xml.charAt(i);
            int start = i;
            if ('<' == c) {
                leftStart = true;
                i++;
                if (i >= len) {
                    return "parser error";
                }
                boolean leftTag = true;
                if ('/' == xml.charAt(i)) {
                    // rigth
                    leftTag = false;
                    i++;
                    if (i >= len) {
                        return "parser error";
                    }
                }
                boolean matched = false;
                while (i < len) {
                    if ('>' == xml.charAt(i)) {
                        leftStart = false;
                        if ((leftTag && i == start + 1) || !leftTag && i == start + 2) {
                            // empty tag;
                            return "parser error";
                        } else {
                            matched = true;
                            break;
                        }
                    }
                    i++;
                }
                if (!matched) {
                    return "parser error";
                }
                String tag = xml.substring(start, i + 1);
                if (leftTag) {
                    stk.push(tag);
                } else if (stk.isEmpty() || !tag.substring(2).equals(stk.pop().substring(1))) {
                    return String.format("start tag miss %s", tag);
                }
            }
            if(i == len - 1 && '>' == c){
                // 最后一个是>
                return "parser error";
            }
            i++;
        }
        // 处理没有< 的情况
        if (stk.isEmpty()) {
            return "valid";
        }
        return String.format("end tag miss %s", stk.pop());
    }
}
