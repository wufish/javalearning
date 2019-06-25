package com.wufish.javalearning.google.guice.aop;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @Author wzj
 * @Create time: 2018/06/24 18:08
 * @Description:
 */
public class BeforeServiceImpl implements IBeforeService {
    @Override
    public void before(MethodInvocation invocation) {
        System.out.println("Before--->" + invocation.getClass().getName());
    }
}
