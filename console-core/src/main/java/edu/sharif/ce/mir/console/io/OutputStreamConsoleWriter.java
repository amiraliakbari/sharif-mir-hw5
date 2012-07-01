package edu.sharif.ce.mir.console.io;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/3/12, 15:44)
 */
public class OutputStreamConsoleWriter implements ConsoleWriter {

    private OutputStream stream;

    public OutputStreamConsoleWriter(OutputStream stream) {
        this.stream = stream;
    }

    @Override
    public void write(Output output) {
        final PrintStream printStream = new PrintStream(stream);
        printStream.print(output.getValue());
    }

    @Override
    public void writeLine(Output output) {
        PrintStream printStream = stream instanceof PrintStream ? (PrintStream) stream : new PrintStream(stream);
        printStream.println(output.getValue());
    }

}
