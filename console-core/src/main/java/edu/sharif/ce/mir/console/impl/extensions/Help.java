package edu.sharif.ce.mir.console.impl.extensions;

import edu.sharif.ce.mir.console.Console;
import edu.sharif.ce.mir.console.api.Command;
import edu.sharif.ce.mir.console.api.Extension;
import edu.sharif.ce.mir.console.command.ConsoleCommandExecutionHandler;
import edu.sharif.ce.mir.console.command.matcher.GroupMap;
import edu.sharif.ce.mir.console.extension.tags.AutoLoadedExtension;
import edu.sharif.ce.mir.console.io.impl.PrimitiveOutput;
import edu.sharif.ce.mir.console.io.impl.TableOutput;
import edu.sharif.ce.mir.console.util.ConsoleCommandFilter;
import edu.sharif.ce.mir.console.util.ConsoleUtils;
import edu.sharif.ce.mir.console.util.QualifiedNameConsoleCommandFilter;
import edu.sharif.ce.mir.utils.ArrayUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 4:12)
 */
@Extension
public class Help implements AutoLoadedExtension {

    @Command(
            definition = "help [all}for all]"
    )
    public void help(Console console, GroupMap groups) {
        final Set<ConsoleCommandExecutionHandler> commands = ConsoleUtils.getCommands(console, groups.containsKey("all") ? new ConsoleCommandFilter.ALL() : new ConsoleCommandFilter.LOADED());
        ConsoleCommandExecutionHandler[] handlers = new ConsoleCommandExecutionHandler[commands.size()];
        handlers = commands.toArray(handlers);
        Arrays.sort(handlers, new Comparator<ConsoleCommandExecutionHandler>() {
            @Override
            public int compare(ConsoleCommandExecutionHandler o1, ConsoleCommandExecutionHandler o2) {
                return o1.getQualifiedName().compareTo(o2.getQualifiedName());
            }
        });
        final TableOutput tableOutput = new TableOutput();
        tableOutput.setCellPadding(2);
        tableOutput.addRow(new PrimitiveOutput("Command"), new PrimitiveOutput("Description"));
        tableOutput.addLine();
        for (ConsoleCommandExecutionHandler handler : handlers) {
            tableOutput.addRow(new PrimitiveOutput(handler.getQualifiedName()), new PrimitiveOutput(handler.getDescription()));
        }
        console.write(tableOutput);
    }
    
    @Command(
            definition = "help #string(command)"
    )
    public void manual(Console console, Map<String, Object> arguments) throws Exception {
        String command = (String) arguments.get("command");
        final Set<ConsoleCommandExecutionHandler> wrappers = ConsoleUtils.getCommands(console, new QualifiedNameConsoleCommandFilter(command));
        if (wrappers.isEmpty()) {
            throw new Exception(String.format("Command not found: %s", command));
        } else if (wrappers.size() > 1) {
            throw new Exception("Ambiguous command " + command + ". Add namespace prefix:\n" + ArrayUtils.toString(wrappers.toArray(), "\n"));
        }
        final String help = wrappers.iterator().next().getHelp();
        if (help.isEmpty()) {
            console.write(new PrimitiveOutput("No help is available for this command"));
        } else {
            console.write(new PrimitiveOutput(help));
        }
    }

}
