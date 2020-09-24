package com.wufish.javalearning.google.aop;

import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * @Author wzj
 * @Create time: 2018/06/24 17:54
 * @Description:
 */
@Singleton
public class ServiceImpl implements IService {
    @Named("log")
    @Override
    public void sayHello() {
        System.out.println(String.format("[%s#%d] execute %s at %d", this.getClass().getSimpleName(), hashCode(), "sayHello", System.nanoTime()));
    }
}
