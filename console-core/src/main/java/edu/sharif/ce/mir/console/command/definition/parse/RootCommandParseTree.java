package edu.sharif.ce.mir.console.command.definition.parse;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (28/2/12, 16:59)
 */
public class RootCommandParseTree extends AbstractCommandParseTree {

    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlatString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            if (i > 0) {
                builder.append(" ");
            }
            if (get(i) instanceof TerminalCommandParseTree) {
                builder.append(((TerminalCommandParseTree) get(i)).getValue());
            }
            if (get(i) instanceof ParameterCommandParseTree) {
                builder.append("#");
            }
            if (get(i) instanceof RootCommandParseTree) {
                builder.append(((RootCommandParseTree) get(i)).getFlatString());
            }
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return (!name.isEmpty() && !name.equals("#root") ? "@" + name : "") + super.toString();
    }
}
