package edu.sharif.ce.mir.console.command.impl;

import edu.sharif.ce.mir.console.Console;
import edu.sharif.ce.mir.console.api.Command;
import edu.sharif.ce.mir.console.command.ConsoleCommandExecutionHandler;
import edu.sharif.ce.mir.console.command.matcher.GroupMap;
import edu.sharif.ce.mir.console.errors.ConsoleCommandExecutionException;

import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 23:38)
 */
public abstract class AnnotatedConsoleCommandExecutionHandler implements ConsoleCommandExecutionHandler {
    
    private Console console;
    private String description;
    private String help;
    private Map<String, Object> parameters;
    private GroupMap groups;
    private final String namespace;
    private final String name;

    protected AnnotatedConsoleCommandExecutionHandler(Console console, String namespace, Command command, String name) {
        this.console = console;
        this.namespace = namespace;
        this.name = name;
        this.description = command.description();
        this.help = command.help();
    }

    @Override
    public Console getConsole() {
        return console;
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    @Override
    public GroupMap getGroups() {
        return groups;
    }

    @Override
    public void setGroups(GroupMap groups) {
        this.groups = groups;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getHelp() {
        return help;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String getQualifiedName() {
        return getNamespace() + ":" + getName();
    }

}
