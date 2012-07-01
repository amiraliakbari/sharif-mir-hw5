package edu.sharif.ce.mir.console.errors;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 2:42)
 */
public class ExtensionAlreadyLoadedException extends Exception {

    public ExtensionAlreadyLoadedException(String name) {
        super("Extension already loaded: " + name);
    }

}
