package edu.sharif.ce.mir.console.io.impl;

import edu.sharif.ce.mir.console.io.Input;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 3:14)
 */
public class IntegerInput implements Input {

    private int value;

    @Override
    public boolean accepts(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public void setValue(String value) {
        this.value = Integer.parseInt(value);
    }

    public int getValue() {
        return value;
    }
}
