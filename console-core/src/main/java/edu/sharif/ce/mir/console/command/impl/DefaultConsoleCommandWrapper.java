package edu.sharif.ce.mir.console.command.impl;

import edu.sharif.ce.mir.console.command.ConsoleCommandExecutionHandler;
import edu.sharif.ce.mir.console.command.ConsoleCommandWrapper;
import edu.sharif.ce.mir.console.command.matcher.ConsoleCommand;
import edu.sharif.ce.mir.console.command.matcher.impl.GroupMapImpl;
import edu.sharif.ce.mir.console.errors.ConsoleCommandExecutionException;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 23:06)
 */
public class DefaultConsoleCommandWrapper implements ConsoleCommandWrapper {

    private final ConsoleCommandExecutionHandler handler;

    public DefaultConsoleCommandWrapper(ConsoleCommand command, ConsoleCommandExecutionHandler handler) {
        handler.setGroups(new GroupMapImpl(command.getGroups()));
        handler.setParameters(command.getParameters());
        this.handler = handler;
    }
    
    @Override
    public void execute() throws ConsoleCommandExecutionException {
        this.handler.execute();
    }

}
