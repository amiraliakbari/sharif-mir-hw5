package edu.sharif.ce.mir.console.errors;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 22:34)
 */
public class ExtensionInitializationError extends Error {

    public ExtensionInitializationError() {
    }

    public ExtensionInitializationError(String message) {
        super(message);
    }

    public ExtensionInitializationError(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtensionInitializationError(Throwable cause) {
        super(cause);
    }

}
