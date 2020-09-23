package com.wufish.javalearning.google.di.providers;

import com.google.inject.Provider;
import com.wufish.javalearning.google.di.impl.OneServiceImpl;
import com.wufish.javalearning.google.di.service.IService;

/**
 * @Author wzj
 * @Create time: 2018/06/24 17:20
 * @Description:每次新建一个新的OneService对象出来
 */
public class OneServiceProvider implements Provider<IService>{
    @Override
    public IService get() {
        return new OneServiceImpl();
    }
}
