package edu.sharif.ce.mir.console.util;

import edu.sharif.ce.mir.console.command.ConsoleCommandExecutionHandler;
import edu.sharif.ce.mir.console.extension.ExtensionWrapper;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/3/12, 15:21)
 */
public class QualifiedNameConsoleCommandFilter implements ConsoleCommandFilter {

    private String namespace;
    private String command;
    private final boolean includeAll;

    public QualifiedNameConsoleCommandFilter(String qualifiedName) {
        this(qualifiedName, false);
    }
    
    public QualifiedNameConsoleCommandFilter(String namespace, String command) {
        this(namespace, command, false);
    }
    
    public QualifiedNameConsoleCommandFilter(String namespace, String command, boolean includeAll) {
        this.namespace = namespace;
        this.command = command;
        this.includeAll = includeAll;
    }
    
    public QualifiedNameConsoleCommandFilter(String qualifiedName, boolean includeAll) {
        this.includeAll = includeAll;
        namespace = "*";
        if (qualifiedName.contains(":")) {
            namespace = qualifiedName.substring(0, qualifiedName.indexOf(":"));
            qualifiedName = qualifiedName.substring(qualifiedName.indexOf(":") + 1);
        }
        command = qualifiedName;
    }

    @Override
    public boolean matches(ExtensionWrapper<?> extension, ConsoleCommandExecutionHandler command) {
        return (namespace.equals("*") || extension.getNamespace().equals(namespace)) &&
                (this.command.equals("*") || command.getName().equals(this.command));
    }

    @Override
    public boolean includeAll() {
        return includeAll;
    }
}
