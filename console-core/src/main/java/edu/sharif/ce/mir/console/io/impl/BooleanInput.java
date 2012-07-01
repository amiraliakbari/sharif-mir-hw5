package edu.sharif.ce.mir.console.io.impl;

import edu.sharif.ce.mir.console.io.Input;
import edu.sharif.ce.mir.utils.ArrayUtils;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 3:14)
 */
public class BooleanInput implements Input {

    private boolean value;

    @Override
    public boolean accepts(String string) {
        return ArrayUtils.contains("", new String[]{"y", "yes", "true", "n", "no", "false"});
    }

    @Override
    public void setValue(String value) {
        this.value = ArrayUtils.contains(value, new String[]{"y", "yes", "true"});
    }

    public boolean getValue() {
        return value;
    }
}
