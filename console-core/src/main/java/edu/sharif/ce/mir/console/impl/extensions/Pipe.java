package edu.sharif.ce.mir.console.impl.extensions;

import edu.sharif.ce.mir.console.Console;
import edu.sharif.ce.mir.console.api.Command;
import edu.sharif.ce.mir.console.api.Extension;
import edu.sharif.ce.mir.console.command.matcher.GroupMap;
import edu.sharif.ce.mir.console.extension.tags.AutoLoadedExtension;
import edu.sharif.ce.mir.console.io.CacheConsoleWriter;
import edu.sharif.ce.mir.console.io.ConsoleWriter;
import edu.sharif.ce.mir.console.io.Output;
import edu.sharif.ce.mir.utils.reflection.BeanAccessor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/3/12, 15:13)
 */
@Extension
public class Pipe implements AutoLoadedExtension {

    @Command(
            definition = "#string(target) | [typed} #string(type) >] #string(command)",
            description = "Pipes the output of one command to the next",
            help = "Pipes the output of one command, using it to template the other\n" +
                    "The right-side command's output will be used to fill in the argument indicators\n" +
                    "of form ${...}. An optional `type` parameter can be used to filter out objects of\n" +
                    "undesired type."
    )
    public void pipe(Console console, Map<String, Object> arguments, GroupMap groups) throws Exception {
        Class<?> type = null;
        if (groups.containsKey("typed")) {
            try {
                type = Class.forName((String) arguments.get("type"));
            } catch (ClassNotFoundException e) {
                throw new Exception("Invalid type: " + arguments.get("type"));
            }
        }
        final ConsoleWriter writer = console.getWriter();
        final CacheConsoleWriter consoleWriter = new CacheConsoleWriter();
        console.setWriter(consoleWriter);
        console.execute((String) arguments.get("command"));
        console.setWriter(writer);
        for (Output output : consoleWriter.getValue()) {
            if (type != null) {
                if (!type.isInstance(output)) {
                    continue;
                }
            }
            String target = (String) arguments.get("target");
            if (target.matches(".*?\\$\\{[^\\}]+?\\}.*?")) {
                final BeanAccessor accessor = new BeanAccessor(output);
                final Matcher matcher = Pattern.compile("\\$\\{([^\\}]+?)\\}").matcher(target);
                final Map<String, Object> values = new HashMap<String, Object>();
                while (matcher.find()) {
                    values.put(matcher.group(0), accessor.getProperty(matcher.group(1)));
                }
                for (String s : values.keySet()) {
                    while (target.contains(s)) {
                        target = target.replace(s, values.get(s) == null ? "null" : values.get(s).toString());
                    }
                }
            }
            console.execute(target);
        }
    }

}
