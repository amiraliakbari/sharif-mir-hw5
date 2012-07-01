package edu.sharif.ce.mir.console.extension.impl;

import edu.sharif.ce.mir.console.Console;
import edu.sharif.ce.mir.console.api.Command;
import edu.sharif.ce.mir.console.api.Extension;
import edu.sharif.ce.mir.console.api.OnLoad;
import edu.sharif.ce.mir.console.api.OnUnload;
import edu.sharif.ce.mir.console.command.ConsoleCommandExecutionHandler;
import edu.sharif.ce.mir.console.command.ConsoleCommandWrapper;
import edu.sharif.ce.mir.console.command.definition.CommandDefinitionParser;
import edu.sharif.ce.mir.console.command.definition.parse.CommandParseTree;
import edu.sharif.ce.mir.console.command.impl.AnnotatedConsoleCommandExecutionHandler;
import edu.sharif.ce.mir.console.command.impl.DefaultConsoleCommandWrapper;
import edu.sharif.ce.mir.console.command.matcher.CommandMatcher;
import edu.sharif.ce.mir.console.command.matcher.ConsoleCommand;
import edu.sharif.ce.mir.console.command.matcher.GroupMap;
import edu.sharif.ce.mir.console.em.ExtensionManager;
import edu.sharif.ce.mir.console.errors.*;
import edu.sharif.ce.mir.console.extension.ExtensionWrapper;
import edu.sharif.ce.mir.utils.entities.Pair;
import edu.sharif.ce.mir.utils.reflection.AnnotationMethodFilter;
import edu.sharif.ce.mir.utils.reflection.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 22:26)
 */
public class ClassExtensionWrapper<E> implements ExtensionWrapper<E> {

    private final Class<E> extensionClass;
    private final Console console;
    private final ClassLoader classLoader;
    private E extension;
    private Method loader;
    private Method unloader;
    private Map<String, ConsoleCommandExecutionHandler> commands = new HashMap<String, ConsoleCommandExecutionHandler>();
    private Map<String, CommandParseTree> parsed = new HashMap<String, CommandParseTree>();
    private String name;
    private String namespace;

    public ClassExtensionWrapper(Class<E> extensionClass, Console console) {
        this.extensionClass = extensionClass;
        this.console = console;
        this.classLoader = getClass().getClassLoader();
        initializeExtension();
    }

    public ClassExtensionWrapper(Class<E> extensionClass, Console console, ClassLoader classLoader) throws IllegalAccessException, InstantiationException {
        this.extensionClass = extensionClass;
        this.console = console;
        this.classLoader = classLoader;
        this.extension = extensionClass.newInstance();
        initializeExtension();
    }

    private void initializeExtension() throws ExtensionInitializationError {
        if (!extensionClass.isAnnotationPresent(Extension.class)) {
            throw new ExtensionInitializationError("Specified class is not properly annotated");
        }
        final Extension extensionAnnotation = extensionClass.getAnnotation(Extension.class);
        name = extensionClass.getSimpleName();
//        if (!extensionAnnotation.name().isEmpty()) {
//            name = extensionAnnotation.name();
//        }
        namespace = extensionClass.getSimpleName();
        if (!extensionAnnotation.namespace().isEmpty()) {
            namespace = extensionAnnotation.namespace();
        }
        final Map<String, Method> loaders = ReflectionUtils.getMethods(extensionClass, new AnnotationMethodFilter(OnLoad.class));
        final Map<String, Method> unloaders = ReflectionUtils.getMethods(extensionClass, new AnnotationMethodFilter(OnUnload.class));
        final Map<String, Method> methods = ReflectionUtils.getMethods(extensionClass, new AnnotationMethodFilter(Command.class));
        if (loaders.size() > 1) {
            throw new ExtensionInitializationError("Extension can have at most one loader");
        }
        if (unloaders.size() > 1) {
            throw new ExtensionInitializationError("Extension can have at most one unloader");
        }
        if (loaders.size() == 1) {
            loader = loaders.get(loaders.keySet().iterator().next());
        }
        if (unloaders.size() == 1) {
            unloader = unloaders.get(unloaders.keySet().iterator().next());
        }
        for (final Method method : methods.values()) {
            method.setAccessible(true);
            final Command annotation = method.getAnnotation(Command.class);
            final AnnotatedConsoleCommandExecutionHandler handler = new AnnotatedConsoleCommandExecutionHandler(console, namespace, annotation, method.getName()) {

                @Override
                public void execute() throws ConsoleCommandExecutionException {
                    final Class<?>[] types = method.getParameterTypes();
                    final Object[] parameters = new Object[types.length];
                    for (int i = 0, typesLength = types.length; i < typesLength; i++) {
                        if (Map.class.isAssignableFrom(types[i]) && !GroupMap.class.isAssignableFrom(types[i])) {
                            parameters[i] = getParameters();
                        } else if (GroupMap.class.isAssignableFrom(types[i])) {
                            parameters[i] = getGroups();
                        } else if (Console.class.isAssignableFrom(types[i])) {
                            parameters[i] = getConsole();
                        } else if (ExtensionManager.class.isAssignableFrom(types[i])) {
                            parameters[i] = getConsole().getExtensionManager();
                        } else if (CommandDefinitionParser.class.isAssignableFrom(types[i])) {
                            parameters[i] = getConsole().getParser();
                        } else if (CommandMatcher.class.isAssignableFrom(types[i])) {
                            parameters[i] = getConsole().getMatcher();
                        }
                    }
                    try {
                        method.invoke(extension, parameters);
                    } catch (IllegalAccessException ignored) {
                    } catch (InvocationTargetException e) {
                        throw new ConsoleCommandExecutionException(e.getCause());
                    }
                }

            };
            commands.put(annotation.definition(), handler);
            parsed.put(annotation.definition(), console.getParser().parse(annotation.definition()));
        }
    }

