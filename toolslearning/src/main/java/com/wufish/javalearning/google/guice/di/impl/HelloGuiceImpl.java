package com.wufish.javalearning.google.guice.di.impl;

import com.google.inject.Singleton;
import com.wufish.javalearning.google.guice.di.service.IHelloGuice;

/**
 * @Author wzj
 * @Create time: 2018/06/24 15:47
 * @Description:
 */

/**
 * 保证是单例
 */
@Singleton
public class HelloGuiceImpl implements IHelloGuice {
    @Override
    public void sayHello() {
        System.out.println("Hello Guice");
    }
}
