package com.wufish.javalearning;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;

/**
 * The type Class utils.
 *
 */
public class ClassUtils {
    private ClassUtils() {
    }

    /**
     * 获取 class 的第 idnex 个泛型的真实参数类
     *
     * @param <T>   the type parameter
     * @param clzz  the clzz
     * @param index the index
     * @return the base generic
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getBaseGeneric(Class<?> clzz, int index) {
        ParameterizedType parameterizedType = (ParameterizedType) clzz.getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[index];
    }

    /**
     * 获取对象声明的字段值
     *
     * @param obj 待处理对象
     * @return the declared field value
     * @throws IllegalAccessException the illegal access exception
     */
    public static Map<String, Object> getFieldsValue(Object obj) throws IllegalAccessException {
        Map<String, Object> result = new HashMap<>();
        Class<?> tempClass = Objects.requireNonNull(obj).getClass();
        while (tempClass != null) { // 当父类为null的时候说明到达了最上层的父类(Object类).
            Field[] fields = tempClass.getDeclaredFields();
            if (ArrayUtils.isNotEmpty(fields)) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    result.put(field.getName(), field.get(obj));
                }
            }
            tempClass = tempClass.getSuperclass(); // 查询父类
        }
        return result;
    }
}
