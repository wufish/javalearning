package com.wufish.javalearning.google.di;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.wufish.javalearning.google.di.annotations.One;
import com.wufish.javalearning.google.di.annotations.Two;
import com.wufish.javalearning.google.di.impl.OneServiceImpl;
import com.wufish.javalearning.google.di.impl.TwoServiceImpl;
import com.wufish.javalearning.google.di.service.IService;

/**
 * @Author wzj
 * @Create time: 2018/06/24 17:05
 * @Description:此类的结构是注入了两个service服务，注解One和OneService关联，第二个和它一样
 */
public class MultiInterfaceImplDemo {
    @Inject
    @One
    private IService oneService;

    @Inject
    @Two
    private IService twoService;

    public static void main(String[] args) {
        MultiInterfaceImplDemo instance = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(IService.class).annotatedWith(One.class).to(OneServiceImpl.class);
                binder.bind(IService.class).annotatedWith(Two.class).to(TwoServiceImpl.class);
            }
        }).getInstance(MultiInterfaceImplDemo.class);
        instance.oneService.execute();
        instance.twoService.execute();
    }
}
