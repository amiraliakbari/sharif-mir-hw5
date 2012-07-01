package edu.sharif.ce.mir.console.errors;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 2:44)
 */
public class DuplicateExtensionException extends Exception {

    public DuplicateExtensionException(String name) {
        super("Another extension with this name already exists: " + name);
    }

}
