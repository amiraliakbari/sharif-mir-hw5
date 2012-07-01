package edu.sharif.ce.mir.console.command.matcher;

import edu.sharif.ce.mir.console.command.definition.parse.CommandParseTree;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (29/2/12, 15:59)
 */
public interface CommandMatcher {

    ConsoleCommand match(CommandParseTree parseTree, String command);

}
