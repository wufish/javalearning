package com.wufish.javalearning.java8;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

/**
 * @author wuzhijun05 <wuzhijun05@kuaishou.com>
 * Created on 2022-06-30
 */
public class LearnStream {
    public static void main(String[] args) {
        List<String> collect = ImmutableList.of("java8", "stream", "source", "code")
                .stream()
                .filter(i -> i.length() > 4)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        int compare = Double.compare(100.0 * 1000 / 2000, 1.0 * 50);
        System.out.println();
    }
}
