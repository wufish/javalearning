package com.wufish.javalearning.java8;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableList;

/**
 * @author wuzhijun05 <wuzhijun05@kuaishou.com>
 * Created on 2022-06-30
 */
public class LearnStream {
    private static final Pattern PHOTO_PAGE_REGEX_PATTERN = Pattern.compile("([a-z]+)(\\d+)([a-z]*)(\\$s)?");
    private static final Pattern REASON_PATTERN = Pattern.compile("[a-z]+(\\d+)[a-z]*(\\$s)?");
    public static void main(String[] args) {
        List<String> collect = ImmutableList.of("java8", "stream", "source", "code")
                .stream()
                .filter(i -> i.length() > 4)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        int compare = Double.compare(100.0 * 1000 / 2000, 1.0 * 50);
        String expTag = "1_i/2001950422452437874_bs1299le";
        String ppReason = parseRecommendReason(expTag);
        String pp = parsePhotoPage(ppReason);
        int reason = parseRecoReason(ppReason);
        System.out.println();
    }

    public static String parseRecommendReason(String expTag) {
        return subStringAfterChar(subStringAfterChar(expTag, '/'), '_');
    }

    private static String subStringAfterChar(String str, char ch) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        int i = str.lastIndexOf(ch);
        if (i < 0) {
            return null;
        }
        String result = str.substring(i + 1);
        return StringUtils.stripToNull(result);
    }

    public static String parsePhotoPage(String recommendReason) {
        if (StringUtils.isEmpty(recommendReason)) {
            return null;
        }

        Matcher matcher = PHOTO_PAGE_REGEX_PATTERN.matcher(recommendReason);
        if (!matcher.find()) {
            return null;
        }

        return matcher.group(1);
    }

    public static int parseRecoReason(String recommendReason) {
        if (StringUtils.isBlank(recommendReason)) {
            return -1;
        }
        Matcher matcher = REASON_PATTERN.matcher(recommendReason);
        if (!matcher.matches()) {
            return -1;
        }
        try {
            return Integer.parseInt(matcher.group(1));
        } catch (Exception e) {
            return -1;
        }
    }
}
