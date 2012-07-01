package edu.sharif.ce.mir.console.impl.extensions;

import edu.sharif.ce.mir.console.Console;
import edu.sharif.ce.mir.console.api.Command;
import edu.sharif.ce.mir.console.api.Extension;
import edu.sharif.ce.mir.console.command.matcher.GroupMap;
import edu.sharif.ce.mir.console.em.ExtensionManager;
import edu.sharif.ce.mir.console.extension.ExtensionWrapper;
import edu.sharif.ce.mir.console.extension.tags.AutoLoadedExtension;
import edu.sharif.ce.mir.console.extension.tags.PerpetualExtension;
import edu.sharif.ce.mir.console.io.Output;
import edu.sharif.ce.mir.utils.entities.list.StringList;

import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 2:30)
 */
@Extension(
        namespace = "ext"
)
public class Extensions implements PerpetualExtension, AutoLoadedExtension {
    
    public static class ExtensionListing implements Output {

        private final ExtensionManager extensionManager;
        private final boolean filter;

        public ExtensionListing(ExtensionManager extensionManager, boolean filter) {
            this.extensionManager = extensionManager;
            this.filter = filter;
        }

        @Override
        public String getValue() {
            final StringBuilder builder = new StringBuilder();
            builder.append(extensionManager.all().size()).append(" extension(s) available.").append("\n");
            for (ExtensionWrapper wrapper : extensionManager.all()) {
                if (extensionManager.isLoaded(wrapper.getName())) {
                    builder.append("[L]");
                } else {
                    if (filter) {
                        continue;
                    }
                    builder.append("   ");
                }
                builder.append(" ").append(wrapper.getName());
                if (!wrapper.getName().equals(wrapper.getNamespace())) {
                    builder.append(" (").append(wrapper.getNamespace()).append(")");
                }
                builder.append("\n");
            }
            return builder.toString();
        }

    }

    @Command(
            definition = "list [filter} loaded]"
    )
    public void list(Console console, ExtensionManager manager, GroupMap groups) {
        console.write(new ExtensionListing(manager, groups.containsKey("filter")));
    }
    
    @Command(
            definition = "unload #list:string(extensions)"
    )
    public void unload(Console console, ExtensionManager manager, Map<String, Object> arguments) {
        final StringList extensions = (StringList) arguments.get("extensions");
        for (String extension : extensions) {
            unloadExtension(console, manager, extension);
        }
    }

    private void unloadExtension(Console console, ExtensionManager manager, String extension) {
        try {
            manager.unload(extension.trim());
        } catch (Throwable e) {
            console.error(e);
        }
    }

    @Command(
            definition = "load #list:string(extensions)"
    )
    public void load(Console console, ExtensionManager manager, Map<String, Object> arguments) {
        final StringList extensions = (StringList) arguments.get("extensions");
        for (String extension : extensions) {
            loadExtension(console, manager, extension);
        }
    }

    private void loadExtension(Console console, ExtensionManager manager, String extension) {
        try {
            manager.load(extension.trim());
        } catch (Throwable e) {
            console.error(e);
        }
    }

}
