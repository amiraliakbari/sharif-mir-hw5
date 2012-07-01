package edu.sharif.ce.mir.console.io.impl;

import edu.sharif.ce.mir.console.io.Input;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 0:03)
 */
public abstract class AcceptingInput implements Input {

    @Override
    public boolean accepts(String string) {
        return true;
    }

}
