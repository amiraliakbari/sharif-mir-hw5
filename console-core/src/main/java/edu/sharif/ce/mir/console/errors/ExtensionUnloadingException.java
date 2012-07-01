package edu.sharif.ce.mir.console.errors;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 22:05)
 */
public class ExtensionUnloadingException extends Exception {

    public ExtensionUnloadingException() {
    }

    public ExtensionUnloadingException(String message) {
        super(message);
    }

    public ExtensionUnloadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtensionUnloadingException(Throwable cause) {
        super(cause);
    }
}
