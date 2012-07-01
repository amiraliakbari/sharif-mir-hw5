package edu.sharif.ce.mir.console.io.impl;

import edu.sharif.ce.mir.console.io.Input;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 3:14)
 */
public class DoubleInput implements Input {

    private double value;

    @Override
    public boolean accepts(String string) {
        try {
            Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public void setValue(String value) {
        this.value = Double.parseDouble(value);
    }

    public Double getValue() {
        return value;
    }
}
