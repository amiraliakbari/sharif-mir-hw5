package edu.sharif.ce.mir.console.command.definition.parse;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (28/2/12, 16:59)
 */
public class TerminalCommandParseTree extends AbstractCommandParseTree {

    private final String value;

    public TerminalCommandParseTree(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
