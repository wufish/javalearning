package com.wufish.javalearning.jvm.bytecode;

/**
 * The type Hot swap class loader.
 *
 * @Author wzj
 * @Create time : 2018/07/03 14:12
 * @Description: 为了多次载入执行类而加入的加载器
 */
public class HotSwapClassLoader extends ClassLoader {
    /**
     * Instantiates a new Hot swap class loader.
     */
    public HotSwapClassLoader() {
        super(HotSwapClassLoader.class.getClassLoader());
    }

    /**
     * Load byte class.
     *
     * @param classByte the class byte
     * @return the class
     */
    public Class loadByte(byte[] classByte) {
        return defineClass(null, classByte, 0, classByte.length);
    }
}
