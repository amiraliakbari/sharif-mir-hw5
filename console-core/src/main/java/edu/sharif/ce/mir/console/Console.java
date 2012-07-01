package edu.sharif.ce.mir.console;

import edu.sharif.ce.mir.console.command.definition.CommandDefinitionParser;
import edu.sharif.ce.mir.console.command.matcher.CommandMatcher;
import edu.sharif.ce.mir.console.em.ExtensionManager;
import edu.sharif.ce.mir.console.io.ConsoleWriter;
import edu.sharif.ce.mir.console.io.Input;
import edu.sharif.ce.mir.console.io.Logger;
import edu.sharif.ce.mir.console.io.Output;

import java.io.InputStream;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (28/2/12, 15:35)
 */
public interface Console extends Logger {

    CommandMatcher getMatcher();

    CommandDefinitionParser getParser();
    
    void setPrompt(String prompt);

    void run();
    
    void execute(String command);

    void terminate();

    boolean isRunning();

    <E extends Input> void read(E input);

    void write(Output... data);

    void setExtensionManager(ExtensionManager extensionManager);

    ExtensionManager getExtensionManager();

    InputStream getInput();

    void setInput(InputStream input);

    ConsoleWriter getWriter();

    void setWriter(ConsoleWriter output);

}
