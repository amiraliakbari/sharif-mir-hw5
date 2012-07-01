package edu.sharif.ce.mir.console.io;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 23:56)
 */
public interface Input {

    boolean accepts(String string);

    void setValue(String value);

}
