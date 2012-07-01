package edu.sharif.ce.mir.console.io.impl;

import edu.sharif.ce.mir.console.io.Output;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 3:07)
 */
public class PrimitiveOutput implements Output {

    private String value;

    public PrimitiveOutput(Object value) {
        this.value = value == null ? "null" : value.toString();
    }

    public PrimitiveOutput(double value) {
        this.value = Double.toString(value);
    }

    public PrimitiveOutput(int value) {
        this.value = Integer.toString(value);
    }

    public PrimitiveOutput(long value) {
        this.value = Long.toString(value);
    }

    public PrimitiveOutput(short value) {
        this.value = Short.toString(value);
    }

    public PrimitiveOutput(char value) {
        this.value = Character.toString(value);
    }

    public PrimitiveOutput(boolean value) {
        this.value = Boolean.toString(value);
    }

    @Override
    public String getValue() {
        return value;
    }
    
    public int getLength() {
        return getValue().length();
    }

}
