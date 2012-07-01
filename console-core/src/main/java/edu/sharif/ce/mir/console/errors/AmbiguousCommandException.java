package edu.sharif.ce.mir.console.errors;

import edu.sharif.ce.mir.utils.ArrayUtils;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 23:18)
 */
public class AmbiguousCommandException extends Exception {

    public AmbiguousCommandException(String command, String[] namespaces) {
        super("Ambiguous command. Add one of the following namespace prefices to resolve the issue:\n" + ArrayUtils.toString(namespaces, "\n"));
    }

    public AmbiguousCommandException(String command, String namespace) {
        super("Command is ambiguous in namespace " + namespace);
    }
}
