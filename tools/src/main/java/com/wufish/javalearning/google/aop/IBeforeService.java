package com.wufish.javalearning.google.aop;

import com.google.inject.ImplementedBy;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @Author wzj
 * @Create time: 2018/06/24 18:07
 * @Description:
 */
@ImplementedBy(BeforeServiceImpl.class)
public interface IBeforeService {
    void before(MethodInvocation invocation);
}
