package com.wufish.javalearning.google.aop;

import com.google.inject.ImplementedBy;

/**
 * @Author wzj
 * @Create time: 2018/06/24 17:53
 * @Description:
 */
@ImplementedBy(ServiceImpl.class)
public interface IService {
    public void sayHello();
}
