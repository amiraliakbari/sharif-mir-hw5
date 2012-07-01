package edu.sharif.ce.mir.console.util;

import edu.sharif.ce.mir.console.Console;
import edu.sharif.ce.mir.console.command.ConsoleCommandExecutionHandler;
import edu.sharif.ce.mir.console.command.ConsoleCommandWrapper;
import edu.sharif.ce.mir.console.em.ExtensionManager;
import edu.sharif.ce.mir.console.extension.ExtensionWrapper;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/3/12, 15:14)
 */
public abstract class ConsoleUtils {

    public static Set<ConsoleCommandExecutionHandler> getCommands(Console console, ConsoleCommandFilter filter) {
        final ExtensionManager manager = console.getExtensionManager();
        final Collection<ExtensionWrapper<?>> wrappers = manager.all();
        final HashSet<ConsoleCommandExecutionHandler> commandWrappers = new HashSet<ConsoleCommandExecutionHandler>();
        for (ExtensionWrapper<?> extension : wrappers) {
            if (!manager.isLoaded(extension.getName())) {
                continue;
            }
            for (ConsoleCommandExecutionHandler command : extension.getCommands()) {
                if (filter.matches(extension, command)) {
                    commandWrappers.add(command);
                }
            }
        }
        return commandWrappers;
    }
    
    public static Set<ConsoleCommandExecutionHandler> getCommands(Console console) {
        return getCommands(console, new ConsoleCommandFilter.ALL());
    }
    
    public String[] getNamespaces(Set<ConsoleCommandExecutionHandler> handlers) {
        Set<String> namespaces = new HashSet<String>();
        for (ConsoleCommandExecutionHandler handler : handlers) {
            namespaces.add(handler.getNamespace());
        }
        final String[] sorted = new String[namespaces.size()];
        Arrays.sort(sorted);
        return sorted;
    }

}
