package edu.sharif.ce.mir.console.command;

import edu.sharif.ce.mir.console.Console;
import edu.sharif.ce.mir.console.command.matcher.GroupMap;
import edu.sharif.ce.mir.console.errors.ConsoleCommandExecutionException;

import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 23:34)
 */
public interface ConsoleCommandExecutionHandler {

    Console getConsole();
    
    Map<String, Object> getParameters();

    void setParameters(Map<String, Object> parameters);

    GroupMap getGroups();

    void setGroups(GroupMap groups);

    String getDescription();

    String getHelp();
    
    String getName();
    
    String getNamespace();

    String getQualifiedName();

    void execute() throws ConsoleCommandExecutionException;

}
