package edu.sharif.ce.mir.console.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/3/12, 15:45)
 */
public class CacheConsoleWriter implements ConsoleWriter {

    private List<Output> value = new ArrayList<Output>();

    public List<Output> getValue() {
        return value;
    }

    @Override
    public void write(Output output) {
        value.add(output);
    }

    @Override
    public void writeLine(Output output) {
        value.add(output);
    }
}
