package edu.sharif.ce.mir.console.errors;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (28/2/12, 16:27)
 */
public class UnclosedBracketInCommandDefinition extends InvalidCommandDefinitionError {

    public UnclosedBracketInCommandDefinition(String definition, int position) {
        super("Unclosed bracket in definition. The opening was at " + position + " in: " + trim(definition, position));
    }

}
