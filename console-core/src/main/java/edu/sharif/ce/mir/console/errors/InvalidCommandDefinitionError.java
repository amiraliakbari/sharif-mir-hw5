package edu.sharif.ce.mir.console.errors;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (28/2/12, 15:52)
 */
public class InvalidCommandDefinitionError extends Error {

    protected static String trim(String definition, int position) {
        return definition.substring(0, position + 1) + (position + 1 < definition.length() ? " ..." : "");
    }

    public InvalidCommandDefinitionError(String msg) {
        super(msg);
    }

}
