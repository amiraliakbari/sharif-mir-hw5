package edu.sharif.ce.mir.console.em.impl;

import edu.sharif.ce.mir.console.Console;
import edu.sharif.ce.mir.console.errors.DuplicateExtensionException;
import edu.sharif.ce.mir.console.errors.ExtensionAlreadyLoadedException;
import edu.sharif.ce.mir.console.errors.ExtensionInitializationException;
import edu.sharif.ce.mir.console.errors.NoSuchExtensionException;
import edu.sharif.ce.mir.console.extension.impl.ClassExtensionWrapper;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 1:26)
 */
public class ClassLoaderExtensionManager extends AbstractExtensionManager {

    public <T> void addExtension(Class<T> extensionClass, ClassLoader classLoader) throws InstantiationException, IllegalAccessException, NoSuchExtensionException, ExtensionInitializationException, ExtensionAlreadyLoadedException, DuplicateExtensionException {
        final ClassExtensionWrapper<T> wrapper = new ClassExtensionWrapper<T>(extensionClass, getConsole(), classLoader);
        addExtension(wrapper);
    }

}
