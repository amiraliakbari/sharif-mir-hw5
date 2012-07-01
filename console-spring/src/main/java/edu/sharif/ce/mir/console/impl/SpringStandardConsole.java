package edu.sharif.ce.mir.console.impl;

import org.springframework.core.Ordered;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (12/6/9, 20:33)
 */
public class SpringStandardConsole extends StandardConsole implements Ordered {

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

}
