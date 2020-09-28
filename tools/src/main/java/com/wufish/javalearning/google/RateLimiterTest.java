package com.wufish.javalearning.google;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @Author wzj@58ganji.com
 * @Create time: 2020/6/21 0021
 * @Description:
 */
public class RateLimiterTest {
    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(4);
        double acquire = rateLimiter.acquire();
    }
}
