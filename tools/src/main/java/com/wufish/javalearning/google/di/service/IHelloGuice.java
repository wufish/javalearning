package com.wufish.javalearning.google.di.service;

import com.google.inject.ImplementedBy;
import com.wufish.javalearning.google.di.impl.HelloGuiceImpl;

/**
 * @Author wzj
 * @Create time: 2018/06/24 15:44
 * @Description:
 */
@ImplementedBy(HelloGuiceImpl.class)
public interface IHelloGuice {
    public void sayHello();
}
