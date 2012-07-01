package edu.sharif.ce.mir.utils.reflection;

import java.lang.reflect.Method;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 22:56)
 */
public interface MethodFilter {

    boolean matches(Method method);

    public static class INCLUDE_ALL implements MethodFilter {

        @Override
        public boolean matches(Method method) {
            return true;
        }
    }

}
