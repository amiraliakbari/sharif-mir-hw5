package edu.sharif.ce.mir.console.util;

import edu.sharif.ce.mir.console.command.ConsoleCommandExecutionHandler;
import edu.sharif.ce.mir.console.extension.ExtensionWrapper;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/3/12, 15:20)
 */
public class NamespaceConsoleCommandFilter implements ConsoleCommandFilter {
    
    private final String namespace;
    private final boolean includeAll;

    public NamespaceConsoleCommandFilter(String namespace) {
        this(namespace, false);
    }
    
    public NamespaceConsoleCommandFilter(String namespace, boolean includeAll) {
        this.namespace = namespace;
        this.includeAll = includeAll;
    }

    @Override
    public boolean matches(ExtensionWrapper<?> extension, ConsoleCommandExecutionHandler command) {
        return extension.getNamespace().equals(namespace);
    }

    @Override
    public boolean includeAll() {
        return includeAll;
    }
}
