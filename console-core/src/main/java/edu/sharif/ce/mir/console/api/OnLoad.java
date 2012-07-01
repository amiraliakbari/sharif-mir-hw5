package edu.sharif.ce.mir.console.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allows you to attach a method to the load event of
 * your extensions. This only has meaning in the context of a class
 * annotated as {@link Extension}<br/>
 * <strong>NB</strong> You can have at most one occurrence of {@link OnLoad}
 * in each extension
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 21:59)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnLoad {
}
