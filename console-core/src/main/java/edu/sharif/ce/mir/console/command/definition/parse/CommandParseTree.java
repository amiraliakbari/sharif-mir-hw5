package edu.sharif.ce.mir.console.command.definition.parse;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (28/2/12, 17:05)
 */
public interface CommandParseTree {

    int add(CommandParseTree tree);

    int size();

    CommandParseTree get(int i);
    
    CommandParseTree getParent();

    void setParent(CommandParseTree parent);

    void walk(CommandParseTreeCallback callback);

}
