package edu.sharif.ce.mir.console.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allows you to dynamically mark a class definition as a
 * console extension. You can, optionally, specify a namespace which would
 * precede the commands separated by a colon (<code>:</code>).
 * @see edu.sharif.ce.mir.console.Console
 * @see Command
 * @see OnLoad
 * @see OnUnload
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (29/2/12, 18:22)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Extension {

    public String namespace() default "";

}
