package edu.sharif.ce.mir.console.em.impl;

import edu.sharif.ce.mir.console.Console;
import edu.sharif.ce.mir.console.em.ExtensionManager;
import edu.sharif.ce.mir.console.errors.*;
import edu.sharif.ce.mir.console.extension.ExtensionWrapper;
import edu.sharif.ce.mir.console.extension.ExtensionWrapperEventCallback;
import edu.sharif.ce.mir.console.extension.impl.ProxiedExtensionWrapper;
import edu.sharif.ce.mir.console.extension.tags.AutoLoadedExtension;
import edu.sharif.ce.mir.console.extension.tags.PerpetualExtension;

import java.util.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 0:19)
 */
public abstract class AbstractExtensionManager implements ExtensionManager {

    private final Map<String, ProxiedExtensionWrapper> extensions = new HashMap<String, ProxiedExtensionWrapper>();
    private final Set<String> loaded = new HashSet<String>();
    private Console console;

    @Override
    public boolean has(String name) {
        return extensions.containsKey(name);
    }

    @Override
    public ExtensionWrapper get(String name) throws NoSuchExtensionException {
        if (!has(name)) {
            throw new NoSuchExtensionException(name);
        }
        return extensions.get(name);
    }

    @Override
    public void load(String name) throws ExtensionInitializationException, NoSuchExtensionException, ExtensionAlreadyLoadedException {
        if (isLoaded(name)) {
            throw new ExtensionAlreadyLoadedException(name);
        }
        get(name).load();
        loaded.add(name);
    }

    @Override
    public void unload(String name) throws ExtensionUnloadingException, NoSuchExtensionException {
        get(name).unload();
        loaded.remove(name);
    }

    @Override
    public void execute(String command) throws InvalidCommandException, AmbiguousCommandException, ConsoleCommandExecutionException {
        command = command.trim();
        String target = "";
        if (command.matches("\\S+:.*")) {
            target = command.substring(0, command.indexOf(":"));
            command = command.substring(command.indexOf(":") + 1).trim();
            for (ProxiedExtensionWrapper wrapper : extensions.values()) {
                if (wrapper.getNamespace().equals(target)) {
                    wrapper.execute(command);
                    target = "";
                    break;
                }
            }
            if (!target.isEmpty()) {
                throw new IllegalStateException("No such namespace: " + target);
            }
            return;
        }
        final Set<ExtensionWrapper> namespaces = new HashSet<ExtensionWrapper>();
        for (String name : loaded) {
            ProxiedExtensionWrapper wrapper = extensions.get(name);
            if (wrapper.knows(command)) {
                namespaces.add(wrapper);
            }
        }
        if (namespaces.isEmpty()) {
            throw new InvalidCommandException("Invalid command: " + command);
        }
        if (namespaces.size() == 1) {
            final ExtensionWrapper wrapper = namespaces.iterator().next();
            wrapper.execute(command);
            return;
        }
        final String[] strings = new String[namespaces.size()];
        int i = 0;
        for (ExtensionWrapper namespace : namespaces) {
            strings[i ++] = namespace.getNamespace();
        }
        throw new AmbiguousCommandException(command, strings);
    }

    @Override
    public Set<ExtensionWrapper<?>> loaded() {
        final HashSet<ExtensionWrapper<?>> loaded = new HashSet<ExtensionWrapper<?>>();
        for (String name : this.loaded) {
            loaded.add(extensions.get(name));
        }
        return loaded;
    }

    @Override
    public Collection<ExtensionWrapper<?>> all() {
        final Set<ExtensionWrapper<?>> wrappers = new HashSet<ExtensionWrapper<?>>();
        for (ExtensionWrapper wrapper : extensions.values()) {
            wrappers.add(wrapper);
        }
        return wrappers;
    }
    
    @Override
    public boolean isLoaded(String name) {
        return loaded.contains(name);
    }

    protected void addExtension(ExtensionWrapper extension) throws NoSuchExtensionException, ExtensionInitializationException, ExtensionAlreadyLoadedException, DuplicateExtensionException {
        if (extension == null || extension.getExtension() == null) {
            throw new IllegalArgumentException("Extension cannot be null");
        }
        if (has(extension.getName())) {
            throw new DuplicateExtensionException(extension.getName());
        }
        @SuppressWarnings("unchecked")
        final ProxiedExtensionWrapper wrapper = new ProxiedExtensionWrapper(extension);
        if (extension.getExtension() instanceof PerpetualExtension) {
            wrapper.setOnBeforeUnload(new ExtensionWrapperEventCallback() {
                
                @Override
                public <E> void execute(ExtensionWrapper<E> wrapper) {
                    throw new IllegalStateException("Extension cannot be unloaded: " + wrapper.getName());
                }
                
            });
        }
        console.debug("Extension found: " + extension.getName());
        extensions.put(extension.getName(), wrapper);
        if (extension.getExtension() instanceof AutoLoadedExtension) {
            console.debug("Extension auto-loaded: " + extension.getName());
            load(extension.getName());
        }
    }

    public Console getConsole() {
        return console;
    }

    public void setConsole(Console console) {
        this.console = console;
    }
    
}
