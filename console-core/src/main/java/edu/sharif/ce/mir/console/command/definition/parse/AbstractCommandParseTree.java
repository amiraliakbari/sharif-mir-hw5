package edu.sharif.ce.mir.console.command.definition.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (28/2/12, 16:59)
 */
public abstract class AbstractCommandParseTree implements CommandParseTree {

    private List<CommandParseTree> items = new ArrayList<CommandParseTree>();
    private CommandParseTree parent = null;

    @Override
    public int add(CommandParseTree tree) {
        if (tree != null) {
            tree.setParent(this);
        }
        items.add(tree);
        return items.size() - 1;
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public CommandParseTree get(int i) {
        return items.get(i);
    }

    @Override
    public CommandParseTree getParent() {
        return parent;
    }

    @Override
    public void setParent(CommandParseTree parent) {
        this.parent = parent;
    }

    @Override
    public void walk(CommandParseTreeCallback callback) {
        callback.execute(this);
        for (int i = 0; i < size(); i ++) {
            if (get(i) != null) {
                get(i).walk(callback);
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0, itemsSize = items.size(); i < itemsSize; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(items.get(i));
        }
        builder.append("]");
        return builder.toString();
    }
}
