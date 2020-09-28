package com.wufish.javalearning.google.di;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.wufish.javalearning.google.di.service.IService;

/**
 * @Author wzj
 * @Create time: 2018/06/24 16:31
 * @Description:
 */
public class SetterInjectDemo {
    private IService service;

    public IService getService() {
        return service;
    }

    @Inject
    public void setService(IService service) {
        this.service = service;
    }

    public static void main(String[] args) {
        SetterInjectDemo instance = Guice.createInjector().getInstance(SetterInjectDemo.class);
        instance.getService().execute();
    }
}
