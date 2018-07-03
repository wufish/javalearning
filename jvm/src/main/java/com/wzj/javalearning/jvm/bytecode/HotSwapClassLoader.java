package com.wzj.javalearning.jvm.bytecode;

/**
 * @Author wzj
 * @Create time: 2018/07/03 14:12
 * @Description: 为了多次载入执行类而加入的加载器
 */
public class HotSwapClassLoader extends ClassLoader {
    public HotSwapClassLoader() {
        super(HotSwapClassLoader.class.getClassLoader());
    }

    public Class loadByte(byte[] classByte) {
        return defineClass(null, classByte, 0, classByte.length);
    }
}