    @Override
    public void load() throws ExtensionInitializationException {
        if (loader != null) {
            try {
                if (loader.getParameterTypes().length == 0) {
                    loader.invoke(extension);
                } else if (loader.getParameterTypes().length == 1 && loader.getParameterTypes()[0].isAssignableFrom(Console.class)) {
                    loader.invoke(extension, console);
                } else {
                    throw new ExtensionInitializationException("Invalid unloader declared for extension " + getName());
                }
            } catch (IllegalAccessException ignored) {
            } catch (InvocationTargetException e) {
                throw new ExtensionInitializationException(e.getCause());
            }
        }
    }

    @Override
    public void unload() throws ExtensionUnloadingException {
        if (unloader != null) {
            try {
                if (unloader.getParameterTypes().length == 0) {
                    unloader.invoke(extension);
                } else if (unloader.getParameterTypes().length == 1 && unloader.getParameterTypes()[0].isAssignableFrom(Console.class)) {
                    unloader.invoke(extension, console);
                } else {
                    throw new ExtensionUnloadingException("Invalid unloader declared for extension " + getName());
                }
            } catch (IllegalAccessException ignored) {
            } catch (InvocationTargetException e) {
                throw new ExtensionUnloadingException(e.getCause());
            }
        }
    }

    @Override
    public E getExtension() {
        return extension;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public Collection<ConsoleCommandExecutionHandler> getCommands() {
        return commands.values();
    }

    @Override
    public boolean knows(String command) throws AmbiguousCommandException {
        final CommandMatcher matcher = console.getMatcher();
        boolean knows = false;
        for (final CommandParseTree parseTree : parsed.values()) {
            final ConsoleCommand match = matcher.match(parseTree, command);
            if (match != null) {
                if (knows) {
                    throw new AmbiguousCommandException(command, getNamespace());
                }
                knows = true;
            }
        }
        return knows;
    }

    private Pair<ConsoleCommand, ConsoleCommandExecutionHandler> getHandler(String command) throws AmbiguousCommandException {
        final CommandMatcher matcher = console.getMatcher();
        Pair<ConsoleCommand, ConsoleCommandExecutionHandler> pair = null;
        for (String definition : parsed.keySet()) {
            final CommandParseTree parseTree = parsed.get(definition);
            final ConsoleCommand match = matcher.match(parseTree, command);
            if (match != null) {
                if (pair != null) {
                    throw new AmbiguousCommandException(command, getNamespace());
                }
                pair = new Pair<ConsoleCommand, ConsoleCommandExecutionHandler>(match, commands.get(definition));
            }
        }
        return pair;
    }

    @Override
    public void execute(String command) throws InvalidCommandException, AmbiguousCommandException, ConsoleCommandExecutionException {
        final Pair<ConsoleCommand, ConsoleCommandExecutionHandler> handler = getHandler(command);
        if (handler == null) {
            throw new InvalidCommandException("Command is not known in this namespace");
        }
        final ConsoleCommandWrapper wrapper = new DefaultConsoleCommandWrapper(handler.getFirst(), handler.getSecond());
        wrapper.execute();
    }

    @Override
    public int compareTo(ExtensionWrapper o) {
        return getName().compareTo(o.getName());
    }
}
