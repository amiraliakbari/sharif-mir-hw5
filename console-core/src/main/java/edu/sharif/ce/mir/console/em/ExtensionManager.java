package edu.sharif.ce.mir.console.em;

import edu.sharif.ce.mir.console.Console;
import edu.sharif.ce.mir.console.errors.*;
import edu.sharif.ce.mir.console.extension.ExtensionWrapper;
import edu.sharif.ce.mir.console.extension.impl.ProxiedExtensionWrapper;
import edu.sharif.ce.mir.console.impl.StandardConsole;

import java.util.Collection;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 0:06)
 */
public interface ExtensionManager {

    boolean has(String name);

    ExtensionWrapper get(String name) throws NoSuchExtensionException;

    void load(String name) throws ExtensionInitializationException, NoSuchExtensionException, ExtensionAlreadyLoadedException;

    void unload(String name) throws ExtensionUnloadingException, NoSuchExtensionException;

    void execute(String command) throws InvalidCommandException, AmbiguousCommandException, ConsoleCommandExecutionException;

    Set<ExtensionWrapper<?>> loaded();

    Collection<ExtensionWrapper<?>> all();

    boolean isLoaded(String name);

    void setConsole(Console console);
}
