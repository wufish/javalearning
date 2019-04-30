package com.wufish.javalearning.google.guice.di;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import java.util.Properties;

/**
 * The type Constant inject demo.
 *
 * @Author wzj
 * @Create time : 2018/06/24 17:45
 * @Description: 1.绑定基本类型外  2. 绑定Properties 还可以绑定一个Properties到Guice中，当然了，由于Properties本质上时一个Map<String,String>， 因此Guice也允许绑定一个Map<String,String>。
 */
public class ConstantInjectDemo {
    /**
     * 1. 绑定基本类型
     */
    /*@Inject
    @Named("a")
    private int a;

    public static void main(String[] args) {
        ConstantInjectDemo instance = Guice.createInjector(new Module() {

            @Override
            public void configure(Binder binder) {
                // 将数值22绑定到常量值a上
                binder.bindConstant().annotatedWith(Names.named("a")).to(22);

            }
        }).getInstance(ConstantInjectDemo.class);
        System.out.println(instance.a);// 22
    }*/

    /**
     * 2. 绑定Properties 还可以绑定一个Properties到Guice中，当然了，由于Properties本质上时一个Map<String,String>， 因此Guice也允许绑定一个Map<String,String>。
     */
    @Inject
    @Named("csdn")
    private String csdn;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        ConstantInjectDemo instance = Guice.createInjector(new Module() {

            @Override
            public void configure(Binder binder) {
                Properties properties = new Properties();
                properties.setProperty("csdn", "www.csdn.com");
                Names.bindProperties(binder, properties);
            }
        }).getInstance(ConstantInjectDemo.class);
        System.out.println(instance.csdn);
    }
}
