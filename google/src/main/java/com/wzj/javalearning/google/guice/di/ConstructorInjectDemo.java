package com.wzj.javalearning.google.guice.di;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.wzj.javalearning.google.guice.di.service.IHelloGuice;
import com.wzj.javalearning.google.guice.di.service.IService;

/**
 * @Author wzj
 * @Create time: 2018/06/24 15:48
 * @Description:构造器注入, 支持多参数
 */
public class ConstructorInjectDemo {
    private com.wzj.javalearning.google.guice.di.service.IService IService;
    private IHelloGuice helloGuice;

    public IService getIService() {
        return IService;
    }

    public IHelloGuice getHelloGuice() {
        return helloGuice;
    }

    @Inject
    public ConstructorInjectDemo(IService IService, IHelloGuice helloGuice) {
        this.IService = IService;
        this.helloGuice = helloGuice;
    }

    public static void main(String[] args) {
        ConstructorInjectDemo instance = Guice.createInjector().getInstance(ConstructorInjectDemo.class);
        instance.getIService().execute();
        instance.getHelloGuice().sayHello();
    }
}
