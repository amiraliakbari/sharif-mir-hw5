package edu.sharif.ce.mir.console.errors;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 22:05)
 */
public class ExtensionInitializationException extends Exception {

    public ExtensionInitializationException() {
        super();
    }

    public ExtensionInitializationException(String message) {
        super(message);
    }

    public ExtensionInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtensionInitializationException(Throwable cause) {
        super(cause);
    }
}
