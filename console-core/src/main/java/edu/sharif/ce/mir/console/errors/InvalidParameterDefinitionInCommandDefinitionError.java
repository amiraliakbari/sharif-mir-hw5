package edu.sharif.ce.mir.console.errors;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (28/2/12, 18:54)
 */
public class InvalidParameterDefinitionInCommandDefinitionError extends InvalidCommandDefinitionError {

    public InvalidParameterDefinitionInCommandDefinitionError(String definition, String parameter, int position) {
        super("Invalid parameter definition `" + parameter + "` at " + position + " in: " + trim(definition, position));
    }

}
