package com.wzj.javalearning.google.guice.aop;

import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;

/**
 * @Author wzj
 * @Create time: 2018/06/24 17:58
 * @Description:关于此结果有几点说明: <p>
 * （1）由于使用了AOP我们的服务得到的不再是我们写的服务实现类了，而是一个继承的子类，这个子类应该是在内存中完成的。
 * （2）除了第一次调用比较耗时外（可能guice内部做了比较多的处理），其它调用事件为0毫秒（我们的服务本身也没做什么事）。
 * （3）确实完成了我们期待的AOP功能。
 */
public class AopTester {
    @Inject
    private IService service;

    public static void main(String[] args) {
        /*Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bindInterceptor(Matchers.any(), Matchers.annotatedWith(Names.named("log")),
                        new LoggerMethodInterceptor());
            }
        });
        injector.getInstance(AopTester.class).service.sayHello();
        injector.getInstance(AopTester.class).service.sayHello();
        injector.getInstance(AopTester.class).service.sayHello();*/

        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                AfterMethodIntercepter after = new AfterMethodIntercepter();
                binder.requestInjection(after);
                // 第一个参数是匹配类，第二个参数是匹配方法，第三个数组参数是方法拦截器
                binder.bindInterceptor(Matchers.any(), Matchers.annotatedWith(Names.named("log")), after);
            }
        });
        injector.getInstance(AopTester.class).service.sayHello();
    }
}
