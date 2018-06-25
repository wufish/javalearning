package com.wzj.javalearning.google.guice.di;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.wzj.javalearning.google.guice.di.service.IService;

/**
 * @Author wzj
 * @Create time: 2018/06/24 16:39
 * @Description:
 */
public class FieldInjectDemo {
    @Inject
    private IService service;

    public IService getService() {
        return service;
    }

    public static void main(String[] args) {
        FieldInjectDemo instance = Guice.createInjector().getInstance(FieldInjectDemo.class);
        instance.getService().execute();
    }
}
