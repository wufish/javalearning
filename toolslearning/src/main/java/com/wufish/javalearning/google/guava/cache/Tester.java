package com.wufish.javalearning.google.guava.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author 58
 * @Create time: 2020/7/1 17:17
 * @Description:
 */
public class Tester {
    public static void main(String[] args) throws ExecutionException {
        LoadingCache<Object, Object> cache1 = CacheBuilder.newBuilder()
                .maximumSize(100000)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build(new CacheLoader<Object, Object>() {
                    @Override
                    public Object load(Object o) throws Exception {
                        return null;
                    }
                });

        Cache<Object, Object> cache2 = CacheBuilder.newBuilder()
                .maximumSize(100000).build();
        cache2.get("", () -> {
            return "";
        });
    }
}
