package edu.sharif.ce.mir.utils.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/3/12, 15:54)
 */
public class BeanAccessor {

    private Object bean;

    public BeanAccessor(Object bean) {
        this.bean = bean;
    }

    public <T> T getProperty(String name, Class<T> type) throws NoSuchFieldException, InvocationTargetException {
        final Method accessor = ReflectionUtils.getAccessor(bean.getClass(), name);
        if (!type.isAssignableFrom(accessor.getReturnType())) {
            throw new IllegalArgumentException("Expected <" + accessor.getReturnType().getName() + "> while found <" + type.getName() + ">");
        }
        try {
            //noinspection unchecked
            return (T) accessor.invoke(bean);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public Object getProperty(String name) throws NoSuchFieldException, InvocationTargetException {
        final Method accessor = ReflectionUtils.getAccessor(bean.getClass(), name);
        try {
            return accessor.invoke(bean);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

}
