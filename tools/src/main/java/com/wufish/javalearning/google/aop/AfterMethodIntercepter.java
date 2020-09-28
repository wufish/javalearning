package com.wufish.javalearning.google.aop;

import com.google.inject.Inject;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @Author wzj
 * @Create time: 2018/06/24 18:10
 * @Description:
 */
public class AfterMethodIntercepter implements MethodInterceptor {
    @Inject
    private IBeforeService beforeService;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        beforeService.before(invocation);
        Object obj = null;

        try {
            obj = invocation.proceed();
        } finally {
            System.out.println("after--->" + invocation.getClass().getName());
        }
        return obj;
    }
}
