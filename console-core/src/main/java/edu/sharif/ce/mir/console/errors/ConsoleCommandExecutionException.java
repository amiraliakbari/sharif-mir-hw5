package edu.sharif.ce.mir.console.errors;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 23:09)
 */
public class ConsoleCommandExecutionException extends Exception {

    public ConsoleCommandExecutionException() {
    }

    public ConsoleCommandExecutionException(String message) {
        super(message);
    }

    public ConsoleCommandExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsoleCommandExecutionException(Throwable cause) {
        super(cause);
    }
}
