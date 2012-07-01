package edu.sharif.ce.mir.console.command;

import edu.sharif.ce.mir.console.errors.ConsoleCommandExecutionException;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 22:07)
 */
public interface ConsoleCommandWrapper {
    void execute() throws ConsoleCommandExecutionException;
}
