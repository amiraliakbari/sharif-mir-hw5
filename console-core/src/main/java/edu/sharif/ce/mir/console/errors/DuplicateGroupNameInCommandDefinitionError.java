package edu.sharif.ce.mir.console.errors;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (28/2/12, 20:09)
 */
public class DuplicateGroupNameInCommandDefinitionError extends InvalidCommandDefinitionError {

    public DuplicateGroupNameInCommandDefinitionError(String name, String definition) {
        super(String.format("Duplicate group name `%s` in %s", name, definition));
    }

}
