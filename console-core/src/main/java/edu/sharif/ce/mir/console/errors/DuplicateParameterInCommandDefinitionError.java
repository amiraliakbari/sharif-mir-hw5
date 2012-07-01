package edu.sharif.ce.mir.console.errors;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (28/2/12, 19:53)
 */
public class DuplicateParameterInCommandDefinitionError extends InvalidCommandDefinitionError {

    public DuplicateParameterInCommandDefinitionError(String name, String definition) {
        super(String.format("Parameter %s has been already introduced in: %s", name, definition));
    }

}
