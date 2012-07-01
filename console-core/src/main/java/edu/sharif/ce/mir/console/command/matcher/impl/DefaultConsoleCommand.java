package edu.sharif.ce.mir.console.command.matcher.impl;

import edu.sharif.ce.mir.console.command.matcher.ConsoleCommand;

import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (29/2/12, 17:08)
 */
public class DefaultConsoleCommand implements ConsoleCommand {

    private final Map<String,String> groups;
    private final Map<String,Object> parameters;

    public DefaultConsoleCommand(Map<String, Object> parameters, Map<String, String> groups) {
        this.parameters = parameters;
        this.groups = groups;
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public Map<String, String> getGroups() {
        return groups;
    }
    
    @Override
    public String toString() {
        return "{parameters:" + parameters + ";groups:" + groups + "}";
    }

}
