package com.wufish.javalearning.google.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @Author wzj
 * @Create time: 2018/06/24 17:55
 * @Description:自定义的方法拦截器，用于输出方法的执行时间
 */
public class LoggerMethodInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String name = invocation.getMethod().getName();
        long startTime = System.nanoTime();
        System.out.println(String.format("before method[%s] at %s", name, startTime));

        Object obj = null;
        try {
            // 执行服务
            obj = invocation.proceed();
        } finally {
            long endTime = System.nanoTime();
            System.out.println(String.format("after method[%s] at %s, cost(ns):%d", name, endTime, (endTime - startTime)));
        }
        return obj;
    }
}
