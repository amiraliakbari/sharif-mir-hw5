package edu.sharif.ce.mir.console.impl;

import edu.sharif.ce.mir.console.Console;
import edu.sharif.ce.mir.console.command.definition.CommandDefinitionParser;
import edu.sharif.ce.mir.console.command.definition.impl.DefaultCommandDefinitionParser;
import edu.sharif.ce.mir.console.command.matcher.CommandMatcher;
import edu.sharif.ce.mir.console.command.matcher.impl.DefaultCommandMatcher;
import edu.sharif.ce.mir.console.em.ExtensionManager;
import edu.sharif.ce.mir.console.io.*;
import edu.sharif.ce.mir.console.io.impl.PrimitiveOutput;
import edu.sharif.ce.mir.utils.ArrayUtils;

import java.io.InputStream;
import java.util.Scanner;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 1:31)
 */
public class StandardConsole implements Console {

    private final CommandMatcher matcher = new DefaultCommandMatcher();
    private final CommandDefinitionParser parser = new DefaultCommandDefinitionParser();
    private InputStream input;
    private ConsoleWriter output;
    private String prompt = "console> ";
    private boolean terminated = true;
    private ExtensionManager extensionManager;
    private OutputLevel outputLevel = OutputLevel.ERROR;

    public StandardConsole() {
        this(System.in, new OutputStreamConsoleWriter(System.out));
    }

    public StandardConsole(InputStream input, ConsoleWriter output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public InputStream getInput() {
        return input;
    }

    @Override
    public void setInput(InputStream input) {
        this.input = input;
    }

    @Override
    public ConsoleWriter getWriter() {
        return output;
    }

    @Override
    public void setWriter(ConsoleWriter output) {
        this.output = output;
    }

    @Override
    public CommandMatcher getMatcher() {
        return matcher;
    }

    @Override
    public CommandDefinitionParser getParser() {
        return parser;
    }

    @Override
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    @Override
    public void run() {
        if (!this.terminated) {
            throw new IllegalStateException("Console is already running");
        }
        writeLine("Chimp Java Console v1.0");
        writeLine("(c) 2012 Mohammad Milad Naseri (m.m.naseri@gmail.com)");
        writeLine("Console started ...");
        if (this.extensionManager == null) {
            throw new IllegalStateException("Cannot start console without an extension manager");
        }
        this.terminated = false;
        while (!terminated) {
            write(prompt + " ");
            final String command = readLine();
            execute(command);
        }
    }

    @Override
    public void execute(String command) {
        if (command.isEmpty()) {
            return;
        }
        try {
            extensionManager.execute(command);
        } catch (Throwable e) {
            error(e);
        }
    }

    @Override
    public void terminate() {
        if (this.terminated) {
            throw new IllegalStateException("Console is not running");
        }
        this.terminated = true;
    }

    @Override
    public boolean isRunning() {
        return !terminated;
    }

    @Override
    public <E extends Input> void read(E input) {
        final String line = readLine();
        if (input.accepts(line)) {
            input.setValue(line);
        } else {
            error("Invalid input");
            read(input);
        }
    }

    @Override
    public void write(Output... data) {
        for (Output datum : data) {
            output.writeLine(datum);
        }
    }
    
    @Override
    public void setExtensionManager(ExtensionManager extensionManager) {
        this.extensionManager = extensionManager;
        extensionManager.setConsole(this);
    }

    @Override
    public ExtensionManager getExtensionManager() {
        return extensionManager;
    }

    public void write(String string) {
        output.write(new PrimitiveOutput(string));
    }

    public void writeLine(String string) {
        output.writeLine(new PrimitiveOutput(string));
    }
    
    public String readLine() {
        final Scanner scanner = new Scanner(input);
        String line = "";
        if (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.endsWith("\\")) {
                line = line.substring(0, line.length() - 1) + readLine();
            }
        }
        return line;
    }

    @Override
    public void setOutputLevel(OutputLevel outputLevel) {
        this.outputLevel = outputLevel;
    }

    @Override
    public void error(String message) {
        if (outputLevel.getLevel() < OutputLevel.ERROR.getLevel()) {
            return;
        }
        writeLine("[ERROR] " + message);
    }

    @Override
    public void error(Throwable throwable) {
        Throwable err = throwable;
        String msg = err.getClass().getSimpleName();
        do {
            if (err.getMessage() != null && !err.getMessage().isEmpty()) {
                msg = err.getMessage();
            }
            err = err.getCause();
        } while (err != null);
        error(msg);
        debug(ArrayUtils.toString(throwable.getStackTrace(), "\n"));
    }

    @Override
    public void info(String message) {
        if (outputLevel.getLevel() < OutputLevel.INFO.getLevel()) {
            return;
        }
        writeLine("[INFO] " + message);
    }

    @Override
    public void warn(String message) {
        if (outputLevel.getLevel() < OutputLevel.WARNING.getLevel()) {
            return;
        }
        writeLine("[WARNING] " + message);
    }

    @Override
    public void debug(String message) {
        if (outputLevel.getLevel() < OutputLevel.DEBUG.getLevel()) {
            return;
        }
        writeLine("[DEBUG] " + message);
    }
}
