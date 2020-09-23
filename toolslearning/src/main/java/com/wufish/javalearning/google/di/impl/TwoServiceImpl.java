package com.wufish.javalearning.google.di.impl;

import com.wufish.javalearning.google.di.service.IService;

/**
 * @Author wzj
 * @Create time: 2018/06/24 16:58
 * @Description:
 */
public class TwoServiceImpl implements IService {
    @Override
    public void execute() {
        System.out.println("Hello, I'm service 2");
    }
}
