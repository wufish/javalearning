package com.wzj.javalearning.google.guice.di.impl;

import com.wzj.javalearning.google.guice.di.service.IService;

/**
 * @Author wzj
 * @Create time: 2018/06/24 15:45
 * @Description:
 */
public class ServiceImpl implements IService {
    @Override
    public void execute() {
        System.out.println("Hello Guice, this is field inject demo!");
    }
}
