package edu.sharif.ce.mir.utils.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 22:56)
 */
public class AnnotationMethodFilter implements MethodFilter {

    private final Class<? extends Annotation> annotation;

    public AnnotationMethodFilter(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean matches(Method method) {
        return method.isAnnotationPresent(annotation);
    }
}
