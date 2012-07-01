package edu.sharif.ce.mir.console.command.definition;

import edu.sharif.ce.mir.console.command.definition.parse.CommandParseTree;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (28/2/12, 15:53)
 */
public interface CommandDefinitionParser {

    CommandParseTree parse(String definition);

}
