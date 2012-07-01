package edu.sharif.ce.mir.console.util;

import edu.sharif.ce.mir.console.command.ConsoleCommandExecutionHandler;
import edu.sharif.ce.mir.console.extension.ExtensionWrapper;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/3/12, 15:18)
 */
public interface ConsoleCommandFilter {

    boolean matches(ExtensionWrapper<?> extension, ConsoleCommandExecutionHandler command);

    boolean includeAll();

    public static class ALL implements ConsoleCommandFilter {

        @Override
        public boolean matches(ExtensionWrapper<?> extension, ConsoleCommandExecutionHandler command) {
            return true;
        }

        @Override
        public boolean includeAll() {
            return true;
        }

    }

    public static class LOADED implements ConsoleCommandFilter {

        @Override
        public boolean matches(ExtensionWrapper<?> extension, ConsoleCommandExecutionHandler command) {
            return true;
        }

        @Override
        public boolean includeAll() {
            return false;
        }

    }
}
