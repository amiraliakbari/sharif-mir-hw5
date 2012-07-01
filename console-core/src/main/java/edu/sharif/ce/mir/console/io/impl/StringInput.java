package edu.sharif.ce.mir.console.io.impl;

import edu.sharif.ce.mir.console.io.Input;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 0:01)
 */
public class StringInput extends AcceptingInput {

    private String value;

    @Override
    public void setValue(String value) {
        value = value;
    }

    public String getValue() {
        return value;
    }
}
