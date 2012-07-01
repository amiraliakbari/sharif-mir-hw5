package edu.sharif.ce.mir.utils.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 22:51)
 */
public abstract class ReflectionUtils {

    public static Map<String, Method> getMethods(Class<?> clazz) {
        return getMethods(clazz, new MethodFilter.INCLUDE_ALL());
    }
    
    public static Map<String, Method> getMethods(Class<?> clazz, MethodFilter filter) {
        final HashMap<String, Method> methods = new HashMap<String, Method>();
        while (clazz != null) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (methods.containsKey(method.getName())) {
                    continue;
                }
                if (!filter.matches(method)) {
                    continue;
                }
                methods.put(method.getName(), method);
            }
            clazz = clazz.getSuperclass();
        }
        return methods;
    }

    public static Method getAccessor(Class<?> clazz, String propertyName) throws NoSuchFieldException {
        final String name = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        final Map<String, Method> methods = getMethods(clazz, new MethodFilter() {

            @Override
            public boolean matches(Method method) {
                return (method.getParameterTypes().length == 0) && Modifier.isPublic(method.getModifiers()) && ((method.getReturnType().isPrimitive() && method.getReturnType().getName().matches("boolean") && method.getName().equals("is" + name))
                        || (method.getName().equals("get" + name)));
            }

        });
        if (methods.isEmpty()) {
            throw new NoSuchFieldException(propertyName);
        }
        return methods.get(methods.keySet().iterator().next());
    }

}
