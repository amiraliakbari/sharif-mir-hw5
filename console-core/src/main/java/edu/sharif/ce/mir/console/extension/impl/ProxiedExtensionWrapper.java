package edu.sharif.ce.mir.console.extension.impl;

import edu.sharif.ce.mir.console.command.ConsoleCommandExecutionHandler;
import edu.sharif.ce.mir.console.errors.*;
import edu.sharif.ce.mir.console.extension.ExtensionWrapper;
import edu.sharif.ce.mir.console.extension.ExtensionWrapperEventCallback;

import java.util.Collection;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 0:11)
 */
public class ProxiedExtensionWrapper implements ExtensionWrapper<Object> {

    private ExtensionWrapper wrapper;
    private ExtensionWrapperEventCallback onBeforeLoad = new ExtensionWrapperEventCallback.NONE();
    private ExtensionWrapperEventCallback onAfterLoad = new ExtensionWrapperEventCallback.NONE();
    private ExtensionWrapperEventCallback onBeforeUnload = new ExtensionWrapperEventCallback.NONE();
    private ExtensionWrapperEventCallback onAfterUnload = new ExtensionWrapperEventCallback.NONE();
    private ExtensionWrapperEventCallback onBeforeExecute = new ExtensionWrapperEventCallback.NONE();
    private ExtensionWrapperEventCallback onAfterExecute = new ExtensionWrapperEventCallback.NONE();

    public ProxiedExtensionWrapper(ExtensionWrapper wrapper) {
        this.wrapper = wrapper;
    }
    
    private void check(ExtensionWrapperEventCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback cannot be set to null");
        }
    }

    public void setOnBeforeLoad(ExtensionWrapperEventCallback onBeforeLoad) {
        check(onBeforeLoad);
        this.onBeforeLoad = onBeforeLoad;
    }

    public void setOnAfterLoad(ExtensionWrapperEventCallback onAfterLoad) {
        check(onAfterLoad);
        this.onAfterLoad = onAfterLoad;
    }

    public void setOnBeforeUnload(ExtensionWrapperEventCallback onBeforeUnload) {
        check(onBeforeUnload);
        this.onBeforeUnload = onBeforeUnload;
    }

    public void setOnAfterUnload(ExtensionWrapperEventCallback onAfterUnload) {
        check(onAfterUnload);
        this.onAfterUnload = onAfterUnload;
    }

    public void setOnBeforeExecute(ExtensionWrapperEventCallback onBeforeExecute) {
        check(onBeforeExecute);
        this.onBeforeExecute = onBeforeExecute;
    }

    public void setOnAfterExecute(ExtensionWrapperEventCallback onAfterExecute) {
        check(onAfterExecute);
        this.onAfterExecute = onAfterExecute;
    }

    @Override
    public void load() throws ExtensionInitializationException {
        onBeforeLoad.execute(wrapper);
        wrapper.load();
        onAfterLoad.execute(wrapper);
    }

    @Override
    public void unload() throws ExtensionUnloadingException {
        onBeforeUnload.execute(wrapper);
        wrapper.unload();
        onAfterUnload.execute(wrapper);
    }

    @Override
    public Object getExtension() {
        return wrapper.getExtension();
    }

    @Override
    public String getName() {
        return wrapper.getName();
    }

    @Override
    public String getNamespace() {
        return wrapper.getNamespace();
    }

    @Override
    public Collection<ConsoleCommandExecutionHandler> getCommands() {
        return wrapper.getCommands();
    }

    @Override
    public boolean knows(String command) throws AmbiguousCommandException {
        return wrapper.knows(command);
    }

    @Override
    public void execute(String command) throws InvalidCommandException, AmbiguousCommandException, ConsoleCommandExecutionException {
        onBeforeExecute.execute(wrapper);
        wrapper.execute(command);
        onAfterExecute.execute(wrapper);
    }

    @Override
    public int compareTo(ExtensionWrapper o) {
        return getName().compareTo(o.getName());
    }
}
