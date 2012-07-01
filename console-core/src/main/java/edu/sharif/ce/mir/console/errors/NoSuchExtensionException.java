package edu.sharif.ce.mir.console.errors;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 0:24)
 */
public class NoSuchExtensionException extends Exception {

    public NoSuchExtensionException(String name) {
        super("No such extension found in the context: " + name);
    }

}
