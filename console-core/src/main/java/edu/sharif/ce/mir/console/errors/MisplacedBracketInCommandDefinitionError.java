package edu.sharif.ce.mir.console.errors;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (28/2/12, 16:18)
 */
public class MisplacedBracketInCommandDefinitionError extends InvalidCommandDefinitionError {

    public MisplacedBracketInCommandDefinitionError(String definition, int position) {
        super("Unexpected opening at " + position + " in: " + trim(definition, position));
    }
}
