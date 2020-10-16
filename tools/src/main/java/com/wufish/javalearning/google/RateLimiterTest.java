package com.wufish.javalearning.google;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @Author wufish
 * @Create time: 2020/6/21 0021
 * @Description:
 */
public class RateLimiterTest {
    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(100);
        double acquire = rateLimiter.acquire();
        rateLimiter.tryAcquire();
    }
}
