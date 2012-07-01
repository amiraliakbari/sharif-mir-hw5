package edu.sharif.ce.mir.console.errors;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (28/2/12, 16:10)
 */
public class BracketWithoutOpeningInCommandDefinitionError extends InvalidCommandDefinitionError {

    public BracketWithoutOpeningInCommandDefinitionError(String definition, int position) {
        super("Bracket was closed too soon at " + position + " in: " + trim(definition, position));
    }

}
