package edu.sharif.ce.mir.console.command.matcher;

import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (29/2/12, 15:59)
 */
public interface ConsoleCommand {
    
    Map<String, Object> getParameters();

    Map<String, String> getGroups();

}
