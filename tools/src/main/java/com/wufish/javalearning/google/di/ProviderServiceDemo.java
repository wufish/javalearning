package com.wufish.javalearning.google.di;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.wufish.javalearning.google.di.providers.OneServiceProvider;
import com.wufish.javalearning.google.di.service.IService;

/**
 * @Author wzj
 * @Create time: 2018/06/24 17:21
 * @Description: 1.如果不想使用Module手动关联服务的话，可以使用@ProviderBy注解。
 * 2.可以注入Provider而不是注入服务,由于我们OneServiceProvider每次都是构造一个新的服务出来，
 * 因此在类ProviderServiceDemo3中的provider每次获取的服务也是不一样的。
 */
public class ProviderServiceDemo {
    /*@Inject
    private IService service;

    public static void main(String[] args) {
        ProviderServiceDemo instance = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(IService.class).toProvider(OneServiceProvider.class);
            }
        }).getInstance(ProviderServiceDemo.class);
        instance.service.execute();
    }*/
    @Inject
    private Provider<IService> provider;

    public static void main(String[] args) {
        ProviderServiceDemo instance = Guice.createInjector(binder -> binder.bind(IService.class).toProvider(OneServiceProvider.class)).getInstance(ProviderServiceDemo.class);
        instance.provider.get().execute();
    }
}
