package com.wzj.javalearning.google.guice.di;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.wzj.javalearning.google.guice.di.impl.OneServiceImpl;
import com.wzj.javalearning.google.guice.di.impl.TwoServiceImpl;
import com.wzj.javalearning.google.guice.di.service.IService;

/**
 * @Author wzj
 * @Create time: 2018/06/24 17:12
 * @Description:不想写注解来区分多个服务则可以使用Google提供的一个叫Names的模板来生成注解
 */
public class NoAnnotationMultiInterfaceImplDemo {
    @Inject
    @Named("One")
    private static IService oneService;

    @Inject
    @Named("Two")
    private static IService twoService;

    public static void main(String[] args) {
        Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(IService.class).annotatedWith(Names.named("One")).to(OneServiceImpl.class);
                binder.bind(IService.class).annotatedWith(Names.named("Two")).to(TwoServiceImpl.class);
                // 静态注入
                binder.requestStaticInjection(NoAnnotationMultiInterfaceImplDemo.class);
            }
        }).getInstance(NoAnnotationMultiInterfaceImplDemo.class);
        NoAnnotationMultiInterfaceImplDemo.oneService.execute();
        NoAnnotationMultiInterfaceImplDemo.twoService.execute();
    }
}
