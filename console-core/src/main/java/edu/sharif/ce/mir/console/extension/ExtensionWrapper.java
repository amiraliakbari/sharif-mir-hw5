package edu.sharif.ce.mir.console.extension;

import edu.sharif.ce.mir.console.command.ConsoleCommandExecutionHandler;
import edu.sharif.ce.mir.console.errors.*;

import java.util.Collection;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 22:04)
 */
public interface ExtensionWrapper<E> extends Comparable<ExtensionWrapper> {

    void load() throws ExtensionInitializationException;
    
    void unload() throws ExtensionUnloadingException;
    
    E getExtension();
    
    String getName();
    
    String getNamespace();

    Collection<ConsoleCommandExecutionHandler> getCommands();

    boolean knows(String command) throws AmbiguousCommandException;
    
    void execute(String command) throws InvalidCommandException, AmbiguousCommandException, ConsoleCommandExecutionException;

}
