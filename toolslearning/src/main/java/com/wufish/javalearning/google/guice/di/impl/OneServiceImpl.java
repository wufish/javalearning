package com.wufish.javalearning.google.guice.di.impl;

import com.wufish.javalearning.google.guice.di.service.IService;

/**
 * @Author wzj
 * @Create time: 2018/06/24 16:57
 * @Description:
 */
public class OneServiceImpl implements IService {
    @Override
    public void execute() {
        System.out.println("Hello, I'm service 1");
    }
}
